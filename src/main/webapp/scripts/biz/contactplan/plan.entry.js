var actionList = [
	{"ACTION_CODE":"ZX","ACTION_NAME":"咨询"},
	{"ACTION_CODE":"XQLTYTS","ACTION_NAME":"需求蓝图一推送"},
	{"ACTION_CODE":"SMJRQLC","ACTION_NAME":"扫码进入全流程"},
	{"ACTION_CODE":"HXJC","ACTION_NAME":"核心接触"},
	{"ACTION_CODE":"GZHGZ","ACTION_NAME":"公众号关注"},
	{"ACTION_CODE":"LTZDSTS","ACTION_NAME":"蓝图指导书推送"},
	{"ACTION_CODE":"JW","ACTION_NAME":"加微"},
	{"ACTION_CODE":"DKCSMU","ACTION_NAME":"带看城市木屋"},
	{"ACTION_CODE":"YJALTS","ACTION_NAME":"一键案例推送"},
];
var planEntry = {
	planActionList : [],
    init : function() {
        currentAction : '',
		window["myPopup"] = new Wade.Popup("myPopup",{
			visible:false,
			mask:true
		});
		
		window["newCustNum"] = new Wade.IncreaseReduce("newCustNum", {
			disabled:false,
			step:1
		});
		
		window["contactNum"] = new Wade.IncreaseReduce("contactNum", {
			disabled:false,
			step:1
		});
		
		window["adviceNum"] = new Wade.IncreaseReduce("adviceNum", {
			disabled:false,
			step:1
		});
		
		window["holidaySwitch"] = new Wade.Switch("holidaySwitch",{
			switchOn:false,
			onValue:"1",
			offValue:"0",
		});
		
		window["workMode"] = new Wade.Segment("workMode",{
			disabled:false
		});

		//默认值 开始
        planEntry.currentActionIndex = 0;
        $('#workMode').val(0);

		var actionDatasetList = $.DatasetList(actionList);
		actionDatasetList.bind('ACTION_LIST', 'flex');
		
		planEntry.setCurrentActionOn();
		
		$("#workMode").change(function(){
			var modeVal = this.value; // this.value 获取分段器组件当前值
			planActionList = [];//清空已选动作
			if(modeVal == '0') {
				$('#ACTION_PART').show();
			} else {
				$('#ACTION_PART').hide();
			}
		});
    },
	afterSelectedCust : function(obj) {
		if(!planEntry.checkSelectedCust()) {
			return;
		}
		
		var custNameList = getCheckedValues('selectCustBox');
		
		var template = $('#SELECTED_CUST').html();//获取模板的html
		var newCustNum = ($("#newCustNum").val());
				
		var custName = '';
		if(newCustNum > 0) {
//			for(var i = 0; i < newCustNum; i++) {
//				custName += '新客户' + parseInt(i+1) +',';
//			}
			custName += '新客户 * ' + newCustNum + ',';
		}
		custName += custNameList;
		backPopup(obj);
		
		var actionCode = actionList[planEntry.currentActionIndex].ACTION_CODE;
		$('#' + actionCode + ' div[tag=ACTION_NAME_CONTENT]').html(custName);
		
		//设置下一动作
		
		if(planEntry.currentActionIndex < actionList.length - 1) {
			planEntry.currentActionIndex = planEntry.currentActionIndex + 1;
			planEntry.setCurrentActionOn();
		} else {
			$('#' +actionCode).unbind('tap');
			$('#' + actionCode + ' span[tag=ACTION_NAME_TEXT]').removeClass("e_red").addClass('e_green');
			$('#' + actionCode + ' span[tag=ACTION_ICO_OK]').show();
			$('#' + actionCode + ' div[tag=ACTION_SIDE]').hide();
			$('#' + actionCode + ' div[tag=ACTION_MORE]').hide();
		}
	},
	afterPlanTargetSet : function(obj) {
		backPopup(obj);
		
		var ds=$.DataMap(
			{
				"contactNum" : $('#contactNum').val(),
				"adviceNum" : $('#adviceNum').val()
			}
		);
		ds.bind('planTarget','block');
		
		$('#ACTION_PART').show();
		
		var actionDatasetList = $.DatasetList(actionList);
		actionDatasetList.bind('ACTION_LIST', 'flex');
		
//		$('#ACTION_LIST')
		planEntry.setCurrentActionOn();
	},
	checkSelectedCust : function() {
		var flag = true;
		
		if(planEntry.getSelectedCustNum()  == 0) {
			alert('至少选择一个客户');
			flag = false;
		}
		
		return flag;
	},
	setCurrentActionOn : function () {
		if(planEntry.currentActionIndex > 0) {
			var oldActionCode = actionList[planEntry.currentActionIndex-1].ACTION_CODE;
			$('#' +oldActionCode).unbind('tap');
			$('#' + oldActionCode + ' span[tag=ACTION_NAME_TEXT]').removeClass("e_red").addClass('e_green');
			$('#' + oldActionCode + ' span[tag=ACTION_ICO_OK]').show();
			$('#' + oldActionCode + ' div[tag=ACTION_SIDE]').hide();
			$('#' + oldActionCode + ' div[tag=ACTION_MORE]').hide();
		}
		
		if(planEntry.currentActionIndex < actionList.length) {
			var actionCode = actionList[planEntry.currentActionIndex].ACTION_CODE;
			$('#' +actionCode).bind('tap', planEntry.selectCust);
			$('#' + actionCode + ' span[tag=ACTION_NAME_TEXT]').addClass("e_red");
			$('#' + actionCode + ' div[tag=ACTION_SIDE]').show();
			$('#' + actionCode + ' div[tag=ACTION_MORE]').show();
		}
		
	},
	getSelectedCustNum : function() {
		var custNum = getCheckedBoxNum('selectCustBox');
		var newCustNum = parseInt($("#newCustNum").val());
		
		return custNum + newCustNum;
	},
	selectCust : function () {
		if(planEntry.currentActionIndex == actionList.length) {
			alert('已到最后一个动作');
			return;
		}
		
		var currentActionCode = actionList[planEntry.currentActionIndex].ACTION_CODE;
		
		$("#newCustNum").val("0")
		
		var ds=$.DatasetList([
				{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"李四","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"王五","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"赵六","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
		]);
		ds.bind('CUST_LIST','-webkit-inline-box');
			
		showPopup('myPopup','customerSelectPopup');
	},
	queryCust : function(obj) {
		var ds=$.DatasetList([
				{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"李四","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"王五","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"赵六","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
				{"CUST_NAME":"张三2","SERIAL_NUMBER":"13467517522","HOUSE_INFO":"桔园小区1栋1001"},
		]);
		ds.bind('CUST_LIST','-webkit-inline-box');
		
		backPopup(obj);
	},
	setPlanTarget : function() {
		showPopup('myPopup','planTargetSetPopup');
	},
	getCustInfo : function(custId) {
		var custInfo = {};
		custInfo.custId = "123";
		custInfo.custName = "张三";
		custInfo.serialNumber = "13467517522";
		custInfo.houseInfo = "桔园小区1栋1001";
		custInfo.executedAction = [
			{"ACTION_NAME" : "加微", "DONE_DATE" : "2018-04-12"},
			{"ACTION_NAME" : "公众号关注", "DONE_DATE" : "2018-04-12"},
			{"ACTION_NAME" : "蓝图指导书推送", "DONE_DATE" : "2018-04-12"}
		]

		return custInfo;
	}
}