package com.eeesns.tshow.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sparknet.starshine.common.util.StringUtil;

import com.eeesns.tshow.common.json.Message;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.dao.Page;
import com.eeesns.tshow.dao.StudentDao;
import com.eeesns.tshow.entity.Product;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.StudentService;

@Controller
@RequestMapping(value = "student")
public class StudentController {
	@Resource
	private StudentService studentService;
	@Resource
	private StudentDao studentdao;

	@RequestMapping(params = "saveStudent")
	public String saveStudent(Student stu, HttpServletRequest request) {
		studentService.insertLabel(stu);
		studentService.insertSchoolLabel(stu);
		studentService.saveStudent(stu);
		JSONObject object = JSONObject.fromObject(stu);
		System.out.println(object.toString());
		return "success";

	}

	@ResponseBody
	@RequestMapping(params = "findStudentById")
	public Object findStudentById(HttpServletRequest request, HttpServletResponse response,
			String studentId) throws ServletException, IOException {
		Student stu = studentService.findStudentById(studentId);
		return stu;
	}

	@ResponseBody
	@RequestMapping(params = "findProductById")
	public Object findProductById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Product stu = studentService.findProductById();
		return stu;

	}

	/**
	 * 关注人
	 * 
	 * @param toStudentId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveAttention")
	public Object saveAttention(String toStudentId, HttpServletRequest request) {
		Object object = studentService.saveAttention(toStudentId, request);
		return object;
	}

	/**
	 * 取消关注人
	 * 
	 * @param toStudentId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAttention")
	public Object deleteAttention(String toStudentId, HttpServletRequest request) {
		Object object = studentService.deleteAttention(toStudentId, request);
		return object;
	}

	// 消息
	/**
	 * 当前用户的自己作品的消息查询(即根据last_act_time排序作品)
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMessage/product/list")
	public Object findMessageProductList(Page page, HttpServletRequest request) {
		Object object = studentService.findMessageProductList(page, request);
		return object;
	}

	/**
	 * 查询自己单个作品的消息(回复，赞，收藏，投票)
	 * 
	 * @param productId
	 * @param type
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMessage/product/info")
	public Object findMessageProductInfo(String productId, String type, HttpServletRequest request) {
		Object object = studentService.findMessageProductInfo(productId, type, request);
		return object;
	}

	/**
	 * 查询用户参与的消息详情（被二级回复）
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMessage/reply/list")
	public Object findMessageReplyList(Page page, HttpServletRequest request) {
		Object object = studentService.findMessageReplyList(page, request);
		return object;
	}

	/**
	 * 查询所有的粉丝
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMessage/flower/list")
	public Object findMessageStudentFlowerList(Page page, HttpServletRequest request) {
		Object object = studentService.findMessageStudentFlowerList(page, request);
		return object;
	}

	/**
	 * 查询企业以及达人用户的学生
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMessage/student/list")
	public Object findMessageStudentStudentList(Page page, HttpServletRequest request) {
		Object object = studentService.findMessageStudentStudentList(page, request);
		return object;
	}

	/**
	 * 查询自己参与的消息详情(根据作品查询)
	 * 
	 * @param productId
	 * @param type
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMessage/reply/info")
	public Object findMessageReplyInfo(String productId, String type, HttpServletRequest request) {
		Object object = studentService.findMessageReplyInfo(productId, type, request);
		return object;
	}

	/**
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findMessage/count")
	public Object findMessageCount(HttpServletRequest request) {
		if (request.getSession().getAttribute("student") == null) {
			Message me = new Message();
			me.setMessage("无权限访问消息");
			Map m = new HashMap();
			m.put("failed", me);
			return m;
		}
		Object replyCount = studentService.findMessageReplyCount(request);
		Object productCount = studentService.findMessageProductCount(request);
		Object studentStudentCount = studentService.findMessageStudentStudentCoun(request);
		Object flowerCount = studentService.findMessageStudentFollowerCoun(request);
		Map map = new HashMap();
		map.put("messageCount", (Integer) replyCount + (Integer) productCount
				+ (Integer) studentStudentCount + (Integer) flowerCount);
		map.put("replyCount", (Integer) replyCount);
		map.put("productCount", (Integer) productCount);
		map.put("studentStudentCount", (Integer) studentStudentCount);
		map.put("flowerCount", (Integer) flowerCount);
		return map;

	}

	/**
	 * 查询个人中心
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(value = "")//将个人中心挂在域名下 public Object
	 * findPersonCenterJsp(HttpServletRequest request) { Student student =
	 * (Student) request.getSession().getAttribute("student"); if (student ==
	 * null) { return "jsp/login"; } request.setAttribute("studentJSON",
	 * JSONObject.fromObject(student).toString());
	 * request.setAttribute("loginningstudent", student); return
	 * "jsp/student/personCenter";
	 * 
	 * }
	 */

	/**
	 * 查询个人中心数据
	 * 
	 * @param type
	 *            全部、关注、（标签）
	 * @param labelId
	 *            标签ID
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/center/list")
	public Object findPersonCenter(String type, String labelId, Page page,
			HttpServletRequest request) {
		Object object = studentService.findPersonCenter(type, labelId, page, request);
		return object;
	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/publish") public Object saveProduct(Product
	 * product, HttpServletRequest request) { Object state =
	 * studentService.saveProduct(product, request); return state; }
	 */
	/**
	 * 查询当前用户动态
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	public Object findAct(Page page, HttpServletRequest request) {
		Object object = studentService.findAct(page, request);
		return object;
	}

	/**
	 * 查询当前用户点评动态
	 * 
	 * @param page
	 * @param request
	 * @return
	 */
	public Object findYjAct(Page page, HttpServletRequest request) {
		Object object = studentService.findYjAct(page, request);
		return object;
	}

	/**
	 * 个人主页
	 * 
	 * @param id
	 *            邀请码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/home/{id}", method = RequestMethod.GET)
	public Object findHomeByStudentId(@PathVariable("id") String id, HttpServletRequest request) {
		// 直接过滤掉非法请求
		if (request.getServletPath().indexOf(".") != -1) {
			return "jsp/404";
		}
		Student student = studentService.findStudentByInvitationId(id);
		if (student == null) {
			return "jsp/404";
		}
		// 更新此用户的被访问量
		studentService.saveViewCount(student);
		request.setAttribute("student", student);
		Student stu = (Student) request.getSession().getAttribute("student");
		if (stu != null) {
			request.setAttribute("loginningstudent",
					(Student) request.getSession().getAttribute("student"));
			request.setAttribute("studentJSON", JSONObject.fromObject(stu).toString());
			// 查询当前用户是否已经关注被访问的用户
			boolean boo = studentdao.findIsAttention(stu.getStudentId(), student.getStudentId());
			request.setAttribute("isAttention", boo);

		} else {
			request.setAttribute("loginningstudent", null);

			request.setAttribute("studentJSON", false);
			request.setAttribute("isAttention", false);
		}
		return "jsp/student/studentHome";
	}

	/**
	 * 个人主页作品列表
	 * 
	 * @param studentId
	 *            用户id
	 * @param type
	 *            自己作品/参与作品/收藏作品
	 * @param labelId
	 *            标签
	 * @param page
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/home/product/list")
	public Object findHomeProducts(String studentId, String type, String labelId, Page page,
			HttpServletRequest request) {
		Object object = studentService.findHomeProducts(studentId, type, labelId, page, request);
		return object;
	}

	@ResponseBody
	@RequestMapping(value = "/home/student/list")
	public Object findHomeStudents(String studentId, String type, Page page,
			HttpServletRequest request) {
		Object object = studentService.findHomeStudents(studentId, type, page, request);
		return object;
	}

	/**
	 * 普通学生加老师(type为'QY'的用户)
	 * 
	 * @param invitationId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addTeacher")
	public Object saveStudentStudent(String invitationId, String telephone,
			HttpServletRequest request) {
		Object object = studentService.saveStudentStudent(invitationId, telephone, request);
		return object;
	}

	/**
	 * 老师(type=='QY'的用户)处理学生请求
	 * 
	 * @param studentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/dealStudentApply")
	public Object saveStudentStudentMessage(String studentId, String type,
			HttpServletRequest request) {
		Object object = studentService.saveStudentStudentMessage(studentId, type, request);
		return object;
	}
}
