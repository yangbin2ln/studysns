function initLabel(labelId, pointId, schoolId){
    initLabelData();
    initLoad(labelId, pointId, schoolId);
    //初始化高度
    initLabelHeight();
    //初始化事件
    initLabelEvent();
    initUl2();
}

window.onresize = function(){
    initLabelHeight();
}

window.onscroll = function(){
    if (checkscrollside()) {
        scrollLoad();
    }
}

function initUl2(){
    var dw = $(window).width();
    var num = dw / 256 == 0 ? 1 : dw / 256;
    num = Math.floor(num);
    $(".zlc-ul").data({
        num: num
    });
    var ulHtml = "";
    $(".zlc-ul").html("");
    for (var i = 0; i < num; i++) {
        ulHtml += "<ul></ul>";
    }
    $(".zlc-ul").html(ulHtml);
    dw = $(window).width();
    $(".zlc-ul").css("padding-left", Math.floor((dw - num * 256) / 2))//对于子元素是浮动的，可以设置padding调整居中
}

function initLabelData(){
    //内容标签
    $(".zl-table").data({
        page: {
            pageNo: 1,
            pageSize: 10
        }
    });
    //达人
    $(".master-pul").data({
        page: {
            pageNo: 1,
            pageSize: 10
        }
    });
    //作品
    $(".zlc").data({
        page: {
            pageNo: 1,
            pageSize: 10
        },
        flag: true
    });
    //普通用户
    $(".zl-r").data({
        page: {
            pageNo: 1,
            pageSize: 10
        },
        flag: true
    });
}

function initLoad(labelId, pointId, schoolId){
    var obj = {
        labelId: labelId,
        pointId: pointId,
        schoolId: schoolId,
        pageNo: 1,
        pageSize: 10
    }
    $tsBlockUI({
        message: "<img src='/img/123321.gif'>",
        css: {
            width: '50px',
            height: '50px',
            top: '47%',
            left: '47%',
            'border-radius': '50%',
            'background': 'none',
            border: 'none'
        
        },
        showOverlay: false
    });
    $.post("/school/initSchoolLabelHtml", obj, function(res){
        $.unblockUI();
        //console.log(res);
        //加载points div
        labelRefresh(res);
    })
}


function initLabelHeight(){
    $("#zl").height($(window).height());
}


function initLabelEvent(){
    //内容标签翻页
    $(".points-prev").on("click", function(){
        //判断是否处于动画状态
        $(".zl-table-k ul").stop(true, true);
        //判断是否到达边界
        var zltkulWidth = $(".zl-table-k ul").width();
        var zltkulLeft = parseInt($(".zl-table-k ul").css("left"));
        //console.log(zltkulWidth)
        //console.log(zltkulLeft)
        if (zltkulLeft >= 0) {
            $(".zl-table-k ul").animate({
                left: '0px'
            }, 1500);
            return;
        }
        //判断剩余宽度是否有一屏
        if (-zltkulLeft < $(".zl-table-k").width()) {//不足一屏
            $(".zl-table-k ul").animate({
                left: '0px'
            }, 1500);
        }
        else {
            $(".zl-table-k ul").animate({
                left: $(".zl-table-k").width() + parseInt($(".zl-table-k ul").css("left"))
            }, 1500);
        }
    })
    
    $(".points-next").on("click", function(){
        loadPoints();
        //判断是否处于动画状态
        $(".zl-table-k ul").stop(true, true)
        //判断是否到达边界
        var zltkulWidth = $(".zl-table-k ul").width();
        var zltkulLeft = parseInt($(".zl-table-k ul").css("left"));
        //console.log(zltkulWidth)
        //console.log(zltkulLeft)
        if (-zltkulLeft + $(".zl-table-k").width() >= zltkulWidth) {
            return;
        }
        //判断剩余宽度是否有一屏
        if (zltkulWidth + zltkulLeft - $(".zl-table-k").width() < $(".zl-table-k").width()) {//不足一屏
            $(".zl-table-k ul").animate({
                left: -(zltkulWidth + zltkulLeft - $(".zl-table-k").width()) + parseInt($(".zl-table-k ul").css("left"))
            }, 1500);
        }
        else {
            $(".zl-table-k ul").animate({
                left: -$(".zl-table-k").width() + parseInt($(".zl-table-k ul").css("left"))
            }, 1500);
        }
        
    })
    
    
    //达人翻页
    $(".students-prev").on("click", function(){
        //判断是否处于动画状态
        $(".master-pul>ul").stop(true, true);
        //判断是否到达边界
        var zltkulWidth = $(".master-pul .master-pul-ul").width();
        var zltkulLeft = parseInt($(".master-pul .master-pul-ul").css("left"));
        //console.log(zltkulWidth)
        //console.log(zltkulLeft)
        if (zltkulLeft >= 0) {
            $(".master-pul .master-pul-ul").animate({
                left: '0px'
            }, 1500);
            return;
        }
        //判断剩余宽度是否有一屏
        if (-zltkulLeft < $(".master-pul").width()) {//不足一屏
            $(".master-pul .master-pul-ul").animate({
                left: '0px'
            }, 1500);
        }
        else {
            $(".master-pul .master-pul-ul").animate({
                left: $(".master-pul").width() + parseInt($(".master-pul .master-pul-ul").css("left"))
            }, 1500);
        }
    });
    
    $(".students-next").on("click", function(){
        loadStudents();
        //判断是否处于动画状态
        $(".master-pul .master-pul-ul").stop(true, true)
        //判断是否到达边界
        var zltkulWidth = $(".master-pul .master-pul-ul").width();
        var zltkulLeft = parseInt($(".master-pul .master-pul-ul").css("left"));
        //console.log(zltkulWidth)
        //console.log(zltkulLeft)
        if (-zltkulLeft + $(".master-pul").width() >= zltkulWidth) {
            return;
        }
        //判断剩余宽度是否有一屏
        if (zltkulWidth + zltkulLeft - $(".master-pul").width() < $(".master-pul").width()) {//不足一屏
            $(".master-pul .master-pul-ul").animate({
                left: -(zltkulWidth + zltkulLeft - $(".master-pul").width()) + parseInt($(".master-pul .master-pul-ul").css("left"))
            }, 1500);
        }
        else {
            $(".master-pul .master-pul-ul").animate({
                left: -$(".master-pul").width() + parseInt($(".master-pul .master-pul-ul").css("left"))
            }, 1500);
        }
    });
    
    /*start 代理事件*/
    //达人box事件
    $(".zl-r").delegate(".ubc-h", "click", function(){
        var box = $(this).parent().parent();
        var invitationId = box.attr("data-invitation");
        var href = "/student/home/" + invitationId;
        var a = $("<a target='_blank'></a>");
        a.attr({
            href: href
        })
        //console.log(a)
        a[0].click();
    })
    
    
    /* $(".qhschool").on("click", function(){
     $("#school-ql-m").toggle();
     
     });
     //选择学校
     $(".header-c").delegate("#school-ql-m ul li", "click", function(){
     var $me = $(this);
     var schoolId = $me.attr("data-id");
     if (schoolId == $(".header-c-sc span:eq(0)").attr("data-id")) {
     $("#school-ql-m").toggle();
     return;
     }
     var schoolName = $me.text().trim();
     var labelId = $(".header-c-la").attr("data-id");
     var labelName = $(".header-c-la").text().trim();
     var pointId = $(".header-c-po").attr("data-id");
     var pointName = $(".header-c-po").text().trim();
     //console.log(schoolId)
     //console.log(labelId)
     //console.log(pointId)
     var obj = {
     schoolId: schoolId,
     labelId: labelId,
     pointId: pointId
     }
     $tsBlockUI({
     message: "<img src='/img/123321.gif'>",
     css: {
     width: '50px',
     height: '50px',
     top: '47%',
     left: '47%',
     'border-radius': '50%',
     'background': 'none',
     border: 'none'
     
     },
     showOverlay: false
     });
     $.post("/label/initHtml", obj, function(res){
     $.unblockUI();
     //console.log(res);
     if (res.points.length == 0) {
     if (pointName == "") {
     createPromptBox("此学校" + labelName + "标签尚未开通", {
     'max-width': '170px',
     padding: '5px',
     left: '0%',
     top: '100%',
     'line-height': '20px',
     position: 'absolute'
     }, $me)
     }
     else {
     createPromptBox("此学校" + labelName + "的" + pointName + "标签尚未开通", {
     'max-width': '170px',
     padding: '5px',
     left: '0%',
     top: '100%',
     'line-height': '20px',
     position: 'absolute'
     }, $me)
     }
     return;
     }
     $("#school-ql-m").toggle();
     //刷新页面
     $(".header-c-sc span:eq(0)").text("(" + schoolName + ")");
     $(".header-c-sc").attr({
     "data-id": schoolId
     });
     labelRefresh(res);
     });
     })
     
     //搜索学校
     $(".school-ql-m-s").on("click", findSchools);
     $(".school-ql-m-s").prev().on("keydown", function(){
     if (event.keyCode == 13) {
     findSchools();
     }
     }); */
    //打开作品详情
    $(".zlc-ul").delegate(".boxw-c", "click", openProductInfo);
    
    //内容标签事件
    $(".zl-table-k").delegate("li", "click", function(){
        var $me = $(this);
        var headPoint = $(".header-c-po");
        var span = $me.find("span").eq(0);
        var pointId = span.attr("data-id");
        if (pointId == $(".header-c-po").attr("data-id")) {
            return;
        }
        $(".header-c-po-s").remove();
        var pointName = span.text().trim();
        headPoint.attr({
            "data-id": pointId
        });
        headPoint.text(pointName);
        var headPointS = "<span class='header-c-po-s'><span class='header-c-po-l'>" + pointName + "</span><span class='la-point-close'>X</span></span>";
        headPoint.after(headPointS);
        var schoolId = $(".header-c-sc").attr("data-id");
        var labelId = $(".header-c-la").attr("data-id");
        var obj = {
            schoolId: schoolId,
            labelId: labelId,
            pointId: pointId
        }
        $tsBlockUI({
            message: "<img src='/img/123321.gif'>",
            css: {
                width: '50px',
                height: '50px',
                top: '47%',
                left: '47%',
                'border-radius': '50%',
                'background': 'none',
                border: 'none'
            
            },
            showOverlay: false
        });
        $.post("/school/initSchoolLabelHtml", obj, function(res){
            $.unblockUI();
            labelRefresh(res)
        });
    });
    
    //关闭标签事件
    $(".header-c").delegate(".la-point-close", "click", function(){
        $(this).parent().remove();
        $(".header-c-po").attr({
            "data-id": ""
        });
        $(".header-c-po").text("");
        var schoolId = $(".header-c-sc").attr("data-id");
        var pointId = $(".header-c-po").attr("data-id");
        var labelId = $(".header-c-la").attr("data-id");
        var obj = {
            schoolId: schoolId,
            labelId: labelId,
            pointId: pointId
        }
        $tsBlockUI({
            message: "<img src='/img/123321.gif'>",
            css: {
                width: '50px',
                height: '50px',
                top: '47%',
                left: '47%',
                'border-radius': '50%',
                'background': 'none',
                border: 'none'
            
            },
            showOverlay: false
        });
        $.post("/school/initSchoolLabelHtml", obj, function(res){
            $.unblockUI();
            labelRefresh(res)
        });
    });
    
    //最新最热事件
    $(".zlc-nav>span").on("click", function(){
        var $me = $(this);
        if ($me.hasClass("zlc-nav-black")) {
            return;
        }
        else {
            $me.addClass("zlc-nav-black").siblings().removeClass("zlc-nav-black");
            loadProducts("qh");
        }
    });
    
    //普通用户主页链接事件
    $(".zx-b-list-user").delegate(".user-box", "click", function(){
        var $me = $(this);
        var invitationId = $me.attr("data-invitationId");
        var href = "/student/home/" + invitationId;
        $("<a target='_Blank' href='" + href + "'></a>").get(0).click();
    });
    
    
    /*end  代理事件*/
    //中间导航栏事件
    $(".zx-table-nav li").on("click", function(){
        var $me = $(this);
        $me.addClass("zx-b-nav-col").siblings().removeClass("zx-b-nav-col");
        var val = $me.attr("data-val");
        if (val == 'product') {
            $(".zl-r").hide();
            $(".zlc").show();
        }
        else 
            if (val == 'student') {
                $(".zl-r").show();
                $(".zlc").hide();
            }
    });
    //回到学校主页事件
    $(".header-c-sc").on("click", function(){
        var schoolId = $(this).attr("data-id");
        var href = "/school/home/" + schoolId;
        $("<a target='view_window' href='" + href + "'></a>").get(0).click();
    });
}

function scrollLoad(){
    var type = $(".zx-table-nav .zx-b-nav-col").attr("data-val");
    if (type == "product") {
        loadProducts();
    }
    else 
        if (type == 'student') {
            loadGenerals();
        }
    
}

/*校友列表*/
function createGeneralsHtml(students){
    var box = $(".zx-b-list-user>ul")
    var html = '';
    for (var i = 0; i < students.length; i++) {
        var student = students[i];
        html += "<li> " +
        "							<div class=\"user-box\"  data-invitationId='" +
        student.invitationId +
        "'> " +
        "								<div class=\"user-box-pic\"> " +
        "									" +
        (student.headIco ? '<img src="' + student.headIco + '">' : '<span class="headoco-100 ">' + student.realName.substring(student.realName.length - 1) + '</span>') +
        "								</div> " +
        "								<div class=\"user-box-a\"> " +
        "									<p>" +
        student.realName +
        "·@" +
        student.invitationId +
        "</p> " +
        "									<span>" +
        student.jobLabel.labelName +
        "·" +
        student.productCount +
        "件作品</span> " +
        "								</div> " +
        "							</div> " +
        "</li>"
    }
    box.append(html);
}


function findSchools(){
    var key = $(".school-ql-m-s").prev().val().trim();
    if (key == "") {
        var labelId = $(".header-c-la").attr("data-id");
        var pointId = $(".header-c-po").attr("data-id");
        $.post("/label/schools", {
            labelId: labelId,
            pointId: pointId
        }, function(res){
            //console.log(res);
            createSchoolsHtml(res.schools);
        })
    }
    else {
        $.post("/school/key", {
            key: key
        }, function(res){
            //console.log(res);
            createSchoolsHtml(res.map.schools);
        });
    }
    
}

function labelRefresh(res){
    initLabelData();
    $(".zl-table-k ul").width("0px");
    $(".zl-table-k ul").css({
        left: "0px"
    });
    $(".master-pul ul").width("0px");
    $(".master-pul ul").css({
        left: "0px"
    });
    $(".zl-table-k ul").empty();
    $(".master ul").empty();
    $(".zlc .zlc-ul").children("ul").empty();
    $(".zx-b-list-user").children("ul").empty();
    initAllCount(res);
    createPointsHtml(res.points);
    createStudentsHtml(JSON.parse(res.students));
    createGeneralsHtml(JSON.parse(res.generals));
    createProductsHtml(JSON.parse(res.products), ".zlc .zlc-ul");
    /*createSchoolsHtml(res.schools);*/
}

/*人数作品数*/
function initAllCount(res){
    $(".master .stu-count").html("达人数(" + res.studentsCount + ")总人数(" + parseInt(res.generalsCount + res.studentsCount) + ")")
    $(".zlc-nav .pro-count").html("作品数(" + res.productsCount + ")")
}

function loadPoints(){
    var schoolId = $(".header-c-sc").attr("data-id");
    var labelId = $(".header-c-la").attr("data-id");
    var pointId = $(".header-c-po").attr("data-id");
    var obj = {
        labelId: labelId,
        pointId: pointId,
        schoolId: schoolId,
        pageNo: ++$(".zl-table").data("page").pageNo,
        pageSize: 10
    }
    $.ajax({
        url: "/label/points",
        type: "post",
        data: obj,
        async: false,
        success: function(res){
            //console.log(res);
            //加载points div
            createPointsHtml(res.points);
        }
    });
}

function loadGenerals(){
    if (!$(".zl-r").data("flag")) {
        createPromptBox("加载全部完成", {
            top: '95%'
        })
        return;
    }
    var schoolId = $(".header-c-sc").attr("data-id");
    var labelId = $(".header-c-la").attr("data-id");
    var pointId = $(".header-c-po").attr("data-id");
    var obj = {
        labelId: labelId,
        pointId: pointId,
        schoolId: schoolId,
        pageNo: ++$(".zl-r").data("page").pageNo,
        pageSize: 10,
        stType: 'time',
        filter: 'general'
    }
    $.ajax({
        url: "/school/students",
        type: "post",
        data: obj,
        async: false,
        success: function(res){
            var students = JSON.parse(res.students);
            if (students.length == 0) {
                $(".zl-r").data({
                    flag: false
                });
                return;
            }
            //console.log(res);
            //加载points div
            createGeneralsHtml(students);
        }
    });
}

function loadStudents(){
    var schoolId = $(".header-c-sc").attr("data-id");
    var labelId = $(".header-c-la").attr("data-id");
    var pointId = $(".header-c-po").attr("data-id");
    var obj = {
        labelId: labelId,
        pointId: pointId,
        schoolId: schoolId,
        pageNo: ++$(".master-pul").data("page").pageNo,
        pageSize: 10
    }
    $.ajax({
        url: "/label/students",
        type: "post",
        data: obj,
        async: false,
        success: function(res){
            //console.log(res);
            //加载points div
            createStudentsHtml(JSON.parse(res.students));
        }
    });
}

function loadProducts(state){
    var schoolId = $(".header-c-sc").attr("data-id");
    var labelId = $(".header-c-la").attr("data-id");
    var pointId = $(".header-c-po").attr("data-id");
    var type = $(".zlc-nav-black").attr("data-val");
    var pageNo;
    if (state == 'qh') {
        $(".zlc .zlc-ul").children("ul").empty();
        $(".zlc").data("page").pageNo = 1;
        $(".zlc").data({
            flag: true
        });
        pageNo = $(".zlc").data("page").pageNo;
        
    }
    else {
        pageNo = ++$(".zlc").data("page").pageNo;
        if (!$(".zlc").data("flag")) {
            createPromptBox("加载全部完成", {
                top: '95%'
            })
            return;
        }
    }
    var obj = {
        labelId: labelId,
        pointId: pointId,
        schoolId: schoolId,
        pageNo: pageNo,
        pageSize: 10,
        type: type
    }
    $tsBlockUI({
        message: "<img src='/img/123321.gif'>",
        css: {
            width: '50px',
            height: '50px',
            top: '47%',
            left: '47%',
            'border-radius': '50%',
            'background': 'none',
            border: 'none'
        
        },
        showOverlay: false
    });
    $.ajax({
        url: "/label/products",
        type: "post",
        data: obj,
        async: false,
        success: function(res){
            $.unblockUI();
            //console.log(res);
            //加载points div
            var products = JSON.parse(res.products);
            if (products.length == 0) {
                $(".zlc").data({
                    "flag": false
                });
            }
            createProductsHtml(products, ".zlc .zlc-ul");
        }
    });
}


/*加载达人 div*/
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
        "									<a target='_blank' href='/student/home/" +
        obj.invitationId +
        "'>" +
        ((!obj.headIco || obj.headIco == "" || obj.headIco.trim() == "") ? '<div class="ubc-b-img">' + obj.realName.substring(obj.realName.length - 1) + '</div>' : '<img src="' + obj.headIco + '" >') +
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
    //计算points主div宽度
    $(".master-pul .master-pul-ul").width($(".master-pul .master-pul-ul").width() + students.length * 256);
    $(".master ul").append(html);
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

/*加载内容标签 div*/
function createPointsHtml(points){
    var html = "";
    for (var i = 0; i < points.length; i++) {
        var point = points[i]
        html += "<li><a  > <img src='" + point.backImaS + "'> <span data-id = '" + point.pointId + "'>" + point.pointName + "</span> </a></li>";
    }
    //计算points主div宽度
    $(".zl-table-k ul").width($(".zl-table-k ul").width() + points.length * 128);
    $(".zl-table-k ul").append(html);
}

/*加载学校列表*/
function createSchoolsHtml(schools){
    var html = "<li data-id=''>全部</li>";
    for (var i = 0; i < schools.length; i++) {
        var school = schools[i];
        html += "<li data-id='" + school.schoolId + "'>" + school.schoolName + "</li>"
    }
    $("#school-ql-m ul").html(html);
}

function initUl(){
    var dw = $(window).width();
    var num = dw / 320 == 0 ? 1 : dw / 320;
    num = Math.floor(num);
    $(".zlc-ul").data({
        num: num
    });
    var ulHtml = "";
    $(".zlc-ul").html("");
    for (var i = 0; i < num; i++) {
        ulHtml += "<ul></ul>";
    }
    $(".zlc-ul").html(ulHtml);
    dw = $(window).width();
    $(".zlc-ul").css("padding-left", Math.floor((dw - num * 320) / 2))//对于子元素是浮动的，可以设置padding调整居中
}
