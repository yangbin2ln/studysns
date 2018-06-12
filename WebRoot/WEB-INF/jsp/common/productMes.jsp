<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!-- 作品详情 -->
<div id="content">
	<div class="close"></div>
	<!-- 评论详情弹出框 -->
	<div class="tshow-zzc"></div>
	<div class="reply-main"></div>
	<div class="pointEdit">
		<div class='edit-full-box'>
			<span class='icon-full-screen'></span>
		</div>
		<div class="pointEdit-box">
			<div class="pointEdit-box-1">
				<span>标题:</span>
				<div class='pointEdit-box-title-p'>
					<input id="title" autocomplete="off" class="titleEdit">
					<ul class='reply-point-recommend'>

					</ul>
				</div>
			</div>
			<div class="jy">
				<span>建议:</span>
				<div id="advise" class="divEdit" contentEditable=true></div>
			</div>
			<div class="bp">
				<button class="cancel">取消</button>
				<button class="bsubmit">提交</button>
			</div>
		</div>
	</div>
	<!--end  打点编辑框-->
	<div class="imgMain"></div>
	<!-- 大文本编辑框 -->
	<div class='big-edit'>
		<div class='zzc'></div>
		<div class='big-edit-bar'>
			<div class='edit-small-box icon-small-screen'></div>
		</div>
		<div class='big-edit-box'>
			<div class='big-edit-mai'>
				<div class='big-edit-main-box'>
					<div class='big-edit-content' contenteditable=true
						spellcheck="false" style="min-height: 170px;"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- start 消息弹出框 -->
<div class="tshow-zzc"></div>
<div id="message-controller">
	<table class="img-div">
		<tbody>
			<tr>
				<td class='img-td-b'><a class="img-controller"><img>
				</a></td>
			</tr>
		</tbody>
	</table>
	<div>
		<div class="px2">
			<div class="px-h">
				<a href="#" class="news">作品<span class="message-a-count"></span>
				</a> <a href="#" class="you">参与<span class="message-a-count"></span>
				</a> <a href="#" class="person">报名<span class="message-a-count"></span>
				</a> <a href="#" class="flowers">粉丝<span class="message-a-count"></span>
				</a> <a href="#" class="sys">通知<span class="message-a-count"></span>
				</a>
			</div>
			<div class='message-box-p'>
				<div class="message-news">
					<div class="px-f">
						<div class="more">查看更多</div>
					</div>
				</div>
				<div class="message-you">
					<div class="px-f">
						<div class="more">查看更多</div>
					</div>
				</div>
				<div class="message-student">
					<div class="px-f">
						<div class="more">查看更多</div>
					</div>
				</div>
				<div class="message-flowers">
					<div class="px-f">
						<div class="more">查看更多</div>
					</div>
				</div>

				<div class="message-sys">
					<div class="px-f">
						<ul>
							<li><span class="px-f-n">213</span> <span class="px-f-title">T-Show欢迎您的加入</span>
							</li>
						</ul>
						<div class="more">查看更多</div>
					</div>
				</div>
			</div>
		</div>
		<!--详情news-->
		<div class="pxn new-info" style="display:none">
			<div class="pxn-h">
				<a href="#"> <span class="pxn-h-l"><</span> </a> <span
					class="pxn-h-title color">摄影·美女</span> <span
					class="pxn-h-time color">9/29</span>
			</div>
			<div class="pxn-b"></div>
			<div class="pxn-f">
				<div class="pxn-f-c">
					<ul>
					</ul>
				</div>
				<div class="pxn-f-c">
					<ul>
					</ul>
				</div>
				<div class="pxn-f-c">
					<ul>
					</ul>
				</div>
				<div class="pxn-f-c">
					<ul>
					</ul>
				</div>

			</div>
		</div>
		<!--详情you-->
		<div class="pxn you-info" style="display: none;">
			<div class="pxn-h"></div>
			<div class="pxn-b"></div>
			<div class="pxn-f" style="height: 154px;">
				<div class="pxn-f-c" style="display: block;">
					<ul>
					</ul>
				</div>
				<div class="pxn-f-c" style="display: none;">
					<ul>
					</ul>
				</div>
				<div class="pxn-f-c" style="display: none;">
					<ul>
					</ul>
				</div>
				<div class="pxn-f-c" style="display: none;">
					<ul>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- end 消息弹出框 -->
<!--start 反馈临时页面 -->
<div id='feedback' class='feedback'>
	<div class='tshow-zzc tshow-show'></div>
	<div class='feedback-box'>
		<div class='feedback-box-p'>
			<div class='feedback-box-c-1'>
				<h2>给T-Show提点建议吧！</h2>
			</div>
			<div class='feedback-box-c-p'>
				<div class='feedback-box-c-2'>
					<span>标题</span><input class='feedback-header-title'>
				</div>
				<div class='feedback-box-c-3'>
					<span>内容</span>
					<div contenteditable="true" class='feedback-header-content'></div>
				</div>
				<div class='feedback-bottom'>
					<div>
						<button class='feedback-cancel'>取消</button>
						<button class='feedback-sub'>提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--end   反馈临时页面 -->