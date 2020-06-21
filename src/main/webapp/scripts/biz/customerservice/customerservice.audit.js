(function($){
    $.extend({custserviceaudit:{
            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "auditstat",
                    // 参数设置
                    {
                        id:"AUDITSTATUS",
                        name:"AUDITSTATUS",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"未审批", VALUE:"0"},
                        {TEXT:"审批通过", VALUE:"1"},
                        {TEXT:"审批不通过", VALUE:"2"}

                    ]
                );

                $("#AUDITSTATUS").bind("change", function(){
                    $("#AUDITSTATUS").val(this.value); // 当前值
                });

                $("#AUDITSTATUS").val("0");


                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "busitype",
                    // 参数设置
                    {
                        id:"BUSITYPE",
                        name:"BUSITYPE",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"客户清理申请", VALUE:"1"},

                    ]
                );

                $("#BUSITYPE").bind("change", function(){
                    $("#BUSITYPE").val(this.value); // 当前值
                });

                $("#BUSITYPE").val("1");

                $.beginPageLoading();
                $.ajaxPost('initCustServiceAudit',null,function(data) {
                    $.endPageLoading();
                    var rst = new Wade.DataMap(data);
                    //var datas = rst.get("CUSTSERVICEINFO");
                    var applyinfolist = rst.get("APPLYINFOLIST");

                    //$.custserviceaudit.drawCustServiceInfo4Query(datas);
                    $.custserviceaudit.drawApplyInfo(applyinfolist);
                });
            },

            queryCustService :function(){
                $.beginPageLoading("查询中。。。");

                var custServiceName=$("#CUSTSERVICE_NAME").val();

                var param='&CUSTSERVICE_NAME='+custServiceName;
                $.ajaxPost('queryCustServiceByName',param,function(data) {
                    var rst = new Wade.DataMap(data);
                    var datas=rst.get("CUSTSERVICEINFO");
                    $.custserviceaudit.drawCustServiceInfo4Query(datas);

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
                    html.push("<li class='link' custserviceEmpId='" + data.get("EMPLOYEE_ID") + "' custserviceEmpName='" + data.get("NAME") + "' ontap='$.custserviceaudit.selectCustService(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\"> ");
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



            query :function(){
                if($.validate.verifyAll("queryArea")) {
                    $.beginPageLoading();
                    var param=$.buildJsonData("queryArea");
                    $.ajaxPost('queryApplyInfo4Audit',param, function (data) {
                        $.beginPageLoading();
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("APPLYINFOLIST");
                        $.custserviceaudit.drawApplyInfo(datas);
                        hidePopup('UI-popup','UI-popup-query-cond');
                    });
                }
            },

            drawApplyInfo : function(datas){
                $.endPageLoading();
                $("#applyinfos").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var applyDate=data.get("APPLY_DATE");
                    var auditdate=data.get("AUDIT_DATE");
                    var applyreason=data.get("APPLY_REASON");
                    var status=data.get("STATUS");
                    var auditname=data.get("AUDIT_EMPLOYEE_NAME");
                    var applyName=data.get("APPLY_EMPLOYEE_NAME");
                    var partyName=data.get("PARTY_NAME");
                    var createDate=data.get("CREATE_TIME");
                    var wxNick=data.get("WX_NICK");


                    if(auditdate=='undefined'|| auditdate == null) {
                        auditdate='';
                    }

                    if(auditname=='undefined'|| auditname == null) {
                        auditname='';
                    }
                    if(partyName=='undefined'|| partyName == null) {
                        partyName='';
                    }

                    if(wxNick=='undefined'|| wxNick == null) {
                        wxNick='';
                    }

                    /*ontap=\"$.custservicemanager.redirectFlow(\'"+data.get("PARTY_ID")+"\',\'"+data.get("PROJECT_ID")+"\')\"*/
                    html.push("<li class='link'><div class=\"group\"><div class=\"content content-auto\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"content content-auto\">");
                    html.push("<span class=''>客户姓名：</span>")
                    html.push(partyName);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>微信名：</span>"+wxNick);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>创建时间：</span>"+createDate);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>客户代表：</span>"+applyName);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>申请时间：</span>"+applyDate.substr(0,19));
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    if(status=='0'){
                        html.push("<span class='e_red e_strong'>状态：未审批</span>");

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

                    if(status=='0'){
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-close e_ico-pic-red e_ico-pic-r\" ontap='$.custserviceaudit.auditConfirm(\""+data.get("PARTY_ID")+"\",\""+data.get("ID")+"\",\""+data.get("APPLY_EMPLOYEE_ID")+"\",\"2\");'></span>");
                    html.push("</div>");

                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-ok e_ico-pic-green e_ico-pic-r \" ontap='$.custserviceaudit.auditConfirm(\""+data.get("PARTY_ID")+"\",\""+data.get("ID")+"\",\""+data.get("APPLY_EMPLOYEE_ID")+"\",\"1\");'></span>");
                    html.push("</div>");
                    }

                }

                $.insertHtml('beforeend', $("#applyinfos"), html.join(""));
            },

            auditConfirm : function(partyId,id,applyEmployeeId,auditStatus) {
                var message='';
                if(auditStatus==1){
                     message = "确认审批通过吗？";
                }else{
                     message = "确认审批不通过吗？";
                }
                MessageBox.success("提示信息",message, function(btn){
                    if("ok" == btn) {
                        $.beginPageLoading('提交中');
                        $.ajaxReq({
                            url : 'auditConfirm',
                            data : {
                                PARTY_ID : partyId,
                                AUDIT_STATUS : auditStatus,
                                ID : id,
                                APPLY_EMPLOYEE_ID:applyEmployeeId
                            },
                            type : 'POST',
                            successFunc : function(data) {
                                $.endPageLoading();
                                MessageBox.success("审批成功", "点击确定返回页面，点击取消关闭当前页面", function (btn) {
                                    if ("ok" == btn) {
                                        document.location.reload();
                                    } else {
                                        $.redirect.closeCurrentPage();
                                    }
                                }, {"cancel": "取消"})
                            },
                            errorFunc : function (resultCode, reusltInfo) {
                                $.endPageLoading();
                            }
                        })
                    }
                    else {

                    }
                },{"cancel":"取消"})

            },

            clearCond : function () {
                $('#CUSTSERVICEEMPLOYEENAME').val('');
                $('#CUSTSERVICEEMPLOYEEID').val('');
            },
        }});
})($);