<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.hirun.pub.domain.entity.user.UserEntity" %>
<%@ page import="com.hirun.pub.domain.entity.org.EmployeeEntity" %>
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
    <link rel="stylesheet" href="frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="frame/css/project.css" rel="stylesheet" type="text/css"/>
    <script src="frame/TouchUI/content/js/jcl-base.js"></script>
    <script src="frame/TouchUI/content/js/jcl.js"></script>
    <script src="frame/TouchUI/content/js/i18n/code.zh_CN.js"></script>
    <script src="frame/TouchUI/content/js/jcl-plugins.js"></script>
    <script src="frame/TouchUI/content/js/jcl-ui.js?a=1"></script>
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
    <script src="frame/TouchUI/content/js/ui/component/tree/tree.js?v=20181127"></script>
    <script src="frame/TouchUI/content/js/local.js"></script>
    <script src="frame/js/ajax.js"></script>
    <script src="frame/js/redirect.js"></script>
    <script src="frame/js/date.js"></script>
    <script src="frame/TouchUI/content/js/plugins/template/template.js"></script>
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
</html>
