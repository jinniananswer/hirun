<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>密码修改</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/personnel/reset.password.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">重置员工密码</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="l_padding">
    <div class="c_box">
        <!-- 表单 开始 -->
        <div class="c_list c_list-form">
            <ul id="submitArea">
                <li>
                    <div class="value">
                        <span class="e_mix">
                            <input id="SEARCH_TEXT" name="SEARCH_TEXT" type="text" placeholder="请输入员工姓名（模糊搜索）" nullable="no" desc="查询条件"/>
                            <button type="button" class="e_button-blue" ontap="$.employee.query();"><span class="e_ico-search"></span><span>查询</span></button>
                        </span>
                    </div>
                </li>
            </ul>
        </div>
        <!-- 表单 结束 -->
    </div>

</div>

<div class="c_scroll">
    <div class="l_padding">
        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">sorry,没有找到您想要的信息~</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="c_box">
            <div class="c_list c_list-line">
                <ul id="employees">

                </ul>
            </div>
        </div>
    </div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
    <div class="c_space-3"></div>
</div>

<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>

<script>
    Wade.setRatio();
    window["UI-popup"] = new Wade.Popup("UI-popup");
</script>
</body>
</html>