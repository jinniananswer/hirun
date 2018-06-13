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
</head>
<body>
<div class="c_tab c_tab-avg c_tab-full c_tab-nav"
     style="height:4em;position: absolute;bottom:0"
     id="myTab">
    <div class="tab">
        <div class="list">
            <ul>
                <li ontap="baseButtom.openHomePage()">
                    <div class="ico"><span class="e_ico-home"></span></div>
                    <div class="text">首页</div>
                </li>
                <li>
                    <div class="ico"><span class="e_ico-cart"></span></div>
                    <div class="text">数据中心</div>
                </li>
                <li>
                    <div class="ico"><span class="e_ico-guide"></span></div>
                    <div class="text">运营中心</div>
                </li>
                <li>
                    <div class="ico"><span class="e_ico-user"></span></div>
                    <div class="text">我的</div>
                </li>
            </ul>
        </div>
    </div>
</div>
<script type="text/javascript">
    var baseButtom = {
        openHomePage : function() {
            if($.os.phone) {
                $.redirect.open('phone/phone_index.jsp','首页')
            } else {
                $.redirect.open('home.jsp','首页')
            }
        }
    }
</script>
</body>
</html>