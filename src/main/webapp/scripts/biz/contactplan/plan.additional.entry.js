var planAdditionalEntry = {
    type : '',
    init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup");

        window["PLAN_DATE"] = new Wade.DateField(
            "PLAN_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );

        planAdditionalEntry.type = $.params.get('TYPE');
    },
    okOnclick : function () {
        if ($.validate.verifyAll("pre_info_form")) {
            var url = '';
            var param = '';
            var title = '';
            var planDate = $('#PLAN_DATE').val();
            if(planAdditionalEntry.type == '1') {
                url = 'biz/operations/contactplan/plan_entry.jsp';
                title = planDate+'计划补录';
            } else if(planAdditionalEntry.type == '2') {
                url = 'biz/operations/contactplan/plan_summarize.jsp';
                title = planDate+'计划总结补录';
            }

            param = '?EXECUTOR_ID=' + $('#EMPLOYEE_NAME').attr('employee_id');
            param += '&PLAN_DATE=' + $('#PLAN_DATE').val();
            if(planAdditionalEntry.type == '1') {
                param += '&IS_ADDITIONAL_RECORD=1'
            } else if(planAdditionalEntry.type == '2') {
                param += '&IS_ADDITIONAL_RECORD_SUMMARIZE=1'
            }
            $.redirect.open(url+param,title);
        }
    },
    initCounselorPopup : function() {
        $.ajaxReq({
            url : 'employee/getAllSubordinatesCounselors',
            data : {
                EMPLOYEE_IDS : Employee.employeeId,
                COLUMNS : 'EMPLOYEE_ID,NAME'
            },
            successFunc : function(data) {
                $('#BIZ_COUNSELORS').html(template("employee_template", data));
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        })

        showPopup('UI-popup','UI-popup-query');
    },
    afterSelectEmployee : function(obj) {
        var $obj = $(obj);
        var employeeName = $obj.attr('employee_name');
        var employeeId = $obj.attr('employee_id');

        $('#EMPLOYEE_NAME').val(employeeName);
        $('#EMPLOYEE_NAME').attr('employee_id',employeeId);

        hidePopup(obj);
    }
};