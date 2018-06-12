/*
   作品详情页面的评论模块事件响应 
   comments 父级id
 */
  window.onload = function(){
  var comments = document.getElementById('comments');
  var cmnts = comments.children;
  var textArea = cmnts.getElementByClassName('comment-text-self');//修改
  var timer;


//评论获取焦点
textArea.onfocus = function{
       textArea.parentNode.className = 'commentext-box-on btns';
       this.value = this.value == '评论...' ? '' : this.value;
       this.onkeyup();
  }

//评论失去焦点
textArea.onblur = function () {
    var cur = this;
    var val = me.value;
    if (val == '') {
        timer = setTimeout(function () {
            cur.value = '评论…';
            cur.parentNode.className = 'comment-text-box btns';
            }, 200);
    }
}


//评论按键事件
textArea.onkeyup = function () {
    var val = this.value;
    var len = val.length;
    var els = this.parentNode.children;
    var al = els[1];
    var word = els[2];
    if (len <=0 || len > 200) {
        al.className = 'dis-replay-btn oBtn btn-off';
    }
    else {
         al.className = 'dis-replay-btn oBtn';
    }
        word.innerHTML = len + '/200';
}

/*
   格式化日期
 */
function formateDate(date) {
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var h = date.getHours();
        var mi = date.getMinutes();
        m = m > 9 ? m : '0' + m;
        return y + '-' + m + '-' + d + ' ' + h + ':' + mi;
    }


/*
   评论内容
 */
function replay(ul,el){
   var lis = ul.getElementByClassName('dis-post')[1];
   var textarea = ul.getElementByClassName('comment-text-self');
   var commenTextBox = document.createElement('div');
   commenTextBox.className = 'dis-post-self';
   commenTextBox.setAttribute('data-user','myself');
   commenTextBox.innerHTML = 
        '<a class="dis-round-head" href="#">'+
           '<div class="dis-head">'+
                '<img src="img/big.jpg">'+
            '</div>'+
        '</a>'+
        '<div class="dis-comment-body">'+
            '<div class="dis-commnet-header">'+
                 '<h5 class="dis-user-name">'+小七+
                 '</h5>'+
                 '<span class="dis-time" title="#">'+formateDate(new Date())+
                 '</span>'+
            '</div>'+
            '<p class="dis-comment-text">'+textarea.value+'</p>'+
        '</div>'+
        '<div class="dis-comment-footer">'+
            '<a class="dis-comment-replay" href="javascript:void(0);">'+'<span class="glyphicon glyphicon-share-alt">'+回复+'</span>'+'</a>'+
            '<a class="dis-comment-praise" href="javascript:void(0);" data-total="0" my="0" style="">'+'<span class="glyphicon glyphicon-thumbs-up">'+赞+'</span>'+'</a>'+
            '<a class="dis-comment-warning" href="javascript:void(0);>'+'<span class="glyphicon glyphicon-flag">'+举报+'</sapn>'+'</a>'+
        '</div>'
    lis.appendChild(commenTextBox);
    textarea.value = '';
    textarea.onblur();
}

  //事件代理
  for(i = 0; i < cmnts.length; i++){
     //点击
    cmnts[i].onclick = function(e){
        e = e || window.event;
        var el = e.srcElement || e.target;
        switch(el.className){
            //点击标签
            case 'dis-label':
                  break;

            //点击最新、最热
            case 'dis-order-new':
                break;

            case 'dis-order-most':
                break;

            //点击评论按钮
            case 'dis-replay-btn':
               replay(el.parentNode.parentNode.parentNode,el);
               break;
        }
    }

  }

}
