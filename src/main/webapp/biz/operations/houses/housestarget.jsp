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
</head>
<body>
<div class="p_index">
    <div class="c_scroll c_scroll-float c_scroll-phone-header" id="UI-scroller">
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
                                            <div class="title e_size-xxl">楼盘规划-恒基凯旋门-开发咨询目标占比数与实际数据对比（实际数据暂无）</div>
                                            <div class="content content-auto">

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
                    <div name="myLineChart" id="myLineChart" style="height:35em;"></div>

                </div>
                <!-- 基础 结束 -->


            </div>
        </div>
    </div>
</div>
<script>
    Wade.setRatio();
    if(get("UI-loading")){ hide(parent.get("UI-loading"));}
    setTimeout(function(){
        var chart = echarts.init(document.getElementById("myLineChart"));
        var option = {
            "title": {
                "text": "楼盘规划-恒基凯旋门-开发咨询目标占比数与实际数据对比（实际数据暂无）",
                "subtext": ""
            },
            "legend": {
                "data": [
                    "-3到0个月",
                    "0到6个月",
                    "6-12个月",
                    "12-24个月"
                ]
            },
            "xAxis": [
                {
                    "type": "category",
                    "boundaryGap": false,
                    "data": [
                        "-3到0个月",
                        "0到6个月",
                        "6-12个月",
                        "12-24个月"
                    ]
                }
            ],
            "yAxis": [
                {
                    "type": "value"
                }
            ],
            "tooltip": {
                "trigger": "axis"
            },
            "series": [
                {
                    "areaStyle": {
                        "normal": { }
                    },
                    "name": "咨询数百分比",
                    "type": "line",
                    "data": [
                        1,
                        6,
                        9,
                        12
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