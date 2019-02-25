(function($){
    $.extend({employee:{
            jobs : null,
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

                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "educationcontainer",
                    // 参数设置
                    {
                        id:"EDUCATION",
                        name:"EDUCATION",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"本科", VALUE:"1"},
                        {TEXT:"硕士", VALUE:"2"},
                        {TEXT:"博士", VALUE:"3"},
                        {TEXT:"专科", VALUE:"4"},
                        {TEXT:"高中", VALUE:"5"},
                        {TEXT:"初中", VALUE:"6"}
                    ]
                );

                window["orgTree"] = new Wade.Tree("orgTree");
                $("#orgTree").textAction(function(e, nodeData){
                    var hasChild = nodeData.haschild;
                    if(hasChild == "true")
                        return true;

                    var id = nodeData.id;
                    var text = nodeData.text;
                    $("#ORG_TEXT").val(text);
                    $("#ORG_ID").val(id);
                    hidePopup('UI-popup','UI-ORG');
                    return true;
                });

                $("#SEX").bind("change", function(){
                    $("#SEX").val(this.value); // 当前值
                });

                $("#SEX").val("1");

                window["IN_DATE"] = new Wade.DateField(
                    "IN_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false
                    }
                );

                window["REGULAR_DATE"] = new Wade.DateField(
                    "REGULAR_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false
                    }
                );

                $.ajaxPost('initCreateEmployee',null,function(data){
                    var trees = data.ORG_TREE;
                    var today = data.TODAY;
                    var jobRoles = data.JOB_ROLES;
                    var citys = data.CITYS;
                    var defaultCityId = data.DEFAULT_CITY_ID;
                    var defaultCityName = data.DEFAULT_CITY_NAME;
                    if(trees != null){
                        window["orgTree"].data = trees;
                        window["orgTree"].init();
                    }
                    $("#IN_DATE").val(today);
                    $.employee.jobs = new $.DatasetList(jobRoles);
                    $.employee.drawJobRoles();
                    $.employee.drawCitys(new $.DatasetList(citys), defaultCityId, defaultCityName);
                });
            },

            query : function(){
                if($.validate.verifyAll("searchArea")) {
                    var parameter = $.buildJsonData("searchArea");
                    $.ajaxPost('queryContacts', parameter, function (data) {
                        var rst = new Wade.DataMap(data);
                        var datas = rst.get("DATAS");
                        $.employee.drawEmployees(datas);
                    });
                }
            },

            drawEmployees : function(datas){
                $("#employees").empty();
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

                $.insertHtml('beforeend', $("#employees"), html.join(""));
            },

            selectEmployee : function(employeeId, name){
                $("#PARENT_EMPLOYEE_ID").val(employeeId);
                $("#PARENT_EMPLOYEE_NAME").val(name);
                hidePopup('UI-popup','UI-PARENT');
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
                hidePopup('UI-popup','UI-CITY');
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
                hidePopup('UI-popup','UI-JOB');
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
            },

            submit : function(){
                if($.validate.verifyAll("submitArea")) {
                    var parameter = $.buildJsonData("submitArea");
                    $.beginPageLoading();
                    $.ajaxPost('createEmployee', parameter, function (data) {
                        $.endPageLoading();
                        MessageBox.success("新增员工成功","点击确定返回新增页面，点击取消关闭当前页面", function(btn){
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