<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %> 
<html size="s"> 
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
	<title>hi-run</title>
	<link rel="stylesheet" href="frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" href="frame/css/project.css" rel="stylesheet" type="text/css"/>
	<script src="frame/TouchUI/content/js/jcl-base.js"></script>
	<script src="frame/TouchUI/content/js/jcl.js"></script>
	<script src="frame/TouchUI/content/js/i18n/code.zh_CN.js"></script>
	<script src="frame/TouchUI/content/js/jcl-plugins.js"></script>
	<script src="frame/TouchUI/content/js/jcl-ui.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/popup.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/segment.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/base/switch.js"></script>
	<script src="frame/TouchUI/content/js/ui/component/tabset/tabset.js"></script>
	<script src="frame/TouchUI/content/js/local.js"></script>
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
						<form name="loginForm" method="post" action="/loginPost">
							<div class="c_space"></div>
							<!-- 表单 开始 -->
							<div class="c_form c_form-label-3 c_form-col-1">
								<ul>
									<li>
										<div class="label">用户名：</div>
										<div class="value">
											<input id="username" name="username" type="text" value="SUPERUSR"/>
										</div>
									</li>
									<li>
										<div class="label">密　码：</div>
										<div class="value">
											<input id="password" name="password"  type="password" value="lc" />
										</div>
									</li>
								</ul>
							</div>
							<!-- 表单 结束 -->
							<div class="c_space-2"></div>
							<!-- 提交 开始 -->
							<div class="c_submit c_submit-full">
								<button id="login_btn" type="submit" class="e_button-l e_button-green">登陆</button>
							</div>
							<!-- 提交 结束 -->
							<div class="c_space-2"></div>
							<div class="e_right p_loginLinks">
								<a href="#nogo">忘记密码？</a>
								<a href="#nogo">免费注册</a>
							</div>
						</form>
					</div>
				</div>
			</div>
			<!-- 悬浮层 结束 -->
		</div>
		<div class="copyright">
			Copyright © 2017 m.o.s.t. All rights reserved.
		</div>
	</div>
	<script>
		Wade.setRatio();
	</script>
</body>
</html>