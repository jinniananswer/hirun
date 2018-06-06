(function($){
    $.extend({
        date:{
            now : function(){
                var now = new Date();
                var y = now.getFullYear();
                var m = now.getMonth() + 1;
                m = m < 10 ? ('0' + m) : m;
                var d = now.getDate();
                d = d < 10 ? ('0' + d) : d;
                // var h = date.getHours();
                // h = h < 10 ? ('0' + h) : h;
                // var minute = date.getMinutes();
                // var second = date.getSeconds();
                // minute = minute < 10 ? ('0' + minute) : minute;
                // second = second < 10 ? ('0' + second) : second;
                return y + '-' + m + '-' + d;
            },
        },

    });
})($);