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
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span id="planName"></span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_list c_list-line c_list-space">
		<ul>
			<li>
					<span class="e_segment">
						<span idx="0" val="1">正常上班</span>
						<span idx="1" val="2">活动</span>
						<span idx="2" val="3">休假</span>
						<input type="hidden" name="planStatus" id="workMode" nullable="no" desc="级别" />
					</span>
			</li>
			<li class="link" id="PLAN_TARGET_SET_PART">
				<div class="main">
					<div class="title">今日目标设置</div>
					<div id="planTarget">
						<div class="content">
							<ul>
								<li>咨询数：<span tag="adviceNum"></span></li>
								<li>核心接触数：<span tag="scanHouseCounselorNum"></span></li>
								<li>加微数：<span tag="addWxNum"></span></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="more"></div>
			</li>
		</ul>
	</div>
	<div id="ACTION_PART" style="display: none;">
		<div class="c_space"></div>
		<%--<div class="c_title">--%>
			<%--<div class="text">设定动作及客户</div>--%>
		<%--</div>--%>
		<div class="c_list c_list-line c_list-space">
			<div class="c_space"></div>
			<ul id="ACTION_LIST">
				<%--<li class="link" tag="ACTION_TAG" x_tag="x-databind-template" style="display:none" id={ACTION_CODE}>--%>
					<%--<div class="main">--%>
						<%--<div class="title group">--%>
							<%--<span tag="ACTION_NAME_TEXT">{ACTION_NAME}</span>--%>
							<%--<span tag="ACTION_ICO_OK" class="fn" style="display: none;"><span class="e_ico-ok e_ico-pic e_ico-pic-xxxs"></span></span>--%>
						<%--</div>--%>
						<%--<div tag="ACTION_NAME_CONTENT" class="content content-auto"></div>--%>
					<%--</div>--%>
					<%--<div tag="ACTION_SIDE" class="side" style="display: none;">选择客户</div>--%>
					<%--<div tag="ACTION_MORE" class="more" style="display: none;"></div>--%>
				<%--</li>--%>
			</ul>
		</div>
	</div>
	<div class="c_space"></div>
	<div class="c_submit c_submit-full">
		<button class="e_button-r e_button-l e_button-green" type="button" ontap="planEntry.submitPlan()">提交</button>
	</div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<div class="c_popup" id="myPopup">
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
						<div id="NEW_CUST_PART" style="display: none;">
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
						</div>
						<!-- 新客户 结束 -->
						<!-- 上一动作选择的客户 -->
						<div id="before_action_cust_list_part">
						</div>
						<!-- 上一动作选择的客户 -->

						<!-- 查询客户 开始-->
						<div class="c_space"></div>
						<div id="QUERY_CUST_PART" style="display: none;">
							<div class="l_queryFn">
								<div class="c_fn">
									<div class="right">
										<button class="e_button-blue" type="button" ontap="forwardPopup(this,'customerSelectPopup2');">
											<span class="e_ico-search"></span>
										</button>
									</div>
									<div class="left" id="ADD_CUST_BUTTON">
										<button class="" type="button" ontap="selectCust.showCustEdit(this)">
											<span class="e_ico-add"></span>新增客户
										</button>
									</div>
								</div>
							</div>
							<div class="c_list c_list-col-1 c_list-phone-col-1">
								<ul id="CUST_LIST">

								</ul>
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
						<div class="c_list c_form-label-5">
							<ul>
								<li>
									<div class="label">咨询数</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="adviceNum" maxlength="100" value="0" desc="咨询数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">核心接触数</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="scanHouseCounselorNum" maxlength="100" value="0" desc="核心接触数"/>
											<span class="e_ico-add"></span>
										</div>
									</div>
								</li>
								<li>
									<div class="label">加微数</div>
									<div class="value">
										<div class="e_mix">
											<span class="e_ico-reduce"></span>
											<input type="text" class="e_center" id="addWxNum" maxlength="10" value="1" desc="扫码数"/>
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
						<div class="c_space"></div>
					</div>
				</div>
				<div class="c_popupItem" id="custInfoEditPopup">
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
										<input type="text" id="MOBILE_NO" name="MOBILE_NO" nullable="yes" desc="电话号码"/>
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
								<li>
									<div class="label">客户基本情况</div>
									<div class="value">
										<textarea name="CUST_DETAIL" class="e_textarea-row-3" ></textarea>
									</div>
								</li>
							</ul>
						</div>
						<!-- 客户列表 结束 -->
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="selectCust.submitCustInfo(this)">提交</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/template" id="CUST_TEMPLATE">
	<li>
		<label class="group link ">
			<div class="content">
				<div class="main">
					<div class="title" tag="CUST_NAME">{CUST_NAME}</div>
					<div class="content">
						<ul>
							<li>微信昵称：{WX_NICK}</li>
						</ul>
					</div>
				</div>
				<div class="fn"><input name="selectCustBox" value={CUST_ID} {CHECKED}
									   type="checkbox" onchange="selectCust.selectCustBoxClick(this)"/></div>
			</div>
		</label>
	</li>
</script>
<script id="before_action_cust_list_template" type="text/html" rel_id = "before_action_cust_list_part">
	<div class="c_box c_box-border" id="FINISH_INFO_{{ACTION_CODE}}">
		<div class="c_title" ontap="$(this).next().toggle();">
			<div class="text">上一动作【{{BEFORE_ACTION_NAME}}】选择的客户</div>
			<div class="fn">
				<ul>
					<li><span>人数:{{BEFORE_ACTION_CUSTNUM}}人</span><span class="e_ico-unfold"></span></li>
				</ul>
			</div>
		</div>
		<div class="l_padding l_padding-u">
			<div class="c_list c_list-v c_list-col-3">
				<ul>
					{{each CUST_LIST cust idx}}
					<li class="link" ontap="$(this).find('div[tag=cust_title]').addClass('e_delete')">
						<div class="main">
							<div class="title" tag="cust_title" cust_id="{{cust.CUST_ID}}">{{cust.CUST_NAME}}<span class="e_ico-delete"></span></div>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
		</div>
	</div>
</script>
<script id="action_list_template" type="text/html" rel_id="ACTION_LIST">
	{{each ACTION_LIST action idx}}
	<div class="c_box c_box-border" id="FINISH_INFO_{{action.ACTION_CODE}}" tag="PLAN_ACTION" action_code="{{action.ACTION_CODE}}">
		<div class="c_title" ontap="$(this).next().toggle();">
			<div class="text e_strong e_blue">{{action.ACTION_NAME}}</div>
			<div class="fn">
				<ul>
					<%--<li><span>计划数:{{PLAN_CUSTNUM}}人 / 实际数:<span tag="finishCustNum">{{FINISH_CUSTNUM}}</span></span><span class="e_ico-unfold"></span></li>--%>
						<li><span class="e_ico-unfold"></span></li>
				</ul>
			</div>
		</div>
		<div class="l_padding l_padding-u">
			{{if action.NEW_CUST_LIST && action.NEW_CUST_LIST.length > 0}}
			<div class="c_list">
				<ul>
					<li class="link" ontap="">
						<div class="content">
							<div class="main">
								<div class="title e_strong">新客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul>
					{{each action.NEW_CUST_LIST cust idx}}
					<li cust_id="{{cust.CUST_ID}}"
						action_code="{{ACTION_CODE}}" class="link" oper_code="{{OPER_CODE}}"
						li_type="cust"
						{{if action.CAN_DELETE_NEWCUST}}
						ontap="planEntry.operCust(this)"
						{{/if}}
					>
						<div class="main">
							<div class="title">
								<span tag="text">{{cust.CUST_NAME}}</span>
								{{if action.CAN_DELETE_NEWCUST}}
								<span tag="ico" class="e_ico-delete e_blue"></span>
								{{/if}}
							</div>
						</div>
					</li>
					{{/each}}
				</ul>
			</div>
			{{/if}}
			{{if action.CUST_LIST && action.CUST_LIST.length > 0}}
			<div class="c_line c_line-dashed"></div>
			<div class="c_list">
				<ul>
					<li class="link" action_code="{{ACTION_CODE}}" ontap="{{SELECT_CUST_FUNC}}">
						<div class="content">
							<div class="main">
								<div class="title e_strong">未完成客户</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul tag="CUST_LIST">
					{{each action.CUST_LIST cust idx}}
					<li cust_id="{{cust.CUST_ID}}"
						action_code="{{ACTION_CODE}}" class="link" oper_code="{{OPER_CODE}}"
						li_type="cust"
						{{if action.CAN_DELETE}}
						ontap="planEntry.operCust(this)"
						{{/if}}
					>
						<div class="main">
							<div class="title">
								<span tag="text">{{cust.CUST_NAME}}</span>
								{{if action.CAN_DELETE}}
								<span tag="ico" class="e_ico-delete e_blue"></span>
								{{/if}}
							</div>

						</div>
					</li>
					{{/each}}
				</ul>
			</div>
			{{/if}}
		</div>
	</div>
	{{/each}}
</script>
<script id="action_list_template1" type="text/html" rel_id = "ACTION_LIST">
	{{each ACTION_LIST action idx}}
	<li class="link" tag="ACTION_TAG" id="{{action.ACTION_CODE}}">
		<div class="main">
			<div class="title group">
				<span tag="ACTION_NAME_TEXT">{{action.ACTION_NAME}}</span>
				<span tag="ACTION_ICO_OK" class="fn" style="display: none;"><span class="e_ico-ok e_ico-pic e_ico-pic-xxxs"></span></span>
			</div>
			<div tag="ACTION_NAME_CONTENT" class="content content-auto"></div>
		</div>
		<div tag="ACTION_SIDE" class="side" style="display: none;">选择客户</div>
		<div tag="ACTION_MORE" class="more" style="display: none;"></div>
	</li>
	{{/each}}
</script>

<script type="text/javascript">
	Wade.setRatio();
    planEntry.init();
</script>
</body>
</html>