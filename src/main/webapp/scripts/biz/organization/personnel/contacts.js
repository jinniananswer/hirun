(function($){
    $.extend({contact:{
            query : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.ajaxPost('queryContacts', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.contact.drawContacts(datas);
                    });
                }
            },

            drawContacts : function(datas){
                $("#contacts").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var sex = data.get("SEX");
                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    if(sex == "1")
                        html.push("<img src=\"/frame/img/male.png\" class='e_pic-r' style='width:4em;height:4em'/>");
                    else
                        html.push("<img src=\"/frame/img/female.png\" class='e_pic-r' style='width:4em;height:4em'/>");

                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    var parentOrgName = data.get("PARENT_ORG_NAME");
                    if(parentOrgName != null && parentOrgName != "undefined")
                        html.push(parentOrgName + "-");
                    html.push(data.get("ORG_NAME"));
                    html.push("</div><div class='content'>"+data.get("JOB_ROLE_NAME"));
                    html.push("</div></div>")
                    html.push("<div class=\"side e_size-m\"><a href='tel:"+data.get("CONTACT_NO")+"'>");
                    html.push(data.get("CONTACT_NO"));
                    html.push("</a></div></div></div></li>");
                }

                $.insertHtml('beforeend', $("#contacts"), html.join(""));
            }
        }});
})($);