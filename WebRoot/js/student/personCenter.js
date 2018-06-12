(function(window, $){
    var initPersonCenter = function(){
        makePcUser("all", 1, false);
        initPerCenEvent();
        initPersonData();
    }
    window.initPersonCenter = initPersonCenter;
    window.onresize = function(){
        initHeight();
    }
    window.onscroll = function(){
        if (checkscrollside()) {
            var pageNo = ++$(".Personal-Center-nav").data("page").pageNo;
            var type = $(".Personal-Center-nav").data("type");
            makePcUser(type, pageNo, false);
        }
    }
    function makePcUser(type, pageNo, override){
		 if(!isMoreData($("#Personal-Center-c-ul"))){
					return;
				}
        var obj = {
            type: type,
            pageNo: pageNo,
            pageSize: 10
        }
        $tsBlockUI({
            message: "<img src='/img/123321.gif'>",
            css: {
                width: '50px',
                height: '50px',
                top: '90%',
                'border-radius': '50%',
                'background': 'none',
                border: 'none'
            
            },
            showOverlay: false
        });
        tshowPost("/student/center/list", obj, function(res){
            $.unblockUI();
				var list = JSON.parse(res.map.products);
				if(list.length < obj.pageSize){
					$("#Personal-Center-c-ul").data({'flag':false});
				}
            makePcUserHtml(list, override);
        })
    }
    
    function makePcUserHtml(list, override){
        var pccu = $("#Personal-Center-c-ul");
        if (override) {
            pccu.empty();
        }
        //console.log(list);
        for (var i = 0; i < list.length; i++) {
            var obj = list[i];
            var html = "<li data-id='" + obj.productId + "'>" +
            "					<div class=\"pc-user\">" +
            "						<div class=\"pc-user-pic\">" +
            "							<a target='_blank' href='/student/home/" +
            obj.student.invitationId +
            "'> " +
            createHeadIcoPc(obj.student) +
            " </a>" +
            "						</div>" +
            "						<div class=\"pc-user-mes\">" +
            "							<div class=\"pc-user-mes-un\">" +
            "								<span> <a target='_blank' href='/student/home/" +
            obj.student.invitationId +
            "' data-id='" +
            obj.student.studentId +
            "'>" +
            obj.student.realName +
            "</a> </span> <span>·</span> <span> <a target='_blank' href='/school/home/" +
            obj.student.school.schoolId +
            "'  " +
            "									>" +
            obj.student.school.name +
            "</a> </span> <span>·</span> <span>" +
            obj.student.year.substring(0, 4) +
            "</span> <span>·</span>" +
            "								<span>" +
            getDateInterval(obj.createTime, "/") +
            "</span> <span class=\"sx " +
            (obj.original == 'Y' ? 'att-b-yc' : 'att-b-zz') +
            "\">" +
            "</span>" +
            "							</div>" +
            "							<div class=\"pc-user-mes-zp\">" +
            "								<span><a target='_blank' href='/label/home/" +
            obj.label.labelId +
            "'>" +
            obj.label.labelName +
            "</span></a> <span>" +
            obj.productName +
            "</span>" +
            "							</div>" +
            "							<div class='thsow-cen'><div class=\"pc-user-mes-pic\">" +
            (obj.versionCount != 1 ? "<div class='product-version-box'><div class='product-version-t'>" + obj.versionCount + "</div><div class='product-version-b'></div></div>" : '') +
            "								<a  data-id='" +
            obj.productId +
            "'> <img src=\"" +
            obj.smallContent +
            "\"> </a>";
            for (var m = 0; m < obj.titleReplys.length; m++) {
                var titleReply = obj.titleReplys[m];
                var pointHtml = makePoint(titleReply);
                html += pointHtml;
                
            }
            html += "							</div></div>" +
            "							<div class=\"pc-user-mes-xlabel\">" +
            "								<ul>";
            for (var j = 0; j < obj.points.length; j++) {
                var point = obj.points[j];
                html += "<li data-id='" + point.pointId + "'><a target='_blank' href='/label/home/" + obj.label.labelId + "?pointId=" + point.pointId + "'>" + point.pointName + "</a></li>";
            }
            html += "								</ul>" +
            "							</div>" +
            "							<div class=\"pc-user-mes-e\">" +
            "								<p>" +
            obj.productDisc +
            "</p>" +
            "							</div>" +
            "							<div class=\"pc-user-mes-p\">" +
            "								<ul>" +
            "									<li>#" +
            obj.score +
            "</li>" +
            "									<li><a >" +
            (obj.collections.length == 0 ? '<span data-id="' + obj.productId + '" class=\"collect\">收藏</span>' : '<span data-id="' + obj.productId + '" class=\"collect collected\">已收藏</span>') +
            "  <span>" +
            obj.collectionCount +
            "</span>" +
            "									</a></li>" +
            "									<li><a >" +
            (obj.praises.length == 0 ? ' <span class=\"pc-praise\" data-id="' + obj.productId + '">喜爱</span>' : ' <span data-id="' + obj.productId + '" class=\"pc-praise pc-praised\">已喜爱</span>') +
            "<span>" +
            obj.praiseCount +
            "</span>" +
            "									</a></li>" +
            "									</a>" +
            "</li>";
            var $html = $(html).data({
                product: obj
            });
            pccu.append($html);
        }
        
    }
    
    function makePoint(point){
        var pointHtml = "";
        var left = point.start * 100 + '%';
        var top = point.end * 100 + '%';
        var b = point.replyStudent.studentId == ($(document).data("student") ? $(document).data("student").studentId : '')//判斷是否是當前用戶
        if (b) {
            pointHtml += "<div class=\"mineP\" style='top: " + top + ";left: " + left + "'> " +
            "	<div class=\"point-title\">" +
            "<div class='point-pike'></div>" +
            "<span title='标题：" +
            point.titleName +
            "  总参与(" +
            point.nrPersonListCount +
            ")'>" +
            "		<div class='point-title-name' ><a>" +
            point.titleName +
            "</a></div><div class='point-title-nrcount'><a>#" +
            point.nrPersonListCount +
            "</a></div> " +
            "	</span></div> " +
            "</div>";
        }
        else {
            pointHtml += "<div class=\"box\" style='top: " + top + ";left: " + left + "'> " +
            "	<div class=\"point-title\">"+
			"<div class='point-pike'></div>"+
			"<span title='标题：" +
            point.titleName +
            "  总参与(" +
            point.nrPersonListCount +
            ")'>" +
            "		<div class='point-title-name' ><a>" +
            point.titleName +
            "</a></div><div class='point-title-nrcount'><a>#" +
            point.nrPersonListCount +
            "</a></div> " +
            "	</span></div> " +
            "</div>";
        }
        return pointHtml;
        //imgParent.append($(pointHtml).data({notationReply:res[i]}));
    }
    
    
    function initPerCenEvent(){
        dailiPerCenc();
        dailiPerCenNav();
    }
    
    /*个人中心内容框代理事件*/
    function dailiPerCenc(){
        $(".Personal-Center-c").delegate(".pc-user-mes-pic", "click", function(){
            openProductInfo("personCenter", this);
        });
    }
    
    function dailiPerCenNav(){
        $(".Personal-Center-nav .center-data-type").on("click", function(){
            var $me = $(this);
            $me.addClass("active-bot").siblings().removeClass('active-bot');
            var page = {
                pageNo: 1,
                pageSize: 10
            }
			$("#Personal-Center-c-ul").data().flag=true;
			$("#Personal-Center-c-ul").data().flagNum=-1;
            var type;
            type = $me.attr("data-type");
            makePcUser(type, 1, true);
            $(".Personal-Center-nav").data({
                page: page,
                type: type
            })
        });
    }
    
    /*默认前台缓存*/
    function initPersonData(){
        var page = {
            pageNo: 1,
            pageSize: 10
        }
        $(".Personal-Center-nav").data({
            page: page,
            type: "all"
        })
    }
    //个人中心喜爱收藏举报
    $(document).delegate(".pc-praise:not(.pc-praised)", "click", function(){
        var $me = $(this);
        var productId = $me.attr("data-id");
        tshowPost("/product/savePraiseProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
        })
        $me.addClass("pc-praised").text("已喜爱");
        $me.next().text(parseInt($me.next().text()) + 1);
    });
    $(document).delegate(".pc-praised", "click", function(){
        var $me = $(this);
        var productId = $me.attr("data-id");
        tshowPost("/product/deletePraiseProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
        })
        $me.removeClass("pc-praised").text("喜爱");
        $me.next().text(parseInt($me.next().text()) - 1);
    });
    $(document).delegate(".collect:not(.collected)", "click", function(){
        var $me = $(this);
        var productId = $me.attr("data-id");
        tshowPost("/product/saveCollectionProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
        })
        $me.addClass("collected").text("已收藏");
        $me.next().text(parseInt($me.next().text()) + 1);
        
    });
    $(document).delegate(".collected", "click", function(){
        var $me = $(this);
        var productId = $me.attr("data-id");
        tshowPost("/product/deleteCollectionProduct", {
            productId: productId
        }, function(res){
            //console.log(res);
        })
        $me.removeClass("collected").text("收藏");
        $me.next().text(parseInt($me.next().text()) - 1);
    });
    
    
})(window, $);

