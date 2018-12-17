<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/12/1
  Time: 3:56 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>讲师详情</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/teacher/view.teacher.js"></script>
</head>
<body>
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">讲师详情</div>
    <div class="fn">

    </div>
</div>
<div class="c_title">
    <div class="text"></div>
    <div class="fn">
        <input type="hidden" name="TEACHER_ID" id="TEACHER_ID" nullable="no" value="${pageContext.request.getParameter("TEACHER_ID") }" desc="讲师ID" />
    </div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="l_col l_col-space-1 l_col-phone-merge">
            <div class="l_colItem" style="width:30%"> <!-- 第一列 -->
                <img id="TEACHER_PIC" src=""/>
            </div>
            <div class="l_colItem"> <!-- 第二列 -->
                <div class="c_param c_param-border c_param-col-1 c_param-label-6">
                    <ul id="TEACHER_INFO">

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.teacher.init();
</script>
</body>
</html>
