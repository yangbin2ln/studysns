(function($, window){
    var initSchool = function(schoolId){
        //初始化页面
        initSchoolHtml(schoolId);
        //绑定事件
        schoolInitEvent();
        //初始化前台数据
        initData();
        initUl();
        
        
    }
    window.initSchool = initSchool
    window.onscroll = function(){
        showOrHideScSearch();
        scrollLoad();
    }
    
    
    function initUl(){
        var dw = $(window).width();
        var num = dw / 256 == 0 ? 1 : dw / 256;
        num = Math.floor(num);
        $(".zxc-ul").data({
            num: num
        });
        var ulHtml = "";
        $(".zxc-ul").html("");
        for (var i = 0; i < num; i++) {
            ulHtml += "<ul></ul>";
        }
        $(".zxc-ul").html(ulHtml);
        dw = $(window).width();
        $(".zxc-ul").css("padding-left", Math.floor((dw - num * 256) / 2))//对于子元素是浮动的，可以设置padding调整居中
    }
    
    //初始化前台数据
    function initData(){
        var lis = $(".zx-b-nav li");
        for (var i = 0; i < lis.length; i++) {
            var li = lis.eq(i);
            li.data({
                "page": {
                    pageNo: 1,
                    pageSize: 10
                },
                flag: true
            });
        }
    }
    
    //滚动加载数据
    function scrollLoad(){
        if (checkscrollside()) {
            var box = $(".zx-b-nav li.zx-b-nav-col");
            if (!box.data().flag) {
                createPromptBox("已加载完毕", {
                    top: '90%'
                });
                return;
            }
            $tsBlockUI({
                message: "<img src='/img/123321.gif'>",
                css: {
                    width: '50px',
                    height: '50px',
                    top: '90%',
                    left: '47%',
                    'border-radius': '50%',
                    'background': 'none',
                    border: 'none'
                
                },
                showOverlay: false
            });
            var type = box.attr("data-val");
            var page = box.data().page;
            var boxlist = $(".zx-b-list").children().has(":visible");
            $.post("/school/" + type, {
                schoolId: $(document).data().param.schoolId,
                prType: 'score',
                stType: 'score',
                pageNo: ++page.pageNo,
                pageSize: page.pageSize
            }, function(res){
                $.unblockUI();
                //console.log(res);
                if (type == "students") {
                    var students = JSON.parse(res.students);
                    if (students.length == 0) {
                        box.data().flag = false;
                    }
                    createStudentsHtml(students);
                }
                else 
                    if (type == "labels") {
                        if (res.labels.length == 0) {
                            box.data().flag = false;
                        }
                        createLabelsHtml(res.labels);
                    }
                    else 
                        if (type == "products") {
                            var products = JSON.parse(res.products);
                            if (products.length == 0) {
                                box.data().flag = false;
                            }
                            createProductsHtml(products, ".zxc-ul")
                        }
                
            })
        }
    }
    
    function initSchoolHtml(schoolId){
        $.post("/school/initHtml", {
            schoolId: schoolId
        }, function(res){
            //console.log(res);
            createLabelsHtml(res.labels);
            createStudentsHtml(JSON.parse(res.students));
            createProductsHtml(JSON.parse(res.products), ".zxc-ul");
        });
    }
    
    /*校友列表*/
    function createStudentsHtml(students){
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
    
    /*标签列表*/
    function createLabelsHtml(labels){
        var box = $(".zx-b-list-table>ul");
        var html = "";
        for (var i = 0; i < labels.length; i++) {
            var label = labels[i];
            html += "<li class=\"table-list\" data-id='" + label.labelId + "'> " +
            "							<div class=\"table-box\"> " +
            "								<h4><a data-id='" +
            label.pLabelId +
            "'>" +
            label.pLabelName +
            "</a>-<a data-id ='" +
            label.labelId +
            "'>" +
            label.labelName +
            "</a></h4> " +
            "								<p>人数" +
            label.studentCount +
            "·作品" +
            label.productCount +
            "</p> " +
            "								<div class=\"table-box-h\"> " +
            "									<img " +
            (label.images[0] ? 'src="' + label.images[0].backImaS + '"' : '') +
            "> " +
            "								</div> " +
            "								<div class=\"table-box-b\"> " +
            "									<ul> " +
            "										<li>" +
            (label.images[1] ? '<img src="' + label.images[1].backImaS + '">' : '<div>等你的作品</div>') +
            "										</li> " +
            "										<li>" +
            (label.images[2] ? '<img src="' + label.images[2].backImaS + '">' : '<div>等你的作品</div>') +
            "										</li> " +
            "										<li>" +
            (label.images[3] ? '<img src="' + label.images[3].backImaS + '">' : '<div>等你的作品</div>') +
            "										</li> " +
            "									</ul> " +
            "								</div> " +
            "							</div> " +
            "</li>"
        }
        box.append(html);
    }
    
    /*学校检索框位置监听*/
    function showOrHideScSearch(){
        var btop = $("body").scrollTop();
        var box = $("#sch-m-s-p");
        var box2 = $("#zx-search");
        if (btop >= 490) {
            $("#header").addClass("hea-back-whi");
            $('.header-l .nav').addClass('col-bla');
            $('.sch-m input').addClass('sch-m-bac');
            //右侧按钮
            $('.header-r .added').removeClass('added').addClass('add');
            $('.header-r .seared').removeClass('seared').addClass('sear');
            $('.header-r .mesed').removeClass('mesed').addClass('mes');
        }
        else {
            $("#header").removeClass("hea-back-whi");
            $('.header-l .nav').removeClass('col-bla');
            $('.sch-m input').removeClass('sch-m-bac');
            //右侧按钮
            $('.header-r .add').removeClass('add').addClass('added');
            $('.header-r .sear').removeClass('sear').addClass('seared');
            $('.header-r .mes').removeClass('mes').addClass('mesed');
        }
        if (btop > 385) {
            $('.header-c img').addClass('mar-550');
        }
        else {
            $('.header-c img').removeClass('mar-550');
        }
        if (btop > 225) {
            $(".header-c").show();
        }
        else {
            $(".header-c").hide();
        }
        
        if (btop >= 225 && btop < 389) {
            box.addClass("fix-t60").show().removeClass("zch-finx-bac");
            box2.hide();
            $(".zx-b-nav").removeClass("zx-b-nav-fix");
            box.children("p").show();
            box.find(".school-search-inp").css({
                color: 'white'
            });
        }
        else 
            if (btop >= 389) {
                box.addClass("zch-finx-bac").addClass("fix-t60").children("p").hide();
                $(".zx-b-nav").addClass("zx-b-nav-fix");
                box.find(".school-search-inp").css({
                    color: 'black'
                });
            }
            else {
                box.removeClass("fix-t60").removeClass('zch-finx-bac');
                box.children("p").show();
                $(".zx-b-nav").removeClass("zx-b-nav-fix");
                box.find(".school-search-inp").css({
                    color: 'white'
                });
            }
        
    }
    
    //搜索下拉框页面
    function createSearchHtml(res, key){
        var box = $("#search-list-ul");
        var html = "";
        if (res.students.length == 0 && res.points.length == 0 && res.labels.length == 0) {
            box.html("<li class='none-data-tip'>未找到&nbsp<a>去搜索页面里试试&nbsp" + key + "</a></li>");
            return;
        }
        if (res.students.length > 0) {
            html += "<li><ul><li class='search-list-ul-fir'>用户</li>";
            for (var i = 0; i < res.students.length; i++) {
                var student = res.students[i];
                html += "<li class='search-list-ul-st' data-id='" + student.studentId + "' data-invitationId = '" + student.invitationId + "'>" + student.realName + "<span><img src='" + student.headIco + "'></span></li>";
            }
            html += "</li></ul>";
        }
        
        if (res.points.length > 0 || res.labels.length > 0) {
            html += "<li><ul><li class='search-list-ul-fir'>标签</li>"
            for (var i = 0; i < res.points.length; i++) {
                var point = res.points[i];
                html += "<li class='search-list-ul-po' data-poid='" + point.pointId + "' data-laid='" + point.labelId + "'>" + point.labelName + "-" + point.pointName + "<span><img src='" + point.backImaS + "'></span></li>";
            }
            for (var i = 0; i < res.labels.length; i++) {
                var label = res.labels[i];
                html += "<li class='search-list-ul-la' data-laid='" + label.labelId + "'>" + label.labelName + "<span><img src='" + label.backImaS + "'></span></li>";
            }
            html += "</li></ul>";
        }
        box.html(html);
    }
    
    function schoolInitEvent(){
        //绑定检索框的监听事件
        $(".school-search-inp").on("keyup", function(){
            var schoolId = $(document).data().param.schoolId;
            var key = $("#school-search-inp").val().trim();
            var $me = $(this);
            if (event.keyCode == 13) {
                //console.log($me.val())
                //console.log("post...")
                var box1 = $(".search-list-ul-st").eq(0);
                var box2 = $(".search-list-ul-po").eq(0);
                if (box1.length != 0 || box2.length != 0) {
                    if (box1.length != 0) {
                        box1.click();
                    }
                    else {
                        box2.click();
                    }
                }
            }
            else {
                //console.log($me.val())
                $.post("/search/school", {
                    schoolId: schoolId,
                    key: key
                }, function(res){
                    //console.log(res);
                    //搜索下拉框
                    createSearchHtml(res, key);
                    
                });
            }
        });
        
        //校内检索框焦点事件
        $("#school-search-inp").on("blur", function(){
            setTimeout(function(){
                $("#search-list-ul").hide()
            }, 500);
        });
        
        $("#school-search-inp").on("focus", function(){
            $("#search-list-ul").show()
        });
        
        //中间导航栏切换事件
        $(".zx-b-nav li").on("click", function(){
            var $me = $(this);
            var index = $me.index();
            $me.addClass("zx-b-nav-col").siblings().removeClass("zx-b-nav-col");
            $(".zx-b-list").children().eq(index).show().siblings().hide();
        });
        
        //校友列表个人主页链接事件
        $(".zx-b-list-user").delegate(".user-box", "click", function(){
            var $me = $(this);
            var invitationId = $me.attr("data-invitationId");
            var href = "/student/home/" + invitationId;
            $("<a target='_blank' href='" + href + "'></a>").get(0).click();
        });
        
        //打开作品详情
        $(".zxc-ul").delegate(".boxw-c", "click", openProductInfo);
        
        //检索内容代理事件
        $("#search-list-ul").delegate(".search-list-ul-st", "click", function(){
            var $me = $(this);
            var invitationId = $me.attr("data-invitationid");
            var href = "/student/home/" + invitationId;
            $("<a target='_Blank' href ='" + href + "'></a>").get(0).click();
        });
        $("#search-list-ul").delegate(".search-list-ul-po", "click", function(){
            var $me = $(this);
            var schoolId = $(document).data().param.schoolId;
            var pointId = $me.attr("data-poid");
            var labelId = $me.attr("data-laid");
            var href = "/school/home/" + schoolId + "/" + labelId + "?pointId=" + pointId;
            $("<a target='_blank' href ='" + href + "'></a>").get(0).click();
        });
        $("#search-list-ul").delegate(".search-list-ul-la", "click", function(){
            var schoolId = $(document).data().param.schoolId;
            var $me = $(this);
            var labelId = $me.attr("data-laid");
            var href = "/school/home/" + schoolId + "/" + labelId;
            $("<a target='_blank' href ='" + href + "'></a>").get(0).click();
        });
        //标签主页跳转代理事件
        $(".zx-b-list-table").delegate(".table-list", "click", function(){
            var $me = $(this);
            var labelId = $me.attr("data-id");
            var schoolId = $(document).data().param.schoolId;
            var href = "/school/home/" + schoolId + "/" + labelId;
            $("<a target='_blank' href = '" + href + "'></a>").get(0).click();
        });
        
    }
    
})($, window)

