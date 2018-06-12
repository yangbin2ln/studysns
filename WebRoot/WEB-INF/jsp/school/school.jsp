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
<TITLE>${school.name} - T-Show</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/school/school.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/jquery.easing.1.3.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/school/school.js"></script>
<script>
	$(document).ready(function() {
		addTrag(".move");
		stopClick(".box,.move");
		kedadian();
		pointSubmit();
		initHeight();
		initMessage();
		initUploadProduct();
		initSearch();
		initSchool(${school.schoolId});
		$(document).data({"param":{schoolId:${school.schoolId}}});
		$(document).data({"student":${studentJSON}});
;	})
</script>
<style type="text/css">
</style>
</HEAD>

<BODY>
	<div class="header" id='header'>
		<div class="header-l">
			<ul>
				<li><a href="/"> <span class="logo"></span> </a></li>
				<li><a href="/contend"> <span class="nav">争鸣</span> </a>
				</li>
				<li><a href="/find"> <span class="nav">探索</span> </a></li>
				<li><a href="#"> <span class="nav feedback-nav">反馈</span> </a></li>
			</ul>
		</div>
		<div class="header-c" style="display:none">
			<img src="${school.badge}">
		</div>
		<div class="header-r">
			<ul>
				<li><a href="#"> <span id='sear' class="seared"></span> </a>
				</li>
				<li><span>|</span>
				</li>
				<li><a href="#"> <span class="added" id="upload-but"></span>
				</a>
				</li>
				<c:if test="${loginningstudent!=null}">
					<li><a href="#" id="message-but"> <span class="mesed"></span><span
							class="message-but-count"></span> </a>
					</li>
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
						</div>
					</li>
				</c:if>
				<c:if test="${loginningstudent==null}">
					<li><a href="/user/toLogin/#login"> <span
							class="login">登陆</span> </a></li>
					<li><a href="/user/toLogin/#register"> <span
							class="signup">注册</span> </a></li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="zx">

		<div class="zx-h" style='background-image:url("${school.backImaB}");'>
			<div class="sch-m">
				<div class="sch-logo">
					<img src="${school.badge}">
				</div>
				<div class="sch-name" data-id='${school.schoolId}'>${school.name}</div>
				<div class='sch-m-s-p' id='sch-m-s-p'>
					<div>
						<input class='school-search-inp' id="school-search-inp"
							type="search" placeholder="搜索标签、用户">
						<ul class='search-list-ul' id='search-list-ul'>

						</ul>
					</div>
					<p>您可以通过搜索：标签、姓名、ID，查找您想要的</p>
				</div>
			</div>
		</div>
		<div class="zx-b">
			<div class="zx-b-nav">
				<ul>
					<li class="l zx-b-nav-col" data-val='products'><a>作品</a></li>
					<li class="c" data-val='labels'><a>标签</a></li>
					<li class="r" data-val='students'><a>校友</a></li>
				</ul>
			</div>
			<div class="zx-b-list">
				<div class='zx-b-list-pro'>
					<div class='zxc-ul'>
						<ul></ul>
						<ul></ul>
						<ul></ul>
						<ul></ul>
					</div>
				</div>
				<div class="zx-b-list-table" style="display:none">
					<span class="zx-b-list-table-nav">最新﹀</span> <span
						class="zx-b-list-table-nav">最热﹀</span>
					<ul>
					</ul>
				</div>
				<div class="zx-b-list-user" style="display:none">
					<ul>

					</ul>
				</div>

			</div>
		</div>
	</div>
	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
</BODY>
</HTML>
