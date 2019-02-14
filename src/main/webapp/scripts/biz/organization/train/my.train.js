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
                    html.push("<div class=\"main\">");
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
                    html.push("</div>");
                    html.push("</li>");
                    this.level ++;
                }
                $.insertHtml('beforeend', area, html.join(""));
            }
        }});
})($);