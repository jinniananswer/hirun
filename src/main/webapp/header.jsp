<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;
%>
<html size="s">
<head>
    <base href="<%=basePath%>"></base>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <script src="scripts/header.js"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="$.redirect.closeCurrentPage();">
        <span tag="header_name"><%=request.getParameter("headerName")%></span>
    </div>
    <div tag="headerPhone" class="right" style="display:none">
        <div class="fn">
            <span class="e_ico-menu "ontap="Header.showFloatLayerFn(this);"></span>
        </div>
    </div>
</div>
<!-- 悬浮层部分，必须是 body 的直接子对象 -->
<div class="c_float c_float-phone-menu" id="funcArea">
    <div class="bg"></div>
    <div class="content">
        <div class="c_list c_list-line" style="width:50em; top:10em; left:10em;">
            <ul>
                <li class="link" ontap="$.redirect.open('/biz/common/msglist_query.jsp','我的消息')">
                    <div class="main">
                        <div class="title">
                            我的消息
                            <span class="e_ico-pic-red e_ico-pic-xxxs e_ico-pic-r" id="myMsg">0</span>
                        </div>
                    </div>
                </li>
                <li class="link" ontap="$.redirect.open('/biz/organization/personnel/change_password.jsp','密码变更')">
                    <div class="main">
                        <div class="title">修改密码</div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</div>
<script type="text/javascript">
    Header.init();
</script>
</body>
</html>