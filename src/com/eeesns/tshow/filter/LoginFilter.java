package com.eeesns.tshow.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eeesns.tshow.dao.BaseDao;
import com.eeesns.tshow.entity.Student;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		checkCookie(req);
		chain.doFilter(req, response);

	}

	private void checkCookie(HttpServletRequest req) {
		System.out.println("filter: "+req.getRequestURL());
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(req
				.getSession().getServletContext());
		BaseDao baseDao = (BaseDao) wac.getBean("baseDao");
		if (req.getSession().getAttribute("student") == null) {
			Cookie[] cookies = req.getCookies();
			if (cookies == null) {
				return;
			}
			for (Cookie cookie : cookies) {
				if ("loginName".equals(cookie.getName())) {// 存在cookie，自动登陆
					String studentId = cookie.getValue();
					Student student = (Student) baseDao.findEntity(Student.class, studentId);
					if (student == null) {
						cookie.setMaxAge(0);// 立即删除cookie
					} else {
						JSONObject.fromObject(student).toString();
						req.getSession().setAttribute("student", student);
					}

				}
			}
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
