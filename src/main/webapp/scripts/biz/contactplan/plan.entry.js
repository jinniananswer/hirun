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
	planActionMap : $.DataMap(),//{actionCode:custList}
    planTargetList : $.DatasetList(),
    currentAction : '',
    currentActionIndex : '',
    planDate : '',
    init : function() {
		window["myPopup"] = new Wade.Popup("myPopup",{
			visible:false,
			mask:true
		});
		
		window["newCustNum"] = new Wade.IncreaseReduce("newCustNum", {
			disabled:false,
			step:1
		});
		
		window["scanHouseCounselorNum"] = new Wade.IncreaseReduce("scanHouseCounselorNum", {
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
        $('#workMode').val(1);

		var actionDatasetList = $.DatasetList(actionList);
		actionDatasetList.bind('ACTION_LIST', 'flex');
		
		planEntry.setCurrentActionOn();
		
		$("#workMode").change(function(){
			var modeVal = this.value; // this.value 获取分段器组件当前值
			planActionList = [];//清空已选动作
            $('#ACTION_PART').hide();
			if(modeVal == '1') {
				$('#PLAN_TARGET_SET_PART').show();
			} else {
				$('#PLAN_TARGET_SET_PART').hide();
			}
		});

		//从后台获取初始化数据，同步方式
        $.ajaxReq({
            url:'plan/getPlanInitData',
            data: {},
            type:'GET',
            dataType:'json',
            async:false,
            successFunc:function(data) {
                planEntry.planDate = data.PLAN_DATE;
            },
            errorFunc:function(resultCode, resultInfo) {
                alert(resultInfo);
                top.$.index.closePage("今日计划录入");
            },
        });

        $('#planName').html(planEntry.planDate + '计划');

        //客户查询条件初始化 开始
        $.Select.append(
            // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
            "queryCustParamForm_house_container",
            // 参数设置
            {
                id:"queryCustParamForm_house",
                name:"HOUSE_ID",
            },
            // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
            [
                {TEXT:"Tony Stark", VALUE:"0"},
                {TEXT:"Steve Rogers", VALUE:"1"},
                {TEXT:"Thor", VALUE:"2"}
            ]
        );
        ////客户查询条件初始化 结束

        //客户资料编辑初始化 开始
        window["SEX"] = new Wade.Switch("SEX",{
            switchOn:true,
            onValue:"1",
            offValue:"2",
            onColor:"blue",
            offColor:"red"
        });

        $.Select.append(
            // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
            "custEditForm_house_container",
            // 参数设置
            {
                id:"custEditForm_house",
                name:"HOUSE_ID",
                nullable : "no",
                desc : "楼盘",
            },
            // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
            [
                {TEXT:"Tony Stark", VALUE:"0"},
                {TEXT:"Steve Rogers", VALUE:"1"},
                {TEXT:"Thor", VALUE:"2"}
            ]
        );
        //客户资料编辑初始化 结束
    },
	afterSelectedCust : function(obj) {
		if(!planEntry.checkSelectedCust()) {
			return;
		}
		var newCustNum = ($("#newCustNum").val());
				
		var custName = '';
		if(newCustNum > 0) {
			var param = {
                NEW_CUSTNUM : newCustNum,
                CUST_NAME_PREFIX : planEntry.planDate,
            }
            $.ajaxPost('cust/addCustByNum',param,function(data){
                var result = new Wade.DataMap(data);
                var resultCode = result.get("HEAD").get("RESULT_CODE");

                if(resultCode == "0"){
                    var body = result.get('BODY');
                    var custList= body.get('custList');
                    $.each(custList, function(idx, cust){
                        custName += cust.get('CUST_NAME') + "；";
                        planEntry.planActionMap.put(planEntry.currentAction, custList);
					});
                    backPopup(obj);


                    $('#' + planEntry.currentAction + ' div[tag=ACTION_NAME_CONTENT]').html(custName);

                    //设置下一动作

                    if(planEntry.currentActionIndex < actionList.length - 1) {
                        planEntry.currentActionIndex = planEntry.currentActionIndex + 1;
                        planEntry.setCurrentActionOn();
                    } else {
                        $('#' +planEntry.currentAction).unbind('tap');
                        $('#' + planEntry.currentAction + ' span[tag=ACTION_NAME_TEXT]').removeClass("e_red").addClass('e_green');
                        $('#' + planEntry.currentAction + ' span[tag=ACTION_ICO_OK]').show();
                        $('#' + planEntry.currentAction + ' div[tag=ACTION_SIDE]').hide();
                        $('#' + planEntry.currentAction + ' div[tag=ACTION_MORE]').hide();
                    }
                }
            },function(){
                alert('error');
            });
		} else {
            // var custIdList = getCheckedValues('selectCustBox').split(',');
            // var custNum = getCheckedBoxNum('selectCustBox');
            var custList = $.DatasetList();

            //通过搜索选择的客户
            $.each(selectCust.selectedCustMap, function(key, cust) {
                custList.push($.DataMap(cust));
                custName += cust.CUST_NAME + "；";
            })
            // if(custNum > 0) {
            //     for(var i = 0; i < custIdList.length; i++) {
            //         var cust = selectCust.currentCustMap.get(custIdList[i]);
            //         custList.add(cust);
            //
            //         custName += cust.get('CUST_NAME') + "；";
            //     }
            // }
            //通过上一动作带过来，又保留的客户
            var unDeletedBeforeActionCustList = selectCust.getUndeletedBeforeActionCust();
            $.each(unDeletedBeforeActionCustList, function (idx, cust) {
                custList.add(cust);
                custName += cust.get('CUST_NAME') + "；";
            })

            planEntry.planActionMap.put(planEntry.currentAction, custList);

            backPopup(obj);

            $('#' + planEntry.currentAction + ' div[tag=ACTION_NAME_CONTENT]').html(custName);

            //设置下一动作

            if(planEntry.currentActionIndex < actionList.length - 1) {
                planEntry.currentActionIndex = planEntry.currentActionIndex + 1;
                planEntry.setCurrentActionOn();
            } else {
                $('#' +planEntry.currentAction).unbind('tap');
                $('#' + planEntry.currentAction + ' span[tag=ACTION_NAME_TEXT]').removeClass("e_red").addClass('e_green');
                $('#' + planEntry.currentAction + ' span[tag=ACTION_ICO_OK]').show();
                $('#' + planEntry.currentAction + ' div[tag=ACTION_SIDE]').hide();
                $('#' + planEntry.currentAction + ' div[tag=ACTION_MORE]').hide();
            }
		}
	},
	afterPlanTargetSet : function(obj) {
	    if(!planEntry.checkPlanTarget()) {
	        return;
        }

        planEntry.planTargetList.add($.DataMap({"ACTION_CODE":"ZX", "NUM" : $('#adviceNum').val()}));
        planEntry.planTargetList.add($.DataMap({"ACTION_CODE":"SMJRQLC", "NUM" : $('#scanHouseCounselorNum').val()}));

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
		var checkFlag = true;
        var newCustNum = parseInt($("#newCustNum").val());
        var checkBoxCustNum = parseInt(getCheckedBoxNum('selectCustBox'));
        var undeletedBeforeActionCustNum = parseInt(selectCust.getUndeletedBeforeActionCustNum());
        var custNum = newCustNum + checkBoxCustNum + undeletedBeforeActionCustNum;

        $.ajaxRequest({
                url:'plan/checkPlanAction',
                data: {
                    PLAN_DATE : planEntry.planDate,
                    ACTION_CODE : planEntry.currentAction,
                    CUSTNUM : custNum,
                    PLAN_TARGET_LIST : planEntry.planTargetList.toString(),
                },
                type:'POST',
                dataType:'json',
                async:false,
                success:function(data) {
                    var result = $.DataMap(data);
                    var resultCode = result.get('HEAD').get('RESULT_CODE');
                    if(resultCode == 0) {
                        // alert('校验成功');
                        checkFlag = true;
                    } else {
                        var resultInfo = result.get('HEAD').get('RESULT_INFO');
                        alert(resultInfo);
                        checkFlag = false;
                    }
                },
                error:function(status, errorMessage) {

                },
            }
        );
		
		return checkFlag;
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
			planEntry.currentAction = actionCode;
			$('#' +actionCode).bind('tap', selectCust.showSelectCust);
			$('#' + actionCode + ' span[tag=ACTION_NAME_TEXT]').addClass("e_red");
			$('#' + actionCode + ' div[tag=ACTION_SIDE]').show();
			$('#' + actionCode + ' div[tag=ACTION_MORE]').show();
		}
		
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

        var param = {
            PLANLIST : planList.toString(),
            PLAN_DATE : planEntry.planDate,
            PLAN_TYPE : $("#workMode").val(),
        };
        $.ajaxPost("plan/addPlan.json", param, function(data){
            var result = new Wade.DataMap(data);
            var resultCode = result.get("HEAD").get("RESULT_CODE");

            if(resultCode == "0"){
                alert('计划提交成功');
                // MessageBox.success("成功信息", "成功信息");
                top.$.index.closePage('今日计划录入');
            }
        }, function() {
            alert('error');
        })
    },
    checkPlanTarget : function() {
        var scanHouseCounselorNum =  $('#scanHouseCounselorNum').val();
        var adviceNum = $('#adviceNum').val();

        var checkFlag = false;

        var planTargetList = $.DatasetList([
            {"ACTION_CODE":"ZX","NUM":adviceNum},
            {"ACTION_CODE":"SMJRQLC","NUM":scanHouseCounselorNum},
        ]);
        $.ajaxRequest({
                url:'plan/checkPlanTarget',
                data: {
                    PLAN_DATE : planEntry.planDate,
                    PLAN_TARGET_LIST : planTargetList.toString(),
                },
                type:'POST',
                dataType:'json',
                async:false,
                success:function(data) {
                    var result = $.DataMap(data);
                    var resultCode = result.get('HEAD').get('RESULT_CODE');
                    if(resultCode == 0) {
                        // alert('校验成功');
                        checkFlag = true;
                    } else {
                        var resultInfo = result.get('HEAD').get('RESULT_INFO');
                        alert(resultInfo);
                        checkFlag = false;
                    }
                },
                error:function(status, errorMessage) {

                },
            }
        );

        return checkFlag;
    },
    getActionNameByCode : function (actionCode) {
	    var actionName = '';
        $.each(actionList, function(idx, action){
            if(actionCode == action.ACTION_CODE) {
                actionName = action.ACTION_NAME;
                return;
            }
        });

        return actionName;
    },
    getBeforeAction : function () {
	    if(planEntry.currentActionIndex == 0) {
	        return '';
        } else {
            return actionList[planEntry.currentActionIndex-1].ACTION_CODE;
        }
    }
};

var selectCust = {
    currentCustMap : {},
    selectedCustMap : {},
    showCustEdit : function(obj) {
        var $obj = $(obj);

        resetArea("custForm", true);

        $("#SEX").val("1");

        forwardPopup(obj,'custInfoEditPopup');
    },
    submitCustInfo : function (obj) {
        if($.validate.verifyAll("custForm")){
            var param = $.buildJsonData("custForm");
            var url = '';
            param.CUST_STATUS = '1';
            if(param.CUST_ID) {
                url = 'cust/editCust';
            } else {
                url = 'cust/addCust';
            }
            $.ajaxPost(url,param,function(data){
                var result = new Wade.DataMap(data);
                var resultCode = result.get("HEAD").get("RESULT_CODE");

                if(resultCode == "0"){
                    // if(param.CUST_ID) {
                    //     $('#'+param.CUST_ID + ' div[tag=CUST_NAME]').html(param.CUST_NAME);
                    //     $('#'+param.CUST_ID + ' li[tag=MOBILE_NO]').html(param.MOBILE_NO);
                    //     $('#'+param.CUST_ID + ' li[tag=HOUSE_DETAIL]').html(param.HOUSE_DETAIL);
                    // } else {
                    param.CUST_ID = result.get('BODY').get('CUST_ID');
                    var template = $('#CUST_TEMPLATE').html();
                    var tpl=$.Template(template);
                    param.CHECKED = 'checked';
                    tpl.insertFirst('#CUST_LIST',param,true);

                    selectCust.currentCustMap[param.CUST_ID] = param;
                    selectCust.selectedCustMap[param.CUST_ID] = param;
                    // }


                    // selectCust.currentCustMap.put(param.CUST_ID, $.DataMap(param));

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
        //清空查询结果
        $('#CUST_LIST').empty();
        selectCust.currentCustMap = {};
        selectCust.setCovertGenderParam(param);
        $.ajaxReq({
            url : 'cust/queryCustList',
            data : param,
            type : 'GET',
            dataType : 'json',
            successFunc : function(data){
                var ds= data.CUSTOMERLIST;
                if(ds) {
                    $.each(ds, function(idx, item) {
                        var custId = item.CUST_ID;
                        selectCust.currentCustMap[custId] = item;

                        var template = $('#CUST_TEMPLATE').html();
                        var tpl=$.Template(template);
                        tpl.append('#CUST_LIST',item,true);
                    });
                }

                if(callback) {
                    callback();
                }
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        });
    },
    selectCustBoxClick : function(obj) {
        $checkBox = $(obj);
        var custId = $checkBox.val();
        if($checkBox.attr('checked')) {
            selectCust.selectedCustMap[custId] = selectCust.currentCustMap[custId];
        } else {
            delete selectCust.selectedCustMap[custId]
        }
    },
    queryCust : function(obj) {
        var param = $.buildJsonData("queryCustParamForm");
        selectCust._queryCust(param, function() {
            backPopup(obj);
        });
    },
    showSelectCust : function () {
        if(planEntry.currentActionIndex == actionList.length) {
            alert('已到最后一个动作');
            return;
        }

        $('#before_action_cust_list_part').empty();
        var currentActionCode = actionList[planEntry.currentActionIndex].ACTION_CODE;
        if(currentActionCode == 'JW') {
            $('#QUERY_CUST_PART').hide();
            $('#NEW_CUST_PART').show();

            showPopup('myPopup','customerSelectPopup');
        } else {
            $('#QUERY_CUST_PART').show();
            $('#NEW_CUST_PART').hide();

            var beforeActionCode = actionList[planEntry.currentActionIndex-1].ACTION_CODE;

            //渲染上一动作选择客户区域
            if(currentActionCode != 'DKCSMU' && currentActionCode != 'YJALTS') {
                var templateData = {};
                var beforeCustList = planEntry.planActionMap.get(beforeActionCode);
                templateData.BEFORE_ACTION_NAME = planEntry.getActionNameByCode(beforeActionCode);
                templateData.BEFORE_ACTION_CUSTNUM = beforeCustList.length;
                templateData.CUST_LIST = JSON.parse(beforeCustList.toString());
                $('#before_action_cust_list_part').html(template('before_action_cust_list_template',templateData));
            }

            var param = {};
            selectCust._queryCust(param, function() {
                showPopup('myPopup','customerSelectPopup');
            });

            if(currentActionCode == 'ZX' || currentActionCode == 'LTZDSTS') {
                $('#ADD_CUST_BUTTON').show();
            } else {
                $('#ADD_CUST_BUTTON').hide();
            }
        }

        $("#newCustNum").val("0")
    },
    getUndeletedBeforeActionCustNum : function() {
        var num = 0;
        $.each($('#before_action_cust_list_part').find('div[tag=cust_title]'), function(idx, item) {
            if(!$(item).hasClass('e_delete')) {
                num++;
            }
        });

        return num;
    },
    getUndeletedBeforeActionCust : function() {
        var custList = $.DatasetList();
        if(planEntry.currentActionIndex == 0) {
            return custList;
        }
        var beforeActionCode = actionList[planEntry.currentActionIndex-1].ACTION_CODE;
        var beforeActionCustList = planEntry.planActionMap.get(beforeActionCode);
        $.each($('#before_action_cust_list_part').find('div[tag=cust_title]'), function(idx, item) {
            item = $(item)
            if(!item.hasClass('e_delete')) {
                var custId = item.attr('cust_id');
                $.each(beforeActionCustList, function(idx2, beforeActionCust) {
                    if(custId == beforeActionCust.get('CUST_ID')) {
                        custList.add(beforeActionCust);
                        return false;
                    }
                })
            }
        });

        return custList;
    },
    setCovertGenderParam : function(param) {
        var actionCode = planEntry.currentAction;
        if(actionCode == 'DKCSMU' || actionCode == 'YJALTS') {
            param['UNEXECUTED_ACTION'] = actionCode;
        } else if(actionCode == 'JW') {

        } else {
            param['LAST_ACTION'] = planEntry.getBeforeAction();
        }
    }
};