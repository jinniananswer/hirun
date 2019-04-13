(function($){
    $.extend({train:{
            index : 0,
            currentIndex : 0,
            addEmployees : ",",
            existsEmployees : ",",
            addLength : 0,
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

                $.ajaxPost('initQuerySignList','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
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
                    $.train.drawSigns(signs);
                });
            },

            drawSigns : function(datas){
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
                        $.train.existsEmployees += data.get("EMPLOYEE_ID") + ",";
                        html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                        html.push("</div></div>");
                        html.push("<div class=\"main\"><div class=\"title\">");
                        html.push(data.get("NAME"));
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
                        html.push("<div class=\"content\">");
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
                        var educationLevelName = data.get("EDUCATION");
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

                        var inDateDiff = data.get("IN_DATE_DIFF");
                        if(inDateDiff == null || inDateDiff == "undefined") {
                            inDateDiff = "";
                        }
                        html.push("<div class=\"content content-auto\">");
                        html.push("鸿扬工作年限："+inDateDiff);
                        html.push("</div>");

                        var jobDateDiff = data.get("JOB_DATE_DIFF");
                        if(jobDateDiff == null || jobDateDiff == "undefined") {
                            jobDateDiff = "";
                        }
                        html.push("<div class=\"content content-auto\">");
                        html.push("社会工作年限："+jobDateDiff);
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
                    MessageBox.success("终止报名成功","点击确定返回当前页面，点击取消关闭当前页面", function(btn){
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
                    html.push("<li class=\"link e_center\" ontap=\"$.train.afterSelectJob(\'"+jobRole.get("CODE_VALUE")+"\',\'"+jobRole.get("CODE_NAME")+"\')\"><label class=\"group\" ><div class=\"main\">"+jobRole.get("CODE_NAME")+"</div></label></li>");
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
                        html.push("<li class=\"link e_center\" ontap=\"$.train.afterSelectJob(\'"+jobRole.get("CODE_VALUE")+"\',\'"+jobRole.get("CODE_NAME")+"\')\"><label class=\"group\" ><div class=\"main\">"+jobRole.get("CODE_NAME")+"</div></label></li>");
                }
                $.insertHtml('beforeend', $("#jobRoles"), html.join(""));
            },

            query : function() {
                $.beginPageLoading();
                var parameter = $.buildJsonData("queryArea");
                $.ajaxPost('queryWantSignEmployee',parameter,function(data) {
                    var rst = new Wade.DataMap(data);
                    var employees = rst.get("EMPLOYEE");
                    $.train.drawSelectEmployees(employees);
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
                    html.push("<li class='link' employeeId='"+data.get("EMPLOYEE_ID")+"' sex='"+data.get("SEX")+"' employeeName='"+data.get("NAME")+"' ontap=\"$.train.selectEmployee(this)\"><div class=\"group\"><div class=\"content\">");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    var allOrgName = data.get("ALL_ORG_NAME");
                    html.push(allOrgName);
                    html.push("</div><div class='content'>"+data.get("JOB_ROLE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#select_employees"), html.join(""));
                $.endPageLoading();
            },

            selectEmployee : function(obj) {
                var li = $(obj);
                var className = li.attr("class");
                if(className == "link checked") {
                    li.attr("class", "link");
                }
                else {
                    li.attr("class", "link checked");
                }
            },

            export : function() {
                var trainId = $("#TRAIN_ID").val();
                window.location.href = "/exportSignList?TRAIN_ID=" + trainId;
            },

            confirmSelectEmployee : function() {
                var lis = $("#select_employees li");

                if(lis == null || lis.length <= 0) {
                    MessageBox.alert("您没有选中任何员工，请先选择员工");
                    return;
                }

                var length = lis.length;
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        if(this.existsEmployees.indexOf(","+li.attr("employeeId")+",") >= 0) {
                            continue;
                        }
                        this.addEmployees += li.attr("employeeId")+",";
                        this.addLength++;
                    }
                }
                if(this.addLength <= 0) {
                    MessageBox.alert("您没有选中任何要增加的员工，或者所选的员工已经报过名了，请重新选择");
                    return;
                }
                var employeeIds = this.addEmployees.substring(1,this.addEmployees.length - 1);
                var parameter = '&NEW_EMPLOYEE_ID='+employeeIds+"&TRAIN_ID="+$("#TRAIN_ID").val();
                $.beginPageLoading();
                $.ajaxPost('signNewEmployee', parameter, function (data) {
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
            }
        }});
})($);