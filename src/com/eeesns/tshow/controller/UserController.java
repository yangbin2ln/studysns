package com.eeesns.tshow.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.entity.ApplyRegister;
import com.eeesns.tshow.entity.Feedback;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.CompanyService;
import com.eeesns.tshow.service.LabelService;
import com.eeesns.tshow.service.SchoolService;
import com.eeesns.tshow.service.StudentService;
import com.eeesns.tshow.service.UserService;
import com.mchange.v1.util.ArrayUtils;

@Controller
@RequestMapping(value = "user")
public class UserController {
	@Resource
	UserService userService;
	@Resource
	LabelService labelService;
	@Resource
	CompanyService companyService;
	@Resource
	SchoolService schoolService;
	@Resource
	StudentService studentService;

	/**
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param verifyCode
	 *            验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "login")
	public Object login(String userName, String password, String verifyCode,
			HttpServletRequest request, HttpServletResponse response) {
		Object object = userService.login(userName, password, verifyCode, request, response);
		return object;
	}

	/**
	 * 进入登录主页面
	 * 
	 * @param userName
	 * @param password
	 * @param verifyCode
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "toLogin")
	public void toLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("student") != null) {
			response.sendRedirect("/");
		} else {
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
		}
	}

	/**
	 * 用户退出
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "destory")
	public Object destoryLogin(HttpServletRequest request, HttpServletResponse response) {
		Object object = userService.destoryLogin(request, response);
		return object;
	}

	/**
	 * 用户注册
	 * 
	 * @param invitationCode
	 * @param account
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "register")
	public Object saveRegister(String invitationId, String email, String password, String realName,
			String verify, HttpServletRequest request) {
		Object object = userService.saveRegister(invitationId, email, password, realName, verify,
				request);
		return object;
	}

	/* end 注册 */

	/**
	 * 注册激活邮箱
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "activated")
	public void findActivedEmail(String key, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String jsp = userService.saveActiveEmail(key, request, response);
		if (jsp != null) {
			try {
				request.getRequestDispatcher(jsp).forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 发送邀请好友注册的邮件
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "savePtAccount")
	public Object saveDealFriendToRegister(String email, HttpServletRequest request) {
		Object object = userService.saveDealFriendToRegister(email, request);
		return object;
	}

	/**
	 * 通过邮箱找回密码（此处是为此邮箱发送验证码）
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findPwdByEmail")
	public Object findPwdByEmail(String email, HttpServletRequest request) {
		Object object = userService.findPwdByEmail(email, request);
		return object;
	}

	/**
	 * 判断此验证码是否有效
	 * 
	 * @param yzm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findYzmValid")
	public Object findYzmValid(String yzm, String email, HttpServletRequest request) {
		Object object = userService.findYzmValid(yzm, email, request);
		return object;
	}

	/**
	 * 重新设置密码
	 * 
	 * @param pwd
	 * @param email
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findResetPwd")
	public Object findResetPwd(String pwd, String email, HttpServletRequest request) {
		Object object = userService.findResetPwd(pwd, email, request);
		return object;
	}

	/**
	 * 完善/修改个人的基本信息
	 * 
	 * @param realName
	 * @param headIco
	 * @param schoolId
	 * @param jobId
	 * @param professionId
	 * @param year
	 * @param signature
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateUserInfo")
	public Object updateUserInfo(String realName, String headIco, String schoolId, String jobId,
			String professionId, String year, String signature, HttpServletRequest request) {
		Object object = userService.updateUserInfo(realName, headIco, schoolId, jobId,
				professionId, year, signature, request);
		return object;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param pwd
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateUserPwd")
	public Object updateUserPwd(String pwdOld, String pwdNew, HttpServletRequest request) {
		Object object = userService.updateUserPwd(pwdOld, pwdNew, request);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "saveProductShowArea")
	public Object saveProductShowArea(String[] schoolIds, HttpServletRequest request) {
		synchronized (request.getSession()) {
			Object object = userService.saveProductShowArea(schoolIds, request);
			return object;
		}
	}

	/**
	 * 配置用户作品的曝光范围(年级)
	 * 
	 * @param years
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveProductsShowYear")
	public Object saveProductsShowYear(String[] years, HttpServletRequest request) {
		synchronized (request.getSession()) {
			Object object = userService.saveProductsShowYear(years, request);
			return object;
		}
	}

	/**
	 * 查看个人设置页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "setup")
	public String findSetUp(HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		if (student == null) {
			return "jsp/404";
		}
		// 查询页面展示权限
		List<String> modules = userService.findModulesById(student.getStudentId());
		// 查询所有的一级标签
		EditJson editJson = (EditJson) labelService.findLabelsF();
		// 初始查询企业子账户
		Page page = new Page();
		EditJson editJsonAccount = (EditJson) companyService.checkAllAccount(page, request);
		// 查询本省的学校范围
		EditJson schools = (EditJson) schoolService.findSchoolsByProvince(student.getProvinceId(),
				request);
		// 查询年级范围
		EditJson years = (EditJson) companyService.findYearsArea(student.getStudentId());
		request.setAttribute("studentJSON", JSONObject.fromObject(student).toString());
		request.setAttribute("loginningstudent", student);
		request.setAttribute("modules", modules);
		request.setAttribute("labelFs", editJson.getBean());
		request.setAttribute("accounts", editJsonAccount.getPage());
		request.setAttribute("schools", schools.getBean());
		request.setAttribute("years", years.getBean());
		return "jsp/student/setup";
	}

	/**
	 * 用户申请注册
	 * 
	 * @param applyRegister
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "applyRegister")
	public Object findApplyResiter(ApplyRegister applyRegister, HttpServletRequest request) {
		Object object = userService.findApplyResiter(applyRegister);
		return object;
	}

	/**
	 * 用户反馈
	 * 
	 * @param feedbackTitle
	 * @param feedbackContent
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveFeedback")
	public Object saveFeedback(Feedback feedback, HttpServletRequest request) {
		Object object = userService.saveFeedback(feedback, request);
		return object;
	}

}
