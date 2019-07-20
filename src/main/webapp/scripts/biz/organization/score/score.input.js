(function($){
    $.extend({score:{
            jobs : null,
            companyPass : null,
            totalComm : 0,
            totalPro : 0,
            passComm : 0,
            passPro : 0,
            totalComposite : 0,
            passComposite : 0,
            init : function(){
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


                $.ajaxPost('initScoreQuery','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data){

                    var trees = data.ORG_TREE;

                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();
                    }
                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("COMPANY_SCORES");
                    $.score.companyPass = rst.get("COMPANY_PASS");
                    $.score.totalComm = rst.get("TOTAL_COMM");
                    if ($.score.totalComm == null || $.score.totalComm == "undefined") {
                        $.score.totalComm = 0;
                    }
                    $.score.totalPro = rst.get("TOTAL_PRO");
                    if ($.score.totalPro == null || $.score.totalPro == "undefined") {
                        $.score.totalPro = 0;
                    }
                    $.score.passComm = rst.get("PASS_COMM");
                    if ($.score.passComm == null || $.score.passComm == "undefined") {
                        $.score.passComm = 0;
                    }
                    $.score.passPro = rst.get("PASS_PRO");
                    if ($.score.passPro == null || $.score.passPro == "undefined") {
                        $.score.passPro = 0;
                    }

                    $.score.totalComposite = rst.get("TOTAL_COMPOSITE");
                    if ($.score.totalComposite == null || $.score.totalComposite == "undefined") {
                        $.score.totalComposite = 0;
                    }
                    $.score.passComposite = rst.get("PASS_COMPOSITE");
                    if ($.score.passComposite == null || $.score.passComposite == "undefined") {
                        $.score.passComposite = 0;
                    }
                    $.score.drawScore(datas);

                });
            },

            query : function(){
                if($.validate.verifyAll("queryArea")) {
                    $.beginPageLoading();
                    var name=$("#NAME").val();
                    var org_id=$("#ORG_ID").val();
                    var train_id=$("#TRAIN_ID").val();

                    $.ajaxPost('queryPostJobScore', '&TRAIN_ID='+train_id+'&NAME='+name+'&ORG_ID='+org_id, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("COMPANY_SCORES");
                        $.score.drawScore(datas);
                        hidePopup('UI-popup','UI-popup-query-cond');
                    });
                }
            },


            drawScore : function(datas){
                $.endPageLoading();
                $("#scores").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#queryMessage").css("display","");
                    $("#tip").css("display","none");
                    $("#submitButton").css("display","none");

                    //$("#submitButton").hide();
                    return;
                }
                $("#submitButton").css("display","");

                $("#queryMessage").css("display","none");

                datas.eachKey(function(key, index, totalCount){
                    html.push("<div class='c_box c_box-border'><div class='c_title' ontap=\"$(this).next().toggle();\">");
                    var scores = datas.get(key);
                    var length = scores.length;
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
                        var data = scores.get(i);
                        var score=data.get("SCORE");
                        var scoreItem0=data.get("ITEM_0");
                        var scoreItem1=data.get("ITEM_1");
                        var lateTime = data.get("LATE_TIME");
                        var money = data.get("MONEY");
                        var type=data.get("TYPE");

                        if (lateTime == null || lateTime == "undefined") {
                            lateTime = "";
                        }

                        if (money == null || money == "undefined") {
                            money = "";
                        }
                        var needComm=data.get("NEED_COMM");
                        var needPro=data.get("NEED_PRO");
                        html.push("<li class='link' ><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");

                        html.push("</div></div>");
                        html.push("<div class=\"main\"><div class=\"title\">");
                        html.push("<span class=\"e_strong \">");
                        html.push(data.get("NAME"));
                        html.push("<span>");
                        html.push("</div>");
                        html.push("<div class=\"content\">");
                        var parentOrgName = data.get("PARENT_ORG_NAME");
                        if(parentOrgName != null && parentOrgName != "undefined")
                            html.push(parentOrgName + "-");
                        html.push(data.get("ORG_NAME"));
                        html.push("</div><div class='content'>");
                        html.push("<ul>")

                        html.push("<li>")
                        html.push("<span class=\"label\">"+"培训名称："+"</span>")
                        var train_name=data.get("TRAIN_NAME");
                        html.push("<span class=\"value \">"+train_name+""+"</span>")
                        html.push("</li>")

                        if(2==type ||3==type) {

                            html.push("<li>")
                            html.push("<span class=\"label\">" + "综合成绩：" + "</span>")
                            html.push("<span class=\'value\'>")
                            var score = data.get("SCORE")
                            if (score != null) {
                                html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID") + "' nullable=\"yes\" value='" + data.get("SCORE") + "'  desc=\"课程内容\"   />");
                                //onblur="$.score.checkScore($('#userName').val())"

                            } else {
                                html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID") + "' nullable=\"yes\"   desc=\"课程内容\" />");
                            }
                            html.push("</span>");
                            html.push("</li>");

                            html.push("<li>");
                            html.push("<span class=\"label\">"+"违纪行为："+"</span>");
                            html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "_late' name='" + data.get("EMPLOYEE_ID")+"_late" + "'nullable=\"yes\" value='" + lateTime + "'  desc=\"违纪行为\"   />");
                            html.push("</li>");

                            html.push("<li>");
                            html.push("<span class=\"label\">"+"罚款金额："+"</span>");
                            html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "_money' name='" + data.get("EMPLOYEE_ID")+"_money" + "'nullable=\"yes\" value='" + money + "'  desc=\"罚款金额\"   />");
                            html.push("</li>");
                        } else {
                            if(needComm=="TRUE"){
                                html.push("<li >")
                                html.push("<span class=\"label\">"+"通用成绩："+"</span>")
                                if(scoreItem0!=null){
                                    html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID")+"_0" + "' nullable=\"yes\" value='" + scoreItem0 + "'  desc=\"课程内容\"   />");

                                }else{
                                    html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID")+"_0" + "'+\"_0\" nullable=\"yes\"   desc=\"课程内容\" />");
                                }
                                html.push("</li>")
                            }
                            if(needPro=="TRUE"){
                                html.push("<li>")
                                html.push("<span class=\"label\">"+"专业成绩："+"</span>")
                                if(scoreItem1!=null){
                                    html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID")+"_1" + "'nullable=\"yes\" value='" + scoreItem1 + "'  desc=\"课程内容\"   />");

                                }else{
                                    html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID") +"_1"+ "' nullable=\"yes\"   desc=\"课程内容\" />");

                                }
                                html.push("</li>");

                            }

                            html.push("<li>");
                            html.push("<span class=\"label\">"+"迟到分钟："+"</span>");
                            html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "_late' name='" + data.get("EMPLOYEE_ID")+"_late" + "'nullable=\"yes\" value='" + lateTime + "'  desc=\"迟到分钟\"   />");
                            html.push("</li>");

                            html.push("<li>");
                            html.push("<span class=\"label\">"+"罚款金额："+"</span>");
                            html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "_money' name='" + data.get("EMPLOYEE_ID")+"_money" + "'nullable=\"yes\" value='" + money + "'  desc=\"罚款金额\"   />");
                            html.push("</li>");
                        }
                        html.push("</ul>");

                        html.push("</div></div>");
                    }
                    html.push("</ul>");
                    html.push("</div>");

                    if ($.score.companyPass != null && $.score.companyPass != "undefined") {
                        var companyTotalComm = $.score.companyPass.get(key + "_COMM_TOTAL");
                        if (companyTotalComm == null || companyTotalComm == "undefined") {
                            companyTotalComm = 0;
                        }

                        var companyTotalPro = $.score.companyPass.get(key + "_PRO_TOTAL");
                        if (companyTotalPro == null || companyTotalPro == "undefined") {
                            companyTotalPro = 0;
                        }

                        var companyPassComm = $.score.companyPass.get(key + "_COMM_PASS");
                        if (companyPassComm == null || companyPassComm == "undefined") {
                            companyPassComm = 0;
                        }

                        var companyPassPro = $.score.companyPass.get(key + "_PRO_PASS");
                        if (companyPassPro == null || companyPassPro == "undefined") {
                            companyPassPro = 0;
                        }

                        var companyTotalComposite = $.score.companyPass.get(key + "_COMPOSITE_TOTAL");
                        if (companyTotalComposite == null || companyTotalComposite == "undefined") {
                            companyTotalComposite = 0;
                        }

                        var companyPassComposite = $.score.companyPass.get(key + "_COMPOSITE_PASS");
                        if (companyPassComposite == null || companyPassComposite == "undefined") {
                            companyPassComposite = 0;
                        }


                        if (companyTotalComm > 0) {
                            html.push("<div class='c_list l_padding'><ul><li class='link'>");
                            var pass = companyPassComm / companyTotalComm;
                            pass = pass * 100;
                            pass = pass.toFixed(2);
                            html.push(key+"通用成绩通过率："+pass+"%");
                            html.push("</li></ul></div>");
                        }

                        if (companyTotalPro > 0) {
                            html.push("<div class='c_list l_padding'><ul><li class='link'>");
                            var pass = companyPassPro / companyTotalPro;
                            pass = pass * 100;
                            pass = pass.toFixed(2);
                            html.push(key+"专业成绩通过率："+pass+"%");
                            html.push("</li></ul></div>");
                        }

                        if (companyTotalComposite > 0) {
                            html.push("<div class='c_list l_padding'><ul><li class='link'>");
                            var pass = companyPassComposite / companyTotalComposite;
                            pass = pass * 100;
                            pass = pass.toFixed(2);
                            html.push(key+"综合成绩通过率："+pass+"%");
                            html.push("</li></ul></div>");
                        }
                    }

                    html.push("</div>");
                    html.push("</div>");
                    html.push("<div class='c_space'></div>");
                });


                if (this.totalComm > 0) {
                    html.push("<div class='c_list c_list-border l_padding'><ul><li class='link'>");
                    var pass = this.passComm / this.totalComm;
                    pass = pass * 100;
                    pass = pass.toFixed(2);
                    html.push("通用成绩总通过率："+pass+"%");
                    html.push("</li></ul></div>");
                    html.push("<div class='c_space'></div>");
                }

                if (this.totalPro > 0) {
                    html.push("<div class='c_list c_list-border l_padding'><ul><li class='link'>");
                    var pass = this.passPro / this.totalPro;
                    pass = pass * 100;
                    pass = pass.toFixed(2);
                    html.push("专业成绩总通过率："+pass+"%");
                    html.push("</li></ul></div>");
                    html.push("<div class='c_space'></div>");
                }

                if (this.totalComposite > 0) {
                    html.push("<div class='c_list c_list-border l_padding'><ul><li class='link'>");
                    var pass = this.passComposite / this.totalComposite;
                    pass = pass * 100;
                    pass = pass.toFixed(2);
                    html.push("综合成绩总通过率："+pass+"%");
                    html.push("</li></ul></div>");
                    html.push("<div class='c_space'></div>");
                }


                $.insertHtml('beforeend', $("#scores"), html.join(""));
            },

            checkScore: function(score){
               // alert(score);
                var reg=/^\+?[1-9][0-9]*$/;
                if(!reg.test(score)){
                    alert("输入分数不满足条件");
                }
            },


            submit : function() {
                if($.validate.verifyAll("submitarea")) {
                    var parameter = $.buildJsonData("submitarea");
                    $.beginPageLoading();
                    $.ajaxPost('inputScore', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("成绩录入成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
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