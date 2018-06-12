(function($, window){
    var setup = function(){
        initEvent();
        initData();
    }
    window.setup = setup;
    
    function initData(){
        addYxz();
    }
    function addYxz(){
        //初始化已选择的学校范围和年级范围
        var box = $(".box4-yxz").eq(0);
        var box2 = $("#box4 .box4-fwxz:eq(0) .right label input:checked");
        for (var i = 0; i < box2.length; i++) {
            var obj = box2.eq(i);
            var html = "<span data-id='" + obj.attr("data-id") + "'>" + obj.attr("data-name") + "<span>X</span>";
            box.append(html);
        }
        box = $(".box4-yxz").eq(1);
        box2 = $("#box4 .box4-fwxz:eq(1) .right label input:checked");
        for (var i = 0; i < box2.length; i++) {
            var obj = box2.eq(i);
            var html = "<span data-id='" + obj.attr("data-id") + "'>" + obj.attr("data-id") + "<span>X</span>";
            box.append(html);
        }
    }
    function initEvent(){
        $(".Basicdata-uerpic-fix").on('click', function(){
            $("#file1").click();
            cjimg();
        });
        $("#tpcj .submit-2").on("click", function(){
            var image = new Image();
            var img = $(".tpcj-img img");
            image.src = img.attr("src");
            var obj = getNaturalWidth(img);
            var naturalWidth = obj.naturalWidth;
            var naturalHeight = obj.naturalHeight;
            var left = -parseInt(img.css("left"));
            var top = -parseInt(img.css("top"));
            //console.log(left);
            //console.log(top);
            saveCanvas(image, left * naturalWidth / img.width(), top * naturalHeight / img.height(), 160 / img.width() * naturalWidth, 160 / img.height() * naturalHeight);
            $("#tpcj").hide();
        });
        $("#tpcj .quit").on("click", function(){
            $("#tpcj").hide();
        });
        //拖拽图片
        (function(flag){
            var x, y, left, top;
            $(".tpcj-zzc").on("mousedown", function(){
                flag = true;
                var img = $("#tpcj .tpcj-img img");
                left = parseInt(img.css('left'));
                top = parseInt(img.css('top'));
                x = event.pageX;
                y = event.pageY;
                //console.log(flag);
            }).on("mouseup", function(){
                flag = false;
                //console.log(flag);
            
            }).on("mousemove", function(){
                if (flag) {
                    //console.log(x - event.pageX);
                    //console.log(y - event.pageY);
                    var img = $("#tpcj .tpcj-img img");
                    img.css({
                        left: left + event.pageX - x + 'px',
                        top: top + event.pageY - y + 'px'
                    });
                    isOverFlow();
                }
            })
        })(false);
        //基本资料保存
        $(".jbzl-sub").on("click", function(){
            var $me = $(this);
            var realName = $(".username").val().trim();
            var headIco = $(".Basicdata-uerpic img").attr("src");
            var schoolId = $("#school-info").attr("data-id");
            var year = $('.userschool-year option:selected').val();
            var professionId = $(".userProfessional").attr("data-id");
            var jobId = $('#selectJob option:selected').val();
            var signature = $(".signature-info").val().trim();
            var obj = {
                realName: realName,
                headIco: headIco,
                schoolId: schoolId,
                year: year,
                professionId: professionId,
                jobId: jobId,
                signature: signature
            }
            tshowPost("/user/updateUserInfo", obj, function(res){
                //console.log(res);
                if (res.success) {
                    createPromptBox("保存成功");
                }
                else {
                    createPromptBox(res.mess.message);
                }
            })
        });
        
        //修改密码事件
        $(".buttontext-but").on("click", function(){
            $(".xgmm").toggle();
        });
        $(".xgmm .submit-2").on("click", function(){
            var pwdOld = $(".pwd-old").val().trim();
            var pwdNew = $(".pwd-new").val().trim();
            var pwdNew2 = $(".pwd-new2").val().trim();
            var boo = checkPwd(pwdOld, pwdNew, pwdNew2);
            if (boo) {
                tshowPost("/user/updateUserPwd", {
                    pwdOld: pwdOld,
                    pwdNew: pwdNew
                }, function(res){
                    //console.log(res);
                    if (!res.success) {
                        $(".pwd-old").next().text("密码错误");
                    }
                    else {
                        createPromptBox("修改密码成功");
                    }
                })
            }
        });
        
        $(".xgmm .cancle").on("click", function(){
            $(".xgmm").toggle();
        });
        
        //模糊检索学校
        $("#school-info").on("keyup", function(){
            var key = $(this).val().trim();
            if (key == '') {
                return;
            }
            tshowPost("/school/key", {
                key: key
            }, function(res){
                //console.log(res);
                createSchoolList(res);
            })
        });
        //模糊检索专业
        $(".userProfessional").on("keyup", function(){
            var key = $(this).val().trim();
            if (key == '') {
                return;
            }
            tshowPost("/profession/key", {
                key: key
            }, function(res){
                //console.log(res);
                createProfeList(res);
            })
        });
        $("#school-info").on("focus", function(){
            $(".school-list").show();
        });
        $("#school-info").on("blur", function(){
            var $me = $(this);
            setTimeout(function(){
                $me.val($me.attr("data-name"));
                $(".school-list").hide();
            }, 200);
        });
        $(".userProfessional").on("focus", function(){
            $(".profe-list").show();
        });
        $(".userProfessional").on("blur", function(){
            var $me = $(this);
            setTimeout(function(){
                $me.val($me.attr("data-name"));
                $(".profe-list").hide();
            }, 200);
        });
        //学校列表的选中事件
        $(".school-list").delegate(".sc-list-e", "click", function(){
            var $me = $(this);
            var box = $("#school-info");
            box.val($me.text());
            box.attr({
                'data-id': $me.attr("data-id"),
                'data-name': $me.attr("data-name")
            });
            
        });
        //专业列表的选中事件
        $(".profe-list").delegate(".pro-list-e", "click", function(){
            var $me = $(this);
            var box = $(".userProfessional");
            box.val($me.text());
            box.attr({
                'data-id': $me.attr("data-id"),
                'data-name': $me.attr("data-name")
            });
        });
        //分配企业子账户
        $("#send-co").on("click", function(){
            var $me = $(this);
            if ($me.data("flag") == false) {
                return;
            }
            $me.data({
                flag: false
            });
            var email = $(".email1").val().trim();
            if (!checkEmail(email)) {
                createPromptBox("邮件格式有误");
                $me.data({
                    flag: true
                });
                return;
            }
            tshowPost("/company/saveQyAccount", {
                email: email
            }, function(res){
                $me.data({
                    flag: true
                });
                //console.log(res);
                if (res.success) {
                    createPromptBox("邮件发送成功");
                }
                else {
                    createPromptBox(res.mess.message);
                }
            });
        });
        //邀请好友
        $("#send-hy").on("click", function(){
            var $me = $(this);
            if ($me.data("flag") == false) {
                return;
            }
            $me.data({
                flag: false
            });
            var email = $(".email2").val().trim();
            if (!checkEmail(email)) {
                createPromptBox("邮件格式有误");
                $me.data({
                    flag: true
                });
                return;
            }
            tshowPost("/user/savePtAccount", {
                email: email
            }, function(res){
                //console.log(res);
                $me.data({
                    flag: true
                });
                if (res.success) {
                    createPromptBox("邮件发送成功");
                }
                else {
                    createPromptBox(res.mess.message);
                }
            });
        });
        //课程模块切换
        $(".Course-setting-h>h3").on("click", function(){
            var $me = $(this);
            var index = $me.index();
            $me.addClass("Course-setting-h-col").siblings().removeClass("Course-setting-h-col");
            $(".Course-setting-li").eq(index).show().siblings().hide();
            if (index == 1) {
                //if (!$me.data("course")) {
                var page = {
                    pageNo: 1,
                    pageSize: 10
                }
                loadCourse(page, $me);
                // }
            }
        });
        //发布课程
        $(".course-sub").on("click", function(){
            var $me = $(this);
            if ($me.data("flag") == false) {
                return;
            }
            $me.data({
                flag: false
            });
            var place = $(".course-place").val();
            var telephone = $(".course-tel").val();
            var count = $(".course-count").val();
            var content = $(".course-intro").val();
            var obj = {
                place: place,
                telephone: telephone,
                countLimit: count,
                content: content
            };
            tshowPost("/company/saveCourse", obj, function(res){
                $me.data({
                    flag: true
                });
                if (res.success) {
                    createPromptBox("发布成功");
                }
                //console.log(res);
            });
        });
        //修改删除课程事件
        $(".ta1").delegate(".course-up", "click", function(){
            var r = confirm("你确认要提交此次修改");
            if (!r) {
                return;
            }
            var $me = $(this);
            var box = $me.parent().parent().parent();
            var courseId = box.find(".course-id").text();
            var place = box.find('td').eq(2).find('textarea').val().trim();
            var telephone = box.find('td').eq(3).find('textarea').val().trim();
            var content = box.find('td').eq(4).find('textarea').val().trim();
            var countLimit = box.find('td').eq(6).find("input").val().trim();
            tshowPost("/company/updateCourse", {
                courseId: courseId,
                place: place,
                telephone: telephone,
                content: content,
                countLimit: countLimit
            }, function(res){
                if (res.success) {
                    createPromptBox("修改成功");
                }
            })
        });
        $(".ta1").delegate(".course-de", "click", function(){
            var r = confirm("你确认要删除此课程吗");
            if (!r) {
                return;
            }
            var $me = $(this);
            var box = $me.parent().parent().parent();
            var courseId = box.find(".course-id").text();
            tshowPost("/company/deleteCourse", {
                courseId: courseId
            }, function(res){
                //console.log(res);
                if (res.success) {
                    createPromptBox("删除成功");
                    box.remove();
                }
                else {
                    createPromptBox(res.mess.message);
                }
            })
        });
        //分页代理事件
        $(document).delegate(".pageNext", "click", function(){
            var page = $(this).parents("table").data("page");
            page.pageNo++;
            if (page.pageNo > page.totalPages) {
                page.pageNo = page.totalPages;
                return
            }
            loadCourse(page);
        });
        $(document).delegate(".pagePrev", "click", function(){
            var page = $(this).parents("table").data("page");
            page.pageNo--;
            if (page.pageNo <= 0) {
                page.pageNo = 1;
                return
            }
            loadCourse(page);
        });
        $(document).delegate(".pageFir", "click", function(){
            var page = $(this).parents("table").data("page");
            page.pageNo = 1;
            loadCourse(page);
        });
        //关闭弹出框
        $(".close-2").on("click", function(){
            var $me = $(this);
            $me.parents(".tshow-tck").eq(0).hide();
        });
        //账户详情事件
        $(".Companyaccount-c").delegate(".account-info-ope", "click", function(){
            //查询相关信息
            var $me = $(this);
            var studentId = $me.parent().parent().children().eq(0).text().trim();
            tshowAjax({
                url: "/company/findChildCount",
                type: "post",
                data: {
                    studentId: studentId
                },
                async: false,
                success: function(res){
                    //console.log(res);
                    createAccountHtml(res);
                    $(".account-info").show();
                }
            });
        });
        //冻结解冻子账户
        $("#account-info").delegate(".djed", "click", function(){
            var $me = $(this);
            var studentId = $me.attr("data-id");
            tshowPost("/company/freeAccount", {
                studentId: studentId
            }, function(res){
                //console.log(res);
                if (res.success) {
                    $me.text("冻结")
                    $me.removeClass("djed").addClass("dj");
                }
            });
        });
        $("#account-info").delegate(".dj", "click", function(){
            var $me = $(this);
            var studentId = $me.attr("data-id");
            tshowPost("/company/freezeAccount", {
                studentId: studentId
            }, function(res){
                //console.log(res);
                if (res.success) {
                    $me.text("已冻结")
                    $me.removeClass("dj").addClass("djed");
                }
            });
        });
        //学校选中事件
        $("#box4 .box4-fwxz:eq(0)").delegate(".right label input", "click", function(){
            var $me = $(this);
            var box = $("#box4 .box4-fwxz:eq(0) .right label input:checked");
            var schoolIds = "";
            for (var i = 0; i < box.length; i++) {
                schoolIds += box.eq(i).attr("data-id") + ",";
            }
            schoolIds = schoolIds.substring(0, schoolIds.length - 1);
            tshowPost("/user/saveProductShowArea", {
                'schoolIds': schoolIds
            }, function(res){
                //console.log(res);
            });
            $(".box4-yxz").find(">span").remove();
            addYxz();
        });
        //年级范围选中事件
        $("#box4 .box4-fwxz:eq(1)").delegate(".right label input", "click", function(){
            var $me = $(this);
            var box = $("#box4 .box4-fwxz:eq(1) .right label input:checked");
            var years = "";
            for (var i = 0; i < box.length; i++) {
                years += box.eq(i).attr("data-id") + ",";
            }
            years = years.substring(0, years.length - 1);
            tshowPost("/user/saveProductsShowYear", {
                'years': years
            }, function(res){
                //console.log(res);
            });
            $(".box4-yxz").find(">span").remove();
            addYxz();
        });
        //范围切换事件
        $("#box4 .box4-h3-area span").on("click", function(){
            var $me = $(this);
            $me.addClass("box4-h3-area-s-col").siblings().removeClass("box4-h3-area-s-col");
            var index = $me.index();
            $("#box4 .box4-fwxz").eq(index).show().siblings().hide();
        });
        //擦掉已选择的学校和年级
        $(".box4-yxz:eq(0)").delegate(">span>span", "click", function(){
            var $me = $(this);
            var id = $me.parent().attr("data-id");
            var box2 = $("#box4 .box4-fwxz .right label input:checked");
            for (var i = 0; i < box2.length; i++) {
                if (box2.eq(i).attr("data-id") == id) {
                    box2.eq(i).attr("checked", false);
                    $me.parent().remove();
                    /*start*/
                    var box = $("#box4 .box4-fwxz:eq(0) .right label input:checked");
                    var schoolIds = "";
                    for (var i = 0; i < box.length; i++) {
                        schoolIds += box.eq(i).attr("data-id") + ",";
                    }
                    schoolIds = schoolIds.substring(0, schoolIds.length - 1);
                    tshowPost("/user/saveProductShowArea", {
                        'schoolIds': schoolIds
                    }, function(res){
                        //console.log(res);
                    })
                    /*end*/
                    return;
                }
            }
        });
        $(".box4-yxz:eq(1)").delegate(">span>span", "click", function(){
            var $me = $(this);
            var id = $me.parent().attr("data-id");
            var box2 = $("#box4 .box4-fwxz .right label input:checked");
            for (var i = 0; i < box2.length; i++) {
                if (box2.eq(i).attr("data-id") == id) {
                    box2.eq(i).attr("checked", false);
                    $me.parent().remove();
                    /*start 发后台请求*/
                    var box = $("#box4 .box4-fwxz:eq(1) .right label input:checked");
                    var years = "";
                    for (var i = 0; i < box.length; i++) {
                        years += box.eq(i).attr("data-id") + ",";
                    }
                    years = years.substring(0, years.length - 1);
                    tshowPost("/user/saveProductsShowYear", {
                        'years': years
                    }, function(res){
                        //console.log(res);
                    })
                    /*end   发后台请求*/
                    return;
                }
            }
        });
        
    }
    
    //渲染子账户详情页面
    function createAccountHtml(res){
        var box = $("#account-info>ul");
        var html = "";
        var obj = res.bean;
        html += "<li>" +
        "					<h3>姓名</h3>" +
        "					<div>" +
        "						<input type=\"text\" class=\"name\" value=\"" +
        obj.realName +
        "\" disabled=\"true\">" +
        "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>账号</h3>" +
        "					<div>" +
        "						<input type=\"text\" class=\"name\" value=\"" +
        obj.invitationId +
        "\" disabled=\"true\">" +
        "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>标签</h3>" +
        "					<div>" +
        "						<input type=\"text\" class=\"name\" value=\"" +
        obj.labelName +
        "\" disabled=\"true\">" +
        "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>曝光学校</h3><div>";
        for (var i = 0; i < obj.showSchools.length; i++) {
            var school = obj.showSchools[i];
            html += "<input disabled=\"true\" type=\"text\" class=\"name school " + (i != 0 ? 'school-t' : '') + "\" value=\"" + school.schoolName + "\" data-id='" + school.schoolId + "'>";
            
        }
        html += "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>曝光年级</h3>" +
        "					<div>";
        for (var i = 0; i < obj.showGrades.length; i++) {
            var grade = obj.showGrades[i];
            html += "<input disabled=\"true\" type=\"text\" class=\"name school " + (i != 0 ? 'school-t' : '') + "\" value=\"" + grade.grade + "\">";
            
        }
        html += "</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>粉丝数</h3>" +
        "					<div>" +
        "						<input disabled=\"true\" type=\"text\" class=\"name\" value=\"" +
        obj.followerCount +
        "\" disabled=\"true\">" +
        "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>网上上课</h3>" +
        "					<div>" +
        "						<input disabled=\"true\" type=\"text\" class=\"name\" value=\"" +
        (obj.studentCount.WBM || 0) +
        "\" disabled=\"true\">" +
        "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>报名上课</h3>" +
        "					<div>" +
        "						<input type=\"text\" class=\"name\" value=\"" +
        (obj.studentCount.YBM || 0) +
        "\" disabled=\"true\">" +
        "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>毕业学生</h3>" +
        "					<div>" +
        "						<input disabled=\"true\" type=\"text\" class=\"name\" value=\"" +
        (obj.studentCount.YBY || 0) +
        "\" disabled=\"true\">" +
        "					</div>" +
        "				</li>" +
        "				<li>" +
        "					<h3>账号管理</h3>" +
        "					<div>" +
        (obj.state == 'DJ' ? "<button class=\"accountFrozen djed\" data-id='" + obj.studentId + "'>已冻结</button>" : "<button class=\"accountFrozen dj\" data-id='" + obj.studentId + "'>冻结</button>") +
        "					</div>" +
        "				</li>";
        box.html(html);
    }
    
    //渲染课程列表
    function createCourseList(res){
        var box = $(".ta1 tbody");
        var html = "";
        for (var i = 0; i < res.page.result.length; i++) {
            var obj = res.page.result[i];
            html += "<tr>" +
            "<td style='display:none' class='course-id'>" +
            obj.courseId +
            "</td><td>" +
            (i + 1) +
            "</td>" +
            "										<td><textarea class='course-info-up' >" +
            obj.place +
            "</textarea></td>" +
            "										<td><textarea class='course-info-up' >" +
            obj.telephone +
            "</textarea></td>" +
            "										<td><textarea class='course-info-up' >" +
            obj.content +
            "</textarea></td>" +
            "										<td>" +
            obj.count +
            "</td>" +
            "										<td><input class='course-info-up-in' value='" +
            obj.countLimit +
            "'>" +
            "</td>" +
            "										<td style='font-size:10px'>" +
            obj.createTime.substring(0, obj.createTime.length - 2) +
            "</td>" +
            "										<td><div>" +
            "												<button class='course-up'>修改</button>" +
            "												<button class='course-de'>删除</button>" +
            "											</div>" +
            "										</td>" +
            "</tr>";
        }
        html = appendPage(html, res.page);
        $(".ta1").data({
            page: res.page
        });
        box.html(html);
    }
    
    function appendPage(html, page){
        var htm = "<tr><td style='text-align:right;height:28px' colspan=8><div class='pagedi'>";
        htm += "<span>第" + page.pageNo + "页</span><span>共" + page.totalPages + "页</span>";
        htm += "<span class='pagesx pageFir'>首页</span><span class='pagesx pagePrev'>上一页</span><span class='pagesx pageNext'>下一页</span></div></td></tr>";
        html += htm;
        return html;
    }
    
    //查询课程
    function loadCourse(page, $me){
        tshowPost("/company/findCourse/list", {
            studentId: $(document).data("student").studentId,
            pageNo: page.pageNo,
            pageSize: page.pageSize
        }, function(res){
            if ($me) {
                $me.data({
                    course: page
                })
            }
            //console.log(res);
            createCourseList(res);
        })
    }
    
    //重新发送邮件效果
    function repeatSend($me){
        var a = 5;
        var timer = setInterval(function(){
            if (a == 0) {
                $me.text("发送");
                $me.data({
                    flag: true
                });
                clearInterval(timer);
                return;
            }
            $me.text(a-- + "秒后可重新发送")
        }, 1000);
    }
    
    function createSchoolList(res){
        var box = $(".school-list>div");
        var html = ""
        var schools = res.map.schools;
        for (var i = 0; i < schools.length; i++) {
            var school = schools[i];
            html += "<div class='sc-list-e' data-id='" + school.schoolId + "' data-name='" + school.schoolName + "' >" + school.schoolName + "</div>"
        }
        box.html(html);
    }
    function createProfeList(res){
        var box = $(".profe-list>div");
        var html = ""
        var professions = res.bean;
        for (var i = 0; i < professions.length; i++) {
            var profession = professions[i];
            html += "<div class='pro-list-e' data-id='" + profession.professionId + "' data-name='" + profession.professionName + "'>" + profession.professionName + "</div>"
        }
        box.html(html);
    }
    
    function checkPwd(pwdOld, pwdNew, pwdNew2){
        $(".pwd-error").text("");
        if (pwdOld == '') {
            $(".pwd-old").next().text("不能为空");
            return false;
        }
        if (pwdNew == '') {
            $(".pwd-new").next().text("不能为空");
            return false;
        }
        if (pwdNew2 == '') {
            $(".pwd-new2").next().text("不能为空");
            return false;
        }
        if (pwdNew != pwdNew2) {
            $(".pwd-new2").next().text("两次密码不一致");
            return false;
        }
        return true;
    }
    
    function cjimg(){
        $('input[type=file]').change(function(){
            var image = new Image();
            var file = this.files[0];
            
            var reader = new FileReader();
            reader.onload = function(){
                // 通过 reader.result 来访问生成的 DataURL
                var url = reader.result;
				console.log(image.width);
                setImageURL(image, url);
                $("#tpcj .tpcj-img").html(image);
                $("#tpcj").show();
            };
            reader.readAsDataURL(file);
        });
    }
    
    function saveCanvas(image, x, y, width, height){
        var canvas = $('<canvas id="can" width="' + width + '" height="' + height + '"></canvas>')[0];
        var ctx = canvas.getContext('2d');
        ctx.drawImage(image, x, y, width, height, 0, 0, width, height);
        uploadBase64Img(canvas);
    }
    
    function setImageURL(image, url){
        image.src = url;
    }
    /*start 上传文件(图片)*/
    function uploadFile(id, imgparent){
        $.ajaxFileUpload({
            url: "/ueditor/jsp/controller.jsp?action=uploadimage", //需要链接到服务器地址
            secureuri: true,
            fileElementId: id, //文件选择框的id属性
            dataType: "json",
            success: function(data, status){
                //console.log(data.url);
                if (data.state.toUpperCase() != "SUCCESS") {
                    alert(data.state)
                    return;
                }
                var url = data.url;
                $(imgparent).html($("<img>").attr("src", url));
                $(".zs .zs-r-wp-z-sc").removeClass("tshow-col-red");
                //console.log(status);
            },
            error: function(data, status, e){
                showDialogWithMsg('ideaMsg', '提示', '文件错误！');
            }
        });
    }
    
    function uploadBase64Img(canvas){
        var data = canvas.toDataURL("image/png");
        
        // dataURL 的格式为 “data:image/png;base64,****”,逗号之前都是一些说明性的文字，我们只需要逗号之后的就行了
        data = data.replace(/^data:image\/(png|jpg);base64,/, "")
		var size = data.length;
		console.log(size);
        var fd = new FormData();
        $.ajax({
            url: "/ueditor/jsp/controller.jsp?action=uploadscrawl",
            type: "POST",
            data: {
                "upfile": data
            },
            dataType: 'json',
            success: function(res){
                //console.log(res);
                $(".Basicdata-uerpic img").attr({
                    src: res.url
                })
            }
        });
    }
    
    /*end   上传文件(图片)*/
    
    //拖拽
    function tragHeadImg(){
        //判断编辑
        isOverFlow();
    }
    function isOverFlow(){
        var box = $('#tpcj .tpcj-img img');
        var boxp = $('#tpcj .tpcj-img');
        var left = parseInt(box.css('left'));
        var top = parseInt(box.css('top'));
        if (left >= 0) {
            box.css({
                left: '0px'
            });
        }
        if (top >= 0) {
            box.css({
                top: '0px'
            });
        }
        if (left < -(box.width() - boxp.width())) {
            box.css({
                left: -(box.width() - boxp.width()) + 'px'
            });
        }
        if (top < -(box.height() - boxp.height())) {
            box.css({
                top: -(box.height() - boxp.height()) + 'px'
            });
        }
    }
    function getNaturalWidth(img){
        var image = new Image();
        image.src = img.attr("src");
        var naturalWidth = image.width;
        var naturalHeight = image.height;
        return {
            naturalWidth: naturalWidth,
            naturalHeight: naturalHeight
        };
    }
 
    //提示框
    function createPromptBox(message, css, box){
        var defaultCss = {
            left: '48%',
            top: '50%',
            color: 'white',
            background: 'black',
            'border-radius': '10px',
            position: 'fixed',
            display: 'none',
            'text-align': 'center',
            padding: '10px 5px',
            'z-index': '9999',
            'font-size': '12px'
        };
        $.extend(defaultCss, css);
        var promptBox = $("<div class='prompt-box'>" + message + "</div>");
        promptBox.on("click", function(){
            $(this).remove()
        }).css(defaultCss);
        if (box) {
            box.append(promptBox);
        }
        else {
            $("body").append(promptBox);
        }
        promptBox.fadeIn("slow");
        setTimeout(function(){
            promptBox.fadeOut("slow");
            setTimeout(function(){
                promptBox.remove()
            }, 1800);
        }, 1800);
        return false;
    }
 
    
})($, window);
