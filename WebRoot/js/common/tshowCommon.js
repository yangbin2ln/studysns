/* start  作品详情*/

/*打开详情*/

/*判断当前移动的点是否和其他点有重叠*/
/**
 activePoint  移动的点的坐标/dom节点
 type  dom节点(0)/坐标(1)
 */
function isOverlay(position, type){
    var $points;
    var x, y, w = 0, h = 0;
    if (type == 0) {
        x = position.offset().left;
        y = position.offset().top;
        w = position.width();//当前活动节点的宽
        h = position.height();
        $points = position.siblings(".box,.move,.mineP");//注意：此处是写死的
    }
    else {
        x = position.left;
        y = position.top;
        $points = $(".box");
    }
    for (var i = 0; i < $points.length; i++) {
        var point = $points.eq(i)
        var maxX = point.offset().left + point.width();
        var maxY = point.offset().top + point.height();
        var minX = point.offset().left - w;
        var minY = point.offset().top - h;
        if (x > minX && x < maxX && y > minY && y < maxY) {
            //console.log("不能覆盖别人的评论哦！");
            //将当前的鼠标样式改变
            //position.addClass("curNd");
            //默认将活动点放置在被覆盖点的右侧
            position.offset({
                left: point.offset().left + point.width(),
                top: point.offset().top
            })
            isOverlay(position, 0);
        }
        else {
            position.removeClass("curNd");
        }
    }
    
}


/*为元素添加拖拽属性*/
/**
 parget 被拖拽的元素（jquery中匹配类的字符串）
 parent 被拖拽元素的的父元素（用于显示拖拽的界限）
 */
function addTrag(target){
    $(document).mousemove(function(e){
        if (!!this.move) {
            var posix = !document.move_target ? {
                'x': 0,
                'y': 0
            } : document.move_target.posix, callback = document.call_over ||
            function(){
                $(this.move_target).css({
                    'top': e.pageY - posix.y,
                    'left': e.pageX - posix.x
                });
            };
            
            callback.call(this, e, posix);
        }
    }).mouseup(function(e){
        if (!!this.move) {
            var callback = document.call_up ||
            function(){
            };
            callback.call(this, e);
            $.extend(this, {
                'move': false,
                'move_target': null,
                'call_over': false,
                'call_up': false
            });
        }
    });
    $("#content").delegate(target, "mousedown", function(e){
        var top = $(this).position().top;
        var left = $(this).position().left
        
        this.posix = {
            'x': e.pageX - left,
            'y': e.pageY - top
        };
        var call_over = function(e, posix){
            var active = $(target).parent();
            var activeOffset = active.offset();
            var maxLeft = active.width() - $(this.move_target).width();
            var maxTop = active.height() - $(this.move_target).height();
            var minLeft = 0;
            var minTop = 0;
            var _y = e.pageY - posix.y;
            var _x = e.pageX - posix.x;
            if (_y < minTop) {
                _y = minTop
            }
            if (_x < minLeft) {
                _x = minLeft
            }
            if (_y > maxTop) {
                _y = maxTop
            }
            if (_x > maxLeft) {
                _x = maxLeft
            }
            $(this.move_target).css({
                'top': _y,
                'left': _x
            });
            //移动编辑框
            $(".pointEdit").show().offset({
                left: $(this.move_target).offset().left - $(".pointEdit").width() / 2,
                top: $(this.move_target).offset().top + 25
            });
            //判断是否覆盖别人的点
            var boo = isOverlay($(this.move_target), 0);
        }
        $.extend(document, {
            'move': true,
            'move_target': this,
            'call_over': call_over
        });
        return false;
    });
}


function getNowDate(){
    var date = new Date();
    return "刚刚";
}

/*打开作品详情*/
function openProductInfo(type, me){
    /*记录主页面的滚动条位置*/
    $(".pointEdit").hide()
    $("body").data({
        "scrollTop": $("body").scrollTop()
    });
    $("body").scrollTop(0);
    var boxw, product, productId;
    if (type == "personCenter") {
        boxw = $(me).parents("li");//个人中心
        var product = boxw.data("product");
        var productId = boxw.attr("data-id");
        //if (!product) {
        $tsBlockUI({
            message: "<img src='/img/123321.gif'>",
            css: {},
        });
        tshowAjax({
            type: 'POST',
            url: "/product/findProduct",
            data: {
                productId: productId
            },
            success: function(res){
                if (!res.success) {
                    createPromptBox(res.mess.message);
                    return;
                }
                $.unblockUI();
                product = JSON.parse(res.map.product);
                boxw.data({
                    "product": product
                })
            },
            async: false
        });
        //}
    }
    else 
        if (type == "message") {
            boxw = me;//个人中心
            var productId = boxw.attr("data-id");
            $tsBlockUI({
                message: "<img src='/img/123321.gif'>",
                css: {},
            });
            tshowAjax({
                type: 'POST',
                url: "/product/findProduct",
                data: {
                    productId: productId
                },
                success: function(res){
                    if (!res.success) {
                        createPromptBox(res.mess.message);
                        return;
                    }
                    $.unblockUI();
                    product = JSON.parse(res.map.product);
                    boxw.data({
                        "product": product
                    })
                },
                async: false
            });
        }
        else {
            boxw = $(this).parent().parent();//争鸣
            var product = boxw.data("product");
            var productId = boxw.attr("data-id");
            // if (!product) {
            $tsBlockUI({
                message: "<img src='/img/123321.gif'>",
                css: {}
            });
            tshowAjax({
                type: 'POST',
                url: "/product/findProduct",
                data: {
                    productId: productId
                },
                success: function(res){
                    if (!res.success) {
                        createPromptBox(res.mess.message);
                        return;
                    }
                    $.unblockUI();
                    product = JSON.parse(res.map.product);
                    boxw.data({
                        "product": product
                    })
                },
                async: false
            });
        // }
        }
    var html = createProductInfoHtml(product);
    $("body").css({
        "overflow": "hidden"
    });
    $("#content").show().find(".imgMain").html($(html).data({
        "boxwData": boxw.data()
    }));
    $("#content").attr({
        'data-pid': product.label.pid
    });
    showPoint(product.productId);
}

/*作品详情页面html*/
function createProductInfoHtml(product){
    var nowStudentId = $(document).data('student').studentId;
    var proStudentId = product.student.studentId;
    var html = "<div class=\"px\" id='productInfo' data-id='" + product.productId + "' data-ver='" +
    product.version +
    "' data-vercou='" +
    product.versionCount +
    "' data-seriesId='" +
    product.productSeriesId +
    "'> " +
    "   <div class=\"px-1\"> " +
    "     <div class=\"px-l-h\"> " +
    "       <span data-id='" +
    product.label.labelId +
    "' class=\"px-l-h-title\">" +
    product.label.labelName +
    "</span> " +
    "       <span class=\"px-l-h-header\">" +
    product.productName +
    "</span> " +
    
    "       <span class=\"px-l-f-t\"> ";
    for (var i = 0; i < product.points.length; i++) {
        html += "<span><a target='_blank' href='/label/home/" + product.label.labelId + "?pointId=" + product.points[i].pointId + "'>" + product.points[i].pointName + "</a></span>";
    }
    html += "</span> " +
    "<div class='pro-time'><span>" +
    formatDate(product.createTime) +
    "</span></div>" +
    "     </div> " +
    "     <div class=\"px-1-img\"> " +
    "<div class='pro-dis'><p>" +
    product.productDisc +
    "</p></div>" +
    "       <a class='imgParent' data='" +
    product.productId +
    "' >" +
    "<div class='toumc'></div>" +
    "<img src = '" +
    product.content +
    "'> " +
    "         " +
    "       </a> " +
    "   <!--右边设计--> " +
    "   <div class=\"px-r\"> " +
    "     <div class=\"px-r-h\"> " +
    "       <h3>作者<span class='pro-original'>" +
    (product.original == 'Y' ? '原创' : '转载') +
    "</span></h3> " +
    "       <div class=\"px-r-h-u\"> " +
    "           <div class=\"message\"> " +
    "             <div class=\"name\" id='student' data-id = '" +
    product.student.studentId +
    "'> " +
    "<span class='icon icon-student'></span><a target='_blank' href='/student/home/" +
    product.student.invitationId +
    "'>" +
    product.student.realName +
    "</a>" +
    (product.student.isAttention == 'N' ? '<button class=\'stu-attention attention\'>关注</button>' : '<button class=\'stu-attention  attention attentioned\'>已关注</button>') +
    "               </div> " +
    "              <div class=\"school\"> " +
    "<span class='icon icon-school'></span><a  target='_blank' href='/school/home/" +
    product.student.school.schoolId +
    "'>" +
    product.student.school.name +
    "</a> " +
    "              </div> " +
    "<div><span class='icon icon-label'></span><a>" +
    product.student.jobLabel.labelName +
    "</a></div>" +
    "<div class='student-year'><span class='icon icon-year'></span><a>" +
    product.student.year.substring(0, 4) +
    "</a></div>" +
    "           </div> " +
    "         </a> " +
    "       </div> " +
    "     </div> " +
    "       <div class=\"px-l-f-u\"> " +
    "         <h3>共同参与人员</h3> " +
    "         <ul> ";
    for (var j = 0; j < product.productStudents.length; j++) {
        html += "<li> " +
        "             <a target='_blank' href='/student/home/" +
        product.productStudents[j].invitationId +
        "'> " +
        "               <img src='" +
        product.productStudents[j].headIco +
        "'> " +
        "             </a> " +
        "           </li> ";
    }
    html += "         </ul> " +
    "       </div> " +
    "   </div> " +
    "     </div> " +
    
    "   </div> " +
    
    " </div>";
    html += "     <div class=\"px-l-f\"> " +
    "     <div class=\"px-r-f\"> " +
    "       <ul> " +
    (nowStudentId == proStudentId ? "<li><button><span title='发布新版本' class='icon icon-sub-pro new-version'></span></button></li>" : "") +
    "<li><button><span title='上一版本' class='icon icon-prev-version'></span></button></li>" +
    "<li><button><span class='icon-version-sum'><span>" +
    product.version +
    "</span>/<span>" +
    product.versionCount +
    "</span></span></button></li>" +
    "<li><button><span title='下一版本' class='icon icon-next-version'></span></button></li>" +
    "<li><button><span title='隐藏所有点' class='icon icon-remove-point'></span></button></li>" +
    "<li><button><span title='筛选出热评点' class='icon icon-filter-point'></span></button></li>" +
    "<li><button><span title='显示所有点' class='icon icon-show-pointed'></span></button></li>" +
    "<li><button><span title='打点' class='icon icon-add-point putPoint'></span></button></li>" +
    "         <li> " +
    "           " +
    (product.collections.length == 0 ? '<button class=\"pro-col\"><span title="收藏" class="icon icon-collect"></span>' : '<button class=\"pro-col pro-coled\"><span title="取消收藏" class="icon icon-collected"></span>') +
    "<span class='number'>" +
    product.collectionCount +
    "</span> </button> " +
    "         </li> " +
    "         <li> " +
    "          " +
    (product.praises.length == 0 ? ' <button class=\"pro-love\"><span title="喜爱" class="icon icon-love"></span>' : ' <button title="取消喜爱" class=\"pro-love pro-loved\"><span class="icon icon-loved"></span>') +
    "<span class='number'>" +
    product.praiseCount +
    "</span></button> " +
    "         </li> " +
    "         <li> " +
    "            " +
    (product.votes.length == 0 ? '<button class=\"pro-vote\"><span title="投一票" class="icon icon-vote"></span>' : '<button class=\"pro-vote pro-voted\"><span class="icon icon-voted"></span>') +
    "<span class='number'>" +
    product.voteCount +
    "</span></button> " +
    "         </li> " +
    (nowStudentId == proStudentId ? "<li><button><span title='删除' class='icon icon-delete'></span></button></li>" : "") +
    "       </ul> " +
    "     </div> " +
    "     </div> ";
    return html;
}


/*生成评论点*/
function showPoint(productId){
    $tsBlockUI({
        message: "<img src='/img/123321.gif'>",
        css: {}
    });
    tshowPost("/product/findTtileReply", {
        'productId': productId
    }, function(res){
        $.unblockUI();
        res = JSON.parse(res.map.list);
        var imgParent = $(".imgParent");
        for (var i = 0; i < res.length; i++) {
            var point = res[i];
            var left = point.start * 100 + "%";
            var top = point.end * 100 + "%";
            var pointHtml = "";
            var b = point.replyStudent.studentId == ($(document).data("student") ? $(document).data("student").studentId : '')//判斷是否是當前用戶
            if (b) {
                pointHtml += "<div class=\"mineP\" style='top: " + top + ";left: " + left + "'> " +
                "<div class='reply-point'></div>" +
                "	<div class=\"point-title\"><span title='标题：" +
                point.titleName +
                "  总参与(" +
                point.nrPersonListCount +
                ")'>" +
                "<div class='point-pike'></div>" +
                "		<div class='point-title-name' ><a>" +
                point.titleName +
                "</a></div><div class='point-title-nrcount'><a>#" +
                point.nrPersonListCount +
                "</a></div> " +
                "	</span></div> " +
                "<div class='point-delete'><div>X</div></div>" +
                "</div>";
            }
            else {
                pointHtml += "<div class=\"box\" style='top: " + top + ";left: " + left + "'> " +
                "<div class='reply-point'></div>" +
                "	<div class=\"point-title\"><span title='标题：" +
                point.titleName +
                "  总参与(" +
                point.nrPersonListCount +
                ")'>" +
                "<div class='point-pike'></div>" +
                "		<div class='point-title-name' ><a>" +
                point.titleName +
                "</a></div><div class='point-title-nrcount'><a>#" +
                point.nrPersonListCount +
                "</a></div> " +
                "	</span></div> " +
                "</div>";
            }
            //imgParent.append($(pointHtml).data({notationReply:res[i]}));
            imgParent.append($(pointHtml).data({
                "titleReply": res[i]
            }));//此处先不绑定此点的评论数据,待点击查看是将完整数据在进行绑定，前台绑定后台只需查询一次，减轻后台压力
        }
    })
}


/*通过事件代理为可打点的活动对象绑定打点事件*/
function kedadian(){
    /*发步新版本按钮事件*/
    $("#content").delegate(".new-version", "click", uploadProductNewVersion);
    /*发布新版本遮罩层绑定事件*/
    $("#up-new-version .tshow-zzc").on("click", function(){
        $("#up-new-version").hide();
        $(".zs-r-wt-b-num").text(0);
    });
    
    /*切换版本事件*/
    $("#content").delegate(".px-l-f .icon-prev-version", "click", function(){
        var $me = $(this);
        var box = $('.imgMain .px');
        var seriesid = box.attr("data-seriesid");
        var verCou = parseInt(box.attr("data-vercou"));
        var ver = parseInt(box.attr("data-ver"));
        if (ver <= 1) {
            return;
        }
        --ver;
        var obj = {
            productSeriesId: seriesid,
            version: ver
        }
        tshowPost("/product/findProductVersion", obj, function(res){
            //console.log(res);
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            var product = JSON.parse(res.map.productVersion);
            var html = createProductInfoHtml(product);
            $("#content").show().find(".imgMain").html($(html));
            showPoint(product.productId);
        });
    });
    
    $("#content").delegate(".px-l-f .icon-next-version", "click", function(){
        var $me = $(this);
        var box = $('.imgMain .px');
        var productId = box.attr("data-id");
        var seriesid = box.attr("data-seriesid");
        var verCou = parseInt(box.attr("data-vercou"));
        var ver = parseInt(box.attr("data-ver"));
        if (ver >= verCou) {
            return;
        }
        ++ver;
        if (ver == verCou) {
            var obj = {
                productSeriesId: seriesid
            }
            tshowPost("/product/findProductBySeriesId", obj, function(res){
                //console.log(res);
                var product = JSON.parse(res.map.product);
                var html = createProductInfoHtml(product);
                $("#content").show().find(".imgMain").html($(html));
                showPoint(product.productId);
            });
        }
        else {
            var obj = {
                productSeriesId: seriesid,
                version: ver
            }
            tshowPost("/product/findProductVersion", obj, function(res){
                //console.log(res);
                var product = JSON.parse(res.map.productVersion);
                var html = createProductInfoHtml(product);
                $("#content").show().find(".imgMain").html($(html));
                showPoint(product.productId);
            });
        }
        
    });
    
    $("#content").delegate(".active", "click", function(e){
        //console.log(11);
        //按钮事件
        $('.icon-show-point').click();
        //去除点可移动（正在编辑）的点
        $(".move").remove();
        //此处的offset浏览器解析为相对父元素定位的left和top，不是相对于文档的
        var move = $("<div class='move'><div class='reply-point'></div></div>").offset({
            left: e.pageX - $(this).offset().left,
            top: e.pageY - $(this).offset().top
        });
        //添加打点
        $(this).append(move);
        isOverlay(move, 0);
        //打开打点编辑器
        $(".pointEdit").show().offset({
            left: move.offset().left - $(".pointEdit").width() / 2,
            top: move.offset().top + 25
        });
        //将此点设为当前编辑状态
        move.addClass("activePoint")
    })
    //进入全屏编辑模式
    $('.edit-full-box').on('click', function(){
        $('.big-edit').show();
        $('.big-edit-content').focus();
        $('.big-edit-content').html($('.divEdit').html());
    });
    $('.edit-small-box').on('click', function(){
        $('.big-edit').hide();
        $('.divEdit').html($('.big-edit-content').html())
    });
    //推荐标题
    $('.titleEdit').on('focus', function(){
        var labelId = $('.px-l-h-title').attr('data-id');
        var box = $('.reply-point-recommend');
        box.show();
        if (box.data('data')) {
            return;
        }
        tshowPost('/product/findReplyPoint', {
            labelId: labelId
        }, function(res){
            //console.log(res);
            box.html(createRecoRpHtml(res.bean));
            box.data({
                'data': res.bean
            });
        })
    }).on('keyup', function(){
        var $me = $(this);
        findReplyPointWeb($me.val());
    }).on('blur', function(){
        var $me = $(this);
        setTimeout(function(){
            $me.next().hide();
        }, 200)
    }).on('keyup', function(){
        var $me = $(this);
        var val = $me.val();
        if (val.length >= 15) {
            $me.val(val.substring(0, 15));
        }
    });
    //推荐标题选中事件
    $('#content').delegate('.reply-point-recommend>li', 'click', function(){
        var $me = $(this);
        var pointName = $me.find('span').text().trim();
        $('.titleEdit').val(pointName);
        $me.parent().hide();
    });
    
}

function findReplyPointWeb(val){
    //console.log(val)
    var html = "";
    var box = $('.reply-point-recommend');
    var list = box.data('data');
    for (var i = 0; i < list.length; i++) {
        var obj = list[i];
        if (obj.pointName.indexOf(val) != -1) {
            html += '<li><span data-id="' + obj.pointId + '">' + obj.pointName + '</span></li>'
        }
    }
    box.html(html);
}

/*拼接推荐的标题*/
function createRecoRpHtml(list){
    var html = "";
    for (var i = 0; i < list.length; i++) {
        var obj = list[i];
        html += "<li><span data-id='" + obj.pointId + "'>" + obj.pointName + "</span></li>"
    }
    return html;
}

/*禁止常规点的点击事件冒泡*/
function stopClick(str){
    $("#content").delegate(str, "click", function(){
        return false;
    })
}


/*#content（作品详情页面）下代理事件*/
function pointSubmit(){
    /*打点评论提交事件*/
    $("#content").delegate(".bsubmit", "click", function(){
        //console.log("bsubmit")
        //提交后台
        var me = this;
        var title = $("#title").val();
        var advise = $("#advise").text();
        var activePoint = $(".activePoint");
        var productId = $(".activePoint").parent().attr("data");
        var labelId = $(".px-l-h-title").attr("data-id");
        //计算此点相对于此图片的位置比例
        var x = (activePoint.position().left / $(".activePoint").parent().width()).toFixed(3);
        var y = (activePoint.position().top / $(".activePoint").parent().height()).toFixed(3);
        //console.log("x:" + x + "y:" + y)
        var obj = {
            productId: productId,
            titleName: title,
            start: x,
            end: y,
            notationContent: advise
        }
        if (title.trim() == '' || advise == '') {
            createPromptBox("标题或意见不能为空");
            return;
        }
        else 
            if ((title + advise).toLowerCase().indexOf('<script') != -1) {
                title = title.replace(/<script/g, '&ltscript')
                advise = advise.replace(/<script/g, '&ltscript')
            }
        $("#content .pointEdit").hide();
        tshowPost("/product/saveTitleReply", obj, function(res){
            if (!res.success) {
                createPromptBox(res.message);
            }
            res = res.bean;
            res.createTime = new Date().toString();
            //console.log(res);
            activePoint.data({
                "titleReply": res
            });
            //改变子节点，置为查看按钮
            var pointTitle = $("<div class='point-title'><span><div class='point-pike'></div><div class='point-title-name'><div>" + title + "</div></div><div class='point-title-nrcount'><div>#(0)</div></div></span></div><div class='point-delete'><div>X</div></div>")
            $(".activePoint").append(pointTitle);
            //将此点改为不可移动
            $(".activePoint").removeClass("move");
            $(".activePoint").addClass("mineP");
            $(".activePoint").removeClass("activePoint");
        });
        
    });
    
    $("#content").delegate(".close", "click", function(){
        $("#content").hide();
        scrollBody();
        //恢复主页面的滚动条位置
        $("body").scrollTop($("body").data("scrollTop"));
    });
    
    //取消事件
    $("#content").delegate(".cancel", "click", function(){
        //console.log("cancel");
        $(".activePoint").remove();
        $(".putPoint").removeClass("active-color");
        $("#content .active").removeClass("active");
        $('.pointEdit').hide();
        $('#title').val('');
        $('#advise').text('');
        $('.putPointed').addClass('putPoint').removeClass('putPointed').removeClass('icon-add-pointed').addClass('icon-add-point');
        return false;
    });
    
    
    /*查看点的详情*/
    $("#content").delegate(".box .point-title,.mineP .point-title", "click", function(){
        $('#content .imgParent').removeClass('active');
        $('.move').remove();
        $('.icon-add-pointed').removeClass('icon-add-pointed').addClass('icon-add-point');
        $('#content').css({
            'overflow': 'hidden'
        });
        //将当前作品页面定位到左边
        $("#content .px-1").addClass("thow-abso-left").parent().addClass("margin-0");
        var me = this;
        var point = $(this).parents(".mineP").length == 0 ? $(this).parents(".box") : $(this).parents(".mineP");
        if (point.attr("class").indexOf("active-p") != -1) {//表示此点是上次操作的点
            //显示页面
            $(".reply-main").animate({
                opacity: 1,
                right: "0px"
            }, 300).show().prev().show();
            return;
        }
        //将当前活动点取消活动状态
        $(".mineP.active-p,.box.active-p").removeClass("active-p")
        var studentId = $(document).data("student").studentId;
        var productStudentId = $("#student").attr("data-id")
        var reply = point.addClass("active-p");//标记此点为活动状态，以便于此点的回复数据的前台缓存
        var titleReply = point.data("titleReply");
        var titleReplyId = titleReply.titleReplyId;
        //判断此点是否已经缓存了数据
        var notationReply;
        if ($(this).data("notationReply")) {
            notationReply = $(this).data("notationReply");
            var html = "<h2 id=\"reply-title\" data-id='" + titleReplyId + "' stu-id='" + titleReply.replyStudent.studentId + "'>" + titleReply.titleName + "</h2>" +
            "			<h3 id=\"reply-count\">" +
            notationReply.length +
            '个回答' +
            "</h3><div class='reply-box-list'>";
            for (var i = 0; i < notationReply.length; i++) {
                var obj = notationReply[i];
                html += "			<div class=\"reply-box\" data-id='" + obj.notationReplyId + "' stu-id='" + obj.replyStudent.studentId + "'>" +
                "				<div class=\"reply-header\">" +
                "					<div>" +
                "						<a class=\"student-img\" target='_blank' href='/student/home/" +
                obj.replyStudent.invitationId +
                "'><img src='" +
                obj.replyStudent.headIco +
                "'> </a> <a></a>" +
                "					</div>" +
                "				</div>" +
                "				<div>" +
                "					<a  target='_blank' href='/student/home/" +
                obj.replyStudent.invitationId +
                "'>" +
                obj.replyStudent.realName +
                "</a>" +
                "				</div>" +
                "				<div class=\"reply-content\">" +
                obj.notationContent +
                "</div>" +
                "				<div class=\"reply-action\">" +
                "					<div>" +
                "						<a href=\"javascript:;\" class=\"reply-date\">" +
                getDateInterval(obj.createTime) +
                "</a> <a" +
                "							class=\"reply-count reply_s\"><span>" +
                obj.replyReplyCount +
                "</span><span>条评论</span> </a>" +
                "						<a>" +
                (obj.myPraiseCount == 0 ? '<span class=\"praise parent\" data-id=\'' + obj.notationReplyId + '\'>有用</span>' : '<span class=\"praise praised parent\" data-id=\'' + obj.notationReplyId + '\'>取消有用</span>') +
                "</a> <a>" +
                (productStudentId == studentId ? (obj.isadopt == 'N' ? '<span class=\"adopt\" data-id=\'' + obj.notationReplyId + '\'>采纳</span>' : '<span class=\"adopt adopted\" data-id=\'' + obj.notationReplyId + '\'>取消采纳</span>') : '') +
                "" +
                "						</a> " +
                (obj.replyStudent.studentId == studentId ? "<a><span class='delete-reply'><span>删除</span></span></a>" : "") +
                "<a class='reply-praise-count'>" +
                (obj.praiseCount == 0 ? '' : obj.praiseCount + '人认为有用') +
                "</a><a class='isadopt'>" +
                (obj.isadopt == 'N' ? '' : '已采纳') +
                "</a>" +
                "					</div>" +
                "					<div class=\"reply-text-main\">" +
                "						<div class=\"reply-reply-list\">" +
                
                "						</div>" +
                "						<div stu-id=\"2011\" class='edit-box'>" +
                "							<div contenteditable=\"true\" class=\"reply-text\"></div>" +
                "							<div class=\"reply-but-main\">" +
                "								<div class=\"reply-but reply-box-submit\">发表</div>" +
                "								<div class=\"exit-but\">取消</div>" +
                "							</div>" +
                "						</div>" +
                "					</div>" +
                "				</div>" +
                "			</div>";
            }
            html += "						</div><div stu-id=\"2011\" class='edit-box'>" +
            "							<div contenteditable=\"true\" class=\"reply-text\"></div>" +
            "							<div class=\"reply-but-main\">" +
            "								<div class=\"reply-but title-submit\">发表</div>" +
            "								<div class=\"exit-but\">取消</div>" +
            "							</div>" +
            "						</div>";
            //显示页面
            $(".reply-main").html(html);
        }
        else {
            $tsBlockUI({
                message: "<img src='/img/123321.gif'>",
                css: {
                    width: '50px',
                    height: '50px',
                    top: '47%',
                    left: '73%',
                    'border-radius': '50%',
                    'background': 'none',
                    border: 'none'
                
                },
                // showOverlay: false
            });
            tshowAjax({
                type: 'POST',
                url: "/product/findNotationReply",
                data: {
                    replyId: titleReplyId
                },
                success: function(res){
                    $.unblockUI();
                    var labelId = $('#content').attr('data-pid');
                    notationReply = JSON.parse(res.map.list);
                    $(me).data({
                        "notationReply": notationReply
                    })
                    //console.log(notationReply);
                    //拼接页面
                    var html = "<h2 id=\"reply-title\" data-id='" + titleReplyId + "' stu-id='" + titleReply.replyStudent.studentId + "'>" + titleReply.titleName + "</h2>" +
                    "			<h3 id=\"reply-count\"><span class='reply-count-num'>" +
                    notationReply.length +
                    '</span>个回答' +
                    "</h3><div class='reply-box-list'>";
                    for (var i = 0; i < notationReply.length; i++) {
                        var obj = notationReply[i];
                        html += "			<div class=\"reply-box\" data-name ='" + obj.replyStudent.realName + "' data-inv-id='" + obj.replyStudent.invitationId + "' data-id='" + obj.notationReplyId + "' stu-id='" + obj.replyStudent.studentId + "'>" +
                        "				<div class=\"reply-header\">" +
                        "					<div>" +
                        "						<a class=\"student-img\" target='_blank' href ='/student/home/" +
                        obj.replyStudent.invitationId +
                        "'><img src='" +
                        obj.replyStudent.headIco +
                        "'> </a> <a></a>" +
                        "					</div>" +
                        "				</div>" +
                        "				<div>" +
                        "					<a " +
                        (obj.replyStudent.jobLabel.labelId == labelId ? 'class=\'col-green\'' : 'class=\'col-red\'') +
                        " target='_blank' href='/student/home/" +
                        obj.replyStudent.invitationId +
                        "'>" +
                        obj.replyStudent.realName +
                        "</a>" +
                        "				</div>" +
                        "				<div class=\"reply-content\">" +
                        obj.notationContent +
                        "</div>" +
                        "				<div class=\"reply-action\">" +
                        "					<div>" +
                        "						<a href=\"javascript:;\" class=\"reply-date\">" +
                        getDateIntervalS(obj.createTime, res.date) +
                        "</a> <a" +
                        "							class=\"reply-count reply_s\"><span>" +
                        obj.replyReplyCount +
                        "</span><span>条评论</span> </a>" +
                        "						<a>" +
                        (obj.myPraiseCount == 0 ? '<span class=\"praise parent\" data-id=\'' + obj.notationReplyId + '\'>有用</span>' : '<span class=\"praise praised parent\" data-id=\'' + obj.notationReplyId + '\'>取消有用</span>') +
                        "</a> <a>" +
                        (productStudentId == studentId ? (obj.isadopt == 'N' ? '<span class=\"adopt\" data-id=\'' + obj.notationReplyId + '\'>采纳</span>' : '<span class=\"adopt adopted\" data-id=\'' + obj.notationReplyId + '\'>取消采纳</span>') : '') +
                        "" +
                        "						</a>" +
                        (obj.replyStudent.studentId == studentId ? "<a><span class='delete-reply'><span>删除</span></span></a>" : "") +
                        " <a class='reply-praise-count'>" +
                        (obj.praiseCount == 0 ? '' : obj.praiseCount + '人认为有用') +
                        "</a><a class='isadopt'>" +
                        (obj.isadopt == 'N' ? '' : '已采纳') +
                        "</a>" +
                        "					</div>" +
                        "					<div class=\"reply-text-main\">" +
                        "						<div class=\"reply-reply-list\">" +
                        
                        "						</div>" +
                        "						<div stu-id=\"2011\" class='edit-box'>" +
                        "							<div contenteditable=\"true\" class=\"reply-text\"></div>" +
                        "							<div class=\"reply-but-main\">" +
                        "								<div class=\"reply-but reply-box-submit\">发表</div>" +
                        "								<div class=\"exit-but\">取消</div>" +
                        "							</div>" +
                        "						</div>" +
                        "					</div>" +
                        "				</div>" +
                        "			</div>";
                    }
                    html += "						</div><div stu-id=\"2011\" class='edit-box'>" +
                    "							<div contenteditable=\"true\" class=\"reply-text\"></div>" +
                    "							<div class=\"reply-but-main\">" +
                    "								<div class=\"reply-but title-submit\">发表</div>" +
                    "								<div class=\"exit-but\">取消</div>" +
                    "							</div>" +
                    "						</div>";
                    //显示页面
                    $(".reply-main").html(html);
                },
                async: true
            });
        }
        
        //显示页面
        $(".reply-main").animate({
            opacity: 1,
            right: "0px"
        }, 300).show().prev().show();
        $('.putPointed').removeClass('putPointed').addClass('putPoint');
        $('.putPointed').removeClass('icon-add-pointed').addClass('icon-add-point');
        return false;
    });
    //删除点
    $("#content").delegate('.point-delete', 'click', function(){
        var boo = confirm("确定要删除吗?");
        if (!boo) {
            return;
        }
        var $me = $(this);
        var titleReplyId = $me.parent().data('titleReply').titleReplyId;
        tshowPost('/product/deleteTitleReply', {
            titleReplyId: titleReplyId
        }, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            $me.parent().remove();
        });
        return false;
    });
    
    
    //位右侧按钮绑定事件（标记父元素为可打点状态）
    $("#content").delegate(".putPoint", "click", function(){
        //console.log("toActive")
        var $me = $(this);
        //显示全部点
        $('.icon-show-point').click();
        $(".active").removeClass("active");
        $me.parents(".imgMain").find(".imgParent").addClass("active");//根据布局需要改变
        $me.addClass('putPointed').removeClass('putPoint');
        $me.addClass('icon-add-pointed').removeClass('icon-add-point');
        //去除点可移动（正在编辑）的点
        $(".move").remove();
        return false;
    });
    $("#content").delegate(".putPointed", "click", function(){
        $('.move').remove();
        $('.pointEdit').hide();
        //console.log("toActive")
        var $me = $(this);
        $(".active").removeClass("active");
        $me.addClass('putPoint').removeClass('putPointed');
        $me.addClass('icon-add-point').removeClass('icon-add-pointed');
        //去除点可移动（正在编辑）的点
        $(".move").remove();
        return false;
    });
    
    
    //为遮罩层绑定点击事件
    $("#content .tshow-zzc").on("click", function(){
        //console.log("tshow-zzc");
        $('#content').css({
            'overflow': 'auto'
        });
        $(this).hide().next().animate({
            opacity: 0,
            right: "-883px"
        }, 300);
        //回复作品div位置
        $("#content .px-1").removeClass("thow-abso-left").parent().removeClass("margin-0");
        
    })
    /*评论回复框焦点事件*/
    $("#content").delegate(".reply-text", "focus click", function(){
        $(this).addClass("reply-text-active");
    }).delegate(".reply-text", "blur", function(){
        var me = this;
        setTimeout(function(){
            $(me).removeClass("reply-text-active");
        }, 200);
    });
    
    /*评论发表 title回复*/
    $("#content").delegate(".title-submit", "click", function(){
        //console.log("title-submit");
        var $me = $(this);
        var student = $(document).data("student");
        var replyId = $("#reply-title").attr("data-id");
        var replyContent = $(this).parent().prev().text();
        //因为后台表设计的原因，此处start和end随便给个值#
        var obj = {
            titleReplyId: replyId,
            notationContent: replyContent,
            start: "#",
            end: "#"
        }
        if (replyContent.trim() == '') {
            createPromptBox("评论不能为空");
            return;
        }
        else 
            if (replyContent.toLowerCase().indexOf('<script') != -1) {
                replyContent = replyContent.replace(/</g, '&lt')
            }
        tshowPost("/product/saveNotationReply", obj, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            res = res.bean;
            //console.log(res);
            //拼接页面
            var html = "";
            html += "			<div class=\"reply-box\" data-name ='" + student.realName + "' data-inv-id='" + student.invitationId + "' data-id='" + res.notationReplyId + "' stu-id='" + student.studentId + "'>" +
            "				<div class=\"reply-header\">" +
            "					<div>" +
            "						<a class=\"student-img\" target='_blank' href = '/student/home/" +
            student.invitationId +
            "'><img src='" +
            student.headIco +
            "'> </a> <a></a>" +
            "					</div>" +
            "				</div>" +
            "				<div>" +
            "					<a target='_blank' href='/student/home/" +
            student.invitationId +
            "'>" +
            student.realName +
            "</a>" +
            "				</div>" +
            "				<div class=\"reply-content\">" +
            obj.notationContent +
            "</div>" +
            "				<div class=\"reply-action\">" +
            "					<div>" +
            "						<a href=\"javascript:;\" class=\"reply-date\">" +
            getNowDate() +
            "</a> <a" +
            "							class=\"reply-count reply_s\"><span>" +
            0 +
            "</span><span>条评论</span> </a>" +
            "						<a>" +
            '<span class=\"praise parent\" data-id=\'' +
            res.notationReplyId +
            '\'>有用</span>' +
            "</a><a><span class='delete-reply'><span>删除</span></span></a> <a class='reply-praise-count'>" +
            "</a><a class='isadopt'>" +
            "</a>" +
            "					</div>" +
            "					<div class=\"reply-text-main\">" +
            "						<div class=\"reply-reply-list\">" +
            
            "						</div>" +
            "						<div stu-id=\"2011\" class='edit-box'>" +
            "							<div contenteditable=\"true\" class=\"reply-text\"></div>" +
            "							<div class=\"reply-but-main\">" +
            "								<div class=\"reply-but reply-box-submit\">发表</div>" +
            "								<div class=\"exit-but\">取消</div>" +
            "							</div>" +
            "						</div>" +
            "					</div>" +
            "				</div>" +
            "			</div>";
            $("#content .reply-box-list").append(html);
            var replyBox = $me.closest('.reply-main').find('.reply-count-num');
            replyBox.text(parseInt(replyBox.text()) + 1);
        });
        
        //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
        $("#content .active-p .point-title").data({
            "notationReply": null
        });
        // $("#content").find(".px").data("boxwData").product = null;
    })
    /*评论发表 一级回复*/
    $("#content").delegate(".reply-box-submit", "click", function(){
        //console.log("reply-box-submit");
        var me = this;
        var student = $(document).data("student");
        var replyBox = $(this).parents(".reply-box");
        var notationReplyId = replyBox.attr("data-id");
        var toStudentId = replyBox.attr("stu-id");
        var toInvitationId = replyBox.attr("data-inv-id");
        var toRealName = replyBox.attr("data-name");
        var replyContent = $(this).parent().prev().text();
        var obj = {
            "toreplyStudent.studentId": toStudentId,
            notationReplyId: notationReplyId,
            replyContent: replyContent
        }
        if (replyContent.trim() == '') {
            createPromptBox("意见不能为空");
            return;
        }
        else 
            if (replyContent.toLowerCase().indexOf('<script') != -1) {
                replyContent = replyContent.replace(/</g, '&lt')
            }
        tshowPost("/product/saveNotationReplyReply", obj, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            //console.log(res);
            res = res.bean;
            //拼接页面
            var html = "";
            var replyReply = obj;
            html += "							<div class=\"reply-reply-box\" data-id='" + res.notationReplyReplyId + "'  stu-id='" + student.studentId + "' stu-name='" + student.realName + "'>" +
            "								<a class=\"student-img\" target='_blank' href =  '/student/home/" +
            student.invitationId +
            "'><img src='" +
            student.headIco +
            "'> </a>" +
            "" +
            "								<div class=\"reply-reply-right\">" +
            "									<div>" +
            "<a target='_blank' href='/student/home/" +
            student.invitationId +
            "'>" +
            student.realName +
            "</a></div>" +
            "									<div class='reply-reply-right-content'>" +
            obj.replyContent +
            "</div>" +
            "									<div class=\"reply-reply-action\">" +
            "										<a href=\"javascript:;\" class=\"reply-date\">" +
            getNowDate() +
            "</a>  <a>" +
            '<span class=\"praise\" data-id=\'' +
            res.notationReplyReplyId +
            '\' >赞</span>' +
            "" +
            "										</a><a><span data-id='" +
            res.notationReplyReplyId +
            "' class='reply-reply-delete'><span>删除</span></span></a> <span class='reply-praise-count'>" +
            "</span>" +
            "										<div stu-id=\"2011\" class=\"edit-box\" style='display:none'>" +
            "											<div contenteditable=\"true\" class=\"reply-text\"></div>" +
            "											<div class=\"reply-but-main\">" +
            "												<div class=\"reply-but reply-reply-box-submit\">发表</div>" +
            "												<div class=\"exit-but\">取消</div>" +
            "											</div>" +
            "										</div>" +
            "									</div>" +
            "								</div>" +
            "							</div>";
            $(me).parents(".edit-box").prev().append(html);
            //评论总数加1
            var replyCountBox = $(me).closest('.reply-action').find('.reply-count span:eq(0)');
            replyCountBox.text(parseInt(replyCountBox.text()) + 1);
            $(me).parent().prev().text('');
        })
        //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
        $("#content .active-p .point-title").data({
            "notationReply": null
        });
        //$("#content").find(".px").data("boxwData").product = null;
    })
    /*评论发表 二级回复*/
    $("#content").delegate(".reply-reply-box-submit", "click", function(){
        var me = this;
        var student = $(document).data("student");
        var replyBox = $(this).parents(".reply-box");
        var notationReplyId = replyBox.attr("data-id");
        var replyReplyBox = $(this).parents(".reply-reply-box");
        var toStudentId = replyReplyBox.attr("stu-id");
        var toStudentName = replyReplyBox.attr("stu-name");
        var notationReplyReplyId = replyReplyBox.attr("data-id");
        var replyContent = $(this).parent().prev().text();
        var obj = {
            "toreplyStudent.studentId": toStudentId,
            notationReplyId: notationReplyId,
            replyContent: replyContent,
            toNotationReplyReplyId: notationReplyReplyId
        }
        if (replyContent.trim() == '') {
            createPromptBox("评论不能为空");
            return;
        }
        else 
            if (replyContent.toLowerCase().indexOf('<script') != -1) {
                replyContent = replyContent.replace(/</g, '&lt')
            }
        //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
        $("#content .active-p .point-title").data({
            "notationReply": null
        });
        // $("#content").find(".px").data("boxwData").product = null;
        tshowPost("/product/saveNotationReplyReply", obj, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            //console.log(res);
            //拼接页面
            var html = "";
            var replyReply = obj;
            html += "							<div class=\"reply-reply-box\" data-id='" + res.notationReplyReplyId + "' stu-id='" + student.studentId + "' stu-name='" + student.realName + "'>" +
            "								<a class=\"student-img\" target='_blank' href =  '/student/home/" +
            student.invitationId +
            "'><img src='" +
            student.headIco +
            "'> </a>" +
            "" +
            "								<div class=\"reply-reply-right\">" +
            "									<div>" +
            "										<a>" +
            student.realName +
            "</a>回复:<a>" +
            toStudentName +
            "</a>" +
            "									</div>" +
            "									<div class='reply-reply-right-content'>" +
            obj.replyContent +
            "</div>" +
            "									<div class=\"reply-reply-action\">" +
            "										<a href=\"javascript:;\" class=\"reply-date\">" +
            getNowDate() +
            "</a> <a>" +
            '<span class=\"praise\" data-id=\'' +
            res.notationReplyReplyId +
            '\' >赞</span>' +
            "" +
            "										</a>" +
            "<a><span class='reply-reply-delete' data-id='" +
            res.notationReplyReplyId +
            "'>删除</span></a>" +
            " <span class='reply-praise-count'>" +
            "</span>" +
            "										<div stu-id=\"2011\" class=\"edit-box\" style='display:none'>" +
            "											<div contenteditable=\"true\" class=\"reply-text\"></div>" +
            "											<div class=\"reply-but-main\">" +
            "												<div class=\"reply-but reply-reply-box-submit\">发表</div>" +
            "												<div class=\"exit-but\">取消</div>" +
            "											</div>" +
            "										</div>" +
            "									</div>" +
            "								</div>" +
            "							</div>";
            $(me).parents(".edit-box").hide();
            $(me).parents(".edit-box").find(".reply-text").text("");
            $(me).parents(".reply-reply-list").append(html);
        });
        $(this).parent().prev().html("");
    });
    
    /*评论取消*/
    $("#content").delegate(".exit-but", "click", function(){
        $(this).closest('.reply-text-main').hide().find('.reply-text').html("");
    });
    
    /*评论点击事件显示隐藏下方编辑框*/
    $("#content").delegate(".reply_s", "click", function(){
        var $me = $(this);
        //console.log("pinglunxianshi")
        var $dom = $(this).parent().parent().find(".reply-text-main");
        var replyReplyList = $dom.find(".reply-reply-list");
        var student = $(document).data("student");
        if ($dom.css("display") == "none") {
            if ($me.data("data")) {
                $dom.show();
                return;
            }
            var replyBox = $(this).closest(".reply-box");
            var notationReplyId = replyBox.attr("data-id");
            var studentId = replyBox.attr("stu-id");
            if (replyReplyList.html().trim() == "") {
                var top = $me.offset().top;
                var left = $me.offset().left;
                $tsBlockUI({
                    message: "<img src='/img/123321.gif'>",
                    css: {
                        width: '50px',
                        height: '50px',
                        top: top,
                        left: left,
                        'border-radius': '50%',
                        'background': 'none',
                        border: 'none',
                        cursor: 'pointer'
                    
                    },
                    // showOverlay: false
                });
                tshowAjax({
                    type: 'POST',
                    url: "/product/findNotationReplyReply",
                    data: {
                        notationReplyId: notationReplyId
                    },
                    success: function(res){
                        bean = JSON.parse(res.map.list);
                        $.unblockUI();
                        $me.data().data = bean;
                        var notationReply = bean;
                        //console.log(bean);
                        var html = "";
                        for (var i = 0; i < bean.length; i++) {
                            var replyReply = bean[i];
                            html += "							<div class=\"reply-reply-box\" data-inv-id ='" + replyReply.replyStudent.invitationId + "' data-id='" + replyReply.notationReplyReplyId + "'  stu-id='" + replyReply.replyStudent.studentId + "' stu-name='" + replyReply.replyStudent.realName + "'>" +
                            "								<a class=\"student-img\" target='_blank' href = '/student/home/" +
                            replyReply.replyStudent.invitationId +
                            "'><img src='" +
                            replyReply.replyStudent.headIco +
                            "'> </a>" +
                            "" +
                            "								<div class=\"reply-reply-right\">" +
                            "									<div>" +
                            "										<a target='_blank' href = '/student/home/" +
                            replyReply.replyStudent.invitationId +
                            "'>" +
                            replyReply.replyStudent.realName +
                            "</a>" +
                            (studentId == replyReply.toreplyStudent.studentId ? '' : '<span>&nbsp回复:</span><a target="_blank" href = "/student/home/' + replyReply.toreplyStudent.invitationId + '">' + replyReply.toreplyStudent.realName + '</a>') +
                            "" +
                            "									</div>" +
                            "									<div class='reply-reply-right-content'>" +
                            replyReply.replyContent +
                            "</div>" +
                            "									<div class=\"reply-reply-action\">" +
                            "										<a href=\"javascript:;\" class=\"reply-date\">" +
                            getDateIntervalS(replyReply.createTime, res.date) +
                            "</a>" +
                            (student && student.studentId == replyReply.replyStudent.studentId ? '' : '<a class=\"reply-reply-but\">回复 </a>') +
                            "<a>" +
                            (replyReply.myPraiseCount == 0 ? '<span class=\"praise\" data-id=\'' + replyReply.notationReplyReplyId + '\' >赞</span>' : '<span class=\"praise praised\" data-id=\'' + replyReply.notationReplyReplyId + '\'>取消赞</span>') +
                            "										</a> " +
                            (student && student.studentId == replyReply.replyStudent.studentId ? '<a data-id="' + replyReply.notationReplyReplyId + '" class=\"reply-reply-delete\"><span>删除</span></a>' : '') +
                            "<span class='reply-praise-count'>" +
                            (replyReply.praiseCount == 0 ? '' : replyReply.praiseCount + '赞') +
                            "</span>" +
                            "										<div stu-id=\"2011\" class=\"edit-box\" style='display:none'>" +
                            "											<div contenteditable=\"true\" class=\"reply-text\"></div>" +
                            "											<div class=\"reply-but-main\">" +
                            "												<div class=\"reply-but reply-reply-box-submit\">发表</div>" +
                            "												<div class=\"exit-but\">取消</div>" +
                            "											</div>" +
                            "										</div>" +
                            "									</div>" +
                            "								</div>" +
                            "							</div>";
                            replyReplyList.html(html);
                        }
                    },
                    async: true
                });
            }
            $dom.show();
        }
        else {
            $dom.hide();
        }
    })
    
    //删除一级评论事件
    $('#content').delegate('.delete-reply', 'click', function(){
        var boo = confirm('确定要删除吗?');
        if (!boo) {
            return;
        }
        var replyBox = $(this).closest(".reply-box");
        var notationReplyId = replyBox.attr('data-id');
        tshowPost('/product/deleteNotationReply', {
            notationReplyId: notationReplyId
        }, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            replyBox.remove();
            //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
            $("#content .active-p .point-title").data({
                "notationReply": null
            });
            var replyCount = $('.reply-count-num');
            replyCount.text(parseInt(replyCount.text()) - 1);
        });
    });
    
    /*点赞事件*/
    $("#content").delegate(".praise:not(.praised)", "click", function(){
        //console.log("praise");
        var me = this;
        var replyId = $(me).attr("data-id");
        if ($(this).attr('class').indexOf('parent') != -1) {
            tshowAjax({
                url: "/product/savePraiseNotationReply",
                type: "post",
                data: {
                    replyId: replyId
                },
                async: true,
                success: function(res){
                    //console.log(res);
                }
            });
            $(me).text("取消有用")
            $(me).addClass("praised");
            var obj = $(me).parent().siblings(".reply-praise-count");
            if (obj.text().trim() == "") {
                obj.html("1人认为有用")
            }
            else {
                var end = obj.text().trim().indexOf("人");
                var count = parseInt(obj.text().trim().substring(0, end)) + 1;
                obj.html(count + "人认为有用");
            }
        }
        else {
            tshowAjax({
                url: "/product/savePraiseNotationReplyReply",
                type: "post",
                data: {
                    replyReplyId: replyId
                },
                async: true,
                success: function(res){
                    //console.log(res);
                }
            });
            $(me).text("取消赞");
            $(me).addClass("praised");
            var obj = $(me).parent().siblings(".reply-praise-count");
            if (obj.text().trim() == "") {
                obj.html("1赞")
            }
            else {
                var end = obj.text().trim().indexOf("赞");
                var count = parseInt(obj.text().trim().substring(0, end)) + 1;
                obj.html(count + "赞");
            }
        }
        //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
        $("#content .active-p .point-title").data({
            "notationReply": null
        });
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    
    /*取消赞事件*/
    $("#content").delegate(".praised", "click", function(){
        //console.log("praised");
        var me = this;
        var replyId = $(me).attr("data-id");
        if ($(this).attr('class').indexOf('parent') != -1) {
            tshowPost("/product/deletePraiseNotationReply", {
                replyId: replyId
            }, function(res){
                //console.log(res);
            });
            $(me).text("有用")
            $(me).removeClass("praised");
            var obj = $(me).parent().siblings(".reply-praise-count");
            var end = obj.text().trim().indexOf("人");
            var count = parseInt(obj.text().trim().substring(0, end)) - 1;
            if (end == 1) {
                obj.html("")
            }
            else {
                obj.html(count + "人认为有用");
            }
        }
        else {
            tshowPost("/product/deletePraiseNotationReplyReply", {
                replyReplyId: replyId
            }, function(res){
                //console.log(res);
                //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
                $("#content .active-p .point-title").data({
                    "notationReply": null
                });
            });
            $(me).text("赞")
            $(me).removeClass("praised");
            var obj = $(me).parent().siblings(".reply-praise-count");
            var end = obj.text().trim().indexOf("赞");
            var count = parseInt(obj.text().trim().substring(0, end)) - 1;
            if (end == 1) {
                obj.html("")
            }
            else {
                obj.html(count + "人认为有用");
            }
        }
        //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
        $("#content .active-p .point-title").data({
            "notationReply": null
        });
        // $("#content").find(".px").data("boxwData").product = null;
    })
    
    //删除二级评论
    $('#content').delegate('.reply-reply-delete', 'click', function(){
        var boo = confirm('确定要删除吗?');
        if (!boo) {
            return;
        }
        var $me = $(this);
        var notationReplyReplyId = $me.attr('data-id');
        tshowPost('/product/deleteNotationReplyReply', {
            notationReplyReplyId: notationReplyReplyId
        }, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            //评论总数减1
            var replyCountBox = $me.closest('.reply-action').find('.reply-count span:eq(0)');
            replyCountBox.text(parseInt(replyCountBox.text()) - 1);
            $me.closest('.reply-reply-box').remove();
            //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
            $("#content .active-p .point-title").data({
                "notationReply": null
            });
        });
    });
    
    /*采纳和取消采纳*/
    $("#content").delegate(".adopt:not(.adopted)", "click", function(res){
        //console.log(res);
        var me = this;
        var replyId = $(me).attr("data-id");
        tshowAjax({
            url: "/product/saveOrDeleteAdopt",
            type: "post",
            data: {
                id: replyId,
                type: 1,
                saveOrDelete: 's'
            },
            async: true,
            success: function(res){
                //console.log(res);
            }
        });
        $(me).addClass("adopted");
        $(me).text("已采纳");
        $(me).parent().siblings(".isadopt").html("已采纳");
        //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
        $("#content .active-p .point-title").data({
            "notationReply": null
        });
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    $("#content").delegate(".adopted", "click", function(res){
        //console.log(res);
        var me = this;
        var replyId = $(me).attr("data-id");
        tshowPost("/product/saveOrDeleteAdopt", {
            id: replyId,
            type: 1,
            saveOrDelete: 'd'
        }, function(res){
            //console.log(res);
        })
        $(me).removeClass("adopted");
        $(me).text("取消采纳");
        $(me).parent().siblings(".isadopt").html("");
        //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
        $("#content .active-p .point-title").data({
            "notationReply": null
        });
        //清空详作品情页面缓存
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    
    /*点（自己的）的删除事件*/
    $("#content").delegate(".mineP .delete", "click", function(){
        //console.log("delete");
        var me = this;
        var titleReply = $(me).data("titleReply");
        tshowPost("/product/deleteTitleReply", {
            titleReplyId: titleReply.titleReplyId
        }, function(res){
            //console.log(res)
            if (res != "success") {
                alert("已有较多回复的点不允许删除");
                return;
            }
            $(me).parent().parent().parent().remove();
        })
        return false;
    });
    
    /*删除点（一级评论）*/
    $("#content").delegate(".mineP .delete", "click", function(){
        tshowPost("", {}, function(res){
            //console.log(res);
        })
        
    });
    
    /*删除二级评论*/
    $("#content").delegate(".delete_reply_reply", "click", function(){
        var me = this;
        var obj = $(this).parent().parent().parent().prev();
        var replyId = obj.attr("reply_id");
        tshowPost("/product/deleteReplyReply", {
            deleteReplyReply: replyId
        }, function(res){
            //console.log(res);
            if (res == "success") {
                $(me).parents(".reply-wrap").remove();
            }
            else {
                alert("此评论已被采纳，不能删除")
            }
            //目前在进行前台交互后不同步前台缓存，直接清空，下次查询后台
            $("#content .active-p .point-title").data({
                "notationReply": null
            });
        })
    });
    
    /*二级回复框的显示与隐藏*/
    $("#content").delegate(".reply-reply-but", "click", function(){
        var editBox = $(this).parent().children(".edit-box");
        if (editBox.css("display") == "none") {
            editBox.show();
        }
        else {
            editBox.hide();
        }
    });
    
    /*start 对作品的操作*/
    /*对作品的收藏*/
    $("#content").delegate(".px-l-f .icon-collect", "click", function(){
        var $me = $(this);
        var obj = $('.imgMain .px');
        var productId = obj.attr("data-id");
        tshowPost("/product/saveCollectionProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
        })
        $me.removeClass('icon-collect').addClass('icon-collected');
        $me.next().text(parseInt($me.next().text()) + 1);
        $me.attr({
            title: '取消收藏'
        });
        //清空前台缓存
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    /*对作品取消收藏*/
    $("#content").delegate(".px-l-f .icon-collected", "click", function(){
        var $me = $(this);
        var obj = $('.imgMain .px');
        var productId = obj.attr("data-id");
        tshowPost("/product/deleteCollectionProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
        });
        $me.attr({
            title: '收藏'
        });
        $me.removeClass('icon-collected').addClass('icon-collect');
        $me.next().text(parseInt($me.next().text()) - 1);
        //清空前台缓存
        //  $("#content").find(".px").data("boxwData").product = null;
    });
    
    /*对作品的喜爱*/
    $("#content").delegate(".px-l-f .icon-love", "click", function(){
        var $me = $(this);
        var obj = $('.imgMain .px');
        var productId = obj.attr("data-id");
        tshowAjax({
            url: "/product/savePraiseProduct",
            type: "post",
            data: {
                productId: productId
            },
            async: true,
            success: function(res){
                //console.log(res);
            }
        });
        $me.attr({
            title: '取消喜爱'
        });
        $me.removeClass('icon-love').addClass('icon-loved');
        $me.next().text(parseInt($me.next().text()) + 1);
        //清空前台缓存
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    /*对作品取消喜爱*/
    $("#content").delegate(".px-l-f .icon-loved", "click", function(){
        var $me = $(this);
        var obj = $('.imgMain .px');
        var productId = obj.attr("data-id");
        tshowPost("/product/deletePraiseProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
        });
        $me.removeClass('icon-loved').addClass('icon-love');
        $me.next().text(parseInt($me.next().text()) - 1);
        $me.attr({
            title: '喜爱'
        });
        //清空前台缓存
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    /*对作品的投票*/
    $("#content").delegate(".px-l-f .icon-vote", "click", function(){
        var $me = $(this);
        var obj = $('.imgMain .px');
        var productId = obj.attr("data-id");
        tshowPost("/product/saveVoteProduct", {
            productId: productId
        }, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            $me.removeClass('icon-vote').addClass('icon-voted');
            $me.next().text(parseInt($me.next().text()) + 1);
        });
        //清空前台缓存
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    /*对作品的删除*/
    $("#content").delegate(".px-l-f .icon-delete", "click", function(){
        var $me = $(this);
        var box = $('.imgMain .px');
        var productId = box.attr("data-id");
        var verCou = parseInt(box.attr("data-vercou"));
        var ver = parseInt(box.attr("data-ver"));
        if (verCou == 1) {
            var boo = confirm("您确定要删除此作品吗?");
            if (!boo) {
                return;
            }
        }
        else {
            if (ver < verCou) {
                var boo = confirm("删除历史版本的作品会连同此版本之后的所有作品一并删除！您确定要删除吗?");
                if (!boo) {
                    return;
                }
            }
            else {
                var boo = confirm("删除最新版本的作品后您的上一历史版本作品会被置为最新版！您确定要删除吗?");
                if (!boo) {
                    return;
                }
            }
        }
        
        tshowPost("/product/deleteProduct", {
            productId: productId
        }, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            var product = res.bean;
            if (product == null) {
                $('body').css({
                    'overflow': 'auto'
                });
                $('#content').hide();
                return;
            }
            product.collections = [];
            product.praises = [];
            product.reports = [];
            product.votes = [];
            var html = createProductInfoHtml(product);
            $("#content").show().find(".imgMain").html($(html));
            showPoint(product.productId);
        });
        $me.removeClass('icon-vote').addClass('icon-voted');
        $me.next().text(parseInt($me.next().text()) + 1);
        //清空前台缓存
        // $("#content").find(".px").data("boxwData").product = null;
    });
    
    /*筛选点*/
    $("#content").delegate(".px-l-f .icon-filter-point", "click", function(){
        //console.log("筛选");
        var points = $("#content .mineP,#content .box");
        for (var i = 0; i < points.length; i++) {
            var point = points.eq(i);
            var titleReply = point.data("titleReply")
            var stuCount = titleReply.hotParams.stuCount;
            if (stuCount < 2) {//参入讨论的人数少于2人时则隐藏
                point.hide();
            }
        }
        $(this).addClass('icon-filter-pointed').removeClass('icon-filter-point');
        //去掉互斥的操作
        $('.icon-remove-pointed').removeClass('icon-remove-pointed').addClass('icon-remove-point');
        $('.icon-show-pointed').removeClass('icon-show-pointed').addClass('icon-show-point');
    });
    /*全部显示点*/
    $("#content").delegate(".px-l-f .icon-show-point", "click", function(){
        $("#content .mineP,#content .box").show();
        //去掉互斥的操作
        $('.icon-filter-pointed').removeClass('icon-filter-pointed').addClass('icon-filter-point');
        $('.icon-remove-pointed').removeClass('icon-remove-pointed').addClass('icon-remove-point');
        $(this).addClass('icon-show-pointed').removeClass('icon-show-point');
    });
    
    /*全部隐藏点*/
    $("#content").delegate(".px-l-f .icon-remove-point", "click", function(){
        $("#content .mineP,#content .box").hide();
        //去掉互斥的操作
        $('.icon-filter-pointed').removeClass('icon-filter-pointed').addClass('icon-filter-point');
        $('.icon-show-pointed').removeClass('icon-show-pointed').addClass('icon-show-point');
        $(this).addClass('icon-remove-pointed').removeClass('icon-remove-point');
    });
    /*关注*/
    $("#content").delegate(".attention:not(.attentioned)", "click", function(){
        //console.log("attention");
        var toStudentId = $("#student").attr("data-id");
        tshowPost("/student/saveAttention", {
            toStudentId: toStudentId
        }, function(res){
            //console.log(res);
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            $(this).text("已关注");
            $(this).addClass("attentioned");
            $("#content").find(".px").data("boxwData").product = null
        })
    });
    
    /*取消关注*/
    $("#content").delegate(".attentioned", "click", function(){
        //console.log("attention");
        var toStudentId = $("#student").attr("data-id");
        tshowPost("/student/deleteAttention", {
            toStudentId: toStudentId
        }, function(res){
            //console.log(res);
        })
        $(this).text("关注");
        $(this).removeClass("attentioned");
        $("#content").find(".px").data("boxwData").product = null
    });
    
    //瀑布流作品点赞事件
    $(document).delegate(".boxw-p .love:not(.loved)", "click", function(){
        var $me = $(this);
        var productId = $me.attr("data-id");
        tshowPost("/product/savePraiseProduct", {
            productId: productId
        }, function(res){
            if (res != 'success') {
                return;
            }
            //console.log(res);
            $me.addClass('icon-love-pbled').removeClass('icon-love-pbl').addClass('loved');
            $me.next().text(parseInt($me.next().text()) + 1);
        })
    });
    $(document).delegate(".boxw-p .loved", "click", function(){
        var $me = $(this);
        var productId = $me.attr("data-id");
        tshowPost("/product/deletePraiseProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
            if (res != 'success') {
                return;
            }
            $me.addClass('icon-love-pbl').removeClass('icon-love-pbled').removeClass('loved');
            $me.next().text(parseInt($me.next().text()) - 1);
        })
    });
    
    //添加通用反馈功能
    
    /*start 反馈功能*/
    $('.feedback-nav').on('click', function(){
        $('#feedback').show();
    })
    
    $('.feedback-sub').on('click', function(){
        var title = $('#feedback .feedback-header-title').val().trim();
        var content = $('#feedback .feedback-header-content').text().trim();
        if (title == '' || content == '') {
            createPromptBox("说点什么在提交吧！");
            return;
        }
        tshowPost('/user/saveFeedback', {
            title: title,
            content: content
        }, function(res){
            createPromptBox("谢谢您的反馈！");
            $('#feedback').hide();
        })
    });
    
    $('.feedback-cancel,#feedback .tshow-zzc').on('click', function(){
        $('#feedback').hide();
    });
    /*end   反馈功能*/
}


/*发布新版本*/
function uploadProductNewVersion(){
    var productName = $("#content .px-l-h-header").text();
    var title = $("#content .px-l-h-title").text();
    var point = $("#content .px-l-f-t").html();
    var original = $("#content .pro-original").text();
    var html = "<div class=\"px-1\"> " +
    "	<div class=\"px-l-h\"> <span class=\"px-l-h-title\">" +
    title +
    "</span>" +
    "		<span class=\"px-l-h-header\">" +
    productName +
    "<span class=\"pro-original\">" +
    original +
    "</span></span>" +
    "		<span class=\"px-l-f-t\"> " +
    point +
    " " +
    "		</span> " +
    "</span> " +
    "	</div> " +
    "	<div class=\"px-1-img\"> " +
    "<div class=\"zs-r-wp-b\">" +
    "	<div class=\"zs-r-wp-z-sc\">" +
    "		<span></span>" +
    "	</div>" +
    "	<table class=\"zs-r-wp-b-1\">" +
    "		<tbody>" +
    "			<tr>" +
    "				<td class=\"zs-r-wp-b-td\"></td>" +
    "			</tr>" +
    "		</tbody>" +
    "	</table>" +
    "</div>" +
    "</div>" +
    "<div class=\"zs-r-wt\"> " +
    "	<div class=\"zs-r-wt-h\"> " +
    "		<span>text</span> " +
    "	</div> " +
    "	<div class=\"zs-r-wt-b\"> " +
    "		<textarea class=\"sjsm\"  placeholder=\"添加设计说明\"></textarea> " +
    "<div class='zs-r-wt-b-box'>" +
    "						<div class='zs-r-wt-b-p'>" +
    "							<span class='zs-r-wt-b-num'>0</span><span>/</span><span" +
    "								class='zs-r-wt-b-count'>350</span>" +
    "						</div>" +
    "					</div>" +
    "	</div> " +
    "</div>" +
    "<div><button onclick = 'saveProductVersion()' class='sub-new-v-product'>发布</button></div>" +
    "<input class=\"tshow-hide\" type=\"file\" id=\"imgfile2\" name=\"imgfile2\" onchange=\"uploadFile('imgfile2','#up-new-version .zs-r-wp-b-td')\">";
    $('#up-new-version').find(".px-1").remove().end().append(html).show();
}

/*保存新版本作品*/
function saveProductVersion(){
    var productId = $("#productInfo").attr("data-id");
    var productSeriesId = $('#productInfo').attr('data-seriesid');
    var content = $("#up-new-version .zs-r-wp-b-td img");
    var productDisc = $("#up-new-version .sjsm").val();
    var src = content.attr("src")
    if (!src) {
        $("#up-new-version .zs-r-wp-b").addClass("tshow-col-red");
        return;
    }
    var naturalImg = getNaturalWidth(content);
    var naturalWidth = naturalImg.naturalWidth;
    var naturalHeight = naturalImg.naturalHeight;
    var obj = {
        productId: productId,
        content: src,
        productDisc: productDisc,
        width: naturalWidth,
        height: naturalHeight,
        productSeriesId: productSeriesId
    }
    tshowPost("/product/updateProduct", obj, function(res){
        //console.log(res);
        if (res.success) {
            createPromptBox("新版本发布成");
            var bean = res.bean;
            bean.productStudents = [];
            bean.collections = [];
            bean.collections = [];
            bean.praises = [];
            bean.votes = [];
            var html = createProductInfoHtml(res.bean);
            $("#content").show().find(".imgMain").html($(html));
            $("#content").attr({
                'data-pid': bean.label.pid
            });
            $("#up-new-version").hide();
        }
    });
}

/*start 常用工具类*/

/*隐藏所有点*/
function hideAllPoints(){
    $("#content .mineP,#content .box").hide();
}


/*显示所有点*/
function showAllPoints(){
    $("#content .mineP,#content .box").show();
}

/* end   作品详情*/

/*start  消息*/
/*消息弹出框处理*/
function initMessage(){
    $('#message-controller').prev().on('click', function(){
        $(this).hide().next().hide();
        scrollBody();
    });
    
    tshowPost("/student/findMessage/count", {}, function(res){
        ////console.log(res);
        if (res.messageCount != 0) {
            $("#message-but .message-but-count").text(res.messageCount).show();
        }
        if (res.productCount != 0) {
            $("#message-controller .news .message-a-count").text(res.productCount).show();
        }
        if (res.replyCount != 0) {
            $("#message-controller .you .message-a-count").text(res.replyCount).show();
        }
        if (res.flowerCount != 0) {
            $("#message-controller .flowers .message-a-count").text(res.flowerCount).show();
        }
        if (res.studentStudentCount != 0) {
            $("#message-controller .person .message-a-count").text(res.studentStudentCount).show();
        }
        
    });
    
    //绑定消息事件
    (function(){
        $("#message-but").click(function(){
            $("#message-controller").show().prev().show();
            $("body").css({
                "overflow": "hidden"
            })
            $('body').data({
                'scrollTop': $('body').scrollTop()
            });
            //默认加载news消息
            $("#message-controller .px-h a:eq(0)").click();
        });
    })();
    
    //绑定消息切换事件
    (function(){
        $("#message-controller .px-h a").on("click", function(){
            var index = $(this).index();
            var me = this;
            $(this).addClass("mes-active").siblings().removeClass("mes-active");
            if (index == 0) {
                if (!$(this).data("message")) {
                    //默认加载news列表
                    var page = {
                        pageNo: 1,
                        pageSize: 10
                    }
                    $tsBlockUI({
                        message: "<img src='/img/123321.gif'>",
                        css: {
                            width: '50px',
                            height: '50px',
                            top: '47%',
                            left: '70%',
                            'border-radius': '50%',
                            'background': 'none',
                            border: 'none'
                        
                        },
                        showOverlay: false
                    });
                    tshowPost("/student/findMessage/product/list", page, function(res){
                        $.unblockUI();
                        var html = makeNewsMesList(res);
                        $("#message-controller .message-news .px-f").html(html).append($("<div class=\"more\">查看更多</div>").data({
                            page: page
                        }));
                        $(".message-news").show().siblings().filter(":not(.px-h)").hide();
                        $(me).data({
                            "message": res
                        });
                    });
                }
                else {
                    $(".message-news").show().siblings().filter(":not(.px-h)").hide();
                }
            }
            else 
                if (index == 1) {
                    if (!$(this).data("message")) {
                        //默认加载you列表
                        var page = {
                            pageNo: 1,
                            pageSize: 10
                        }
                        $tsBlockUI({
                            message: "<img src='/img/123321.gif'>",
                            css: {
                                width: '50px',
                                height: '50px',
                                top: '47%',
                                left: '70%',
                                'border-radius': '50%',
                                'background': 'none',
                                border: 'none'
                            
                            },
                            showOverlay: false
                        });
                        tshowPost("/student/findMessage/reply/list", page, function(res){
                            $.unblockUI();
                            var $html = makeNewsMesList(res);
                            $("#message-controller .message-you .px-f").html($html).append($("<div class=\"more\">查看更多</div>").data({
                                page: page
                            }));
                            $(".message-you").show().siblings().filter(":not(.px-h)").hide();
                            $(me).data({
                                "message": res
                            });
                        });
                    }
                    else {
                        $(".message-you").show().siblings().filter(":not(.px-h)").hide();
                    }
                }
                else 
                    if (index == 2) {
                        var page = {
                            pageNo: 1,
                            pageSize: 10
                        }
                        $(".message-student").show().siblings().filter(":not(.px-h)").hide();
                        if (!$(this).data("message")) {
                            $tsBlockUI({
                                message: "<img src='/img/123321.gif'>",
                                css: {
                                    width: '50px',
                                    height: '50px',
                                    top: '47%',
                                    left: '70%',
                                    'border-radius': '50%',
                                    'background': 'none',
                                    border: 'none'
                                
                                },
                                showOverlay: false
                            });
                            tshowPost("/student/findMessage/student/list", page, function(res){
                                $.unblockUI();
                                //console.log(res);
                                var $html = makeStudentMesList(res);
                                $("#message-controller .message-student .px-f").html($html).append($("<div class=\"more\">查看更多</div>").data({
                                    page: page
                                }));
                                $(".message-student").show().siblings().filter(":not(.px-h)").hide();
                                $(me).data({
                                    "message": res
                                });
                            })
                        }
                        
                    }
                    else 
                        if (index == 3) {
                            var page = {
                                pageNo: 1,
                                pageSize: 10
                            }
                            $(".message-student").show().siblings().filter(":not(.px-h)").hide();
                            if (!$(this).data("message")) {
                                $tsBlockUI({
                                    message: "<img src='/img/123321.gif'>",
                                    css: {
                                        width: '50px',
                                        height: '50px',
                                        top: '47%',
                                        left: '70%',
                                        'border-radius': '50%',
                                        'background': 'none',
                                        border: 'none'
                                    
                                    },
                                    showOverlay: false
                                });
                                tshowPost("/student/findMessage/flower/list", page, function(res){
                                    $.unblockUI();
                                    //console.log(res);
                                    var $html = makeFlowersMesList(res);
                                    $("#message-controller .message-flowers .px-f").html($html).append($("<div class=\"more\">查看更多</div>").data({
                                        page: page
                                    }));
                                    $(".message-flowers").show().siblings().filter(":not(.px-h)").hide();
                                    $(me).data({
                                        "message": res
                                    });
                                })
                            }
                        }
                        else 
                            if (index == 4) {
                                $(".message-sys").show().siblings().filter(":not(.px-h)").hide();
                            }
            
        })
    })();
    
    //为消息左边的作品绑定事件
    $("#message-controller").delegate(".img-controller img", "click", function(){
        openProductInfo("message", $(this));
    });
    
    //为news消息绑定事件信息
    $("#message-controller").delegate(".message-news .px-f ul li", "click", function(){
        var message = $(this).data("message");
        if (message) {
            var $dom = $("#message-controller .new-info");
            var obj = {
                productId: message.productId,
                type: 'reply'
            }
            tshowPost("/student/findMessage/product/info", obj, function(res){
                makeNewsMessInfo(res, message);
                $("#message-controller .new-info .pxn-b a:eq(0)").trigger("click")
                $("#message-controller .img-controller img").attr({
                    "src": message.content,
                    "data-id": message.productId
                });
                $dom.show().siblings(":not(.tshow-zzc,.img-div)").hide();
                initHeight();
            });
        }
    });
    
    //为you消息绑定事件信息
    $("#message-controller").delegate(".message-you .px-f ul li", "click", function(){
        var message = $(this).data("message");
        if (message) {
            var $dom = $("#message-controller .you-info");
            var obj = {
                productId: message.productId,
                type: 'reply'
            }
            tshowPost("/student/findMessage/reply/info", obj, function(res){
                makeYouMessInfo(res, message);
                $("#message-controller .you-info .pxn-b a:eq(0)").trigger("click");
                $("#message-controller .img-controller img").attr({
                    "src": message.content,
                    "data-id": message.productId
                });
                $dom.show().siblings(":not(.tshow-zzc,.img-div)").hide();
                initHeight();
            });
        }
    });
    
    $("#message-controller").delegate(".px-f ul li", "mouseover", function(){
        var message = $(this).data("message");
        if (message) {
            $("#message-controller .img-controller img").attr({
                "src": message.smallContent,
                "data-id": message.productId
            });
        }
    })
    
    //为you消息类别绑定事件
    $("#message-controller").delegate(".you-info .pxn-b a", "click", function(){
        var me = this;
        var index = $(this).index();
        var $dom = $("#message-controller .you-info .pxn-f .pxn-f-c");
        var productId = $("#pxn-h-title").attr("data-id");
        if (index == 1) {
            if (!$(this).data("message")) {
                tshowPost("/student/findMessage/reply/info", {
                    productId: productId,
                    type: 'adopt'
                }, function(res){
                    makeYouAdopt(res, $dom.eq(index).find("ul"));
                    $(me).data({
                        message: res
                    });
                });
            }
            
        }
        else 
            if (index == 2) {
                if (!$(this).data("message")) {
                    tshowPost("/student/findMessage/reply/info", {
                        productId: productId,
                        type: 'useful'
                    }, function(res){
                        makeYouUseful(res, $dom.eq(index).find("ul"));
                        $(me).data({
                            message: res
                        });
                    });
                }
                
            }
            else 
                if (index == 3) {
                    if (!$(this).data("message")) {
                        tshowPost("/student/findMessage/reply/info", {
                            productId: productId,
                            type: 'praise'
                        }, function(res){
                            makeYouPraise(res, $dom.eq(index).find("ul"));
                            $(me).data({
                                message: res
                            });
                        });
                    }
                }
        $dom.eq(index).show().siblings().hide();
    });
    
    //为news消息类别绑定事件
    $("#message-controller").delegate(".new-info .pxn-b a", "click", function(){
        var me = this;
        var index = $(this).index();
        var $dom = $("#message-controller .new-info .pxn-f .pxn-f-c");
        var productId = $("#pxn-h-title").attr("data-id");
        if (index == 0) {
        
        }
        else 
            if (index == 1) {
                if (!$(this).data("message")) {
                    tshowPost("/student/findMessage/product/info", {
                        productId: productId,
                        type: 'collect'
                    }, function(res){
                        makeMeCollect(res, $dom.eq(index).find("ul"));
                        $(me).data({
                            message: res
                        });
                    });
                }
            }
            else 
                if (index == 2) {
                    if (!$(this).data("message")) {
                        tshowPost("/student/findMessage/product/info", {
                            productId: productId,
                            type: 'praise'
                        }, function(res){
                            makeMePraise(res, $dom.eq(index).find("ul"));
                            $(me).data({
                                message: res
                            });
                        });
                    }
                }
                else 
                    if (index == 3) {
                        if (!$(this).data("message")) {
                            tshowPost("/student/findMessage/product/info", {
                                productId: productId,
                                type: 'vote'
                            }, function(res){
                                makeMeVote(res, $dom.eq(index).find("ul"));
                                $(me).data({
                                    message: res
                                });
                            });
                        }
                    }
        $dom.eq(index).show().siblings().hide();
        
    });
    
    
    //返回消息主页面事件
    $("#message-controller").delegate(".pxn-h-l", "click", function(){
        $(this).parent().parent().parent().hide();
        $("#message-controller .px2").show();
        //情况详情页面信息
        $("#message-controller .pxn .pxn-f .pxn-f-c ul").empty();
        var imgController = $("#message-controller .img-controller");
        imgController.find(".mineP,.box").remove();
    });
    
    //为消息详情列表绑定mouseover事件
    $("#message-controller").delegate(".pxn-f-c li", "mouseover", function(){
        var message = $(this).data("message");
        showPointByMes(message);
    });
    
    //绑定you'查看更多'事件
    $("#message-controller").delegate(".message-you .px-f .more", "click", function(){
        findMoreYouMesList(this);
    });
    
    //绑定news'查看更多'事件
    $("#message-controller").delegate(".message-news .px-f .more", "click", function(){
        findMoreNewsMesList(this);
    });
    //绑定student'查看更多'事件
    $("#message-controller").delegate(".message-student .px-f .more", "click", function(){
        findMoreStudentMesList(this);
    });
    //绑定flowers'查看更多'事件
    $("#message-controller").delegate(".message-flowers .px-f .more", "click", function(){
        findMoreFlowersMesList(this);
    });
    
    //为you消息回复按钮绑定切换隐藏显示编辑框
    $("#message-controller").delegate(".mes-r-reply-but.reply", "click", function(){
        var edit = $(this).parent().next();
        if (edit.css("display") == "none") {
            edit.show();
        }
        else {
            edit.hide();
        }
    });
    
    //消息采纳事件
    $("#message-controller").delegate(".mes-r-reply-but.adopt:not(.adopted)", "click", function(){
        var $me = $(this);
        var id = $me.attr("data-id");
        var obj = {
            id: id,
            type: '1',
            saveOrDelete: 's'
        }
        tshowPost("/product/saveOrDeleteAdopt", obj, function(res){
            ////console.log(res);
        });
        $me.addClass("adopted");
        $me.text("已采纳");
    });
    
    //绑定回复的提交和取消事件
    $("#message-controller").delegate(".mes-reply-info-t .mes-r-submit", "click", function(){
        var mesrr = $(this).closest('.mes-reply-info-t');
        var notationReplyReplyId = mesrr.attr("data-id");
        var toStudentId = mesrr.attr("rr-stu-id") || mesrr.attr("stu-id");
        var notationReplyId = mesrr.children(".mes-r:not(.mes-rr)").attr("data-id");
        var edit = $(this).parent().prev();
        var replyContent = edit.text();
        var nrrRealName = mesrr.attr("rr-stu-name") || mesrr.attr("stu-name");
        edit.empty();
        edit.parent().hide();
        if (!$(this).closest('.mes-r').hasClass("mes-rr")) {
            var obj = {
                "toreplyStudent.studentId": toStudentId,
                notationReplyId: notationReplyId,
                replyContent: replyContent
            }
        }
        else {
            var obj = {
                "toreplyStudent.studentId": toStudentId,
                notationReplyId: notationReplyId,
                replyContent: replyContent,
                toNotationReplyReplyId: notationReplyReplyId
            }
        }
        
        tshowPost("/product/saveNotationReplyReply", obj, function(res){
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            var info = res.bean;
            ////console.log(res);
            var html = "<div class='mes-r mes-rr' data-id='" + info.notationReplyReplyId + "' rr-stu-id='" + info.replyStudent.studentId + "' rr-to-stu-id='" + obj.toStudentId + "'>" +
            "<a target = '_blank' href = '/student/home/" +
            info.replyStudent.invitationId +
            "'>" +
            (info.replyStudent.headIco == null || info.replyStudent.headIco == '' ? '<span>' + info.replyStudent.realName.substring(info.replyStudent.realName.length - 1) + '</span>' : '<img src="' + info.replyStudent.headIco + '">') +
            "</a><div><a data-id='" +
            info.replyStudent.studentId +
            "'>" +
            info.replyStudent.realName +
            "</a>回复<a data-id='" +
            obj.toStudentId +
            "'>" +
            nrrRealName +
            "</a>:" +
            obj.replyContent +
            "</div><div><span>" +
            getDateInterval() +
            "</span></div></div> " +
            "						</div>";
            mesrr.append($(html));
        });
    });
    $("#message-controller").delegate(".mes-reply-info-t .mes-r-quit", "click", function(){
        $(this).parent().prev().empty().parent().hide();
    });
    
    //同意或者拒绝学生加老师请求
    $("#message-controller").delegate(".message-student-y-stu", "click", function(){
        var $me = $(this);
        var css = {
            left: $me.offset().left,
            top: $me.offset().top
        }
        var center = "<div>将<span class='xsgl-name' data-id = '" + $me.prev().find("a").attr("data-id") + "'>" + $me.prev().find("a").text() + "</span>归类于</div>" +
        "<div><label><span>未报名</span><input type='radio' name = 'xsgl' class='xsgl' value='Y'></label></div>" +
        "<div><label><span>已报名</span><input type='radio' name = 'xsgl' class='xsgl' value='YBM'></label></div>" +
        "<div><label><span>已毕业</span><input type='radio' name = 'xsgl' class='xsgl' value='YBY'></label></div>";
        var obj = {
            header: '学生归类',
            center: center
        }
        createTck(obj, css, function(me){
            var $me = $(me).parents(".tshow-tck");
            var studentId = $me.find(".xsgl-name").attr("data-id");
            var type = $me.find(".xsgl:checked").val();
            var typeVal = $me.find(".xsgl:checked").prev().text();
            tshowPost("/student/dealStudentApply", {
                studentId: studentId,
                type: type
            }, function(res){
                //console.log(res);
                $('.active-but').next().remove();
                $('.active-but').prev().after("<span class=\"message-student-state\">已归类至" + typeVal + "学生</span>");
                $('.active-but').remove();
            });
        });
        $me.addClass("active-but");
    });
    
    $("#message-controller").delegate(".message-student-n-stu", "click", function(){
        var $me = $(this);
        $me.addClass("active-but");
        var studentId = $me.prev().prev().find("a").attr("data-id");
        var realName = $me.prev().prev().find("a").text();
        var r = confirm("你确认要拒绝" + realName + "的请求吗");
        if (!r) {
            return;
        }
        tshowPost("/student/dealStudentApply", {
            studentId: studentId,
            type: "N"
        }, function(res){
            //console.log(res);
            $('.active-but').prev().remove();
            $('.active-but').prev().after("<span class=\"message-student-state\">已拒绝</span>");
            $('.active-but').remove();
        });
    })
    
    $("#message-controller").delegate(".message-student-n-stu", "click", function(){
    
    });
    
    //弹出框取消确认事件
    $(document).delegate(".tck-qx", "click", function(){
        $(this).parents('.tshow-tck').remove();
    });
    $(document).delegate(".tck-qr", "click", function(){
        var box = $(this).parents('.tshow-tck');
        box.data("qrClick")(this);
        box.remove();
    });
    
    //个人设置相关事件
    $("#set-up-quit").on("click", function(){
        tshowPost("/user/destory", {}, function(){
            $("<a href ='/user/toLogin/#login' ></a>").get(0).click();
        });
    });
}

//封装news消息详情页面
function makeNewsMesList(res){
    var $htm = $("<ul></ul>");
    for (var i = 0; i < res.bean.length; i++) {
        var obj = res.bean[i];
        var html = "<li><span class=\"px-f-n\">" + obj.score + "</span> <span class=\"px-f-title\"><a data-id='" + obj.studentId + "'>" + obj.realName + "</a><a data-id='" + obj.labelId + "'>" + obj.labelName + "</a>·<span class='tshow-rel'>" + (obj.newCount == 0 ? '' : '<span class="message-but-count-l">' + obj.newCount) + "</span>" + obj.productName + "</span></span>" +
        "							<span class=\"px-f-s\">收藏(" +
        obj.collectionCount +
        ")</span> <span class=\"px-f-s\">喜爱(" +
        obj.praiseCount +
        ")</span> " +
        "							<span class=\"px-f-s\">投票(" +
        obj.voteCount +
        ")</span> <span class=\"px-f-s\">评论(" +
        obj.replyCount +
        ")</span><span" +
        "							class=\"px-f-time\">" +
        getDateIntervalS(obj.createTime, res.date) +
        "</span></li>";
        var $html = $(html).data({
            message: obj
        });
        ;
        $htm.append($html);
    }
    return $htm;
}

//封装students消息详情页面
function makeStudentMesList(res){
    var $htm = $("<ul></ul>");
    var studentId = $(document).data("student").studentId;
    for (var i = 0; i < res.bean.length; i++) {
        var obj = res.bean[i];
        if (studentId == obj.toStudentId) {
            var html = "<li><span class='message-student-left-info'><a target='_blank' href='/student/home/" + obj.invitationId + "' data-id='" + obj.studentId + "'>" + obj.realName + "</a>" +
            "请求加你为老师</span>" +
            (obj.state == 'N' ? '<span class="message-student-state">已拒绝</span>' : obj.state == 'Y' ? '<span class="message-student-state">已归类至未报名学生</span>' : obj.state == 'YBM' ? '<span class="message-student-state">已归类至已报名学生</span>' : obj.state == 'YBY' ? '<span class="message-student-state">已归类至已毕业学生</span>' : '<button class=\'message-student-y-stu\'>同意</button><button class=\'message-student-n-stu\'>拒绝</button>') +
            "</span></li>";
            var $html = $(html).data({
                message: obj
            });
            $htm.append($html);
        }
        else {
            var html = "<li><span class='message-student-left-info'><a target='_blank' href='/student/home/" + obj.toInvitationId + "' data-id='" + obj.toStudentId + "'>" + obj.toRealName + "</a>" +
            (obj.state == 'N' ? '拒绝' : '同意') +
            "了你的师生请求</span>" +
            "</span></li>";
            var $html = $(html).data({
                message: obj
            });
            $htm.append($html);
        }
        
    }
    return $htm;
}

//封装flowers消息详情页面
function makeFlowersMesList(res){
    var $htm = $("<ul></ul>");
    var studentId = $(document).data("student").studentId;
    for (var i = 0; i < res.bean.length; i++) {
        var obj = res.bean[i];
        var html = "<li><span class='message-student-left-info'><a target='_blank' href='/student/home/" + obj.invitationId + "' data-id='" + obj.studentId + "'>" + obj.realName + "</a>" +
        "关注了你</span>" +
        "</span></li>";
        $htm.append(html);
    }
    return $htm;
}

//you消息列表分页事件
function findMoreYouMesList(dom){
    var $dom = $(dom);
    var page = $dom.data("page");
    page.pageNo++;
    $tsBlockUI({
        message: "<img src='/img/123321.gif'>",
        css: {
            width: '50px',
            height: '50px',
            top: '50%',
            left: '70%',
            'margin-left': '-20px',
            'border-radius': '50%',
            'background': 'none',
            border: 'none'
        
        },
        showOverlay: false
    });
    tshowPost("/student/findMessage/reply/list", page, function(res){
        $.unblockUI();
        if (res.bean.length == 0) {
            $("#message-controller .message-you .px-f .more").removeClass("more").text("无更多数据").addClass('no-more');
            return;
        }
        var $html = makeNewsMesList(res, page);
        $("#message-controller .message-you .px-f .more").before($html);
        $(".message-you").show().siblings().filter(":not(.px-h)").hide();
    });
}

//news消息列表分页事件
function findMoreNewsMesList(dom){
    var $dom = $(dom);
    var page = $dom.data("page");
    page.pageNo++;
    $tsBlockUI({
        message: "<img src='/img/123321.gif'>",
        css: {
            width: '50px',
            height: '50px',
            top: '50%',
            left: '70%',
            'margin-left': '-20px',
            'border-radius': '50%',
            'background': 'none',
            border: 'none'
        
        },
        showOverlay: false
    });
    tshowPost("/student/findMessage/product/list", page, function(res){
        $.unblockUI();
        if (res.bean.length == 0) {
            $("#message-controller .message-news .px-f .more").removeClass("more").text("无更多数据").addClass('no-more');
            return;
        }
        var $html = makeNewsMesList(res, page);
        $("#message-controller .message-news .px-f .more").before($html);
        $(".message-news").show().siblings().filter(":not(.px-h)").hide();
    });
}

//students消息列表分页事件
function findMoreStudentMesList(dom){
    var $dom = $(dom);
    var page = $dom.data("page");
    page.pageNo++;
    $tsBlockUI({
        message: "<img src='/img/123321.gif'>",
        css: {
            width: '50px',
            height: '50px',
            top: '50%',
            left: '70%',
            'margin-left': '-20px',
            'border-radius': '50%',
            'background': 'none',
            border: 'none'
        
        },
        showOverlay: false
    });
    tshowPost("/student/findMessage/student/list", page, function(res){
        $.unblockUI();
        if (res.bean.length == 0) {
            $("#message-controller .message-student .px-f .more").removeClass("more").text("无更多数据").addClass('no-more');
            return;
        }
        var $html = makeStudentMesList(res, page);
        $("#message-controller .message-student .px-f .more").before($html);
        $(".message-student").show().siblings().filter(":not(.px-h)").hide();
    });
}

//flowers消息列表分页事件
function findMoreFlowersMesList(dom){
    var $dom = $(dom);
    var page = $dom.data("page");
    page.pageNo++;
    $tsBlockUI({
        message: "<img src='/img/123321.gif'>",
        css: {
            width: '50px',
            height: '50px',
            top: '50%',
            left: '70%',
            'margin-left': '-20px',
            'border-radius': '50%',
            'background': 'none',
            border: 'none'
        
        },
        showOverlay: false
    });
    tshowPost("/student/findMessage/flower/list", page, function(res){
        $.unblockUI();
        if (res.bean.length == 0) {
            $("#message-controller .message-flowers .px-f .more").removeClass("more").text("无更多数据").addClass('no-more');
            return;
        }
        var $html = makeFlowersMesList(res, page);
        $("#message-controller .message-student .px-f .more").before($html);
        $(".message-student").show().siblings().filter(":not(.px-h)").hide();
    });
}


function makeNewMesInfo(){

}

//封装you消息详情页面（默认加载reply）
function makeYouMessInfo(res, message){
    var obj = $("#message-controller .you-info");
    var pxnhhtml = "				<a > <span class=\"pxn-h-l\"><</span>< </a> <span " +
    "				id='pxn-h-title' data-stu-id='" +
    message.studentId +
    "'data-stu-name='" +
    message.realName +
    "'	class=\"pxn-h-title color\" data-id='" +
    message.productId +
    "'>" +
    message.labelName +
    "·" +
    message.productName +
    "</span> <span " +
    "					class=\"pxn-h-time color\">" +
    getDateInterval(message.createTime) +
    "</span> ";
    obj.find(".pxn-h").html(pxnhhtml);
    var pxnbhtml = "				<a >回复" + (message.newReplyCount == 0 ? '' : '<span class="message-but-count-r">' + message.newReplyCount) +
    "</span></a> <a >采纳" +
    (message.newAdoptCount == 0 ? '' : '<span class="message-but-count-r">' + message.newAdoptCount) +
    "</span></a> <a >有用" +
    (message.newUsefulCount == 0 ? '' : '<span class="message-but-count-r">' + message.newUsefulCount) +
    "</span></a> " +
    "				<a >赞" +
    (message.newPraiseCount == 0 ? '' : '<span class="message-but-count-r">' + message.newPraiseCount) +
    "</span></a> ";
    obj.find(".pxn-b").html(pxnbhtml);
    "			<div class=\"pxn-f\"> " +
    "				<div class=\"pxn-f-c\"> " +
    "					<ul> ";
    var pxnfc1ulhtml = obj.find(".pxn-f .pxn-f-c:eq(0) ul");
    pxnfc1ulhtml.empty();
    if (res.length == 0) {
        pxnfc1ulhtml.append("<div style='text-align:center'>暂时没有人回复你</div>")
    }
    for (var i = 0; i < res.length; i++) {
        var info = res[i];
        var html = "<li> " +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<a class=\"mes-user-pho\" target = '_blank' href='/student/home/" +
        info.nrrInvitationId +
        "'>" +
        (info.nrrHeadIco == null || info.nrrHeadIco == '' ? '<span>' + info.nrrRealName.substring(info.nrrRealName.length - 1) + '</span>' : '<img src="' + info.nrrHeadIco + '">') +
        " </a> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span><a target = '_blank' href='/student/home/" +
        info.nrrInvitationId +
        "' data-id='" +
        info.nrrStudentId +
        "'>" +
        info.nrrRealName +
        "</a>回复了你</span>" +
        "								</div> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span>" +
        getDateInterval(info.nrrCreateTime) +
        "</span>" +
        "								</div> " +
        "							</div><div class='mes-reply-info-t' stu-name='" +
        info.nrRealName +
        "' data-id='" +
        info.replyReplyId +
        "' stu-id='" +
        info.nrStudentId +
        "'><h2>" +
        info.titleName +
        "</h2> " +
        "							<div class='mes-r' data-id='" +
        info.replyId +
        "'><a target = '_blank' href='/student/home/" +
        info.nrInvitationId +
        "' >" +
        (info.nrHeadIco == null || info.nrHeadIco == '' ? '<span>' + info.nrRealName.substring(info.nrRealName.length - 1) + '</span>' : '<img src="' + info.nrHeadIco + '">') +
        " </a><div><a data-id='" +
        info.nrStudentId +
        "'>" +
        info.nrRealName +
        "</a>:" +
        info.nrContent +
        "</div><div><span>" +
        getDateInterval(info.nrCreateTime) +
        "</span></div></div> ";
        if (info.toReplyReplyId) {
            html += "<div class='mes-r mes-rr' data-id='" + info.toReplyReplyId + "' rr-stu-id='" + info.nrrToStudentId + "' rr-stu-name='" + info.nrrToRealName + "' rr-to-stu-id='" + info.nrrToTostudentId + "' rr-to-stu-name='" + info.nrrToToRealName + "'><a target = '_blank' href='/student/home/" + info.nrrToInvitationId + "'>" + (info.nrrToHeadIco == null || info.nrrToHeadIco == '' ? '' : '<img src="' + info.nrrToRealName + '">') + "</a><div><a target = '_blank' href='/student/home/" + info.nrrToInvitationId + "'  data-id='" + info.nrrToStudentId + "'>" + info.nrrToRealName + "</a>回复<a data-id='" + info.nrrToTostudentId + "' target = '_blank' href='/student/home/" + info.nrrToToInvitationId + "'>" + info.nrrToToRealName + "</a>:" + info.nrrToContent + "</div><div><span>" + getDateInterval(info.nrrToCreateTime) + "</span></div></div> ";
        }
        html += "<div class='mes-r mes-rr' data-id='" + info.replyReplyId + "' rr-stu-id='" + info.nrrStudentId + "' rr-stu-name='" + info.nrrRealName + "' rr-to-stu-id='" + info.nrrToStudentId + "' rr-to-stu-name='" + info.nrrToRealName + "'><a target = '_blank' href='/student/home/" + info.nrrInvitationId + "'>" + (info.nrrHeadIco == null || info.nrrHeadIco == '' ? '<span>' + info.nrrRealName.substring(info.nrrRealName.length - 1) + '</span>' : '<img src="' + info.nrrHeadIco + '">') + "</a><div><a target = '_blank' href='/student/home/" + info.nrrInvitationId + "' data-id='" + info.nrrStudentId + "'>" + info.nrrRealName + "</a>回复<a target = '_blank' href='/student/home/" + info.nrrToInvitationId + "' data-id='" + info.nrrToStudentId + "'>" + info.nrrToRealName + "</a>:" + info.nrrContent + "</div><div><span>" + getDateInterval(info.nrrCreateTime) + "</span><span class='mes-r-reply-but reply'>回复</span></div><div class='tshow-hide'><div contentEditable=true class='edit'></div><div class='mes-r-sq'><span class='mes-r-submit'>提交</span><span class='mes-r-quit'>取消</span></div></div></div> " +
        "						</div></li> ";
        pxnfc1ulhtml.append($(html).data({
            message: res[i]
        }))
    }
}

//news消息详情页面
function makeNewsMessInfo(res, message){
    var obj = $("#message-controller .new-info");
    var pxnhhtml = "				<a > <span class=\"pxn-h-l\"><</span> </a> <span " +
    "				id='pxn-h-title' data-stu-id='" +
    message.studentId +
    "'data-stu-name='" +
    message.realName +
    "'	class=\"pxn-h-title color\" data-id='" +
    message.productId +
    "'>" +
    message.labelName +
    "·" +
    message.productName +
    "</span> <span " +
    "					class=\"pxn-h-time color\">" +
    getDateInterval(message.createTime) +
    "</span> ";
    obj.find(".pxn-h").html(pxnhhtml);
    var pxnbhtml = "				<a >意见" + (message.newReplyCount == 0 ? '' : '<span class="message-but-count-r">' + message.newReplyCount) +
    "</span></a> <a >收藏" +
    (message.newCollectCount == 0 ? '' : '<span class="message-but-count-r">' + message.newCollectCount) +
    "</span></a> <a >喜欢" +
    (message.newPraiseCount == 0 ? '' : '<span class="message-but-count-r">' + message.newPraiseCount) +
    "</span></a> " +
    "				<a >投票" +
    (message.newVoteCount == 0 ? '' : '<span class="message-but-count-r">' + message.newVoteCount) +
    "</span></a> ";
    obj.find(".pxn-b").html(pxnbhtml);
    "			<div class=\"pxn-f\"> " +
    "				<div class=\"pxn-f-c\"> " +
    "					<ul> ";
    var pxnfc1ulhtml = obj.find(".pxn-f .pxn-f-c:eq(0) ul");
    pxnfc1ulhtml.empty();
    if (res.bean.length == 0) {
        pxnfc1ulhtml.append("<div style='text-align:center'>此作品暂时没有评论</div>");
        return;
    }
    for (var i = 0; i < res.bean.length; i++) {
        var info = res.bean[i];
        var html = "<li> " +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<a target='_blank' href = '/student/home/" +
        info.invitationId +
        "' class=\"mes-user-pho\">" +
        (info.headIco == null || info.headIco == '' ? '<span>' + info.realName.substring(info.realName.length - 1) + '</span>' : '<img src = "' + info.headIco + '">') +
        " </a> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span><a target='_blank' href = '/student/home/" +
        info.invitationId +
        "' data-id='" +
        info.studentId +
        "'>" +
        info.realName +
        "</a>给你提了意见</span>" +
        "								</div> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span>" +
        getDateInterval(info.createTime) +
        "</span>" +
        "								</div> " +
        "							</div><div class='mes-reply-info-t' stu-name='" +
        info.realName +
        "' stu-id='" +
        info.studentId +
        "' data-id='" +
        info.titleReplyId +
        "'><h2>标题:&nbsp&nbsp" +
        info.titleName +
        "</h2> " +
        "							<div class='mes-r' data-id='" +
        info.replyId +
        "' stu-id='" +
        info.studentId +
        "' stu-name='" +
        info.realName +
        "'><a target='_blank' href='/student/home/" +
        info.invitationId +
        "'>" +
        (info.headIco == null || info.headIco == '' ? '<span>' + info.realName.substring(info.realName.length - 1) + '</span>' : '<img src="' + info.headIco + '">') +
        " </a><div><a target='_blank' href='/student/home/" +
        info.invitationId +
        "' data-id='" +
        info.studentId +
        "'>" +
        info.realName +
        "</a>:" +
        info.replyContent +
        "</div><div><span>" +
        getDateInterval(info.createTime) +
        "</span><span class='mes-r-reply-but reply'>回复</span>" +
        (info.isadopt == 'Y' ? '<span class=\'mes-r-reply-but adopt adopted\' data-id=\'' + info.replyId + '\'>已采纳</span>' : '<span class=\'mes-r-reply-but adopt\' data-id=\'' + info.replyId + '\'>采纳</span>') +
        "</div> <div class='tshow-hide'><div contentEditable=true class='edit'></div><div class='mes-r-sq'><span class='mes-r-submit'>提交</span><span class='mes-r-quit'>取消</span></div></div></div>";
        for (var j = 0; j < info.nrrList.length; j++) {
            var info2 = info.nrrList[j];
            html += "<div class=\"mes-r mes-rr\" data-id=\"'" + info2.nrrReplyReplyId + "'\" rr-stu-id=\"'" + info2.nrrStudentId + "'\" rr-to-stu-id=\"'" + info2.nrrToStudentId + "'\">" +
            "<a target='_blank' href='/student/home/" +
            info2.nrrInvitationId +
            "'>" +
            (info2.nrrHeadIco == null || info2.nrrHeadIco == '' ? '<span>' + info2.nrrRealName.substring(info2.nrrRealName.length - 1) + '</span>' : '<img src="' + info2.nrrHeadIco + '">') +
            " </a><div><a target='_blank' href='/student/home/" +
            info2.nrrInvitationId +
            "' data-id=\"'" +
            info2.nrrStudentId +
            "'\">" +
            info2.nrrRealName +
            "</a>回复<a target='_blank' href='/student/home/" +
            info2.nrrToInvitationId +
            "' data-id=\"'" +
            info2.nrrToStudentId +
            "'\">" +
            info2.nrrToRealName +
            "</a>" +
            info2.nrrContent +
            "</div><div><span>" +
            getDateInterval(info2.nrrCreateTime) +
            "</span></div></div>"
        }
        html += "</div></li> ";
        pxnfc1ulhtml.append($(html).data({
            message: info
        }))
    }
}

function makeYouAdopt(res, ul){
    ul.empty();
    var pxnhtitle = $("#pxn-h-title");
    var studentId = pxnhtitle.attr("data-stu-id");
    var realName = pxnhtitle.attr("data-stu-name");
    if (res.length == 0) {
        ul.append("<div style='text-align:center'>暂时没有被采纳的意见</div>")
    }
    for (var i = 0; i < res.length; i++) {
        var obj = res[i];
        var html = "<li> " +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<div> " +
        "									<a target='_blank' href='/student/home/" +
        obj.invitationId +
        "' class=\"mes-user-pho\">" +
        (obj.headIco == null || obj.headIco == '' ? '<span>' + obj.realName.substring(obj.realName.length - 1) + '</span>' : '<img src="' + obj.headIco + '">') +
        " </a> " +
        "									<div class=\"mes-reply-info\"> " +
        "										<span><a target='_blank' href='/student/home/" +
        obj.invitationId +
        "' data-id='" +
        obj.studentId +
        "'>" +
        obj.realName +
        "</a>采纳了你的意见<span>" +
        "</span> " +
        "										</span> " +
        "									</div> " +
        "								</div> " +
        "							</div> " +
        "						</li>";
        ul.append($(html).data({
            message: res[i]
        }));
    }
    
}

function makeYouUseful(res, ul){
    ul.empty();
    if (res.length == 0) {
        ul.append("<div style='text-align:center'>暂时没有被认为有用的意见</div>")
    }
    for (var i = 0; i < res.length; i++) {
        var obj = res[i];
        var html = "<li> " +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<a target='_blank' href='/student/home/" +
        obj.invitationId +
        "' class=\"mes-user-pho\">" +
        (obj.headIco == null || obj.headIco == '' ? '<span>' + obj.prRealName.substring(obj.prRealName.length - 1) + '</span>' : '<img src="' + obj.headIco + '">') +
        " </a> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span><a target='_blank' href='/student/home/" +
        obj.invitationId +
        "' data-id='" +
        obj.prStudentId +
        "'>" +
        obj.prRealName +
        "</a>认为你的意见有用<span>" +
        obj.nrContent +
        "</span> </span> " +
        "								</div> " +
        "							</div> " +
        "						</li>";
        ul.append($(html).data({
            message: res[i]
        }));
    }
}

function makeYouPraise(res, ul){
    ul.empty();
    if (res.length == 0) {
        ul.append("<div style='text-align:center'>暂时没有获得赞</div>")
    }
    for (var i = 0; i < res.length; i++) {
        var html = "<li> " +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<a target='_blank' href='/student/home/" +
        res[i].invitationId +
        "' class=\"mes-user-pho\">" +
        (res[i].headIco == null || res[i].headIco == '' ? '<span>' + res[i].prRealName.substring(res[i].prRealName.length - 1) + '</span>' : '<img src="' + res[i].headIco + '">') +
        "</a> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span><a target='_blank' href='/student/home/" +
        res[i].invitationId +
        "' data-id='" +
        res[i].prStudentId +
        "'>" +
        res[i].prRealName +
        "</a>赞了你的回复<span>" +
        res[i].nrrContent +
        "</span> </span> " +
        "								</div> " +
        "							</div> " +
        "						</li>";
        ul.append($(html).data({
            message: res[i]
        }));
    }
}

function makeMeCollect(res, ul){
    ul.empty();
    if (res.bean.length == 0) {
        ul.append("<div style='text-align:center'>此作品还没有人收藏</div>")
    }
    for (var i = 0; i < res.bean.length; i++) {
        var obj = res.bean[i];
        var html = "<li> " +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<a target='_blank' href='/student/home/" +
        obj.invitationId +
        "' class=\"mes-user-pho\">" +
        (obj.headIco == null || obj.headIco == '' ? '<span>' + obj.realName.substring(obj.realName.length - 1) + '</span>' : '<img src="' + obj.headIco + '">') +
        "</a> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span><a target='_blank' href='/student/home/" +
        obj.invitationId +
        "'>" +
        obj.realName +
        "</a>收藏了你的作品</span> " +
        "								</div> " +
        "							</div></li>";
        ul.append($(html).data({
            message: res[i]
        }));
    }
}

function makeMePraise(res, ul){
    ul.empty();
    if (res.bean.length == 0) {
        ul.append("<div style='text-align:center'>此作品暂时还没有人喜欢</div>")
    }
    for (var i = 0; i < res.bean.length; i++) {
        var obj = res.bean[i];
        var html = "<li> " +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<a target='_blank' href='/student/home/" +
        obj.invitationId +
        "' class=\"mes-user-pho\">" +
        (obj.headIco == null || obj.headIco == '' ? '<span>' + obj.realName.substring(obj.realName.length - 1) + '</span>' : '<img src="' + obj.headIco + '">') +
        "</a> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span><a target='_blank' href='/student/home/" +
        obj.invitationId +
        "'>" +
        obj.realName +
        "</a>喜欢了你的作品</span> " +
        "								</div> " +
        "							</div></li>";
        ul.append($(html).data({
            message: res[i]
        }));
    }
}

function makeMeVote(res, ul){
    ul.empty();
    if (res.bean.length == 0) {
        ul.append("<div style='text-align:center'>此作品暂时还没有获得投票</div>")
    }
    for (var i = 0; i < res.bean.length; i++) {
        var obj = res.bean[i];
        var html = "<li>" +
        "							<div class=\"pxn-f-c-box\"> " +
        "								<a target='_blank' href='/student/home/" +
        obj.invitationId +
        "' class=\"mes-user-pho\"> " +
        (obj.headIco == null || obj.headIco == '' ? '<span>' + obj.realName.substring(obj.realName.length - 1) + '</span>' : '<img src="' + obj.headIco + '">') +
        "</a> " +
        "								<div class=\"mes-reply-info\"> " +
        "									<span><a target='_Blank' href='/student/home/" +
        obj.invitationId +
        "'>" +
        obj.realName +
        "</a>投票了你的作品</span> " +
        "								</div> " +
        "							</div></li>";
        ul.append($(html).data({
            message: res[i]
        }));
    }
}


//动态显示点事件
function showPointByMes(point){
    var imgController = $("#message-controller .img-controller");
    var pointHtml = "";
    imgController.find(".mineP,.box").remove();
    if (!point.start) 
        return;
    var left = point.start * 100 + "%";
    var top = point.end * 100 + "%";
    var b = point.studentId == ($(document).data("student") ? $(document).data("student").studentId : '')//判斷是否是當前用戶
    if (b) {
        pointHtml += "<div class=\"mineP\" style='top: " + top + ";left: " + left + "'> " +
        "<div class='reply-point'></div>" +
        "	<div class=\"point-title\"><span title='标题：" +
        point.titleName +
        "  总参与(" +
        point.nrCount +
        ")'>" +
        "<div class='point-pike'></div>" +
        "		<div class='point-title-name' ><a>" +
        point.titleName +
        "</a></div><div class='point-title-nrcount'><a>#" +
        point.nrCount +
        "</a></div> " +
        "	</span></div> " +
        "</div>";
    }
    else {
        pointHtml += "<div class=\"box\" style='top: " + top + ";left: " + left + "'> " +
        "<div class='reply-point'></div>" +
        "	<div class=\"point-title\"><span title='标题：" +
        point.titleName +
        "  总参与(" +
        point.nrCount +
        ")'>" +
        "<div class='point-pike'></div>" +
        "		<div class='point-title-name' ><a>" +
        point.titleName +
        "</a></div><div class='point-title-nrcount'><a>#" +
        point.nrCount +
        "</a></div> " +
        "	</span></div> " +
        "</div>";
    }
    //imgParent.append($(pointHtml).data({notationReply:res[i]}));
    imgController.append($(pointHtml).data({
        "titleReply": point
    }));//此处先不绑定此点的评论数据,待点击查看是将完整数据在进行绑定，前台绑定后台只需查询一次，减轻后台压力
}

//计算高度
function initHeight(){
    var pxf = $("#message-controller .message-box-p");
    var pxfHeight = $("#message-controller").height() - 50;
    pxf.height(pxfHeight);
    var pxnf = $("#message-controller .pxn-f");
    var pxnfcHeight = $("#message-controller").height() - 120;
    pxnf.height(pxnfcHeight);
    //左边图片最大高度
    $("#message-controller .img-controller img").css({
        "max-height": $("#message-controller").height()
    });
}


/*end  消息*/


//jquery 实现滚动加载
function checkscrollside(type){
    //var oParent = $('#pul-main');
    //var aPin = oParent.children('.pin');
    // var lastPinH=aPin[aPin.length-1].offsetTop+Math.floor(aPin[aPin.length-1].offsetHeight/2);//创建【触发添加块框函数waterfall()】的高度：最后一个块框的距离网页顶部+自身高的一半(实现未滚到底就开始加载)
    if (type) {
        var lastPinH = $(type).get(0).scrollHeight - 12;//总高度
        var scrollTop = $(type).scrollTop();//注意解决兼容性
        var documentH = $(type).height();//页面高度
    }
    else {
        var lastPinH = $(document).height() - 20;
        var scrollTop = $(document).scrollTop();//注意解决兼容性
        var documentH = document.documentElement.clientHeight;//页面高度
    }
    return (lastPinH < scrollTop + documentH) ? true : false;//到达指定高度后 返回true，触发waterfall()函数
}

/*start 创建作品*/
function initUploadProduct(){
    //为作品上传主页面添加缓存
    var obj = {};
    $(".zs").data({
        "obj": obj
    })
    
    $(document).delegate(".zs .tshow-zzc", "click", function(){
        $(this).parent().hide();
    });
    $("#upload-but").on("click", function(){
        $(".zs").show();
        $("body").data({
            scrollTop: $("body").scrollTop()
        })
        $("body").css({
            "overflow": "hidden"
        })
        
    })
    $(".zs .zs-l-h-logo,.add-product-calcel,.ts-close").on("click", function(){
        $(".zs").hide();
        scrollBody();
    });
    
    /*选择标签*/
    $(".zs-l-b-title button").on("click", function(){
        var $dom = $(".zs-Occupation-p1");
        var leftUl = $(".zs-Occupation-l ul");
        $dom.show();
        if (!$dom.data("label")) {
            tshowPost("/label/list", {}, function(res){
                //console.log(res);
                var list = res.bean;
                for (var i = 0; i < list.length; i++) {
                    var parentLabel = list[i];
                    leftUl.append($("<li data-id ='" + parentLabel.labelId + "'><span>" + parentLabel.labelName + "</span></li>").data({
                        "children": parentLabel.children
                    }));
                }
                $dom.data({
                    "label": list
                });
            });
        }
    });
    /*选择标签确认事件*/
    $(".zs .zs-Occupation-submit").on("click", function(){
        var $dom = $(".zs .zs-Occupation-r ul li .tshow-checked").parent();
        $(".zs .zs-l-b-title-but").removeClass("tshow-col-red");
        if ($dom.length == 0) {
            $(".zs .zs-l-b-title-but").addClass("tshow-col-red");
        }
        var labelId = $dom.attr("data-id");
        var labelName = $dom.attr("data-name");
        //console.log(labelId);
        $(".zs").data("obj").labelId = labelId;
        $(".zs .zs-Occupation-p1").hide();
        $(".zs .zs-l-b-title-but").text(labelName).attr({
            "data-id": labelId
        });
        createHotPoints(labelId);
    });
    
    /*原创转载选中事件*/
    $(".zs .zs-l-b-sx span").on("click", function(){
        var $me = $(this);
        $(".zs .zs-l-b-sx").removeClass("tshow-col-red");
        $me.addClass("tshow-checked").siblings().removeClass("tshow-checked");
        $(".zs").data("obj").original = $me.attr("data-val");
    });
    /* start sz下的代理事件*/
    //标签的切换事件
    $(".zs").delegate(".zs-Occupation-l ul li", "click", function(){
        $(this).children().addClass("tshow-checked").parent().siblings().children().removeClass("tshow-checked");
        var children = $(this).data("children");
        var rightUl = $(".zs-Occupation-r ul");
        rightUl.empty();
        //console.log(children);
        for (var i = 0; i < children.length; i++) {
            var obj = children[i];
            rightUl.append($("<li data-id ='" + obj.labelId + "' data-name = '" + obj.labelName + "'><span>" + obj.labelName + "</span></li>").click(function(){
                $(this).children().addClass("tshow-checked").parent().siblings().children().removeClass("tshow-checked");
            }));
        }
    })
    
    /*添加内容标签事件*/
    $(".zs").delegate(".zs-r-ws-list ul li", "click", function(){
        var $me = $(this);
        var text = $me.text();
        var conlab = $(".zs .zs-r-ws-conlab span");
        if (conlab.length >= 3) {
            return;
        }
        for (var i = 0; i < conlab.length; i++) {
            if (conlab.eq(i).text() == text) {
                return;
            }
        }
        $(".zs .zs-r-ws-conlab").append("<span>" + text + "</span>");
        initZsinpCss();
    });
    
    $(".zs").delegate(".zs-r-ws-conlab span", "click", function(){
        $(this).remove();
        initZsinpCss();
    })
    
    //编辑作品内容标签的文本框相关事件
    $(".zs").delegate(".zs-r-ws-inp", "blur", function(){
        var $me = $(this);
        var text = $me.val().trim();
        var conlab = $(".zs .zs-r-ws-conlab span");
        if (conlab.length >= 3 || text == "") {
            $me.val("");
            return;
        }
        for (var i = 0; i < conlab.length; i++) {
            if (conlab.eq(i).text() == text) {
                $me.val("");
                return;
            }
        }
        if (text.length > 15) {
            text = $me.val().substring(0, 15).trim();
        }
        $(".zs .zs-r-ws-conlab").append("<span>" + text + "</span>");
        initZsinpCss();
        $me.val("");
    });
    $(".zs").delegate(".zs-r-ws-inp", "keydown", function(event){
        var conlab = $(".zs .zs-r-ws-conlab");
        //console.log(event.keyCode);
        var $me = $(this);
        if (event.keyCode == 13) {
            $me.blur();
            $me.val("");
            $me.focus();
        }
        else 
            if (event.keyCode == 8) {
                conlab.children().last().remove();
                initZsinpCss();
            }
    });
    //标题文本框监听
    $(".zs").delegate(".zs-r-wp-h input", "keydown", function(event){
        var title = $(".zs .zs-r-wp-h input");
        if (title.val().trim() != "") {
            title.removeClass("tshow-col-red");
        }
        else {
            title.addClass("tshow-col-red");
        }
    });
    
    /*图片删除事件*/
    $(".zs").delegate(".img-close", "click", function(){
        $(this).next().remove().end().remove();
        return false;
    });
    
    //上传按钮点击事件
    $(".zs").delegate(".zs-r-wp-z-sc,.zs-r-wp-b-1", "click", function(){
        $("#imgfile").click();
    });
    
    //发布新版本上传按钮点击事件
    $("#up-new-version").delegate(".zs-r-wp-z-sc,.zs-r-wp-b-1", "click", function(){
        $("#imgfile2").click();
    });
    
    /*发布新版本图片删除事件*/
    $("#up-new-version").delegate(".img-close", "click", function(){
        $(this).next().remove().end().remove();
    });
    
    /*发布作品事件*/
    $(".zs").delegate(".submit:not(.submitting)", "click", function(){
        var $me = $(this);
        var state = checkProductSubmit();
        if (!state) {
            alert("还有必输项没有填写");
            return;
        }
        var points = $(".zs .zs-r-ws-conlab span");
        for (var i = 0; i < points.length; i++) {
            var pointName = points.eq(i).text();
            var obj = {};
            var key = 'points[' + i + '].pointName';
            var key2 = 'points[' + i + '].labelId';
            obj[key] = pointName;
            obj[key2] = state["label.labelId"];
            $.extend(state, obj);
        }
        $.extend(state, obj);
        $tsBlockUI({
            showOverlay: true
        });
        //console.log(state);
        tshowPost("/product/saveProduct", state, function(res){
            //console.log(res);
            if (res.success) {
                createPromptBox("发布成功");
                $me.removeClass("submitting").text("发布");
                $('<a href="/"></a>').get(0).click();
            }
        });
        $me.addClass("submitting").text("发布中...");
    });
    
    //作品说明监听事件
    $(document).delegate('.sjsm', 'keyup', function(){
        var $me = $(this);
        var val = $me.val();
        if (val.length >= 350) {
            $me.val($me.val().substring(0, 350));
        }
        $('.zs-r-wt-b-num').text($me.val().length);
    });
    
    /* end   sz下的代理事件*/
}

function createHotPoints(labelId){
    //查询热门内容标签
    var box = $(".zs-r-ws-list ul");
    var html = "";
    tshowPost("/label/hotPoints", {
        labelId: labelId
    }, function(res){
        //console.log(res);
        for (var i = 0; i < res.bean.length; i++) {
            html += '<li>' + res.bean[i].pointName + '</li>';
        }
        box.html(html);
    });
}

/*end   创建作品*/
//重新计算内容标签文本框样式
function initZsinpCss(){
    var inp = $(".zs .zs-r-ws-inp");
    var conlab = $(".zs .zs-r-ws-conlab");
    var inp = $(".zs .zs-r-ws-inp");
    inp.css({
        "width": inp.parent().width() - conlab.width(),
        "padding-left": conlab.width()
    });
    inp.focus();
}

/*start 上传文件(图片)*/
function uploadFile(id, imgparent){
    $.ajaxFileUpload({
        url: "/ueditor/jsp/controller.jsp?action=uploadimage", //需要链接到服务器地址
        secureuri: true,
        fileElementId: id, //文件选择框的id属性
        dataType: "json",
        success: function(data, status){
            //console.log(data.url);
            if (data.state.toUpperCase() != "SUCCESS") {
                alert(data.state)
                return;
            }
            var url = data.url;
            $(imgparent).html($("<img>").attr("src", url)).prepend($("<div class='img-close'></div>"));
            $(".zs .zs-r-wp-z-sc").removeClass("tshow-col-red");
            //console.log(status);
        },
        error: function(data, status, e){
            showDialogWithMsg('ideaMsg', '提示', '文件错误！');
        }
    });
}

/*end   上传文件(图片)*/
/*上传图片验证*/
function checkProductSubmit(){
    var titleBut = $(".zs .zs-l-b-title-but");
    var bsx = $(".zs .zs-l-b-sx");
    var content = $(".zs .zs-r-wp-b-td");
    var conlab = $(".zs .zs-r-ws-conlab");
    var title = $(".zs .zs-r-wp-h input");
    var productName = title.val().trim();
    var state = true;
    titleBut.removeClass("tshow-col-red");
    bsx.removeClass("tshow-col-red");
    $(".zs .zs-r-wp-z-sc").removeClass("tshow-col-red");
    if (!(titleBut).attr("data-id")) {
        titleBut.addClass("tshow-col-red");
        state = false;
    }
    if (bsx.find(".tshow-checked").length == 0) {
        bsx.addClass("tshow-col-red");
        state = false;
    }
    if (content.find("img").length == 0) {
        $(".zs .zs-r-wp-z-sc").addClass("tshow-col-red");
        state = false;
    }
    if (productName == "") {
        title.addClass("tshow-col-red");
        state = false;
    }
    if (!state) {
        return state;
    }
    //获取图片的原始尺寸
    var naturalImg = getNaturalWidth(content.find("img"));
    var naturalWidth = naturalImg.naturalWidth;
    var naturalHeight = naturalImg.naturalHeight;
    var obj = {
        "label.labelId": titleBut.attr("data-id"),
        "original": bsx.find(".tshow-checked").attr("data-val"),
        "content": content.find("img").attr("src"),
        "productDisc": $(".sjsm").val(),
        "productName": productName,
        "width": naturalWidth,
        "height": naturalHeight
    }
    return obj;
}

/*start 搜索*/
function initSearch(){
    //导航栏绑定搜索事件
    $("#header #sear").on("click", function(){
        $("body").data({
            "scrollTop": $("body").scrollTop()
        }).css({
            "overflow": "hidden"
        });
        $("#search").show();
    });
    $("#search .t-search-h-close ").on("click", function(){
        $("body").scrollTop($("body").data("scrollTop"));
        $("#search").hide();
        scrollBody();
    });
    //检索请求
    $("#search .t-search-h-input input").on("keydown", function(event){
        var $me = $(this);
        if (event.keyCode == 13) {
            $me.focus();
            var key = $me.val().trim();
            tshowPost("/search", {
                "key": key
            }, function(res){
                //console.log(res);
                createSearchSchool(res.schools);
                createSearchLabel(res.labels);
                createSearchStudent(res.students);
            });
        }
    });
    //查看详情事件(7个事件)
    //搜索下的代理事件
    $(".t-search ").delegate(".search-lp", "click", function(){
        var $me = $(this);
        var spans = $me.find(".result-box-b-box-list .name span");
        var labelId = spans.eq(0).attr("data-id");
        var pointId = spans.eq(1).attr("data-id");
        var href = "/label/home/" + labelId + "?pointId=" + pointId;
        var a = $("<a target='_blank'></a>");
        a.attr({
            href: href
        })
        //console.log(a)
        a.get(0).click();
    });
    
    $(".t-search ").delegate(".search-l", "click", function(){
        var $me = $(this);
        var spans = $me.find(".result-box-b-box-list .name span");
        var labelId = spans.eq(0).attr("data-id");
        var href = "/label/home/" + labelId;
        var a = $("<a target='_blank'></a>");
        a.attr({
            href: href
        })
        //console.log(a)
        a.get(0).click();
    });
    
    $(".t-search ").delegate(".search-ls", "click", function(){
        var $me = $(this);
        var spans = $me.find(".result-box-b-box-list .name span");
        var labelId = spans.eq(0).attr("data-id");
        var schoolId = spans.eq(1).attr("data-id");
        var href = "/label/home/" + labelId + "?schoolId=" + schoolId + "";
        var a = $("<a target='_blank'></a>");
        a.attr({
            href: href
        })
        //console.log(a)
        a.get(0).click();
    });
    
    $(".t-search ").delegate(".search-st", "click", function(){
        var $me = $(this);
        var spans = $me.find(".result-box-b-box-list .name span");
        var invitationId = spans.eq(2).text().trim();
        var href = "/student/home/" + invitationId;
        var a = $("<a target='_blank'></a>");
        a.attr({
            href: href
        })
        //console.log(a)
        a.get(0).click();
    });
    
    $(".t-search ").delegate(".search-sp", "click", function(){
        var $me = $(this);
        var spans = $me.find(".result-box-b-box-list .name span");
        var schoolId = spans.eq(0).attr("data-id");
        var pointId = spans.eq(1).attr("data-id");
        var href = "/school/home/" + schoolId + "?pointId=" + pointId;
        var a = $("<a target='_blank'></a>");
        a.attr({
            href: href
        })
        //console.log(a)
        a.get(0).click();
    });
    
    $(".t-search ").delegate(".search-s", "click", function(){
        var $me = $(this);
        var spans = $me.find(".result-box-b-box-list .name span");
        var schoolId = spans.eq(0).attr("data-id");
        if (spans.length > 1) {
            createPromptBox('此学校还未解锁');
            return;
        }
        var href = "/school/home/" + schoolId;
        var a = $("<a target='_blank'></a>");
        a.attr({
            href: href
        })
        //console.log(a)
        a.get(0).click();
    });
    
    //达人样式中的关注事件
    $(document).delegate(".ubc-b button:not(.attentioned)", "click", function(){
        var $me = $(this);
        $me.addClass("attentioned");
        var toStudentId = $me.prev().attr("data-id");
        var studentId = $(document).data("student").studentId;
        if (!studentId) {
            createPromptBox("请先登录", {
                width: '90px',
                height: '20px',
                'position': 'absolute',
                left: '75px',
                top: '85px'
            }, $me);
            return;
        }
        if (studentId && studentId == toStudentId) {
            createPromptBox("无需关注自己", {
                width: '90px',
                height: '10px',
                'position': 'absolute',
                left: '75px',
                top: '22px',
                'line-height': '10px'
            }, $me);
            return;
        }
        //console.log(toStudentId);
        tshowPost("/student/saveAttention", {
            toStudentId: toStudentId
        }, function(res){
            //console.log(res);
            if (!res.success) {
                createPromptBox(res.mess.message);
                return;
            }
            $me.html("已关注");
        })
    });
    
    $(document).delegate(".ubc-b .attentioned", "click", function(){
        var $me = $(this);
        $me.removeClass("attentioned");
        var toStudentId = $me.prev().attr("data-id");
        var studentId = $(document).data("student").studentId;
        if (!studentId) {
            createPromptBox("请先登录", {
                width: '90px',
                height: '10px',
                'position': 'absolute',
                left: '75px',
                top: '22px',
                'line-height': '10px'
            }, $me);
            return;
        }
        if (studentId && studentId == toStudentId) {
            createPromptBox("无需关注自己", {
                width: '90px',
                height: '20px',
                'position': 'absolute',
                left: '75px',
                top: '85px'
            }, $me);
            return;
        }
        //console.log(toStudentId);
        tshowPost("/student/deleteAttention", {
            toStudentId: toStudentId
        }, function(res){
            //console.log(res);
        })
        $me.html("关注");
    });
    
}

function createSearchSchool(schools){
    var html = "";
    var schoolBox = $(".t-search-result .result-box:eq(0) .result-box-b ul");
    var schoolS = schools.s;
    var schoolSL = schools.sl;
    var schoolSP = schools.sp;
    for (var i = 0; i < schoolS.length; i++) {
        var s = schoolS[i];
        html += "<li class='search-s'> " +
        "						<div class=\"result-box-b-box\"> " +
        "							<div class=\"result-box-b-box-pic\"> " +
        "								<img src='" +
        s.badge +
        "'> " +
        "							</div> " +
        "							<div class=\"result-box-b-box-list\"> " +
        "								<p class=\"name\"> " +
        "									<span data-id='" +
        s.schoolId +
        "'>" +
        s.schoolName +
        "</span> " +
        (s.state == 'N' ? '<span class=\"lock\"></span>' : '') +
        "								</p> " +
        "								<p class=\"number\"> " +
        "									<span>人数" +
        s.studentCount +
        "</span> <span>标签" +
        s.labelCount +
        "</span> " +
        "								</p> " +
        "							</div> " +
        "						</div> " +
        "</li>";
    }
    for (var i = 0; i < schoolSP.length; i++) {
        var sp = schoolSP[i];
        html += "<li class='search-sp'> " +
        "						<div class=\"result-box-b-box\"> " +
        "							<div class=\"result-box-b-box-pic\"> " +
        "								<img src='" +
        sp.badge +
        "'> " +
        "							</div> " +
        "							<div class=\"result-box-b-box-list\"> " +
        "								<p class=\"name\"> " +
        "									<span data-id='" +
        sp.schoolId +
        "'>" +
        sp.schoolName +
        "</span> <span data-id='" +
        sp.pointId +
        "'>(" +
        sp.pointName +
        ")</span>" +
        "								</p> " +
        "								<p class=\"number\"> " +
        "									<span>人数" +
        sp.studentCount +
        "</span> <span>标签" +
        sp.labelCount +
        "</span> " +
        "								</p> " +
        "							</div> " +
        "						</div> " +
        "</li>";
    }
    for (var i = 0; i < schoolSL.length; i++) {
        var sl = schoolSL[i];
        html += "<li class='search-sl'> " +
        "						<div class=\"result-box-b-box\"> " +
        "							<div class=\"result-box-b-box-pic\"> " +
        "								<img src='" +
        sl.badge +
        "'> " +
        "							</div> " +
        "							<div class=\"result-box-b-box-list\"> " +
        "								<p class=\"name\"> " +
        "									<span data-id='" +
        sl.schoolId +
        "'>" +
        sl.schoolName +
        "</span> <span data-id='" +
        sl.labelId +
        "'>(" +
        sl.labelName +
        ")</span>" +
        "								</p> " +
        "								<p class=\"number\"> " +
        "									<span>人数" +
        sl.studentCount +
        "</span> <span>标签" +
        sl.labelCount +
        "</span> " +
        "								</p> " +
        "							</div> " +
        "						</div> " +
        "</li>";
    }
    schoolBox.html(html);
}

function createSearchLabel(labels){
    var html = "";
    var schoolBox = $(".t-search-result .result-box:eq(1) .result-box-b ul");
    var points = labels.p;
    var labelsL = labels.l;
    var LabelsLS = labels.ls;
    for (var i = 0; i < points.length; i++) {
        var p = points[i];
        html += "<li class='search-lp'> " +
        "						<div class=\"result-box-b-box\"> " +
        "							<div class=\"result-box-b-box-pic\"> " +
        "								<img src='" +
        p.backImaS +
        "'> " +
        "							</div> " +
        "							<div class=\"result-box-b-box-list\"> " +
        "								<p class=\"name\"> " +
        "									<span data-id = '" +
        p.labelId +
        "'>" +
        p.labelName +
        "</span><span data-id='" +
        p.pointId +
        "'>(" +
        p.pointName +
        ")</span>" +
        "								</p> " +
        "								<p class=\"number\"> " +
        "									<span>人数" +
        p.studentCount +
        "</span> <span>作品" +
        p.productCount +
        "</span> " +
        "								</p> " +
        "							</div> " +
        "						</div> " +
        "</li>"
    }
    for (var i = 0; i < labelsL.length; i++) {
        var l = labelsL[i];
        html += "<li class='search-l'> " +
        "						<div class=\"result-box-b-box\"> " +
        "							<div class=\"result-box-b-box-pic\"> " +
        "								<img src='" +
        l.backImaS +
        "'> " +
        "							</div> " +
        "							<div class=\"result-box-b-box-list\"> " +
        "								<p class=\"name\"> " +
        "									<span data-id = '" +
        l.labelId +
        "'>" +
        l.labelName +
        "</span>" +
        "								</p> " +
        "								<p class=\"number\"> " +
        "									<span>人数" +
        l.studentCount +
        "</span> <span>作品" +
        l.productCount +
        "</span> " +
        "								</p> " +
        "							</div> " +
        "						</div> " +
        "</li>"
    }
    for (var i = 0; i < LabelsLS.length; i++) {
        var ls = LabelsLS[i];
        html += "<li class='search-ls'> " +
        "						<div class=\"result-box-b-box\"> " +
        "							<div class=\"result-box-b-box-pic\"> " +
        "								<img src='" +
        ls.backImaS +
        "'> " +
        "							</div> " +
        "							<div class=\"result-box-b-box-list\"> " +
        "								<p class=\"name\"> " +
        "									<span data-id = '" +
        ls.labelId +
        "'>" +
        ls.labelName +
        "</span><span data-id='" +
        ls.schoolId +
        "'>(" +
        ls.schoolName +
        ")</span>" +
        "								</p> " +
        "								<p class=\"number\"> " +
        "									<span>人数" +
        ls.studentCount +
        "</span> <span>作品" +
        ls.productCount +
        "</span> " +
        "								</p> " +
        "							</div> " +
        "						</div> " +
        "</li>"
    }
    schoolBox.html(html);
}

function createSearchStudent(students){
    var html = "";
    var schoolBox = $(".t-search-result .result-box:eq(2) .result-box-b ul");
    var sts = students.st;
    for (var i = 0; i < sts.length; i++) {
        var st = sts[i];
        html += "<li class='search-st'> " +
        "						<div class=\"result-box-b-box\"> " +
        "							<div class=\"result-box-b-box-pic\"> " +
        (st.headIco == null || st.headIco == '' ? '<span>' + st.realName.substring(st.realName.length - 1) + '</span>' : '<img src="' + st.headIco + '">') +
        "							</div> " +
        "							<div class=\"result-box-b-box-list\"> " +
        "								<p class=\"name\"> " +
        "									<span data-id = '" +
        st.studentId +
        "'>" +
        st.realName +
        "</span> <span>|</span> <span>" +
        st.invitationId +
        "</span> <span " +
        "										class=\"fl-right\">" +
        (st.isAttention ? '<span class="attention attentioned">已关注</span>' : '<span class="attention">关注</span>') +
        "</span> " +
        "								</p> " +
        "								<p class=\"number\"> " +
        "									<span data-id='" +
        st.labelId +
        "'>" +
        st.labelName +
        "</span> " +
        "								</p> " +
        "							</div> " +
        "						</div> " +
        "</li>";
    }
    schoolBox.html(html);
}

/*end 搜索*/

/*start瀑布流作品*/
/*加载作品*/
function createProductsHtml(products, ulp){
    for (var i = 0; i < products.length; i++) {
        var product = products[i];
        var html = "<div class=\"boxw\" data-id=\"" + product.productId + "\">" +
        "            <div class=\"boxin\">" +
        "                <div class=\"boxw-c\">" +
        "                    <div style=\"height:" +
        (Math.floor(product.height / (product.width / 236))) +
        "px\" class=\"tshow-rel\">" +
        (product.versionCount != 1 ? "<div class='product-version-box'><div class='product-version-t'>" + product.versionCount + "</div><div class='product-version-b'></div></div>" : '') +
        "                        <img src=\"" +
        product.smallContent +
        "\">" +
        "                    </div>" +
        "                </div>" +
        "                <div class='zt-t-p'>" +
        "                    <span class=\"zt-t\">" +
        product.productName +
        "</span>" +
        "                    <div class=\"att\">" +
        "                        <span class=\"" +
        (product.original == 'Y' ? 'att-b-yc' : 'att-b-zz') +
        "\">" +
        "</span>" +
        "                    </div>" +
        "                </div>" +
        "                <div class=\"boxw-l\">" +
        "                    <ul>";
        for (var j = 0; j < product.points.length; j++) {
            var point = product.points[j];
            html += "                        <li>" +
            "                            <a target=\"_blank\" href=\"/label/home/" +
            product.label.labelId +
            "/?pointId=" +
            point.pointId +
            "\" data-id=\"" +
            point.pointId +
            "\" data-label-id=\"" +
            point.pointId +
            "\">" +
            point.pointName +
            "</a>" +
            "                        </li>";
        }
        
        html += "                    </ul>" +
        "                </div>" +
        "                <div class=\"boxw-e\">" +
        "                    <p>" +
        product.productDisc +
        "                    </p>" +
        "                </div>" +
        "                <div class=\"boxw-p\">" +
        "                    <span class=\"hot\">#" +
        product.score +
        "</span>" +
        (product.praises.length > 0 ? '<span data-id="' + product.productId + '" class=\"love loved icon-love-pbled icon\"></span><span class="praise-count">' + product.praiseCount + '</span>' : '<span data-id="' + product.productId + '" class=\"love icon-love-pbl icon\"></span><span class="praise-count">' + product.praiseCount + '</span>') +
        "                </div>" +
        "                <div class=\"boxw-n\">" +
        "                    <div class=\"boxw-n-p\">" +
        "                        <a target=\"_blank\" href=\"/student/home/" +
        product.student.invitationId +
        "\">" +
        (product.student.headIco == null || product.student.headIco == '' ? '<span>' + product.student.realName.substring(product.student.realName.length - 1) + '</span>' : '<img src="' + product.student.headIco + '">') +
        "</a>" +
        "                    </div>" +
        "                    <div class=\"boxw-n-u-info\">" +
        "                        <div class=\"boxw-n-u-info-n\">" +
        "                            <span><a target='_blank' href='/student/home/" +
        product.student.invitationId +
        "'>" +
        product.student.realName +
        "</a></span>" +
        "                        </div>" +
        "                        <div class=\"boxw-n-u-info-s\">" +
        "                            <span><a target='_blank' href='/school/home/" +
        product.student.school.schoolId +
        "'>" +
        product.student.school.name +
        "</a></span>·<span>" +
        product.student.year +
        "</span>" +
        "                        </div>" +
        "                    </div>" +
        "                </div>" +
        "            </div>" +
        "</div>";
        //找出ul中高度最小的下标
        var index = findMinUlIndex(ulp);
        //将请求数据添加到各个ul
        $(ulp).children("ul").eq(index).append($(html).data({
            "product": products[i]
        }));
    }
    
}

//判断生成头像
function createHeadIco(student){
    var html;
    if (!student.headIco || student.headIco == null || student.headIco == '') {
        html = "<span class='boxw-n-p-imgspan'>" + student.realName.substring(student.realName.length - 1) + "</span>"
    }
    else {
        html = " 					<img src='" +
        student.headIco +
        "'> ";
    }
    return html;
    
}

function createHeadIcoPc(student){
    var html;
    if (!student.headIco || student.headIco == null || student.headIco == '') {
        html = "<span class='boxw-n-p-imgspan-pc'>" + student.realName.substring(student.realName.length - 1) + "</span>"
    }
    else {
        html = " 					<img src='" +
        student.headIco +
        "'> ";
    }
    return html;
    
}

function findMinUlIndex(ulp){
    var oParent = $(ulp);
    var uls = oParent.children("ul");
    var arr = [];
    var index = 0;
    for (var i = 0; i < uls.length - 1; i++) {
        if (uls.eq(index).height() > uls.eq(i + 1).height()) {
            index = i + 1;
        }
    }
    return index;
}

/*end  瀑布流作品*/


/*start 通用工具类*/
//获取图片的原始尺寸
function getNaturalWidth(img){
    var image = new Image();
    image.src = img.attr("src");
    var naturalWidth = image.width;
    var naturalHeight = image.height;
    return {
        naturalWidth: naturalWidth,
        naturalHeight: naturalHeight
    };
}

//获取图片的原始尺寸
function getNaturalWidthSrc(src){
    var image = new Image();
    image.src = src;
    var naturalWidth = image.width;
    var naturalHeight = image.height;
    return {
        naturalWidth: naturalWidth,
        naturalHeight: naturalHeight
    };
}


/*计算和当前时间的时间间隔*/
function getDateInterval(createTime, type){
    var y = "年";
    var m = "月";
    var d = "日";
    if (type == "/") {
        y = m = d = "/";
    }
    if (!createTime) {
        return "刚刚";
    }
    var date = new Date();
    var year = createTime.substring(0, 4);
    var month = createTime.substring(5, 7);
    var day = createTime.substring(8, 10);
    var hours = createTime.substring(11, 13);
    var minites = createTime.substring(14, 16);
    var seconds = createTime.substring(17, 19);
    if (date.getFullYear() != year) {
        return year + y + month + m + day + d + hours + ":" + minites;
    }
    else 
        if ((date.getMonth() + 1) != month) {
            return month + m + day + d + hours + ":" + minites;
        }
        else 
            if (date.getDate() != day) {
                return month + m + day + d + hours + ":" + minites;
            }
            else 
                if (date.getHours() != hours) {
                    return parseInt(date.getHours()) - parseInt(hours) + "小时前";
                }
                else 
                    if (date.getMinutes() != minites) {
                        return parseInt(date.getMinutes()) - parseInt(minites) + "分钟前";
                    }
                    else 
                        if (date.getSeconds() != seconds) {
                            return parseInt(date.getSeconds()) - parseInt(seconds) + "秒前";
                        }
}

//后台时间差
function getDateIntervalS(createTime, serverTime, type){
    var y = "年";
    var m = "月";
    var d = "日";
    if (type == "/") {
        y = m = d = "/";
    }
    var year = createTime.substring(0, 4);
    var month = createTime.substring(5, 7);
    var day = createTime.substring(8, 10);
    var hours = createTime.substring(11, 13);
    var minites = createTime.substring(14, 16);
    var seconds = createTime.substring(17, 19);
    var year2 = serverTime.substring(0, 4);
    var month2 = serverTime.substring(5, 7);
    var day2 = serverTime.substring(8, 10);
    var hours2 = serverTime.substring(11, 13);
    var minites2 = serverTime.substring(14, 16);
    var seconds2 = serverTime.substring(17, 19);
    if (parseInt(year2) - parseInt(year) != 0) {
        return year + y + month + m + day + d + hours + ":" + minites;
    }
    if (parseInt(month2) - parseInt(month) != 0) {
        return month + m + day + d + hours + ":" + minites;
    }
    if (parseInt(day) - parseInt(day2) != 0) {
        return month + m + day + d + hours + ":" + minites;
    }
    if (parseInt(hours2) - parseInt(hours) != 0) {
        return (parseInt(hours2) - parseInt(hours)) + "小时前";
    }
    if (parseInt(minites2) - parseInt(minites) != 0) {
        return (parseInt(minites2) - parseInt(minites)) + "分钟前";
    }
    if (parseInt(seconds2) - parseInt(seconds) != 0) {
        return (parseInt(minites2) - parseInt(minites)) + "秒前";
    }
}

/*body的滚动条是否显示*/
function scrollBody(){
    if ($("#message-controller").is(":visible") || $(".phg-opu-x").is(":visible") || $("#search").is(":visible") || $(".zs").is(":visible")) {
        $("body").css({
            "overflow": "hidden"
        });
    }
    else {
        $("body").css({
            "overflow": "auto"
        });
        $("body").scrollTop($("body").data("scrollTop"));
    }
}

//提示框
function createPromptBox(message, css, box, time){
    if (!time) {
        time = 1800;
    }
    var defaultCss = {
        left: '50%',
        'margin-left': '-90px',
        top: '50%',
        color: 'white',
        background: 'black',
        'border-radius': '10px',
        position: 'fixed',
        display: 'none',
        'text-align': 'center',
        padding: '10px 5px',
        'z-index': '9999',
        'font-size': '12px',
        'width': '180px',
        'text-align': 'center'
    };
    $.extend(defaultCss, css);
    var promptBox = $("<div class='prompt-box'>" + message + "</div>");
    promptBox.on("click", function(){
        $(this).remove()
    }).css(defaultCss);
    if (box) {
        box.append(promptBox);
    }
    else {
        $("body").append(promptBox);
    }
    promptBox.fadeIn("slow");
    setTimeout(function(){
        promptBox.fadeOut("slow");
        setTimeout(function(){
            promptBox.remove()
        }, time);
    }, time);
    return false;
}

function getCookie(name){
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) 
        return unescape(arr[2]);
    else 
        return null;
}

function delCookie(name){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) 
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

//如果需要设定自定义过期时间
//那么把上面的setCookie　函数换成下面两个函数就ok;
//程序代码
function setCookie(name, value, time){
    var strsec = getsec(time);
    var exp = new Date();
    exp.setTime(exp.getTime() + strsec * 1);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function getsec(str){
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    if (str2 == "s") {
        return str1 * 1000;
    }
    else 
        if (str2 == "h") {
            return str1 * 60 * 60 * 1000;
        }
        else 
            if (str2 == "d") {
                return str1 * 24 * 60 * 60 * 1000;
            }
}

function tshowPost(url, obj, callBack){
    $.post(url, obj, function(res){
        if (res.success == false) {
            if (res.mess.state == 'sysFailed' || res.mess.state == 'noprivilege') {
                createPromptBox(res.mess.message);
                return;
            }
        }
        callBack(res);
    });
}

function tshowAjax(obj){
    $.ajax({
        url: obj.url,
        type: obj.type,
        data: obj.data,
        async: obj.async,
        success: function(res){
            if (res.success == false) {
                if (res.mess.state == 'sysFailed' || res.mess.state == 'noprivilege') {
                    createPromptBox(res.mess.message);
                    return;
                }
            }
            obj.success(res);
        }
    });
}

/*tshow弹出框*/
function createTck(obj, css, qrClick){
    var defaultCss = {
        position: 'fixed',
        left: '50%',
        top: '50%',
        'z-index': 1000,
        'border-radius': '3px',
        'border': '1px solid #F7E2E2',
        'background-color': 'white'
    }
    var html = "<div class='tshow-tck'>" +
    "<div class='tck-header'><div>" +
    obj.header +
    "</div></div>" +
    "<div class='tck-center'><div>" +
    obj.center +
    "</div></div>" +
    "<div class='tck-bottom'><div><button class='tck-qr'>确定</button><button class='tck-qx'>取消</button></div></div>" +
    "</div>";
    var $html = $(html);
    $html.data({
        "qrClick": qrClick
    })
    $.extend(defaultCss, css);
    $html.css(defaultCss);
    $('body').append($html);
}

function $tsBlockUI(obj){
    var defaultObj = {
        css: {
            width: '50px',
            height: '50px',
            top: '47%',
            left: '50%',
            'border-radius': '50%',
            'background': 'none',
            border: 'none',
            'margin-left': '-25px'
        
        },
        showOverlay: false,
        message: "<img src='/img/123321.gif'>"
    }
    
    $.extend(true, defaultObj, obj);
    $.blockUI(defaultObj);
}

function formatDate(date){
    date = date.replace(/-/g, '/').substring(0, 10);
    return date;
}

/*查询是否有更多数据*/
function isMoreData($obj){
    if ($obj.data().flag == false) {
        if ($obj.data().flagNum != 0) {
            createPromptBox('加载完毕', {
                'top': '91%'
            });
            $obj.data({
                'flagNum': 0
            });
        }
        return false;
    }
    return true;
}

function checkEmail(email){
    var reg = '^([a-zA-Z0-9_-])+@(([a-zA-Z0-9_-]){1,10}.){1,10}(com|cn|tw|126.com|163.com|sina.com|com.cn|com.tw)$';
    if (!email.match(reg)) {
        return false;
    }
    return true;
}

//这是有设定过期时间的使用示例：
//s20是代表20秒
//h是指小时，如12小时则是：h12
//d是天数，30天则：d30
//导航栏切换事件
/*end  通用工具类*/
