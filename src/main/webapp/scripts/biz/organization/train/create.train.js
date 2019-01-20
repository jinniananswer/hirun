(function($){
    $.extend({train:{
            index : 0,
            currentIndex : 0,
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["START_DATE"] = new Wade.DateField(
                    "START_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["END_DATE"] = new Wade.DateField(
                    "END_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["courseTree"] = new Wade.Tree("courseTree");
                window["singleCourseTree"] = new Wade.Tree("singleCourseTree");

                $.Select.append(
                    "type_select",
                    {
                        id:"TYPE",
                        name:"TYPE"
                    },
                    [
                        {TEXT:"岗前培训", VALUE:"1"},
                        {TEXT:"职前培训", VALUE:"2"},
                        {TEXT:"在职培训", VALUE:"3"}
                    ]
                );

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "COURSE_NATURE_0",
                    // 参数设置
                    {
                        id:"NATURE_0",
                        name:"NATURE_0",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"课程体系", VALUE:"0"},
                        {TEXT:"非课程体系", VALUE:"1"}
                    ]
                );

                $("#NATURE_0").bind("change", function(){
                    if(this.value == "0") {
                        $("#CONTENT_INPUT_0").css("display", "none");
                        $("#CONTENT_SELECT_0").css("display", "");
                    }
                    else{
                        $("#CONTENT_INPUT_0").css("display", "");
                        $("#CONTENT_SELECT_0").css("display", "none");
                    }
                });

                window["START_DATE_0"] = new Wade.DateField(
                    "START_DATE_0",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd HH:mm:ss",
                        useTime:true
                    }
                );
                window["END_DATE_0"] = new Wade.DateField(
                    "END_DATE_0",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd HH:mm:ss",
                        useTime:true
                    }
                );

                $("#singleCourseTree").textAction(function(e, nodeData){
                    $("#COURSE_ID_"+$.train.currentIndex).val(nodeData.id);
                    $("#COURSE_NAME_"+$.train.currentIndex).val(nodeData.text);
                    hidePopup('UI-popup','UI-SINGLE_COURSE');
                    return false;
                });

                $.ajaxPost('initCreateTrain',null,function(data) {
                    var trees = data.COURSE;
                    var singleTrees = data.SINGLE_COURSE;
                    if(trees != null){
                        window["courseTree"].data = trees;
                        window["courseTree"].init();
                        window["courseTree"].expandByPath("-1", "●");

                        window["singleCourseTree"].data = singleTrees;
                        window["singleCourseTree"].init();
                        window["singleCourseTree"].expandByPath("-1", "●");
                    }

                    var rst = new Wade.DataMap(data);
                    var teachers = rst.get("TEACHERS");
                    $.train.drawTeachers(teachers);
                });
            },

            addCourse : function() {
                this.index++;
                var html = [];
                html.push("<div class=\"c_title\">");
                html.push("<div class=\"text\">课程表安排</div>");
                html.push("<div class=\"fn\" ontap='$.train.deleteCourse(this);'>");
                html.push("<ul><li><span class=\"e_ico-delete\"></span></li></ul>");
                html.push("</div>");
                html.push("</div>");
                html.push("<div class=\"c_box c_box-gray\">");
                html.push("<div class=\"c_list c_list-col-1 c_list-fixWrapSpace c_list-form\">");
                html.push("<ul>");
                html.push("<li class=\"link required\">");
                html.push("<div class=\"label\">课程性质</div>");
                html.push("<div class=\"value\">");
                html.push("<span id=\"COURSE_NATURE_"+this.index+"\">");
                html.push("</span>");
                html.push("</div>");
                html.push("</li>");
                html.push("<li class=\"link required\" id=\"CONTENT_SELECT_"+this.index+"\" ontap=\"$('#COURSE_NAME_"+this.index+"').focus();$('#COURSE_NAME_"+this.index+"').blur();$.train.selectCourse("+this.index+")\">");
                html.push("<div class=\"label\">课程内容</div>");
                html.push("<div class=\"value\">");
                html.push("<input type=\"text\" id=\"COURSE_NAME_"+this.index+"\" name=\"COURSE_NAME_"+this.index+"\" nullable=\"yes\" readonly=\"true\" desc=\"课程内容\" />");
                html.push("<input type=\"hidden\" id=\"COURSE_ID_"+this.index+"\" name=\"COURSE_ID_"+this.index+"\" nullable=\"yes\" desc=\"课程内容\" />");
                html.push("</div>");
                html.push("<div class=\"more\"></div>");
                html.push("</li>");
                html.push("<li class=\"link required\" id=\"CONTENT_INPUT_"+this.index+"\" style=\"display:none\">");
                html.push("<div class=\"label\">课程内容</div>");
                html.push("<div class=\"value\">");
                html.push("<input type=\"text\" id=\"COURSE_CONTENT_"+this.index+"\" name=\"COURSE_CONTENT_"+this.index+"\" nullable=\"yes\" desc=\"课程内容\" />");
                html.push("</div>");
                html.push("</li>");
                html.push("<li class=\"link\" ontap=\"$('#TEACHER_NAME_"+this.index+"').focus();$('#TEACHER_NAME_"+this.index+"').blur();$.train.selectTeacher("+this.index+");\">");
                html.push("<div class=\"label\">授课讲师</div>");
                html.push("<div class=\"value\">");
                html.push("<input type=\"text\" id=\"TEACHER_NAME_"+this.index+"\" name=\"TEACHER_NAME_"+this.index+"\" nullable=\"yes\" readonly=\"true\" desc=\"授课讲师\" />");
                html.push("<input type=\"hidden\" id=\"TEACHER_ID_"+this.index+"\" name=\"TEACHER_ID_"+this.index+"\" nullable=\"yes\" desc=\"授课讲师\" />");
                html.push("</div>");
                html.push("<div class=\"more\"></div>");
                html.push("</li>");
                html.push("<li class=\"link required\">");
                html.push("<div class=\"label\">课程开始时间</div>");
                html.push("<div class=\"value\">");
                html.push("<span class=\"e_mix\">");
                html.push("<input type=\"text\" id=\"START_DATE_"+this.index+"\" name=\"START_DATE_"+this.index+"\" datatype=\"date\" readonly=\"true\" desc=\"培训开始时间\" />");
                html.push("<span class=\"e_ico-date\"></span>");
                html.push("</span>");
                html.push("</div>");
                html.push("</li>");
                html.push("<li class=\"link required\">");
                html.push("<div class=\"label\">课程结束时间</div>");
                html.push("<div class=\"value\">");
                html.push("<span class=\"e_mix\">");
                html.push("<input type=\"text\" id=\"END_DATE_"+this.index+"\" name=\"END_DATE_"+this.index+"\" datatype=\"date\" readonly=\"true\" desc=\"培训结束时间\" />");
                html.push("<span class=\"e_ico-date\"></span>");
                html.push("</span>");
                html.push("</div>");
                html.push("</li>");
                html.push("</ul>");
                html.push("</div></div>");

                $.insertHtml('beforeend', $("#courseList"), html.join(""));
                this.initNewControl();
            },

            initNewControl : function() {
                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "COURSE_NATURE_"+this.index,
                    // 参数设置
                    {
                        id:"NATURE_"+this.index,
                        name:"NATURE_"+this.index,
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"课程体系", VALUE:"0"},
                        {TEXT:"非课程体系", VALUE:"1"}
                    ]
                );

                var input = "#CONTENT_INPUT_"+this.index;
                var select = "#CONTENT_SELECT_"+this.index;

                $("#NATURE_"+this.index).bind("change", function(){
                    if(this.value == "0") {
                        $(input).css("display", "none");
                        $(select).css("display", "");
                    }
                    else{
                        $(input).css("display", "");
                        $(select).css("display", "none");
                    }
                });

                window["START_DATE_"+this.index] = new Wade.DateField(
                    "START_DATE_"+this.index,
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd HH:mm:ss",
                        useTime:true
                    }
                );
                window["END_DATE_"+this.index] = new Wade.DateField(
                    "END_DATE_"+this.index,
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd HH:mm:ss",
                        useTime:true
                    }
                );
            },

            deleteCourse : function(obj) {
                var div = $(obj).parent();
                var next = div.next();

                next.remove();
                div.remove();
            },

            confirmCourse : function() {
                var courses = $("#courseTree input");
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
                $("#COURSE_IDS").val(selectedCourseIds.substring(0, selectedCourseIds.length - 1));
                $("#COURSE_NAMES").val(selectedCourseNames.substring(0, selectedCourseNames.length - 1));
                hidePopup('UI-popup','UI-SELECT_COURSE');
            },

            selectCourse : function(idx) {
                this.currentIndex = idx;
                showPopup('UI-popup','UI-SINGLE_COURSE');
            },

            drawTeachers : function(teachers) {
                var area = $("#TEACHERS");
                area.empty();

                if(teachers == null || teachers.length <= 0) {
                    return;
                }
                var length = teachers.length;
                var html=[];
                for(var i=0;i<length;i++) {
                    var teacher = teachers.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"$.train.afterSelectTeacher(\'" + teacher.get("TEACHER_ID") + "\',\'"+teacher.get("TEACHER_NAME")+"\')\"><label class=\"group\"><div class=\"main\">" + teacher.get("TEACHER_NAME") + "</div></label></li>");
                }
                $.insertHtml('beforeend', area, html.join(""));
            },

            selectTeacher : function(idx) {
                this.currentIndex = idx;
                showPopup('UI-popup','UI-TEACHER');
            },

            afterSelectTeacher : function(teacherId, name) {
                $("#TEACHER_NAME_"+this.currentIndex).val(name);
                $("#TEACHER_ID_"+this.currentIndex).val(teacherId);
                hidePopup('UI-popup', 'UI-TEACHER');
            },

            queryEmployee : function(){
                if($.validate.verifyAll("searchArea")) {
                    var parameter = $.buildJsonData("searchArea");
                    $.ajaxPost('queryContacts', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.train.drawEmployees(datas);
                    });
                }
            },

            drawEmployees : function(datas){
                $("#employees").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var sex = data.get("SEX");
                    html.push("<li class='link' ontap=\"$.train.afterSelectEmployee(\'"+data.get("EMPLOYEE_ID")+"\',\'"+data.get("NAME")+"\')\"><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
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

            afterSelectEmployee : function(employeeId, name) {
                $("#CHARGE_EMPLOYEE_NAME").val(name);
                $("#CHARGE_EMPLOYEE_ID").val(employeeId);
                hidePopup('UI-popup', 'UI-EMPLOYEE');
            },

            submit : function() {
                if($.validate.verifyAll("trainArea")) {
                    var parameter = $.buildJsonData("allSubmitArea");
                    parameter["SCHEDULE_NUM"] = this.index+1;
                    $.beginPageLoading();
                    $.ajaxPost('createTrain', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("新增培训成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
                            if("ok" == btn) {
                                document.location.reload();
                            }
                            else {
                                $.redirect.closeCurrentPage();
                            }
                        },{"cancel":"取消"})
                    });

                }
            }
        }});
})($);