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
    <title>客户明细</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/cust/custdetail.query.js"></script>
</head>
<body>
<%--<div class="c_header">--%>
    <%--<div class="back" ontap="back();"><span>客户明细</span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
    <jsp:param value="客户明细" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
    <div class="c_title">
        <div class="text">基本信息</div>
    </div>
    <div class="c_param c_param-label-8" >
        <ul id="cust_detail">
            <li>
                <span class="label">姓名：</span>
                <span class="value" tag="cust_name"></span>
            </li>
            <li>
                <span class="label">性别：</span>
                <span class="value" tag="sex"></span>
            </li>
            <li>
                <span class="label">微信昵称：</span>
                <span class="value" tag="wx_nick"></span>
            </li>
            <li>
                <span class="label">电话号码：</span>
                <span class="value" tag="mobile_no"></span>
            </li>
            <li>
                <span class="label">楼盘：</span>
                <span class="value" tag="house"></span>
            </li>
            <li>
                <span class="label">楼栋号：</span>
                <span class="value" tag="house_detail"></span>
            </li>
            <li>
                <span class="label">户型：</span>
                <span class="value" tag="house_mode"></span>
            </li>
            <li>
                <span class="label">面积：</span>
                <span class="value" tag="house_area"></span>
            </li>
            <li>
                <span class="label">归属家装顾问：</span>
                <span class="value" tag="employee_name"></span>
            </li>
            <li>
                <span class="label">客户基本情况：</span>
                <span class="value" tag="cust_detail"></span>
            </li>
        </ul>
    </div>
    <div class="c_title">
        <div class="text">客户完成动作</div>
    </div>
    <div id="custActionTable" class="c_table c_table-hasGrid c_table-lite c_table-border">
        <div class="body">
            <div class="wrapper">
                <table>
                    <thead>
                    <tr>
                        <th col="ACTION_NAME">接触动作</th>
                        <th col="FINISH_TIME">完成时间</th>
                        <th col="EMPLOYEE_NAME">家装顾问</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
        <div class="top">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="left" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="right" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="leftTop" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="rightTop" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
    </div>
    <div class="c_title">
        <div class="text">跟踪记录</div>
    </div>
    <div class="c_list c_list-line">
        <ul id="custContactList">

        </ul>
    </div>
    <div class="c_title">
        <div class="text">未完成原因</div>
    </div>
    <div id="custUnFinishCauseTable" class="c_table c_table-hasGrid c_table-lite c_table-border">
        <div class="body">
            <div class="wrapper">
                <table>
                    <thead>
                    <tr>
                        <th col="ACTION_NAME">接触动作</th>
                        <th col="UNFINISH_CAUSE_DESC" class="e_red">未完成原因</th>
                        <th col="EMPLOYEE_NAME">家装顾问</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
        <div class="top">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="left" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="right" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="leftTop" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
        <div class="rightTop" style="display:none">
            <table><thead></thead><tbody></tbody></table>
        </div>
    </div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>

<script id="cust_action_list_template" rel_id="" type="text/html">
    {{each CUST_FINISH_ACTION_LIST action idx}}
    <li>
        <span class="label">{{action.ACTION_NAME}}：</span>
        <span class="value">{{action.FINISH_TIME}} {{action.EMPLOYEE_NAME}}</span>
    </li>
    {{/each}}
</script>
<script id="cust_contact_list_template" rel_id="" type="text/html">
    {{each CUST_CONTACT_LIST custContact idx}}
    <li>
        <div class="content">
            <div class="main" style="font-size:100%">
                <div class="title title-auto">{{custContact.CONTACT_NOTE}}</div>
            </div>
        </div>
        <div class="sub sub-noline">
            <div class="main">

            </div>
            <div class="side">{{custContact.EMPLOYEE_NAME}} {{custContact.CONTACT_DATE}}</div>
        </div>
    </li>
    {{/each}}
</script>
<script type="text/javascript">
    Wade.setRatio();
    custDetailQuery.init();
</script>
</body>
</html>