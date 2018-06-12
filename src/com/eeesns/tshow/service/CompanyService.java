package com.eeesns.tshow.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.CompanyDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.dao.StudentDao;
import com.eeesns.tshow.entity.Course;
import com.eeesns.tshow.entity.InvitationCode;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.entity.StudentCourse;
import com.eeesns.tshow.entity.StudentCourseKey;
import com.eeesns.tshow.entity.StudentStudent;
import com.eeesns.tshow.util.DateUtil;
import com.eeesns.tshow.util.MD5Util;
import com.eeesns.tshow.util.MailUtil;
import com.eeesns.tshow.util.UUUID;

@Service
public class CompanyService {
	@Resource
	private CompanyDao companyDao;
	@Resource
	private BaseDao baseDao;
	@Resource
	private StudentDao studentDao;
	@Resource
	private UUUID uuuId;

	public Object saveCourse(Course course, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		course.setStudentId(student.getStudentId());
		Serializable courseId = companyDao.saveCourse(course);
		editJson.setBean(course);
		return editJson;
	}

	public Object deleteCourse(Course course, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		Course course2 = (Course) companyDao.findCourseById(course.getCourseId());
		if (!course2.getStudentId().equals(student.getStudentId())) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("非法操作");
			return editJson;
		}
		companyDao.deleteCourse(course2);
		return editJson;
	}

	public Object updateCourse(Course course, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		Course course2 = (Course) companyDao.findCourseById(course.getCourseId());
		if (!course2.getStudentId().equals(student.getStudentId())) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("非法操作");
			return editJson;
		}
		course2.setPlace(course.getPlace());
		course2.setContent(course.getContent());
		course2.setTelephone(course.getTelephone());
		course2.setCountLimit(course.getCountLimit());
		course2.setCreateTime(DateUtil.formatDate());
		companyDao.updateCourse(course2);
		return editJson;
	}

	public Object findCourse(String studentId, Page page, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		Page page2 = companyDao.findCourse(studentId, student.getStudentId(), page);
		editJson.setPage(page2);
		return editJson;
	}

	public Object findCourseById(String courseId, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Object object = companyDao.findCourseById(courseId);
		editJson.setBean(object);
		return editJson;
	}

	public Object saveSingupCourse(StudentCourse studentCourse, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		studentCourse.setStudentId(student.getStudentId());
		// 判断是否已经报名
		StudentCourseKey sck = new StudentCourseKey();
		sck.setCourseId(studentCourse.getCourseId());
		sck.setStudentId(studentCourse.getStudentId());
		StudentCourse studentCourse2 = (StudentCourse) baseDao.findEntity(StudentCourse.class, sck);
		if (studentCourse2 != null) {
			editJson.setSuccess(false);
			editJson.getMess().setState("serviceFailed");
			editJson.getMess().setMessage("已经报名了");
			return editJson;
		}
		Course course = (Course) companyDao.findCourseById(studentCourse.getCourseId());
		// 判断是否是已经发送了加老师的请求
		StudentStudent studentStudent = companyDao.findStudentStudent(student.getStudentId(),
				course.getStudentId());
		if (studentStudent == null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("请先提交报名信息");
			return editJson;
		}
		studentCourse.setRemark(studentStudent.getRemark());
		studentCourse.setTelephone(studentStudent.getTelephone());
		// 添加报名信息
		String message = companyDao.saveStudentCourse(studentCourse);
		// 更新课程的报名人数
		course.setCount(course.getCount() + 1);
		companyDao.updateCourse(course);
		return editJson;
	}

	public Object saveQyAccount(String email, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		String type;
		if (student.getType().equals("QYGL")) {
			type = "QYFP";
		}
		// 发送邮件
		sendMailQYFP(email, request, editJson, "QYFP");
		return editJson;
	}
	
	private void sendMailQYFP(String email, HttpServletRequest request, EditJson editJson, String type) {
		Student student = (Student) request.getSession().getAttribute("student");
		String invitatonId = null;
		InvitationCode ic = companyDao.findInvitationCodeByEmail(email);
		// 若邮箱不存在invitation_code中，则添加
		InvitationCode icnew = new InvitationCode();
		if (ic == null) {
			invitatonId = uuuId.getInvitationId();
			icnew.setEmail(email);
			icnew.setInvitationId(invitatonId);
			icnew.setStudentId(student.getStudentId());
			icnew.setType(type);
			icnew.setKeycode(MD5Util.MD5(UUUID.getNextIntValue()));
			companyDao.saveInvitationCode(icnew);
		} else {
			// 判断是否已经注册
			Student stu = studentDao.findStudentByEmail(email);
			if (stu != null) {// 已经注册直接返回
				editJson.setSuccess(false);
				editJson.getMess().setMessage("此邮箱已经被注册");
				return;
			}
			invitatonId = uuuId.getInvitationId();
			ic.setInvitationId(invitatonId);
			ic.setStudentId(student.getStudentId());
			ic.setType(type);
			ic.setCreateTime(DateUtil.formatDate());
			ic.setKeycode(MD5Util.MD5(UUUID.getNextIntValue()));
			companyDao.updateInvitationCode(ic);
		}
		try {
			Object[] object = new Object[4];
			object[0] = student.getInvitationId();
			object[1] = student.getRealName();
			object[2] = invitatonId;
			object[3] = icnew.getKeycode();
			String path = request.getServletContext().getRealPath("templete/mailTemplete.xml");
			MailUtil.sendMainTemplete("这是一封T-Show的邀请函", object, path, email);
		} catch (MessagingException e) {
			RuntimeException ee = new RuntimeException("发送邮件异常");
			throw ee;
		}
	}

	/**
	 * 达人企业发送邮件
	 * 
	 * @param email
	 *            接收者邮箱
	 * @param request
	 * @param editJson
	 * @param type
	 *            发送邮件的动机(分配企业子账户，邀请好友)
	 */
	private void sendMail(String email, HttpServletRequest request, EditJson editJson, String type) {
		Student student = (Student) request.getSession().getAttribute("student");
		String invitatonId = null;
		InvitationCode ic = companyDao.findInvitationCodeByEmail(email);
		// 若邮箱不存在invitation_code中，则添加
		InvitationCode icnew = new InvitationCode();
		if (ic == null) {
			invitatonId = uuuId.getInvitationId();
			icnew.setEmail(email);
			icnew.setInvitationId(invitatonId);
			icnew.setStudentId(student.getStudentId());
			icnew.setType(type);
			icnew.setKeycode(MD5Util.MD5(UUUID.getNextIntValue()));
			companyDao.saveInvitationCode(icnew);
		} else {
			// 判断是否已经注册
			Student stu = studentDao.findStudentByEmail(email);
			if (stu != null) {// 已经注册直接返回
				editJson.setSuccess(false);
				editJson.getMess().setMessage("此邮箱已经被注册");
				return;
			}
			invitatonId = uuuId.getInvitationId();
			ic.setInvitationId(invitatonId);
			ic.setStudentId(student.getStudentId());
			ic.setType(type);
			ic.setCreateTime(DateUtil.formatDate());
			ic.setKeycode(MD5Util.MD5(UUUID.getNextIntValue()));
			companyDao.updateInvitationCode(ic);
		}
		try {
			Object[] object = new Object[4];
			object[0] = student.getInvitationId();
			object[1] = student.getRealName();
			object[2] = invitatonId;
			object[3] = icnew.getKeycode();
			String path = request.getServletContext().getRealPath("templete/mailTemplete.xml");
			MailUtil.sendMainTemplete("这是一封T-Show的邀请函", object, path, email);
		} catch (MessagingException e) {
			RuntimeException ee = new RuntimeException("发送邮件异常");
			throw ee;
		}
	}

	public Object updateQyAccountToFreeZe(String studentId, HttpServletRequest request) {
		EditJson editJosn = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		Student studentChild = studentDao.findStudentById(studentId);
		if (studentChild != null && !student.getStudentId().equals(studentChild.getStudentPid())) {// 当前用户不是此用户的父用户
			editJosn.setSuccess(false);
			editJosn.getMess().setMessage("非法操作");
			return editJosn;
		}
		studentChild.setState("DJ");
		studentDao.updateStudent(studentChild);
		return editJosn;
	}

	public Object checkAllAccount(Page page, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		EditJson editJson = new EditJson();
		Page page2 = companyDao.checkAllAccount(page, student.getStudentId());
		editJson.setPage(page2);
		return editJson;
	}

	public Object saveAddTeacher(String toStudentId, String telephone, String remark,
			HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		// 判断是新学生则发送加老师请求
		StudentStudent studentStudent = companyDao.findStudentStudent(student.getStudentId(),
				toStudentId);
		if (studentStudent == null) {
			StudentStudent ss = new StudentStudent();
			ss.setTelephone(telephone);
			ss.setRemark(remark);
			ss.setStudentId(student.getStudentId());
			ss.setToStudentId(toStudentId);
			companyDao.saveStudentStudent(ss);
			// 默认关注老师
			studentDao.saveAttention(toStudentId, student);
		} else {// 更新本次请求信息
			studentStudent.setCreateTime(DateUtil.formatDate());
			studentStudent.setTelephone(telephone);
			studentStudent.setRemark(remark);
			companyDao.updadeStudentStudent(studentStudent);
		}
		return editJson;
	}

	public Object checkAccount(String studentId, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		Student student2 = studentDao.findStudentById(studentId);
		// 用户安全验证
		if (!student2.getStudentPid().equals(student.getStudentId())) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("非法操作");
			return editJson;
		}
		Map map = companyDao.checkAccount(studentId);
		Map map2 = studentDao.findstudentStudentTypeCount(studentId);
		List showSchools = studentDao.findCPSA(studentId);
		List showGrades = studentDao.findCPSY(studentId);
		map.put("studentCount", map2);
		map.put("showSchools", showSchools);
		map.put("showGrades", showGrades);
		editJson.setBean(map);
		return editJson;
	}

	public Object updateQyAccountToFree(String studentId, HttpServletRequest request) {
		EditJson editJosn = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		Student studentChild = studentDao.findStudentById(studentId);
		if (studentChild != null && !student.getStudentId().equals(studentChild.getStudentPid())) {// 当前用户不是此用户的父用户
			editJosn.setSuccess(false);
			editJosn.getMess().setMessage("非法操作");
			return editJosn;
		}
		studentChild.setState("A");
		studentDao.updateStudent(studentChild);
		return editJosn;
	}

	public Object findYearsArea(String studentId) {
		EditJson editJson = new EditJson();
		List list = companyDao.findYearsArea(studentId);
		editJson.setBean(list);
		return editJson;
	}

	public Object findYbmStudents(String courseId, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		List list = null;
		Student student = (Student) request.getSession().getAttribute("student");
		// 判断此课程的发布者是否是当前访问者
		Course course = (Course) companyDao.findCourseById(courseId);
		if (course.getStudentId().equals(student.getStudentId())) {
			list = companyDao.findYbmStudents(courseId, true);
		} else {
			list = companyDao.findYbmStudents(courseId, false);
		}

		editJson.setBean(list);
		return editJson;
	}
}
