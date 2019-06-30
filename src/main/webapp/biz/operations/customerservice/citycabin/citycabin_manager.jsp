<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>培训查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="scripts/biz/customerservice/citycabin/citycabin.manager.js?v=20190508"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header">
    <div class="back" ontap="$.redirect.closeCurrentPage();">城市木屋管理</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','UI-popup-query-cond')"><span class="e_ico-search"></span></button>
    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div id="tip" class="c_tip">请点击标题栏右侧的放大镜，选择查询条件来进行查询</div>

    <div class="l_padding">
        <div class="c_title">
            <div class="text"></div>
            <div class="fn">
                <ul>
                    <li ontap="$.redirect.open('biz/operations/customerservice/citycabin/create_citycabin.jsp', '新增城市木屋信息');"><span class="e_ico-add"></span>新增城市木屋</li>
                </ul>
            </div>
        </div>


        <div class="c_msg c_msg-warn" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content">没有找到城市木屋信息~~</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="c_list c_list-line c_list-border c_list-space l_padding">
            <ul id="citycabins">

            </ul>
        </div>

    </div>
    <div class="c_space-4"></div>
    <div class="c_space-4"></div>
</div>
<!-- 滚动 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>

        <div class="c_popup" id="UI-popup">
            <div class="c_popupBg" id="UI-popup_bg"></div>
            <div class="c_popupBox">
                <div class="c_popupWrapper" id="UI-popup_wrapper">
                    <div class="c_popupGroup">
                        <div class="c_popupItem" id="UI-popup-query-cond">
                             <div class="c_header">
                                 <div class="back" ontap="hidePopup(this)">请选择查询条件</div>
                             </div>
                             <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                                 <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                                     <ul id="queryArea">
                                         <li class="link">
                                             <div class="label">楼盘地址</div>
                                             <div class="value"><input id="HOUSEADDRESS" name="HOUSEADDRESS" type="text" nullable="yes"  desc="楼盘地址"/></div>
                                         </li>
                                         <li class="link">
                                             <div class="label">归属门店</div>
                                             <div class="value"><input id="SHOP" name="SHOP" type="text" nullable="yes"  desc="归属门店"/></div>
                                         </li>
                                         <li class="link">
                                             <div class="label">主题系列</div>
                                             <div class="value"><input id="STYLE" name="STYLE" type="text" nullable="yes"  desc="主题系列"/></div>
                                         </li>
                                         <li class="link">
                                             <div class="label">客户姓名</div>
                                             <div class="value"><input id="CUST_NAME" name="CUST_NAME" type="text" nullable="yes"  desc="客户姓名"/></div>
                                         </li>
                                         <li class="link">
                                             <div class="label">是否有效</div>
                                             <div class="value">
                                                 <span id="sexcontainer"></span>
                                             </div>
                                         </li>
                                     </ul>
                                 </div>
                             </div>
                             <div class="l_bottom">
                                 <div class="c_submit c_submit-full">
                                     <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.citybain.query();" class="e_button-l e_button-green">确定</button>
                                 </div>
                             </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


<script>
    Wade.setRatio();
    $.citybain.init();
</script>
</body>
</html>
