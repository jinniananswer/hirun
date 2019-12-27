<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>密码修改</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/personnel/change.password.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="back();">工具箱</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="c_list c_list-line">
        <ul>
            <li class="link" ontap="$.redirect.open('../biz/organization/personnel/change_password.jsp','密码变更')">
                <div class="main">
                    <div class="title">密码变更</div>
                    <div class="content"></div>
                </div>
                <div class="side"></div>
                <div class="more"></div>
            </li>
            <li class="link" ontap="$.redirect.open('/biz/organization/personnel/contacts.jsp','通讯录');">
                <div class="main">
                    <div class="title">通讯录</div>
                    <div class="content"></div>
                </div>
                <div class="side"></div>
                <div class="more"></div>
            </li>
            <li class="link">
                <div class="main">
                    <div class="title"><a href="/doc/order.pptx">培训文档</a></div>
                    <div class="content"></div>
                </div>
                <div class="side"></div>
                <div class="more"></div>
            </li>
            <li class="link" ontap="window.location.href='/out'">
                <div class="main">
                    <div class="title">退出登陆</div>
                    <div class="content"></div>
                </div>
                <div class="side"></div>
                <div class="more"></div>
            </li>
        </ul>
    </div>
    <!-- 列表 结束 -->
    <div class="c_line"></div>
</div>
<!-- 滚动 结束 -->
<script>
    Wade.setRatio();
</script>
</body>
</html>