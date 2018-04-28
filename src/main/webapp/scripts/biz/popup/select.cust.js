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

        var actionCode = $obj.attr('id');
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
    }
}