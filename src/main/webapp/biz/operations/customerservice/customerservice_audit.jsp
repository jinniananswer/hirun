<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户代表审核</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/customerservice.audit.js?v=20200620"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">客户代表审核界面</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
	<div class="c_tip c_tip-red">客户清理审核只针对无效客户、测试客户。其他类型客户请通过标签方式标记。</div>
    <div class="l_padding">
        <div class="c_title">
            <div class="text">申请列表</div>

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
            <ul id="applyinfos">

            </ul>
        </div>

    </div>
    <div class="c_space-4"></div>
    <div class="c_space-4"></div>
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

                                <li class="link">
                                     <div class="label">业务类型</div>
                                     <div class="value">
                                           <span id="busitype"></span>
                                      </div>
                                </li>

                                <li class="link">
                                     <div class="label">审批状态</div>
                                     <div class="value">
                                           <span id="auditstat"></span>
                                      </div>
                                </li>

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY1" name="SUBMIT_QUERY1" ontap="$.custserviceaudit.clearCond();" class="e_button-r e_button-l e_button-green">重置</button>
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.custserviceaudit.query();" class="e_button-r e_button-l e_button-green">查询</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>

            <div class="c_popupGroup">
                  <div class="c_popupItem" id="UI-QUERYCUSTSERVICE">
                        <div class="c_header">
                                 <div class="back" ontap="hidePopup(this);">请选择客户代表</div>
                        </div>
                          <div class="l_padding">
                                 <div class="c_box">
                                     <div class="c_list c_list-line">
                                        <ul id="jobArea">
                                            <li>
                                                 <div class="value">
                                                     <span class="e_mix">
                                                         <input id="CUSTSERVICE_NAME" name="CUSTSERVICE_NAME" type="text" placeholder="请输入客户代表姓名（模糊搜索）" nullable="no" desc="查询条件"/>
                                                         <button type="button" class="e_button-blue" ontap="$.custserviceaudit.queryCustService();"><span class="e_ico-search"></span><span>查询</span></button>
                                                     </span>
                                                 </div>
                                            </li>
                                        </ul>
                                     </div>
                                 </div>
                          </div>

                            <div class="c_msg c_msg-warn" id="custservicemessagebox" style="display:none">
                                <div class="wrapper">
                                    <div class="emote"></div>
                                    <div class="info">
                                        <div class="text">
                                            <div class="title">提示信息</div>
                                            <div class="content">没有找到相关的信息~~</div>
                                        </div>
                                    </div>
                                </div>
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
                                      <div class="c_space-3"></div>
                                      <div class="c_space-3"></div>
                             </div>
                  </div>
            </div>

            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->

<script>
    Wade.setRatio();
    $.custserviceaudit.init();
</script>
</body>
</html>
