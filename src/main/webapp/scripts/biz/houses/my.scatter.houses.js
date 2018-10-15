(function($){
    $.extend({housesPlan:{
            initQuery: function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");

                $.ajaxPost('queryMyScatterHouses',null,function(data){
                    var rst = new Wade.DataMap(data);
                    var dataset = rst.get("DATA");
                    $.housesPlan.drawHousesPlan(dataset);
                });
            },

            drawHousesPlan : function(dataset){
                $("#houses_plan").empty();
                if(dataset == null || dataset.length <= 0){
                    return;
                }

                var html = [];
                var size = dataset.length;
                for(var i=0;i<size;i++){
                    var data = dataset.get(i);
                    html.push("<li class='link' ontap=\"$.redirect.open('redirectToScatterDetail?HOUSES_ID="+data.get("HOUSES_ID")+"','"+data.get("NAME")+"规划详情');\">");
                    html.push("<div class=\"content\">");
                    html.push("<div class=\"group\">");
                    html.push("<div class=\"content\">");
                    html.push("<div class=\"main\">");
                    html.push("<div class=\"title \"><span class=\"e_strong\">"+data.get("NAME")+"</span>");
                    var status = data.get("STATUS");
                    if(status == "0") {
                        //html.push("<div class=\"right link\" ontap=\"$.housesPlan.initAudit(" + data.get("HOUSES_ID") + ")\"><span class=\"e_ico-select\"></span> 审核</div>");
                    }
                    //html.push("<div class=\"right link\" ontap=\"$.redirect.open('redirectToChangeHousesPlan?HOUSES_ID="+data.get("HOUSES_ID")+"','变更楼盘规划');\"><span class=\"e_ico-edit\"></span> 编辑</div>");
                    html.push("</div>");
                    html.push("<div class=\"content\">");

                    html.push("</div>");
                    html.push("<div class=\"content\"></div>");
                    html.push("</div></div></div>");
                    html.push("<div class=\"side\">");
                    html.push("<span class=\"e_ico-pic-orange e_ico-pic-r e_ico-pic-xs\">");
                    html.push(data.get("NATURE_NAME")+"</span></div>");
                    html.push("</div>");
                    html.push("<div class=\"sub\"><div class=\"main\">归属城市："+data.get("CITY_NAME")+"</div>");
                    html.push("</div>");
                    html.push("</li>");
                }
                $.insertHtml('beforeend', $("#houses_plan"), html.join(""));
            },

            query : function(){
                if(!$.validate.verifyAll("queryArea")){
                    return;
                }
                var parameter = $.buildJsonData("queryArea");
                hidePopup('UI-popup','UI-popup-query-cond');
                $.ajaxPost('queryMyScatterHouses', parameter, function (data) {
                    var rst = new Wade.DataMap(data);
                    var dataset = rst.get("DATA");
                    $.housesPlan.drawHousesPlan(dataset);
                });
            }
        }});
})($);