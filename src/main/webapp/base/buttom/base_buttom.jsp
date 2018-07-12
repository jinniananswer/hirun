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
     style="height:4em;position: absolute;bottom:0; display: none;"
     id="myTab" tag="buttomPhone">
    <div class="tab">
        <div class="list">
            <ul>
                <li ontap="baseButtom.open('phone/phone_index.jsp','首页')">
                    <div class="ico"><span class="e_ico-home"></span></div>
                    <div class="text">首页</div>
                </li>
                <li ontap="baseButtom.open('biz/common/msglist_query.jsp','我的消息')">
                    <div class="ico"><span class="e_ico-msg"></span></div>
                    <div class="text">我的消息</div>
                </li>
                <li ontap="baseButtom.open('phone/toolbox.jsp','工具箱')">
                    <div class="ico"><span class="e_ico-config"></span></div>
                    <div class="text">工具箱</div>
                </li>

            </ul>
        </div>
    </div>
</div>
<script type="text/javascript">
    var baseButtom = {
        init : function() {
            if($.os.phone) {
                $('div[tag=buttomPhone]').show();
            }
        },
        open : function(url, title) {
            if($.os.phone) {
                $.redirect.topOpen(url,title);
            } else {
                $.redirect.open(url,title);
            }
        }
    }
    baseButtom.init();
</script>
</body>
</html>