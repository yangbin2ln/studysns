<%@ page language="java"
	import="java.util.*,java.net.URL, com.eeesns.tshow.service.UserService,
	org.springframework.web.context.WebApplicationContext,
	org.springframework.web.context.support.WebApplicationContextUtils"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
	WebApplicationContext wac = WebApplicationContextUtils
			.getRequiredWebApplicationContext(request.getSession().getServletContext());
	UserService userService = (UserService) wac.getBean("userService");
	userService.index(request, response);
%>
