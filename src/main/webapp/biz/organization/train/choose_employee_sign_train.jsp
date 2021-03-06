<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/1/12
  Time: 11:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>选择员工报名</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/train/choose.employee.sign.train.js?v=20190505"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="$.redirect.closeCurrentPage();">选择培训报名员工</div>
    <div class="fn">
        <input type="hidden" name="TRAIN_ID" id="TRAIN_ID" nullable="no" value="${pageContext.request.getParameter("TRAIN_ID") }" desc="培训ID" />
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 滚动开始 -->
<div class="c_scroll c_scroll-float c_scroll-header" id="scroll_div">
    <div class="l_padding">
        <div id="tip" class="c_tip" style="display:none"></div>

        <!-- 筛选 结束 -->
        <div class="c_title">
            <div class="text" id="train_name"></div>
            <div class="fn">
                <ul id="addArea">
                    <li ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-add"></span>选择报名人员</li>
                </ul>
            </div>
        </div>

        <div class='c_box c_box-border' id="new_sign">
            <div class='c_title' ontap="$(this).next().toggle();">
                <div class="text e_strong e_blue">
                    本次新报名人员
                </div>
                <div class="fn">
                    <ul>
                        <li>
                        <span id="new_num">
                            人数：0
                        </span>
                            <span class="e_ico-fold"></span>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="l_padding l_padding-u">
                <div class="c_list c_list-v c_list-col-6 c_list-phone-col-3 c_list-fixWrapSpace">
                    <ul id="new_employees">

                    </ul>
                </div>
            </div>
        </div>
        <div class="c_space"></div>
        <div class='c_box c_box-border'>
            <div class='c_title' ontap="$(this).next().toggle();">
                <div class="text e_strong e_blue">
                    已报名人员
                </div>
                <div class="fn">
                    <ul>
                        <li>
                        <span id="signed_num">
                            0
                        </span>
                            <span class="e_ico-fold"></span>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="l_padding l_padding-u">
                <div class="c_list c_list-v c_list-col-6 c_list-phone-col-3 c_list-fixWrapSpace">
                    <ul id="employees">

                    </ul>
                </div>
            </div>
        </div>
        <div class="c_space"></div>
        <div class="c_submit c_submit-full">
            <button type="button" id="delTrainEmployee" name="delTrainEmployee" class="e_button-r e_button-l e_button-red" ontap="$.train.submitDelete()">删除已报名人员</button>
            <button type="button" id="addTrainEmployee" name="addTrainEmployee" class="e_button-r e_button-l e_button-green" ontap="$.train.submitAdd()">提交新报名人员</button>
        </div>
    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
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
                                    <div class="label">姓名</div>
                                    <div class="value"><input id="NAME" name="NAME" type="text" nullable="yes" desc="姓名"/></div>
                                </li>
                                <li class="link" ontap="$('#ORG_TEXT').focus();$('#ORG_TEXT').blur();forwardPopup('UI-popup','UI-ORG')">
                                    <div class="label">所属部门</div>
                                    <div class="value">
                                        <input type="text" id="ORG_TEXT" name="ORG_TEXT" nullable="yes" readonly="true" desc="所属部门" />
                                        <input type="hidden" id="ORG_ID" name="ORG_ID" nullable="yes" desc="所属部门" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                <li class="link" ontap="$('#JOB_TEXT').focus();$('#JOB_TEXT').blur();forwardPopup('UI-popup','UI-JOB')">
                                    <div class="label">岗位</div>
                                    <div class="value">
                                        <input type="text" id="JOB_TEXT" name="JOB_TEXT" nullable="yes" readonly="true" desc="岗位" />
                                        <input type="hidden" id="JOB_ROLE" name="JOB_ROLE" nullable="yes" desc="岗位" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_submit c_submit-full">
                            <button type="button" name="SUBMIT_QUERY" ontap="$.train.query();" class="e_button-l e_button-navy">查询</button>
                        </div>
                        <div class="c_msg" id="messagebox">
                            <div class="wrapper">
                                <div class="emote"></div>
                                <div class="info">
                                    <div class="text">
                                        <div class="title">提示信息</div>
                                        <div class="content">请输入查询条件查询要报名的人员~~</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="c_list c_list-line c_list-border c_list-space l_padding">
                            <ul id="select_employees">

                            </ul>
                        </div>
                    </div>

                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.train.confirmSelectEmployee();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="BUSI_GRADE_SELECT">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this);">请选择业绩等级</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul>
                                <li class="link">
                                    <div class="label">业绩等级</div>
                                    <div class="value">
                                        <span id="grade_type">

                                        </span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="l_bottom">
                            <div class="c_submit c_submit-full">
                                <input type="hidden" name="GRADE_EMPLOYEE_ID" id="GRADE_EMPLOYEE_ID" nullable="no" value="" desc="考评员工ID" />
                                <button type="button" id="CONFIRM_GRADE" name="CONFIRM_GRADE" ontap="$.train.confirmGrade();" class="e_button-l e_button-green">确定</button>
                            </div>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-ORG">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择部门</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_box">
                            <div class="l_padding">
                                <div id="orgTree" class="c_tree"></div>
                            </div>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-JOB">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择岗位</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="jobArea">
                                    <li>
                                        <div class="value">
                                                <span class="e_mix">
                                                    <input id="JOB_SEARCH_TEXT" name="JOB_SEARCH_TEXT" type="text" placeholder="请输入岗位名称（模糊搜索）" nullable="no" desc="查询条件"/>
                                                    <button type="button" class="e_button-blue" ontap="$.train.queryJobs();"><span class="e_ico-search"></span><span>查询</span></button>
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
                            <div class="c_box">
                                <div class="c_list c_list-line">
                                    <ul id="jobRoles">

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

<script>
    Wade.setRatio();
    $.train.init();
</script>
</body>
</html>
