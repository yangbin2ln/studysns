<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<HTML>
<HEAD>
<TITLE>${student.realName} - T-Show</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/student/studentHome.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/common/tshowUtil.js"></script>
<script src="<%=path%>/js/student/studentHome.js"></script>
<style type="text/css">
body {
	background: #ffffff;
}
</style>
<script>
	$(document).ready(function() {
		addTrag(".move");
		stopClick(".box,.move");
		kedadian();
		pointSubmit();
		initMessage();
		initSearch();
		initHeight();
		initUploadProduct();
		studentHome();
	})
	//	直接在jsp中将当前用户信息写入js变量中,以便于前台页面信息的展示
		$(document).data({
			student : ${studentJSON}
		});
</script>
</HEAD>

<BODY>
	<div class="header" id="header">
		<div class="header-l">
			<ul>
				<li><a href="/"> <span class="logo"></span> </a></li>
				<li><a href="/contend"> <span class="nav">争鸣</span> </a>
				</li>
				<li><a href="/find"> <span class="nav">探索</span> </a></li>
				<li><a href="#"> <span class="nav feedback-nav">反馈</span> </a></li>
			</ul>
		</div>
		<div class="header-c">
			<span id='head-stu-name'>${student.realName}</span>
			<div class='head-xlk' id='head-xlk'>
				<span id='head-xlk-span'>作品</span>
				<div>
					<ul>
						<li><a href='#anchor-phghu'><span>作品</span> </a>
						</li>
						<li><a href='#anchor-phghu'><span>参与</span> </a>
						</li>
						<li><a href='#anchor-phghu'><span>收藏</span> </a>
						</li>
						<li><a href='#anchor-phghu'><span>关注</span> </a>
						</li>
						<c:if
							test="${student.role.roleType=='company_manager_user'||student.role.roleType=='company_common_user'||student.role.roleType=='nc_company_common_user'||student.role.roleType=='nc_company_manager_user'}">
							<li><a href='#anchor-phghu'><span>学生</span> </a>
							</li>
						</c:if>
					</ul>
				</div>
			</div>
		</div>
		<div class="header-r">
			<ul>
				<li><a href="#"> <span id='sear' class="sear"></span> </a>
				</li>
				<li><span>|</span>
				</li>
				<li><a href="#"> <span class="add" id="upload-but"></span>
				</a>
				</li>
				<c:if test="${loginningstudent!=null}">
					<li><a href="#" id="message-but"> <span class="mes"></span><span
							class="message-but-count">
					</a>
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
							class="login">登陆</span> </a>
					</li>
					<li><a href="/user/toLogin/#register"> <span
							class="signup">注册</span> </a>
					</li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="phg">
		<!--完善账户-->
		<div class=""></div>
		<div class=""></div>
		<!--标签·作品列表-->
		<div class="phg-opu-x" style="display:none">
			<div class="phg-opu-x-h">
				<span></span> <span
					class='phg-opu-x-h-close tshow-abs icon-delete-s-box'></span>
			</div>
			<div class="phg-opu-x-b"></div>
		</div>
		<div class="phg-h">
			<div class="phg-h-userp">
				<c:if test="${empty loginningstudent.headIco}">
					<span>${fn:substring(loginningstudent.realName,fn:length(loginningstudent.realName)-1,fn:length(loginningstudent.realName))}</span>
				</c:if>
				<c:if test="${!empty loginningstudent.headIco}">
					<img src="${loginningstudent.headIco}">
				</c:if>

			</div>
			<div class="phg-h-usern" id='phg-h-usern'
				data-id='${student.studentId}'>
				<h1>${student.realName}</h1>
			</div>
			<div class="phg-h-schn">
				<span data-id='${student.school.schoolId}'><a target='_blank'
					href='/school/home/${student.school.schoolId}'>${student.school.name}</a>·${fn:substring(student.year,0,4)}·${student.invitationId}</span>
			</div>
			<div class="phg-h-userl">
				<span data-id='${student.profession.professionId}'>${student.profession.professionName}</span>
				<span>·</span> <span data-id='${student.jobLabel.labelId}'>${student.jobLabel.labelName}</span>
			</div>
			<div class="phg-h-userm">
				<span>${student.signature}</apan>
			</div>
			<div class="phg-h-useratt">
				<c:if test="${loginningstudent!=null}">
					<c:if
						test="${student.perfectInfo=='Y'&&student.studentId==loginningstudent.studentId}">
						<span class="phg-h-useratt-button phg-h-useratt-button-edit">修改</span>
					</c:if>
					<c:if
						test="${student.perfectInfo=='N'&&student.studentId==loginningstudent.studentId}">
						<span class="phg-h-useratt-button phg-h-useratt-button-perfect">去完善信息</span>
					</c:if>
					<c:if test="${student.studentId!=loginningstudent.studentId}">
						<c:choose>
							<c:when test="${isAttention==true}">
								<span class="phg-h-useratt-button phg-h-useratt-attented"
									data-id='${student.studentId}'>已关注</span>
							</c:when>
							<c:otherwise>
								<span class="phg-h-useratt-button phg-h-useratt-attent"
									data-id='${student.studentId}'>关注</span>
							</c:otherwise>
						</c:choose>
					</c:if>

				</c:if>
				<c:if test="${loginningstudent==null}">
					<span class="phg-h-useratt-button">+关注</span>
				</c:if>
				<div class="phg-h-useratt-list">
					<ul>
						<li>
							<p class="phg-h-useratt-list-a">${student.followerCount}</p>
							<p class="phg-h-useratt-list-n">粉丝</p>
						</li>
						<li>
							<p class="phg-h-useratt-list-a">${student.praiseCount}</p>
							<p class="phg-h-useratt-list-n">认可度</p>
						</li>
						<li>
							<p class="phg-h-useratt-list-a">${student.viewCount}</p>
							<p class="phg-h-useratt-list-n">访问量</p>
						</li>
					</ul>
				</div>
			</div>
			<div class="phg-h-usersx" id='anchor-phghu'>
				<ul>
					<li class='tshwo-bor-bot' data-name='作品' data-type='product'><a>
							<p>${student.productCount}</p>
							<p>作品</p> </a>
					</li>
					<li data-name='参与' data-type='suggestion'><a>
							<p>${student.suggestionCount}</p>
							<p>参与</p> </a>
					</li>
					<li data-name='收藏' data-type='collect'><a>
							<p>${student.collectCount}</p>
							<p>收藏</p> </a>
					</li>
					<li data-name='关注' data-type='attention'><a>
							<p>${student.attentionCount}</p>
							<p>关注</p> </a>
					</li>
					<c:if
						test="${student.role.roleType=='company_manager_user'||student.role.roleType=='company_common_user'||student.role.roleType=='nc_company_common_user'||student.role.roleType=='nc_company_manager_user'}">
						<li data-name='学生' data-type='student'><a>
								<p>${student.studentCount}</p>
								<p>学生</p> </a>
						</li>
					</c:if>
				</ul>
				<c:if
					test="${student.role.roleType=='company_manager_user'||student.role.roleType=='company_common_user'||student.role.roleType=='nc_company_common_user'||student.role.roleType=='nc_company_manager_user'||student.role.roleType=='talent_user'}">
					<div class="study-button">
						<button id='gtx-but' class="study-button-grx"
							data-id='${student.studentId}'>跟他学习</button>
					</div>
				</c:if>
			</div>
		</div>
		<div class='phg-b-list-p'>
			<div class='phg-b-list'>
				<div class="phg-b">
					<div class="phg-b-opu">
						<ul>
							<c:forEach items="${student.studentLabels}" var="studentLabel">
								<li>
									<div class="m-box" data-id='${studentLabel.label.labelId}'>
										<img src="${studentLabel.backgroundImage}">
										<p class="table" data-id='${studentLabel.label.labelId}'>${studentLabel.label.labelName}</p>
										<p class="number">作品${studentLabel.productCount}</p>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
					<div class="phg-b-col"></div>
					<div class="phg-b-pi"></div>

				</div>
			</div>
			<div class='phg-b-list tshow-hide'>
				<div class="phg-b">
					<div class="phg-b-opu">
						<ul>
							<c:forEach items="${student.studentSuggestionLabels}"
								var="studentSuggestionLabel">
								<li>
									<div class="m-box"
										data-id='${studentSuggestionLabel.label.labelId}'>
										<img src="${studentSuggestionLabel.backgroundImage}">
										<p class="table"
											data-id='${studentSuggestionLabel.label.labelId}'>${studentSuggestionLabel.label.labelName}</p>
										<p class="number">作品${studentSuggestionLabel.productCount}</p>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
					<div class="phg-b-col"></div>
					<div class="phg-b-pi"></div>

				</div>
			</div>
			<div class='phg-b-list tshow-hide'>
				<div class="phg-b">
					<div class="phg-b-opu">
						<ul>
							<c:forEach items="${student.studentCollectLabels}"
								var="studentCollectLabel">
								<li>
									<div class="m-box"
										data-id='${studentCollectLabel.label.labelId}'>
										<img src="${studentCollectLabel.backgroundImage}">
										<p class="table"
											data-id='${studentCollectLabel.label.labelId}'>${studentCollectLabel.label.labelName}</p>
										<p class="number">作品${studentCollectLabel.productCount}</p>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
					<div class="phg-b-col"></div>
					<div class="phg-b-pi"></div>

				</div>
			</div>
			<div class='phg-b-list tshow-hide'>
				<div class="phg-b-att">
					<ul>

					</ul>
				</div>
			</div>

			<c:if
				test="${student.role.roleType=='company_manager_user'||student.role.roleType=='company_common_user'||student.role.roleType=='nc_company_common_user'||student.role.roleType=='nc_company_manager_user'}">
				<div class='phg-b-list tshow-hide'>
					<div class="phg-b-att">
						<ul>

						</ul>
					</div>
				</div>
			</c:if>
		</div>
	</div>

	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
	<c:if
		test="${student.role.roleType=='company_manager_user'||student.role.roleType=='company_common_user'||student.role.roleType=='nc_company_common_user'||student.role.roleType=='nc_company_manager_user'||student.role.roleType=='talent_user'}">
		<jsp:include page="../student/gentaxue.jsp"></jsp:include>
	</c:if>
</BODY>
</HTML>
