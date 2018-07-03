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

            $.ajaxPost('initChangeHousesPlan','&HOUSES_ID='+$("#HOUSES_ID").val(),function(data){
                var rst = new Wade.DataMap(data);
                var citys = rst.get("CITYS");
                var housePlan = rst.get("HOUSES_PLAN");

                if(citys != null){
                    var length = citys.length;
                    var html=[];
                    for(var i=0;i<length;i++){
                        var city = citys.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectCity(\'"+city.get("CODE_VALUE")+"\',\'"+city.get("CODE_NAME")+"\')\"><div class=\"main\">"+city.get("CODE_NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_CITY"), html.join(""));
                }

                if(housePlan != null){
                    $("#NAME").val(housePlan.get("NAME"));
                    $("#NATURE").val(housePlan.get("NATURE"));
                    $("#AREA").val(housePlan.get("AREA"));
                    $("#AREA_TEXT").val(housePlan.get("AREA_NAME"));
                    $("#CHECK_DATE").val(housePlan.get("CHECK_DATE"));
                    $("#HOUSE_NUM").val(housePlan.get("HOUSE_NUM"));
                    $("#PLAN_COUNSELOR_NUM").val(housePlan.get("PLAN_COUNSELOR_NUM"));
                    $("#PLAN_IN_DATE").val(housePlan.get("PLAN_IN_DATE"));
                    $("#CITY_TEXT").val(housePlan.get("CITY_NAME"));
                    $("#CITY").val(housePlan.get("CITY"));
                    $("#SHOP").val(housePlan.get("ORG_ID"));
                    $("#SHOP_TEXT").val(housePlan.get("ORG_NAME"));

                    var counselors = housePlan.get("COUNSELORS");
                    var counselorNames = "";
                    var counselorIds = "";
                    if(counselors != null && counselors.length > 0){
                        var length = counselors.length;
                        var html=[];
                        for(var i=0;i<length;i++){
                            var counselor = counselors.get(i);
                            html.push("<li tag=\"TOWERNUM\"><div class=\"label\">"+counselor.get("EMPLOYEE_NAME") +"分配的楼栋</div><div class=\"value\"><input type=\"text\" nullable=\"no\" datatype=\"text\" desc=\""+counselor.get("EMPLOYEE_NAME")+"分配楼栋\" id=\""+counselor.get("EMPLOYEE_ID")+"_TOWERNUM\" name=\""+counselor.get("EMPLOYEE_ID")+"_TOWERNUM\" value=\""+counselor.get("TOWER_NO")+"\"/></div></li>");
                            html.push("<li tag=\"HOUSENUM\"><div class=\"label\">"+counselor.get("EMPLOYEE_NAME") +"负责的户数</div><div class=\"value\"><input type=\"text\" nullable=\"no\" datatype=\"numeric\" desc=\""+counselor.get("EMPLOYEE_NAME")+"负责户数\" id=\""+counselor.get("EMPLOYEE_ID")+"_HOUSENUM\" name=\""+counselor.get("EMPLOYEE_ID")+"_HOUSENUM\" value=\""+counselor.get("EMPLOYEE_HOUSE_NUM")+"\"/></div></li>");
                            if(i != length -1){
                                counselorIds += counselor.get("EMPLOYEE_ID") + ",";
                                counselorNames += counselor.get("EMPLOYEE_NAME") + ",";
                            }
                            else{
                                counselorIds += counselor.get("EMPLOYEE_ID") + ",";
                                counselorNames += counselor.get("EMPLOYEE_NAME");
                            }
                        }
                        if(housePlan.get("PLAN_COUNSELOR_NUM") > 1)
                            $.insertHtml('beforeend', $("#submitArea"), html.join(""));
                    }
                    $("#EMPLOYEE_NAME").val(counselorNames);
                    $("#EMPLOYEE_ID").val(counselorIds);
                    $("#OLD_EMPLOYEE_ID").val(counselorIds);
                }
                $.housesPlan.afterSelectCity($("#CITY").val(),$("#CITY_TEXT").val());
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
                if(shops != null){
                    var length = shops.length;
                    var html = [];
                    for(var i=0;i<length;i++){
                        var shop = shops.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectShop(\'"+shop.get("ORG_ID")+"\',\'"+shop.get("NAME")+"\')\"><div class=\"main\">"+shop.get("NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#BIZ_SHOP"), html.join(""));
                    $.housesPlan.afterSelectShop($("#SHOP").val(), $("#SHOP_TEXT").val());
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
                $.housesPlan.initCounselor();
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
            if(actualNum <= 0){
                hidePopup('UI-popup','UI-popup-query');
                return;
            }
            selectedEmployeeId = selectedEmployeeId.substring(0, selectedEmployeeId.length-1);
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
        },

        checkHouseNum : function(){
            var obj = $("#HOUSE_NUM");
            if($.validate.verifyField(obj)){
                var counselorNum = Math.round(obj.val()/500);
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
                $.ajaxPost('changeHousesPlan', parameter, function (data) {
                    MessageBox.success("变更楼盘规划成功","点击确定关闭当前页面", function(btn){
                        $.redirect.closeCurrentPage();
                    })
                });
            }
        }
    }});
})($);