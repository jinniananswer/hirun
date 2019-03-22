(function($){
    $.extend({prework:{
            train : null,
            jobs : null,
            addEmployees : null,
            existsEmployees : null,
            init : function() {
                this.addEmployees = new Wade.DatasetList();
                this.existsEmployees = new Wade.DatasetList();
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

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "evaluation_type_parent",
                    // 参数设置
                    {
                        id:"EXAM_TYPE_PARENT",
                        name:"EXAM_TYPE_PARENT",
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

                $("#EXAM_TYPE_PARENT").bind("change", function(){
                    if(this.value == "1" || this.value == "3") {
                        $("#EXAM_ITEM_PARENT").css("display", "");
                    }
                    else{
                        $("#EXAM_ITEM_PARENT").css("display", "none")
                    }
                });

                $.ajaxPost('initChooseEmployeeSignPreWork','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var trees = data.ORG_TREE;
                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();
                    }

                    var rst = new Wade.DataMap(data);
                    var signEmployees = rst.get("SIGN_EMPLOYEE");
                    $.prework.train = rst.get("TRAIN");
                    var trainName = $("#train_name");
                    trainName.html($.prework.train.get("NAME"));

                    var signStatus = $.prework.train.get("SIGN_STATUS");
                    if (signStatus == "1") {
                        $("#addArea").css("display", "none");
                        $("#addPreworkEmployee").css("display", "none");
                        $("#delPreworkEmployee").css("display", "none");
                    }
                    $.prework.drawSignEmployees(signEmployees);

                    $.prework.jobs = rst.get("JOB_ROLE");
                    $.prework.drawJobRoles();

                    var needSignEmployees = rst.get("NEED_SIGN_EMPLOYEE");
                    var mustSignEmployees = rst.get("MUST_SIGN_EMPLOYEE");

                    $.prework.drawSelectEmployees(needSignEmployees);
                    $.prework.drawMustSignEmployees(mustSignEmployees);
                });
            },

            drawSignEmployees : function(datas){
                $("#employees").empty();

                if(datas == null || datas.length <= 0) {
                    return;
                }
                var html = [];

                var length = datas.length;
                var signedNum = $("#signed_num");
                signedNum.empty();
                signedNum.html("人数："+length);
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class=\"link\" employeeId='"+data.get("EMPLOYEE_ID")+"' ontap=\"$.prework.selectSignedEmployee(this);\">");
                    html.push("<div class='c_space-2'></div>");
                    html.push("<div class=\"pic\">");
                    var sex = data.get("SEX");
                    if(sex == "1") {
                        html.push("<img src=\"/frame/img//male.png\" alt=\"\" class='e_pic-r' />");
                    }
                    else {
                        html.push("<img src=\"/frame/img/female.png\" alt=\"\" class='e_pic-r' />");
                    }
                    html.push("</div>");
                    html.push("<div class=\"main\">");
                    html.push("<div class=\"content content-auto\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");

                    html.push("<div class='content content-auto'>");
                    html.push("<span class=\"e_tag e_tag-green\">");
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

                    html.push("</div>");
                    html.push("</li>");
                    var map = new Wade.DataMap();
                    map.put("EMPLOYEE_ID", data.get("EMPLOYEE_ID"));
                    map.put("EXAM_TYPE", data.get("EXAM_TYPE"));
                    map.put("EXAM_ITEM", data.get("EXAM_ITEM"));
                    $.prework.existsEmployees.add(map);
                }

                $.insertHtml('beforeend', $("#employees"), html.join(""));
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
                    $("#messagebox").css("display", "");
                    return;
                }
                $("#select_employees").empty();
                $("#messagebox").css("display", "none");
                var html = [];

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' employeeId='"+data.get("EMPLOYEE_ID")+"' sex='"+data.get("SEX")+"' employeeName='"+data.get("NAME")+"' examType='0'><div class=\"group\"><div class=\"content\">");
                    html.push("<div class=\"main\" onclick=\"$.prework.selectEmployee(event, this);\"><div class=\"title\">");
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

                    html.push("<div class=\"content\">");
                    html.push("<input type='checkbox' id='CANTEEN_"+data.get("EMPLOYEE_ID")+"' name='CANTEEN_"+data.get("EMPLOYEE_ID")+"' checked/>在食堂就餐");
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

            selectEmployee : function(e,obj) {
                var src = e.srcElement.tagName;
                if(src == "INPUT" || src == "A") {
                    return;
                }
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
                    //this.selectEmployee(obj);
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

            showExamParent : function(obj) {
                var li = $($(obj).parent()).parent();
                $("#EXAM_EMPLOYEE_ID_PARENT").val(li.attr("employeeId"));
                var examType = li.attr("examType");
                $("#EXAM_TYPE_PARENT").val(examType);
                $("#EXAM_TYPE_PARENT").trigger("change");
                var inCanteen = li.attr("inCanteen");

                if(inCanteen == "1") {
                    $("#IN_CANTEEN").attr("checked", true);
                }

                var examItemComm = li.attr("examItemComm");
                var examItemPro = li.attr("examItemPro");
                if (examItemComm == "true" || typeof(examItemComm) == "undefined") {
                    $("#EXAM_ITEM_COMM_PARENT").attr("checked", "true");
                }
                else {
                    $("#EXAM_ITEM_COMM_PARENT").removeAttr("checked");
                }

                if (examItemPro == "true" || typeof(examItemComm) == "undefined") {
                    $("#EXAM_ITEM_PRO_PARENT").attr("checked", "true");
                }
                else {
                    $("#EXAM_ITEM_PRO_PARENT").removeAttr("checked");
                }

                showPopup('UI-popup','EXAM_SELECT_PARENT');
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

            confirmExamParent : function() {
                var examType = $("#EXAM_TYPE_PARENT").val();
                var examItemComm = $("#EXAM_ITEM_COMM_PARENT").attr("checked");
                var examItemPro = $("#EXAM_ITEM_PRO_PARENT").attr("checked");

                var employeeId = $("#EXAM_EMPLOYEE_ID_PARENT").val();
                var inCanteen = $("#IN_CANTEEN");
                var isInCanteen = "0";
                if(inCanteen.attr("checked")) {
                    isInCanteen = "1";
                }

                var lis = $("#new_employees li");
                var length = lis.length;
                var html = [];
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var selectEmployeeId = li.attr("employeeId");

                    if(selectEmployeeId == employeeId) {
                        li.attr("examType", examType);
                        li.attr("examItemComm", examItemComm);
                        li.attr("examItemPro", examItemPro);
                        li.attr("inCanteen", isInCanteen);

                        var main = $(li.children()[2]);
                        html.push("<div class='content content-auto' ontap='$.prework.showExamParent(this);'>");
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

                        node = $(main.children()[1]);
                        if(node != null) {
                            node.remove();
                        }
                        html.push("<div class=\"content\" ontap='$.prework.deleteNewEmployee(this);'>");
                        html.push("<span class=\"e_ico-delete e_ico-pic-r e_ico-pic-red e_ico-pic-xxs\">");
                        html.push("</span>");
                        html.push("</div>");
                        $.insertHtml('beforeend', main, html.join(""));

                        var size = this.addEmployees.length;
                        for(var i=0;i<size;i++) {
                            var addEmployee = this.addEmployees.get(i);
                            var addEmployeeId = addEmployee.get("EMPLOYEE_ID");
                            if(addEmployeeId == employeeId) {
                                addEmployee.put("EXAM_TYPE", li.attr("examType"));
                                addEmployee.put("EXAM_ITEM_COMM", li.attr("examItemComm"));
                                addEmployee.put("EXAM_ITEM_PRO", li.attr("examItemPro"));
                                addEmployee.put("IN_CANTEEN", li.attr("inCanteen"));
                            }
                        }
                        break;
                    }

                }
                hidePopup('UI-popup', 'EXAM_SELECT_PARENT');

            },

            confirmSelectEmployee : function() {
                var lis = $("#select_employees li");

                if(lis == null || lis.length <= 0) {
                    MessageBox.alert("您没有选中任何员工，请先选择员工");
                    return;
                }

                var length = lis.length;
                var html = [];
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        if(this.isExists(li.attr("employeeId"))){
                            continue;
                        }
                        var examType = li.attr("examType");
                        var examItemComm = li.attr("examItemComm");
                        var examItemPro = li.attr("examItemPro");

                        var inCanteen = $("#CANTEEN_"+li.attr("employeeId"));
                        var isInCanteen = "1";
                        if(!inCanteen.attr("checked")){
                            isInCanteen = "0";
                        }
                        html.push("<li class=\"link\" employeeId='"+li.attr("employeeId")+"' examType='"+examType+"' examItemComm='"+examItemComm+"' examItemPro='"+examItemPro+"' inCanteen='"+isInCanteen+"'>");
                        html.push("<div class='c_space-2'></div>");
                        html.push("<div class=\"pic\">");
                        var sex = li.attr("sex");
                        if(sex == "1") {
                            html.push("<img src=\"/frame/img//male.png\" alt=\"\" class='e_pic-r' />");
                        }
                        else {
                            html.push("<img src=\"/frame/img/female.png\" alt=\"\" class='e_pic-r' />");
                        }
                        html.push("</div>");
                        html.push("<div class=\"main\">");
                        html.push("<div class=\"content content-auto\">");
                        html.push(li.attr("employeeName"));
                        html.push("</div>");

                        html.push("<div class=\"content content-auto\" ontap='$.prework.showExamParent(this);'>");
                        html.push("<span class=\"e_tag e_tag-green\">");
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
                        if (examType == "1" || examType == "3") {
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

                            html.push("</span>");
                        }
                        html.push("</div>");


                        html.push("<div class=\"content\" ontap='$.prework.deleteNewEmployee(this);'>");
                        html.push("<span class=\"e_ico-delete e_ico-pic-r e_ico-pic-red e_ico-pic-xxs\">");
                        html.push("</span>");
                        html.push("</div>");
                        html.push("</div>");
                        html.push("</li>");
                        var map = new Wade.DataMap();
                        map.put("EMPLOYEE_ID", li.attr("employeeId"));
                        map.put("EXAM_TYPE", li.attr("examType"));
                        map.put("EXAM_ITEM_COMM", li.attr("examItemComm"));
                        map.put("EXAM_ITEM_PRO", li.attr("examItemPro"));


                        map.put("IN_CANTEEN",isInCanteen);
                        this.addEmployees.add(map);
                    }
                }
                $("#new_num").empty();
                $("#new_num").html("人数："+this.addEmployees.length);
                $.insertHtml('beforeend', $("#new_employees"), html.join(""));
                hidePopup('UI-popup','UI-popup-query-cond');
            },

            isExists : function(employeeId) {
                var size = this.addEmployees.length;
                for(var i=0;i<size;i++) {
                    var addEmployee = this.addEmployees.get(i);
                    var addEmployeeId = addEmployee.get("EMPLOYEE_ID");
                    if(addEmployeeId == employeeId) {
                        return true;
                    }
                }

                size = this.existsEmployees.length;
                for(var i=0;i<size;i++) {
                    var existsEmployee = this.existsEmployees.get(i);
                    var existsEmployeeId = existsEmployee.get("EMPLOYEE_ID");
                    if(existsEmployeeId == employeeId) {
                        return true;
                    }
                }
                return false;
            },

            drawMustSignEmployees : function(datas) {
                if(datas == null || datas.length <= 0) {
                    return;
                }
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class=\"link\" employeeId='"+data.get("EMPLOYEE_ID")+"' examType='0'>");
                    html.push("<div class='c_space-2'></div>");
                    html.push("<div class=\"pic\">");
                    var sex = data.get("SEX");
                    if(sex == "1") {
                        html.push("<img src=\"/frame/img//male.png\" alt=\"\" class='e_pic-r' />");
                    }
                    else {
                        html.push("<img src=\"/frame/img/female.png\" alt=\"\" class='e_pic-r' />");
                    }
                    html.push("</div>");
                    html.push("<div class=\"main\">");
                    html.push("<div class=\"content content-auto\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("</div>");
                    html.push("</li>");
                    var map = new Wade.DataMap();
                    map.put("EMPLOYEE_ID", data.get("EMPLOYEE_ID"));
                    map.put("EXAM_TYPE", "0");
                    this.addEmployees.add(map);
                }

                $.insertHtml('beforeend', $("#new_employees"), html.join(""));
                $("#new_num").empty();
                $("#new_num").html("人数："+this.addEmployees.length);
            },

            deleteNewEmployee : function(obj) {
                var li = $(obj).parent().parent();
                var employeeId = li.attr("employeeId");
                var length = this.addEmployees.length;
                var temp = new Wade.DatasetList();
                for(var i=0;i<length;i++) {
                    var employee = this.addEmployees.get(i);
                    var newEmployeeId = employee.get("EMPLOYEE_ID");
                    if(employeeId != newEmployeeId) {
                        temp.add(employee);
                    }
                }
                this.addEmployees = temp;
                $("#new_num").empty();
                $("#new_num").html("人数："+this.addEmployees.length);

                li.remove();
            },

            submitAdd : function() {
                if(this.addEmployees.length <= 0) {
                    MessageBox.alert("您没有新增任何人员");
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

            submitDelete : function() {
                var lis = $("#employees li");

                if(lis == null || lis.length <= 0) {
                    MessageBox.alert("您没有选中任何员工，请先选择员工");
                    return;
                }

                var employeeIds = "";
                var length = lis.length;
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        var employeeId = li.attr("employeeId");
                        employeeIds += employeeId + ",";
                    }
                }

                if(employeeIds.length <= 0) {
                    MessageBox.alert("您没有选中任何员工，请先选择员工");
                    return;
                }

                employeeIds = employeeIds.substring(0, employeeIds.length - 1);

                var parameter = '&SELECTED_EMPLOYEE_ID='+ employeeIds + "&TRAIN_ID="+$("#TRAIN_ID").val();

                $.beginPageLoading();

                $.ajaxPost('deleteSignedEmployee', parameter, function (data) {
                    $.endPageLoading();
                    MessageBox.success("删除已报名人员成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });
            }

        }});
})($);