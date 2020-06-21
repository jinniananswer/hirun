(function($){
    $.extend({custclear:{

            init : function() {

                $.Select.append(
                    "mySelectContainer",
                    {
                        id:"CLEAR_REASON",
                        name:"CLEAR_REASON"
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"无效客户", VALUE:"1"},
                        {TEXT:"测试客户", VALUE:"2"},
                    ]
                );

                $.beginPageLoading();
                $.ajaxPost('initQueryForCustClear','&PARTY_ID='+$("#PARTY_ID").val()+'&PROJECT_ID='+$("#PROJECT_ID").val(),function(data) {
                    $.endPageLoading();

                    var rst = new Wade.DataMap(data);
                    var partyInfo = rst.get("CUSTSERVICEINFOLIST");
                    var applyInfo=rst.get("APPLYINFO");
                    $.custclear.drawApplyInfo(applyInfo);
                    $.custclear.drawPartyInfo(partyInfo);

                });
            },

            drawApplyInfo :function(datas){
                $.endPageLoading();

                $("#applyinfo").empty();
                $("#submitButton").css("display","");
                $("#reason").css("display","");
                var html = [];
                if(datas == null || datas.length <= 0){
                    return;
                }
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var applyDate=data.get("APPLY_DATE");
                    var auditdate=data.get("AUDIT_DATE");
                    var auditadvice=data.get("AUDIT_ADVICE");
                    var applyreason=data.get("APPLY_REASON");
                    var status=data.get("STATUS");
                    var auditname=data.get("AUDITNAME");

                    if(auditdate=='undefined'|| auditdate == null) {
                        auditdate='';
                    }
                    if(auditadvice=='undefined'|| auditadvice == null) {
                        auditadvice='';
                    }
                    if(auditname=='undefined'|| auditname == null) {
                        auditname='';
                    }

                    html.push("<li class='link'><div class=\"group\"><div class=\"content content-auto\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"content content-auto\">");
                    html.push("<span class=''>申请时间：</span>")
                    html.push(applyDate.substr(0,19));
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    if(status=='0'){
                    html.push("<span class=''>状态：未处理</span>");

                    $("#submitButton").css("display","none");
                    $("#reason").css("display","none");

                    }else if(status=='1'){
                    html.push("<span class=''>状态：审批通过</span>");
                    $("#submitButton").css("display","none");
                    $("#reason").css("display","none");
                    }else if(status=='2'){
                        html.push("<span class=''>状态：审批不通过</span>");
                    }
                    html.push("</div>");


                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>申请原因：</span>"+applyreason);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>审核员工：</span>"+auditname);
                    html.push("</div>");


                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>审核时间：</span>"+auditdate.substr(0,19));
                    html.push("</div>");

                    html.push("</div>");

                    html.push("</div></div>");
                    html.push("</li>");
                }
                $.insertHtml('beforeend', $("#applyinfo"), html.join(""));

            },

            drawPartyInfo : function(datas){
                $.endPageLoading();

                $("#preworks").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                let length = datas.length;
                for(let i=0;i<length;i++) {
                    let data = datas.get(i);

                    let partyname=data.get("PARTY_NAME");
                    let creat_date=data.get("CREATE_TIME");
                    let wxnick=data.get("WX_NICK");
                    let mobileno=data.get("MOBILE_NO");
                    let houseaddress=data.get("HOUSE_ADDRESS");
                    let housemode=data.get("HOUSE_MODE");
                    let housearea=data.get("HOUSE_AREA");
                    let designername=data.get("DESIGNERNAME");
                    let consultTime=data.get("CONSULT_TIME");

                    if(partyname=='undefined'|| partyname == null) {
                        partyname='';
                    }
                    if(wxnick=='undefined'|| wxnick == null) {
                        wxnick='';
                    }
                    if(mobileno=='undefined'|| mobileno == null) {
                        mobileno='';
                    }
                    if(houseaddress=='undefined'|| houseaddress == null) {
                        houseaddress='';
                    }
                    if(housemode=='undefined'|| housemode == null) {
                        housemode='';
                    }
                    if(housearea=='undefined'|| housearea == null) {
                        housearea='';
                    }
                    if(designername=='undefined'|| designername == null) {
                        designername='';
                    }
                    html.push("<li class='link'><div class=\"group\"><div class=\"content content-auto\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"content content-auto\">");
                    html.push("<span class='e_strong'>客户姓名：</span>")
                    html.push(partyname);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class='e_strong'>微信昵称：</span>"+wxnick);
                    html.push("</div>");


                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class='e_strong'>客户联系电话：</span>"+mobileno);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    if(consultTime==='' || consultTime==null){
                        html.push("<span class='e_strong'>客户咨询时间：</span>");
                    }else{
                        html.push("<span class='e_strong'>客户咨询时间：</span>"+consultTime.substr(0,19));
                    }
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class='e_strong'>楼盘地址：</span>"+houseaddress);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class='e_strong'>户型：</span>"+housemode);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class='e_strong'>面积：</span>"+housearea);
                    html.push("</div>");

                    html.push("<div class='content content-auto'><span class='e_strong'>客户代表：</span>");
                    html.push(data.get("CUSTSERVICENAME"));
                    html.push("</div>");

                    html.push("<div class='content content-auto'><span class='e_strong'>设计师：</span>");
                    html.push(designername);
                    html.push("</div>");

                    html.push("</div>");


                    html.push("</div></div>");
                    html.push("</li>");
                }

                $.insertHtml('beforeend', $("#preworks"), html.join(""));
            },


            submit : function () {
                if ($.validate.verifyAll("reason")) {
                     $.beginPageLoading();

                  let reason=$("#CLEAR_REASON").text();

                    let parameter = '&PROJECT_ID=' + $("#PROJECT_ID").val() + "&PARTY_ID=" + $("#PARTY_ID").val() + "&REASON=" +reason;

                    $.ajaxPost('submitCustClearApply', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("客户清理申请成功", "点击确定返回新增页面，点击取消关闭当前页面", function (btn) {
                            if ("ok" == btn) {
                                document.location.reload();
                            } else {
                                $.redirect.closeCurrentPage();
                            }
                        }, {"cancel": "取消"})
                    });
                }
            }



        }});
})($);