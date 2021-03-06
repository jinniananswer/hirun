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

                    html.push("<div class='content content-auto'><span class='e_strong'>报名截止时间：</span>");
                    html.push(data.get("SIGN_END_DATE"));
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
                    html.push("<span class=\"e_ico-pic-blue e_ico-pic-r e_ico-pic-xs\" ontap='$.prework.signPrework(\"" + data.get("TRAIN_ID") + "\",\""+signStatus+"\");'>人</span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-xs\" ontap='$.prework.viewScore(\"" + data.get("TRAIN_ID") + "\");'>绩</span>");
                    html.push("</div>");
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-xs\" ontap='$.prework.viewNotice(\"" + data.get("TRAIN_ID") + "\");'>通</span>");
                    html.push("</div>");

                    html.push("</div></div>");
                    html.push("</li>");
                }

                $.insertHtml('beforeend', $("#preworks"), html.join(""));
            },

            signPrework : function(trainId, signStatus) {
                if(signStatus == "0") {
                    $.redirect.open('redirectToSignPreWork?TRAIN_ID='+trainId, '岗前考评报名');
                }
                else {
                    $.redirect.open('redirectToPreworkSignList?TRAIN_ID='+trainId, '人员详情');
                }
            },

            viewNotice : function(trainId) {
                $.redirect.open('redirectToViewPreWorkNotice?TRAIN_ID='+trainId, '查看岗前考评告知书');
            },

            viewScore : function(trainId) {
                $.redirect.open('redirectToTrainScoreQuery?TRAIN_ID='+trainId, '培训成绩查询');
            }
        }});
})($);