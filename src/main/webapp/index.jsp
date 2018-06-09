<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>hi-run</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/index.js"></script>
</head>
<body>
<div class="p_index">
    <div class="side">
        <div class="user">
            <div class="c_list c_list-v">
                <ul>
                    <li>
                        <div class="pic"><img id="HEAD_IMAGE" name="HEAD_IMAGE" class="e_pic-r" src="frame/img/male.png" alt="" /></div>
                        <div class="main">
                            <div class="title" id="USER_NAME" name="USER_NAME"></div>
                            <div class="content" id="ORG_NAME" name="ORG_NAME"></div>
                            <div class="content" id="JOB_ROLE_NAME" name="JOB_ROLE_NAME"></div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="menu">
            <!-- 列表 开始 -->
            <div class="c_list c_list-s">
                <ul>
                    <li class="link" title="密码变更" ontap="$.index.openNav('/biz/organization/personnel/change_password.jsp','密码变更');">
                        <div class="main">密码变更</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="我的档案" ontap="">
                        <div class="main">我的档案</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="系统帮助" ontap="">
                        <div class="main">系统帮助</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="我的消息" ontap="$.redirect.open('/biz/common/msglist_query.jsp','我的消息');$('#myMsg').html(0)">
                        <div class="main">
                            我的消息
                            <span class="e_ico-pic-red e_ico-pic-xxxs e_ico-pic-r" id="myMsg">0</span>
                        </div>
                        <div class="more"></div>
                    </li>
                </ul>
            </div>
            <!-- 列表 结束 -->
        </div>
    </div>
    <div class="main">
        <div class="task">
            <ul id="page_titles">
                <li class="on"  title="首页" onclick="$.index.switchPage('首页')">
                    <div class="text"><span class="e_ico-home"></span></div>
                </li>
            </ul>
        </div>
        <div id="page_contents" class="content">
            <iframe src="home.jsp" title="首页" frameborder="0"></iframe>
        </div>
    </div>
</div>
<script>
    $.index.init();

    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        websocket = new WebSocket("ws://localhost:8080/websocketServer/633");
    }
    else{
        alert('Not support websocket');
    }

    //连接发生错误的回调方法
    websocket.onerror = function(){
        setMessageInnerHTML("error");
    };

    //连接成功建立的回调方法
    websocket.onopen = function(event){
        setMessageInnerHTML("open");
    };

    //接收到消息的回调方法
    websocket.onmessage = function(event){
        var msg = event.data;
        var num = parseInt($('#myMsg').html());
        $('#myMsg').html(num+1);
    };

    //连接关闭的回调方法
    websocket.onclose = function(){
        setMessageInnerHTML("close");
    };

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    };

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        console.log(innerHTML);
    }

    //关闭连接
    function closeWebSocket(){
        websocket.close();
    }

    //发送消息
    function send(msg){
        var json = {};
        json.CONTENT = msg;
        json.TYPE = "CLIENT";
        websocket.send(JSON.stringify(json));
    }
</script>
</body>
</html>