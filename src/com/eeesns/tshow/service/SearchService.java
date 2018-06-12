package com.eeesns.tshow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.dao.SearchDao;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.util.ZhengzeUtil;

@Service
public class SearchService {
	@Resource
	SearchDao searchDao;

	public Object findByKey(String key,Page page, HttpServletRequest request) {
		Map<String, Map<String, List>> m = new HashMap<String, Map<String, List>>();
		m.put("schools", new HashMap());
		Map m1 = new HashMap();
		m.get("schools").put("s", new ArrayList());
		m.get("schools").put("sl", new ArrayList());
		m.get("schools").put("sp", new ArrayList());
		m.put("labels", new HashMap());
		m.get("labels").put("l", new ArrayList());
		m.get("labels").put("ls", new ArrayList());
		m.get("labels").put("p", new ArrayList());
		m.put("students", new HashMap());
		m.get("students").put("st", new ArrayList());
		/* start 精确匹配 */
		/*
		 * Map school = findSchoolByKey(key); Map label = findLabelByKey(key);
		 */
		Map student = null;
		List students = new ArrayList();
		// 检索用户
		boolean boo = ZhengzeUtil.isNumber(key);
		if (boo) {
			student = findStudentsByKeyId(key);
		} else {
			students = findStudentsByKeyName(key,page);
		}
		/* end 精确匹配 */

		// 模糊匹配学校(分页)
		List schools = findSchoolsByKey(key,page);
		// 模糊匹配二级标签（分页）
		List labels = findLabelsByKey(key,page);
		// 模糊匹配内容标签
		List points = findPointsByKey(key,page);
		m.get("labels").get("p").addAll(points);
		if (schools.size() > 0) {// 找到学校
			m.get("schools").get("s").addAll(schools);
			Map school = (Map) schools.get(0);
			// 根据学校去找对应的标签
			List list = findSchoolLabelBySchoolId((String) school.get("schoolId"),page);
			m.get("labels").get("l").addAll(labels);
			m.get("labels").get("ls").addAll(list);
			// 根据学校去找学生
			List studentList = findStudentsBySchoolId((String) school.get("schoolId"),page);
			if (student != null) {
				m.get("students").get("st").add(student);
			}
			m.get("students").get("st").addAll(students);
			m.get("students").get("st").addAll(studentList);

		} else if (labels.size() > 0) {// 找到标签
			Map label = (Map) labels.get(0);
			// if (label.get("pid") != null) {// 是子标签
			m.get("labels").get("l").addAll(labels);
			// 找出玩此标签的学校
			List schoolList = findSchoolsByLabelId((String) label.get("labelId"),page);
			m.get("schools").get("s").addAll(schools);
			m.get("schools").get("sl").addAll(schoolList);
			// 找出玩标签的达人
			List studentList = findStudentsByLabelId((String) label.get("labelId"),page);
			m.get("students").get("st").addAll(studentList);
			// }

		} else if (student != null || students.size() != 0) {// 找到学生
			if (student != null) {
				m.get("students").get("st").add(student);
			}
			if (students.size() != 0) {
				m.get("students").get("st").addAll(students);
			}
			// 根据用户查询对应的学校
			if (student != null) {
				Map sc = findSchoolByStudentId((String) student.get("studentId"));
				m.get("schools").get("s").addAll(schools);
				m.get("schools").get("s").add(sc);
				// 根据学生id去找对应的标签
				List labelsList = findLabelsByStudentId((String) student.get("studentId"),page);
				m.get("labels").get("l").addAll(labels);
				m.get("labels").get("l").addAll(labelsList);
			} else {
				Map sc = findSchoolByStudentId((String) ((Map) students.get(0)).get("studentId"));
				m.get("schools").get("s").addAll(schools);
				m.get("schools").get("s").add(sc);
				// 根据学生id去找对应的标签
				List labelsList = findLabelsByStudentId((String) ((Map) students.get(0))
						.get("studentId"),page);
				m.get("labels").get("l").addAll(labels);
				m.get("labels").get("l").addAll(labelsList);
			}
		}
		// 模糊查询父一级标签
		List<Map> pidList = findPLabelByKey(key,page);
		String pids = "''";
		for (Map mm : pidList) {
			String pid = (String) mm.get("labelId");
			pids += ",'" + pid + "'";
		}
		if (pidList.size() > 0) {
			List childLabelList = findLabelsByPids(pids,page);
			if (childLabelList.size() > 0) {
				m.get("labels").get("l").addAll(childLabelList);
				// 找出玩此标签的学校
				List schoolList = findSchoolsByLabelId((String) ((Map) childLabelList.get(0))
						.get("labelId"),page);
				m.get("schools").get("sl").addAll(schoolList);
			}
		}
		// 存在内容标签，则关联出学校和学生
		if (points.size() > 0) {
			String pointId = (String) ((Map) points.get(0)).get("pointId");
			List schoolsPoint = findSchoolsByPointId(pointId,page);
			m.get("schools").get("sp").addAll(schoolsPoint);
			List studentsPoint = findStudentsByPointId(pointId,page);
			m.get("students").get("st").addAll(studentsPoint);
		}

		// 判断用户是否被关注
		isAttention(m.get("students").get("st"), request);
		return m;
	}

	private List findStudentsByPointId(String pointId,Page page) {
		List list = searchDao.findStudentsByPointId(pointId,page);
		return list;
	}

	private List findSchoolsByPointId(String pointId,Page page) {
		List list = searchDao.findSchoolsByPointId(pointId,page);
		return list;
	}

	private void isAttention(List list, HttpServletRequest request) {
		if (request.getSession().getAttribute("student") == null)
			return;
		Student student = (Student) request.getSession().getAttribute("student");
		searchDao.isAttention(list, student);

	}

	private List findLabelsByPids(String pids,Page page) {
		List list = searchDao.findLabelsByPids(pids,page);
		return list;
	}

	private List findPLabelByKey(String key,Page page) {
		List list = searchDao.findPLabelByKey(key,page);
		return list;
	}

	private List findPointsByKey(String key,Page page) {
		List list = searchDao.findPointsByKey(key,page);
		return list;
	}

	private List findLabelsByStudentId(String studentId,Page page) {
		List list = searchDao.findLabelsByStudentId(studentId,page);
		return list;
	}

	private List findLabelsByPid(String pid,Page page) {
		List list = searchDao.findLabelsByPid(pid,page);
		return list;
	}

	private Map findSchoolByStudentId(String studentId) {
		Map map = searchDao.findSchoolByStudentId(studentId);
		return map;
	}

	private List findStudentsByLabelId(String labelId ,Page page) {
		List list = searchDao.findStudentsByLabelId(labelId,page);
		return list;
	}

	private List findSchoolsByLabelId(String labelId,Page page) {
		List list = searchDao.findSchoolsByLabelId(labelId,page);
		return list;
	}

	private List findStudentsBySchoolId(String schoolId,Page page) {
		List list = searchDao.findStudentsBySchoolId(schoolId,page);
		return list;
	}

	private List findSchoolLabelBySchoolId(String schoolId,Page page) {
		List list = searchDao.findSchoolLabelBySchoolId(schoolId,page);
		return list;
	}

	private List findStudentsByKeyName(String key,Page page) {
		List students = searchDao.findStudentsByKeyName(key,page);
		return students;
	}

	private Map findStudentsByKeyId(String key) {
		Map student = searchDao.findStudentsByKeyId(key);
		return student;
	}

	private Map findLabelByKey(String key) {
		Map label = searchDao.findLabelByKey(key);
		return label;
	}

	private Map findSchoolByKey(String key) {
		Map school = searchDao.findSchoolByKey(key);
		return school;
	}

	private List findLabelsByKey(String key,Page page) {
		List list = searchDao.findLabelsByKey(key,page);
		return list;
	}

	private List findSchoolsByKey(String key,Page page) {
		List list = searchDao.findSchoolsByKey(key,page);
		return list;
	}

	public void saveSearchLog(String key, HttpServletRequest request) {
		Object object = request.getSession().getAttribute("student");
		String studentId = object != null ? ((Student) object).getStudentId() : "";
		searchDao.saveSearchLog(key, studentId);
	}

	public Object findBySchoolIdAndKey(String schoolId, String key,Page page, HttpServletRequest request) {
		Map<String, List> map = new HashMap<String, List>();
		// 模糊匹配校内标签
		List labels = searchDao.findLabelsBySchoolIdAndKey(schoolId, key,page);
		List points = searchDao.findPointsBySchoolIdAndKey(schoolId, key,page);
		List students = null;
		Object student;
		// 检索用户
		boolean boo = ZhengzeUtil.isNumber(key);
		if (boo) {
			student = findStudentsByKeyIdAndSchoolId(key, schoolId);
			if (student != null) {
				List sts = new ArrayList();
				sts.add(student);
				map.put("students", sts);
			}else{
				map.put("students", new ArrayList());
			}

		} else {
			students = searchDao.findStudentsBySchoolIdAndKey(schoolId, key,page);
			map.put("students", students);
		}

		isAttention(map.get("students"), request);
		map.put("labels", labels);
		map.put("points", points);

		return map;
	}

	private Object findStudentsByKeyIdAndSchoolId(String key, String schoolId) {
		Map students = searchDao.findStudentsByKeyIdAndSchoolId(key, schoolId);
		return students;
	}

}
