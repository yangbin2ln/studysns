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
<TITLE>${school.name} - ${label.labelName} - T-Show</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/school/schoolLabel.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/jquery.easing.1.3.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/school/schoolLabel.js"></script>
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
			<a target='_Blank' href="/school/home/${school.schoolId}"
				class='header-c-bad'><img src='${school.badge}'> </a> <span
				class='header-c-sc' data-id='${school.schoolId}'>${school.name}</span>
			<span class='header-c-la' style='position:relative'
				data-id='${label.labelId}'><c:if test="${empty label}">
					<span>(全部)</span>
				</c:if> <c:if test="${!empty label}">
					<span>(${label.labelName})</span>
				</c:if> <!-- <span class='qhschool'>V</span> --> <!-- start 学校列表 -->
				<div id='school-ql-m'>
					<ul>
					</ul>
					<div>
						<input>
						<button class='school-ql-m-s'>搜索</button>
					</div>
				</div> <!-- end   学校列表 --> </span>
			<c:if test="${!empty point}">
				<span class='header-c-po-s'><span class='header-c-po-l'
					data-id='${point.pointId}'>${point.pointName}</span><span
					class='la-point-close'>X</span> </span>
			</c:if>
			<span class='header-c-po tshow-hide' data-id='${point.pointId}'>${point.pointName}</span>
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


	<!--内容导航-->
	<div class="zl-table">
		<div class='points-prev'>&lt</div>
		<div class="zl-table-k">
			<ul>
			</ul>
		</div>
		<div class='points-next'>&gt</div>
	</div>

	<div class="zx-table-nav">
		<ul>
			<li class="l zx-b-nav-col" data-val='product'><a>作品</a></li>
			<li class="r" data-val='student'><a>校友</a></li>
		</ul>
	</div>
	<!--标签·打开·热门人物-->
	<div class="zl-r  tshow-hide">
		<div class="master">
			<div class='stu-count'></div>
			<h3>达人推荐</h3>
			<div class='students-prev'>&lt</div>
			<div class='master-pul'>
				<ul class='master-pul-ul'></ul>
			</div>
			<div class='students-next'>&gt</div>
		</div>
		<div class="zx-b-list-user">
			<ul>
			</ul>
		</div>
	</div>
	<div class="zlc">
		<div class="zlc-nav">
			<span class='zlc-nav-black' data-val='time'>最新﹀</span> <span
				data-val='score'>最热﹀</span> <i class='pro-count'>作品数(${productsCount})</i>
		</div>
		<div class='zlc-ul '></div>
	</div>
	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
</BODY>
</HTML>