(function ($) {
    $.extend({
        familyMember: {

            init: function () {

                $.beginPageLoading();
                $.ajaxPost('initShowFamilyMember', '&PARTY_ID=' + $("#PARTY_ID").val() + '&PROJECT_ID=' + $("#PROJECT_ID").val(), function (data) {
                    $.endPageLoading();

                    let rst = new Wade.DataMap(data);
                    let familyMemberInfo = rst.get("FAMILY_MEMBER_INFO");
                    $.familyMember.drawFamilyInfo(familyMemberInfo);
                });
            },


            drawFamilyInfo: function (datas) {
                $.endPageLoading();

                $("#members").empty();
                var html = [];

                if (datas == null || datas.length <= 0) {
                    $("#messagebox").css("display", "");
                    return;
                }

                $("#messagebox").css("display", "none");


                var length = datas.length;
                for(let i=0;i<length;i++) {
                    let data = datas.get(i);
                    let wxnick=data.get("WX_NICK");
                    let mobile=data.get("MOBILE_NO");
                    let create_date=data.get("CREATE_TIME");
                    let party_name=data.get("PARTY_NAME");
                    let custserviceName=data.get("CUSTSERVICENAME");
                    let headUrl=data.get("HEAD_URL");
                    let showMobile=data.get("SHOWMOBILE");

                    html.push("<li class='link' ><div class=\"group\"><div class=\"content\">");

                    html.push("<div class='l_padding'>");
                    html.push("<div class=\"pic pic-middle\">");
                    if(headUrl!='undefined'&& headUrl != null){
                        html.push("<img src=\""+headUrl+"\" class='e_pic-r' style='width:2em;height:2em'/>");
                    }else {
                    }

                    html.push("</div></div>");
                    html.push("<div class=\"main\">");
                    if(party_name!='undefined'&& party_name != null){
                        html.push("<div class=\"content content-auto\">");
                        html.push("客户姓名: " +party_name);
                        html.push("</div>")
                    }else {
                        html.push("<div class=\"content content-auto \">");
                        html.push("客户姓名: " );
                        html.push("</div>")
                    }

                    if("YES"==showMobile){
                        if(mobile!='undefined'&& mobile != null){
                            html.push("<div class=\"content content-auto\">");
                            html.push("联系电话: " +mobile);
                            html.push("</div>")
                        }else {
                            html.push("<div class=\"content content-auto\">");
                            html.push("联系电话: " );
                            html.push("</div>")
                        }
                    }else{

                    }
                    html.push("<div class=\"content content-auto\">");
                    html.push("咨询时间: " + create_date.substr(0,19));
                    html.push("</div>")

                    html.push("<div class=\"content content-auto\">");
                    html.push("客户代表: " +custserviceName);
                    html.push("</div>")

                    if(wxnick!='undefined'&& wxnick != null){
                        html.push("<div class=\"content content-auto\">");
                        html.push("微信昵称: " +wxnick);
                        html.push("</div>")
                    }else {
                        html.push("<div class=\"content content-auto\">");
                        html.push("微信昵称: " );
                        html.push("</div>")
                    }

                    html.push("</div>")

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-flow e_ico-pic-green e_ico-pic-r\" ontap='$.familyMember.redirectFlow(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'></span>");
                    html.push("</div>");


                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#members"), html.join(""));
            },

            redirectFlow : function(parytId,projectId) {
                $.redirect.open('redirectToProjectFlow?PARTY_ID='+parytId+'&PROJECT_ID='+projectId, '客户流程');
            },

        }
    });
})($);