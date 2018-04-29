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
    	var id = $obj.attr('id');
        var factCustNum = data.custList.length;
        var factCustDetail = '';
        if(factCustNum > 0) {
			for(var i = 0; i < data.custList.length; i++) {
				var cust = data.custList[i];
                factCustDetail += cust.get('CUST_NAME') + ',';
			}
		}

        $('#' + id + ' span[tag=factCustNum]').html('实际完成数：' + factCustNum);
        $('#' + id + ' span[tag=factCustDetail]').html('实际客户：' + factCustDetail);
    },
    showFinishInfo : function() {
        /*
        * ACTION_CODE/TAP_FUNCTION/ACTION_NAME/PLAN_CUSTNUM/PLAN_CUSTNAMES
        * FINISH_CUSTNUM/FINISH_CUSTNAMES/SIDE_NAME
        * */
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
            custList.push(cust);
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