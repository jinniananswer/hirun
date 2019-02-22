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

                $.ajaxPost('initExamQuery',null,function(data){
                    var trees = data.ORG_TREE;
                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();
                    }
                });
            },

            query : function(){
                if($.validate.verifyAll("queryArea")) {
                    $.beginPageLoading();
                    var parameter = $.buildJsonData("queryArea");
                    $.ajaxPost('queryExamScore', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.score.drawScores(datas);
                        hidePopup('UI-popup','UI-popup-query-cond');
                    });
                }
            },

            drawScores : function(datas){
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
                    var sex = data.get("SEX");
                    var exam_id_1 = data.get("EXAM_ID_1");
                    var exam_id_2 = data.get("EXAM_ID_2");
                    var exam_id_3 = data.get("EXAM_ID_3");
                    var exam_id_4 = data.get("EXAM_ID_4");
                    var exam_id_5 = data.get("EXAM_ID_5");
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
                   //html.push("归属城市："+data.get("CITY_NAME"));
                    html.push("</div><div class='content'>");
                    html.push("<ul>")
                    html.push("<li>")
                    html.push("<span class=\"label\">"+"通用知识："+"</span>")


                    if(exam_id_1 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_1 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_1")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_1")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"产品知识："+"</span>")
                    if(exam_id_2 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_2 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_2")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_2")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"鸿扬介绍："+"</span>")
                    if(exam_id_3 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_3 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_3")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_3")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"家装知识："+"</span>")
                    if(exam_id_4 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_4 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_4")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_4")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"客户服务："+"</span>")
                    if(exam_id_5 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_5 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_5")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_5")+" 分"+"</span>")
                    }                    html.push("</li>")

                    html.push("</ul>")

                    html.push("</div></div>")

                }

                $.insertHtml('beforeend', $("#scores"), html.join(""));
            },


        }});
})($);