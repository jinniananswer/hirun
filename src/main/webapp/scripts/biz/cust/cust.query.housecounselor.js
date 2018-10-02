var actionList = [
    {"ACTION_CODE":"JW","ACTION_NAME":"加微"},
    {"ACTION_CODE":"LTZDSTS","ACTION_NAME":"蓝图指导书推送"},
    {"ACTION_CODE":"GZHGZ","ACTION_NAME":"公众号关注"},
    {"ACTION_CODE":"HXJC","ACTION_NAME":"核心接触"},
    {"ACTION_CODE":"SMJRQLC","ACTION_NAME":"扫码进入全流程"},
    {"ACTION_CODE":"XQLTYTS","ACTION_NAME":"需求蓝图一推送"},
    {"ACTION_CODE":"ZX","ACTION_NAME":"咨询"},
    {"ACTION_CODE":"DKCSMU","ACTION_NAME":"带看城市木屋"},
    {"ACTION_CODE":"YJALTS","ACTION_NAME":"一键案例推送"},
];
var custQuery4HouseCounselor = {
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

        window["custTable"] = new Wade.Table("custTable", {
            fixedMode:true,
            fixedLeftCols:1,
            editMode:false
        });

        // $('#MON_DATE').val(nowYYYYMM);
        // $("#MON_DATE").bind("afterAction", function(){
        //     var $obj = $(this);
        //     EmployeeMonStatQueryNew.queryEmployeeDailySheetList($obj.val());
        // });

        // custQuery4HouseCounselor.queryCustList();
    },
    queryCustList : function() {
	    $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url : 'cust/queryCustAction4HouseCounselor',
            data : {
                START_DATE : $('#COND_START_DATE').val() ? $('#COND_START_DATE').val() + " 00:00:00" : "",
                END_DATE : $('#COND_END_DATE').val() ? $('#COND_END_DATE').val() + " 23:59:59" : "",
                FINISH_ACTION : $('#ACTION_NAME').attr('action_code'),
                HOUSE_COUNSELOR_IDS : $('#EMPLOYEE_NAMES').attr('employee_ids'),
                CUST_NAME : $('#COND_CUST_NAME').val()
            },
            type : 'GET',
            successFunc : function(data) {
                $.endPageLoading();
                $('#custTable tbody').html('');
                $.each(data.RESULT, function(idx, cust) {
                    cust._className = "no";
                    custTable.addRow(cust);
                });
            },
            errorFunc : function(resultCode, resultInfo) {
                $.endPageLoading();
            }
        });
    },
    selectCounselor : function(obj) {
        counselorPopup.initCounselorPopup(obj, function(selectedEmployeeList) {
            var employeeId = '';
            var employeeName = '';
            $.each(selectedEmployeeList, function(idx, employee) {
                employeeId += employee.EMPLOYEE_ID;
                employeeName += employee.EMPLOYEE_NAME;
                if(idx+1 != selectedEmployeeList.length) {
                    employeeId += ',';
                    employeeName += ',';
                }
            })

            if(selectedEmployeeList.length > 2) {
                employeeName = selectedEmployeeList.length + '个家装顾问';
            }

            $('#EMPLOYEE_NAMES').val(employeeName);
            $('#EMPLOYEE_NAMES').attr('employee_ids', employeeId);
        });
    },
    clickQueryButton : function() {
        QueryCondPopup.showQueryCond(function() {
            custQuery4HouseCounselor.queryCustList();
        });
    },
    initActionPopup : function(obj) {
        var data = {"ACTION_LIST" : actionList};
        $('#actionPopupItem_ACTIONLIST').html(template("action_template", data));
        forwardPopup(obj,'actionPopupItem');
    },
    afterSelectAction : function(obj) {
        var $obj = $(obj);
        var actionName = $obj.attr('ACTION_NAME');
        var actionCode = $obj.attr('ACTION_CODE');

        $('#ACTION_NAME').val(actionName);
        $('#ACTION_NAME').attr('action_code',actionCode);

        backPopup(obj);
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

var counselorPopup = {
    callback : '',
    init : true,
    initCounselorPopup : function(obj, callback) {
        if(counselorPopup.init) {
            $.ajaxReq({
                url : 'employee/getAllSubordinatesCounselors',
                data : {
                    EMPLOYEE_IDS : Employee.employeeId,
                    COLUMNS : 'EMPLOYEE_ID,NAME'
                },
                successFunc : function(data) {
                    // var myEmployeeInfo = {};
                    // myEmployeeInfo.EMPLOYEE_ID = Employee.employeeId;
                    // myEmployeeInfo.NAME = Employee.employeeName;
                    // data.EMPLOYEE_LIST.push(myEmployeeInfo);
                    $('#BIZ_COUNSELORS').html(template("employee_template", data));
                },
                errorFunc : function(resultCode, resultInfo) {

                }
            })

            counselorPopup.init = false;

            if(callback && callback != '') counselorPopup.callback = callback;
        } else {

        }

        forwardPopup(obj,'counselorPopupItem');
    },
    clickEmployee : function(obj) {
        var $obj = $(obj);
        var employeeId = $obj.attr('employee_id');
        var selected = $obj.attr("selected");
        if(selected && selected == "true"){
            //已有，表示取消动作
            var ico = $("#COUNSELOR_"+employeeId+"_ico");
            ico.remove();
            $obj.attr("selected", "false");
        }
        else{
            //没有，表示新添加
            $obj.attr("selected", "true");
            var label = $("#LABEL_"+employeeId);
            var html=[];
            html.push("<div class=\"side\" id=\"COUNSELOR_"+employeeId+"_ico\"><span class=\"e_ico-ok e_ico-pic e_ico-pic-xxxs\"></span></div>");
            $.insertHtml('beforeend', label, html.join(""));
        }
    },
    confirm : function(obj) {
        var selectedEmployeeList = [];
        $('#BIZ_COUNSELORS li[tag=li_employee]').each(function(idx, item) {
            var $item = $(item);
            var selected = $item.attr('selected');
            if(selected && selected == "true") {
                var employee = {};
                employee.EMPLOYEE_ID = $item.attr('employee_id');
                employee.EMPLOYEE_NAME = $item.attr('employee_name');
                selectedEmployeeList.push(employee);
            }
        })

        if(counselorPopup.callback) {
            counselorPopup.callback(selectedEmployeeList);
        }

        backPopup(obj);
    },
    clear : function(obj) {
        $('#BIZ_COUNSELORS li[tag=li_employee]').each(function(idx, item) {
            var $item = $(item);
            var selected = $item.attr('selected');
            var employeeId = $item.attr('employee_id');
            if(selected && selected == "true") {
                var ico = $("#COUNSELOR_"+employeeId+"_ico");
                ico.remove();
                $item.attr("selected", "false");
            }
        })
    }
};