(function($){
    $.extend({changeCityCabin:{
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

                $.ajaxPost('initChangeCityCabin','&CITY_CABIN_ID='+$("#CITY_CABIN_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    var citycabininfo = rst.get("CITYCABININFO");
                    $.changeCityCabin.setCitycabinValue(citycabininfo);
                });
            },

            setCitycabinValue : function(citycabininfo) {
                $("#SHOP").val(citycabininfo.get("SHOP"));
                $("#CUSTNAME").val(citycabininfo.get("CUST_NAME"));
                $("#HOUSE_ADDRESS").val(citycabininfo.get("CITYCABIN_ADDRESS"));
                $("#HOUSE_BUILDING").val(citycabininfo.get("CITYCABIN_BUILDING"));
                $("#HOUSE_ROOM").val(citycabininfo.get("CITYCABIN_ROOM"));
                $("#STYLE").val(citycabininfo.get("CITYCABIN_TITLE"));
                $("#HOUSE_AREA").val(citycabininfo.get("AREA"));
                $("#DESINGER").val(citycabininfo.get("DESIGNER"));
                $("#CUSTOMERSERVICE").val(citycabininfo.get("CUSTSERVICE"));
                $("#PROJECT_MANAGER").val(citycabininfo.get("PROJECT_MANAGER"));
                $("#COSTS").val(citycabininfo.get("COSTS"));
                $("#CONTACT").val(citycabininfo.get("CONTACT"));
                $("#COND_START_DATE").val((citycabininfo.get("SCAN_START_TIME")).substr(0,10));
                $("#COND_END_DATE").val((citycabininfo.get("SCAN_END_TIME")).substr(0,10));
                $("#IS_PPT").val(citycabininfo.get("IS_HAVE_PPT"));
                $("#REMARK").val(citycabininfo.get("REMARK"));

            },



            submit : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.beginPageLoading();
                    $.ajaxPost('changeCityCabin', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("城市木屋修改成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
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