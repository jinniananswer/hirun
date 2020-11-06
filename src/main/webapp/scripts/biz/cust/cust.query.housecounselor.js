var actionList = [
    {"ACTION_CODE": "JW", "ACTION_NAME": "加微"},
    {"ACTION_CODE": "LTZDSTS", "ACTION_NAME": "客户产品需求库使用指导书推送"},
    {"ACTION_CODE": "GZHGZ", "ACTION_NAME": "公众号关注"},
    {"ACTION_CODE": "HXJC", "ACTION_NAME": "核心接触"},
    {"ACTION_CODE": "SMJRQLC", "ACTION_NAME": "扫码进入全流程"},
    {"ACTION_CODE": "XQLTYTS", "ACTION_NAME": "需求蓝图一推送"},
    {"ACTION_CODE": "ZX", "ACTION_NAME": "咨询"},
    {"ACTION_CODE": "DKCSMU", "ACTION_NAME": "带看城市木屋"},
    {"ACTION_CODE": "YJALTS", "ACTION_NAME": "一键案例推送"},
];
var custQuery4HouseCounselor = {
    init: function () {
        window["UI-popup"] = new Wade.Popup("UI-popup", {
            visible: false,
            mask: true
        });

        window["COND_START_DATE"] = new Wade.DateField(
            "COND_START_DATE",
            {
                dropDown: true,
                format: "yyyy-MM-dd",
                useTime: false,
            }
        );
        window["COND_END_DATE"] = new Wade.DateField(
            "COND_END_DATE",
            {
                dropDown: true,
                format: "yyyy-MM-dd",
                useTime: false,
            }
        );

        window["custTable"] = new Wade.Table("custTable", {
            fixedMode: true,
            fixedLeftCols: 1,
            editMode: false
        });

        // $('#MON_DATE').val(nowYYYYMM);
        // $("#MON_DATE").bind("afterAction", function(){
        //     var $obj = $(this);
        //     EmployeeMonStatQueryNew.queryEmployeeDailySheetList($obj.val());
        // });

        // custQuery4HouseCounselor.queryCustList();
    },
    queryCustList: function () {
        $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url: 'cust/queryCustAction4HouseCounselor',
            data: {
                START_DATE: $('#COND_START_DATE').val() ? $('#COND_START_DATE').val() + " 00:00:00" : "",
                END_DATE: $('#COND_END_DATE').val() ? $('#COND_END_DATE').val() + " 23:59:59" : "",
                FINISH_ACTION: $('#ACTION_NAME').attr('action_code'),
                HOUSE_COUNSELOR_IDS: $('#EMPLOYEE_NAMES').attr('employee_ids'),
                CUST_NAME: encodeURI($('#COND_CUST_NAME').val()),
                WX_NICK: encodeURI($('#COND_WX_NICK').val()),
            },
            type: 'GET',
            successFunc: function (data) {
                var rst = new Wade.DataMap(data);
                var datas = rst.get("RESULT");

                $.endPageLoading();
                $('#custTable tbody').html('');
                custQuery4HouseCounselor.drawCustomerInfo(datas);
                /*                $.each(data.RESULT, function(idx, cust) {
                                    cust._className = "no";
                                    custTable.addRow(cust);
                                });*/

            },
            errorFunc: function (resultCode, resultInfo) {
                $.endPageLoading();
            }
        });
    },
    selectCounselor: function (obj) {
        counselorPopup.initCounselorPopup(obj, function (selectedEmployeeList) {
            var employeeId = '';
            var employeeName = '';
            $.each(selectedEmployeeList, function (idx, employee) {
                employeeId += employee.EMPLOYEE_ID;
                employeeName += employee.EMPLOYEE_NAME;
                if (idx + 1 != selectedEmployeeList.length) {
                    employeeId += ',';
                    employeeName += ',';
                }
            })

            if (selectedEmployeeList.length > 2) {
                employeeName = selectedEmployeeList.length + '个家装顾问';
            }

            $('#EMPLOYEE_NAMES').val(employeeName);
            $('#EMPLOYEE_NAMES').attr('employee_ids', employeeId);
        });
    },

    drawCustomerInfo: function (datas) {
        $.endPageLoading();
        $('#custTable tbody').html('');

        let length = datas.length;
        for (let i = 0; i < length; i++) {
            let data = datas.get(i);
            let openMidCount=data.get("OPENCOUNT");

            if(openMidCount=="undefined" || openMidCount ==null ){
                openMidCount='0';
            }
            custTable.addRow({
                "_className": "no",
                "CUST_NAME": data.get("CUST_NAME"),
                "NAME": data.get("NAME"),
                "JW_NUM": data.get("JW_NUM"),
                "JW_LAST_TIME": data.get("JW_LAST_TIME"),
                "LTZDSTS_NUM": data.get("LTZDSTS_NUM"),
                "LTZDSTS_LAST_TIME": data.get("LTZDSTS_LAST_TIME"),
                "GZHGZ_LAST_TIME": data.get("GZHGZ_LAST_TIME"),
                "HXJC_LAST_TIME": data.get("HXJC_LAST_TIME"),
                "SMJRQLC_NUM": data.get("SMJRQLC_NUM"),
                "SMJRQLC_LAST_TIME": data.get("SMJRQLC_LAST_TIME"),
                "XQLTYTS_NUM": "<span class='e_red'  ontap='custQuery4HouseCounselor.showCustomerBluePrintDetail(\""+data.get("CUST_ID")+"\",\""+data.get("HOUSE_COUNSELOR_ID")+"\");'>"+data.get("XQLTYTS_NUM")+"</span>",
                "XQLTYTS_LAST_TIME": data.get("XQLTYTS_LAST_TIME"),
                "ZX_LAST_TIME": data.get("ZX_LAST_TIME"),
                "YJALTS_NUM": data.get("YJALTS_NUM"),
                "YJALTS_LAST_TIME": data.get("YJALTS_LAST_TIME"),
                "DKCSMU_LAST_TIME": data.get("DKCSMU_LAST_TIME"),
                "OPEN_COUNT":"<span class='e_red' >"+openMidCount+"</span>",
                "WX_NICK": data.get("WX_NICK"),
                "FIRST_PLAN_DATE":data.get("FIRST_PLAN_DATE")
            });
        }
    },

    export :function(){
       let startDate= $('#COND_START_DATE').val() ? $('#COND_START_DATE').val() + " 00:00:00" : "";
       let   endDate= $('#COND_END_DATE').val() ? $('#COND_END_DATE').val() + " 23:59:59" : "";
       let finishAction= $('#ACTION_NAME').attr('action_code');
       let houseCounelorIds= $('#EMPLOYEE_NAMES').attr('employee_ids');
       let custName=$('#COND_CUST_NAME').val();
       let wxNick= $('#COND_WX_NICK').val();

        var param='START_DATE='+startDate+"&END_DATE="+endDate+"&FINISH_ACTION="+finishAction+"&HOUSE_COUNSELOR_IDS="+houseCounelorIds
            +"&CUST_NAME="+encodeURI(encodeURI(custName))+"&WX_NICK="+encodeURI(encodeURI(wxNick));

        window.location.href = "cust/exportCustomerInfo4Counselor?"+param;
    },

    showCustomerBluePrintDetail:function(customerId,employeeId){
        console.log(customerId,employeeId);
        $.ajaxPost('showCustomerBluePrintDetail', '&CUSTOMER_ID=' + customerId+'&HOUSE_COUNSELOR_ID='+employeeId, function (data) {
            let rst = new Wade.DataMap(data);
            let xqltyInfo = rst.get("PROJECTXQLTYINFO");
            custQuery4HouseCounselor.drawXQLTY(xqltyInfo);

            showPopup('UI-popup', 'XQLTYUI-popup-query-cond');
        });
    },


    drawXQLTY: function (datas) {
        $("#xqltyinfo").empty();
        var html = [];

        if (datas == null || datas.length <= 0) {
            $("#messagebox").css("display", "");
            return;
        }

        $("#messagebox").css("display", "none");


        var length = datas.length;
        for (var i = 0; i < length; i++) {
            var data = datas.get(i);
            var name = data.get("NAME");
            var age = data.get("AGE");
            var housekind = data.get("HOUSE_KIND");
            var style = data.get("STYLE");
            var func = data.get("FUNC");
            var appa = data.get("APPLICATION");
            var housearea = data.get("HOUSE_AREA");
            var modetime = data.get("MODE_TIME");
            var actionCode = data.get("ACTION_CODE");
            let ltType = actionCode.split("_")[1];

            if (name == "undefined" || name == null)
                name = "";
            if (age == "undefined" || age == null)
                age = "";
            if (housekind == "undefined" || housekind == null)
                housekind = "";
            if (style == "undefined" || style == null)
                style = "";
            if (func == "undefined" || func == null)
                func = "";
            if (appa == "undefined" || appa == null)
                appa = "";
            if (housearea == "undefined" || housearea == null)
                appa = "";
            if (modetime == "undefined" || modetime == null)
                modetime = "";

            html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
            html.push("</div></div>");
            html.push("<div class=\"main\"><div class=\"title title-auto\">");
            if (ltType == 'A' || ltType == '' || ltType == undefined) {
                html.push("需求蓝图一A");
            } else if (ltType == 'B') {
                html.push("需求蓝图一B");
            } else if (ltType == 'C') {
                html.push("需求蓝图一C");
            }
            html.push("</div>");
            html.push("<div class='content content-auto'>户型：");
            html.push(housekind);
            html.push("</div>");
            html.push("<div class='content content-auto'>面积：");
            html.push(housearea);
            html.push("</div>");
            html.push("<div class='content content-auto'>用途：");
            html.push(appa);
            html.push("</div>");
            html.push("<div class='content content-auto'>保存时间：");
            html.push(modetime);
            html.push("</div>");
            html.push("<div class='content content-auto'>风格：");
            html.push(style);
            html.push("</div>");

            html.push("<div class='content content-auto'>功能：");
            html.push(func);
            html.push("</div>");
            html.push("</div>");
            html.push("</div></div></li>");
        }

        $.insertHtml('beforeend', $("#xqltyinfo"), html.join(""));
    },

    clickQueryButton: function () {
        QueryCondPopup.showQueryCond(function () {
            custQuery4HouseCounselor.queryCustList();
        });
    },
    initActionPopup: function (obj) {
        var data = {"ACTION_LIST": actionList};
        $('#actionPopupItem_ACTIONLIST').html(template("action_template", data));
        forwardPopup(obj, 'actionPopupItem');
    },
    afterSelectAction: function (obj) {
        var $obj = $(obj);
        var actionName = $obj.attr('ACTION_NAME');
        var actionCode = $obj.attr('ACTION_CODE');

        $('#ACTION_NAME').val(actionName);
        $('#ACTION_NAME').attr('action_code', actionCode);

        backPopup(obj);
    }
};

var QueryCondPopup = {
    callback: '',
    showQueryCond: function (callback) {
        if (callback) QueryCondPopup.callback = callback;

        showPopup('UI-popup', 'QueryCondPopupItem');
    },
    confirm: function (obj) {
        var startDate = $('#COND_START_DATE').val();
        var endDate = $('#COND_END_DATE').val();
        hidePopup(obj);
        if (QueryCondPopup.callback) {
            QueryCondPopup.callback(startDate, endDate);
        }
    }
};

var counselorPopup = {
    callback: '',
    init: true,
    counselors: null,
    initCounselorPopup: function (obj, callback) {
        if (counselorPopup.init) {
            $.ajaxReq({
                url: 'employee/getAllSubordinatesCounselors',
                data: {
                    EMPLOYEE_IDS: Employee.employeeId,
                    COLUMNS: 'EMPLOYEE_ID,NAME'
                },
                successFunc: function (data) {
                    // var myEmployeeInfo = {};
                    // myEmployeeInfo.EMPLOYEE_ID = Employee.employeeId;
                    // myEmployeeInfo.NAME = Employee.employeeName;
                    // data.EMPLOYEE_LIST.push(myEmployeeInfo);
                    counselorPopup.counselors = data;
                    $('#BIZ_COUNSELORS').html(template("employee_template", data));
                },
                errorFunc: function (resultCode, resultInfo) {

                }
            })

            counselorPopup.init = false;

            if (callback && callback != '') counselorPopup.callback = callback;
        } else {

        }

        forwardPopup(obj, 'counselorPopupItem');
    },
    clickEmployee: function (obj) {
        var $obj = $(obj);
        var employeeId = $obj.attr('employee_id');
        var selected = $obj.attr("selected");
        if (selected && selected == "true") {
            //已有，表示取消动作
            var ico = $("#COUNSELOR_" + employeeId + "_ico");
            ico.remove();
            $obj.attr("selected", "false");
        } else {
            //没有，表示新添加
            $obj.attr("selected", "true");
            var label = $("#LABEL_" + employeeId);
            var html = [];
            html.push("<div class=\"side\" id=\"COUNSELOR_" + employeeId + "_ico\"><span class=\"e_ico-ok e_ico-pic e_ico-pic-xxxs\"></span></div>");
            $.insertHtml('beforeend', label, html.join(""));
        }
    },

    queryEmployee: function () {
        if (!counselorPopup.counselors.EMPLOYEE_LIST) {
            return;
        }
        if (counselorPopup.counselors.EMPLOYEE_LIST.length <= 0) {
            return;
        }

        let name = $("#SEARCH_TEXT").val();

        let result = [];
        for (let i = 0; i < counselorPopup.counselors.EMPLOYEE_LIST.length; i++) {
            let counselor = counselorPopup.counselors.EMPLOYEE_LIST[i];
            if (counselor.NAME.indexOf(name) >= 0) {
                result.push(counselor);
            }
        }
        let data = {};
        data.EMPLOYEE_LIST = result;
        $('#BIZ_COUNSELORS').html(template("employee_template", data));
    },

    confirm: function (obj) {
        var selectedEmployeeList = [];
        $('#BIZ_COUNSELORS li[tag=li_employee]').each(function (idx, item) {
            var $item = $(item);
            var selected = $item.attr('selected');
            if (selected && selected == "true") {
                var employee = {};
                employee.EMPLOYEE_ID = $item.attr('employee_id');
                employee.EMPLOYEE_NAME = $item.attr('employee_name');
                selectedEmployeeList.push(employee);
            }
        })

        if (counselorPopup.callback) {
            counselorPopup.callback(selectedEmployeeList);
        }

        backPopup(obj);
    },
    clear: function (obj) {
        $('#BIZ_COUNSELORS li[tag=li_employee]').each(function (idx, item) {
            var $item = $(item);
            var selected = $item.attr('selected');
            var employeeId = $item.attr('employee_id');
            if (selected && selected == "true") {
                var ico = $("#COUNSELOR_" + employeeId + "_ico");
                ico.remove();
                $item.attr("selected", "false");
            }
        })
    }
};