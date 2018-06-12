			/*屏蔽一些键 start*/
			//document.onkeydown =function()  
		/*	shielding();
			initPizhuSelect()
			stopPzmp();
			addSpan();
			
			// 全局批注集合（只需实例化一次）
			var notationPoints = new NotationPoints();
			initNotationPoint(${notationJson});
			showPoints();*/
			var notationPoints = new NotationPoints();
			var flag = false;
			var flag2 = false;
			function shielding() {
				$("body").keydown(function() {
					var k2 = window.event.keyCode;
					var k = window.event.ctrlKey;// Ctrl组合键
					//console.log(k)
					if (k == true && k2 == 65) {// 屏蔽ctrl+a
						window.event.returnValue = false;
					}
				});
				//禁止鼠标选择标题文字
				$("#main h2").on("selectstart",function(){return false});
			}
			/* end 屏蔽一些键 */
			
			function getSelection1_ceshi() {
				var startIndex = window.getSelection().getRangeAt(0).startOffset;
			    var endIndex = window.getSelection().getRangeAt(0).endOffset;
			    var slicedText = $(this).text().slice(startIndex, endIndex);
			    //console.log(slicedText);
			    return slicedText;
			}
			
			//1 的备份
			function getSelection1() {
				var userSelection;
				if (window.getSelection) { // 现代浏览器
					userSelection = window.getSelection();
				} else if (document.selection) { // IE浏览器 考虑到Opera，应该放在后面
					userSelection = document.selection.createRange();
				}
				return userSelection;
			}
			
			/* 获取选中的文字 start */
			function getRange() {
				var userSelection;
				if (window.getSelection) { // 现代浏览器
					userSelection = window.getSelection();
				} else if (document.selection) { // IE浏览器 考虑到Opera，应该放在后面
					userSelection = document.selection.createRange();
				}
				// 从Selection对象创建Range对象 start
				var getRangeObject = function(selectionObject) {
					if (selectionObject.getRangeAt)
						return selectionObject.getRangeAt(0);
					else { // 较老版本Safari!
						var range = document.createRange();
						range.setStart(selectionObject.anchorNode,
								selectionObject.anchorOffset);
						range
								.setEnd(selectionObject.focusNode,
										selectionObject.focusOffset);
						return range;
					}
				}
				var rangeObject = getRangeObject(userSelection);
				return rangeObject;
			}
			
			// end 从Selection对象创建Range对象
			/* end 获取选中的文字 */
		
			
			/* 批注点start */
			function NotationPoints() {
				this.value = [];
				this.get = function(end) {
					for ( var i = 0; i < this.value.length; i++) {
						if (this.value[i].end == end) {
							return this.value[i]
						}
					}
					return null;
				};
				// 添加批注点
				this.add = function(obj) {
					var notationPoint = new NotationPoint(obj.end, obj);
					this.value.push(notationPoint);
					return notationPoint;
				};
			}
			
			// 批注点对象
			function NotationPoint(end, notation) {
				this.end = end;// 末尾节点
				this.notations = [ notation ];// 此批注点的所有批注集合
				this.length = function() {
					return notations.length;
				};
				this.get = function(index) {
					if (index < 0)
						index = 0;
					if (index > this.length())
						index = this.length() - 1;
					return notations[index];
				}
			}
			
			// 将某个批注对象存入批注点集合中
			function createNotationPoint(obj) {
				var notationPoint = notationPoints.get(obj.end);
				if (notationPoint == null) {// 还没有和次对象相同的end的批注点，则新建一个批注点
					notationPoint = notationPoints.add(obj);
					return notationPoint;// 表示需要新建批注点
				} else {// 则在已经存在的批注点对象的notations属性中添加此批注点
					notationPoint.notations.push(obj);
					return notationPoint;// 表示不需要新建批注点
				}
			}
			// 初始化批注点对象（实现批注所有信息的前台保存）
			function initNotationPoint(notationJson) {
				// var notationJson=${notationJson}
				// 遍历所有的批注
				for ( var i = 0; i < notationJson.length; i++) {
					createNotationPoint(notationJson[i]);
				}
			
			}
			
			// 初次生成批注点
			function showPoints() {
				var spans = $("#main span[data-offset]");
				for ( var i = 0; i < notationPoints.value.length; i++) {
					var notationPoint = notationPoints.value[i];
					var span = spans.eq(notationPoint.end);
					var point = $("<div class='point-p'><div class='point'></div></div>")
							.css({
								"left" : span.offset().left - $("#main").offset().left,
								"top" : span.offset().top - $("#main").offset().top - 14
							});
					addPointClick(notationPoint, point);// 使用闭包批量绑定事件
					$("#main").append(point);
				}
			}
			// 为批注点绑定click事件
			function addPointClick(notationPoint, point) {
				(function(notationPoint) {
					point.click(function() {
						//console.log($(this));
						var obj = $(this)
						createNoteDisplayTipHtml(obj, notationPoint, 0);
						return false;
					})
				})(notationPoint);
			}
			
			function createNoteDisplayTipHtml(obj, notationPoint, index) {
				//console.log(obj.offset())
				// 生成显示下划线
				$(".mainUnderLine").empty();
				removeSelectBack();
				/*$("body").append(
						createUnderLines(notationPoint.notations[index].start,
								notationPoint.notations[index].end));*/
				//阴影
				addSelectBack2(notationPoint.notations[index].start,notationPoint.notations[index].end);
			
				//console.log(notationPoint);
				var ndt = $(".notation-content");
				var studentName = notationPoint.notations[index].replyStudent.realName;
				var studentId = notationPoint.notations[index].replyStudent.studentId;
				var notationContent = notationPoint.notations[index].notationContent;
				var notationReplyId = notationPoint.notations[index].notationReplyId;
				var pageSize = notationPoint.notations.length;
				var notationReplyReplys = notationPoint.notations[index].notationReplyReplys
						|| [];
				var replyCount = notationReplyReplys.length;
				var _html = ''
				/* 用户信息div start */
				_html += '<div><a href="studentController.do?findStudentById=true">'
				_html += studentId;
				_html += '</a>'
				_html += '<span>' + studentName + '</span>';
				/* 分页 start */
				_html += '<div style="float:right" class="pageSize"><span class="pageLeft"></span><span class="page">'
						+ (index + 1)
						+ '</span>/<span class="pageCount">'
						+ pageSize
						+ '</span><span class="pageRight"></span></div>'
				/***************************************************************************
				 * end 分页 _html+='</div>'; /*end 用户信息div
				 */
				/* 批注信息div start */
				_html += '<div class="note-content">' + notationContent + '</div>'
				/* end 批注信息div */
				/* 批注底部 （回复）start */
				_html += '<div>'
				_html += '<a href="javascript:;" class="reply"> 回复</a><span>' + replyCount
						+ '</span>'
				_html += '<div class="replys" style="display:none">'
				// 对子回复的遍历添加节点
				for ( var i = 0; i < replyCount; i++) {
					var replyStudentId = notationPoint.notations[index].notationReplyReplys[i].replyStudent.studentId;
					var replyStudentName = notationPoint.notations[index].notationReplyReplys[i].replyStudent.realName;
					var toreplyStudentId = notationPoint.notations[index].notationReplyReplys[i].toreplyStudent.studentId;
					var toreplyStudentName = notationPoint.notations[index].notationReplyReplys[i].toreplyStudent.realName;
					var replyReplyCreateTime = notationPoint.notations[index].notationReplyReplys[i].createTime;
					var replyReplyContent = notationPoint.notations[index].notationReplyReplys[i].replyContent;
					var url = "studentController.do?findStudentById=true&studentId="
					_html += '<ul><span class="childs-notation-content"><a href=' + url
							+ replyStudentId + '>' + replyStudentName + '</a>回复了'
							+ toreplyStudentName + ':' + replyReplyContent + '</span>'
							+ '<span class="childs-notation-content">'
							+ replyReplyCreateTime
							+ '</span><a class="act-reply"></a></ul>'
					// 为每一条评论添加回复框
					_html += '<div class="comment-child-input"><input placeholder=回复"'
							+ replyStudentName
							+ '：."><button type="submit" class="btn-post-child">回复</button></div>'
				}
				// 生成回复框
				_html += '<div><div contenteditable="true" class="comment-input" ></div><button type="submit" class="btn-post">回复</button></div>';
				_html += '</div>';
				_html += '</div>'
				/* end 批注底部 （回复） */
				ndt.html(_html);
				// 定位 offset()表示当前节点相对于文档的位置
				// 判断页码宽度，自适应小尺寸屏幕
				if ($(document.body).outerWidth(true) <= 700) {
					ndt.parent(".note-display-tip").css({
						left : '25%',
						top : obj.offset().top + 37
					}).show();
					$(".arrow-outer").hide();
				} else {
					ndt.parent(".note-display-tip").css({
						left : obj.offset().left - $(".note-display-tip").width() / 2 - 6,
						top : obj.offset().top + 37
					}).show();
				}
				// 取消事件
				$(".reply,.btn-post,.replys .comment-child-input,.pageSize").unbind();
				// 位分页绑定事件
				(function(notationPoint) {
					$(".pageLeft").click(function() {
						var page = parseInt($(".page").text());
						// 改变当前页码
						$(".page").text((--page) < 1 ? 1 : (page))
						var index = parseInt($(".page").text());
						createNoteDisplayTipHtml(obj, notationPoint, index - 1)
						return false;
					});
				})(notationPoint);
				(function(notationPoint, obj) {
					$(".pageRight").click(function() {
						var page = parseInt($(".page").text());
						var pageCount = parseInt($(".pageCount").text());
						// 改变当前页码
						$(".page").text((++page) > pageCount ? pageCount : (page))
						var index = parseInt($(".page").text());
						createNoteDisplayTipHtml(obj, notationPoint, index - 1)
						return false;
					});
				})(notationPoint, obj);
			
				// 为回复区域绑定隐藏显示切换事件
				$(".reply").click(function() {
					//console.log(notationPoint)
					$(".replys").toggle()
					return false;
				})
			
				// 为主回复按钮绑定提交请求事件
				$(".btn-post").click(
						function() {
							var obj = {
								notationReplyId : notationReplyId,
								'toreplyStudent.studentId' : studentId,
								replyContent : $(".comment-input").text()
			
							}
							$.post("productController.do?saveNotationReplyReply=true", obj,
									function(notationReplyReply) {
										if (notationReplyReply == 'failed') {
											alert("系統异常")
										}
										//console.log(notationReplyReply);
									})
						});// 这里一定要加分号
				//console.log(notationPoint);
				//console.log(notationPoint.notations[index]);
				// 为子回复绑定请求事件
				(function(notationReply2) {
					for ( var ii = 0; notationReply2.notationReplyReplys&&ii < notationReply2.notationReplyReplys.length; ii++) {
						(function(ii) {
							$(".btn-post-child")
									.eq(ii)
									.click(
											function() {
												alert(ii)
												var notationReplyReply = notationReply2.notationReplyReplys[ii];
												var replyContent = $(".active-ib input")
														.val();
												var obj = {
													'toreplyStudent.studentId' : notationReplyReply.replyStudent.studentId,
													notationReplyId : notationReplyReply.notationReplyId,
													replyContent : replyContent
												}
												$
														.post(
																'productController.do?saveNotationReplyReply=true',
																obj, function(res) {
																	if (res == 'failed') {
																		alert('系统异常')
																	}
																	//console.log(res)
																})
			
											});
						})(ii);
					}
				})(notationPoint.notations[index]);
			
				// 为所有的子回复按钮绑定提交请求事件
				$(".replys").delegate(".act-reply","click",function() {
					// 隐藏主回复框
					$(".comment-input").parent().hide();
					// 隐藏所有的子回复框
					$(this).parent().siblings("ul").next().removeClass("active-ib");
					// 显示当前回复框
					$(this).parent().next().addClass("active-ib");
					return false;// 禁止冒泡
			
				});
			
				// 点击主区域时切换子回复和主回复文本框
				$(".note-display-tip").click(function() {
					$(".comment-child-input").removeClass("active-ib");
					$(".comment-input").parent().show();
				})
				// 禁止子回复框事件冒泡
				$(".comment-child-input").click(function() {
					return false
				})
				return false;// 禁止冒泡
			}
			/* end 批注点 */
			
			function initPizhuSelect(){
				// $("#main").on("mouseover",function(){flag=false})
				$("#main").on("mousedown", ":not(#btns-tip)", function(i) {
					$(".mainUnderLine").remove();
					removeSelectBack();
					$("#btns-tip").hide();
				//	flag2 = true;
					// 在选中文字的过程中，将目前选择的文字加下划线(作废)
					/*$("#main").on("mouseover", function() {
						flag = false;
				
						
						 * if(flag2){ $("body").append(createUnderLines()) }
						 
					})*/
				/*	a = {};
					b = {};
					startX = i.pageX;
					startY = i.pageY;*/
					var _target = (i.target ? i.target : null);
					if (_target) {
						var tx1 = _target.offsetLeft;
						var ty1 = _target.offsetTop;
					}
					// a={'x':i.offsetX+tx1,'y':i.offsetY+ty1};//保存鼠标离开时的相对位置（相对父div）
				});
				$("#main")
						.on(
								"mouseup",
								":not(#btns-tip)",
								function(j) {
								//	flag2 = false;
									var range = getRange();
									var select = getSelection1();
									var _target1 = (j.target ? j.target : null);
									if (_target1) {
										var tx = _target1.offsetLeft;
										var ty = _target1.offsetTop;
									}
									/*
									 * b={'x':j.offsetX+tx,'y':j.offsetY+ty};//保存鼠标离开时的相对位置（相对父div）
									 * //console.log(b)
									 */
									var startSpan = $(select.baseNode.parentElement)[0];
									var endSpan = $(select.focusNode.parentElement)[0];
									if(!($(startSpan).attr("data-offset")&&$(endSpan).attr("data-offset"))){
										return;
									}
									var acconation = getAnnocation();
									startSpan = {
										"x" : startSpan.offsetLeft,
										"y" : startSpan.offsetTop
									};
									endSpan = {
										"x" : endSpan.offsetLeft,
										"y" : endSpan.offsetTop
									};
									/*
									 * endX=j.pageX; endY=j.pageY;
									 */
									if (startSpan.x != endSpan.x || startSpan.y != endSpan.y) {
										if (endSpan.y > startSpan.y
												|| (endSpan.y == startSpan.y && endSpan.x > startSpan.x)) {
											// $("#btns-tip").css({"display":"block","left":endX-147,"top":endY+20});
											// 为批注按钮定位
											$("#btns-tip").css(
													{
														"display" : "block",
														"left" : endSpan.x
																+ $("#main").offset().left + 16
																- 30,
														"top" : endSpan.y
																+ $("#main").offset().top + 20
													});
											$("._arrow-box").eq(0).css("display", "none");
											$("._arrow-box").eq(1).css("display", "block");
											// $("#main").append($("<div
											// class='point'></div>").css({"left":endSpan.x+8,"top":endSpan.y-7}));
				
										} else {
											// $("#btns-tip").css({"display":"block","left":endX-147,"top":endY-40});
											$("#btns-tip")
													.css(
															{
																"display" : "block",
																"left" : endSpan.x
																		+ $("#main").offset().left
																		- 30,
																"top" : endSpan.y
																		+ $("#main").offset().top
																		- 38
															});
											$("._arrow-box").eq(0).css("display", "block");
											$("._arrow-box").eq(1).css("display", "none");
											// $("#main").append($("<div
											// class='point'></div>").css({"left":startSpan.x+8,"top":startSpan.y-7}));
				
										}
				
									}
								})
			}
							
			// 预处理所有文字，加span标签
			function addSpan() {
				$("#main span,#main strong").contents().filter(function(){ return this.nodeType != 1; }).wrap("<teshu>");
				var ps = $("#main teshu");
				var m = 0;
				for ( var i = 0; i < ps.length; i++) {
					var _html = ps.eq(i).html();
					//排除空格
					if(_html.indexOf("&nbsp")!=-1){
						ps.eq(i).children().unwrap("<teshu>");
						continue;
					}
					var span = "";
					for ( var j = 0; j < _html.length; j++) {
						span += "<span class='notation-span' data-offset=" + m++ + ">" + _html.charAt(j)
						+ "</span>";
					}
					ps.eq(i).html(span);
					ps.eq(i).children().unwrap("<teshu>");
				}
			}
			// 预处理所有文字，加span标签
			function addSpan2() {
				var ps = $("#main p");
				var m = 0;
				for ( var i = 0; i < ps.length; i++) {
					var _html = ps.eq(i).html();
					var span = "";
					for ( var j = 0; j < _html.length; j++) {
						span += "<span data-offset=" + m++ + ">" + _html.charAt(j)
						+ "</span>";
					}
					ps.eq(i).html(span);
				}
			}
			
			// 获取文本节点 $("span:gt(146):lt(41)").text()
			function getText() {
				var select = getSelection1();
				var startSpan = $($(select.baseNode.parentElement)[0]);
				var endSpan = $($(select.focusNode.parentElement)[0]);
				
				var a = parseInt(startSpan.attr('data-offset'));
				var b = parseInt(endSpan.attr('data-offset'));
				var _text = $(
						"#main span[data-offset]:gt(" + (a > b ? b : a) + "):lt(" + Math.abs(a - b)
								+ ")").text();
				return _text
			}
			
			// 根据end获取endSpan的绝对位置
			function getEndSpanPosition(end) {
				return $("#main span[data-offset=end]").offset();
			}
			
			// 前台获取首尾文字
			function getStartEnd() {
				var select = getSelection1();
				var startSpan = $($(select.baseNode.parentElement)[0]);
				var endSpan = $($(select.focusNode.parentElement)[0]);
				var a = parseInt(startSpan.attr('data-offset'));
				var b = parseInt(endSpan.attr('data-offset'));
				return a > b ? {
					startSpan : endSpan,
					endSpan : startSpan
				} : {
					startSpan : startSpan,
					endSpan : endSpan
				};
			}
			
			// 获取批注文字
			function getAnnocation() {
				var select = getSelection1();
				var startSpan = $($(select.baseNode.parentElement)[0]);
				var endSpan = $($(select.focusNode.parentElement)[0]);
				var a = parseInt(startSpan.attr('data-offset'));
				var b = parseInt(endSpan.attr('data-offset'));
				var acconation = a > b ? {
					startOffset : b,
					endOffset : a
				} : {
					startOffset : a,
					endOffset : b
				};
				return acconation;
			}
			
			// 获取选择文字的总行数
			function getCols(start, end) {
				var underLineInfo = {};
				// 当start和end都存在没有赋值的时候，这些下面的
				var startSpan, endSpan, sleft, stop, eleft, etop, cols;
				if (!(start && end)) {
					var select = getSelection1();
			
					startSpan = $($(select.baseNode.parentElement)[0])
					endSpan = $($(select.focusNode.parentElement)[0])
					if (parseInt(startSpan.attr("data-offset")) > parseInt(endSpan
							.attr("data-offset"))) {
						var a = startSpan;
						startSpan = endSpan;
						endSpan = a;
					} else {
						startSpan = startSpan.next();// 选中的文字默认是前一个
					}
					stop = startSpan.position().top;
					sleft = startSpan.position().left;
					etop = endSpan.position().top;// 相对父元素的位置
					eleft = endSpan.position().left + 16;// 下划线选中最后一个字
					cols = Math.abs(stop - etop);
				} else {
					startSpan = $("#main p span[data-offset=" + start + "]")
					endSpan = $("#main p span[data-offset=" + end + "]")
					stop = startSpan.position().top;
					sleft = startSpan.position().left;
					etop = endSpan.position().top;// 相对父元素的位置
					eleft = endSpan.position().left + 16;// 下划线选中最后一个字
					cols = Math.abs(stop - etop);
				}
				cols = cols / 30 + 1;// 30表示行高
				if (etop > stop || (etop == stop && eleft > sleft)) {
					underLineInfo.sx = sleft;
					underLineInfo.sy = stop;
					underLineInfo.ex = eleft;
					underLineInfo.ey = etop;
				} else {
					underLineInfo.sx = eleft;
					underLineInfo.sy = etop;
					underLineInfo.ex = sleft;
					underLineInfo.ey = stop;
				}
				underLineInfo.cols = cols;
				return underLineInfo;
			}
			
			// 生成下划线效果
			function createUnderLines(start, end) {
				// 先将页面上的下划线去掉
				$(".mainUnderLine").remove();
				removeSelectBack();
				var mainUnderLine = "<div class='mainUnderLine'>";
				var underLines = getUnderLineInfo(start, end);
				for ( var i = 0; i < underLines.length; i++) {
					var underLine = createUnderLine(underLines[i]);
					mainUnderLine += underLine;
			
				}
				mainUnderLine += "</div>";
				return mainUnderLine;
			}
			
			// 生成单个下划线
			function createUnderLine(underLine) {
				var left = underLine.left;
				var top = underLine.top;
				var width = underLine.width;
				var div = "<div class='underLine' style='top:" + (top - 10) + "px;left:"
						+ left + "px;width:" + width + "px'>" + "</div>";
				return div;
			}
			
			// 获取下划线信息
			function getUnderLineInfo(start, end) {
				var underLineArr = new Array();
				;
				// 保存单个下滑线对象
				var underLineceshi = {
					left : '',
					top : '',
					width : '$("#main").offset().left+$("#main").width()-x'
				}
				var mainLeft = $("#main").offset().left;
				var mainTop = $("#main").offset().top;
				var underLineInfo = getCols(start, end);
				var cols = underLineInfo.cols;
				for ( var i = 0; i < cols; i++) {
					var underLine = {};
					if (cols == 1) {
						underLine = {
							left : underLineInfo.sx + mainLeft,
							top : underLineInfo.sy + mainTop,
							width : underLineInfo.ex - underLineInfo.sx
						}
						underLineArr.push(underLine);
						break;
					}
			
					if (i == 0) {
						underLine = {
							left : underLineInfo.sx + mainLeft,
							top : underLineInfo.sy + mainTop,
							width : $("#main").width() - underLineInfo.sx
									+ parseInt($("#main").css("padding-left"))// $("#main").width()是不包含内外边距的，多减去了左内边距，所有加上
						}
					} else if (i == (cols - 1)) {
						underLine = {
							left : mainLeft + parseInt($("#main").css("padding-left")),// 下划线越过边距从第一个第开始划起
							top : underLineInfo.ey + mainTop,
							width : underLineInfo.ex
									- parseInt($("#main").css("padding-left"))// 减去左外边距
						}
					} else {
						underLine = {
							left : mainLeft + parseInt($("#main").css("padding-left")),// 下划线越过边距从第一个第开始划起
							top : underLineInfo.sy + mainTop + (i * 30),
							width : $("#main").width()
						}
					}
					underLineArr.push(underLine);
				}
				return underLineArr;
			}
			
			// 为页面绑定点击事件，取消备注的显示
			function stopPzmp() {
			
				$("#main").on("click", function(event) {
					$(".tz_dialog").remove();
					$(".note-display-tip").hide();
					//$("#btns-tip").hide();
					/*if (flag) {
						$("#btns-tip").hide();
					}*/
			
				})/*.mousedown(function() {
					flag = true;
				});
				$(document).click(function(event) {
					if (flag) {
						$("#btns-tip").hide();
					}
			
				}).mousedown(function() {
					flag = true;
				})*/
			}
			
			
			// 批注事件
			function pizhu() {
				// 保存选中文字的信息
				var select = getSelection1();
				var annocation = getAnnocation();
				var _text = getText();
				var startSpan1 = $(select.baseNode.parentElement)[0];
				var endSpan1 = $(select.focusNode.parentElement)[0];
				var acconation = getAnnocation();
				var startSpan = {
					"x" : startSpan1.offsetLeft,
					"y" : startSpan1.offsetTop
				};
				var endSpan = {
					"x" : endSpan1.offsetLeft,
					"y" : endSpan1.offsetTop
				};
				//$("body").append(createUnderLines());
				addSelectBack(select);
				$("#btns-tip").hide();
				$
						.tzDialog({
							title : 'title',
							content : '.con',
							callback : function(ok) {
								if (ok) {
									// 操作后台，等等 ... 你想做的事情
									alert("您点击了ok ！");
									var notation = annocation;
									var productContent = _text;
									var start = parseInt(notation.startOffset) + 1;
									var end = notation.endOffset;
									var productId = $("#productId").val();
									var notationContent = $(".tz_dialog .notationContent")
											.eq(0).val();
									var notationReply = {
										productContent : productContent,
										start : start,
										end : end,
										productId : productId,
										notationContent : notationContent
									};
									$
											.post(
													"productController.do?saveNotationReply=true",
													notationReply,
													function(obj) {
														var notationPoint = createNotationPoint(obj);
														var point;
														if (notationPoint.notations.length == 1) {// 新的批注点
															// 针对此次批注生成批注点
															if (endSpan.y > startSpan.y
																	|| (endSpan.y == startSpan.y && endSpan.x > startSpan.x)) {
																point = $(
																		"<div class='point-p'><div class='point'></div></div>")
																		.css(
																				{
																					"left" : endSpan.x,
																					"top" : endSpan.y - 14
																				});
																$("#main").append(point);
															} else {
																point = $(
																		"<div class='point-p'><div class='point'></div></div>")
																		.css(
																				{
																					"left" : startSpan.x,
																					"top" : startSpan.y - 14
																				});
																$("#main").append(point);
															}
															addPointClick(notationPoint,
																	point);
														}
			
													})
			
								} else {
									alert("您点击了取消 ！");
								}
							}
						});
			}
			
			//为选中的文字生成阴影背景
			
			function addSelectBack(select){
				var startSpan = $($(select.baseNode.parentElement)[0]);
				var endSpan = $($(select.focusNode.parentElement)[0]);
				
				var a = parseInt(startSpan.attr('data-offset'));
				var b = parseInt(endSpan.attr('data-offset'));
				var c;
				if(a>b){
					c=Math.abs(a - b)+1}
				else{
					c=Math.abs(a - b);
				}
				$("#main span[data-offset]:gt(" + (a > b ? b-1 : a) + "):lt(" + c
						+ ")").addClass("not-span-bac");
			}
			function addSelectBack2(a,b){
				a=parseInt(a);
				b=parseInt(b);
				var c=Math.abs(a - b)+1;
				$("#main span[data-offset]:gt(" + (a > b ? b-1 : a-1) + "):lt(" + c
						+ ")").addClass("not-span-bac");
			}
			//取消阴影背景效果
			function removeSelectBack(){
				 $("#main span[data-offset]").removeClass("not-span-bac");
			}
			
			// 禁止鼠标双击选中文字
			/*
			 * function stopxzBydblclick(){ $(document).bind("dblclick", function (e) {
			 * $(document).bind('selectstart',function(){return false;}) }) }
			 * stopxzBydblclick();
			 */
			// 禁止鼠标双击事件
			/*
			 * $(document).bind("dblclick", function (e) { e.preventDefault(); return false; })
			 */
			
			/*
			 * $(function(){ $('[data-toggle="popover"]').popover( { container:'body',
			 * placement:'bottom', title:'title', target:"#ddd" }
			 *  ); });
			 */
			
