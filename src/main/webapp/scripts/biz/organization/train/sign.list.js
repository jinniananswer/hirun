(function($){
    $.extend({train:{
            index : 0,
            currentIndex : 0,
            init : function() {
                $.ajaxPost('initQuerySignList','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    var signs = rst.get("SIGN_LIST");
                    var train = rst.get("TRAIN");
                    var signStatus = train.get("SIGN_STATUS");
                    if(signStatus == "0") {
                        $("#DELETE_BUTTON").css("display", "");
                        $("#END_SIGN_BUTTON").css("display", "");
                    }
                    else {
                        $("#DELETE_BUTTON").css("display", "none");
                        $("#END_SIGN_BUTTON").css("display", "none");
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
                    MessageBox.success("终止报名成功","点击确定返回当前页面，点击取消关闭当前页面", function(btn){
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