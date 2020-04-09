<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/5/29
  Time: 上午12:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <jsp:include page="/phone_include.jsp"></jsp:include>
    <script src="../scripts/phone.index.js?v=20200409"></script>
    <title>HIRUN</title>
</head>
<body>

<div class="c_header">
    <div class="left">
        <div class="fn">

        </div>
    </div>
    <div class="center"><div class="text">鸿助手</div></div>
    <div class="right">
        <div class="fn">

        </div>
    </div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header c_scroll-white">
    <div class="c_list c_list-purple">
        <ul>
            <li>
                <div class="content">
                    <div class="pic"><span class="e_pic-img-r"><img id="HEAD_IMAGE" src="../frame/img/male.png" alt="" /></span></div>
                    <div class="main">
                        <div class="title" id="USER_NAME" name="USER_NAME"></div>
                        <div class="content" id="ORG_NAME" name="ORG_NAME"></div>
                    </div>
                    <div class="side e_white" id="JOB_ROLE_NAME" name="JOB_ROLE_NAME">
                    </div>
                </div>
            </li>
        </ul>
    </div>
    <div id="menus" class="l_padding l_padding-side">

    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.index.init();
</script>
</body>
</html>
