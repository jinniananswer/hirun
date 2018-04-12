<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>Always Online Office</title>
    <link rel="stylesheet" href="/frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="/frame/css/dmpManager.css" rel="stylesheet" type="text/css"/>
    <script src="/frame/TouchUI/content/js/jcl-base.js"></script>
    <script src="/frame/TouchUI/content/js/jcl.js"></script>
    <script src="/frame/TouchUI/content/js/i18n/code.zh_CN.js"></script>
    <script src="/frame/TouchUI/content/js/jcl-plugins.js"></script>
    <script src="/frame/TouchUI/content/js/jcl-ui.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/base/popup.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/base/segment.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/base/switch.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/tabset/tabset.js"></script>
    <script src="/frame/TouchUI/content/js/ui/component/chart/echarts.js"></script>
    <script src="/frame/TouchUI/content/js/local.js"></script>
    <script src="/frame/TouchUI/content/js/code.js"></script>
</head>
<body>
<div class="l_query">
    <div class="l_queryFn">
        <!-- 功能 开始 -->
        <div class="c_fn">
            <div class="left">
                <button type="button" onclick="showPopup('editPopup')"><span class="e_ico-add"></span><span>楼盘分配</span></button>
            </div>
            <div class="right">
                <div class="e_mix" onclick="showPopup('popup')">
                    <input type="text" />
                    <button class="e_button-blue" type="button"><span class="e_ico-search"></span></button>
                </div>
            </div>
        </div>
        <!-- 功能 结束 -->
    </div>
    <div class="l_queryResult">
        <!-- 表格 开始 -->
        <div class="c_table">
            <table>
                <thead>
                <tr>
                    <th class="e_center"><span>进入时间</span></th>
                    <th class="e_center"><span>楼盘名称</span></th>
                    <th class="e_center"><span>归属分公司</span></th>
                    <th class="e_center"><span>归属门店</span></th>
                    <th class="e_center"><span>分配状态</span></th>
                    <th class="e_center"><span>计划分配家装顾问数</span></th>
                    <th class="e_center"><span>实际分配家装顾问数</span></th>
                    <th class="e_center"><span>计划与实际差额</span></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="e_center">待定</td>
                    <td class="e_center">麓阳和景</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">河西店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">待定</td>
                    <td class="e_center">南山雍江汇</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">河西店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-03-31</td>
                    <td class="e_center">润和紫郡</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">金星店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-03-31</td>
                    <td class="e_center">山与墅（别墅）</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">金星店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-03-31</td>
                    <td class="e_center">玛丽的花园一期</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">金星店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-04-15</td>
                    <td class="e_center">盈峰翠邸（别墅）</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">金星店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-04-15</td>
                    <td class="e_center">龙湾</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">优仕馆</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-03-30</td>
                    <td class="e_center">北辰中央公园</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">新芙蓉店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-03-30</td>
                    <td class="e_center">逸景华庭</td>
                    <td class="e_center">株洲鸿扬</td>
                    <td class="e_center">攸县店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                <tr>
                    <td class="e_center">2018-03-30</td>
                    <td class="e_center">新伟未来城</td>
                    <td class="e_center">株洲鸿扬</td>
                    <td class="e_center">攸县店</td>
                    <td class="e_center">未分配</td>
                    <td class="e_center">1</td>
                    <td class="e_center">0</td>
                    <td class="e_center">-1</td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- 表格 结束 -->
    </div>
    <div class="l_queryPage">
        <!-- 分页 开始 -->
        <div class="c_page c_page-border">
            <div class="more"><span class="e_ico-fold"></span></div>
            <div class="turn">
                <div class="first"><span class="e_ico-first"></span></div>
                <div class="prev">上一页</div>
                <div class="page">10/23</div>
                <div class="next">下一页</div>
                <div class="last"><span class="e_ico-last"></span></div>
            </div>
        </div>
        <!-- 分页 结束 -->
    </div>
</div>
</body>
</html>