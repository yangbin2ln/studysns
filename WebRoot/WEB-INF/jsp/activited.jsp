<%@ page language="java" import="java.util.*,java.net.URL"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
</head>
<div>
	邮箱已经激活，<span id='content'><span id='s' style='color:red'>3</span>秒后将自动跳转注册页面,若无跳转请点击<a
		href='/#register'>这里</a>
	</span>
</div>
<script>
	var int = setInterval(function() {
		changeNumber();
	}, 1000);
	setTimeout(function() {
		clearInterval(int);
		var s = document.getElementById('content');
		s.innerHTML = '正在跳转...';
		window.location.href = '/user/toLogin/#register';
	}, 3000);
	function changeNumber() {
		var s = document.getElementById('s');
		s.innerHTML = JSON.parse(s.innerHTML) - 1
	}
</script>
</html>