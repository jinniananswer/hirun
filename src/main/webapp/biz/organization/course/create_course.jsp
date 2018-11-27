<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/11/17
  Time: 11:44 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>创建课程</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/create.course.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">新建课程</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div id="UI-other">
            <!-- 表单 开始 -->
            <div class="c_list c_list-form">
                <ul id="submitArea">
                    <li class="link required">
                        <div class="label">课程名称</div>
                        <div class="value"><input id="NAME" name="NAME" type="text" nullable="no" desc="课程名称"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">课程类型</div>
                        <div class="value">
                            <span id="course_type_container"></span>
                        </div>
                    </li>
                    <li class="required link" ontap="$('#ORG_TEXT').focus();$('#ORG_TEXT').blur();showPopup('UI-popup','UI-ORG')">
                        <div class="label">上级课程</div>
                        <div class="value">
                            <input type="text" id="PARENT_COURSE_TEXT" name="PARENT_COURSE_TEXT" nullable="no" readonly="true" desc="所属部门" />
                            <input type="hidden" id="ORG_ID" name="ORG_ID" nullable="no" desc="所属部门" />
                        </div>
                        <div class="more"></div>
                    </li>
                </ul>
            </div>
            <div class="c_title">
                <div class="text">课件内容</div>
                <div class="fn">
                    <ul>
                        <li><span class="e_ico-add"></span>增加课件</li>
                    </ul>
                </div>
            </div>

            <div class="c_list c_list-form">
                <ul>
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
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.course.submit()">提交</button>
        </div>
        <!-- 提交 结束 -->
        <div class="c_space"></div>

    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.course.init();
</script>
</body>
</html>
