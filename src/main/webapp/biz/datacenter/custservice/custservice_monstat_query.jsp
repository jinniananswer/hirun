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
	<title>客户代表月报表</title>
	<jsp:include page="/common.jsp"></jsp:include>
	<script src="scripts/biz/datacenter/custservice/custservice.monstat.query.js?v=20190823"></script>
</head>
<body>
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">客户代表月报表</div>
    <div class="fn">
        <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','QueryCondPopupItem')"><span class="e_ico-search"></span></button>
    </div>
</div>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4em;">
        <div class="c_title">
            <div class="text"></div>
            <div class="fn">
                <ul>
                    <li ontap="$.custServiceMonstatQuery.export();" class=""><span class="e_ico-export"></span>数据导出</li>
                </ul>
            </div>
        </div>
	<div id="custserviceTable" class="c_table c_table-hasGrid c_table-border c_table-lite c_table-row-10" style="height: 34em;">
		<div class="body">
			<div class="wrapper">
				<table>
					<thead>
					<tr>
						<th col="CUSTSERVICE_NAME">客户代表</th>
						<th col="ZX_COUNT" ontap="$.sortTable(this, 'int')">新客户咨询数</th>
						<th col="FINISH_STYLE"class="" ontap="$.sortTable(this, 'int')" >生成风格蓝图风格数</th>
						<th col="FINISH_STYLECALE" class="e_red" >生成风格蓝图比例</th>
						<th col="FINISH_FUNC" ontap="$.sortTable(this, 'int')" >生成功能蓝图风格数</th>
						<th col="FINISH_FUNCCALE" class="e_red" >生成功能蓝图比例</th>
						<th col="FINISH_XQLTECALE" class="e_red" >蓝图二生成比例</th>
						<th col="FINISH_SMQLC" class="" ontap="$.sortTable(this, 'int')" >扫客户代表码进入全流程数</th>
						<th col="FINISH_SMQLCCALE" class="e_red" >扫客户代表码进入全流程比例</th>
						<th col="FINISH_DKCSMW" class="e_red" ontap="$.sortTable(this, 'int')" >带看城市木屋数</th>
						<th col="FINISH_DKCSMWCALE" class="e_red" >带看城市木屋比例</th>
					</tr>
					</thead>
					<tbody id="custservicestatinfo">

					</tbody>
				</table>
                <div class="c_msg c_msg-warn" id="queryMessage" style="display:none">
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
		</div>
		<div class="top">
			<table><thead></thead><tbody></tbody></table>
		</div>
		<div class="left" style="display:none">
			<table><thead></thead><tbody></tbody></table>
		</div>
		<div class="right" style="display:none">
			<table><thead></thead><tbody></tbody></table>
		</div>
		<div class="leftTop" style="display:none">
			<table><thead></thead><tbody></tbody></table>
		</div>
		<div class="rightTop" style="display:none">
			<table><thead></thead><tbody></tbody></table>
		</div>
	</div>

	<div class="e_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>


<div class="c_popup" id="UI-popup">
	<div class="c_popupBg" id="UI-popup_bg"></div>
	<div class="c_popupBox">
		<div class="c_popupWrapper" id="UI-popup_wrapper">
			<div class="c_popupGroup">
				<div class="c_popupItem" id="QueryCondPopupItem">
					<div class="c_header">
						<div class="back" ontap="backPopup(this)">查询条件</div>
					</div>
					<div class="c_scroll c_scroll-float c_scroll-header l_padding">
						<div class="c_list c_list_form c_list-line" id="QueryCondForm">
							<ul>
								<li class="required">
                                    <div class="label">月份</div>
									<div class="value">
										<span class="e_mix">
											<input type="text" id="MON_DATE" name="MON_DATE" datatype="date" nullable="no" desc="月份" readonly="true"/>
											<span class="e_ico-date"></span>
										</span>
									</div>
								</li>
                                <li>
                                    <div class="label">客户代表</div>
                                    <div class="value">
                                         <span class="e_mix" ontap="forwardPopup('UI-popup','UI-QUERYCUSTSERVICE')">
                                             <input type="text" id="CUSTSERVICEEMPLOYEENAME" name="CUSTSERVICEEMPLOYEENAME" datatype="text"
                                                    nullable="yes" desc="客户代表" value="" readonly="true"/>
                                             <input type="hidden" id="CUSTSERVICEEMPLOYEEID" name="CUSTSERVICEEMPLOYEEID" placeholder="" nullable="yes" desc="" />
                                             <span class="e_ico-check"></span>
                                         </span>
                                    </div>
                                </li>
                                <li class="link" ontap="$('#ORG_TEXT').focus();$('#ORG_TEXT').blur();forwardPopup('UI-popup','UI-ORG')">
                                        <div class="label">所属部门</div>
                                        <div class="value">
                                             <input type="text" id="ORG_TEXT" name="ORG_TEXT" nullable="yes" readonly="true" desc="所属部门" />
                                             <input type="hidden" id="ORG_ID" name="ORG_ID" nullable="yes" desc="所属部门" />
                                        </div>
                                        <div class="more"></div>
                                </li>
							</ul>
						</div>
						<div class="c_space"></div>
						<div class="c_submit c_submit-full">
							<button type="button" class="e_button-r e_button-l e_button-green" ontap="$.custServiceMonstatQuery.clearCond();">重置</button>
							<button type="button" class="e_button-r e_button-l e_button-green" ontap="$.custServiceMonstatQuery.query();">查询</button>
						</div>
					</div>
				</div>
			</div>

            <div class="c_popupGroup">
                 <div class="c_popupItem" id="UI-ORG">
                      <div class="c_header">
                           <div class="back" ontap="hidePopup(this);">请选择部门</div>
                      </div>
                      <div class="c_scroll c_scroll-float c_scroll-header">
                           <div class="c_box">
                               <div class="l_padding">
                                   <div id="orgTree" class="c_tree"></div>
                               </div>
                           </div>
                      </div>
                 </div>

                 <div class="c_popupItem" id="UI-QUERYCUSTSERVICE">
                      <div class="c_header">
                           <div class="back" ontap="hidePopup(this);">请选择客户代表</div>
                      </div>
                      <div class="l_padding">
                           <div class="c_box">
                               <div class="c_list c_list-line">
                                   <ul id="jobArea">
                                       <li>
                                           <div class="value">
                                                <span class="e_mix">
                                                         <input id="CUSTSERVICE_NAME" name="CUSTSERVICE_NAME" type="text" placeholder="请输入客户代表姓名（模糊搜索）" nullable="no" desc="查询条件"/>
                                                         <button type="button" class="e_button-blue" ontap="$.custServiceMonstatQuery.queryCustService();"><span class="e_ico-search"></span><span>查询</span></button>
                                                 </span>
                                           </div>
                                       </li>
                                   </ul>
                               </div>
                           </div>
                      </div>
                      <div class="c_scroll">
                           <div class="l_padding">
                                <div class="c_box">
                                    <div class="c_list c_list-line">
                                        <ul id="querycustservicesinfo">

                                        </ul>
                                     </div>
                                </div>
                                <div class="c_space-3"></div>
                                <div class="c_space-3"></div>
                                <div class="c_space-3"></div>
                           </div>
                                 <div class="c_space-3"></div>
                                 <div class="c_space-3"></div>
                      </div>
                 </div>
            </div>
		</div>
	</div>
</div>


<script type="text/javascript">
    Wade.setRatio();
    $.custServiceMonstatQuery.init();
</script>
</body>
</html>