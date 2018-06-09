<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.hirun.pub.domain.entity.user.UserEntity" %>
<%@ page import="com.hirun.pub.domain.entity.org.EmployeeEntity" %>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;

    UserEntity userEntity = (UserEntity) session.getAttribute("USER");
    String userId = "";
    if(userEntity != null) {
        userId = userEntity.getUserId();
    }

    EmployeeEntity employeeEntity = (EmployeeEntity)session.getAttribute("EMPLOYEE");
    String employeeId = "";
    if(employeeEntity != null) {
        employeeId = employeeEntity.getEmployeeId();
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
    <script src="frame/TouchUI/content/js/jcl-ui.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/popup.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/segment.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/switch.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/select.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/tabset/tabset.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/box/tipbox.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/datefield.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/calendar/calendar.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/base/increasereduce.js"></script>
    <script src="frame/TouchUI/content/js/ui/component/chart/echarts.js"></script>
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
    </script>
</head>
</html>
