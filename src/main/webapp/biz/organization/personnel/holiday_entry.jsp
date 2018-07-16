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
    <title>休假填写</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/organization/personnel/holiday.entry.js"></script>
</head>
<body>
<jsp:include page="/header.jsp">
	<jsp:param value="休假填写" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div id="holidayForm" class="c_list c_list-form">
		<ul>
			<li class="link required">
				<div class="label">休假开始时间</div>
				<div class="value">
					<span class="e_mix">
						<input type="text" id="HOLIDAY_START_DATE" name="HOLIDAY_START_DATE" datatype="date"
							   nullable="no" desc="休假开始时间" readonly="true"/>
						<span class="e_ico-date"></span>
					</span>
				</div>
			</li>
			<li class="link required">
				<div class="label">休假结束时间</div>
				<div class="value">
					<span class="e_mix">
						<input type="text" id="HOLIDAY_END_DATE" name="HOLIDAY_END_DATE" datatype="date"
							   nullable="no" desc="休假结束时间" readonly="true"/>
						<span class="e_ico-date"></span>
					</span>
				</div>
			</li>
		</ul>
	</div>
	<div class="e_space"></div>
	<div class="c_submit c_submit-full">
		<button type="button" class="e_button-r e_button-l e_button-green" ontap="holidayEntry.okOnclick()">确定</button>
	</div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script type="text/javascript">
	Wade.setRatio();
    holidayEntry.init();
</script>
</body>
</html>