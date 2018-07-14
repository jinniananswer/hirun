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
    <div class="back" ontap="$.redirect.closeCurrentPage();">密码修改</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div id="UI-other">
            <!-- 表单 开始 -->
            <div class="c_list c_list-form">
                <ul id="submitArea">
                    <li class="link required">
                        <div class="label">请输入原密码</div>
                        <div class="value"><input id="OLD_PASSWORD" name="OLD_PASSWORD" type="password" nullable="no" datatype="numeric" equsize="6" desc="原密码" onblur="$.org.checkOldPassword()"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">请输入新密码</div>
                        <div class="value"><input id="PASSWORD" name="PASSWORD" type="password" nullable="no" datatype="numeric" equsize="6" desc="新密码"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">请确认新密码</div>
                        <div class="value"><input id="CONFIRM_PASSWORD" name="CONFIRM_PASSWORD" type="password" datatype="numeric" equsize="6" nullable="no" desc="确认新密码"/></div>
                    </li>

                </ul>
            </div>
            <!-- 表单 结束 -->
        </div>
        <div class="c_space"></div>
        <!-- 提交 开始 -->
        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.org.submit()">提交</button>
        </div>
        <!-- 提交 结束 -->
        <div class="c_space"></div>

    </div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
</script>
</body>
</html>
