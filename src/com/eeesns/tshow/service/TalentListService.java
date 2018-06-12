package com.eeesns.tshow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.drools.core.util.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.common.json.TshowMap;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Student;

@Service
public class TalentListService {

	@Resource
	private BaseDao baseDao;

	/**
	 * @param page2
	 *            分页信息
	 * @param schoolId
	 *            学校id
	 * @param labelId2
	 *            标签id 注意问题：
	 *            对于“查询兴趣中的每个标签的点赞、收藏、投票数量”，如果存在没有作品的标签，那么通过join是拿不到相应的那个标签的
	 *            ，为什么不用left join 的原因是，不能确定此标签是兴趣标签还是专业标签，数据库设计上目前将俩个分开保存，
	 *            前台处理时可以通过判断兴趣标签集合和专业标签是否包含查出来的标签 若没有包含，则对应作品数肯定为零
	 * 
	 * @return
	 */
	public EditJson findStudent(Page page2, String schoolId, String labelId2) {
		Session session = baseDao.getSessionFactory().getCurrentSession();
		EditJson editJson = new EditJson();
		//查询学校
		Object school = baseDao.findObjByHql("from School s where s.schoolId = '"+schoolId+"'");
		editJson.getMap().put("school", school);
		// 分页查询用户
		
		List<Student> result = null;
		try {
			String sql = "select DISTINCT s.* from student s left join student_interest si on(s.student_id=si.student_id)   where 1=1 " ;
			if(!StringUtils.isEmpty(schoolId)){
				sql+=
						" and s.school_id='"
								+ schoolId+"'";
			}
			if(!StringUtils.isEmpty(labelId2)){
				sql+= " and (s.job_id='"
						+ labelId2
						+ "' or (si.interest_id='"
						+ labelId2
						+ "'))  ";
			}
				sql+= " order by s.create_time";
			// SQLQuery sqlQuery =
			// session.createSQLQuery(sql).addEntity(Student.class);
			Page page = baseDao.findPageEntityBySql(sql, page2.getPageNo(),
					page2.getPageSize(), true, Student.class);
			result = page.getResult();
			addStudentParam(session, result);
			editJson.setBean(result);
			editJson.setPage(page);
			editJson.setSuccess(true);
		} catch (Exception e) {
			editJson.setSuccess(false);
			e.printStackTrace();
			throw new RuntimeException("达人榜查询异常");
		}
		return editJson;

	}

	/**
	 * 组装学生实体的一些参数信息
	 * @param session
	 * @param result
	 */
	private void addStudentParam(Session session, List<Student> result) {
		for (Student student : result) {
			//查询总的作品数量
			String productCountSql = "select count(*) from product p where p.student_id = '"
					+ student.getStudentId() + "'";
			int productCount = baseDao.findIntBySql(productCountSql);
			
			// 查询粉丝数量
			String attentionCountSql = "select count(*) from attention a where a.to_student_id = '"
					+ student.getStudentId() + "'";
			int attentionCount = baseDao.findIntBySql(attentionCountSql);
			//浏览量
			String viewCountSql = "select count(*) from view v where v.to_student_id = '"
					+ student.getStudentId() + "'";
			int viewCount = baseDao.findIntBySql(viewCountSql);
			student.setAttentionCount(attentionCount);
			student.setViewCount(viewCount);
			student.setProductCount(productCount);
			// 按标签分组查询对应作品的，作品和、点赞和、收藏和、投票和 问题1：没有作品的标签是拉不出来的
			SQLQuery query = session
					.createSQLQuery("SELECT p.label_id labelId,(select count(*) from product pp where pp.student_id='"
							+ student.getStudentId()
							+ "' and pp.label_id = p.label_id) labelProductCount,	sum(p.praise_count) praisecount,sum(p.collection_count) collectionCount,sum(p.vote_count) voteCount"
							+ " FROM student st JOIN student_interest si ON (	st.student_id = si.student_id ) "
							+ " JOIN product p ON ((si.interest_id = p.label_id or st.job_id = p.label_id) and p.student_id=st.student_id) where p.student_id='"
							+ student.getStudentId()
							+ "' GROUP BY	p.label_id");
			List list = query.setResultTransformer(
					Transformers.ALIAS_TO_ENTITY_MAP).list();
			//student.setIntePrauseMap(list);
			System.out.println(JSONObject.fromObject(student));
			Map map = new HashMap();
			// 查出每个标签的俩个代表作
		/*	for (int i = 0; i < student.getIntePrauseMap().size(); i++) {
				String labelId = (String) student.getIntePrauseMap().get(i)
						.get("labelId");
				// TODO 待加排序
				query = session
						.createSQLQuery("select p.product_id productId, p.product_name productName, p.product_disc productDisc, p.content content,"
								+ "p.type type,p.create_time createTime, p.score score, p.view_count vieCount, p.praise_count praiseCount, p.vote_count voteCount, "
								+ "p.collection_count collectionCount, p.version version, p.original original from product p where p.label_id='"
								+ labelId
								+ "' and student_id='"
								+ student.getStudentId() + "' limit 2");
				list = query.setResultTransformer(
						Transformers.ALIAS_TO_ENTITY_MAP).list();
				map.put(labelId, list);
			}*/
			//student.setProductLabels(map);
		}
	}

}
