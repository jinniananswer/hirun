(function($){
    $.extend({myHouse:{
            initQuery: function(){
                $.ajaxPost('queryMyHouses',null,function(data){
                    var rst = new Wade.DataMap(data);
                    var dataset = rst.get("DATA");
                    $.myHouse.drawHouses(dataset);
                });
            },

            drawHouses : function(dataset){
                $("#houses").empty();
                if(dataset == null || dataset.length <= 0){
                    MessageBox.alert("提示信息", "亲，您还没有楼盘哦", function(btn){
                        $.redirect.closeCurrentPage();
                    });
                    return;
                }

                var html = [];
                var size = dataset.length;
                for(var i=0;i<size;i++){
                    var data = dataset.get(i);
                    html.push("<li class='link' ontap=\"$.redirect.open('redirectToMyDetail?HOUSES_ID="+data.get("HOUSES_ID")+"','"+data.get("NAME")+"规划详情');\">");
                    html.push("<div class=\"content\">");
                    html.push("<div class=\"group\">");
                    html.push("<div class=\"content\">");
                    html.push("<div class=\"main\">");
                    html.push("<div class=\"title\"><div><span class=\"e_strong\">"+data.get("NAME")+"</span></div>");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    if(data.get("PAST_CHECK_DATE") == "true")
                        html.push("<span class=\"e_progress e_progress-red\">");
                    else if(data.get("PAST_RESPONSIBILITY") == true)
                        html.push("<span class=\"e_progress e_progress-orange\">");
                    else
                        html.push("<span class=\"e_progress\">");
                    html.push("<span class=\"e_progressBar\">");
                    html.push("<span style=\"width:"+data.get("CUR_PROGRESS")+"%\" class=\"e_progressProgress\">发展周期"+data.get("ALL_DAYS")+"天×"+data.get("CUR_PROGRESS")+"%</span>");
                    html.push("</span></span>");
                    html.push("</div>");
                    html.push("<div class=\"content\">"+data.get("CITY_NAME")+"/"+data.get("AREA_NAME")+"/"+data.get("CHECK_DATE")+"交房</div>");
                    html.push("</div></div></div>");
                    html.push("<div class=\"side\">");
                    if(data.get("NATURE")=="0"){
                        html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-xs\">");
                    }
                    else if(data.get("NATURE")=="1"){
                        html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-xs\">");
                    }
                    else{
                        html.push("<span class=\"e_ico-pic-blue e_ico-pic-r e_ico-pic-xs\">");
                    }
                    html.push(data.get("NATURE_NAME")+"</span></div>");
                    html.push("</div>");
                    html.push("<div class=\"sub\"><div class=\"main\">总户数："+data.get("HOUSE_NUM")+"</div>");
                    html.push("</div>");
                    html.push("<div class=\"statu e_size-s statu-orange statu-right\">"+data.get("STATUS_NAME")+"</div>");
                    html.push("</li>");
                }
                $.insertHtml('beforeend', $("#houses"), html.join(""));
            }
        }});
})($);