(function($){
    $.extend({train:{
            index : 0,
            currentIndex : 0,
            init : function() {
                $.ajaxPost('initQueryTrains',null,function(data) {
                    var rst = new Wade.DataMap(data);
                    var trains = rst.get("TRAINS");
                    $.train.drawTrains(trains);
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
                    html.push("<div class=\"main\" ontap='$.train.viewDetail(\""+data.get("TRAIN_ID")+"\");'><div class=\"title\">");
                    html.push(data.get("TRAIN_NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("起止时间："+data.get("START_DATE")+"~"+data.get("END_DATE"));
                    html.push("</div><div class='content'>"+"涉及课程："+data.get("COURSE_NAME"));
                    html.push("</div><div class='content'>班主任：");
                    html.push(data.get("EMPLOYEE_NAME"));
                    html.push("</div><div class='content'>培训描述：");
                    html.push(data.get("TRAIN_DESC"));
                    html.push("</div>");
                    html.push("<div class='content'>培训地址：");
                    html.push(data.get("TRAIN_ADDRESS"));
                    html.push("</div>");

                    html.push("<div class='content'>住宿地址：");
                    html.push(data.get("HOTEL_ADDRESS"));
                    html.push("</div>");

                    html.push("<div class='content'>报名状态：");
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
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-m\" ontap='$.train.signTrain(\"" + data.get("TRAIN_ID") + "\",\""+signStatus+"\");'>人</span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-m\" ontap='$.train.viewScore(\"" + data.get("TRAIN_ID") + "\");'>绩</span>");
                    html.push("</div>");
                    if(signStatus == "1") {
                        html.push("<div class=\"side e_size-s\">");
                        html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-m\" ontap='$.train.viewNotice(\"" + data.get("TRAIN_ID") + "\");'>通</span>");
                        html.push("</div>");
                    }
                    html.push("</div></div>");
                    html.push("</li>");
                }

                $.insertHtml('beforeend', $("#trains"), html.join(""));
            },

            viewDetail : function(trainId) {
                $.redirect.open('redirectToTrainDetail?TRAIN_ID='+trainId, '培训详情');
            },

            deleteTrain : function(trainId) {
                $.ajaxPost('deleteTrain','&TRAIN_ID='+trainId,function(data) {
                    MessageBox.success("成功信息", "删除培训成功", function(btn){
                        document.location.reload();
                    });
                });
            },

            signTrain : function(trainId, signStatus) {
                if(signStatus == "0") {
                    $.redirect.open('redirectToSign?TRAIN_ID=' + trainId, '培训报名');
                }
                else {
                    $.redirect.open('redirectToSignList?TRAIN_ID='+trainId, '人员详情');
                }
            },

            viewNotice : function(trainId) {
                $.redirect.open('redirectToViewTrainNotice?TRAIN_ID='+trainId, '查看培训告知书');
            },

            viewScore : function(trainId) {
                $.redirect.open('redirectToTrainScoreQuery?TRAIN_ID='+trainId, '培训成绩管理');
            }
        }});
})($);