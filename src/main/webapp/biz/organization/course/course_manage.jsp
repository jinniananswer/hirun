<%--
  Created by IntelliJ IDEA.
  User: jinnian
  Date: 2018/11/26
  Time: 10:55 AM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"  %>
<html size="s">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1" />
    <title>课程体系管理</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script src="/scripts/biz/organization/course/course.manage.js"></script>
</head>
<body>
<!-- 标题栏 开始 -->
<div class="c_header e_show">
    <div class="back" ontap="$.redirect.closeCurrentPage();">课程体系管理</div>
</div>
<!-- 标题栏 结束 -->
<!-- 滚动（替换为 java 组件） 开始 -->
<div class="c_scroll c_scroll-float c_scroll-header">
    <div class="l_padding">
        <div class="c_title">
            <div class="text">课程体系</div>
            <div class="fn">

            </div>
        </div>
        <div class="c_box">
            <div class="l_padding">
                <div id="courseTree" class="c_tree">

                </div>
            </div>
        </div>
        <div class="c_title">
            <div class="text"></div>
            <div class="fn">
                <ul>
                    <li ontap="$.course.initCreateCourse();"><span class="e_ico-add"></span>新增子课程</li>
                    <li ontap="$.course.initChangeCourse();"><span class="e_ico-edit"></span>编辑课程</li>
                    <li ontap="$.course.deleteCourse();"><span class="e_ico-delete"></span>删除课程</li>
                    <li ontap="$.course.initUploadCourse();"><span class="e_ico-upload"></span>上传课件</li>
                    <li ontap="$.course.initCourseFile();"><span class="e_ico-word"></span>课件管理</li>
                    <li ontap="$.course.viewDetail();"><span class="e_ico-show"></span>课程详情</li>
                </ul>
            </div>
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
                <div class="c_popupItem" id="UI-CREATE_COURSE">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">创建子课程</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="createCourseArea">
                                <li class="link required">
                                    <div class="label">课程名称</div>
                                    <div class="value">
                                        <input type="text" id="COURSE_NAME" name="COURSE_NAME" placeholder="请输入课程名称" nullable="yes" desc="课程名称" />
                                    </div>
                                </li>
                                <li class="link required">
                                    <div class="label">上级课程</div>
                                    <div class="value">
                                        <input type="hidden" id="PARENT_COURSE_ID" name="PARENT_COURSE_ID" nullable="no" desc="上级课程" />
                                        <input type="text" id="PARENT_COURSE_NAME" name="PARENT_COURSE_NAME" readonly="true" nullable="no" desc="上级课程" />
                                    </div>
                                </li>
                                <li class="link">
                                    <div class="label">课程描述</div>
                                    <div class="value">
                                        <textarea id="COURSE_DESC" name="COURSE_DESC" placeholder="请输入课程描述" nullable="yes" desc="课程描述" class="e_textarea-row-5"></textarea>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.course.createCourse();" class="e_button-l e_button-green">创建</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-CHANGE_COURSE">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">修改子课程</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="changeCourseArea">
                                <li class="link required">
                                    <div class="label">课程名称</div>
                                    <div class="value">
                                        <input type="hidden" id="CHANGE_COURSE_ID" name="CHANGE_COURSE_ID" nullable="no" desc="课程ID" />
                                        <input type="text" id="CHANGE_COURSE_NAME" name="CHANGE_COURSE_NAME" placeholder="请输入课程名称" nullable="yes" desc="课程名称" />
                                    </div>
                                </li>
                                <li class="link required">
                                    <div class="label">上级课程</div>
                                    <div class="value">
                                        <input type="hidden" id="CHANGE_PARENT_COURSE_ID" name="CHANGE_PARENT_COURSE_ID" nullable="no" desc="上级课程" />
                                        <input type="text" id="CHANGE_PARENT_COURSE_NAME" name="CHANGE_PARENT_COURSE_NAME" readonly="true" nullable="no" desc="上级课程" />
                                    </div>
                                </li>
                                <li class="link">
                                    <div class="label">课程描述</div>
                                    <div class="value">
                                        <textarea id="CHANGE_COURSE_DESC" name="CHANGE_COURSE_DESC" placeholder="请输入课程描述" nullable="yes" desc="课程描述" class="e_textarea-row-5"></textarea>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.course.changeCourse();" class="e_button-l e_button-green">修改课程</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-UPLOAD_COURSE">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">上传课件</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <div class="c_tip c_tip-red">上传的文件必须是PDF格式</div>
                        <form id="uploadForm" method="post" enctype="multipart/form-data">
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">

                            <ul id="uploadCourseArea">
                                <li class="link required">
                                    <div class="label">课程名称</div>
                                    <div class="value">
                                        <input type="hidden" id="UPLOAD_COURSE_ID" name="UPLOAD_COURSE_ID" nullable="no" desc="课程ID" />
                                        <input type="text" id="UPLOAD_COURSE_NAME" name="UPLOAD_COURSE_NAME" readonly="true" placeholder="请输入课程名称" nullable="yes" desc="课程名称" />
                                    </div>
                                </li>
                                <li class="link required">
                                    <div class="label">选择课程文件</div>
                                    <div class="value">
                                        <span class="e_mix" ontap="$('#COURSE_FILE0').trigger('click')">
                                            <input id="FILE_NAME0" name="FILE_NAME" type="text" value="" readonly="true"/>
                                            <span class="e_ico-browse"></span>
                                        </span>
                                        <input type="file" id="COURSE_FILE0" accept=".pdf" name="COURSE_FILE" style="display:none" onchange="$.course.fileChange(0)"/>
                                    </div>
                                </li>
                            </ul>

                        </div>
                        <div class="c_title">
                            <div class="text"></div>
                            <div class="fn">
                                <ul>
                                    <li ontap="$.course.addFile();"><span class="e_ico-add"></span>添加课程文件</li>
                                </ul>
                            </div>
                        </div>
                        <!-- 列表 结束 -->
                        </form>
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.course.uploadCourse();" class="e_button-l e_button-green">上传课件</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
                <div class="c_popupItem" id="UI-COURSE_FILE">
                    <div class="c_header">
                        <div class="back" ontap="hidePopup(this)">课件管理</div>
                    </div>
                    <div class="c_scroll c_scroll-float c_scroll-header c_scroll-submit">
                        <div class="c_msg" id="messagebox" style="display:none">
                            <div class="wrapper">
                                <div class="emote"></div>
                                <div class="info">
                                    <div class="text">
                                        <div class="title">暂无课件</div>
                                        <div class="content"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 列表 开始 -->
                        <div class="c_list c_list-col-1 c_list-fixWrapSpace c_list-form">
                            <ul id="courseFileArea">

                            </ul>
                        </div>
                        <!-- 列表 结束 -->
                    </div>
                    <div class="l_bottom">
                        <div class="c_submit c_submit-full">
                            <button type="button" ontap="$.course.deleteFile();" class="e_button-l e_button-red">删除课件</button>
                        </div>
                    </div>
                    <!-- 滚动 结束 -->
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 弹窗 结束 -->
<jsp:include page="/base/buttom/base_buttom.jsp"/>
<script>
    Wade.setRatio();
    $.course.init();
</script>
</body>
</html>
