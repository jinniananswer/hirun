var planListQuery = {
    stopPropagation : false,
    init : function() {
        planListQuery.queryEmployeeList();
    },
    queryEmployeeList : function() {
        $.ajaxReq({
            url : 'employee/getAllSubordinatesCounselors',
            data : {
                EMPLOYEE_IDS : Employee.employeeId,
                COLUMNS : 'EMPLOYEE_ID,NAME'
            },
            successFunc : function(data) {
                // data.EMPLOYEE_LIST = [];
                // data.EMPLOYEE_LIST.push({EMPLOYEE_NAME : '小安', EMPLOYEE_ID : '157', HINT : '今日没有录计划'})
                $('#employee_list').html(template("employee_template", data));
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        })
        // planListQuery.getEmployeeDailySheet();
    },
    // getEmployeeDailySheet : function() {
    //     $('#employee_list div[tag=employee_box]').each(function(idx, item) {
    //         var $item = $(item);
    //         var employeeId = $item.attr('employee_id');
    //
    //     })
    // },
    queryDetail : function(obj) {
        planListQuery.stopPropagation = true;

        var $obj = $(obj)
        var employeeId = $obj.attr('employee_id');

        $.redirect.open("biz/operations/contactplan/employee_dailysheet_detail_query.jsp?EXECUTOR_ID="+employeeId,"家装顾问日报表详情");
    },
    clickEmployee: function(obj) {
        if(planListQuery.stopPropagation) {
            planListQuery.stopPropagation = false;
            return;
        }

        $obj = $(obj);
        $obj.next().toggle();
        var isQuery = $obj.next().attr('is_query');

        if(isQuery == 'false') {
            $obj.next().attr('is_query', "true");
            var employeeId = $obj.attr('employee_id');

            $.ajaxReq({
                url : 'plan/queryEmployeeDailySheet',
                data : {
                    PLAN_EXECUTOR_ID : employeeId
                },
                successFunc : function(data) {
                    var chart = echarts.init(document.getElementById("daily_sheet_" + employeeId));
                    var yAxisLabel = [];
                    var yesterdayPlanNumData = [];
                    var yesterdayFinishNumData = [];
                    var todayPlanNumData = [];
                    $.each(data.ACTION_COUNT_LIST, function(idx, actionCountData) {
                        yAxisLabel.push(actionCountData.ACTION_NAME);
                        yesterdayPlanNumData.push(actionCountData.YESTERDAY_PLAN_NUM);
                        yesterdayFinishNumData.push(actionCountData.YESTERDAY_FINISH_NUM);
                        todayPlanNumData.push(actionCountData.TODAY_PLAN_NUM);
                    })

                    var option = {
                        baseOption: {
                            "title": {
                                "text": "",
                                "subtext": ""
                            },
                            "legend": {
                                "data": ['昨日计划','昨日完成','今日计划'],
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
                                            "position" : 'right' //数字的位置
                                        }
                                    },
                                    barWidth:'25%',
                                    "name": "昨日计划",
                                    "type": "bar",
                                    "stack": "昨日计划",
                                    "data": yesterdayPlanNumData
                                },
                                {
                                    "label": {
                                        "normal": {
                                            "show": true,
                                            "position" : 'right'
                                        }
                                    },
                                    barWidth:'25%',
                                    "name": "昨日完成",
                                    "type": "bar",
                                    "stack": "昨日完成",
                                    "data": yesterdayFinishNumData
                                },
                                {
                                    "label": {
                                        "normal": {
                                            "show": true,
                                            "position" : 'right'
                                        }
                                    },
                                    barWidth:'25%',
                                    "name": "今日计划",
                                    "type": "bar",
                                    "stack": "今日计划",
                                    "data": todayPlanNumData
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

                    // chart.on('click', function (params) {
                    //     alert(params.targetType);
                    //     console.log(params);
                    // });

                    chart = null;
                },
                errorFunc : function (resultCode, resultInfo) {

                }

            })
        }
    }
};