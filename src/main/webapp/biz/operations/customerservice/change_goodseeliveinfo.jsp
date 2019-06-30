<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户需求信息录入</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/change.goodseeliveinfo.js?v=20190419"></script>
</head>
<body>
<div class="c_header e_show">
    <div class="back" ontap="$.changegoodseeliveinfo.backToFlow();">客户需求信息录入</div>
    <div class="fn">

    </div>
</div>

<div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
          <div class="c_space"></div>
          <div class="c_space"></div>
          <div class="c_guide">
          	<ul>
          		<li class="on" id="guidebase"><span>基本信息</span></li>
          		<li id="guideintention"><span>需求意向</span></li>
          		<li id="guidereceive"><span>客户接待</span></li>
          	</ul>
          </div>


    <div class="l_padding" id="allSubmitArea">
    <input type="hidden" id="PARTY_ID" name="PARTY_ID" nullable="no" value="${pageContext.request.getParameter("PARTY_ID") }" desc="参与人ID" />
    <input type="hidden" id="PROJECT_ID" name="PROJECT_ID" nullable="no" value="${pageContext.request.getParameter("PROJECT_ID") }" desc="项目ID" />
       <!--基本信息开始-->
       <div id="baseinfo">
        <div class="c_title">
            <div class="text">基本信息</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
            <ul >
                <li class="link required">
                    <div class="label re">客户姓名</div>
                    <div class="value">
                        <input type="text" id="NAME" name="NAME" placeholder="请输入客户姓名" nullable="no"  desc="客户姓名" />
                    </div>
                </li>

               <li class="link required">
                                <div class="label">电话号码</div>
                                <div class="value">
                                    <input type="text" id="CONTACT" name="CONTACT" placeholder="" nullable="no" desc="电话号码" />
                                </div>
                </li>

               <li class="link required">
                                <div class="label">装修地点</div>
                                <div class="value">
                                    <input type="text" id="FIX_PLACE" name="FIX_PLACE" placeholder="" nullable="no" desc="装修地点" />
                                </div>
                </li>
               <li class="link ">
                                <div class="label">QQ号码</div>
                                <div class="value">
                                    <input type="text" id="QQCONTACT" name="QQCONTACT" placeholder="" nullable="yes" desc="QQ号码" />
                                </div>
                </li>

               <li class="link ">
                                <div class="label">微信号码</div>
                                <div class="value">
                                    <input type="text" id="WXCONTACT" name="WXCONTACT" placeholder="" nullable="yes" desc="微信号码" />
                                </div>
                </li>

               <li class="link required">
                                <div class="label">户型</div>
                                <div class="value">
                                    <input type="text" id="HOUSEKIND" name="HOUSEKIND" placeholder="" nullable="no" desc="户型" />
                                </div>
                </li>

               <li class="link required">
                                <div class="label">面积</div>
                                <div class="value">
                                    <input type="text" id="AREA" name="AREA" placeholder="" nullable="no" desc="面积" />
                                </div>
                </li>


            </ul>
        </div>
        <div id="">
            <div class="c_title">
                <div class="text">生活形态</div>
                <div class="fn" >

                </div>
            </div>
            <div class="c_box">
                <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                    <ul id="LIFESHAPEAREA">
                        <li class="link ">
                                <div class="label">年龄</div>
                                <div class="value">
                                    <input type="text" id="AGE" name="AGE" placeholder="" nullable="yes" desc="年龄" />
                                </div>
                        </li>

                        <li class="link ">
                                <div class="label">学历</div>
                                <div class="value">
                                    <input type="text" id="EDUCATE" name="EDUCATE" placeholder="" nullable="yes" desc="学历" />
                                </div>
                         </li>

                         <li class="link ">
                                 <div class="label">工作单位（职业）</div>
                                 <div class="value">
                                     <input type="text" id="COMPANY" name="COMPANY" placeholder="" nullable="yes" desc="工作单位" />
                                 </div>
                        </li>

                         <li class="link ">
                                 <div class="label">常驻人口</div>
                                 <div class="value">
                                     <input type="text" id="PEOPLE_COUNT" name="PEOPLE_COUNT" placeholder="" nullable="yes" desc="常驻人口" />
                                 </div>
                                 <div class="label">人</div>
                        </li>

                         <li class="link " ontap="$('#ELDER_TEXT').focus();$('#ELDER_TEXT').blur();showPopup('UI-popup','UI-ELDERINFO');">
                                 <div class="label">老人</div>
                                 <div class="value">
                                     <input type="text" id="ELDER_TEXT" name="ELDER_TEXT" readonly="true" placeholder="" nullable="yes" desc="老人" />
                                     <input type="hidden" id="ELDER_MAN" name="ELDER_MAN" placeholder="" nullable="yes" desc="男老人" />
                                     <input type="hidden" id="ELDER_WOMAN" name="ELDER_WOMAN" placeholder="" nullable="yes" desc="女老人" />
                                 </div>
                                 <div class="more"></div>
                        </li>

                         <li class="link " ontap="$('#CHILD_TEXT').focus();$('#CHILD_TEXT').blur();showPopup('UI-popup','UI-CHILDINFO');">
                                 <div class="label">小孩</div>
                                 <div class="value">
                                     <input type="text" id="CHILD_TEXT" name="CHILD_TEXT" placeholder="" readonly="true" nullable="yes" desc="小孩" />
                                     <input type="hidden" id="CHILD_BOY" name="CHILD_BOY" placeholder="" nullable="yes" desc="小男孩" />
                                     <input type="hidden" id="CHILD_GIRL" name="CHILD_GIRL" placeholder="" nullable="yes" desc="小女孩" />
                                 </div>
                                 <div class="more"></div>
                        </li>

                         <li class="link " ontap="$('#HOBBY_TEXT').focus();$('#HOBBY_TEXT').blur();showPopup('UI-popup','UI-HOBBYLIST');">
                                 <div class="label">个人爱好</div>
                                 <div class="value">
                                     <input type="text" id="HOBBY_TEXT" name="HOBBY_TEXT" readonly="true" placeholder="" nullable="yes" desc="兴趣爱好" />
                                     <input type="hidden" id="HOBBY" name="HOBBY" placeholder="" nullable="yes" desc="兴趣爱好" />
                                 </div>
                                 <div class="more"></div>
                        </li>
                         <li class="link " >
                                 <div class="label">其他个人爱好</div>
                                 <div class="value">
                                     <input type="text" id="OTHER_HOBBY" name="OTHER_HOBBY" placeholder="" nullable="yes" desc="兴趣爱好" />
                                 </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
       </div>
       <!--基本信息结束-->
       <!--客户意向信息开始-->
        <div id="custintention">
		<div id="">
                <div class="c_title">
                   <div class="text">28大设计主题</div>
                      <div class="fn" >

                    </div>
                    </div>
                    <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                <li class="link " ontap="$('#CHINESESTYLE').focus();$('#CHINESESTYLE').blur();showPopup('UI-popup','UI-CHINESSSTYLE');">
                                        <div class="label">中国风骨</div>
                                        <div class="value">
                                            <input type="text" id="CHINESESTYLE_TEXT"  name="CHINESESTYLE_TEXT" readonly="true" placeholder="" nullable="yes" desc="中国风骨" />
                                            <input type="hidden" id="CHINESESTYLE" name="CHINESESTYLE" placeholder="" nullable="yes" desc="中国风骨" />
                                        </div>
                                         <div class="more"></div>
                                </li>

                                <li class="link " ontap="$('#EUROPEANCLASSICS').focus();$('#EUROPEANCLASSICS').blur();showPopup('UI-popup','UI-EUROPEANCLASSICS');">
                                        <div class="label">欧洲经典</div>
                                        <div class="value">
                                            <input type="text" id="EUROPEANCLASSICS_TEXT" name="EUROPEANCLASSICS_TEXT" placeholder="" readonly="true" nullable="yes" desc="欧洲经典" />
                                            <input type="hidden" id="EUROPEANCLASSICS" name="EUROPEANCLASSICS" placeholder="" nullable="yes" desc="欧洲经典" />
                                        </div>
                                         <div class="more"></div>
                                 </li>

                                 <li class="link " ontap="$('#MODERNSOURCE').focus();$('#MODERNSOURCE').blur();showPopup('UI-popup','UI-MODERNSOURCE');">
                                         <div class="label">现代之源</div>
                                         <div class="value">
                                             <input type="text" id="MODERNSOURCE_TEXT" name="MODERNSOURCE_TEXT" placeholder="" readonly="true" nullable="yes" desc="现代之源" />
                                             <input type="hidden" id="MODERNSOURCE" name="MODERNSOURCE" placeholder="" nullable="yes" desc="现代之源" />
                                         </div>
                                         <div class="more"></div>
                                </li>

                                 <li class="link " >
                                         <div class="label">其他要求</div>
                                         <div class="value">
                                             <input type="text" id="OTHER_TOPIC_REQ" name="OTHER_TOPIC_REQ" placeholder="" nullable="yes" desc="兴趣爱好" />
                                         </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

		<div id="">
                <div class="c_title">
                   <div class="text">15大功能系统</div>
                      <div class="fn" >

                    </div>
                    </div>
                    <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                <li class="link " ontap="$('#FUNC').focus();$('#FUNC').blur();showPopup('UI-popup','UI-FUNCSYSTEM');">
                                        <div class="label">功能系统</div>
                                        <div class="value">
                                            <input type="text" id="FUNCS_TEXT" name="FUNCS_TEXT" readonly="true" placeholder="" nullable="yes" desc="功能系统" />
                                            <input type="hidden" id="FUNC" name="FUNC" placeholder="" nullable="yes" desc="功能系统" />
                                        </div>
                                         <div class="more"></div>
                                </li>

                                 <li class="link " >
                                         <div class="label">已生成</div>
                                         <div class="value">
                                             <input type="text" id="BULEPRINT" name="BULEPRINT" placeholder="" nullable="yes" desc="" />
                                         </div>
                                         <div class="label">功能蓝图</div>
                                </li>

                                 <li class="link " >
                                         <div class="label">特殊要求</div>
                                         <div class="value">
                                             <input type="text" id="FUNC_SPEC_REQ" name="FUNC_SPEC_REQ" placeholder="" nullable="yes" desc="特殊要求" />
                                         </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

        <div id="">
            <div class="c_title">
                <div class="text">价格区间</div>
                <div class="fn" >

                </div>
            </div>
            <div class="c_box">
                <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                    <ul id="">
                        <li class="link ">
                                <div class="label">客户投资计划</div>
                                <div class="value">
                                    <input type="text" id="TOTALPRICEPLAN" name="TOTALPRICEPLAN" placeholder="" nullable="yes" desc="投资计划" />
                                </div>
                                <div class="label">万入住</div>
                        </li>

                        <li class="link ">
                                <div class="label">基础部分+木制品</div>
                                <div class="value">
                                    <input type="text" id="BASICANDWOODPRICEPLAN" name="BASICANDWOODPRICEPLAN" placeholder="" nullable="yes" desc="学历" />
                                </div>
                                <div class="label">万元</div>
                         </li>

                         <li class="link ">
                                 <div class="label">暖通及设备</div>
                                 <div class="value">
                                     <input type="text" id="HVACPRICEPLAN" name="HVACPRICEPLAN" placeholder="" nullable="yes" desc="工作单位" />
                                 </div>
                                <div class="label">万元</div>
                        </li>

                         <li class="link ">
                                 <div class="label">主材</div>
                                 <div class="value">
                                     <input type="text" id="MATERIALPRICEPLAN" name="MATERIALPRICEPLAN" placeholder="" nullable="yes" desc="常驻人口" />
                                 </div>
                                <div class="label">万元</div>
                        </li>

                         <li class="link " >
                                 <div class="label">移动家具</div>
                                 <div class="value">
                                     <input type="text" id="FURNITUREPRICEPLAN" name="FURNITUREPRICEPLAN" placeholder="" nullable="yes" desc="常驻人口" />
                                 </div>
                                <div class="label">万元</div>
                        </li>

                         <li class="link " >
                                 <div class="label">电器</div>
                                 <div class="value">
                                     <input type="text" id="ELECTRICALPRICEPLAN" name="ELECTRICALPRICEPLAN" placeholder="" nullable="yes" desc="小孩" />
                                 </div>
                                <div class="label">万元</div>
                        </li>

                    </ul>
                </div>
            </div>
        </div>

        </div>
       <!--客户意向信息结束-->
       <!--接待记录开始-->
        <div id="receiveinfo">
		<div id="">
                <div class="c_title">
                   <div class="text">优势介绍</div>
                      <div class="fn" >

                    </div>
                    </div>
                    <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                <li class="link " ontap="$('#ADVANTAGE').focus();$('#ADVANTAGE').blur();showPopup('UI-popup','UI-ADVANTAGE');">
                                        <div class="label">优势介绍</div>
                                        <div class="value">
                                            <input type="text" id="ADVANTAGE_TEXT" name="ADVANTAGE_TEXT" readonly="true" placeholder="" nullable="yes" desc="优势介绍" />
                                            <input type="hidden" id="ADVANTAGE" name="ADVANTAGE" placeholder="" nullable="yes" desc="优势介绍" />
                                        </div>
                                         <div class="more"></div>
                                </li>

                            </ul>
                        </div>
                    </div>
                </div>



		<div id="">
                <div class="c_title">
                   <div class="text">接待记录</div>
                      <div class="fn" >

                    </div>
                    </div>
                    <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                <li class="link " ontap="$('#CRITICALPROCESS').focus();$('#CRITICALPROCESS').blur();showPopup('UI-popup','UI-CRITICALPROCESS');">
                                        <div class="label">关键流程介绍</div>
                                        <div class="value">
                                            <input type="text" id="CRITICALPROCESS_TEXT" name="CRITICALPROCESS_TEXT" readonly="true" placeholder="" nullable="yes" desc="关键流程介绍" />
                                            <input type="hidden" id="CRITICALPROCESS" name="CRITICALPROCESS" placeholder="" nullable="yes" desc="关键流程介绍" />
                                        </div>
                                         <div class="more"></div>
                                </li>
                                <li class="link ">
                                        <div class="label">去过的家装公司和家具卖场、感兴趣的主材品牌:</div>
                                        <div class="value">
                                            <textarea class="e_textarea-row-2" id="OTHER_INFO" name="OTHER_INFO" placeholder="" nullable="yes" desc="兴趣品牌"></textarea>
                                        </div>
                                </li>
                                <li class="link " >
                                        <div class="label">计划入住时间</div>
                                        <div class="value">
                                             <span class="e_mix">
                                             <input type="text" id="PLAN_LIVE_TIME" name="PLAN_LIVE_TIME" datatype="date" nullable="yes" readonly="true" desc="计划入住时间" />
                                             <span class="e_ico-date"></span>
                                             </span>
                                       </div>
                                </li>
                                <li class="link ">
                                        <div class="label">城市木屋体验</div>
                                        <div class="value">
                                             <span class="e_mix">
                                             <input type="text" id="MW_EXPERIENCE_TIME" name="MW_EXPERIENCE_TIME" datatype="date" nullable="yes" readonly="true" desc="城市木屋体验" />
                                             <span class="e_ico-date"></span>
                                             </span>
                                       </div>
                                </li>
                                <li class="link required" >
                                        <div class="label">是否观看了宣传片</div>
                                        <div class="value">
                                            <input type="text" id="ISSCANVIDEO" name="ISSCANVIDEO" placeholder="" nullable="no" desc="是否观看宣传片" />
                                        </div>
                                </li>
                                <li class="link " >
                                        <div class="label">是否参加公司展厅</div>
                                        <div class="value">
                                            <input type="text" id="ISSCANSHOWROOM" name="ISSCANSHOWROOM" placeholder="" nullable="yes" desc="是否参加公司展厅" />
                                        </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

		<div id="">
                <div class="c_title">
                   <div class="text">信息来源</div>
                      <div class="fn" >

                    </div>
                    </div>
                    <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                <li class="link " >
                                        <div class="label">家装顾问</div>
                                        <div class="value">
                                            <input type="text" id="COUNSELOR_NAME" name="COUNSELOR_NAME" placeholder="" nullable="yes" desc="家装顾问" />
                                        </div>
                                </li>
                                <li class="link " ontap="$('#INFORMATIONSOURCE').focus();$('#INFORMATIONSOURCE').blur();showPopup('UI-popup','UI-INFORMATIONSOURCE');">
                                        <div class="label">信息来源</div>
                                        <div class="value">
                                            <input type="text" id="INFORMATIONSOURCE_TEXT" name="INFORMATIONSOURCE_TEXT" readonly="true" placeholder="" nullable="yes" desc="信息来源" />
                                            <input type="hidden" id="INFORMATIONSOURCE" name="INFORMATIONSOURCE" placeholder="" nullable="yes" desc="信息来源" />
                                        </div>
                                         <div class="more"></div>
                                </li>
                                <li class="link " >
                                        <div class="label">其他信息来源</div>
                                        <div class="value">
                                            <input type="text" id="OTHER_SOURCE" name="OTHER_SOURCE" placeholder="" nullable="yes" desc="其他信息来源" />
                                        </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

		<div id="">
                <div class="c_title">
                   <div class="text">时间</div>
                      <div class="fn" >

                    </div>
                    </div>
                    <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                <li class="link " >
                                        <div class="label">量房</div>
                                        <div class="value">
                                             <span class="e_mix">
                                             <input type="text" id="GAUGE_HOUSE_TIME" name="GAUGE_HOUSE_TIME" datatype="date" nullable="yes" readonly="true" desc="量房" />
                                             <span class="e_ico-date"></span>
                                             </span>
                                       </div>
                                </li>
                                <li class="link ">
                                        <div class="label">直接出平面</div>
                                        <div class="value">
                                             <span class="e_mix">
                                             <input type="text" id="OFFER_PLANE_TIME" name="OFFER_PLANE_TIME" datatype="date" nullable="yes" readonly="true" desc="直接出平面" />
                                             <span class="e_ico-date"></span>
                                             </span>
                                       </div>
                                </li>
                                <li class="link ">
                                        <div class="label">再联系</div>
                                        <div class="value">
                                             <span class="e_mix">
                                             <input type="text" id="CONTACT_TIME" name="CONTACT_TIME" datatype="date" nullable="yes" readonly="true" desc="再联系" />
                                             <span class="e_ico-date"></span>
                                             </span>
                                       </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
          </div>
          <!--接待信息结束-->
          <div class="c_space-3"></div>

        <div class="c_submit c_submit-full">
            <button type="button" id="PREVIOUS_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.changegoodseeliveinfo.previous()">上一页</button>
            <button type="button" id="CONFIRM_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.changegoodseeliveinfo.changeGoodSeeLiveInfo()">提交</button>
            <button type="button" id="NEXT_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.changegoodseeliveinfo.next()">下一页</button>
        </div>

    </div>
</div>
<!-- 滚动 结束 -->



<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                                    <!-- 老人信息弹出浮层 -->
                <div class="c_popupItem" id="UI-ELDERINFO">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">老人信息</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                             <ul id="elderinfolist">
                                <li class="link">
                                    <div class="label">男:</div>
                                    <div class="value"><input id="OLDMANCOUNT" name="OLDMANCOUNT" type="text" nullable="yes" desc="老男人"/></div>
                                </li>
                                <li class="link">
                                    <div class="label">女:</div>
                                    <div class="value"><input id="OLDWOMANCOUNT" name="OLDWOMANCOUNT" type="text" nullable="yes" desc="老女人"/></div>
                                </li>

                             </ul>
                        </div>
                        <div class="l_bottom">
                            <div class="c_submit c_submit-full">
                                <button type="button" ontap="$.changegoodseeliveinfo.confirmElderinfo();" class="e_button-l e_button-green">确定</button>
                            </div>
                        </div>
                    </div>
                </div>
                        <!-- 小孩信息弹出浮层 -->
                <div class="c_popupItem" id="UI-CHILDINFO">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">小孩信息</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                             <ul id="childinfolist">
                                <li class="link">
                                    <div class="label">男:</div>
                                    <div class="value"><input id="CHILDBOYCOUNT" name="OLDMANCOUNT" type="text" nullable="yes" desc="小男孩"/></div>
                                </li>
                                <li class="link">
                                    <div class="label">女:</div>
                                    <div class="value"><input id="CHILDGIRLCOUNT" name="OLDWOMANCOUNT" type="text" nullable="yes" desc="小女孩"/></div>
                                </li>

                             </ul>
                        </div>
                        <div class="l_bottom">
                            <div class="c_submit c_submit-full">
                                <button type="button" ontap="$.changegoodseeliveinfo.confirmChildinfo();" class="e_button-l e_button-green">确定</button>
                            </div>
                        </div>
                    </div>
                </div>
                 <!-- 兴趣爱好选择 -->
                <div class="c_popupItem" id="UI-HOBBYLIST">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">兴趣爱好</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="hobbylist">

                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                     <div class="l_bottom">
                         <div class="c_submit c_submit-full">
                             <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmSelectHobby();" class="e_button-l e_button-green">确定</button>
                         </div>
                     </div>
                        </div>
                    </div>
                </div>
                 <!-- 中国风骨 -->
                <div class="c_popupItem" id="UI-CHINESSSTYLE">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">中国风骨</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="chinesestylelist">

                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>

                        <div class="l_bottom">
                                    <div class="c_submit c_submit-full">
                                        <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmSelectChineseStyle();" class="e_button-l e_button-green">确定</button>
                                    </div>
                                </div>
                        </div>
                </div>
                </div>
                    <!-- 欧洲经典 -->
                <div class="c_popupItem" id="UI-EUROPEANCLASSICS">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">欧洲经典</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="europeanclassicslist">

                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                                <div class="l_bottom">
                                   <div class="c_submit c_submit-full">
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmEuropeanClassics();" class="e_button-l e_button-green">确定</button>
                                   </div>
                               </div>
                        </div>
                  </div>
                </div>
                    <!-- 现代之源 -->
                               <div class="c_popupItem" id="UI-MODERNSOURCE">
                                   <!-- 标题栏 开始 -->
                                   <div class="c_header">
                                       <div class="back" ontap="hidePopup(this);">现代之源</div>
                                   </div>
                                   <!-- 标题栏 结束 -->
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                                   <div class="l_padding">
                                       <div class="c_box">
                                           <!-- 表单 开始 -->
                                           <div class="c_list c_list-form">
                                               <ul id="modernsourcelist">

                                               </ul>
                                           </div>
                                           <!-- 表单 结束 -->

                            <div class="c_space-3"></div>
                            <div class="c_space-3"></div>
                            <div class="c_space-3"></div>

                                    <div class="l_bottom">
                                        <div class="c_submit c_submit-full">
                                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmModernSource();" class="e_button-l e_button-green">确定</button>
                                        </div>
                                    </div>
                                       </div>
                                   </div>
                                </div>
                                </div>
                    <!-- 功能系统 -->

                <div class="c_popupItem" id="UI-FUNCSYSTEM">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">功能系统</div>
                    </div>
                    <!-- 标题栏 结束 -->
                   <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="funcsystemlist">

                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                                <div class="l_bottom">
                                   <div class="c_submit c_submit-full">
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmFuncs();" class="e_button-l e_button-green">确定</button>
                                   </div>
                               </div>
                        </div>
                  </div>
                  </div>
                </div>

                <div class="c_popupItem" id="UI-ADVANTAGE">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">优势介绍</div>
                    </div>
                    <!-- 标题栏 结束 -->
                   <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="advantagelist">

                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                                <div class="l_bottom">
                                   <div class="c_submit c_submit-full">
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmAdvantages();" class="e_button-l e_button-green">确定</button>
                                   </div>
                               </div>
                        </div>
                  </div>
                  </div>
                </div>

                <div class="c_popupItem" id="UI-CRITICALPROCESS">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">关键流程介绍</div>
                    </div>
                    <!-- 标题栏 结束 -->
                   <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="criticalprocesslist">

                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                                <div class="l_bottom">
                                   <div class="c_submit c_submit-full">
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmCriticalProcess();" class="e_button-l e_button-green">确定</button>
                                   </div>
                               </div>
                        </div>
                  </div>
                  </div>
                </div>

                <div class="c_popupItem" id="UI-INFORMATIONSOURCE">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">信息来源</div>
                    </div>
                    <!-- 标题栏 结束 -->
                   <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                    <div class="l_padding">
                        <div class="c_box">
                            <!-- 表单 开始 -->
                            <div class="c_list c_list-form">
                                <ul id="informationsourcelist">

                                </ul>
                            </div>
                            <!-- 表单 结束 -->
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>
                                <div class="l_bottom">
                                   <div class="c_submit c_submit-full">
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.changegoodseeliveinfo.confirmInformationSource();" class="e_button-l e_button-green">确定</button>
                                   </div>
                               </div>
                        </div>
                  </div>
                  </div>
                </div>

            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
   $.changegoodseeliveinfo.init();
</script>
</body>
</html>
