(function($){
    $.extend({custServiceActionQuery:{
            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });

                window["myTable"] = new Wade.Table("myTable", {
                    fixedMode:true,
                    fixedLeftCols:1,
                    editMode:false
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

                $.Select.append(
                    "busiTypeTimeContainer",
                    {
                        id:"busiTimeType",
                        name:"busiTimeType",
                    },
                    [
                        {TEXT:"录入时间", VALUE:"1"},
                        {TEXT:"咨询时间", VALUE:"2"},
                    ]
                );

                $("#busiTimeType").val("1");


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
                    var rst = new Wade.DataMap(data);
                    //var datas = rst.get("FLAG");


                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();

                    }

                    var tagset=rst.get("TAGINFO");
                    $.custServiceActionQuery.drawTagInfo(tagset);

                    hidePopup('UI-popup','QueryCondPopupItem');
                    hidePopup('UI-popup','UI-QUERYCUSTSERVICE');
                    hidePopup('UI-popup','UI-ORG');


                });

            },



            query :function(){
                $.beginPageLoading("查询中。。。");

                let custServiceEmpId=$("#CUSTSERVICEEMPLOYEEID").val();
                let orgId=$("#ORG_ID").val();
                let startDate=$('#COND_START_DATE').val();
                let endDate=$('#COND_END_DATE').val();
                let name= $("#NAME").val();
                let tagId=$("#QUERY_TAG_ID").val();
                let wxNick=$("#WX_NICK").val();
                let busiTypeTime=$("#busiTimeType").val();


                var param='&CUSTSERVICEEMPID='+custServiceEmpId+"&ORG_ID="+orgId+"&START_DATE="+startDate+"&END_DATE="+endDate
                    +"&NAME="+name+"&TAG_ID="+tagId+"&WX_NICK="+wxNick+"&BUSI_TYPE_TIME="+busiTypeTime;
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



                let length = datas.length;
                for(let i=0;i<length;i++) {
                    let data = datas.get(i);
                    let partyName=data.get("PARTY_NAME");
                    let create_date=data.get("CREATE_TIME");
                    let houseAddress=data.get("HOUSE_ADDRESS");
                    let custservName=data.get("CUSTSERVICENAME");
                    let scanCityTime=data.get("EXPERIENCE_TIME");
                    let scanCityCabins=data.get("CITYCABINNAMES");
                    let experience=data.get("EXPERIENCE");
                    let smjrlcfinishTime=data.get("SMJRLC_FINISHTIME");
                    let hzhkfinishTime=data.get("HZHK_FINISHTIME");
                    let apsjsfinishTime=data.get("APSJS_FINISHTIME");
                    let funcPrintTime=data.get("FUNCPRINT_CREATE_TIME");
                    let stylePrintTime=data.get("STYLEPRINT_CREATE_TIME");
                    let wxnick=data.get("WX_NICK");
                    let visitcount=data.get("VISITCOUNT");
                    let tagName=data.get("TAG_NAME");
                    let consultTime=data.get("CONSULT_TIME");

                    if(wxnick=="undefined" || wxnick ==null || wxnick =="null"){
                        wxnick='';
                    }
                    if(houseAddress=="undefined" || houseAddress ==null || houseAddress =="null"){
                        houseAddress='';
                    }

                    if(smjrlcfinishTime=="undefined" || smjrlcfinishTime ==null){
                        smjrlcfinishTime='';
                    }else{
                        smjrlcfinishTime=smjrlcfinishTime.substr(0,19)
                    }

                    if(stylePrintTime=="undefined" || stylePrintTime ==null ){
                        stylePrintTime='';
                    }else{
                        stylePrintTime=stylePrintTime.substr(0,19)
                    }

                    if(funcPrintTime=="undefined" || funcPrintTime ==null ){
                        funcPrintTime='';
                    }else{
                        funcPrintTime=funcPrintTime.substr(0,19)
                    }

                    if(hzhkfinishTime=="undefined" || hzhkfinishTime ==null ){
                        hzhkfinishTime='';
                    }else{
                        hzhkfinishTime=hzhkfinishTime.substr(0,19)
                    }

                    if(apsjsfinishTime=="undefined" || apsjsfinishTime ==null ){
                        apsjsfinishTime='';
                    }else{
                        apsjsfinishTime=apsjsfinishTime.substr(0,19)
                    }

                    if(scanCityTime=="undefined" || scanCityTime ==null ){
                        scanCityTime='';
                    }else{
                        scanCityTime=scanCityTime.substr(0,19)
                    }

                    if(visitcount=="undefined" || visitcount ==null ){
                        visitcount='0';
                    }

                    if(consultTime===""||consultTime==null||consultTime==="undefined"){
                        consultTime="";
                    }else{
                        consultTime=consultTime.substr(0,19)
                    }

                    myTable.addRow({
                        "_className":"no",
                        "CUST_NAME":partyName,
                        "WX_NICK":wxnick,
                        "CREATE_TIME":consultTime,
                        "ADDRESS":houseAddress,
                        "CUST_SERVICE":custservName,
                        "SCAN_DATE":smjrlcfinishTime,
                        "STYLE_DATE":stylePrintTime,
                        "FUNC_DATE":funcPrintTime,
                        "FINISH_HZHK":hzhkfinishTime,
                        "FINISH_APSJS":apsjsfinishTime,
                        "FINISH_DKCSMW":scanCityTime,
                        "CITYCABIN":scanCityCabins,
                        "EXCEPERICE":experience,
                        "VISITCOUNT":"<span class='e_red'  ontap='$.custServiceActionQuery.redirectPartyVisit(\""+data.get("PARTY_ID")+"\",\""+data.get("PROJECT_ID")+"\");'>"+visitcount+"</span>",
                        "TAG_NAME":tagName,
                    });


                }
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
                    html.push(data.get("PARENT_ORG_NAME")+"-"+data.get("ORG_NAME"));
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

            clearCond : function () {
                $('#CUSTSERVICEEMPLOYEENAME').val('');
                $('#CUSTSERVICEEMPLOYEEID').val('');
                $('#ORG_TEXT').val('');
                $('#ORG_ID').val('');
                $('#TAG_TEXT').val('');
                $('#QUERY_TAG_ID').val('');
                $('#NAME').val('');
            },

            redirectPartyVisit : function(parytId,projectId) {
                $.redirect.open('redirectPartyVisit?PARTY_ID='+parytId+'&PROJECT_ID='+projectId, '客户回访');
            },

            redirectHKHZ : function(parytId,projectId) {
                $.redirect.open('redirectToChangeGoodSeeLiveInfo?PARTY_ID='+parytId+'&PROJECT_ID='+projectId, '咨询信息');
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
                    html.push("<li class='link'  tagId4Query='" + data.get("CODE_VALUE") + "' tagName4Query='" + data.get("CODE_NAME") + "' ontap='$.custServiceActionQuery.selectTag(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("CODE_NAME"));
                    html.push("</div>");
                    html.push("</div></div></div></li>");
                }
                $.insertHtml('beforeend', $("#TagInfo"), html.join(""));

            },

            selectTag : function (e) {
                var obj = $(e);
                var tagId = obj.attr("tagId4Query");
                var tagName=obj.attr("tagName4Query");
                $("#TAG_TEXT").val(tagName);
                $("#QUERY_TAG_ID").val(tagId);
                backPopup(document.getElementById("UI-TAG4QUERY"));
            },

            export : function() {
                let custServiceEmpId=$("#CUSTSERVICEEMPLOYEEID").val();
                let orgId=$("#ORG_ID").val();
                let startDate=$('#COND_START_DATE').val();
                let endDate=$('#COND_END_DATE').val();
                let name= $("#NAME").val();
                let tagId=$("#QUERY_TAG_ID").val();
                let wxNick=$("#WX_NICK").val();
                let busiTypeTime=$("#busiTimeType").val();


                var param='CUSTSERVICEEMPID='+custServiceEmpId+"&ORG_ID="+orgId+"&START_DATE="+startDate+"&END_DATE="+endDate
                    +"&NAME="+name+"&TAG_ID="+tagId+"&WX_NICK="+wxNick+"&BUSI_TYPE_TIME="+busiTypeTime;
                window.location.href = "/exportCustActionInfo?"+param;
            },

        }});
})($);