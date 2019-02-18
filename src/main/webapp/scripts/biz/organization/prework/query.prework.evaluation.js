(function($){
    $.extend({prework:{
            init : function() {
                $.ajaxPost('initQueryPreWorkEvaluation',null,function(data) {
                    var rst = new Wade.DataMap(data);
                    var preworks = rst.get("PREWORKS");
                    $.prework.drawPreworks(preworks);
                });
            },

            drawPreworks : function(datas){
                $("#preworks").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link'><div class=\"group\"><div class=\"content content-auto\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title title-auto\">");
                    html.push(data.get("TRAIN_NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class='e_strong'>起止时间：</span>"+data.get("START_DATE")+"~"+data.get("END_DATE"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'><span class='e_strong'>考评地址：</span>");
                    html.push(data.get("TRAIN_ADDRESS"));
                    html.push("</div>");

                    html.push("<div class='content content-auto'><span class='e_strong'>报名状态：</span>");
                    var signStatus = data.get("SIGN_STATUS");
                    if(signStatus == "0") {
                        html.push("可报名");
                    }
                    else if(signStatus == "1") {
                        html.push("已截止止报名");
                    }
                    html.push("</div>");

                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-m\" ontap='$.prework.signPrework(\"" + data.get("TRAIN_ID") + "\");'>报</span>");
                    html.push("</div>");
                    if(signStatus == "1") {
                        html.push("<div class=\"side e_size-s\">");
                        html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-m\" ontap='$.prework.viewNotice(\"" + data.get("TRAIN_ID") + "\");'>通</span>");
                        html.push("</div>");
                    }
                    html.push("</div></div>");
                    html.push("</li>");
                }

                $.insertHtml('beforeend', $("#preworks"), html.join(""));
            },

            signPrework : function(trainId) {
                $.redirect.open('redirectToSignPreWork?TRAIN_ID='+trainId, '岗前考评报名');
            },

            viewNotice : function(trainId) {
                $.redirect.open('redirectToViewPreWorkNotice?TRAIN_ID='+trainId, '查看岗前考评告知书');
            }
        }});
})($);