var custCounselorChange = {
    init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup",{
            visible:false,
            mask:true
        });

        /*
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
        */

        custCounselorChange.queryCustList({});

        $("#HOUSE_SEARCH_TEXT").keydown(function(){
            if(event.keyCode == "13") {
                housesPopup.searchHouses($(this).val())
            }
        });
    },
    queryCustList : function(param) {
        param.CUST_STATUS = '1,7';
        if(!param.HOUSE_COUNSELOR_IDS) {
            param.TOP_EMPLOYEE_ID = Employee.employeeId;
        }

        $.ajaxReq({
            url : 'cust/queryCustList4TopEmployeeId',
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
        param.HOUSE_ID = $('#HOUSES_NAME').attr('houses_id');
        delete param.EMPLOYEE_NAMES;
        delete param.HOUSES_NAME;
        custCounselorChange.queryCustList(param);
        hidePopup(obj);
    },
    showCustDetail : function(obj) {
        var $obj = $(obj);
        var url = 'biz/operations/cust/custdetail_query.jsp?custId=' + $obj.attr('cust_id');
        $.redirect.open(url, $obj.attr('cust_name'));
    },
    selectCounselor : function(obj) {
        counselorPopup.initCounselorPopup(obj, 'mutil', function(selectedEmployeeList) {
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
    clickCustCounselorChange : function(obj) {
        var $obj = $(obj);
        custCounselorChangePopup.showCustCounselorChangePopup($obj.attr('cust_id'), function() {

        })
    },
    selectHouses : function (obj) {
        housesPopup.showHousesPopup(obj, function(housesId, housesName) {
            $('#HOUSES_NAME').val(housesName);
            $('#HOUSES_NAME').attr('houses_id', housesId);
        })
    }
}

var custCounselorChangePopup = {
    callback : '',
    custId : '',
    showCustCounselorChangePopup : function (custId, callback) {
        custCounselorChangePopup.custId = custId;
        if(callback) custCounselorChangePopup.callback;

        showPopup('UI-popup', 'CustCounselorChangePopupItem');

    },
    selectCounselor : function(obj) {
        counselorPopup.initCounselorPopup(obj, 'single', function(employee) {
            $('#EMPLOYEE_NAME').val(employee.EMPLOYEE_NAME);
            $('#EMPLOYEE_NAME').attr('employee_id',employee.EMPLOYEE_ID);
        })
    },
    confirm : function (obj) {
        if($.validate.verifyAll("changeForm")) {
            var param = $.buildJsonData('changeForm');
            delete param.EMPLOYEE_NAME;
            param.NEW_EMPLOYEE_ID = $('#EMPLOYEE_NAME').attr('employee_id');
            param.CUST_ID = custCounselorChangePopup.custId;

            $.beginPageLoading("变更家装顾问中。。。。。。")
            $.ajaxReq({
                url : 'cust/changeCounselor',
                data : param,
                type : 'POST',
                successFunc : function (data) {
                    $.endPageLoading();
                    backPopup(obj);
                },
                errorFunc : function (resultCode, resultInfo) {
                    $.endPageLoading();
                    alert("变更家装顾问失败：" + resultInfo);
                }
            })

        }
    },
}

var counselorPopup = {
    callback : '',
    init : true,
    mode : '',
    initCounselorPopup : function(obj, mode, callback) {
        if(!mode) {
            counselorPopup.mode = 'single';
        } else {
            counselorPopup.mode = mode;
        }
        // if(counselorPopup.init) {
        $.ajaxReq({
            url : 'employee/getAllSubordinatesCounselors',
            data : {
                EMPLOYEE_IDS : Employee.employeeId,
                COLUMNS : 'EMPLOYEE_ID,NAME'
            },
            successFunc : function(data) {
                var myEmployeeInfo = {};
                myEmployeeInfo.EMPLOYEE_ID = Employee.employeeId;
                myEmployeeInfo.NAME = Employee.employeeName;
                data.EMPLOYEE_LIST.push(myEmployeeInfo);
                $('#BIZ_COUNSELORS').html(template("employee_template", data));
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        })

        counselorPopup.init = false;

        if(callback && callback != '') counselorPopup.callback = callback;

        if(counselorPopup.mode == 'single') {
            $('#submitPart').hide();
        } else {
            $('#submitPart').show();
        }
        // } else {
        //
        // }

        forwardPopup(obj,'counselorPopupItem');
    },
    clickEmployee : function(obj) {
        if(counselorPopup.mode == 'single') {
            counselorPopup._clickEmployee4Single(obj);
        } else {
            counselorPopup._clickEmployee4Mutil(obj);
        }
    },
    _clickEmployee4Single : function(obj) {
        var $obj = $(obj);
        var employeeName = $obj.attr('employee_name');
        var employeeId = $obj.attr('employee_id');

        var employee = {};
        employee.EMPLOYEE_ID = employeeId;
        employee.EMPLOYEE_NAME = employeeName;

        if(counselorPopup.callback) counselorPopup.callback(employee);

        backPopup(obj);
    },
    _clickEmployee4Mutil : function(obj) {
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

var housesPopup = {
    callback : '',
    showHousesPopup : function(obj, callback) {
        if(callback) housesPopup.callback = callback;

        forwardPopup(obj,'housesPopupItem');
    },
    searchHouses : function(housesName) {
        $.ajaxReq({
            url : 'houses/queryHousesByName',
            data : {
                HOUSES_NAME : housesName
            },
            successFunc : function (data) {
                $('#BIZ_HOUSES').html(template('houses_template', data))
            }
        })
    },
    clickHouses : function(obj) {
        var $obj = $(obj);
        var housesId = $obj.attr('houses_id');
        var housesName = $obj.attr('houses_name');

        if(housesPopup.callback) housesPopup.callback(housesId, housesName);

        backPopup(obj);
    },
}