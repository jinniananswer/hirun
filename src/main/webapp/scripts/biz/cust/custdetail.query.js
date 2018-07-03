var custDetailQuery = {
    init : function() {
        window["custActionTable"] = new Wade.Table("custActionTable", {
            fixedMode:true,
            editMode:false
        });

        window["custUnFinishCauseTable"] = new Wade.Table("custUnFinishCauseTable", {
            fixedMode:true,
            editMode:false
        });

        custDetailQuery.queryCustDetail($.params.get('custId'));
        custDetailQuery.queryCustFinishActioList($.params.get('custId'));
        custDetailQuery.queryCustContactList($.params.get('custId'));
        custDetailQuery.queryCustUnFinishActioList($.params.get('custId'));
    },
    queryCustDetail : function(custId) {
        $.ajaxReq({
            url : 'cust/getCustById',
            data : {
                CUST_ID : custId
            },
            type : 'GET',
            successFunc : function(data) {
                $('span[tag=cust_name]').html(data.CUST_NAME);
                $('span[tag=sex]').html(data.SEX_DESC ? data.SEX_DESC : '');
                $('span[tag=wx_nick]').html(data.WX_NICK ? data.WX_NICK : '');
                $('span[tag=mobile_no]').html(data.MOBILE_NO ? data.MOBILE_NO : '');
                $('span[tag=house]').html(data.HOUSE_DESC ? data.HOUSE_DESC : '');
                $('span[tag=house_detail]').html(data.HOUSE_DETAIL ? data.HOUSE_DETAIL : '');
                $('span[tag=house_mode]').html(data.HOUSE_MODE_DESC ? data.HOUSE_MODE_DESC : '');
                $('span[tag=house_area]').html(data.HOUSE_AREA ? data.HOUSE_AREA : '');
                $('span[tag=employee_name]').html(data.EMPLOYEE_NAME ? data.EMPLOYEE_NAME : '');
                $('span[tag=cust_detail]').html(data.CUST_DETAIL ? data.CUST_DETAIL : '');
            },
            errorFunc : function(resultCode, resultInfo) {
                alert(resultInfo);
            }
        })
    },
    queryCustFinishActioList : function(custId) {
        $.ajaxReq({
            url:'plan/getCustFinishActionList',
            data: {
                CUST_ID : custId
            },
            type:'GET',
            dataType:'json',
            async:true,
            successFunc:function(data) {
                $.each(data.CUST_FINISH_ACTION_LIST, function(idx, action) {
                    action._className = "no";
                    custActionTable.addRow(action);
                })
            }
        });
    },
    queryCustContactList : function(custId) {
        $.ajaxReq({
            url:'cust/queryCustContact',
            data: {
                CUST_ID : custId
            },
            type:'GET',
            dataType:'json',
            async:true,
            successFunc:function(data) {
                $('#custContactList').html(template('cust_contact_list_template', data));
            }
        });
    },
    queryCustUnFinishActioList : function(custId) {
        $.ajaxReq({
            url:'plan/queryCustUnFinishCause',
            data: {
                CUST_ID : custId
            },
            type:'GET',
            dataType:'json',
            successFunc:function(data) {
                $.each(data.CUST_UNFINISH_ACTION_CAUSE, function(idx, action) {
                    action._className = "no";
                    custUnFinishCauseTable.addRow(action);
                })
            }
        });
    }
};