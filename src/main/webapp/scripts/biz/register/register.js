(function($){
    $.extend({register:{
        checkUserName: function(userName){
            if(userName == null || userName == ""){
                $.validate.alerter.one("userName", "用户名不能为空");
                return;
            }
            $.ajaxPost('register/checkUser','userName:'+userName,function(data){
                alert(data["ss"]);
            },function(){
                alert('error');
            })
        }
    }});
})($);