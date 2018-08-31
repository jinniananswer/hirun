<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/5/4
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>楼盘规划查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/houses/my.houses.js?v=20180901000700"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="$.redirect.closeCurrentPage();">我的楼盘</div>
    <div class="fn">

    </div>
</div>
<!-- 滚动开始 -->
<div class="c_scroll c_scroll-float c_scroll-header" id="scroll_div">
    <!-- 筛选 结束 -->
    <div class="c_list c_list-line c_list-border c_list-space l_padding">
        <ul id="houses">

        </ul>
    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>

<script>
    Wade.setRatio();
    $.myHouse.initQuery();
</script>
</body>
</html>