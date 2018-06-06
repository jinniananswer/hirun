var EmployeeMonStatQuery = {
	init : function() {
        EmployeeMonStatQuery.queryEmployeeList();
        var now = $.date.now();
        var nowYYYYMM = $.date.now().substring(0,4) + $.date.now().substring(5,7);
        window["PLAN_DATE"] = new Wade.DateField(
            "PLAN_DATE",
            {
                value:nowYYYYMM,
                dropDown:true,
                format:"yyyyMM",
                useTime:false,
                useMode:'month',
            }
        );
        $('#PLAN_DATE').val(nowYYYYMM);
        $("#PLAN_DATE").bind("afterAction", function(){
            var $obj = $(this);
            EmployeeMonStatQuery.refreshMonStat($obj.val(), $obj.attr('employee_Id'));
        });
    },
    queryEmployeeList : function() {
        var data = {};
        data.EMPLOYEE_LIST = [];
        data.EMPLOYEE_LIST.push({EMPLOYEE_NAME : '小安', EMPLOYEE_ID : '157'})
        $('#employee_list').html(template("employee_template", data));
    },
    clickEmployee : function(obj) {
        $obj = $(obj);
        $obj.next().toggle();
        var isQuery = $obj.next().attr('is_query');

        if(isQuery == 'false') {
            $obj.next().attr('is_query', "true");
            var employeeId = $obj.attr('employee_id');
            var statMon = $('#PLAN_DATE').val();
            EmployeeMonStatQuery.refreshMonStat(statMon, employeeId);
        }
    },
    refreshMonStat : function(statMon, employeeId) {
        $.ajaxReq({
            url : 'plan/queryEmployeeMonStat',
            data : {
                PLAN_EXECUTOR_ID : employeeId,
                STAT_MON : statMon,
            },
            successFunc : function(data) {
                var chart = echarts.init(document.getElementById("stat_mon_" + employeeId));
                var yAxisLabel = [];
                var totalNum = [];
                $.each(data.ACTION_COUNT_LIST, function(idx, actionCountData) {
                    yAxisLabel.push(actionCountData.ACTION_NAME);
                    totalNum.push(actionCountData.TOTAL_NUM);
                })

                var option = {
                    baseOption: {
                        "title": {
                            "text": "",
                            "subtext": ""
                        },
                        "legend": {
                            "data": ['月完成情况'],
                            inactiveColor: '#999',
                            orient: 'vertical',  //竖着排
                            width: 150,//宽度
                            borderColor: 'blue',
                            textStyle: {
                                color: '#000'
                            }
                        },
                        "xAxis": [
                            {
                                "type": "value",
                                minInterval : 5,
                            }
                        ],
                        "yAxis": [
                            {
                                "type": "category",
                                "data": yAxisLabel,
                                "axisLabel" : {
                                    interval : 0,
                                    formatter : function(params){
                                        var newParamsName = "";
                                        var paramsNameNumber = params.length;
                                        var provideNumber = 4;
                                        var rowNumber = Math.ceil(paramsNameNumber / provideNumber);
                                        if (paramsNameNumber > provideNumber) {
                                            for (var p = 0; p < rowNumber; p++) {
                                                var tempStr = "";
                                                var start = p * provideNumber;
                                                var end = start + provideNumber;
                                                if (p == rowNumber - 1) {
                                                    tempStr = params.substring(start, paramsNameNumber);
                                                } else {
                                                    tempStr = params.substring(start, end) + "\n";
                                                }
                                                newParamsName += tempStr;
                                            }

                                        } else {
                                            newParamsName = params;
                                        }
                                        return newParamsName;
                                    }
                                },
                                triggerEvent : true
                            }
                        ],
                        "tooltip": {
                            "trigger": "axis",
                            "axisPointer": {
                                "type": "line"
                            }
                        },

                        "series": [
                            {
                                "label": {
                                    "normal": {
                                        "show": true,
                                        "position" : 'right'
                                    }
                                },
                                "name": "月完成情况",
                                "type": "bar",
                                "stack": "月完成情况",
                                "data": totalNum
                            },
                        ],
                        "animation": true,
                    },
                    media: [
                        {
                            option: {
                                legend: {
                                    orient: 'horizontal',
                                    left: 'right',
                                    itemGap: 10
                                },
                                grid: {
                                    left: '20%',
                                    top: 80,
                                    right: 0,
                                    bottom: 10
                                },
                            }
                        }]
                };
                if( option && option.baseOption.series ){
                    chart.setOption(option);
                }

                chart = null;
            },
            errorFunc : function (resultCode, resultInfo) {

            }

        })
    }
};