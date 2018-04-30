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
    planList : $.DatasetList(),
    finishActionList : $.DatasetList(),
    unFinishActionList : $.DatasetList(),
    init : function() {
        window["selectCustPopup"] = new Wade.Popup("selectCustPopup",{
            visible:false,
            mask:true
        });

        window["summarizeUnFinishCustPopup"] = new Wade.Popup("summarizeUnFinishCustPopup",{
            visible:false,
            mask:true
        });

        new radios('cause_options');

        $.ajaxRequest({
            url : 'plan/getSummarizeInitData',
            data : {},
            type:'GET',
            dataType:'json',
            async : false,
            success:function(data) {
                var result = $.DataMap(data);
                var resultCode = result.get('HEAD').get('RESULT_CODE');
                if(resultCode == 0) {
                    var body = result.get('BODY');
                    planSummarize.planDate = body.get('PLAN_DATE');
                    planSummarize.planId = body.get('PLAN_ID');
                    planSummarize.planList = body.get('PLANLIST');
                    planSummarize.finishActionList = body.get('FINISH_ACTION_LIST');
                    planSummarize.unFinishActionList = body.get('UNFINISH_ACTION_LIST');

                    $('#planName').html(planSummarize.planDate + '计划总结');

                    planSummarize.showFinishInfo();
                } else {
                    var resultInfo = result.get('HEAD').get('RESULT_INFO');
                    alert(resultInfo);
                    // checkFlag = false;
                }
            },
            error:function(status, errorMessage) {

            },
        });
    },
    selectCust : function(obj) {
        selectCust.showSelectCust(obj, function (data) {
            planSummarize.afterSelectedCust(obj, data);
		});
	},
    afterSelectedCust : function(obj, data) {
    	var $obj = $(obj);
    	var actionCode = $obj.attr('action_code');
        var factCustNum = data.custList.length;

        //更新实际客户
        var templateData = {};
        templateData.FINISH_CUST_LIST = data.custList;
        var html = template('finishCustListTemplate',templateData);
        $('#FINISH_INFO_' + actionCode + ' ul[tag=FINISH_CUST_LIST]').empty().append(html);

        //更新未完成客户
        //先全量覆盖，因为可能多次选客户操作，如果没这一步，那前一次删了的都就找不到了
        templateData.ACTION_CODE = actionCode;
        templateData.UNFINISH_CUST_LIST = JSON.parse(planSummarize.getCustListByActionCode(actionCode, planSummarize.unFinishActionList).toString());
        html = template('unFinishCustListTemplate',templateData);
        $('#FINISH_INFO_' + actionCode + ' ul[tag=UNFINISH_CUST_LIST]').empty().append(html);

        $.each(data.custList, function(idx, cust){
            $('#FINISH_INFO_' + actionCode + ' li[tag=UNFINISH_'+cust.CUST_ID+']').remove();
        });

        //更新实际数
        $('#FINISH_INFO_' + actionCode + ' span[tag=finishCustNum]').html(factCustNum);

    },
    showFinishInfo : function() {
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
            data.PLAN_CUST_LIST = JSON.parse(planCustList.toString());
            data.FINISH_CUST_LIST = JSON.parse(finishCustList.toString());
            data.UNFINISH_CUST_LIST = JSON.parse(unFinishCustList.toString());
            var html = template('finishInfoTemplate',data);
            $('#finishInfoList').append(html);
        })
    },
    getCustListByActionCode : function (actionCode, list) {
        var custList = $.DatasetList();
        $.each(list, function(idx, item) {
            var tmpActionCode = item.get('ACTION_CODE');
            if(tmpActionCode == actionCode) {
                custList = item.get('CUSTLIST');
                return;
            }
        });

        return custList;
    },
    summarize : function(obj) {
        var $obj = $(obj);
        var actionCode = $obj.attr('action_code');
        var custId = $obj.attr('cust_id');
        summaryPopup.showSummaryPopup(actionCode, custId, planSummarize.planId);
    }
};

var selectCust = {
    currentCustMap : $.DataMap(),
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
        var param = '';
        if(actionCode == 'JW') {
            $('#ADD_CUST_BUTTON').show();
        } else {
            $('#ADD_CUST_BUTTON').hide();
        }

        selectCust._queryCust(param, function() {
            showPopup('selectCustPopup','customerSelectPopup');
        });
        // $.ajaxGet('cust/queryCustList',param,function(data){
        //     var result = new Wade.DataMap(data);
        //     var resultCode = result.get("HEAD").get("RESULT_CODE");
        //
        //     if(resultCode == "0"){
        //         //清空表格
        //         $('#CUST_LIST').empty();
        //
        //         var body = result.get('BODY');
        //         var ds= body.get('CUSTOMERLIST')
        //         if(ds) {
        //             $.each(ds, function(idx, item) {
        //                 var custId = item.get('CUST_ID');
        //                 selectCust.currentCustMap.put(custId, item);
        //
        //                 var template = $('#CUST_TEMPLATE').html();
        //                 var tpl=$.Template(template);
        //                 tpl.append('#CUST_LIST',item,true);
        //             });
        //         }
        //
        //
        //
        //     }
        // },function(){
        //     alert('error');
        // });
    },
    queryCust : function(obj) {
        var param = '';
        selectCust._queryCust(param, function() {
            backPopup(obj);
        });
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
    confirmCusts : function(obj) {
        var custIdList = getCheckedValues('selectCustBox').split(",");
        var custNum = getCheckedBoxNum('selectCustBox');

        var newCustNum = ($("#newCustNum").val());

        var custName = '';
        if(newCustNum > 0) {
            custName += '新客户*' + newCustNum + ",";
        };

        var custList = [];
        for(var i = 0; i < custNum; i++) {
            var custDetail = {};
            var cust = selectCust.currentCustMap.get(custIdList[i]);
            // custDetail.custId = custIdList[i];
            custList.push(JSON.parse(cust.toString()));
        }

        var data = {
            newCustNum: newCustNum,
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
                        param.CUST_ID = result.get('BODY').get('CUST_ID');
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
    }
}

var summaryPopup = {
    custId : '',
    planId : '',
    showSummaryPopup : function(actionCode, custId, planId) {
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

        showPopup('summarizeUnFinishCustPopup', 'summarizeUnFinishCustPopupItem')
    },
    afterSummarizeCust : function(obj) {
        backPopup(obj);
    }
}