(function($){
    $.extend({prework:{
            init : function() {
                $.ajaxPost('initViewPreWorkNotice','&TRAIN_ID='+$("#TRAIN_ID").val(),function(data) {
                    var rst = new Wade.DataMap(data);
                    var train = rst.get("TRAIN");

                    $("#subtitle").html(train.get("START_DATE").substring(0,7));
                    $("#simple_date").html(train.get("START_DATE").substring(0,7));
                    $("#train_name").html(train.get("NAME"));
                    $("#date").html(train.get("START_DATE")+"至"+train.get("END_DATE"));
                    $("#address").html(train.get("TRAIN_ADDRESS"));

                    var signEndDate = train.get("SIGN_END_DATE");
                    if (signEndDate != null && signEndDate != "undefined") {
                        $("#sign_end_date").html(signEndDate);
                    }

                });
            }
        }});
})($);