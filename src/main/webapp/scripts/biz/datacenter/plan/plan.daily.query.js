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
        window["myTab"] = new Wade.Tabset("myTab");
        PlanDailyQuery.queryPlanEmployeeList(0);

        $("#myTab").afterSwitchAction(function(e, idx){
            PlanDailyQuery.queryPlanEmployeeList(idx);
        });
    },
    queryPlanEmployeeList : function(idx) {
	    $.beginPageLoading("查询中。。。");
	    var url;
	    if(idx == 1) {
	        url = 'datacenter/plan/queryActivitiPlanList';
        } else if(idx == 2) {
	        url = 'datacenter/plan/queryHolidayPlanList';
        } else {
            url = 'datacenter/plan/queryUnEntryPlanList';
        }
        $.ajaxReq({
            url : url,
            data : {

            },
            successFunc : function(data) {
                $.endPageLoading();
                var id;
                if(idx == 1) {
                    id = 'activitiPlanList';
                } else if(idx == 2) {
                    id = 'holidayPlanList';
                } else {
                    id = 'unEntryPlanList';
                }

                var templateId = data.COMPANY_LIST.length ? 'planListTemplate' : 'noResultTemplate';
                $('#'+id).html(template(templateId, data));
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