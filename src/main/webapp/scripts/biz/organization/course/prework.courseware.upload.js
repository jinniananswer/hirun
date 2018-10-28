(function($){
    $.extend({courseware:{
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initPreworkCourseUpload',null,function(data){
                    var rst = $.DataMap(data);
                    $.courseware.drawCourseType(rst.get("COURSE"));
                });
            },

            drawCourseType : function(courses) {
                var html=[];
                var length = courses.length;
                for(var i=0;i<length;i++){
                    var course = courses.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"$.courseware.afterSelectCourse(\'"+course.get("COURSE_ID")+"\',\'"+course.get("NAME")+"\')\"><div class=\"main\">"+course.get("NAME")+"</div></li>");
                }
                $.insertHtml('beforeend', $("#COURSE_LIST"), html.join(""));
            },

            afterSelectCourse : function(id, name) {
                $("#COURSE_ID").val(id);
                $("#NAME").val(name);
                hidePopup('UI-popup','COURSE_SELECT');
            },

            fileChange : function() {
                var file = $("#COURSE_FILE").attr("value");
                $("#FILE_NAME").val(file);
            },

            submit : function() {
                var formData = new FormData($("#submitForm")[0]);
                var request = new XMLHttpRequest();
                request.open( "POST", "uploadPreworkCourseware" , true );
                request.onload = function(oEvent) {
                    if (request.status == 200) {
                        MessageBox.success("岗前学习资料上传成功","点击确定关闭当前页面", function(btn){
                            $.redirect.closeCurrentPage();
                        });
                    } else {
                        MessageBox.error("错误信息","对不起，偶们的系统出错了，55555555555555,亲，赶紧联系管理员报告功能问题吧");
                    }
                };
                request.send(formData);
            }
        }});
})($);