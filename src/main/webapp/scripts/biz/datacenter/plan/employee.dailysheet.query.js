var EmployeeDailySheetQuery = {
	init : function() {
        window["QUERY_DATE"] = new Wade.DateField(
            "QUERY_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );

        window["dailysheetTable"] = new Wade.Table("dailysheetTable", {
            fixedMode:true,
            editMode:false
        });

        var now = $.date.now();
        $('#QUERY_DATE').val(now);

        $("#QUERY_DATE").bind("afterAction", function(){
            var $obj = $(this);
            EmployeeDailySheetQuery.queryEmployeeDailySheetList($obj.val());
        });

        EmployeeDailySheetQuery.queryEmployeeDailySheetList(now);
    },
    queryEmployeeDailySheetList : function(date) {
	    $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url : 'datacenter/plan/queryEmployeeDaillySheet',
            data : {
                TOP_EMPLOYEE_ID : Employee.employeeId,
                QUERY_DATE : date
            },
            successFunc : function(data) {
                $.endPageLoading();
                $.each(data.EMPLOYEE_DAILYSHEET_LIST, function(idx, employeeDailySheet) {
                    employeeDailySheet._className = "no";
                    dailysheetTable.addRow(employeeDailySheet);
                });
            },
            errorFunc : function(resultCode, resultInfo) {
                $.endPageLoading();
            }
        });
        // var employeeDailySheet = {};
        // employeeDailySheet.EMPLOYEE_NAME = '安文轩';
        // employeeDailySheet.PLAN_JW = '安文轩';
        // employeeDailySheet.PLAN_LTZDSTS = '安文轩';
        // employeeDailySheet.PLAN_GZHGZ = '安文轩';
        // employeeDailySheet.PLAN_HXJC = '安文轩';
        // employeeDailySheet.PLAN_SMJRQLC = '安文轩';
        // employeeDailySheet.PLAN_XQLTYTS = '安文轩';
        // employeeDailySheet.PLAN_ZX = '安文轩';
        // employeeDailySheet.PLAN_YJALTS = '安文轩';
        // employeeDailySheet.PLAN_DKCSMU = '安文轩';
        // employeeDailySheet.FINISH_JW = '安文轩';
        // employeeDailySheet.FINISH_LTZDSTS = '安文轩';
        // employeeDailySheet.FINISH_GZHGZ = '安文轩';
        // employeeDailySheet.FINISH_HXJC = '安文轩';
        // employeeDailySheet.FINISH_SMJRQLC = '安文轩';
        // employeeDailySheet.FINISH_XQLTYTS = '安文轩';
        // employeeDailySheet.FINISH_ZX = '安文轩';
        // employeeDailySheet.FINISH_YJALTS = '安文轩';
        // employeeDailySheet.FINISH_DKCSMU = '安文轩';
        // dailysheetTable.addRow(employeeDailySheet);
        // dailysheetTable.addRow(employeeDailySheet);
    },
};