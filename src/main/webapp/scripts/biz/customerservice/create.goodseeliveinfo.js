(function ($) {
    $.extend({
        goodseelive: {
            index: 0,
            currentIndex: 0,
            init: function () {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["PLAN_LIVE_TIME"] = new Wade.DateField(
                    "PLAN_LIVE_TIME",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd",
                        useTime: false,
                    }
                );

                window["MW_EXPERIENCE_TIME"] = new Wade.DateField(
                    "MW_EXPERIENCE_TIME",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd",
                        useTime: false,
                    }
                );

                window["GAUGE_HOUSE_TIME"] = new Wade.DateField(
                    "GAUGE_HOUSE_TIME",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd",
                        useTime: false,
                    }
                );

                window["OFFER_PLANE_TIME"] = new Wade.DateField(
                    "OFFER_PLANE_TIME",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd",
                        useTime: false,
                    }
                );

                window["CONTACT_TIME"] = new Wade.DateField(
                    "CONTACT_TIME",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd",
                        useTime: false,
                    }
                );

                var _this = this;

                $.beginPageLoading();
                $.ajaxPost('initCreateGoodSeeLiveInfo', null, function (data) {
                    $.endPageLoading();

                    var rst = new Wade.DataMap(data);
                    var hobbylist = rst.get("HOBBYLIST");
                    var chineseStylelist = rst.get("CHINESESTYLELIST");
                    var europeanclassicslist = rst.get("EUROPEANCLASSICSLIST");
                    var modernsourcelist = rst.get("MODERNSOURCELIST");
                    var funcsystemlist = rst.get("FUNCSYSTEMLIST");
                    var advantagelist = rst.get("ADVANTAGELIST");
                    var criticalprocesslist = rst.get("CRITICALPROCESSLIST");
                    var informationsourcelist = rst.get("INFORMATIONSOURCELIST");

                    $.goodseelive.drawHobbyList(hobbylist);
                    $.goodseelive.drawTopicList(chineseStylelist, "chinesestylelist", "UI-CHINESSSTYLE");
                    $.goodseelive.drawTopicList(europeanclassicslist, "europeanclassicslist", "UI-EUROPEANCLASSICS");
                    $.goodseelive.drawTopicList(modernsourcelist, "modernsourcelist", "UI-MODERNSOURCE");
                    $.goodseelive.drawFuncList(funcsystemlist);
                    $.goodseelive.drawAdvantageList(advantagelist);
                    $.goodseelive.drawCriticalProcessList(criticalprocesslist);
                    $.goodseelive.drawInformationSoruceList(informationsourcelist);

                    $("#receiveinfo").css("display", "none");
                    $("#custintention").css("display", "none");
                    $("#NEXT_BUTTON").css("display", "");
                    _this.index = 0;
                });
            },

            previous: function () {
                if (this.index == 1) {
                    $("#baseinfo").css("display", "");
                    $("#custpention").css("display", "none");
                    $("#receiveinfo").css("display", "none");
                    $("#PREVIOUS_BUTTON").css("display", "none");
                    $("#NEXT_BUTTON").css("display", "");
                    $("#CONFIRM_BUTTON").css("display", "none");
                    $("#guidebase").attr("class", "on");
                    $("#guideintention").attr("class", "");
                    $("#guidereceive").attr("class", "");
                    this.index = this.index - 1;
                    return;
                }
                if (this.index == 2) {
                    $("#baseinfo").css("display", "none");
                    $("#custintention").css("display", "");
                    $("#custintention").css("display", "none");
                    $("#PREVIOUS_BUTTON").css("display", "");
                    $("#NEXT_BUTTON").css("display", "");
                    $("#CONFIRM_BUTTON").css("display", "none");
                    $("#guidebase").attr("class", "");
                    $("#guideintention").attr("class", "on");
                    $("#guidereceive").attr("class", "");
                    this.index = this.index - 1;
                    return;
                }
            },

            next: function () {
                console.info(this.index);
                if (this.index == 0 && $.validate.verifyAll("baseinfo")) {

                    $("#baseinfo").css("display", "none");
                    $("#custintention").css("display", "");
                    $("#receiveinfo").css("display", "none");
                    $("#PREVIOUS_BUTTON").css("display", "");
                    $("#NEXT_BUTTON").css("display", "");
                    $("#guideintention").attr("class", "on");
                    $("#guidebase").attr("class", "");
                    $("#guidereceive").attr("class", "");

                    this.index += 1;
                    return;
                }
                if (this.index == 1) {
                    $("#baseinfo").css("display", "none");
                    $("#custintention").css("display", "none");
                    $("#receiveinfo").css("display", "");
                    $("#PREVIOUS_BUTTON").css("display", "");
                    $("#NEXT_BUTTON").css("display", "none");
                    $("#guideintention").attr("class", "");
                    $("#guidebase").attr("class", "");
                    $("#guidereceive").attr("class", "on");
                    $("#CONFIRM_BUTTON").css("display", "");


                    this.index += 1;
                    return;
                }
            },

            confirmElderinfo: function () {
                var oldmancount = $("#OLDMANCOUNT").val();
                var oldwomancount = $("#OLDWOMANCOUNT").val();
                var content = '';
                if (oldmancount != '') {
                    content += "男：" + oldmancount + "人 ";
                }
                if (oldwomancount != '') {
                    content += "女：" + oldwomancount + "人";
                }
                $("#ELDER_TEXT").val(content);
                $("#ELDER_MAN").val(oldmancount);
                $("#ELDER_WOMAN").val(oldwomancount);

                hidePopup('UI-popup', 'UI-ELDERINFO');
            },

            confirmChildinfo: function () {
                var childboycount = $("#CHILDBOYCOUNT").val();
                var childgirlcount = $("#CHILDGIRLCOUNT").val();
                var content = '';
                if (childboycount != '') {
                    content += "男：" + childboycount + "人 ";
                }
                if (childgirlcount != '') {
                    content += "女：" + childgirlcount + "人";
                }
                $("#CHILD_TEXT").val(content);
                $("#CHILD_BOY").val(childboycount);
                $("#CHILD_GIRL").val(childgirlcount);

                hidePopup('UI-popup', 'UI-CHILDINFO');
            },

            drawHobbyList: function (datas) {
                $("#hobbylist").empty();
                var html = [];
                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' hobbyname='" + data.get("CODE_NAME") + "' ontap='$.goodseelive.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>" + data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#hobbylist"), html.join(""));
                hidePopup('UI-popup', 'UI-HOBBYLIST');

            },

            drawAdvantageList: function (datas) {
                $("#advantagelist").empty();
                var html = [];
                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' advantagename='" + data.get("CODE_NAME") + "' ontap='$.goodseelive.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>" + data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#advantagelist"), html.join(""));
                hidePopup('UI-popup', 'UI-ADVANTAGE');

            },

            drawCriticalProcessList: function (datas) {
                $("#criticalprocesslist").empty();
                var html = [];
                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' advantagename='" + data.get("CODE_NAME") + "' ontap='$.goodseelive.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>" + data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#criticalprocesslist"), html.join(""));
                hidePopup('UI-popup', 'UI-CRITICALPROCESS');

            },

            drawInformationSoruceList: function (datas) {
                $("#informationsourcelist").empty();
                var html = [];
                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' advantagename='" + data.get("CODE_NAME") + "' ontap='$.goodseelive.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>" + data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#informationsourcelist"), html.join(""));
                hidePopup('UI-popup', 'UI-INFORMATIONSOURCE');

            },

            drawTopicList: function (datas, ul_id, ui_id) {
                $("#" + ul_id + "").empty();
                var html = [];
                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("TOPIC_CODE") + "' topicname='" + data.get("TOPIC_NAME") + "' ontap='$.goodseelive.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>" + data.get("TOPIC_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#" + ul_id + ""), html.join(""));
                hidePopup('UI-popup', '' + ui_id);

            },

            drawFuncList: function (datas) {
                $("#funcsystemlist").empty();
                var html = [];
                var length = datas.length;
                for (var i = 0; i < length; i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("FUNC_CODE") + "' funcname='" + data.get("FUNC_NAME") + "' ontap='$.goodseelive.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>" + data.get("FUNC_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#funcsystemlist"), html.join(""));
                hidePopup('UI-popup', 'UI-FUNCSYSTEM');

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

            confirmSelectHobby: function () {
                var lis = $("#hobbylist li");
                var length = lis.length;
                var hobby_id = '';
                var hobby_name = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        hobby_id += li.attr("id") + ",";
                        hobby_name += li.attr("hobbyname") + "、"
                    }
                }
                $("#HOBBY_TEXT").val(hobby_name.substring(0, hobby_name.length - 1));
                $("#HOBBY").val(hobby_id.substring(0, hobby_id.length - 1));
                hidePopup('UI-popup', 'UI-HOBBYLIST');

            },

            confirmAdvantages: function () {
                var lis = $("#advantagelist li");
                var length = lis.length;
                var advantage_id = '';
                var advantage_name = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        advantage_id += li.attr("id") + ",";
                        advantage_name += li.attr("advantagename") + "、"
                    }
                }
                $("#ADVANTAGE_TEXT").val(advantage_name.substring(0, advantage_name.length - 1));
                $("#ADVANTAGE").val(advantage_id.substring(0, advantage_id.length - 1));
                hidePopup('UI-popup', 'UI-ADVANTAGE');

            },

            confirmInformationSource: function () {
                var lis = $("#informationsourcelist li");
                var length = lis.length;
                var advantage_id = '';
                var advantage_name = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        advantage_id += li.attr("id") + ",";
                        advantage_name += li.attr("advantagename") + "、"
                    }
                }
                $("#INFORMATIONSOURCE_TEXT").val(advantage_name.substring(0, advantage_name.length - 1));
                $("#INFORMATIONSOURCE").val(advantage_id.substring(0, advantage_id.length - 1));
                hidePopup('UI-popup', 'UI-INFORMATIONSOURCE');

            },

            confirmCriticalProcess: function () {
                var lis = $("#criticalprocesslist li");
                var length = lis.length;
                var advantage_id = '';
                var advantage_name = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        advantage_id += li.attr("id") + ",";
                        advantage_name += li.attr("advantagename") + "、"
                    }
                }
                $("#CRITICALPROCESS_TEXT").val(advantage_name.substring(0, advantage_name.length - 1));
                $("#CRITICALPROCESS").val(advantage_id.substring(0, advantage_id.length - 1));
                hidePopup('UI-popup', 'UI-CRITICALPROCESS');

            },

            confirmSelectChineseStyle: function () {
                var lis = $("#chinesestylelist li");
                var length = lis.length;
                var topicIds = '';
                var topicNames = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        topicIds += li.attr("id") + ",";
                        topicNames += li.attr("topicname") + "、"
                    }
                }
                $("#CHINESESTYLE_TEXT").val(topicNames.substring(0, topicNames.length - 1));
                $("#CHINESESTYLE").val(topicIds.substring(0, topicIds.length - 1));
                hidePopup('UI-popup', 'UI-CHINESSSTYLE');

            },

            confirmEuropeanClassics: function () {
                var lis = $("#europeanclassicslist li");
                var length = lis.length;
                var topicIds = '';
                var topicNames = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        topicIds += li.attr("id") + ",";
                        topicNames += li.attr("topicname") + "、"
                    }
                }
                $("#EUROPEANCLASSICS_TEXT").val(topicNames.substring(0, topicNames.length - 1));
                $("#EUROPEANCLASSICS").val(topicIds.substring(0, topicIds.length - 1));
                hidePopup('UI-popup', 'UI-EUROPEANCLASSICS');

            },

            confirmModernSource: function () {
                var lis = $("#modernsourcelist li");
                var length = lis.length;
                var topicIds = '';
                var topicNames = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        topicIds += li.attr("id") + ",";
                        topicNames += li.attr("topicname") + "、"
                    }
                }
                $("#MODERNSOURCE_TEXT").val(topicNames.substring(0, topicNames.length - 1));
                $("#MODERNSOURCE").val(topicIds.substring(0, topicIds.length - 1));
                hidePopup('UI-popup', 'UI-MODERNSOURCE');

            },

            confirmFuncs: function () {
                var lis = $("#funcsystemlist li");
                var length = lis.length;
                var funcCode = '';
                var funcName = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        funcCode += li.attr("id") + ",";
                        funcName += li.attr("funcname") + "、"
                    }
                }
                $("#FUNCS_TEXT").val(funcName.substring(0, funcName.length - 1));
                $("#FUNC").val(funcCode.substring(0, funcCode.length - 1));
                hidePopup('UI-popup', 'UI-FUNCSYSTEM');

            },


            createGoodSeeLiveInfo: function () {
                if ($.validate.verifyAll("allSubmitArea")) {
                    $.beginPageLoading();
                    var parameter = $.buildJsonData("allSubmitArea");
                    var mobileNo = $('#CONTACT').val();
                    $.ajaxPost('hasCustPreparation', '&MOBILE_NO=' + mobileNo, function (data) {
                        $.endPageLoading();
                        var rst = new Wade.DataMap(data);
                        var custPreparation = rst.get("CUSTPREPARATION");
                        if (custPreparation.length < 0) {
                            $.goodseelive.realCreate(parameter);
                        }else{
                            MessageBox.success("新增成功", "该客户为报备客户,是否转换成你的客户，该动作不可逆。", function (btn) {
                                if ("ok" == btn) {
                                    $.goodseelive.realCreate(parameter);
                                } else {
                                    $.redirect.closeCurrentPage();
                                }
                            }, {"cancel": "取消"})
                        }
                    });
                }
            },

            realCreate: function (parameter) {
                $.ajaxPost('createGoodSeeLiveInfo', parameter, function (data) {
                    $.endPageLoading();
                    MessageBox.success("新增成功", "点击确定返回新增页面，点击取消关闭当前页面", function (btn) {
                        if ("ok" == btn) {
                            document.location.reload();
                        } else {
                            $.redirect.closeCurrentPage();
                        }
                    }, {"cancel": "取消"})
                });
            }

        }
    });
})($);