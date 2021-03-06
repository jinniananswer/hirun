<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>变更楼盘规划</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/houses/my.house.detail.js?v=20180901000700"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header">
    <div class="back" ontap="$.redirect.closeCurrentPage();">楼盘规划</div>
    <div class="fn">

    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="c_box c_box-border">
        <!-- 标题 开始 -->
        <div class="c_title">
            <div class="text">
                <span class="e_strong e_purple" id="HOUSE_NAME"></span>
                <span class="e_space"></span>
                <span class="e_ico-grid e_size-xs e_purple"></span>
                <input type="hidden" name="HOUSES_ID" id="HOUSES_ID" nullable="no" value="${pageContext.request.getParameter("HOUSES_ID") }" desc="楼盘编号" />
            </div>
            <div class="fn">

            </div>
        </div>
        <!-- 标题 结束 -->
        <!-- 列表 开始 -->
        <div class="c_list" style="margin-top:-1.1em;">
            <ul>
                <li>
                    <div class="main" id="project">
                        <!-- 参数 开始 -->
                        <div class="c_param c_param-label-9 e_size-s" id="params">
                            <ul id="HOUSE_CONTENT">

                            </ul>
                        </div>
                        <!-- 参数 结束 -->
                    </div>
                </li>
            </ul>
        </div>
        <div class="l_padding">
            <!-- 标题 开始 -->
            <div class="c_space-2"></div>
            <div class="c_title">
                <div class="text" id="CUSTOMER_TITLE"></div>
            </div>
            <!-- 标题 结束 -->
            <!-- 列表 开始 -->
            <div class="c_list c_list-line">
                <ul id="CUSTOMER_DETAIL">

                </ul>
            </div>
            <!-- 列表 结束 -->
        </div>
    </div>
    <div class="c_space"></div>
    <div class="c_box c_box-border l_padding l_padding-u">
        <!-- 标题 开始 -->
        <div class="c_title">
            <div class="text">我的完成情况（当前周期：交房前三个月）</div>
        </div>
        <!-- 标题 结束 -->
        <div class="c_table c_table-hasGrid c_table-lite c_table-border">
            <table>
                <thead>
                <tr>
                    <th>指标</th>
                    <th>当前完成(户)</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>咨询数</td>
                    <td class="e_orange">-</td>
                </tr>
                <tr>
                    <td>推送蓝图一数</td>
                    <td class="e_orange">-</td>
                </tr>
                <tr>
                    <td>扫码数</td>
                    <td class="e_orange">-</td>
                </tr>
                <tr>
                    <td>关注公众号数</td>
                    <td class="e_orange">-</td>
                </tr>
                <tr>
                    <td>推送客户产品需求库使用指导书数</td>
                    <td class="e_orange">-</td>
                </tr>
                <tr>
                    <td>加微数</td>
                    <td class="e_orange">-</td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- 表格 结束 -->
    </div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.myHouse.init();
</script>
</body>
</html>