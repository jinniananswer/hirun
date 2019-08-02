(function($){
    $.extend({custServiceMonstatQuery:{
            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });

                window["custserviceTable"] = new Wade.Table("custserviceTable", {
                    fixedMode:true,
                    fixedLeftCols:1,
                    editMode:false,
                    useScroller:true
                });

                var now = $.date.now();
                var nowYYYYMM = $.date.now().substring(0,4) + $.date.now().substring(5,7);

                window["MON_DATE"] = new Wade.DateField(
                    "MON_DATE",
                    {
                        value:nowYYYYMM,
                        dropDown:true,
                        format:"yyyyMM",
                        useTime:false,
                        useMode:'month',
                    }
                );



                window["orgTree"] = new Wade.Tree("orgTree");
                $('#MON_DATE').val(nowYYYYMM);

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
                    //var datas = rst.get("CUSTSERVICEINFO");

                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();

                    }

                    //$.custServiceActionQuery.drawCustServiceInfo4Query(datas);
                    hidePopup('UI-popup','QueryCondPopupItem');
                    hidePopup('UI-popup','UI-QUERYCUSTSERVICE');
                    hidePopup('UI-popup','UI-ORG');


                });

            },



            query :function(){
                $.beginPageLoading("查询中。。。");

                var custServiceEmpId=$("#CUSTSERVICEEMPLOYEEID").val();
                var orgId=$("#ORG_ID").val();
                var monDate=$('#MON_DATE').val();

                var param='&CUSTSERVICEEMPID='+custServiceEmpId+"&ORG_ID="+orgId+"&MON_DATE="+monDate;
                $.ajaxPost('queryCustServMonStatInfo',param,function(data) {
                    var rst = new Wade.DataMap(data);
                    var datas=rst.get("CUSTSERVICESTATINFO");
                    $.custServiceMonstatQuery.drawStatInfo(datas);
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
                    $.custServiceMonstatQuery.drawCustServiceInfo4Query(datas);

                });
            },



            drawStatInfo : function(datas){
                $.endPageLoading();
                if(datas == null || datas.length <= 0){
                    $("#queryMessage").css("display","");
                    return;
                }
                $("#queryMessage").css("display","none");



                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var employeename=data.get("EMPLOYEE_NAME");
                    var consultcount=data.get("CONSULT_COUNT");
                    var stylecount=data.get("STYLE_COUNT");
                    var stylescale=data.get("STYLE_SCALE");
                    var funccount=data.get("FUNC_COUNT");
                    var funcscale=data.get("FUNC_SCALE");
                    var xqltescale=data.get("XQLTE_SCALE");
                    var scancount=data.get("SCAN_COUNT");
                    var scanscale=data.get("SCAN_SCALE");
                    var scancityhousecount=data.get("SCANCITYHOUSE_COUNT");
                    var scancityhousescale=data.get("SCANCITYHOUSE_SCALE");


                    custserviceTable.addRow({
                        "_className":"no",
                        "CUSTSERVICE_NAME":employeename,
                        "ZX_COUNT":consultcount,
                        "FINISH_STYLE":stylecount,
                        "FINISH_STYLECALE":stylescale,
                        "FINISH_FUNC":funccount,
                        "FINISH_FUNCCALE":funcscale,
                        "FINISH_XQLTECALE":xqltescale,
                        "FINISH_SMQLC":scancount,
                        "FINISH_SMQLCCALE":scanscale,
                        "FINISH_DKCSMW":scancityhousecount,
                        "FINISH_DKCSMWCALE":scancityhousescale
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
                    html.push("<li class='link' custserviceEmpId='" + data.get("EMPLOYEE_ID") + "' custserviceEmpName='" + data.get("NAME") + "' ontap='$.custServiceMonstatQuery.selectCustService(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
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
            },



        }});
})($);