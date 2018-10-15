(function($){
    $.extend({housesPlan:{
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");

                $.ajaxPost('initChangeScatterHouses','&HOUSES_ID='+$("#HOUSES_ID").val(),function(data){
                    var rst = new Wade.DataMap(data);
                    var citys = rst.get("CITYS");
                    var defaultCityId = rst.get("DEFAULT_CITY_ID");
                    var defaultCityName = rst.get("DEFAULT_CITY_NAME");
                    var house = rst.get("HOUSE");

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
                    $.housesPlan.initHouse(house);
                });
            },

            initHouse : function(data) {
                if(data == null || data == "undefined") {
                    return;
                }

                $("#HOUSES_ID").val(data.get("HOUSES_ID"));
                $("#NAME").val(data.get("NAME"));
                $("#CITY_TEXT").val(data.get("CITY_NAME"));
                $("#CITY").val(data.get("CITY"));
            },

            afterSelectCity : function(value, text){
                $("#CITY_TEXT").val(text);
                $("#CITY").val(value);
                hidePopup('UI-popup','UI-CITY');
            },

            submit : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.ajaxPost('changeScatterHouse', parameter, function (data) {
                        MessageBox.success("修改散盘规划成功","点击确定关闭当前页面", function(btn){
                            $.redirect.closeCurrentPage();
                        });
                    });
                }
            }
        }});
})($);