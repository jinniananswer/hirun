<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title></title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/customerservice.manager.js?v=201907016"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">客户代表管理界面</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">

    <div class="l_padding">
        <div class="c_title">
            <div class="text">客户列表</div>
            <div class="fn">
                <ul>
                    <li ontap="$.redirect.open('biz/operations/customerservice/create_goodseeliveinfo.jsp','客户需求信息录入');"><span class="e_ico-add"></span>新增客户需求信息</li>
                </ul>
            </div>
        </div>
        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">没有找到和您相关的客户信息~~</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="partyinfos">

            </ul>
        </div>

    </div>
   <div class="c_space-4"></div>
   <div class="c_space-4"></div>



</div>
<!-- 滚动 结束 -->
    <div class="l_bottom  c_submit-full ">
          <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.custservicemanager.init();" class="e_button-r e_button-l e_button-green">刷新</button>
    </div>
    <div class="c_space-4"></div>
    <div class="c_space-4"></div>

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
                    <div class="c_scroll c_scroll-float c_scroll-header ">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="queryArea">

                                <li class="link">
                                    <div class="label">客户姓名</div>
                                    <div class="value"><input id="NAME" name="NAME" type="text" nullable="yes"  desc="姓名"/></div>
                                </li>

                                <li class="link">
                                    <div class="label">微信昵称</div>
                                    <div class="value"><input id="WXNICK" name="WXNICK" type="text" nullable="yes"  desc="微信昵称"/></div>
                                </li>

                                <li class="link">
                                    <div class="label">电话号码</div>
                                    <div class="value"><input id="MOBLIE" name="MOBLIE" type="text" nullable="yes"  desc="电话号码"/></div>
                                </li>

                                <li class="link">
                                    <div class="label">楼盘地址</div>
                                    <div class="value"><input id="HOUSEADDRESS" name="HOUSEADDRESS" type="text" nullable="yes"  desc="楼盘地址"/></div>
                                </li>

                                 <li id="childcustservice" style="none">
                                     <div class="label">客户代表</div>
                                         <div class="value">
                                             <span class="e_mix" ontap="forwardPopup('UI-popup','UI-QUERYCUSTSERVICE')">
                                             <input type="text" id="CUSTSERVICEEMPLOYEENAME" name="CUSTSERVICEEMPLOYEENAME" datatype="text"
                                                    nullable="yes" desc="客户代表" value="" readonly="true"/>
                                             <input type="hidden" id="CUSTSERVICEEMPLOYEEID" name="CUSTSERVICEEMPLOYEEID" placeholder="" nullable="yes" desc="" />
                                             <span class="e_ico-check"></span>
                                             </span>
                                         </div>
                                  </li>

                                  <li class="link" ontap="$('#TAG_TEXT').focus();$('#TAG_TEXT').blur();forwardPopup('UI-popup','UI-TAG4QUERY')">
                                    <div class="label">客户标签</div>
                                    <div class="value">
                                        <input type="text" id="TAG_TEXT" name="TAG_TEXT" nullable="yes" readonly="true" desc="标签ID" />
                                        <input type="hidden" id="QUERY_TAG_ID" name="QUERY_TAG_ID" nullable="yes" desc="标签ID" />
                                    </div>
                                    <div class="more"></div>
                                  </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
						<div class="c_space"></div>

                            <div class="c_submit c_submit-full">
                                <button type="button" id="SUBMIT_CLEAR" name="SUBMIT_CLEAR" ontap="$.custservicemanager.clearCond();" class="e_button-r e_button-l e_button-green">重置</button>
                                <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.custservicemanager.query();" class="e_button-r e_button-l e_button-green">查询</button>
                            </div>

                    </div>

                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-popup-tag-cond">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">标签管理</div>
                    </div>
                    <input type="hidden" id="PARTY_ID" name="PARTY_ID" placeholder="" nullable="yes" desc="" />

                    <div class="c_scroll c_scroll-float c_scroll-header ">
                        <!-- 列表 开始 -->
                     <div class="l_padding">
                        <div class="c_list c_list-v c_list-col-3 c_list-phone-col-3 c_list-space c_list-line c_list-s">
                            <ul id="partyTagInfo">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
						<div class="c_space"></div>

                            <div class="c_submit c_submit-full">
                                <button type="button" id="SUBMIT_PARTYTAGINFO" name="SUBMIT_PARTYTAGINFO" ontap="$.custservicemanager.submitPartyTagInfo();" class="e_button-r e_button-l e_button-green">保存</button>
                            </div>
                     </div>
                    </div>

                    <!-- 滚动 结束 -->
                </div>
            </div>

                    <div class="c_popupGroup">
                        <div class="c_popupItem" id="UI-QUERYCUSTSERVICE">
                            <div class="c_header">
                                <div class="back" ontap="backPopup(this);">请选择客户代表</div>
                            </div>
                            <div class="l_padding">

                            </div>
                            <div class="c_scroll">
                                <div class="l_padding">
                                    <div class="c_box">
                                        <div class="c_list c_list-line">
                                            <ul id="querycustservicesinfo">

                                            </ul>
                                        </div>
                                    </div>
                                     <div class="c_space-3"></div>
                                     <div class="c_space-3"></div>
                                     <div class="c_space-3"></div>
                                </div>
                            </div>
                        </div>
                        <div class="c_popupItem" id="UI-TAG4QUERY">
                            <div class="c_header">
                                <div class="back" ontap="backPopup(this)">客户标签</div>
                            </div>

                            <div class="c_scroll c_scroll-float c_scroll-header ">
                            <!-- 列表 开始 -->
                                <div class="l_padding">
                                    <div class="c_list c_list-v c_list-col-3 c_list-phone-col-3 c_list-space c_list-line c_list-s">
                                        <ul id="TagInfo">

                                        </ul>
                                    </div>
                            <!-- 列表 结束 -->
 						        <div class="c_space"></div>
                                </div>
                            </div>

                            <!-- 滚动 结束 -->
                         </div>
                    </div>

            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->

<script>
    Wade.setRatio();
    $.custservicemanager.init();
</script>
</body>
</html>
