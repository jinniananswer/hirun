var actionList = [
	{"ACTION_CODE":"JW","ACTION_NAME":"加微","SELECT_CUST_FUNC":"planSummarize.selectCust(this)","IS_SELECT_CUST":true},
    {"ACTION_CODE":"LTZDSTS","ACTION_NAME":"蓝图指导书推送"},
	{"ACTION_CODE":"GZHGZ","ACTION_NAME":"公众号关注"},
    {"ACTION_CODE":"HXJC","ACTION_NAME":"核心接触","SELECT_CUST_FUNC":"planSummarize.selectCust(this)","IS_SELECT_CUST":true},
	{"ACTION_CODE":"SMJRQLC","ACTION_NAME":"扫码进入全流程"},
	{"ACTION_CODE":"XQLTYTS","ACTION_NAME":"需求蓝图一推送"},
    {"ACTION_CODE":"ZX","ACTION_NAME":"咨询"},
	{"ACTION_CODE":"DKCSMU","ACTION_NAME":"带看城市木屋","SELECT_CUST_FUNC":"planSummarize.selectCust(this)","IS_SELECT_CUST":true},
	{"ACTION_CODE":"YJALTS","ACTION_NAME":"一键案例推送","SELECT_CUST_FUNC":"planSummarize.selectCust(this)","IS_SELECT_CUST":true},
];
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

        $.ajaxReq({
            url:'plan/getSummarizeInitData',
            data: {},
            type:'GET',
            dataType:'json',
            async:false,
            successFunc:function(data) {
                planSummarize.planDate = data.PLAN_DATE;
                planSummarize.planId = data.PLAN_ID;
                $('#planName').html(planSummarize.planDate + '计划总结');

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
                top.$.index.closePage("今日总结");
            },
        });

        //客户查询条件初始化 开始
        $.ajaxReq({
            url : 'queryHousesByEmployeeId',
            data : {

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
            url : 'queryHousesByEmployeeId',
            data : {

            },
            successFunc : function(data) {
                var options = [];
                $.each(data.HOUSES_LIST, function(idx, house) {
                    options.push({TEXT : house.NAME, VALUE : house.HOUSES_ID})
                })
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

                    $('#edit_cust_list_part').hide();
                    $('#finishInfoList').show();
                    $('#submitButton').show();
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
        summaryPopup.showSummaryPopup(actionCode, custId, planSummarize.planId, function(cause) {
            planSummarize.afterSummarizeCust(obj, cause);
        });
    },
    afterSummarizeCust : function(obj, cause) {
        var $obj = $(obj);
        var actionId = $obj.attr('action_id');
        var custId = $obj.attr('cust_id');
        $obj.attr('unfinish_cause_id', cause.UNFINISH_CAUSE_ID ? cause.UNFINISH_CAUSE_ID : '');
        $obj.attr('unfinish_cause_desc', cause.UNFINISH_CAUSE_DESC ? cause.UNFINISH_CAUSE_DESC : '');
        $obj.attr('oper_code', '2');

        $obj.find('span[tag=unfinish_cause_desc]').html(cause.UNFINISH_CAUSE_DESC ? cause.UNFINISH_CAUSE_DESC : '');
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

        $.ajaxReq({
            url : 'plan/summarizePlan',
            data : param,
            type : 'POST',
            dataType : 'json',
            successFunc : function(data) {
                alert('提交总结成功');
                top.$.index.closeCurrentPage();
            },
            errorFunc : function (resultCode, resultInfo) {

            }
        })
    },
    checkBeforeSubmit : function() {
        var hasUnSummaryCust = false;
        var errorMessage = '';
        $.each(actionList, function(idx, action){
            var actionCode = action.ACTION_CODE;
            $('#FINISH_INFO_' + actionCode + ' ul[tag=UNFINISH_CUST_LIST]').find('li[li_type=unFinish]').each(function(idx, unFinishCust) {
                var $unFinishCust = $(unFinishCust);
                if('2' != $unFinishCust.attr('oper_code')) {
                    hasUnSummaryCust = true;
                    return false;
                }
            })
            if(hasUnSummaryCust) {
                return false;
            }
        });

        if(hasUnSummaryCust) {
            errorMessage = '还有未完成客户没有填写原因，请先填写未完成原因';
        }

        return errorMessage;
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
            var url = '';
            param.CUST_STATUS = "1";
            if (param.CUST_ID) {
                url = 'cust/editCust';
            } else {
                url = 'cust/addCust';
                param.FIRST_PLAN_DATE = planSummarize.planDate;
            }
            $.ajaxReq({
                url: url,
                data: param,
                type: 'POST',
                dataType: 'json',
                successFunc: function (data) {
                    if(!param.CUST_ID) {
                        param.CUST_ID = data.CUST_ID;
                    }

                    custEditPopup.callback ? custEditPopup.callback(param) : '';

                    backPopup(obj);
                },
                errorFunc: function (resultCode, resultInfo) {

                }
            });
        }
    }
}

var summaryPopup = {
    actionId : '',
    custId : '',
    planId : '',
    callback : '',
    showSummaryPopup : function(actionCode, custId, planId, callback) {
        summaryPopup.planId = planId;

        //获取客户信息
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
                    $('#summarize_cust_info_part span[tag=sex]').html(body.SEX);
                    $('#summarize_cust_info_part span[tag=wx_nick]').html(body.WX_NICK);
                    $('#summarize_cust_info_part span[tag=mobile_no]').html(body.MOBILE_NO);
                    $('#summarize_cust_info_part span[tag=house_detail]').html(body.HOUSE_DETAIL);
                },
                error : function(status, errorMessage) {

                }
            }
        )

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
    }
}