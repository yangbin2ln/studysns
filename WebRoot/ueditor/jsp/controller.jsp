<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
	request.setCharacterEncoding("utf-8");
	response.setHeader("Content-Type", "text/html");

	String rootPath = application.getRealPath("/");
	System.out.println(rootPath);
	String rootPath2 = application.getRealPath("");
	System.out.println(rootPath2);
	String rootPath3 = application.getRealPath("/tShow");
	System.out.println(rootPath3);
	out.write(new ActionEnter(request, rootPath).exec());
%>