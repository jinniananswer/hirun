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
    <title>员工月度计划设定</title>
	<jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/contactplan/employee.month.target.entry.js?a=1"></script>
</head>
<body>
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span id="planName"></span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="员工月度计划目标设定" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
	<div class="c_space"></div>
	<div class="c_list c_list-form">
		<ul>
			<li class="required">
				<div class="label">月份：</div>
				<div class="value">
					<span class="e_mix">
						<input type="text" id="MON_DATE" name="MON_DATE" datatype="date"
							   nullable="no" desc="月份" readonly="true"/>
						<span class="e_ico-date"></span>
					</span>
				</div>
			</li>
		</ul>
	</div>
	<div class="c_space"></div>
	<div class="l_queryFn">
		<div class="c_fn">
			<div class="left">
				<span class="text" oper_type="single" ontap="employeeMonthTargetEntry.changeOperType()">批量操作</span>
				<span class="text" oper_type="batch" style="display: none" ontap="checkedAll('employeeBox', true)">全选</span>
				<span class="text" oper_type="batch" style="display: none" ontap="checkedOther('employeeBox')">反选</span>
			</div>
			<div class="right">
				<button class="e_button-blue" oper_type=""
						type="button" ontap="" style="display: none">
					<span class="e_ico-search"></span>
				</button>
				<button class="e_button-blue" oper_type="batch" style="display: none"
						type="button" ontap="employeeMonthTargetEntry.batchSetTarget()">
					<span>设定</span>
				</button>
				<button class="e_button" oper_type="batch" style="display: none"
						type="button" ontap="employeeMonthTargetEntry.cancelBatch()">
					<span>取消</span>
				</button>
			</div>
		</div>
	</div>
	<div class="c_space"></div>
	<div id="employee_list">

	</div>
	<div class="e_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>

<!-- popup start-->
<div class="c_popup" id="UI-popup">
	<div class="c_popupBg" id="UI-popup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="UI-popup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="targetSettingPopup">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">目标设置</div>
					</div>
					<div class="l_padding">
						<div class="c_list c_form-label-7" id="targetForm">
							<ul>
								<li>
									<div class="label">加微</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="JW" name="JW" maxlength="100" value="1" desc="咨询数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">蓝图指导书推送</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="LTZDSTS" name="LTZDSTS" maxlength="100" value="1" desc="核心接触数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">公众号关注</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="GZHGZ" name="GZHGZ" maxlength="10" value="1" desc="扫码数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">核心接触</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="HXJC" name="HXJC" maxlength="10" value="1" desc="扫码数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">扫码进入全流程</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="SMJRQLC" name="SMJRQLC" maxlength="10" value="1" desc="扫码数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">需求蓝图一推送</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="XQLTYTS" name="XQLTYTS" maxlength="10" value="1" desc="扫码数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">咨询</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="ZX" name="ZX" maxlength="10" value="1" desc="扫码数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">带看城市木屋</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="DKCSMU" name="DKCSMU" maxlength="10" value="1" desc="扫码数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">一键案例推送</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="YJALTS" name="YJALTS" maxlength="10" value="1" desc="扫码数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
							</ul>
						</div>
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="monthTargetSettingPopup.confirm(this)">确定</button>
						</div>
						<div class="c_space"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- popup end-->

<!-- template start-->
<script id="employee_template" rel_id="cust_list" type="text/html">
	<div class="c_list c_list-line">
		<ul>
			{{each EMPLOYEE_LIST employee idx}}
			<li>
				<div class="main">
					<div class="title">{{employee.NAME}}</div>
					<!--
                    <div class="content">
                        <div class="c_param">
                            <ul>
                                <li>
                                    <span class="label">加微：</span>
                                    <span class="value">1</span>
                                </li>
                                <li>
                                    <span class="label">蓝图指导书推送：</span>
                                    <span class="value">2</span>
                                </li>
                                <li>
                                    <span class="label">关注公众号：</span>
                                    <span class="value">1</span>
                                </li>
                                <li>
                                    <span class="label">核心接触：</span>
                                    <span class="value">1</span>
                                </li>
                                <li>
                                    <span class="label">扫码进入全流程：</span>
                                    <span class="value">1</span>
                                </li>
                                <li>
                                    <span class="label">需求蓝图一推送：</span>
                                    <span class="value">1</span>
                                </li>
                                <li>
                                    <span class="label">咨询：</span>
                                    <span class="value">1</span>
                                </li>
                                <li>
                                    <span class="label">带看城市木屋：</span>
                                    <span class="value">1</span>
                                </li>
                                <li>
                                    <span class="label">一间案例推送：</span>
                                    <span class="value">1</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                    -->
				</div>
				<div class="link side" employee_id = "{{employee.EMPLOYEE_ID}}" oper_type="single"
					 ontap="employeeMonthTargetEntry.settingButtonOnClick(this)">
					设定
				</div>
				<div class="fn">
					<input name="employeeBox" value={{employee.EMPLOYEE_ID}} oper_type="batch"
						   style="display: none" type="checkbox"/>
				</div>
			</li>
			{{/each}}
		</ul>
	</div>
</script>
<!-- template end-->

<script type="text/javascript">
	Wade.setRatio();
    employeeMonthTargetEntry.init();
</script>
</body>
</html>