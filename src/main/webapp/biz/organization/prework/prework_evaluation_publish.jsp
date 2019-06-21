<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/1/21
  Time: 11:01 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>发布岗前考评通知</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/prework/prework.evaluation.publish.js"></script>
</head>
<body>
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">发布岗前考评通知</div>
    <div class="fn">

    </div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
    <div class="l_padding" id="allSubmitArea">
        <div class="c_title">
            <div class="text">通知内容设置</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
            <ul id="trainArea">
                <li class="link required">
                    <div class="label">岗前考评通知名称</div>
                    <div class="value">
                        <input type="text" id="NAME" name="NAME" placeholder="请输入岗前考评通知名称" nullable="no" desc="岗前考评通知名称" />
                    </div>
                </li>

                <li class="link required">
                    <div class="label">岗前考评地址</div>
                    <div class="value">
                        <input type="text" id="TRAIN_ADDRESS" name="TRAIN_ADDRESS" value="兴业银行大厦（原六都国际综合大楼 长沙市雨花区东塘街道芙蓉中路三段567号）21楼会议室" placeholder="请输入培训地址" nullable="no" desc="培训地址" />
                    </div>
                </li>
                <li class="link required">
                    <div class="label">考评开始时间</div>
                    <div class="value">
                        <span class="e_mix">
                            <input type="text" id="START_DATE" name="START_DATE" datatype="date" nullable="no" readonly="true" desc="培训开始时间" />
                            <span class="e_ico-date"></span>
                        </span>
                    </div>
                </li>
                <li class="link required">
                    <div class="label">考评结束时间</div>
                    <div class="value">
                        <span class="e_mix">
                            <input type="text" id="END_DATE" name="END_DATE" datatype="date" nullable="no" readonly="true" desc="培训结束时间" />
                        </span>
                    </div>
                </li>
                <li class="link required">
                    <div class="label">报名截止时间</div>
                    <div class="value">
                        <span class="e_mix">
                            <input type="text" id="SIGN_END_DATE" name="SIGN_END_DATE" datatype="date" nullable="no" readonly="true" desc="培训结束时间" />
                            <span class="e_ico-date"></span>
                        </span>

                    </div>
                </li>
            </ul>
        </div>
        <div class="c_space-2"></div>

        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.train.submit()">提交</button>
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