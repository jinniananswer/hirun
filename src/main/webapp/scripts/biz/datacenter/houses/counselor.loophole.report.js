(function($){
    $.extend({counselorReport:{
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('queryCounselorLoopholeReport','',function(data){
                    var rst = new Wade.DataMap(data);
                    var loophole = rst.get("LOOPHOLE");
                    var num = rst.get("NUM");
                    var companys = rst.get("COMPANYS");
                    $("#loophole_num").empty();
                    html = [];
                    html.push("缺口总数："+num);
                    $.insertHtml('beforeend', $("#loophole_num"), html.join());
                    $.counselorReport.drawCompanys(companys);
                    $.counselorReport.drawPieChart(num, loophole);
                });
            },

            drawCompanys : function(companys){
                if(companys != null){
                    var length = companys.length;
                    var html=[];
                    for(var i=0;i<length;i++){
                        var company = companys.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.counselorReport.afterSelectCompany(\'"+company.get("ORG_ID")+"\',\'"+company.get("NAME")+"\')\"><div class=\"main\">"+company.get("NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#COMPANY"), html.join(""));
                }
            },

            afterSelectCompany : function(orgId, name){
                $.ajaxPost('queryCounselorLoopholeReport','&ORG_ID='+orgId,function(data){
                    var rst = new Wade.DataMap(data);
                    var num = rst.get("NUM");
                    var loophole = rst.get("LOOPHOLE");
                    $("#company_name").empty();
                    var html = [];
                    html.push(name+"家装顾问缺口数统计");
                    $.insertHtml('beforeend', $("#company_name"), html.join(""));
                    $("#loophole_num").empty();
                    html = [];
                    html.push("缺口总数："+num);
                    $.insertHtml('beforeend', $("#loophole_num"), html.join());
                    $.counselorReport.drawPieChart(num, loophole);
                });
                hidePopup('UI-popup','UI-popup-query-cond');
            },

            drawPieChart : function(num, loophole){
                var data = [];
                var seriesDatas = [];
                if(loophole != null && loophole.length > 0){
                    for(var i=0;i<loophole.length;i++){
                        var hole = loophole.get(i);
                        var nameValue = hole.get("NAME")+":"+hole.get("LOOPHOLE");
                        data.push(nameValue);
                        seriesDatas.push({
                            name:nameValue,
                            value:hole.get("LOOPHOLE")
                        });
                    }
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
                    grid :{
                        x:70,
                        y2:140,
                        x2:10,
                        bottom:'50%'
                    },
                    "tooltip": {
                        "trigger": "item",
                        "formatter": "{b} ({d}%)"
                    },
                    "series": [
                        {
                            "avoidLabelOverlap": false,
                            "radius": "55%",
                            "center":['50%','50%'],
                            "label": {
                                "normal":{
                                    "show":true
                                },
                                "textStyle": {
                                    "fontWeight": "bold",
                                    "fontSize": 9
                                }
                            },
                            "type": "pie",
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