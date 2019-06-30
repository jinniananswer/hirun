<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户清理申请</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/cust.clear.apply.js?v=20190321"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">客户清理申请</div>
    <div class="fn">

    </div>
  <input type="hidden" id="PARTY_ID" name="PARTY_ID" nullable="no" value="${pageContext.request.getParameter("PARTY_ID") }" desc="参与人ID" />
  <input type="hidden" id="PROJECT_ID" name="PROJECT_ID" nullable="no" value="${pageContext.request.getParameter("PROJECT_ID") }" desc="参与人ID" />

</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text">基本信息</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">没有找到评信息哦~~</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="preworks">

            </ul>
        </div>

        <div class="c_title">
            <div class="text">申请记录</div>
            <div class="fn">

            </div>
        </div>

        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="applyinfo">

            </ul>
        </div>

        <div class="c_title">
            <div class="text">操作区</div>
            <div class="fn">

            </div>
        </div>

        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="reason">
                <li class="link required">
                    <div class="label">客户清理原因：</div>
                    <div class="value">
                        <textarea id="CLEAR_REASON" name="CLEAR_REASON" placeholder="请填写客户清理原因" nullable="no" desc="客户清理原因" class="e_textarea-row-2"></textarea>
                    </div>
                </li>
            </ul>
        </div>

        <div class="c_submit c_submit-full">
            <button type="button" id="submitButton" class="e_button-r e_button-l e_button-green" ontap="$.custclear.submit()">提交</button>
        </div>

        <div class="c_space-4"></div>
        <div class="c_space-4"></div>
    </div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.custclear.init();
</script>
</body>
</html>
