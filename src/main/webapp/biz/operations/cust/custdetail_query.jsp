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
    <title>客户明细</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/cust/custdetail.query.js"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="back();"><span>客户明细</span></div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
    <div class="c_title">
        <div class="text" tag="cust_name">安文轩</div>
    </div>
    <div class="c_param c_param-label-6" >
        <ul id="cust_detail">
            <li>
                <span class="label">性别：</span>
                <span class="value" tag="sex"></span>
            </li>
            <li>
                <span class="label">微信昵称：</span>
                <span class="value" tag="wx_nick"></span>
            </li>
            <li>
                <span class="label">电话号码：</span>
                <span class="value" tag="mobile_no"></span>
            </li>
            <li>
                <span class="label">楼盘：</span>
                <span class="value" tag="house"></span>
            </li>
            <li>
                <span class="label">楼栋号：</span>
                <span class="value" tag="house_detail"></span>
            </li>
            <li>
                <span class="label">户型：</span>
                <span class="value" tag="house_mode"></span>
            </li>
            <li>
                <span class="label">面积：</span>
                <span class="value" tag="house_area"></span>
            </li>
            <li>
                <span class="label">客户基本情况：</span>
                <span class="value" tag="cust_detail"></span>
            </li>
        </ul>
    </div>
</div>
<script id="cust_action_list_template" rel_id="" type="text/html">
    {{each CUST_FINISH_ACTION_LIST action idx}}
    <li>
        <span class="label">{{action.ACTION_NAME}}：</span>
        <span class="value">{{action.FINISH_TIME}}</span>
    </li>
    {{/each}}
</script>
<script type="text/javascript">
    Wade.setRatio();
    custDetailQuery.init();
</script>
</body>
</html>