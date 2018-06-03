<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;
%>
<html size="s">
<head>
    <base href="<%=basePath%>"></base>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/cust/custlist.query.js"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="back();"><span>客户查询</span></div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
    <div class="c_space"></div>
    <div class="l_queryFn">
        <div class="c_fn">
            <div class="right">
                <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','CustQueryCondPopupItem');">
                    <span class="e_ico-search"></span>
                </button>
            </div>
        </div>
    </div>
    <div id="cust_list">

    </div>
    <div class="e_space"></div>
</div>
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="CustQueryCondPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">客户查询条件</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header l_padding">
                        <div class="c_list c_list_form c_list-line" id="queryCustParamForm">
                            <ul>
                                <li>
                                    <div class="label">客户姓名</div>
                                    <div class="value">
                                        <input type="text" name="CUST_NAME"/>
                                    </div>
                                </li>
                                <li>
                                    <div class="label">联系电话</div>
                                    <div class="value">
                                        <input type="text" name="MOBILE_NO"/>
                                    </div>
                                </li>
                                <%--<li>--%>
                                    <%--<div class="label">微信昵称</div>--%>
                                    <%--<div class="value">--%>
                                        <%--<input type="text" name="WX_NICK"/>--%>
                                    <%--</div>--%>
                                <%--</li>--%>
                                <li>
                                    <div class="label">楼盘</div>
                                    <div class="value">
                                        <span id="queryCustParamForm_house_container"></span>
                                        <%--<span>--请选择--</span>--%>
                                        <%--<input type="hidden" id="mySelect" name="HOUSE_ID" value="" nullable="yes" desc="选择项目" />--%>
                                        </span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 客户列表 结束 -->
                        <div class="c_space"></div>
                        <div class="c_submit c_submit-full">
                            <button type="button" class="e_button-l e_button-green" ontap="custListQuery.queryCustList4Cond(this)">查询</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script id="cust_template" rel_id="cust_list" type="text/html">
    <div class="c_list c_list-line">
        <ul>
            {{each CUSTOMERLIST cust idx}}
            <li id="CUST_ID_{{cust.CUST_ID}}">
                <div class="main">
                    <div class="title">{{cust.CUST_NAME}}</div>
                </div>
                <div class="side" cust_id="{{cust.CUST_ID}}" cust_name="{{cust.CUST_NAME}}"
                     ontap="custListQuery.showCustDetail(this)">
                    查看详情
                </div>
                <div class="more"></div>
                <div class="fn" cust_id="{{cust.CUST_ID}}" ontap="custListQuery.deleteCust($(this).attr('cust_id'))">
                    <span class="e_ico-delete"></span>
                </div>
            </li>
            {{/each}}
        </ul>
    </div>
</script>
<script type="text/javascript">
    Wade.setRatio();
    custListQuery.init();
</script>
</body>
</html>