(function($){
    $.extend({score:{
            jobs : null,
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
                    var datas = rst.get("DATAS");

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
                    html.push("</li>");

                    var lateTime = data.get("LATE_TIME");
                    var money = data.get("MONEY");

                    if (lateTime == null || lateTime == "undefined") {
                        lateTime = "";
                    }

                    if (money == null || money == "undefined") {
                        money = "";
                    }

                    if(2==type ||3==type){
                        html.push("<li>");
                        html.push("<span class=\"label\">"+"综合成绩："+"</span>");
                        if(score==null){
                            html.push("<span class=\"value e_red\">"+"未录入成绩"+"</span>");
                        }
                        else {
                            html.push("<span class=\"value \">"+score+" 分"+"</span>");
                        }
                        html.push("</li>");

                        html.push("<li>");
                        html.push("<span class=\"label\">"+"违纪行为："+"</span>");
                        html.push("<span class=\"value e_red\">"+lateTime+"</span>");
                        html.push("</li>");

                        html.push("<li>");
                        html.push("<span class=\"label\">"+"罚款金额："+"</span>");
                        html.push("<span class=\"value e_red\">"+money+"</span>");
                        html.push("</li>");
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

                        html.push("<li>");
                        html.push("<span class=\"label\">"+"迟到分钟："+"</span>");
                        html.push("<span class=\"value e_red\">"+lateTime+"</span>");
                        html.push("</li>");

                        html.push("<li>");
                        html.push("<span class=\"label\">"+"罚款金额："+"</span>");
                        html.push("<span class=\"value e_red\">"+money+"</span>");
                        html.push("</li>");

                    }

                    html.push("</ul>")

                    html.push("</div></div>")
                }

                var htmlPass=[];
                if (this.totalComm > 0) {
                    htmlPass.push("<div class='c_list c_list-border l_padding'><ul><li class='link'>");
                    var pass = this.passComm / this.totalComm;
                    pass = pass * 100;
                    pass = pass.toFixed(2);
                    htmlPass.push("通用成绩总通过率："+pass+"%");
                    htmlPass.push("</li></ul></div>");
                    htmlPass.push("<div class='c_space'></div>");
                }

                if (this.totalPro > 0) {
                    htmlPass.push("<div class='c_list c_list-border l_padding'><ul><li class='link'>");
                    var pass = this.passPro / this.totalPro;
                    pass = pass * 100;
                    pass = pass.toFixed(2);
                    htmlPass.push("专业成绩总通过率："+pass+"%");
                    htmlPass.push("</li></ul></div>");
                    htmlPass.push("<div class='c_space'></div>");
                }

                if (this.totalComposite > 0) {
                    htmlPass.push("<div class='c_list c_list-border l_padding'><ul><li class='link'>");
                    var pass = this.passComposite / this.totalComposite;
                    pass = pass * 100;
                    pass = pass.toFixed(2);
                    htmlPass.push("综合成绩总通过率："+pass+"%");
                    htmlPass.push("</li></ul></div>");
                    htmlPass.push("<div class='c_space'></div>");
                }



                $.insertHtml('beforeend', $("#scores"), html.join(""));
                $.insertHtml('beforeend', $("#pass"), htmlPass.join(""));
            },


        }});
})($);