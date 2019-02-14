(function($){
    $.extend({employee:{
            jobs : null,
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");


                window["orgTree"] = new Wade.Tree("orgTree");
                $("#orgTree").textAction(function(e, nodeData){
                    var id = nodeData.id;
                    var text = nodeData.text;
                    $("#ORG_TEXT").val(text);
                    $("#ORG_ID").val(id);

                    backPopup(document.getElementById('UI-ORG'));
                    return true;
                });

                $.ajaxPost('initExamQuery',null,function(data){
                    var trees = data.ORG_TREE;
                    var jobRoles = data.JOB_ROLES;
                    var citys = data.CITYS;
                    var defaultCityId = data.DEFAULT_CITY_ID;
                    var defaultCityName = data.DEFAULT_CITY_NAME;
                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();
                    }
                    $.employee.jobs = new $.DatasetList(jobRoles);
                    $.employee.drawJobRoles();
                    $.employee.drawCitys(new $.DatasetList(citys), defaultCityId, defaultCityName);
                });
            },

            query : function(){
                if($.validate.verifyAll("queryArea")) {
                    $.beginPageLoading();
                    var parameter = $.buildJsonData("queryArea");
                    $.ajaxPost('queryExamScore', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.employee.drawEmployees(datas);
                        hidePopup('UI-popup','UI-popup-query-cond');
                    });
                }
            },

            drawEmployees : function(datas){
                $.endPageLoading();
                $("#employees").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#queryMessage").css("display","");
                    $("#tip").css("display","none");
                    return;
                }

                $("#queryMessage").css("display","none");
                $("#tip").css("display","");

                var length = datas.length;

                $("#tip").empty();
                $.insertHtml('beforeend', $("#tip"), "共查询到"+length+"条数据");
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var sex = data.get("SEX");
                    var exam_id_1 = data.get("EXAM_ID_1");
                    var exam_id_2 = data.get("EXAM_ID_2");
                    var exam_id_3 = data.get("EXAM_ID_3");
                    var exam_id_4 = data.get("EXAM_ID_4");
                    var exam_id_5 = data.get("EXAM_ID_5");
                    html.push("<li class='link' ><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");

                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push("<span class=\"e_strong \">");
                    html.push(data.get("NAME"));
                    html.push("<span>");
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    var parentOrgName = data.get("PARENT_ORG_NAME");
                    if(parentOrgName != null && parentOrgName != "undefined")
                        html.push(parentOrgName + "-");
                    html.push(data.get("ORG_NAME"));
                   //html.push("归属城市："+data.get("CITY_NAME"));
                    html.push("</div><div class='content'>");
                    html.push("<ul>")
                    html.push("<li>")
                    html.push("<span class=\"label\">"+"通用知识："+"</span>")


                    if(exam_id_1 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_1 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_1")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_1")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"产品知识："+"</span>")
                    if(exam_id_2 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_2 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_2")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_2")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"鸿扬介绍："+"</span>")
                    if(exam_id_3 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_3 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_3")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_3")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"家装知识："+"</span>")
                    if(exam_id_4 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_4 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_4")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_4")+" 分"+"</span>")
                    }
                    html.push("</li>")

                    html.push("<li>")
                    html.push("<span class=\"label\">"+"客户服务："+"</span>")
                    if(exam_id_5 == null ){
                        html.push("<span class=\"value e_red\">"+"未考试"+"</span>")
                    }
                    else if(exam_id_5 < 80 ){
                        html.push("<span class=\"value e_red\">"+data.get("EXAM_ID_5")+" 分"+"</span>")
                    }
                    else {
                        html.push("<span class=\"value \">"+data.get("EXAM_ID_5")+" 分"+"</span>")
                    }                    html.push("</li>")

                    html.push("</ul>")

                    html.push("</div></div>")

                }

                $.insertHtml('beforeend', $("#employees"), html.join(""));
            },

            changeEmployee : function(employeeId, name){
                $.redirect.open('redirectChangeEmployee?EMPLOYEE_ID='+employeeId,'修改'+name+'员工档案');
            },

            queryParent : function(){
                if($.validate.verifyAll("searchArea")) {
                    var parameter = $.buildJsonData("searchArea");
                    $.ajaxPost('queryContacts', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.employee.drawParentEmployees(datas);
                    });
                }
            },

            drawParentEmployees : function(datas){
                $("#parent_employees").empty();
                var html = [];

                if(datas == null || datas.length <= 0){
                    $("#messagebox").css("display","");
                    return;
                }

                $("#messagebox").css("display","none");


                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    var sex = data.get("SEX");
                    html.push("<li class='link' ontap=\"$.employee.selectEmployee(\'"+data.get("EMPLOYEE_ID")+"\',\'"+data.get("NAME")+"\')\"><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
                    if(sex == "1")
                        html.push("<img src=\"/frame/img/male.png\" class='e_pic-r' style='width:4em;height:4em'/>");
                    else
                        html.push("<img src=\"/frame/img/female.png\" class='e_pic-r' style='width:4em;height:4em'/>");

                    html.push("</div></div>");
                    html.push("<div class=\"main\"><div class=\"title\">");
                    html.push(data.get("NAME"));
                    html.push("</div>");
                    html.push("<div class=\"content\">");
                    var parentOrgName = data.get("PARENT_ORG_NAME");
                    if(parentOrgName != null && parentOrgName != "undefined")
                        html.push(parentOrgName + "-");
                    html.push(data.get("ORG_NAME"));
                    html.push("</div><div class='content'>"+data.get("JOB_ROLE_NAME"));
                    html.push("</div></div>")
                    html.push("<div class=\"side e_size-m\">"+data.get("CONTACT_NO")+"</div>");
                    html.push("</div></div></li>");
                }

                $.insertHtml('beforeend', $("#parent_employees"), html.join(""));
            },

            selectEmployee : function(employeeId, name){
                $("#PARENT_EMPLOYEE_ID").val(employeeId);
                $("#PARENT_EMPLOYEE_NAME").val(name);
                backPopup(document.getElementById('UI-PARENT'));
            },

            drawCitys : function(citys, defaultCityId, defaultCityName){
                if(citys != null){
                    var length = citys.length;
                    var html=[];
                    for(var i=0;i<length;i++){
                        var city = citys.get(i);
                        html.push("<li class=\"link e_center\" ontap=\"$.employee.afterSelectCity(\'"+city.get("CODE_VALUE")+"\',\'"+city.get("CODE_NAME")+"\')\"><div class=\"main\">"+city.get("CODE_NAME")+"</div></li>");
                    }
                    $.insertHtml('beforeend', $("#citys"), html.join(""));

                    if(defaultCityId != null && defaultCityId != "undefined"){
                        $.employee.afterSelectCity(defaultCityId,defaultCityName);
                    }
                }
            },

            afterSelectCity : function(cityId, cityName){
                $("#CITY_TEXT").val(cityName);
                $("#CITY").val(cityId);
                backPopup(document.getElementById('UI-CITY'));
            },

            drawJobRoles : function(){
                if(this.jobs == null || this.jobs == "undefined")
                    return;

                var length = this.jobs.length;
                var html = [];
                $("#jobRoles").empty();
                for(var i=0;i<length;i++){
                    var jobRole = this.jobs.get(i);
                    html.push("<li class=\"link e_center\" ontap=\"$.employee.afterSelectJob(\'"+jobRole.get("CODE_VALUE")+"\',\'"+jobRole.get("CODE_NAME")+"\')\"><label class=\"group\" ><div class=\"main\">"+jobRole.get("CODE_NAME")+"</div></label></li>");
                }
                $.insertHtml('beforeend', $("#jobRoles"), html.join(""));
            },

            afterSelectJob : function(value, name){
                $("#JOB_TEXT").val(name);
                $("#JOB_ROLE").val(value);
                backPopup(document.getElementById('UI-JOB'));
            },

            queryJobs : function(){
                var searchText = $("#JOB_SEARCH_TEXT").val();
                $("#jobRoles").empty();
                var length = this.jobs.length;
                var html = [];
                for(var i=0;i<length;i++){
                    var jobRole = this.jobs.get(i);
                    var name = jobRole.get("CODE_NAME");
                    if(name.indexOf(searchText) >= 0 || searchText == "")
                        html.push("<li class=\"link e_center\" ontap=\"$.employee.afterSelectJob(\'"+jobRole.get("CODE_VALUE")+"\',\'"+jobRole.get("CODE_NAME")+"\')\"><label class=\"group\" ><div class=\"main\">"+jobRole.get("CODE_NAME")+"</div></label></li>");
                }
                $.insertHtml('beforeend', $("#jobRoles"), html.join(""));
            }
        }});
})($);