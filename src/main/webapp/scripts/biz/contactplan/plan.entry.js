var actionList = [
    {"ACTION_CODE":"JW","ACTION_NAME":"加微"},
    {"ACTION_CODE":"LTZDSTS","ACTION_NAME":"客户产品需求库使用指导书推送"},
    {"ACTION_CODE":"GZHGZ","ACTION_NAME":"公众号关注"},
    {"ACTION_CODE":"HXJC","ACTION_NAME":"核心接触"},
    {"ACTION_CODE":"SMJRQLC","ACTION_NAME":"扫码进入全流程"},
    {"ACTION_CODE":"XQLTYTS","ACTION_NAME":"需求蓝图一推送"},
	{"ACTION_CODE":"ZX","ACTION_NAME":"咨询"},
	{"ACTION_CODE":"DKCSMU","ACTION_NAME":"带看城市木屋"},
	{"ACTION_CODE":"YJALTS","ACTION_NAME":"一键案例推送"},
];
var planEntry = {
	planActionMap : {},//{actionCode:custList}
    planTargetList : [],
    newCustList : [],
    currentAction : '',
    currentActionIndex : '',
    planDate : '',
    executorId : '',
    isAdditionalRecord : '',
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

        window["addWxNum"] = new Wade.IncreaseReduce("addWxNum", {
            disabled:false,
            step:1
        });
		
		window["adviceNum"] = new Wade.IncreaseReduce("adviceNum", {
			disabled:false,
			step:1
		});
		
		// window["holidaySwitch"] = new Wade.Switch("holidaySwitch",{
		// 	switchOn:false,
		// 	onValue:"1",
		// 	offValue:"0",
		// });
		
		window["planType"] = new Wade.Segment("planType",{
			disabled:false
		});

        window["workMode"] = new Wade.Segment("workMode",{
            disabled:false
        });

		//默认值 开始
        planEntry.currentActionIndex = 0;
        $('#planType').val(1);
        $('#workMode').val(1);
		
		planEntry.setCurrentActionOn();
		
		$("#planType").change(function(){
			var modeVal = this.value; // this.value 获取分段器组件当前值
			planActionList = [];//清空已选动作
            $('#ACTION_PART').hide();
            $('#ACTION_LIST').empty();//清空动作计划
			if(modeVal == '1') {
				// $('#PLAN_TARGET_SET_PART').show();
			} else {
			    var message = '今日您';
			    if(modeVal == '2') {
                    message += '<span class="e_red e_strong">参加活动</span>';
                } else {
                    message += '<span class="e_red e_strong">休假</span>';
                }
                message += '，可以不设定具体计划。点击【确定】不设定具体计划并提交，点击【继续】继续设定计划<br/>';
                message += '<span class="e_red">温馨提示：如您选择继续设定计划，则必须填写总结！</span>';
                MessageBox.success("提示信息",message, function(btn){
                    if("ok" == btn) {
                        // document.location.reload();
                        planEntry.submitPlan();
                    }
                    else {

                    }
                },{"cancel":"继续"})
			}
		});

        var isAdditionalRecord = $.params.get('IS_ADDITIONAL_RECORD');
        if(isAdditionalRecord) {
            planEntry.isAdditionalRecord = isAdditionalRecord;
        } else {
            planEntry.isAdditionalRecord = "0";
        }

		//从后台获取初始化数据，同步方式
        var planDate = $.params.get('PLAN_DATE');
        var executorId = $.params.get('EXECUTOR_ID');
        var param = {};
        if(planDate && executorId) {
            param.PLAN_DATE = planDate;
            param.EXECUTOR_ID = executorId;
        }
        $.ajaxReq({
            url:'plan/getPlanInitData',
            data: param,
            type:'GET',
            dataType:'json',
            async:false,
            successFunc:function(data) {
                planEntry.planDate = data.PLAN_DATE;
                planEntry.executorId = data.EXECUTOR_ID;
            },
            errorFunc:function(resultCode, resultInfo) {
                alert(resultInfo);
                $.redirect.closeCurrentPage();
            },
        });

        // $('#planName').html(planEntry.planDate + '计划');
        Header.setHeaderName(planEntry.planDate + '计划');

        //客户查询条件初始化 开始
        // $.ajaxReq({
        //     url : 'queryHousesByEmployeeId',
        //     data : {
        //
        //     },
        //     successFunc : function(data) {
        //         var options = [];
        //         $.each(data.HOUSES_LIST, function(idx, house) {
        //             options.push({TEXT : house.NAME, VALUE : house.HOUSES_ID})
        //         })
        //         $.Select.append(
        //             "queryCustParamForm_house_container",
        //             {
        //                 id:"queryCustParamForm_house",
        //                 name:"HOUSE_ID",
        //             },
        //             options
        //         );
        //     },
        //     errorFunc : function(resultCode, resultInfo) {
        //
        //     }
        // })
        ////客户查询条件初始化 结束

        //客户资料编辑初始化 开始
        window["SEX"] = new Wade.Switch("SEX",{
            switchOn:true,
            onValue:"1",
            offValue:"2",
            onColor:"blue",
            offColor:"red"
        });

        // $.ajaxReq({
        //     url : 'queryHousesByEmployeeId',
        //     data : {
        //
        //     },
        //     successFunc : function(data) {
        //         var options = [];
        //         $.each(data.HOUSES_LIST, function(idx, house) {
        //             options.push({TEXT : house.NAME, VALUE : house.HOUSES_ID})
        //         })
        //         $.Select.append(
        //             "custEditForm_house_container",
        //             参数设置
                    // {
                    //     id:"custEditForm_house",
                    //     name:"HOUSE_ID",
                    //     nullable : "no",
                    //     desc : "楼盘",
                    // },
                    // options
                // );
            // },
            // errorFunc : function(resultCode, resultInfo) {
            //
            // }
        // })
        //客户资料编辑初始化 结束

        $('#PLAN_TARGET_SET_PART').unbind('tap').bind('tap', function() {
            planEntry.setPlanTarget();
        });
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
                FIRST_PLAN_DATE : planEntry.planDate,
                HOUSE_COUNSELOR_ID : planEntry.executorId,
            }
            $.ajaxReq({
                url : 'cust/addCustByNum',
                data : param,
                type : 'POST',
                dataType : 'json',
                successFunc : function(data) {
                    var custList= data['custList'];
                    $.each(custList, function(idx, cust){
                        custName += cust.CUST_NAME + "；";
                        planEntry.planActionMap[planEntry.currentAction] = custList;
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
                },
                errorFunc : function(resultCode, resultInfo) {

                }
            });
		} else {
            var custList = [];

            //通过搜索选择的客户
            $.each(selectCust.selectedCustMap, function(key, cust) {
                custList.push(cust);
                custName += cust.CUST_NAME + "；";
            })
            //通过上一动作带过来，又保留的客户
            var unDeletedBeforeActionCustList = selectCust.getUndeletedBeforeActionCust();
            $.each(unDeletedBeforeActionCustList, function (idx, cust) {
                if(!planEntry.isInCustList(custList, cust)) {
                    custList.push(cust);
                    custName += cust.CUST_NAME + "；";
                }
            })

            planEntry.planActionMap[planEntry.currentAction] = custList;

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
	isInCustList : function(custList, cust) {
	    var found = false;
        $.each(custList, function(idx, item) {
            var custId = item.CUST_ID;
            if(custId == cust.CUST_ID) {
                found = true;
                return false;
            }
        })

        return found;
    },
	afterPlanTargetSet : function(obj) {
	    if(!planEntry.checkPlanTarget()) {
	        return;
        }

        //按JW数量新增客户 start
        planEntry.addCustByNum();
	    //按JW数量新增客户 end

        planEntry.planTargetList.push({"ACTION_CODE":"ZX", "NUM" : $('#adviceNum').val()});
        planEntry.planTargetList.push({"ACTION_CODE":"HXJC", "NUM" : $('#scanHouseCounselorNum').val()});
        planEntry.planTargetList.push({"ACTION_CODE":"JW", "NUM" : $('#addWxNUm').val()});

        $('#planTarget span[tag=adviceNum]').html($('#adviceNum').val());
        $('#planTarget span[tag=scanHouseCounselorNum]').html($('#scanHouseCounselorNum').val());
        $('#planTarget span[tag=addWxNum]').html($('#addWxNum').val());

        planEntry.showPlanActionAndCustList(obj);


        // $('#PLAN_TARGET_SET_PART').unbind('tap');
        //
        // planEntry.setCurrentActionOn();
	},
	addCustByNum : function() {
        var param = {
            NEW_CUSTNUM : $('#addWxNum').val(),
            // CUST_NAME_PREFIX : planEntry.planDate,
            FIRST_PLAN_DATE : planEntry.planDate,
            HOUSE_COUNSELOR_ID : planEntry.executorId,
        }
        $.ajaxReq({
            url : 'cust/addCustByNum',
            data : param,
            type : 'POST',
            async : false,
            dataType : 'json',
            successFunc : function(data) {
                var custList= data['custList'];
                planEntry.newCustList = custList;
                // $.each(custList, function(idx, cust){
                //     custName += cust.CUST_NAME + "；";
                //     planEntry.planActionMap[planEntry.currentAction] = custList;
                // });
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        });
    },
    showPlanActionAndCustList : function(obj) {
	    var planType = $('#planType').val();
        $.ajaxReq({
            url : 'plan/getPlanActionAndCustList',
            data : {
                PLAN_DATE : planEntry.planDate,
                PLAN_EXECUTOR_ID : planEntry.executorId
            },
            type : 'POST',
            async : false,
            dataType : 'json',
            successFunc : function(data) {
                var actionList = data.ACTION_LIST;
                $('#ACTION_PART').show();

                $.each(actionList, function(idx, action) {
                    action.NEW_CUST_LIST = planEntry.newCustList;
                    if("1" != planType) {
                        action.CAN_DELETE = true;
                        action.CAN_DELETE_NEWCUST = true;
                    } else {
                        if("XQLTYTS" == action.ACTION_CODE || "ZX" == action.ACTION_CODE) {
                            action.CAN_DELETE_NEWCUST = true;
                        }
                    }

                });

                $('#ACTION_LIST').html(template('action_list_template', {ACTION_LIST : actionList}));

                backPopup(obj);
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        });
    },
	checkSelectedCust : function() {
		var checkFlag = true;
        var newCustNum = parseInt($("#newCustNum").val());
        var checkBoxCustNum = 0;
        $.each(selectCust.selectedCustMap, function(idx, cust) {
            checkBoxCustNum++;
        })
        var undeletedBeforeActionCustNum = parseInt(selectCust.getUndeletedBeforeActionCustNum());
        var custNum = newCustNum + parseInt(checkBoxCustNum) + undeletedBeforeActionCustNum;

        $.ajaxReq({
                url:'plan/checkPlanAction',
                data: {
                    EXECUTOR_ID : planEntry.executorId,
                    PLAN_DATE : planEntry.planDate,
                    ACTION_CODE : planEntry.currentAction,
                    CUSTNUM : custNum,
                    PLAN_TARGET_LIST : JSON.stringify(planEntry.planTargetList),
                },
                type:'GET',
                dataType:'json',
                async:false,
                successFunc:function(data) {
                    checkFlag = true;
                },
                errorFunc:function(resultCode, resultInfo) {
                    alert(resultInfo);
                    checkFlag = false;
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
	    var planType = $('#planType').val();
	    if(planType == '1') {
            var targetList = [
                {"ACTION_CODE" : 'ZX'},
                {"ACTION_CODE" : 'HXJC'},
            ]
            $.ajaxReq({
                url : 'plan/getTargetLowerLimit',
                data : {
                    TARGET_LIST : JSON.stringify(targetList),
                    PLAN_EXECUTOR_ID : planEntry.executorId,
                },
                successFunc : function(data) {
                    $.each(data.TARGET_LIST, function(idx, target) {
                        var actionCode = target.ACTION_CODE;
                        if(actionCode == 'ZX') {
                            $('#adviceNum').val(target.LOWER_LIMIT);
                        } else if(actionCode == 'HXJC') {
                            $('#scanHouseCounselorNum').val(target.LOWER_LIMIT);
                        }
                    })
                    showPopup('myPopup','planTargetSetPopup');
                } ,
                errorFunc : function(errorCode, errorInfo) {

                }
            })
        } else {
            showPopup('myPopup','planTargetSetPopup');
        }
	},
    submitPlan : function() {
        var message = '确定提交计划吗';
        MessageBox.success("提示信息",message, function(btn){
            if("ok" == btn) {
                var planType = $("#planType").val();

                var planList = [];
                $('#ACTION_LIST div[tag=PLAN_ACTION]').each(function(idx, item) {
                    var $item = $(item);
                    var actionPlan = {};
                    actionPlan.ACTION_CODE = $item.attr('action_code');
                    var custList = [];
                    $item.find('li[li_type=cust]').each(function(idx2, cust) {
                        var operCode = $(cust).attr('oper_code');
                        if(operCode == '1') {
                            return true;
                        }
                        custList.push({CUST_ID : $(cust).attr('cust_id')});
                    });
                    actionPlan.CUSTLIST = custList;

                    planList.push(actionPlan);
                })
                // $.each(planEntry.planActionMap, function(key, item){
                //     var actionPlan = {};
                //     actionPlan.ACTION_CODE = key;
                //     actionPlan.CUSTLIST = item;
                //     planList.push(actionPlan);
                // });

                var param = {
                    PLANLIST : JSON.stringify(planList),
                    PLAN_DATE : planEntry.planDate,
                    PLAN_EXECUTOR_ID : planEntry.executorId,
                    PLAN_TYPE : $("#planType").val(),
                    WORK_MODE : $('#workMode').val(),
                    IS_ADDITIONAL_RECORD : planEntry.isAdditionalRecord,
                };
                $.beginPageLoading("计划录入中。。。");
                $.ajaxReq({
                    url : "plan/addPlan",
                    data : param,
                    type : 'POST',
                    dataType : 'json',
                    successFunc : function(data) {
                        $.endPageLoading();
                        MessageBox.success("新增计划成功","点击【确定】关闭当前页面", function(btn){
                            if("ok" == btn) {
                                $.redirect.closeCurrentPage();
                            }
                        });
                    },
                    errorFunc : function(resultCode, resultInfo) {
                        $.endPageLoading();
                        alert('计划提交失败:' + resultInfo);
                    }
                });
            }
            else {

            }
        },{"cancel":"取消"})

    },
    checkPlanTarget : function() {
        var scanHouseCounselorNum =  $('#scanHouseCounselorNum').val();
        var adviceNum = $('#adviceNum').val();
        var addWxNum = $('#addWxNum').val();
        var planType = $('#planType').val();

        if(planType != '1') {
            //非正常工作，不校验
            return true;
        }

        var checkFlag = false;

        var planTargetList = [
            {"ACTION_CODE":"ZX","NUM":adviceNum},
            {"ACTION_CODE":"HXJC","NUM":scanHouseCounselorNum},
            {"ACTION_CODE":"JW","NUM":addWxNum},
        ];
        $.ajaxReq({
                url:'plan/checkPlanTarget',
                data: {
                    PLAN_DATE : planEntry.planDate,
                    PLAN_TARGET_LIST : JSON.stringify(planTargetList),
                    EXECUTOR_ID : planEntry.executorId
                },
                type:'POST',
                dataType:'json',
                async:false,
                successFunc:function(data) {
                    checkFlag = true;
                },
                errorFunc:function(resultCode, resultInfo) {
                    alert(resultInfo);
                    checkFlag = false;
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
    },
    operCust : function(obj) {
        var $obj = $(obj);
        var operCode = $obj.attr('oper_code');
        if('1' == operCode) {
            //已删除的情况
            $obj.attr('oper_code', '0');
            $obj.find('span[tag=text]').removeClass('e_delete');
            $obj.find('span[tag=ico]').removeClass('e_ico-add').removeClass('e_red');
            $obj.find('span[tag=ico]').addClass('e_ico-delete').addClass('e_blue');
        } else {
            //未删除的情况
            $obj.attr('oper_code', '1');
            $obj.find('span[tag=text]').addClass('e_delete');
            $obj.find('span[tag=ico]').removeClass('e_ico-delete').removeClass('e_blue');
            $obj.find('span[tag=ico]').addClass('e_ico-add').addClass('e_red');
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
                param.FIRST_PLAN_DATE = planEntry.planDate;
                param.HOUSE_COUNSELOR_ID = planEntry.executorId;
            }
            $.ajaxReq({
                url : url,
                data : param,
                successFunc : function(data){
                    param.CUST_ID = data.CUST_ID;
                    var template = $('#CUST_TEMPLATE').html();
                    var tpl=$.Template(template);
                    param.CHECKED = 'checked';
                    tpl.insertFirst('#CUST_LIST',param,true);

                    selectCust.currentCustMap[param.CUST_ID] = param;
                    selectCust.selectedCustMap[param.CUST_ID] = param;

                    backPopup(obj);
                },
                errorFunc : function(resultCode, resultInfo) {
                    alert('资料新增失败——' + reusltInfo);
                }
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

        selectCust.selectedCustMap = {};//清空

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
                var beforeCustList = planEntry.planActionMap[beforeActionCode];
                templateData.BEFORE_ACTION_NAME = planEntry.getActionNameByCode(beforeActionCode);
                templateData.BEFORE_ACTION_CUSTNUM = beforeCustList.length;
                templateData.CUST_LIST = beforeCustList;
                $('#before_action_cust_list_part').html(template('before_action_cust_list_template',templateData));
            }

            var param = {};
            selectCust._queryCust(param, function() {
                showPopup('myPopup','customerSelectPopup');
            });

            if(currentActionCode == 'ZX') {
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
        var custList = [];
        if(planEntry.currentActionIndex == 0) {
            return custList;
        }
        var beforeActionCode = actionList[planEntry.currentActionIndex-1].ACTION_CODE;
        var beforeActionCustList = planEntry.planActionMap[beforeActionCode];
        $.each($('#before_action_cust_list_part').find('div[tag=cust_title]'), function(idx, item) {
            item = $(item)
            if(!item.hasClass('e_delete')) {
                var custId = item.attr('cust_id');
                $.each(beforeActionCustList, function(idx2, beforeActionCust) {
                    if(custId == beforeActionCust.CUST_ID) {
                        custList.push(beforeActionCust);
                        return false;
                    }
                })
            }
        });

        return custList;
    },
    setCovertGenderParam : function(param) {
        var actionCode = planEntry.currentAction;
        param.HOUSE_COUNSELOR_ID = planEntry.executorId;
        if(actionCode == 'DKCSMU' || actionCode == 'YJALTS') {
            param['UNEXECUTED_ACTION'] = actionCode;
        } else if(actionCode == 'JW') {

        } else {
            param['UNEXECUTED_ACTION'] = actionCode;
        }
    }
};

var workModePopup = {
    showPopup : function () {
        workModePopup.clear();

        showPopup("myPopup", "workModePopupItem");
    },
    clear : function() {

    },
    confirm : function() {

    },
    addNewWorkMode : function () {

    }
}