<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;
%>
<html size="s">
<head>
    <base href="<%=basePath%>"></base>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户新增</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/cust/cust.add.js"></script>
</head>
<body>
<div id="HOUSE_ID_float" class="c_float">
    <div class="bg"></div>
    <div class="content">
        <div class="c_scrollContent">
            <div class="c_list c_list-pc-s c_list-phone-line ">
                <ul>

                </ul>
            </div>
        </div>
    </div>
</div>
<div class="l_edit">
    <div class="c_header e_show-phone">
        <div class="back" ontap="closeNav();">客户新增</div>
    </div>
    <span class="l_editMain">
        <div class="c_space"></div>
		<div class="c_list c_list-form" id="custForm">
			<ul>
				<li class="required">
                    <div class="label">客户姓名</div>
                    <div class="value">
                        <input type="text" id="CUST_NAME" name="CUST_NAME"/>
                        <input type="text" id="CUST_ID" name="CUST_ID" style="display: none"/>
                    </div>
				</li>
                <li class="required">
                    <div class="label">微信昵称</div>
                    <div class="value">
                        <input type="text" id="WX_NICK" name="WX_NICK"/>
                    </div>
				</li>
                <li class="required">
                    <div class="label">性别</div>
                    <div class="value">
                        <div class="e_switch">
                            <div class="e_switchOn">男</div>
                            <div class="e_switchOff">女</div>
                            <input type="hidden" id="SEX" name="SEX"/>
                        </div>
                    </div>
				</li>
                <li>
                    <div class="label">电话号码</div>
                    <div class="value">
                        <input type="text" id="MOBILE_NO"/>
                    </div>
				</li>
                <li class="required">
                    <div class="label">楼盘</div>
                    <div class="value">
                        <span class="e_select">
                            <span>--请选择--</span>
                            <input type="hidden" id="HOUSE_ID" name="HOUSE_ID" value="" nullable="yes" desc="楼盘" />
                        </span>
                    </div>
				</li>
                <li class="required">
                    <div class="label">楼栋号</div>
                    <div class="value">
                        <input type="text" id="HOUSE_DETAIL" name="HOUSE_DETAIL"/>
                    </div>
				</li>
                <li class="required">
                    <div class="label">户型</div>
                    <div class="value">
                        <input type="text" id="HOUSE_MODE" name="HOUSE_MODE"/>
                    </div>
				</li>
                <li>
                    <div class="label">面积</div>
                    <div class="value">
                        <span class="e_mix">
                            <input type="text" id="HOUSE_AREA" name="HOUSE_AREA"/>
                            <span class="e_label"><span>平方</span></span>
                        </span>
                    </div>
				</li>
			</ul>
		</div>
		<div class="c_space"></div>
		<div class="c_submit c_submit-full">
			<button class="e_button-r e_button-l e_button-green" type="button" ontap="custAdd.submit()">提交</button>
		</div>
	</span>
    <span class="l_editPlace"></span>
</div>
<!-- 弹出层 结束 -->
<script type="text/template" id="SELECTED_CUST">
    <li>
        <div class="main">
            <div class="title">{actionName}：{num}</div>
            <div class="content">{custName}</div>
        </div>
    </li>
</script>


<script type="text/javascript">
    Wade.setRatio();
    custAdd.init();
</script>
</body>
</html>