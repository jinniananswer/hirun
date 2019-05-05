<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/12/31
  Time: 3:53 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>报名详情</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/train/sign.list.js?v=20190505"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">培训查看</div>
    <div class="fn">
        <input type="hidden" name="TRAIN_ID" id="TRAIN_ID" nullable="no" value="${pageContext.request.getParameter("TRAIN_ID") }" desc="培训ID" />
    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text" id="total_num">报名人员列表</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">还没有人报名哦~~</div>
                    </div>
                </div>
            </div>
        </div>
        <div id="sign_list">

        </div>

        <div class="c_space"></div>
        <div class="c_submit c_submit-full">
            <button type="button" id="ADD_BUTTON" name="ADD_BUTTON"  class="e_button-r e_button-l e_button-green" ontap="showPopup('UI-popup','UI-popup-query-cond')">新增报名人员</button>
            <button type="button" id="DELETE_BUTTON" name="DELETE_BUTTON"  class="e_button-r e_button-l e_button-red" ontap="$.train.deleteSignedEmployee();">删除报名人员</button>
            <button type="button" id="END_SIGN_BUTTON" name="END_SIGN_BUTTON" class="e_button-r e_button-l e_button-green" ontap="$.train.endSign();">生成正式报名名单</button>
            <button type="button" id="EXPORT_SIGN_BUTTON" name="EXPORT_SIGN_BUTTON" class="e_button-r e_button-l e_button-navy" ontap="$.train.export();" style="display:none">导出正式报名名单</button>
        </div>
        <div class="c_space-4"></div>
        <div class="c_space-4"></div>
    </div>
</div>
<!-- 滚动 结束 -->
<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-popup-query-cond">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">请选择查询条件</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="queryArea">
                                <li class="link">
                                    <div class="label">姓名</div>
                                    <div class="value"><input id="NAME" name="NAME" type="text" nullable="yes" desc="姓名"/></div>
                                </li>
                                <li class="link" ontap="$('#ORG_TEXT').focus();$('#ORG_TEXT').blur();forwardPopup('UI-popup','UI-ORG')">
                                    <div class="label">所属部门</div>
                                    <div class="value">
                                        <input type="text" id="ORG_TEXT" name="ORG_TEXT" nullable="yes" readonly="true" desc="所属部门" />
                                        <input type="hidden" id="ORG_ID" name="ORG_ID" nullable="yes" desc="所属部门" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                                <li class="link" ontap="$('#JOB_TEXT').focus();$('#JOB_TEXT').blur();forwardPopup('UI-popup','UI-JOB')">
                                    <div class="label">岗位</div>
                                    <div class="value">
                                        <input type="text" id="JOB_TEXT" name="JOB_TEXT" nullable="yes" readonly="true" desc="岗位" />
                                        <input type="hidden" id="JOB_ROLE" name="JOB_ROLE" nullable="yes" desc="岗位" />
                                    </div>
                                    <div class="more"></div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_submit c_submit-full">
                            <button type="button" name="SUBMIT_QUERY" ontap="$.train.query();" class="e_button-l e_button-navy">查询</button>
                        </div>
                        <div class="c_msg" id="messagebox_employee">
                            <div class="wrapper">
                                <div class="emote"></div>
                                <div class="info">
                                    <div class="text">
                                        <div class="title">提示信息</div>
                                        <div class="content">请输入查询条件查询要报名的人员~~</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="c_list c_list-line c_list-border c_list-space l_padding">
                            <ul id="select_employees">

                            </ul>
                        </div>
                    </div>

                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.train.confirmSelectEmployee();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-ORG">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择部门</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_box">
                            <div class="l_padding">
                                <div id="orgTree" class="c_tree"></div>
                            </div>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-JOB">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择岗位</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="jobArea">
                                    <li>
                                        <div class="value">
                                                <span class="e_mix">
                                                    <input id="JOB_SEARCH_TEXT" name="JOB_SEARCH_TEXT" type="text" placeholder="请输入岗位名称（模糊搜索）" nullable="no" desc="查询条件"/>
                                                    <button type="button" class="e_button-blue" ontap="$.train.queryJobs();"><span class="e_ico-search"></span><span>查询</span></button>
                                                </span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        </div>
                    </div>
                    <!-- 滚动（替换为 java 组件） 开始 -->
                    <div class="c_scroll">
                        <div class="l_padding">
                            <div class="c_box">
                                <div class="c_list c_list-line">
                                    <ul id="jobRoles">

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

                </div>
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.train.init();
</script>
</body>
</html>
