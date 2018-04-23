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
    <title>今日计划总结</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/contactplan/plan.summarize.js"></script>
</head>
<body>
<div class="l_edit">
    <div class="c_header e_show-phone">
        <div class="back" ontap="closeNav();">今日计划总结</div>
    </div>
    <span class="l_editMain">
    	<div class="c_title">
			<div class="text">2018-04-20计划总结</div>
			<div class="fn">
				<ul>
					<li>
						<span class="e_ico-add"></span>客户信息补录
					</li>
				</ul>
			</div>
		</div>
		<div class="c_list c_list-line c_list-space">
			<ul id="ACTION_LIST">
				<!--<li class="link" tag="ACTION_TAG" x_tag="x-databind-template" style="display:none" id={ACTION_CODE}>
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">{ACTION_NAME}<span>
							<span tag="ACTION_ICO_OK" class="fn" style="display: none;"><span class="e_ico-ok"></span></span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content">
							<ul>
								<li><span>计划数：{PLAN_NUM}</span></li>
								<li><span class="e_red">实际完成数：{REAL_NUM}</span></li>
							</ul>
						</div>
					</div>
					<div tag="ACTION_SIDE" class="side">填写总结</div>
					<div tag="ACTION_MORE" class="more"></div>
				</li>-->
				<li class="link" id="JW" ontap="planSummarize.selectCust(this)">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">加微</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span tag="factCustNum"></span></li>
								<li><span tag="factCustDetail"></span></li>
							</ul>
						</div>
					</div>
					<div tag="ACTION_SIDE" class="side">选择客户</div>
					<div tag="ACTION_MORE" class="more"></div>
				</li>
				<li class="link" id="LTZDSTS" ontap="">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">蓝图指导书推送</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span class="">实际完成数：3</span></li>
								<li><span>实际客户：张三、王五、weixin1</span></li>
							</ul>
						</div>
					</div>
					<!--<div tag="ACTION_SIDE" class="side">填写总结</div>-->
					<!--<div tag="ACTION_MORE" class="more"></div>-->
				</li>
				<li class="link" id="GZHGZ" ontap="">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">公众号关注</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span class="">实际完成数：2</span></li>
								<li><span>实际客户：张三、weixin1</span></li>
							</ul>
						</div>
					</div>
					<!--<div tag="ACTION_SIDE" class="side">填写总结</div>-->
					<!--<div tag="ACTION_MORE" class="more"></div>-->
				</li>
				<li class="link" id="HXJC" ontap="planSummarize.selectCust(this)">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">核心接触</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span tag="factCustNum"></span></li>
								<li><span tag="factCustDetail"></span></li>
							</ul>
						</div>
					</div>
					<div tag="ACTION_SIDE" class="side">选择客户</div>
					<div tag="ACTION_MORE" class="more"></div>
				</li>
				<li class="link" id="SMJRQLC" ontap="">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">扫码进入全流程</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span class="">实际完成数：3</span></li>
								<li><span>实际客户：张三、李四、王五</span></li>
							</ul>
						</div>
					</div>
					<!--<div tag="ACTION_SIDE" class="side">填写总结</div>-->
					<!--<div tag="ACTION_MORE" class="more"></div>-->
				</li>
				<li class="link" id="XQLTYTS" ontap="">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">需求蓝图一推送</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span class="">实际完成数：3</span></li>
								<li><span>实际客户：张三、李四、王五</span></li>
							</ul>
						</div>
					</div>
					<!--<div tag="ACTION_SIDE" class="side">填写总结</div>-->
					<!--<div tag="ACTION_MORE" class="more"></div>-->
				</li>
				<li class="link" id="ZX" ontap="">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">咨询</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span class="">实际完成数：3</span></li>
								<li><span>实际客户：张三、李四、王五</span></li>
							</ul>
						</div>
					</div>
					<!--<div tag="ACTION_SIDE" class="side">填写总结</div>-->
					<!--<div tag="ACTION_MORE" class="more"></div>-->
				</li>
				<li class="link" id="DKCSMW" ontap="planSummarize.selectCust(this)">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">带看城市木屋</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span tag="factCustNum"></span></li>
								<li><span tag="factCustDetail"></span></li>
							</ul>
						</div>
					</div>
					<div tag="ACTION_SIDE" class="side">选择客户</div>
					<div tag="ACTION_MORE" class="more"></div>
				</li>
				<li class="link" id="YJALTS" ontap="">
					<div class="main">
						<div class="title group">
							<span tag="ACTION_NAME_TEXT">一键案例推送</span>
						</div>
						<div tag="ACTION_NAME_CONTENT" class="content content-auto">
							<ul>
								<li><span>计划数：3</span></li>
								<li><span>计划客户：张三、李四、王五</span></li>
								<li><span class="">实际完成数：3</span></li>
								<li><span>实际客户：张三、李四、王五</span></li>
							</ul>
						</div>
					</div>
					<!--<div tag="ACTION_SIDE" class="side">填写总结</div>-->
					<!--<div tag="ACTION_MORE" class="more"></div>-->
				</li>
			</ul>
		</div>
		<div class="c_space"></div>
		<div class="c_list">
			<ul>
				<li>
					<div class="label">今日总结</div>
					<div class="value">
						<textarea class="e_textarea-row-4"></textarea>
					</div>
				</li>
			</ul>
		</div>
		<div class="c_space"></div>
		<div class="c_submit c_submit-full">
			<button class="e_button-r e_button-l e_button-green" type="button">提交</button>
		</div>
	</span>
    <span class="l_editPlace"></span>
</div>
<!-- 弹出层 开始 -->
<!--
<div class="c_popup c_popup-half" id="myPopup">
	<div class="c_popupBg" id="myPopup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="myPopup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="customerSelectPopup3">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">选择客户</div>
					</div>	
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
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
							<div class="c_space"></div>
							<div class="c_submit c_submit-full">
								<button type="button" class="e_button-l e_button-green" ontap="planEntry.afterSelectedCust(this)">确定</button>
							</div>
							<div class="c_space"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="c_popupGroup">
				<div class="c_popupItem" id="customerSelectPopup4">
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
-->
<jsp:include page="../popup/select_cust.jsp"></jsp:include>
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
    planSummarize.init();
</script>
</body>
</html>