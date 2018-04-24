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
    <title>今日计划录入</title>
	<jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/contactplan/plan.entry.js"></script>
</head>
<body>
<div class="l_edit">
    <div class="c_header e_show-phone">
        <div class="back" ontap="closeNav();">今日计划录入</div>
    </div>
    <span class="l_editMain">
    	<div class="c_title">
			<div class="text">2018-04-20计划</div>
		</div>
		<div class="c_list c_list-line c_list-space">
			<ul>
				<li>
					<span class="e_segment">
						<span idx="0" val="0">正常上班</span>
						<span idx="1" val="1">活动</span>
						<span idx="2" val="2">休假</span>
						<input type="hidden" name="planStatus" id="workMode" nullable="no" desc="级别" />
					</span>
				</li>
			</ul>
		</div>
		<div id="ACTION_PART" style="display: '';">
			<!--<div class="c_space"></div>-->
			<!--
			<div class="c_title">
				<div class="text">设定动作及客户</div>
			</div>
			-->
			<div class="c_list c_list-line c_list-space">
				<div class="c_space"></div>
				<ul id="ACTION_LIST">
					<li class="link" tag="ACTION_TAG" x_tag="x-databind-template" style="display:none" id={ACTION_CODE}>
						<div class="main">
							<div class="title group">
								<span tag="ACTION_NAME_TEXT">{ACTION_NAME}</span>
								<span tag="ACTION_ICO_OK" class="fn" style="display: none;"><span class="e_ico-ok e_ico-pic e_ico-pic-xxxs"></span></span>
							</div>
							<div tag="ACTION_NAME_CONTENT" class="content content-auto"></div>
						</div>
						<div tag="ACTION_SIDE" class="side" style="display: none;">选择客户</div>
						<div tag="ACTION_MORE" class="more" style="display: none;"></div>
					</li>
				</ul>
			</div>
		</div>
		<div class="c_space"></div>
		<div class="c_submit c_submit-full">
			<button class="e_button-r e_button-l e_button-green" type="button">提交</button>
		</div>
	</span>
    <span class="l_editPlace"></span>
</div>
<!-- 弹出层 开始 -->
<div class="c_popup c_popup-half" id="myPopup">
	<div class="c_popupBg" id="myPopup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="myPopup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="customerSelectPopup">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">选择客户</div>
					</div>	
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<!-- 新客户 开始 -->
						<!--<div id="NEW_CUST_PART" style="display: none;">-->
						<div class="c_list">
							<ul>
								<li>
									<div class="label">新客户</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="newCustNum" maxlength="10" value="0" desc="数量"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
							</ul>
						</div>
						<div class="c_space"></div>
						<!--</div>-->
						<!-- 新客户 结束 -->
						<!-- 查询客户 开始-->
						<div id="QUERY_CUST_PART" style="display: none;">
							<div class="l_queryFn">
								<div class="c_fn">
									<div class="right">
										<button class="e_button-blue" type="button" ontap="forwardPopup(this,'customerSelectPopup2');">
											<span class="e_ico-search"></span>
										</button>
									</div>
								</div>
							</div>
							<div class="c_list">
								<div class="c_list c_list c_list-col-2 c_list-phone-col-1">
									<ul id="CUST_LIST">
										<li x_tag="x-databind-template" style="display:none">
											<label class="group link">
												<div class="content">
													<div class="main">
														<div class="title">{CUST_NAME}</div>
														<div class="content">
															<ul>
																<li>{SERIAL_NUMBER}</li>
																<li>{HOUSE_INFO}</li>
															</ul>
														</div>
													</div>
													<div class="fn"><input name="selectCustBox" value={CUST_NAME} type="checkbox" /></div>
												</div>
											</label>
										</li>
									</ul>
								</div>
							</div>
						</div>
						<!-- 查询客户 结束 -->
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="planEntry.afterSelectedCust(this)">确定</button>
						</div>
						<div class="c_space"></div>
					</div>
				</div>
				<div class="c_popupItem" id="planTargetSetPopup">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">目标设置</div>
					</div>	
					<div class="l_padding">
						<div class="c_list">
							<ul>
								<li>
									<div class="label">今日休假</div>
									<div class="value">
										<div class="e_switch">
											<div class="e_switchOn">是</div>
											<div class="e_switchOff">否</div>
											<input type="hidden" id="holidaySwitch" />
										</div>
									</div>
									
								</li>
								<li>
									<div class="label">核心接触数</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="contactNum" maxlength="10" value="0" desc="核心接触数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">咨询数</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="adviceNum" maxlength="10" value="0" desc="咨询数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
							</ul>
						</div>
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="planEntry.afterPlanTargetSet(this)">确定</button>
						</div>
						<div class="c_space"></div>
					</div>
				</div>
			</div>
			<div class="c_popupGroup">
				<div class="c_popupItem" id="customerSelectPopup2">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">客户查询条件</div>
					</div>	
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<div class="c_list c_list_form">
							<ul>
								<li>
									<div class="label">客户姓名</div>
									<div class="value">
										<input type="text" />
									</div>
								</li>
								<li>
									<div class="label">联系电话</div>
									<div class="value">
										<input type="text" />
									</div>
								</li>
								<li>
									<div class="label">楼盘</div>
									<div class="value">
										<span class="e_select">
											<span>--请选择--</span>
											<input type="hidden" id="mySelect" value="" nullable="yes" desc="选择项目" />
										</span>
									</div>
								</li>
							</ul>
						</div>
						<!-- 客户列表 结束 -->
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="planEntry.queryCust(this)">查询</button>
						</div>
						<div class="c_space"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 弹出层 结束 -->
<script type="text/template" id="SELECTED_CUST">
<li>
	<div class="main">
		<div class="title">{actionName}：{num}</div>
		<div class="content">{custName}</div>
	</div>
</li>
</script>


<script type="text/javascript">
	Wade.setRatio();
    planEntry.init();
</script>
</body>
</html>