package com.eeesns.tshow.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.dao.Page;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.ContendService;
import com.eeesns.tshow.service.StudentService;

/**
 * @author yb 争鸣
 * 
 */
@Controller
@RequestMapping(value = "contend")
public class ContendController {
	@Resource
	ContendService contendService;
	@Resource
	StudentService studentService;

	/**
	 * 查询最新解锁的学校标签
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findSchoolLabelUnlock")
	public Object findSchoolLabelUnlock(HttpServletRequest request) {
		EditJson editJson = contendService.findSchoolLabelUnlock();
		return editJson;
	}

	/**
	 * 查询所有的作品信息，不包括评论
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findAllProducts")
	public Object findAllProducts(Page page, String type, HttpServletRequest request) {
		EditJson editJson = contendService.findAllProducts(page,type,
				request);
		return editJson;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String findJsp(HttpServletRequest request) {
		if (request.getSession().getAttribute("student") == null) {
			request.setAttribute("studentJSON", false);
			request.setAttribute("loginningstudent", null);
		} else {
			Student student = (Student) request.getSession().getAttribute("student");
			request.setAttribute("studentJSON", JSONObject.fromObject(student).toString());
			request.setAttribute("loginningstudent", student);
		}

		return "jsp/contend/contend";
	}
}
