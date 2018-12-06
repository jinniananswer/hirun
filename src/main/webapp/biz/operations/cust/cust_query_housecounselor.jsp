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
	<title>客户查询（家装顾问）</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/cust/cust.query.housecounselor.js?a=3"></script>
</head>
<body>
<jsp:include page="/header.jsp">
	<jsp:param value="客户查询（家装顾问）" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_tip c_tip-red">一次查询最多展示300条客户信息，过多界面会变慢</div>
	<div class="c_form">
		<ul>
			<li>
				<div class="value">
					<div class="e_mix">
						<input type="text" id="QUERY_COND_TEXT" disabled="true"/>
						<button class="e_button-blue" type="button" ontap="custQuery4HouseCounselor.clickQueryButton();">
							<span class="e_ico-search"></span>
						</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
	<div id="custTable" class="c_table c_table-hasGrid c_table-border c_table-lite c_table-row-10" style="height: 32em;">
		<div class="body">
			<div class="wrapper">
				<table>
					<thead>
					<tr>
						<th col="CUST_NAME">客户姓名</th>
						<th col="JW_NUM" ontap="$.sortTable(this, 'int')">加微完成次数</th>
						<th col="JW_LAST_TIME" ontap="$.sortTable(this, 'date')">加微最后完成时间</th>
						<th col="LTZDSTS_NUM" ontap="$.sortTable(this, 'int')">蓝图指导书完成次数</th>
						<th col="LTZDSTS_LAST_TIME" ontap="$.sortTable(this, 'date')">蓝图指导书最后完成时间</th>
						<th col="GZHGZ_LAST_TIME" ontap="$.sortTable(this, 'date')">关注公众号完成时间</th>
						<th col="HXJC_LAST_TIME" ontap="$.sortTable(this, 'date')">核心接触完成时间</th>
						<th col="SMJRQLC_NUM" ontap="$.sortTable(this, 'int')">扫码完成次数</th>
						<th col="SMJRQLC_LAST_TIME" ontap="$.sortTable(this, 'date')">扫码完成时间</th>
						<th col="XQLTYTS_NUM" ontap="$.sortTable(this, 'int')">需求蓝图一完成次数</th>
						<th col="XQLTYTS_LAST_TIME" ontap="$.sortTable(this, 'date')">需求蓝图一最后完成时间</th>
						<th col="ZX_LAST_TIME" ontap="$.sortTable(this, 'date')">咨询完成时间</th>
						<th col="YJALTS_NUM" ontap="$.sortTable(this, 'int')">一键案例完成次数</th>
						<th col="YJALTS_LAST_TIME" ontap="$.sortTable(this, 'date')">一键案例最后完成时间</th>
						<th col="DKCSMU_LAST_TIME" ontap="$.sortTable(this, 'date')">城市木屋完成时间</th>
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

<div class="c_popup" id="UI-popup">
	<div class="c_popupBg" id="UI-popup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="UI-popup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="QueryCondPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">查询条件</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<div class="c_list c_list_form c_list-line" id="QueryCondForm">
							<ul>
								<li>
									<div class="label">客户姓名</div>
									<div class="value">
										<input type="text" id="COND_CUST_NAME" name="COND_CUST_NAME" desc="客户姓名"/>
									</div>
								</li>
								<li>
									<div class="label">家装顾问</div>
									<div class="value">
                                        <span class="e_mix" ontap="custQuery4HouseCounselor.selectCounselor(this)">
                                            <input type="text" id="EMPLOYEE_NAMES" name="EMPLOYEE_NAMES" datatype="text"
												   employee_ids=""
												   nullable="no" desc="家装顾问" value="" readonly="true"/>
                                            <span class="e_ico-check"></span>
                                        </span>
									</div>
								</li>
								<li class="">
									<div class="label">完成动作</div>
									<div class="value">
										<span class="e_mix" ontap="custQuery4HouseCounselor.initActionPopup(this)">
											<input type="text" id="ACTION_NAME" name="ACTION_NAME" datatype="text"
												   action_code=""
												   desc="完成动作" value="" readonly="true"/>
											<span class="e_ico-check"></span>
										</span>
									</div>
								</li>
								<li>
									<div class="label">开始时间</div>
									<div class="value">
										<span class="e_mix">
											<input type="text" id="COND_START_DATE" name="COND_START_DATE" datatype="date" desc="开始日期" readonly="true"/>
											<span class="e_ico-date"></span>
										</span>
									</div>
								</li>
								<li>
									<div class="label">结束时间</div>
									<div class="value">
										<span class="e_mix">
											<input type="text" id="COND_END_DATE" name="COND_END_DATE" datatype="date" desc="结束日期" readonly="true"/>
											<span class="e_ico-date"></span>
										</span>
									</div>
								</li>
							</ul>
						</div>
						<!-- 客户列表 结束 -->
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="QueryCondPopup.confirm(this)">查询</button>
						</div>
					</div>
				</div>
			</div>
			<div class="c_popupGroup">
				<div class="c_popupItem" id="counselorPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">请选择家装顾问</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
						<div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
							<ul id="BIZ_COUNSELORS">

							</ul>
						</div>
						<div class="c_line"></div>
					</div>
					<div class="l_bottom">
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-red" ontap="counselorPopup.clear(this)">清空</button>
							<button type="button" class="e_button-l e_button-green" ontap="counselorPopup.confirm(this)">确定</button>
						</div>
					</div>
				</div>
				<div class="c_popupItem" id="actionPopupItem">
					<div class="c_header">
						<div class="back" ontap="hidePopup(this)">请选择完成动作</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
						<!-- 列表 开始 -->
						<div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
							<ul id="actionPopupItem_ACTIONLIST">

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
		ontap="counselorPopup.clickEmployee(this)" tag="li_employee">
		<label class="group" id="LABEL_{{employee.EMPLOYEE_ID}}">
			<div class="main">{{employee.NAME}}</div>
		</label>
	</li>
	{{/each}}
</script>
<script id="action_template" rel_id="actionPopupItem_ACTIONLIST" type="text/html">
	{{each ACTION_LIST action idx}}
	<li class="link e_center" action_name="{{action.ACTION_NAME}}" action_code="{{action.ACTION_CODE}}"
		ontap="custQuery4HouseCounselor.afterSelectAction(this)">
		<label class="group">
			<div class="main">{{action.ACTION_NAME}}</div>
		</label>
	</li>
	{{/each}}
</script>

<script type="text/javascript">
    Wade.setRatio();
    custQuery4HouseCounselor.init();
</script>
</body>
</html>