var EmployeeDailySheetQuery = {
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

        window["dailysheetTable"] = new Wade.Table("dailysheetTable", {
            fixedMode:true,
            fixedLeftCols:1,
            editMode:false
        });

        var now = $.date.now();
        $('#COND_START_DATE').val(now);
        $('#COND_END_DATE').val(now);

        $.ajaxPost('initMyControlEnterprise',null,function(data){
            var rst = new Wade.DataMap(data);
            var enterprises = rst.get("ENTERPRISES");

            if(enterprises != null){
                var length = enterprises.length;
                var html=[];
                for(var i=0;i<length;i++){
                    var enterprise = enterprises.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"EmployeeDailySheetQuery.afterSelectEnterprise(\'"+enterprise.get("ORG_ID")+"\',\'"+enterprise.get("NAME")+"\')\"><div class=\"main\">"+enterprise.get("NAME")+"</div></li>");
                }
                $.insertHtml('beforeend', $("#BIZ_ENTERPRISE"), html.join(""));
            }
        });

        // EmployeeDailySheetQuery.queryEmployeeDailySheetList(now);
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
            $("#BIZ_SHOP").empty();
            if(shops != null){
                var length = shops.length;
                var html = [];
                for(var i=0;i<length;i++){
                    var shop = shops.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"EmployeeDailySheetQuery.afterSelectShop(\'"+shop.get("ORG_ID")+"\',\'"+shop.get("NAME")+"\')\"><div class=\"main\">"+shop.get("NAME")+"</div></li>");
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

    queryEmployeeDailySheetList : function(startDate, endDate, houseCounselorName, orgId) {
	    $.beginPageLoading("查询中。。。");
        $.ajaxReq({
            url : 'datacenter/plan/queryEmployeeDaillySheet2',
            data : {
                EMPLOYEE_ID : Employee.employeeId,
                START_DATE : startDate,
                END_DATE : endDate,
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
    clickQueryButton : function() {
        QueryCondPopup.showQueryCond(function(startDate, endDate, houseCounselorName, orgId) {
            $('#QUERY_COND_TEXT').val(startDate + "~" + endDate);
            EmployeeDailySheetQuery.queryEmployeeDailySheetList(startDate, endDate, houseCounselorName, orgId);
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
        var startDate = $('#COND_START_DATE').val();
        var endDate = $('#COND_END_DATE').val();
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
            QueryCondPopup.callback(startDate, endDate, houseCounselorName, orgId);
        }
    }
};