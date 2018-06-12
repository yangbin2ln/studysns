(function($, window){
    var initFind = function(){
        initHtml();
        initEvent();
    }
    window.initFind = initFind;
    
    window.onscroll = function(){
        if (checkscrollside()) {
            findLabelsFir();
        }
    }
    
    function initEvent(){
        //一级标签下的二级标签更多加载
        $("#labels-fir").delegate(".more", "click", function(){
            var $me = $(this);
            $me.data().page ||
            $me.data({
                page: {
                    pageNo: 1,
                    pageSize: 6
                },
                flag: true
            });
            if (!$me.data().flag) {
                createPromptBox("加载完毕", {
                    top: '21px',
                    left: '6px',
                    position: 'absolute'
                }, $me)
                return;
            }
            var box = $me.parent(".search-view-list-table").prev();
            var labelId = box.attr("data-id");
            $.post("/find/labelChildren", {
                pid: labelId,
                pageNo: ++$me.data().page.pageNo,
                pageSize: $me.data().page.pageSize
            }, function(res){
                if (res.children.length == 0) {
                    $me.data().flag = false;
                    createPromptBox("加载完毕", {
                        top: '21px',
                        left: '6px',
                        position: 'absolute'
                    }, $me)
                    return;
                }
                //console.log(res);
                createLabelsChildren(res.children, $me.prev());
            });
        });
        //最新标签更多加载
        $("#labels-new .more").on("click", function(){
            var $me = $(this);
            $me.data().page ||
            $me.data({
                page: {
                    pageNo: 1,
                    pageSize: 6
                },
                flag: true
            });
            if (!$me.data().flag) {
                createPromptBox("加载完毕", {
                    top: '21px',
                    left: '6px',
                    position: 'absolute'
                }, $me)
                return;
            }
            $.post("/find/labelNew", {
                pageNo: ++$me.data().page.pageNo,
                pageSize: $me.data().page.pageSize
            }, function(res){
                if (res.labelsNew.length == 0) {
                    $me.data().flag = false;
                    createPromptBox("加载完毕", {
                        top: '21px',
                        left: '6px',
                        position: 'absolute'
                    }, $me)
                    return;
                }
                //console.log(res);
                createLabelNewHtml(res.labelsNew);
            });
        });
		
		//二级标签主页跳转事件
		$("#search-view").delegate(".m-box:not(.recommend-po)","click",function(){
			var $me = $(this);
			var labelId = $me.find(".table").attr("data-id");
			var href = "/label/home/"+labelId;
			$("<a target='_blank' href='"+href+"'></a>").get(0).click();
		});
		
		//内容标签主页跳转事件
		$("#search-view").delegate(".recommend-po","click",function(){
			var $me = $(this);
			var labelId = $me.find(".table").attr("data-id");
			var pointId = $me.find(".table").attr("data-poid");
			var href = "/label/home/"+labelId+"?pointId="+pointId;
			$("<a target='_blank' href='"+href+"'></a>").get(0).click();
		});
        
    }
    
    function initHtml(){
        $.post("/find/initHtml", {}, function(res){
            //console.log(res);
            createLabelNewHtml(res.labelsNew);
           // createCommendHtml(res.recommend);
            createLabelFirHtml(res.labelsFir);
        });
    }
    
    function findLabelsFir(){
        var $me = $(this);
        $me.data().page ||
        $me.data({
            page: {
                pageNo: 1,
                pageSize: 6
            },
            flag: true
        });
        if (!$me.data().flag) {
            createPromptBox("加载完毕", {
                top: '90%'
            })
            return;
        }
        $.post("/find/labelsFir", {
            pageNo: ++$me.data().page.pageNo,
            pageSize: $me.data().page.pageSize
        }, function(res){
            if (res.labelsFir.length == 0) {
                $me.data().flag = false;
                createPromptBox("加载完毕", {
                    top: '90%'
                })
                return;
            }
            //console.log(res);
            createLabelFirHtml(res.labelsFir);
        });
    }
    
    //最新二级标签
    function createLabelNewHtml(labelsNew){
        var box = $("#labels-new ul");
        var html = "";
        for (var i = 0; i < labelsNew.length; i++) {
            var obj = labelsNew[i];
            html += "<li>" +
            "						<div class=\"m-box\">" +
            "							<img src='" +
            obj.backImgB +
            "'>" +
            "							<p class=\"table\" data-id ='" +
            obj.labelId +
            "'>" +
            obj.labelName +
            "</p>" +
            "							<p class=\"number\">" +
            "								<span>作品" +
            obj.productCount +
            "</span>" +
            "							</p>" +
            "						</div>" +
            "</li>";
        }
        box.append(html);
    }
    
    //热门推荐（二级标签、内容标签等等）
    function createCommendHtml(recommend){
        var box = $("#recommend ul");
        var html = "";
        for (var i = 0; i < recommend.labels.length; i++) {
            var obj = recommend.labels[i];
            html += "<li>" +
            "						<div class=\"m-box recommend-la\">" +
            "							<img src='" +
            obj.backImgB +
            "'>" +
            "							<p class=\"table\" data-id ='" +
            obj.labelId +
            "'>" +
            obj.labelName +
            "</p>" +
            "							<p class=\"number\">" +
            "								<span>作品" +
            obj.productCount +
            "</span>" +
            "							</p>" +
            "						</div>" +
            "</li>";
        }
        for (var i = 0; i < recommend.points.length; i++) {
            var obj = recommend.points[i];
            html += "<li>" +
            "						<div class=\"m-box recommend-po\">" +
            "							<img src='" +
            obj.backImgB +
            "'>" +
            "<p class=\"table\" data-poid='" +
            obj.pointId +
            "' data-id ='" +
            obj.labelId +
            "'>" +
            obj.labelName +
            obj.pointName +
            "</p>" +
            "							<p class=\"number\">" +
            "								<span>作品" +
            obj.hot +
            "</span>" +
            "							</p>" +
            "						</div>" +
            "</li>";
        }
        box.append(html);
    }
    
    //一级标签以及初始的二级标签
    function createLabelFirHtml(labelsFir){
        var box = $("#labels-fir");
        var html = "";
        for (var i = 0; i < labelsFir.length; i++) {
            var obj = labelsFir[i];
            html += "<h3 data-id='" + obj.labelId + "'>" + obj.labelName + "</h3>" +
            "				<div class=\"search-view-list-table\">" +
            "<ul>";
            for (var j = 0; j < obj.children.length; j++) {
                var child = obj.children[j];
                html += "<li>" +
                "							<div class=\"m-box\">" +
                "								<img src='" +
                child.backImgB +
                "'>" +
                "								<p class=\"table\" data-id='" +
                child.labelId +
                "'>" +
                child.labelName +
                "</p>" +
                "								<p class=\"number\">" +
                "</span> <span>作品" +
                child.productCount +
                "</span>" +
                "								</p>" +
                "</div>" +
                "</li>";
            }
            html += "</ul><a class='more'>显示更多</a></div>";
        }
        
        box.append(html);
    }
    
    //二级标签
    function createLabelsChildren(children, box){
        var html = "";
        for (var j = 0; j < children.length; j++) {
            var child = children[j];
            html += "<li>" +
            "							<div class=\"m-box\">" +
            "								<img src='" +
            child.backImgB +
            "'>" +
            "								<p class=\"table\" data-id='" +
            child.labelId +
            "'>" +
            child.labelName +
            "</p>" +
            "								<p class=\"number\">" +
            "</span> <span>作品" +
            child.productCount +
            "</span>" +
            "								</p>" +
            "</div>" +
            "</li>";
        }
        box.append(html);
    }
    
})($, window)
