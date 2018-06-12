<%@ page language="java" import="java.util.*,java.net.URL"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!doctype html>
<HTML>
<HEAD>
<TITLE>探索 - T-Show</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/find/find.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/jquery.easing.1.3.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/find/find.js"></script>
<script>
	$(document).ready(function() {
		addTrag(".move");
		stopClick(".box,.move");
		kedadian();
		pointSubmit();
		initMessage();
		initUploadProduct();
		initSearch();
		initFind();
		$(document).data({
			student : ${studentJSON}
		});
	});
</script>
<style type="text/css">
</style>
</HEAD>

<BODY>
	<div class="header" id="header">
		<div class="header-l">
			<ul>
				<li><a href="/"> <span class="logo"></span> </a>
				</li>
				<li><a href="/contend"> <span class="nav">争鸣</span> </a>
				</li>
				<li><a href="/find"> <span class="nav">探索</span> </a>
				</li>
				<li><a href="#"> <span class="nav feedback-nav">反馈</span> </a>
				</li>
			</ul>
		</div>
		<div class="header-c">
			<span>探索</span>
		</div>
		<div class="header-r">
			<ul>
				<li><a href="#"> <span id='sear' class="sear"></span> </a></li>
				<li><span>|</span></li>
				<li><a href="#"> <span class="add" id="upload-but"></span>
				</a></li>
				<c:if test="${loginningstudent!=null}">
					<li><a href="#" id="message-but"> <span class="mes"></span><span
							class="message-but-count"></span> </a></li>
					</li>
					<li id='set-up-li' style='position:relative'><a
						href="/student/home/${loginningstudent.invitationId}"> <c:choose>
								<c:when
									test="${fn:trim(loginningstudent.headIco)==''||empty loginningstudent.headIco}">
									<span class="set-up-head-ico">
										${fn:substring(student.realName,fn:length(student.realName)-1,fn:length(student.realName))}
									</span>
								</c:when>
								<c:otherwise>
									<span class="set-up-head-ico" style='background: none;'>
										<img src='${loginningstudent.headIco}'> </span>
								</c:otherwise>
							</c:choose> </a>
						<div id='set-up'>
							<div>
								<a href='/student/home/${loginningstudent.invitationId}'>个人主页</a>
							</div>
							<div id='set-up-sc' data-id='${loginningstudent.school.schoolId}'>
								<a href='/school/home/${loginningstudent.school.schoolId}'>${loginningstudent.school.name}</a>
							</div>
							<div>
								<a href='/user/setup'>个人设置</a>
							</div>
							<div id='set-up-quit'>
								<a>退出</a>
							</div>
						</div></li>
				</c:if>
				<c:if test="${loginningstudent==null}">
					<li><a href="/user/toLogin/#login"> <span class="login">登陆</span>
					</a>
					</li>
					<li><a href="/user/toLogin/#register"> <span class="signup">注册</span>
					</a>
					</li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="search-view" id="search-view">
		<div class="search-view-list">
			<!--  
			<h3>热门推荐</h3>
			<div class='search-view-list-commend' id='recommend'>
				<ul>
				</ul>
			</div>-->
			<h3>最新解锁标签</h3>
			<div class="search-view-list-table" id="labels-new">
				<ul>
				</ul>
				<a class="more">显示更多</a>
			</div>
			<div class="search-view-list" id='labels-fir'></div>
		</div>
	</div>
	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
</BODY>
</HTML>
