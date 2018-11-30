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
                    MessageBox.alert("请先选中某一课程，才能添加子课程");
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
                        $.course.hasChild = "true";

                        //清空创建课程框中的内容
                        $("#COURSE_NAME").val("");
                        $("#PARENT_COURSE_ID").val("");
                        $("#PARENT_COURSE_NAME").val("");
                        hidePopup('UI-popup','UI-CREATE_COURSE');
                        MessageBox.success("创建子课程成功");
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
                        var html = [];
                        html.push($("#CHANGE_COURSE_NAME").val());
                        var parent = $("#courseTree○"+$.course.dataId);
                        var children = parent.children();
                        var changeNode = $(children[1]);
                        changeNode.empty();
                        $.insertHtml('beforeend', $(changeNode), html.join(""));
                        hidePopup('UI-popup','UI-CHANGE_COURSE');
                        MessageBox.success("修改课程成功");
                    });
                }
            },

            deleteCourse : function() {
                if(this.treeId == null) {
                    MessageBox.alert("请先选中要删除的课程");
                    return;
                }

                if(this.treeId == -1) {
                    MessageBox.alert("根节点不能删除");
                    return;
                }

                var message = "确定要删除该课程吗？";
                if(this.hasChild == "true") {
                    message = "该课程下还有其它子课程，将删除该课程和其下所有的子课程，确定删除吗？";
                }

                MessageBox.confirm("提示信息",message, function(btn) {
                    if(btn == "ok") {
                        $.ajaxPost('deleteCourse',"&COURSE_ID="+$.course.treeId,function(data){
                            window["courseTree"].remove($.course.dataId);
                            $.course.dataId = null;
                            $.course.treeId = null;
                            $.course.treeName = null;
                            $.course.hasChild = null;
                            MessageBox.success("删除成功");
                        });
                    }
                });
            },

            initUploadCourse : function() {
                if(this.treeId == null) {
                    MessageBox.alert("请先选择需要上传文件的课程");
                    return false;
                }
                if(this.treeId == -1) {
                    MessageBox.alert("根结点鸿扬课程体系下不能上传文件");
                    return false;
                }
                $("#UPLOAD_COURSE_ID").val(this.treeId);
                $("#UPLOAD_COURSE_NAME").val(this.treeName);
                showPopup('UI-popup','UI-UPLOAD_COURSE');
            },

            fileChange : function(idx) {
                var file = $("#COURSE_FILE"+idx).attr("value");
                var index = file.lastIndexOf(".");
                if(index < 0){
                    MessageBox.alert("上传的文件没有扩展名，不能上传");
                    return false;
                }
                var fileType = file.substr(index + 1);
                if(fileType != 'pdf') {
                    MessageBox.alert("上传的文件只能是PDF类型的");
                    return false;
                }

                $("#FILE_NAME"+idx).val(file);
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
                    MessageBox.alert("上传的文件不能为空");
                    return;
                }
                $.beginPageLoading();
                var formData = new FormData($("#uploadForm")[0]);
                var request = new XMLHttpRequest();
                request.open( "POST", "uploadCourse" , true );
                request.onload = function(oEvent) {
                    hidePopup('UI-popup','UI-UPLOAD_COURSE');
                    $.endPageLoading();
                    if (request.status == 200) {
                        MessageBox.success("上传课件成功");
                    } else {
                        MessageBox.error("上传课件失败")
                    }

                };
                request.send(formData);
            },

            viewDetail : function() {
                if(this.treeId == null) {
                    MessageBox.alert("请先选择需要查看的课程");
                    return false;
                }
                if(this.treeId == -1) {
                    MessageBox.alert("根结点无需查看");
                    return false;
                }
                $.redirect.open('redirectToCourseDetail?COURSE_ID='+this.treeId, this.treeName+'课程详情');
            },

            initCourseFile : function() {
                if(this.treeId == null) {
                    MessageBox.alert("请先选择需要管理课件的课程");
                    return false;
                }
                if(this.treeId == -1) {
                    MessageBox.alert("根结点无需管理课件");
                    return false;
                }

                $.ajaxPost('initCourseFile',"&COURSE_ID="+$.course.treeId,function(data){
                    var rst = $.DataMap(data);
                    $.course.drawFile(rst.get("FILES"));
                });
            },

            drawFile : function(datas) {
                var html = [];
                var ul = $("#courseFileArea");
                ul.empty();

                if(datas == null || datas.length <= 0) {
                    showPopup('UI-popup','UI-COURSE_FILE');
                    $("#messagebox").css("display","");
                    return;
                }
                $("#messagebox").css("display","none");
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li>");
                    html.push("<div class='main'><div class='title link' ontap=\"$.redirect.open(\'redirectToViewFile?FILE_ID="+data.get("FILE_ID")+"\',\'课件详情\')\">");
                    html.push(data.get("NAME"));
                    html.push("</div></div>");
                    html.push("<div class='fn'><input type='checkbox' name='DELETE_FILE' value='"+data.get("FILE_ID")+"' />");
                    html.push("</div></li>");
                }
                $.insertHtml('beforeend', ul, html.join(""));
                showPopup('UI-popup','UI-COURSE_FILE');
            },

            deleteFile : function() {
                var files = $("#courseFileArea input");

                if(files == null || files.length <= 0) {
                    MessageBox.alert("没有要删除的课件");
                    return;
                }

                var length = files.length;
                var num = 0;
                for(var i=0;i<length;i++) {
                    var file = $(files[i]);
                    if(file.attr("checked")) {
                        num++;
                    }
                }
                if(num == 0) {
                    MessageBox.alert("请选中要删除的文件");
                    return;
                }

                var parameter = $.buildJsonData("courseFileArea");
                MessageBox.confirm("提示信息","确认要删除课件吗？", function(btn) {
                    if(btn == "ok") {
                        $.ajaxPost('deleteCourseFile',parameter,function(data){
                            hidePopup('UI-popup','UI-COURSE_FILE');
                            MessageBox.success("删除成功");
                        });
                    }
                });
            }
        }});
})($);