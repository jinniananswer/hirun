<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>新增城市木屋信息</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/citycabin/change.citycabin.js?v=20190412"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">新增城市木屋信息</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div id="UI-other">
            <!-- 表单 开始 -->
            <div class="c_list c_list-form">
                <ul id="submitArea">
                    <li class="link required">
                        <div class="label">归属店面</div>
                        <div class="value"><input id="SHOP" name="SHOP" type="text" nullable="no" desc="归属店面"/></div>
                        <input type="hidden" id="CITY_CABIN_ID" name="CITY_CABIN_ID" nullable="no" value="${pageContext.request.getParameter("CITY_CABIN_ID") }" desc="城市木屋ID" />
                    </li>
                    <li class="required link">
                        <div class="label">客户姓名</div>
                        <div class="value"><input id="CUSTNAME" name="CUSTNAME" type="text"   nullable="no" desc="客户姓名"/></div>
                    </li>
                    <li class="required link">
                        <div class="label">楼盘地址</div>
                        <div class="value"><input id="HOUSE_ADDRESS" name="HOUSE_ADDRESS" type="text"  nullable="no" desc="楼盘地址"/></div>
                    </li>
                    <li class="required link">
                        <div class="label">栋号</div>
                        <div class="value"><input id="HOUSE_BUILDING" name="HOUSE_BUILDING" type="text"   nullable="no" desc="栋号"/></div>
                        <div class="label">栋</div>

                    </li>
                    <li class="required link">
                        <div class="label">房号</div>
                        <div class="value"><input id="HOUSE_ROOM" name="HOUSE_ROOM" type="text"  nullable="no" desc="房号"/></div>
                    </li>

                    <li class="link required">
                        <div class="label">主题系列</div>
                        <div class="value"><input id="STYLE" name="STYLE" type="text" nullable="no" desc="主题系列"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">面积</div>
                        <div class="value"><input id="HOUSE_AREA" name="HOUSE_AREA" type="text" nullable="no" desc="面积"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">设计师</div>
                        <div class="value"><input id="DESINGER" name="DESINGER" type="text" nullable="no" desc="设计师"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">客户代表</div>
                        <div class="value"><input id="CUSTOMERSERVICE" name="CUSTOMERSERVICE" type="text" nullable="no" desc="客户代表"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">项目经理</div>
                        <div class="value"><input id="PROJECT_MANAGER" name="PROJECT_MANAGER" type="text" nullable="no" desc="项目经理"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">造价</div>
                        <div class="value"><input id="COSTS" name="COSTS" type="text" nullable="no" desc="造价"/></div>
                    </li>
                    <li class="link required">
                        <div class="label">参观联系人</div>
                        <div class="value"><input id="CONTACT" name="CONTACT" type="text" nullable="no" desc="参观联系人"/></div>
                    </li>
         			<li>
                         <div class="label required">可参观开始时间</div>
         					<div class="value">
         						<span class="e_mix">
         							<input type="text" id="COND_START_DATE" name="COND_START_DATE" datatype="date" nullable="no" desc="可参观开始时间" readonly="true"/>
         								<span class="e_ico-date"></span>
         					    </span>
         					</div>
         			</li>
         			<li>
                         <div class="label required">可参观结束时间</div>
         					<div class="value">
         						<span class="e_mix">
         							<input type="text" id="COND_END_DATE" name="COND_END_DATE" datatype="date" nullable="no" desc="可参观结束时间" readonly="true"/>
         								<span class="e_ico-date"></span>
         					    </span>
         					</div>
         			</li>
                    <li class="link required">
                        <div class="label">是否有PPT介绍</div>
                        <div class="value"><input id="IS_PPT" name="IS_PPT" type="text" nullable="no" desc="是否有PPT介绍"/></div>
                    </li>
                    <li class="link">
                        <div class="label">备注</div>
                        <div class="value"><input id="REMARK" name="REMARK" type="text" nullable="yes" desc="备注"/></div>
                    </li>
                </ul>
            </div>
            <!-- 表单 结束 -->
        </div>
        <div class="c_space"></div>
        <!-- 提交 开始 -->
        <div class="c_submit c_submit-full">
            <button type="button" class="e_button-r e_button-l e_button-green" ontap="$.changeCityCabin.submit()">提交</button>
        </div>
        <!-- 提交 结束 -->
        <div class="c_space"></div>

    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>


<!-- 弹窗 结束 -->

<script>
    Wade.setRatio();
    $.changeCityCabin.init();
</script>
</body>
</html>