package com.eeesns.tshow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Component;

import com.eeesns.tshow.entity.NotationReply;
import com.eeesns.tshow.entity.NotationReplyReply;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.ProductVersion;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.entity.TitleReply;
import com.eeesns.tshow.util.CommonDataUtil;
import com.eeesns.tshow.util.DateUtil;
import com.eeesns.tshow.util.UUUID;

@Component
public class ProductDao {
	@Resource
	BaseDao baseDao;
	@Resource
	StudentDao studentDao;

	public List findTtileReply(String productId) {
		String hql = "from TitleReply tr where tr.productId = '" + productId
				+ "' order by tr.createTime desc";
		List list = baseDao.findListByHql(hql);
		return list;
	}

	public String savepraiseNotationReplyReply(String replyReplyId, Student student) {
		String studentId = student.getStudentId();
		String sql = "select * from praise p where p.product_id = ? and p.student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString(0, replyReplyId);
		query.setString(1, studentId);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() > 0) {
			return "alread praise";
		}
		sql = "insert into praise(praise_id,product_id,student_id,type) values(?,?,?,?)";
		String praiseId = UUUID.getNextIntValue();
		session = baseDao.getSession();
		query = session.createSQLQuery(sql);
		query.setString(0, praiseId);
		query.setString(1, replyReplyId);
		query.setString(2, studentId);
		query.setInteger(3, 2);
		query.executeUpdate();
		NotationReplyReply notationReplyReply = (NotationReplyReply) session.get(
				NotationReplyReply.class, replyReplyId);
		notationReplyReply.setPraiseCount(notationReplyReply.getPraiseCount() + 1);
		session.saveOrUpdate(notationReplyReply);
		// 同步更新被有用用户的认可度
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		studentDao.updateStudentPraiseCount(notationReplyReply.getReplyStudent().getStudentId(),
				instance.findScoreByKey("praiseStu"));
		return "success";
	}

	public List<NotationReplyReply> findNotationReplyReply(String notationReplyId) {
		String hql = "from NotationReplyReply rr where rr.notationReplyId = '" + notationReplyId
				+ "'";
		List<NotationReplyReply> list = baseDao.findListByHql(hql);
		return list;

	}

	public String deletePraiseNotationReplyReply(String replyReplyId, Student student) {
		String studentId = student.getStudentId();
		String sql = "select * from praise p where p.product_id = ? and p.student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString(0, replyReplyId);
		query.setString(1, studentId);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() == 0) {
			return "alread praised or never praise";
		}
		sql = "delete from praise  where product_id =? and student_id = ? ";
		String praiseId = UUUID.getNextIntValue();
		session = baseDao.getSession();
		query = session.createSQLQuery(sql);
		query.setString(0, replyReplyId);
		query.setString(1, studentId);
		query.executeUpdate();
		NotationReplyReply notationReplyReply = (NotationReplyReply) session.get(
				NotationReplyReply.class, replyReplyId);
		notationReplyReply.setPraiseCount(notationReplyReply.getPraiseCount() - 1);
		session.saveOrUpdate(notationReplyReply);
		// 同步更新被有用用户的认可度
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		studentDao.updateStudentPraiseCount(notationReplyReply.getReplyStudent().getStudentId(),
				-instance.findScoreByKey("praiseStu"));
		// 作品热度更新
		NotationReply nr = (NotationReply) session.get(NotationReply.class, replyReplyId);
		TitleReply tr = (TitleReply) session.get(TitleReply.class, nr.getTitleReplyId());
		updateProductHot(tr.getProductId(), -instance.findScoreByKey("praise"));
		return "success";
	}

	public String savepraiseNotationReply(String replyId, Student student) {
		String studentId = student.getStudentId();
		String sql = "select * from praise p where p.product_id = ? and p.student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString(0, replyId);
		query.setString(1, studentId);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() > 0) {
			return "alread praise";
		}
		sql = "insert into praise(praise_id,product_id,student_id,type) values(?,?,?,?)";
		String praiseId = UUUID.getNextIntValue();
		session = baseDao.getSession();
		query = session.createSQLQuery(sql);
		query.setString(0, praiseId);
		query.setString(1, replyId);
		query.setString(2, studentId);
		query.setInteger(3, 1);
		query.executeUpdate();
		NotationReply notationReply = (NotationReply) session.get(NotationReply.class, replyId);
		notationReply.setPraiseCount(notationReply.getPraiseCount() + 1);
		session.saveOrUpdate(notationReply);
		// 同步更新被有用用户的认可度
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		studentDao.updateStudentPraiseCount(notationReply.getReplyStudent().getStudentId(),
				instance.findScoreByKey("userfulStu"));
		return "success";
	}

	public String deletePraiseNotationReply(String replyId, Student student) {
		String studentId = student.getStudentId();
		String sql = "select * from praise p where p.product_id = ? and p.student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString(0, replyId);
		query.setString(1, studentId);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() == 0) {
			return "alread praised or never praise";
		}
		sql = "delete from praise  where product_id =? and student_id = ? ";
		String praiseId = UUUID.getNextIntValue();
		session = baseDao.getSession();
		query = session.createSQLQuery(sql);
		query.setString(0, replyId);
		query.setString(1, studentId);
		query.executeUpdate();
		NotationReply notationReply = (NotationReply) session.get(NotationReply.class, replyId);
		notationReply.setPraiseCount(notationReply.getPraiseCount() - 1);
		session.saveOrUpdate(notationReply);
		// 同步更新被有用用户的认可度
		CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
		studentDao.updateStudentPraiseCount(notationReply.getReplyStudent().getStudentId(),
				-instance.findScoreByKey("userfulStu"));
		// 作品热度更新
		TitleReply tr = (TitleReply) session.get(TitleReply.class, notationReply.getTitleReplyId());
		updateProductHot(tr.getProductId(), -instance.findScoreByKey("userful"));
		return "success";
	}

	public String savePraiseProduct(String productId, Student student) {
		String studentId = student.getStudentId();
		String sql = "select * from praise p where p.product_id = ? and p.student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString(0, productId);
		query.setString(1, studentId);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() > 0) {
			return "alread praise";
		}
		sql = "insert into praise(praise_id,product_id,student_id,type) values(?,?,?,?)";
		String praiseId = UUUID.getNextIntValue();
		session = baseDao.getSession();
		query = session.createSQLQuery(sql);
		query.setString(0, praiseId);
		query.setString(1, productId);
		query.setString(2, studentId);
		query.setInteger(3, 0);
		query.executeUpdate();
		Product product = (Product) session.get(Product.class, productId);
		if (product == null) {
			ProductVersion pv = (ProductVersion) session.get(ProductVersion.class, productId);
			product = findProductBySeriesId(pv.getProductSeriesId());
			updateProductLastActTime(session, product);
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			updateProductHot(productId, instance.findScoreByKey("love"));
			// 同步更新被喜欢用户的认可度
			studentDao.updateStudentPraiseCount(pv.getStudent().getStudentId(),
					instance.findScoreByKey("loveStu"));
		} else {
			updateProductLastActTime(session, product);
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			updateProductHot(productId, instance.findScoreByKey("love"));
			// 同步更新被喜欢用户的认可度
			studentDao.updateStudentPraiseCount(product.getStudent().getStudentId(),
					instance.findScoreByKey("loveStu"));
		}
		return "success";
	}

	public String deletePraiseProduct(String productId, Student student) {
		String studentId = student.getStudentId();
		String sql = "select * from praise p where p.product_id = ? and p.student_id = ?";
		Session session = baseDao.getSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString(0, productId);
		query.setString(1, studentId);
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if (list.size() == 0) {
			return "alread praised or never praise";
		}
		sql = "delete from praise  where product_id =? and student_id = ? ";
		String praiseId = UUUID.getNextIntValue();
		session = baseDao.getSession();
		query = session.createSQLQuery(sql);
		query.setString(0, productId);
		query.setString(1, studentId);
		query.executeUpdate();
		Product product = (Product) session.get(Product.class, productId);
		if (product == null) {
			ProductVersion pv = (ProductVersion) session.get(ProductVersion.class, productId);
			product = findProductByProductSeriesId(pv.getProductSeriesId());
			pv.setPraiseCount(pv.getPraiseCount() - 1);
			updateProductLastActTime(session, product);
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			updateProductHot(productId, -instance.findScoreByKey("love"));
			// 同步更新被喜欢用户的认可度
			studentDao.updateStudentPraiseCount(pv.getStudent().getStudentId(),
					-instance.findScoreByKey("loveStu"));
		} else {
			product.setPraiseCount(product.getPraiseCount() - 1);
			updateProductLastActTime(session, product);
			// 作品热度更新
			CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
			updateProductHot(productId, -instance.findScoreByKey("love"));
			// 同步更新被喜欢用户的认可度
			studentDao.updateStudentPraiseCount(product.getStudent().getStudentId(),
					-instance.findScoreByKey("loveStu"));
		}
		return "success";
	}

	public String saveAdopt(String id, String type, Student student) {
		String studentId = student.getStudentId();
		Session session = baseDao.getSession();
		if (type.equals("1")) {
			NotationReply notationReply = (NotationReply) session.get(NotationReply.class, id);
			TitleReply tr = (TitleReply) session.get(TitleReply.class,
					notationReply.getTitleReplyId());
			String productId = tr.getProductId();
			Product product = (Product) session.get(Product.class, productId);
			if (product.getStudent().getStudentId().equals(studentId)) {// 当前登录用户是当前评论对应作品的用户
				notationReply.setIsadopt("Y");
				notationReply.setAdoptTime(DateUtil.formatDate());
				session.save(notationReply);
				// 更新此作品的共同参与者
				String productStudentSql = "insert into product_student(product_student_id,product_id,student_id,type) values(?,?,?,?)";
				SQLQuery sqlQuery = session.createSQLQuery(productStudentSql);
				sqlQuery.setString(0, UUUID.getNextIntValue());
				sqlQuery.setString(1, productId);
				sqlQuery.setString(2, notationReply.getReplyStudent().getStudentId());
				sqlQuery.setString(3, "C");
				sqlQuery.executeUpdate();
				// 更新此作品最后一次的交互时间
				updateProductLastActTime(session, product);
				// 同步更新被采纳用户的认可度
				CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
				studentDao.updateStudentPraiseCount(notationReply.getReplyStudent().getStudentId(),
						instance.findScoreByKey("adoptStu"));
				return "success";
			}

		} else {
			return "";
		}
		return "前台参数错误";
	}

	public String deleteAdopt(String id, String type, HttpServletRequest res) {
		String studentId = res.getSession().getAttribute("student") != null ? ((Student) res
				.getSession().getAttribute("student")).getStudentId() : "2011228122";
		Session session = baseDao.getSession();
		if (type.equals("1")) {
			NotationReply notationReply = (NotationReply) session.get(NotationReply.class, id);
			TitleReply tr = (TitleReply) session.get(TitleReply.class,
					notationReply.getTitleReplyId());
			String productId = tr.getProductId();
			Product product = (Product) session.get(Product.class, productId);
			if (product.getStudent().getStudentId().equals(studentId)) {// 当前登录用户是当前评论对应作品的用户
				notationReply.setIsadopt("N");
				session.save(notationReply);
				// 更新此作品的共同参与者
				String productStudentSql = "delete from product_student where product_id = ? and student_id = ? and type = ?";
				SQLQuery sqlQuery = session.createSQLQuery(productStudentSql);
				sqlQuery.setString(0, productId);
				sqlQuery.setString(1, studentId);
				sqlQuery.setString(2, "C");
				sqlQuery.executeUpdate();
				// 同步更新被采纳用户的认可度
				CommonDataUtil instance = CommonDataUtil.getInstance(baseDao);
				studentDao.updateStudentPraiseCount(notationReply.getReplyStudent().getStudentId(),
						-instance.findScoreByKey("adoptStu"));
				return "success";
			}
		} else {
			return "";
		}
		return "前台参数错误";
	}

	public void deleteReplyReply(NotationReplyReply nrr) {
		baseDao.deleteEntity(nrr);
	}

	public void deleteReply(NotationReply nr) {
		baseDao.deleteEntity(nr);
	}

	public List<NotationReply> findNotationReply(String replyId) {
		String hql = "from NotationReply nr where nr.titleReplyId = ? order by create_time desc";
		Session session = baseDao.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, replyId);
		List list = query.list();
		return list;
	}

	public Object findProductVersion(String productSeriesId, Integer version) {
		String hql = ("from ProductVersion pv where pv.productSeriesId  = ? and pv.version = ?");
		Session session = baseDao.getSession();
		Query query = session.createQuery(hql);
		query.setString(0, productSeriesId);
		query.setInteger(1, version);
		List list = query.list();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Product findProduct(String productId) {
		Product product = (Product) baseDao.findEntity(Product.class, productId);
		return product;
	}

	public ProductVersion findProductVersion(String productId) {
		ProductVersion product = (ProductVersion) baseDao.findEntity(ProductVersion.class,
				productId);
		return product;
	}

	/**
	 * 查询点的参与人数
	 * 
	 * @param titleReplyId
	 * @param productId
	 * @return
	 */
	public Map findHotParamsByTitleReply(String titleReplyId, String productId) {
		String sql = "SELECT" + "	count(DISTINCT tr.reply_student_id) stuCount" + " FROM"
				+ "	product p" + " JOIN title_reply tr ON (p.product_id = tr.product_id)"
				+ " WHERE" + "	p.product_id = '" + productId + "' and tr.title_reply_id = '"
				+ titleReplyId + "'";
		List list = baseDao.findListBySql(sql, new String[] { "stuCount" });
		return (Map) list.get(0);
	}

	public void deleteTitleReply(TitleReply tr) {
		baseDao.deleteEntity(tr);
	}

	/**
	 * 更新此作品的最后一次交互时间
	 * 
	 * @param session
	 * @param product
	 */
	public void updateProductLastActTime(Session session, Product product) {
		product.setLastActTime(DateUtil.formatDate());
		session.update(product);
	}

	/**
	 * 保存旧版本作品到product_version中
	 * 
	 * @param pro
	 */
	public void saveProductVersion(String productSeriesId) {
		String sql = "insert into product_version (select * from product p where p.product_series_id = '"
				+ productSeriesId + "')";
		baseDao.executeUpdateBySql(sql);
	}

	/**
	 * 删除旧版本作品从product中
	 * 
	 * @param productId
	 */
	public void deleteProductByProductId(String productSeriesId) {
		String sql = "delete from product where product_series_id = '" + productSeriesId + "'";
		baseDao.executeUpdateBySql(sql);
	}

	/**
	 * 保存新版本作品
	 * 
	 * @param pro
	 */
	public void saveNewVersionProduct(Product pro) {
		baseDao.saveEntity(pro);
	}

	/**
	 * 安装作品系列号查询作品从product中
	 * 
	 * @param productSeriesId
	 * @param request
	 * @return
	 */
	public Object findProductByProductId(String productId, HttpServletRequest request) {
		Object entity = baseDao.findEntity(Product.class, productId);
		return entity;
	}

	public Product findProductBySeriesId(String productSeriesId) {
		String sql = "select * from product p where p.product_series_id = '" + productSeriesId
				+ "'";
		List list = baseDao.findListEntityBySql(sql, new String[] {}, Product.class);
		return (Product) list.get(0);
	}

	public void updatePointHot(String pointId) {
		String sql = "update reply_point set hot = hot+1 where point_id='" + pointId + "'";
		baseDao.executeUpdateBySql(sql);
	}

	public void deleteProduct(Product product) {
		baseDao.deleteEntity(product);
	}

	/**
	 * 更新作品热度
	 * 
	 * @param toProductId
	 *            作品id
	 * @param findScoreByKey
	 *            要增加的热度参数
	 */
	public void updateProductHot(String productId, Integer score) {
		String sql = "update product set score = score+" + score + " where product_id = '"
				+ productId + "'";
		baseDao.executeUpdateBySql(sql);
	}

	public void updateProductVersionHot(String productId, Integer score) {
		String sql = "update product_version set score = score+" + score + " where product_id = '"
				+ productId + "'";
		baseDao.executeUpdateBySql(sql);
	}

	public Product findProductByProductSeriesId(String productSeriesId) {
		String hql = "from Product p where p.productSeriesId = ?";
		List list = baseDao.findListByHql(hql, new String[] { productSeriesId });
		return (Product) list.get(0);
	}

	public ProductVersion findProductVersionByProductSeriesId(String productSeriesId) {
		String hql = "from ProductVersion pv where pv.productSeriesId = ? "
				+ " and pv.version = (select max(pv1.version) from ProductVersion pv1 where pv1.productSeriesId = '"
				+ productSeriesId + "')";
		List list = baseDao.findListByHql(hql, new String[] { productSeriesId });
		if (list.size() > 0) {
			return (ProductVersion) list.get(0);
		}
		return null;
	}

	public void saveProductFromProductVersion(String productId) {
		String sql = "insert into product (select * from product_version p where p.product_id = '"
				+ productId + "')";
		baseDao.executeUpdateBySql(sql);

	}

	/**
	 * 删除此系列某个版本号之后的所有作品
	 * 
	 * @param productSeriesId
	 * @param version
	 */
	public void deleteProductByVersionAfter(String productSeriesId, Integer version) {
		String sql = "delete from product_version where product_series_id =? and version >= ?";
		baseDao.deleteBySql(sql, new Object[] { productSeriesId, version });
		String sql2 = "delete from product where product_series_id = ?";
		baseDao.deleteBySql(sql2, new Object[] { productSeriesId });

	}

	/**
	 * 迁移作品从历史版本到最新版本
	 * 
	 * @param productSeriesId
	 * @param version
	 */
	public void saveProductFromProductVersion(String productSeriesId, Integer version) {
		String sql = "insert into product (select * from product_version p where p.product_series_id = '"
				+ productSeriesId + "' and p.version = " + version + ")";
		baseDao.executeUpdateBySql(sql);

	}

	/**
	 * 删除某个历史版本的作品
	 * 
	 * @param productDisc
	 * @param i
	 */
	public void deleteProductBySeriesIdAndVersion(String productSeriesId, Integer version) {
		String sql = "delete from product_version where product_series_id = ? and version =?";
		baseDao.deleteBySql(sql, new Object[] { productSeriesId, version });
	}

	public void updateVersionCount(String productSeriesId, Integer versionCount) {
		String sql = "update product_version set version_count = '" + versionCount
				+ "' where product_series_id = '" + productSeriesId + "'";
		baseDao.executeUpdateBySql(sql);
		String sql2 = "update product set version_count = '" + versionCount
				+ "' where product_series_id = '" + productSeriesId + "'";
		baseDao.executeUpdateBySql(sql2);

	}

}
