(function($){
    $.extend({employee:{
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "sexcontainer",
                    // 参数设置
                    {
                        id:"SEX",
                        name:"SEX",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"男", VALUE:"1"},
                        {TEXT:"女", VALUE:"2"}
                    ]
                );

                $("#SEX").bind("change", function(){
                    $("#SEX").val(this.value); // 当前值
                });

                $("#SEX").val("1");

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "jobrolecontainer",
                    // 参数设置
                    {
                        id:"JOB_ROLE",
                        name:"JOB_ROLE",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"家装顾问", VALUE:"42"},
                        {TEXT:"区域经理", VALUE:"58"}
                    ]
                );

                $("#JOB_ROLE").bind("change", function(){
                    $("#JOB_ROLE").val(this.value); // 当前值
                    $("#PARENT_EMPLOYEE_NAME").val("");
                    $("#PARENT_EMPLOYEE_ID").val("");
                    $.employee.afterSelectShop($("#SHOP").val(), $("#SHOP_TEXT").val());
                });

                $("#JOB_ROLE").val("42");

                window["IN_DATE"] = new Wade.DateField(
                    "IN_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );

                $.ajaxPost('initCreateEmployee',null,function(data){
                    var obj = new Wade.DataMap(data);
                    var shops = obj.get("SHOPS");
                    var today = obj.get("TODAY");
                    var defaultShopId = obj.get("DEFAULT_SHOP_ID");
                    var defaultShopName = obj.get("DEFAULT_SHOP_NAME");
                    if(shops != null){
                        var length = shops.length;
                        var html = [];
                        for(var i=0;i<length;i++){
                            var shop = shops.get(i);
                            html.push("<li class=\"link e_center\" ontap=\"$.employee.afterSelectShop(\'"+shop.get("ORG_ID")+"\',\'"+shop.get("NAME")+"\')\"><div class=\"main\">"+shop.get("NAME")+"</div></li>");
                        }
                        $.insertHtml('beforeend', $("#BIZ_SHOP"), html.join(""));
                        if(defaultShopId != "" && defaultShopId != "undefined"){
                            $.employee.afterSelectShop(defaultShopId, defaultShopName);
                        }
                    }
                    $("#IN_DATE").val(today);
                });
            },

            afterSelectShop : function(value, text){
                hidePopup('UI-popup','UI-SHOP');
                $("#SHOP_TEXT").val(text);
                $("#SHOP").val(value);

                $.ajaxPost('initParentEmployee','&SHOP='+value+'&JOB_ROLE='+$('#JOB_ROLE').val(),function(data){
                    var rst = new Wade.DataMap(data);
                    var parentEmployees = rst.get("PARENT_EMPLOYEES");

                    if(parentEmployees != null){
                        var length = parentEmployees.length;
                        var html = [];
                        $("#BIZ_PARENT").empty();
                        for(var i=0;i<length;i++){
                            var parentEmployee = parentEmployees.get(i);
                            html.push("<li class=\"link e_center\" ontap=\"$.employee.afterSelectParent(\'"+parentEmployee.get("EMPLOYEE_ID")+"\',\'"+parentEmployee.get("NAME")+"\')\"><label class=\"group\" id=\"LABEL_"+parentEmployee.get("EMPLOYEE_ID")+"\"><div class=\"main\">"+parentEmployee.get("NAME")+"</div></label></li>");
                        }
                        $.insertHtml('beforeend', $("#BIZ_PARENT"), html.join(""));
                    }
                });
            },

            afterSelectParent : function(value, text){
                $("#PARENT_EMPLOYEE_ID").val(value);
                $("#PARENT_EMPLOYEE_NAME").val(text);
                hidePopup('UI-popup','UI-PARENT');
            },

            submit : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.ajaxPost('createEmployee', parameter, function (data) {
                        MessageBox.success("新增员工成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
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