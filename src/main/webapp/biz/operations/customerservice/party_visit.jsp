<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户清理申请</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/party.visit.js?v=20190925"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">客户回访</div>
    <div class="fn">

    </div>

</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text">基本信息</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">没有找到信息哦~~</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="preworks">

            </ul>
        </div>

        <div class="c_title" ontap="$(this).next().toggle();">
            <div class="text">回访记录</div>
            <div class="fn">
			    <ul><li><span id="visitCount"></span><span class="e_ico-unfold"></span></li></ul>
            </div>
        </div>

        <div class="c_list c_list-line c_list-border c_list-space l_padding" style="display:none">
            <ul id="partyVisit">

            </ul>
        </div>

        <div class="c_title">
            <div class="text">新增回访</div>
            <div class="fn">

            </div>
        </div>

        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="addPartyVisit">

              <input type="hidden" id="PARTY_ID" name="PARTY_ID" nullable="no" value="${pageContext.request.getParameter("PARTY_ID") }" desc="参与人ID" />
              <input type="hidden" id="PROJECT_ID" name="PROJECT_ID" nullable="no" value="${pageContext.request.getParameter("PROJECT_ID") }" desc="参与人ID" />

                <li class="link required">
                    <div class="label">回访对象：</div>
                    <div class="value">
                        <input type="text" id="VISIT_OBJECT" name="VISIT_OBJECT" placeholder="请填写回访对象" nullable="no" desc="回访对象" />
                    </div>
                </li>
                 <li class="link required">
                     <div class="label">回访类型</div>
                     <div class="value">
                         <span id="visittype"></span>
                     </div>
                 </li>
                <li class="link required">
                    <div class="label">回访方式：</div>
                    <div class="value">
                        <input type="text" name="VISIT_WAY" id="VISIT_WAY" placeholder="请填写回访方式" nullable="no" desc="请填写回访方式"></textarea>
                    </div>
                </li>
                <li class="link required">
                    <div class="label">回访时间：</div>
         			<div class="value">
         				<span class="e_mix">
         					<input type="text" id="VISIT_TIME" name="VISIT_TIME" datatype="date" nullable="no" desc="回访时间" readonly="true"/>
         					<span class="e_ico-date"></span>
         				</span>
         			</div>
                </li>
                <li class="link required">
                    <div class="label">回访内容：</div>
                    <div class="value">
                        <textarea id="VISIT_CONTENT" name="VISIT_CONTENT" placeholder="请填写回访内容" nullable="no" desc="回访内容" class="e_textarea-row-2"></textarea>
                    </div>
                </li>
            </ul>
        </div>

        <div class="c_submit c_submit-full">
            <button type="button" id="submitButton" class="e_button-r e_button-l e_button-green" ontap="$.partyvisit.submit()">提交</button>
        </div>

        <div class="c_space-4"></div>
        <div class="c_space-4"></div>
    </div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.partyvisit.init();
</script>
</body>
</html>
