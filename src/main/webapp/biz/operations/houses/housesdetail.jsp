<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>Always Online Office</title>
    <jsp:include page="/common.jsp"></jsp:include>
</head>
<body>
<div class="l_query">
    <div class="l_queryFn">
        <!-- 功能 开始 -->
        <div class="c_fn">
            <div class="left">
                <button type="button" onclick="showPopup('editPopup')"><span class="e_ico-add"></span><span>新增规划楼盘</span></button>
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
                    <th class="e_center"><span>状态</span></th>
                    <th class="e_center"><span>楼盘名称</span></th>
                    <th class="e_center"><span>归属分公司</span></th>
                    <th class="e_center"><span>归属店面</span></th>
                    <th class="e_center"><span>楼盘性质</span></th>
                    <th class="e_center"><span>家装顾问</span></th>
                    <th class="e_center"><span>总户数</span></th>
                    <th class="e_center"><span>交房时间</span></th>

                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="e_center">2018-01-02</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">中国铁建山语城</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">袁家岭店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">宁艳城</td>
                    <td class="e_center">700</td>
                    <td class="e_center">2018-3-30</td>
                </tr>
                <tr>
                    <td class="e_center">2018-01-20</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">辉煌国际城</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">袁家岭店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">宁艳城、唐德文、唐耿华</td>
                    <td class="e_center">720</td>
                    <td class="e_center">2018-04-20</td>
                </tr>
                <tr>
                    <td class="e_center">2018-01-18</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">鸿涛翡翠湾</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">袁家岭店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">宁艳城</td>
                    <td class="e_center">48</td>
                    <td class="e_center">2018-04-18</td>
                </tr>
                <tr>
                    <td class="e_center">2018-02-20</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">美联天骄城四期</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">袁家岭店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">龙飞</td>
                    <td class="e_center">69</td>
                    <td class="e_center">2018-05-20</td>
                </tr>
                <tr>
                    <td class="e_center">2018-02-20</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">东云台</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">袁家岭店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">唐球、刘云枫、刘爱爱</td>
                    <td class="e_center">69</td>
                    <td class="e_center">2018-05-20</td>
                </tr>
                <tr>
                    <td class="e_center">2018-04-15</td>
                    <td class="e_center">未进驻</td>
                    <td class="e_center">爵世名邸</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">袁家岭店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">待定</td>
                    <td class="e_center">1000</td>
                    <td class="e_center">2018-03-19</td>
                </tr>
                <tr>
                    <td class="e_center">2017-12-18</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">半山壹号二期</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">新芙蓉店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">彭静、彭杰</td>
                    <td class="e_center">800</td>
                    <td class="e_center">2018-05-30</td>
                </tr>
                <tr>
                    <td class="e_center">2017-12-12</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">湘熙水郡</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">新芙蓉店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">刘祖林、丁珊</td>
                    <td class="e_center">600</td>
                    <td class="e_center">2018-06-01</td>
                </tr>
                <tr>
                    <td class="e_center">2018-11-08</td>
                    <td class="e_center">未进驻</td>
                    <td class="e_center">达美西湖湾三期</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">新芙蓉店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">陈博航</td>
                    <td class="e_center">1166</td>
                    <td class="e_center">2018-06-30</td>
                </tr>
                <tr>
                    <td class="e_center">2017-10-30</td>
                    <td class="e_center">已进驻</td>
                    <td class="e_center">梅岭国际</td>
                    <td class="e_center">长沙鸿扬</td>
                    <td class="e_center">河西店</td>
                    <td class="e_center">重点期盘</td>
                    <td class="e_center">田敏</td>
                    <td class="e_center">240</td>
                    <td class="e_center">2018-01-30</td>
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
<div class="c_popup" id="popup">
    <div class="c_popupBg" onclick="hidePopup('popup')"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem c_popupItem-show">
                    <div class="c_header">
                        <div class="back" onclick="hidePopup('popup')">查询条件</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_scrollContent l_padding">
                            <!-- 查询表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul>
                                    <li>
                                        <div class="label">归属分公司</div>
                                        <div class="value">
                                            <span class="e_select">长沙鸿扬</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="label">归属店面</div>
                                        <div class="value">
                                            <span class="e_select">袁家岭店</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="label">楼盘名称（模糊匹配）</div>
                                        <div class="value">
                                            <input type="text" />
                                        </div>
                                    </li>
                                    <li class="link">
                                        <div class="label">状态</div>
                                        <div class="value">
                                            <span class="e_select">未进驻</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="label">楼盘性质</div>
                                        <div class="value">
                                            <span class="e_select">重点期盘</span>
                                        </div>
                                    </li>
                                    <li>
                                        <div class="label">家装顾问</div>
                                        <div class="value">
                                            <input type="text" />
                                        </div>
                                        <div class="more"></div>
                                    </li>
                                </ul>
                            </div>
                            <!-- 查询表单 结束 -->
                            <div class="c_space"></div>
                            <div class="c_submit c_submit-full"><button class="e_button-l e_button-r e_button-blue" type="button" >查询</button></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 查询 结束 -->
<script>
    Wade.setRatio();
    if(get("UI-loading")){ hide(parent.get("UI-loading"));}
    // setTimeout(function(){
    // 	get("UI-scroller").scrollTop = get("UI-scroller").scrollHeight;
    // },100)
    window["popup"] = new Wade.Popup("popup");
</script>
</body>
</html>