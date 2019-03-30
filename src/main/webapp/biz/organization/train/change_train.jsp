<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/12/16
  Time: 11:23 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>修改培训安排</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/train/change.train.js?v=2019032"></script>
</head>
<body>
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">修改培训安排</div>
    <div class="fn">

    </div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
    <div class="l_padding" id="allSubmitArea">
        <div class="c_title">
            <div class="text">培训内容</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
            <ul id="trainArea">
                <li class="link required">
                    <div class="label">培训名称</div>
                    <div class="value">
                        <input type="text" id="NAME" name="NAME" placeholder="请输入培训名称" nullable="no" desc="培训名称" />
                        <input type="hidden" id="TRAIN_ID" name="TRAIN_ID" nullable="no" value="${pageContext.request.getParameter("TRAIN_ID") }" desc="培训ID" />
                    </div>
                </li>
                <li class="link required">
                    <div class="label">培训类型</div>
                    <div class="value">
                        <span id="type_select">

                        </span>
                    </div>
                </li>
                <li class="link required" ontap="$('#COURSE_NAMES').focus();$('#COURSE_NAMES').blur();showPopup('UI-popup','UI-SELECT_COURSE')">
                    <div class="label">培训课程</div>
                    <div class="value">
                        <input type="text" id="COURSE_NAMES" name="COURSE_NAMES" nullable="no" readonly="true" desc="培训课程" />
                        <input type="hidden" id="COURSE_IDS" name="COURSE_IDS" nullable="no" desc="培训课程" />
                    </div>
                    <div class="more"></div>
                </li>
                <li class="link required" ontap="$('#CHARGE_EMPLOYEE_NAME').focus();$('#CHARGE_EMPLOYEE_NAME').blur();showPopup('UI-popup','UI-EMPLOYEE')">
                    <div class="label">班主任</div>
                    <div class="value">
                        <input type="text" id="CHARGE_EMPLOYEE_NAME" name="CHARGE_EMPLOYEE_NAME" nullable="no" readonly="true" desc="班主任" />
                        <input type="hidden" id="CHARGE_EMPLOYEE_ID" name="CHARGE_EMPLOYEE_ID" nullable="no" desc="班主任" />
                    </div>
                    <div class="more"></div>
                </li>
                <li class="link">
                    <div class="label">培训内容描述</div>
                    <div class="value">
                        <textarea id="TRAIN_DESC" name="TRAIN_DESC" placeholder="培训内容描述" nullable="yes" desc="培训内容描述" class="e_textarea-row-2"></textarea>
                    </div>
                </li>
                <li class="link required">
                    <div class="label">培训地址</div>
                    <div class="value">
                        <input type="text" id="TRAIN_ADDRESS" name="TRAIN_ADDRESS" placeholder="请输入培训地址" nullable="no" desc="培训地址" />
                    </div>
                </li>
                <li class="link">
                    <div class="label">住宿地址</div>
                    <div class="value">
                        <input type="text" id="HOTEL_ADDRESS" name="HOTEL_ADDRESS" placeholder="请输入住宿地址" nullable="yes" desc="住宿地址" />
                    </div>
                </li>
                <li class="link required">
                    <div class="label">培训开始时间</div>
                    <div class="value">
                        <span class="e_mix">
                            <input type="text" id="START_DATE" name="START_DATE" datatype="date" nullable="no" readonly="true" desc="培训开始时间" />
                            <span class="e_ico-date"></span>
                        </span>
                    </div>
                </li>
                <li class="link required">
                    <div class="label">培训结束时间</div>
                    <div class="value">
                        <span class="e_mix">
                            <input type="text" id="END_DATE" name="END_DATE" datatype="date" nullable="no" readonly="true" desc="培训结束时间" />
                            <span class="e_ico-date"></span>
                        </span>
                    </div>
                </li>
            </ul>
        </div>
        <div id="schedules">


        </div>
        <div class="c_title">
            <div class="text"></div>
            <div class="fn">
                <ul>
                    <li ontap="$.train.addCourse();"><span class="e_ico-add"></span>增加课程安排</li>
                </ul>
            </div>
        </div>
        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.train.submit()">提交</button>
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
                <div class="c_popupItem" id="UI-SELECT_COURSE">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择涉及课程</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_box">
                            <div class="l_padding">
                                <div id="courseTree" class="c_tree"></div>
                            </div>
                        </div>
                        <div class="l_bottom">
                            <div class="c_submit c_submit-full">
                                <button type="button" ontap="$.train.confirmCourse();" class="e_button-l e_button-green">确定</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="c_popupItem" id="UI-SINGLE_COURSE">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择涉及课程</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_box">
                            <div class="l_padding">
                                <div id="singleCourseTree" class="c_tree"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="c_popupItem" id="UI-TEACHER">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">请选择讲师</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="TEACHERS">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-EMPLOYEE">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择班主任</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="searchArea">
                                    <li>
                                        <div class="value">
                                                <span class="e_mix">
                                                    <input id="SEARCH_TEXT" name="SEARCH_TEXT" type="text" placeholder="请输入班主任姓名（模糊搜索）" nullable="no" desc="查询条件"/>
                                                    <button type="button" class="e_button-blue" ontap="$.train.queryEmployee();"><span class="e_ico-search"></span><span>查询</span></button>
                                                </span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        </div>
                    </div>
                    <!-- 滚动（替换为 java 组件） 开始 -->
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
                                    </div>
                                </div>
                            </div>
                            <div class="c_box">
                                <div class="c_list c_list-line">
                                    <ul id="employees">

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

                </div>
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.train.init();
</script>
</body>
</html>
