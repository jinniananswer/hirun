(function($){
    $.extend({partyvisit:{

            init : function() {

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "visittype",
                    // 参数设置
                    {
                        id:"VISIT_TYPE",
                        name:"VISIT_TYPE",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"咨询后回访", VALUE:"1"},
                        {TEXT:"暂未量房回访", VALUE:"2"},
                        {TEXT:"量房回访", VALUE:"3"},
                        {TEXT:"出平面时回访", VALUE:"4"},
                        {TEXT:"看平面后回访", VALUE:"5"},
                        {TEXT:"出全房图时回访", VALUE:"6"},
                        {TEXT:"看全房图后回访", VALUE:"7"},
                        {TEXT:"二级精算回访", VALUE:"8"},
                        {TEXT:"跑单回访", VALUE:"9"},
                        {TEXT:"施工时回访（未收二期款）", VALUE:"10"},
                        {TEXT:"施工时回访（已收二期款）", VALUE:"11"},
                        {TEXT:"施工中逢6回访）", VALUE:"12"},
                        {TEXT:"验收后整改回访", VALUE:"13"},
                        {TEXT:"验收后一个月回访", VALUE:"14"},
                        {TEXT:"验收后半年回访", VALUE:"15"},
                        {TEXT:"验收后一年回访", VALUE:"16"},
                        {TEXT:"验收后满22个月回访", VALUE:"17"},

                    ]
                );

                $("#VISIT_TYPE").bind("change", function(){
                    $("#VISIT_TYPE").val(this.value); // 当前值
                });

                $("#VISIT_TYPE").val("1");

                $.beginPageLoading();
                $.ajaxPost('initQuery4PartyVisit','&PARTY_ID='+$("#PARTY_ID").val()+'&PROJECT_ID='+$("#PROJECT_ID").val(),function(data) {
                    $.endPageLoading();

                    var rst = new Wade.DataMap(data);
                    var partyInfo = rst.get("CUSTSERVICEINFOLIST");
                    var partyvisitinfo=rst.get("PARTYVISITINFO");
                    $.partyvisit.drawPartyVisitInfo(partyvisitinfo);
                    $.partyvisit.drawPartyInfo(partyInfo);

                });
            },

            drawPartyVisitInfo :function(datas){
                $.endPageLoading();

                $("#partyVisit").empty();
                var html = [];
                if(datas == null || datas.length <= 0){
                    return;
                }
                var length = datas.length;
                $.insertHtml('beforeend', $("#visitCount"), "合计回访次数："+length+"");

                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var visittypename=data.get("VISIT_TYPE_NAME");
                    var visitobject=data.get("VISIT_OBJECT");
                    var visitway=data.get("VISIT_WAY");
                    var visitcontent=data.get("VISIT_CONTENT");
                    var createdate=data.get("CREATE_DATE");
                    var visitname=data.get("VISIT_NAME");



                    html.push("<li class='link'><div class=\"group\"><div class=\"content content-auto\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"content content-auto\">");
                    html.push("<span class=''>回访类型：</span>")
                    html.push(visittypename);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>回访对象：</span>"+visitobject);
                    html.push("</div>");


                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>回访方式：</span>"+visitway);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>回访内容：</span>"+visitcontent);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>回访人：</span>"+visitname);
                    html.push("</div>");

                    html.push("<div class=\"content content-auto\">");
                    html.push("<span class=''>回访时间：</span>"+createdate.substr(0,19));
                    html.push("</div>");

                    html.push("</div>");

                    html.push("</div></div>");
                    html.push("</li>");
                }
                $.insertHtml('beforeend', $("#partyVisit"), html.join(""));

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


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);

                    var partyname=data.get("PARTY_NAME");
                    var creat_date=data.get("CREATE_DATE");
                    var wxnick=data.get("WX_NICK");
                    var mobileno=data.get("MOBILE_NO");
                    var houseaddress=data.get("HOUSE_ADDRESS");
                    var housemode=data.get("HOUSE_MODE");
                    var housearea=data.get("HOUSE_AREA");
                    var designername=data.get("DESIGNERNAME");

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
                    html.push("<span class='e_strong'>客户咨询时间：</span>"+creat_date.substr(0,19));
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
                if ($.validate.verifyAll("addPartyVisit")) {
                    $.beginPageLoading();

                    var parameter=$.buildJsonData("addPartyVisit")
                    //var parameter = '&PROJECT_ID=' + $("#PROJECT_ID").val() + "&PARTY_ID=" + $("#PARTY_ID").val() + "&REASON=" + $("#CLEAR_REASON").val();

                    $.ajaxPost('addPartyVisit', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("新增客户回访记录成功", "点击确定返回新增页面，点击取消关闭当前页面", function (btn) {
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