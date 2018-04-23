var actionList = [
	{"ACTION_CODE":"ZX","ACTION_NAME":"咨询","PLAN_NUM":"3","REAL_NUM":"2"},
	{"ACTION_CODE":"JW","ACTION_NAME":"加微","PLAN_NUM":"5","REAL_NUM":"4"},
	{"ACTION_CODE":"GZHGZ","ACTION_NAME":"公众号关注","PLAN_NUM":"6","REAL_NUM":"3"},
	{"ACTION_CODE":"LTZDSTS","ACTION_NAME":"蓝图指导书推送","PLAN_NUM":"6","REAL_NUM":"3"},
	{"ACTION_CODE":"GZJRQLC","ACTION_NAME":"关注进入全流程","PLAN_NUM":"6","REAL_NUM":"3"},
	{"ACTION_CODE":"XQLTYTS","ACTION_NAME":"需求蓝图一推送","PLAN_NUM":"6","REAL_NUM":"3"},
	{"ACTION_CODE":"DKCSMU","ACTION_NAME":"带看城市木屋","PLAN_NUM":"6","REAL_NUM":"3"},
	{"ACTION_CODE":"YJALTS","ACTION_NAME":"一键案例推送","PLAN_NUM":"6","REAL_NUM":"3"},
];
var planSummarize = {
    init : function() {
        currentAction : '',
		window["myPopup"] = new Wade.Popup("myPopup",{
			visible:false,
			mask:true
		});
    },
    selectCust : function(obj) {
        selectCust.showSelectCust(function (data) {
            planSummarize.afterSelectedCust2(obj, data);
		});
	},
    afterSelectedCust2 : function(obj, data) {
    	var $obj = $(obj);
    	var id = $obj.attr('id');
        var factCustNum = data.custList.length;
        var factCustDetail = '';
        if(factCustNum > 0) {
			for(var i = 0; i < data.custList.length; i++) {
                factCustDetail += data.custList[i].custName + ',';
			}
		}

        $('#' + id + ' span[tag=factCustNum]').html('实际完成数：' + factCustNum);
        $('#' + id + ' span[tag=factCustDetail]').html('实际客户：' + factCustDetail);
    },
}