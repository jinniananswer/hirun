
var employeeMonthTargetEntry = {
    operType : "single",
    actionList : [
        {"VALUE":"JW","TEXT":"加微"},
        {"VALUE":"LTZDSTS","TEXT":"蓝图指导书推送"},
        {"VALUE":"GZHGZ","TEXT":"公众号关注"},
        {"VALUE":"HXJC","TEXT":"核心接触"},
        {"VALUE":"SMJRQLC","TEXT":"扫码进入全流程"},
        {"VALUE":"XQLTYTS","TEXT":"需求蓝图一推送"},
        {"VALUE":"ZX","TEXT":"咨询"},
        {"VALUE":"DKCSMU","TEXT":"带看城市木屋"},
        {"VALUE":"YJALTS","TEXT":"一键案例推送"},
    ],
    init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup",{
            visible:false,
            mask:true
        });

        var now = $.date.now();
        var nowYYYYMM = $.date.now().substring(0,4) + $.date.now().substring(5,7);
        window['MON_DATE'] = new Wade.DateField(
            'MON_DATE',
            {
                value:nowYYYYMM,
                dropDown:true,
                format:"yyyyMM",
                useTime:false,
                useMode:'month',
            }
        );
        $('#MON_DATE').val(nowYYYYMM);

        monthTargetSettingPopup.init();

        employeeMonthTargetEntry.queryEmployeeList();


    },
    queryEmployeeList : function() {
        var now = $.date.now();
        var nowYYYYMM = $.date.now().substring(0,4) + $.date.now().substring(5,7);

        $.beginPageLoading("查询员工中。。。");
        $.ajaxReq({
            url : 'employee/getAllSubordinatesCounselors',
            data : {
                EMPLOYEE_IDS : Employee.employeeId,
                COLUMNS : 'EMPLOYEE_ID,NAME'
            },
            successFunc : function(data) {
                $.endPageLoading();
                // data.EMPLOYEE_LIST = [];
                // data.EMPLOYEE_LIST.push({EMPLOYEE_NAME : '小安', EMPLOYEE_ID : '157', HINT : '今日没有录计划'})
                $('#employee_list').html(template("employee_template", data));


            },
            errorFunc : function(resultCode, resultInfo) {
                $.endPageLoading();
            }
        })
    },
    queryCustList4Cond : function(obj) {
        var param = $.buildJsonData("queryCustParamForm");
        param.HOUSE_COUNSELOR_ID = Employee.employeeId;
        custContactEntry.queryCustList(param);
        hidePopup(obj);
    },
    showCustDetail : function(obj) {
        var $obj = $(obj);
        var url = 'biz/operations/cust/custdetail_query.jsp?custId=' + $obj.attr('cust_id');
        $.redirect.open(url, $obj.attr('cust_name'));
    },
    settingButtonOnClick : function(obj) {
        var $obj = $(obj);
        monthTargetSettingPopup.showPopup($obj.attr('employee_id'));
    },
    changeOperType : function() {
        if(employeeMonthTargetEntry.operType == 'single') {
            $('[oper_type=single]').hide();
            $('[oper_type=batch]').css('display', '');

            employeeMonthTargetEntry.operType = 'batch';
        } else {
            $('[oper_type=single]').css('display', '');
            $('[oper_type=batch]').hide();
            checkedAll('employeeBox', false);

            employeeMonthTargetEntry.operType = 'single';
        }
    },
    cancelBatch : function () {
        employeeMonthTargetEntry.changeOperType();
    },
    batchSetTarget : function () {
        var checkedNum = getCheckedBoxNum('employeeBox');
        if(checkedNum == 0) {
            alert('请先选择员工');
            return;
        }
        monthTargetSettingPopup.showPopup(getCheckedValues('employeeBox'));
    }
}

var monthTargetSettingPopup = {
    employeeIds : '',
    callback : '',
    init : function () {
        window["JW"] = new Wade.IncreaseReduce("JW", {
            disabled:false,
            step:1
        });

        window["LTZDSTS"] = new Wade.IncreaseReduce("LTZDSTS", {
            disabled:false,
            step:1
        });

        window["GZHGZ"] = new Wade.IncreaseReduce("GZHGZ", {
            disabled:false,
            step:1
        });

        window["HXJC"] = new Wade.IncreaseReduce("HXJC", {
            disabled:false,
            step:1
        });

        window["SMJRQLC"] = new Wade.IncreaseReduce("SMJRQLC", {
            disabled:false,
            step:1
        });

        window["XQLTYTS"] = new Wade.IncreaseReduce("XQLTYTS", {
            disabled:false,
            step:1
        });

        window["ZX"] = new Wade.IncreaseReduce("ZX", {
            disabled:false,
            step:1
        });

        window["DKCSMU"] = new Wade.IncreaseReduce("DKCSMU", {
            disabled:false,
            step:1
        });

        window["YJALTS"] = new Wade.IncreaseReduce("YJALTS", {
            disabled:false,
            step:1
        });
    },
    showPopup : function (employeeIds, callback) {
        // resetArea('custContactForm', true);
        showPopup('UI-popup', 'targetSettingPopup');

        monthTargetSettingPopup.employeeIds = employeeIds;
        if(callback) monthTargetSettingPopup.callback = callback;
    },
    confirm : function (obj) {
        if($.validate.verifyAll("targetForm")) {
            var target = $.buildJsonData("targetForm");

            var param = {};
            param.TARGET = JSON.stringify(target);
            param.OBJ_TYPE = "1";
            param.OBJS = monthTargetSettingPopup.employeeIds;
            param.TARGET_TIME_TYPE = '1';
            param.TARGET_TIME_VALUE = $('#MON_DATE').val();

            $.beginPageLoading("设置月度目标中。。。");
            $.ajaxReq({
                url : 'plan/setMonPlanTarget',
                data : param,
                type : 'POST',
                successFunc : function (data) {
                    $.endPageLoading();
                    hidePopup(obj);
                    if(monthTargetSettingPopup.callback) monthTargetSettingPopup.callback(custContactPopup.employeeIds);
                },
                errorFunc : function (resultCode, resultInfo) {
                    $.endPageLoading();
                    alert(resultInfo);
                }
            })
        }
    }
}

