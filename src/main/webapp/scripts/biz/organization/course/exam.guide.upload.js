(function($){
    $.extend({exam:{
            init : function() {

            },

            fileChange : function() {
                var file = $("#EXAM_GUIDE_FILE").attr("value");
                $("#FILE_NAME").val(file);
            },

            submit : function() {
                var file = $("#FILE_NAME").val();
                var fileType = file.substring(file.lastIndexOf("."));

                if("|.doc|.docx|.xls|.xlsx|.ppt|.pptx|".indexOf(fileType) < 0) {
                    alert("不支持的文件类型，请重新上传");
                    return false;
                }

                var formData = new FormData($("#submitForm")[0]);
                var request = new XMLHttpRequest();
                request.open( "POST", "uploadExamGuideFile" , true );
                request.onload = function(oEvent) {
                    if (request.status == 200) {
                        MessageBox.success("考试指南文件上传成功","点击确定关闭当前页面", function(btn){
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