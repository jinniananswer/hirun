var custEdit = {
    init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup",{
            visible:false,
            mask:true
        });

        custEdit._queryCustList({});
        custEditPopup._init();
    },
    _queryCustList : function(param) {
        param.CUST_STATUS = '1,7';
        if(!param.HOUSE_COUNSELOR_IDS) {
            param.TOP_EMPLOYEE_ID = Employee.employeeId;
        }

        $.ajaxReq({
            url : 'cust/queryCustList4TopEmployeeId',
            data : param,
            type : 'GET',
            successFunc : function(data) {
                $('#cust_list').html(template("cust_template", data));
            },
            errorFunc : function(resultCode, resultInfo) {
                alert(resultInfo);
            }
        })
    },
    queryCustList4Cond : function(obj) {
        var param = $.buildJsonData("queryCustParamForm");
        custEdit._queryCustList(param);
        hidePopup(obj);
    },
    clickCustEditButton : function(obj) {
        var $obj = $(obj)
        var custId = $obj.attr('cust_id');
        custEditPopup.showCustEditPopup(custId, function(custInfo) {
            $('#CUST_ID_' + custId).find('.title').html(custInfo.CUST_NAME);
        });
    }
}

var custEditPopup = {
    callback : '',
    _init : function() {
        //性别
        window["SEX"] = new Wade.Switch("SEX",{
            switchOn:true,
            onValue:"1",
            offValue:"2",
            onColor:"blue",
            offColor:"red"
        });

        //户型
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

        $.Select.append(
            "custEditForm_house_container",
            {
                addDefault : false,
                id:"custEditForm_house",
                name:"HOUSE_ID",
                nullable : "no",
                desc : "楼盘",
            },
            []
        );

        $("#custEditForm_house_container").unbind('change').bind("change", function(){
            custEditPopup.onChangeHouses(this.value);
        });
    },
    onChangeHouses : function(housesId) {
        $('#liScatter').hide();
        $('#SCATTER_HOUSE').attr('nullable', 'yes');
        if(housesId == 'sanpan') {
            $('#liScatter').css('display', '-webkit-box');
            $('#SCATTER_HOUSE').attr('nullable', 'no');
        }
    },
    showCustEditPopup : function(custId, callback) {
        custEditForm_house.empty();
        $('#liScatter').hide();
        $('#SCATTER_HOUSE').attr('nullable', 'yes');

        $.beginPageLoading("查询客户信息。。。");
        $.ajaxReq({
            url : 'cust/getCustById',
            data : {
                CUST_ID : custId,
            },
            type: 'GET',
            successFunc : function(data) {
                $.ajaxReq({
                    url : 'queryHousesByEmployeeId2',
                    data : {
                        EMPLOYEE_ID : data.HOUSE_COUNSELOR_ID,
                    },
                    successFunc : function(housesData) {
                        $.endPageLoading();
                        var options = [];
                        $.each(housesData.HOUSES_LIST, function(idx, house) {
                            options.push({TEXT : house.NAME, VALUE : house.HOUSES_ID})
                            custEditForm_house.append(house.NAME,house.HOUSES_ID);
                        })
                        options.push({TEXT : '散盘', VALUE : 'sanpan'});
                        custEditForm_house.append('散盘','sanpan');
                        //custEditForm_house_container/custEditForm_house


                        // $.Select.append(
                        //     "custEditForm_house_container",
                        //     {
                        //         id:"custEditForm_house",
                        //         name:"HOUSE_ID",
                        //         nullable : "no",
                        //         desc : "楼盘",
                        //     },
                        //     options
                        // );
                        fillArea('custForm', data);

                        var isFound = false;
                        $.each(options, function(idx, option) {
                            if(data.HOUSE_ID == option.VALUE) {
                                isFound = true;
                                return;
                            }
                        })
                        if(!isFound) {
                            $("#custEditForm_house").val("sanpan");
                            custEditPopup.onChangeHouses($("#custEditForm_house").val());

                            $('#SCATTER_HOUSE').val(data.HOUSE_DESC);
                            $('#SCATTER_HOUSE').attr('houses_id', data.HOUSE_ID);
                        }
                    },
                    errorFunc : function(resultCode, resultInfo) {
                        $.endPageLoading();
                    }
                })
            },
            errorFunc : function (resultCode, reusltInfo) {
                $.endPageLoading();
            }
        })

        if(callback && callback != '') custEditPopup.callback = callback;

        showPopup('UI-popup','custEditPopupItem');
    },
    submitCustInfo : function (obj) {
        if ($.validate.verifyAll("custForm")) {
            var param = $.buildJsonData("custForm");
            var housesId = $('#custEditForm_house').val();
            if(housesId == 'sanpan') {
                param.HOUSE_ID = $('#SCATTER_HOUSE').attr('houses_id');
            }

            var url = 'cust/editCust';
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
    onClickSelectScatterHousesButton : function(obj) {
        scatterHousesPopup.showHousesPopup(obj, function(housesId, housesName) {
            $('#SCATTER_HOUSE').val(housesName);
            $('#SCATTER_HOUSE').attr('houses_id', housesId);
        })
    },
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
