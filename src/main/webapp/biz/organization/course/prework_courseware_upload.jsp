<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/10/21
  Time: 上午11:46
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
    <script src="/scripts/biz/organization/course/prework.courseware.upload.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">岗前课堂资料上传</div>
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
                    <li class="required link" ontap="$('#CITY_TEXT').focus();$('#CITY_TEXT').blur();showPopup('UI-popup','COURSE_SELECT')">
                        <div class="label">选择课程</div>
                        <div class="value">
                            <input type="text" id="NAME" name="NAME" nullable="no" readonly="true" desc="课程名称" />
                            <input type="hidden" name="COURSE_ID" id="COURSE_ID" nullable="no" desc="课程ID" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="link required">
                        <div class="label">选择课程文件</div>
                        <div class="value">
                            <span class="e_mix" ontap="$('#COURSE_FILE').trigger('click')">
                                <input id="FILE_NAME" name="FILE_NAME" type="text" value="" readonly="true"/>
                                <span class="e_ico-browse"></span>
                            </span>
                            <input type="file" id="COURSE_FILE" name="COURSE_FILE" style="display:none" onchange="$.courseware.fileChange()"/>
                        </div>
                    </li>
                </ul>
            </div>
            <!-- 表单 结束 -->
        </div>
        <div class="c_space"></div>
        <!-- 提交 开始 -->
        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.courseware.submit()">提交</button>
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
<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="COURSE_SELECT">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择课程类别</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-3 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="COURSE_LIST">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 弹窗 结束 -->

<script>
    Wade.setRatio();
    $.courseware.init();
</script>
</body>
</html>
