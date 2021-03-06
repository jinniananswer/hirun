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

                window["SIGN_END_DATE"] = new Wade.DateField(
                    "SIGN_END_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd HH:mm:ss",
                        useTime:true
                    }
                );

                $("#START_DATE").bind("afterAction", function(){
                    var startDate = new Date(this.value);
                    startDate.setDate(startDate.getDate() + 1);
                    var month = startDate.getMonth() + 1;
                    var day = startDate.getDate();
                    $("#END_DATE").val(startDate.getFullYear() + '-' + $.train.getFormatDate(month) + '-' + $.train.getFormatDate(day));
                });
            },

            getFormatDate :function(arg) {
                if (arg == undefined || arg == '') {
                    return '';
                }

                var re = arg + '';
                if (re.length < 2) {
                    re = '0' + re;
                }

                return re;
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