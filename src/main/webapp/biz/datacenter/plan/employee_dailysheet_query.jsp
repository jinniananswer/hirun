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
	<script src="scripts/biz/datacenter/plan/employee.dailysheet.query.js"></script>
</head>
<body>
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span>家装顾问日报表</span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="家装顾问日报表" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_title">
		<div class="text"></div>
		<div class="fn">
			<ul>
				<li>
					<span class="e_mix">
						<input type="text" id="QUERY_DATE" name="QUERY_DATE" datatype="date" nullable="no" desc="日期" readonly="true"/>
						<span class="e_ico-date"></span>
					</span>
				</li>
			</ul>
		</div>
	</div>
	<div class="c_space"></div>
	<div id="dailysheetTable" class="c_table c_table-hasGrid c_table-lite c_table-border">
		<div class="body">
			<div class="wrapper">
				<table>
					<thead>
					<tr>
						<th col="EMPLOYEE_NAME">家装顾问</th>
						<th col="PLAN_JW">加微</th>
						<th col="PLAN_LTZDSTS">蓝图指导书</th>
						<th col="PLAN_GZHGZ">关注公众号</th>
						<th col="PLAN_HXJC">核心接触</th>
						<th col="PLAN_SMJRQLC">扫码</th>
						<th col="PLAN_XQLTYTS">需求蓝图一</th>
						<th col="PLAN_ZX">咨询</th>
						<th col="PLAN_YJALTS">一件案例</th>
						<th col="PLAN_DKCSMU">城市木屋</th>
						<th col="FINISH_JW">加微</th>
						<th col="FINISH_LTZDSTS">蓝图指导书</th>
						<th col="FINISH_GZHGZ">关注公众号</th>
						<th col="FINISH_HXJC">核心接触</th>
						<th col="FINISH_SMJRQLC">扫码</th>
						<th col="FINISH_XQLTYTS">需求蓝图一</th>
						<th col="FINISH_ZX">咨询</th>
						<th col="FINISH_YJALTS">一件案例</th>
						<th col="FINISH_DKCSMU">城市木屋</th>
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
	<div class="e_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script type="text/javascript">
	Wade.setRatio();
    EmployeeDailySheetQuery.init();
</script>
</body>
</html>