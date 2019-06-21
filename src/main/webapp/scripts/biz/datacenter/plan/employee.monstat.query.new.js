var EmployeeMonStatQueryNew = {
	init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup",{
            visible:false,
            mask:true
        });

        // window["COND_START_DATE"] = new Wade.DateField(
        //     "COND_START_DATE",
        //     {
        //         dropDown:true,
        //         format:"yyyy-MM-dd",
        //         useTime:false,
        //     }
        // );
        var now = $.date.now();
        var nowYYYYMM = $.date.now().substring(0,4) + $.date.now().substring(5,7);
        window["COND_MON_DATE"] = new Wade.DateField(
            "COND_MON_DATE",
            {
                value:nowYYYYMM,
                dropDown:true,
                format:"yyyyMM",
                useTime:false,
                useMode:'month',
            }
        );

        window["dailysheetTable"] = new Wade.Table("dailysheetTable", {
            fixedMode:true,
            fixedLeftCols:1,
            editMode:false
        });

        $('#COND_MON_DATE').val(nowYYYYMM);

        $.ajaxPost('initMyControlEnterprise',null,function(data){
            var rst = new Wade.DataMap(data);
            var enterprises = rst.get("ENTERPRISES");

            if(enterprises != null){
                var length = enterprises.length;
                var html=[];
                html.push("<li class=\"link e_center\" ontap=\"EmployeeMonStatQueryNew.afterSelectEnterprise(\'\',\'所有分公司\')\"><div class=\"main\">所有分公司</div></li>");
                for(var i=0;i<length;i++){
                    var enterprise = enterprises.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"EmployeeMonStatQueryNew.afterSelectEnterprise(\'"+enterprise.get("ORG_ID")+"\',\'"+enterprise.get("NAME")+"\')\"><div class=\"main\">"+enterprise.get("NAME")+"</div></li>");
                }
                $.insertHtml('beforeend', $("#BIZ_ENTERPRISE"), html.join(""));
            }
        });

        EmployeeMonStatQueryNew.queryEmployeeDailySheetList(nowYYYYMM);
    },
    queryEmployeeDailySheetList : function(month, houseCounselorName, orgId) {
	    $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url : 'datacenter/plan/queryEmployeeMonthSheet2',
            data : {
                EMPLOYEE_ID : Employee.employeeId,
                MONTH : month,
                HOUSE_COUNSELOR_NAME : houseCounselorName,
                ORG_ID : orgId
            },
            successFunc : function(data) {
                $.endPageLoading();
                $('#dailysheetTable tbody').html('');
                $.each(data.EMPLOYEE_DAILYSHEET_LIST, function(idx, employeeDailySheet) {
                    employeeDailySheet._className = "no";
                    dailysheetTable.addRow(employeeDailySheet);
                });
            },
            errorFunc : function(resultCode, resultInfo) {
                $.endPageLoading();
            }
        });
    },

    afterSelectEnterprise : function(value, text){
        backPopup(document.getElementById("UI-ENTERPRISE"));
        $("#ENTERPRISE_TEXT").val(text);
        $("#ENTERPRISE").val(value);
        if(value == ""){
            return;
        }
        $.ajaxPost('initMyControlShop','&ENTERPRISE='+value,function(data){
            var obj = new Wade.DataMap(data);

            var shops = obj.get("SHOPS");
            if(shops != null){
                var length = shops.length;
                var html = [];
                for(var i=0;i<length;i++){
                    var shop = shops.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"EmployeeMonStatQueryNew.afterSelectShop(\'"+shop.get("ORG_ID")+"\',\'"+shop.get("NAME")+"\')\"><div class=\"main\">"+shop.get("NAME")+"</div></li>");
                }
                $.insertHtml('beforeend', $("#BIZ_SHOP"), html.join(""));
            }
        });
    },

    afterSelectShop : function(value, text){
        backPopup(document.getElementById('UI-SHOP'));
        $("#SHOP_TEXT").val(text);
        $("#SHOP").val(value);
    },

    clickQueryButton : function() {
        QueryCondPopup.showQueryCond(function(monDate, houseCounselorName, orgId) {
            $('#QUERY_COND_TEXT').val(monDate);
            EmployeeMonStatQueryNew.queryEmployeeDailySheetList(monDate, houseCounselorName, orgId);
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
        var monDate = $('#COND_MON_DATE').val();
        var enterpriseId = $("#ENTERPRISE").val();
        var shopId = $("#SHOP").val();

        var orgId = '';
        if(shopId != '') {
            orgId = shopId;
        }
        else {
            orgId = enterpriseId;
        }

        var houseCounselorName = $('#COND_HOUSE_COUNSELOR_NAME').val();
        hidePopup(obj);
        if(QueryCondPopup.callback) {
            QueryCondPopup.callback(monDate, houseCounselorName, orgId);
        }
    }
};