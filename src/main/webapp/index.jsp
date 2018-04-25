<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>Always Online Office</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/index.js"></script>
</head>
<body>
<div class="p_index">
    <div class="side">
        <div class="user">
            <div class="c_list c_list-v">
                <ul>
                    <li>
                        <div class="pic"><img id="HEAD_IMAGE" name="HEAD_IMAGE" class="e_pic-r" src="frame/img/staff.jpg" alt="" /></div>
                        <div class="main">
                            <div class="title" id="USER_NAME" name="USER_NAME"></div>
                            <div class="content" id="ORG_NAME" name="ORG_NAME"></div>
                            <div class="content" id="JOB_ROLE_NAME" name="JOB_ROLE_NAME"></div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="menu">
            <!-- 列表 开始 -->
            <div class="c_list c_list-s">
                <ul>
                    <li class="link" title="密码变更" ontap="">
                        <div class="main">密码变更</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="我的档案" ontap="">
                        <div class="main">我的档案</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="系统帮助" ontap="">
                        <div class="main">系统帮助</div>
                        <div class="more"></div>
                    </li>
                </ul>
            </div>
            <!-- 列表 结束 -->
        </div>
    </div>
    <div class="main">
        <div class="task">
            <ul id="page_titles">
                <li class="on"  title="首页" onclick="$.index.switchPage('首页')">
                    <div class="text"><span class="e_ico-home"></span></div>
                </li>
            </ul>
        </div>
        <div id="page_contents" class="content">
            <iframe src="home.jsp" title="首页" frameborder="0"></iframe>
        </div>
    </div>
</div>
<script>
    $.index.init();
</script>
</body>
</html>