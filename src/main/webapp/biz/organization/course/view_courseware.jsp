<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/10/28
  Time: 下午11:38
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>岗前课堂学习资料</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/query.prework.courseware.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">岗前学习资料</div>
    <div class="fn">
        <input type="hidden" name="FILE_ID" id="FILE_ID" nullable="no" value="${pageContext.request.getParameter("FILE_ID") }" desc="文件ID" />
    </div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header c_scroll-white">
    <div class="main">
        <iframe id="view_frame" src="${pageContext.request.getAttribute("PATH")}" title="在线阅读" frameborder="0"></iframe>
    </div>
</div>

<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.courseware.init();
</script>
</body>
</html>
