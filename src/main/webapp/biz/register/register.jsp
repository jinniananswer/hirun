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
    <script src="/frame/TouchUI/content/js/local.js"></script>
    <script src="/frame/js/ajax.js"></script>
    <script src="/scripts/biz/register/register.js"></script>
</head>
<body>
<div class="p_index">
    <div class="header">
        <div class="logo">HI-RUN - 用户注册</div>
    </div>
    <div class="main">

        <div class="l_edit" style="top:4rem">
            <!-- 主流程 开始 -->
            <form name="loginForm" method="post" action="register">
                <div class="l_editMain">
                    <div class="c_space"></div>
                    <div class="c_guide" >
                        <ul>
                            <li class="on"><span>第一步</span></li>
                            <li><span>第二步</span></li>
                        </ul>
                    </div>

                    <div class="c_list c_list-form">
                        <ul>
                            <li>
                                <div class="ico"><span class="e_ico-user"></span></div>
                                <div class="label">用户名</div>
                                <div class="value"><input type="text"  id="userName" name="userName" value="" placeholder="请填写" onblur="$.register.checkUserName($('#userName').val())"/></div>
                            </li>
                            <li>
                                <div class="label">密码</div>
                                <div class="value"><input type="password" id="password" name="password" value="" /></div>
                            </li>
                            <li>
                                <div class="label">确认密码</div>
                                <div class="value"><input type="password" id="confirm_password" name="confirm_password" value="" /></div>
                            </li>
                            <li>
                                <div class="label">手机号码</div>
                                <div class="value"><input type="text" id="phone" name="phone" value="" placeholder="请填写"/></div>
                            </li>
                        </ul>
                    </div>
                    <div class="c_space"></div>
                    <div class="c_submit c_submit-full">
                        <button class="e_button-l e_button-r e_button-green" type="submit">提交</button>
                    </div>
                    <div class="c_space"></div>
                </div>
            </form>
            <!-- 主流程 结束 -->
            <!-- 占位符 开始 -->
            <div class="l_editPlace"></div>
            <!-- 占位符 结束 -->
        </div>
    </div>
</div>

</body>
</html>