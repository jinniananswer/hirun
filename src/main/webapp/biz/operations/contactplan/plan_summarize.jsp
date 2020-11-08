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
	<script src="scripts/biz/contactplan/plan.summarize.js?a=10"></script>
</head>
<body>
<%--<div class="c_header">--%>
	<%--<div class="back" ontap="back();"><span id="planName"></span></div>--%>
<%--</div>--%>
<jsp:include page="/header.jsp">
	<jsp:param value="" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
	<div class="c_space"></div>
	<div id="edit_cust_list_part">
		<div class="c_title">
			<div class="text">客户资料补录</div>
			<%--<div class="fn">--%>
				<%--<button type="button" ontap="planSummarize.showCustEditPopup(this)">新增客户</button>--%>
			<%--</div>--%>
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
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<!-- 弹出层 开始 -->
<div class="c_popup" id="selectCustPopup">
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
							</div>
						</div>
						<div class="c_space"></div>
						<div class="c_list c_list c_list-col-1 c_list-phone-col-1">
							<ul id="CUST_LIST">

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
								<%--<li>--%>
									<%--<div class="label">微信昵称</div>--%>
									<%--<div class="value">--%>
										<%--<input type="text" name="WX_NICK"/>--%>
									<%--</div>--%>
								<%--</li>--%>
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
<div class="c_popup" id="custInfoEditPopup">
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
								<li>
									<div class="label">微信昵称</div>
									<div class="value">
										<input type="text" id="WX_NICK" name="WX_NICK" desc="微信昵称" readonly="true"/>
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
								<li class="required">
									<div class="label">电话号码</div>
									<div class="value">
										<input type="text" id="MOBILE_NO" name="MOBILE_NO" nullable="no" desc="电话号码"/>
									</div>
								</li>
								<li class="link required">
									<div class="label">楼盘</div>
									<div class="value">
										<span id="custEditForm_house_container"></span>
										<%--<span>--请选择--</span>--%>
										<%--<input type="hidden" id="HOUSE_ID" name="HOUSE_ID" value="" nullable="yes" desc="楼盘" />--%>
									</div>
								</li>
								<li class="link required" style="display: none" id="liScatter">
									<div class="label">散盘名称</div>
									<div class="value">
										<span class="e_mix" ontap="custEditPopup.onClickSelectScatterHousesButton(this)">
											<input type="text" id="SCATTER_HOUSE" name="SCATTER_HOUSE" datatype="text"
												   houses_id=""
												   nullable="yes" desc="散盘名称" value="" readonly="true"/>
											<span class="e_ico-check"></span>
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
										<span id="custEditForm_houseMode_container"></span>
									</div>
								</li>
								<li class="required">
									<div class="label">面积</div>
									<div class="value">
										<span class="e_mix">
											<input type="text" id="HOUSE_AREA" name="HOUSE_AREA" nullable="no" desc="面积"/>
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
							<button type="button" class="e_button-l e_button-green" ontap="custEditPopup.submitCustInfo(this)">提交</button>
						</div>
					</div>
				</div>
			</div>
			<div class="c_popupGroup">
				<div class="c_popupItem" id="scatterHousesPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">请选择散盘</div>
						<div class="right">
							<div class="fn">
<!--								<button type="button" class="e_button-blue"
										ontap="scatterHousesPopup.onClickAddScatterHousesButton(this)">
									<span class="e_ico-add"></span>
									<span>新增散盘</span>
								</button>
-->
							</div>

						</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
						<div class="c_list c_list-form">
							<ul>
								<li>
									<div class="value">
                                        <span class="e_mix">
                                            <input id="scatterHousesPopupItem_HOUSE_SEARCH_TEXT" name="scatterHousesPopupItem_HOUSE_SEARCH_TEXT" type="text" placeholder="散盘名称（模糊搜索）" nullable="no" desc="查询条件">
                                            <button type="button" class="e_button-blue" ontap="scatterHousesPopup.searchHouses($('#scatterHousesPopupItem_HOUSE_SEARCH_TEXT').val())"><span class="e_ico-search"></span><span>查询</span></button>
                                        </span>
									</div>
								</li>
							</ul>
						</div>
						<div class="c_list c_list-col-1 c_list-line c_list-border c_list-fixWrapSpace">
							<ul id="BIZ_SCATTER_HOUSES">

							</ul>
						</div>
						<div class="c_line"></div>
					</div>
				</div>
			</div>
			<div class="c_popupGroup">
				<div class="c_popupItem" id="createScatterHousesPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">新增散盘</div>
					</div>
						<div class="c_list c_list-form" id="createScatterHousesForm">
							<ul>
								<li>
									<div class="label">散盘名称</div>
									<div class="value">
										<input type="text" id="createScatterHousesForm_SCATTER_HOUSES_NAME" name="createScatterHousesForm_SCATTER_HOUSES_NAME" desc="散盘名称" nullable="no"/>
									</div>
								</li>
							</ul>
						</div>
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-l e_button-green" ontap="createScatterHousesPopup.submitScatterHouses(this)">新增</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="c_popup" id="summarizeUnFinishCustPopup">
	<div class="c_popupBg" id="summarizeUnFinishCustPopup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="summarizeUnFinishCustPopup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="summarizeUnFinishCustPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">客户总结</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<div id="summarize_cust_info_part" class="c_list c_list-border">
							<ul>
								<li>
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
										<div class="c_list c_list-v c_list-col-1 c_list-phone-col-1 c_list-space c_list-line c_list-s">
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
							<button type="button" class="e_button-l e_button-red" ontap="summaryPopup.cancel(this)">取消</button>
							<button type="button" class="e_button-l e_button-green" ontap="summaryPopup.confirm(this)">确定</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 弹出层结束 -->
<script type="text/html" id="CUST_TEMPLATE">
	<li>
		<label class="group link ">
			<div class="content">
				<div class="main">
					<div class="title" tag="CUST_NAME">{{CUST_NAME}}</div>
					<div class="content">
						<ul>
							<li>微信昵称：{{WX_NICK}}</li>
						</ul>
					</div>
				</div>
				<div class="fn"><input name="selectCustBox" value={{CUST_ID}} {CHECKED}
									   type="checkbox" onchange="selectCust.selectCustBoxClick(this)"/></div>
			</div>
		</label>
	</li>
</script>
<script id="finishInfoTemplate" type="text/html">
	<div class="c_box c_box-border c_box-gray" id="FINISH_INFO_{{ACTION_CODE}}">
		<div class="c_title" ontap="$(this).next().toggle();">
			<div class="text e_strong e_blue">{{ACTION_NAME}}</div>
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
			<div class="c_line c_line-dashed"></div>
			<div class="c_list">
				<ul tag="UNFINISH_TITLE">
					<li class="" ontap="">
						<div class="content">
							<div class="main">
								<div class="title e_strong">未完成客户</div>
							</div>
							<div class="side link" action_code="{{ACTION_CODE}}" ontap="planSummarize.batchOperTextOnClick(this)" tag="singleOper">
								<span class="text">批量操作</span>
							</div>
							<div class="side link" tag="batchOper" action_code="{{ACTION_CODE}}"
								 ontap="checkedAll('{{ACTION_CODE}}_custCheckBox', true)"
								 style="display: none">
								<span class="text">全选</span>
							</div>
							<div class="side link" tag="batchOper" action_code="{{ACTION_CODE}}"
								 ontap="planSummarize.selectUnEntryCauseCusts('{{ACTION_CODE}}')"
								 style="display: none">
								<span class="text">未填原因的客户</span>
							</div>
							<div class="side link" tag="batchOper" action_code="{{ACTION_CODE}}"
								 ontap="planSummarize.batchSummarize(this)"
								 style="display: none">
								<span class="text">填写原因</span>
							</div>
							<div class="side link" tag="batchOper"
								 action_code="{{ACTION_CODE}}" ontap="planSummarize.cancelBatchOper($(this).attr('action_code'))"
								 style="display: none">
								<span class="text">取消</span>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="c_list c_list-v c_list-col-3">
				<ul tag="UNFINISH_CUST_LIST">
					{{each UNFINISH_CUST_LIST cust idx}}
					<li tag="UNFINISH_{{cust.CUST_ID}}" cust_id="{{cust.CUST_ID}}"
						action_code="{{ACTION_CODE}}" oper_code="{{OPER_CODE}}"
						action_id="{{cust.ACTION_ID}}" li_type="unFinish"
						unfinish_cause_id="{{cust.UNFINISH_CAUSE_ID}}"
						unfinish_cause_desc="{{cust.UNFINISH_CAUSE_DESC}}"
						<%--class="link"--%>
						<%--ontap="planSummarize.summarize(this)"--%>
					>
						<div class="main">
							<div class="title link" tag="singleOper"
								 action_code="{{ACTION_CODE}}"
								 cust_id="{{cust.CUST_ID}}"
								 ontap="planSummarize.summarize(this)">
								<span class="e_red">{{cust.CUST_NAME}}</span>
								<span class="e_ico-edit"></span>
							</div>
							<label class="title group link" style="display: none" tag="batchOper">
								<span class="e_red">{{cust.CUST_NAME}}</span>
								<input type="checkbox" name="{{ACTION_CODE}}_custCheckBox" value="{{cust.CUST_ID}}">
							</label>
							<div class="content">
								<span class="e_tag e_tag-s e_tag-red" tag="unfinish_cause_desc">{{cust.UNFINISH_CAUSE_DESC}}</span>
								<%--<span class="e_tag e_tag-s e_tag-red" tag="unfinish_cause_desc" style="white-space:pre-wrap">{{cust.UNFINISH_CAUSE_DESC}}</span>--%>
							</div>
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
		action_code="{{ACTION_CODE}}" oper_code="{{OPER_CODE}}"
		action_id="{{cust.ACTION_ID}}" li_type="unFinish"
		unfinish_cause_id="{{cust.UNFINISH_CAUSE_ID}}"
		unfinish_cause_desc="{{cust.UNFINISH_CAUSE_DESC}}"
		<%--class="link"--%>
		<%--ontap="planSummarize.summarize(this)">--%>
	>
		<div class="main">
			<div class="title link" tag="singleOper" ontap="planSummarize.summarize(this)"
				 action_code="{{ACTION_CODE}}"
				 cust_id="{{cust.CUST_ID}}">
				<span class="e_red">{{cust.CUST_NAME}}</span>
				<span class="e_ico-edit"></span>
			</div>
			<label class="title group link" style="display: none" tag="batchOper">
				<span class="e_red">{{cust.CUST_NAME}}</span>
				<input type="checkbox" name="{{ACTION_CODE}}_custCheckBox" value="{{cust.CUST_ID}}">
			</label>
			<div class="content">
				<span class="e_tag e_tag-s e_tag-red" tag="unfinish_cause_desc">{{cust.UNFINISH_CAUSE_DESC}}</span>
			</div>
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
	<li class="link" ontap="planSummarize.showCustEditPopup(this)" cust_id = {{cust.CUST_ID}}
		tag="li_cust_content" oper_code="0" is_must="{{cust.IS_MUST}}" cust_name="{{cust.CUST_NAME}}"
	>
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
<script id="scatter_houses_template" rel_id="SCATTER_HOUSES" type="text/html">
	{{each HOUSES_LIST houses idx}}
	<li class="link e_center" houses_name="{{houses.NAME}}" houses_id="{{houses.HOUSES_ID}}"
		ontap="scatterHousesPopup.clickHouses(this)">
		<label class="group">
			<div class="main">{{houses.NAME}}</div>
		</label>
	</li>
	{{/each}}
</script>
<script type="text/javascript">
	Wade.setRatio();
    planSummarize.init();
</script>
</body>
</html>