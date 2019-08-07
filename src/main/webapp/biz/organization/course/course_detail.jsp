<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/10/28
  Time: 下午10:58
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>课程详情</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/course.detail.js?v=20190807"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">课程详情</div>
    <div class="fn">
        <input type="hidden" name="COURSE_ID" id="COURSE_ID" nullable="no" value="${pageContext.request.getParameter("COURSE_ID") }" desc="课程ID" />
    </div>
</div>

<!-- 参数 开始 -->
<div class="l_padding">
    <div class="c_title">
        <div class="text">课程详情</div>
    </div>
    <div class="c_param c_param-border c_param-col-2 c_param-label-6">
        <ul>
            <li>
                <span class="label">课程名称：</span>
                <span id="COURSE_NAME" class="value"></span>
            </li>
            <li>
                <span class="label">上级课程：</span>
                <span id="PARENT_COURSE_NAME" class="value"></span>
            </li>
            <li>
                <span class="label">课程描述：</span>
                <span id="COURSE_DESC" class="value"></span>
            </li>
        </ul>
    </div>
</div>
<div class="c_scroll">
    <div class="l_padding">
        <div class="c_title">
            <div class="text">课件列表</div>
        </div>
        <div class="c_msg" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">暂无课件</div>
                        <div class="content"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="c_box">
            <div class="c_list c_list-line">
                <ul id="courses">

                </ul>
            </div>
        </div>
    </div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
</div>

<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.courseware.init();
</script>
</body>
</html>
