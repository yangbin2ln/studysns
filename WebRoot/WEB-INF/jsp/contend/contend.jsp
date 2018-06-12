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
<HEAD>
<TITLE>争鸣 - T-Show</TITLE>
<meta name="renderer" content="webkit">
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<style type="text/css">
* {
	padding: 0;
	margin: 0;
}
</style>
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/contend/zm.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/contend/waterfall.js"></script>
<script>
	$(document)
			.ready(
					function() {
						var sUserAgent = navigator.userAgent.toLowerCase();
						var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
						var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
						var bIsMidp = sUserAgent.match(/midp/i) == "midp";
						var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
						var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
						var bIsAndroid = sUserAgent.match(/android/i) == "android";
						var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
						var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";

						if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7
								|| bIsUc || bIsAndroid || bIsCE || bIsWM) {//如果是上述设备就会以手机域名打开
							//alert("手机端" + sUserAgent)
						} else {//否则就是电脑域名打开
							//alert("电脑端")
						}
						addTrag(".move");
						stopClick(".box,.move");
						kedadian();
						pointSubmit();
						initMessage();
						initUploadProduct();
						initSearch();
					});
	//	直接在jsp中将当前用户信息写入js变量中,以便于前台页面信息的展示
		$(document).data({
			student : ${studentJSON}
		});
</script>
</HEAD>

<BODY style="overflow:scroll">
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
		<div class="header-c">争鸣</div>
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

	<!--检索栏-->
	<div class="find-items">
		<div id='items-p'>
			<a href="javascript:;"><span id='most-new'
				class="btnText bac-white">最新</span> </a> <a href="javascript:;"><span
				id='most-hot' class="btnText">最热</span> </a>
		</div>
	</div>
	<!--瀑布流-->
	<div id="mainContainer">
		<div id="pul-main"></div>
	</div>
	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
</BODY>
</HTML>