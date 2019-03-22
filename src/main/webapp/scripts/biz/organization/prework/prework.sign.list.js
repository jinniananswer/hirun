(function($){
    $.extend({prework:{
            index : 0,
            currentIndex : 0,
            existsEmployees : null,
            addEmployees : null,
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["orgTree"] = new Wade.Tree("orgTree");
                $("#orgTree").textAction(function(e, nodeData){
                    var id = nodeData.id;
                    var text = nodeData.text;
                    $("#ORG_TEXT").val(text);
                    $("#ORG_ID").val(id);

                    backPopup(document.getElementById('UI-ORG'));
                    return true;
                });

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "evaluation_type",
                    // 参数设置
                    {
                        id:"EXAM_TYPE",
                        name:"EXAM_TYPE",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"初次考评", VALUE:"0"},
                        {TEXT:"补考", VALUE:"1"},
                        {TEXT:"转岗专业考评", VALUE:"2"},
                        {TEXT:"复职考评", VALUE:"3"}
                    ]
                );

                $("#EXAM_TYPE").bind("change", function(){
                    if(this.value == "1" || this.value == "3") {
                        $("#EXAM_ITEM").css("display", "");
                    }
                    else{
                        $("#EXAM_ITEM").css("display", "none")
                    }
                });

                $.beginPageLoading();
                $.ajaxPost('initQueryPreworkSignList','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var trees = data.ORG_TREE;
                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();
                    }
                    var rst = new Wade.DataMap(data);
                    var signs = rst.get("SIGN_LIST");
                    var train = rst.get("TRAIN");
                    var signStatus = train.get("SIGN_STATUS");
                    if(signStatus == "0") {
                        $("#DELETE_BUTTON").css("display", "");
                        $("#END_SIGN_BUTTON").css("display", "");
                        $("#ADD_BUTTON").css("display", "none");
                    }
                    else {
                        $("#END_SIGN_BUTTON").css("display", "none");
                        var hasEndSignOper = rst.get("HAS_END_SIGN_OPER");
                        if(hasEndSignOper == "true") {
                            $("#ADD_BUTTON").css("display", "");
                            $("#DELETE_BUTTON").css("display", "");
                        }
                        else {
                            $("#ADD_BUTTON").css("display", "none");
                            $("#DELETE_BUTTON").css("display", "none");
                        }
                    }

                    var total = $("#total_num");
                    total.html("报名人员列表（已报名人员共："+ rst.get("TOTAL_NUM")+"人）");
                    $.prework.drawSigns(signs);
                    $.endPageLoading();
                });
            },

            drawSigns : function(datas){
                this.existsEmployees = new Wade.DatasetList();
                $("#sign_list").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");
                $("#EXPORT_SIGN_BUTTON").css("display", "");
                datas.eachKey(function(key, index, totalCount){
                    html.push("<div class='c_box c_box-border'><div class='c_title' ontap=\"$(this).next().toggle();\">");
                    var signs = datas.get(key);
                    var length = signs.length;
                    html.push("<div class=\"text e_strong e_blue\">"+key+"</div>");
                    html.push("<div class=\"fn\">");
                    html.push("<ul>");
                    html.push("<li><span>人数："+length+"</span><span class='e_ico-unfold'></span></li>");
                    html.push("</ul>");
                    html.push("</div></div>");

                    html.push("<div class=\"l_padding l_padding-u\" style=\"display: none\">");
                    html.push("<div class=\"c_list c_list-line c_list-border c_list-space l_padding\">");
                    html.push("<ul>");
                    for(var i=0;i<length;i++) {
                        var data = signs.get(i);
                        $.prework.existsEmployees.add(data);
                        html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                        html.push("</div></div>");
                        html.push("<div class=\"main\"><div class=\"title\">");
                        html.push(data.get("NAME"));
                        html.push("&nbsp;<span class=\"e_tag e_tag-green\">");
                        var type = data.get("TYPE");
                        if (type == "0") {
                            html.push("初次考评");
                        }
                        else if (type == "1") {
                            html.push("补考");
                        }
                        else if (type == "2") {
                            html.push("转岗专业考评");
                        }
                        else if (type == "3") {
                            html.push("复职考评");
                        }
                        html.push("</span>");

                        if (type == "1" || type == "3") {
                            var examItemComm = data.get("EXAM_ITEM_COMM");
                            var examItemPro = data.get("EXAM_ITEM_PRO");
                            if (examItemComm == "true") {
                                html.push("&nbsp;<span class=\"e_tag e_tag-navy\">");
                                html.push("通用");
                                html.push("</span>");
                            }

                            if (examItemPro == "true") {
                                html.push("&nbsp;<span class=\"e_tag e_tag-navy\">");
                                html.push("专业");
                                html.push("</span>");
                            }
                        }
                        html.push("</div>");

                        html.push("<div class=\"content content-auto\">");
                        html.push("性别：");
                        var sex = data.get("SEX");
                        if(sex == "1") {
                            html.push("男");
                        }
                        else {
                            html.push("女");
                        }
                        html.push("</div>");
                        html.push("<div class=\"content content-auto\">");
                        html.push("归属部门："+data.get("ALL_ORG_NAME"));
                        html.push("</div>");
                        html.push("<div class=\"content content-auto\">");
                        html.push("归属公司："+data.get("ENTERPRISE_NAME"));
                        html.push("</div>");
                        html.push("<div class=\"content content-auto\">");
                        html.push("工作岗位："+data.get("JOB_ROLE_NAME"));
                        html.push("</div>");
                        html.push("<div class=\"content content-auto\">");
                        html.push("联系电话："+data.get("MOBILE_NO"));
                        html.push("</div>");
                        var school = data.get("SCHOOL");
                        if(school == null || school == "undefined") {
                            school = "";
                        }
                        html.push("<div class=\"content content-auto\">");
                        html.push("毕业院校："+school);
                        html.push("</div>");
                        var educationLevelName = data.get("EDUCATION_LEVEL_NAME");
                        if(educationLevelName == null || educationLevelName == "undefined") {
                            educationLevelName = "";
                        }
                        html.push("<div class=\"content content-auto\">");
                        html.push("学历："+educationLevelName);
                        html.push("</div>");
                        var major = data.get("MAJOR");
                        if(major == null || major == "undefined") {
                            major = "";
                        }
                        html.push("<div class=\"content content-auto\">");
                        html.push("专业："+major);
                        html.push("</div>");

                        var certificateNo = data.get("CERTIFICATE_NO");
                        if(certificateNo == null || certificateNo == "undefined") {
                            certificateNo = "";
                        }
                        html.push("<div class=\"content content-auto\">");
                        html.push("毕业证书编号："+certificateNo);
                        html.push("</div>");

                        html.push("<div class=\"content content-auto\">");
                        html.push("入职日期："+data.get("IN_DATE"));
                        html.push("</div>");


                        var viewId = data.get("VIEW_ID");
                        var isViewNotice = "否";
                        if(viewId != null && viewId != "undefined") {
                            isViewNotice = "是";
                        }
                        html.push("<div class=\"content content-auto\">");
                        html.push("是否查看通知书："+isViewNotice);
                        html.push("</div>");

                        html.push("</div>");

                        html.push("<div class=\"side e_size-s\">");
                        html.push("<input type=\"checkbox\" value='"+data.get("EMPLOYEE_ID")+"'/>");
                        html.push("</div>");


                        html.push("</div></div></li>");
                    }
                    html.push("</ul>");
                    html.push("</div>");
                    html.push("</div>");
                    html.push("</div>");
                    html.push("<div class='c_space'></div>");
                });


                $.insertHtml('beforeend', $("#sign_list"), html.join(""));
            },

            buildData : function() {
                var employeeInputs = $("#sign_list input");
                if(employeeInputs == null || employeeInputs.length <= 0) {
                    MessageBox.alert("您没有选中需要操作的员工，请勾选员工后再进行操作");
                    return '';
                }

                var size = employeeInputs.length;
                var selectedEmployeeIds = '';
                for(var i=0;i<size;i++) {
                    var employeeInput = $(employeeInputs[i]);
                    var isChecked = employeeInput.attr("checked");
                    if(isChecked) {
                        selectedEmployeeIds += employeeInput.val() + ",";
                    }
                }

                if(selectedEmployeeIds.length > 0) {
                    selectedEmployeeIds = selectedEmployeeIds.substring(0, selectedEmployeeIds.length - 1);
                }
                return selectedEmployeeIds;
            },

            deleteSignedEmployee : function(status) {
                var selectedEmployeeIds = this.buildData();
                if(selectedEmployeeIds.length <= 0) {
                    MessageBox.alert("您没有选中需要操作的员工，请勾选员工后再进行操作");
                    return;
                }
                $.ajaxPost('deleteSignedEmployee', '&SELECTED_EMPLOYEE_ID='+selectedEmployeeIds+"&TRAIN_ID="+$("#TRAIN_ID").val(), function (data) {
                    MessageBox.success("删除成功","点击确定返回当前页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });
            },

            endSign : function() {
                $.ajaxPost('endSign', "&TRAIN_ID="+$("#TRAIN_ID").val(), function (data) {
                    MessageBox.success("生成正式报名成功","点击确定返回当前页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });
            },

            drawJobRoles : function(){
                if(this.jobs == null || this.jobs == "undefined")
                    return;

                var length = this.jobs.length;
                var html = [];
                $("#jobRoles").empty();
                for(var i=0;i<length;i++){
                    var jobRole = this.jobs.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"$.prework.afterSelectJob(\'"+jobRole.get("CODE_VALUE")+"\',\'"+jobRole.get("CODE_NAME")+"\')\"><label class=\"group\" ><div class=\"main\">"+jobRole.get("CODE_NAME")+"</div></label></li>");
                }
                $.insertHtml('beforeend', $("#jobRoles"), html.join(""));
            },

            afterSelectJob : function(value, name){
                $("#JOB_TEXT").val(name);
                $("#JOB_ROLE").val(value);
                backPopup(document.getElementById('UI-JOB'));
            },

            queryJobs : function(){
                var searchText = $("#JOB_SEARCH_TEXT").val();
                $("#jobRoles").empty();
                var length = this.jobs.length;
                var html = [];
                for(var i=0;i<length;i++){
                    var jobRole = this.jobs.get(i);
                    var name = jobRole.get("CODE_NAME");
                    if(name.indexOf(searchText) >= 0 || searchText == "")
                        html.push("<li class=\"link e_center\" ontap=\"$.prework.afterSelectJob(\'"+jobRole.get("CODE_VALUE")+"\',\'"+jobRole.get("CODE_NAME")+"\')\"><label class=\"group\" ><div class=\"main\">"+jobRole.get("CODE_NAME")+"</div></label></li>");
                }
                $.insertHtml('beforeend', $("#jobRoles"), html.join(""));
            },

            query : function() {
                $.beginPageLoading();
                var parameter = $.buildJsonData("queryArea");
                $.ajaxPost('queryWantSignEmployee',parameter,function(data) {
                    var rst = new Wade.DataMap(data);
                    var employees = rst.get("EMPLOYEE");
                    $.prework.drawSelectEmployees(employees);
                });
            },

            drawSelectEmployees : function(datas) {
                if(datas == null || datas.length <= 0) {
                    $.endPageLoading();
                    $("#messagebox_employee").css("display", "");
                    return;
                }
                $("#select_employees").empty();
                $("#messagebox_employee").css("display", "none");
                var html = [];

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' employeeId='"+data.get("EMPLOYEE_ID")+"' sex='"+data.get("SEX")+"' employeeName='"+data.get("NAME")+"' examType='0'><div class=\"group\"><div class=\"content\">");
                    html.push("<div class=\"main\" ontap=\"$.prework.selectEmployee(this)\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("入职日期："+data.get("IN_DATE"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    var parentOrgName = data.get("PARENT_ORG_NAME");
                    if(parentOrgName != null && parentOrgName != "undefined")
                        html.push(parentOrgName + "-");
                    html.push(data.get("ORG_NAME"));
                    html.push("</div><div class='content'>"+data.get("JOB_ROLE_NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("<a href='javascript:void(0)' ontap='$.prework.showOnlineScore("+data.get("EMPLOYEE_ID")+")' >在线测试成绩查看</a>");
                    html.push("</div>");
                    html.push("</div>");
                    html.push("<div class='side' ontap='$.prework.showExam(this);'>");
                    html.push("<div class='content'>");
                    html.push("<span class='e_tag e_tag-green'>初次考评</span>");
                    html.push("</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#select_employees"), html.join(""));
                $.endPageLoading();
            },

            showOnlineScore : function(employeeId) {
                $.ajaxPost('showOnlineScore', "&EMPLOYEE_ID="+employeeId, function (data) {
                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("SCORES");
                    $.prework.drawEmployeeScore("SCORE_LIST", datas);
                    forwardPopup('UI-popup','EMPLOYEE_SCORE');
                });
            },

            drawEmployeeScore : function(areaId, datas) {
                var area = $("#"+areaId);
                area.empty();

                var html = [];
                html.push("<li class=\"link\">");
                html.push("<div class=\"main\">");
                html.push("<div class=\"title\">在线测试成绩</div>");
                html.push("<div class=\"content\">");
                html.push("通用知识：");
                var score = this.findScore(1, datas);
                if(score == "未考试" || score < 80) {
                    html.push("<span class='e_red'>"+score+"</span>");
                }
                else {
                    html.push(score);
                }
                html.push("</div>");

                html.push("<div class=\"content\">");
                html.push("产品知识：");
                var score = this.findScore(2, datas);
                if(score == "未考试" || score < 80) {
                    html.push("<span class='e_red'>"+score+"</span>");
                }
                else {
                    html.push(score);
                }
                html.push("</div>");

                html.push("<div class=\"content\">");
                html.push("鸿扬介绍：");
                var score = this.findScore(3, datas);
                if(score == "未考试" || score < 80) {
                    html.push("<span class='e_red'>"+score+"</span>");
                }
                else {
                    html.push(score);
                }
                html.push("</div>");

                html.push("<div class=\"content\">");
                html.push("家装知识：");
                var score = this.findScore(4, datas);
                if(score == "未考试" || score < 80) {
                    html.push("<span class='e_red'>"+score+"</span>");
                }
                else {
                    html.push(score);
                }
                html.push("</div>");

                html.push("<div class=\"content\">");
                html.push("客户服务：");
                var score = this.findScore(5, datas);
                if(score == "未考试" || score < 80) {
                    html.push("<span class='e_red'>"+score+"</span>");
                }
                else {
                    html.push(score);
                }
                html.push("</div>");

                $.insertHtml('beforeend', area, html.join(""));

            },

            findScore : function(examId, datas) {
                if(datas == null || datas.length <= 0) {
                    return "未考试";
                }
                else {
                    var length = datas.length;
                    for(var i=0;i<length;i++) {
                        var data = datas.get(i);
                        var tempExamId = data.get("EXAM_ID");
                        if(examId == tempExamId) {
                            return data.get("SCORE");
                        }
                    }
                    return "未考试";
                }
            },

            selectEmployee : function(obj) {
                var li = $($($($(obj).parent()).parent()).parent());
                var className = li.attr("class");
                if(className == "link checked") {
                    li.attr("class", "link");
                }
                else {
                    li.attr("class", "link checked");
                }
            },

            selectSignedEmployee : function(obj) {
                var li = $(obj);
                var className = li.attr("class");
                if(className == "link checked") {
                    li.attr("class", "link");
                }
                else {
                    li.attr("class", "link checked");
                }
            },

            showExam : function(obj) {
                var li = $($($(obj).parent()).parent()).parent();
                var className = li.attr("class");
                if (className == "link") {
                    this.selectEmployee(obj);
                }
                $("#EXAM_EMPLOYEE_ID").val(li.attr("employeeId"));
                var examType = li.attr("examType");
                $("#EXAM_TYPE").val(examType);
                $("#EXAM_TYPE").trigger("change");

                var examItemComm = li.attr("examItemComm");
                var examItemPro = li.attr("examItemPro");
                if (examItemComm == "true" || typeof(examItemComm) == "undefined") {
                    $("#EXAM_ITEM_COMM").attr("checked", "true");
                }
                else {
                    $("#EXAM_ITEM_COMM").removeAttr("checked");
                }

                if (examItemPro == "true" || typeof(examItemComm) == "undefined") {
                    $("#EXAM_ITEM_PRO").attr("checked", "true");
                }
                else {
                    $("#EXAM_ITEM_PRO").removeAttr("checked");
                }

                forwardPopup('UI-popup','EXAM_SELECT');
                return false;
            },

            confirmExam : function() {
                var examType = $("#EXAM_TYPE").val();
                var examItemComm = $("#EXAM_ITEM_COMM").attr("checked");
                var examItemPro = $("#EXAM_ITEM_PRO").attr("checked");

                var employeeId = $("#EXAM_EMPLOYEE_ID").val();

                var lis = $("#select_employees li");
                var length = lis.length;
                var html = [];
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        var selectEmployeeId = li.attr("employeeId");

                        if(selectEmployeeId == employeeId) {
                            li.attr("examType", examType);
                            li.attr("examItemComm", examItemComm);
                            li.attr("examItemPro", examItemPro);
                            var main = $($(li.children()[0]).children()[0]);
                            html.push("<div class='side' ontap='$.prework.showExam(this);'>");
                            html.push("<span class='e_tag e_tag-green'>");
                            if(examType == "0") {
                                html.push("初次考评");
                            }
                            else if(examType == "1") {
                                html.push("补考");
                            }
                            else if(examType == "2") {
                                html.push('转岗专业考评');
                            }
                            else if(examType = "3") {
                                html.push("复职考评")
                            }
                            html.push("</span>");
                            if(examType == "1" || examType=="3") {
                                if(examItemComm) {
                                    html.push("&nbsp;<span class='e_tag e_tag-navy'>");
                                    html.push("通用");
                                    html.push("</span>");
                                }
                                if(examItemPro) {
                                    html.push("&nbsp;<span class='e_tag e_tag-navy'>");
                                    html.push("专业");
                                    html.push("</span>");
                                }
                            }

                            html.push("</div>");
                            var node = $(main.children()[1]);
                            if(node != null) {
                                node.remove();
                            }

                            node = $(main.children()[2]);
                            if(node != null) {
                                node.remove();
                            }
                            $.insertHtml('beforeend', main, html.join(""));
                            break;
                        }
                    }
                }
                backPopup(document.getElementById("UI-popup-query-cond"));

            },

            confirmSelectEmployee : function() {
                var lis = $("#select_employees li");

                if(lis == null || lis.length <= 0) {
                    MessageBox.alert("您没有选中任何员工，请先选择员工");
                    return;
                }

                var length = lis.length;
                this.addEmployees = new Wade.DatasetList();
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        if(this.isExists(li.attr("employeeId"))){
                            continue;
                        }

                        var map = new Wade.DataMap();
                        map.put("EMPLOYEE_ID", li.attr("employeeId"));
                        map.put("EXAM_TYPE", li.attr("examType"));
                        map.put("EXAM_ITEM_COMM", li.attr("examItemComm"));
                        map.put("EXAM_ITEM_PRO", li.attr("examItemPro"));
                        this.addEmployees.add(map);
                    }
                }

                if(this.addEmployees.length <= 0) {
                    MessageBox.alert("您没有选中任何要增加的员工，或者所选的员工已经报过名了，请重新选择");
                    return;
                }

                var parameter = '&NEW_EMPLOYEE='+this.addEmployees+"&TRAIN_ID="+$("#TRAIN_ID").val();
                $.beginPageLoading();
                $.ajaxPost('addNewPreworkSign', parameter, function (data) {
                    $.endPageLoading();

                    MessageBox.success("新增报名人员成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });

            },

            export : function() {
                var trainId = $("#TRAIN_ID").val();
                window.location.href = "/exportPreworkSignList?TRAIN_ID=" + trainId;
            },

            isExists : function(employeeId) {
                var size = this.existsEmployees.length;
                for(var i=0;i<size;i++) {
                    var existsEmployee = this.existsEmployees.get(i);
                    var existsEmployeeId = existsEmployee.get("EMPLOYEE_ID");
                    if(existsEmployeeId == employeeId) {
                        return true;
                    }
                }
                return false;
            }
        }});
})($);