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
                    //var parameter = $.buildJsonData("queryArea");
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
                    return;
                }

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

                    if(2==type ||3==type){
                    html.push("<li>")
                    html.push("<span class=\"label\">"+"综合成绩："+"</span>")
                    if(score==null){
                    html.push("<span class=\"value e_red\">"+"未录入成绩"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+score+" 分"+"</span>")
                    }
                    html.push("</li>")
                    }else {
                        if(needComm=="TRUE"){
                            html.push("<li>")
                            html.push("<span class=\"label\">"+"通用成绩："+"</span>")
                            if(scoreItem0==null  ){
                                html.push("<span class=\"value e_red\">"+"未录入成绩"+"</span>")
                            }
                            else {

                                html.push("<span class=\"value \">"+scoreItem0+" 分"+"</span>")
                            }
                            html.push("</li>")
                        }
                        if(needPro=="TRUE"){
                            html.push("<li>")
                            html.push("<span class=\"label\">"+"专业成绩："+"</span>")
                            if(scoreItem1==null){
                                html.push("<span class=\"value e_red\">"+"未录入成绩"+"</span>")
                            }
                            else {
                                html.push("<span class=\"value \">"+scoreItem1+" 分"+"</span>")
                            }
                            html.push("</li>")
                        }

                    }

                    html.push("</ul>")

                    html.push("</div></div>")
                }




                $.insertHtml('beforeend', $("#scores"), html.join(""));
            },


        }});
})($);