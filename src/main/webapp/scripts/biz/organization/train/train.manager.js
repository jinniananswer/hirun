(function($){
    $.extend({train:{
            index : 0,
            currentIndex : 0,
            init : function() {
                $.ajaxPost('initManagerTrains',null,function(data) {
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
                        html.push("已终止报名");
                    }
                    html.push("</div>");

                    html.push("</div>");
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-edit e_ico-pic-green e_ico-pic-r\" ontap='$.train.initChangeTrain(\""+data.get("TRAIN_ID")+"\");'></span>");
                    html.push("</div>");
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-delete e_ico-pic-red e_ico-pic-r\" ontap='$.train.deleteTrain(\""+data.get("TRAIN_ID")+"\");'></span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-blue e_ico-pic-r e_ico-pic-xs\" ontap='$.train.viewSign(\"" + data.get("TRAIN_ID") + "\");'>人</span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-xs\" ontap='$.train.viewScore(\"" + data.get("TRAIN_ID") + "\");'>绩</span>");
                    html.push("</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#trains"), html.join(""));
            },

            viewSign : function(trainId) {
                $.redirect.open('redirectToSignList?TRAIN_ID='+trainId, '人员详情');
            },

            viewDetail : function(trainId) {
                $.redirect.open('redirectToTrainDetail?TRAIN_ID='+trainId, '培训详情');
            },

            viewScore : function(trainId) {
                $.redirect.open('redirectToTrainScoreManager?TRAIN_ID='+trainId, '培训成绩管理');
            },

            deleteTrain : function(trainId) {
                $.ajaxPost('deleteTrain','&TRAIN_ID='+trainId,function(data) {
                    MessageBox.success("成功信息", "删除培训成功", function(btn){
                        document.location.reload();
                    });
                });
            },

            initChangeTrain : function(trainId) {
                $.redirect.open('redirectToChangeTrain?TRAIN_ID='+trainId, '修改培训');
            }
        }});
})($);