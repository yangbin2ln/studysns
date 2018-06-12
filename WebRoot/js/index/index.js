(function($, window){

    var emails = ["qq.com", "sina.com", "gmail.com", "163.com", "126.com", "aliyun.com", "hotmail.com", "sohu.com", "vip.126.com", "vip.163.com", "vip.sina.com", "sina.cn", "vip.163.com", "msn.com", "outlook.com", "live.com", "live.cn", "yahoo.cn", "yahoo.com.cn", "yahoo.com.tw", "21cn.com", "tom.com"];
    var indexInit = function(){
        initData();
        initEvent();
        showLoginOrRegister();
    }
    window.indexInit = indexInit;
    
    function showLoginOrRegister(){
        var url = window.location.href;
        if (url.lastIndexOf('#register') != -1) {
            $(".sy-b-signup").click();
        }
        else {
            $(".sy-b-login").click();
        }
    }
    
    function initData(){
        var userName = getCookie("userName");
        $(".login-email").val(userName);
        showOrHideVerify();
    }
    
    function showOrHideVerify(){
        var verify = getCookie("verify");
        if (verify == 'show') {
            var code = new Date().getTime();
            $(".verify-p").show();
            $(".verify-img").html("<img src='/util/verifyCodeLogin?key=+" + code + "'>");
        }
    }
    
    //登录前验证
    function checkLoginBefore(userName, password, verifyCode){
        var leBox = $(".login-error");
        if (userName == '') {
            leBox.html('用户名不能为空');
            leBox.show();
            return false;
        }
        else 
            if (password == '') {
                leBox.html('密码不能为空');
                leBox.show();
                return false;
            }
            else 
                if (verifyCode == '') {
                    if ($(".verify-p").is(':visible')) {
                        leBox.html('验证码不能为空');
                        leBox.show();
                        return false;
                    }
                }
        return true;
    }
    
    function checkRegister(){
        if ($(".register-email").val().trim() == "") {
            $(".register-error").text("邮箱不能为空").show()
            return false;
        }
        else {
            var reg = '^([a-zA-Z0-9_-])+@(([a-zA-Z0-9_-]){1,10}.){1,10}(com|cn|tw|126.com|163.com|sina.com|com.cn|com.tw)$'
            if (!$(".register-email").val().match(reg)) {
                $(".register-error").text("邮箱格式不正确").show()
                return false;
            }
        }
        if ($(".invitation-id").val().trim() == "") {
            $(".register-error").text("邀请码不能为空").show()
            return false;
        }
        if ($(".register-username").val().trim() == "") {
            $(".register-error").text("姓名不能为空").show()
            return false;
        }
        if ($(".register-pwd").val().trim() == "") {
            $(".register-error").text("密码不能为空").show()
            return false;
        }
        if ($(".verify-p2 .verify").val().trim() == "") {
            $(".register-error").text("验证码不能为空").show()
            return false;
        }
        return true;
    }
    
    function initEvent(){
        $(".sy-b-login").on("click", function(){
            $(".log-in").show();
            $(".sign-up").hide();
        });
        $(".sy-b-signup").on("click", function(){
            $(".log-in").hide();
            $(".sign-up").show();
        });
        //登陆事件
        $(document).delegate("#login:not(.loginning)", "click", function(){
            var userName = $(".login-email").val().trim();
            var password = $(".login-pwd").val().trim();
            var verifyCode = $(".verify").val().trim();
            var b = checkLoginBefore(userName, password, verifyCode);
            if (!b) {
                return;
            }
            var obj = {
                userName: userName,
                password: password,
                verifyCode: verifyCode
            }
            $("#login").addClass("loginning").text("正在登录");
            $.post("/user/login", obj, function(res){
                //console.log(res);
                if (res.state == 'loginSuccess') {
                    //验证码默认不显示
                    setCookie("verify", "hide", 'h1');
                    //cookie中存储用户名称
                    setCookie("userName", userName, 'd7');
                    window.location.href = '/';
                }
                else {
                    $("#login").removeClass("loginning").text("登录");
                    var leBox = $(".login-error");
                    if (res.state == 'loginError0') {
                        leBox.html(res.message);
                        $(".verify-img").click();
                    }
                    else 
                        if (res.state == 'loginError1') {
                            leBox.html(res.message);
                        }
                        else 
                            if (res.state == 'loginError2') {
                                leBox.html(res.message);
                            }
                            else 
                                if (res.state == 'loginError3') {
                                    setCookie("verify", "show", 'h1');
                                    leBox.html(res.message);
                                    //生成验证码
                                    var code = new Date().getTime();
                                    $(".verify-p").show();
                                    $(".verify-img").html("<img src='/util/verifyCodeLogin?key=+" + code + "'>");
                                }
                                else 
                                    if (res.state == 'loginErrorYDJ') {
                                        leBox.html(res.message);
                                    }
                    leBox.show();
                }
            })
        });
        
        $(document).delegate("#register:not(.registing)", "click", function(){
            var $me = $(this);
            //注册信息验证
            var flag = checkRegister();
            if (!flag) {
                return;
            }
            //提交注册信息
            var obj = {
                email: $(".register-email").val().trim(),
                invitationId: $(".invitation-id").val().trim(),
                realName: $(".register-username").val().trim(),
                password: $(".register-pwd").val().trim(),
                verify: $(".verify-p2 .verify").val().trim()
            }
            $me.addClass("registing").text("正在提交注册信息...");
            $.post("/user/register", obj, function(res){
                if (!res.success) {
                    $me.removeClass("registing").text("注册");
                    $(".register-error").text(res.mess.message).show();
                    var code = new Date().getTime();
                    $(".verify-p2 .verify-img img").attr({
                        src: "/util/verifyCodeRegister?key=" + code
                    });
                }
                else {
                    alert("注册成功");
                    $("<a href=''></a>").get(0).click();
                }
                //console.log(res);
            });
        });
        
        $(".login-pwd").on("keydown", function(event){
            var $me = $(this);
            //console.log(event.keyCode);
            if (event.keyCode == 13) {
                $("#login").click();
            }
            else 
                if (event.keyCode == 32) {
                    return false;
                }
        });
        
        $(".register-pwd").on("keydown", function(event){
            var $me = $(this);
            //console.log(event.keyCode);
            if (event.keyCode == 32) {
                return false;
            }
        });
        
        
        //申请邀请码事件
        $("#apply-invi").on("click", function(){
            $(".sy-ic").show().prev().show();
        });
        
        //遮罩层事件
        $("#sqyqm").on("click", function(){
            $(this).hide().next().hide();
        });
        $(".zhmm-main .tshow-zzc").on("click", function(){
            $(this).parent().hide();
        });
        $("#forget-pwd").on("click", function(){
            $(".zhmm-main").show();
        });
        //注册验证码切换事件
        $(".verify-p2 .verify-img").on("click", function(){
            var code = new Date().getTime();
            $(this).find("img").attr({
                "src": "/util/verifyCodeRegister?key=" + code
            });
        });
        //登录验证码切换事件
        $(".verify-p .verify-img").on("click", function(){
            var code = new Date().getTime();
            $(this).find("img").attr({
                "src": "/util/verifyCodeLogin?key=" + code
            });
        });
        //邮箱监听事件
        $(".register-email").on("keyup", function(){
            var $me = $(this);
            var value = $me.val().trim();
            if (value == "") {
                $(".register-email-d-u").hide();
                return;
            }
            //console.log(event.keyCode)
            if (event.keyCode == 38) {//上
                var box = $(".register-email-d-u>li.email-col");
                if (box.length == 0) {
                    $me.focus();
                }
                else {
                    var index = box.index();
                    if (index == 0) {
                        // $me.focus();
                    }
                    else {
                        box.prev().addClass("email-col").siblings().removeClass("email-col");
                        box = $(".register-email-d-u>li.email-col")
                    }
                    var text = box.find("span").eq(0).text().trim();
                    text += box.find("span").eq(1).text().trim();
                    text += box.find("span").eq(2).text().trim();
                    $(".register-email").val(text);
                }
                return false;
            }
            else 
                if (event.keyCode == 40) {//下
                    var box = $(".register-email-d-u>li.email-col");
                    if (box.length == 0) {
                        $(".register-email-d-u>li").eq(0).addClass("email-col").siblings().removeClass("email-col");
                        box = $(".register-email-d-u>li.email-col")
                        var text = box.find("span").eq(0).text().trim();
                        text += box.find("span").eq(1).text().trim();
                        text += box.find("span").eq(2).text().trim();
                        $(".register-email").val(text);
                    }
                    else {
                        box.next().addClass("email-col").siblings().removeClass("email-col");
                        box = $(".register-email-d-u>li.email-col")
                        var text = box.find("span").eq(0).text().trim();
                        text += box.find("span").eq(1).text().trim();
                        text += box.find("span").eq(2).text().trim();
                        $(".register-email").val(text);
                    }
                    
                    return;
                }
                else 
                    if (event.keyCode == 13) {
                        var text = $(".register-email-d-u>li.email-col").find("span").eq(0).text().trim();
                        text += $(".register-email-d-u>li.email-col").find("span").eq(1).text().trim();
                        text += $(".register-email-d-u>li.email-col").find("span").eq(2).text().trim();
                        $(".register-email").val(text);
                        $(".register-email-d-u").hide();
                        return;
                    }
            if (value.indexOf("@") != -1 && (value.indexOf("@") != value.length - 1)) {
                createNowEmails(value.substring(0, value.indexOf("@")), value.substring(value.indexOf("@") + 1));
                $(".register-email-d-u>li").eq(0).addClass("email-col");
            }
            else {
                createDefaultEmails(value.substring(0, value.indexOf('@') != -1 ? value.indexOf('@') : value.length));
            }
            $(".register-email-d-u").show();
        })
        $(".register-email").on("blur", function(){
            setTimeout(function(){
                $(".register-email-d-u").hide();
            }, 300)
            
        })
        //匹配邮箱选中事件
        $(".register-email-d-u").delegate("li", "mouseover", function(){
            $(this).addClass("email-col").siblings().removeClass("email-col");
        }).on("click", function(){
            var $me = $(this);
            var text = $me.find('span').eq(0).text().trim();
            text += $me.find('span').eq(1).text().trim();
            text += $me.find('span').eq(2).text().trim();
            $(".register-email").val(text);
        })
        //回车提交注册事件
        $(".verify-p2 .verify").on("keydown", function(){
            if (event.keyCode == 13) {
                $("#register").click();
            }
        })
        //获取邮箱验证码
        $(".hqyzm-but").on("click", function(){
            var $me = $(this);
            if ($me.data("flag") == false) {
                return;
            }
            var email = $(".zhmy-email").val().trim();
            var reg = '^([a-zA-Z0-9_-])+@(([a-zA-Z0-9_-]){1,10}.){1,10}(com|cn|tw|126.com|163.com|sina.com|com.cn|com.tw)$'
            if (!email.match(reg)) {
                $(".zhmm-email-error").remove();
                $(".zhmy-email").after("<span class='zhmm-email-error'>邮箱格式不正确</span>")
                return false;
            }
            repeatSend($me);
            tshowPost("/user/findPwdByEmail", {
                email: email
            }, function(res){
                //console.log(res);
                if (res.success == false) {
                    $(".zhmm-email-error").remove();
                    $(".zhmy-email").after("<span class='zhmm-email-error'>" + res.mess.message + "</span>")
                }
            });
        });
        $(".zhmy-email,.zhmm-yzm").on("focus", function(){
            $(".zhmm-email-error").remove();
        });
        $(".zhmy-email,.zhmm-yzm").on("keyup", function(){
            var box1 = $(".zhmy-email");
            var box2 = $(".zhmm-yzm");
            var reg = '^([a-zA-Z0-9_-])+@(([a-zA-Z0-9_-]){1,10}.){1,10}(com|cn|tw|126.com|163.com|sina.com|com.cn|com.tw)$'
            if (!box1.val().trim().match(reg)) {
                return false;
            }
            if (box1.val().trim() != '' && box2.val().trim().length == 6) {
                $(".next-but").addClass("next-but-col ");
            }
            else {
                $(".next-but").removeClass("next-but-col ");
            }
        });
        //下一步事件
        $(document).delegate(".next-but-col", "click", function(){
            var $me = $(this);
            var box1 = $(".zhmy-email");
            var box2 = $(".zhmm-yzm");
            var reg = '^([a-zA-Z0-9_-])+@(([a-zA-Z0-9_-]){1,10}.){1,10}(com|cn|tw|126.com|163.com|sina.com|com.cn|com.tw)$'
            if (!box1.val().trim().match(reg)) {
                $(".zhmm-email-error").remove();
                $(".zhmy-email").after("<span class='zhmm-email-error'>邮箱格式不正确</span>")
                return false;
            }
            tshowPost("/user/findYzmValid", {
                yzm: box2.val().trim(),
                email: box1.val().trim()
            }, function(res){
                //console.log(res);
                if (res.success == false) {
                    $(".zhmm-email-error").remove();
                    if (res.mess.state == 'moreErrorCount') {
                        $(".zhmm-yzm").after("<span class='zhmm-email-error'>错误次数过多请重新获取</span>");
                    }
                    else {
                        $(".zhmm-yzm").after("<span class='zhmm-email-error'>验证码不正确</span>");
                    }
                }
                else {
                    $(".zhmm-email-set-up").val(box1.val().trim());
                    $(".zhmm-email-set-up").data({
                        email: box1.val().trim()
                    });
                    $me.parent().hide().next().show();
                }
            })
        });
        //提交密码
        $(document).delegate(".zhmm-sub-col", "click", function(){
            var $me = $(this);
            var pwd = $(".zhmm-new-pwd").val().trim();
            var email = $(".zhmm-email-set-up").data("email");
            if ($me.data("flag") == false) {
                return;
            }
            $me.data({
                flag: false
            });
            tshowPost("/user/findResetPwd", {
                pwd: pwd,
                email: email
            }, function(res){
                //console.log(res);
                if (res.success) {
                    $(".zhmm-main").hide();
                }
            });
        });
        //提交密码按钮
        $(".zhmm-new-pwd").on("keyup", function(){
            var $me = $(this);
            if ($me.val().trim().length >= 6) {
                $(".zhmm-sub").addClass("zhmm-sub-col");
            }
            else {
                $(".zhmm-sub").removeClass("zhmm-sub-col");
            }
        });
        
        $(".sczp-but").on("click", function(){
            $("#sczp").click();
        });
        //提交申请
        $(".zpsc-sub").on("click", function(){
            var howKnow = $(".how-know").val().trim();
            var wantKnow = $(".want-know").val().trim();
            var productRemark = $(".product-remark").val().trim();
            var email = $(".sq-email").val().trim();
            var product = $(".sczp-state-name").data("url");
            var obj = {
                howKnow: howKnow,
                wantKnow: wantKnow,
                productRemark: productRemark,
                product: product,
                email: email
            }
            tshowPost("/user/applyRegister", obj, function(res){
                //console.log(res);
                if (res.success) {
                    createPromptBox("您的申请已经提交，若审核通过后，我们将会通过邮件的方式向您发送邀请函！谢谢您对t-Show的关注！", {
                        'font-size': '15px',
                        'line-height': '25px'
                    }, null, 10000);
                }
            });
        });
    }
    
    function createNowEmails(prev, value){
        var html = ""
        for (var i = 0; i < emails.length; i++) {
            var key = emails[i];
            if (key.startsWith(value)) {
                html += "<li><span>" + prev + "</span><span>@</span><span>" + key + "</span></li>";
            }
        }
        $(".register-email-d-u").html(html);
    }
    
    //默认邮箱匹配
    function createDefaultEmails(value){
        var html = "<li class='email-col'><span>" + value + "</span><span>@</span><span>qq.com</span>" +
        "						</li>" +
        "						<li><span>" +
        value +
        "</span><span>@</span><span>sina.com</span>" +
        "						</li>" +
        "						<li><span>" +
        value +
        "</span><span>@</span><span>gmail.com</span>" +
        "						</li>" +
        "						<li><span>" +
        value +
        "</span><span>@</span><span>163.com</span>" +
        "</li>";
        $(".register-email-d-u").html(html);
    }
    //重新发送邮件效果
    function repeatSend($me){
        $me.data({
            flag: false
        });
        var a = 5;
        var timer = setInterval(function(){
            if (a == 0) {
                $me.text("获取验证码");
                $me.data({
                    flag: true
                });
                clearInterval(timer);
                return;
            }
            $me.text(a-- + "秒后可重新获取")
        }, 1000);
    }
    
    function sczp(){
        var $me = $(this);
        $me.prev().click();
    }
    
    
})($, window);
//上传作品
function uploadFile2(id, imgparent){
    $(".sczp-state").addClass("sczp-state-suc").text("上传中...")
    $.ajaxFileUpload({
        url: "/ueditor/jsp/controller.jsp?action=uploadfile", //需要链接到服务器地址
        secureuri: true,
        fileElementId: id, //文件选择框的id属性
        dataType: "json",
        success: function(data, status){
            //console.log(data.url);
            if (data.state.toUpperCase() != "SUCCESS") {
                alert(data.state)
                $(".sczp-state").addClass("sczp-state-suc").text("上传失败");
                return;
            }
            $(".sczp-state").addClass("sczp-state-suc").text("上传成功");
            $(".sczp-state-name").show().data({
                "url": data.url
            }).text(data.original);
            $(".sczp-but").text("重新上传");
            var url = data.url;
            //$(imgparent).html($("<img>").attr("src", url));
            $(".zs .zs-r-wp-z-sc").removeClass("tshow-col-red");
            //console.log(status);
        },
        error: function(data, status, e){
            showDialogWithMsg('ideaMsg', '提示', '文件错误！');
        }
    });
}
