<%@ page language="java"
	import="java.util.*,com.eeesns.tshow.entity.Student"
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
<TITLE>${loginningstudent.realName}的个人中心 - T-Show</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/student/personCenter.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/student/personCenter.js"></script>
<script>
	$(document).ready(function() {
		$(document).data({
			student : ${studentJSON}
		});
		addTrag(".move");
		stopClick(".box,.move");
		kedadian();
		pointSubmit();
		initHeight();
		initMessage();
		initUploadProduct();
		initSearch();
		//加载个人中心数据
		initPersonCenter();
	});
</script>
<style type="text/css">
body {
	background: #F5F8FA;
}
</style>
</HEAD>

<BODY>
	<jsp:include page="../common/header.jsp"></jsp:include>
	<div class="Personal-Center">
		<div class="Personal-Center-nav">
			<ul>
				<li class="center-data-type active-bot" data-type='all'><a>全部</a></li>
				<li class="center-data-type" data-type='attention'><a>关注</a></li>
					<c:if test="${loginningstudent.role.roleType=='company_manager_user'||loginningstudent.role.roleType=='company_common_user'||loginningstudent.role.roleType=='nc_company_common_user'||loginningstudent.role.roleType=='nc_company_manager_user'}">
						<li class="center-data-type" data-type='studentwbm'><a>未报名学生</a>
						</li>
						<li class="center-data-type" data-type='studentybm'><a>已报名学生</a>
						</li>
						<li class="center-data-type" data-type='studentyby'><a>已毕业学生</a>
						</li>
					</c:if>
			</ul>
		</div>
		<div class="Personal-Center-c">
			<ul id="Personal-Center-c-ul">
			</ul>
			<div class='more-data tshow-hide'></div>
		</div>
	</div>
	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
</BODY>
</HTML>
