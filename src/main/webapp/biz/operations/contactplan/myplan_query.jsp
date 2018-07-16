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
    <title>我的计划</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/contactplan/myplan.query.js"></script>
</head>
<body>
<jsp:include page="/header.jsp">
	<jsp:param value="我的计划" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<%--<div class="c_tip c_tip-red">提示文字</div>--%>
	<div class="c_title">
		<div class="text"></div>
		<div class="fn">
			<ul>
				<li>
					<span class="e_mix">
						<input type="text" id="PLAN_DATE" name="PLAN_DATE" datatype="date" nullable="no" desc="日期" readonly="true"/>
						<span class="e_ico-date"></span>
					</span>
				</li>
			</ul>
		</div>
	</div>
	<div class="c_space"></div>
	<div class="c_title" ontap="$(this).next().toggle();">
		<div class="text">计划基本信息</div>
		<div class="fn">
			<ul><li><span class="e_ico-hide"></span></li></ul>
		</div>
	</div>
	<div id="" class="c_list">
		<ul id="plan_base">

		</ul>
	</div>
	<div class="c_line"></div>
	<div class="c_title" ontap="$(this).next().toggle();">
		<div class="text">计划完成明细</div>
		<div class="fn">
			<ul><li><span class="e_ico-hide"></span></li></ul>
		</div>
	</div>
	<div id="plan_datail">

	</div>
	<div class="e_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script id="finishInfoTemplate" type="text/html">
	{{each CUST_ACTION_LIST custAction idx}}
	<div class="c_box c_box-border c_box-gray">
		<div class="c_title" ontap="$(this).next().toggle();">
			<div class="text">{{custAction.ACTION_NAME}}</div>
			<div class="fn">
				<ul>
					<%--<li><span>计划数:{{PLAN_CUSTNUM}}人 / 实际数:<span tag="finishCustNum">{{FINISH_CUSTNUM}}</span></span><span class="e_ico-unfold"></span></li>--%>
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
								<div class="title e_strong">计划客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul>
					{{each custAction.PLAN_CUST_LIST cust idx}}
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
								<div class="title e_strong">实际完成客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul>
					{{each custAction.PLAN_FINISH_CUST_LIST cust idx}}
					<li>
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
					<li class="" ontap="">
						<div class="content">
							<div class="main">
								<div class="title e_strong">未完成客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul tag="UNFINISH_CUST_LIST">
					{{each custAction.PLAN_UNFINISH_CUST_LIST cust idx}}
					<li>
						<div class="main">
							<div class="title">
								<span class="e_red">{{cust.CUST_NAME}}</span>
							</div>
							<div class="content">
								<span class="e_tag e_tag-s e_tag-red" tag="unfinish_cause_desc">{{cust.UNFINISH_CAUSE_DESC}}</span>
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
<script id="planBaseTemplate" type="text/html">
	<li>
		<div class="main">
			<%--<div class="title">{{PLAN_DATE}}</div>--%>
			<div class="content content-auto">
				<div class="c_param">
					<ul>
						<li>
							<span class="label">计划状态：</span>
							<span class="value">{{PLAN_STATUS_DESC}}</span>
						</li>
						<li>
							<span class="label">计划类型：</span>
							<span class="value">{{PLAN_TYPE_DESC}}</span>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</li>
</script>
<script type="text/javascript">
	Wade.setRatio();
    myPlanQuery.init();
</script>
</body>
</html>