var custContactEntry = {
    init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup",{
            visible:false,
            mask:true
        });

        custContactPopup.init();

        $.ajaxReq({
            url : 'queryHousesByEmployeeId',
            data : {
                EMPLOYEE_ID : Employee.employeeId
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

        var param = {};
        param.HOUSE_COUNSELOR_ID = Employee.employeeId;
        custContactEntry.queryCustList(param);
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
        param.HOUSE_COUNSELOR_ID = Employee.employeeId;
        custContactEntry.queryCustList(param);
        hidePopup(obj);
    },
    showCustDetail : function(obj) {
        var $obj = $(obj);
        var url = 'biz/operations/cust/custdetail_query.jsp?custId=' + $obj.attr('cust_id');
        $.redirect.open(url, $obj.attr('cust_name'));
    },
    custTraceClick : function(obj) {
        var $obj = $(obj);
        custContactPopup.showPopup($obj.attr('cust_id'));
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
            {"VALUE":"LTZDSTS","TEXT":"蓝图指导书推送"},
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
            custContactParam.EMPLOYEE_ID = Employee.employeeId;

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

