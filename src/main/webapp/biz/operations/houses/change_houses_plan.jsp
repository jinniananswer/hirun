<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>变更楼盘规划</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/houses/change.houses.plan.js?v=20180819093500"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">变更楼盘规划</div>
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
                        <div class="label">楼盘名称</div>
                        <div class="value">
                            <input type="hidden" name="HOUSES_ID" id="HOUSES_ID" nullable="no" value="${pageContext.request.getParameter("HOUSES_ID") }" desc="楼盘编号" />
                            <input id="NAME" name="NAME" type="text" nullable="no" desc="楼盘名称"/>
                        </div>
                    </li>
                    <li class="link required">
                        <div class="label">楼盘性质</div>
                        <div class="value">
                            <span id="houseNatureContainer"></span>
                        </div>
                    </li>
                    <li class="required link" ontap="$('#CITY_TEXT').focus();$('#CITY_TEXT').blur();showPopup('UI-popup','UI-CITY')">
                        <div class="label">归属城市</div>
                        <div class="value">
                            <input type="text" id="CITY_TEXT" name="CITY_TEXT" nullable="no" readonly="true" desc="归属城市" />
                            <input type="hidden" name="CITY" id="CITY" nullable="no" desc="归属城市" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="link" ontap="$('#AREA_TEXT').focus();$('#AREA_TEXT').blur();showPopup('UI-popup','UI-AREA')">
                        <div class="label">归属区域</div>
                        <div class="value">
                            <input type="text" id="AREA_TEXT" name="AREA_TEXT" nullable="yes" readonly="true" desc="归属区域" />
                            <input type="hidden" name="AREA" id="AREA" nullable="yes" desc="归属区域" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="required link" ontap="$('#SHOP_TEXT').focus();$('#SHOP_TEXT').blur();showPopup('UI-popup','UI-SHOP')">
                        <div class="label">归属门店</div>
                        <div class="value">
                            <input type="text" id="SHOP_TEXT" name="SHOP_TEXT" nullable="no" readonly="true" desc="责任店面" />
                            <input type="hidden" id="SHOP" name="SHOP" nullable="no" desc="责任店面" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="link required" ontap="$('#CHECK_DATE').focus();$('#CHECK_DATE').blur();">
                        <div class="label">交房时间</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="CHECK_DATE" name="CHECK_DATE" datatype="date" readonly="true" desc="交房时间"/>
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="link required">
                        <div class="label">总户数</div>
                        <div class="value"><input id="HOUSE_NUM" name="HOUSE_NUM" datatype="numeric" type="text" nullable="no" desc="总户数" onblur="$.housesPlan.checkHouseNum()"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">计划分配家装顾问数</div>
                        <div class="value"><input id="PLAN_COUNSELOR_NUM" name="PLAN_COUNSELOR_NUM" datatype="numeric" type="text" nullable="no" disabled="true" desc="计划分配家装顾问数" value="0"/></div>
                    </li>
                    <li class="link" ontap="$('#PLAN_IN_DATE').focus();$('#PLAN_IN_DATE').blur();">
                        <div class="label">计划进入时间</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="PLAN_IN_DATE" name="PLAN_IN_DATE" datatype="date" readonly="true" desc="计划进入时间" />
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="link" ontap="$('#ACTUAL_IN_DATE').focus();$('#ACTUAL_IN_DATE').blur();">
                        <div class="label">实际进入时间</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="ACTUAL_IN_DATE" name="ACTUAL_IN_DATE" datatype="date" readonly="true" desc="实际进入时间" />
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="link" ontap="$('#EMPLOYEE_NAME').focus();$('#EMPLOYEE_NAME').blur();$.housesPlan.initCounselor();showPopup('UI-popup','UI-popup-query');">
                        <div class="label">家装顾问</div>
                        <div class="value"><span class="e_mix">
                            <input type="text" id="EMPLOYEE_NAME" name="EMPLOYEE_NAME" datatype="text" nullable="yes" readonly="true" desc="家装顾问" value="" />
                            <input type="hidden" id="EMPLOYEE_ID" name="EMPLOYEE_ID" datatype="text" nullable="yes" desc="家装顾问" value="" />
                            <input type="hidden" id="OLD_EMPLOYEE_ID" name="OLD_EMPLOYEE_ID" datatype="text" nullable="yes" desc="家装顾问" value="" />
                            <span class="e_ico-check"></span>
							</span></div>
                    </li>
                </ul>
            </div>
            <!-- 表单 结束 -->
        </div>
        <div class="c_space"></div>
        <!-- 提交 开始 -->
        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.housesPlan.submit()">提交</button>
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
                <div class="c_popupItem" id="UI-popup-query">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">请选择家装顾问</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_COUNSELORS">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <input type="hidden" id="SELECTED_EMPLOYEE_ID" name="SELECT_EMPLOYEE_ID" datatype="text" nullable="yes" desc="家装顾问" value="" />
                            <input type="hidden" id="SELECTED_EMPLOYEE_NAME" name="SELECTED_EMPLOYEE_NAME" datatype="text" nullable="yes" desc="家装顾问ID" value="" />
                            <button type="button" id="CONFIRM_COUNSELOR" name="CONFIRM_COUNSELOR" ontap="$.housesPlan.confirmCounselor(false)" class="e_button-l e_button-green">确定</button>
                        </div>
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
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-3 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_CITY">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-AREA">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择区域</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_AREA">


                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                    </div>
                    <!-- 滚动 结束 -->
                </div>
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
            </div>
        </div>
    </div>
</div>

<!-- 弹窗 结束 -->

<script>
    Wade.setRatio();
    $.housesPlan.init();
</script>
</body>
</html>