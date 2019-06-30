<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>变更客户代表</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/change.customerservice.js?v=20190419"></script>
</head>
<body>
    <div class="c_header">
        <div class="back" ontap="$.redirect.closeCurrentPage();">变更客户代表</div>
        <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
        </div>
    </div>

    <div class="c_scroll c_scroll-float c_scroll-header" id="scroll_div">
        <div id="tip" class="c_tip">请点击标题栏右侧的放大镜，先查询需要变更的客户列表，再选择新客户代表提交</div>
            <!--开始-->
            <div class="l_padding">

                <div class="c_title">
                    <div class="fn">
                        <ul>
                           <li ontap="$.changecustomerservice.chooseNewCustService();"><span class="e_ico-change"></span>选择新客户代表</li>
                        </ul>
                    </div>
                </div>
                            <!--提示开始-->
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
                            <!--提示结束-->
                            <!--party信息展示开始-->
                <div class="c_list c_list-line c_list-border c_list-space l_padding">
                    <ul id="partyinfos">

                    </ul>
                </div>
                            <!--party信息展示结束-->
                <div class="c_space"></div>
                <div class="c_space"></div>
                <div class="c_space"></div>
                <div class="c_space"></div>
           </div>
           <!--padding结束 -->
    </div>
    <!--滚动结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
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
                                        <li class="link">
                                            <div class="label">电话号码</div>
                                            <div class="value"><input id="MOBILE" name="MOBILE" type="text" nullable="yes" desc="电话号码"/></div>
                                         </li>
                                         <li>
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
                                    </ul>
                                </div>
                                <!-- 列表 结束 -->
                                 <div class="c_space"></div>
                                <div class="c_submit c_submit-full">
                                    <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changecustomerservice.query();" class="e_button-l e_button-green">查询</button>
                                </div>
                                  <div class="c_space-3"></div>
                            </div>



                        </div>

                        <div class="c_popupItem" id="UI-CHOOSECUSTSERVICE">
                            <div class="c_header">
                                <div class="back" ontap="hidePopup(this);">请选择客户代表</div>
                            </div>
                           <div class="c_scroll">
                            <div class="l_padding">
                                <div class="c_box">
                                    <!-- 表单 开始 -->
                                    <div class="c_list c_list-form">
                                        <ul id="searchNewCustService">
                                            <li>
                                                <div class="value">
                                                        <span class="e_mix">
                                                            <input id="SEARCH_TEXT" name="SEARCH_TEXT" type="text" placeholder="请输入员工姓名（模糊搜索）" nullable="yes" desc="查询条件"/>
                                                            <button type="button" class="e_button-blue" ontap="$.changecustomerservice.searchNewCustService();"><span class="e_ico-search"></span><span>查询</span></button>
                                                        </span>
                                                </div>
                                            </li>

                                             <li class="link">
                                                <div class="label">是否跨店:</div>
                                                <div class="value">
                                                    <div class="e_switch ">
                                                    	<div class="e_switchOn"></div>
 	                                                <div class="e_switchOff"></div>
 	                                                <input type="hidden" id="mySwitch" name="mySwitch"/>
                                                    </div>
                                                </div>
                                             </li>

                                        </ul>
                                    </div>
                                    <!-- 表单 结束 -->
                                </div>
                            </div>
                                <div class="l_padding">
                                    <div class="c_box">
                                        <div class="c_list c_list-form">
                                            <ul id="custservicesinfo">

                                             </ul>
                                        </div>
                                    </div>
                                  <div class="c_space-3"></div>
                                    <div id="submitButton" class="c_submit c_submit-full" style="display:none">
                                         <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.changecustomerservice.confirmCustService()">提交</button>
                                    </div>
                                  <div class="c_space-3"></div>
                                  <div class="c_space-3"></div>
                                  <div class="c_space-3"></div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="c_popupGroup">
                        <div class="c_popupItem" id="UI-QUERYCUSTSERVICE">
                            <div class="c_header">
                                <div class="back" ontap="hidePopup(this);">请选择客户代表</div>
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
                    </div>
                </div>
            </div>
        </div>


<script>
    Wade.setRatio();
    $.changecustomerservice.init();
</script>
</body>
</html>
