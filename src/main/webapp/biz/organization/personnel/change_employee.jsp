<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>新增楼盘规划</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/personnel/change.employee.js?v=20190225"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">修改员工档案</div>
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
                        <div class="label">姓名</div>
                        <div class="value">
                            <input id="NAME" name="NAME" type="text" nullable="no" desc="姓名"/>
                            <input type="hidden" name="EMPLOYEE_ID" id="EMPLOYEE_ID" nullable="no" value="${pageContext.request.getParameter("EMPLOYEE_ID") }" desc="员工ID/" />
                        </div>
                    </li>
                    <li class="link required">
                        <div class="label">性别</div>
                        <div class="value">
                            <span id="sexcontainer"></span>
                        </div>
                    </li>
                    <li class="link required" ontap="$('#CITY_TEXT').focus();$('#CITY_TEXT').blur();showPopup('UI-popup','UI-CITY')">
                        <div class="label">工作城市</div>
                        <div class="value">
                            <input type="text" id="CITY_TEXT" name="CITY_TEXT" nullable="no" readonly="true" desc="工作城市" />
                            <input type="hidden" id="CITY" name="CITY" nullable="no" desc="工作城市" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="required link">
                        <div class="label">手机号码</div>
                        <div class="value"><input id="MOBILE_NO" name="MOBILE_NO" type="text" equsize="11" datatype="mbphone" nullable="no" desc="手机号码"/></div>
                    </li>
                    <li class="required link">
                        <div class="label">身份证</div>
                        <div class="value"><input id="IDENTITY_NO" name="IDENTITY_NO" type="text" maxsize="20" datatype="pspt" nullable="no" desc="身份证"/></div>
                    </li>
                    <li class="required link">
                        <div class="label">家庭住址</div>
                        <div class="value"><input id="HOME_ADDRESS" name="HOME_ADDRESS" type="text" maxsize="100" datatype="text" nullable="no" desc="家庭住址"/></div>
                    </li>
                    <li class="link required" ontap="$('#IN_DATE').focus();$('#IN_DATE').blur();">
                        <div class="label">入职日期</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="IN_DATE" name="IN_DATE" datatype="date" readonly="true" desc="计划进入时间" />
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="link required" ontap="$('#REGULAR_DATE').focus();$('#REGULAR_DATE').blur();">
                        <div class="label">转正日期</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="REGULAR_DATE" name="REGULAR_DATE" datatype="date" readonly="true" desc="转正日期" />
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="required link" ontap="$('#ORG_TEXT').focus();$('#ORG_TEXT').blur();showPopup('UI-popup','UI-ORG')">
                        <div class="label">所属部门</div>
                        <div class="value">
                            <input type="text" id="ORG_TEXT" name="ORG_TEXT" nullable="no" readonly="true" desc="所属部门" />
                            <input type="hidden" id="ORG_ID" name="ORG_ID" nullable="no" desc="所属部门" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="link required" ontap="$('#JOB_TEXT').focus();$('#JOB_TEXT').blur();showPopup('UI-popup','UI-JOB')">
                        <div class="label">岗位</div>
                        <div class="value">
                            <input type="text" id="JOB_TEXT" name="JOB_TEXT" nullable="no" readonly="true" desc="岗位" />
                            <input type="hidden" id="JOB_ROLE" name="JOB_ROLE" nullable="no" desc="岗位" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="required link" ontap="$('#PARENT_EMPLOYEE_NAME').focus();$('#PARENT_EMPLOYEE_NAME').blur();showPopup('UI-popup','UI-PARENT')">
                        <div class="label">直属上级</div>
                        <div class="value">
                            <input type="text" id="PARENT_EMPLOYEE_NAME" name="PARENT_EMPLOYEE_NAME" nullable="no" readonly="true" desc="直属上级" />
                            <input type="hidden" id="PARENT_EMPLOYEE_ID" name="PARENT_EMPLOYEE_ID" nullable="no" desc="直属上级" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="link required">
                        <div class="label">最高学历</div>
                        <div class="value">
                            <span id="educationcontainer"></span>
                        </div>
                    </li>
                    <li class="link required">
                        <div class="label">毕业院校</div>
                        <div class="value"><input id="SCHOOL" name="SCHOOL" type="text" nullable="no" desc="毕业院校"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">专业</div>
                        <div class="value"><input id="MAJOR" name="MAJOR" type="text" nullable="no" desc="专业"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">毕业证书编号</div>
                        <div class="value"><input id="CERTIFICATE_NO" name="CERTIFICATE_NO" type="text" nullable="no" desc="毕业证书编号"/></div>
                    </li>
                </ul>
            </div>
            <!-- 表单 结束 -->
        </div>
        <div class="c_space"></div>
        <!-- 提交 开始 -->
        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.employee.submit()">修改员工档案</button>
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
<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
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
                                                    <button type="button" class="e_button-blue" ontap="$.employee.query();"><span class="e_ico-search"></span><span>查询</span></button>
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
                        <div class="l_padding">
                            <div class="c_box">
                                <div class="c_list c_list-line">
                                    <ul id="citys">

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
    $.employee.init();
</script>
</body>
</html>