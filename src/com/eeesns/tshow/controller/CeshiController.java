package com.eeesns.tshow.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eeesns.tshow.common.json.EditJson;
import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.entity.Student;
import com.eeesns.tshow.service.CeshiService;
import com.eeesns.tshow.service.ContendService;

@Controller
@RequestMapping(value = "ceshi")
public class CeshiController {
	@Resource
	CeshiService ceshiService;
	@Resource
	BaseDao baseDao;

	@ResponseBody
	@RequestMapping(value = "/updateStudent")
	public Object updateStudent(HttpServletRequest request) {
		Object object = ceshiService.updateStudent(request);
		return object;
	}

	@RequestMapping(value = "/login")
	public Object login(HttpServletRequest request, HttpServletResponse response) {
		// 测试 ：往session中存储当前用户的信息
		HttpSession session = request.getSession();
		if (session.getAttribute("student") == null) {
			Student student = (Student) baseDao.findEntity(Student.class,
					"14390187826312227924401064798190");
			session.setAttribute("student", student);
			JSONObject.fromObject(student).toString();
		}
		addCookie(request, response);
		return "login success";
	}

	private void addCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		boolean flag = false;
		if (cookies == null) {
			Cookie c = new Cookie("loginName", "00000");
			c.setMaxAge(3600 * 24 * 30);
			response.addCookie(c);
			return ;
		}
		for (Cookie cookie : cookies) {
			if ("login".equals(cookie.getName())) {
				flag = true;
			}
			System.out.println(cookie.getComment());
			System.out.println(cookie.getDomain());
			System.out.println(cookie.getMaxAge());
			System.out.println(cookie.getName());
			System.out.println(cookie.getPath());
			System.out.println(cookie.getSecure());
			System.out.println(cookie.getValue());
			System.out.println(cookie.getVersion());
		}
		if (!flag) {
			Cookie c = new Cookie("loginName", "00000");
			c.setMaxAge(3600 * 24 * 30);
			response.addCookie(c);
		}
	}
}
