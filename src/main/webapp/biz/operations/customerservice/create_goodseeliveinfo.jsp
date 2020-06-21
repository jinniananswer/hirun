<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户需求信息录入</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/customerservice/create.goodseeliveinfo.js?v=20200621"></script>
</head>
<body>
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">客户需求信息录入</div>
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

       <!--基本信息开始-->
       <div id="baseinfo">
        <div class="c_title">
            <div class="text">基本信息</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
            <ul >
<!--                <li class="link required">
                    <div class="label">是否单独木制品</div>
                    <div class="value">
                        <input type="checkbox" id="onlyWood" name="onlyWood"/>
                    </div>
                </li>
-->

                <li class="link required">
                    <div class="label">客户编码</div>
                    <div class="value">
                        <input type="text" id="customerNo" name="customerNo" readonly />
                    </div>
                </li>

                 <input type="hidden" id="cust_id" name="cust_id" readonly />
                 <input type="hidden" id="prepare_id" name="prepare_id" placeholder="" nullable="yes" desc="" />
                 <input type="hidden" id="project_id" name="project_id" placeholder="" nullable="yes" desc="" />
                 <input type="hidden" id="saveContinue" name="saveContinue" placeholder="" nullable="yes" desc="" />
                 <input type="hidden" id="isMoreCustomer" name="isMoreCustomer" placeholder="" nullable="yes" desc="" />


               <li class="link required">
                       <div class="label">电话号码</div>
                       <div class="value">
                             <input type="text" id="CONTACT" name="CONTACT" placeholder="请填写正确的电话号码" nullable="no" desc="电话号码" />
                       </div>
                </li>
                <li class="link required">
                    <div class="label re">客户姓名</div>
                    <div class="value">
                        <input type="text" id="NAME" name="NAME" placeholder="请输入客户姓名" nullable="no"  desc="客户姓名" />
                    </div>
                </li>

               <li class="link required" ontap="$('#houseName').focus();$('#houseName').blur();showPopup('UI-popup','UI-HOUSELIST');">
                                 <div class="label">楼盘</div>
                                 <div class="value">
                                     <input type="text" id="houseName" name="houseName" readonly="true" placeholder="" nullable="no" desc="楼盘" />
                                     <input type="hidden" id="house_id" name="house_id" placeholder="" nullable="no" desc="" />
                                 </div>
                                 <div class="more"></div>
               </li>
               <li class="link ">
                                <div class="label">楼栋</div>
                                <div class="value">
                                    <input type="text" id="house_building" name="house_building" placeholder="请填写楼盘地址、栋号" nullable="yes" desc="装修地点" />
                                </div>
               </li>
               <li class="link">
                                <div class="label">单元/房间号</div>
                                <div class="value">
                                    <input type="text" id="house_room_no" name="house_room_no" placeholder="请填写楼盘地址、栋号" nullable="yes" desc="装修地点" />
                                </div>
               </li>
               <li class="link required">
                                <div class="label">户型</div>
                                <div class="value">
                                    <span id="houseKindContainer"></span>
                                </div>
               </li>

               <li class="link required">
                                <div class="label">面积</div>
                                <div class="value">
                                    <input type="text" id="AREA" name="AREA" placeholder="" nullable="no" desc="面积" />
                                </div>
                </li>
               <li class="link">
                                <div class="label">样板房</div>
                                <div class="value">
                                    <span id="sampleHouseContainer"></span>
                                </div>
               </li>
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
                                     <input type="text" id="PEOPLE_COUNT" name="PEOPLE_COUNT" placeholder="请填写家里人口数" nullable="yes" desc="常驻人口" />
                                 </div>
                                 <div class="label">人</div>
               </li>

               <li class="link ">
                                 <div class="label">老人详情</div>
                                 <div class="value">
                                     <textarea  class="e_textarea-row-2" id="OLDER_DETAIL" name="OLDER_DETAIL"  placeholder="可填写家里老人数量、老人家装需求" nullable="yes" desc="老人详情"></textarea>
                                 </div>
               </li>

               <li class="link">
                                 <div class="label">小孩详情</div>
                                 <div class="value">
                                     <textarea  class="e_textarea-row-2" id="CHILD_DETAIL" name="CHILD_DETAIL"  placeholder="可填写家里小孩数量、小孩家装需求" nullable="yes" desc="小孩详情"></textarea>
                                 </div>
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
        <div id="">
            <div class="c_title">
                <div class="text">客户来源</div>
                <div class="fn" >

                </div>
            </div>
            <div class="c_box">
                <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                    <ul id="LIFESHAPEAREA">
                        <li class="link required">
                                <div class="label">客户类型</div>
                                <div class="value">
                                    <span id="customerTypeContainer"></span>
                                </div>
                        </li>
                        <li class="link">
                                <div class="label">活动类型</div>
                                <div class="value">
                                    <span id="marketingTypeContainer"></span>
                                </div>
                        </li>
                        <li class="link ">
                                 <div class="label">活动名称</div>
                                 <div class="value">
                                     <input type="text" id="marketing_name" name="marketing_name" placeholder="" nullable="yes"  />
                                 </div>
                        </li>
                        <li class="link " >
                                 <div class="label">活动时间</div>
                                 <div class="value">
                                      <span class="e_mix">
                                           <input type="text" id="marketing_time" name="marketing_time" datatype="date" nullable="yes"  />
                                           <span class="e_ico-date"></span>
                                      </span>
                                 </div>
                        </li>
                        <li class="link required" >
                                 <div class="label">咨询时间</div>
                                 <div class="value">
                                      <span class="e_mix">
                                           <input type="text" id="consult_time" name="consult_time" nullable="no"  />
                                           <span class="e_ico-date"></span>
                                      </span>
                                 </div>
                        </li>
<!--                        <li class="link " >
                                 <div class="label">电话咨询时间</div>
                                 <div class="value">
                                      <span class="e_mix">
                                           <input type="text" id="phone_consult_time" name="phone_consult_time" nullable="yes" disabled />
                                           <span class="e_ico-date"></span>
                                      </span>
                                 </div>
                        </li>
-->
                        <li class="link ">
                                <div class="label">其他备注</div>
                                <div class="value">
                                    <input type="text" id="other_remark" name="other_remark" placeholder="" nullable="yes" desc="" />
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
                   <div class="text">客户意向</div>
                      <div class="fn" >

                    </div>
                </div>
                <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                 <li class="link required" >
                                         <div class="label">看中设计师的作品</div>
                                         <div class="value">
                                             <input type="text" id="DESIGNER_OPUS" name="DESIGNER_OPUS" placeholder="" nullable="no" desc="看中设计师的作品" />
                                         </div>
                                 </li>
                                 <li class="link required" >
                                         <div class="label">木制品意向</div>
                                         <div class="value">
                                             <input type="text" id="WOOD_INTENTION" name="WOOD_INTENTION" placeholder="" nullable="no" desc="木制品意向" />
                                         </div>
                                 </li>
                                 <li class="link ">
                                        <div class="label">去过的家装公司和家具卖场、感兴趣的主材品牌:</div>
                                        <div class="value">
                                            <textarea class="e_textarea-row-2" id="OTHER_INFO" name="OTHER_INFO" placeholder="" nullable="yes" desc="兴趣品牌"></textarea>
                                        </div>
                                </li>
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
                                         <div class="label">特殊要求</div>
                                         <div class="value">
                                             <input type="text" id="FUNC_SPEC_REQ" name="FUNC_SPEC_REQ" placeholder="" nullable="yes" desc="特殊要求" />
                                         </div>
                                </li>

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
                                <li class="link " >
                                        <div class="label">计划入住时间</div>
                                        <div class="value">
                                             <span class="e_mix">
                                             <input type="text" id="PLAN_LIVE_TIME" name="PLAN_LIVE_TIME" datatype="date" nullable="yes" readonly="true" desc="计划入住时间" />
                                             <span class="e_ico-date"></span>
                                             </span>
                                       </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

        <div id="">
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
                   <div class="text">家装分期</div>
                      <div class="fn" >

                        </div>
                </div>
                    <div class="c_box">
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="">
                                <li class="link">
                                    <div class="label">合作银行</div>
                                        <div class="value">
                                         <span id="bankContainer"></span>
                                        </div>
                                 </li>
                                 <li class="link " >
                                        <div class="label">额度需求</div>
                                            <div class="value">
                                                <input type="text" id="quota" name="quota" placeholder="" nullable="yes" desc="额度需求" />
                                            </div>
                                       <div class="label">万元</div>
                                 </li>
                                <li class="link ">
                                    <div class="label">期限需求</div>
                                        <div class="value">
                                         <span id="monthNumContainer"></span>
                                        </div>
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
                                <li class="link required" ontap="$('#INFORMATIONSOURCE').focus();$('#INFORMATIONSOURCE').blur();showPopup('UI-popup','UI-INFORMATIONSOURCE');">
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
          </div>
          <!--接待信息结束-->
          <div class="c_space-3"></div>

        <div class="c_submit c_submit-full">
            <button type="button" id="PREVIOUS_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.goodseelive.previous()">上一页</button>
            <button type="button" id="CONFIRM_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.goodseelive.createGoodSeeLiveInfo()">提交</button>
            <button type="button" id="NEXT_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.goodseelive.next()">下一页</button>
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
                             <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmSelectHobby();" class="e_button-l e_button-green">确定</button>
                         </div>
                     </div>
                        </div>
                    </div>
                </div>
                 <!-- 楼盘选择 -->
                <div class="c_popupItem" id="UI-HOUSELIST">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">请选择楼盘</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="c_scroll">
                        <div class="l_padding">
                            <div class="c_box">
                                <!-- 表单 开始 -->
                                <div class="c_list c_list-form">
                                    <ul id="houseArea">
                                          <li>
                                               <div class="value">
                                                    <span class="e_mix">
                                                          <input id="HOUSES_NAME" name="HOUSES_NAME" type="text" placeholder="请输入楼盘地址（模糊搜索）" nullable="no" desc="查询条件"/>
                                                         <button type="button" class="e_button-blue" ontap="$.goodseelive.searchHouse();"><span class="e_ico-search"></span><span>查询</span></button>
                                                     </span>
                                               </div>
                                          </li>
                                    </ul>
                                </div>
                                <!-- 表单 结束 -->
                            </div>
                            <div class="c_box">
                                <!-- 表单 开始 -->
                                <div class="c_list c_list-form">
                                    <ul id="houses">

                                    </ul>
                                </div>
                                <!-- 表单 结束 -->
                            </div>

                             <div class="c_msg c_msg-warn" id="houseMessageBox" style="display:none">
                                <div class="wrapper">
                                    <div class="emote"></div>
                                    <div class="info">
                                        <div class="text">
                                            <div class="title">提示信息</div>
                                            <div class="content">sorry,没有找到您想要的信息~</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                         <div class="c_space-3"></div>
                         <div class="c_space-3"></div>
                    </div>
                     <div class="l_bottom" style="display:none">
                           <div class="c_submit c_submit-full">
                                <button type="houseConfirmButton" id="houseConfirmButton" name="houseConfirmButton" ontap="$.goodseelive.chooseHouse();" style="display:none" class="e_button-l e_button-green">确定</button>
                           </div>
                     </div>
                </div>
                 <!-- 客户选择 -->
                <div class="c_popupItem" id="UI-CUSTOMERLIST">
                    <!-- 标题栏 开始 -->
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">客户存在多次,如果客户已有客户代表则不能转成自己的客户</div>
                    </div>
                    <!-- 标题栏 结束 -->
                    <div class="c_scroll">
                        <div class="l_padding">
                            <div class="c_box">
                                <!-- 表单 开始 -->
                                <div class="c_list c_list-form">
                                    <ul id="moreCustomer">

                                    </ul>
                                </div>
                                <!-- 表单 结束 -->
                                      <div class="c_space-3"></div>
                                      <div class="c_space-3"></div>
                                <div class="l_bottom" >
                                    <div class="c_submit c_submit-full">
                                        <button type="button" id="confirmCustButton" name="confirmCustButton" ontap="$.goodseelive.transCustomer();" class="e_button-l e_button-green">转成我的客户</button>
                                        <button type="button" id="continueSaveButton" name="continueSaveButton" ontap="$.goodseelive.continueSave();" class="e_button-l e_button-green">继续新增</button>
                                    </div>
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
                                        <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmSelectChineseStyle();" class="e_button-l e_button-green">确定</button>
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
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmEuropeanClassics();" class="e_button-l e_button-green">确定</button>
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


                                       </div>
                                   </div>
                                </div>
                                    <div class="l_bottom">
                                        <div class="c_submit c_submit-full">
                                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmModernSource();" class="e_button-l e_button-green">确定</button>
                                        </div>
                                    </div>
                                         <div class="c_space-3"></div>
                                         <div class="c_space-3"></div>

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

                        </div>
                  </div>
                  </div>
                            <div class="l_bottom">
                                   <div class="c_submit c_submit-full">
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmFuncs();" class="e_button-l e_button-green">确定</button>
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
                        </div>
                  </div>
                  </div>
                   <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmAdvantages();" class="e_button-l e_button-green">确定</button>
                        </div>
                    </div>
                        <div class="c_space-3"></div>
                        <div class="c_space-3"></div>

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
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmCriticalProcess();" class="e_button-l e_button-green">确定</button>
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
                                       <button type="button" id="SUBMIT_QUERY" name="SUBMIT_QUERY" ontap="$.goodseelive.confirmInformationSource();" class="e_button-l e_button-green">确定</button>
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
    $.goodseelive.init();
</script>
</body>
</html>
