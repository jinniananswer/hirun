var PlanDailyQuery = {
	init : function() {
        // var data = {};
        // var companyList = [
        //     {"NAME":"长沙鸿扬", "EMPLOYEE_LIST" : [{"EMPLOYEE_NAME":"小安"},{"EMPLOYEE_NAME":"小金"}]},
        //     {"NAME":"株洲鸿扬", "EMPLOYEE_LIST" : [{"EMPLOYEE_NAME":"校花"},{"EMPLOYEE_NAME":"小红"}]},
        // ];
        // data.COMPANY_LIST = companyList;
        //
        // $('#unEntryPlanList').html(template('unEntryPlanListTemplate', data));

        PlanDailyQuery.queryUnEntryPlanEmployeeList();
    },
    queryUnEntryPlanEmployeeList : function() {
	    $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url : 'datacenter/plan/queryUnEntryPlanList',
            data : {

            },
            successFunc : function(data) {
                $.endPageLoading();
                $('#unEntryPlanList').html(template('unEntryPlanListTemplate', data));
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