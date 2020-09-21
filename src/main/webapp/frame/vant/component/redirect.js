define(['ajax'], function(ajax){
    let redirect = {
        close: function() {
            window.history.back(-1);
        },

        toHome: function() {
            window.top.location.href = '/phone/phone_index.jsp';
        },

        open: function(url) {
            window.top.location.href = url;
        }
    }

    return redirect;
})