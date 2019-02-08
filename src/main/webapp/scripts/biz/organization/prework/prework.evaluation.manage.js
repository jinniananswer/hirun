(function($){
    $.extend({prework:{
            index : 0,
            currentIndex : 0,
            init : function() {
                $.ajaxPost('initQueryPreWorkEvaluation',null,function(data) {
                    var rst = new Wade.DataMap(data);
                    var trains = rst.get("PREWORKS");
                    $.prework.drawTrains(trains);
                });
            },

            drawTrains : function(datas){
                $("#trains").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("TRAIN_NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("起止时间："+data.get("START_DATE")+"~"+data.get("END_DATE"));
                    html.push("</div>");
                    html.push("<div class='content'>考评地址：");
                    html.push(data.get("TRAIN_ADDRESS"));
                    html.push("</div>");
                    html.push("<div class='content'>报名状态：");
                    var signStatus = data.get("SIGN_STATUS");
                    if(signStatus == "0") {
                        html.push("可报名");
                    }
                    else if(signStatus == "1") {
                        html.push("已终止报名");
                    }
                    html.push("</div>");

                    html.push("</div>");
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-delete e_ico-pic-red e_ico-pic-r\" ontap='$.prework.deletePreWork(\""+data.get("TRAIN_ID")+"\");'></span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-xs\" ontap='$.prework.viewSign(\"" + data.get("TRAIN_ID") + "\");'>查</span>");
                    html.push("</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#trains"), html.join(""));
            },

            viewSign : function(trainId) {
                $.redirect.open('redirectToSignList?TRAIN_ID='+trainId, '培训详情');
            },

            deletePreWork : function(trainId) {
                $.ajaxPost('deleteTrain','&TRAIN_ID='+trainId,function(data) {
                    MessageBox.success("成功信息", "删除岗前考评成功", function(btn){
                        document.location.reload();
                    });
                });
            }
        }});
})($);