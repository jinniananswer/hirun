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
	<title>客户查询</title>
	<script src="scripts/biz/popup/select.cust.js"></script>
</head>
<body>
<div id="HOUSE_ID_float" class="c_float">
	<div class="bg"></div>
	<div class="content">
		<div class="c_scrollContent">
			<div class="c_list c_list-pc-s c_list-phone-line ">
				<ul>

				</ul>
			</div>
		</div>
	</div>
</div>
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
								<div class="left" id="ADD_CUST_BUTTON">
									<button class="" type="button" ontap="selectCust.showCustEdit(this)">
										<span class="e_ico-add"></span>新增客户
									</button>
								</div>
							</div>
						</div>
						<div class="c_space"></div>
						<!-- 客户列表 开始 -->
							<div class="c_list c_list c_list-col-1 c_list-phone-col-1">
								<ul id="CUST_LIST">
									<li x_tag="x-databind-template" id={custId} style="display:none">
										<label class="group link">
											<div class="content">
												<div class="fn"><input name="selectCustBox" value={CUST_ID} type="checkbox" /></div>
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
										<div class="button">
											<button type="button" class="e_button-s e_button-blue e_button-r"
													custId={CUST_ID} ontap="selectCust.showCustEdit(this)">
												编辑
											</button>
										</div>
									</li>
								</ul>
							</div>
							<!-- 客户列表 结束 -->
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
							<button type="button" class="e_button-l e_button-green" ontap="selectCust.queryCust(this)">查询</button>
						</div>
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
										<input type="text" id="CUST_NAME" name="CUST_NAME"/>
										<input type="text" id="CUST_ID" name="CUST_ID" style="display: none"/>
									</div>
								</li>
								<li class="required">
									<div class="label">微信昵称</div>
									<div class="value">
										<input type="text" id="WX_NICK" name="WX_NICK"/>
									</div>
								</li>
								<li class="required">
									<div class="label">性别</div>
									<div class="value">
										<div class="e_switch">
											<div class="e_switchOn">男</div>
											<div class="e_switchOff">女</div>
											<input type="hidden" id="SEX" name="SEX"/>
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
										<span class="e_select">
											<span>--请选择--</span>
											<input type="hidden" id="HOUSE_ID" name="HOUSE_ID" value="" nullable="yes" desc="楼盘" />
										</span>
									</div>
								</li>
								<li class="required">
									<div class="label">楼栋号</div>
									<div class="value">
										<input type="text" id="HOUSE_DETAIL" name="HOUSE_DETAIL"/>
									</div>
								</li>
								<li class="required">
									<div class="label">户型</div>
									<div class="value">
										<input type="text" id="HOUSE_MODE" name="HOUSE_MODE"/>
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
							<button type="button" class="e_button-l e_button-green" ontap="selectCust.submitCustInfo(this)">提交</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 弹出层 结束 -->
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
		<div class="button">
			<button type="button" class="e_button-s e_button-blue e_button-r"
					custId={CUST_ID} ontap="selectCust.showCustEdit(this)">
				编辑
			</button>
		</div>
	</li>
</script>
<script type="text/javascript">
    selectCust.init();
</script>

</body>
</html>