<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>hi-run</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/datacenter/houses/house.nature.report.js"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="back();">楼盘规划统计</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header" id="UI-scroller">
    <div class="l_padding">
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
                                        <div class="title e_size-xxl" id="company_name">所有分公司楼盘统计</div>
                                        <div class="content content-auto">
                                            <span id="house_num">楼盘总数：0</span>
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
<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-popup-query-cond">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">请选择分公司</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="COMPANY">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>

            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->
<script>
    Wade.setRatio();
    $.housesReport.init();
</script>
</body>
</html>