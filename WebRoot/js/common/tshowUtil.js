(function(window, $){
    var tshowUtil = new TshowUtil();
    window.tshowUtil = tshowUtil;
    function TshowUtil(){
    
    }
    var fn = TshowUtil.prototype;
    /**
     * 导航栏切换事件
     *
     * @param navHeader 导航栏jquery选择器
     * @param content   内容选择器
     * @param css       导航栏选中样式
     * @param type		 鼠标事件
     * @return
     */
    fn.navChange = function(navHeader, content, css, type){
        if (!type) {
            type = "click";
        }
        $(navHeader).on(type, function(){
            var $me = $(this);
            var index = $me.index();
            $me.addClass(css).siblings().removeClass(css);
            $(content).eq(index).show().siblings().hide();
        });
    }
})(window, $);

