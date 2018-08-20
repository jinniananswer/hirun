<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/5/4
  Time: 16:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>员工权限分配</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/personnel/assign.permission.detail.js?v=20180820165300"></script>
</head>
<body>
<div class="c_header">
    <div class="back" ontap="$.redirect.closeCurrentPage();">员工权限分配</div>
    <div class="fn">

    </div>
</div>
<!-- 滚动开始 -->
<div class="c_scroll c_scroll-float c_scroll-header" id="scroll_div">
    <div class="c_list c_list-line c_list-border c_list-space l_padding">
        <ul id="employee">

        </ul>
        <input type="hidden" id="ADD_FUNCS" name="ADD_FUNCS" desc="添加的权限" />
        <input type="hidden" id="DEL_FUNCS" name="DEL_FUNCS" desc="删除的权限" />
        <input type="hidden" name="EMPLOYEE_ID" id="EMPLOYEE_ID" nullable="no" value="${pageContext.request.getParameter("EMPLOYEE_ID") }" desc="员工ID/" />
        <input type="hidden" name="USER_ID" id="USER_ID" nullable="no" value="${pageContext.request.getParameter("USER_ID") }" desc="员工ID/" />
    </div>

    <div class="l_padding">
        <div class="c_title">
            <div class="text">已有权限</div>
            <div class="fn">
                <ul>
                    <li class="link" ontap="$.user.initAddFunc();"><span class="e_ico-add"></span><span class="e_strong">新增权限</span></li>
                </ul>
            </div>
        </div>
        <div class="c_list c_list-col-2 c_list-line c_list-border c_list-fixWrapSpace">
            <ul id="user_funcs">

            </ul>
        </div>
        <div class="c_space"></div>
        <div class="c_submit c_submit-full">
            <button class="e_button-r e_button-l e_button-green" type="button" ontap="$.user.submit();">提交</button>
        </div>

    </div>
</div>
<!-- 滚动 结束 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-popup-rest-func">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">请选择查询条件</div>
                    </div>
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="searchArea">
                                    <li>
                                        <div class="value">
                                                <span class="e_mix">
                                                    <input id="SEARCH_TEXT" name="SEARCH_TEXT" type="text" placeholder="请输入权限名称（模糊搜索）" nullable="no" desc="查询条件"/>
                                                    <button type="button" class="e_button-blue" ontap="$.user.queryRestFuncs();"><span class="e_ico-search"></span><span>查询</span></button>
                                                </span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        </div>
                    </div>
                    <div class="c_scroll">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="rest_funcs">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT" name="SUBMIT" ontap="$.user.confirmAdd();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.user.init();
</script>
</body>
</html>
