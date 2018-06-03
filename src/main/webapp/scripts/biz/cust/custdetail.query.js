var custDetailQuery = {
    init : function() {
        custDetailQuery.queryCustDetail($.params.get('custId'));
        custDetailQuery.queryCustFinishActioListn($.params.get('custId'));
    },
    queryCustDetail : function(custId) {
        $.ajaxReq({
            url : 'cust/getCustById',
            data : {
                CUST_ID : custId
            },
            type : 'GET',
            successFunc : function(data) {
                $('div[tag=cust_name]').html(data.CUST_NAME);
                $('span[tag=sex]').html(data.SEX_DESC ? data.SEX_DESC : '');
                $('span[tag=wx_nick]').html(data.WX_NICK ? data.WX_NICK : '');
                $('span[tag=mobile_no]').html(data.MOBILE_NO ? data.MOBILE_NO : '');
                $('span[tag=house]').html(data.HOUSE_DESC ? data.HOUSE_DESC : '');
                $('span[tag=house_detail]').html(data.HOUSE_DETAIL ? data.HOUSE_DETAIL : '');
                $('span[tag=house_mode]').html(data.HOUSE_MODE_DESC ? data.HOUSE_MODE_DESC : '');
                $('span[tag=house_area]').html(data.HOUSE_AREA ? data.HOUSE_AREA : '');
                $('span[tag=cust_detail]').html(data.CUST_DETAIL ? data.CUST_DETAIL : '');
            },
            errorFunc : function(resultCode, resultInfo) {
                alert(resultInfo);
            }
        })
    },
    queryCustFinishActioListn : function(custId) {
        $.ajaxReq({
            url:'plan/getCustFinishActionList',
            data: {
                CUST_ID : custId
            },
            type:'GET',
            dataType:'json',
            async:true,
            successFunc:function(data) {
                $('#cust_detail').append(template('cust_action_list_template', data));
            }
        });
    }
};