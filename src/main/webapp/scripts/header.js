/**
 * Created by pc on 2018-06-10.
 */
var Header = {
    init : function () {
        $.ajaxReq({
            url : 'common/msg/getUnReadMsgCount',
            data : {
                RECV_ID : User.userId
            },
            type : 'GET',
            successFunc : function (data) {
                $('#myMsg').html(data.UNREAD_NUM);
            },
            errorFunc : function (resultCode, resultInfo) {

            }
        })
    },
    showFloatLayerFn : function (obj){
        // 获取 button 位置并设置给 floatLayer
        var left = $(obj).offset().left;
        var top = $(obj).offset().top;
        var width = $(obj).width();
        var height = $(obj).height();
        $("#funcArea").css("top",top + height);
        $("#funcArea").css("left",left);
        $("#funcArea").css("width",width);

        // 打开 floatLayer
        toggleFloatLayer('funcArea');
    },
    setHeaderName : function (name) {
        $('span[tag=header_name]:first').html(name);
    }
}
