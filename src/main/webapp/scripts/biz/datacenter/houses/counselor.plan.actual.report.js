(function($){
    $.extend({counselorReport:{
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["QUERY_TYPE"] = new Wade.Segment("QUERY_TYPE",{
                    disabled:false
                });

                $("#QUERY_TYPE").change(function(){
                    var modeVal = this.value; // this.value 获取分段器组件当前值
                    $("#QUERY_TYPE").val(modeVal);
                    if(modeVal == "0") {
                        $("#COMPANY_SELECT").css("display", "none");
                        $.ajaxPost('queryCounselorPlanActualReport','&QUERY_TYPE=0',function(data){
                            var rst = new Wade.DataMap(data);
                            var counselorPlan = rst.get("COUNSELOR_PLAN");
                            var counselorActual = rst.get("COUNSELOR_ACTUAL");

                            var html=[];
                            html.push("所有分公司家装顾问计划与实际数");
                            $("#company_name").empty();
                            $.insertHtml('beforeend',$("#company_name"), html.join(""));

                            $.counselorReport.drawBarChart(counselorPlan, counselorActual);
                        });

                        hidePopup('UI-popup','UI-popup-query-cond');
                    }
                    else
                        $("#COMPANY_SELECT").css("display","");
                });
                $("#QUERY_TYPE").val("0");
                $.ajaxPost('queryCounselorPlanActualReport','',function(data){
                    var rst = new Wade.DataMap(data);
                    var counselorPlan = rst.get("COUNSELOR_PLAN");
                    var counselorActual = rst.get("COUNSELOR_ACTUAL");
                    var companys = rst.get("COMPANYS");
                    if(companys != null && companys != "undefined")
                        $.counselorReport.drawCompanys(companys);
                    $.counselorReport.drawBarChart(counselorPlan, counselorActual);
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
                $.ajaxPost('queryCounselorPlanActualReport','&QUERY_TYPE=1&ORG_ID='+orgId,function(data){
                    var rst = new Wade.DataMap(data);
                    var counselorPlan = rst.get("COUNSELOR_PLAN");
                    var counselorActual = rst.get("COUNSELOR_ACTUAL");

                    $.counselorReport.drawBarChart(counselorPlan, counselorActual);
                });
                var html=[];
                html.push(name+"家装顾问计划与实际数-按门店");
                $("#company_name").empty();
                $.insertHtml('beforeend',$("#company_name"), html.join(""));
                backPopup(document.getElementById("COMPANY"));
                hidePopup('UI-popup','UI-popup-query-cond');
            },

            drawBarChart : function(counselorPlan, counselorActual){
                var plan = [];
                var actual = [];
                var name = [];
                if(counselorPlan != null && counselorPlan.length > 0){
                    for(var i=0;i<counselorPlan.length;i++){
                        var temp = counselorPlan.get(i);
                        plan.push(temp.get("PLAN_NUM"));
                        name.push(temp.get("NAME"));
                    }
                }
                if(counselorActual != null && counselorActual.length > 0){
                    for(var i=0;i<counselorActual.length;i++){
                        var temp = counselorActual.get(i);
                        actual.push(temp.get("ACTUAL_NUM"));
                    }
                }
                var chart = echarts.init(document.getElementById("myBarChart"));
                var option = {
                    "title": {
                        "text": "",
                        "subtext": ""
                    },
                    "tooltip": {"trigger": "axis","axisPointer": {"type": "shadow"}},
                    legend: {
                        data: [
                            "计划数",
                            "实际数"
                        ]
                    },
                    grid :{
                        x:70,
                        y2:0,
                        x2:10,
                    },
                    "yAxis": [
                        {
                            "type": "category",
                            axisTick: {show: false},
                            "data": name,
                            axisLabel: {
                                interval : 0,
                                rotate : 0
                            }
                        }
                    ],
                    "xAxis": [
                        {
                            "type": "value"
                        }
                    ],
                    "series": [
                        {
                            "label": {
                                "normal": {
                                    "show": true
                                }
                            },
                            "name": "计划数",
                            "type": "bar",
                            "barGap":0,
                            "stack": "计划数",
                            "data": plan
                        },
                        {
                            "label": {
                                "normal": {
                                    "show": true
                                }
                            },
                            "name": "实际数",
                            "barGap":0,
                            "type": "bar",
                            "stack": "实际数",
                            "data": actual
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