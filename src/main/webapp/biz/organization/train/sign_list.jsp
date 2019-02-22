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
    <script src="/scripts/biz/organization/train/sign.list.js?v=20190222"></script>
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
            <button type="button" id="DELETE_BUTTON" name="DELETE_BUTTON"  class="e_button-r e_button-l e_button-red" ontap="$.train.deleteSignedEmployee();">删除报名人员</button>
            <button type="button" id="END_SIGN_BUTTON" name="END_SIGN_BUTTON" class="e_button-r e_button-l e_button-green" ontap="$.train.endSign();">生成正式报名名单</button>
        </div>
    </div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.train.init();
</script>
</body>
</html>
