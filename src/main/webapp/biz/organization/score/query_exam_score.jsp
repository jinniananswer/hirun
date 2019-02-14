
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>考试成绩查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/score/exam.score.query.js"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="$.redirect.closeCurrentPage();">考试成绩查询</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 滚动开始 -->
<div class="c_scroll c_scroll-float c_scroll-header" id="scroll_div">

    <div id="tip" class="c_tip">请点击标题栏右侧的放大镜，选择查询条件来进行查询</div>

    <div class="c_msg c_msg-warn" id="queryMessage" style="display:none">
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
    <!-- 筛选 结束 -->
    <div class="c_list c_list-line c_list-border c_list-space l_padding">
        <ul id="employees">

        </ul>
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
                                <%--
                                <li class="link" ontap="$('#CITY_TEXT').focus();$('#CITY_TEXT').blur();forwardPopup('UI-popup','UI-CITY')">
                                    <div class="label">工作城市</div>
                                    <div class="value">
                                        <input type="text" id="CITY_TEXT" name="CITY_TEXT" nullable="yes" readonly="true" desc="责任店面" />
                                        <input type="hidden" id="CITY" name="CITY" nullable="yes" desc="归属门店" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                --%>
                                <%-->
                                <li class="link">
                                    <div class="label">手机号码</div>
                                    <div class="value"><input id="MOBILE_NO" name="MOBILE_NO" type="text" equsize="11" datatype="mbphone" nullable="yes" desc="手机号码"/></div>
                                </li>
                                <--%>
                                <%-->
                                <li class="link">
                                    <div class="label">身份证</div>
                                    <div class="value"><input id="IDENTITY_NO" name="IDENTITY_NO" type="text" maxsize="20" datatype="pspt" nullable="yes" desc="身份证"/></div>
                                </li>
                                <--%>

                                <li class="link" ontap="$('#ORG_TEXT').focus();$('#ORG_TEXT').blur();forwardPopup('UI-popup','UI-ORG')">
                                    <div class="label">所属部门</div>
                                    <div class="value">
                                        <input type="text" id="ORG_TEXT" name="ORG_TEXT" nullable="yes" readonly="true" desc="所属部门" />
                                        <input type="hidden" id="ORG_ID" name="ORG_ID" nullable="yes" desc="所属部门" />
                                    </div>
                                    <div class="more"></div>
                                </li>

                                <%-->
                                <li class="link" ontap="$('#JOB_TEXT').focus();$('#JOB_TEXT').blur();forwardPopup('UI-popup','UI-JOB')">
                                    <div class="label">岗位</div>
                                    <div class="value">
                                        <input type="text" id="JOB_TEXT" name="JOB_TEXT" nullable="yes" readonly="true" desc="岗位" />
                                        <input type="hidden" id="JOB_ROLE" name="JOB_ROLE" nullable="yes" desc="岗位" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                <--%>
                                <%-->
                                <li class="link" ontap="$('#PARENT_EMPLOYEE_NAME').focus();$('#PARENT_EMPLOYEE_NAME').blur();forwardPopup('UI-popup','UI-PARENT')">
                                    <div class="label">上级员工</div>
                                    <div class="value">
                                        <input type="text" id="PARENT_EMPLOYEE_NAME" name="PARENT_EMPLOYEE_NAME" nullable="yes" readonly="true" desc="上级员工" />
                                        <input type="hidden" id="PARENT_EMPLOYEE_ID" name="PARENT_EMPLOYEE_ID" nullable="yes" desc="上级员工" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                <--%>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.employee.query();" class="e_button-l e_button-green">确定</button>
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
                <div class="c_popupItem" id="UI-PARENT">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择上级员工</div>
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
                                                    <button type="button" class="e_button-blue" ontap="$.employee.queryParent();"><span class="e_ico-search"></span><span>查询</span></button>
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
                                    <ul id="parent_employees">

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
                                                    <button type="button" class="e_button-blue" ontap="$.employee.queryJobs();"><span class="e_ico-search"></span><span>查询</span></button>
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
                <div class="c_popupItem" id="UI-CITY">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择城市</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll">
                        <div class="c_box">
                            <div class="c_list c_list-line">
                                <ul id="citys">

                                </ul>
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
    $.employee.init();
</script>
</body>
</html>
