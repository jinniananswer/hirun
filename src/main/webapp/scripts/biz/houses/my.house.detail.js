(function($){
    $.extend({myHouse:{
            init : function(){
                $.ajaxPost('showMyHouseDetail','&HOUSES_ID='+$("#HOUSES_ID").val(),function(data){
                    var rst = new Wade.DataMap(data);
                    var house = rst.get("HOUSE");
                    var customers = rst.get("CUSTOMERS");
                    $.myHouse.drawHouseContent(house);
                    $.myHouse.drawCustomers(customers);
                });
            },

            drawHouseContent : function(house){
                var html = [];
                html.push(house.get("NAME"));
                $.insertHtml('beforeend', $("#HOUSE_NAME"), html.join(""));
                html = [];
                html.push("<li><span class='label'>归属城市：</span><span class='value'>"+house.get("CITY_NAME")+"</span></li>");
                html.push("<li><span class='label'>归属区域：</span><span class='value'>"+house.get("AREA_NAME")+"</span></li>");
                html.push("<li><span class='label'>责任店面：</span><span class='value'>"+house.get("ORG_NAME")+"</span></li>");
                html.push("<li><span class='label'>楼盘性质：</span><span class='value'>"+house.get("NATURE_NAME")+"</span></li>");
                html.push("<li><span class='label'>交房时间：</span><span class='value'>"+house.get("CHECK_DATE")+"</span></li>");
                html.push("<li><span class='label'>总户数：</span><span class='value'>"+house.get("HOUSE_NUM")+"</span></li>");
                html.push("<li><span class='label'>发展时间：</span><span class='value'>"+house.get("PLAN_IN_DATE")+"~"+house.get("DESTROY_DATE")+"</span></li>");
                html.push("<li><span class='label'>当前状态：</span><span class='value'>"+house.get("STATUS_NAME")+"</span></li>");
                html.push("<li><span class='label e_red'>我负责的楼栋：</span><span class='value e_red'>");
                var towerNo = house.get("TOWER_NO");
                if(towerNo !=null && towerNo != "undefined")
                    html.push(towerNo);
                html.push("</span></li>");
                var employeeHouseNum = house.get("EMPLOYEE_HOUSE_NUM");
                html.push("<li><span class='label e_red'>我负责的户数：</span><span class='value e_red'>");
                if(employeeHouseNum !=null && employeeHouseNum != "undefined")
                    html.push(employeeHouseNum);
                html.push("</span></li>");
                $.insertHtml('beforeend', $("#HOUSE_CONTENT"), html.join(""));
            },

            drawCustomers : function(customers){
                var html = [];

                html.push("我的客户");
                $.insertHtml('beforeend', $("#CUSTOMER_TITLE"), html.join(""));

                if(customers == null || customers.length <= 0){
                    html = [];
                    html.push("<div class=\"group link\"><div class=\"content\"><span class=\"e_ico-pic-red e_ico-pic-r e_ico-pic-xs\">无</span></div></div>");
                    $.insertHtml('beforeend', $("#CUSTOMER_DETAIL"), html.join(""));
                    return;
                }
                html = [];
                for(var i=0;i<customers.length;i++){
                    var customer = customers.get(i);
                    html.push("<li><div class=\"group link\"><div class=\"content\"><div class=\"pic\"><span class=\"e_pic-img-r\">");
                    if(customer.get("SEX") == "1"){
                        html.push("<img src=\"/frame/img/male.png\"  />")
                    }
                    else{
                        html.push("<img src=\"/frame/img/female.png\"  />")
                    }
                    html.push("</span></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(customer.get("CUST_NAME"));
                    html.push("</div><div class=\"content content-row-3\">");
                    html.push("联系电话：");
                    var mobile_no = customer.get("MOBILE_NO");
                    var houseDetail  = customer.get("HOUSE_DETAIL");
                    if(mobile_no != null && mobile_no != "undefined")
                        html.push("<a href='tel:'"+mobile_no+"'>"+mobile_no+"</a>");
                    html.push("; 楼栋位置：");
                    if(houseDetail != null && houseDetail != "undefined")
                        html.push(houseDetail);

                    html.push("</div></div></div></div>");
                    html.push("</li>");
                }
                $.insertHtml('beforeend', $("#CUSTOMER_DETAIL"), html.join(""));

            }
        }});
})($);