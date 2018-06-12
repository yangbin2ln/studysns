<%@ page language="java" import="java.util.*,java.net.URL"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
%>
<!--头部描述浏览器所需要的信息-->
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>tshow-荒芜人烟</title>
<meta name="Keywords" content="关键词,关键词">
<meta name="description" content="">

<!--css,js-->
<!--css:层叠样式表-->
<style type="text/css">
* {
	margin: 0px;
	padding: 0px;
}

body {
	background: #eff2f5;
}

.text {
	text-align: center;
	font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
	font-size: 17.6000003814697px;
	font-style: normal;
	font-variant: normal;
	font-weight: bold;
	padding: 20px;
}

#error {
	border: 1px solid #babbbc;
	border-radius: 5px;
	background: #f7f7f7;
	text-align: center;
}

#main {
	margin: 30px auto;
    width: 400px;
}

img {
	max-width:100%;
}

#fof {
    padding-left: 20px;
    font-size: 30px;
}
</style>
</head>
<body>
	<div id='main'>
		<div class='text'>
			这是个荒芜人烟的地方 <span id='fof'>404</span>
		</div>
		<div id="error">
		<img src='<%=path%>/img/hwry.jpg'>
		</div>
	</div>
</body>
</html>