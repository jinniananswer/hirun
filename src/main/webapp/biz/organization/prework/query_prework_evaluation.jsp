<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/1/27
  Time: 12:22 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>培训查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/prework/query.prework.evaluation.js?v=20190223"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">岗前考评查看</div>
    <div class="fn">

    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text">近期岗前考评列表</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">没有找到近期有岗前考评信息哦~~</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="preworks">

            </ul>
        </div>

        <div class="c_space-4"></div>
        <div class="c_space-4"></div>
    </div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.prework.init();
</script>
</body>
</html>
