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
                                            <div class="title e_size-xxl">2018年度家装顾问缺口数报表</div>
                                            <div class="content content-auto">
                                                可以查看分公司家装顾问缺口数的数据统计
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
                "text": "家装顾问总缺口数：92",
                "subtext": "分公司家装顾问缺口数",
                "left": "center"
            },
            "legend": {
                "orient": "vertical",
                "data": [
                    "长沙鸿扬:22",
                    "株洲鸿扬:2",
                    "湘潭鸿扬:9",
                    "衡阳鸿扬:4",
                    "武汉鸿扬:6",
                    "娄底鸿扬:4",
                    "怀化鸿扬:6",
                    "常德鸿扬:14",
                    "郴洲鸿扬:12",
                    "浏阳鸿扬:6",
                    "宁乡鸿扬:4",
                    "萍乡鸿扬:3"
                ],
                "left": "left"
            },
            "tooltip": {
                "trigger": "item",
                "formatter": ""
            },
            "series": [
                {
                    "radius": "55%",
                    "type": "pie",
                    "data": [
                        {
                            "name": "长沙鸿扬:22",
                            "value": 22
                        },
                        {
                            "name": "株洲鸿扬:2",
                            "value": 2
                        },
                        {
                            "name": "湘潭鸿扬:9",
                            "value": 9
                        },
                        {
                            "name": "衡阳鸿扬:4",
                            "value": 4
                        },
                        {
                            "name": "武汉鸿扬:6",
                            "value": 6
                        },
                        {
                            "name": "娄底鸿扬:4",
                            "value": 4
                        },
                        {
                            "name": "怀化鸿扬:6",
                            "value": 6
                        },
                        {
                            "name": "常德鸿扬:13",
                            "value": 13
                        },
                        {
                            "name": "郴州鸿扬:12",
                            "value": 12
                        },
                        {
                            "name": "浏阳鸿扬:6",
                            "value": 6
                        },
                        {
                            "name": "宁乡鸿扬:2",
                            "value": 2
                        },
                        {
                            "name": "萍乡鸿扬:3",
                            "value": 3
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