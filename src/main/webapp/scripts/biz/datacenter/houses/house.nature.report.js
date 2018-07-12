(function($){
    $.extend({housesReport:{
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initHousesNatureReport','',function(data){
                    var rst = new Wade.DataMap(data);
                    var housesNum = rst.get("HOUSE_NUM");
                    var natureGroup = rst.get("NATURE_GROUP");
                    var companys = rst.get("COMPANYS");
                    $("#house_num").empty();
                    html = [];
                    html.push("楼盘总数："+housesNum);
                    $.insertHtml('beforeend', $("#house_num"), html.join());
                    $.housesReport.drawCompanys(companys);
                    $.housesReport.drawPieChart(housesNum, natureGroup);
                });
            },

            drawCompanys : function(companys){
                if(companys != null){
                    var length = companys.length;
                    var html=[];
                    for(var i=0;i<length;i++){
                        var company = companys.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesReport.afterSelectCompany(\'"+company.get("ORG_ID")+"\',\'"+company.get("NAME")+"\')\"><div class=\"main\">"+company.get("NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#COMPANY"), html.join(""));
                }
            },

            afterSelectCompany : function(orgId, name){
                $.ajaxPost('queryHousesNatureReport','&ORG_ID='+orgId,function(data){
                    var rst = new Wade.DataMap(data);
                    var housesNum = rst.get("HOUSE_NUM");
                    var natureGroup = rst.get("NATURE_GROUP");
                    $("#company_name").empty();
                    var html = [];
                    html.push(name+"楼盘统计");
                    $.insertHtml('beforeend', $("#company_name"), html.join(""));
                    $("#house_num").empty();
                    html = [];
                    html.push("楼盘总数："+housesNum);
                    $.insertHtml('beforeend', $("#house_num"), html.join());
                    $.housesReport.drawPieChart(housesNum, natureGroup);
                });
                hidePopup('UI-popup','UI-popup-query-cond');
            },

            drawPieChart : function(houseNum, natureGroup){
                var data = [];
                var seriesDatas = [];
                var html=[];
                $("#nature_detail").empty();
                if(natureGroup != null && natureGroup.length > 0){
                    for(var i=0;i<natureGroup.length;i++){
                        var nature = natureGroup.get(i);
                        var nameValue = nature.get("NATURE_NAME");
                        html.push("<li><span class=\"value e_size-l\">"+nature.get("HOUSES_NUM")+"</span>");
                        html.push("<span class=\"label\">"+nameValue+"</span></li>");
                        data.push(nameValue);
                        seriesDatas.push({
                            name:nameValue,
                            value:nature.get("HOUSES_NUM")
                        });
                    }
                    $.insertHtml('beforeend', $("#nature_detail"), html.join(""));
                }
                var chart = echarts.init(document.getElementById("myPieChart"));

                var option = {
                    "title": {
                        "text": "",
                        "subtext": "",
                        "left": "center"
                    },
                    "legend": {
                        "orient": "horizontal",
                        "data": data,
                        "left": "center"
                    },
                    "tooltip": {
                        "trigger": "item",
                        "formatter": "{b} ({d}%)"
                    },
                    "series": [
                        {
                            "radius": "55%",
                            "type": "pie",
                            "label": {
                                "normal":{
                                    "show":false
                                }
                            },
                            "data": seriesDatas
                        }
                    ],
                    "animation": true
                };
                if( option && option.series ){
                    chart.setOption(option);
                }
                chart = null;
            }
        }});
})($);