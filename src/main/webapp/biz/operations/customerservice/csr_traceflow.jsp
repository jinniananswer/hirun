<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户代表流程</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/csr_traceflow.js"></script>
</head>
<body>
        <div class="c_header">
            <div class="back" ontap="$.redirect.closeCurrentPage();">客户流程</div>
        </div>
        <div class="c_scroll c_scroll-float c_scroll-header" id="scroll_div">
            <input type="hidden" id="PARTY_ID" name="PARTY_ID" nullable="no" value="${pageContext.request.getParameter("PARTY_ID") }" desc="参与人ID" />
            <input type="hidden" id="PROJECT_ID" name="PROJECT_ID" nullable="no" value="${pageContext.request.getParameter("PROJECT_ID") }" desc="参与人ID" />
            <div class="l_col l_col-full l_col-middle">
                <div class="l_colItem">
                    <div class="c_scroll c_scroll-white">
                       <div class="l_padding l_padding-u" style="padding-right:0">
                            <div class="c_timeline c_timeline-border">
                                <ul id="traceflow">

                                </ul>
                            </div>
                       </div>
                    </div>
               </div>
            </div>

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
        </div>

<jsp:include page="/base/buttom/base_buttom.jsp"/>
        <div class="c_popup" id="UI-popup">
            <div class="c_popupBg" id="UI-popup_bg"></div>
            <div class="c_popupBox">
                <div class="c_popupWrapper" id="UI-popup_wrapper">
                    <div class="c_popupGroup">
                        <div class="c_popupItem" id="GZGZHUI-popup-query-cond">
                            <div class="c_header">
                                <div class="back" ontap="hidePopup(this)">公众号关注信息</div>
                            </div>
                            <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                                <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                                    <ul id="queryArea">
                                        <li class="link">
                                             <div class="label">微信昵称：</div>
                                             <div class="value"><input id="WX_NICK" name="WX_NICK" readonly="true" type="text"  nullable="yes" desc="姓名"/></div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="c_popupItem" id="XQLTYUI-popup-query-cond">
                            <div class="c_header">
                                <div class="back" ontap="hidePopup(this)">需求蓝图一信息</div>
                            </div>
                            <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                                <div class="c_list c_list-line c_list-border c_list-space l_padding">
                                    <ul id="xqltyinfo">

                                    </ul>
                                </div>
                            </div>
                        </div>
                         <div class="c_popupItem" id="SCANCITYINFOUI-popup-query-cond">
                             <div class="c_header">
                                 <div class="back" ontap="hidePopup(this)">带看城市木屋列表</div>
                             </div>

                             <div class="c_scroll">
			                    <div class="c_title">
                                    <div class="text"></div>
                                    <div class="fn">
                                     <ul>
                                         <li ontap="forwardPopup('UI-popup','UI-CHOOSECITYCABIN')"><span class="e_ico-add"></span>新增带看城市木屋信息</li>
                                     </ul>
                                     </div>
                                </div>
                                 <div class="l_padding">
                                    <div class="c_box">
                                            <div class="c_list c_list-form c_list-line">
                                                <ul id="scancityhouseinfo">

                                                 </ul>
                                            </div>
                                    </div>
                                 </div>
                                <div class="c_space-3"></div>
                                <div class="c_space-3"></div>

                             </div>
                         </div>

                        <div class="c_popupItem" id="UI-CHOOSEDESIGNER">
                            <div class="c_header">
                                <div class="back" ontap="hidePopup(this);">请选择设计师</div>
                            </div>
                          <div class="c_scroll">

                            <div class="l_padding">
                                <div class="c_box">
                                    <div class="c_list c_list-form">
                                        <ul id="desingersearchArea">

                                            <li>
                                                <div class="value">
                                                        <span class="e_mix">
                                                            <input id="SEARCH_TEXT" name="SEARCH_TEXT" type="text" placeholder="请输入员工姓名（模糊搜索）" nullable="no" desc="查询条件"/>
                                                            <button type="button" class="e_button-blue" ontap="$.csrTraceFlow.queryDesigners();"><span class="e_ico-search"></span><span>查询</span></button>
                                                        </span>
                                                </div>
                                            </li>

                                             <li class="link">
                                                <div class="label">是否跨店:</div>
                                                <div class="value">
                                                    <div class="e_switch" >
                                                    	<div class="e_switchOn"></div>
 	                                                <div class="e_switchOff"></div>
 	                                                <input type="hidden" id="mySwitch" name="mySwitch"  />
                                                    </div>
                                                </div>
                                             </li>



                                        </ul>
                                    </div>
                                </div>

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
                                        <ul id="designers">

                                        </ul>
                                    </div>
                                </div>
                                <div class="c_space-3"></div>
                                <div id="submitButton" class="c_submit c_submit-full">
                                    <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.csrTraceFlow.chooseDesigner()">确认</button>
                                </div>
                                <div class="c_space-3"></div>
                                <div class="c_space-3"></div>
                            </div>
                          </div>
                        </div>

                    </div>

                    <div class="c_popupGroup">
                        <div class="c_popupItem" id="UI-CHOOSECITYCABIN">
                            <div class="c_header">
                                <div class="back" ontap="hidePopup(this);">请选择城市木屋</div>
                            </div>
                          <div class="c_scroll" id="mwinfo">
                            <div class="l_padding">

                                <div class="c_box">
                                    <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                                        <ul id="mwexperience">
                                            <li class="link required">
                                                <div class="label">城市木屋体验时间</div>
                                                <div class="value">
                                                     <span class="e_mix">
                                                     <input type="text" id="MW_EXPERIENCE_TIME" name="MW_EXPERIENCE_TIME" datatype="date" nullable="no" readonly="true" desc="城市木屋体验时间" />
                                                     <span class="e_ico-date"></span>
                                                     </span>
                                               </div>
                                            </li>
                                            <li class="link required">
                                                <div class="label">城市木屋感受：</div>
                                                <div class="value">
                                                   <textarea id="MW_EXPERIENCE" name="MW_EXPERIENCE" placeholder="城市木屋感受" nullable="no" desc="城市木屋感受" class="e_textarea-row-2"></textarea>
                                               </div>
                                            </li>

                                        </ul>
                                    </div>
                                </div>
                                <div class="c_space-3"></div>

                                <div class="c_box">
                                    <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                                        <ul id="mwexperience">
                                             <li>
                                                 <div class="value">
                                                         <span class="e_mix">
                                                             <input id="SEARCH_CITYCABIN" name="SEARCH_CITYCABIN" type="text" placeholder="请输入城市木屋信息（模糊搜索）" nullable="yes" desc="查询条件"/>
                                                             <button type="button" class="e_button-blue" ontap="$.csrTraceFlow.queryCityCabinByName();"><span class="e_ico-search"></span><span>查询</span></button>
                                                         </span>
                                                 </div>
                                             </li>
                                        </ul>
                                    </div>
                                </div>

                             <div class="c_msg c_msg-warn" id="querycitycabinmessage" style="display:none">
                                <div class="wrapper">
                                    <div class="emote"></div>
                                    <div class="info">
                                        <div class="text">
                                            <div class="title"></div>
                                            <div class="content">sorry,没有找到相关的城市木屋数据~</div>
                                        </div>
                                    </div>
                                </div>
                             </div>

                                <div class="c_box">
                                    <div class="c_list c_list-line">
                                        <ul id="citycabininfo">

                                        </ul>
                                    </div>
                                </div>
                                <div class="c_space-3"></div>
                                <div id="submitCityCabinButton" class="c_submit c_submit-full">
                                    <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.csrTraceFlow.confirmCityCabin()">确认城市木屋</button>
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
<script>
   Wade.setRatio();
   $.csrTraceFlow.init();
</script>
</body>
</html>