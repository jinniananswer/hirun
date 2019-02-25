(function($){
    $.extend({exam:{
            topics : null,
            currentIndex : null,
            examId : null,
            maxTime : 40*60,
            timer : null,
            errorTopic : ",",
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initPreworkExam',"&EXAM_ID="+this.examId,function(data) {
                    $("#CONFIRM_BUTTON").css("display", "none");
                    $("#exam").css("display", "none");
                    $("#messagebox").css("display", "");
                    var rst = new Wade.DataMap(data);
                    var needExam = rst.get("NEED_EXAM");

                    if(needExam == "false") {
                        $("#START_BUTTON").css("display", "none");

                    }
                    else {
                        $("#START_BUTTON").css("display", "");
                    }
                    $("#EXAM_DESC").empty();
                    $("#EXAM_DESC").html(rst.get("DESC"));
                });
            },

            start : function() {
                $.ajaxPost('startPreworkExam',"&EXAM_ID="+this.examId,function(data) {
                    var rst = new Wade.DataMap(data);
                    $.exam.topics = rst.get("EXAM_TOPIC");
                    $.exam.drawTopic(0);
                    $("#topic_select").css("display", "");
                    $("#timedown_root").css("display", "");
                    $.exam.timer = setInterval("$.exam.timeDown()", 1000);
                });
            },

            timeDown : function() {
                if ($.exam.maxTime >= 0) {
                    var minutes = Math.floor($.exam.maxTime / 60);
                    var seconds = Math.floor($.exam.maxTime % 60);
                    var msg = "距离结束还有" + minutes + "分" + seconds + "秒";
                    $("#timedown").html(msg);
                    --$.exam.maxTime;
                }
                else {
                    clearInterval($.exam.timer);
                    $.exam.submit(false);
                }
            },

            selectTopic : function(){

              try{
                  var index = $("#topic_index").val();
                  if(index == null || index == "" || index == "undfined") {
                      return;
                  }
                  var topicIndex = parseInt(index);
                  if(topicIndex == 0 || topicIndex > 40) {
                      MessageBox.alert("超过本次考试的题目数量范围");
                      return;
                  }
                  $.exam.drawTopic(topicIndex - 1);
              }
              catch(error){

              }
            },

            drawTopic : function(index) {
                $("#messagebox").css("display", "none");
                $("#START_BUTTON").css("display", "none");
                if(index != 0) {
                    $("#PREVIOUS_BUTTON").css("display", "");
                }
                else {
                    $("#PREVIOUS_BUTTON").css("display", "none");
                }

                if(index != $.exam.topics.length - 1) {
                    $("#NEXT_BUTTON").css("display", "");
                }
                else {
                    $("#NEXT_BUTTON").css("display", "none");
                    $("#SUBMIT_BUTTON").css("display", "");
                }
                $("#topic").empty();
                var html = [];
                $.exam.currentIndex = index;
                var topic = $.exam.topics.get(index);
                var type = topic.get("TYPE");
                var typeName = "单选题";
                if(type == "2") {
                    typeName = "多选题";
                }
                else if(type == "3") {
                    typeName = "判断题";
                }
                html.push("<div class='c_box c_box-border'><div class='c_title'>");
                html.push("<div class=\"text e_strong e_blue\">第"+(index+1)+"题："+topic.get("NAME")+"</div>");
                html.push("<div class=\"fn\">");
                html.push("<ul>");
                html.push("<li><span>题型："+typeName+"</span></li>");
                html.push("</ul>");
                html.push("</div></div>");

                html.push("<div class=\"l_padding l_padding-u\">");
                html.push("<div class=\"c_list c_list-line c_list-border c_list-space l_padding\">");
                html.push("<ul>");
                var answer = topic.get("ANSWER");
                var options = topic.get("OPTION");
                var length = options.length;
                for(var i=0;i<length;i++) {
                    var data = options.get(i);
                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("SYMBOL"));
                    html.push("</div>");
                    html.push("<div class=\"content content-auto\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");


                    html.push("</div>");

                    html.push("<div class=\"side\">");
                    if(type == "1" || type == "3") {
                        if(answer == data.get("SYMBOL")) {
                            html.push("<input type=\"radio\" name='RADIO_"+$.exam.currentIndex+"' value='" + data.get("SYMBOL") + "' checked ontap='$.exam.selectAnswer(this);'/>");
                        }
                        else {
                            html.push("<input type=\"radio\" name='RADIO_"+$.exam.currentIndex+"' value='" + data.get("SYMBOL") + "' ontap='$.exam.selectAnswer(this);'/>");
                        }
                    }
                    if(type == "2") {
                        if(answer != null && answer.indexOf(data.get("SYMBOL")) >= 0) {
                            html.push("<input type=\"checkbox\" value='" + data.get("SYMBOL") + "' checked ontap='$.exam.selectAnswer(this);'/>");
                        }
                        else {
                            html.push("<input type=\"checkbox\" value='" + data.get("SYMBOL") + "' ontap='$.exam.selectAnswer(this);'/>");
                        }
                    }

                    html.push("</div>");


                    html.push("</div></div></li>");
                }
                html.push("</ul>");
                html.push("</div>");
                html.push("</div>");
                html.push("</div>");
                html.push("<div class='c_space'></div>");


                $.insertHtml('beforeend', $("#topic"), html.join(""));
            },

            next : function() {
                this.drawTopic(this.currentIndex + 1);
            },

            previous : function() {
                this.drawTopic(this.currentIndex - 1);
            },

            selectAnswer : function(obj) {
                var box = $(obj);
                var value = box.val();
                var topic = this.topics.get(this.currentIndex);
                var type = topic.get("TYPE");
                var answer = topic.get("ANSWER");
                if(answer == null) {
                    answer = "";
                }
                if(type == "2") {
                    if(box.attr("checked")) {
                        answer += value;
                    }
                    else {
                        answer = answer.replace(value, "");
                    }
                }
                else {
                    answer = value;
                }

                topic.put("ANSWER", answer);
            },

            selectExam : function(obj) {
                var radio = $(obj);
                this.examId = radio.val();
            },

            submit : function(needConfirm) {
                var allScore = 0;
                var length = this.topics.length;
                var answerScore = 0;
                for(var i=0;i<length;i++) {
                    var topic = this.topics.get(i);
                    var score = parseInt(topic.get("SCORE"));
                    allScore = score+allScore;

                    var answer = topic.get("ANSWER");
                    if(answer == null && needConfirm) {
                        MessageBox.alert("您第"+(i+1)+"题尚未作答");
                        return;
                    }

                    if(answer == null) {
                        continue;
                    }
                    var type = topic.get("TYPE");
                    var correctAnswer = topic.get("CORRECT_ANSWER");
                    if(type == "1" || type == "3") {
                        if(answer == correctAnswer) {
                            answerScore += score;
                        }
                        else{
                            this.errorTopic += i+",";
                        }
                    }
                    else {
                        if(answer.length == correctAnswer.length) {
                            var num = 0;
                            for(var j=0;j<answer.length;j++) {
                                if(correctAnswer.indexOf(answer.charAt(j)) >= 0) {
                                    num++;
                                }
                            }

                            if( num == correctAnswer.length) {
                                answerScore += score;
                            }
                            else {
                                this.errorTopic += i+",";
                            }
                        }
                        else{
                            this.errorTopic += i+",";
                        }
                    }
                }
                $.ajaxPost('submitExam', "&ANSWER_SCORE="+answerScore+"&EXAM_ID="+$.exam.examId, function (data) {
                    clearInterval($.exam.timer);
                    MessageBox.success("提交试卷成功，您的成绩为"+answerScore+"分","点击确定返回，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            $.endPageLoading();
                            document.location.reload();
                        }
                        else{
                            //$.endPageLoading();
                            $("#submitArea").empty();
                            $.exam.showErrorTopic();
                            //$.redirect.closeCurrentPage();
                        }
                    },{"cancel":"查看错题"});
                });
            },

            showErrorTopic : function(){
                $("#error_topic").empty();
                var html = [];
                var length = this.topics.length;
                for(var i=0;i<length;i++) {

                    if(this.errorTopic.indexOf(i+"") >= 0) {
                        var topic = this.topics.get(i);
                        html.push("<li class='link'>");
                        html.push("<div class=\"group\">");
                        html.push("<div class=\"content\">");
                        html.push("<div class='l_padding'>");
                        html.push("<div class=\"pic pic-middle\">");
                        html.push("</div>");
                        html.push("</div>");
                        html.push("<div class=\"main\">");
                        html.push("<div class=\"title title-auto\">");
                        html.push(topic.get("NAME"));
                        html.push("</div>");
                        var options = topic.get("OPTION");
                        var optionLength = options.length;
                        for(var j=0;j<optionLength;j++){
                            var data = options.get(j);
                            html.push("<div class=\"content content-auto\">");
                            html.push(data.get("SYMBOL"));
                            html.push(data.get("NAME"));
                            html.push("</div>");
                        }

                        html.push("</div>");
                        html.push("</div>");
                        html.push("</div>");
                        html.push("</li>");
                    }
                }

                $.insertHtml('beforeend', $("#error_topic"), html.join(""));
                showPopup('UI-popup','UI-ERROR_ITEM');
            }
        }});
})($);