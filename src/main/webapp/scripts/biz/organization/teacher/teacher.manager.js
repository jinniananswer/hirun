(function($){
    $.extend({teacher:{
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["courseTree"] = new Wade.Tree("courseTree");
                window["courseHoldTree"] = new Wade.Tree("courseHoldTree");
                window["changeCourseHoldTree"] = new Wade.Tree("changeCourseHoldTree");

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "TEACHER_TYPE",
                    // 参数设置
                    {
                        id:"TYPE",
                        name:"TYPE",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"内部讲师", VALUE:"0"},
                        {TEXT:"外聘讲师", VALUE:"1"}
                    ]
                );

                $("#TYPE").bind("change", function(){
                    if(this.value == "0") {
                        $("#NAME_INPUT").css("display", "none");
                        $("#NAME_SELECT").css("display", "");
                    }
                    else{
                        $("#NAME_INPUT").css("display", "");
                        $("#NAME_SELECT").css("display", "none");
                    }
                });

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "TEACHER_LEVEL",
                    // 参数设置
                    {
                        id:"LEVEL",
                        name:"LEVEL",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"讲师", VALUE:"0"},
                        {TEXT:"高级讲师", VALUE:"1"},
                        {TEXT:"资深讲师", VALUE:"2"},
                        {TEXT:"特级讲师", VALUE:"3"}
                    ]
                );

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "CHANGE_TEACHER_TYPE",
                    // 参数设置
                    {
                        id:"CHANGE_TYPE",
                        name:"CHANGE_TYPE",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"内部", VALUE:"0"},
                        {TEXT:"外部", VALUE:"1"}
                    ]
                );

                $("#CHANGE_TYPE").attr("disabled", true);

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "CHANGE_TEACHER_LEVEL",
                    // 参数设置
                    {
                        id:"CHANGE_LEVEL",
                        name:"CHANGE_LEVEL",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"讲师", VALUE:"0"},
                        {TEXT:"高级讲师", VALUE:"1"},
                        {TEXT:"资深讲师", VALUE:"2"},
                        {TEXT:"特级讲师", VALUE:"3"}
                    ]
                );

                $("#courseTree").textAction(function(e, nodeData){
                    $("#COURSE_ID").val(nodeData.id);
                    $("#COURSE_NAME").val(nodeData.text);
                    backPopup(document.getElementById('UI-COURSE'));
                    return false;
                });

                $("#courseTree").expandAction(function(e, nodeData) {
                    return true;
                });


                $("#courseHoldTree").expandAction(function(e, nodeData) {
                    return true;
                });


                $.ajaxPost('initTeacherManage',null,function(data) {
                    var trees = data.COURSE;
                    var rst = new Wade.DataMap(data);
                    var teachers = rst.get("TEACHER");
                    if(trees != null){
                        window["courseTree"].data = trees;
                        window["courseTree"].init();
                        window["courseTree"].expandByPath("-1", "●");
                    }
                    $.teacher.drawTeachers(teachers);
                });
            },

            initCreateTeacher : function() {
                $.ajaxPost('initCreateTeacher',null,function(data) {
                    var trees = data.COURSE;
                    if(trees != null){
                        window["courseHoldTree"].data = trees;
                        window["courseHoldTree"].init();
                        window["courseHoldTree"].expandByPath("-1", "●");
                    }
                    showPopup('UI-popup','UI-CREATE_TEACHER');
                });
            },

            confirmCourse : function() {
                var courses = $("#courseHoldTree input");
                var selectedCourseIds = '';
                var selectedCourseNames = '';

                if(courses == null || courses.length <= 0) {
                    MessageBox.alert("没有课程可供选择，请先检查课程是否已添加");
                }

                var length = courses.length;
                var num = 0;
                for(var i=0;i<length;i++) {
                    var course = $(courses[i]);
                    if(course.attr("checked")) {
                        selectedCourseIds += course.val() + ",";
                        var courseName = $(course.parent().parent().children()[2]).attr("title");
                        selectedCourseNames += courseName+",";
                        num++;
                    }
                }
                if(num == 0) {
                    MessageBox.alert("请至少选择一项课程");
                    return;
                }
                $("#HOLD_COURSE_ID").val(selectedCourseIds.substring(0, selectedCourseIds.length - 1));
                $("#HOLD_COURSE_NAME").val(selectedCourseNames.substring(0, selectedCourseNames.length - 1));
                backPopup(document.getElementById("UI-COURSE_HOLD"));
            },

            queryEmployee : function(){
                if($.validate.verifyAll("searchArea")) {
                    var parameter = $.buildJsonData("searchArea");
                    $.ajaxPost('queryContacts', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.teacher.drawEmployees(datas);
                    });
                }
            },

            drawEmployees : function(datas){
                $("#employees").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messageboxEmployee").css("display","");
                    return;
                }

                $("#messageboxEmployee").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var sex = data.get("SEX");
                    html.push("<li class='link' ontap=\"$.teacher.selectEmployee(\'"+data.get("EMPLOYEE_ID")+"\',\'"+data.get("NAME")+"\')\"><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    if(sex == "1")
                        html.push("<img src=\"/frame/img/male.png\" class='e_pic-r' style='width:4em;height:4em'/>");
                    else
                        html.push("<img src=\"/frame/img/female.png\" class='e_pic-r' style='width:4em;height:4em'/>");

                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    var parentOrgName = data.get("PARENT_ORG_NAME");
                    if(parentOrgName != null && parentOrgName != "undefined")
                        html.push(parentOrgName + "-");
                    html.push(data.get("ORG_NAME"));
                    html.push("</div><div class='content'>"+data.get("JOB_ROLE_NAME"));
                    html.push("</div></div>")
                    html.push("<div class=\"side e_size-m\">"+data.get("CONTACT_NO")+"</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#employees"), html.join(""));
            },

            selectEmployee : function(employeeId, name){
                $("#EMPLOYEE_ID").val(employeeId);
                $("#EMPLOYEE_NAME").val(name);
                backPopup(document.getElementById('UI-EMPLOYEE'));
            },

            fileChange : function() {
                var file = $("#PIC_FILE").attr("value");
                $("#PIC").val(file);
            },

            changeFileChange : function() {
                var file = $("#CHANGE_PIC_FILE").attr("value");
                $("#CHANGE_PIC").val(file);
            },

            createTeacher : function() {
                if($.validate.verifyAll("UI-CREATE_TEACHER")) {
                    var type = $("#TYPE").val();
                    var teacherName = $("#EMPLOYEE_NAME").val();
                    if(type == "0" && (teacherName == "" || teacherName == null)) {
                        $.TipBox.show(document.getElementById('EMPLOYEE_NAME'), "讲师姓名不能为空", "red");
                        return;
                    }

                    var name = $("#NAME").val();
                    if(type == "1" && (name == "" || name == null)) {
                        $.TipBox.show(document.getElementById('NAME'), "讲师姓名不能为空", "red");
                        return;
                    }

                    $.beginPageLoading();
                    var formData = new FormData($("#createTeacherForm")[0]);
                    var request = new XMLHttpRequest();
                    request.open( "POST", "createTeacher" , true );
                    request.onload = function(oEvent) {
                        $.endPageLoading();
                        if (request.status == 200) {
                            backPopup(document.getElementById("UI-CREATE_TEACHER"));
                            MessageBox.success("成功信息", "添加讲师成功", function(btn){
                                document.location.reload();
                            });
                        } else {
                            MessageBox.error("创建讲师失败");
                        }

                    };
                    request.send(formData);
                }
            },

            drawTeachers : function(datas) {
                $("#teachersType").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messageboxTeacher").css("display","");
                    return;
                }

                $("#messageboxTeacher").css("display","none");
                datas.eachKey(function(key, index, totalCount){
                    html.push("<div class='c_box c_box-border'><div class='c_title' ontap=\"$(this).next().toggle();\">");
                    var levelName = "";
                    if(key == "0") {
                        levelName = "讲师";
                    }
                    else if(key == "1") {
                        levelName = "高级讲师";
                    }
                    else if(key == "2") {
                        levelName = "资深讲师";
                    }
                    else {
                        levelName = "特级讲师";
                    }
                    var teachers = datas.get(key);
                    var length = teachers.length;
                    html.push("<div class=\"text e_strong e_blue\">"+levelName+"</div>");
                    html.push("<div class=\"fn\">");
                    html.push("<ul>");
                    html.push("<li><span>人数："+length+"</span><span class='e_ico-unfold'></span></li>");
                    html.push("</ul>");
                    html.push("</div></div>");

                    html.push("<div class=\"l_padding l_padding-u\" style=\"display: none\">");
                    html.push("<div class=\"c_list c_list-line c_list-border c_list-space l_padding\">");
                    html.push("<ul>");
                    for(var i=0;i<length;i++) {
                        var data = teachers.get(i);
                        html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                        html.push("</div></div>");
                        html.push("<div class=\"main\" ontap='$.teacher.viewTeacherDetail(\""+data.get("TEACHER_ID")+"\");'><div class=\"title\">");
                        html.push(data.get("TEACHER_NAME"));
                        html.push("</div>");
                        html.push("<div class=\"content\">");
                        html.push("归属部门："+data.get("ORG_NAME")+"/岗位："+data.get("JOB_ROLE_NAME"));
                        html.push("</div><div class='content'>"+"担任课程："+data.get("COURSE_NAME"));
                        var type = data.get("TYPE");
                        html.push("</div><div class='content'>类型：");
                        if(type == "0") {
                            html.push("内部讲师");
                        }
                        else if(type == "1") {
                            html.push("外聘讲师");
                        }
                        var level = data.get("LEVEL");
                        html.push("</div><div class='content'>级别：");
                        if(level == "0") {
                            html.push("讲师");
                        }
                        else if(level == "1") {
                            html.push("高级讲师");
                        }
                        else if(level == "2") {
                            html.push("资深讲师");
                        }
                        else if(level == "3") {
                            html.push("特级讲师");
                        }
                        var qqNo = data.get("QQ_NO");
                        if(qqNo == null || qqNo == "undefined") {
                            qqNo = "暂无";
                        }

                        var wechatNo = data.get("WECHAT_NO");
                        if(wechatNo == null || wechatNo == "undefined") {
                            wechatNo = "暂无";
                        }
                        html.push("</div><div class='content'>"+"QQ："+qqNo+"/微信号："+wechatNo);
                        html.push("</div></div>");
                        html.push("<div class=\"side e_size-s\">");
                        html.push("<span class=\"e_ico-edit e_ico-pic-green e_ico-pic-r\" ontap='$.teacher.initChangeTeacher(\""+data.get("TEACHER_ID")+"\");'></span>");
                        html.push("</div>");
                        html.push("<div class=\"side e_size-s\">");
                        html.push("<span class=\"e_ico-delete e_ico-pic-red e_ico-pic-r\" ontap='$.teacher.deleteTeacher(\""+data.get("TEACHER_ID")+"\");'></span>");
                        html.push("</div></div></div></li>");
                    }
                    html.push("</ul>");
                    html.push("</div>");
                    html.push("</div>");
                    html.push("</div>");
                    html.push("<div class='c_space'></div>");
                });
                $.insertHtml('beforeend', $("#teachersType"), html.join(""));
            },

            deleteTeacher : function(teacherId) {
                $.ajaxPost('deleteTeacher','&TEACHER_ID='+teacherId,function(data) {
                    MessageBox.success("成功信息", "删除讲师成功", function(btn){
                        document.location.reload();
                    });
                });
            },

            viewTeacherDetail : function(teacherId) {
                $.redirect.open('redirectToTeacherDetail?TEACHER_ID='+teacherId, '讲师详情');
            },

            query : function() {
                $.ajaxPost('queryTeacher','&TEACHER_NAME='+$("#TEACHER_NAME").val()+"&COURSE_ID="+$("#COURSE_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    $.teacher.drawTeachers(rst.get("TEACHER"));
                    hidePopup('UI-popup','UI-QUERY_COND');
                });
            },

            initChangeTeacher : function(teacherId) {

                $.ajaxPost('initChangeTeacher','&TEACHER_ID='+teacherId,function(data) {
                    var trees = data.COURSE;
                    if(trees != null){
                        window["changeCourseHoldTree"].data = trees;
                        window["changeCourseHoldTree"].init();
                        window["changeCourseHoldTree"].expandByPath("-1", "●");
                    }
                    var rst = new Wade.DataMap(data);
                    var teacher = rst.get("TEACHER");
                    var type = teacher.get("TYPE");
                    if(type == "0") {
                        $("#CHANGE_EMPLOYEE_ID").val(teacher.get("EMPLOYEE_ID"));
                        $("#CHANGE_EMPLOYEE_NAME").val(teacher.get("TEACHER_NAME"));
                        $("#CHANGE_NAME_SELECT").css("display", "");
                        $("#CHANGE_NAME_INPUT").css("display", "none");
                    }
                    else if(type == "1") {
                        $("#CHANGE_NAME").val(teacher.get("TEACHER_NAME"));
                        $("#CHANGE_NAME_SELECT").css("display", "none");
                        $("#CHANGE_NAME_INPUT").css("display", "");
                    }

                    $("#CHANGE_TYPE").val(type);
                    $("#CHANGE_TEACHER_ID").val(teacher.get("TEACHER_ID"));
                    $("#CHANGE_LEVEL").val(teacher.get("LEVEL"));
                    $("#CHANGE_QQ_NO").val(teacher.get("QQ_NO"));
                    $("#CHANGE_WECHAT_NO").val(teacher.get("WECHAT_NO"));
                    $("#CHANGE_PIC").val(teacher.get("PIC"));

                    var courseIds = teacher.get("COURSE_ID");
                    var courseNames = teacher.get("COURSE_NAME");
                    $("#CHANGE_HOLD_COURSE_ID").val(courseIds);
                    $("#CHANGE_HOLD_COURSE_NAME").val(courseNames);


                    showPopup('UI-popup','UI-CHANGE_TEACHER');
                });
            },

            confirmChangeCourse : function() {
                var courses = $("#changeCourseHoldTree input");
                var selectedCourseIds = '';
                var selectedCourseNames = '';

                if(courses == null || courses.length <= 0) {
                    MessageBox.alert("没有课程可供选择，请先检查课程是否已添加");
                }

                var length = courses.length;
                var num = 0;
                for(var i=0;i<length;i++) {
                    var course = $(courses[i]);
                    if(course.attr("checked")) {
                        selectedCourseIds += course.val() + ",";
                        var courseName = $(course.parent().parent().children()[2]).attr("title");
                        selectedCourseNames += courseName+",";
                        num++;
                    }
                }
                if(num == 0) {
                    MessageBox.alert("请至少选择一项课程");
                    return;
                }
                $("#CHANGE_HOLD_COURSE_ID").val(selectedCourseIds.substring(0, selectedCourseIds.length - 1));
                $("#CHANGE_HOLD_COURSE_NAME").val(selectedCourseNames.substring(0, selectedCourseNames.length - 1));
                backPopup(document.getElementById("UI-COURSE_HOLD"));
            },

            changeTeacher : function() {
                if($.validate.verifyAll("UI-CHANGE_TEACHER")) {
                    var type = $("#TYPE").val();
                    var teacherName = $("#CHANGE_EMPLOYEE_NAME").val();
                    if(type == "0" && (teacherName == "" || teacherName == null)) {
                        $.TipBox.show(document.getElementById('CHANGE_EMPLOYEE_NAME'), "讲师姓名不能为空", "red");
                        return;
                    }

                    var name = $("#CHANGE_NAME").val();
                    if(type == "1" && (name == "" || name == null)) {
                        $.TipBox.show(document.getElementById('CHANGE_NAME'), "讲师姓名不能为空", "red");
                        return;
                    }

                    $.beginPageLoading();
                    var formData = new FormData($("#changeTeacherForm")[0]);
                    var request = new XMLHttpRequest();
                    request.open( "POST", "changeTeacher" , true );
                    request.onload = function(oEvent) {
                        $.endPageLoading();
                        if (request.status == 200) {
                            backPopup(document.getElementById("UI-CHANGE_TEACHER"));
                            MessageBox.success("成功信息", "修改讲师资料成功", function(btn){
                                document.location.reload();
                            });
                        } else {
                            MessageBox.error("修改讲师失败");
                        }

                    };
                    request.send(formData);
                }
            }
        }});
})($);