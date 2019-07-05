(function($){
    $.extend({custServiceActionQuery:{
            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });


                window["COND_START_DATE"] = new Wade.DateField(
                    "COND_START_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["COND_END_DATE"] = new Wade.DateField(
                    "COND_END_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["orgTree"] = new Wade.Tree("orgTree");

                $("#orgTree").textAction(function(e, nodeData){
                    var id = nodeData.id;
                    var text = nodeData.text;
                    $("#ORG_TEXT").val(text);
                    $("#ORG_ID").val(id);

                    backPopup(document.getElementById('UI-ORG'));
                    return true;
                });



                var now = $.date.now();
                $('#COND_START_DATE').val(now);
                $('#COND_END_DATE').val(now);


                $.ajaxPost('initQueryActionFinishInfo',null,function(data){
                    var trees = data.ORG_TREE;
                   // var rst = new Wade.DataMap(data);
                   // var datas = rst.get("CUSTSERVICEINFO");

                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();

                    }

                   // $.custServiceActionQuery.drawCustServiceInfo4Query(datas);
                    hidePopup('UI-popup','QueryCondPopupItem');
                    hidePopup('UI-popup','UI-QUERYCUSTSERVICE');
                    hidePopup('UI-popup','UI-ORG');


                });

            },



            query :function(){
                $.beginPageLoading("查询中。。。");

                var custServiceEmpId=$("#CUSTSERVICEEMPLOYEEID").val();
                var orgId=$("#ORG_ID").val();
                var startDate=$('#COND_START_DATE').val();
                var endDate=$('#COND_END_DATE').val();

                var param='&CUSTSERVICEEMPID='+custServiceEmpId+"&ORG_ID="+orgId+"&START_DATE="+startDate+"&END_DATE="+endDate;
                $.ajaxPost('queryCustServFinishActionInfo',param,function(data) {
                    var rst = new Wade.DataMap(data);
                    var datas=rst.get("CUSTSERVICEFINISHACTIONINFO");
                    $.custServiceActionQuery.drawActionInfo(datas);
                    hidePopup('UI-popup','QueryCondPopupItem');

                });
            },

            queryCustService :function(){
                $.beginPageLoading("查询中。。。");

                var custServiceName=$("#CUSTSERVICE_NAME").val();

                var param='&CUSTSERVICE_NAME='+custServiceName;
                $.ajaxPost('queryCustServiceByName',param,function(data) {
                    var rst = new Wade.DataMap(data);
                    var datas=rst.get("CUSTSERVICEINFO");
                    $.custServiceActionQuery.drawCustServiceInfo4Query(datas);

                });
            },

            drawActionInfo : function(datas){
                $.endPageLoading();
                $("#actioninfo").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#queryMessage").css("display","");
                    return;
                }
                $("#queryMessage").css("display","none");



                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var partyName=data.get("PARTY_NAME");
                    var create_date=data.get("CREATE_DATE");
                    var houseAddress=data.get("HOUSE_ADDRESS");
                    var custservName=data.get("CUSTSERVICENAME");
                    var scanCityTime=data.get("EXPERIENCE_TIME");
                    var scanCityCabins=data.get("CITYCABINNAMES");
                    var experience=data.get("EXPERIENCE");
                    var smjrlcfinishTime=data.get("SMJRLC_FINISHTIME");
                    var hzhkfinishTime=data.get("HZHK_FINISHTIME");
                    var apsjsfinishTime=data.get("APSJS_FINISHTIME");

                    var funcPrintTime=data.get("FUNCPRINT_CREATE_TIME");
                    var stylePrintTime=data.get("STYLEPRINT_CREATE_TIME");
                    var wxnick=data.get("WX_NICK");

                    html.push("<tr>");
                    html.push("<th class='red'>");
                    html.push(create_date.substr(0,19));
                    html.push("</th><th class='red'>")
                    html.push(partyName);
                    html.push("</th>");

                    html.push("<th class='red'>");
                    if(wxnick=="undefined" || wxnick ==null || wxnick =="null"){
                        html.push("");
                    }else{
                        html.push(wxnick)
                    }
                    html.push("</th>");

                    html.push("<th class='red'>");
                    if(houseAddress=="undefined" || houseAddress ==null || houseAddress =="null"){
                        html.push("");
                    }else{
                        html.push(houseAddress)
                    }
                    html.push("</th>");

                    html.push("<th class='red'>");
                    html.push(custservName)
                    html.push("</th><th class='red'>");
                    if(smjrlcfinishTime=="undefined" || smjrlcfinishTime ==null){
                        html.push("");
                    }else{
                        html.push(smjrlcfinishTime.substr(0,19))
                    }
                    html.push("</th><th class='red'>");
                    if(stylePrintTime=="undefined" || stylePrintTime ==null){
                        html.push("");
                    }else{
                        html.push(stylePrintTime.substr(0,19))
                    }
                    html.push("</th><th class='red'>");

                    if(funcPrintTime=="undefined" || funcPrintTime ==null){
                        html.push("");
                    }else{
                        html.push(funcPrintTime.substr(0,19))
                    }
                    html.push("</th><th class='red'>");

                    if(hzhkfinishTime!="undefined" && hzhkfinishTime !=null){
                        html.push(hzhkfinishTime.substr(0,19));
                    }else{
                        html.push("");
                    }
                    html.push("</th><th class='red'>");

                    if(apsjsfinishTime!="undefined" && apsjsfinishTime !=null){
                        html.push(apsjsfinishTime.substr(0,19));
                    }else{
                        html.push("");
                    }
                    html.push("</th><th class='red'>");

                    if(scanCityTime!="undefined" && scanCityTime !=null){
                        html.push(scanCityTime.substr(0,10));
                    }else{
                        html.push(scanCityTime);
                    }
                    html.push("</th><th class='red'>");
                    html.push(scanCityCabins);
                    html.push("</th><th class='red'>");
                    html.push(experience)
                    html.push("</th>")
                    html.push("</tr>")
                }

                $.insertHtml('beforeend', $("#actioninfo"), html.join(""));
            },

            drawCustServiceInfo4Query : function(datas){
                $.endPageLoading();
                $("#querycustservicesinfo").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    //$("#messagebox").css("display","");
                    $("#submitButton").css("display","none");
                    return;
                }

                //$("#messagebox").css("display","none");
                $("#submitButton").css("display","");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' custserviceEmpId='" + data.get("EMPLOYEE_ID") + "' custserviceEmpName='" + data.get("NAME") + "' ontap='$.custServiceActionQuery.selectCustService(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
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
                var custserviceEmpId=obj.attr("custserviceEmpId");
                var custserviceEmpName=obj.attr("custserviceEmpName");
                $("#CUSTSERVICEEMPLOYEENAME").val(custserviceEmpName);
                $("#CUSTSERVICEEMPLOYEEID").val(custserviceEmpId);
                backPopup(document.getElementById("QueryCondPopupItem"));

            },




        }});
})($);