<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/11/13
  Time: 下午9:47
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>岗前课堂学习资料</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/query.exam.guide.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">考试指南</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 标题栏 结束 -->

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
                <ul id="exams">

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
                                    <div class="label">资料名称</div>
                                    <div class="value">
                                        <input type="text" id="FILE_NAME" name="FILE_NAME" placeholder="请输入文件名（模糊搜索）" nullable="yes" desc="资料名称" />
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                        <div class="c_line"></div>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.exam.query();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->
<script>
    $.exam.init();
    Wade.setRatio();
</script>
</body>
</html>
