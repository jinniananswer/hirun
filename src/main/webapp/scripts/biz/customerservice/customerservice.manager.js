(function($){
    $.extend({custservicemanager:{
            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });
                $.beginPageLoading();
                $.ajaxPost('initPartyManager',null,function(data) {
                    $.endPageLoading();
                    var rst = new Wade.DataMap(data);
                    var flag=rst.get("FLAG");
                    $.custservicemanager.isShowQueryCond(flag);
                    var datas = rst.get("PARTYINFOLIST");
                    $.custservicemanager.drawPartyInfo(datas);
                    //var childcustserviceinfos = rst.get("CUSTSERVICEINFO");
                    //$.custservicemanager.drawCustServiceInfo4Query(childcustserviceinfos);
                    var tagset=rst.get("TAGINFO");
                    $.custservicemanager.drawTagInfo(tagset);
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

            queryCustService :function(){
                $.beginPageLoading("查询中。。。");

                var custServiceName=$("#CUSTSERVICE_NAME").val();

                var param='&CUSTSERVICE_NAME='+custServiceName;
                $.ajaxPost('queryCustServiceByName',param,function(data) {
                    var rst = new Wade.DataMap(data);
                    var datas=rst.get("CUSTSERVICEINFO");
                    $.custservicemanager.drawCustServiceInfo4Query(datas);

                });
            },

            drawCustServiceInfo4Query : function(datas){
                $.endPageLoading();
                $("#querycustservicesinfo").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#custservicemessagebox").css("display","");
                    return;
                }

                $("#custservicemessagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' custserviceEmpId='" + data.get("EMPLOYEE_ID") + "' custserviceEmpName='" + data.get("NAME") + "' ontap='$.custservicemanager.selectCustService(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push(data.get("PARENT_ORG_NAME")+"-"+data.get("ORG_NAME"));
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

            selectTag : function (e) {
                var obj = $(e);
                var tagId = obj.attr("tagId4Query");
                var tagName=obj.attr("tagName4Query");

                $("#TAG_TEXT").val(tagName);
                $("#QUERY_TAG_ID").val(tagId);

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
                    var create_date=data.get("CONSULT_TIME");
                    var party_name=data.get("CUST_NAME");
                    var custserviceName=data.get("CUSTSERVICENAME");
                    var headUrl=data.get("HEAD_URL");
                    var partyTagName=data.get("PARTYTAGNAME");
                    var showMobile=data.get("SHOWMOBILE");

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

                    if(create_date!='undefined'&& create_date != null){
                        html.push("<div class=\"content content-auto\">");
                        html.push("咨询时间: " + create_date.substr(0,10));
                        html.push("</div>")
                    }else {
                        html.push("<div class=\"content content-auto \">");
                        html.push("咨询时间: " );
                        html.push("</div>")
                    }




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

                    if(wxnick!='partyTagName'&& partyTagName != null){
                        html.push("<div class=\"content content-auto\">");
                        html.push("<span class=\"e_tag e_tag-orange\" ontap='$.custservicemanager.redirectPartyTagManager(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'>");
                        html.push(partyTagName);
                        html.push("</span>");
                        html.push("</div>")
                    }else {
                        html.push("<div class=\"content content-auto\">");
                        html.push("<span class=\"e_tag e_tag-orange\" ontap='$.custservicemanager.redirectPartyTagManager(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'>");
                        html.push("无标签");
                        html.push("</span>");
                        html.push("</div>")
                    }

                    html.push("</div>")

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-flow e_ico-pic-green e_ico-pic-r\" ontap='$.custservicemanager.redirectFlow(\""+data.get("CUST_ID")+"\",\""+data.get("PROJECT_ID")+"\");'></span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-xs\" ontap='$.custservicemanager.redirectPartyVisit(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'>回</span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-xs\" ontap='$.custservicemanager.redirectClear(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'>清</span>");
                    html.push("</div>");


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

            redirectPartyTagManager : function(parytId,projectId) {
                    $.beginPageLoading();

                    $.ajaxPost('initPartyTagManager','PARTY_ID='+parytId+'&PROJECT_ID='+projectId, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("PARTYTAGINFO");
                        $.custservicemanager.drawPartyTagInfo(datas);
                        $('#PARTY_ID').val(parytId);
                    });
                showPopup('UI-popup','UI-popup-tag-cond');
            },

            drawTagInfo : function(datas){
                $.endPageLoading();

                $("#TagInfo").empty();
                var html = [];
                if(datas == null || datas.length <= 0){
                    return;
                }

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link'  tagId4Query='" + data.get("CODE_VALUE") + "' tagName4Query='" + data.get("CODE_NAME") + "' ontap='$.custservicemanager.selectTag(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("CODE_NAME"));
                    html.push("</div>");
                    html.push("</div></div></div></li>");
                }
                $.insertHtml('beforeend', $("#TagInfo"), html.join(""));

            },

            drawPartyTagInfo : function(datas){
                $.endPageLoading();

                $("#partyTagInfo").empty();
                var html = [];
                if(datas == null || datas.length <= 0){
                    $("#SUBMIT_PARTYTAGINFO").css("display","none");

                    return;
                }

                $("#SUBMIT_PARTYTAGINFO").css("display","");

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' tagId='" + data.get("CODE_VALUE") + "' ontap='$.custservicemanager.selectPartyTag(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("CODE_NAME"));
                    html.push("</div>");
                    html.push("</div></div></div></li>");
                }
                $.insertHtml('beforeend', $("#partyTagInfo"), html.join(""));

            },

            selectPartyTag : function (e) {
                var obj = $(e);

                var className = obj.attr("class");
                if(className == "link checked") {
                    obj.attr("class", "link");
                }
                else {
                    obj.attr("class", "link checked");
                }
                //清空样式
                obj.siblings().removeClass("checked");
            },

            submitPartyTagInfo : function(parytId,projectId) {
                var lis = $("#partyTagInfo li");
                var partyId=$("#PARTY_ID").val();

                var length = lis.length;
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        var tagId = li.attr("tagId");
                    }
                }

                if(tagId ==null) {
                    MessageBox.alert("您没有选择任何标签，请先选择标签");
                    return;
                }

                var parameter = 'TAG_ID='+ tagId+'&PARTY_ID='+partyId ;

                $.beginPageLoading();

                $.ajaxPost('submitPartyTagInfo', parameter, function (data) {
                    $.endPageLoading();
                    MessageBox.success("客户标签设置成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });
            },

            clearCond : function () {
                $('#NAME').val('');
                $('#WXNICK').val('');
                $('#MOBLIE').val('');
                $('#HOUSEADDRESS').val('');
                $('#CUSTSERVICEEMPLOYEENAME').val('');
                $('#CUSTSERVICEEMPLOYEEID').val('');
                $('#TAG_TEXT').val('');
                $('#QUERY_TAG_ID').val('');
            },

        }});
})($);