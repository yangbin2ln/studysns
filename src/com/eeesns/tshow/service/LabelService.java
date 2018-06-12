package com.eeesns.tshow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.LabelDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Label;
import com.eeesns.tshow.entity.Point;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Student;

@Service
public class LabelService {
	@Resource
	private LabelDao labelDao;
	@Resource
	private BaseDao baseDao;

	public Object findLabels(HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Object object = labelDao.findLabels(request);
		editJson.setBean(object);
		return editJson;
	}

	/**
	 * 更新标签的用户数量
	 * 
	 * @param labelId
	 */
	public void updateStudentCount(String labelId) {
		labelDao.updateStudentCount(labelId);
	}

	/**
	 * 更新标签的作品数量
	 * 
	 * @param labelId
	 */
	public void updateProductCount(String labelId) {
		labelDao.updateProductCount(labelId);
	}

	/**
	 * 初始化label jsp
	 * 
	 * @param schoolId
	 * @param labelId
	 * @param schoolId2
	 * @param request
	 */
	public Map findInitHtml(String labelId, String pointId, String schoolId, Page page,
			HttpServletRequest request) {
		String orderBy1 = "score";
		String orderBy2 = "score";
		String filter1 = "master";
		String filter2 = "general";
		Student student = (Student) request.getSession().getAttribute("student");
		Map map = new HashMap();
		// 查询内容标签
		List points = findPointByLabelIdorSchoolId(labelId, pointId, schoolId, page, request);
		// 查询达人
		Page masterPage = findStudentByLabelIdorSchoolId(labelId, pointId, schoolId, orderBy1,
				filter1, page, request);
		List<Student> students = masterPage.getResult();
		Long studentsCount = masterPage.getTotalCount();
		// 查询作品
		Page productsPage = findProductByLabelIdorSchoolId(labelId, pointId, schoolId, orderBy1,
				page, request);
		List products = productsPage.getResult();
		Long productsCount = productsPage.getTotalCount();
		// 查询标签对应的学校
		page.setPageSize(5);
		List schools = findSchoolsByLabelIdAndPointId(labelId, pointId, page);
		for (Student stu : students) {
			if (student != null) {
				// 查看是否被当前用户关注
				String toStudentId = stu.getStudentId();
				String sql = "select * from attention a where a.student_id = '"
						+ student.getStudentId() + "' and a.to_student_id = '" + toStudentId
						+ "' limit 1";
				List list2 = baseDao.findListBySql(sql, new String[] {});
				if (list2.size() > 0) {// 此用户被当前用户关注
					stu.setIsAttention("Y");
				} else {
					stu.setIsAttention("N");
				}
			}
		}

		// 过滤无需加载的属性
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "labels", "schoolLabels", "interest",
				"studentSuggestionLabels", "studentCollectLabels" });
		JSONArray jsonObject = JSONArray.fromObject(students, jsonConfig);
		String studentsJSON = jsonObject.toString();
		JsonConfig jsonConfig2 = new JsonConfig();
		jsonConfig2.setExcludes(new String[] { "labels", "schoolLabels", "titleReplys", "interest",
				"studentLabels", "studentSuggestionLabels", "studentCollectLabels" });
		JSONArray jsonObject2 = JSONArray.fromObject(products, jsonConfig2);
		String productsJSON = jsonObject2.toString();
		map.put("points", points);
		map.put("students", studentsJSON);
		map.put("products", productsJSON);
		map.put("productsCount", productsCount);
		map.put("schools", schools);
		return map;
	}

	public Page findProductByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			String type, Page page, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		String studentId;
		if (student == null) {
			studentId = "";
		} else {
			studentId = student.getStudentId();
		}

		Page page2 = labelDao
				.findProductByLabelIdorSchoolId(labelId, pointId, schoolId, type, page);
		List<Product> products = page2.getResult();
		/* start 当前用户对作品的操作状态 */
		for (Product pro : products) {
			String productId = pro.getProductId();
			List praises = baseDao.findListByHql("from Praise p where p.productId = '" + productId
					+ "' and p.studentId = '" + studentId + "'");
			pro.setPraises(praises);
			/*
			 * List votes =
			 * baseDao.findListByHql("from Vote v where v.productId = '" +
			 * productId + "' and v.studentId = '" + studentId + "'");
			 * pro.setVotes(votes); List collections =
			 * baseDao.findListByHql("from Collection c where c.productId = '" +
			 * productId + "' and c.studentId = '" + studentId + "'");
			 * pro.setCollections(collections); List reports =
			 * baseDao.findListByHql("from Report r where r.productId = '" +
			 * productId + "' and r.studentId = '" + studentId + "'");
			 * pro.setReports(reports);
			 */
			/*
			 * //查看是否被当前用户关注 if(pro.getStudent()==null){//学生为空的原因可能是(用户被删除)
			 * list.remove(pro); continue; }
			 */
			/*
			 * String toStudentId = pro.getStudent().getStudentId(); String sql
			 * = "select * from attention a where a.student_id = '" + studentId
			 * + "' and a.to_student_id = '" + toStudentId + "'"; List list2 =
			 * baseDao.findListBySql(sql, new String[] {}); if (list2.size() >
			 * 0) {// 此用户被当前用户关注 pro.getStudent().setIsAttention("Y"); } else {
			 * pro.getStudent().setIsAttention("N"); }
			 */
		}
		/* end 当前用户对作品的操作状态 */

		return page2;

	}

	public Page findStudentByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			String orderBy, String filter, Page page, HttpServletRequest request) {
		Page page2 = labelDao.findStudentByLabelIdorSchoolId(labelId, pointId, schoolId, orderBy,
				filter, page);
		return page2;
	}

	public List findPointByLabelIdorSchoolId(String labelId, String pointId, String schoolId,
			Page page, HttpServletRequest request) {
		List list = labelDao.findPointByLabelIdorSchoolId(labelId, pointId, schoolId, page);
		return list;
	}

	public void updatePointHot(String pointId) {
		String sql = "update point set hot = hot+1 where point_id = ?";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		query.setString(0, pointId);
		query.executeUpdate();
	}

	public Label findLabelByLabelId(String labelId) {
		Label label = labelDao.findLabelByLabelId(labelId);
		return label;
	}

	public Point findPointByPointId(String pointId) {
		Point point = labelDao.findPointByPointId(pointId);
		return point;
	}

	public List findSchoolsByLabelIdAndPointId(String labelId, String pointId, Page page) {
		List schools = labelDao.findSchoolsByLabelIdAndPointId(labelId, pointId, page);
		return schools;
	}

	public List findLabelsBySchoolId(String schoolId, Page page) {
		List<Map> labels = labelDao.findLabelsBySchoolId(schoolId, page);
		// 取出标签最热的四个作品
		for (Map m : labels) {
			String labelId = (String) m.get("labelId");
			List images = findProductsBySchoolIdAndLabelId(schoolId, labelId);
			m.put("images", images);
		}
		return labels;
	}

	/**
	 * 查出某个学校标签的四个代表作图片(前台是小图展示，后期完成图片压缩功能后修改)TODO
	 * 
	 * @param schoolId
	 * @param labelId
	 * @return
	 */
	private List findProductsBySchoolIdAndLabelId(String schoolId, String labelId) {
		List images = labelDao.findProductsBySchoolIdAndLabelId(schoolId, labelId);
		return images;
	}

	public List findlabelByLabelIdAndSchoolId(String schoolId, String labelId) {
		List label = labelDao.findlabelByLabelIdAndSchoolId(schoolId, labelId);
		return label;
	}

	public Object findLabelsF() {
		EditJson editJson = new EditJson();
		List list = labelDao.findLabelsF();
		editJson.setBean(list);
		return editJson;
	}

	public Object findHotPointsByLabelId(String labelId, Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		List points = findPointByLabelIdorSchoolId(labelId, null, null, page, request);
		editJson.setBean(points);
		return editJson;
	}

	public Object findHotReplyPointsByLabelId(String labelId, Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		List points = labelDao.findHotReplyPointsByLabelId(labelId, page, request);
		editJson.setBean(points);
		return editJson;
	}
}
