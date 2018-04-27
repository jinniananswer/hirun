var chart = echarts.init(document.getElementById("myPieChart"));
var option = {
    "title": {
        "text": "饼图示例图",
        "subtext": "纯属虚构",
        "left": "center"
    },
    "legend": {
        "orient": "vertical",
        "data": [
            "红色",
            "绿色",
            "黄色",
            "灰色",
            "深灰"
        ],
        "left": "left"
    },
    "tooltip": {
        "trigger": "item",
        "formatter": "{a} <br/>{b}: {c} ({d}%)"
    },
    "series": [
        {
            "radius": "55%",
            "type": "pie",
            "data": [
                {
                    "name": "红色",
                    "value": 300,
                    "itemStyle": {
                        "normal": {
                            "color": "#F7464A"
                        },
                        "emphasis": {
                            "color": "#FF5A5E"
                        }
                    }
                },
                {
                    "name": "绿色",
                    "value": 50,
                    "itemStyle": {
                        "normal": {
                            "color": "#46BFBD"
                        },
                        "emphasis": {
                            "color": "#5AD3D1"
                        }
                    }
                },
                {
                    "name": "黄色",
                    "value": 100,
                    "itemStyle": {
                        "normal": {
                            "color": "#FDB45C"
                        },
                        "emphasis": {
                            "color": "#FFC870"
                        }
                    }
                },
                {
                    "name": "灰色",
                    "value": 40,
                    "itemStyle": {
                        "normal": {
                            "color": "#949FB1"
                        },
                        "emphasis": {
                            "color": "#A8B3C5"
                        }
                    }
                },
                {
                    "name": "深灰",
                    "value": 120,
                    "itemStyle": {
                        "normal": {
                            "color": "#4D5360"
                        },
                        "emphasis": {
                            "color": "#616774"
                        }
                    }
                }
            ]
        }
    ],
    "animation": true
};
if( option && option.series ){
    chart.setOption(option);
}
alert('aa');