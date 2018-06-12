<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="header" id="header">
	<div class="header-l">
		<ul>
			<li><a href="/"> <span class="logo"></span> </a></li>
			<li><a href="/contend"> <span class="nav">争鸣</span> </a></li>
			<li><a href="/find"> <span class="nav">探索</span> </a></li>
			<li><a href="#"> <span class="nav feedback-nav">反馈</span> </a>
			</li>
		</ul>
	</div>
	<div class="header-c">个人中心</div>
	<div class="header-r">
		<ul>
			<li><a href="#"> <span id='sear' class="sear"></span> </a>
			</li>
			<li><span>|</span>
			</li>
			<li><a href="#"> <span class="add" id="upload-but"></span> </a>
			</li>

			<c:if test="${loginningstudent!=null}">
				<li><a href="#" id="message-but"> <span class="mes"></span><span
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
								<span class="set-up-head-ico"  style='background: none;'> <img
									src='${loginningstudent.headIco}'>
								</span>
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