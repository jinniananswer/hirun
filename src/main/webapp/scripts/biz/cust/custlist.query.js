var custListQuery = {
    init : function() {
        window["UI-popup"] = new Wade.Popup("UI-popup",{
            visible:false,
            mask:true
        });

        $.ajaxReq({
            url : 'queryHouses',
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

        custListQuery.queryCustList({});
    },
    queryCustList : function(param) {
        $.ajaxReq({
            url : 'cust/queryCustList',
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
        custListQuery.queryCustList(param);
        hidePopup(obj);
    },
    showCustDetail : function(obj) {
        var $obj = $(obj);
        var url = 'biz/operations/cust/custdetail_query.jsp?custId=' + $obj.attr('cust_id');
        $.redirect.open(url, $obj.attr('cust_name'));
    },
    deleteCust : function(custId) {
        $.beginPageLoading('客户删除中');
        $.ajaxReq({
            url : 'cust/deleteCustById',
            data : {
                CUST_ID : custId
            },
            type : 'POST',
            successFunc : function(data) {
                $.endPageLoading();
                $('#CUST_ID_').remove();
            },
            errorFunc : function (resultCode, reusltInfo) {
                $.endPageLoading();
            }
        })
    }
};
