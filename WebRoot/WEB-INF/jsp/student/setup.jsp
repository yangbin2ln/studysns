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
<TITLE>个人设置 - T-Show</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/common/common.css">
<link rel="stylesheet" href="<%=path%>/css/student/setup.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/common/tshowUtil.js"></script>
<script src="<%=path%>/js/student/setup.js"></script>
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
	$(document).data({
			student : ${studentJSON}
		});
		setup();
	});
</script>
<style type="text/css">
</style>
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
			<span id='head-stu-name'>个人设置</span>
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
						target="_Blank"
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
	<div class="ts-main-content">
		<div class="ts-main-content-inner">
			<div class="left">
				<ul>
					<c:forEach items="${modules}" var="module">
						<c:if test="${module=='jbzl'}">
							<li class="left-1"><a href="#box1">基本资料</a></li>
						</c:if>
						<c:if test="${module=='zhhmm'}">
							<li class="left-2"><a href="#box2">账户和密码</a></li>
						</c:if>
						<c:if test="${module=='gszhgl'}">
							<li class="left-2"><a href="#box3">公司账户管理</a></li>
						</c:if>
						<c:if test="${module=='fwxz'}">
							<li class="left-2"><a href="#box4">范围选择</a></li>
						</c:if>
						<c:if test="${module=='kcsz'}">
							<li class="left-2"><a href="#box5">课程设置</a></li>
						</c:if>
						<c:if test="${module=='yqhy'}">
							<li class="left-3"><a href="#box6">邀请好友</a></li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
			<div class="right">
				<c:forEach items="${modules}" var="module">
					<c:if test="${module=='jbzl'}">
						<div id='box1' class="Basicdata">
							<div class="Basicdata-h">
								<h3>编辑资料</h3>
							</div>
							<ul style='overflow:hidden'>
								<li>
									<h3>姓名</h3>
									<div>
										<input type="text" class="username"
											value="${loginningstudent.realName}"
											<c:if test="${loginningstudent.perfectInfo=='Y'}">disabled="true"</c:if>>

										<span class="Mark">*一经填写不能修改</span>
									</div></li>
								<li>
									<h3>头像</h3>
									<div>
										<div class="Basicdata-uerpic">
											<img src="${loginningstudent.headIco}">
										</div>
										<input id='file1' name='file1' type='file'
											style='display:none'>
										<button class="Basicdata-uerpic-fix">修改头像</button>
									</div></li>
								<li>
									<h3>学校信息</h3>
									<div style='position:relative'>
										<input type="text" id='school-info' class="userschool userjb"
											data-id='${loginningstudent.school.schoolId}'
											data-name='${loginningstudent.school.name}'
											value="${loginningstudent.school.name}"
											<c:if test="${loginningstudent.perfectInfo=='Y'}">disabled="true"</c:if>>
										<span class="Mark">*一经填写不能修改</span>
										<div class='school-list'>
											<div></div>
										</div>
									</div></li>
								<li>
									<h3>入学年份</h3>
									<div>
										<select class="userschool-year">
											<c:if test="${loginningstudent.perfectInfo=='Y'}">
												<option value='${loginningstudent.year}'>${loginningstudent.year}</option>
											</c:if>
											<c:if test="${loginningstudent.perfectInfo!='Y'}">
												<option value='2011'>2011</option>
												<option value='2012'>2012</option>
												<option value='2013'>2013</option>
												<option value='2014'>2014</option>
												<option value='2015'>2015</option>
											</c:if>
										</select> <span class="Mark">*一经填写不能修改</span>
									</div></li>

								<li>
									<h3>专业</h3>
									<div style='position:relative'>
										<input type="text" class="userProfessional userjb"
											data-id='${loginningstudent.profession.professionId}'
											value="${loginningstudent.profession.professionName}"
											<c:if test="${loginningstudent.perfectInfo=='Y'}">disabled="true"</c:if>>
										<span class="Mark">*一经填写不能修改</span>
										<div class='profe-list'>
											<div></div>
										</div>
									</div></li>
								<li>
									<h3>职业方向</h3>
									<div>
										<select name="selectJob" id="selectJob">
											<c:if test="${!empty loginningstudent.jobLabel}">
												<option selected="selected"
													value="${loginningstudent.jobLabel.labelId}">${loginningstudent.jobLabel.labelName}</option>
											</c:if>
											<c:forEach items="${labelFs}" var="labelF">
												<c:if
													test="${loginningstudent.jobLabel.labelId!=labelF.labelId}">
													<option value="${labelF.labelId}">${labelF.labelName}</option>
												</c:if>

											</c:forEach>
										</select> <span class="Mark">选择自己喜欢，擅长的领域</span>
									</div></li>
								<li>
									<h3>个性签名</h3>
									<div>
										<textarea class="userabout signature-info">${loginningstudent.signature}</textarea>
									</div></li>
								<li>
									<button class="submit-2 jbzl-sub">保存</button></li>
							</ul>
						</div>
					</c:if>
					<c:if test="${module=='zhhmm'}">
						<div id='box2' class="userAccount">
							<div class="userAccount-h">
								<h3>账户和密码</h3>
							</div>
							<ul>
								<li>
									<h3>邮箱</h3>
									<div>
										<input type="text" class="useremali userjb" disabled="true"
											value="${loginningstudent.email}">
									</div></li>
								<li>
									<h3>ID</h3>
									<div>
										<input type="text" class="userid userjb" disabled="true"
											value="${loginningstudent.invitationId}">
									</div></li>
								<li>
									<h3>密码</h3>
									<div>
										<span class="buttontext buttontext-but">修改密码</span>
									</div>
									<div class='xgmm'>
										<div>
											<h3>原始密码</h3>
											<input type="password" class="userid userjb pwd-old" value=""><span
												class='pwd-error'></span>
										</div>
										<div>
											<h3>新密码</h3>
											<input type="password" class="userid userjb pwd-new" value=""><span
												class='pwd-error'></span>
										</div>
										<div>
											<h3>新密码确认</h3>
											<input type="password" class="userid userjb pwd-new2"
												value=""><span class='pwd-error'></span>
										</div>
										<div>
											<button class="submit-2">保存</button>
											<button class="cancle">取消</button>
										</div>
									</div></li>
							</ul>
						</div>
					</c:if>
					<c:if test="${module=='gszhgl'}">
						<div id='box3' class="Companyaccount">
							<div class="Companyaccount-h">
								<h3>公司账号设置</h3>
							</div>
							<div class="Companyaccount-f">
								<h3>分配账号</h3>
								<div>
									<input type="search"
										class="CompanyaccountInvitation-code email1 userjb"
										placeholder="电子邮件">
									<button id='send-co'>发送</button>
								</div>
							</div>
							<div class="Companyaccount-c">
								<h3>账号管理(${accounts.totalCount})</h3>
								<div class="Companyaccount-c-n">
									<ul>
										<li>账号</li>
										<li>姓名</li>
										<li>标签</li>
										<li>学生</li>
										<li>管理</li>
									</ul>
								</div>
								<div class="Companyaccount-c-l">
									<ul>
										<c:forEach items="${accounts.result}" var='account'>
											<li class="Companyaccount-c-l-1">
												<div>
													<ul class="Companyaccount-c-l-2">
														<li style='display:none'>${account.studentId}</li>
														<li>${account.invitationId}</li>
														<li><a target='_blank'
															href='/student/home/${account.invitationId}'>${account.realName}</a>
														</li>
														<li>${account.labelName}</li>
														<li>${account.studentCount}</li>
														<li><a class='account-info-ope'>详情</a></li>
													</ul>
												</div></li>
										</c:forEach>
									</ul>

								</div>
							</div>
						</div>
					</c:if>
					<c:if test="${module=='fwxz'}">
						<div id='box4' class="Companyaccount-authority">
							<div class="Companyaccount-authority-h">
								<h3 class='box4-h3-area'>
									<span class='box4-h3-area-s-col'>学校范围选择</span><span>年级范围选择</span>
								</h3>
							</div>
							<div>
								<div class='box4-fwxz'>
									<div class='box4-yxz'>
										<b>已选择的学校:</b>
									</div>
									<ul>
										<c:forEach items="${schools}" var="school">
											<li><input disabled="true" data-id='${school.schoolId}'
												type="text" class="userschool userjb"
												value="${school.schoolName}">
												<div class="right">
													<label><span>选择</span> <input
														<c:if test="${!empty school.areaId}"> checked='true'</c:if>
														type="checkbox" value="选择" data-id='${school.schoolId}'
														data-name='${school.schoolName}'> </label>
												</div>
											</li>
										</c:forEach>

									</ul>
								</div>
								<div class='box4-fwxz' style='display:none'>
									<div class='box4-yxz'>
										<b>已选择的年级:</b>
									</div>
									<ul>
										<c:forEach items="${years}" var="year">
											<li><input disabled="true" data-id='${year.yearId}'
												type="text" value='${year.yearId}' class="userschool userjb">
												<div class="right">
													<label><span>选择</span> <input type="checkbox"
														<c:if test="${!empty year.grade}"> checked='true'</c:if>
														value="选择" data-id='${year.yearId}'> </label>
												</div>
											</li>
										</c:forEach>
									</ul>
								</div>
							</div>

						</div>
					</c:if>
					<c:if test="${module=='kcsz'}">
						<div id='box5' class="Course-setting">
							<div class="Course-setting-h" style='overflow:hidden;'>
								<h3 class='Course-setting-h-col'>课程发布</h3>
								<h3>课程管理</h3>
							</div>
							<div>
								<div class='Course-setting-li'>
									<ul>
										<li>
											<h3>上课地点</h3>
											<div>
												<textarea class="coursetext course-place"></textarea>
											</div></li>
										<li>
											<h3>联系方式</h3>
											<div>
												<textarea class="coursetext course-tel"></textarea>
											</div></li>
										<li>
											<h3>课程介绍</h3>
											<div>
												<textarea class="coursetext course-intro"></textarea>
											</div></li>
										<li>
											<h3>报名人数</h3>
											<div>
												<input value='50' class='course-count'>
											</div></li>
										<li>
											<button class="submit-2 course-sub">发布</button>
										</li>
									</ul>
								</div>
								<div style='overflow:hidden;display:none'
									class='Course-setting-li'>
									<table class='ta1' borderColor=#000000 height=40 cellPadding=1
										width=250 align=center border=1>
										<thead>
											<tr>
												<td class='td1'>序号</td>
												<td class='td2'>上课地点</td>
												<td class='td3'>联系方式</td>
												<td class='td4'>课程介绍</td>
												<td class='td5'>已报名人数</td>
												<td class='td6'>报名上限</td>
												<td class='td7'>发布时间</td>
												<td class='td8'>操作</td>
											</tr>
										</thead>
										<tbody>

										</tbody>
									</table>
								</div>
							</div>
						</div>
					</c:if>
					<c:if test="${module=='yqhy'}">
						<div id='box6' class="invent-friend">
							<div class="invent-friend-h">
								<h3>邀请好友</h3>
							</div>
							<div class="invent-friend-t">
								<input type="search"
									class="CompanyaccountInvitation-code email2 userjb"
									placeholder="电子邮件">
								<button id='send-hy'>发送</button>
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
		</div>
		<div id='account-info'
			class="ts-main-content-Companyaccount-f tshow-tck account-info"
			style="display:none">
			<div class="ts-main-content-Companyaccount-f-h">
				<h3>账户详情</h3>
				<span class='close-2'>X</span>
			</div>
			<ul>

			</ul>
		</div>

	</div>
	<div id='tpcj'>
		<div class='tpcj-f'>
			<div class='tpcj-img'></div>
			<div class='tpcj-zzc'></div>
		</div>
		<div class='but-p'>
			<button class='quit'>取消</button>
			<button class='submit-2'>确认</button>
		</div>
	</div>
	<jsp:include page="../common/productMes.jsp"></jsp:include>
	<jsp:include page="../common/uploadProduct.jsp"></jsp:include>
	<jsp:include page="../common/search.jsp"></jsp:include>
</BODY>
</HTML>