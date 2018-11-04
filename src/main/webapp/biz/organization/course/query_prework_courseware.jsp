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
    <title>岗前课堂学习资料</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/query.prework.courseware.js?v=20181105003300"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">岗前学习资料</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 标题栏 结束 -->

<div class="c_scroll">
    <div class="l_padding">
        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">sorry,没有找到您想要的信息~</div>
                    </div>
                    <div class="text" style="display:none">
                        <a id="android" name="android" href="">android</a>
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
                                <li class="link" ontap="forwardPopup(this,'UI-COURSE')">
                                    <div class="label">课程</div>
                                    <div class="value">
                                        <input type="text" id="NAME" name="NAME" readonly="true" nullable="yes" desc="课程名称" />
                                        <input type="hidden" name="COURSE_ID" id="COURSE_ID" nullable="yes" desc="课程ID" />
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
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.courseware.query();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-COURSE">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">请选择课程</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="COURSE_LIST">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
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
