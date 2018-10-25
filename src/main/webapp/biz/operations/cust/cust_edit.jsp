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
    <title>客户信息修改</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/cust/cust.edit.js?a=4"></script>
</head>
<body>
<jsp:include page="/header.jsp">
    <jsp:param value="客户信息修改" name="headerName"/>
</jsp:include>
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
<jsp:include page="/base/buttom/base_buttom.jsp"/>

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
                                <!--
                                <li>
                                    <div class="label">楼盘</div>
                                    <div class="value">
                                        <span class="e_mix" ontap="custDelete.selectHouses(this)">
                                            <input type="text" id="HOUSES_NAME" name="HOUSES_NAME" datatype="text"
                                                   houses_id=""
                                                   nullable="no" desc="楼盘" value="" readonly="true"/>
                                            <span class="e_ico-check"></span>
                                        </span>
                                    </div>
                                </li>
                                <li>
                                    <div class="label">家装顾问</div>
                                    <div class="value">
                                        <span class="e_mix" ontap="custDelete.selectCounselor(this)">
                                            <input type="text" id="EMPLOYEE_NAMES" name="EMPLOYEE_NAMES" datatype="text"
                                                   employee_ids=""
                                                   nullable="no" desc="家装顾问" value="" readonly="true"/>
                                            <span class="e_ico-check"></span>
                                        </span>
                                    </div>
                                </li>
                                -->
                            </ul>
                        </div>
                        <!-- 客户列表 结束 -->
                        <div class="c_space"></div>
                        <div class="c_submit c_submit-full">
                            <button type="button" class="e_button-l e_button-green" ontap="custEdit.queryCustList4Cond(this)">查询</button>
                        </div>
                    </div>
                </div>
                <div class="c_popupItem" id="custEditPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">客户信息修改</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header l_padding">
                        <div class="c_list c_list-form" id="custForm">
                            <ul>
                                <li class="required">
                                    <div class="label">客户姓名</div>
                                    <div class="value">
                                        <input type="text" id="CUST_NAME" name="CUST_NAME" nullable="no" desc="客户姓名"/>
                                        <input type="text" id="CUST_ID" name="CUST_ID" style="display: none"/>
                                    </div>
                                </li>
                                <li>
                                    <div class="label">微信昵称</div>
                                    <div class="value">
                                        <input type="text" id="WX_NICK" name="WX_NICK" desc="微信昵称" readonly="true"/>
                                    </div>
                                </li>
                                <li class="required">
                                    <div class="label">性别</div>
                                    <div class="value">
                                        <div class="e_switch">
                                            <div class="e_switchOn">男</div>
                                            <div class="e_switchOff">女</div>
                                            <input type="hidden" id="SEX" name="SEX" nullable="no" desc="性别"/>
                                        </div>
                                    </div>
                                </li>
                                <li class="required">
                                    <div class="label">电话号码</div>
                                    <div class="value">
                                        <input type="text" id="MOBILE_NO" name="MOBILE_NO" nullable="no" desc="电话号码"/>
                                    </div>
                                </li>
                                <li class="link required">
                                    <div class="label">楼盘</div>
                                    <div class="value">
                                        <%--<input type="text" id="HOUSE_DESC" name="HOUSE_DESC" desc="楼盘" readonly="true"/>--%>
                                        <span id="custEditForm_house_container"></span>
                                        <%--<span>--请选择--</span>--%>
                                        <%--<input type="hidden" id="HOUSE_ID" name="HOUSE_ID" value="" nullable="yes" desc="楼盘" />--%>
                                    </div>
                                </li>
                                <li class="link required" style="display: none" id="liScatter">
                                    <div class="label">散盘名称</div>
                                    <div class="value">
										<span class="e_mix" ontap="custEditPopup.onClickSelectScatterHousesButton(this)">
											<input type="text" id="SCATTER_HOUSE" name="SCATTER_HOUSE" datatype="text"
                                                   houses_id=""
                                                   nullable="yes" desc="散盘名称" value="" readonly="true"/>
											<span class="e_ico-check"></span>
										</span>
                                    </div>
                                </li>
                                <li class="required">
                                    <div class="label">楼栋号</div>
                                    <div class="value">
                                        <input type="text" id="HOUSE_DETAIL" name="HOUSE_DETAIL" nullable="no" desc="楼栋号"/>
                                    </div>
                                </li>
                                <li class="required">
                                    <div class="label">户型</div>
                                    <div class="value">
                                        <span id="custEditForm_houseMode_container"></span>
                                    </div>
                                </li>
                                <li class="required">
                                    <div class="label">面积</div>
                                    <div class="value">
										<span class="e_mix">
											<input type="text" id="HOUSE_AREA" name="HOUSE_AREA" nullable="no" desc="面积"/>
											<span class="e_label"><span>平方</span></span>
										</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="label">客户基本情况</div>
                                    <div class="value">
                                        <textarea name="CUST_DETAIL" class="e_textarea-row-3" ></textarea>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 客户列表 结束 -->
                        <div class="c_space"></div>
                        <div class="c_submit c_submit-full">
                            <button type="button" class="e_button-l e_button-green" ontap="custEditPopup.submitCustInfo(this)">提交</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="scatterHousesPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">请选择散盘</div>
                        <div class="right">
                            <div class="fn">
                                <button type="button" class="e_button-blue"
                                        ontap="scatterHousesPopup.onClickAddScatterHousesButton(this)">
                                    <span class="e_ico-add"></span>
                                    <span>新增散盘</span>
                                </button>

                            </div>

                        </div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <div class="c_list c_list-form">
                            <ul>
                                <li>
                                    <div class="value">
                                        <span class="e_mix">
                                            <input id="scatterHousesPopupItem_HOUSE_SEARCH_TEXT" name="scatterHousesPopupItem_HOUSE_SEARCH_TEXT" type="text" placeholder="散盘名称（模糊搜索）" nullable="no" desc="查询条件">
                                            <button type="button" class="e_button-blue" ontap="scatterHousesPopup.searchHouses($('#scatterHousesPopupItem_HOUSE_SEARCH_TEXT').val())"><span class="e_ico-search"></span><span>查询</span></button>
                                        </span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_SCATTER_HOUSES">

                            </ul>
                        </div>
                        <div class="c_line"></div>
                    </div>
                </div>
                <!--
                <div class="c_popupItem" id="counselorPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">请选择家装顾问</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_COUNSELORS">

                            </ul>
                        </div>
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" class="e_button-l e_button-red" ontap="counselorPopup.clear(this)">清空</button>
                            <button type="button" class="e_button-l e_button-green" ontap="counselorPopup.confirm(this)">确定</button>
                        </div>
                    </div>
                </div>
                <div class="c_popupItem" id="housesPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">请选择楼盘</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <div class="c_list c_list-form">
                            <ul>
                                <li>
                                    <div class="value">
                                        <span class="e_mix">
                                            <input id="HOUSE_SEARCH_TEXT" name="HOUSE_SEARCH_TEXT" type="text" placeholder="楼盘名称（模糊搜索）" nullable="no" desc="查询条件">
                                            <button type="button" class="e_button-blue" ontap="housesPopup.searchHouses($('#HOUSE_SEARCH_TEXT').val())"><span class="e_ico-search"></span><span>查询</span></button>
                                        </span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
                            <ul id="BIZ_HOUSES">

                            </ul>
                        </div>
                        <div class="c_line"></div>
                    </div>
                </div>
                -->
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="createScatterHousesPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">新增散盘</div>
                    </div>
                    <div class="c_list c_list-form" id="createScatterHousesForm">
                        <ul>
                            <li>
                                <div class="label">散盘名称</div>
                                <div class="value">
                                    <input type="text" id="createScatterHousesForm_SCATTER_HOUSES_NAME" name="createScatterHousesForm_SCATTER_HOUSES_NAME" desc="散盘名称" nullable="no"/>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="c_space"></div>
                    <div class="c_submit c_submit-full">
                        <button type="button" class="e_button-l e_button-green" ontap="createScatterHousesPopup.submitScatterHouses(this)">新增</button>
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
                    <div class="content">家装顾问：{{cust.HOUSE_COUNSELOR_NAME}}</div>
                </div>
                <!--
                <div class="link side" cust_id="{{cust.CUST_ID}}" cust_name="{{cust.CUST_NAME}}"
                     ontap="custDelete.showCustDetail(this)">
                    查看详情
                </div>
                -->

                <div class="fn" cust_id="{{cust.CUST_ID}}" ontap="custEdit.clickCustEditButton(this)">
                    <span class="e_ico-edit"></span>
                </div>
            </li>
            {{/each}}
        </ul>
    </div>
</script>
<script id="scatter_houses_template" rel_id="SCATTER_HOUSES" type="text/html">
    {{each HOUSES_LIST houses idx}}
    <li class="link e_center" houses_name="{{houses.NAME}}" houses_id="{{houses.HOUSES_ID}}"
        ontap="scatterHousesPopup.clickHouses(this)">
        <label class="group">
            <div class="main">{{houses.NAME}}</div>
        </label>
    </li>
    {{/each}}
</script>

<!--
<script id="employee_template" rel_id="BIZ_COUNSELORS" type="text/html">
    {{each EMPLOYEE_LIST employee idx}}
    <li class="link e_center" employee_name="{{employee.NAME}}" employee_id="{{employee.EMPLOYEE_ID}}"
        ontap="counselorPopup.clickEmployee(this)" tag="li_employee">
        <label class="group" id="LABEL_{{employee.EMPLOYEE_ID}}">
            <div class="main">{{employee.NAME}}</div>
        </label>
    </li>
    {{/each}}
</script>
<script id="houses_template" rel_id="BIZ_HOUSES" type="text/html">
    {{each HOUSES_LIST houses idx}}
    <li class="link e_center" houses_name="{{houses.NAME}}" houses_id="{{houses.HOUSES_ID}}"
        ontap="housesPopup.clickHouses(this)">
        <label class="group" id="LABEL_HOUSES_{{houses.HOUSES_ID}}">
            <div class="main">{{houses.NAME}}</div>
        </label>
    </li>
    {{/each}}
</script>
-->

<script type="text/javascript">
    Wade.setRatio();
    custEdit.init();
</script>
</body>
</html>