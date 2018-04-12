<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>Always Online Office</title>
    <link rel="stylesheet" href="frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="frame/css/project.css" rel="stylesheet" type="text/css"/>
    <script src="frame/TouchUI/content/js/jcl-base.js"></script>
    <script src="frame/TouchUI/content/js/jcl.js"></script>
    <script src="frame/TouchUI/content/js/i18n/code.zh_CN.js"></script>
    <script src="frame/TouchUI/content/js/jcl-plugins.js"></script>
    <script src="frame/TouchUI/content/js/jcl-ui.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/popup.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/segment.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/switch.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/tabset/tabset.js"></script>
    <script src="frame/TouchUI/content/js/local.js"></script>
    <script src="scripts/index.js"></script>
</head>
<body>
<div class="p_index">
    <div class="side">
        <div class="user">
            <div class="c_list c_list-v">
                <ul>
                    <li>
                        <div class="pic"><img class="e_pic-r" src="frame/img/staff.jpg" alt="" /></div>
                        <div class="main">
                            <div class="title">薛帅</div>
                            <div class="content">人力资源部经理</div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="menu">
            <!-- 列表 开始 -->
            <div class="c_list c_list-s">
                <ul>
                    <li class="link" title="楼盘规划" ontap="$.index.openNav('biz/operations/houses/housesdetail.jsp','楼盘规划详情')">
                        <div class="main">楼盘规划</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="楼盘分配" ontap="$.index.openNav('biz/operations/houses/housecounselor.jsp','楼盘分配家装顾问情况查询')">
                        <div class="main">楼盘分配</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="楼盘规划报表" ontap="$.index.openNav('biz/datacenter/houses/planreport-store.jsp','重点楼盘计划数与实际分配数的对比')">
                        <div class="main">楼盘规划报表</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="楼盘分配情况报表" ontap="$.index.openNav('biz/datacenter/houses/planreport-counselor-all.jsp','家装顾问缺口数报表')">
                        <div class="main">楼盘分配情况报表</div>
                        <div class="more"></div>
                    </li>
                    <li class="link" title="家装顾问人员缺口报表" ontap="$.index.openNav('biz/operations/houses/housestarget.jsp','开发咨询目标占比数与实际数据对比')">
                        <div class="main">家装顾问人员缺口报表</div>
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
</body>
</html>