<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/11/13
  Time: 下午9:47
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>课件查看</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/query.courseware.js?v=1"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">课件查看</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 标题栏 结束 -->

<div class="c_scroll">
    <div class="l_padding">
        <div class="c_msg" id="messagebox">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content" id="message_content">请点击放大镜图标选择查询条件进行课件查询</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="c_box">
            <div class="c_list c_list-line">
                <ul id="coursewares">

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
<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-popup-query-cond">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">请选择查询条件</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="queryArea">
                                <li class="link">
                                    <div class="label">课件名称</div>
                                    <div class="value"><input id="NAME" name="NAME" type="text" nullable="yes" desc="课件名称"/></div>
                                </li>
                                <li class="link" ontap="$('#COURSE_NAME').focus();$('#COURSE_NAME').blur();forwardPopup('UI-popup','UI-COURSE')">
                                    <div class="label">课程</div>
                                    <div class="value">
                                        <input type="text" id="COURSE_NAME" name="COURSE_NAME" nullable="yes" readonly="true" desc="课程" />
                                        <input type="hidden" id="COURSE_ID" name="COURSE_ID" nullable="yes" desc="课程" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.courseware.query();" class="e_button-l e_button-green">查询</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-COURSE">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择课程</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_box">
                            <div class="l_padding">
                                <div id="courseTree" class="c_tree"></div>
                            </div>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->
<script>
    $.courseware.init();
    Wade.setRatio();
</script>
</body>
</html>