var EmployeeDailySheetDetailQuery = {
	init : function() {
        EmployeeDailySheetDetailQuery.queryDetail();
    },
    queryDetail : function(event) {
        var executorId = $.params.get('EXECUTOR_ID');
        $.ajaxReq({
            url : 'plan/queryEmployeeDailySheetDetail',
            data : {
                EXECUTOR_ID : executorId
            },
            successFunc : function(data) {
                $('#dailysheet_detail').html(template("dailysheet_detail_template", data));
                
            },
            errorFunc : function (resultCode, resultInfo) {
                
            }
        })
    },
};