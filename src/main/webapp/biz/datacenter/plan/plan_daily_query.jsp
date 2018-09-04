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
    <title>家装顾问计划录入情况</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/datacenter/plan/plan.daily.query.js?a=1"></script>
</head>
<body>
<jsp:include page="/header.jsp">
	<jsp:param value="家装顾问计划录入情况" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div id="unEntryPlanList">

	</div>

</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>

<script id="unEntryPlanListTemplate" type="text/html">
	{{each COMPANY_LIST company idx}}
	<div class="c_box c_box-border c_box-gray">
		<div class="c_title" ontap="$(this).next().toggle();">
			<div class="text">{{company.NAME}}</div>
			<div class="fn">
				<ul>
					<li><span class="e_ico-unfold"></span></li>
				</ul>
			</div>
		</div>
		<div class="l_padding l_padding-u" style="">
			<div class="c_list c_list-v c_list-col-3">
				<ul>
					{{each company.EMPLOYEE_LIST employee idx}}
					<li class="link" ontap="">
						<div class="main">
							<div class="title">{{employee.EMPLOYEE_NAME}}</div>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
		</div>
	</div>
	{{/each}}
</script>

<script type="text/javascript">
	Wade.setRatio();
    PlanDailyQuery.init();
</script>
</body>
</html>