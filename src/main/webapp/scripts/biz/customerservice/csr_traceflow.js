(function($){
    $.extend({csrTraceFlow:{

            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });


                window["MW_EXPERIENCE_TIME"] = new Wade.DateField(
                    "MW_EXPERIENCE_TIME",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                window["mySwitch"] = new Wade.Switch("mySwitch",{
                    switchOn:true,
                    onValue:"on",
                    offValue:"off",
                });

                $("#mySwitch").val("off");

                $("#mySwitch").change(function(){
                    $("#mySwitch").val(this.value); // this.value 获取开关组件当前值
                });

                $.beginPageLoading();

                $.ajaxPost('initCsrTraceFlow','&PARTY_ID='+$("#PARTY_ID").val()+'&PROJECT_ID='+$("#PROJECT_ID").val(),function(data){
                    $.endPageLoading();

                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("PARTYACTIONFLOW");
                    var projectDesignerInfo=rst.get("PROJECTDESIGNERINFO");
                    var xqltyInfo=rst.get("PROJECTXQLTYINFO");
                    var cityCabinInfo=rst.get("CITYCABININFO");
                    var insScanCityInfo=rst.get("INSSCANCITYINFO");
                    var partyInfo=rst.get("PARTYINFO");
                    var flag=rst.get("FLAG");
                    $.csrTraceFlow.isShowQueryCond(flag);
                    $.csrTraceFlow.drawFlow(datas,projectDesignerInfo);
                    $.csrTraceFlow.drawXQLTY(xqltyInfo);
                    $.csrTraceFlow.drawCityhouseInfo(insScanCityInfo);
                    $.csrTraceFlow.drawCityCabin(cityCabinInfo);
                    $.csrTraceFlow.drawPartyInfo(partyInfo);

                    hidePopup('UI-popup','GZGZHUI-popup-query-cond');
                    hidePopup('UI-popup','XQLTYUI-popup-query-cond');
                    hidePopup('UI-popup','SCANCITYINFOUI-popup-query-cond');
                    hidePopup('UI-popup','UI-CHOOSEDESIGNER');
                    hidePopup('UI-popup','UI-CHOOSECITYCABIN');


                });
            },

            isShowQueryCond :function(flag){
                var flag1=flag.get("FLAG");
                if("TRUE"==flag1){
                    //$("#mySwitch").attr("disabled", false);
                    $("#isSwitch").css("display", "");

                }else{
                    $("#isSwitch").css("display", "none");
                }
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

                    var partyname=datas.get("PARTY_NAME");
                    var wxnick=datas.get("WX_NICK");

                    if(partyname=='undefined'|| partyname == null) {
                        partyname='';
                    }
                    if(wxnick=='undefined'|| wxnick == null) {
                        wxnick='';
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

                    html.push("</div>");


                    html.push("</div></div>");
                    html.push("</li>");


                $.insertHtml('beforeend', $("#preworks"), html.join(""));
            },


            drawFlow :function (datas,projectDesignerInfo) {
                $.endPageLoading();
                if(datas == null || datas.length <= 0){
                    $("#queryMessage").css("display","");
                    $("#tip").css("display","none");
                    return;
                }

                $("#queryMessage").css("display","none");
                $("#tip").css("display","");

                var length = datas.length;
                var html="";

                for(var i=0;i<length;i++){
                   var data=datas.get(i);
                   var action_name=data.get("ACTION_NAME");
                   var finish_time=data.get("FINISH_TIME");
                   var status_name=data.get("STATUS_NAME");
                   var action_code=data.get("ACTION_CODE");
                   var mode_time=data.get("XQLTY_FINISHTIME");
                   var xqltefinishtime=data.get("XQLTE_FINISHTIME");
                    var signin_time=data.get("SMDLUPCD_FINISHTIME");
                   var status=data.get("STATUS");

                   html += '<li id="'+action_code+'_li">';
                   html += '<div class="box" onclick="$.csrTraceFlow.showFlowDetail(this);" id="'+action_code+'">';

                   if("APSJS"==action_code || "DKCSMW"==action_code || "HZHK"==action_code){
                       html += '<div class="ico e_ico-task"></div>';

                   }else{
                       html += '<div class="ico"></div>';
                   }

                   html += '<div class="title" id="' + action_code + '_title">' +action_name + '</div>';
                   if("XQLTY"==action_code ){
                       if(mode_time!="undefined" && mode_time!=null){
                           html += '<div class="date" id="' + action_code + '_Date">'+mode_time.substr(0,19)+'</div>';
                           html += '<div class="content"><span class="e_green" id="' + action_code + '_State">'+status_name+'</span></div>';
                       }else {
                           html += '<div class="content"><span class="e_red" id="' + action_code + '_State">'+status_name+'</span></div>';
                       }
                   }else if("SMDLUPCD"==action_code){
                       if(signin_time!="undefined" && signin_time!=null){
                           html += '<div class="date" id="' + action_code + '_Date">'+signin_time.substr(0,19)+'</div>';
                           html += '<div class="content"><span class="e_green" id="' + action_code + '_State">'+status_name+'</span></div>';
                       }else {
                           html += '<div class="content"><span class="e_red" id="' + action_code + '_State">'+status_name+'</span></div>';
                       }
                   }else if("XQLTE"==action_code){
                       if(xqltefinishtime!="undefined" && xqltefinishtime!=null){
                           html += '<div class="date" id="' + action_code + '_Date">'+xqltefinishtime.substr(0,19)+'</div>';
                           html += '<div class="content"><span class="e_green" id="' + action_code + '_State">'+status_name+'</span></div>';
                       }else {
                           html += '<div class="content"><span class="e_red" id="' + action_code + '_State">'+status_name+'</span></div>';
                       }
                   } else {
                     if(finish_time != null && finish_time != "undefined")
                         if("DKCSMW"==action_code){
                           html += '<div class="date" id="' + action_code + '_Date">'+finish_time.substr(0,11)+'</div>';
                         }else{
                             html += '<div class="date" id="' + action_code + '_Date">'+finish_time.substr(0,19)+'</div>';
                         }

                     if(status=='0'){
                       html += '<div class="content"><span class="e_red" id="' + action_code + '_State">'+status_name+'</span></div>';
                     }else{
                       html += '<div class="content"><span class="e_green" id="' + action_code + '_State">'+status_name+'</span></div>';
                       if("APSJS"==action_code) {
                           html += '<div class="content"><span class="e_green" >'+"设计师："+projectDesignerInfo.get(0).get("NAME")+'</span></div>';
                       }
                   }
                   }
                   html += '</div>'
                   html += '</li>';

                    $('#traceflow').empty().append(html);
                }

            },

            drawXQLTY : function(datas){
                $("#xqltyinfo").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var name=data.get("NAME");
                    var age=data.get("AGE");
                    var housekind=data.get("HOUSE_KIND");
                    var style=data.get("STYLE");
                    var func=data.get("FUNC");
                    var appa=data.get("APPLICATION");
                    var housearea=data.get("HOUSE_AREA");
                    var modetime=data.get("MODE_TIME");

                    if(name == "undefined" || name==null)
                        name="";
                    if(age=="undefined" || age==null)
                        age="";
                    if(housekind=="undefined" || housekind==null)
                        housekind="";
                    if(style=="undefined" || style==null)
                        style="";
                    if(func=="undefined" || func==null)
                        func="";
                    if(appa=="undefined" || appa==null)
                        appa="";
                    if(housearea=="undefined" || housearea==null)
                        appa="";
                    if(modetime=="undefined" || modetime==null)
                        modetime="";

                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title title-auto\">");
                    html.push("</div>");
                    html.push("<div class='content content-auto'>户型：");
                    html.push(housekind);
                    html.push("</div>");
                    html.push("<div class='content content-auto'>面积：");
                    html.push(housearea);
                    html.push("</div>");
                    html.push("<div class='content content-auto'>用途：");
                    html.push(appa);
                    html.push("</div>");
                    html.push("<div class='content content-auto'>保存时间：");
                    html.push(modetime);
                    html.push("</div>");
                    html.push("<div class='content content-auto'>风格：");
                    html.push(style);
                    html.push("</div>");

                    html.push("<div class='content content-auto'>功能：");
                    html.push(func);
                    html.push("</div>");
                    html.push("</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#xqltyinfo"), html.join(""));
            },

            drawCityhouseInfo : function(datas){
                $("#scancityhouseinfo").empty();
                var html = [];


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var experiencetime=data.get("EXPERIENCE_TIME");
                    var citycabins=data.get("CITYCABINNAMES");
                    var experience=data.get("EXPERIENCE");


                    if(experiencetime == "undefined" || experiencetime==null)
                        experiencetime="";
                    if(citycabins=="undefined" || citycabins==null)
                        citycabins="";
                    if(experience=="undefined" || experience==null)
                        experience="";


                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title title-auto\">");
                    html.push("</div>");
                    html.push("<div class='content content-auto'>城市木屋体验时间：");
                    html.push(experiencetime.substr(0,10));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>城市木屋地址：");
                    html.push(citycabins);
                    html.push("</div>");
                    html.push("<div class='content content-auto'>城市木屋感受：");
                    html.push(experience);
                    html.push("</div>");

                    html.push("</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#scancityhouseinfo"), html.join(""));
            },

             showFlowDetail :function(e) {
                var action_code= $(e).attr('id');
                var party_id=$("#PARTY_ID").val();
                var project_id=$("#PROJECT_ID").val();

                 if("GZGZH"==action_code){
                     $.ajaxPost('queryGZGZHDetailByPartyId', '&PARTY_ID='+party_id, function (data) {
                         var partyinfo = data.PARTY_INFO;
                         var wx_nick=partyinfo.WX_NICK;
                         $("#WX_NICK").val(wx_nick);
                         showPopup('UI-popup','GZGZHUI-popup-query-cond');

                     });
                 }
                 if("APSJS"==action_code){
                     $("#submitButton").css("display","none");
                     $("#messagebox").css("display","none");

                     showPopup('UI-popup','UI-CHOOSEDESIGNER');

                 }
                 if("HZHK"==action_code){
                     window.location.href = "/redirectToChangeGoodSeeLiveInfo?PARTY_ID="+party_id+"&PROJECT_ID="+project_id;
                    //$.redirect.open('redirectToChangeGoodSeeLiveInfo?PARTY_ID='+party_id+'&PROJECT_ID='+project_id, '好看好住修改界面');
                 }
                 if("XQLTY"==action_code){
                     showPopup('UI-popup','XQLTYUI-popup-query-cond');
                 }
                 if("DKCSMW"==action_code){
                     $("#querycitycabinmessage").css("display","none");
                     //showPopup('UI-popup','UI-CHOOSECITYCABIN');SCANCITYINFOUI-popup-query-cond
                     showPopup('UI-popup','SCANCITYINFOUI-popup-query-cond');

                 }
             },

            queryDesigners :function(){
                var param=$.buildJsonData("desingersearchArea");
                $.beginPageLoading();

                $.ajaxPost('queryDesignerByEmployeeId',param, function (data) {
                    $.endPageLoading();

                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("DESIGNERINFO");
                    $.csrTraceFlow.drawDesigners(datas);
                    showPopup('UI-popup','UI-CHOOSEDESIGNER');

                });
            },

            drawDesigners : function(datas){
                $.endPageLoading();

                $("#designers").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    $("#submitButton").css("display","none");

                    return;
                }

                $("#messagebox").css("display","none");
                $("#submitButton").css("display","");

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' id='" + data.get("EMPLOYEE_ID") + "' ontap='$.csrTraceFlow.selectDesigner(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push(data.get("PARENT_ORG_NAME"));
                    html.push("</div></div><div class='side e_size-s'>"+data.get("DESIGNER_LEVEL_NAME"));
                    html.push("</div>")
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#designers"), html.join(""));
            },

            selectDesigner : function (e) {
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

            chooseDesigner : function () {
                var lis = $("#designers li");
                var party_id=$("#PARTY_ID").val();
                var project_id=$("#PROJECT_ID").val();

                var length = lis.length;
                for(var i=0;i<length;i++) {
                    var li = $(lis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        var employeeId = li.attr("id");
                    }
                }

                if(employeeId ==null) {
                    MessageBox.alert("您没有选中任何员工，请先选择员工");
                    return;
                }

                var parameter = '&SELECTED_EMPLOYEE_ID='+ employeeId + "&PARTY_ID="+party_id+"&PROJECT_ID="+project_id;

                $.beginPageLoading();

                $.ajaxPost('chooseDesigner', parameter, function (data) {
                    $.endPageLoading();
                    MessageBox.success("设计师安排成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });
            },

            queryCityCabinByName :function(){
                var cityCabinName=$('#SEARCH_CITYCABIN').val();
                $.beginPageLoading();

                $.ajaxPost('queryCityCabinByName','CityCabinName='+cityCabinName, function (data) {
                    $.endPageLoading();

                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("CITYCABININFO");
                    $.csrTraceFlow.drawCityCabin(datas);

                });
            },

            drawCityCabin : function(datas){
                $.endPageLoading();

                $("#citycabininfo").empty();
                var html = [];
                if(datas == null || datas.length <= 0){
                    $("#querycitycabinmessage").css("display","");
                    $("#submitCityCabinButton").css("display","none");

                    return;
                }

                $("#querycitycabinmessage").css("display","none");
                $("#submitCityCabinButton").css("display","");

                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' city_cabin_id='" + data.get("CITY_CABIN_ID") + "' ontap='$.csrTraceFlow.selectManyElement(this);'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("CITYCABIN_ADDRESS")+'|'+data.get("CITYCABIN_BUILDING")+'|栋'+data.get("CITYCABIN_ROOM"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("CITYCABIN_TITLE"));
                    html.push("</div>")
                    html.push("<div class=\"content\">");
                    html.push("</div><div class='content'>"+data.get("COSTS"));
                    html.push("</div>")
                    html.push("</div></div></div></li>");
                }
                $.insertHtml('beforeend', $("#citycabininfo"), html.join(""));



            },


            confirmCityCabin : function () {
                if(!$.validate.verifyAll("mwinfo"))
                    return;

                var citycabinlis = $("#citycabininfo li");
                var citycabinlength = citycabinlis.length;
                var citycabinids='';
                for(var i=0;i<citycabinlength;i++) {
                    var li = $(citycabinlis[i]);
                    var className = li.attr("class");
                    if(className == "link checked") {
                        citycabinids += li.attr("city_cabin_id")+",";
                    }
                }
                if(citycabinids ==null ||citycabinids=='') {
                    MessageBox.alert("您没有选中任何城市木屋，请先选择城市木屋");
                    return;
                }
                var experiencetime=$("#MW_EXPERIENCE_TIME").val();
                var partyid=$("#PARTY_ID").val();
                var projectid=$("#PROJECT_ID").val();
                var experience=$("#MW_EXPERIENCE").val();

                var parameter = '&PARTY_ID='+ partyid +'&PROJECT_ID='+ projectid+'&MW_EXPERIENCE_TIME='+ experiencetime+'&MW_EXPERIENCE='+experience+"&CITY_CABIN_IDS="+citycabinids.substr(0,citycabinids.length-1);

                $.beginPageLoading();
                $.ajaxPost('confirmScanCityCabin', parameter, function (data) {
                    $.endPageLoading();
                    MessageBox.success("带看城市木屋数据提交成功","点击确定返回页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                })
            },



        }});
})($);

