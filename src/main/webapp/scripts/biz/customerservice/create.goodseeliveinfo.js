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



                $.Select.append(
                    "houseKindContainer",
                    {
                        id:"HOUSEKIND",
                        name:"HOUSEKIND",
                        addDefault:true
                    },
                    [
                        {TEXT:"一房", VALUE:"1"},
                        {TEXT:"两房", VALUE:"2"},
                        {TEXT:"三房", VALUE:"3"},
                        {TEXT:"四房", VALUE:"4"},
                        {TEXT:"五房", VALUE:"5"},
                        {TEXT:"六房", VALUE:"6"},
                        {TEXT:"复式", VALUE:"7"},
                        {TEXT:"别墅", VALUE:"8"},
                        {TEXT:"平层与错层", VALUE:"9"}
                    ]
                );

                $.Select.append(
                    "sampleHouseContainer",
                    {
                        id:"sample_house",
                        name:"sample_house",
                        addDefault:true
                    },
                    [
                        {TEXT:"阶段性样品房", VALUE:"1"},
                        {TEXT:"木制品样板房", VALUE:"2"},
                    ]
                );

                $.Select.append(
                    "customerTypeContainer",
                    {
                        id:"customer_type",
                        name:"customer_type",
                        addDefault:true,
                        nullable:"no"
                    },
                    [
                        {TEXT:"上门咨询", VALUE:"1"},
                        {TEXT:"活动客户", VALUE:"2"},
                        {TEXT:"电话咨询", VALUE:"3"},
                        {TEXT:"报备客户", VALUE:"4"},
                        {TEXT:"其他", VALUE:"5"},
                    ]
                );

                $.Select.append(
                    "marketingTypeContainer",
                    {
                        id:"marketing_type",
                        name:"marketing_type",
                        addDefault:true,
                        disabled:true
                    },
                    [
                        {TEXT:"房交会", VALUE:"1"},
                        {TEXT:"家博会", VALUE:"2"},
                        {TEXT:"业主见面会", VALUE:"3"},
                        {TEXT:"其他", VALUE:"4"},
                    ]
                );

                $("#customer_type").bind("change", function(){
                    if(this.value=='2'){
                        $('#marketing_type').attr("disabled",false);
                        $('#marketing_name').attr("disabled",false);
                        $('#marketing_time').attr("disabled",false);
                        $('#phone_consult_time').attr("disabled",true);
                        $('#consult_time').attr("disabled",true);
                        $('#phone_consult_time').val('');
                        $('#consult_time').val('');
                    }else if(this.value=='3'){
                        $('#phone_consult_time').attr("disabled",false);
                        $('#marketing_type').attr("disabled",true);
                        $('#marketing_name').attr("disabled",true);
                        $('#marketing_time').attr("disabled",true);
                        $('#consult_time').attr("disabled",true);
                        $('#marketing_type').val('');
                        $('#marketing_name').val('');
                        $('#marketing_time').val('');
                        $('#consult_time').val('');
                    }else if(this.value=='1'){
                        $('#consult_time').attr("disabled",false);
                        $('#phone_consult_time').attr("disabled",true);
                        $('#marketing_name').attr("disabled",true);
                        $('#marketing_time').attr("disabled",true);
                        $('#marketing_type').val('');
                        $('#marketing_name').val('');
                        $('#marketing_time').val('');
                        $('#marketing_type').attr("disabled",true);
                        $('#phone_consult_time').val('');
                    }else{
                        $('#marketing_type').attr("disabled",true);
                        $('#marketing_name').attr("disabled",true);
                        $('#marketing_time').attr("disabled",true);
                        $('#marketing_type').val('');
                        $('#marketing_name').val('');
                        $('#marketing_time').val('');
                        $('#phone_consult_time').attr("disabled",true);
                        $('#phone_consult_time').val('');
                        $('#consult_time').attr("disabled",true);
                        $('#consult_time').val('');
                    }
                });

                window["marketing_time"] = new Wade.DateField(
                    "marketing_time",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd",
                        useTime: false,
                    }
                );

                window["phone_consult_time"] = new Wade.DateField(
                    "phone_consult_time",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd",
                        useTime: false,
                    }
                );

                window["consult_time"] = new Wade.DateField(
                    "consult_time",
                    {
                        dropDown: true,
                        format: "yyyy-MM-dd HH:mm:ss",
                        useTime: false,
                    }
                );


                $.Select.append(
                    "informationSourceContainer",
                    {
                        id:"information_source",
                        name:"information_source",
                        addDefault:true,
                        disabled:true
                    },
                    [
                        {TEXT:"报纸", VALUE:"1"},
                        {TEXT:"电视媒体", VALUE:"2"},
                        {TEXT:"户外广告牌", VALUE:"3"},
                        {TEXT:"家装顾问-电话", VALUE:"4"},
                        {TEXT:"家装顾问-守点", VALUE:"5"},
                        {TEXT:"其他", VALUE:"6"},
                        {TEXT:"熟人介绍", VALUE:"7"},
                        {TEXT:"网络开发平台", VALUE:"8"},
                        {TEXT:"小区宣传", VALUE:"9"},
                        {TEXT:"展会", VALUE:"10"},
                    ]
                );

                $.Select.append(
                    "bankContainer",
                    {
                        id:"bank_id",
                        name:"bank_id",
                        addDefault:true,
                    },
                    [
                        {TEXT:"建设银行", VALUE:"1"},
                        {TEXT:"其他银行", VALUE:"2"},
                    ]
                );

                $.Select.append(
                    "monthNumContainer",
                    {
                        id:"month_num",
                        name:"month_num",
                        addDefault:true,
                    },
                    [
                        {TEXT:"6期", VALUE:"1"},
                        {TEXT:"12期", VALUE:"2"},
                        {TEXT:"18期", VALUE:"3"},
                        {TEXT:"24期", VALUE:"4"},
                        {TEXT:"36期", VALUE:"5"},
                        {TEXT:"48期", VALUE:"6"},
                        {TEXT:"60期", VALUE:"7"},
                    ]
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
                    var custNo = rst.get("CUSTNO");

                    $("#customerNo").val(custNo);
                    $.goodseelive.drawHobbyList(hobbylist);
                    $.goodseelive.drawTopicList(chineseStylelist, "chinesestylelist", "UI-CHINESSSTYLE");
                    $.goodseelive.drawTopicList(europeanclassicslist, "europeanclassicslist", "UI-EUROPEANCLASSICS");
                    $.goodseelive.drawTopicList(modernsourcelist, "modernsourcelist", "UI-MODERNSOURCE");
                    $.goodseelive.drawFuncList(funcsystemlist);
                    $.goodseelive.drawAdvantageList(advantagelist);
                    $.goodseelive.drawCriticalProcessList(criticalprocesslist);
                    $.goodseelive.drawInformationSoruceList(informationsourcelist);


                    //2020/03/02新增
                    var continueSave=rst.get("CONTINUE");
                    console.log(continueSave);
                    if(continueSave=='true'){
                        $('#saveContinue').val('true');
                        $('#continueSaveButton').attr("disable",true);
                    }else{
                        $('#saveContinue').val('false');
                        $('#continueSaveButton').attr("disable",true);
                    }


                    $("#receiveinfo").css("display", "none");
                    $("#custintention").css("display", "none");
                    $("#NEXT_BUTTON").css("display", "");
                    _this.index = 0;
                });
            },

            previous: function () {
                if (this.index == 1) {
                    $("#baseinfo").css("display", "");
                    $("#custintention").css("display", "none");
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
                    $("#receiveinfo").css("display", "none");
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

            searchHouse :function(){
                var param=$.buildJsonData("houseArea");
                $.beginPageLoading();
                $.ajaxPost('/houses/queryHousesByName',param, function (data) {
                    $.endPageLoading();
                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("HOUSES_LIST");
                    $.goodseelive.drawHouse(datas);
                    showPopup('UI-popup','UI-HOUSELIST');

                });
            },

            drawHouse : function(datas){
                $.endPageLoading();

                $("#houses").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#houseMessageBox").css("display","");
                    $("#confirmButton").css("display","none");
                    return;
                }

                $("#houseMessageBox").css("display","none");
                $("#confirmButton").css("display","");

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("HOUSES_ID") + "' name='" + data.get("NAME")+ "' ontap='$.goodseelive.selectHouse(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div></div><div class='side e_size-s'>");
                    html.push("</div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#houses"), html.join(""));
            },

            selectHouse : function (e) {
                var obj = $(e);

                if(!obj.hasClass("checked")) {
                    obj.addClass("checked");
                }else{
                    obj.attr("class", "link");
                }
                //添加选择样式
                obj.addClass("checked");
                //清空样式
                obj.siblings().removeClass("checked");
                this.chooseHouse();
            },

            chooseHouse: function () {
                var lis = $("#houses li");
                var length = lis.length;
                var houseId = '';
                var houseName = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        $("#house_id").val(li.attr("id"));
                        $("#houseName").val(li.attr("name"));
                    }
                }
                hidePopup('UI-popup', 'UI-HOUSELIST');

            },

            createGoodSeeLiveInfo: function () {
                if ($.validate.verifyAll("allSubmitArea")) {
                    var isMoreCustomer=$("#isMoreCustomer").val();
                    var saveContinue=$("#saveContinue").val();
                    //判断客户代表是否有权限提交
                    if(saveContinue=='false'&&isMoreCustomer=='true'){
                        MessageBox.alert("该客户存在多客户,您没有新增的权限。请输入号码选择客户转成您的客户或者联系文员进行新增。");
                        return;
                    }

                    $.beginPageLoading();
                    var parameter = $.buildJsonData("allSubmitArea");
                    $.goodseelive.realCreate(parameter);
                }
            },

            checkCustomerByMobile:function(){
                $.beginPageLoading();
                var mobileNo=$('#CONTACT').val();
                $.ajaxPost('/customer/checkCustomerByMobile','&mobileNo='+mobileNo, function (data) {
                    $.endPageLoading();
                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("CUSTOMERINFO");

                    if(datas == null || datas.length <= 0){
                        return;
                    }
                    $.goodseelive.drawCustomer(datas);
                    showPopup('UI-popup','UI-CUSTOMERLIST');
                });
            },

            drawCustomer : function(datas){
                $.endPageLoading();

                $("#moreCustomer").empty();
                var html = [];
                if(datas == null || datas.length <= 0){
                    $("#confirmCustButton").css("display","none");
                    $("#isMoreCustomer").val("false");
                    return;
                }else{
                    $("#isMoreCustomer").val("true");
                }

                $("#confirmCustButton").css("display","");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var isSelect=data.get("isSelect");

                    if(isSelect){
                        html.push("<li class='link' cust_id='" + data.get("CUST_ID") + "'  ontap='$.goodseelive.selectCustomer(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    }else{
                        html.push("<li class='link' style='pointer-events: none;' + cust_id='" + data.get("CUST_ID") + "' ontap='$.goodseelive.selectCustomer(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    }
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">客户姓名：");
                    html.push(data.get("CUST_NAME"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>楼盘地址：");
                    html.push(data.get("HOUSE_NAME"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>电话：");
                    html.push(data.get("MOBILE_NO"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>客户状态：");
                    html.push(data.get("ORDER_STATUS_NAME"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>客户类型：");
                    html.push(data.get("CUST_TYPE_NAME"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>申报人：");
                    html.push(data.get("PREPARE_NAME"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>申报时间：");
                    html.push(data.get("PREPARE_TIME"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>申报状态：");
                    html.push(data.get("PREPARE_STATUS_NAME"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>客户代表：");
                    html.push(data.get("CUST_SERVICE_NAME"));
                    html.push("</div>");

                    html.push("</div></div></div>");
                    html.push("</li>");
                }
                $.insertHtml('beforeend', $("#moreCustomer"), html.join(""));
            },

            selectCustomer : function (e) {
                var obj = $(e);

                if(!obj.hasClass("checked")) {
                    obj.addClass("checked");
                }else{
                    obj.attr("class", "link");
                }
                //添加选择样式
                obj.addClass("checked");
                //清空样式
                obj.siblings().removeClass("checked");
            },

            transCustomer: function () {
                var lis = $("#moreCustomer li");
                var length = lis.length;
                var custId = '';
                for (var i = 0; i < length; i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if (className == "link checked") {
                        custId=li.attr("cust_id");
                    }

                    if(custId =='') {
                        MessageBox.alert("您没有选中任何客户，请先选择");
                        return;
                    }

                    MessageBox.success("提示信息", "是否转换成你的客户，该动作不可逆。", function (btn) {
                        if ("ok" == btn) {
                            $.goodseelive.loadCustomer(custId);
                        }
                    }, {"cancel": "取消"})
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
            },

            loadCustomer:function (custId) {
                $.ajaxPost('initChangeGoodSeeLiveInfo','&CUST_ID='+custId, function (data) {
                    $.endPageLoading();
                    var partyInfo=data.PARTYINFO;
                    var projectInfo=data.PROJECTINFO;
                    var intentionInfo=data.PROJECTINTENTIONINFO;

                    $.goodseelive.drawPartyInfo(partyInfo)
                    $.goodseelive.drawProjectInfo(projectInfo)
                    $.goodseelive.drawIntentionInfo(intentionInfo)
                    hidePopup('UI-popup', 'UI-CUSTOMERLIST');
                });
            },

            drawPartyInfo : function (partyInfo) {
                if(partyInfo==''){
                    return;
                }
                $("#NAME").val(partyInfo.CUST_NAME);
                $("#customerNo").val(partyInfo.CUST_NO);
                $("#cust_id").val(partyInfo.CUST_ID);
                $("#prepare_id").val(partyInfo.PREPARE_ID);
                $("#AGE").val(partyInfo.AGE);
                $("#EDUCATE").val(partyInfo.EDUCATE);
                $("#PEOPLE_COUNT").val(partyInfo.PEOPLE_COUNT);
                $("#COMPANY").val(partyInfo.COMPANY);
                $("#OTHER_HOBBY").val(partyInfo.OTHER_HOBBY);
                $("#HOBBY").val(partyInfo.HOBBY);
                $("#HOBBY_TEXT").val(partyInfo.HOBBY_TEXT);
                $("#OLDER_DETAIL").val(partyInfo.OLDER_DETAIL);
                $("#CONTACT").val(partyInfo.MOBILE_NO);
                $("#customer_type").val(partyInfo.CUST_TYPE);
                $("#marketing_type").val(partyInfo.PLOY_TYPE);
                $("#marketing_name").val(partyInfo.PLOY_NAME);
                $("#marketing_time").val(partyInfo.PLOY_TIME);
                $("#other_remark").val(partyInfo.OTHER_REMARK);
                $("#consult_time").val(partyInfo.CONSULT_TIME);
                $("#phone_consult_time").val(partyInfo.TEL_CONSULT_TIME);
            },

            drawProjectInfo : function (projectInfo) {
                if(projectInfo==''){
                    return;
                }
                $("#HOUSEKIND").val(projectInfo.HOUSEKIND);
                $("#AREA").val(projectInfo.AREA);
                $("#house_id").val(projectInfo.HOUSE_ID);
                $("#houseName").val(projectInfo.HOUSE_NAME);
                $("#ADVANTAGE").val(projectInfo.ADVANTAGE);
                $("#ADVANTAGE_TEXT").val(projectInfo.ADVANTAG_TEXT);
                $("#CRITICALPROCESS").val(projectInfo.CRITICALPROCESS);
                $("#CRITICALPROCESS_TEXT").val(projectInfo.CRITICALPROCESS_TEXT);
                $("#OTHER_INFO").val(projectInfo.OTHER_INFO);
                $("#MW_EXPERIENCE_TIME").val(projectInfo.MW_EXPERIENCE_TIME);
                $("#ISSCANVIDEO").val(projectInfo.ISSCANVIDEO);
                $("#ISSCANSHOWROOM").val(projectInfo.ISSCANSHOWROOM);
                $("#INFORMATIONSOURCE").val(projectInfo.INFORMATIONSOURCE);
                $("#INFORMATIONSOURCE_TEXT").val(projectInfo.INFORMATIONSOURCE_TEXT);
                $("#GAUGE_HOUSE_TIME").val(projectInfo.GAUGE_HOUSE_TIME);
                $("#OFFER_PLANE_TIME").val(projectInfo.OFFER_PLANE_TIME);
                $("#CONTACT_TIME").val(projectInfo.CONTACT_TIME);
                $("#COUNSELOR_NAME").val(projectInfo.COUNSELOR_NAME);
                $("#OTHER_SOURCE").val(projectInfo.OTHER_SOURCE);
                $("#house_building").val(projectInfo.HOUSE_BUILDING);
                $("#house_room_no").val(projectInfo.HOUSE_ROOM_NO);
                $("#project_id").val(projectInfo.PROJECT_ID);
            },

            drawIntentionInfo : function (projectIntentionInfo) {
                if(projectIntentionInfo==''){
                    return;
                }
                $("#CHINESESTYLE").val(projectIntentionInfo.CHINESESTYLE);
                $("#CHINESESTYLE_TEXT").val(projectIntentionInfo.CHINESESTYLE_TEXT);
                $("#EUROPEANCLASSICS").val(projectIntentionInfo.EUROPEANCLASSICS);
                $("#EUROPEANCLASSICS_TEXT").val(projectIntentionInfo.EUROPEANCLASSICS_TEXT);
                $("#MODERNSOURCE").val(projectIntentionInfo.MODERNSOURCE);
                $("#MODERNSOURCE_TEXT").val(projectIntentionInfo.MODERNSOURCE_TEXT);
                $("#OTHER_TOPIC_REQ").val(projectIntentionInfo.OTHER_TOPIC_REQ);
                $("#FUNC").val(projectIntentionInfo.FUNC);
                $("#FUNCS_TEXT").val(projectIntentionInfo.FUNCS_TEXT);
                $("#BULEPRINT").val(projectIntentionInfo.BULEPRINT);
                $("#FUNC_SPEC_REQ").val(projectIntentionInfo.FUNC_SPEC_REQ);
                $("#TOTALPRICEPLAN").val(projectIntentionInfo.TOTALPRICEPLAN);
                $("#BASICANDWOODPRICEPLAN").val(projectIntentionInfo.BASICANDWOODPRICEPLAN);
                $("#HVACPRICEPLAN").val(projectIntentionInfo.HVACPRICEPLAN);
                $("#MATERIALPRICEPLAN").val(projectIntentionInfo.MATERIALPRICEPLAN);
                $("#FURNITUREPRICEPLAN").val(projectIntentionInfo.FURNITUREPRICEPLAN);
                $("#ELECTRICALPRICEPLAN").val(projectIntentionInfo.ELECTRICALPRICEPLAN);
                $("#PLAN_LIVE_TIME").val(projectIntentionInfo.PLAN_LIVE_TIME);
                $("#sample_house").val(projectIntentionInfo.SAMPLE_HOUSE);
                $("#bank_id").val(projectIntentionInfo.COOPERATIVE_BANK);
                $("#month_num").val(projectIntentionInfo.MONTH_NUM);
                $("#DESIGNER_WORKS").val(projectIntentionInfo.DESIGNER_OPUS);
                $("#WOOD_WISH").val(projectIntentionInfo.WOOD_INTENTION);
                $("#quota").val(projectIntentionInfo.QUOTA);
            },


        }
    });
})($);