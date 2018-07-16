<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>变更楼盘规划</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/houses/house.detail.js"></script>
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
    <div class="l_padding">
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
                <div class="text" id="COUNSELOR_TITLE"></div>
            </div>
            <!-- 标题 结束 -->
            <!-- 列表 开始 -->
            <div class="c_list c_list-line">
                <ul id="COUNSELOR_DETAIL">

                </ul>
            </div>
            <!-- 列表 结束 -->
        </div>
    </div>
    <div class="c_space"></div>
    <div class="c_box c_box-border l_padding l_padding-u">
        <!-- 标题 开始 -->
        <div class="c_title">
            <div class="text">指标与实际完成情况（当前周期：交房前三个月）</div>
        </div>
        <!-- 标题 结束 -->
        <div class="c_table c_table-hasGrid c_table-lite c_table-border">
            <table>
                <thead>
                <tr>
                    <th>指标</th>
                    <th>计划完成(户)</th>
                    <th>当前完成(户)</th>
                </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>咨询数</td>
                        <td>-</td>
                        <td class="e_orange">-</td>
                    </tr>
                    <tr>
                        <td>推送蓝图一数</td>
                        <td>-</td>
                        <td class="e_orange">-</td>
                    </tr>
                    <tr>
                        <td>扫码数</td>
                        <td>-</td>
                        <td class="e_orange">-</td>
                    </tr>
                    <tr>
                        <td>关注公众号数</td>
                        <td>-</td>
                        <td class="e_orange">-</td>
                    </tr>
                    <tr>
                        <td>推送蓝图指导书数</td>
                        <td>-</td>
                        <td class="e_orange">-</td>
                    </tr>
                    <tr>
                        <td>加微数</td>
                        <td>-</td>
                        <td class="e_orange">-</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <!-- 表格 结束 -->
    </div>
    <div class="c_space"></div>
    <div class="c_submit c_submit-full">
        <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.housesPlan.redirectToChange()">修改</button>
        <button type="button" class="e_button-r e_button-l e_button-navy" ontap="$.housesPlan.initAudit()">审核</button>
    </div>
    <div class="c_space"></div>
    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-popup-audit">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">审核楼盘信息</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="auditArea">
                                <li class="link">
                                    <div class="label">审核通过</div>
                                    <div class="value">
                                        <div class="e_switch">
                                            <div class="e_switchOn">是</div>
                                            <div class="e_switchOff">否</div>
                                            <input type="hidden" id="AUDIT_OPTION" name="AUDIT_OPTION" datatype="text" nullable="no" desc="审核通过"/>
                                        </div>
                                    </div>
                                </li>
                                <li class="link">
                                    <div class="label">审核意见</div>
                                    <div class="value">
                                        <input type="hidden" id="AUDIT_HOUSES_ID" name="AUDIT_HOUSES_ID" datatype="text" nullable="yes" desc="楼盘ID" value="" />
                                        <textarea id="AUDIT_OPINION" name="AUDIT_OPINION" desc="审核意见" datatype="text" nullable="yes" class="e_textarea-row-2"></textarea>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_AUDIT" name="SUBMIT_QUERY" ontap="$.housesPlan.submitAudit();" class="e_button-l e_button-green">提交审核</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 滚动 结束 -->
<script>
    Wade.setRatio();
    $.housesPlan.init();
</script>
</body>
</html>