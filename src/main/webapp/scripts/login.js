(function($){
    $.extend({login:{
        init : function() {
            $('#password').bind('keyup', function(event) {
                if (event.keyCode == "13") {
                    //回车执行查询
                    $('#login_btn').trigger("tap");
                }
            });
        },

        verifyLogin: function(){
            if($.validate.verifyAll("loginForm")){
                var param = $.buildJsonData("loginForm");
                $.ajaxPost('loginPost',param,function(data){
                    if($.os.phone){
                        window.location.href = "/phone_index";
                    }
                    else {
                        window.location.href = "/";
                    }
                }, function(resultCode, resultInfo){
                    if(resultCode == "HIRUN_LOGIN_000001")
                        $.TipBox.show(document.getElementById('username'), resultInfo, "red");
                    else if(resultCode == "HIRUN_LOGIN_000002")
                        $.TipBox.show(document.getElementById('password'), resultInfo, "red");
                    else{
                        MessageBox.error("错误信息","对不起，偶们的系统出错了，55555555555555", null,"", "错误编码："+resultCode+"，错误信息："+resultInfo+"亲，赶紧联系管理员报告功能问题吧");
                    }
                });
            }
            else{

            }
        }
    }});
})($);