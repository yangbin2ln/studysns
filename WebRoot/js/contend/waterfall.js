window.onload = function(){
    //计算页面高度
    initHeight();
    //初始化ul
    initUl();
    $("#pul-main").data({
        page: {
            pageNo: 1,
            pageSize: 10
        }
    });
    
    var page = $("#pul-main").data("page");
    findProducts(page);
    window.onresize = function(){
        initHeight();
    }
    
    var dataInt = {
        'data': [{
            'src': '1.jpg'
        }, {
            'src': '2.jpg'
        }, {
            'src': '3.jpg'
        }, {
            'src': '4.jpg'
        }]
    };
    
    window.onscroll = function(){
        if (checkscrollside()) {
            var page = $("#pul-main").data("page");
            
            //发送请求取数据
            findProducts(page);
        };
            }
    
    
    /*
     parend 父级id
     pin 元素id
     */
    /*
     function checkscrollside(){
     var oParent=document.getElementById('pul-main');
     var aPin=getClassObj(oParent,'boxw');
     var lastPinH=aPin[aPin.length-1].offsetTop+Math.floor(aPin[aPin.length-1].offsetHeight/2);//创建【触发添加块框函数waterfall()】的高度：最后一个块框的距离网页顶部+自身高的一半(实现未滚到底就开始加载)
     var scrollTop=document.documentElement.scrollTop||document.body.scrollTop;//注意解决兼容性
     var documentH=document.documentElement.clientHeight;//页面高度
     return (lastPinH<scrollTop+documentH)?true:false;//到达指定高度后 返回true，触发waterfall()函数
     }*/
    /*查询作品*/
    function findProducts(page, type){
        if (!isMoreData($("#pul-main"))) {
            return;
        }
        if (!type) {
            type = 'time';
        }
        $tsBlockUI({
            message: "<img src='/img/123321.gif'>",
            css: {
                top: '90%'
            },
            showOverlay: false
        });
        var divPageNo = page.pageNo;
        $.post("contend/findAllProducts", {
            pageNo: page.pageNo,
            pageSize: page.pageSize,
            type: type
        }, function(res){
            $.unblockUI();
            var products = JSON.parse(res.map.products);
            if (products.length < page.pageSize) {
                page.pageNo--;
                $("#pul-main").data({
                    'flag': false
                });
            }
            ////console.log(products);
            var oParent = $('#pul-main');// 父级对象
            createProductsHtml(products, "#pul-main");
        })
        page.pageNo++;
    }
    
    /*设定动画启动和停止事件*/
    (function startOrStopLabel(){
        ////console.log("asdfsdafsdaf")
        $(".sch-brands-slider").delegate("", "mouseover", stopUnlockLabelDh)
        $(".sch-brands-slider").delegate("", "mouseout", unlockLabelDh)
    })();
    
    (function(){
        $(".sch-brands-right").on("click", function(){
            var obj = $(".sch-brands-container li").last().addClass("mar158");
            $(".sch-brands-container li").last().insertBefore($(".sch-brands-container li").first())
            obj.removeClass("mar158");
        });
        $(".sch-brands-left").on("click", function(){
            var obj = $(".sch-brands-container li").first().addClass("mar158");
            // setTimeout(function(){
            $(".sch-brands-container li").first().insertAfter($(".sch-brands-container li").last())
            obj.removeClass("mar158");
            // }, 800)
        
        });
    })();
    //绑定弹出详情页面事件
    (function(){
        $("#pul-main").delegate(".boxw-c", "click", openProductInfo)
    })();
    $("#most-new").on("click", function(){
        $("#pul-main>ul").empty();
        var page = $("#pul-main").data("page");
        page.pageNo = 0;
        findProducts(page, 'time');
    });
    
    $("#most-hot").on("click", function(){
        $("#pul-main>ul").empty();
        var page = $("#pul-main").data("page");
        page.pageNo = 1;
        $("#pul-main").data({
            'flag': true,
            'flagNum': -1
        });
        findProducts(page, 'score');
    });
    
    //导航栏样式
    $('#items-p .btnText').on('click', function(){
        $(this).addClass('bac-white').parent().siblings().children().removeClass('bac-white');
    });
}


function initUl(){
    var dw = $(window).width();
    var num = dw / 256 == 0 ? 1 : dw / 256;
    num = Math.floor(num);
    $("#pul-main").data({
        num: num
    });
    var ulHtml = "";
    $("#pul-main").html("");
    for (var i = 0; i < num; i++) {
        ulHtml += "<ul></ul>";
    }
    $("#pul-main").html(ulHtml);
    dw = $(window).width();
    $("#pul-main").css("padding-left", Math.floor((dw - num * 256) / 2))//对于子元素是浮动的，可以设置padding调整居中
}

/****
 *通过父级和子元素的class类 获取该同类子元素的数组
 */
function getClassObj(parent, className){
    var obj = parent.getElementsByTagName('*');//获取 父级的所有子集
    var pinS = [];//创建一个数组 用于收集子元素
    for (var i = 0; i < obj.length; i++) {//遍历子元素、判断类别、压入数组
        if (obj[i].className == className) {
            pinS.push(obj[i]);
        }
    };
    return pinS;
}

/****
 *获取 pin高度 最小值的索引index
 */
function getminHIndex(arr, minH){
    for (var i in arr) {
        if (arr[i] == minH) {
            return i;
        }
    }
}

/*解锁标签切换动画*/
function unlockLabelDh(){
    var time = setInterval(function(){
        var obj = $(".h100").eq(0);
        obj.width(0);
        obj.css("margin", "0px");
        setTimeout(function(){
            $(".sch-brands-container").append(obj);
            obj.width(138);
            obj.css("margin", "5px 10px 5px 10px")
        }, 1000);
    }, 1000);
    $(".sch-brand").data({
        "time": time
    });
}

/*停止解锁标签切换动画*/
function stopUnlockLabelDh(){
    ////console.log("stop")
    var time = $(".sch-brand").data("time");
    clearInterval(time);
}
