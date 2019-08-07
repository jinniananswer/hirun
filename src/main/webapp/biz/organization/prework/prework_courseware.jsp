<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/2/10
  Time: 3:10 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>课程体系管理</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/prework/prework.courseware.js?v=20190807"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">课件资料查看</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text">课程体系</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_box">
            <div class="l_padding">
                <div id="courseTree" class="c_tree">

                </div>
            </div>
        </div>
        <div class="c_title">
            <div class="text"></div>
            <div class="fn">
                <ul>
                    <li ontap="$.prework.initCourseFile();"><span class="e_ico-word"></span>课件查看</li>
                </ul>
            </div>
        </div>
    </div>
</div>
<!-- 滚动 结束 -->

<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-COURSE_FILE">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">课件查看</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
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
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="courseFileArea">

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
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.prework.init();
</script>
</body>
</html>