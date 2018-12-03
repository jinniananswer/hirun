(function($){
    $.extend({teacher:{
            init : function() {
                $.ajaxPost('initTeacher','&TEACHER_ID='+$("#TEACHER_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    $.teacher.drawTeacher(rst.get("TEACHER"));
                });
            },

            drawTeacher : function(data) {
                $("#TEACHER_PIC").attr("src", "/doc/teacher/"+data.get("PIC"));
                var teacherInfo = $("#TEACHER_INFO");
                teacherInfo.empty();

                var html = [];
                html.push("<li>");
                html.push("<span class='label'>");
                html.push("讲师姓名：");
                html.push("</span>");
                html.push("<span class='value'>");
                html.push(data.get("TEACHER_NAME"));
                html.push("</span>");
                html.push("</li>");

                html.push("<li>");
                html.push("<span class='label'>");
                html.push("讲师类型：");
                html.push("</span>");
                html.push("<span class='value'>")
                var type = data.get("TYPE");
                if(type == "0") {
                    html.push("内部讲师");
                }
                else if(type == "1") {
                    html.push("外聘讲师");
                }
                html.push("</span>");
                html.push("</li>");

                html.push("<li>");
                html.push("<span class='label'>");
                html.push("讲师级别：");
                html.push("</span>");
                html.push("<span class='value'>")
                var level = data.get("LEVEL");
                if(level == "0") {
                    html.push("讲师");
                }
                else if(level == "1") {
                    html.push("高级讲师");
                }
                else if(level == "2") {
                    html.push("资深讲师");
                }
                else if(level == "3") {
                    html.push("特级讲师");
                }
                html.push("</span>");
                html.push("</li>");

                html.push("<li>");
                html.push("<span class='label'>");
                html.push("归属部门：");
                html.push("</span>");
                html.push("<span class='value'>");
                html.push(data.get("ORG_NAME"));
                html.push("</span>");
                html.push("</li>");

                html.push("<li>");
                html.push("<span class='label'>");
                html.push("岗位：");
                html.push("</span>");
                html.push("<span class='value'>");
                html.push(data.get("JOB_ROLE_NAME"));
                html.push("</span>");
                html.push("</li>");

                html.push("<li>");
                html.push("<span class='label'>");
                html.push("担任课程：");
                html.push("</span>");
                html.push("<span class='value'>");
                html.push(data.get("COURSE_NAME"));
                html.push("</span>");
                html.push("</li>");

                html.push("<li>");
                html.push("<span class='label'>");
                html.push("QQ号码：");
                html.push("</span>");
                html.push("<span class='value'>");
                html.push(data.get("QQ_NO"));
                html.push("</span>");
                html.push("</li>");

                html.push("<li>");
                html.push("<span class='label'>");
                html.push("微信号：");
                html.push("</span>");
                html.push("<span class='value'>");
                html.push(data.get("WECHAT_NO"));
                html.push("</span>");
                html.push("</li>");

                $.insertHtml('beforeend', teacherInfo, html.join(""));
            }
        }});
})($);