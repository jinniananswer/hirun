(function($){
    $.extend({changegoodseeliveinfo:{

            index : 0,
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["PLAN_LIVE_TIME"] = new Wade.DateField(
                    "PLAN_LIVE_TIME",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["MW_EXPERIENCE_TIME"] = new Wade.DateField(
                    "MW_EXPERIENCE_TIME",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["GAUGE_HOUSE_TIME"] = new Wade.DateField(
                    "GAUGE_HOUSE_TIME",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["OFFER_PLANE_TIME"] = new Wade.DateField(
                    "OFFER_PLANE_TIME",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["CONTACT_TIME"] = new Wade.DateField(
                    "CONTACT_TIME",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
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
                        nullable:"no",
                        disabled:true,
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

                var project_id=$("#PROJECT_ID").val();
                var party_id=$("#PARTY_ID").val();
                var _this=this;

                $.beginPageLoading();

                $.ajaxPost('initChangeGoodSeeLiveInfo','&CUST_ID='+party_id+'&PROJECT_ID='+project_id,function(data) {
                    $.endPageLoading();

                    var partyInfo=data.PARTYINFO;
                    var projectInfo=data.PROJECTINFO;
                    var projectIntentionInfo=data.PROJECTINTENTIONINFO;

                    var rst = new Wade.DataMap(data);
                    var hobbylist = rst.get("HOBBYLIST");
                    var chineseStylelist = rst.get("CHINESESTYLELIST");
                    var europeanclassicslist = rst.get("EUROPEANCLASSICSLIST");
                    var modernsourcelist = rst.get("MODERNSOURCELIST");
                    var funcsystemlist=rst.get("FUNCSYSTEMLIST");
                    var advantagelist=rst.get("ADVANTAGELIST");
                    var criticalprocesslist=rst.get("CRITICALPROCESSLIST");
                    var informationsourcelist=rst.get("INFORMATIONSOURCELIST");



                    $.changegoodseeliveinfo.drawHobbyList(hobbylist);
                    $.changegoodseeliveinfo.drawTopicList(chineseStylelist,"chinesestylelist","UI-CHINESSSTYLE");
                    $.changegoodseeliveinfo.drawTopicList(europeanclassicslist,"europeanclassicslist","UI-EUROPEANCLASSICS");
                    $.changegoodseeliveinfo.drawTopicList(modernsourcelist,"modernsourcelist","UI-MODERNSOURCE");
                    $.changegoodseeliveinfo.drawFuncList(funcsystemlist);
                    $.changegoodseeliveinfo.drawAdvantageList(advantagelist);
                    $.changegoodseeliveinfo.drawCriticalProcessList(criticalprocesslist);
                    $.changegoodseeliveinfo.drawInformationSoruceList(informationsourcelist);
                    $.changegoodseeliveinfo.drawPartyInfo(partyInfo);
                    $.changegoodseeliveinfo.drawProjectInfo(projectInfo);
                    $.changegoodseeliveinfo.drawProjectIntentionInfo(projectIntentionInfo);

                    $("#receiveinfo").css("display", "none");
                    $("#custintention").css("display", "none");
                    $("#NEXT_BUTTON").css("display", "");
                    _this.index=0;

                });
            },

            previous :function(){
                if(this.index==1){
                    $("#baseinfo").css("display", "");
                    $("#custintention").css("display", "none");
                    $("#receiveinfo").css("display", "none");
                    $("#PREVIOUS_BUTTON").css("display", "none");
                    $("#NEXT_BUTTON").css("display", "");
                    $("#CONFIRM_BUTTON").css("display", "none");
                    $("#guidebase").attr("class","on");
                    $("#guideintention").attr("class","");
                    $("#guidereceive").attr("class","");
                    this.index=this.index-1;
                    return;
                }
                if(this.index==2){
                    $("#baseinfo").css("display", "none");
                    $("#custintention").css("display", "");
                    $("#receiveinfo").css("display", "none");
                    $("#PREVIOUS_BUTTON").css("display", "");
                    $("#NEXT_BUTTON").css("display", "");
                    $("#CONFIRM_BUTTON").css("display", "none");
                    $("#guidebase").attr("class","");
                    $("#guideintention").attr("class","on");
                    $("#guidereceive").attr("class","");
                    this.index=this.index-1;
                    return;
                }
            },

            next : function(){
                if(this.index==0&&$.validate.verifyAll("baseinfo")){

                    $("#baseinfo").css("display", "none");
                    $("#custintention").css("display", "");
                    $("#receiveinfo").css("display", "none");
                    $("#PREVIOUS_BUTTON").css("display", "");
                    $("#NEXT_BUTTON").css("display", "");
                    $("#guideintention").attr("class","on");
                    $("#guidebase").attr("class","");
                    $("#guidereceive").attr("class","");

                    this.index+=1;
                    return;
                }
                if(this.index==1){
                    $("#baseinfo").css("display", "none");
                    $("#custintention").css("display", "none");
                    $("#receiveinfo").css("display", "");
                    $("#PREVIOUS_BUTTON").css("display", "");
                    $("#NEXT_BUTTON").css("display", "none");
                    $("#guideintention").attr("class","");
                    $("#guidebase").attr("class","");
                    $("#guidereceive").attr("class","on");
                    $("#CONFIRM_BUTTON").css("display", "");

                    this.index +=1;
                    return;
                }
            },

            confirmElderinfo : function(){
                var oldmancount=$("#OLDMANCOUNT").val();
                var oldwomancount=$("#OLDWOMANCOUNT").val();
                var content='';
                if(oldmancount !='') {
                    content +="男："+oldmancount+"人 ";
                }
                if(oldwomancount !=''){
                    content +="女："+oldwomancount+"人";
                }
                $("#ELDER_TEXT").val(content);
                $("#ELDER_MAN").val(oldmancount);
                $("#ELDER_WOMAN").val(oldwomancount);

                hidePopup('UI-popup','UI-ELDERINFO');
            },

            confirmChildinfo : function(){
                var childboycount=$("#CHILDBOYCOUNT").val();
                var childgirlcount=$("#CHILDGIRLCOUNT").val();
                var content='';
                if(childboycount !='') {
                    content +="男："+childboycount+"人 ";
                }
                if(childgirlcount !=''){
                    content +="女："+childgirlcount+"人";
                }
                $("#CHILD_TEXT").val(content);
                $("#CHILD_BOY").val(childboycount);
                $("#CHILD_GIRL").val(childgirlcount);

                hidePopup('UI-popup','UI-CHILDINFO');
            },

            drawHobbyList : function(datas){
                $("#hobbylist").empty();
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' hobbyname='" + data.get("CODE_NAME") + "' ontap='$.changegoodseeliveinfo.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#hobbylist"), html.join(""));
                hidePopup('UI-popup','UI-HOBBYLIST');

            },

            drawAdvantageList : function(datas){
                $("#advantagelist").empty();
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' advantagename='" + data.get("CODE_NAME") + "' ontap='$.changegoodseeliveinfo.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#advantagelist"), html.join(""));
                hidePopup('UI-popup','UI-ADVANTAGE');

            },

            drawCriticalProcessList : function(datas){
                $("#criticalprocesslist").empty();
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' advantagename='" + data.get("CODE_NAME") + "' ontap='$.changegoodseeliveinfo.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#criticalprocesslist"), html.join(""));
                hidePopup('UI-popup','UI-CRITICALPROCESS');

            },

            drawInformationSoruceList : function(datas){
                $("#informationsourcelist").empty();
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("CODE_VALUE") + "' advantagename='" + data.get("CODE_NAME") + "' ontap='$.changegoodseeliveinfo.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("CODE_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#informationsourcelist"), html.join(""));
                hidePopup('UI-popup','UI-INFORMATIONSOURCE');

            },

            drawTopicList : function(datas,ul_id,ui_id){
                $("#"+ul_id+"").empty();
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("TOPIC_CODE") + "' topicname='" + data.get("TOPIC_NAME") + "' ontap='$.changegoodseeliveinfo.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("TOPIC_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#"+ul_id+""), html.join(""));
                hidePopup('UI-popup',''+ui_id);

            },

            drawFuncList : function(datas){
                $("#funcsystemlist").empty();
                var html = [];
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("FUNC_CODE") + "' funcname='" + data.get("FUNC_NAME") + "' ontap='$.changegoodseeliveinfo.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("FUNC_NAME"));
                    html.push("</div></div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#funcsystemlist"), html.join(""));
                hidePopup('UI-popup','UI-FUNCSYSTEM');

            },

            selectManyElement : function(e){
                var li = $(e);
                var className = li.attr("class");
                if(className == "link checked") {
                    li.attr("class", "link");
                }
                else {
                    li.attr("class", "link checked");
                }

            },

            confirmSelectHobby : function () {
                var lis = $("#hobbylist li");
                var length = lis.length;
                var hobby_id='';
                var hobby_name='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        hobby_id += li.attr("id")+",";
                        hobby_name +=li.attr("hobbyname")+"、"
                    }
                }
                $("#HOBBY_TEXT").val(hobby_name.substring(0, hobby_name.length - 1));
                $("#HOBBY").val(hobby_id.substring(0, hobby_id.length - 1));
                hidePopup('UI-popup','UI-HOBBYLIST');

            },

            confirmAdvantages : function () {
                var lis = $("#advantagelist li");
                var length = lis.length;
                var advantage_id='';
                var advantage_name='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        advantage_id += li.attr("id")+",";
                        advantage_name +=li.attr("advantagename")+"、"
                    }
                }
                $("#ADVANTAGE_TEXT").val(advantage_name.substring(0, advantage_name.length - 1));
                $("#ADVANTAGE").val(advantage_id.substring(0, advantage_id.length - 1));
                hidePopup('UI-popup','UI-ADVANTAGE');

            },

            confirmInformationSource : function () {
                var lis = $("#informationsourcelist li");
                var length = lis.length;
                var advantage_id='';
                var advantage_name='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        advantage_id += li.attr("id")+",";
                        advantage_name +=li.attr("advantagename")+"、"
                    }
                }
                $("#INFORMATIONSOURCE_TEXT").val(advantage_name.substring(0, advantage_name.length - 1));
                $("#INFORMATIONSOURCE").val(advantage_id.substring(0, advantage_id.length - 1));
                hidePopup('UI-popup','UI-INFORMATIONSOURCE');

            },

            confirmCriticalProcess : function () {
                var lis = $("#criticalprocesslist li");
                var length = lis.length;
                var advantage_id='';
                var advantage_name='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        advantage_id += li.attr("id")+",";
                        advantage_name +=li.attr("advantagename")+"、"
                    }
                }
                $("#CRITICALPROCESS_TEXT").val(advantage_name.substring(0, advantage_name.length - 1));
                $("#CRITICALPROCESS").val(advantage_id.substring(0, advantage_id.length - 1));
                hidePopup('UI-popup','UI-CRITICALPROCESS');

            },

            confirmSelectChineseStyle : function () {
                var lis = $("#chinesestylelist li");
                var length = lis.length;
                var topicIds='';
                var topicNames='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        topicIds += li.attr("id")+",";
                        topicNames +=li.attr("topicname")+"、"
                    }
                }
                $("#CHINESESTYLE_TEXT").val(topicNames.substring(0, topicNames.length - 1));
                $("#CHINESESTYLE").val(topicIds.substring(0, topicIds.length - 1));
                hidePopup('UI-popup','UI-CHINESSSTYLE');

            },

            confirmEuropeanClassics : function () {
                var lis = $("#europeanclassicslist li");
                var length = lis.length;
                var topicIds='';
                var topicNames='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        topicIds += li.attr("id")+",";
                        topicNames +=li.attr("topicname")+"、";
                    }
                }
                $("#EUROPEANCLASSICS_TEXT").val(topicNames.substring(0, topicNames.length - 1));
                $("#EUROPEANCLASSICS").val(topicIds.substring(0, topicIds.length - 1));
                hidePopup('UI-popup','UI-EUROPEANCLASSICS');

            },

            confirmModernSource : function () {
                var lis = $("#modernsourcelist li");
                var length = lis.length;
                var topicIds='';
                var topicNames='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        topicIds += li.attr("id")+",";
                        topicNames +=li.attr("topicname")+"、"
                    }
                }
                $("#MODERNSOURCE_TEXT").val(topicNames.substring(0, topicNames.length - 1));
                $("#MODERNSOURCE").val(topicIds.substring(0, topicIds.length - 1));
                hidePopup('UI-popup','UI-MODERNSOURCE');

            },

            confirmFuncs : function () {
                var lis = $("#funcsystemlist li");
                var length = lis.length;
                var funcCode='';
                var funcName='';
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        funcCode += li.attr("id")+",";
                        funcName +=li.attr("funcname")+"、"
                    }
                }
                $("#FUNCS_TEXT").val(funcName.substring(0, funcName.length - 1));
                $("#FUNC").val(funcCode.substring(0, funcCode.length - 1));
                hidePopup('UI-popup','UI-FUNCSYSTEM');

            },


            changeGoodSeeLiveInfo : function(){
                if($.validate.verifyAll("allSubmitArea")) {
                    $.beginPageLoading();
                    var parameter = $.buildJsonData("allSubmitArea");
                    $.ajaxPost('changeGoodSeeLiveInfo', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("修改成功","点击确定返回修改页面，点击取消关闭当前页面", function(btn){
                            if("ok" == btn) {
                                document.location.reload();
                            }
                            else {
                                $.redirect.closeCurrentPage();
                            }
                        },{"cancel":"取消"})
                    });
                }
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
                $("#CHILD_DETAIL").val(partyInfo.CHILD_DETAIL);
                $("#CONTACT").val(partyInfo.MOBILE_NO);
                $("#customer_type").val(partyInfo.CUST_TYPE);
                $("#marketing_type").val(partyInfo.PLOY_TYPE);
                $("#marketing_name").val(partyInfo.PLOY_NAME);
                $("#marketing_time").val(partyInfo.PLOY_TIME);
                $("#other_remark").val(partyInfo.OTHER_REMARK);
                $("#consult_time").val(partyInfo.CONSULT_TIME);
                $("#phone_consult_time").val(partyInfo.TEL_CONSULT_TIME);

            },


            drawProjectIntentionInfo : function (projectIntentionInfo) {
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
                if(projectIntentionInfo.ONLY_WOOD=='1'){
                    $("#onlyWood").attr('checked',true);
                }else{
                    $("#onlyWood").attr('checked',false);
                }

            },

            backToFlow : function() {
                //$.redirect.open('redirectToProjectFlow?PARTY_ID='+parytId+'&PROJECT_ID='+projectId, '客户流程');
                var partyId = $("#PARTY_ID").val();
                var projectId = $("#PROJECT_ID").val();

                window.location.href = "/redirectToProjectFlow?PARTY_ID="+partyId+"&PROJECT_ID="+projectId;
            }

        }});
})($);