(function($){
    $.extend({login:{
        verifyLogin: function(){
            if($.validate.verifyAll("loginForm")){
                var param = $.buildJsonData("loginForm");
                $.ajaxPost('loginPost',param,function(data){
                    var result = new Wade.DataMap(data);
                    var resultCode = result.get("HEAD").get("RESULT_CODE");

                    if(resultCode == "0"){
                        if($.os.phone){
                            window.location.href = "/phone_index";
                        }
                        else {
                            window.location.href = "/";
                        }
                        return;
                    }
                    if(resultCode == "HIRUN_LOGIN_000001")
                        $.TipBox.show(document.getElementById('username'), result.get("HEAD").get("RESULT_INFO"), "red");
                    else if(resultCode == "HIRUN_LOGIN_000002")
                        $.TipBox.show(document.getElementById('password'), result.get("HEAD").get("RESULT_INFO"), "red");
                },function(){
                    alert('error');
                });
            }
            else{

            }
        }
    }});
})($);