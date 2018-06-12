package com.eeesns.tshow.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.eeesns.tshow.entity.Course;
import com.eeesns.tshow.entity.InvitationCode;
import com.eeesns.tshow.entity.StudentCourse;
import com.eeesns.tshow.entity.StudentCourseKey;
import com.eeesns.tshow.entity.StudentStudent;

@Component
public class CompanyDao {
	@Resource
	private BaseDao baseDao;

	public Serializable saveCourse(Course course) {
		Serializable cou = baseDao.saveEntity(course);
		return cou;
	}

	public void deleteCourse(Course course) {
		baseDao.deleteEntity(course);
	}

	public void updateCourse(Course course) {
		baseDao.saveOrUpdateEntity(course);
	}

	public Page findCourse(String studentId, String studentIdNow, Page page) {
		if (studentId.toUpperCase().contains("or")) {
			return null;
		}
		String sql = "SELECT co.course_id courseId, co.student_id studentId,"
				+ "	co.place place, co.telephone telephone, co.count_limit countLimit,"
				+ "	co.count count, co.content content,"
				+ " DATE_FORMAT(  co.create_time , '%Y-%m-%d %H:%i:%S')createTime,"
				+ "  sc.course_id isbm FROM course co"
				+ " LEFT JOIN (select * from student_course where student_id = '"+studentIdNow+"')sc ON (co.course_id = sc.course_id) WHERE"
				+ "	co.student_id = '" + studentId + "' order by co.create_time desc";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), true,
				new String[] {});
		return page2;
	}

	public Object findCourseById(String courseId) {
		Course course = (Course) baseDao.findEntity(Course.class, courseId);
		return course;
	}

	public String saveStudentCourse(StudentCourse studentCourse) {
		baseDao.saveEntity(studentCourse);
		return "报名成功";
	}

	public StudentStudent findStudentStudent(String studentId, String toStudentId) {
		String hql = "from StudentStudent ss where ss.studentId = '" + studentId
				+ "' and ss.toStudentId = '" + toStudentId + "'";
		StudentStudent studentStudent = (com.eeesns.tshow.entity.StudentStudent) baseDao
				.findObjByHql(hql);
		return studentStudent;
	}

	/**
	 * 更新用户的学生信息
	 * 
	 * @param studentStudent
	 */
	public void updadeStudentStudent(StudentStudent studentStudent) {
		baseDao.updateEntity(studentStudent);
	}

	public void saveStudentStudent(StudentStudent ss) {
		baseDao.saveEntity(ss);
	}

	public InvitationCode findInvitationCodeByInvitationId(String invitationId) {
		String hql = "from InvitationCode ic where ic.invitationId = '" + invitationId + "'";
		InvitationCode ic = (InvitationCode) baseDao.findObjByHql(hql);
		return ic;
	}

	public InvitationCode findInvitationCodeByEmail(String email) {
		String hql = "from InvitationCode ic where ic.email = '" + email + "'";
		InvitationCode ic = (InvitationCode) baseDao.findObjByHql(hql);
		return ic;
	}

	public void updateInvitationCode(InvitationCode ic) {
		baseDao.updateEntity(ic);
	}

	public void saveInvitationCode(InvitationCode ic) {
		baseDao.saveEntity(ic);
	}

	public Page checkAllAccount(Page page, String studentId) {
		if (studentId.toLowerCase().contains("or")) {
			return null;
		}
		String sql = "select st.student_id studentId,st.realname realName,st.invitation_id invitationId,"
				+ " la.label_id labelId,la.label_name labelName,st.student_count studentCount,st.follower_count followerCount"
				+ " from student st left join label la on(st.job_id = la.label_id) where st.student_pid = '"
				+ studentId + "'";
		Page page2 = baseDao.findPageBySql(sql, page.getPageNo(), page.getPageSize(), true,
				new String[] {});
		return page2;
	}

	public Map checkAccount(String studentId) {
		String sql = "select st.student_id studentId,st.realname realName,st.invitation_id invitationId,"
				+ " la.label_id labelId,la.label_name labelName,st.student_count studentCount,st.follower_count followerCount,st.state state"
				+ " from student st left join label la on(st.job_id = la.label_id) where st.student_id = '"
				+ studentId + "'";
		HashMap map = baseDao.findMapBySql(sql, new String[] {});
		return map;
	}

	public List findYearsArea(String studentId) {
		String sql = "SELECT t.grade grade, y.year_id yearId FROM ("
				+ "	 SELECT cpsy.grade grade FROM"
				+ "	 company_product_show_year cpsy WHERE cpsy.student_id = '" + studentId + "'"
				+ "	) t RIGHT JOIN YEAR y ON (t.grade = y.year_id)";
		List list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}

	/**
	 * 查询课程的报名者
	 * 
	 * @param courseId
	 *            课程id
	 * @param yqx
	 *            是否有权限查询用户的联系方式等信息
	 * @return
	 */
	public List findYbmStudents(String courseId, boolean yqx) {
		String sql = "select st.head_ico headIco, sc.student_id studentId,sc.course_id courseId,st.realname realName,"
				+ " st.invitation_id invitationId,sc.name courseName,sch.school_id schoolId,sch.name schoolName,st.year year";
		if (yqx) {// 若没有过滤则查出电话等信息
			sql += ",sc.telephone telephone,sc.remark remark";

		}
		sql += " ,DATE_FORMAT(sc.create_time , '%Y-%m-%d %H:%i:%S') createTime from student_course sc "
				+ " left join student st on(st.student_id = sc.student_id) "
				+ " left join school sch on(sch.school_id = st.school_id) where sc.course_id = '"
				+ courseId + "'";
		List list = baseDao.findListBySql(sql, new String[] {});
		return list;
	}
}
