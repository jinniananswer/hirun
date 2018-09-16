var EmployeeMonStatQueryNew = {
	init : function() {
        // window["UI-popup"] = new Wade.Popup("UI-popup",{
        //     visible:false,
        //     mask:true
        // });

        // window["COND_START_DATE"] = new Wade.DateField(
        //     "COND_START_DATE",
        //     {
        //         dropDown:true,
        //         format:"yyyy-MM-dd",
        //         useTime:false,
        //     }
        // );
        var now = $.date.now();
        var nowYYYYMM = $.date.now().substring(0,4) + $.date.now().substring(5,7);
        window["MON_DATE"] = new Wade.DateField(
            "MON_DATE",
            {
                value:nowYYYYMM,
                dropDown:true,
                format:"yyyyMM",
                useTime:false,
                useMode:'month',
            }
        );

        window["dailysheetTable"] = new Wade.Table("dailysheetTable", {
            fixedMode:true,
            fixedLeftCols:1,
            editMode:false
        });

        $('#MON_DATE').val(nowYYYYMM);
        $("#MON_DATE").bind("afterAction", function(){
            var $obj = $(this);
            EmployeeMonStatQueryNew.queryEmployeeDailySheetList($obj.val());
        });

        EmployeeMonStatQueryNew.queryEmployeeDailySheetList(nowYYYYMM);
    },
    queryEmployeeDailySheetList : function(month) {
	    $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url : 'datacenter/plan/queryEmployeeMonthSheet2',
            data : {
                EMPLOYEE_ID : Employee.employeeId,
                MONTH : month,
            },
            successFunc : function(data) {
                $.endPageLoading();
                $('#dailysheetTable tbody').html('');
                $.each(data.EMPLOYEE_DAILYSHEET_LIST, function(idx, employeeDailySheet) {
                    employeeDailySheet._className = "no";
                    dailysheetTable.addRow(employeeDailySheet);
                });
            },
            errorFunc : function(resultCode, resultInfo) {
                $.endPageLoading();
            }
        });
    },
    // clickQueryButton : function() {
    //     QueryCondPopup.showQueryCond(function(startDate, endDate) {
    //         $('#QUERY_COND_TEXT').val(startDate + "~" + endDate);
    //         EmployeeDailySheetQuery.queryEmployeeDailySheetList(startDate, endDate);
    //     });
    // }
};

/*
var QueryCondPopup = {
    callback : '',
    showQueryCond : function(callback) {
        if(callback) QueryCondPopup.callback = callback;

        showPopup('UI-popup','QueryCondPopupItem');
    },
    confirm : function(obj) {
        var startDate = $('#COND_START_DATE').val();
        var endDate = $('#COND_END_DATE').val();
        hidePopup(obj);
        if(QueryCondPopup.callback) {
            QueryCondPopup.callback(startDate, endDate);
        }
    }
};
*/