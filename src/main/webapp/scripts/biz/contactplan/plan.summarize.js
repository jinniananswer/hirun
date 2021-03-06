var actionList = [
	{"ACTION_CODE":"JW","ACTION_NAME":"加微"},
    {"ACTION_CODE":"LTZDSTS","ACTION_NAME":"客户产品需求库使用指导书推送"},
	{"ACTION_CODE":"GZHGZ","ACTION_NAME":"公众号关注"},
    {"ACTION_CODE":"HXJC","ACTION_NAME":"核心接触","SELECT_CUST_FUNC":"planSummarize.selectCust(this)","IS_SELECT_CUST":true},
	{"ACTION_CODE":"SMJRQLC","ACTION_NAME":"扫码进入全流程"},
	{"ACTION_CODE":"XQLTYTS","ACTION_NAME":"需求蓝图一推送"},
    {"ACTION_CODE":"ZX","ACTION_NAME":"咨询"},
	{"ACTION_CODE":"DKCSMU","ACTION_NAME":"带看城市木屋","SELECT_CUST_FUNC":"planSummarize.selectCust(this)","IS_SELECT_CUST":true},
	{"ACTION_CODE":"YJALTS","ACTION_NAME":"一键案例推送"},
];

var relActionMap = {
    'JW' : [
        {"ACTION_CODE":'LTZDSTS',"UNFINISH_CAUSE_ID":'29','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'GZHGZ',"UNFINISH_CAUSE_ID":'30','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'HXJC',"UNFINISH_CAUSE_ID":'31','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'SMJRQLC',"UNFINISH_CAUSE_ID":'32','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'XQLTYTS',"UNFINISH_CAUSE_ID":'33','UNFINISH_CAUSE_DESC':'上一动作未完成'}
    ],
    'LTZDSTS' : [
        {"ACTION_CODE":'GZHGZ',"UNFINISH_CAUSE_ID":'30','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'HXJC',"UNFINISH_CAUSE_ID":'31','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'SMJRQLC',"UNFINISH_CAUSE_ID":'32','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'XQLTYTS',"UNFINISH_CAUSE_ID":'33','UNFINISH_CAUSE_DESC':'上一动作未完成'}
    ],
    'GZHGZ' : [
        {"ACTION_CODE":'HXJC',"UNFINISH_CAUSE_ID":'31','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'SMJRQLC',"UNFINISH_CAUSE_ID":'32','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'XQLTYTS',"UNFINISH_CAUSE_ID":'33','UNFINISH_CAUSE_DESC':'上一动作未完成'}
    ],
    'HXJC' : [
        {"ACTION_CODE":'SMJRQLC',"UNFINISH_CAUSE_ID":'32','UNFINISH_CAUSE_DESC':'上一动作未完成'},
        {"ACTION_CODE":'XQLTYTS',"UNFINISH_CAUSE_ID":'33','UNFINISH_CAUSE_DESC':'上一动作未完成'}
    ],
    'SMJRQLC' : [
        {"ACTION_CODE":'XQLTYTS',"UNFINISH_CAUSE_ID":'33','UNFINISH_CAUSE_DESC':'上一动作未完成'}
    ]
};
var planSummarize = {
    planId : '',
    planDate : '',
    currentAction : '',
    planList : [],
    finishActionList : [],
    unFinishActionList : [],
    unFinishSummaryList : [],//未完成动作总结的结果集
    addExtraCustActionList : [],//计划外触发的客户动作（在总结时选择的）的结果集
    custOperMap : {},
    executorId : '',
    isAdditionalRecordSummarize : '',
    init : function() {
        window["selectCustPopup"] = new Wade.Popup("selectCustPopup",{
            visible:false,
            mask:true
        });

        window["summarizeUnFinishCustPopup"] = new Wade.Popup("summarizeUnFinishCustPopup",{
            visible:false,
            mask:true
        });

        window["custInfoEditPopup"] = new Wade.Popup("custInfoEditPopup",{
            visible:false,
            mask:true
        });

        new radios('cause_options');

        var isAdditionalRecordSummarize = $.params.get('IS_ADDITIONAL_RECORD_SUMMARIZE');
        if(isAdditionalRecordSummarize) {
            planSummarize.isAdditionalRecordSummarize = isAdditionalRecordSummarize;
        } else {
            planSummarize.isAdditionalRecordSummarize = "0";
        }

        var planDate = $.params.get('PLAN_DATE');
        var executorId = $.params.get('EXECUTOR_ID');
        var param = {};
        if(planDate && executorId) {
            param.PLAN_DATE = planDate;
            param.PLAN_EXECUTOR_ID = executorId;
        }

        var exit = 'false';

        $.ajaxReq({
            url:'plan/getSummarizeInitData',
            data: param,
            type:'GET',
            dataType:'json',
            async:false,
            successFunc:function(data) {
                planSummarize.planDate = data.PLAN_DATE;
                planSummarize.planId = data.PLAN_ID;
                planSummarize.executorId = data.PLAN_EXECUTOR_ID;
                Header.setHeaderName(planSummarize.planDate + '计划总结');

                if(data.CUST_LIST && data.CUST_LIST.length > 0) {
                    //计划中有新客户
                    $.each(data.CUST_LIST, function(idx, cust) {
                        if(cust.WX_NICK) {
                            cust.IS_MUST = "true";
                        }
                    });
                    $('#edit_cust_list').html(template('edit_cust_list_template', data));
                    $('#submitButton').hide();

                    //遍历新客户资料补录列表，得到已完成的动作
                    $.each(data.CUST_LIST, function(idx, cust){
                        //异步获取动作并填充客户动作轨迹
                        $.ajaxReq({
                            url:'plan/getCustFinishActionList',
                            data: {
                                CUST_ID : cust.CUST_ID
                            },
                            type:'GET',
                            dataType:'json',
                            async:true,
                            successFunc:function(data) {
                                $('#edit_cust_' + cust.CUST_ID).append(template('edit_cust_action_list_template', data));
                            }
                        });
                    });
                } else {
                    //没新客户的情况直接展示完成情况
                    planSummarize.showFinishInfo();
                }
            },
            errorFunc:function(resultCode, resultInfo) {
                alert(resultInfo);
                exit = 'true';
                $.redirect.closeCurrentPage();
            },
        });


        if(exit == 'true') {
            return;
        }

        //客户查询条件初始化 开始
        $.ajaxReq({
            url : 'queryHousesByEmployeeId2',
            data : {
                EMPLOYEE_ID : planSummarize.executorId,
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
        ////客户查询条件初始化 结束

        //客户资料编辑初始化 开始
        window["SEX"] = new Wade.Switch("SEX",{
            switchOn:true,
            onValue:"1",
            offValue:"2",
            onColor:"blue",
            offColor:"red"
        });

        $.ajaxReq({
            url : 'queryHousesByEmployeeId2',
            data : {
                EMPLOYEE_ID : planSummarize.executorId,
            },
            successFunc : function(data) {
                var options = [];
                $.each(data.HOUSES_LIST, function(idx, house) {
                    options.push({TEXT : house.NAME, VALUE : house.HOUSES_ID})
                })
                options.push({TEXT : '散盘', VALUE : 'sanpan'})
                $.Select.append(
                    "custEditForm_house_container",
                    {
                        id:"custEditForm_house",
                        name:"HOUSE_ID",
                        nullable : "no",
                        desc : "楼盘",
                    },
                    options
                );
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        })
        $("#custEditForm_house_container").unbind('change').bind("change", function(){
            custEditPopup.onChangeHouses(this.value);
        });

        $.ajaxReq({
            url : 'common/getCodeTypeDatas',
            data : {
                CODE_TYPE : 'HOUSE_MODE'
            },
            successFunc : function(data) {
                var options = [];
                $.each(data.STATICDATA_LIST, function(idx, staticData) {
                    options.push({TEXT : staticData.CODE_NAME, VALUE : staticData.CODE_VALUE})
                })
                $.Select.append(
                    "custEditForm_houseMode_container",
                    // 参数设置
                    {
                        id:"custEditForm_houseMode",
                        name:"HOUSE_MODE",
                    },
                    options
                );
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        })

        //客户资料编辑初始化 结束
    },
    selectCust : function(obj) {
        selectCust.showSelectCust(obj, function (data) {
            planSummarize.afterSelectedCust(obj, data);
		});
	},
    afterSelectedCust : function(obj, data) {
    	var $obj = $(obj);
    	var actionCode = $obj.attr('action_code');
    	var custList = data.custList;
        var factCustNum = custList.length;

        //
        //先全量覆盖，因为可能多次选客户操作，如果没这一步，那前一次删了的都就找不到了 开始
        //保存总结
        var tmpSummarizedList = [];
        $('#FINISH_INFO_' + actionCode + ' ul[tag=UNFINISH_CUST_LIST]').find('li[li_type=unFinish]').each(function (idx, item) {
            var $item = $(item);
            if($item.attr('unfinish_cause_id')) {
                var summary = {};
                summary.CUST_ID = $item.attr('cust_id');
                summary.UNFINISH_CAUSE_ID = $item.attr('unfinish_cause_id');
                summary.UNFINISH_CAUSE_DESC = $item.attr('unfinish_cause_desc');
                tmpSummarizedList.push(summary);
            }
        })

        var templateData = {};
        templateData.ACTION_CODE = actionCode;
        templateData.UNFINISH_CUST_LIST = planSummarize.getCustListByActionCode(actionCode, planSummarize.unFinishActionList);
        html = template('unFinishCustListTemplate',templateData);
        $('#FINISH_INFO_' + actionCode + ' ul[tag=UNFINISH_CUST_LIST]').empty().append(html);

        //填上总结
        $.each(tmpSummarizedList, function(idx, summary) {
            ($('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+summary.CUST_ID+']').attr('unfinish_cause_id', summary.UNFINISH_CAUSE_ID));
            ($('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+summary.CUST_ID+']').attr('unfinish_cause_desc', summary.UNFINISH_CAUSE_DESC));
            ($('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+summary.CUST_ID+']').attr('oper_code', '2'));
            ($('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+summary.CUST_ID+']').find('span[tag=unfinish_cause_desc]').html(summary.UNFINISH_CAUSE_DESC));
        })
        //结束

        //将选择的客记录下来，用于提交
        $.each(custList, function(idx, cust) {
            //如果在未完成里找到了，则证明是trans，否则是新增
            if($('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+cust.CUST_ID+']').length > 0) {
                var actionId = $('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+cust.CUST_ID+']').attr('action_id');
                cust.ACTION_ID = actionId;
            } else {

            }
        });

        $('#FINISH_INFO_' + actionCode + ' ul[tag=FINISH_CUST_LIST] li[oper_code=2]').remove();

        //更新实际客户
        templateData.FINISH_CUST_LIST = custList;
        templateData.ACTION_CODE = actionCode;
        templateData.OPER_CODE = "2";
        var html = template('finishCustListTemplate',templateData);
        $('#FINISH_INFO_' + actionCode + ' ul[tag=FINISH_CUST_LIST]').append(html);

        //更新未完成客户
        $.each(data.custList, function(idx, cust){
            $('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+cust.CUST_ID+']').remove();
        });

        //更新实际数
        $('#FINISH_INFO_' + actionCode + ' span[tag=finishCustNum]').html(factCustNum);

        planSummarize.showSingleOper(actionCode);
    },
    showFinishInfo : function() {
        //先判断是否都补录完了
        var mustButUnEditCustList = [];
        $('#edit_cust_list li[tag=li_cust_content]').each(function(idx, item) {
            var $item = $(item);
            if($item.attr('IS_MUST') == 'true') {
                if($item.attr('oper_code') != '2') {
                    //表示没改
                    var mustButUnEditCust = {};
                    mustButUnEditCust.CUST_NAME = $item.attr('cust_name');
                    mustButUnEditCustList.push(mustButUnEditCust);
                }
            }
        });
        if(mustButUnEditCustList.length > 0) {
            var errorInfo = '以下客户必须补录完资料\n';
            $.each(mustButUnEditCustList, function(idx, mustButUnEditCust) {
                errorInfo += mustButUnEditCust.CUST_NAME + '\n';
            })
            alert(errorInfo);
            return;
        }

        $.ajaxReq({
            url : 'plan/getPlanFinishedInfo',
            data : {
                PLAN_ID : planSummarize.planId
            },
            type : 'GET',
            dataType : 'json',
            successFunc : function(data) {
                planSummarize.planList = data.PLANLIST;
                planSummarize.finishActionList = data.FINISH_ACTION_LIST;
                planSummarize.unFinishActionList = data.UNFINISH_ACTION_LIST;

                $.each(actionList, function(idx, actionMap) {
                    var actionCode = actionMap.ACTION_CODE;
                    var selectCustFunc = actionMap.SELECT_CUST_FUNC;
                    var actionName = actionMap.ACTION_NAME;
                    var planCustNum = 0;
                    var finishCustNum = 0;

                    var planCustList = planSummarize.getCustListByActionCode(actionCode, planSummarize.planList);
                    var finishCustList = planSummarize.getCustListByActionCode(actionCode, planSummarize.finishActionList);
                    var unFinishCustList = planSummarize.getCustListByActionCode(actionCode, planSummarize.unFinishActionList);

                    planCustNum = planCustList.length;
                    $.each(planCustList, function(idx, planCust) {
                        if(planCust.CUST_ID == '0') {
                            planCustNum += parseInt(planCust.CUST_NUM) - 1;
                        }
                    })
                    finishCustNum = finishCustList.length;

                    //插入html
                    var data = {};
                    data.ACTION_CODE = actionCode;
                    data.ACTION_NAME = actionName;
                    data.PLAN_CUSTNUM = planCustNum;
                    data.FINISH_CUSTNUM = finishCustNum;
                    data.SELECT_CUST_FUNC = selectCustFunc ? selectCustFunc : '';
                    data.PLAN_CUST_LIST = planCustList;
                    data.FINISH_CUST_LIST = finishCustList;
                    data.UNFINISH_CUST_LIST = unFinishCustList;
                    var html = template('finishInfoTemplate',data);
                    $('#finishInfoList').append(html);

                    //绑定未完成客户li上的编辑事件

                    $('#edit_cust_list_part').hide();
                    $('#finishInfoList').css('display','');
                    $('#submitButton').css('display','');
                })
            },
            errorFunc : function(resultCode, resultInfo) {

            }
        });

    },
    getCustListByActionCode : function (actionCode, list) {
        var custList = []
        $.each(list, function(idx, item) {
            var tmpActionCode = item.ACTION_CODE;
            if(tmpActionCode == actionCode) {
                custList = item.CUSTLIST;
                return false;
            }
        });

        return custList;
    },
    summarize : function(obj) {
        var $obj = $(obj);
        var actionCode = $obj.attr('action_code');
        var custId = $obj.attr('cust_id');
        summaryPopup.showSummaryPopup(actionCode, "singleOper", custId, planSummarize.planId, function(cause) {
            planSummarize.afterSummarizeCust(obj, cause);
        });
    },
    selectUnEntryCauseCusts : function(actionCode) {
        checkedAll(actionCode + '_custCheckBox', false);
        $('#FINISH_INFO_' + actionCode + ' li[li_type=unFinish]').each(function(idx, liItem) {
            liItem = $(liItem);
            if(!liItem.attr('unfinish_cause_id')) {
                liItem.find('input[name='+actionCode+'_custCheckBox]').attr('checked', 'true');
            }
        })
    },
    batchSummarize : function(obj) {
        var $obj = $(obj);
        var actionCode = $obj.attr('action_code');

        if(getCheckedBoxNum(actionCode + "_custCheckBox") == 0) {
            alert('请先选择客户');
            return;
        }

        var custList = getCheckedValues(actionCode + "_custCheckBox").split(',');
        summaryPopup.showSummaryPopup(actionCode, "batchOper", null, planSummarize.planId, function(cause) {
            planSummarize.afterBatchSummarizeCust(actionCode, custList, cause);
        }, function() {
            planSummarize.cancelBatchOper(actionCode);
        });
    },
    afterSummarizeCust : function(obj, cause) {
        var $obj = $(obj);
        var actionCode = $obj.attr('action_code');
        var custId = $obj.attr('cust_id');

        var selectorStr = '#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_' + custId + ']';

        var $liObj = $(selectorStr);

        $liObj.attr('unfinish_cause_id', cause.UNFINISH_CAUSE_ID ? cause.UNFINISH_CAUSE_ID : '');
        $liObj.attr('unfinish_cause_desc', cause.UNFINISH_CAUSE_DESC ? cause.UNFINISH_CAUSE_DESC : '');
        $liObj.attr('oper_code', '2');

        $liObj.find('span[tag=unfinish_cause_desc]').html(cause.UNFINISH_CAUSE_DESC ? cause.UNFINISH_CAUSE_DESC : '');

        planSummarize._dealSameCustRelAction(actionCode, custId);
    },
    afterBatchSummarizeCust : function(actionCode, custList, cause) {
        for(var i = 0, size = custList.length; i < size; i++) {
            var custId = custList[i];
            var selectorStr = '#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_' + custId + ']';
            var unfinishCustObj = $(selectorStr);
            unfinishCustObj.attr('unfinish_cause_id', cause.UNFINISH_CAUSE_ID ? cause.UNFINISH_CAUSE_ID : '');
            unfinishCustObj.attr('unfinish_cause_desc', cause.UNFINISH_CAUSE_DESC ? cause.UNFINISH_CAUSE_DESC : '');
            unfinishCustObj.attr('oper_code', '2');

            unfinishCustObj.find('span[tag=unfinish_cause_desc]').html(cause.UNFINISH_CAUSE_DESC ? cause.UNFINISH_CAUSE_DESC : '');

            planSummarize._dealSameCustRelAction(actionCode, custId);
        }

        planSummarize.showSingleOper(actionCode);
    },
    _dealSameCustRelAction : function(actionCode, custId) {
        var relActionList = relActionMap[actionCode];
        if(relActionList) {
            $.each(relActionList, function(idx, relAction) {
                var relSelectorStr = '#FINISH_INFO_' + relAction.ACTION_CODE + ' li[tag=UNFINISH_' + custId + ']';
                var $relLiObj = $(relSelectorStr);

                if($relLiObj.attr('oper_code') != '2' || !$relLiObj.attr('unfinish_cause_id')) {
                    $relLiObj.attr('unfinish_cause_id', relAction.UNFINISH_CAUSE_ID);
                    $relLiObj.attr('unfinish_cause_desc', relAction.UNFINISH_CAUSE_DESC);
                    $relLiObj.attr('oper_code', '2');

                    $relLiObj.find('span[tag=unfinish_cause_desc]').html(relAction.UNFINISH_CAUSE_DESC);
                }
            });
        }
    },
    showCustEditPopup : function(obj) {
        var $obj = $(obj)
        var custData = {};

        if($obj.attr('cust_id')) {
            custData.CUST_ID = $obj.attr('cust_id');
            custData.CUST_NAME = $obj.find('div[tag=cust_name]').html();
            custData.WX_NICK = $obj.find('span[tag=wx_nick]').html();
        }

        custEditPopup.showCustEditPopup(planSummarize.planId, custData, function(data) {
            planSummarize.afterCustEdit(obj, data);
        })
    },
    afterCustEdit : function(obj, data) {
        var $obj = $(obj);
        if($obj.attr('cust_id')) {
            //修改已有客户
            $obj.find('div[tag=cust_name]').html(data.CUST_NAME);
            $obj.find('span[tag=wx_nick]').html(data.WX_NICK);
            $obj.attr('oper_code', '2');
        } else {
            //新增客户
            var custList = [];
            custList.push(data);
            $('#edit_cust_list').prepend(template('edit_cust_list_template', {CUST_LIST:custList}));
        }
    },
    submit : function() {
        var errorMessage = planSummarize.checkBeforeSubmit();
        if(errorMessage && errorMessage != '') {
            alert(errorMessage);
            return;
        }

        var message = '确定总结吗';
        MessageBox.success("提示信息",message, function(btn){
            if("ok" == btn) {
                var param = {};
                var unfinishSummaryList = [];
                var addExtraCustActionList = [];
                var transToFinishList = [];
                $.each(actionList, function(idx, action){
                    var actionCode = action.ACTION_CODE;
                    $('#FINISH_INFO_' + actionCode + ' ul[tag=FINISH_CUST_LIST]').find('li[li_type=finish]').each(function(idx, finishCust) {
                        var $finishCust = $(finishCust);
                        if($finishCust.attr('oper_code') == '2') {
                            var actionId = $finishCust.attr('action_id');
                            if(actionId) {$
                                var transToFinish = {};
                                transToFinish.ACTION_ID = actionId;
                                transToFinishList.push(transToFinish)
                            } else {
                                var addExtraCustAction = {};
                                addExtraCustAction.ACTION_CODE = $finishCust.attr('action_code');
                                addExtraCustAction.CUST_ID = $finishCust.attr('cust_id');
                                addExtraCustActionList.push(addExtraCustAction);
                            }
                        }
                    })

                    $('#FINISH_INFO_' + actionCode + ' ul[tag=UNFINISH_CUST_LIST]').find('li[li_type=unFinish]').each(function(idx, unFinishCust) {
                        var $unFinishCust = $(unFinishCust);
                        if($unFinishCust.attr('oper_code') == '2') {
                            var unfinishSummary = {};
                            unfinishSummary.ACTION_ID = $unFinishCust.attr('action_id');
                            unfinishSummary.UNFINISH_CAUSE_ID = $unFinishCust.attr('unfinish_cause_id');
                            unfinishSummary.UNFINISH_CAUSE_DESC = $unFinishCust.attr('unfinish_cause_desc');
                            unfinishSummaryList.push(unfinishSummary);
                        }
                    })
                })

                param.PLAN_ID = planSummarize.planId;
                param.UNFINISH_SUMMARY_LIST = JSON.stringify(unfinishSummaryList);
                param.ADD_EXTRA_CUST_ACTION_LIST = JSON.stringify(addExtraCustActionList);
                param.TRANS_TO_FINISH_LIST = JSON.stringify(transToFinishList);
                param.IS_ADDITIONAL_RECORD_SUMMARIZE = planSummarize.isAdditionalRecordSummarize;

                $.beginPageLoading("提交总结中。。。");
                $.ajaxReq({
                    url : 'plan/summarizePlan',
                    data : param,
                    type : 'POST',
                    dataType : 'json',
                    successFunc : function(data) {
                        $.endPageLoading();
                        MessageBox.success("计划总结成功","点击【确定】关闭当前页面", function(btn){
                            if("ok" == btn) {
                                $.redirect.closeCurrentPage();
                            }
                        });
                    },
                    errorFunc : function (resultCode, resultInfo) {
                        $.endPageLoading();
                        alert(resultInfo);
                    }
                })
            }
            else {

            }
        },{"cancel":"取消"})

    },
    checkBeforeSubmit : function() {
        var hasUnSummaryCust = false;
        var errorMessage = '';
        var errorAction = [];
        $.each(actionList, function(idx, action){
            var actionCode = action.ACTION_CODE;
            hasUnSummaryCust = false;
            $('#FINISH_INFO_' + actionCode + ' ul[tag=UNFINISH_CUST_LIST]').find('li[li_type=unFinish]').each(function(idx, unFinishCust) {
                var $unFinishCust = $(unFinishCust);
                if('2' != $unFinishCust.attr('oper_code')) {
                    hasUnSummaryCust = true;
                    return false;

                }
            })
            if(hasUnSummaryCust) {
                errorAction.push("【" + action.ACTION_NAME + "】");
            }
        });

        if(errorAction.length > 0) {
            errorMessage = '以下这些动作还有未完成客户没有填写原因，请先填写未完成原因。包括' + errorAction.join(",");
        }

        return errorMessage;
    },
    batchOperTextOnClick : function(obj) {
        var $obj = $(obj);
        var actionCode = $obj.attr('action_code');
        planSummarize.showBatchOper(actionCode);
        //UNFINISH_CUST_LIST
        //FINISH_INFO_{{ACTION_CODE}}
        //UNFINISH_TITLE
    },
    cancelBatchOper : function(actionCode) {
        planSummarize.showSingleOper(actionCode);
    },
    showBatchOper : function(actionCode) {
        $('#FINISH_INFO_' + actionCode + " [tag=singleOper]").hide();
        $('#FINISH_INFO_' + actionCode + " [tag=batchOper]").css('display','');
    },
    showSingleOper : function(actionCode) {
        $('#FINISH_INFO_' + actionCode + " [tag=singleOper]").css('display','');
        $('#FINISH_INFO_' + actionCode + " [tag=batchOper]").hide();

        $('[name=' + actionCode + '_custCheckBox]').each(function(idx, item) {
            $(item).removeAttr('checked');
        });
    }
};

var selectCust = {
    currentCustMap : {},
    selectedCustMap : {},
    currentActionCode : '',
    callBack : '',
    init : function () {
        window["selectCustPopup"] = new Wade.Popup("selectCustPopup",{
            visible:false,
            mask:true
        });
    },
    showSelectCust : function (obj, callBack) {
        var $obj = $(obj);
        if(callBack) {
            selectCust.callBack = callBack;
        }

        var actionCode = $obj.attr('action_code');
        selectCust.actionCode = actionCode;

        selectCust.selectedCustMap = {};

        var param = {};
        selectCust._queryCust(param, function() {
            showPopup('selectCustPopup','customerSelectPopup');
        });
    },
    queryCust : function(obj) {
        var param = $.buildJsonData("queryCustParamForm");
        selectCust._queryCust(param, function() {
            backPopup(obj);
        });
    },
    _queryCust : function(param, callback) {
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
                        // var template = $('#CUST_TEMPLATE').html();
                        // var tpl=$.Template(template);
                        $('#CUST_LIST').append(template('CUST_TEMPLATE', item));
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
    confirmCusts : function(obj) {
        var custList = [];
        $.each(selectCust.selectedCustMap, function(key, cust) {
            custList.push(cust);
        })

        var data = {
            custList: custList,
        };

        if(selectCust.callBack) {
            selectCust.callBack(data);
        }

        backPopup(obj);
    },
    showCustEdit : function(obj) {
        var $obj = $(obj);

        resetArea("custForm", true);

        $("#SEX").val("1");

        var custId = $(obj).attr('custId');
        if(custId) {
            //修改已有客户
            var cust = selectCust.currentCustMap.get(custId);
            $('#custForm #CUST_NAME').val(cust.get('CUST_NAME'));
            $('#custForm #CUST_ID').val(custId);
            $('#custForm #WX_NICK').val(cust.get('WX_NICK'));
            $('#custForm #SEX').val(cust.get('SEX'));
            $('#custForm #MOBILE_NO').val(cust.get('MOBILE_NO'));
            $('#custForm #HOUSE_ID').val(cust.get('HOUSE_ID'));
            $('#custForm #HOUSE_DETAIL').val(cust.get('HOUSE_DETAIL'));
            $('#custForm #HOUSE_MODE').val(cust.get('HOUSE_MODE'));
            $('#custForm #HOUSE_AREA').val(cust.get('HOUSE_AREA'));
        }

        forwardPopup(obj,'custInfoEditPopup');
    },
    setCovertGenderParam : function(param) {
        var actionCode = selectCust.actionCode;
        // if(actionCode == 'DKCSMU' || actionCode == 'YJALTS' || actionCode == 'JW') {
            param['UNEXECUTED_ACTION'] = actionCode;
            param.HOUSE_COUNSELOR_ID = planSummarize.executorId;
        // }
    }
}

var custEditPopup = {
    custData : '',
    planId : '',
    callback : '',
    showCustEditPopup : function(planId, custData, callback) {
        custEditPopup.planId = planId;
        custEditPopup.custData = custData;
        custEditPopup.callback = callback;

        //重置区域的值
        resetArea("custForm", true);

        $("#SEX").val("1");

        if(custData) {
            //修改已有客户
            $('#custForm #CUST_NAME').val(custData.CUST_NAME);
            $('#custForm #CUST_ID').val(custData.CUST_ID);
            $('#custForm #WX_NICK').val(custData.WX_NICK);
        }

        showPopup('custInfoEditPopup', 'custInfoEditPopupItem');
    },
    submitCustInfo : function (obj) {
        if ($.validate.verifyAll("custForm")) {
            var param = $.buildJsonData("custForm");
            var housesId = $('#custEditForm_house').val();
            if(housesId == 'sanpan') {
                param.HOUSE_ID = $('#SCATTER_HOUSE').attr('houses_id');
            }

            var url = '';
            param.CUST_STATUS = "1";
            if (param.CUST_ID) {
                url = 'cust/editCust';
            } else {
                url = 'cust/addCust';
                param.FIRST_PLAN_DATE = planSummarize.planDate;
                param.HOUSE_COUNSELOR_ID = planSummarize.executorId;
            }
            // debugger;
            // return;
            $.beginPageLoading("客户资料补录中。。。");
            $.ajaxReq({
                url: url,
                data: param,
                type: 'POST',
                dataType: 'json',
                successFunc: function (data) {
                    $.endPageLoading();
                    if(!param.CUST_ID) {
                        param.CUST_ID = data.CUST_ID;
                    }

                    custEditPopup.callback ? custEditPopup.callback(param) : '';

                    backPopup(obj);
                },
                errorFunc: function (resultCode, resultInfo) {
                    $.endPageLoading();
                    alert(resultInfo);
                }
            });
        }
    },
    onChangeHouses : function(housesId) {
        $('#liScatter').hide();
        $('#SCATTER_HOUSE').attr('nullable', 'yes');
        if(housesId == 'sanpan') {
            $('#liScatter').css('display', '-webkit-box');
            $('#SCATTER_HOUSE').attr('nullable', 'no');
        }
    },
    onClickSelectScatterHousesButton : function(obj) {
        scatterHousesPopup.showHousesPopup(obj, function(housesId, housesName) {
            $('#SCATTER_HOUSE').val(housesName);
            $('#SCATTER_HOUSE').attr('houses_id', housesId);
        })
    }
}

var summaryPopup = {
    actionId : '',
    custId : '',
    planId : '',
    callback : '',
    cancelCallback : '',
    showSummaryPopup : function(actionCode, operType, custId, planId, callback, cancelCallback) {
        summaryPopup.planId = planId;

        //获取客户信息
        if(operType=="singleOper") {
            $('#summarize_cust_info_part').css('display','');
            $.ajaxRequest({
                    url : 'cust/getCustById',
                    data : {
                        CUST_ID : custId
                    },
                    type : 'GET',
                    dataType : 'json',
                    success : function(data) {
                        var body = data.BODY;
                        $('#summarize_cust_info_part div[tag=cust_name]').html(body.CUST_NAME);
                        $('#summarize_cust_info_part span[tag=sex]').html(body.SEX_DESC ? body.SEX_DESC : '');
                        $('#summarize_cust_info_part span[tag=wx_nick]').html(body.WX_NICK ? body.WX_NICK : '');
                        $('#summarize_cust_info_part span[tag=mobile_no]').html(body.MOBILE_NO ? body.MOBILE_NO : '');
                        $('#summarize_cust_info_part span[tag=house_detail]').html(body.HOUSE_DETAIL ? body.HOUSE_DETAIL : '');
                    },
                    error : function(status, errorMessage) {

                    }
                }
            )
        } else if(operType=="batchOper") {
            $('#summarize_cust_info_part').hide();
        }


        //获取原因列表
        $.ajaxRequest({
                url : 'plan/getCauseListByActionCode',
                data : {
                    ACTION_CODE : actionCode
                },
                type : 'GET',
                dataType : 'json',
                success : function(data) {
                    var body = data.BODY;
                    $('#cause_options').empty().html(template('cause_option_template', body));
                    new radios('cause_options');
                },
                error : function(status, errorMessage) {

                }
            }
        )

        if(callback) {
            summaryPopup.callback = callback;
        }

        if(cancelCallback) {
            summaryPopup.cancelCallback = cancelCallback;
        }

        showPopup('summarizeUnFinishCustPopup', 'summarizeUnFinishCustPopupItem');
    },
    confirm : function(obj) {
        //cause_options
        var flag = false;
        var cause = {};
        $('#cause_options').find('li').each(function (idx, elem) {
            var $elem = $(elem);
            if($elem.hasClass('checked')) {
                flag = true;
                cause.UNFINISH_CAUSE_ID = $elem.attr('cause_id');
                cause.UNFINISH_CAUSE_DESC = $elem.attr('cause_NAME');
                return false;
            }
        })

        if(!flag) {
            alert('请选择一个原因');
            return
        }

        if(summaryPopup.callback) {
            summaryPopup.callback(cause);
        }
        backPopup(obj);
    },
    cancel : function(obj) {
        if(summaryPopup.cancelCallback) {
            summaryPopup.cancelCallback();
        }
        backPopup(obj);
    }
}

var scatterHousesPopup = {
    callback : '',
    showHousesPopup : function(obj, callback) {
        scatterHousesPopup.searchHouses();
        if(callback) scatterHousesPopup.callback = callback;

        forwardPopup(obj,'scatterHousesPopupItem');
    },
    searchHouses : function(housesName) {
        $.ajaxReq({
            url : 'houses/queryScatterHouses',
            data : {
                HOUSES_NAME : housesName
            },
            successFunc : function (data) {
                $('#BIZ_SCATTER_HOUSES').html(template('scatter_houses_template', data))
            }
        })
    },
    clickHouses : function(obj) {
        var $obj = $(obj);
        var housesId = $obj.attr('houses_id');
        var housesName = $obj.attr('houses_name');

        if(scatterHousesPopup.callback) scatterHousesPopup.callback(housesId, housesName);

        backPopup(obj);
    },
    onClickAddScatterHousesButton : function(obj) {
        createScatterHousesPopup.showCreateScatterHousesPopup(obj, function(housesId, housesName) {
            if(scatterHousesPopup.callback) scatterHousesPopup.callback(housesId, housesName);
            backPopup(obj);
        })
    }
}

var createScatterHousesPopup = {
    callback : '',
    showCreateScatterHousesPopup : function(obj, callback) {
        if(callback) createScatterHousesPopup.callback = callback;

        forwardPopup(obj,'createScatterHousesPopupItem');
    },
    submitScatterHouses : function(obj) {
        if ($.validate.verifyAll("createScatterHousesForm")) {
            var param = $.buildJsonData("createScatterHousesForm");
            param.NAME = param.createScatterHousesForm_SCATTER_HOUSES_NAME;
            $.beginPageLoading("散盘新增中。。。");
            $.ajaxReq({
                url: 'submitScatterHouses',
                data: param,
                type: 'POST',
                dataType: 'json',
                successFunc: function (data) {
                    $.endPageLoading();

                    createScatterHousesPopup.callback ? createScatterHousesPopup.callback(data.HOUSE_ID, param.NAME) : '';

                    backPopup(obj);
                },
                errorFunc: function (resultCode, resultInfo) {
                    $.endPageLoading();
                    alert("散盘新增失败，原因是:" + resultInfo);
                }
            });
        }
    }
}