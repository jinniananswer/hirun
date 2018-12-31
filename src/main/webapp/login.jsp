<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;
%>
<html size="s"> 
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
	<base href="<%=basePath%>"></base>
	<link rel="stylesheet" href="frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" href="frame/css/project.css" rel="stylesheet" type="text/css"/>
	<script src="frame/TouchUI/content/js/jcl-base.js"></script>
	<script src="frame/TouchUI/content/js/jcl.js"></script>
	<script src="frame/TouchUI/content/js/i18n/code.zh_CN.js"></script>
	<script src="frame/TouchUI/content/js/jcl-plugins.js"></script>
	<script src="frame/TouchUI/content/js/jcl-ui.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/popup.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/frame.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/segment.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/switch.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/select.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/tabset/tabset.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/box/tipbox.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/box/popupbox.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/datefield.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/calendar/calendar.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/increasereduce.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/ipaddressfield.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/chart/echarts.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/table/table.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/tree/tree.js"></script>
	<script src="frame/TouchUI/content/js/local.js"></script>
	<script src="frame/js/ajax.js"></script>
	<script src="frame/js/redirect.js"></script>
	<script src="frame/js/date.js"></script>
	<script src="frame/TouchUI/content/js/plugins/template/template.js"></script>
	<title>hi-run</title>
	<%--<jsp:include page="/common.jsp"></jsp:include>--%>
	<style>
		.c_tooltip { z-index:99999 !important; }
		.c_msg-full{ z-index:99999 !important; }
	</style>
	<script src="scripts/login.js?v=20190101071000"></script>
</head>
<body>
	<div class="p_login">
		<div class="logo"><strong>HI-RUN</strong> LOGIN</div>
		<div class="pannel">
			<!-- 悬浮层 开始 -->
			<div class="c_float c_float-show">
				<div class="bg"></div>
				<div class="content">
					<div class="l_padding l_padding-n">
						<div class="c_list">
							<ul>
								<li>
									<div class="main">
										<div class="title">登录</div>
									</div>
								</li>
							</ul>
						</div>
					</div>
					<div class="l_padding-3 l_padding-u">
						<form name="loginForm" id="loginForm" method="post" action="">
							<div class="c_space"></div>
							<!-- 表单 开始 -->
							<div class="c_form c_form-label-3 c_form-col-1">
								<ul>
									<li>
										<div class="label">用户名：</div>
										<div class="value">
											<input type="text" id="username" name="username" placeholder="请输入手机号码" datatype="text" nullable="no" desc="用户名" />
										</div>
									</li>
									<li>
										<div class="label">密　码：</div>
										<div class="value">
											<input type="password" id="password" name="password" placeholder="请输入密码" datatype="text" nullable="no" desc="密码" />
										</div>
									</li>
								</ul>
							</div>
							<!-- 表单 结束 -->
							<div class="c_space-2"></div>
							<!-- 提交 开始 -->
							<div class="c_submit c_submit-full">
								<button id="login_btn" name="login_btn" type="button" class="e_button-l e_button-green" ontap="$.login.verifyLogin();">登陆</button>
							</div>
							<div class="c_space-2"></div>
							<div class="e_right p_loginLinks">
								<a href="phone/download.html">手机APP下载</a>
							</div>
							<!-- 提交 结束 -->
							<!--
							<div class="c_space-2"></div>
							<div class="e_right p_loginLinks">

							</div>
							-->
						</form>
					</div>
				</div>
			</div>
			<!-- 悬浮层 结束 -->
		</div>
		<div class="copyright">
			Copyright © 2018 m.o.s.t. All rights reserved.
		</div>
	</div>
	<script>
        $.login.init();
		Wade.setRatio();
	</script>
</body>
</html>