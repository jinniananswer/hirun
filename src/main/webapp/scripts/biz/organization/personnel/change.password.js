(function($){
    $.extend({org:{
        checkOldPassword : function(){
            $.ajaxPost('checkOldPassword','&OLD_PASSWORD='+$("#OLD_PASSWORD").val(),function(data){
                var result = new Wade.DataMap(data);
                var resultCode = result.get("RESULT_CODE");
                if(resultCode != "0"){
                    $.TipBox.show(document.getElementById('OLD_PASSWORD'), result.get("RESULT_INFO"), "red");
                }
            });
        },

        submit : function(){
            if($.validate.verifyAll("submitArea")) {
                var password = $("#PASSWORD").val();
                var confirmPassword = $("#CONFIRM_PASSWORD").val();
                if(password != confirmPassword){
                    $.TipBox.show(document.getElementById('CONFIRM_PASSWORD'), "新密码两次输入不一致", "red");
                    return;
                }
                var parameter = $.buildJsonData("submitArea");
                $.ajaxPost('submitChangePassword', parameter, function (data) {
                    MessageBox.success("密码修改成功，重新登陆时请使用新密码","点击确定关闭当前页面", function(btn){
                        parent.$.index.closeCurrentPage();
                    })
                });
            }
        }
    }});
})($);