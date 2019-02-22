<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/1/25
  Time: 11:32 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>培训查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/train/my.train.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">我的培训档案</div>
    <div class="fn">

    </div>
</div>
<!-- 标题栏 结束 -->
<div class="c_scroll c_scroll-float c_scroll-header c_scroll-white">
    <div class="l_padding">
        <div class="c_list l_padding">
            <ul>
                <li>
                    <div class="pic">
                        <span class="e_pic-img-r e_pic-img-r-l"><img id="employee_pic" src="/frame/img/male.png" alt="" /></span>
                    </div>
                    <div class="main">
                        <div class="title" id="employee_name"></div>
                    </div>
                    <div class="side">
                        <span id="employee_level" class="e_star e_star-gold">

                        </span>
                    </div>
                </li>
            </ul>
        </div>
        <div class="c_line"></div>
        <div class="c_tab c_tab-avg c_tab-sub c_tab-home" id="myTab">
            <div class="tab">
                <div class="list">
                    <ul></ul>
                </div>
            </div>
            <div class="page">
                <div class="content" title="近期培训">
                    <div class="c_msg c_msg-sweat" id="current_message" style="display:none">
                        <div class="wrapper">
                            <div class="emote"></div>
                            <div class="info">
                                <div class="text">
                                    <div class="title">提示信息</div>
                                    <div class="content">没有找到近期有培训信息哦~~</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="c_list">
                        <ul id="current">

                        </ul>
                    </div>
                </div>
                <div class="content" title="往期培训">
                    <div class="c_msg c_msg-sweat" id="history_message" style="display:none">
                        <div class="wrapper">
                            <div class="emote"></div>
                            <div class="info">
                                <div class="text">
                                    <div class="title">提示信息</div>
                                    <div class="content">没有找到往期培训信息哦~~</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="c_list">
                        <ul id="history">

                        </ul>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="c_space-4"></div>
    <div class="c_space-4"></div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.train.init();
</script>
</body>
</html>