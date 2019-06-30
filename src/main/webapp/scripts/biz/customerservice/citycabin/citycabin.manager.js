(function($){
    $.extend({citybain:{



            init : function() {

                window["UI-popup"] = new Wade.Popup("UI-popup",{
                    visible:false,
                    mask:true
                });

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "sexcontainer",
                    // 参数设置
                    {
                        id:"ISVAILD",
                        name:"ISVAILD",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"有效", VALUE:"1"},
                        {TEXT:"失效", VALUE:"2"}
                    ]
                );

                $("#ISVAILD").bind("change", function(){
                    $("#ISVAILD").val(this.value); // 当前值
                });

                $("#ISVAILD").val("1");
                /*
                $.ajaxPost('initManagerCityCabin',null,function(data) {
                    var rst = new Wade.DataMap(data);
                    var datas = rst.get("CITYCABININFO");
                    $.citybain.drawCityCabin(datas);
                });*/
            },


            query :function(){
                $.beginPageLoading("查询中。。。");
                var parameter = $.buildJsonData("queryArea");

                $.ajaxPost('queryCityCabinInfo',parameter,function(data) {
                    var rst = new Wade.DataMap(data);
                    var datas=rst.get("CITYCABININFOLIST");
                    $.citybain.drawCityCabin(datas);
                    hidePopup('UI-popup','UI-popup-query-cond');

                });
            },


            drawCityCabin : function(datas){
                $.endPageLoading();
                $("#citycabins").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var oDate1 = (data.get("SCAN_END_TIME")).substr(0,10);
                    var status=data.get("STATUS");
                    var datenow=$.date.now()
                    html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    html.push("</div></div>");
                    html.push("<div class=\"main\" ontap=''><div class=\"title title-auto\">");
                    html.push("</div>");
                    html.push("<div class=\"content content-auto \">");
                    html.push("可参观周期："+(data.get("SCAN_START_TIME")).substr(0,10)+"~"+(data.get("SCAN_END_TIME")).substr(0,10));
                    html.push("</div><div class='content content-auto'>"+"楼盘地址："+data.get("CITYCABIN_ADDRESS"));
                    html.push("</div>");

                    html.push("<div class='content content-auto'>栋号：");
                    html.push(data.get("CITYCABIN_BUILDING"));
                    html.push(" 栋");
                    html.push("</div>");

                    html.push("<div class='content content-auto'>房号：");
                    html.push(data.get("CITYCABIN_ROOM"));
                    html.push("</div>");

                    html.push("<div class='content content-auto'>风格：");
                    html.push(data.get("CITYCABIN_TITLE"));
                    html.push("</div><div class='content content-auto'>造价：");
                    html.push(data.get("COSTS"));
                    html.push("</div>");
                    html.push("<div class='content content-auto'>参观联系人及电话：");
                    html.push(data.get("CONTACT"));
                    html.push("</div>");

                    html.push("<div class='content content-auto'>归属门店：");
                    html.push(data.get("SHOP"));
                    html.push("</div>");

                    html.push("</div>");
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-edit e_ico-pic-green e_ico-pic-r\" ontap='$.citybain.initChangeCityCabin(\""+data.get("CITY_CABIN_ID")+"\");'></span>");
                    html.push("</div>");

                    if(oDate1>datenow){
                    html.push("<div class=\"side e_size-s\">");
                    html.push("<span class=\"e_ico-delete e_ico-pic-red e_ico-pic-r\" ontap='$.citybain.deleteCityCabin(\""+data.get("CITY_CABIN_ID")+"\");'></span>");
                    html.push("</div>");
                    }

                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#citycabins"), html.join(""));
            },



            viewDetail : function(trainId) {
                $.redirect.open('redirectToTrainDetail?TRAIN_ID='+trainId, '培训详情');
            },



            deleteCityCabin : function(cityCabinId) {
                $.ajaxPost('deleteCityCabin','&CITY_CABIN_ID='+cityCabinId,function(data) {
                    MessageBox.success("成功信息", "删除城市木屋成功", function(btn){
                        document.location.reload();
                    });
                });
            },

            initChangeCityCabin : function(cityCabinId) {
                $.redirect.open('redirectToChangeCityCabin?CITY_CABIN_ID='+cityCabinId, '修改城市木屋');
            }
        }});
})($);