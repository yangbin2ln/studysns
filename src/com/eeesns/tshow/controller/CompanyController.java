package com.eeesns.tshow.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.Course;
import com.eeesns.tshow.entity.StudentCourse;
import com.eeesns.tshow.service.CompanyService;

@Controller
@RequestMapping(value = "company")
public class CompanyController {
	@Resource
	private CompanyService companyService;

	/**
	 * 发布课程
	 * 
	 * @param course
	 * @return
	 */
	@RequestMapping(value = "saveCourse")
	@ResponseBody
	public Object saveCourse(Course course, HttpServletRequest request) {
		Object object = companyService.saveCourse(course, request);
		return object;
	}

	/**
	 * 更新课程
	 * 
	 * @param course
	 * @return
	 */
	@RequestMapping(value = "updateCourse")
	@ResponseBody
	public Object updateCourse(Course course, HttpServletRequest request) {
		Object object = companyService.updateCourse(course, request);
		return object;
	}

	/**
	 * 删除课程
	 * 
	 * @param course
	 * @return
	 */
	@RequestMapping(value = "deleteCourse")
	@ResponseBody
	public Object deleteCourse(Course course, HttpServletRequest request) {
		Object object = companyService.deleteCourse(course, request);
		return object;
	}

	/**
	 * 查看某人的全部课程
	 * 
	 * @param studentId
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "findCourse/list")
	@ResponseBody
	public Object findCourseAll(String studentId, Page page, HttpServletRequest request) {
		Object object = companyService.findCourse(studentId, page, request);
		return object;
	}

	/**
	 * 查询某个课程
	 * 
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "findCourse/{courseId}", method = RequestMethod.POST)
	@ResponseBody
	public Object findCourseById(@PathVariable("courseId") String courseId,
			HttpServletRequest request) {
		Object object = companyService.findCourseById(courseId, request);
		return object;
	}

	/**
	 * 学生报名公开课
	 * 
	 * @param studentCourse
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "saveSingupCourse", method = RequestMethod.POST)
	@ResponseBody
	public Object saveSingupCourse(StudentCourse studentCourse, HttpServletRequest request) {
		Object object = companyService.saveSingupCourse(studentCourse, request);
		return object;
	}

	/**
	 * 报名（即发送加老师请求）
	 * 
	 * @param toStudentId
	 * @param telephone
	 * @param remark
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "saveAddTeacher", method = RequestMethod.POST)
	@ResponseBody
	public Object saveAddTeacher(String toStudentId, String telephone, String remark,
			HttpServletRequest request) {
		Object object = companyService.saveAddTeacher(toStudentId, telephone, remark, request);
		return object;
	}

	/**
	 * 查询课程的已报名人员信息
	 * 
	 * @param courseId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findYbmStudents", method = RequestMethod.POST)
	@ResponseBody
	public Object findYbmStudents(String courseId, HttpServletRequest request) {
		Object object = companyService.findYbmStudents(courseId, request);
		return object;
	}

	/**
	 * 企业分配子账户
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "saveQyAccount", method = RequestMethod.POST)
	@ResponseBody
	public Object saveQyAccount(String email, HttpServletRequest request) {
		Object object = companyService.saveQyAccount(email, request);
		return object;
	}

	/**
	 * 冻结企业账户
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "freezeAccount", method = RequestMethod.POST)
	@ResponseBody
	public Object updateQyAccountToFreeZe(String studentId, HttpServletRequest request) {
		Object object = companyService.updateQyAccountToFreeZe(studentId, request);
		return object;
	}

	/**
	 * 解冻企业账户
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "freeAccount", method = RequestMethod.POST)
	@ResponseBody
	public Object updateQyAccountToFree(String studentId, HttpServletRequest request) {
		Object object = companyService.updateQyAccountToFree(studentId, request);
		return object;
	}

	/**
	 * 企业管理员查询其子账户
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "findChildCountList", method = RequestMethod.POST)
	@ResponseBody
	public Object checkAllAccount(Page page, HttpServletRequest request) {
		Object object = companyService.checkAllAccount(page, request);
		return object;
	}

	/**
	 * 查询企业单个子账户详情
	 * 
	 * @param studentId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "findChildCount", method = RequestMethod.POST)
	@ResponseBody
	public Object checkAccount(String studentId, HttpServletRequest request) {
		Object object = companyService.checkAccount(studentId, request);
		return object;
	}

}
