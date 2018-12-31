(function($){
    $.extend({train:{
            index : 0,
            currentIndex : 0,
            init : function() {
                $.ajaxPost('initQuerySignList','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    var signs = rst.get("SIGN_LIST");
                    var hasEndSign = rst.get("HAS_END_SIGN");
                    if(hasEndSign == "true") {
                        $("#END_SIGN_BUTTON").css("display", "");
                    }
                    else {
                        $("#END_SIGN_BUTTON").css("display", "none");
                    }
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
                    var statusName = "";
                    if(key == "0") {
                        statusName = "待审核";
                    }
                    else if(key == "1") {
                        statusName = "审核通过";
                    }
                    else if(key == "2") {
                        statusName = "审核拒绝";
                    }
                    var signs = datas.get(key);
                    var length = signs.length;
                    html.push("<div class=\"text e_strong e_blue\">"+statusName+"</div>");
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
                        html.push("<div class=\"content\">");
                        html.push("归属部门："+data.get("ORG_NAME"));
                        html.push("</div>");
                        html.push("<div class=\"content\">");
                        html.push("归属公司："+data.get("ENTERPRISE_NAME"));
                        html.push("</div>");

                        var signStatus = data.get("STATUS");
                        if(signStatus == "0") {
                            html.push("<div class='content'>审核状态：");
                            html.push("待审核");
                            html.push("</div>");
                        }
                        else if(signStatus == "1") {
                            html.push("<div class='content'>审核状态：");
                            html.push("审核通过");
                            html.push("</div>");
                        }
                        else if(signStatus == "2") {
                            html.push("<div class='content'>审核状态：");
                            html.push("拒绝");
                            html.push("</div>");
                        }


                        html.push("</div>");

                        if(signStatus == "0") {
                            html.push("<div class=\"side e_size-s\">");
                            html.push("<input type=\"checkbox\" value='"+data.get("EMPLOYEE_ID")+"'/>");
                            html.push("</div>");
                        }

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

            audit : function(status) {
                var selectedEmployeeIds = this.buildData();
                if(selectedEmployeeIds.length <= 0) {
                    return;
                }
                $.ajaxPost('auditSignTrain', '&SELECTED_EMPLOYEE_ID='+selectedEmployeeIds+"&STATUS="+status+"&TRAIN_ID="+$("#TRAIN_ID").val(), function (data) {
                    MessageBox.success("审核成功","点击确定返回当前页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.rediret.closeCurrentPage();
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
                            $.rediret.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });
            }
        }});
})($);