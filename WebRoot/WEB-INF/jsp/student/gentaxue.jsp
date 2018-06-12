<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + path + "/";
%>
<div id='gtx' style='display:none'>
	<div class='tshow-zzc' style='display:block'></div>
	<div class="phg-enroll">
		<div class="phg-enroll-title">跟他学</div>
		<div class="phg-enroll-nav">
			<span>报名</span> <span class='phg-enroll-nav-col'>公开课</span>
		</div>
		<div class="phg-enroll-nav-c">
			<div class="phg-enroll-nav-c-left" style="display:none">
				<div class="phg-enroll-nav-c-left-b">
					<input class='phg-enroll-nav-c-left-b-tel' type="text" placeholder="手机号码">
					<textarea class='phg-enroll-nav-c-left-b-text' placeholder="备注"></textarea>
					<button id='submit-bm'>提交报名信息</button>
				</div>
				<div class="phg-enroll-nav-c-left-f">
					<h3>报名能干什么？</h3>
					<ol>
						<li>可以让你的作品获得老师的点评</li>
						<li>可以获取老师的作品</li>
						<li>可以获取老师线下公开课的名额</li>
					</ol>
					<h4 style='color:red;display:inline-block'>温馨提示：</h4>
					<span style='font-size:14px;color:red'>首次参加的请先提交报名信息</span>

				</div>
			</div>
			<div class="phg-enroll-nav-c-right">
				<div class="phg-enroll-nav-c-right-h">
					<h3>公开课是什么？</h3>
					<p>公开课是老师为了与同学更好的交流、探讨，开设的线上课程体验，旨在让学生更好的了解本科目，了解老师，在活动中不断的提高自己，认识更多的朋友。</p>
				</div>
				<div id='pencrb' class="phg-enroll-nav-c-right-b">
					<ul>
						 
					</ul>
				</div>
				<div class="phg-enroll-nav-c-right-f"></div>
			</div>

		</div>
	</div>
</div>