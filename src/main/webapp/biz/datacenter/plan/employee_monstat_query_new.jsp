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
	<script src="scripts/biz/datacenter/plan/employee.monstat.query.new.js?a=12"></script>
</head>
<body>
<jsp:include page="/header.jsp">
	<jsp:param value="家装顾问月报表" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div class="l_padding l_padding-u">
		<div class="c_form">
			<ul>
				<li>
					<div class="value">
						<div class="e_mix">
							<input type="text" id="QUERY_COND_TEXT" disabled="true"/>
							<button class="e_button-blue" type="button" ontap="EmployeeMonStatQueryNew.clickQueryButton();">
								<span class="e_ico-search"></span>
							</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<div class="c_space"></div>
	<div id="dailysheetTable" class="c_table c_table-hasGrid c_table-border c_table-lite c_table-row-10" style="height: 34em;">
		<div class="body">
			<div class="wrapper">
				<table>
					<thead>
					<tr>
						<th col="EMPLOYEE_NAME">家装顾问</th>
						<th col="PLAN_JW">加微计划数</th>
						<th col="PLAN_LTZDSTS">蓝图指导书计划数</th>
						<th col="PLAN_GZHGZ">关注公众号计划数</th>
						<th col="PLAN_HXJC">核心接触计划数</th>
						<th col="PLAN_SMJRQLC">扫码计划数</th>
						<th col="PLAN_XQLTYTS">需求蓝图一计划数</th>
						<th col="PLAN_ZX">咨询计划数</th>
						<th col="PLAN_YJALTS">一键案例计划数</th>
						<th col="PLAN_DKCSMU">城市木屋计划数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_JW">加微完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_LTZDSTS">蓝图指导书完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_GZHGZ">关注公众号完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_HXJC">核心接触完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_SMJRQLC">扫码完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_XQLTYTS">需求蓝图一完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_ZX">咨询完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_YJALTS">一键案例完成数</th>
						<th ontap="$.sortTable(this, 'int')" class="e_red" col="FINISH_DKCSMU">城市木屋完成数</th>
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
								<li class="required">
									<div class="label">查询月份：</div>
									<div class="value">
										<span class="e_mix">
											<input type="text" id="COND_MON_DATE" name="COND_MON_DATE" datatype="date"
												   nullable="no" desc="月份" readonly="true"/>
											<span class="e_ico-date"></span>
										</span>
									</div>
								</li>
								<li>
									<div class="label">家装顾问姓名</div>
									<div class="value">
										<input type="text" id="COND_HOUSE_COUNSELOR_NAME" name="COND_HOUSE_COUNSELOR_NAME" nullable="no" desc="家装顾问姓名"/>
									</div>
								</li>
								<li class="link" ontap="$('#ENTERPRISE_TEXT').focus();$('#ENTERPRISE_TEXT').blur();forwardPopup(this,'UI-ENTERPRISE')">
									<div class="label">分公司</div>
									<div class="value">
										<input type="text" id="ENTERPRISE_TEXT" name="ENTERPRISE_TEXT" nullable="yes" desc="" />
										<input type="hidden" name="ENTERPRISE" id="ENTERPRISE" nullable="yes" desc="分公司" />
									</div>
									<div class="more"></div>
								</li>
								<li class="link" ontap="$('#SHOP_TEXT').focus();$('#SHOP_TEXT').blur();forwardPopup('UI-popup','UI-SHOP')">
									<div class="label">门店</div>
									<div class="value">
										<input type="text" id="SHOP_TEXT" name="SHOP_TEXT" nullable="yes" desc="门店" />
										<input type="hidden" id="SHOP" name="SHOP" nullable="yes" desc="门店" />
									</div>
									<div class="more"></div>
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
				<div class="c_popupItem" id="UI-ENTERPRISE">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">请选择分公司</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
						<!-- 列表 开始 -->
						<div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
							<ul id="BIZ_ENTERPRISE">

							</ul>
						</div>
						<!-- 列表 结束 -->
						<div class="c_line"></div>
					</div>
				</div>
				<div class="c_popupItem" id="UI-SHOP">
					<!-- 标题栏 开始 -->
					<div class="c_header">
						<div class="back" ontap="backPopup(this);">请选择门店</div>
					</div>
					<!-- 标题栏 结束 -->
					<!-- 滚动（替换为 java 组件） 开始 -->
					<div class="c_scroll c_scroll-float c_scroll-header">
						<!-- 列表 开始 -->
						<div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
							<ul id="BIZ_SHOP">

							</ul>
						</div>
						<!-- 列表 结束 -->
					</div>
					<!-- 滚动 结束 -->
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
    Wade.setRatio();
    EmployeeMonStatQueryNew.init();
</script>
</body>
</html>