(function($){
    $.extend({courseware:{
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["courseTree"] = new Wade.Tree("courseTree");
                $("#courseTree").textAction(function(e, nodeData){
                    $("#COURSE_NAME").val(nodeData.text);
                    $("#COURSE_ID").val(nodeData.id);
                    backPopup(document.getElementById('UI-COURSE'));
                });
                $.ajaxPost('initCoursewareQuery',null,function(data){
                    var trees = data.COURSEWARE;
                    if(trees != null){
                        window["courseTree"].data = trees;
                        window["courseTree"].init();
                        window["courseTree"].expandByPath("-1", "●");
                    }
                });
            },

            drawCourseware : function(datas){
                $("#coursewares").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    var content = $("#message_content");
                    content.empty();
                    var html = [];
                    html.push("sorry,没有找到您要的课件");
                    $.insertHtml('beforeend', content, html.join(""));
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' ontap=\"$.courseware.openCourseware(\'"+data.get("STORAGE_PATH")+"\',\'"+data.get("FILE_ID")+"\')\"><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("归属课程："+data.get("COURSE_NAME"));
                    html.push("</div><div class='content'>上传时间："+data.get("CREATE_DATE"));
                    html.push("</div></div>")
                    html.push("<div class=\"side e_size-m\">");
                    html.push(data.get("FILE_TYPE"));
                    html.push("</div></div></div></li>");
                }

                $.insertHtml('beforeend', $("#coursewares"), html.join(""));
            },

            query : function() {
                var parameter = $.buildJsonData("queryArea");
                $.ajaxPost('queryCourseware', parameter, function (data) {
                    hidePopup('UI-popup','UI-popup-query-cond');
                    var rst = new Wade.DataMap(data);
                    $.courseware.drawCourseware(rst.get("COURSEWARE"));
                });
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
            }
        }});
})($);