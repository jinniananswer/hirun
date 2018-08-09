(function($){
    $.extend({employee:{
            destroyUserId : null,
            query : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.ajaxPost('queryContacts', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.employee.drawEmployees(datas);
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
                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
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
                    html.push("<div class=\"side e_size-m\" ontap=\"$.employee.destroy(\'"+data.get("USER_ID")+"\');\"><span class='e_ico-delete e_ico-pic-red e_ico-pic-r'></span></div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#employees"), html.join(""));
            },

            queryParent : function(){
                if($.validate.verifyAll("searchArea")) {
                    $.ajaxPost('queryContacts', "&SEARCH_TEXT="+$("#PARENT_EMPLOYEE_NAME").val(), function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.employee.drawParentEmployees(datas);
                    });
                }
            },

            drawParentEmployees : function(datas){
                $("#parent_employees").empty();
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
                    html.push("<li class='link' ontap=\"$.employee.selectParentEmployee('"+data.get("EMPLOYEE_ID")+"');\"><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
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

                $.insertHtml('beforeend', $("#parent_employees"), html.join(""));
            },

            destroy : function(userId){
                this.destroyUserId = userId;
                $.ajaxPost('hasSubordinates', '&USER_ID='+userId, function (data) {
                    var hasSub = data.HAS_SUB;
                    if(hasSub == "true"){
                        MessageBox.alert("提示信息", "您注销的员工拥有直属下级员工，点击确定后请为该员工的直属下级员工选择新的上级", function(btn){
                            showPopup('UI-popup','UI-PARENT');
                        });
                    }
                    else{
                        $.employee.truelyDestroy(userId, null);
                    }
                });
            },

            selectParentEmployee : function(parentEmployeeId){
                $.employee.truelyDestroy(this.destroyUserId, parentEmployeeId);
            },

            truelyDestroy : function(userId, parentEmployeeId){
                hidePopup('UI-popup','UI-PARENT');
                var param = '&USER_ID='+userId;
                if(parentEmployeeId != null && parentEmployeeId != "" && parentEmployeeId != "undefined")
                    param+="&PARENT_EMPLOYEE_ID="+parentEmployeeId;

                $.ajaxPost('destroyEmployee', param, function (data) {
                    MessageBox.success("注销员工档案成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
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