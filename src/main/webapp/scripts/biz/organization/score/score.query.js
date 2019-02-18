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

                var parameter = $.buildJsonData("queryArea");

                $.ajaxPost('initScoreQuery',parameter,function(data){

                    var trees = data.ORG_TREE;
                    var jobRoles = data.JOB_ROLES;
                    var citys = data.CITYS;
                    var defaultCityId = data.DEFAULT_CITY_ID;
                    var defaultCityName = data.DEFAULT_CITY_NAME;
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
                    var parameter = $.buildJsonData("queryArea");
                    $.ajaxPost('queryPostJobScore', parameter, function (data) {
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

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"培训成绩："+"</span>")

                    var score=data.get("SCORE")
                    if(score==null){
                    html.push("<span class=\"value e_red\">"+"未录入成绩"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+score+" 分"+"</span>")
                    }
                    html.push("</li>")


                    html.push("</ul>")

                    html.push("</div></div>")
                }




                $.insertHtml('beforeend', $("#scores"), html.join(""));
            },










        }});
})($);