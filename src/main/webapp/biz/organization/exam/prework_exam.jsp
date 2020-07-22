<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2019/1/3
  Time: 10:08 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>培训查询</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/exam/prework.exam.js?v=20200722"></script>
</head>
<body>
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">岗前测试</div>
    <div class="fn">

    </div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header" id="submitArea">
    <div class="l_padding">
        <div class="c_msg" id="messagebox" style="display:none">
            <div class="wrapper">
                <div class="emote"></div>
                <div class="info">
                    <div class="text">
                        <div class="title">提示信息</div>
                        <div class="content" id="EXAM_DESC"></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="timedown_root" style="display:none">
            <div class="title">
                <div class="text e_strong e_red" id="timedown">

                </div>
            </div>
            <div class="c_space"></div>
        </div>
        <div id="exam">
            <div class="c_box">
                <div class='c_title'>
                    <div class="text e_strong e_blue">
                        请选择要测试的科目
                    </div>
                    <div class="fn"></div>
                </div>
                <div class="l_padding l_padding-u">
                    <div class="c_list c_list-line c_list-border c_list-space l_padding">
                        <ul>
                            <li>
                                <div class="group">
                                    <div class="content">
                                        <div class='l_padding'>
                                            <div class="pic pic-middle">
                                            </div>
                                        </div>
                                        <div class="main">
                                            <div class="title">
                                            </div>
                                            <div class="content content-auto">
                                                通用知识
                                            </div>
                                        </div>
                                    </div>
                                    <div class="side">
                                        <input type="radio" name='RADIO_EXAM' value='1' style="outline:none;" ontap='$.exam.selectExam(this);'/>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="group">
                                    <div class="content">
                                        <div class='l_padding'>
                                            <div class="pic pic-middle">
                                            </div>
                                        </div>
                                        <div class="main">
                                            <div class="title">
                                            </div>
                                            <div class="content content-auto">
                                                产品知识
                                            </div>
                                        </div>
                                    </div>
                                    <div class="side">
                                        <input type="radio" name='RADIO_EXAM' value='2' ontap='$.exam.selectExam(this);'/>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="group">
                                    <div class="content">
                                        <div class='l_padding'>
                                            <div class="pic pic-middle">
                                            </div>
                                        </div>
                                        <div class="main">
                                            <div class="title">
                                            </div>
                                            <div class="content content-auto">
                                                鸿扬介绍
                                            </div>
                                        </div>
                                    </div>
                                    <div class="side">
                                        <input type="radio" name='RADIO_EXAM' value='3' ontap='$.exam.selectExam(this);'/>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="group">
                                    <div class="content">
                                        <div class='l_padding'>
                                            <div class="pic pic-middle">
                                            </div>
                                        </div>
                                        <div class="main">
                                            <div class="title">
                                            </div>
                                            <div class="content content-auto">
                                                家装知识
                                            </div>
                                        </div>
                                    </div>
                                    <div class="side">
                                        <input type="radio" name='RADIO_EXAM' value='4' ontap='$.exam.selectExam(this);'/>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div class="group">
                                    <div class="content">
                                        <div class='l_padding'>
                                            <div class="pic pic-middle">
                                            </div>
                                        </div>
                                        <div class="main">
                                            <div class="title">
                                            </div>
                                            <div class="content content-auto">
                                                客户服务
                                            </div>
                                        </div>
                                    </div>
                                    <div class="side">
                                        <input type="radio" style="border: none !important;" name='RADIO_EXAM' value='5' ontap='$.exam.selectExam(this);'/>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div id="topic">

        </div>
        <div class="c_form"  id="topic_select" style="display:none">
                跳转至
                <input type="text" id="topic_index" placeholder="请输入题目编号"/>
                <button type="button" ontap="$.exam.selectTopic()">确定</button>
        </div>
        <div class="c_space"></div>
        <div class="c_submit c_submit-full">
            <button type="button" id="CONFIRM_BUTTON" class="e_button-r e_button-l e_button-green" ontap="$.exam.init()">确定</button>
            <button type="button" id="START_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.exam.start()">开始考试</button>
            <button type="button" id="PREVIOUS_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.exam.previous()">上一题</button>
            <button type="button" id="NEXT_BUTTON" style="display:none" class="e_button-r e_button-l e_button-green" ontap="$.exam.next()">下一题</button>

            <button type="button" id="SUBMIT_BUTTON" style="display:none" class="e_button-r e_button-l e_button-red" ontap="$.exam.submit(true)">提交考卷</button>
        </div>
    </div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
    <div class="c_space"></div>
</div>
<!-- 滚动 结束 -->
<!-- 弹窗 开始 -->
<div class="c_popup" id="UI-popup">
    <div class="c_popupBg" id="UI-popup_bg"></div>
    <div class="c_popupBox">
        <div class="c_popupWrapper" id="UI-popup_wrapper">
            <div class="c_popupGroup">
                <div class="c_popupItem" id="UI-ERROR_ITEM">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this);">本次做错的题目</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header">
                        <div class="c_list c_list-line c_list-border c_list-space l_padding">
                            <ul id="error_topic">

                            </ul>
                        </div>
                        <div class="c_submit c_submit-full">
                            <button type="button" id="CLOLSE_BUTTON" class="e_button-r e_button-l e_button-red" ontap="$.redirect.closeCurrentPage();">关闭</button>
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
</script>
</body>
</html>
