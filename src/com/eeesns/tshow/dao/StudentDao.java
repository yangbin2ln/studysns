package com.eeesns.tshow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eeesns.tshow.entity.Attention;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.entity.StudentAct;
import com.eeesns.tshow.entity.StudentLabel;
import com.eeesns.tshow.entity.StudentStudent;
import com.eeesns.tshow.entity.ViewMessage;
import com.eeesns.tshow.util.DateUtil;
import com.eeesns.tshow.util.UUUID;

@Component
public class StudentDao {
	@Resource
	BaseDao baseDao;

	public Object saveAttention(String toStudentId, Student student) {
		String studentId = student.getStudentId();
		String sql = "select * from attention a where a.student_id =? and a.to_student_id =?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		sqlQuery.setString(1, toStudentId);
		List list = sqlQuery.list();
		if (list.size() > 0) {
			return "attentioned";
		}
		Attention attention = new Attention();
		attention.setStudentId(studentId);
		attention.setToStudentId(toStudentId);
		session.save(attention);
		// 更新当前用户的关注数和被关注用户的粉丝数
		String sql2 = "update student set attention_count = attention_count+1 where student_id = '"
				+ student.getStudentId() + "'";
		session.createSQLQuery(sql2).executeUpdate();
		String sql3 = "update student set follower_count = follower_count+1 where student_id = '"
				+ toStudentId + "'";
		session.createSQLQuery(sql3).executeUpdate();
		return "success";
	}

	public Object deleteAttention(String toStudentId, Student student) {
		String studentId = student.getStudentId();
		String sql = "delete from attention where student_id =? and to_student_id =?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		sqlQuery.setString(1, toStudentId);
		int count = sqlQuery.executeUpdate();
		if (count == 0) {
			return "never attentioned";
		} else {
			String sql2 = "update student set attention_count = attention_count-1 where student_id = '"
					+ student.getStudentId() + "'";
			session.createSQLQuery(sql2).executeUpdate();
			String sql3 = "update student set follower_count = follower_count-1 where student_id = '"
					+ toStudentId + "'";
			session.createSQLQuery(sql3).executeUpdate();
			return "success";
		}
	}

	/* 消息 */
	/**
	 * 更新此作品的最后一次交互时间
	 * 
	 * @param session
	 * @param productId
	 */
	public void updateProductLastActTime(Session session, String productId) {
		Product product = (Product) session.load(Product.class, productId);
		product.setLastActTime(DateUtil.formatDate());
		session.update(product);
	}

	/**
	 * 查询用户自己所有作品（包括历史版本）
	 * 
	 * @param page
	 * @param studentId
	 * @return
	 */
	public Page findMessageProductList(Page page, String studentId) {
		String sql = "select * from (select DATE_FORMAT( p.create_time, '%Y-%m-%d %H:%i:%S') createTime, p.score score, p.reply_count replyCount,p.praise_count praiseCount,p.collection_count collectionCount,p.vote_count voteCount, p.product_id productId,p.version version ,p.product_name productName,p.content content,p.small_content smallContent,p.width width,p.height height,"
				+ "s.student_id studentId,s.realname realName,s.head_ico headico,l.label_id labelId,l.label_name labelName,p.last_act_time lastActTime from (select * from product_version union select * from product) p "
				+ "join label l on(p.label_id =l.label_id) join student s on(p.student_id = s.student_id) where s.student_id ='"
				+ studentId + "' ) a order by a.lastActTime desc";
		Page pageList = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return pageList;
	}

	public List findMessageProductInfo(String productId, String type, String studentId) {
		List list = null;
		String sql = null;
		if (type.equals("praise")) {// 点赞查询
			sql = "select s.student_id studentId,s.head_ico headIco,s.invitation_id invitationId,s.realname realName,DATE_FORMAT( pr.create_time, '%Y-%m-%d %H:%i:%S') createTime "
					+ "from (select * from product union select * from product_version) p join praise pr on(pr.product_id = p.product_id) join student s on(s.student_id = pr.student_id) where p.product_id ='"
					+ productId + "' order by pr.create_time desc";
			list = baseDao.findListBySql(sql, new String[] {});
		} else if (type.equals("reply")) {// 回复查询
			sql = "SELECT "
					+ " tr.title_reply_id titleReplyId,(select count(1) from notation_reply where title_reply_id = titleReplyId) nrCount, "
					+ " tr.start start, " + " tr.end end, " + " tr.title_name titleName, "
					+ " nr.notation_content replyContent, " + " nr.notation_reply_id replyId, "
					+ " nr.isadopt isadopt, " + " nr.praise_count praiseCount, "
					+ " DATE_FORMAT(nr.create_time, '%Y-%m-%d %H:%i:%S') createTime, "
					+ " s.student_id studentId, "
					+ " s.realname realName ,s.invitation_id invitationId,s.head_ico headIco"
					+ " FROM title_reply tr" + " JOIN notation_reply nr ON ( "
					+ "	tr.title_reply_id = nr.title_reply_id " + ") " + " JOIN student s ON ( "
					+ "	s.student_id = nr.reply_student_id " + ") " + " WHERE "
					+ "	tr.product_id = '" + productId + "' " + "  ORDER BY "
					+ "	nr.create_time DESC";
			list = baseDao.findListBySql(sql, new String[] {});
			for (int i = 0; i < list.size(); i++) {
				Map mm = (Map) list.get(i);
				String replyId = (String) mm.get("replyId");
				String nrStudentId = (String) mm.get("studentId");
				String sql2 = "select nrr.notation_reply_reply_id nrrReplyReplyId, s.student_id  nrrStudentId,s.invitation_id nrrInvitationId,"
						+ " s.realname nrrRealName,s.head_ico nrrHeadIco,s2.student_id nrrToStudentId,s2.invitation_id nrrToInvitationId,s2.realname nrrToRealName,"
						+ " s2.head_ico nrrToHeadIco, DATE_FORMAT(nrr.create_time, '%Y-%m-%d %H:%i:%S') nrrCreateTime,nrr.reply_content nrrContent"
						+ " from notation_reply_reply nrr join notation_reply nr on(nr.notation_reply_id = nrr.notation_reply_id) "
						+ "join student s on(nrr.reply_student_id = s.student_id) join student s2 on(nrr.toreply_student_id=s2.student_id) where nr.notation_reply_id ='"
						+ replyId
						+ "' and ((nrr.reply_student_id = '"
						+ studentId
						+ "' and nrr.toreply_student_id='"
						+ nrStudentId
						+ "') or (nrr.reply_student_id = '"
						+ nrStudentId
						+ "' and nrr.toreply_student_id = '" + studentId + "'))";// 当前用户的一级评论者的所有的关于此一级回复的二级回复
				List list2 = baseDao.findListBySql(sql2, new String[] {});
				mm.put("nrrList", list2);
			}
		} else if (type.equals("collect")) {// 收藏查询
			sql = "SELECT "
					+ " s.student_id studentId,s.head_ico headIco,s.invitation_id invitationId,s.realname realName,DATE_FORMAT(c.create_time, '%Y-%m-%d %H:%i:%S') createTime "
					+ " FROM " + "(select * from product union select * from product_version)	p "
					+ " join collection c on(p.product_id = c.product_id) "
					+ " JOIN student s ON ( " + "	s.student_id = c.student_id " + ") " + "WHERE "
					+ "	p.product_id = '" + productId + "' " + " ORDER BY " + "	c.create_time DESC";
			list = baseDao.findListBySql(sql, new String[] {});
		} else if (type.equals("vote")) {// 投票查询
			sql = "select s.student_id studentId,s.head_ico headIco,s.invitation_id invitationId, s.realname realName,DATE_FORMAT(v.create_time, '%Y-%m-%d %H:%i:%S') createTime from (select * from product union select * from product_version) p join vote v on(p.product_id = v.product_id)"
					+ " join student s on(s.student_id = v.student_id) where p.product_id = '"
					+ productId + "' order by v.create_time";
			list = baseDao.findListBySql(sql, new String[] {});

		}
		return list;
	}

	/**
	 * 取出第二条数据
	 * 
	 * @param studentId
	 * @return
	 */
	public String findLastSecondViewMessageTime(String studentId) {
		String sql = "select vm.create_time createTime from view_message vm where vm.student_id ='"
				+ studentId + "' order by vm.create_time desc LIMIT 1,1";
		HashMap map = baseDao.findMapBySql(sql, new String[] { "createTime" });
		return (String) map.get("createTime");
	}

	/**
	 * 取出第一条数据
	 * 
	 * @param studentId
	 * @return
	 */
	public String findLastFirstViewMessageTime(String studentId) {
		String sql = "select vm.create_time createTime from view_message vm where vm.student_id ='"
				+ studentId + "' order by vm.create_time desc LIMIT 0,1";
		HashMap map = baseDao.findMapBySql(sql, new String[] { "createTime" });
		return (String) map.get("createTime");
	}

	public void saveLastViewMessage(String studentId) {
		String sql = "insert into view_message(view_message_id,student_id) values('"
				+ UUUID.getNextIntValue() + "','" + studentId + "')";
		baseDao.executeUpdateBySql(sql);
	}

	/**
	 * 查询作品的最新消息数量
	 * 
	 * @param productId
	 * @param lastTime
	 * @param m
	 * @return
	 */
	public void findMessageCount(String productId, String lastTime, Map m) {
		String sql = "SELECT" + " count(1)" + " FROM" + " praise p" + " WHERE"
				+ " p.product_id = '" + productId + "'" + " AND p.create_time > '" + lastTime + "'";
		String sql2 = "select count(1) from collection c where c.product_id = '" + productId
				+ "' and c.create_time > '" + lastTime + "'";
		String sql3 = "select count(1) from vote v where v.product_id = '" + productId
				+ "' and v.create_time > '" + lastTime + "'";
		String sql4 = "select count(1) from notation_reply nr join title_reply tr on(tr.title_reply_id = nr.title_reply_id) where tr.product_id = '"
				+ productId + "' and nr.create_time > '" + lastTime + "'";
		/*
		 * 此处对于针对自己对自己作品的二级回复的消息时不拉出来的，是放在我的参与里面的，如果后期需要放在我的作品里，思路是:
		 * 二级回复带出作品id以及二级回复的被回复者id然后筛选，
		 */
		int praiseCount = baseDao.findIntBySql(sql);
		int collectCount = baseDao.findIntBySql(sql2);
		int voteCount = baseDao.findIntBySql(sql3);
		int replyCount = baseDao.findIntBySql(sql4);
		m.put("newPraiseCount", praiseCount);
		m.put("newCollectCount", collectCount);
		m.put("newVoteCount", voteCount);
		m.put("newReplyCount", replyCount);
		m.put("newCount", praiseCount + collectCount + voteCount + replyCount);
	}

	public Object findMessageProductCount(String studentId) {
		String lastTime = findLastFirstViewMessageTime(studentId);
		Map m = new HashMap();
		String sql = "select count(1) from praise pr join product p on(pr.product_id=p.product_id)  where p.student_id='"
				+ studentId + "' and pr.create_time > '" + lastTime + "'";
		String sql2 = "select count(1) from collection c join product  p on(c.product_id=p.product_id)  where p.student_id='"
				+ studentId + "' and c.create_time > '" + lastTime + "'";
		String sql3 = "select count(1) from vote v join product  p on(v.product_id=p.product_id)  where p.student_id='"
				+ studentId + "' and v.create_time > '" + lastTime + "'";
		String sql4 = "select count(1) from notation_reply nr join title_reply tr on(nr.title_reply_id = tr.title_reply_id) join product p on(tr.product_id=p.product_id)  where p.student_id='"
				+ studentId + "' and nr.create_time > '" + lastTime + "'";
		int praiseCount = baseDao.findIntBySql(sql);
		int collectCount = baseDao.findIntBySql(sql2);
		int voteCount = baseDao.findIntBySql(sql3);
		int replyCount = baseDao.findIntBySql(sql4);
		return praiseCount + collectCount + voteCount + replyCount;

	}

	/*
	 * "SELECT" + "	p.product_id productId," + "	p.product_name productName," +
	 * "	tr.title_reply_id titleReplyId," + "	tr.title_name titleName," +
	 * "	tr.start start," + "	tr.end end," +
	 * "	nr.notation_reply_id notationReplyId," +
	 * "	nr.notation_content notationContent, " +
	 * "  s2.student_id toStudentId, " + "	s2.realname toRealName, " +
	 * "	s.student_id studentId, " + "	s.realname realName, " +
	 * "	nrr.reply_content replyContent, " +
	 * "	nrr2.notation_reply_reply_id toNotationReplyReplyId, " +
	 * "	nrr2.reply_content toReplyContent, " +
	 * "	nrr2.create_time toCreateTime " + "	 " + "FROM " +
	 * "	notation_reply_reply nrr " + "JOIN notation_reply nr ON ( " +
	 * "	nrr.notation_reply_id = nr.notation_reply_id " + ") " +
	 * "JOIN title_reply tr ON ( " + "	nr.title_reply_id = tr.title_reply_id " +
	 * ") " + "JOIN product p ON (tr.product_id = p.product_id) " +
	 * "join student s on(nrr.reply_student_id = s.student_id) " +
	 * "join student s2 on(nrr.toreply_student_id = s2.student_id) " +
	 * "left join notation_reply_reply nrr2 on(nrr2.notation_reply_reply_id = nrr.to_notation_reply_reply_id) "
	 * + "left join student s3 on(nrr2.reply_student_id = s3.student_id) " +
	 * "where nrr.toreply_student_id = '2011228122' and nrr.create_time > '2012' "
	 * ;
	 */
	/**
	 * 查看用户参与的消息总数
	 * 
	 * @param request
	 * @return
	 */
	public Object findMessageReplyCount(HttpServletRequest request, String studentId,
			String lastTime) {
		/* 被回复总数 */
		String sql = "SELECT count(1) FROM notation_reply_reply nrr "
				+ " where nrr.toreply_student_id = '" + studentId + "' and nrr.create_time > '"
				+ lastTime + "' ";
		/* 二级评论被点赞总数 */
		String sql2 = "SELECT count(1) FROM praise p  join notation_reply_reply nrr on(nrr.notation_reply_reply_id = p.product_id) "
				+ " where nrr.reply_student_id = '"
				+ studentId
				+ "' and nrr.create_time > '"
				+ lastTime + "' ";
		/* 一级评论被有用的总数 */
		String sql3 = "SELECT " + "	count(1) " + " FROM " + "	praise p "
				+ " join notation_reply nr on(nr.notation_reply_id = p.product_id) " + " WHERE "
				+ "	nr.reply_student_id = '" + studentId + "' and p.create_time > '" + lastTime
				+ "'";
		String sql4 = "SELECT count(1) adoptCount FROM notation_reply nr "
				+ " where nr.reply_student_id = '" + studentId
				+ "' and nr.isadopt = 'Y' and nr.adopt_time > '" + lastTime + "'";
		int replyPraiseCount = baseDao.findIntBySql(sql2);
		int replyCount = baseDao.findIntBySql(sql);
		int replyUsefulCount = baseDao.findIntBySql(sql3);
		int adoptCount = baseDao.findIntBySql(sql4);
		return replyCount + replyPraiseCount + replyUsefulCount + adoptCount;
	}

	public Object findMessageReplyList2(Page page, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId = student.getStudentId();
		String lastTime = findLastSecondViewMessageTime(studentId);
		String sql = "SELECT"
				+ "	p.product_id productId,"
				+ "	p.product_name productName,"
				+ "	p.content proContent,"
				+ "	p.create_time pCreateTime,"
				+ "	tr.title_reply_id titleReplyId,"
				+ "	tr.title_name titleName,"
				+ "	tr.start start,"
				+ "	tr.end end,"
				+ "	nr.notation_reply_id notationReplyId,"
				+ "	nr.notation_content notationContent, "
				+ "	nr.create_time rCreateTime, "
				+ "	s.student_id studentId, "
				+ "	s.realname realName, "
				+ "  s2.student_id toStudentId, "
				+ "	s2.realname toRealName, "
				+ "	nrr.reply_content replyContent, "
				+ "	nrr.create_time rrCreateTime, "
				+ "	nrr2.notation_reply_reply_id toNotationReplyReplyId, "
				+ "	nrr2.reply_content toReplyContent, "
				+ "	nrr2.create_time rr2CreateTime "
				+ "	 "
				+ "FROM "
				+ "	notation_reply_reply nrr "
				+ "JOIN notation_reply nr ON ( "
				+ "	nrr.notation_reply_id = nr.notation_reply_id "
				+ ") "
				+ "JOIN title_reply tr ON ( "
				+ "	nr.title_reply_id = tr.title_reply_id "
				+ ") "
				+ "JOIN product p ON (tr.product_id = p.product_id) "
				+ "join student s on(nrr.reply_student_id = s.student_id) "
				+ "join student s2 on(nrr.toreply_student_id = s2.student_id) "
				+ "left join notation_reply_reply nrr2 on(nrr2.notation_reply_reply_id = nrr.to_notation_reply_reply_id) "
				+ "left join student s3 on(nrr2.reply_student_id = s3.student_id) "
				+ "where nrr.toreply_student_id = '" + studentId + "' and nrr.create_time > '"
				+ lastTime + "' order by nrr.create_time desc";
		Page p = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] { "productId", "productName", "proContent", "pCreateTime",
						"titleReplyId", "titleName", "start", "end", "notationReplyId",
						"notationContent", "rCreateTime", "studentId", "realName", "toStudentId",
						"toRealName", "replyContent", "rrCreateTime", "toNotationReplyReplyId",
						"toReplyContent", "rr2CreateTime" });
		return p.getResult();

	}

	/**
	 * 查询别人对自己回复的操作(赞，有用，采纳，回复)(以作品归类) 注意：查询所有作品的时候是包含历史版本的
	 * 
	 * @param page
	 * @param studentId
	 * @param request
	 * @return
	 */
	public Page findMessageReplyList(Page page, String studentId, HttpServletRequest request) {
		String sql = "select DISTINCT createTime,content,height,width,score,replyCount,praiseCount,collectionCount,voteCount,productId,collectCount,version,productName,studentId,realName,headico,labelId,labelName from "
				+ "(SELECT DATE_FORMAT(p.create_time, '%Y-%m-%d %H:%i:%S') createTime,p.content content,p.small_content smallContent,p.height height,p.width width, p.score score,  p.reply_count replyCount,p.praise_count praiseCount,p.collection_count collectionCount,p.vote_count voteCount, "
				+ "	p.product_id productId ,p.collection_count collectCount,p.version version ,p.product_name productName,s.student_id studentId,s.realname realName,s.head_ico headico,l.label_id labelId,l.label_name labelName"
				+ " FROM student_act sa"
				+ " JOIN (select * from product union select * from product_version) p ON (sa.to_product_id = p.product_id) "
				+ " join student s on(p.student_id = s.student_id)"
				+ " join label l on(l.label_id = p.label_id)"
				+ "	where sa.to_student_id = '"
				+ studentId
				+ "'"
				+ " AND (sa.obj_type = '1' or sa.obj_type = '2') order by sa.create_time desc)t";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2;
	}

	/**
	 * 查询别人对自己回复的回复总数（以作品为单位统计） 注意:在查询最新消息数量的时候只是从product表中查询
	 * 
	 * @param productId
	 * @param lastTime
	 * @param lastTime2
	 */
	public int findMessageReplyCountByProduct(String productId, String studentId, String lastTime) {
		String sql = "SELECT count(1) replyCount " + " FROM " + "	notation_reply_reply nrr "
				+ "JOIN notation_reply nr ON ( " + "	nrr.notation_reply_id = nr.notation_reply_id "
				+ ") " + " JOIN title_reply tr ON ( " + "	nr.title_reply_id = tr.title_reply_id "
				+ ") " + " where nrr.toreply_student_id = '" + studentId
				+ "' and tr.product_id = '" + productId + "' and nrr.create_time > '" + lastTime
				+ "' order by nrr.create_time desc";
		int replyCount = baseDao.findIntBySql(sql);
		return replyCount;

	}

	/**
	 * 查询自己用户一级回复最新被采纳的数量(以作品归类)
	 * 
	 * @param productId
	 * @param studentId
	 * @param lastTime
	 * @return
	 */
	public int findAdoptCount(String productId, String studentId, String lastTime) {
		String sql = "SELECT count(1) adoptCount FROM " + "	notation_reply nr "
				+ " JOIN title_reply tr ON ( " + "	nr.title_reply_id = tr.title_reply_id " + " ) "
				+ " where tr.product_id = '" + productId + "' and nr.reply_student_id = '"
				+ studentId + "' and nr.isadopt = 'Y' and nr.adopt_time > '" + lastTime + "'";
		int adoptCount = baseDao.findIntBySql(sql);
		return adoptCount;
	}

	/**
	 * 查询自己用户一级二级回复最新被赞的数量(以作品归类)
	 * 
	 * @param productId
	 * @param studentId
	 * @param lastTime
	 * @return
	 */
	public int findPraiseCount(String productId, String studentId, String lastTime) {
		String sql = "SELECT " + "	count(1) " + " FROM " + "	praise p "
				+ " JOIN notation_reply_reply nrr ON ( "
				+ "	p.product_id = nrr.notation_reply_reply_id " + ") "
				+ " join notation_reply nr on(nr.notation_reply_id = nrr.notation_reply_id) "
				+ " join title_reply tr on(tr.title_reply_id = nr.title_reply_id) " + " WHERE "
				+ "	nrr.reply_student_id = '" + studentId + "'  and tr.product_id = '" + productId
				+ "' and p.create_time > '" + lastTime + "'";
		int replyReplyPraiseCount = baseDao.findIntBySql(sql);

		return replyReplyPraiseCount;
	}

	/**
	 * 查询自己用户一级二级回复最新别认为有用的数量(以作品归类)
	 * 
	 * @param productId
	 * @param studentId
	 * @param lastTime
	 * @return
	 */
	public int finduUsefulCount(String productId, String studentId, String lastTime) {
		String sql2 = "SELECT " + "	count(1) " + " FROM " + "	praise p "
				+ " join notation_reply nr on(nr.notation_reply_id = p.product_id) "
				+ " join title_reply tr on(tr.title_reply_id = nr.title_reply_id) " + " WHERE "
				+ "	nr.reply_student_id = '" + studentId + "'  and tr.product_id = '" + productId
				+ "' and p.create_time > '" + lastTime + "'";
		int replyPraiseCount = baseDao.findIntBySql(sql2);
		return replyPraiseCount;
	}

	/**
	 * 查询某一作品的详情时时包含历史版本作品的
	 * 
	 * @param productId
	 * @param studentId
	 * @param type
	 * @return
	 */
	public Object findMessageReplyInfo(String productId, String studentId, String type) {
		String sql = null;
		List list = null;
		if (type.equals("praise")) {// 点赞查询
			sql = "SELECT "
					+ "	s.student_id prStudentId,s.invitation_id invitationId,s.head_ico headIco, "
					+ "	s.realname prRealName, "
					+ "	DATE_FORMAT(pr.create_time, '%Y-%m-%d %H:%i:%S') prCreateTime, "
					+ "	tr.title_reply_id titleReplyId,(select count(1) from notation_reply where title_reply_id = titleReplyId) nrCount, "
					+ "	tr.start start, "
					+ "	tr.end end, "
					+ " tr.title_name titleName, "
					+ " nr.notation_reply_id replyId, "
					+ " s2.student_id nrStudentId, "
					+ " s2.realname nrRealName, "
					+ " nr.notation_content nrContent, "
					+ " nr.isadopt isadopt, "
					+ " nr.praise_count nrPraiseCount, "
					+ " DATE_FORMAT(nr.create_time, '%Y-%m-%d %H:%i:%S') nrCreateTime, "
					+ " nrr.reply_content nrrContent, "
					+ " nrr.praise_count nrrPraiseCount, "
					+ " DATE_FORMAT(nrr.create_time, '%Y-%m-%d %H:%i:%S') nrrCreateTime, "
					+ " s3.student_id toNrrStudentId, "
					+ " s3.realname toNrrRealName "
					+ " FROM "
					+ "	title_reply tr "
					+ " JOIN notation_reply nr ON ( "
					+ "	tr.title_reply_id = nr.title_reply_id "
					+ ") "
					+ " JOIN notation_reply_reply nrr ON ( "
					+ "	nr.notation_reply_id = nrr.notation_reply_id "
					+ ") "
					+ " JOIN praise pr ON ( "
					+ "	nrr.notation_reply_reply_id = pr.product_id "
					+ ") "
					+ " JOIN student s ON (s.student_id = pr.student_id) "
					+ " join student s2 on(s2.student_id = nr.reply_student_id) "
					+ " left join notation_reply_reply nrr2 on(nrr.to_notation_reply_reply_id = nrr2.notation_reply_reply_id) "
					+ " left join student s3 on(s3.student_id= nrr2.reply_student_id) " + " WHERE "
					+ "	tr.product_id = '" + productId + "' " + " AND nrr.reply_student_id = '"
					+ studentId + "' " + " order by nrr.create_time desc";

			list = baseDao.findListBySql(sql, new String[] {});
		} else if (type.equals("useful")) {// 有用查询
			sql = "SELECT "
					+ "	s.student_id prStudentId,s.invitation_id invitationId,s.head_ico headIco, "
					+ "	s.realname prRealName, "
					+ "	DATE_FORMAT(pr.create_time, '%Y-%m-%d %H:%i:%S') createTime, "
					+ "	tr.title_reply_id titleReplyId, (select count(1) from notation_reply where title_reply_id = titleReplyId) nrCount, "
					+ "	tr.start start, "
					+ "	tr.end end, "
					+ "	s2.student_id trStudentId , "
					+ "	s2.realname trRealName, "
					+ " tr.title_name titleName, "
					+ " nr.notation_reply_id replyId, "
					+ " nr.notation_content nrContent, "
					+ " nr.isadopt isadopt, "
					+ " nr.praise_count nrPraiseCount, "
					+ " DATE_FORMAT(nr.create_time, '%Y-%m-%d %H:%i:%S') nrCreateTime from title_reply tr "
					+ " JOIN notation_reply nr ON ( " + "	tr.title_reply_id = nr.title_reply_id "
					+ ") " + " JOIN praise pr ON ( " + "	nr.notation_reply_id = pr.product_id "
					+ ") " + " JOIN student s ON (s.student_id = pr.student_id) "
					+ " JOIN student s2 ON (s2.student_id = tr.reply_student_id) " + " WHERE "
					+ "	tr.product_id = '" + productId + "' " + " AND nr.reply_student_id = '"
					+ studentId + "' " + "ORDER BY " + "	nr.create_time DESC";
			list = baseDao.findListBySql(sql, new String[] {});
		} else if (type.equals("adopt")) {// 被采纳查询
			sql = "SELECT "
					+ "	tr.title_reply_id titleReplyId,tr.title_name titleName,(select count(1) from notation_reply where title_reply_id = titleReplyId) nrCount, "
					+ "  tr.start start, "
					+ "  tr.end end, "
					+ "  s.student_id trStudentId, s3.invitation_id invitationId,s3.head_ico headIco,s3.realname realName, "
					+ "  s.realname trRealName, "
					+ "  DATE_FORMAT(tr.create_time, '%Y-%m-%d %H:%i:%S') trCreateTime, "
					+ "  nr.notation_reply_id replyId, "
					+ "  nr.praise_count nrPraiseCount, "
					+ "  nr.praise_count nrContent, "
					+ "  s2.student_id nrStudentId, "
					+ "  s2.realname nrRealName, "
					+ "  DATE_FORMAT(nr.create_time, '%Y-%m-%d %H:%i:%S') nrCreateTime  "
					+ " FROM "
					+ "	notation_reply nr "
					+ " JOIN title_reply tr ON ( "
					+ "	nr.title_reply_id = tr.title_reply_id "
					+ ") join (select * from product_version union select * from  product) pr on(pr.product_id = tr.product_id)"
					+ "join student s3 on(s3.student_id = pr.student_id)"
					+ " join student s on(s.student_id = tr.reply_student_id) "
					+ " join student s2 on(s2.student_id = nr.reply_student_id) " + " WHERE "
					+ "	tr.product_id = '" + productId + "' " + " AND nr.reply_student_id = '"
					+ studentId + "' " + " and nr.isadopt = 'Y' " + "ORDER BY "
					+ "	nr.create_time DESC";
			list = baseDao.findListBySql(sql, new String[] {});
		} else if (type.equals("reply")) {
			sql = "SELECT "
					+ "	tr.title_reply_id titleReplyId, (select count(1) from notation_reply where title_reply_id = titleReplyId) nrCount,"
					+ "	tr.start start, "
					+ "	tr.end end, "
					+ " tr.title_name titleName, "
					+ " s.student_id trStudentId, "
					+ " s.realname trRealName, "
					+ " nr.notation_reply_id replyId, "
					+ " nr.isadopt isadopt, "
					+ " nr.notation_content nrContent, "
					+ " nr.praise_count nrPraiseCount, "
					+ " s2.student_id nrStudentId, "
					+ " s2.realname nrRealName,s2.head_ico nrHeadIco,s2.invitation_id  nrInvitationId,"
					+ " DATE_FORMAT(nr.create_time, '%Y-%m-%d %H:%i:%S') nrCreateTime, "
					+ " nrr.reply_content nrrContent, "
					+ " nrr.notation_reply_reply_id replyReplyId, "
					+ " s3.student_id nrrStudentId, "
					+ " s3.realname nrrRealName,s3.head_ico nrrHeadIco,s3.invitation_id nrrInvitationId, "
					+ " s4.student_id nrrToStudentId, "
					+ " s4.realname nrrToRealName, s4.head_ico nrrToHeadIco,s4.invitation_id nrrToInvitationId,"
					+ " DATE_FORMAT(nrr.create_time, '%Y-%m-%d %H:%i:%S') nrrCreateTime, "
					+ " nrr2.notation_reply_reply_id toReplyReplyId, "
					+ " nrr2.reply_content nrrToContent, "
					+ " nrr2.praise_count nrrPraiseCount, "
					+ " DATE_FORMAT(nrr2.create_time, '%Y-%m-%d %H:%i:%S') nrrToCreateTime, "
					+ " s5.student_id nrrToTostudentId, "
					+ " s5.realname nrrToToRealName,s5.head_ico nrrToTooHeadIco,s5.invitation_id nrrToToInvitationId "
					+ " FROM " + "	notation_reply_reply nrr " + " JOIN notation_reply nr ON ( "
					+ "	nrr.notation_reply_id = nr.notation_reply_id " + ") "
					+ " JOIN title_reply tr ON ( " + "	nr.title_reply_id = tr.title_reply_id "
					+ ") " + " LEFT JOIN notation_reply_reply nrr2 ON ( "
					+ "	nrr.to_notation_reply_reply_id = nrr2.notation_reply_reply_id " + ") "
					+ "JOIN student s ON ( " + "	s.student_id = tr.reply_student_id " + ") "
					+ " JOIN student s2 ON ( " + "	s2.student_id = nr.reply_student_id " + ") "
					+ " JOIN student s3 ON ( " + "	s3.student_id = nrr.reply_student_id " + ") "
					+ " JOIN student s4 ON ( " + "	s4.student_id = nrr.toreply_student_id " + ") "
					+ " LEFT JOIN student s5 ON ( " + "	s5.student_id = nrr2.toreply_student_id "
					+ ") " + " WHERE " + "	tr.product_id = '" + productId
					+ "' and nrr.toreply_student_id = '" + studentId
					+ "' order by nrr.create_time desc";
			list = baseDao.findListBySql(sql, new String[] {});
		}
		return list;
	}

	/*
	 * SELECT s.student_id prStudentId, s.realname prRealName, pr.create_time
	 * createTime, tr.title_reply_id titleReplyId, tr. START START, tr. END END,
	 * tr.title_name titleName, nr.notation_reply_id replyId, s2.student_id
	 * nrStudentId, s2.realname nrRealName, nr.notation_content nrContent,
	 * nr.isadopt isadopt, nr.praise_count nrPraiseCount, nr.create_time
	 * nrCreateTime, nrr.reply_content nrrContent, nrr.reply_content nrrContent,
	 * nrr.praise_count nrrPraiseCount, nrr.create_time nrrCreateTime FROM
	 * product p JOIN title_reply tr ON (p.product_id = tr.product_id) JOIN
	 * notation_reply nr ON ( tr.title_reply_id = nr.title_reply_id ) JOIN
	 * notation_reply_reply nrr ON ( nr.notation_reply_id =
	 * nrr.notation_reply_id ) JOIN praise pr ON ( nrr.notation_reply_reply_id =
	 * pr.product_id ) JOIN student s ON (s.student_id = pr.student_id) join
	 * student s2 on(s2.student_id = nr.reply_student_id) WHERE p.product_id =
	 * '14395427060234244935655640484161' AND nrr.reply_student_id
	 */

	public Object saveAct(String studentId, String actType, String objType, String objId,
			String actId, String toProductId, String toStudentId) {
		StudentAct studentAct = new StudentAct();
		studentAct.setActType(actType);
		studentAct.setObjId(objId);
		studentAct.setObjType(objType);
		studentAct.setStudentId(studentId);
		studentAct.setActId(actId);
		studentAct.setToProductId(toProductId);
		studentAct.setToStudentId(toStudentId);
		baseDao.saveEntity(studentAct);
		return "success";
	}

	/**
	 * 查询被点赞的作品
	 * 
	 * @param objId
	 */
	public void findPraiseProduct(String objId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被点赞的一级评论
	 * 
	 * @param objId
	 */
	public void findPraiseReply(String objId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被点赞的二级评论
	 * 
	 * @param objId
	 */
	public void findPraiseReplyReply(String objId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被收藏的作品
	 * 
	 * @param objId
	 */
	public void findCollectProduct(String objId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被投票的作品
	 * 
	 * @param objId
	 */
	public void findVoteProduct(String objId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被采纳的一级评论
	 * 
	 * @param objId
	 */
	public void findAdoptReply(String objId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被评论的作品以及此一级评论
	 * 
	 * @param objId
	 * @param actId
	 */
	public void findPLProduct(String objId, String actId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被回复的一级评论以及此二级评论
	 * 
	 * @param objId
	 * @param actId
	 */
	public void findPlReply(String objId, String actId) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询被回复的二级评论以及此二级评论
	 * 
	 * @param objId
	 * @param actId
	 */
	public void findPLReplyReplyt(String objId, String actId) {
		// TODO Auto-generated method stub

	}

	public void saveProduct(Product product) {
		baseDao.saveEntity(product);
	}

	/**
	 * 查询全部
	 * 
	 * @param student
	 * @param page
	 * @return
	 */
	public List findPersonCenterByAll(Student student, Page page) {
		String studentId = student.getStudentId();
		String schoolIds = "'"
				+ (student.getSchool() == null ? "" : student.getSchool().getSchoolId()) + "'";
		String attention = "'" + studentId + "'";
		String labelIds = "''";
		String labelSql = "";
		@SuppressWarnings("unchecked")
		List<Attention> listAttention = baseDao
				.findListByHql("from  Attention a where a.studentId = '" + studentId + "'");
		for (Attention map : listAttention) {
			String toStudentId = (String) map.getToStudentId();
			attention += ",'" + toStudentId + "'";
		}
		Page page2 = null;
		// 判断用户类型
		String roleType = student.getRole().getRoleType();
		if (roleType.equals("user") || roleType.equals("nc_user")) {// 普通学生用户(完善信息和未完善信息的)
			labelIds += ",'"
					+ (student.getJobLabel() == null ? "" : student.getJobLabel().getLabelId())
					+ "'";
			labelSql = " and la.pid in (" + labelIds + ")";
			/*// 查询用户配置的标签范围
			String sql3 = "select oa.area_id areaId from obtaion_area oa where oa.student_id = '"
					+ student.getStudentId() + "' and oa.type = 'LA'";
			List<Map> areaList2 = baseDao.findListBySql(sql3, new String[] {});
			for (Map m : areaList2) {
				String area = (String) m.get("areaId");
				labelIds += ",'" + area + "'";
			}
			// 若有标签配置
			if (areaList2.size() > 0) {
				labelSql = " and la.label_id in (" + labelIds + ")";
			}*/
			String sql = "select p.* from  product p join student st on(st.student_id = p.student_id) join label la on(p.label_id = la.label_id)"
					+ " where  ( st.school_id in ("
					+ schoolIds
					+ ")"
					+ labelSql
					+ " or (st.student_id in ("
					+ attention
					+ "))) or ('"
					+ (student.getSchool() == null ? "" : student.getSchool().getSchoolId())
					+ "' in (select cp.area_id from company_product_show_area cp where cp.student_id = st.student_id ) "
					+ " and "
					+ student.getYear()
					+ " in (select cy.grade from company_product_show_year cy where cy.student_id = st.student_id )  ) order by p.create_time desc";
			page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
					Product.class);
		} else if (roleType.equals("company_manager_user")
				|| roleType.equals("company_common_user")
				|| roleType.equals("nc_company_common_user")
				|| roleType.equals("nc_company_manager_user")) {// 企业用户(完善信息和未完善信息的)
			labelIds += ",'"
					+ (student.getJobLabel() == null ? "" : student.getJobLabel().getLabelId())
					+ "'";
			labelSql = " and la.pid in (" + labelIds + ")";
			// 查询企业配置的学校范围
			String sql2 = "select oa.area_id areaId from obtaion_area oa where oa.student_id = '"
					+ student.getStudentId() + "' and oa.type = 'SC'";
			List<Map> areaList = baseDao.findListBySql(sql2, new String[] {});
			for (Map m : areaList) {
				String area = (String) m.get("areaId");
				schoolIds += ",'" + area + "'";
			}
			/*// 查询用户配置的标签范围
			String sql3 = "select oa.area_id areaId from obtaion_area oa where oa.student_id = '"
					+ student.getStudentId() + "' and oa.type = 'LA'";
			List<Map> areaList2 = baseDao.findListBySql(sql3, new String[] {});
			for (Map m : areaList2) {
				String area = (String) m.get("areaId");
				labelIds += ",'" + area + "'";
			}
			if (areaList2.size() > 0) {
				labelSql = " la.label_id in (" + labelIds + ")";
			}*/
			String sql = "select p.* from  product p  join student st on(st.student_id = p.student_id) join label la on(p.label_id = la.label_id)"
					+ " where  (st.school_id in ("
					+ schoolIds
					+ ")"
					+ labelSql
					+ " or (st.student_id in ("
					+ attention
					+ "))) and ((st.role_id != '1' and st.role_id !='2' and st.role_id !='3' and st.role_id !='4') or st.student_id = '"
					+ student.getStudentId() + "') order by p.create_time desc";
			page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
					Product.class);
		} else if (roleType.equals("talent_user")) {// 达人用户
			labelIds += ",'"
					+ (student.getJobLabel() == null ? "" : student.getJobLabel().getLabelId())
					+ "'";
			labelSql = " and la.pid in (" + labelIds + ")";
			// 查询企业配置的学校范围
			String sql2 = "select oa.area_id areaId from obtaion_area oa where oa.student_id = '"
					+ student.getStudentId() + "' and oa.type = 'SC'";
			List<Map> areaList = baseDao.findListBySql(sql2, new String[] {});
			for (Map m : areaList) {
				String area = (String) m.get("areaId");
				schoolIds += ",'" + area + "'";
			}
			// 查询用户配置的标签范围
			String sql3 = "select oa.area_id areaId from obtaion_area oa where oa.student_id = '"
					+ student.getStudentId() + "' and oa.type = 'LA'";
			List<Map> areaList2 = baseDao.findListBySql(sql3, new String[] {});
			for (Map m : areaList2) {
				String area = (String) m.get("areaId");
				labelIds += ",'" + area + "'";
			}
			if (areaList2.size() > 0) {
				labelSql = " la.label_id in (" + labelIds + ")";
			}
			String sql = "select p.* from  product p  join student st on(st.student_id = p.student_id) join label la on(p.label_id = la.label_id)"
					+ " where  (st.school_id in ("
					+ schoolIds
					+ ")"
					+ labelSql
					+ " or (st.student_id in (" + attention + "))) order by p.create_time desc";
			page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
					Product.class);
		}
		return page2.getResult();
	}

	/**
	 * 按关注者查询
	 * 
	 * @param student
	 * @param page
	 * @return
	 */
	public List findPersonCenterByAttent(Student student, Page page) {
		String studentId = student.getStudentId();
		String attention = "''";
		/*
		 * for (Label lab : student.getInterest()) { labelIds += ",'" +
		 * lab.getLabelId() + "'"; }
		 */
		List<Attention> listAttention = baseDao
				.findListByHql("from  Attention a where a.studentId = '" + studentId + "'");
		for (Attention map : listAttention) {
			String toStudentId = (String) map.getToStudentId();
			attention += ",'" + toStudentId + "'";
		}
		String sql = "select p.* from product p join student st on(p.student_id = st.student_id)"
				+ " where  st.student_id in (" + attention + ") order by p.create_time desc";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Product.class);
		return page2.getResult();
	}

	/**
	 * 按标签查询
	 * 
	 * @param student
	 * @param labelId
	 * @param page
	 * @return
	 */
	public List findPersonCenterByLabel(Student student, String labelId, Page page) {
		String studentId = student.getStudentId();
		String schoolId = student.getSchool().getSchoolId();
		String attention = "'" + studentId + "'";
		String labelIds = "'" + labelId + "'";
		List<Attention> listAttention = baseDao
				.findListByHql("from  Attention a where a.studentId = '" + studentId + "'");
		for (Attention map : listAttention) {
			String toStudentId = (String) map.getToStudentId();
			attention += ",'" + toStudentId + "'";
		}
		String sql = "select p.* from student_act sa  join product p on(p.product_id = sa.act_id) join student st on(st.student_id = sa.student_id)"
				+ " where  (sa.act_type in('F') and sa.obj_type in('0') and p.label_id in("
				+ labelIds
				+ ")) and( st.school_id = '"
				+ schoolId
				+ "'"
				+ " or (sa.student_id in (" + attention + "))) order by sa.create_time desc";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Product.class);
		return page2.getResult();
	}

	public List findAct(Page page, String studentId) {
		String sql = "select sa.student_id studentId,sa.act_id actId,sa.act_type actType,sa.obj_id objId,sa.obj_type objType,sa.create_time createTime "
				+ "from student_act sa where sa.student_id ='"
				+ studentId
				+ "' order by sa.create_time desc ";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2.getResult();
	}

	public List findYjAct(Page page, String studentId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Student findStudentByInvitationId(String invitationId) {
		String hql = "from Student s where s.invitationId = '" + invitationId + "'";
		List list = baseDao.findListByHql(hql);
		if (list == null || list.size() == 0)
			return null;
		return (Student) list.get(0);
	}

	public List findHomeProductsByProduct(String studentId, String type, String labelId, Page page) {
		String hql = "from Product p where p.student.studentId = '" + studentId
				+ "' and p.label.labelId = '" + labelId + "'";
		Page page2 = baseDao.findPageByHql(hql, page.getPageNo(), page.getPageSize(), false);
		return page2.getResult();
	}

	public List<Product> findHomeProductsBySuggestion(String studentId, String type,
			String labelId, Page page) {
		String sql = "select t.* from (select DISTINCT p.* from notation_reply nr join title_reply tr on(nr.title_reply_id = tr.title_reply_id) "
				+ " join (select * from product union select * from  product_version) p on(p.product_id = tr.product_id) "
				+ " where nr.reply_student_id = '"
				+ studentId
				+ "' and p.label_id ='"
				+ labelId
				+ "'  ORDER BY nr.create_time desc) t";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Product.class);
		return page2.getResult();
	}

	public List<Product> findHomeProductsByCollect(String studentId, String type, String labelId,
			Page page) {
		String sql = "select DISTINCT p.* from collection c join (select * from product union select * from  product_version) p on(p.product_id = c.product_id)"
				+ " where c.student_id = '" + studentId + "' and p.label_id = '" + labelId + "'";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Product.class);
		return page2.getResult();
	}

	public List<Student> findHomeProductsByAttention(String studentId, String type, Page page) {
		String sql = "select st.* from attention at join student st on(at.to_student_id = st.student_id) "
				+ " where at.student_id = '" + studentId + "'";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Student.class);
		return page2.getResult();
	}

	public List<Student> findHomeProductsByStudent(String studentId, String type, Page page) {
		String sql = "select s.* from student_student ss join student s on(ss.student_id = s.student_id) "
				+ "where ss.to_student_id ='" + studentId + "' and (ss.state = 'Y' or ss.state = 'YBM' or ss.state='YBY')";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Student.class);
		return page2.getResult();
	}

	/**
	 * 查询个人主页参与作品中个人的意见
	 * 
	 * @param productId
	 * @param studentId
	 * @return
	 */
	public List findHomeProductsNotationBySuggestion(String titleReplyId, String studentId) {
		String sql = "select st.realname realName ,tr.reply_student_id trStudentId ,tr.title_reply_id titleReplyId, tr.start start, "
				+ "	tr.end end, tr.title_name titleName, "
				+ " nr.notation_reply_id notationReplyId, DATE_FORMAT(nr.create_time, '%Y-%m-%d %H:%i:%S') nrCreateTime, "
				+ " nr.isadopt isAdopt, "
				+ " nr.notation_content notationContent, "
				+ " nr.praise_count nrPraiseCount "
				+ "FROM "
				+ "	notation_reply nr "
				+ " JOIN title_reply tr ON ( "
				+ "	nr.title_reply_id = tr.title_reply_id "
				+ ") join student st on(st.student_id = nr.reply_student_id)"
				+ " WHERE "
				+ "	nr.reply_student_id = '"
				+ studentId
				+ "' "
				+ "AND tr.title_reply_id = '"
				+ titleReplyId + "'";
		List list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}

	public Object findMessageStudentStudentCoun(String studentId) {
		String lastTime = findLastFirstViewMessageTime(studentId);
		String sql = "select count(1) from student_student ss where ss.to_student_id ='"
				+ studentId + "' and ss.state='D' and ss.create_time > '" + lastTime + "'";
		int count = baseDao.findIntBySql(sql);
		return count;
	}

	public Object findMessageStudentFollowerCoun(String studentId) {
		String lastTime = findLastFirstViewMessageTime(studentId);
		String sql = "select count(1) from attention at where at.to_student_id = '" + studentId
				+ "' and at.create_time > '" + lastTime + "'";
		int count = baseDao.findIntBySql(sql);
		return count;
	}

	public Object findMessageStudentFlowerList(Page page, String studentId) {
		String sql = "select s.student_id studentId, s.realname realName,s.head_ico headIco,s.invitation_id invitationId,"
				+ " at.to_student_id toStudentId, DATE_FORMAT( at.create_time, '%Y-%m-%d %H:%i:%S') createTime "
				+ " from attention at join student s on(s.student_id = at.student_id) where at.to_student_id ='"
				+ studentId + "' order by at.create_time desc";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2.getResult();
	}

	public Object findMessageStudentStudentList(Page page, String studentId) {
		String sql = "select s.student_id studentId,s.head_ico headIco,s.invitation_id invitationId,ss.state state,s2.student_id toStudentId,"
				+ " s.realname realName, s2.realname toRealName,s2.head_ico toHeadIco,s2.invitation_id toInvitationId,"
				+ "DATE_FORMAT( ss.create_time, '%Y-%m-%d %H:%i:%S') createTime from student_student ss join student s on(ss.student_id = s.student_id) join student s2 on(s2.student_id = ss.to_student_id)"
				+ " where ss.to_student_id = '"
				+ studentId
				+ "' or ss.student_id ='"
				+ studentId
				+ "' order by ss.state, ss.create_time desc";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), false,
				new String[] {});
		return page2.getResult();
	}

	public StudentStudent findStudentStudentByRelation(String studentId, String studentId2) {
		String hql = "from StudentStudent ss where ss.studentId = " + studentId
				+ " and ss.toStudentId = " + studentId2 + "";
		List list = baseDao.findListByHql(hql);
		if (list == null || list.size() == 0)
			return null;
		return (StudentStudent) list.get(0);
	}

	public void updateStudentStudent(StudentStudent ss) {
		baseDao.saveOrUpdateEntity(ss);

	}

	public void saveStudentStudent() {
		// TODO Auto-generated method stub

	}

	public void saveStudentStudent(String studentId, String toStudentId, String telephone) {
		StudentStudent ss = new StudentStudent();
		ss.setStudentId(studentId);
		ss.setToStudentId(toStudentId);
		ss.setTelephone(telephone);
		baseDao.saveEntity(ss);
	}

	public Object updateStudentStudent(String studentId, String toStudentId, String type) {
		String sql = "update student_student set state= ? where student_id =? and to_student_id =?";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, type);
		sqlQuery.setString(1, studentId);
		sqlQuery.setString(2, toStudentId);
		int count = sqlQuery.executeUpdate();
		return count;
	}

	public List<Product> findWbm(Student student, Page page) {
		String sql = "select pr.* from product pr join student_student ss on(pr.student_id = ss.student_id) "
				+ " where ss.state = 'Y' and ss.to_student_id = '" + student.getStudentId() + "'";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Product.class);
		return page2.getResult();
	}

	public List<Product> findYbm(Student student, Page page) {
		String sql = "select pr.* from product pr join student_student ss on(pr.student_id = ss.student_id) "
				+ " where ss.state = 'YBM' and ss.to_student_id = '" + student.getStudentId() + "'";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Product.class);
		return page2.getResult();
	}

	public List<Product> findYby(Student student, Page page) {
		String sql = "select pr.* from product pr join student_student ss on(pr.student_id = ss.student_id) "
				+ " where ss.state = 'YBY' and ss.to_student_id = '" + student.getStudentId() + "'";
		Page page2 = baseDao.findPageEntityBySql(sql, page.getPageNo(), page.getPageSize(), false,
				Product.class);
		return page2.getResult();
	}

	public Student findStudentByEmail(String email) {
		String hql = "from Student s where s.email = '" + email + "'";
		return (Student) baseDao.findObjByHql(hql);
	}

	public void saveStudentLog(String studentId, String methodName, String mapping) {
		String sql = "insert into visit_log(student_id,method_name,mapping) values(?,?,?)";
		Session session = baseDao.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setString(0, studentId);
		sqlQuery.setString(1, methodName);
		sqlQuery.setString(2, mapping);
		sqlQuery.executeUpdate();
	}

	public void saveLastViewMessage(ViewMessage vm) {
		baseDao.saveEntity(vm);

	}

	public void updateStudent(Student student) {
		baseDao.updateEntity(student);

	}

	public Student findStudentById(String studentId) {
		Student student = (Student) baseDao.findEntity(Student.class, studentId);
		return student;
	}

	public Map findstudentStudentTypeCount(String studentId) {
		String sql = "select count(1) count,ss.state state from student_student ss where ss.to_student_id = '2011228122' GROUP BY ss.state";
		List list = baseDao.findListBySql(sql, new String[] {});
		Map map2 = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			map2.put(map.get("state"), map.get("count"));
		}
		return map2;
	}

	/**
	 * 用户作品曝光范围（学校）
	 * 
	 * @param studentId
	 * @return
	 */
	public List<Map> findCPSA(String studentId) {
		String sql = "select sc.school_id schoolId,sc.name schoolName from company_product_show_area cpsa join school sc on(sc.school_id = cpsa.area_id)"
				+ " where cpsa.student_id = '" + studentId + "'";
		List<Map> list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}

	/**
	 * 用户作品曝光范围（年级）
	 * 
	 * @param studentId
	 * @return
	 */
	public List<Map> findCPSY(String studentId) {
		String sql = "select cpsy.grade  grade from company_product_show_year cpsy where cpsy.student_id = '"
				+ studentId + "'";
		List<Map> list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}

	/**
	 * 查询用户1是否访问了用户2
	 * 
	 * @param studentId
	 *            用户1
	 * @param studentId2
	 *            用户2
	 * @return
	 */
	public boolean findIsAttention(String studentId, String studentId2) {
		String sql = "select *  from attention at where at.student_id='" + studentId
				+ "' and to_student_id = '" + studentId2 + "'";
		List list = baseDao.findListBySql(sql, new String[] {});
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 更新用户的认可度
	 * 
	 * @param studentId
	 * @param praiseCount
	 */
	public void updateStudentPraiseCount(String studentId, Integer praiseCount) {
		String sql = "update student set praise_count = praise_count+" + praiseCount
				+ " where student_id = '" + studentId + "'";
		baseDao.executeUpdateBySql(sql);
	}

	public void updateStudentLabelSQL(StudentLabel sl) {
		String sql = "update student_label set product_count = product_count+"
				+ sl.getProductCount() + " " + "where student_id = '" + sl.getStudentId()
				+ "' and label_id = '" + sl.getLabel().getLabelId() + "'";
		baseDao.executeUpdateBySql(sql);

	}
}
