var custListQuery = {
    init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup",{
            visible:false,
            mask:true
        });

        custContactPopup.init();

        $.ajaxReq({
            url : 'queryHouses',
            data : {

            },
            successFunc : function(data) {
                var options = [];
                $.each(data.HOUSES_LIST, function(idx, house) {
                    options.push({TEXT : house.NAME, VALUE : house.HOUSES_ID})
                })
                $.Select.append(
                    "queryCustParamForm_house_container",
                    // 参数设置
                    {
                        id:"queryCustParamForm_house",
                        name:"HOUSE_ID",
                    },
                    options
                );
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        })

        custListQuery.queryCustList({});
    },
    queryCustList : function(param) {
        param.CUST_STATUS = '1,7';
        $.ajaxReq({
            url : 'cust/queryCustList',
            data : param,
            type : 'GET',
            successFunc : function(data) {
                $('#cust_list').html(template("cust_template", data));
            },
            errorFunc : function(resultCode, resultInfo) {
                alert(resultInfo);
            }
        })
    },
    queryCustList4Cond : function(obj) {
        var param = $.buildJsonData("queryCustParamForm");
        param.HOUSE_COUNSELOR_IDS = $('#EMPLOYEE_NAMES').attr('EMPLOYEE_IDS');
        delete param.EMPLOYEE_NAMES;
        custListQuery.queryCustList(param);
        hidePopup(obj);
    },
    showCustDetail : function(obj) {
        var $obj = $(obj);
        var url = 'biz/operations/cust/custdetail_query.jsp?custId=' + $obj.attr('cust_id');
        $.redirect.open(url, $obj.attr('cust_name'));
    },
    deleteCust : function(custId) {
        $.beginPageLoading('客户删除中');
        $.ajaxReq({
            url : 'cust/deleteCustById',
            data : {
                CUST_ID : custId
            },
            type : 'POST',
            successFunc : function(data) {
                $.endPageLoading();
                $('#CUST_ID_' + custId).remove();
            },
            errorFunc : function (resultCode, reusltInfo) {
                $.endPageLoading();
            }
        })
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
    custTraceClick : function(obj) {
        var $obj = $(obj);
        custContactPopup.showPopup($obj.attr('cust_id'));
    }
}

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
}

var custContactPopup = {
    custId : '',
    callback : '',
    init : function () {
        window["AFTER_ACTION"] = new Wade.Segment("AFTER_ACTION",{
            disabled:false
        });

        var options = [
            {"VALUE":"JW","TEXT":"加微"},
            {"VALUE":"LTZDSTS","TEXT":"客户产品需求库使用指导书推送"},
            {"VALUE":"GZHGZ","TEXT":"公众号关注"},
            {"VALUE":"HXJC","TEXT":"核心接触"},
            {"VALUE":"SMJRQLC","TEXT":"扫码进入全流程"},
            {"VALUE":"XQLTYTS","TEXT":"需求蓝图一推送"},
            {"VALUE":"ZX","TEXT":"咨询"},
            {"VALUE":"DKCSMU","TEXT":"带看城市木屋"},
            {"VALUE":"YJALTS","TEXT":"一键案例推送"},
        ];
        $.Select.append(
            "custContactForm_action_container",
            {
                id:"REMIND_ACTION_CODE",
                name:"REMIND_ACTION_CODE",
                nullable : "no",
                desc : "提醒动作",
            },
            options
        );

        window["RESTORE_DATE"] = new Wade.DateField(
            "RESTORE_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );
        window["REMIND_DATE"] = new Wade.DateField(
            "REMIND_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );
        window["CONTACT_DATE"] = new Wade.DateField(
            "CONTACT_DATE",
            {
                dropDown:true,
                format:"yyyy-MM-dd",
                useTime:false,
            }
        );

        $("#AFTER_ACTION").change(function(){
            var value = this.value; // this.value 获取分段器组件当前值
            $('#custContactForm li[tag=remind]').hide();
            $('#custContactForm li[tag=pause]').hide();
            $('#REMIND_ACTION_CODE').attr('nullable', 'yes');
            $('#REMIND_DATE').attr('nullable', 'yes');
            $('#RESTORE_DATE').attr('nullable', 'yes');
            if(this.value == '1') {

            } else if(this.value == '2') {
                $('#custContactForm li[tag=remind]').css('display', '');
                $('#REMIND_ACTION_CODE').attr('nullable', 'no');
                $('#REMIND_DATE').attr('nullable', 'no');
            } else if(this.value == '3') {
                $('#custContactForm li[tag=pause]').css('display', '');
                $('#RESTORE_DATE').attr('nullable', 'no');
            }
        });
    },
    showPopup : function (custId, callback) {
        resetArea('custContactForm', true);
        showPopup('UI-popup', 'CustContactPopupItem');
        $('#CONTACT_DATE').val($.date.now());
        $('#AFTER_ACTION').val("1");
        $('#REMIND_ACTION_CODE').val('');
        $('#custContactForm li[tag=remind]').hide();
        $('#custContactForm li[tag=pause]').hide();
        $('#REMIND_ACTION_CODE').attr('nullable', 'yes');
        $('#REMIND_DATE').attr('nullable', 'yes');
        $('#RESTORE_DATE').attr('nullable', 'yes');

        custContactPopup.custId = custId;
        if(callback) custContactPopup.callback = callback;
    },
    confirm : function (obj) {
        if($.validate.verifyAll("custContactForm")) {
            var custContactParam = $.buildJsonData("custContactForm");
            custContactParam.CUST_ID = custContactPopup.custId;

            $.ajaxReq({
                url : 'cust/addCustContact',
                data : custContactParam,
                type : 'POST',
                successFunc : function (data) {
                    hidePopup(obj);
                    if(custContactPopup.callback) custContactPopup.callback(custContactPopup.custId);
                },
                errorFunc : function (resultCode, resultInfo) {
                    alert(resultInfo);
                }
            })
        }
    }
}

