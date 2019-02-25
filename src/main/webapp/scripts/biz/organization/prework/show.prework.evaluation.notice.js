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

                    var startDate = train.get("START_DATE");
                    var endDate = train.get("END_DATE");

                    $("#new_start_date").html(startDate);

                    $("#design_sign_date").html(startDate+" 08:20");
                    $("#design_start_date").html(startDate);

                    $("#make_design_sign_date").html(startDate+" 08:20");
                    $("#make_design_start_date").html(startDate);

                    $("#customer_sign_date").html(startDate+" 08:20");
                    $("#customer_start_date").html(startDate);

                    $("#cg_design_sign_date").html(startDate+" 08:20");
                    $("#cg_design_start_date").html(startDate);

                    $("#counselor_sign_date").html(startDate+"、"+endDate+" 08:20");
                    $("#counselor_sign_date").html(startDate+"、"+endDate);

                    $("#kj_sign_date").html(endDate+" 08:20");
                    $("#kj_start_date").html(endDate);

                    $("#rz_sign_date").html(endDate+" 08:20");
                    $("#rz_start_date").html(endDate);

                    $("#gc_sign_date").html(endDate+" 08:20");
                    $("#gc_start_date").html(endDate);

                    $("#gyl_sign_date").html(endDate+" 08:20");
                    $("#gyl_start_date").html(endDate);


                    $("#ddgl_sign_date").html(startDate+" 08:20");
                    $("#ddgl_start_date").html(startDate);

                    $("#shfw_sign_date").html(endDate+" 08:20");
                    $("#shfw_start_date").html(endDate);

                    $("#jg_sign_date").html(endDate+" 08:20");
                    $("#jg_start_date").html(endDate);

                    var signEndDate = train.get("SIGN_END_DATE");
                    if (signEndDate != null && signEndDate != "undefined") {
                        $("#sign_end_date").html(signEndDate);
                    }

                });
            }
        }});
})($);