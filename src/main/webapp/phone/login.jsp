<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/5/28
  Time: 上午1:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <jsp:include page="/phone_include.jsp"></jsp:include>
    <script src="../scripts/login.js"></script>
    <script type="text/javascript">
        function saveUserDeviceToken(deviceToken){
            $("#USER_DEVICE_TOKEN").val(deviceToken);
        }
    </script>
    <style>
        .c_tooltip { z-index:99999 !important; }
    </style>
    <title>HIRUN</title>
</head>
<body>
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float m_login" id="loginForm">
    <div class="logo">
        <img src="../frame/img/logo-phone.png" alt="" />
    </div>
    <!-- 表单 开始 -->
    <div class="form">
        <ul>
            <li>
                <span class="e_ico-user"></span>
                <input type="text" id="username" name="username" placeholder="请输入手机号码" datatype="text" nullable="no" desc="用户名" />
            </li>
            <li>
                <span class="e_ico-pwd"></span>
                <input type="password" id="password" name="password" placeholder="请输入密码" datatype="text" nullable="no" desc="密码" />
                <input type="hidden" name="USER_DEVICE_TOKEN" id="USER_DEVICE_TOKEN" nullable="yes" desc="用户终端标识" />
            </li>
        </ul>
    </div>
    <!-- 表单 结束 -->
    <!-- 分列 开始 -->
    <div class="l_col">
        <div class="l_colItem">
            <input type="checkbox" class="e_checkbox"  checked="checked" id="automatic" name="automatic" value="1"/><span class="e_space"></span><label for="automatic">自动登录</label>
        </div>
        <div class="l_colItem e_right">

        </div>
    </div>
    <!-- 分列 结束 -->
    <!-- 提交 开始 -->
    <div class="c_submit c_submit-full submit">
        <button id="login_btn" type="button" class="e_button-l e_button-green" ontap="$.login.verifyLogin();">登陆</button>
    </div>
    <!-- 提交 结束 -->

</div>
<!-- 滚动 结束 -->

<script>
    Wade.setRatio();
</script>

</body>
</html>