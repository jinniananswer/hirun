<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/11/12
  Time: 下午8:23
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>新增散盘</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/exam.guide.upload.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">考试指南资料上传</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <form id="submitForm" method="post" action="uploadCourseware" enctype="multipart/form-data">
        <div class="l_padding">
            <div id="UI-other">
                <!-- 表单 开始 -->
                <div class="c_list c_list-form">
                    <ul id="submitArea">
                        <li class="link required">
                            <div class="label">选择课程文件</div>
                            <div class="value">
                            <span class="e_mix" ontap="$('#EXAM_GUIDE_FILE').trigger('click')">
                                <input id="FILE_NAME" name="FILE_NAME" type="text" value="" readonly="true"/>
                                <span class="e_ico-browse"></span>
                            </span>
                                <input type="file" id="EXAM_GUIDE_FILE" name="EXAM_GUIDE_FILE" style="display:none" onchange="$.exam.fileChange()"/>
                            </div>
                        </li>
                    </ul>
                </div>
                <!-- 表单 结束 -->
            </div>
            <div class="c_space"></div>
            <!-- 提交 开始 -->
            <div class="c_submit c_submit-full">
                <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.exam.submit()">提交</button>
            </div>
            <!-- 提交 结束 -->
            <div class="c_space"></div>

        </div>
        <div class="c_space"></div>
        <div class="c_space"></div>
        <div class="c_space"></div>
    </form>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>

<script>
    $.exam.init();
    Wade.setRatio();
</script>
</body>
</html>
