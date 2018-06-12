package com.eeesns.tshow.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Student;

@Component
public class ContendDao {
	@Resource
	BaseDao baseDao;

	public List findSchoolLabelUnlock() {
		String sql = "SELECT\n"
				+ "	s.school_id schoolId,s.name schoolName,s.badge badge,sl.unlock_time unlockTime,sl.count count,l.label_id labelId,l.label_name labelName\n"
				+ "FROM\n" + "	school s\n"
				+ "JOIN school_label sl ON (s.school_id = sl.school_id)\n"
				+ "JOIN label l ON (sl.label_id = l.label_id)\n" + "WHERE\n" + "	sl.state = 'Y'\n"
				+ "AND sl.unlock_time > DATE_SUB(NOW(), INTERVAL 1 MONTH)";
		List list = baseDao.findListBySql(sql, new String[] { "schoolId", "schoolName", "badge",
				"labelId", "labelName", "unlockTime", "count" });
		return list;
	}

	public List findAllProducts(Page page, String type, HttpServletRequest request) {
		if ("time".equals(type)) {
			type = " order by p.createTime desc";
		} else if ("score".equals(type)) {
			type = " order by p.score desc";
		} else {
			type = " order by p.createTime desc";
		}
		Page p = baseDao.findPageByHql("from Product p where p.student.studentId is not null "
				+ type, page.getPageNo(), page.getPageSize(), true);
		if (request.getSession().getAttribute("student") != null) {
			String studentId = ((Student) request.getSession().getAttribute("student"))
					.getStudentId();
			List<Product> list = p.getResult();
			for (Product pro : list) {
				String productId = pro.getProductId();
				List praises = baseDao.findListByHql("from Praise p where p.productId = '"
						+ productId + "' and p.studentId = '" + studentId + "'");
				pro.setPraises(praises);
				/*
				 * List votes =
				 * baseDao.findListByHql("from Vote v where v.productId = '" +
				 * productId + "' and v.studentId = '" + studentId + "'");
				 * pro.setVotes(votes); List collections =
				 * baseDao.findListByHql(
				 * "from Collection c where c.productId = '" + productId +
				 * "' and c.studentId = '" + studentId + "'");
				 * pro.setCollections(collections); List reports =
				 * baseDao.findListByHql("from Report r where r.productId = '" +
				 * productId + "' and r.studentId = '" + studentId + "'");
				 * pro.setReports(reports);
				 * 
				 * //查看是否被当前用户关注 if(pro.getStudent()==null){//学生为空的原因可能是(用户被删除)
				 * list.remove(pro); continue; }
				 * 
				 * String toStudentId = pro.getStudent().getStudentId(); String
				 * sql = "select * from attention a where a.student_id = '" +
				 * studentId + "' and a.to_student_id = '" + toStudentId + "'";
				 * List list2 = baseDao.findListBySql(sql, new String[] {}); if
				 * (list2.size() > 0) {// 此用户被当前用户关注
				 * pro.getStudent().setIsAttention("Y"); } else {
				 * pro.getStudent().setIsAttention("N"); }
				 */
			}
		}

		return p.getResult();
	}

}
