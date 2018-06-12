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


