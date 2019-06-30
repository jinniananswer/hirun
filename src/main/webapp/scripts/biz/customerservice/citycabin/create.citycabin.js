(function($){
    $.extend({createCityCabin:{
            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });

                window["COND_START_DATE"] = new Wade.DateField(
                    "COND_START_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["COND_END_DATE"] = new Wade.DateField(
                    "COND_END_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

            },




            submit : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.beginPageLoading();
                    $.ajaxPost('createCityCabin', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("新增城市木屋成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
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