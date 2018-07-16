<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>新增楼盘规划</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/personnel/create.employee.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">新建员工档案</div>
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
                        <div class="value"><input id="NAME" name="NAME" type="text" nullable="no" desc="姓名"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">性别</div>
                        <div class="value">
                            <span id="sexcontainer"></span>
                        </div>
                    </li>
                    <li class="required link">
                        <div class="label">联系电话</div>
                        <div class="value"><input id="MOBILE_NO" name="MOBILE_NO" type="text" equsize="11" datatype="numeric" nullable="no" desc="联系电话"/></div>
                    </li>
                    <li class="required link">
                        <div class="label">身份证</div>
                        <div class="value"><input id="IDENTITY_NO" name="IDENTITY_NO" type="text" maxsize="20" datatype="pspt" nullable="no" desc="身份证"/></div>
                    </li>
                    <li class="required link">
                        <div class="label">家庭住址</div>
                        <div class="value"><input id="HOME_ADDRESS" name="HOME_ADDRESS" type="text" maxsize="100" datatype="text" nullable="no" desc="家庭住址"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">岗位</div>
                        <div class="value">
                            <span id="jobrolecontainer"></span>
                        </div>
                    </li>
                    <li class="link required" ontap="$('#IN_DATE').focus();$('#IN_DATE').blur();">
                        <div class="label">入职日期</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="IN_DATE" name="IN_DATE" datatype="date" readonly="true" desc="计划进入时间" />
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="required link" ontap="$('#SHOP_TEXT').focus();$('#SHOP_TEXT').blur();showPopup('UI-popup','UI-SHOP')">
                        <div class="label">归属门店</div>
                        <div class="value">
                            <input type="text" id="SHOP_TEXT" name="SHOP_TEXT" nullable="no" readonly="true" desc="责任店面" />
                            <input type="hidden" id="SHOP" name="SHOP" nullable="no" desc="归属门店" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="required link" ontap="$('#PARENT_EMPLOYEE_NAME').focus();$('#PARENT_EMPLOYEE_NAME').blur();showPopup('UI-popup','UI-PARENT')">
                        <div class="label">上级员工</div>
                        <div class="value">
                            <input type="text" id="PARENT_EMPLOYEE_NAME" name="PARENT_EMPLOYEE_NAME" nullable="no" readonly="true" desc="上级员工" />
                            <input type="hidden" id="PARENT_EMPLOYEE_ID" name="PARENT_EMPLOYEE_ID" nullable="no" desc="上级员工" />
                        </div>
                        <div class="more"></div>
                    </li>
                </ul>
            </div>
            <!-- 表单 结束 -->
        </div>
        <div class="c_space"></div>
        <!-- 提交 开始 -->
        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.employee.submit()">提交</button>
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
                <div class="c_popupItem" id="UI-SHOP">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择门店</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_SHOP">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-PARENT">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择上级员工</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_PARENT">

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
    $.employee.init();
</script>
</body>
</html>