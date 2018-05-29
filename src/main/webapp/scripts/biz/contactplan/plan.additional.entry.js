var planAdditionalEntry = {
    type : '',
    init : function() {
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

            param = '?EXECUTOR_ID=' + $('#EMPLOYEE_ID').val();
            param += '&PLAN_DATE=' + $('#PLAN_DATE').val();
            top.$.index.openNav(url+param,title);
        }
    }
};