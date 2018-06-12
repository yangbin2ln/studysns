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
<TITLE>${label.labelName} - T-Show</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/label/label.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/jquery.easing.1.3.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/label/label.js"></script>
<style type="text/css">
</style>
<script>
$(document)
			.ready(
					function() {
						addTrag(".move");
						stopClick(".box,.move");
						kedadian();
						pointSubmit();
						initHeight();
						initMessage();
						initUploadProduct();
						initSearch();
						initLabel('${label.labelId}','${point.pointId}','${school.schoolId}');
					});
	//	直接在jsp中将当前用户信息写入js变量中,以便于前台页面信息的展示
		$(document).data({
			student : ${studentJSON},
			params:{labelId:'${label.labelId}',pointId:'${point.pointId}',schoolId:'${school.schoolId}'}
		});
</script>
</HEAD>

<BODY style="zoom: 1;">
	<div class="header back-none" id="header">
		<div class="header-l">
			<ul>
				<li><a href="/"> <span class="logo"></span> </a>
				</li>
				<li><a href="/contend"> <span class="nav col-white">争鸣</span>
				</a>
				</li>
				<li><a href="/find"> <span class="nav col-white">探索</span>
				</a>
				</li>
				<li><a href="#"> <span class="nav col-white feedback-nav">反馈</span> </a>
				</li>
			</ul>
		</div>
		<div class="header-c">
			<span class='header-c-la nav col-white' data-id='${label.labelId}'>${label.labelName}</span>
			<span class='header-c-sc' style='position:relative'
				data-id='${school.schoolId}'> <span
				class='qhschool nav col-white select-inco-ts'></span> <c:if
					test="${empty school}">
					<span class='nav col-white'>全部学校</span>
				</c:if> <c:if test="${!empty school}">
					<span class='nav col-white'>(${school.name})</span>
				</c:if> <!-- start 学校列表 -->
				<div id='school-ql-m'>
					<ul>
					</ul>
					<div class='school-ql-m-s-p'>
						<input> <span class='school-ql-m-s school-find-icon'></span>
					</div>
				</div> <!-- end   学校列表 --> </span>
			<c:if test="${!empty point}">
				<span class='header-c-po-s'><span class='header-c-po-l'
					data-id='${point.pointId}'>${point.pointName}</span><span
					class='la-point-close'>×</span> </span>
			</c:if>
			<span class='header-c-po tshow-hide' data-id='${point.pointId}'>${point.pointName}</span>
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
					<li><a href="/user/toLogin/#login"> <span
							class="login">登陆</span> </a></li>
					<li><a href="/user/toLogin/#register"> <span
							class="signup">注册</span> </a></li>
				</c:if>
			</ul>
		</div>
	</div>
	<!--标签打开设计-->
	<div id="zl" class="zl">
		<div class="table-h"
			style='background-image:url("${label.backImaB}");'>
			<div class="table-h-box">
				<div class="table-h-tn">
					<div class="table-h-tn-1">
						<h2>${label.labelName}</h2>
						<div class="table-h-tn-a">
							<span>用户${label.studentCount}</span> <span>|</span> <span>作品${label.productCount}</span>
						</div>
					</div>
				</div>
				<div class="table-h-f">
					<h3>向下滚动查看更多 ﹀</h3>
				</div>
			</div>
		</div>
	</div>

	<!--内容导航-->
	<div class="zl-table">
		<div class='points-prev'>&lt</div>
		<div class="zl-table-k">
			<ul>
			</ul>
		</div>
		<div class='points-next'>&gt</div>
	</div>
	<!--标签·打开·热门人物-->
	<div class="zl-r" style='background-image:url("${label.backImaB}");'>
		<div class="master">
			<h3>达人推荐</h3>
			<div class='students-prev'>&lt</div>
			<div class='master-pul'>
				<ul class='master-pul-ul'></ul>
			</div>
			<div class='students-next'>&gt</div>
		</div>
	</div>
	<div class="zlc">
		<div class="zlc-nav">
			<span class='zlc-nav-black' data-val='time'>最新﹀</span> <span
				data-val='score'>最热﹀</span>
		</div>
		<div class='zlc-ul '>
			<ul></ul>
			<ul></ul>
			<ul></ul>
			<ul></ul>
		</div>
	</div>
	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
</BODY>
</HTML>