(function($){
    $.extend({train:{
            init : function() {
                $.ajaxPost('initViewTrainNotice','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    var train = rst.get("TRAIN");

                    $("#train_date").html(train.get("START_DATE")+"至"+train.get("END_DATE"));
                    var schedule = rst.get("SCHEDULE");
                    if (schedule != null && schedule != "undefined") {
                        $("#start_sign").html(schedule.get("START_DATE")+"至"+schedule.get("END_DATE"));
                    }

                    var employeeName = rst.get("EMPLOYEE_NAME");
                    var mobileNo = rst.get("MOBILE_NO");
                    if (employeeName != null && employeeName != "undefined") {
                        $("#charge_employee").html("本期带班组织："+employeeName+"； 联系电话："+mobileNo+";");
                    }
                    let selfEmployeeName = rst.get("SELF_EMPLOYEE_NAME");
                    let now = rst.get("NOW");
                    if (selfEmployeeName != null && selfEmployeeName != "undefined") {
                        $("#employee").html(selfEmployeeName);
                        $("#self_employee").html("知晓人：" + selfEmployeeName);
                    }

                    if (now != null && now != "undefined") {
                        $("#now").html(now);
                    }
                });
            }
        }});
})($);