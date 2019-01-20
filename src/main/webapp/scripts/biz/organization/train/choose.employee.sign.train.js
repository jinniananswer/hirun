(function($){
    $.extend({train:{
            train : null,
            jobs : null,
            addEmployees : ",",
            addLength : 0,
            existsEmployees : ",",
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

                $.ajaxPost('initChooseEmployeeSignTrain','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var trees = data.ORG_TREE;
                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();
                    }

                    var rst = new Wade.DataMap(data);
                    var signEmployees = rst.get("SIGN_EMPLOYEE");
                    $.train.train = rst.get("TRAIN");
                    var trainName = $("#train_name");
                    trainName.html($.train.train.get("NAME"));
                    $.train.drawSignEmployees(signEmployees);

                    $.train.jobs = rst.get("JOB_ROLE");
                    $.train.drawJobRoles();

                    var needSignEmployees = rst.get("NEED_SIGN_EMPLOYEE");
                    var mustSignEmployees = rst.get("MUST_SIGN_EMPLOYEE");

                    $.train.drawSelectEmployees(needSignEmployees);
                    $.train.drawMustSignEmployees(mustSignEmployees);
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
                    html.push("<li class=\"link\" employeeId='"+data.get("EMPLOYEE_ID")+"' ontap=\"$.train.selectEmployee(this);\">");
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
                    $.train.existsEmployees += data.get("EMPLOYEE_ID")+",";
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
                    $("#messagebox").css("display", "");
                    return;
                }
                $("#select_employees").empty();
                $("#messagebox").css("display", "none");
                var html = [];

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' employeeId='"+data.get("EMPLOYEE_ID")+"' sex='"+data.get("SEX")+"' employeeName='"+data.get("NAME")+"' ontap=\"$.train.selectEmployee(this)\"><div class=\"group\"><div class=\"content\">");
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
                        if(this.addEmployees.indexOf(","+li.attr("employeeId")+",") >= 0 || this.existsEmployees.indexOf(","+li.attr("employeeId")+",") >= 0) {
                            continue;
                        }
                        this.addEmployees += li.attr("employeeId")+",";
                        html.push("<li class=\"link\" employeeId='"+li.attr("employeeId")+"'>");
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
                        html.push("<div class=\"content\" ontap='$.train.deleteNewEmployee(this);'>");
                        html.push("<span class=\"e_tag e_tag-orange\">");
                        html.push("移除");
                        html.push("</span>");
                        html.push("</div>");
                        html.push("</div>");
                        html.push("</li>");
                        this.addLength++;

                    }
                }
                $("#new_num").empty();
                $("#new_num").html("人数："+this.addLength);
                $.insertHtml('beforeend', $("#new_employees"), html.join(""));
                hidePopup('UI-popup','UI-popup-query-cond');
            },

            drawMustSignEmployees : function(datas) {
                if(datas == null || datas.length <= 0) {
                    return;
                }
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class=\"link\" employeeId='"+data.get("EMPLOYEE_ID")+"'>");
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
                    this.addEmployees += data.get("EMPLOYEE_ID") + ",";
                    this.addLength++;
                }

                $.insertHtml('beforeend', $("#new_employees"), html.join(""));
                $("#new_num").empty();
                $("#new_num").html("人数："+this.addLength);
            },

            deleteNewEmployee : function(obj) {
                var li = $(obj).parent().parent();
                var employeeId = li.attr("employeeId");
                var array = this.addEmployees.split(",");
                var length = array.length;
                var newEmployeeIds = ",";
                var num = 0;
                for(var i=0;i<length;i++) {
                    var newEmployeeId = array[i];
                    if(newEmployeeId.length == 0) {
                        continue;
                    }
                    if(newEmployeeId == employeeId) {
                        continue;
                    }

                    newEmployeeIds += newEmployeeId + ",";
                    num++;
                }
                this.addEmployees = newEmployeeIds;
                this.addLength = num;
                $("#new_num").empty();
                $("#new_num").html("人数："+this.addLength);

                li.remove();
            },

            submitAdd : function() {
                if(this.addEmployees.length <= 1) {
                    MessageBox.alert("您没有新增任何人员");
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

                var parameter = '&DEL_EMPLOYEE_ID='+ employeeIds + "&TRAIN_ID="+$("#TRAIN_ID").val();

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