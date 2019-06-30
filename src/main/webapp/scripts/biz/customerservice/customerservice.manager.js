(function($){
    $.extend({custservicemanager:{
            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });

                $.ajaxPost('initPartyManager',null,function(data) {
                    var rst = new Wade.DataMap(data);
                    var flag=rst.get("FLAG");
                    $.custservicemanager.isShowQueryCond(flag);
                    var datas = rst.get("PARTYINFOLIST");
                    $.custservicemanager.drawPartyInfo(datas);
                    var childcustserviceinfos = rst.get("CUSTSERVICEINFO");
                    $.custservicemanager.drawCustServiceInfo4Query(childcustserviceinfos);
                    hidePopup('UI-popup','UI-popup-query-cond');

                });
            },

            isShowQueryCond :function(flag){
                var flag1=flag.get("FLAG");
                if("TRUE"==flag1){
                    $("#childcustservice").css("display","");
                }else{
                    $("#childcustservice").css("display","none");

                }
                },

            drawCustServiceInfo4Query : function(datas){
                $.endPageLoading();
                $("#querycustservicesinfo").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    $("#submitButton").css("display","none");
                    return;
                }

                $("#messagebox").css("display","none");
                $("#submitButton").css("display","");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' custserviceEmpId='" + data.get("EMPLOYEE_ID") + "' custserviceEmpName='" + data.get("NAME") + "' ontap='$.custservicemanager.selectCustService(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div>");
                    html.push("</div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#querycustservicesinfo"), html.join(""));
            },

            selectCustService : function (e) {
                var obj = $(e);
                var custserviceEmpId = obj.attr("custserviceEmpId");
                var custserviceName=obj.attr("custserviceEmpName");

                $("#CUSTSERVICEEMPLOYEENAME").val(custserviceName);
                $("#CUSTSERVICEEMPLOYEEID").val(custserviceEmpId);

                backPopup(document.getElementById("UI-popup-query-cond"));

            },

            query :function(){
                if($.validate.verifyAll("queryArea")) {
                    $.beginPageLoading();
                    var param=$.buildJsonData("queryArea");

                    $.ajaxPost('queryPartyByCustServicerEmployeeId',param, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("PARTYINFOLIST");
                        $.custservicemanager.drawPartyInfo(datas);
                        hidePopup('UI-popup','UI-popup-query-cond');
                    });
                }
            },



            drawPartyInfo : function(datas){
                $.endPageLoading();
                $("#partyinfos").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var wxnick=data.get("WX_NICK");
                    var mobile=data.get("MOBILE_NO");
                    var create_date=data.get("CREATE_DATE");
                    var party_name=data.get("PARTY_NAME");
                    var custserviceName=data.get("CUSTSERVICENAME");
                    /*ontap=\"$.custservicemanager.redirectFlow(\'"+data.get("PARTY_ID")+"\',\'"+data.get("PROJECT_ID")+"\')\"*/
                    html.push("<li class='link' ><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\">");
                    if(party_name!='undefined'&& party_name != null){
                        html.push("<div class=\"content\">");
                        html.push("客户姓名: " +party_name);
                        html.push("</div>")
                    }else {
                        html.push("<div class=\"content \">");
                        html.push("客户姓名: " );
                        html.push("</div>")
                    }


                    if(mobile!='undefined'&& mobile != null){
                        html.push("<div class=\"content\">");
                        html.push("联系电话: " +mobile);
                        html.push("</div>")
                    }else {
                        html.push("<div class=\"content \">");
                        html.push("联系电话: " );
                        html.push("</div>")
                    }

                    html.push("<div class=\"content \">");
                    html.push("咨询时间: " + create_date.substr(0,19));
                    html.push("</div>")

                    html.push("<div class=\"content\">");
                    html.push("客户代表: " +custserviceName);
                    html.push("</div>")

                    if(wxnick!='undefined'&& wxnick != null){
                        html.push("<div class=\"content\">");
                        html.push("微信昵称: " +wxnick);
                        html.push("</div>")
                    }else {
                        html.push("<div class=\"content \">");
                        html.push("微信昵称: " );
                        html.push("</div>")
                    }

                    html.push("</div>")

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-flow e_ico-pic-green e_ico-pic-r\" ontap='$.custservicemanager.redirectFlow(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'></span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-xs\" ontap='$.custservicemanager.redirectPartyVisit(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'>回</span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-xs\" ontap='$.custservicemanager.redirectClear(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'>清</span>");
                    html.push("</div>");
                    html.push("</div></div></li>");


                    html.push("</div></div></li>");
                }


                $.insertHtml('beforeend', $("#partyinfos"), html.join(""));
            },




            redirectFlow : function(parytId,projectId) {
                $.redirect.open('redirectToProjectFlow?PARTY_ID='+parytId+'&PROJECT_ID='+projectId, '客户流程');
            },

            redirectClear : function(parytId,projectId) {
                $.redirect.open('redirectToCustClear?PARTY_ID='+parytId+'&PROJECT_ID='+projectId, '客户清理申请');
            },

            redirectPartyVisit : function(parytId,projectId) {
                $.redirect.open('redirectPartyVisit?PARTY_ID='+parytId+'&PROJECT_ID='+projectId, '客户回访');
            },

        }});
})($);