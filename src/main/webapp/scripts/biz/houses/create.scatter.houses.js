(function($){
    $.extend({housesPlan:{
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");

                $.ajaxPost('initCreateScatterHouses',null,function(data){
                    var rst = new Wade.DataMap(data);
                    var citys = rst.get("CITYS");
                    var defaultCityId = rst.get("DEFAULT_CITY_ID");
                    var defaultCityName = rst.get("DEFAULT_CITY_NAME");

                    if(citys != null){
                        var length = citys.length;
                        var html=[];
                        for(var i=0;i<length;i++){
                            var city = citys.get(i);
                            html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectCity(\'"+city.get("CODE_VALUE")+"\',\'"+city.get("CODE_NAME")+"\')\"><div class=\"main\">"+city.get("CODE_NAME")+"</div></li>");
                        }
                        $.insertHtml('beforeend', $("#BIZ_CITY"), html.join(""));

                        if(defaultCityId != null && defaultCityId != "undefined"){
                            $.housesPlan.afterSelectCity(defaultCityId,defaultCityName);
                        }
                    }
                });
            },

            afterSelectCity : function(value, text){
                $("#CITY_TEXT").val(text);
                $("#CITY").val(value);
                hidePopup('UI-popup','UI-CITY');
            },

            submit : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.ajaxPost('submitScatterHouses', parameter, function (data) {
                        MessageBox.success("新增散盘成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
                            if("ok" == btn) {
                                document.location.reload();
                            }
                            else {
                                $.rediret.closeCurrentPage();
                            }
                        },{"cancel":"取消"})
                    });
                }
            }
        }});
})($);