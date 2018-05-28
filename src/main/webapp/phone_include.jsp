<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;
%>
<html size="s">
<head>
    <base href="<%=basePath%>"></base>
    <link rel="stylesheet" href="frame/TouchUI/content/css/base.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="frame/css/m_project.css" rel="stylesheet" type="text/css"/>
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
    <script src="frame/TouchUI/content/js/plugins/template/template.js"></script>
</head>
</html>