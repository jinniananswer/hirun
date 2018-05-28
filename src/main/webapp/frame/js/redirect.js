(function($){
    $.extend({redirect:{
            open : function(url, title){
                if($.os.phone){
                    window.location.href = url;
                }
                else{
                    parent.$.index.openNav(url, title);
                }
            }
        }});
})($);