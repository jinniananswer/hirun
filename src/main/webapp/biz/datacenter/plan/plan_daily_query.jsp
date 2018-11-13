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
	<script src="scripts/biz/datacenter/plan/plan.daily.query.js?a=2"></script>
</head>
<body>
<jsp:include page="/header.jsp">
	<jsp:param value="家装顾问计划录入情况" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div class="c_tab c_tab-level-2" id="myTab">
		<div class="tab">
			<div class="list">
				<ul></ul>
			</div>
		</div>
		<div class="page">
			<div class="content" title="没有录计划">
				<div id="unEntryPlanList">

				</div>
			</div>
			<div class="content" title="参加活动">
				<div id="activitiPlanList">

				</div>
			</div>
			<div class="content" title="休假">
				<div id="holidayPlanList">

				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>

<script id="noResultTemplate" type="text/html">
	<div class="l_queryMsg" id="UI-queryMsg">
		<div class="c_msg">
			<div class="wrapper">
				<div class="emote"></div>
				<div class="info">
					<div class="text">
						<div class="title">暂时没有数据</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</script>
<script id="planListTemplate" type="text/html">
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