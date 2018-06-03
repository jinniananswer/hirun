(function($){
    $.extend({
        redirect:{
            open : function(url, title){
                if($.os.phone){
                    window.location.href = url;
                }
                else{
                    top.$.index.openNav(url, title);
                }
            },
            closeCurrentPage : function () {
                if($.os.phone){
                    back();
                }
                else{
                    top.$.index.closeCurrentPage(url, title);
                }
            }
        },

    });
})($);