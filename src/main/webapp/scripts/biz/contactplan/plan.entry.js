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
var planEntry = {
	planActionMap : $.DataMap(),
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
				$('#PLAN_TARGET_SET_PART').show();
				$('#ACTION_PART').show();
			} else {
				$('#PLAN_TARGET_SET_PART').hide();
                $('#ACTION_PART').hide();
			}
		});
    },
	afterSelectedCust : function(obj) {
		// if(!planEntry.checkSelectedCust()) {
		// 	return;
		// }
		var newCustNum = ($("#newCustNum").val());
				
		var custName = '';
        var actionCode = actionList[planEntry.currentActionIndex].ACTION_CODE;
		if(newCustNum > 0) {
			var param = "NEW_CUSTNUM=" + newCustNum;
            $.ajaxPost('cust/addCustByNum',param,function(data){
                var result = new Wade.DataMap(data);
                var resultCode = result.get("HEAD").get("RESULT_CODE");

                if(resultCode == "0"){
                    var body = result.get('BODY');
                    var custList= body.get('custList');
                    $.each(custList, function(idx, cust){
                        custName += cust.get('CUST_NAME') + "；";
                        planEntry.planActionMap.put(actionCode, custList);
					});
                    backPopup(obj);


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
                }
            },function(){
                alert('error');
            });
		} else {
            var custIdList = getCheckedValues('selectCustBox').split(',');
            var custNum = getCheckedBoxNum('selectCustBox');
            var custList = $.DatasetList();
            if(custNum > 0) {
                for(var i = 0; i < custIdList.length; i++) {
                    var cust = selectCust.currentCustMap.get(custIdList[i]);
                    custList.add(cust);

                    custName += cust.get('CUST_NAME') + ",";
                }
            }

            planEntry.planActionMap.put(actionCode, custList);

            backPopup(obj);

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
		}
	},
	afterPlanTargetSet : function(obj) {
	    if(!planEntry.checkPlanTarget()) {
	        return;
        }

		backPopup(obj);
		
		var ds=$.DataMap(
			{
				"scanHouseCounselorNum" : $('#scanHouseCounselorNum').val(),
				"adviceNum" : $('#adviceNum').val()
			}
		);
		ds.bind('planTarget','block');
		
		$('#ACTION_PART').show();
		
		var actionDatasetList = $.DatasetList(actionList);
		actionDatasetList.bind('ACTION_LIST', 'flex');
		
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
			$('#' +actionCode).bind('tap', selectCust.showSelectCust);
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
	setPlanTarget : function() {
		showPopup('myPopup','planTargetSetPopup');
	},
    submitPlan : function() {
	    var paramData = $.DataMap();
	    var planList = $.DatasetList();
        paramData.put("PLANLIST", planList);
        planEntry.planActionMap.eachKey(function(key, item){
            var actionPlan = $.DataMap();
            actionPlan.put("ACTION_CODE", key);
            actionPlan.put("CUSTLIST", item);
            planList.add(actionPlan);
        });

        var param = '&planInfoString=' + paramData.toString();
        $.ajaxPost("plan/addPlan.json", param, function(data){
            var result = new Wade.DataMap(data);
            var resultCode = result.get("HEAD").get("RESULT_CODE");

            if(resultCode == "0"){
                alert('计划提交成功');
            }
        }, function() {
            alert('error');
        })
    },
    checkPlanTarget : function() {
        var scanHouseCounselorNum =  $('#scanHouseCounselorNum').val();
        var adviceNum = $('#adviceNum').val();

        var checkFlag = false;

        $.ajaxRequest({
                url:'plan/checkPlanTarget',
                data: {
                    scanHouseCounselorNum : scanHouseCounselorNum,
                    adviceNum : adviceNum,
                },
                type:'POST',
                dataType:'json',
                async:false,
                success:function(data) {
                    var result = $.DataMap(data);
                    var resultCode = result.get('HEAD').get('RESULT_CODE');
                    if(resultCode == 0) {
                        alert('校验成功');
                        checkFlag = true;
                    } else {
                        alert('校验失败');
                        checkFlag = false;
                    }
                },
                error:function(status, errorMessage) {

                },
            }
        );

        alert(checkFlag);
        return checkFlag;
    }
};

var selectCust = {
    currentCustMap: $.DataMap(),
    showCustEdit : function(obj) {
        var $obj = $(obj);

        resetArea("custForm", true);

        window["SEX"] = new Wade.Switch("SEX",{
            switchOn:true,
            onValue:"1",
            offValue:"2",
            onColor:"blue",
            offColor:"red"
        });

        window["HOUSE_ID"] = new Wade.Select(
            "HOUSE_ID",
            {
                value:"",
                inputable:false,
                disabled:false,
                addDefault:true,
                selectedIndex:-1,
                optionAlign:"left"
            }
        );

        $("#SEX").val("1");

        HOUSE_ID.append("湘江世纪城一期","1");
        HOUSE_ID.append("保利西海岸一期","2");
        HOUSE_ID.append("四方坪一期","3");
        HOUSE_ID.append("四方坪二期","4");

        // var custId = $(obj).attr('custId');
        // if(custId) {
            //修改已有客户
            // var cust = selectCust.currentCustMap.get(custId);
            // $('#custForm #CUST_NAME').val(cust.get('CUST_NAME'));
            // $('#custForm #CUST_ID').val(custId);
            // $('#custForm #WX_NICK').val(cust.get('WX_NICK'));
            // $('#custForm #SEX').val(cust.get('SEX'));
            // $('#custForm #MOBILE_NO').val(cust.get('MOBILE_NO'));
            // $('#custForm #HOUSE_ID').val(cust.get('HOUSE_ID'));
            // $('#custForm #HOUSE_DETAIL').val(cust.get('HOUSE_DETAIL'));
            // $('#custForm #HOUSE_MODE').val(cust.get('HOUSE_MODE'));
            // $('#custForm #HOUSE_AREA').val(cust.get('HOUSE_AREA'));
        // }

        forwardPopup(obj,'custInfoEditPopup');
    },
    submitCustInfo : function (obj) {
        if($.validate.verifyAll("custForm")){
            var param = $.buildJsonData("custForm");
            var url = '';
            if(param.CUST_ID) {
                url = 'cust/editCust';
            } else {
                url = 'cust/addCust';
            }
            $.ajaxPost(url,param,function(data){
                var result = new Wade.DataMap(data);
                var resultCode = result.get("HEAD").get("RESULT_CODE");

                if(resultCode == "0"){
                    if(param.CUST_ID) {
                        $('#'+param.CUST_ID + ' div[tag=CUST_NAME]').html(param.CUST_NAME);
                        $('#'+param.CUST_ID + ' li[tag=MOBILE_NO]').html(param.MOBILE_NO);
                        $('#'+param.CUST_ID + ' li[tag=HOUSE_DETAIL]').html(param.HOUSE_DETAIL);
                    } else {
                        param.CUST_ID = '666';
                        var template = $('#CUST_TEMPLATE').html();
                        var tpl=$.Template(template);
                        param.CHECKED = 'checked';
                        tpl.insertFirst('#CUST_LIST',param,true);
                    }


                    selectCust.currentCustMap.put(param.CUST_ID, $.DataMap(param));

                    backPopup(obj);
                }
            },function(){
                alert('error');
            });
        }
        else{

        }
    },
    _queryCust : function(param, callback) {
        $.ajaxGet('cust/queryCustList',param,function(data){
            var result = new Wade.DataMap(data);
            var resultCode = result.get("HEAD").get("RESULT_CODE");

            if(resultCode == "0"){
                //清空表格
                $('#CUST_LIST').empty();

                var body = result.get('BODY');
                var ds= body.get('CUSTOMERLIST')
                if(ds) {
                    $.each(ds, function(idx, item) {
                        var custId = item.get('CUST_ID');
                        selectCust.currentCustMap.put(custId, item);

                        var template = $('#CUST_TEMPLATE').html();
                        var tpl=$.Template(template);
                        tpl.append('#CUST_LIST',item,true);
                    });
                }

                if(callback) {
                    callback();
                }
                // showPopup('selectCustPopup','customerSelectPopup');
            }
        },function(){
            alert('error');
        });
    },
    queryCust : function(obj) {
        var param = '';
        selectCust._queryCust(param, function() {
            backPopup(obj);
        });
        // var ds=$.DatasetList([
        // 	{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 	{"CUST_NAME":"李四","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 	{"CUST_NAME":"王五","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 	{"CUST_NAME":"赵六","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 	{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 	{"CUST_NAME":"张三2","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // ]);
        // $.each(ds, function(idx, item) {
        //     var template = $('#CUST_TEMPLATE').html();
        //     var tpl=$.Template(template);
        //     tpl.append('#CUST_LIST',item,true);
        // });
    },
    showSelectCust : function () {
        if(planEntry.currentActionIndex == actionList.length) {
            alert('已到最后一个动作');
            return;
        }

        var currentActionCode = actionList[planEntry.currentActionIndex].ACTION_CODE;
        if(currentActionCode == 'JW') {
            $('#QUERY_CUST_PART').hide();
            $('#NEW_CUST_PART').show();
        } else {
            $('#QUERY_CUST_PART').show();
            $('#NEW_CUST_PART').hide();

            if(currentActionCode == 'ZX') {
                $('#ADD_CUST_BUTTON').show();
            } else {
                $('#ADD_CUST_BUTTON').hide();
            }
        }

        $("#newCustNum").val("0")

        // var ds=$.DatasetList([
        // 		{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 		{"CUST_NAME":"李四","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 		{"CUST_NAME":"王五","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 		{"CUST_NAME":"赵六","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // 		{"CUST_NAME":"张三","SERIAL_NUMBER":"13467517522","HOUSE_DETAIL":"桔园小区1栋1001"},
        // ]);
        // ds.bind('CUST_LIST','flex');
        // $.each(ds, function(idx, item) {
        //    var template = $('#CUST_TEMPLATE').html();
        //    var tpl=$.Template(template);
        //    tpl.append('#CUST_LIST',item,true);
        // });

        var param = '';
        selectCust._queryCust(param, function() {
            showPopup('myPopup','customerSelectPopup');
        });
    },
};