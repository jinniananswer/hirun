var holidayEntry = {
    type : '',
    init : function() {
        window["HOLIDAY_START_DATE"] = new Wade.DateField(
            "HOLIDAY_START_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );
        window["HOLIDAY_END_DATE"] = new Wade.DateField(
            "HOLIDAY_END_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );

        planAdditionalEntry.type = $.params.get('TYPE');
    },
    okOnclick : function () {
        var param = $.buildJsonData("queryCustParamForm");
        param.PLAN_EXECUTOR_ID = User.userId;
        if ($.validate.verifyAll("holidayForm")) {
            $.beginPageLoading("休假录入中。。。");
            $.ajaxReq({
                url : 'employee/entryHoliday',
                data : param,
                type : 'POST',
                successFunc : function (data) {
                    $.endPageLoading();
                    MessageBox.success("休假录入成功","点击【确定】关闭当前页面", function(btn){
                        if("ok" == btn) {
                            $.redirect.closeCurrentPage();
                        }
                    });
                },
                errorFunc : function (resultCode, resultInfo) {
                    $.endPageLoading();
                    alert('休假录入失败:' + resultInfo);
                }
            })
        }
    }
};