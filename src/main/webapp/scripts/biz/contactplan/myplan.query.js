var myPlanQuery = {
    init : function() {
        window["PLAN_DATE"] = new Wade.DateField(
            "PLAN_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );

        var now = $.date.now();
        $('#PLAN_DATE').val(now);
        myPlanQuery.queryMyPlanList(now);

        $("#PLAN_DATE").bind("afterAction", function(){
            var $obj = $(this);
            myPlanQuery.queryMyPlanList($obj.val());
        });
    },
    queryMyPlanList : function(planDate) {
        $.beginPageLoading("查询计划中。。。");
        $.ajaxReq({
            url : 'plan/queryPlanDetail',
            data : {
                PLAN_EXECUTOR_ID : Employee.employeeId,
                PLAN_DATE : planDate
            },
            type : 'GET',
            successFunc : function(data) {
                $.endPageLoading();
                $('#plan_base').html(template('planBaseTemplate', data.PLAN_INFO));
                $('#plan_datail').html(template("finishInfoTemplate", data));
            },
            errorFunc : function(resultCode, resultInfo) {
                $.endPageLoading();
                alert('查询计划失败：' + resultInfo);
            }
        })
    },
    queryDetail : function(obj) {
        planListQuery.stopPropagation = true;

        var $obj = $(obj)
        var employeeId = $obj.attr('employee_id');

        $.redirect.open("biz/operations/contactplan/employee_dailysheet_detail_query.jsp?EXECUTOR_ID="+employeeId,"家装顾问日报表详情");
    },
};