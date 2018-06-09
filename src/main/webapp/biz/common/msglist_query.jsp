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
    <title>我的消息</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/common/msglist.query.js"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="back();"><span>我的消息</span></div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
    <div class="c_space"></div>
    <div id="msg_list">
    </div>
    <div class="e_space"></div>
</div>
<script id="msg_template" rel_id="msg_list" type="text/html">
    {{each MSG_LIST msg idx}}
    <div class="c_article" style="padding: 0em 0.5em 0em 0.5em;">
        <div class="content" style="padding: 0">
            <span class="e_tag e_tag-s">{{msg.MSG_TYPE_DESC}}</span>
            {{msg.MSG_CONTENT}}
        </div>
        <div class="aside">{{msg.USERNAME}}</div>
        <div class="aside">{{msg.SEND_TIME}}</div>
    </div>
    {{/each}}
</script>
<script type="text/javascript">
    Wade.setRatio();
    MsgListQuery.init();
</script>
</body>
</html>