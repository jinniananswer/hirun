<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/12/1
  Time: 11:47 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>讲师管理</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/teacher/teacher.manager.js?v=201902230141"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">讲师管理</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-QUERY_COND')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text">讲师列表</div>
            <div class="fn">
                <ul>
                    <li ontap="$.teacher.initCreateTeacher();"><span class="e_ico-add"></span>新增讲师资料</li>
                </ul>
            </div>
        </div>
        <div class="c_msg c_msg-warn" id="messageboxTeacher" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">没有找到讲师信息哦~~</div>
                    </div>
                </div>
            </div>
        </div>
        <div id="teachersType">

        </div>
    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
</div>
<!-- 滚动 结束 -->

<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-QUERY_COND">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">查询条件</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="queryCond">
                                <li class="link required">
                                    <div class="label">讲师姓名</div>
                                    <div class="value">
                                        <input type="text" id="TEACHER_NAME" name="TEACHER_NAME" placeholder="请输入讲师姓名（模糊查找)" nullable="yes" desc="讲师姓名" />
                                    </div>
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
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.teacher.query();" class="e_button-l e_button-green">查询</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-CREATE_TEACHER">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">新增讲师资料</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <form id="createTeacherForm" method="post" enctype="multipart/form-data">
                            <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                                <ul id="createTeacherArea">
                                    <li class="link required">
                                        <div class="label">讲师类型</div>
                                        <div class="value">
                                            <span id="TEACHER_TYPE">

                                            </span>
                                        </div>
                                    </li>
                                    <li class="link required" id="NAME_INPUT" style="display:none">
                                        <div class="label">讲师姓名</div>
                                        <div class="value">
                                            <input type="text" id="NAME" name="NAME" nullable="yes" placeholder="请输入讲师姓名" desc="讲师姓名" />
                                        </div>
                                    </li>
                                    <li class="link required" id="NAME_SELECT" ontap="$('#EMPLOYEE_NAME').focus();$('#EMPLOYEE_NAME').blur();forwardPopup('UI-popup','UI-EMPLOYEE')">
                                        <div class="label">讲师姓名</div>
                                        <div class="value">
                                            <input type="hidden" id="EMPLOYEE_ID" name="EMPLOYEE_ID" nullable="yes" desc="员工ID" />
                                            <input type="text" id="EMPLOYEE_NAME" name="EMPLOYEE_NAME" readonly="true" nullable="yes" placeholder="请从通讯录中选择" desc="讲师姓名" />
                                        </div>
                                        <div class="more"></div>
                                    </li>
                                    <li class="link required">
                                        <div class="label">讲师类型</div>
                                        <div class="value">
                                            <span id="TEACHER_LEVEL">

                                            </span>
                                        </div>
                                    </li>
                                    <li class="link required" ontap="$('#HOLD_COURSE_NAME').focus();$('#HOLD_COURSE_NAME').blur();forwardPopup('UI-popup','UI-COURSE_HOLD')">
                                        <div class="label">担任课程</div>
                                        <div class="value">
                                            <input type="hidden" id="HOLD_COURSE_ID" name="HOLD_COURSE_ID" nullable="yes" desc="担任课程" />
                                            <input type="text" id="HOLD_COURSE_NAME" name="HOLD_COURSE_NAME" readonly="true" nullable="no" placeholder="请选择" desc="担任课程" />
                                        </div>
                                        <div class="more"></div>
                                    </li>
                                    <li class="link">
                                        <div class="label">讲师QQ</div>
                                        <div class="value">
                                            <input type="text" id="QQ_NO" name="QQ_NO" nullable="yes" maxsize="20" placeholder="请输入QQ号码" desc="讲师QQ" />
                                        </div>
                                    </li>
                                    <li class="link">
                                        <div class="label">微信号</div>
                                        <div class="value">
                                            <input type="text" id="WECHAT_NO" name="WECHAT_NO" nullable="yes" maxsize="50" placeholder="请输入微信号" desc="微信号" />
                                        </div>
                                    </li>
                                    <li class="link required">
                                        <div class="label">讲师照片</div>
                                        <div class="value">
                                            <span class="e_mix" ontap="$('#PIC_FILE').trigger('click')">
                                                <input id="PIC" name="PIC" type="text" value="" readonly="true" nullable="no" desc="讲师照片"/>
                                                <span class="e_ico-browse"></span>
                                            </span>
                                            <input type="file" id="PIC_FILE" accept="image/*" name="PIC_FILE" style="display:none" onchange="$.teacher.fileChange()"/>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </form>
                        <!-- 列表 结束 -->
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.teacher.createTeacher();" class="e_button-l e_button-green">新增讲师资料</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>

                <div class="c_popupItem" id="UI-CHANGE_TEACHER">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">修改讲师资料</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <form id="changeTeacherForm" method="post" enctype="multipart/form-data">
                            <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                                <ul id="changeTeacherArea">
                                    <li class="link required">
                                        <div class="label">讲师类型</div>
                                        <div class="value">
                                            <span id="CHANGE_TEACHER_TYPE">

                                            </span>
                                        </div>
                                    </li>
                                    <li class="link required" id="CHANGE_NAME_INPUT" style="display:none">
                                        <div class="label">讲师姓名</div>
                                        <div class="value">
                                            <input type="text" id="CHANGE_NAME" name="CHANGE_NAME" nullable="yes" placeholder="请输入讲师姓名" desc="讲师姓名" />
                                        </div>
                                    </li>
                                    <li class="link required" id="CHANGE_NAME_SELECT">
                                        <div class="label">讲师姓名</div>
                                        <div class="value">
                                            <input type="hidden" id="CHANGE_EMPLOYEE_ID" name="EMPLOYEE_ID" nullable="yes" desc="员工ID" />
                                            <input type="text" id="CHANGE_EMPLOYEE_NAME" name="EMPLOYEE_NAME" readonly="true" nullable="yes" placeholder="请从通讯录中选择" desc="讲师姓名" />
                                        </div>
                                        <div class="more"></div>
                                    </li>
                                    <li class="link required">
                                        <div class="label">讲师类型</div>
                                        <div class="value">
                                            <span id="CHANGE_TEACHER_LEVEL">

                                            </span>
                                        </div>
                                    </li>
                                    <li class="link required" ontap="$('#CHANGE_HOLD_COURSE_NAME').focus();$('#CHANGE_HOLD_COURSE_NAME').blur();forwardPopup('UI-popup','UI-CHANGE_COURSE_HOLD')">
                                        <div class="label">担任课程</div>
                                        <div class="value">
                                            <input type="hidden" id="CHANGE_TEACHER_ID" name="CHANGE_TEACHER_ID" nullable="yes" desc="讲师ID" />
                                            <input type="hidden" id="CHANGE_HOLD_COURSE_ID" name="CHANGE_HOLD_COURSE_ID" nullable="yes" desc="担任课程" />
                                            <input type="text" id="CHANGE_HOLD_COURSE_NAME" name="CHANGE_HOLD_COURSE_NAME" readonly="true" nullable="no" placeholder="请选择" desc="担任课程" />
                                        </div>
                                        <div class="more"></div>
                                    </li>
                                    <li class="link">
                                        <div class="label">讲师QQ</div>
                                        <div class="value">
                                            <input type="text" id="CHANGE_QQ_NO" name="CHANGE_QQ_NO" nullable="yes" maxsize="20" placeholder="请输入QQ号码" desc="讲师QQ" />
                                        </div>
                                    </li>
                                    <li class="link">
                                        <div class="label">微信号</div>
                                        <div class="value">
                                            <input type="text" id="CHANGE_WECHAT_NO" name="CHANGE_WECHAT_NO" nullable="yes" maxsize="50" placeholder="请输入微信号" desc="微信号" />
                                        </div>
                                    </li>
                                    <li class="link required">
                                        <div class="label">讲师照片</div>
                                        <div class="value">
                                            <span class="e_mix" ontap="$('#CHANGE_PIC_FILE').trigger('click')">
                                                <input id="CHANGE_PIC" name="CHANGE_PIC" type="text" value="" readonly="true" nullable="no" desc="讲师照片"/>
                                                <span class="e_ico-browse"></span>
                                            </span>
                                            <input type="file" id="CHANGE_PIC_FILE" accept="image/*" name="CHANGE_PIC_FILE" style="display:none" onchange="$.teacher.changeFileChange()"/>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </form>
                        <!-- 列表 结束 -->
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.teacher.changeTeacher();" class="e_button-l e_button-green">修改讲师资料</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-COURSE">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this);">请选择课程</div>
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
                <div class="c_popupItem" id="UI-COURSE_HOLD">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this);">请选择担任课程</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_box">
                            <div class="l_padding">
                                <div id="courseHoldTree" class="c_tree"></div>
                            </div>
                        </div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.teacher.confirmCourse();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-CHANGE_COURSE_HOLD">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this);">请选择担任课程</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_box">
                            <div class="l_padding">
                                <div id="changeCourseHoldTree" class="c_tree"></div>
                            </div>
                        </div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                        <div class="c_space"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.teacher.confirmChangeCourse();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-EMPLOYEE">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this);">请选择内部讲师</div>
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
                                                    <input id="SEARCH_TEXT" name="SEARCH_TEXT" type="text" placeholder="请输入员工姓名（模糊搜索）" nullable="no" desc="查询条件"/>
                                                    <button type="button" class="e_button-blue" ontap="$.teacher.queryEmployee();"><span class="e_ico-search"></span><span>查询</span></button>
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
                            <div class="c_msg c_msg-warn" id="messageboxEmployee" style="display:none">
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
    $.teacher.init();
</script>
</body>
</html>
