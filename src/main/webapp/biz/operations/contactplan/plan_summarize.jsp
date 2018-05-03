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
<%--<div id="HOUSE_ID_float" class="c_float">--%>
	<%--<div class="bg"></div>--%>
	<%--<div class="content">--%>
		<%--<div class="c_scrollContent">--%>
			<%--<div class="c_list c_list-pc-s c_list-phone-line ">--%>
				<%--<ul id="edit_cust_list">--%>
<%----%>
				<%--</ul>--%>
			<%--</div>--%>
		<%--</div>--%>
	<%--</div>--%>
<%--</div>--%>
<div class="c_header">
	<div class="back" ontap="back();"><span id="planName"></span></div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
	<div class="c_space"></div>
	<div id="edit_cust_list_part">
		<div class="c_title">
			<div class="text">客户资料补录</div>
			<div class="fn">
				<button type="button" ontap="planSummarize.showCustEditPopup(this)">新增客户</button>
			</div>
		</div>
		<div class="c_list c_list-phone-line ">
			<ul id="edit_cust_list">

			</ul>
		</div>
		<div class="c_space"></div>
		<div class="c_submit c_submit-full">
			<button class="e_button-r e_button-l e_button-green" type="button" ontap="planSummarize.showFinishInfo();">确认补录完毕</button>
		</div>
	</div>
	<div class="e_space"></div>
	<div id="finishInfoList" style="display: none">


	</div>
	<div class="c_space"></div>
	<div class="c_submit c_submit-full">
		<button id="submitButton" class="e_button-r e_button-l e_button-green" type="button" ontap="planSummarize.submit()">提交</button>
	</div>
</div>
<!-- 弹出层 开始 -->
<div class="c_popup c_popup-half" id="selectCustPopup">
	<div class="c_popupBg" id="selectCustPopup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="selectCustPopup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="customerSelectPopup">
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
								<%--<div class="left" id="ADD_CUST_BUTTON">--%>
									<%--<button class="" type="button" ontap="selectCust.showCustEdit(this)">--%>
										<%--<span class="e_ico-add"></span>新增客户--%>
									<%--</button>--%>
								<%--</div>--%>
							</div>
						</div>
						<div class="c_space"></div>
						<div class="c_list c_list c_list-col-1 c_list-phone-col-1">
							<ul id="CUST_LIST">
								<%--<li x_tag="x-databind-template" id={custId} style="display:none">--%>
									<%--<label class="group link">--%>
										<%--<div class="content">--%>
											<%--<div class="fn"><input name="selectCustBox" value={CUST_ID} type="checkbox" /></div>--%>
											<%--<div class="main">--%>
												<%--<div class="title" tag="CUST_NAME">{CUST_NAME}</div>--%>
												<%--<div class="content">--%>
													<%--<ul>--%>
														<%--<li tag="MOBILE_NO">{MOBILE_NO}</li>--%>
														<%--<li tag="HOUSE_DETAIL">{HOUSE_DETAIL}</li>--%>
													<%--</ul>--%>
												<%--</div>--%>
											<%--</div>--%>
										<%--</div>--%>
									<%--</label>--%>
									<%--<div class="button">--%>
										<%--<button type="button" class="e_button-s e_button-blue e_button-r"--%>
												<%--custId={CUST_ID} ontap="selectCust.showCustEdit(this)">--%>
											<%--编辑--%>
										<%--</button>--%>
									<%--</div>--%>
								</li>
							</ul>
						</div>
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="selectCust.confirmCusts(this)">确定</button>
						</div>
					</div>
				</div>
			</div>
			<div class="c_popupGroup">
				<div class="c_popupItem" id="customerSelectPopup2">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">客户查询条件</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<div class="c_list c_list_form" id="queryCustParamForm">
							<ul>
								<li>
									<div class="label">客户姓名</div>
									<div class="value">
										<input type="text" name="CUST_NAME"/>
									</div>
								</li>
								<li>
									<div class="label">联系电话</div>
									<div class="value">
										<input type="text" name="MOBILE_NO"/>
									</div>
								</li>
								<li>
									<div class="label">微信昵称</div>
									<div class="value">
										<input type="text" name="WX_NICK"/>
									</div>
								</li>
								<li>
									<div class="label">楼盘</div>
									<div class="value">
										<span id="queryCustParamForm_house_container"></span>
										<%--<span>--请选择--</span>--%>
										<%--<input type="hidden" id="mySelect" name="HOUSE_ID" value="" nullable="yes" desc="选择项目" />--%>
										</span>
									</div>
								</li>
							</ul>
						</div>
						<!-- 客户列表 结束 -->
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="selectCust.queryCust(this)">查询</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="c_popup c_popup-half" id="custInfoEditPopup">
	<div class="c_popupBg" id="custInfoEditPopup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="custInfoEditPopup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="custInfoEditPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">客户信息录入</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<div class="c_list c_list-form" id="custForm">
							<ul>
								<li class="required">
									<div class="label">客户姓名</div>
									<div class="value">
										<input type="text" id="CUST_NAME" name="CUST_NAME" nullable="no" desc="客户姓名"/>
										<input type="text" id="CUST_ID" name="CUST_ID" style="display: none"/>
									</div>
								</li>
								<li class="required">
									<div class="label">微信昵称</div>
									<div class="value">
										<input type="text" id="WX_NICK" name="WX_NICK" nullable="no" desc="微信昵称"/>
									</div>
								</li>
								<li class="required">
									<div class="label">性别</div>
									<div class="value">
										<div class="e_switch">
											<div class="e_switchOn">男</div>
											<div class="e_switchOff">女</div>
											<input type="hidden" id="SEX" name="SEX" nullable="no" desc="性别"/>
										</div>
									</div>
								</li>
								<li>
									<div class="label">电话号码</div>
									<div class="value">
										<input type="text" id="MOBILE_NO" name="MOBILE_NO"/>
									</div>
								</li>
								<li class="required">
									<div class="label">楼盘</div>
									<div class="value">
										<span id="custEditForm_house_container"></span>
										<%--<span>--请选择--</span>--%>
										<%--<input type="hidden" id="HOUSE_ID" name="HOUSE_ID" value="" nullable="yes" desc="楼盘" />--%>
										</span>
									</div>
								</li>
								<li class="required">
									<div class="label">楼栋号</div>
									<div class="value">
										<input type="text" id="HOUSE_DETAIL" name="HOUSE_DETAIL" nullable="no" desc="楼栋号"/>
									</div>
								</li>
								<li class="required">
									<div class="label">户型</div>
									<div class="value">
										<input type="text" id="HOUSE_MODE" name="HOUSE_MODE" nullable="no" desc="户型"/>
									</div>
								</li>
								<li>
									<div class="label">面积</div>
									<div class="value">
										<span class="e_mix">
											<input type="text" id="HOUSE_AREA" name="HOUSE_AREA"/>
											<span class="e_label"><span>平方</span></span>
										</span>
									</div>
								</li>
							</ul>
						</div>
						<!-- 客户列表 结束 -->
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="custEditPopup.submitCustInfo(this)">提交</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="c_popup c_popup-half" id="summarizeUnFinishCustPopup">
	<div class="c_popupBg" id="summarizeUnFinishCustPopup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="summarizeUnFinishCustPopup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="summarizeUnFinishCustPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">客户总结</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<div class="c_list c_list-border">
							<ul>
								<li id="summarize_cust_info_part">
									<div class="main">
										<div class="title" tag="cust_name"></div>
										<div class="content content-auto">
											<div class="c_param">
												<ul>
													<li>
														<span class="label">性别：</span>
														<span class="value" tag="sex"></span>
													</li>
													<li>
														<span class="label">微信昵称：</span>
														<span class="value" tag="wx_nick"></span>
													</li>
													<li>
														<span class="label">电话号码：</span>
														<span class="value" tag="mobile_no"></span>
													</li>
													<li>
														<span class="label">楼盘信息：</span>
														<span class="value" tag="house_detail"></span>
													</li>
												</ul>
											</div>
										</div>
									</div>
								</li>
							</ul>
						</div>
						<div class="c_space"></div>
						<div class="c_list">
							<ul>
								<li>
									<div class="label">原因：</div>
									<div class="value">
										<div class="c_list c_list-v c_list-col-3 c_list-space c_list-line c_list-s">
											<ul id="cause_options">

											</ul>
										</div>
										<div class="c_list" id="other_cause_part" style="display: none">
											<ul>
												<li>
													<textarea class="e_textarea-row-2" ></textarea>
												</li>
											</ul>
										</div>
									</div>
								</li>
							</ul>
						</div>

						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="summaryPopup.confirm(this)">确定</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 弹出层结束 -->
<!--
<script type="text/template" id="ACTION_TEMPLATE">
	<li class="link" id="{ACTION_CODE}" ontap="{TAP_FUNCTION}">
		<div class="main">
			<div class="title group">
				<span tag="ACTION_NAME_TEXT">{ACTION_NAME}</span>
			</div>
			<div tag="ACTION_NAME_CONTENT" class="content content-auto">
				<ul>
					<li><span>计划数：{PLAN_CUSTNUM}</span></li>
					<li><span>计划客户：{PLAN_CUSTNAMES}</span></li>
					<li><span class="">实际完成数：{FINISH_CUSTNUM}</span></li>
					<li><span>实际客户：{FINISH_CUSTNAMES}</span></li>
				</ul>
			</div>
		</div>
		<div tag="ACTION_SIDE" class="side">{SIDE_NAME}</div>
		<div tag="ACTION_MORE" class="more"></div>
	</li>
</script>
-->
<script type="text/template" id="CUST_TEMPLATE">
	<li>
		<label class="group link">
			<div class="content">
				<div class="fn"><input name="selectCustBox" value={CUST_ID} {CHECKED} type="checkbox" /></div>
				<div class="main">
					<div class="title" tag="CUST_NAME">{CUST_NAME}</div>
					<div class="content">
						<ul>
							<li tag="MOBILE_NO">{MOBILE_NO}</li>
							<li tag="HOUSE_DETAIL">{HOUSE_DETAIL}</li>
						</ul>
					</div>
				</div>
			</div>
		</label>
		<%--<div class="button">--%>
			<%--<button type="button" class="e_button-s e_button-blue e_button-r"--%>
					<%--custId={CUST_ID} ontap="selectCust.showCustEdit(this)">--%>
				<%--编辑--%>
			<%--</button>--%>
		<%--</div>--%>
	</li>
</script>
<script id="finishInfoTemplate" type="text/html">
	<div class="c_box c_box-border" id="FINISH_INFO_{{ACTION_CODE}}">
		<div class="c_title" ontap="$(this).next().toggle();">
			<div class="text">{{ACTION_NAME}}</div>
			<div class="fn">
				<ul>
					<li><span>计划数:{{PLAN_CUSTNUM}}人 / 实际数:<span tag="finishCustNum">{{FINISH_CUSTNUM}}</span></span><span class="e_ico-unfold"></span></li>
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
					{{each PLAN_CUST_LIST cust idx}}
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
					<li class="link" action_code="{{ACTION_CODE}}" ontap="{{SELECT_CUST_FUNC}}">
						<div class="content">
							<div class="main">
								<div class="title e_strong">实际完成客户</div>
							</div>
							{{if SELECT_CUST_FUNC}}
							<div class="side proedit">
								选择客户
							</div>
							<div class="more"></div>
							{{/if}}
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul tag="FINISH_CUST_LIST">
					{{each FINISH_CUST_LIST cust idx}}
					<li cust_id="{{cust.CUST_ID}}"
						action_code="{{ACTION_CODE}}" class="link" oper_code="{{OPER_CODE}}"
						action_id="{{cust.ACTION_ID}}" li_type="finish"
					>
						<div class="main">
							<div class="title">{{cust.CUST_NAME}}</div>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
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
					{{each UNFINISH_CUST_LIST cust idx}}
					<li tag="UNFINISH_{{cust.CUST_ID}}" cust_id="{{cust.CUST_ID}}"
						action_code="{{ACTION_CODE}}" class="link" oper_code="{{OPER_CODE}}"
						action_id="{{cust.ACTION_ID}}" li_type="unFinish"
						unfinish_cause_id="{{cust.UNFINISH_CAUSE_ID}}"
						unfinish_cause_desc="{{cust.UNFINISH_CAUSE_DESC}}"
						ontap="planSummarize.summarize(this)">
						<div class="main">
							<div class="title" >{{cust.CUST_NAME}}<span class="e_ico-edit"></span></div>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
		</div>
	</div>
</script>
<script id="finishCustListTemplate" type="text/html">
	{{each FINISH_CUST_LIST cust idx}}
	<li cust_id="{{cust.CUST_ID}}"
		action_code="{{ACTION_CODE}}" class="link" oper_code="{{OPER_CODE}}"
		action_id="{{cust.ACTION_ID}}" li_type="finish"
		>
		<div class="main">
			<div class="title">{{cust.CUST_NAME}}</div>
		</div>
	</li>
	{{/each}}
</script>
<script id="unFinishCustListTemplate" type="text/html">
	{{each UNFINISH_CUST_LIST cust idx}}
	<li tag="UNFINISH_{{cust.CUST_ID}}" cust_id="{{cust.CUST_ID}}"
		action_code="{{ACTION_CODE}}" class="link" oper_code="{{OPER_CODE}}"
		action_id="{{cust.ACTION_ID}}" li_type="unFinish"
		unfinish_cause_id="{{cust.UNFINISH_CAUSE_ID}}"
		unfinish_cause_desc="{{cust.UNFINISH_CAUSE_DESC}}"
		ontap="planSummarize.summarize(this)">
		<div class="main">
			<div class="title">{{cust.CUST_NAME}}<span class="e_ico-edit"></span></div>
		</div>
	</li>
	{{/each}}
</script>
<script id="cause_option_template" rel_id = "cause_options" type="text/html">
	{{each CAUSE_LIST cause idx}}
	<li class="link" cause_id="{{cause.CAUSE_ID}}" cause_name="{{cause.CAUSE_NAME}}">
		<div class="main">{{cause.CAUSE_NAME}}</div>
	</li>
	{{/each}}
</script>
<script id="edit_cust_list_template" rel_id="edit_cust_list" type="text/html">
	{{each CUST_LIST cust idx}}
	<li class="link" ontap="planSummarize.showCustEditPopup(this)" cust_id = {{cust.CUST_ID}}>
		<div class="main">
			<div class="title" tag="cust_name">{{cust.CUST_NAME}}</div>
			<div class="content content-auto">
				<div class="c_param c_param-label-auto">
					<ul id="edit_cust_{{cust.CUST_ID}}">
						<li>
							<span class="label">微信昵称：</span>
							<span class="value" tag="wx_nick">{{cust.WX_NICK}}</span>
						</li>
					</ul>
				</div>
			</div>

		</div>
		<div class="side"><span class="e_ico-edit"></span></div>
	</li>
	{{/each}}
</script>
<script id="edit_cust_action_list_template" rel_id="" type="text/html">
	{{each CUST_FINISH_ACTION_LIST action idx}}
	<li>
		<span class="label">{{action.ACTION_NAME}}：</span>
		<span class="value">{{action.FINISH_TIME}}</span>
	</li>
	{{/each}}
</script>
<script type="text/javascript">
	Wade.setRatio();
    planSummarize.init();
</script>
</body>
</html>