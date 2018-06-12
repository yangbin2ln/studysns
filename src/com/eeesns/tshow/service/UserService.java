package com.eeesns.tshow.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sparknet.starshine.common.util.StringUtil;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.common.json.Message;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.CompanyDao;
import com.eeesns.tshow.dao.FeedbackDao;
import com.eeesns.tshow.dao.SchoolDao;
import com.eeesns.tshow.dao.StudentDao;
import com.eeesns.tshow.dao.UserDao;
import com.eeesns.tshow.entity.ApplyRegister;
import com.eeesns.tshow.entity.Feedback;
import com.eeesns.tshow.entity.InvitationCode;
import com.eeesns.tshow.entity.Role;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.entity.ViewMessage;
import com.eeesns.tshow.util.CommonDataUtil;
import com.eeesns.tshow.util.DateUtil;
import com.eeesns.tshow.util.MD5Util;
import com.eeesns.tshow.util.MailUtil;
import com.eeesns.tshow.util.UUUID;

@Service
public class UserService {
	@Resource
	UserDao userDao;
	@Resource
	StudentDao studentDao;
	@Resource
	BaseDao baseDao;
	@Resource
	CompanyDao companyDao;
	@Resource
	SchoolDao schoolDao;
	@Resource
	FeedbackDao feedbackDao;
	@Resource
	private UUUID uuuId;

	/**
	 * 域名主页
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void index(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("student") == null) {// 进入登录页面
			// request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request,
			// response);
			// 默认进入探索页面
			response.sendRedirect("/find");
		} else {// 进入个人中心
			Student student = (Student) request.getSession().getAttribute("student");
			// 查询个人的模块权限列表
			List<String> modules = findModulesById(student.getStudentId());
			student.setModules(modules);
			request.setAttribute("studentJSON", JSONObject.fromObject(student).toString());
			request.setAttribute("loginningstudent", student);
			request.getRequestDispatcher("/WEB-INF/jsp/student/personCenter.jsp").forward(request,
					response);
		}
	}

	public Object login(String userName, String password, String verifyCode,
			HttpServletRequest request, HttpServletResponse response) {
		Map map = checkLogin(userName, password, verifyCode, request);
		Message message = (Message) map.get("message");
		Student student = (Student) map.get("student");
		if (message.getState().startsWith("loginError")) {
			return message;
		}
		// 绑定session
		JSONObject.fromObject(student).toString();
		request.getSession().setAttribute("student", student);
		// 添加cookie
		addCookie(request, response, student);
		return message;
	}

	private void addCookie(HttpServletRequest request, HttpServletResponse response, Student student) {
		Cookie cookie = new Cookie("loginName", student.getStudentId());
		cookie.setMaxAge(3600 * 24 * 30);
		cookie.setPath("/");
		response.addCookie(cookie);

	}

	private Map checkLogin(String userName, String password, String verifyCode,
			HttpServletRequest request) {
		Map map2 = new HashMap();
		Message message = new Message();
		// 验证验证码
		HttpSession session = request.getSession();
		String realVerifyCode = (String) session.getAttribute("verifyCodeLogin");
		if (realVerifyCode != null) {
			if (!realVerifyCode.toLowerCase().equals(verifyCode)) {
				message.setMessage("验证码错误");
				message.setState("loginError0");
				map2.put("message", message);
				return map2;
			}
		}
		// 验证用户密码
		Student student = checkUserNameAndPassword(userName, password, message);
		if (student == null) {
			// 判断登陆的错误次数
			if (session.getAttribute("loginErrorCount") == null) {
				Map map = new HashMap();
				map.put("count", 1);
				session.setAttribute("loginErrorCount", map);
			} else {
				Map map = (Map) session.getAttribute("loginErrorCount");
				int count = (Integer) map.get("count");
				map.put("count", ++count);
				if (count >= 5) {
					message.setState("loginError3");
				}
			}
			map2.put("message", message);
			return map2;
		} else {
			if (student.getState().equals("DJ")) {
				message.setMessage("账户已被冻结");
				message.setState("loginErrorYDJ");
				map2.put("message", message);
				return map2;
			}
		}

		message.setMessage("登陆成功");
		message.setState("loginSuccess");
		map2.put("message", message);
		map2.put("student", student);
		return map2;
	}

	private Student checkUserNameAndPassword(String userName, String password, Message message) {
		Student student = findStudentByUserNameAndPassword(userName, password);
		// md5密码加密
		password = MD5Util.MD5(password);
		if (student == null) {
			message.setMessage("用户名不存在");
			message.setState("loginError1");
			return null;
		} else if (!student.getPassword().equals(password)) {
			message.setMessage("密码错误");
			message.setState("loginError2");
			return null;
		}
		return student;
	}

	private Student findStudentByUserNameAndPassword(String userName, String password) {
		Student student = userDao.findStudentByUserNameAndPassword(userName, password);
		return student;
	}

	public Object destoryLogin(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		// 清除前台cookie
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if ("loginName".equals(cookie.getName())) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			if ("verify".equals(cookie.getName())) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		return null;
	}

	public List<String> findModulesById(String studentId) {
		List<String> modules = userDao.findModulesById(studentId);
		return modules;
	}

	public String saveActiveEmail(String key, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InvitationCode ic = userDao.findInvitationCodeByKey(key);
		if (ic == null) {
			response.getWriter().print("此链接已经失效,注意查看最新的邀请邮件");
			return null;
		}
		Student student = studentDao.findStudentByEmail(ic.getEmail());
		if (student != null) {// 若已经注册则跳转登(主)录页面
			response.sendRedirect("");
			return null;
		}
		ic.setState("Y");
		companyDao.updateInvitationCode(ic);
		// 发送激活页面并跳转注册页面
		return "/WEB-INF/jsp/activited.jsp";
	}

	public Object saveRegister(String invitationId, String email, String password, String realName,
			String verify, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		// 判断验证码是否正确
		String realVerfy = (String) request.getSession().getAttribute("verifyCodeRegister");
		if (!verify.equals(realVerfy)) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("验证码不正确");
			return editJson;
		}

		// 验证此账户是否已经被注册
		Student stu = studentDao.findStudentByEmail(email);
		if (stu != null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("此邮箱已经注册");
			return editJson;
		}
		// 验证账户和邀请码是否存在于invitation_code中
		InvitationCode ic = companyDao.findInvitationCodeByInvitationId(invitationId);
		if (ic == null || !ic.getEmail().equals(email)) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("邮箱和邀请码不匹配");
			return editJson;
		} else if (ic.getState().equals("N")) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("此邮箱未激活");
			return editJson;
		}
		CommonDataUtil commonDataUtil = CommonDataUtil.getInstance(baseDao);
		// 判断是企业子用户注册还是普通用户注册
		Student newStudent = new Student();
		if (ic.getType().equals("QYFP")) {// 企业分配子账户
			newStudent.setInvitationId(invitationId);
			newStudent.setEmail(email);
			newStudent.setStudentPid(ic.getStudentId());
			// 和主账户的省份一致
			Student student2 = studentDao.findStudentById(ic.getStudentId());
			newStudent.setProvinceId(student2.getProvinceId());
			newStudent.setPassword(MD5Util.MD5(password));
			newStudent.setRealName(realName);
			newStudent.setType("QY");
			Role role = new Role();
			String roleId = commonDataUtil.findRoleByType("nc_company_common_user").getRoleId();
			role.setRoleId(roleId);// 未完善信息的企业子用户
			newStudent.setRole(role);
		} else if (ic.getType().equals("YQHY")) {// 达人的邀请好友
			newStudent.setInvitationId(invitationId);
			newStudent.setEmail(email);
			newStudent.setStudentPid(ic.getStudentId());
			newStudent.setPassword(MD5Util.MD5(password));
			newStudent.setRealName(realName);
			newStudent.setType("S");
			Role role = new Role();
			String roleId = commonDataUtil.findRoleByType("nc_user").getRoleId();
			role.setRoleId(roleId);
			newStudent.setRole(role);
		} else if (ic.getType().equals("HTSH")) {// 后台审核
			newStudent.setInvitationId(invitationId);
			newStudent.setEmail(email);
			newStudent.setPassword(MD5Util.MD5(password));
			newStudent.setRealName(realName);
			newStudent.setType("S");
			Role role = new Role();
			String roleId = commonDataUtil.findRoleByType("nc_user").getRoleId();
			role.setRoleId(roleId);
			newStudent.setRole(role);
		}
		// 初始化此用户的消息记录
		baseDao.saveOrUpdateEntity(newStudent);
		ViewMessage vm = new ViewMessage();
		vm.setStudentId(newStudent.getStudentId());
		studentDao.saveLastViewMessage(vm);
		return editJson;
	}

	public Object saveDealFriendToRegister(String email, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		sendMail(email, request, editJson, "YQHY");
		return editJson;
	}

	private void sendMail(String email, HttpServletRequest request, EditJson editJson, String type) {
		Student student = (Student) request.getSession().getAttribute("student");
		InvitationCode ic = companyDao.findInvitationCodeByEmail(email);
		// 若邮箱不存在invitation_code中，则添加
		InvitationCode icObj = new InvitationCode();
		if (ic == null) {
			InvitationCode icnew = new InvitationCode();
			String invitatonId = uuuId.getInvitationId();
			icnew.setInvitationId(invitatonId);
			icnew.setEmail(email);
			icnew.setInvitationId(invitatonId);
			icnew.setStudentId(student.getStudentId());
			icnew.setType(type);
			icnew.setKeycode(MD5Util.MD5(UUUID.getNextIntValue()));
			companyDao.saveInvitationCode(icnew);
			icObj = icnew;
		} else {
			// 判断是否已经注册
			Student stu = studentDao.findStudentByEmail(email);
			if (stu != null) {// 已经注册直接返回
				editJson.setSuccess(false);
				editJson.getMess().setMessage("此邮箱已经被注册");
				return;
			}
			ic.setStudentId(student.getStudentId());
			ic.setType(type);
			ic.setCreateTime(DateUtil.formatDate());
			ic.setKeycode(MD5Util.MD5(UUUID.getNextIntValue()));
			companyDao.updateInvitationCode(ic);
			icObj = ic;
		}
		try {
			Object[] object = new Object[4];
			object[0] = student.getInvitationId();
			object[1] = student.getRealName();
			object[2] = icObj.getInvitationId();
			object[3] = icObj.getKeycode();
			String path = request.getServletContext().getRealPath("templete/mailTemplete.xml");
			MailUtil.sendMainTemplete("这是一封T-Show的邀请函", object, path, email);
		} catch (MessagingException e) {
			RuntimeException ee = new RuntimeException("发送邮件异常");
			throw ee;
		}
	}

	@Transactional(readOnly = false)
	public Object findPwdByEmail(String email, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		// 判断是否是已经注册邮箱
		Student student = studentDao.findStudentByEmail(email);
		if (student == null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("此邮箱未注册");
			return editJson;
		}
		String yzm = "";
		Random rd = new Random();
		for (int i = 0; i < 6; i++) {
			int num = rd.nextInt(9);
			yzm += num;
		}
		Object[] object = new Object[1];
		object[0] = yzm;
		String path = request.getServletContext().getRealPath("templete/findPwdEmailTemplete.xml");
		try {
			MailUtil.sendMainTemplete("找回密码—T-Show", object, path, email);
		} catch (MessagingException e) {
			RuntimeException ee = new RuntimeException("找回密码-发送邮件异常");
			throw ee;
		}
		// 添加至表yam-email中
		userDao.saveYzmEmail(yzm, email);
		return editJson;
	}

	@Transactional(readOnly = false)
	public Object findYzmValid(String yzm, String email, HttpServletRequest request) {
		HttpSession session = request.getSession();
		EditJson editJson = new EditJson();
		// 查询此验证码是否存在并有效
		Map map = userDao.findYzmValid(yzm, email);
		if (map == null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("验证码错误");
			Integer errorCount = (Integer) session.getAttribute("errorCount");
			if (errorCount == null) {
				errorCount = 1;
			}
			if (errorCount >= 5) {// 错误超过五次则需要重新发送邮件
				userDao.deleteYzm(email);
				editJson.getMess().setState("moreErrorCount");
			}
			session.setAttribute("errorCount", errorCount + 1);
			return editJson;
		}
		// 在当前session中绑定密码可修改状态
		Map map2 = new HashMap();
		map2.put("email", email);
		session.setAttribute("couldUpdatePwd", map2);
		userDao.deleteYzm(email);
		return editJson;
	}

	public Object updateUserInfo(String realName, String headIco, String schoolId, String jobId,
			String professionId, String year, String signature, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		CommonDataUtil commonDataUtil = CommonDataUtil.getInstance(baseDao);
		Student student = (Student) request.getSession().getAttribute("student");
		// 后台信息完整性验证
		if (StringUtil.isEmpty(schoolId) || StringUtil.isEmpty(professionId)
				|| StringUtil.isEmpty(year) || StringUtil.isEmpty(realName)) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("必输信息不完整");
			return editJson;
		}
		if (student.getPerfectInfo().equals("Y")) {// 判断是否已经完善了信息
			userDao.updateUserInfo(headIco, jobId, signature, student.getStudentId());
			student = studentDao.findStudentById(student.getStudentId());
		} else {
			// 判断是企业用户还是普通用户
			String roleId = "";
			if (student.getType().equals("QY")) {
				roleId = commonDataUtil.findRoleByType("company_common_user").getRoleId();
			} else if (student.getType().equals("QYGL")) {
				roleId = commonDataUtil.findRoleByType("company_manager_user").getRoleId();
			} else {
				roleId = commonDataUtil.findRoleByType("user").getRoleId();
			}
			userDao.updateUserInfo(realName, headIco, schoolId, jobId, professionId, year,
					signature, roleId, student.getStudentId());
			student = studentDao.findStudentById(student.getStudentId());
			// 解锁学校
			schoolDao.unLock(student.getSchool().getSchoolId());
		}
		request.getSession().setAttribute("student", student);
		JSONObject.fromObject(student).toString();
		request.getSession().setAttribute("student", student);
		return editJson;
	}

	public Object updateUserPwd(String pwdOld, String pwdNew, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		// 验证旧密码是否正确
		if (!student.getPassword().equals(MD5Util.MD5(pwdOld))) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("原始密码不正确");
			return editJson;
		}
		userDao.updateUserPwd(MD5Util.MD5(pwdNew), student.getStudentId());
		return editJson;
	}

	public Object saveProductShowArea(String[] schoolIds, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		// 先清空现有配置
		userDao.deleteProductShowArea(student.getStudentId());
		for (int i = 0; i < schoolIds.length; i++) {
			String schoolId = schoolIds[i];
			userDao.saveProductShowArea(schoolId, student.getStudentId());
		}
		return editJson;
	}

	public Object saveProductsShowYear(String[] years, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		// 先清空现有配置
		userDao.deleteProductShowYear(student.getStudentId());
		for (int i = 0; i < years.length; i++) {
			String year = years[i];
			userDao.saveProductShowYear(year, student.getStudentId());
		}
		return editJson;
	}

	@Transactional(readOnly = false)
	public Object findResetPwd(String pwd, String email, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Map map = (Map) request.getSession().getAttribute("couldUpdatePwd");
		if (map == null) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("非法操作");
			return editJson;
		}
		String emailOld = (String) map.get("email");
		if (!emailOld.equals(email)) {
			editJson.setSuccess(false);
			editJson.getMess().setMessage("非法操作,邮箱不一致");
			return editJson;
		}
		Student student = studentDao.findStudentByEmail(email);
		userDao.updateUserPwd(MD5Util.MD5(pwd), student.getStudentId());
		request.removeAttribute("couldUpdatePwd");
		return editJson;
	}

	@Transactional(readOnly = false)
	public Object findApplyResiter(ApplyRegister applyRegister) {
		EditJson editJson = new EditJson();
		userDao.findApplyResiter(applyRegister);
		return editJson;
	}

	/**
	 * 用户反馈
	 * 
	 * @param feedbackTitle
	 * @param feedbackContent
	 * @param request
	 * @return
	 */
	public Object saveFeedback(Feedback feedback, HttpServletRequest request) {
		EditJson editJson = new EditJson();
		Student student = (Student) request.getSession().getAttribute("student");
		feedback.setStudentId(student.getStudentId());
		Object saveFeedback = feedbackDao.saveFeedback(feedback);
		editJson.setBean(saveFeedback);
		return editJson;
	}
}
