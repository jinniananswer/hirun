(function($){
    $.extend({housesPlan:{
        initQuery: function(){
            window["UI-popup"] = new Wade.Popup("UI-popup");
            $.Select.append(
                "nature_select",
                {
                    id:"NATURE",
                    name:"NATURE"
                },
                [
                    {TEXT:"重点期盘", VALUE:"0"},
                    {TEXT:"重点现盘", VALUE:"1"},
                    {TEXT:"责任楼盘", VALUE:"2"},
                    {TEXT:"散盘", VALUE:"2"}
                ]
            );

            $.Select.append(
                "audit_select",
                {
                    id:"STATUS",
                    name:"STATUS"
                },
                [
                    {TEXT:"未审核", VALUE:"0"},
                    {TEXT:"已审核", VALUE:"1"},
                    {TEXT:"不批准", VALUE:"2"}
                ]
            );

            $.ajaxPost('queryHousesPlan',null,function(data){
                var rst = new Wade.DataMap(data);
                var dataset = rst.get("DATA");
                $.housesPlan.drawHousesPlan(dataset);
            });

            $.ajaxPost('initCreateHousesPlan',null,function(data){
                var rst = new Wade.DataMap(data);
                var citys = rst.get("CITYS");

                if(citys != null){
                    var length = citys.length;
                    var html=[];
                    html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectCity(\'\',\'所有城市\')\"><div class=\"main\">所有城市</div></li>");
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

                window["PLAN_IN_DATE"] = new Wade.DateField(
                    "PLAN_IN_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false,
                    }
                );
            });
        },

        drawHousesPlan : function(dataset){
            $("#houses_plan").empty();
            if(dataset == null || dataset.length <= 0){
                return;
            }

            var html = [];
            var size = dataset.length;
            for(var i=0;i<size;i++){
                var data = dataset.get(i);
                html.push("<li class='link' ontap=\"$.redirect.open('redirectToDetail?HOUSES_ID="+data.get("HOUSES_ID")+"','"+data.get("NAME")+"规划详情');\">");
                html.push("<div class=\"content\">");
                html.push("<div class=\"group\">");
                html.push("<div class=\"content\">");
                html.push("<div class=\"main\">");
                html.push("<div class=\"title \"><span class=\"e_strong\">"+data.get("NAME")+"</span>");
                var status = data.get("STATUS");
                if(status == "0") {
                    //html.push("<div class=\"right link\" ontap=\"$.housesPlan.initAudit(" + data.get("HOUSES_ID") + ")\"><span class=\"e_ico-select\"></span> 审核</div>");
                }
                //html.push("<div class=\"right link\" ontap=\"$.redirect.open('redirectToChangeHousesPlan?HOUSES_ID="+data.get("HOUSES_ID")+"','变更楼盘规划');\"><span class=\"e_ico-edit\"></span> 编辑</div>");
                html.push("</div>");
                html.push("<div class=\"content\">");
                if(data.get("NATURE") == "0")
                    html.push("<span class=\"e_progress e_progress-red\">");
                else if(data.get("NATURE") == "1")
                    html.push("<span class=\"e_progress\">");
                else
                    html.push("<span class=\"e_progress e_progress-orange\">");
                html.push("<span class=\"e_progressBar\">");
                html.push("<span style=\"width:"+data.get("CUR_PROGRESS")+"%\" class=\"e_progressProgress\">发展周期:"+data.get("PAST_DAYS")+"天/"+data.get("ALL_DAYS")+"天</span>");
                html.push("</span></span>");
                html.push("</div>");
                html.push("<div class=\"content\">"+data.get("CITY_NAME")+"/"+data.get("ORG_NAME")+"/"+data.get("CHECK_DATE")+"交房</div>");
                html.push("</div></div></div>");
                html.push("<div class=\"side\">");
                if(data.get("NATURE")=="0"){
                    html.push("<span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-xs\">");
                }
                else if(data.get("NATURE")=="1"){
                    html.push("<span class=\"e_ico-pic-green e_ico-pic-r e_ico-pic-xs\">");
                }
                else{
                    html.push("<span class=\"e_ico-pic-orange e_ico-pic-r e_ico-pic-xs\">");
                }
                html.push(data.get("NATURE_NAME")+"</span></div>");
                html.push("</div>");
                html.push("<div class=\"sub\"><div class=\"main\">总户数："+data.get("HOUSE_NUM")+"</div>");

                var employees = data.get("COUNSELORS");
                if(employees != null && employees.length > 0) {
                    for(var j=0;j<employees.length;j++) {
                        var employee = employees.get(j);
                        if(j<=2)
                            html.push("<div class=\"side content content-row-2\"><span class=\"e_tag e_tag-green\">"+employee.get("EMPLOYEE_NAME"));
                        else
                        {
                            html.push("<div class=\"side content content-row-2\"><span class=\"e_tag e_tag-green\">...</span></div>");
                            break;
                        }

                        html.push("</span></div>");
                    }
                }
                else{
                    html.push("<div class=\"side\"><span class=\"e_tag e_tag-red\">未分配家装顾问</span></div>");
                }
                html.push("</div>");
                html.push("<div class=\"statu e_size-s statu-orange statu-right\">"+data.get("STATUS_NAME")+"</div>");
                html.push("</li>");
            }
            $.insertHtml('beforeend', $("#houses_plan"), html.join(""));
        },

        afterSelectCity : function(value, text){
            backPopup(document.getElementById("UI-CITY"));
            $("#CITY_TEXT").val(text);
            $("#CITY").val(value);
            if(value == ""){
                return;
            }
            $.ajaxPost('initArea','&CITY_ID='+value,function(data){
                var obj = new Wade.DataMap(data);
                var areas = obj.get("AREAS");
                if(areas != null){
                    var length = areas.length;
                    var html = [];
                    html.push("<li class=\"link e_center\" ontap=\"$.housesPlan.afterSelectArea(\'\',\'所有区域\')\"><div class=\"main\">所有区域</div></li>");
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
            });
        },

        afterSelectArea : function(value, text){
            backPopup(document.getElementById('UI-AREA'));
            $("#AREA_TEXT").val(text);
            $("#AREA").val(value);
        },

        afterSelectShop : function(value, text){
            backPopup(document.getElementById('UI-SHOP'));
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

        initCounselor : function(){
            var counselors = $("li[tag=COUNSELOR]");
            if(counselors == null || counselors.length <= 0){
                forwardPopup('UI-popup','UI-COUNSELOR');
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
            }
            forwardPopup('UI-popup','UI-COUNSELOR');
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
                $("#EMPLOYEE_ID").val("");
                $("#EMPLOYEE_NAME").val("");
                backPopup(document.getElementById('UI-COUNSELOR'));
                return;
            }
            selectedEmployeeId = selectedEmployeeId.substring(0, selectedEmployeeId.length-1);
            selectedEmployeeName = selectedEmployeeName.substring(0, selectedEmployeeName.length-1);

            $("#EMPLOYEE_ID").val(selectedEmployeeId);
            $("#EMPLOYEE_NAME").val(selectedEmployeeName);
            backPopup(document.getElementById('UI-COUNSELOR'));
        },

        query : function(){
            if(!$.validate.verifyAll("queryArea")){
                return;
            }
            var parameter = $.buildJsonData("queryArea");
            hidePopup('UI-popup','UI-popup-query-cond');
            $.ajaxPost('queryHousesPlan', parameter, function (data) {
                var rst = new Wade.DataMap(data);
                var dataset = rst.get("DATA");
                $.housesPlan.drawHousesPlan(dataset);
            });
        },

        scrollHandler: function(){
            var scrollTop = $("#scroll_div").scrollTop();
            var divHeight = $("#scroll_div").height();
            var scrollHeight = document.getElementById("scroll_div").scrollHeight;
            if(scrollTop + divHeight >= scrollHeight){
                alert('加载数据');
            }
        }
    }});
})($);
