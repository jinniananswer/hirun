<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>HIRUN</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/home.js"></script>
</head>
<div class="c_scroll c_scroll-white">
    <div class="l_padding-3">
        <div class="l_float" style="top:2em; left:2.14em; right:2.14em; bottom:0;">
            <div class="c_tab c_tab-full c_tab-sp" id="homeTab">
                <div class="tab">
                    <div class="list">
                        <ul></ul>
                    </div>
                </div>
                <div class="page">
                    <div class="content" title="功能列表(其中部分功能待完善)">
                        <!-- 分列 开始 -->
                        <div class="l_col l_col-space-1">


                            <div class="l_colItem"  id="menus">
                                <!-- 列表 结束 -->
                                <div class="c_space"></div>
                            </div>
                        </div>
                        <!-- 分列 结束 -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 标签页 结束 -->
<script>
    Wade.setRatio();
    window["homeTab"] = new Wade.Tabset("homeTab", {slidable:false});
</script>

</body>

</html>