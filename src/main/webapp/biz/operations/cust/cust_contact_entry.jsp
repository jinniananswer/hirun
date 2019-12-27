<%@ page import="com.hirun.pub.domain.entity.user.UserEntity" %>
<%@ page import="com.most.core.pub.data.SessionEntity" %>
<%@ page import="com.most.core.web.session.HttpSessionManager" %>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;

    Object user = session.getAttribute("USER");
    String userId = "";
    if(user != null) {
        UserEntity userEntity = (UserEntity)user;
        userId = userEntity.getUserId();
    }

    /*
    EmployeeEntity employeeEntity = (EmployeeEntity)session.getAttribute("EMPLOYEE");
    String employeeId = "";
    String employeeName = "";
    if(employeeEntity != null) {
        employeeId = employeeEntity.getEmployeeId();
        employeeName = employeeEntity.getName();
    }*/
    String employeeId = "";
    String employeeName = "";
    SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
    if(sessionEntity != null) {
        employeeId = sessionEntity.get("EMPLOYEE_ID");
        employeeName = sessionEntity.get("EMPLOYEE_NAME");
    }
%>
<html size="s">
<head>
    <base href="<%=basePath%>"></base>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>客户接触记录录入</title>
    <%--<jsp:include page="/common.jsp"></jsp:include>--%>
    <link rel="stylesheet" href="frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="frame/css/project.css" rel="stylesheet" type="text/css"/>
    <script src="frame/TouchUI/content/js/jcl-base.js"></script>
    <script src="frame/TouchUI/content/js/jcl.js"></script>
    <script src="frame/TouchUI/content/js/i18n/code.zh_CN.js"></script>
    <script src="frame/TouchUI/content/js/jcl-plugins.js"></script>
    <script src="frame/TouchUI/content/js/jcl-ui.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/popup.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/frame.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/segment.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/switch.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/select.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/tabset/tabset.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/box/tipbox.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/box/popupbox.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/datefield.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/calendar/calendar.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/increasereduce.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/ipaddressfield.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/chart/echarts.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/table/table.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/tree/tree.js"></script>
    <script src="frame/TouchUI/content/js/local.js"></script>
    <script src="frame/js/ajax.js"></script>
    <script src="frame/js/redirect.js"></script>
    <script src="frame/js/date.js"></script>
    <script src="frame/TouchUI/content/js/plugins/template/template.js"></script>
    <script src="scripts/biz/cust/cust.contact.entry.js?a=1"></script>
    <script type="text/javascript">
        var System = {};
        System.basePath = '<%=path%>';

        var User = {};
        User.userId = '<%=userId%>';

        var Employee = {};
        Employee.employeeId = '<%=employeeId%>';
        Employee.employeeName = '<%=employeeName%>';

        window.alert = function(name){
            var iframe = document.createElement("IFRAME");
            iframe.style.display="none";
            iframe.setAttribute("src", 'data:text/plain,');
            document.documentElement.appendChild(iframe);
            window.frames[0].window.alert(name);
            iframe.parentNode.removeChild(iframe);
        };
        window.confirm = function (message) {
            var iframe = document.createElement("IFRAME");
            iframe.style.display = "none";
            iframe.setAttribute("src", 'data:text/plain,');
            document.documentElement.appendChild(iframe);
            var alertFrame = window.frames[0];
            var result = alertFrame.window.confirm(message);
            iframe.parentNode.removeChild(iframe);
            return result;
        };
    </script>
</head>
<body>
<jsp:include page="/header.jsp">
    <jsp:param value="客户接触记录录入" name="headerName"/>
</jsp:include>
<div class="c_scroll c_scroll-float c_scroll-header" style="bottom:4.4em;">
    <div class="c_space"></div>
    <div class="l_queryFn">
        <div class="c_fn">
            <div class="right">
                <button class="e_button-blue" type="button" ontap="showPopup('UI-popup','CustQueryCondPopupItem');">
                    <span class="e_ico-search"></span>
                </button>
            </div>
        </div>
    </div>
    <div id="cust_list">

    </div>
    <div class="e_space"></div>
</div>
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="CustQueryCondPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">客户查询条件</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header l_padding">
                        <div class="c_list c_list_form c_list-line" id="queryCustParamForm">
                            <ul>
                                <li>
                                    <div class="label">客户姓名</div>
                                    <div class="value">
                                        <input type="text" name="CUST_NAME"/>
                                    </div>
                                </li>
                                <li>
                                    <div class="label">联系电话</div>
                                    <div class="value">
                                        <input type="text" name="MOBILE_NO"/>
                                    </div>
                                </li>
                                <li>
                                    <div class="label">楼盘</div>
                                    <div class="value">
                                        <span id="queryCustParamForm_house_container"></span>
                                        </span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 客户列表 结束 -->
                        <div class="c_space"></div>
                        <div class="c_submit c_submit-full">
                            <button type="button" class="e_button-l e_button-green" ontap="custContactEntry.queryCustList4Cond(this)">查询</button>
                        </div>
                    </div>
                </div>
                <div class="c_popupItem" id="CustContactPopupItem">
                    <div class="c_header">
                        <div class="back" ontap="backPopup(this)">客户接触记录填写</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header l_padding">
                        <div class="c_list c_list_form c_list-line" id="custContactForm">
                            <ul>
                                <li class="required">
                                    <div class="label">接触笔记</div>
                                    <div class="value">
                                        <textarea id="CONTACT_NOTE" name="CONTACT_NOTE" class="e_textarea-row-4"
                                                  nullable="no" desc="接触笔记"></textarea>
                                    </div>
                                </li>
                                <li class="required">
                                    <div class="label">接触日期</div>
                                    <div class="value">
                                        <span class="e_mix">
                                            <input type="text" id="CONTACT_DATE" name="CONTACT_DATE"
                                                   datatype="date" desc="接触日期" readonly="true" nullable="no"/>
                                            <span class="e_ico-date"></span>
                                        </span>
                                    </div>
                                </li>
                                <li class="required">
                                    <div class="label">后续动作</div>
                                    <div class="value">
                                        <span class="e_segment">
                                            <span idx="0" val="1">无</span>
                                            <span idx="1" val="2">提醒</span>
                                            <span idx="2" val="3">暂停接触</span>
                                            <input type="hidden" name="AFTER_ACTION" id="AFTER_ACTION" nullable="no" desc="后续动作" />
                                        </span>
                                    </div>
                                </li>
                                <li class="link required" tag="remind" style="display: none">
                                    <div class="label">提醒动作</div>
                                    <div class="value">
                                        <span id="custContactForm_action_container"></span>
                                    </div>
                                </li>
                                <li class="link required" tag="remind" style="display: none">
                                    <div class="label">提醒时间</div>
                                    <div class="value">
                                        <span class="e_mix">
                                            <input type="text" id="REMIND_DATE" name="REMIND_DATE"
                                                   datatype="date" desc="提醒时间" />
                                            <span class="e_ico-date"></span>
                                        </span>
                                    </div>
                                </li>
                                <li class="link required" tag="pause" style="display: none">
                                    <div class="label">恢复接触日期</div>
                                    <div class="value">
                                        <span class="e_mix">
                                            <input type="text" id="RESTORE_DATE" name="RESTORE_DATE"
                                                   datatype="date" desc="跟踪恢复日期" />
                                            <span class="e_ico-date"></span>
                                        </span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 客户列表 结束 -->
                        <div class="c_space"></div>
                        <div class="c_submit c_submit-full">
                            <button type="button" class="e_button-l e_button-green" ontap="custContactPopup.confirm(this)">确定</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script id="cust_template" rel_id="cust_list" type="text/html">
    <div class="c_list c_list-line">
        <ul>
            {{each CUSTOMERLIST cust idx}}
            <li class='link' cust_id="{{cust.CUST_ID}}" cust_name="{{cust.CUST_NAME}}" ontap="custContactEntry.showCustDetail(this)">
                <div class="group">
                    <div class="content">
                        <div class='l_padding'>
                            <div class="pic pic-middle">
            {{if cust.SEX == 1}}
                <img src="/frame/img/male.png" class='e_pic-r' style='width:4em;height:4em'/>
            {{if cust.SEX == 2}}
                <img src="/frame/img/female.png" class='e_pic-r' style='width:4em;height:4em'/>

                            </div>
                        </div>
                    <div class="main">
                        <div class="title">
                        {{cust.CUST_NAME}}
                        </div>
                    <div class="content">
                        {{cust.WX_NICK}}
                    </div>
                    <div class='content'>
                        {{cust.HOUSE_ID}}
                    </div>
                </div>
                <div class="side e_size-m">
                {{cust.MOBILE_NO}}
                </div>
            </div>
        </div>
    </li>
            {{/each}}
        </ul>
    </div>
</script>

<script id="employee_template" rel_id="BIZ_COUNSELORS" type="text/html">
    {{each EMPLOYEE_LIST employee idx}}
    <li class="link e_center" employee_name="{{employee.NAME}}" employee_id="{{employee.EMPLOYEE_ID}}"
        ontap="counselorPopup.clickEmployee(this)" tag="li_employee">
        <label class="group" id="LABEL_{{employee.EMPLOYEE_ID}}">
            <div class="main">{{employee.NAME}}</div>
        </label>
    </li>
    {{/each}}
</script>

<script type="text/javascript">
    Wade.setRatio();
    custContactEntry.init();
</script>
</body>
</html>