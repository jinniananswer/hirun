(function($){
    $.extend({housesPlan:{
        init : function(){
            window["UI-popup"] = new Wade.Popup("UI-popup");
            $.Select.append(
                // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                "houseNatureContainer",
                // 参数设置
                {
                    id:"NATURE",
                    name:"NATURE",
                    addDefault:false
                },
                // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                [
                    {TEXT:"重点期盘", VALUE:"0"},
                    {TEXT:"重点现盘", VALUE:"1"},
                    {TEXT:"责任楼盘", VALUE:"2"},
                    {TEXT:"散盘", VALUE:"3"}
                ]
            );

            $("#NATURE").bind("change", function(){
                $("#NATURE").val(this.value); // 当前值
            });

            $("#NATURE").val("0");

            window["CHECK_DATE"] = new Wade.DateField(
                "CHECK_DATE",
                {
                    dropDown:true,
                    format:"yyyy-MM-dd",
                    useTime:false,
                }
            );

            window["PLAN_IN_DATE"] = new Wade.DateField(
                "PLAN_IN_DATE",
                {
                    dropDown:true,
                    format:"yyyy-MM-dd",
                    useTime:false,
                }
            );

            window["ACTUAL_IN_DATE"] = new Wade.DateField(
                "ACTUAL_IN_DATE",
                {
                    dropDown:true,
                    format:"yyyy-MM-dd",
                    useTime:false,
                }
            );

            $.ajaxPost('initCreateHousesPlan',null,function(data){
                var rst = new Wade.DataMap(data);
                var citys = rst.get("CITYS");
                var today = rst.get("TODAY");
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
                $("#CHECK_DATE").val(today);
                $("#PLAN_IN_DATE").val(today);
            });
        },

        afterSelectCity : function(value, text){
            hidePopup('UI-popup','UI-CITY');
            if(value != $("#CITY").val()){
                this.clearArea();
                this.clearShop();
                this.clearCounselors();
            }
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
                var defaultShopId = obj.get("DEFAULT_SHOP_ID");
                var defaultShopName = obj.get("DEFAULT_SHOP_NAME");
                if(shops != null){
                    var length = shops.length;
                    var html = [];
                    for(var i=0;i<length;i++){
                        var shop = shops.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectShop(\'"+shop.get("ORG_ID")+"\',\'"+shop.get("NAME")+"\')\"><div class=\"main\">"+shop.get("NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_SHOP"), html.join(""));
                    if(defaultShopId != "" && defaultShopId != "undefined"){
                        $.housesPlan.afterSelectShop(defaultShopId, defaultShopName);
                    }
                }
            });
        },

        afterSelectArea : function(value, text){
            hidePopup('UI-popup','UI-AREA');
            $("#AREA_TEXT").val(text);
            $("#AREA").val(value);
        },

        afterSelectShop : function(value, text){
            hidePopup('UI-popup','UI-SHOP');
            if(value != $("#SHOP").val()){
                this.clearCounselors();
            }
            $("#SHOP_TEXT").val(text);
            $("#SHOP").val(value);

            $.ajaxPost('initCounselors','&ORG_ID='+value,function(data){
                var rst = new Wade.DataMap(data);
                var counselors = rst.get("COUNSELORS");

                if(counselors != null){
                    var length = counselors.length;
                    var html = [];
                    $("#BIZ_COUNSELORS").empty();
                    for(var i=0;i<length;i++){
                        var counselor = counselors.get(i);
                        html.push("<li class=\"link e_center\" tag=\"COUNSELOR\" selected=\"false\" employeeName=\""+counselor.get("NAME")+"\" employeeId=\""+counselor.get("EMPLOYEE_ID")+"\" ontap=\"$.housesPlan.afterSelectCounselor(\'"+counselor.get("EMPLOYEE_ID")+"\',this)\"><label class=\"group\" id=\"LABEL_"+counselor.get("EMPLOYEE_ID")+"\"><div class=\"main\">"+counselor.get("NAME")+"</div></label></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_COUNSELORS"), html.join(""));
                }
            });
        },

        afterSelectCounselor : function(value, eventObj){
            var obj = $(eventObj);
            var selected = obj.attr("selected");
            if(selected == "true"){
                //已有，表示取消动作
                var ico = $("#COUNSELOR_"+value+"_ico");
                ico.remove();
                obj.attr("selected", "false");
            }
            else{
                //没有，表示新添加
                obj.attr("selected", "true");
                var label = $("#LABEL_"+value);
                var html=[];
                html.push("<div class=\"side\" id=\"COUNSELOR_"+value+"_ico\"><span class=\"e_ico-ok e_ico-pic e_ico-pic-xxxs\"></span></div>");
                $.insertHtml('beforeend', label, html.join(""));
            }
        },

        clearArea : function(){
            $("#AREA").val("");
            $("#AREA_TEXT").val("");
        },

        clearShop : function(){
          $("#SHOP").val("");
          $("#SHOP_TEXT").val("");
        },

        clearCounselors : function(){
            var towers = $("li[tag=TOWERNUM]");
            if(towers != null && towers.length > 0){
                for(var i=0;i<towers.length;i++){
                    var tower = $(towers[i]);
                    tower.remove();
                }
            }

            var personalHouses = $("li[tag=HOUSENUM]");
            if(personalHouses != null && personalHouses.length > 0){
                for(var i=0;i<personalHouses.length;i++){
                    var personalHouse = $(personalHouses[i]);
                    personalHouse.remove();
                }
            }
            $("#EMPLOYEE_ID").val("");
            $("#EMPLOYEE_NAME").val("");
        },

        confirmCounselor : function(isClear){
            var employees = $("li[tag=COUNSELOR]");
            if(employees == null || employees.length <= 0){
                return;
            }

            var actualNum = 0;
            var selectedEmployeeId = "";
            var selectedEmployeeName = "";
            for(var i=0;i<employees.length;i++){
                var employee = $(employees[i]);
                var counselorId = employee.attr("employeeId");
                var selected = employee.attr("selected");
                if(selected == "true"){
                    actualNum++;
                    var counselorName = employee.attr("employeeName");
                    selectedEmployeeId += counselorId + ",";
                    selectedEmployeeName += counselorName + ",";
                }
            }
            if(selectedEmployeeId.length > 0)
                selectedEmployeeId = selectedEmployeeId.substring(0, selectedEmployeeId.length-1);
            if(selectedEmployeeName.length > 0)
                selectedEmployeeName = selectedEmployeeName.substring(0, selectedEmployeeName.length-1);
            var planNum = $("#PLAN_COUNSELOR_NUM").val();
            if(planNum < actualNum){
                if(isClear){
                    var towers = $("li[tag=TOWERNUM]");
                    if(towers != null && towers.length > 0){
                        for(var i=0;i<towers.length;i++){
                            var tower = $(towers[i]);
                            tower.remove();
                        }
                    }

                    var personalHouses = $("li[tag=HOUSENUM]");
                    if(personalHouses != null && personalHouses.length > 0){
                        for(var i=0;i<personalHouses.length;i++){
                            var personalHouse = $(personalHouses[i]);
                            personalHouse.remove();
                        }
                    }
                    $("#EMPLOYEE_ID").val("");
                    $("#EMPLOYEE_NAME").val("");
                }
                else {
                    $.TipBox.show(document.getElementById('CONFIRM_COUNSELOR'), "所选家装顾问数量不能多于计划分配的数量", "red");
                }
                return;
            }


            $("#EMPLOYEE_ID").val(selectedEmployeeId);
            $("#EMPLOYEE_NAME").val(selectedEmployeeName);
            hidePopup('UI-popup','UI-popup-query');

            var towers = $("li[tag=TOWERNUM]");
            if(towers != null && towers.length > 0){
                for(var i=0;i<towers.length;i++){
                    var tower = $(towers[i]);
                    tower.remove();
                }
            }

            var personalHouses = $("li[tag=HOUSENUM]");
            if(personalHouses != null && personalHouses.length > 0){
                for(var i=0;i<personalHouses.length;i++){
                    var personalHouse = $(personalHouses[i]);
                    personalHouse.remove();
                }
            }

            if(planNum <= 1){
                return;
            }

            if(selectedEmployeeId.length <= 0)
                return;

            var html=[];
            var employeeIdArray = selectedEmployeeId.split(",");
            var employeeNameArray = selectedEmployeeName.split(",");
            var size = employeeIdArray.length;

            for(var i=0;i<size;i++){
                html.push("<li tag=\"TOWERNUM\"><div class=\"label\">"+employeeNameArray[i] +"分配的楼栋</div><div class=\"value\"><input type=\"text\" nullable=\"no\" datatype=\"text\" desc=\""+employeeNameArray[i]+"分配楼栋\" id=\""+employeeIdArray[i]+"_TOWERNUM\" name=\""+employeeIdArray[i]+"_TOWERNUM\" /></div></li>");
                html.push("<li tag=\"HOUSENUM\"><div class=\"label\">"+employeeNameArray[i] +"负责的户数</div><div class=\"value\"><input type=\"text\" nullable=\"no\" datatype=\"numeric\" desc=\""+employeeNameArray[i]+"负责户数\" id=\""+employeeIdArray[i]+"_HOUSENUM\" name=\""+employeeIdArray[i]+"_HOUSENUM\" /></div></li>");
            }
            $.insertHtml('beforeend', $("#submitArea"), html.join(""));
        },

        initCounselor : function(){
            var counselors = $("li[tag=COUNSELOR]");
            if(counselors == null || counselors.length <= 0){
                return;
            }

            var employeeId = $("#EMPLOYEE_ID").val();

            for(var i=0;i<counselors.length;i++){
                var counselor = $(counselors[i]);
                var counselorId = counselor.attr("employeeId");
                if(employeeId.indexOf(counselorId) < 0){
                    var ico = $("#COUNSELOR_"+counselorId+"_ico");
                    if(ico != null){
                        ico.remove();
                    }
                    counselor.attr("selected", "false");
                }
                else{
                    if(counselor.attr("selected") == "false"){
                        counselor.attr("selected", "true");
                        var label = $("#LABEL_"+counselorId);
                        var html=[];
                        html.push("<div class=\"side\" id=\"COUNSELOR_"+counselorId+"_ico\"><span class=\"e_ico-ok e_ico-pic e_ico-pic-xxxs\"></span></div>");
                        $.insertHtml('beforeend', label, html.join(""));
                    }
                }
            }
            showPopup('UI-popup','UI-popup-query');
        },

        checkHouseNum : function(){
            var obj = $("#HOUSE_NUM");
            if($.validate.verifyField(obj)){
                var counselorNum = parseInt(obj.val()/500) + 1;
                if(counselorNum == 0 && obj.val() != 0){
                    counselorNum = 1;
                }

                if(counselorNum == $("#PLAN_COUNSELOR_NUM").val()){
                    return;
                }

                $("#PLAN_COUNSELOR_NUM").val(counselorNum);
            }
            this.confirmCounselor(true);
        },

        submit : function(){
            if($.validate.verifyAll("submitArea")) {
                var parameter = $.buildJsonData("submitArea");
                $.ajaxPost('submitHousesPlan', parameter, function (data) {
                    MessageBox.success("新增楼盘规划成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
                        if("ok" == btn) {
                            document.location.reload();
                        }
                        else {
                            $.redirect.closeCurrentPage();
                        }
                    },{"cancel":"取消"})
                });
            }
        }
    }});
})($);