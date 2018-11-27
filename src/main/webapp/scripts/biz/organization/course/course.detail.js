(function($){
    $.extend({courseware:{
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initCourseDetail',"&COURSE_ID="+$("#COURSE_ID").val(),function(data){
                    var rst = $.DataMap(data);
                    $.courseware.drawCourseDetail(rst.get("COURSE"));
                    $.courseware.drawCourse(rst.get("FILES"));
                });
            },

            drawCourseDetail : function(course) {
                var html=[];
                html.push(course.get("NAME"));
                $.insertHtml('beforeend', $("#COURSE_NAME"), html.join(""));

                html = [];
                html.push(course.get("COURSE_DESC"));
                $.insertHtml('beforeend', $("#COURSE_DESC"), html.join(""));

                html = [];
                html.push(course.get("PARENT_COURSE_NAME"));
                $.insertHtml('beforeend', $("#PARENT_COURSE_NAME"), html.join(""));
            },

            drawCourse : function(datas){
                $("#courses").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var sex = data.get("SEX");
                    html.push("<li class='link' ontap=\"$.courseware.openCourseware(\'"+data.get("STORAGE_PATH")+"\',\'"+data.get("FILE_ID")+"\')\"><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("归属课程："+data.get("COURSE_NAME"));
                    html.push("</div><div class='content'>");
                    html.push("</div></div>")
                    html.push("<div class=\"side e_size-m\">");
                    html.push(data.get("FILE_TYPE"));
                    html.push("</div></div></div></li>");
                }

                $.insertHtml('beforeend', $("#courses"), html.join(""));
            },

            openCourseware : function(url, fileId) {
                //$.redirect.open('redirectToViewFile?FILE_ID="+data.get("FILE_ID")+"','资料详情');
                if($.os.phone) {

                    if($.os.ios) {
                        try {
                            window.webkit.messageHandlers.openCourseware.postMessage(url);
                        } catch (err) {

                        }
                    }
                    else if($.os.android) {
                        document.location = "office://courseware?FILE_URL="+url;
                    }

                }
                else{
                    $.redirect.open('redirectToViewFile?FILE_ID='+fileId,'资料详情');
                }
            }
        }});
})($);