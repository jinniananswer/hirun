/**
 * Created by pc on 2018-06-09.
 */
var MsgListQuery = {
    init : function() {
        MsgListQuery.queryMsgList();
        MsgListQuery.setMsgToRead();
    },
    queryMsgList : function () {
        $.ajaxReq({
            url : 'common/msg/queryMsgListByRecvId',
            data : {
                RECV_ID : User.userId
            },
            type:'GET',
            successFunc : function (data) {
                $('#msg_list').html(template('msg_template', data));
            },
            errorFunc : function (resultCode, resultInfo) {

            }
        })
    },
    setMsgToRead : function () {
        $.ajaxReq({
            url : 'common/msg/setMsgListToRead',
            data : {
                RECV_ID : User.userId
            },
            type:'POST',
            successFunc : function (data) {

            },
            errorFunc : function (resultCode, resultInfo) {

            }
        })
    }
}
