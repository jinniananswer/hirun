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
	<script src="scripts/biz/popup/select.cust.js"></script>
</head>
<body>
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
							</div>
						</div>
						<div class="c_list">
						<!-- 客户列表 开始 -->
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
							<!-- 客户列表 结束 -->
							<div class="c_space"></div>
							<div class="c_submit c_submit-full">
								<button type="button" class="e_button-l e_button-green" ontap="selectCust.confirmCusts(this)">确定</button>
							</div>
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
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
    selectCust.init();
</script>
</body>
</html>