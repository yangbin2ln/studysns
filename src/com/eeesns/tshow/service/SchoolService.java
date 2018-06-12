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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import sparknet.starshine.common.util.StringUtil;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.dao.SchoolDao;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.School;
import com.eeesns.tshow.entity.Student;

@Service
public class SchoolService {
	@Resource
	private BaseDao baseDao;
	@Resource
	private SchoolDao schoolDao;
	@Resource
	private LabelService labelService;

	/**
	 * 此处配置二级缓存 查询所有学校
	 * 
	 * @return
	 */
	public EditJson findSchools() {
		EditJson editJson = null;
		Session session = baseDao.getSession();
		try {
			editJson = new EditJson();
			String hql = "from Province";
			List provinceList = session.createQuery(hql).setCacheable(true).list();
			JsonConfig jsonConfig = new JsonConfig();
			// 不需要查询出学校标签
			jsonConfig.setExcludes(new String[] { "schoolLabels", "labels" });
			JSONArray jsonObject = (JSONArray) JSONSerializer.toJSON(provinceList, jsonConfig);
			Map m = editJson.getMap();
			m.put("provinceList", jsonObject.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("查询所有学校异常");
		}
		return editJson;
	}

	public EditJson findSchoolById(String schoolId) {
		EditJson editJson = new EditJson();
		try {
			Object obj = baseDao.findEntity(School.class, schoolId);
			editJson.getMap().put("school", obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("查询单个id为" + schoolId + "学校异常");
		}
		return editJson;
	}

	/**
	 * 更新学校的标签数量
	 * 
	 * @param schoolId
	 */
	public void updateLabelCount(String schoolId) {
		schoolDao.updateLabelCount(schoolId);
	}

	/**
	 * 更新学校标签的作品数量
	 * 
	 * @param schoolId
	 * @param labelId
	 */
	public void updateLabelCountBySchoolLabel(String schoolId, String labelId) {
		schoolDao.updateLabelCountBySchoolLabel(schoolId, labelId);
	}

	public School findSchoolBySchoolId(String schoolId) {
		School school = schoolDao.findSchoolBySchoolId(schoolId);
		return school;
	}

	public EditJson findSchoolsByKey(String key) {
		EditJson editJson = new EditJson();
		List schools = schoolDao.findSchoolsByKey(key);
		editJson.getMap().put("schools", schools);
		return editJson;
	}

	public Object findInitHtml(String schoolId, Page page, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		Map map = new HashMap();
		// 查询用户
		Page studentsPage = labelService.findStudentByLabelIdorSchoolId(null, null, schoolId,
				"score", "all", page, request);
		List<Student> students = studentsPage.getResult();
		Long studentsCount = studentsPage.getTotalCount();
		// 查询作品
		Page productsPage = labelService.findProductByLabelIdorSchoolId(null, null, schoolId,
				"score", page, request);
		List products = productsPage.getResult();
		Long productsCount = productsPage.getTotalCount();
		// 查询学校对应的标签
		List labels = labelService.findLabelsBySchoolId(schoolId, page);
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
		map.put("students", studentsJSON);
		map.put("studentsCount", studentsCount);
		map.put("productsCount", productsCount);
		map.put("products", productsJSON);
		map.put("labels", labels);
		return map;
	}

	public Object findStudents(String schoolId, String labelId, String pointId, String stType,
			String filter, Page page, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		Map map = new HashMap();
		// 查询达人
		Page masterPage = labelService.findStudentByLabelIdorSchoolId(labelId, pointId, schoolId,
				stType, filter, page, request);
		List<Student> students = masterPage.getResult();
		Long studentsCount = masterPage.getTotalCount();
		for (Student stu : students) {
			// 统计职业方向下的作品
			if (StringUtil.isEmpty(labelId) && StringUtil.isEmpty(pointId)) {
				String stuId = stu.getStudentId();
				String sql = "select sum(sl.product_count) jobProductCount from student_label sl join label la on(sl.label_id = la.label_id) "
						+ " join student st on(st.student_id = sl.student_id) where st.job_id = la.pid";
				Integer jobProductCount = baseDao.findIntBySql(sql);
				stu.setJobProductCount(jobProductCount);

			}
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
		map.put("students", studentsJSON);
		map.put("studentsCount", studentsCount);
		return map;
	}

	public Object findProducts(String schoolId, String labelId, String pointId, String stType,
			String prType, Page page, HttpServletRequest request) {
		Map map = new HashMap();
		// 查询作品
		Page productsPage = labelService.findProductByLabelIdorSchoolId(null, null, schoolId,
				prType, page, request);
		List<Product> products = productsPage.getResult();
		Long productsCount = productsPage.getTotalCount();
		JsonConfig jsonConfig2 = new JsonConfig();
		jsonConfig2.setExcludes(new String[] { "labels", "schoolLabels", "titleReplys", "interest",
				"studentLabels", "studentSuggestionLabels", "studentCollectLabels" });
		JSONArray jsonObject2 = JSONArray.fromObject(products, jsonConfig2);
		String productsJSON = jsonObject2.toString();
		map.put("products", productsJSON);
		map.put("productsCount", productsCount);
		return map;
	}

	/**
	 * 学校标签主页数据查询(达人、普通用户、作品)
	 * 
	 * @param labelId
	 * @param pointId
	 * @param schoolId
	 * @param page
	 * @param request
	 * @return
	 */
	public Map findSchoolLabelInitHtml(String labelId, String pointId, String schoolId, Page page,
			HttpServletRequest request) {
		String orderBy1 = "time";
		String orderBy2 = "score";
		String filter1 = "master";
		String filter2 = "general";
		Student student = (Student) request.getSession().getAttribute("student");
		Map map = new HashMap();
		// 查询内容标签
		List points = labelService.findPointByLabelIdorSchoolId(labelId, pointId, schoolId, page,
				request);
		// 查询达人
		Page masterPage = labelService.findStudentByLabelIdorSchoolId(labelId, pointId, schoolId,
				orderBy2, filter1, page, request);
		List<Student> students = masterPage.getResult();
		Long studentsCount = masterPage.getTotalCount();
		// 查询普通用户
		Page generalPage = labelService.findStudentByLabelIdorSchoolId(labelId, pointId, schoolId,
				orderBy1, filter2, page, request);
		List<Student> generals = generalPage.getResult();
		Long generalsCount = generalPage.getTotalCount();
		// 查询作品
		Page productsPage = labelService.findProductByLabelIdorSchoolId(labelId, pointId, schoolId,
				orderBy1, page, request);
		List products = productsPage.getResult();
		Long productsCount = productsPage.getTotalCount();
		/*
		 * //查询标签对应的学校 page.setPageSize(5); List schools =
		 * labelService.findSchoolsByLabelIdAndPointId(labelId,pointId,page);
		 */
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
		// 普通用户属性过滤
		JsonConfig jsonConfig3 = new JsonConfig();
		jsonConfig3.setExcludes(new String[] { "labels", "schoolLabels", "interest",
				"studentSuggestionLabels", "studentCollectLabels" });
		JSONArray jsonObject3 = JSONArray.fromObject(generals, jsonConfig3);
		String generalsJSON = jsonObject3.toString();
		// 作品属性过滤
		JsonConfig jsonConfig2 = new JsonConfig();
		jsonConfig2.setExcludes(new String[] { "labels", "schoolLabels", "titleReplys", "interest",
				"studentLabels", "studentSuggestionLabels", "studentCollectLabels" });
		JSONArray jsonObject2 = JSONArray.fromObject(products, jsonConfig2);
		String productsJSON = jsonObject2.toString();
		map.put("points", points);
		map.put("students", studentsJSON);
		map.put("generals", generalsJSON);
		map.put("generalsCount", generalsCount);
		map.put("studentsCount", studentsCount);
		map.put("productsCount", productsCount);
		map.put("products", productsJSON);
		/* map.put("schools", schools); */
		return map;
	}

	public Object findSchoolsByProvince(String province, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		List list = schoolDao.findSchoolsByProvince(province, student.getStudentId());
		editJson.setBean(list);
		return editJson;
	}
}
