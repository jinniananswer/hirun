<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/5/4
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>楼盘规划查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/houses/query.houses.plan.js"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="back();">楼盘规划查询</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 滚动开始 -->
<div class="c_scroll c_scroll-float c_scroll-header" id="scroll_div">
    <!-- 筛选 结束 -->
    <div class="c_list c_list-line c_list-border c_list-space l_padding">
        <ul id="houses_plan">

        </ul>
    </div>
</div>
<!-- 滚动 结束 -->

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
                                    <div class="label">楼盘名称</div>
                                    <div class="value"><input id="NAME" name="NAME" type="text" nullable="yes" desc="楼盘名称"/></div>
                                </li>
                                <li class="link">
                                    <div class="label">楼盘性质</div>
                                    <div class="value">
                                        <span id="nature_select">

                                        </span>
                                    </div>
                                </li>
                                <li class="link">
                                    <div class="label">审核状态</div>
                                    <div class="value">
                                        <span id="audit_select">

                                        </span>
                                    </div>
                                </li>
                                <li class="link" ontap="forwardPopup(this,'UI-CITY')">
                                    <div class="label">归属城市</div>
                                    <div class="value">
                                        <input type="text" id="CITY_TEXT" name="CITY_TEXT" nullable="yes" desc="归属城市" />
                                        <input type="hidden" name="CITY" id="CITY" nullable="yes" desc="归属城市" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                <li class="link" ontap="forwardPopup(this,'UI-AREA')">
                                    <div class="label">归属区域</div>
                                    <div class="value">
                                        <input type="text" id="AREA_TEXT" name="AREA_TEXT" nullable="yes" desc="归属区域" />
                                        <input type="hidden" name="AREA" id="AREA" nullable="yes" desc="归属区域" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                <li class="link" ontap="forwardPopup('UI-popup','UI-SHOP')">
                                    <div class="label">归属门店</div>
                                    <div class="value">
                                        <input type="text" id="SHOP_TEXT" name="SHOP_TEXT" nullable="yes" desc="责任店面" />
                                        <input type="hidden" id="SHOP" name="SHOP" nullable="yes" desc="责任店面" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                <li class="link">
                                    <div class="label">交房时间大于</div>
                                    <div class="value">
                                        <span class="e_mix">
                                            <input type="text" id="CHECK_DATE" name="CHECK_DATE" datatype="date" nullable="yes" desc="交房时间"/>
                                            <span class="e_ico-date"></span>
                                        </span>
                                    </div>
                                </li>
                                <li class="link">
                                    <div class="label">总户数大于</div>
                                    <div class="value"><input id="HOUSE_NUM" name="HOUSE_NUM" datatype="numeric" type="text" nullable="yes" desc="总户数" /></div>
                                </li>
                                <li class="link">
                                    <div class="label">计划分配家装顾问数大于</div>
                                    <div class="value"><input id="PLAN_COUNSELOR_NUM" name="PLAN_COUNSELOR_NUM" datatype="numeric" type="text" nullable="yes" desc="计划分配家装顾问数"/></div>
                                </li>
                                <li class="link">
                                    <div class="label">计划进入时间大于</div>
                                    <div class="value">
                                        <span class="e_mix">
                                            <input type="text" id="PLAN_IN_DATE" name="PLAN_IN_DATE" datatype="date" desc="计划进入时间" />
                                            <span class="e_ico-date"></span>
                                        </span>
                                    </div>
                                </li>
                                <li class="link">
                                    <div class="label">家装顾问</div>
                                    <div class="value">
                                        <span class="e_mix" ontap="$.housesPlan.initCounselor()">
                                            <input type="text" id="EMPLOYEE_NAME" name="EMPLOYEE_NAME" datatype="text" nullable="yes" desc="家装顾问" value="" />
                                            <input type="hidden" id="EMPLOYEE_ID" name="EMPLOYEE_ID" datatype="text" nullable="yes" desc="家装顾问" value="" />
                                            <span class="e_ico-check"></span>
                                        </span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.housesPlan.query();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-CITY">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">请选择城市</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_CITY">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                </div>
                <div class="c_popupItem" id="UI-AREA">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this);">请选择区域</div>
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
                        <div class="back" ontap="backPopup(this);">请选择门店</div>
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
                <div class="c_popupItem" id="UI-COUNSELOR">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">请选择家装顾问</div>
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
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->

<script>
    Wade.setRatio();
    $.housesPlan.initQuery();
</script>
</body>
</html>
