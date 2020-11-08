define(['ajax'], function(ajax){
    let redirect = {
        close: function() {
            window.history.back(-1);
        },

        toHome: function() {
            window.top.location.href = '../../../phone/home.jsp';
        },

        open: function(url) {
            window.top.location.href = url;
        }
    }

    return redirect;
})