(function($){
    $.extend({courseware:{
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initPreworkCourseQuery',null,function(data){
                    var rst = $.DataMap(data);
                    $.courseware.drawCourseType(rst.get("COURSE"));
                    $.courseware.drawCourse(rst.get("COURSE_LIST"));
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

            afterSelectCourse : function(id, name) {
                $("#COURSE_ID").val(id);
                $("#NAME").val(name);
                backPopup(document.getElementById("UI-COURSE"));
            },

            query : function() {
                var parameter = $.buildJsonData("queryArea");
                $.ajaxPost('queryPreworkCourse', parameter, function (data) {
                    hidePopup('UI-popup','UI-popup-query-cond');
                    var rst = new Wade.DataMap(data);
                    $.courseware.drawCourse(rst.get("COURSE_LIST"));
                });
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