<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<div class="zs tshow-hide">
	<div class='zs-Occupation-p1 tshow-hide'>
		<div class='tshow-zzc tshow-show'></div>
		<div class="zs-Occupation">
			<button class="zs-Occupation-submit">确认</button>
			<div class="zs-Occupation-l">
				<h3>职业方向</h3>
				<ul>
				</ul>
			</div>
			<div class="zs-Occupation-r">
				<h3>技能</h3>
				<ul>
				</ul>
			</div>

		</div>
	</div>
	<div class="zs-r">
		<div class='ts-close'></div>
		<div class="zs-r-wp">
			<div class="zs-r-wp-z">
				<div class="zs-r-wp-h">
					<input type="text" placeholder="标题">
					<div class="zs-l-b-title">
						<button class="zs-l-b-title-but">选择标签</button>
					</div>
				</div>
				<div class="zs-r-wp-b">
					<div class='zs-r-wp-z-sc'>
						<span></span>
					</div>
					<table class="zs-r-wp-b-1">
						<tr>
							<td class="zs-r-wp-b-td"></td>
						</tr>
					</table>
				</div>
				<input class="tshow-hide" type="file" id="imgfile" name="imgfile"
					onchange="uploadFile('imgfile','.zs .zs-r-wp-b-td')">
			</div>
			<div class="zs-r-wt">
				<div class="zs-r-wt-h">
					<span>这里可以写上您作品的创作背景或者创作意图，方便别人更好的理解您的作品</span>
				</div>
				<div class="zs-r-wt-b">
					<textarea class='sjsm' placeholder="添加设计说明"></textarea>
					<div class='zs-r-wt-b-box'>
						<div class='zs-r-wt-b-p'>
							<span class='zs-r-wt-b-num'>0</span><span>/</span><span
								class='zs-r-wt-b-count'>350</span>
						</div>
					</div>
				</div>
			</div>
			<div class="zs-r-ws">
				<h3>添加内容标签（最多可添加3个，按回车键确认内容标签）</h3>
				<div class="tshow-rel">
					<div class="tshow-abs zs-r-ws-conlab"></div>
					<input class="zs-r-ws-inp" type="text"placeholder:'添加内容标签'>
				</div>
				<div class="zs-r-ws-list">
					<span> 推荐添加</span>
					<ul>
					</ul>
				</div>
			</div>
			<div class='zs-l-b-sx-p'>
				<div class="zs-l-b-sx">
					<span class="zs-l-b-sx-y" data-val='Y'>原创</span> <span
						class="zs-l-b-sx-z" data-val='Z'>转载</span>
				</div>
				<div>
					<div class="add-product-calcel">
						<span>返回</span>
					</div>
					<div class="submit">
						<span>发布</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 发布新版本 -->
<div id="up-new-version" class="tshow-hide">
	<div class="tshow-zzc tshow-show"></div>
</div>