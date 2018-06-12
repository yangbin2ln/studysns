<%@ page language="java" import="java.util.*,java.net.URL"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>T-Show 一个充实、展示自己的校园平台</TITLE>
<META charset='utf-8'>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="stylesheet" href="<%=path%>/css/index/index.css">
<script src="<%=path%>/js/common/jquery-1.9.1.js"></script>
<script src="<%=path%>/js/common/jquery.easing.1.3.js"></script>
<script src="<%=path%>/js/common/ajaxfileupload.js"></script>
<script src="<%=path%>/js/common/jquery.blockUI.js"></script>
<script src="<%=path%>/js/common/tshowCommon.js"></script>
<script src="<%=path%>/js/index/index.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		indexInit();
	});
</script>
<style type="text/css">
</style>
</HEAD>
<BODY>
	<div class="sy">
		<div class="sy1">
			<div class="log-in">
				<h1>tshow</h1>
				<input class="text1 login-email" type="text" placeholder="电子邮箱/邀请码">
				<input class="text2 login-pwd" type="password" placeholder="密码">
				<div class='verify-p'>
					<input class="verify" type="text" placeholder="输入验证码"><span
						class='verify-img'></span>
				</div>
				<div class='login-error'></div>
				<button id='login'>登录</button>
				<a id='forget-pwd' href="#">忘记密码？</a>
			</div>
			<div class="sign-up" style="display:none">
				<div id='register-email-d'>
					<input class="text1 register-email" type="text" placeholder="电子邮箱"
						value=''>
					<ul class='register-email-d-u'>

					</ul>
				</div>
				<input class="text2 invitation-id" type="text" placeholder="邀请码"
					value=''> <input
					class="text3 register-username" type="text" placeholder="姓名">
				<input class="text3 register-pwd" type="password" placeholder="密码">
				<div class='verify-p2'>
					<input class="verify" type="text" placeholder="输入验证码"><span
						class='verify-img'><img
						src='/util/verifyCodeRegister?key=4564897'> </span>
				</div>
				<div class='register-error'></div>
				<div class="Invitation-code">
					<a id='apply-invi' href="#">申请邀请码</a>
				</div>
				<button id='register'>注册</button>
				<div class="Exploration">
					<a href="/find">探索Tshow</a>
				</div>

			</div>
			<div id='sqyqm' class='tshow-zzc'></div>
			<div class="sy-ic" style="display:none">
				<div class="sy-ic-t">
					<span>邀请码</span>
				</div>
				<div class="sy-ic-ln">
					<span class="dh"></span> <span class="dhc">愿景</span>
					<p>t-show旨在搭建一个全新的校内学习、交流平台，让每个用户通过T-SHOW可以认识任何来自世界各地，与自己有着相同的爱好的朋友，并且跟随学习全世界的文化、知识，丰富自己的人生</p>
				</div>
				<div class="sy-ic-am">
					<span class="dh"></span> <span class="dhc">邀请码发放方式</span>
					<p>1、在网站平台每天限量发放</p>
					<p>2、由达人发出邀请码</p>
				</div>
				<div class="sy-ic-use">
					<span class="dh"></span> <span class="dhc">邀请码使用规则</span>
					<p>T-SHOW的邀请码为，一号一码制。邀请码只能和接收邀请码的邮箱配合使用。</p>
				</div>
				<div class="sy-ic-b">
					<p>为了营造良好的社交环境，T-SHOW每天会开放一定数量的邀请码，以下的信息请，用户认真的填写，T-SHOW不会泄露您的信息。您的电子邮箱，将会是您接收邀请码和注册平台的邮箱。</p>
					<div class="form">
						<div class="form-1">
							<h3 class="form-hd">你是如何知道tshow的？</h3>
							<textarea class='how-know'></textarea>
						</div>
						<div class="form-2">
							<h3 class="form-hd">你希望从tshow能收获什么？</h3>
							<textarea class='want-know'></textarea>
						</div>
						<div class="form-3">
							<h3 class="form-hd">上传你的作品（支持的格式：）</h3>
							<input name='sczp' id='sczp' style='display:none' type='file'
								onchange='uploadFile2("sczp")' />
							<button class='sczp-but'>上传</button>
							<span class='sczp-state'>未上传</span> <span class='sczp-state-name'></span>
						</div>
						<div class="form-4">
							<h3 class="form-hd">作品说明：</h3>
							<textarea class='product-remark'></textarea>
						</div>
					</div>
					<div class="Submit">
						<h4>电子邮箱:（本邮箱将用于接收邀请码）</h4>
						<input class='sq-email' type="text" placeholder="邮箱">
						<button class='zpsc-sub'>提交</button>
					</div>

				</div>
			</div>
		</div>
		<div class="sy-b">
			<a href="#login" class="sy-b-login">登陆</a> <a href="#register"
				class="sy-b-signup">注册</a>
		</div>
	</div>
	<div class='zhmm-main' style='display:none'>
		<div class='tshow-zzc' style='display:block'></div>
		<div class='zhmm-pare'>
			<div class='zhmm-pare-c'>
				<h3 class='zhmm-header'>找回密码</h3>
				<div style='position:relative'>
					<input class='zhmy-email' placeholder="邮箱">
				</div>
				<div style='position:relative'>
					<input class='zhmm-yzm' placeholder="邮箱验证码">
					<button class='hqyzm-but'>获取验证码</button>
				</div>
				<button class='next-but'>下一步</button>
			</div>
			<div style='display:none' class='zhmm-pare-c'>
				<h3 class='zhmm-header'>重设密码</h3>
				<div>
					<input disabled="true" class='zhmm-email-set-up' placeholder="邮箱">
				</div>
				<div>
					<input class='zhmm-new-pwd' placeholder="新密码">
				</div>
				<button class='zhmm-sub'>提交</button>
			</div>
		</div>
	</div>

</BODY>
</HTML>
