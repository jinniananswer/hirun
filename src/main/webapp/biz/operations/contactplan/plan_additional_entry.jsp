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
	<script src="scripts/biz/contactplan/plan.additional.entry.js?a=1"></script>
</head>
<body>
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span>计划补录</span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="补录" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div id="pre_info_form" class="c_list c_list-form">
		<ul>
			<li class="link required">
				<div class="label">家装顾问</div>
				<div class="value">
					<span class="e_mix" ontap="planAdditionalEntry.initCounselorPopup()">
						<input type="text" id="EMPLOYEE_NAME" name="EMPLOYEE_NAME" datatype="text"
							   employee_id=""
							   nullable="no" desc="家装顾问" value="" readonly="true"/>
						<span class="e_ico-check"></span>
					</span>
				</div>
			</li>
			<li class="link required">
				<div class="label">补录计划日期</div>
				<div class="value">
					<span class="e_mix">
						<input type="text" id="PLAN_DATE" name="PLAN_DATE" datatype="date" nullable="no" desc="补录计划日期" readonly="true"/>
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
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<div class="c_popup" id="UI-popup">
	<div class="c_popupBg" id="UI-popup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="UI-popup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="UI-popup-query">
					<div class="c_header">
						<div class="back" ontap="hidePopup(this)">请选择家装顾问</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
						<!-- 列表 开始 -->
						<div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
							<ul id="BIZ_COUNSELORS">

							</ul>
						</div>
						<!-- 列表 结束 -->
						<div class="c_line"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script id="employee_template" rel_id="BIZ_COUNSELORS" type="text/html">
	{{each EMPLOYEE_LIST employee idx}}
	<li class="link e_center" employee_name="{{employee.NAME}}" employee_id="{{employee.EMPLOYEE_ID}}"
		ontap="planAdditionalEntry.afterSelectEmployee(this)">
		<label class="group">
			<div class="main">{{employee.NAME}}</div>
		</label>
	</li>
	{{/each}}
</script>


<script type="text/javascript">
	Wade.setRatio();
    planAdditionalEntry.init();
</script>
</body>
</html>