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
    <title>家装顾问日报表详情</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/contactplan/employee.dailysheet.detail.query.js"></script>
</head>
<body>
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span>家装顾问日报表</span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="家装顾问日报表详情" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div id="dailysheet_detail">

	</div>
	<div class="e_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script id="dailysheet_detail_template" type="text/html">
	{{each DATA_LIST action idx}}
	<div class="c_box c_box-border">
		<div class="c_title" ontap="$(this).next().toggle();">
			<div class="text e_strong e_blue">{{action.ACTION_NAME}}</div>
			<div class="fn">
				<ul>
					<li><span class="e_ico-unfold"></span></li>
				</ul>
			</div>
		</div>
		<div class="l_padding l_padding-u" style="display: none">
			<div class="c_list">
				<ul>
					<li class="link" ontap="">
						<div class="content">
							<div class="main">
								<div class="title e_strong">昨日计划客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul>
					{{each action.YESTERDAY_PLAN_CUST_LIST cust idx}}
					<li class="link" ontap="">
						<div class="main">
							<div class="title">{{cust.CUST_NAME}}</div>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
			<div class="c_line c_line-dashed"></div>
			<div class="c_list">
				<ul>
					<li class="link">
						<div class="content">
							<div class="main">
								<div class="title e_strong">昨日完成客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul tag="FINISH_CUST_LIST">
					{{each action.YESTERDAY_PLAN_FINISH_CUST_LIST cust idx}}
					<li>
						<div class="main">
							<div class="title">{{cust.CUST_NAME}}</div>
						</div>
						<div class="content">
							<span>{{cust.FINISH_TIME}}</span>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
			<div class="c_line c_line-dashed"></div>
			<div class="c_list">
				<ul>
					<li class="" ontap="">
						<div class="content">
							<div class="main">
								<div class="title e_strong">昨日未完成客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul tag="UNFINISH_CUST_LIST">
					{{each action.YESTERDAY_PLAN_UNFINISH_CUST_LIST cust idx}}
					<li>
						<div class="main">
							<div class="title">
								<span class="e_red">{{cust.CUST_NAME}}</span>
								<%--<span class="e_tag e_tag-s e_tag-red" tag="unfinish_cause_desc">{{cust.UNFINISH_CAUSE_DESC}}</span>--%>
							</div>
							<div class="content">
								<span class="e_tag e_tag-s e_tag-red" tag="unfinish_cause_desc">{{cust.UNFINISH_CAUSE_DESC}}</span>
							</div>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
			<div class="c_line c_line-dashed"></div>
			<div class="c_list">
				<ul>
					<li class="" ontap="">
						<div class="content">
							<div class="main">
								<div class="title e_strong">今日计划客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul tag="UNFINISH_CUST_LIST">
					{{each action.PLAN_CUST_LIST cust idx}}
					<li>
						<div class="main">
							<div class="title">
								<span>{{cust.CUST_NAME}}</span>
							</div>
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
    EmployeeDailySheetDetailQuery.init();
</script>
</body>
</html>