(function($){
    $.extend({train:{
            index : 0,
            currentIndex : 0,
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initTrainDetail','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    var trains = rst.get("TRAINS");
                    var schedules = rst.get("SCHEDULES");
                    $.train.drawTrains(trains);
                    $.train.drawSchedules(schedules);
                });
            },

            drawTrains : function(datas){
                $("#trains").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\" ontap='$.train.viewDetail(\""+data.get("TRAIN_ID")+"\");'><div class=\"title\">");
                    html.push(data.get("TRAIN_NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("起止时间："+data.get("START_DATE")+"~"+data.get("END_DATE"));
                    html.push("</div><div class='content content-auto'>"+"涉及课程："+data.get("COURSE_NAME"));
                    html.push("</div><div class='content'>班主任：");
                    html.push(data.get("EMPLOYEE_NAME"));
                    html.push("</div><div class='content content-auto'>培训描述：");
                    html.push(data.get("TRAIN_DESC"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>培训地址：");
                    html.push(data.get("TRAIN_ADDRESS"));
                    html.push("</div>");

                    html.push("<div class='content content-auto'>住宿地址：");
                    html.push(data.get("HOTEL_ADDRESS"));
                    html.push("</div>");

                    html.push("</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#trains"), html.join(""));
            },

            drawSchedules : function(datas) {
                $("#schedules").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");

                datas.eachKey(function(key, index, totalCount){

                    html.push("<div class='c_box c_box-border'><div class='c_title' ontap=\"$(this).next().toggle();\">");
                    html.push("<div class=\"text e_strong e_blue\">"+key+"</div>");
                    html.push("<div class=\"fn\">");
                    html.push("<ul>");
                    html.push("<li><span class='e_ico-unfold'></span></li>");
                    html.push("</ul>");
                    html.push("</div></div>");

                    html.push("<div class=\"l_padding l_padding-u\">");
                    html.push("<div class=\"c_list c_list-line c_list-border c_list-space l_padding\">");
                    html.push("<ul>");

                    var schedules = datas.get(key);
                    var length = schedules.length;
                    for(var i=0;i<length;i++) {
                        var data = schedules.get(i);
                        html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                        html.push("</div></div>");
                        html.push("<div class=\"main\" ontap='$.train.viewCourseFile(\""+data.get("COURSE_ID")+"\");'><div class=\"title\">");
                        html.push(data.get("START_DATE").substring(11,16)+"~"+data.get("END_DATE").substring(11,16)+": <span class='e_blue'>"+data.get("COURSE_NAME")+"</span>");
                        html.push("</div>");
                        var teacherName = data.get("TEACHER_NAME");
                        if(teacherName != null && teacherName != 'undefined') {
                            html.push("<div class=\"content\">");
                            html.push("讲师：");
                            html.push("<a href='javascript:' ontap='$.train.viewTeacher("+data.get("TEACHER_ID")+");'>"+teacherName+"</a>");
                            html.push("</div>");
                        }

                        html.push("</div>");
                        html.push("</div></div></li>");
                    }
                    html.push("</ul>");
                    html.push("</div>");
                    html.push("</div>");
                    html.push("</div>");
                    html.push("<div class='c_space'></div>");

                });

                $.insertHtml('beforeend', $("#schedules"), html.join(""));
            },

            viewCourseFile : function(courseId) {
                if(courseId == "-1") {
                    return;
                }
                $.ajaxPost('initCourseFile',"&COURSE_ID="+courseId,function(data){
                    var rst = $.DataMap(data);
                    $.train.drawFile(rst.get("FILES"));
                });
            },

            drawFile : function(datas) {
                var html = [];
                var ul = $("#courseFileArea");
                ul.empty();

                if(datas == null || datas.length <= 0) {
                    showPopup('UI-popup','UI-COURSE_FILE');
                    $("#messageboxFile").css("display","");
                    return;
                }
                $("#messageboxFile").css("display","none");
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' ontap=\"$.train.openCourseware('"+data.get("STORAGE_PATH")+"','"+data.get("FILE_ID")+"')\">");
                    html.push("<div class='main'><div class='title'>");
                    html.push(data.get("NAME"));
                    html.push("</div></div>");
                    html.push("<div class='fn'>");
                    html.push("</div></li>");
                }
                $.insertHtml('beforeend', ul, html.join(""));
                showPopup('UI-popup','UI-COURSE_FILE');
            },

            openCourseware : function(url, fileId) {
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
                else if($.os.pad) {
                    $.redirect.open('redirectToViewPdf?FILE_ID='+fileId,'资料详情');
                }
                else{
                    $.redirect.open('redirectToViewFile?FILE_ID='+fileId,'资料详情');
                }
            },

            viewTeacher : function(teacherId) {
                $.redirect.open('redirectToTeacherDetail?TEACHER_ID='+teacherId, '讲师详情');
            }
        }});
})($);