(function($){
    $.extend({course:{
            treeId : null,
            treeName : null,
            dataId : null,
            hasChild : null,

            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["courseTree"] = new Wade.Tree("courseTree");
                $("#courseTree").textAction(function(e, nodeData){
                    $.course.treeId = nodeData.id;
                    $.course.treeName = nodeData.text;
                    $.course.dataId = nodeData.dataid;
                    $.course.hasChild = nodeData.haschild;
                    return false;
                });

                $("#courseTree").expandAction(function(e, nodeData) {
                    $.course.treeId = nodeData.id;
                    $.course.treeName = nodeData.text;
                    $.course.dataId = nodeData.dataid;
                    $.course.hasChild = nodeData.haschild;
                    return true;
                });

                $.ajaxPost('initCourseManage',null,function(data) {
                    var trees = data.COURSE_TREE;

                    if(trees != null){
                        window["courseTree"].data = trees;
                        window["courseTree"].init();
                        window["courseTree"].expandByPath("-1", "●");
                    }
                });
            },

            initCreateCourse : function() {
                if(this.treeId != null) {
                    $("#PARENT_COURSE_ID").val(this.treeId);
                    $("#PARENT_COURSE_NAME").val(this.treeName);
                    showPopup('UI-popup','UI-CREATE_COURSE');
                }
                else {
                    alert("请先选中某一课程，才能添加子课程");
                }
            },

            createCourse : function() {
                if($.validate.verifyAll("createCourseArea")) {
                    var parameter = $.buildJsonData("createCourseArea");
                    $.ajaxPost('createCourse', parameter, function (data) {
                        var newCourseId = data.NEW_COURSE_ID;
                        var courseTree = window["courseTree"];
                        var courseDataId = $.course.dataId + "●" + newCourseId;
                        courseTree.append({"id":newCourseId, "text":$("#COURSE_NAME").val(), "value":newCourseId, "showcheck":"false", "dataid":courseDataId, "expand":"false", "haschild":"false"}, $.course.dataId);
                        $.course.treeId = newCourseId;
                        $.course.treeName = $("#COURSE_NAME").val();


                        //清空创建课程框中的内容
                        $("#COURSE_NAME").val("");
                        $("#PARENT_COURSE_ID").val("");
                        $("#PARENT_COURSE_NAME").val("");
                        hidePopup('UI-popup','UI-CREATE_COURSE');
                    });
                }
            },

            initChangeCourse : function() {
              if(this.treeId != null && this.treeId != "-1") {
                  $.ajaxPost('initChangeCourse',"&COURSE_ID="+$.course.treeId,function(data){
                      var course = data.COURSE;
                      $("#CHANGE_COURSE_NAME").val(course.NAME);
                      $("#CHANGE_COURSE_DESC").val(course.COURSE_DESC);
                      $("#CHANGE_COURSE_ID").val(course.COURSE_ID);
                      $("#CHANGE_PARENT_COURSE_ID").val(course.PARENT_COURSE_ID);
                      $("#CHANGE_PARENT_COURSE_NAME").val(course.PARENT_COURSE_NAME);
                      showPopup('UI-popup','UI-CHANGE_COURSE');
                  });
              }
            },

            changeCourse : function() {
                if($.validate.verifyAll("changeCourseArea")) {
                    var parameter = $.buildJsonData("changeCourseArea");
                    $.ajaxPost('changeCourse', parameter, function (data) {
                        hidePopup('UI-popup','UI-CHANGE_COURSE');
                    });
                }
            },

            deleteCourse : function() {
                if(this.treeId == null) {
                    alert("请先选中要删除的课程");
                    return;
                }

                if(this.treeId == -1) {
                    alert("根节点不能删除");
                    return;
                }
                if(this.hasChild == "true") {
                    if(window.confirm("该课程下还有其它子课程，将删除该课程和其下所有的子课程，确定删除吗？")) {

                    }
                    else {
                        return;
                    }
                }

                $.ajaxPost('deleteCourse',"&COURSE_ID="+$.course.treeId,function(data){
                    window["courseTree"].remove($.course.dataId);
                    $.course.dataId = null;
                    $.course.treeId = null;
                    $.course.treeName = null;
                    $.course.hasChild = null;
                });

            },

            initUploadCourse : function() {
                if(this.treeId == null) {
                    alert("请先选择需要上传文件的课程");
                    return false;
                }
                if(this.treeId == -1) {
                    alert("根结点鸿扬课程体系下不能上传文件");
                    return false;
                }
                $("#UPLOAD_COURSE_ID").val(this.treeId);
                $("#UPLOAD_COURSE_NAME").val(this.treeName);
                showPopup('UI-popup','UI-UPLOAD_COURSE');
            },

            fileChange : function(index) {
                var file = $("#COURSE_FILE"+index).attr("value");
                var index = file.lastIndexOf(".");
                if(index < 0){
                    alert("上传的文件没有扩展名，不能上传");
                    return false;
                }
                var fileType = file.substr(index + 1);
                if(fileType != 'pdf') {
                    alert("上传的文件只能是PDF类型的");
                    return false;
                }
                $("#FILE_NAME"+index).val(file);
            },

            addFile : function() {
                var ul = $("#uploadCourseArea");
                var fileLength = ul.children().length - 1;

                var html = [];
                html.push("<li class=\"link\">");
                html.push("<div class=\"label\">选择课程文件</div>");
                html.push("<div class=\"value\">");
                html.push("<span class=\"e_mix\" ontap=\"$('#COURSE_FILE"+fileLength+"').trigger('click')\">");
                html.push("<input id=\"FILE_NAME"+fileLength+"\" name=\"FILE_NAME\" type=\"text\" value=\"\" readonly=\"true\"/>");
                html.push("<span class=\"e_ico-browse\"></span>");
                html.push("</span>");
                html.push("<input type=\"file\" id=\"COURSE_FILE"+fileLength+"\" accept='.pdf' name=\"COURSE_FILE\" style=\"display:none\" onchange=\"$.course.fileChange("+fileLength+")\"/>");
                html.push("</div>");
                html.push("</li>");

                $.insertHtml('beforeend', $("#uploadCourseArea"), html.join(""));
            },

            uploadCourse : function() {
                var file = $("#COURSE_FILE0").val();
                if(file == ""){
                    alert("上传的文件不能为空");
                    return;
                }

                var formData = new FormData($("#uploadForm")[0]);
                var request = new XMLHttpRequest();
                request.open( "POST", "uploadCourse" , true );
                request.onload = function(oEvent) {
                    hidePopup('UI-popup','UI-UPLOAD_COURSE');
                    if (request.status == 200) {
                        alert("上传文件成功");
                    } else {
                        alert("上传文件失败")
                    }
                };
                request.send(formData);
            },

            viewDetail : function() {
                if(this.treeId == null) {
                    alert("请先选择需要查看的课程");
                    return false;
                }
                if(this.treeId == -1) {
                    alert("根结点无需查看");
                    return false;
                }
                $.redirect.open('redirectToCourseDetail?COURSE_ID='+this.treeId, this.treeName+'课程详情');
            }
        }});
})($);