(function($){
    $.extend({score:{
            jobs : null,
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
                    var datas = rst.get("DATAS");
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
                        var datas = rst.get("DATAS");
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
                $("#tip").css("display","");

                var length = datas.length;

                $("#tip").empty();
                $.insertHtml('beforeend', $("#tip"), "共查询到"+length+"条数据");
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var score=data.get("SCORE");
                    var scoreItem0=data.get("ITEM_0");
                    var scoreItem1=data.get("ITEM_1");
                    var type=data.get("TYPE");
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
                        html.push("</span>")
                        html.push("</li>")
                    } else {
                        if(needComm=="TRUE"){
                            html.push("<li >")
                            html.push("<span class=\"label\">"+"通用成绩："+"</span>")
                            if(scoreItem0!=null){
                                html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID")+"_0" + "' nullable=\"yes\" value='" + scoreItem0 + "'  desc=\"课程内容\"   />");

                            }else{
                                html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID")+"_0" + "'+\"_0\" nullable=\"yes\"   desc=\"课程内容\" />");
                            }
                            html.push("<li>")
                        }
                        if(needPro=="TRUE"){
                            html.push("<li>")
                            html.push("<span class=\"label\">"+"专业成绩："+"</span>")
                            if(scoreItem1!=null){
                                html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID")+"_1" + "'nullable=\"yes\" value='" + scoreItem1 + "'  desc=\"课程内容\"   />");

                            }else{
                                html.push("<input type=\"text\" id='" + data.get("EMPLOYEE_ID") + "' name='" + data.get("EMPLOYEE_ID") +"_1"+ "' nullable=\"yes\"   desc=\"课程内容\" />");

                            }
                            html.push("<li>")

                        }
                    }
                    html.push("</ul>")

                    html.push("</div></div>")
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