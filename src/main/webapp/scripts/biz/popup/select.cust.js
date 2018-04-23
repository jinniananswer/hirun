var selectCust = {
    callBack : '',
    init : function () {
        window["selectCustPopup"] = new Wade.Popup("selectCustPopup",{
            visible:false,
            mask:true
        });
    },
    showSelectCust : function (callBack) {
        if(callBack) {
            selectCust.callBack = callBack;
        }
        var ds=$.DatasetList([
            {"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"李四","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"王五","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"赵六","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"张三3","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
        ]);
        ds.bind('CUST_LIST','-webkit-inline-box');

        showPopup('selectCustPopup','customerSelectPopup');
    },
    queryCust : function(obj) {
        var ds=$.DatasetList([
            {"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"李四","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"王五","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"赵六","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"张三2","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
            {"CUST_NAME":"张三3","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
        ]);
        ds.bind('CUST_LIST','-webkit-inline-box');

        backPopup(obj);
    },
    confirmCusts : function(obj) {
        var custNameList = getCheckedValues('selectCustBox').split(",");

        var newCustNum = ($("#newCustNum").val());

        var custName = '';
        if(newCustNum > 0) {
            custName += '新客户*' + newCustNum + ",";
        };

        var custList = [];
        for(var i = 0; i < custNameList.length; i++) {
            var custDetail = {};
            custDetail.custName = custNameList[i];
            custList.push(custDetail);
        }

        var data = {
            newCustNum: newCustNum,
            custList: custList,
        };

        if(selectCust.callBack) {
            selectCust.callBack(data);
        }

        backPopup(obj);
    }
}