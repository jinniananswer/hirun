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
    <title>家装顾问月报表</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/datacenter/plan/employee.monstat.query.js"></script>
</head>
<body>
<div class="c_header">
	<div class="back" ontap="back();"><span>家装顾问月报表</span></div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
	<div class="c_space"></div>
	<div id="employee_list">

	</div>
	<div class="e_space"></div>
</div>
<script id="employee_template" rel_id="employee_list" type="text/html">
	{{each EMPLOYEE_LIST employee idx}}
	<div class="c_box c_box-border" tag="employee_box" employee_id="{{employee.EMPLOYEE_ID}}">
		<div class="c_title" employee_id="{{employee.EMPLOYEE_ID}}" ontap="EmployeeMonStatQuery.clickEmployee(this)">
			<div class="text">
				<span tag="employee_name">{{employee.EMPLOYEE_NAME}}</span>
			</div>
			<div class="fn">
				<ul>
					<li><span class="e_ico-unfold"></span></li>
				</ul>
			</div>
		</div>
		<div class="l_padding l_padding-u" tag="mon_stat" is_query="false" style="display:none">
			<div class="c_list c_list-form">
				<ul>
					<li class="required">
						<div class="label">月份：</div>
						<div class="value">
							<span class="e_mix">
								<input type="text" id="PLAN_DATE" name="PLAN_DATE" datatype="date"
									   employee_id="{{employee.EMPLOYEE_ID}}" nullable="no" desc="补录计划日期" />
								<span class="e_ico-date"></span>
							</span>
						</div>
					</li>
				</ul>
			</div>
			<div name="stat_mon_{{employee.EMPLOYEE_ID}}" id="stat_mon_{{employee.EMPLOYEE_ID}}" style="height:50em;"></div>
		</div>
	</div>
	{{/each}}
</script>
<script type="text/javascript">
    Wade.setRatio();
    EmployeeMonStatQuery.init();
</script>
</body>
</html>