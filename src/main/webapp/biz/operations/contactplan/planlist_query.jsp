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
    <title>家装顾问日报表</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/contactplan/planlist.query.js"></script>
</head>
<body>
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span>家装顾问日报表</span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="家装顾问日报表" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div id="employee_list">

	</div>
	<div class="e_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script id="employee_template" rel_id="employee_list" type="text/html">
	{{each EMPLOYEE_LIST employee idx}}
	<div class="c_box c_box-border" tag="employee_box" employee_id="{{employee.EMPLOYEE_ID}}">
		<div class="c_title" employee_id="{{employee.EMPLOYEE_ID}}" ontap="planListQuery.clickEmployee(this)">
			<div class="text">
				<span tag="employee_name">{{employee.NAME}}</span>
				{{if employee.HINT }}
				<span tag="hint" class="e_red">({{employee.HINT}})</span>
				{{/if}}
			</div>
			<div class="fn">
				<ul>
					<li><span class="e_ico-unfold"></span></li>
					<li><span class="text" employee_id="{{employee.EMPLOYEE_ID}}" ontap="planListQuery.queryDetail(this)">查看详情</span></li>
				</ul>
			</div>
		</div>
		<div class="l_padding l_padding-u" tag="daily_sheet" is_query="false" style="display:none">
			<div name="daily_sheet_{{employee.EMPLOYEE_ID}}" id="daily_sheet_{{employee.EMPLOYEE_ID}}" style="height:50em;"></div>
		</div>
	</div>
	{{/each}}
</script>
<script type="text/javascript">
	Wade.setRatio();
    planListQuery.init();
</script>
</body>
</html>