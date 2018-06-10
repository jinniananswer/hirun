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
    <title>计划补录</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/contactplan/plan.additional.entry.js"></script>
</head>
<body>
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span>计划补录</span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="计划补录" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
	<div class="c_space"></div>
	<div id="pre_info_form" class="c_list c_list-form">
		<ul>
			<li class="link required">
				<div class="label">家装顾问</div>
				<div class="value">
					<span class="e_mix" ontap="">
						<input type="text" id="EMPLOYEE_ID" name="EMPLOYEE_ID" datatype="text" nullable="no" desc="家装顾问" value="" />
						<%--<input type="hidden" id="EMPLOYEE_ID" name="EMPLOYEE_ID" datatype="text" nullable="yes" desc="家装顾问" value="" />--%>
						<span class="e_ico-check"></span>
					</span>
				</div>
			</li>
			<li class="link required">
				<div class="label">补录计划日期</div>
				<div class="value">
					<span class="e_mix">
						<input type="text" id="PLAN_DATE" name="PLAN_DATE" datatype="date" nullable="no" desc="补录计划日期" />
						<span class="e_ico-date"></span>
					</span>
				</div>
			</li>
		</ul>
	</div>
	<div class="e_space"></div>
	<div class="c_submit c_submit-full">
		<button type="button" class="e_button-r e_button-l e_button-green" ontap="planAdditionalEntry.okOnclick()">确定</button>
	</div>
</div>

<script type="text/javascript">
	Wade.setRatio();
    planAdditionalEntry.init();
</script>
</body>
</html>