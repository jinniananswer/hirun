var EmployeeDailySheetQuery = {
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

        window["dailysheetTable"] = new Wade.Table("dailysheetTable", {
            fixedMode:true,
            fixedLeftCols:1,
            editMode:false
        });

        var now = $.date.now();
        $('#COND_START_DATE').val(now);
        $('#COND_END_DATE').val(now);

        // EmployeeDailySheetQuery.queryEmployeeDailySheetList(now);
    },
    queryEmployeeDailySheetList : function(startDate, endDate) {
	    $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url : 'datacenter/plan/queryEmployeeDaillySheet2',
            data : {
                EMPLOYEE_ID : Employee.employeeId,
                START_DATE : startDate,
                END_DATE : endDate
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
    clickQueryButton : function() {
        QueryCondPopup.showQueryCond(function(startDate, endDate) {
            $('#QUERY_COND_TEXT').val(startDate + "~" + endDate);
            EmployeeDailySheetQuery.queryEmployeeDailySheetList(startDate, endDate);
        });
    }
};

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