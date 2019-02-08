(function($){
    $.extend({train:{
            init : function() {
                window["START_DATE"] = new Wade.DateField(
                    "START_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["END_DATE"] = new Wade.DateField(
                    "END_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );
            },

            submit : function() {
                if($.validate.verifyAll("trainArea")) {
                    var parameter = $.buildJsonData("allSubmitArea");
                    $.beginPageLoading();
                    $.ajaxPost('createPreworkEvaluation', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("创建岗前考评通知成功","点击确定返回创建岗前考评通知页面，点击取消关闭当前页面", function(btn){
                            if("ok" == btn) {
                                document.location.reload();
                            }
                            else {
                                $.redirect.closeCurrentPage();
                            }
                        },{"cancel":"取消"})
                    });

                }
            }
        }});
})($);