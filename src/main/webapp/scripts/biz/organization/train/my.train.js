(function($){
    $.extend({train:{
            level : 0,
            init : function() {
                window["myTab"] = new Wade.Tabset("myTab");
                $.ajaxPost('initQueryMyTrain',null,function(data) {
                    var rst = new Wade.DataMap(data);
                    var employee = rst.get("EMPLOYEE");
                    var current = rst.get("CURRENT");
                    var history  = rst.get("HISTORY");
                    $.train.drawEmployee(employee);
                    $.train.drawTrains('current',current);
                    $.train.drawTrains('history',history);
                    $.train.drawEmployeeLevel();
                });
            },

            drawEmployeeLevel : function() {
                var html=[];
                var area = $("#employee_level");
                area.empty();
                for(var i=0;i<this.level;i++) {
                    html.push("<span></span>");
                }
                $.insertHtml('beforeend', area, html.join(""));
            },

            drawEmployee : function(data) {
                if (data == null || data == "undefined") {
                    return;
                }

                var sex = data.get("SEX");
                if (sex == "2") {
                    $("#employee_pic").attr("src", "/frame/img/female.png");
                }

                $("#employee_name").html(data.get("NAME"));
            },

            drawTrains : function(id, datas) {
                if (datas == null || datas == "undefined" || datas.length <= 0) {
                    $("#"+id+"_message").css("display", "");
                    return;
                }

                $("#"+id+"_message").css("display", "none");

                var area = $("#"+id);
                area.empty();

                var html=[];
                var length = datas.length;
                for (var i=0;i<length;i++) {
                    var data = datas.get(i);

                    html.push("<li class='link'>");
                    var type = data.get("TRAIN_TYPE");
                    if(type == "1") {
                        html.push("<div class=\"main\">");
                    }
                    else{
                        html.push("<div class='main' ontap='$.train.viewDetail(\""+data.get("TRAIN_ID")+"\")'>");
                    }
                    html.push("<div class=\"title\">"+data.get("TRAIN_NAME")+"</div>");
                    var type = data.get("TRAIN_TYPE");
                    var typeName = "";
                    if (type == "1") {
                        typeName = "岗前考评";
                    }
                    else if (type == "2") {
                        typeName = "职前培训";
                    }
                    else if (type == "3") {
                        typeName = "在职培训";
                    }

                    var courseName = data.get("COURSE_NAME");
                    html.push("<div class='content content-auto'>类型："+typeName+"</div>");
                    html.push("<div class='content content-auto'>课程："+courseName+"</div>");
                    html.push("<div class=\"content content-auto\">培训时间："+data.get("START_DATE")+"~"+data.get("END_DATE")+"</div>");
                    html.push("<div class='content content-auto'>");
                    var scores = data.get("SCORES");
                    if(scores == null || scores == "undfined" || scores.length <= 0) {
                        html.push("暂无成绩");
                    }
                    else {
                        var scoreLength = scores.length;
                        for(var j=0;j<scoreLength;j++) {
                            var score = scores.get(j);
                            var item = score.get("ITEM");
                            var value = score.get("SCORE");
                            if(value == null || value == "undefined") {
                                value = "";
                            }
                            if(item == null || item == "undefined") {
                                html.push("综合成绩："+value+"<br/>");
                            }
                            else {
                                if(item == "0") {
                                    html.push("通用成绩："+value+"<br/>");
                                }
                                else if(item == "1") {
                                    html.push("专业成绩："+value+"<br/>");
                                }
                            }
                        }
                    }
                    html.push("</div>");
                    html.push("</div>");
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-m\" ontap='$.train.viewNotice(\"" + data.get("TRAIN_ID") + "\");'>通</span>");
                    html.push("</div>");
                    html.push("</li>");
                    this.level ++;
                }
                $.insertHtml('beforeend', area, html.join(""));
            },

            viewNotice : function(trainId) {
                $.redirect.open('redirectToViewPreWorkNotice?TRAIN_ID='+trainId, '查看岗前考评告知书');
            },

            viewDetail : function(trainId) {
                $.redirect.open('redirectToTrainDetail?TRAIN_ID='+trainId, '培训详情');
            }
        }});
})($);