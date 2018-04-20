<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>Always Online Office</title>
    <link rel="stylesheet" href="/frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="/frame/css/dmpManager.css" rel="stylesheet" type="text/css"/>
    <script src="/frame/TouchUI/content/js/jcl-base.js"></script>
    <script src="/frame/TouchUI/content/js/jcl.js"></script>
    <script src="/frame/TouchUI/content/js/i18n/code.zh_CN.js"></script>
    <script src="/frame/TouchUI/content/js/jcl-plugins.js"></script>
    <script src="/frame/TouchUI/content/js/jcl-ui.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/base/popup.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/base/segment.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/base/switch.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/tabset/tabset.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/chart/echarts.js"></script>
    <script src="/frame/TouchUI/content/js/local.js"></script>
    <script src="/frame/TouchUI/content/js/code.js"></script>
    <script src="/scripts/biz/houses/planreport.js"></script>
</head>
<body>

<div class="p_index">

    <div class="c_scroll c_scroll-float c_scroll-phone-data" id="UI-scroller">
        <div class="p_paper" id="UI-paper">

            <!-- 基础 开始 -->
            <div class="c_box c_box-border">
                <div class="l_padding-1 l_padding-phone-2">
                    <!-- 简介 开始 -->
                    <div class="l_col  p_intro">

                        <div class="l_colItem">
                            <div class="c_list">
                                <ul>
                                    <li>
                                        <div class="main">
                                            <div class="title e_size-xxl">年度楼盘规划统计</div>
                                            <div class="content content-auto">
                                                可以查看重点楼盘与责任楼盘所占总数的百分比
                                                <div class="c_space-2"></div>
                                                <div class="e_right">

                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <!-- 简介 结束 -->
                    <div class="c_space-2"></div>
                    <div name="myPieChart" id="myPieChart" style="height:35em;"></div>

                </div>
                <!-- 基础 结束 -->


            </div>
        </div>
    </div>
</div>


<script>

    setTimeout(function(){
        var chart = echarts.init(document.getElementById("myPieChart"));
        var option = {
            "title": {
                "text": "总规划楼盘数：534",
                "subtext": "楼盘分布",
                "left": "center"
            },
            "legend": {
                "orient": "vertical",
                "data": [
                    "重点现盘:209",
                    "重点期盘:288",
                    "责任楼盘:35",
                    "散盘:2"
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
                            "name": "重点现盘:209",
                            "value": 209
                        },
                        {
                            "name": "重点期盘:288",
                            "value": 288
                        },
                        {
                            "name": "责任楼盘:35",
                            "value": 35
                        },
                        {
                            "name": "散盘:2",
                            "value": 2
                        }
                    ]
                }
            ],
            "animation": true
        };
        if( option && option.series ){
            chart.setOption(option);
        }
        chart = null;

    },200);
</script>
</body>
</html>