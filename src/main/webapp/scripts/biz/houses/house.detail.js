(function($){
    $.extend({housesPlan:{
        init : function(){
            window["UI-popup"] = new Wade.Popup("UI-popup");
            window["AUDIT_OPTION"] = new Wade.Switch("AUDIT_OPTION",{
                switchOn:true,
                onValue:"1",
                offValue:"2"
            });

            $("#AUDIT_OPTION").change(function(){
                if(this.value == "1"){
                    //审核通过
                    $("#AUDIT_OPINION").attr("nullable", "yes");
                }
                else if(this.value == "2"){
                    //审核不通过
                    $("#AUDIT_OPINION").attr("nullable", "no");
                }
            });

            $.ajaxPost('initChangeHousesPlan','&HOUSES_ID='+$("#HOUSES_ID").val(),function(data){
                var rst = new Wade.DataMap(data);
                var housePlan = rst.get("HOUSES_PLAN");
                $.housesPlan.drawHouseContent(housePlan);
                $.housesPlan.drawCounselors(housePlan.get("COUNSELORS"), housePlan.get("PLAN_COUNSELOR_NUM"));
            });
        },

        drawHouseContent : function(housePlan){
            var html = [];
            html.push(housePlan.get("NAME"));
            $.insertHtml('beforeend', $("#HOUSE_NAME"), html.join(""));
            html = [];
            html.push("<li><span class='label'>归属城市：</span><span class='value'>"+housePlan.get("CITY_NAME")+"</span></li>");
            html.push("<li><span class='label'>归属区域：</span><span class='value'>"+housePlan.get("AREA_NAME")+"</span></li>");
            html.push("<li><span class='label'>责任店面：</span><span class='value'>"+housePlan.get("ORG_NAME")+"</span></li>");
            html.push("<li><span class='label'>楼盘性质：</span><span class='value'>"+housePlan.get("NATURE_NAME")+"</span></li>");
            html.push("<li><span class='label'>交房时间：</span><span class='value'>"+housePlan.get("CHECK_DATE")+"</span></li>");
            html.push("<li><span class='label'>总户数：</span><span class='value'>"+housePlan.get("HOUSE_NUM")+"</span></li>");
            html.push("<li><span class='label'>发展时间：</span><span class='value'>"+housePlan.get("PLAN_IN_DATE")+"~"+housePlan.get("DESTROY_DATE")+"</span></li>");
            html.push("<li><span class='label'>当前状态：</span><span class='value'>"+housePlan.get("STATUS_NAME")+"</span></li>");
            $.insertHtml('beforeend', $("#HOUSE_CONTENT"), html.join(""));
        },

        drawCounselors : function(counselors, planCounselorNum){
            var html = [];
            var actualNum = 0;
            if(counselors != null && counselors.length > 0){
                actualNum = counselors.length;
            }
            html.push("责任家装顾问（计划分配:"+planCounselorNum+"人,实际分配："+actualNum+"人)");
            $.insertHtml('beforeend', $("#COUNSELOR_TITLE"), html.join(""));

            if(actualNum <= 0){
                return;
            }
            html = [];
            for(var i=0;i<actualNum;i++){
                var counselor = counselors.get(i);
                html.push("<li><div class=\"group link\"><div class=\"content\"><div class=\"pic\"><span class=\"e_pic-img-r\">");
                if(counselor.get("SEX") == "1"){
                    html.push("<img src=\"/frame/img/male.png\"  />")
                }
                else{
                    html.push("<img src=\"/frame/img/female.png\"  />")
                }
                html.push("</span></div>");
                html.push("<div class=\"main\"><div class=\"title\">");
                html.push(counselor.get("EMPLOYEE_NAME"));
                html.push("</div><div class=\"content\">");
                if(planCounselorNum > 1) {
                    html.push("负责楼栋：");
                    var towerNo = counselor.get("TOWER_NO");
                    var employeeHouseNum  = counselor.get("EMPLOYEE_HOUSE_NUM");
                    if(towerNo != null && towerNo != "undefined")
                        html.push(towerNo);
                    html.push("; 负责户数：");
                    if(employeeHouseNum != null && employeeHouseNum != "undefined")
                        html.push(employeeHouseNum);
                }
                html.push("</div></div></div></div>");
                html.push("</li>");
            }
            $.insertHtml('beforeend', $("#COUNSELOR_DETAIL"), html.join(""));

        },

        initAudit : function(){
            var houseId = $("#HOUSES_ID").val();
            $("#AUDIT_HOUSES_ID").val(houseId);
            $("#AUDIT_OPTION").val("1");
            showPopup('UI-popup','UI-popup-audit');
        },

        redirectToChange : function(){
            var houseId = $("#HOUSES_ID").val();
            $.redirect.open('redirectToChangeHousesPlan?HOUSES_ID='+houseId,'变更楼盘规划');
        },

        submitAudit : function(){
            if(!$.validate.verifyAll("auditArea")){
                return;
            }

            var parameter = $.buildJsonData("auditArea");
            $.ajaxPost('submitAudit', parameter, function (data) {
                MessageBox.success("审核楼盘规划成功","点击确定返回当前页，点击取消关闭当前页面", function(btn){
                    if("ok" == btn) {
                        document.location.reload();
                    }
                    else {
                        parent.$.index.closeCurrentPage();
                    }
                },{"cancel":"取消"})
            });
        }
    }});
})($);