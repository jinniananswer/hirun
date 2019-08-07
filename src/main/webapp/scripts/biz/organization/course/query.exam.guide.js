(function($){
    $.extend({exam:{
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initExamGuideQuery',null,function(data){
                    var rst = $.DataMap(data);
                    $.exam.drawExamGuide(rst.get("EXAM_GUIDE_LIST"));
                });
            },

            drawExamGuide : function(datas){
                $("#exams").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' ontap=\"$.exam.openCourseware(\'"+data.get("STORAGE_PATH")+"\',\'"+data.get("FILE_ID")+"\')\"><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
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

                $.insertHtml('beforeend', $("#exams"), html.join(""));
            },

            query : function() {
                var parameter = $.buildJsonData("queryArea");
                $.ajaxPost('queryExamGuide', parameter, function (data) {
                    hidePopup('UI-popup','UI-popup-query-cond');
                    var rst = new Wade.DataMap(data);
                    $.exam.drawExamGuide(rst.get("EXAM_GUIDE_LIST"));
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
                else if($.os.pad) {
                    $.redirect.open('redirectToViewPdf?FILE_ID='+fileId,'资料详情');
                }
                else{
                    $.redirect.open('redirectToViewFile?FILE_ID='+fileId,'资料详情');
                }
            }
        }});
})($);