(function($){
    $.extend({housesPlan:{
        init : function(){
            window["UI-popup"] = new Wade.Popup("UI-popup");
            window["NATURE"] = new Wade.Segment("NATURE",{
                disabled:false
            });

            $("#NATURE").change(function(){
                var modeVal = this.value; // this.value 获取分段器组件当前值
                $("#NATURE").val(modeVal);
            });

            $("#NATURE").val("0");

            $.ajaxPost('initCreateHousesPlan',null,function(data){
                var rst = new Wade.DataMap(data);
                var citys = rst.get("CITYS");
                var today = rst.get("TODAY");

                if(citys != null){
                    var length = citys.length;
                    var html=[];
                    for(var i=0;i<length;i++){
                        var city = citys.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectCity(\'"+city.get("CODE_VALUE")+"\',\'"+city.get("CODE_NAME")+"\')\"><div class=\"main\">"+city.get("CODE_NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_CITY"), html.join(""));
                }

                window["CHECK_DATE"] = new Wade.DateField(
                    "CHECK_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );
                $("#CHECK_DATE").val(today);

                window["PLAN_IN_DATE"] = new Wade.DateField(
                    "PLAN_IN_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );
                $("#PLAN_IN_DATE").val(today);
            },function(){
                alert('error');
            });
        },

        afterSelectCity : function(value, text){
            hidePopup('UI-popup','UI-CITY');
            $("#CITY_TEXT").val(text);
            $("#CITY").val(value);
            $.ajaxPost('initArea','&CITY_ID='+value,function(data){
                var obj = new Wade.DataMap(data);
                var areas = obj.get("AREAS");
                if(areas != null){
                    var length = areas.length;
                    var html = [];
                    for(var i=0;i<length;i++){
                        var area = areas.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectArea(\'"+area.get("CODE_VALUE")+"\',\'"+area.get("CODE_NAME")+"\')\"><div class=\"main\">"+area.get("CODE_NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_AREA"), html.join(""));
                }

                var shops = obj.get("SHOPS");
                if(shops != null){
                    var length = shops.length;
                    var html = [];
                    for(var i=0;i<length;i++){
                        var shop = shops.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectShop(\'"+shop.get("ORG_ID")+"\',\'"+shop.get("NAME")+"\')\"><div class=\"main\">"+shop.get("NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_SHOP"), html.join(""));
                }
            },function(){
                alert('error');
            });
        },

        afterSelectArea : function(value, text){
            hidePopup('UI-popup','UI-AREA');
            $("#AREA_TEXT").val(text);
            $("#AREA").val(value);
        },

        afterSelectShop : function(value, text){
            hidePopup('UI-popup','UI-SHOP');
            $("#SHOP_TEXT").val(text);
            $("#SHOP").val(value);

            $.ajaxPost('initCounselors','&ORG_ID='+value,function(data){
                var counselors = new Wade.DatasetList(data);

                if(counselors != null){
                    var length = counselors.length;
                    var html = [];
                    $("#BIZ_COUNSELORS").empty();
                    for(var i=0;i<length;i++){
                        var counselor = counselors.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectCounselor(\'"+counselor.get("EMPLOYEE_ID")+"\',\'"+counselor.get("NAME")+"\')\"><div class=\"main\">"+counselor.get("NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_COUNSELORS"), html.join(""));
                }
            },function(){
                alert('error');
            });
        },

        afterSelectCounselor : function(value, text){
            hidePopup('UI-popup','UI-popup-query');
            $("#COUNSELOR_NAME").val(text);
            $("#COUNSELOR_ID").val(value);
        },

        submit : function(){
            if($.validate.verifyAll("submitArea")) {
                var parameter = $.buildJsonData("submitArea");
                $.ajaxPost('submitHousesPlan', parameter, function (data) {
                    alert("ok");
                }, function () {
                    alert('error');
                });
            }
        }
    }});
})($);