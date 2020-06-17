(function ($) {
    $.extend({
        familyManager: {
            init: function () {

                window["UI-popup"] = new Wade.Popup("UI-popup", {
                    visible: false,
                    mask: true
                });

                window["mySwitch"] = new Wade.Switch("mySwitch", {
                    switchOn: false,
                    onValue: "on",
                    offValue: "off"
                });

                $("#mySwitch").val("off");

                $("#mySwitch").change(function () {
                    $("#mySwitch").val(this.value); // this.value 获取开关组件当前值
                });

                $.beginPageLoading();
                $.ajaxPost('initchangecustservice', null, function (data) {
                    $.endPageLoading();

                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("CUSTSERVICEINFO");
                    //$.changecustomerservice.drawCustServiceInfo(datas);
                    //$.changecustomerservice.drawCustServiceInfo4Query(datas);

                    hidePopup('UI-popup', 'UI-popup-query-cond');
                    hidePopup('UI-popup', 'UI-CHOOSECUSTSERVICE');
                    hidePopup('UI-popup', 'UI-QUERYCUSTSERVICE');
                    $("#submitButton").css("display", "none");


                });

            },

            queryCustService: function () {
                $.beginPageLoading("查询中。。。");

                var custServiceName = $("#CUSTSERVICE_NAME").val();

                var param = '&CUSTSERVICE_NAME=' + custServiceName;
                $.ajaxPost('queryCustServiceByName', param, function (data) {
                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("CUSTSERVICEINFO");
                    $.familyManager.drawCustServiceInfo4Query(datas);

                });
            },

            query: function () {
                if ($.validate.verifyAll("queryArea")) {
                    $.beginPageLoading();
                    var name = $("#NAME").val();
                    var mobile = $("#MOBILE").val();
                    var employeeid = $("#CUSTSERVICEEMPLOYEEID").val();

                    $.ajaxPost('queryParty4CreateFamily', '&NAME=' + name + '&MOBILE=' + mobile + '&CUSTSERVICEEMPLOYEEID=' + employeeid, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("PARTYINFOLIST");
                        $.familyManager.drawPartyInfo(datas);
                        hidePopup('UI-popup', 'UI-CHOOSECUSTSERVICE');
                    });
                }
            },

            searchNewCustService: function () {
                if ($.validate.verifyAll("searchNewCustService")) {
                    $.beginPageLoading();
                    var param = $.buildJsonData("searchNewCustService");
                    $.ajaxPost('searchNewCustService', param, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("CUSTSERVICEINFO");
                        $.familyManager.drawCustServiceInfo(datas);
                    });
                }
            },

            drawPartyInfo: function (datas) {
                $.endPageLoading();
                $("#partyinfos").empty();
                var html = [];

                if (datas == null || datas.length <= 0) {
                    $("#queryPartyInfoMessage").css("display", "");

                    return;
                }

                $("#queryPartyInfoMessage").css("display", "none");
                $("#submitButton").css("display", "");


                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    var wxnick = data.get("WX_NICK");
                    var mobile = data.get("MOBILE_NO");
                    var create_date = data.get("CREATE_TIME");
                    var party_name = data.get("PARTY_NAME");
                    var custservice_name = data.get("CUSTSERVICENAME");
                    html.push("<li class='link' party_id='" + data.get("PARTY_ID") + "' ontap='$.familyManager.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\">");
                    if (party_name != 'undefined' && party_name != null) {
                        html.push("<div class=\"content\">");
                        html.push("客户姓名: " + party_name);
                        html.push("</div>")
                    } else {
                        html.push("<div class=\"content \">");
                        html.push("客户姓名: ");
                        html.push("</div>")
                    }


                    if (wxnick != 'undefined' && wxnick != null) {
                        html.push("<div class=\"content\">");
                        html.push("微信昵称: " + wxnick);
                        html.push("</div>")
                    } else {
                        html.push("<div class=\"content \">");
                        html.push("微信昵称: ");
                        html.push("</div>")
                    }

                    html.push("<div class=\"content \">");
                    html.push("咨询时间: " + create_date.substr(0, 19));
                    html.push("</div>")

                    html.push("<div class=\"content \">");
                    html.push("客户代表: " + custservice_name);
                    html.push("</div>")

                    html.push("</div>")
                    if (mobile != "undefined" && mobile != null) {
                        html.push("<div class=\"side e_size-m\">" + mobile + "</div>");
                    }
                    html.push("</div></div></li>");
                }


                $.insertHtml('beforeend', $("#partyinfos"), html.join(""));
            },

            selectManyElement: function (e) {
                var li = $(e);
                var className = li.attr("class");
                if (className == "link checked") {
                    li.attr("class", "link");
                } else {
                    li.attr("class", "link checked");
                }

            },

            selectOneElement: function (e) {
                var obj = $(e);

                if (!obj.hasClass("checked")) {
                    obj.addClass("checked");
                } else {
                    obj.attr("class", "link");
                }
                //添加选择样式
                obj.addClass("checked");
                //清空样式
                obj.siblings().removeClass("checked");

            },

            createFamily: function () {
                let lis = $("#partyinfos li");
                let length = lis.length;
                let partyIds = '';

                for (let i = 0; i < length; i++) {
                    let li = $(lis[i]);
                    let className = li.attr("class");
                    if (className == "link checked") {
                        partyIds += li.attr("party_id") + ",";
                    }
                }


                if (partyIds == '') {
                    MessageBox.alert("您没有选中任何客户，请先选择客户");
                    return;
                }

                partyIds = partyIds.substr(0, partyIds.length - 1);
                let partyIdArr = partyIds.split(",");

                if (partyIdArr.length < 2) {
                    MessageBox.alert("请至少选择两个以上客户进行组网");
                    return;
                }

                MessageBox.success("提示信息", "是否确认组网，该动作不可逆。如需拆网需联系开发人员", function (btn) {
                    if ("ok" == btn) {
                        $.beginPageLoading();
                        $.familyManager.realCreateFamily(partyIds);
                    }
                }, {"cancel": "取消"})

            },

            realCreateFamily: function (partyIds) {
                let param = "&PARTY_IDS=" + partyIds;
                $.ajaxPost('createFamily', param, function (data) {
                    $.endPageLoading();
                    MessageBox.success("组网成功", "点击确定返回新增页面，点击取消关闭当前页面", function (btn) {
                        if ("ok" == btn) {
                            document.location.reload();
                        } else {
                            $.redirect.closeCurrentPage();
                        }
                    }, {"cancel": "取消"})
                })
            },

            drawCustServiceInfo: function (datas) {
                $.endPageLoading();
                $("#custservicesinfo").empty();
                var html = [];

                if (datas == null || datas.length <= 0) {
                    $("#messagebox").css("display", "");
                    $("#submitButton").css("display", "none");
                    return;
                }

                $("#messagebox").css("display", "none");
                $("#submitButton").css("display", "");


                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' employee_id='" + data.get("EMPLOYEE_ID") + "' ontap='$.familyManager.selectOneElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push(data.get("PARENT_ORG_NAME") + "-" + data.get("ORG_NAME"));
                    html.push("</div>");
                    html.push("</div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#custservicesinfo"), html.join(""));
            },


            drawCustServiceInfo4Query: function (datas) {
                $.endPageLoading();
                $("#querycustservicesinfo").empty();
                var html = [];

                if (datas == null || datas.length <= 0) {
                    $("#custservicemessagebox").css("display", "");
                    $("#submitButton").css("display", "none");
                    return;
                }

                $("#custservicemessagebox").css("display", "none");
                $("#submitButton").css("display", "");


                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' custserviceEmpId='" + data.get("EMPLOYEE_ID") + "' custserviceEmpName='" + data.get("NAME") + "' ontap='$.familyManager.selectCustService(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>")
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push(data.get("PARENT_ORG_NAME") + "-" + data.get("ORG_NAME"));
                    html.push("</div>");
                    html.push("</div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#querycustservicesinfo"), html.join(""));
            },

            confirmCustService4Query: function () {
                var custservlist = $("#querycustservicesinfo li");
                var custservlength = custservlist.length;
                var custserviceEmpId = '';
                var custserviceName = '';
                for (var i = 0; i < custservlength; i++) {
                    var li = $(custservlist[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        custserviceEmpId = li.attr("custserviceEmpId");
                        custserviceName = li.attr("custserviceEmpName");
                    }
                }
                $("#CUSTSERVICEEMPLOYEENAME").val(custserviceName);
                $("#CUSTSERVICEEMPLOYEEID").val(custserviceEmpId);

                backPopup(document.getElementById("UI-popup-query-cond"));

            },
            selectCustService: function (e) {
                var obj = $(e);
                var custserviceEmpId = obj.attr("custserviceEmpId");
                var custserviceName = obj.attr("custserviceEmpName");

                $("#CUSTSERVICEEMPLOYEENAME").val(custserviceName);
                $("#CUSTSERVICEEMPLOYEEID").val(custserviceEmpId);

                backPopup(document.getElementById("UI-popup-query-cond"));

            },

            confirmCustService: function () {

                var partylis = $("#partyinfos li");
                var partylength = partylis.length;
                var partyids = '';
                for (var i = 0; i < partylength; i++) {
                    var li = $(partylis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        partyids += li.attr("party_id") + ",";
                    }
                }

                var lis = $("#custservicesinfo li");
                var length = lis.length;
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        var custServiceEmpId = li.attr("employee_id");
                    }
                }
                if (custServiceEmpId == null) {
                    MessageBox.alert("您没有选中任何客户代表，请先选择客户代表");
                    return;
                }

                var parameter = '&SELECTED_CUSTSERVICE_ID=' + custServiceEmpId + "&PARTY_IDS=" + partyids.substr(0, partyids.length - 1);

                $.beginPageLoading();
                $.ajaxPost('confirmChangeCustService', parameter, function (data) {
                    $.endPageLoading();
                    MessageBox.success("客户代表变更成功", "点击确定返回新增页面，点击取消关闭当前页面", function (btn) {
                        if ("ok" == btn) {
                            document.location.reload();
                        } else {
                            $.redirect.closeCurrentPage();
                        }
                    }, {"cancel": "取消"})
                })
            },

            clearCond: function () {
                $('#NAME').val('');
                $('#MOBILE').val('');
                $('#CUSTSERVICEEMPLOYEENAME').val('');
                $('#CUSTSERVICEEMPLOYEEID').val('');
            },

        }
    });
})($);