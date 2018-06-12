(function(window, document, $){
    var studentHome = function(){
        initStudentHomeData();
        initHeight();
        initphgoupxbHeight();
        initPhgDaili();
        initUl();
        initHomeEvent();
    }
    window.studentHome = studentHome;
    
    $(document).on("scroll", function(){
        showOrHideHeadCenter();
    });
    
    window.onresize = function(){
        initHeight();
        initphgoupxbHeight();
    }
    /*显示或者隐藏顶部姓名和下拉框*/
    function showOrHideHeadCenter(){
        var phghuTop = $("#phg-h-usern").offset().top - $("#header").height();
        var anchorpTop = $("#anchor-phghu").offset().top - $("#header").height();
        if ($(document).scrollTop() > phghuTop) {
            $("#head-stu-name").show();
        }
        else {
            $("#head-stu-name").hide();
        }
        if ($(document).scrollTop() > anchorpTop) {
            $("#head-xlk").show().css({
                display: 'inline-block'
            });
        }
        else {
            $("#head-xlk").hide();
        }
    }
    
    /*主页面下的代理事件*/
    function initPhgDaili(){
        //中间导航栏按钮
        $(".phg").delegate(".phg-h-usersx ul li", "click", function(){
            var $me = $(this);
            var index = $me.index();
            var dom = $(".phg .phg-b-list-p .phg-b-list").eq(index);
            dom.show().siblings().hide();
            $me.addClass("tshwo-bor-bot").siblings().removeClass("tshwo-bor-bot");
            var text = $me.attr("data-name");
            $("#head-xlk-span").text(text);
            var studentId = $("#phg-h-usern").attr("data-id");
            var dataType = $me.attr('data-type');
            var page = $me.data("page");
            if (dataType == 'attention') {
                if ($me.data("students")) {
                    /*var html = createStudentsHtml($me.data("students"));
                     dom.find(".phg-b-att").children("ul").html(html);*/
                }
                else {
                    var obj = {
                        studentId: studentId,
                        type: dataType,
                        pageNo: page.pageNo,
                        pageSize: page.pageSize
                    };
                    $.post("/student/home/student/list", obj, function(res){
                        var students = JSON.parse(res.map.students);
                        //console.log(students);
                        var html = createStudentsHtml(students);
                        dom.find(".phg-b-att").children("ul").html(html);
                        $me.data({
                            "students": students
                        });
                    })
                }
            }
            else 
                if (dataType == 'student') {
                    if ($me.data("students")) {
                    /* createStudentsHtml($me.data("students"));*/
                    }
                    else {
                        var obj = {
                            studentId: studentId,
                            type: dataType,
                            pageNo: page.pageNo,
                            pageSize: page.pageSize
                        };
                        $.post("/student/home/student/list", obj, function(res){
                            //console.log(res);
                            var students = JSON.parse(res.map.students);
                            //console.log(students);
                            var html = createStudentsHtml(students);
                            dom.find(".phg-b-att").children("ul").html(html);
                            $me.data({
                                "students": students
                            });
                        })
                    }
                }
        });
        
        //点击作品文件夹展示列表
        $(".phg").delegate(".m-box", "click", function(){
            var $me = $(this);
            showPhgopux();
            var t = $me.find('.table');
            var id = $me.attr('data-id');
            var name = t.text();
            var type = $('.phg .phg-h-usersx .tshwo-bor-bot').attr("data-type");
            var studentId = $("#phg-h-usern").attr("data-id");
            $('.phg .phg-opu-x-h span').eq(0).text(name);
            $('.phg .phg-opu-x-h span').eq(0).attr({
                "data-id": id
            });
            $(".phg .phg-opu-x-b").children("ul").empty();
            var page = $(".phg .phg-opu-x-b").data("page");
            page.pageNo = 1;
            if (($me).data('products')) {
                createWaterFall(($me).data('products'));
            }
            else {
                var obj = {
                    studentId: studentId,
                    type: type,
                    labelId: id,
                    pageNo: 1,
                    pageSize: 10
                };
                $.post("/student/home/product/list", obj, function(res){
                    var products = JSON.parse(res.map.products);
                    //console.log(products);
                    createWaterFall(products);
                    $me.data({
                        'products': products
                    });
                })
            }
        });
        
        $(".phg").delegate(".ubc-h", "click", function(){
            // showPhgopux();
        });
        
        $(".phg").delegate(".phg-opu-x-h-close", "click", function(){
            $(".phg-opu-x").hide();
            $("body").css({
                "overflow": "auto"
            })
            $("body").scrollTop($("body").data("srollTop"));
            showOrHideHeadCenter();
            //重置前台文件夹更多数据加载参数
            $(".phg .phg-opu-x-b").data({
                flag: true,
                'flagNum': 1
            });
        });
        
        $(".phg").delegate(".phg-b-att-list .ubc-h", "click", function(){
            var id = $(this).parent().parent().attr("data-invitation");
            var url = "/student/home/" + id;
            window.location.href = url;
        });
        
        $("body").delegate(".head-xlk ul li", "click", function(){
            var $me = $(this);
            $(".phg .phg-h-usersx ul li").eq($me.index()).click();
            $(".phg .phg-opu-x-h-close").click();
            
        });
        
        //绑定弹出详情页面事件
        (function(){
            $(".phg").delegate(".phg-opu-x-b .boxw-c", "click", openProductInfo);
        })();
        
        /*显示用户的参与信息*/
        $(".phg").delegate(".point-title", "click", function(event){
            //console.log(event.pageX)
            var $me = $(this);
            var lOrRBox = $me.next();
            if (lOrRBox.hasClass('point-suggest-nrs-left') || lOrRBox.hasClass('point-suggest-nrs-right')) {
                lOrRBox.removeClass('point-suggest-nrs-left').removeClass('point-suggest-nrs-right');
                return false;
            }
            var bl = event.pageX / $(window).width();
            $(".phg .point-suggest-nrs").removeClass("point-suggest-nrs-left").removeClass("point-suggest-nrs-right");
            if (bl < 0.5) {
                $me.next().addClass("point-suggest-nrs-left");
            }
            else {
                $me.next().addClass("point-suggest-nrs-right");
                $me.next().children("div").addClass("tshow-dir-r")
            }
            return false;
        });
        //跟他学事件
        $(".study-button-grx").on("click", function(){
            var $me = $(this);
            $("#gtx").show();
            var page = $me.data("page");
            if (!$me.data("course")) {
                loadCourseList($me);
            }
        });
        $("#gtx .tshow-zzc").on("click", function(){
            $(this).parent().hide();
        });
        //跟他学导航栏切换
        tshowUtil.navChange(".phg-enroll-nav>span", ".phg-enroll-nav-c>div", "phg-enroll-nav-col");
        //查看课程的报名者
        $(document).delegate(".wybm", "click", function(){
            var $me = $(this);
            var courseId = $me.attr("data-id");
            if (!$me.data("students")) {
            
                //发请求
                tshowPost("/company/findYbmStudents", {
                    courseId: courseId
                }, function(res){
                    //console.log(res);
                    if (res.success) {
                        $me.data({
                            students: true
                        });
                        createYbmStudents(res.bean, $me);
                    }
                });
            }
            $me.parent().next().toggle();
        });
        //报名课程
        $(document).delegate(".Course-box-user-bm:not(.Course-box-user-bmed)", "click", function(){
            var $me = $(this);
            var courseId = $me.attr("data-id");
            tshowPost("/company/saveSingupCourse", {
                courseId: courseId
            }, function(res){
                //console.log(res);
                if (!res.success) {
                    if (res.mess.state == 'serviceFailed') {
                        createPromptBox("已经报名啦");
                    }
                    else {
                        createPromptBox("首次参加请先提交报名信息");
                        $(".phg-enroll-nav>span:eq(0)").click();
                    }
                }
                else {
                    $me.addClass("Course-box-user-bmed").text("已参加");
                    createPromptBox("参加成功");
                }
                
            });
        });
        //收起报名人数
        $(document).delegate(".course-shoqi", "click", function(){
            var $me = $(this);
            $me.parent().hide();
            
        });
        //提交报名信息
        $("#submit-bm").on("click", function(){
            var telephone = $(".phg-enroll-nav-c-left-b-tel").val();
            var remark = $(".phg-enroll-nav-c-left-b-text").val();
            var toStudentId = $("#phg-h-usern").attr("data-id");
            var obj = {
                telephone: telephone,
                remark: remark,
                toStudentId: toStudentId
            }
            tshowPost("/company/saveAddTeacher", obj, function(res){
                if (res.success) {
                    createPromptBox("报名成功");
                }
            });
        });
        
        //修改和完善信息的跳转事件
        $('.phg-h-useratt-button-edit,.phg-h-useratt-button-perfect').on('click', function(){
            $('<a target="_blank" href="/user/setup"></a>').get(0).click();
        });
        $(document).delegate('.phg-h-useratt-attent', 'click', function(){
            var $me = $(this);
            var toStudentId = $me.attr('data-id');
            tshowPost("/student/saveAttention", {
                toStudentId: toStudentId
            }, function(res){
                $me.addClass('phg-h-useratt-attented').removeClass('phg-h-useratt-attent');
                $me.text('已关注');
            });
        });
        $(document).delegate('.phg-h-useratt-attented', 'click', function(){
            var $me = $(this);
            var toStudentId = $me.attr('data-id');
            tshowPost("/student/deleteAttention", {
                toStudentId: toStudentId
            }, function(res){
                $me.addClass('phg-h-useratt-attent').removeClass('phg-h-useratt-attented');
                $me.text('关注');
            });
        });
    }
    
    //已报名列表渲染
    function createYbmStudents(students, $me){
        var box = $me.parent().next().find(">ul");
        var html = "";
        for (var i = 0; i < students.length; i++) {
            var obj = students[i];
            html += "<li><a style='margin-top: 0px;display: inline;padding: 0px;' target='_blank' href ='/student/home/" + obj.invitationId + "'><img src=\"" + obj.headIco + "\"> <span class=\"name\"></a>" + obj.realName + "·" + obj.year + "·" + obj.schoolName + "</span>" +
            "										<span class=\"phone\">" +
            (obj.telephone != null ? obj.telephone : '') +
            "</span>" +
            "</li>";
        }
        box.html(html);
    }
    
    function loadCourseList($me){
        if ($me.data("flag") == false) {
            return;
        }
        var studentId = $("#gtx-but").attr("data-id");
        var page = $me.data("page");
        tshowPost("/company/findCourse/list", {
            pageNo: page.pageNo,
            pageSize: page.pageSize,
            studentId: studentId
        }, function(res){
            //console.log(res);
            if (res.page.result.length == 0) {
                $me.data("flag") == false;
                return;
            }
            createCourseList(res.page.result);
            $me.data({
                "course": res
            });
        })
        page.pageNo++;
    }
    
    function createCourseList(result){
        var box = $("#pencrb>ul");
        var html = ""
        for (var i = 0; i < result.length; i++) {
            var obj = result[i];
            html += "<li data-id='" + obj.courseId + "' class=\"phg-enroll-nav-c-right-b-l\">" +
            "							<div class=\"Course-box\">" +
            "								<h4>更新时间：" +
            obj.createTime +
            "</h4>" +
            "								<h4>上课地点：" +
            obj.place +
            "</h4>" +
            "								<h4>联系方式：" +
            obj.telephone +
            "</h4>" +
            "								<div class=\"Course-box-c\">" +
            "									<h4>课程详情：</h4>" +
            "									<p>" +
            obj.content +
            "</p>" +
            "								</div>" +
            "								<a data-id='" +
            obj.courseId +
            "' class=\"wybm en\">我要参加（" +
            obj.count +
            "）</a> <a class=\"en\">剩余名额(" +
            (obj.countLimit - obj.count) +
            ")</a>" +
            "							</div>" +
            "							<div class=\"Course-box-user\" style=\"display:none\">" +
            "								<span class=\"opticrt\"></span>" +
            "								<button class='Course-box-user-bm " +
            (obj.isbm != null ? 'Course-box-user-bmed' : '') +
            "' data-id='" +
            obj.courseId +
            "'>" +
            (obj.isbm != null ? '已参加' : '参加') +
            "</button>" +
            "								<ul>" +
            "								</ul>" +
            "								<a class='course-shoqi'>收起︿</a>" +
            "							</div>" +
            "</li>"
        }
        box.html(html);
        
    }
    
    function initStudentHomeData(){
        $(".phg .phg-h-usersx ul li").each(function(){
            $(this).data({
                page: {
                    pageNo: 1,
                    pageSize: 10
                },
                flag: true
            });
        });
        $(".phg .phg-opu-x-b").data({
            page: {
                pageNo: 1,
                pageSize: 10
            }
        });
        $(".study-button-grx").data({
            page: {
                pageNo: 1,
                pageSize: 10
            }
        });
    }
    
    /*显示文件夹详情主页面*/
    function showPhgopux(){
        $(".phg .phg-opu-x").show();
        //记录body滚动条位置
        $("body").data({
            "scrollTop": $("body").scrollTop()
        });
        $("body").css({
            overflow: 'hidden'
        });
        $("#head-xlk").show();
        var obj = $(".phg .phg-opu-x-b");
        var pheight = $(window).height() - $(".header").height();
        var height = pheight - obj.prev().height();
        obj.height(height);
    }
    
    function initphgoupxbHeight(){
        var obj = $(".phg .phg-opu-x-b");
        var pheight = $(window).height() - $(".header").height();
        var height = pheight - obj.prev().height();
        obj.parent().height(pheight);
        obj.height(height);
    }
    
    function createWaterFall(products){
        for (var i = 0; i < products.length; i++) {
            var product = products[i];
            var html = "<div class=\"boxw\" data-id=\"" + product.productId + "\">" +
            "            <div class=\"boxin\">" +
            "                <div class=\"boxw-c\">" +
            "                    <div style=\"height:" +
            (Math.floor(product.height / (product.width / 236))) +
            "px\" class=\"tshow-rel\">" +
            (product.versionCount != 1 ? "<div class='product-version-box'><div class='product-version-t'>" + product.versionCount + "</div><div class='product-version-b'></div></div>" : '') +
            showPointsSuggesion(product.titleReplys) +
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
            "\">"+(product.student.headIco==null||product.student.headIco==''?'<span>'+product.student.realName.substring(product.student.realName.length-1)+'</span>':'<img src="'+product.student.headIco+'">')+"</a>" +
            "                    </div>" +
            "                    <div class=\"boxw-n-u-info\">" +
            "                        <div class=\"boxw-n-u-info-n\">" +
            "                            <span><a target='_blank' href='/student/home/" +
            product.student.invitationId +
            "'>" +
            product.student.realName +
            "</span>" +
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
            var index = findMinUlIndex();
            //将请求数据添加到各个ul
            $(".phg .phg-opu-x-b").children("ul").eq(index).append($(html).data({
                "product": product
            }));
        }
    }
    
    function findMinUlIndex(){
        var oParent = $(".phg .phg-opu-x-b");
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
    
    function initUl(){
        var dw = $(window).width();
        var num = dw / 256 == 0 ? 1 : dw / 256;
        num = Math.floor(num);
        var ulHtml = "";
        for (var i = 0; i < num; i++) {
            ulHtml += "<ul></ul>";
        }
        $(".phg .phg-opu-x-b").html(ulHtml);
        $(".phg .phg-opu-x-b").css("padding-left", Math.floor((dw - num * 256) / 2))//对于子元素是浮动的，可以设置padding调整居中
    }
    
    function initHomeEvent(){
        window.onscroll = function(){
            if (checkscrollside()) {
                var bot = $(".phg .tshwo-bor-bot");
                var type = bot.attr("data-type");
                if (type == "attention" || type == "student") {
                    if (bot.data("flag") == false) 
                        return;
                    var activeDom = $(".phg .phg-b-list").filter(":visible");
                    var studentId = $("#phg-h-usern").attr("data-id");
                    bot.data().page.pageNo++;
                    var obj = {
                        studentId: studentId,
                        type: type,
                        pageNo: bot.data().page.pageNo,
                        pageSize: bot.data().page.pageSize
                    };
                    $.post("/student/home/student/list", obj, function(res){
                        var students = JSON.parse(res.map.students);
                        //console.log(students);
                        if (students.length == 0) {
                            bot.data().flag = false;
                            //console.log("无更多数据");
                            return;
                        }
                        var html = createStudentsHtml(students);
                        activeDom.find(".phg-b-att").children("ul").append(html);
                    })
                }
            }
        };
        
        //滚动加载作品列表
        $(".phg-opu-x-b").scroll(function(){
            if (!isMoreData($(".phg .phg-opu-x-b"))) {
                return;
            }
            if (checkscrollside('.phg-opu-x-b')) {
                var page = $(".phg .phg-opu-x-b").data("page");
                page.pageNo++;
                var id = $('.phg .phg-opu-x-h span').eq(0).attr("data-id");
                var type = $('.phg .phg-h-usersx .tshwo-bor-bot').attr("data-type");
                var studentId = $("#phg-h-usern").attr("data-id");
                var obj = {
                    studentId: studentId,
                    type: type,
                    labelId: id,
                    pageNo: page.pageNo,
                    pageSize: page.pageSize
                };
                $.post("/student/home/product/list", obj, function(res){
                    //console.log(res);
                    if (JSON.parse(res.map.products).length < page.pageSize) {
                        $(".phg .phg-opu-x-b").data().flag = false;
                    }
                    var products = JSON.parse(res.map.products);
                    createWaterFall(products);
                })
                
            }
        })
    }
    
    function createStudentsHtml(students){
        var html = "";
        for (var i = 0; i < students.length; i++) {
            var obj = students[i];
            html += "<li class=\"phg-b-att-list\" data-id = '" + obj.studentId + "' data-invitation='" + obj.invitationId + "'> " +
            "							<div class=\"ubc\"> " +
            "								<div class=\"ubc-h\"> " +
            "									<ul> " +
            craeteStudentsLi(obj.studentLabels) +
            "									</ul> " +
            "								</div> " +
            "								<div class=\"ubc-b\"> " +
            "									<a href=\"#\">" +
            ((!obj.headIco || obj.headIco == "" || obj.headIco.trim() == "") ? '<div class="ubc-b-img">' + obj.realName.substring(obj.realName.length - 1) + '</div>' : '<img src="' + obj.headIco + '">') +
            "</a> " +
            "									<p data-id='" +
            obj.studentId +
            "'>" +
            obj.realName +
            "</p> " +
            (obj.isAttention == 'Y' ? '<button class="attentioned">已关注</button>' : '<button>关注</button>') +
            "								</div> " +
            "							</div> " +
            "</li>";
        }
        return html;
    }
    
    function craeteStudentsLi(studentLabels){
        if (studentLabels.length > 0 && studentLabels.length <= 2) {
            return "<li style='height:100%;width:100%'><img src='" + studentLabels[0].backgroundImage + "'> <span style='bottom:43%' data-id = '" + studentLabels[0].label.labelId + "'><a>" + studentLabels[0].label.labelName + "</a></span>";
        }
        else 
            if (studentLabels.length == 3) {
                var nature = getNaturalWidthSrc(studentLabels[0].backgroundImage);
                //console.log(nature.naturalWidth);
                //console.log(nature.naturalHeight);
                return "<li style='width:100%;'><img " + (nature.naturalWidth < nature.naturalHeight ? 'class="homeperimgrot"' : '') + " src='" + studentLabels[0].backgroundImage + "'><img src=\"/1.jpg\"> <span data-id = '" + studentLabels[0].label.labelId + "'><a>" + studentLabels[0].label.labelName + "</a></span> " +
                "</li> " +
                "<li><img src='" +
                studentLabels[1].backgroundImage +
                "'> <span data-id = '" +
                studentLabels[1].label.labelId +
                "'><a>" +
                studentLabels[1].label.labelName +
                "</a></span> " +
                "</li> " +
                "<li><img src='" +
                studentLabels[2].backgroundImage +
                "'> <span data-id = '" +
                studentLabels[2].label.labelId +
                "'><a>" +
                studentLabels[2].label.labelName +
                "</a></span> " +
                "</li> ";
            }
            else 
                if (studentLabels.length >= 4) {
                    return "<li><img src='" + studentLabels[0].backgroundImage + "'> <span data-id = '" + studentLabels[0].label.labelId + "'><a>" + studentLabels[0].label.labelName + "</a></span> " +
                    "</li> " +
                    "<li><img src='" +
                    studentLabels[1].backgroundImage +
                    "'> <span data-id = '" +
                    studentLabels[1].label.labelId +
                    "'><a>" +
                    studentLabels[1].label.labelName +
                    "</a></span> " +
                    "</li> " +
                    "<li><img src='" +
                    studentLabels[2].backgroundImage +
                    "'> <span data-id = '" +
                    studentLabels[2].label.labelId +
                    "'><a>" +
                    studentLabels[2].label.labelName +
                    "</a></span> " +
                    "</li> " +
                    "<li><img src='" +
                    studentLabels[3].backgroundImage +
                    "'> <span data-id = '" +
                    studentLabels[3].label.labelId +
                    "'><a>" +
                    studentLabels[3].label.labelName +
                    "</a></span> " +
                    "</li> "
                }
        return "无图";
    }
    
    function createNrPersonListHtml(nrPersonList){
        var html = "<div class='point-suggest-nrs'><ul class='point-suggest-nrs-ul'>";
        for (var i = 0; i < nrPersonList.length; i++) {
            var nrPerson = nrPersonList[i];
            html += "<li class='point-suggest-nrs-li'><div class='point-suggest-nrs-li-mai'><div class='point-suggest-nrs-li-title'><span>" + nrPerson.realName + "</span>提出了意见</div>" +
            "<div class='point-suggest-nrs-li-content'>" +
            nrPerson.notationContent +
            "</div>" +
            "<div class='point-suggest-nrs-li-time'><span>" +
            getDateInterval(nrPerson.nrCreateTime) +
            "</span></div>" +
            "</div></li>"
        }
        html += "</ul><div class='tshow-dir-l'></div>";
        html += "</div>";
        return html;
    }
    
    function showPointsSuggesion(titleReplys){
        if (!titleReplys) 
            return "";
        var pointHtml = "";
        for (var i = 0; i < titleReplys.length; i++) {
            var point = titleReplys[i];
            var left = point.start * 100 + '%';
            var top = point.end * 100 + '%';
            if (!point.start) 
                return;
            var b = point.replyStudent.studentId == ($(document).data("student") ? $(document).data("student").studentId : '')//判斷是否是當前用戶
            if (b) {
                pointHtml += "<div class=\"mineP\" style='top: " + top + ";left: " + left + "'> " +
                "<div class='reply-point'></div>" +
                "	<div class=\"point-title\">" +
                "<div class='point-pike'></div>" +
                "<span title='" +
                point.titleName +
                "参与(" +
                point.nrPersonList.length +
                ")'>" +
                "		<div class='point-title-name' ><a>" +
                point.titleName +
                "</a></div><div class='point-title-nrcount'><a>参与(" +
                point.nrPersonList.length +
                ")</a></div> " +
                "	</span></div> " +
                createNrPersonListHtml(point.nrPersonList) +
                "</div>";
            }
            else {
                pointHtml += "<div class=\"box\" style='top: " + top + ";left: " + left + "'> " +
                "<div class='reply-point'></div>" +
                "	<div class=\"point-title\">" +
                "<div class='point-pike'></div>" +
                "<span title='" +
                point.titleName +
                "参与(" +
                point.nrPersonList.length +
                ")'>" +
                "		<div class='point-title-name' ><a>" +
                point.titleName +
                "</a></div><div class='point-title-nrcount'><a>参与(" +
                point.nrPersonList.length +
                ")</a></div> " +
                "	</span></div> " +
                createNrPersonListHtml(point.nrPersonList) +
                "</div>";
            }
        }
        return pointHtml;
    }
    
})(window, document, $);


