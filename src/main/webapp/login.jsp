<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %> 
<html size="s"> 
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
	<title>hi-run</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<style>
		.c_tooltip { z-index:99999 !important; }
	</style>
	<script src="scripts/login.js"></script>
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
											<input type="text" id="username" name="username" datatype="text" nullable="no" desc="用户名" />
										</div>
									</li>
									<li>
										<div class="label">密　码：</div>
										<div class="value">
											<input type="password" id="password" name="password" datatype="text" nullable="no" desc="密码" />
										</div>
									</li>
								</ul>
							</div>
							<!-- 表单 结束 -->
							<div class="c_space-2"></div>
							<!-- 提交 开始 -->
							<div class="c_submit c_submit-full">
								<button id="login_btn" type="button" class="e_button-l e_button-green" ontap="$.login.verifyLogin();">登陆</button>
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
		Wade.setRatio();
	</script>
</body>
</html>