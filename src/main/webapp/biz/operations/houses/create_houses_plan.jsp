<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/4/25
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>Always Online Office</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/houses/create.houses.plan.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header">
    <div class="back" ontap="back();">新增楼盘规划</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div id="UI-other">
            <!-- 表单 开始 -->
            <div class="c_list c_list-line c_list-border">
                <ul>

                    <li class="link required">
                        <div class="label">楼盘名称</div>
                        <div class="value"><input id="NAME" name="NAME" type="text" nullable="no" desc="楼盘名称"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">楼盘性质</div>
                        <div class="value">
							<span class="e_segment">
								<span idx="0" val="0">重点期盘</span>
								<span idx="1" val="1">重点现盘</span>
								<span idx="2" val="2">责任楼盘</span>
                                <input type="hidden" name="NATURE" id="NATURE" nullable="no" desc="级别" />
							</span>
                        </div>
                    </li>
                    <li class="required link" ontap="showPopup('UI-popup','UI-CITY')">
                        <div class="label">归属城市</div>
                        <div class="value">
                            <input type="text" id="CITY_TEXT" name="CITY_TEXT" nullable="no" desc="归属城市" />
                            <input type="hidden" name="CITY" id="CITY" nullable="no" desc="归属城市" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="required link" ontap="showPopup('UI-popup','UI-AREA')">
                        <div class="label">归属区域</div>
                        <div class="value">
                            <input type="text" id="AREA_TEXT" name="AREA_TEXT" nullable="no" desc="归属区域" />
                            <input type="hidden" name="AREA" id="AREA" nullable="no" desc="归属区域" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="required link" ontap="showPopup('UI-popup','UI-SHOP')">
                        <div class="label">归属门店</div>
                        <div class="value">
                            <input type="text" id="SHOP_TEXT" name="SHOP_TEXT" nullable="no" desc="责任店面" />
                            <input type="hidden" id="SHOP" name="SHOP" nullable="no" desc="责任店面" />
                        </div>
                        <div class="more"></div>
                    </li>
                    <li class="link required">
                        <div class="label">交房时间</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="CHECK_DATE" name="CHECK_DATE" datatype="date" desc="交房时间"/>
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="link required">
                        <div class="label">楼栋</div>
                        <div class="value"><input id="BUILD_NUM" name="BUILD_NUM" datatype="text" type="text" nullable="no" desc="楼栋"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">户数</div>
                        <div class="value"><input id="HOUSE_NUM" name="HOUSE_NUM" datatype="numeric" type="text" nullable="no" desc="总户数"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">计划进入时间</div>
                        <div class="value"><span class="e_mix">
								<input type="text" id="PLAN_IN_DATE" name="PLAN_IN_DATE" datatype="date" desc="计划进入时间" />
								<span class="e_ico-date"></span>
							</span></div>
                    </li>
                    <li class="link required">
                        <div class="label">家装顾问</div>
                        <div class="value"><span class="e_mix" ontap="showPopup('UI-popup','UI-popup-query')">
                            <input type="text" id="COUNSELOR_NAME" name="COUNSELOR_NAME" value="" />
                            <input type="hidden" id="COUNSELOR_ID" name="COUNSELOR_ID" value="1" />
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
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="goto('../home.html')">提交</button>
        </div>
        <!-- 提交 结束 -->
        <div class="c_space"></div>

    </div>
</div>
<!-- 滚动 结束 -->
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
                        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_COUNSELORS">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-CITY">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">选择城市</div>
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
                        <div class="back" ontap="hidePopup(this);">选择区域</div>
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
                        <div class="back" ontap="hidePopup(this);">选择门店</div>
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
