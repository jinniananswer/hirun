(function($){
    $.extend({user:{
            restFuncs : null,
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.beginPageLoading();
                $.ajaxPost('queryEmployeeFuncs','&EMPLOYEE_ID='+$('#EMPLOYEE_ID').val(),function(data){
                    var rst = $.DataMap(data);
                    var employee = rst.get("EMPLOYEE");
                    var userFuncs = rst.get("USER_FUNCS");
                    var restFuncs = rst.get("REST_FUNCS");
                    $.user.drawEmployee(employee);
                    $.user.drawFuncs(userFuncs);
                    $.user.drawRestFuncs(restFuncs);
                    $.endPageLoading();
                });
            },


            drawEmployee : function(data){
                $("#employee").empty();
                var html = [];

                var sex = data.get("SEX");
                html.push("<li class='link'><div class=\"group\"><div class=\"content\"><div class='l_padding'><div class=\"pic pic-middle\">");
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


                $.insertHtml('beforeend', $("#employee"), html.join(""));
            },

            drawFuncs : function(datas){
                if(datas == null || datas == "undefined" || datas.length <= 0)
                    return;

                $("#user_funcs").empty();
                var html = [];
                var size = datas.length;
                for(var i=0;i<size;i++){
                    var data = datas.get(i);
                    html.push("<li ontap=\"$.user.clickFunc(this,"+data.get("FUNC_ID")+")\">");
                    html.push("<label class='group link'>");
                    html.push("<div class='main'>");
                    html.push(data.get("FUNC_DESC")+"["+data.get("TYPE_NAME")+"]");
                    html.push("</div>");
                    html.push("<div class='fn'>");
                    html.push("<input type='checkbox' checked name='func' id='FUNC_"+data.get("FUNC_ID")+"' value='"+data.get("FUNC_ID")+"'/>");
                    html.push("</div>");
                    html.push("</label>");
                    html.push("</li>");
                }

                $.insertHtml("beforeend", $("#user_funcs"), html.join(""));
            },

            drawRestFuncs : function(datas){
                if(datas == null || datas == "undefined" || datas.length <= 0)
                    return;

                this.restFuncs = datas;
                $("#rest_funcs").empty();
                var html = [];
                var size = datas.length;
                var funcIds = '';
                for(var i=0;i<size;i++){
                    var data = datas.get(i);
                    html.push("<li ontap=\"$.user.clickRestFunc(this)\" funcId='"+data.get("FUNC_ID")+"' typeName='"+data.get("TYPE_NAME")+"' funcDesc='"+data.get("FUNC_DESC")+"'>");
                    html.push("<label class='group link'>");
                    html.push("<div class='main'>");
                    html.push(data.get("FUNC_DESC")+"["+data.get("TYPE_NAME")+"]");
                    html.push("</div>");
                    html.push("<div class='fn'>");
                    html.push("</div>");
                    html.push("</label>");
                    html.push("</li>");
                }
                $.insertHtml("beforeend", $("#rest_funcs"), html.join(""));
            },

            clickFunc : function(obj, funcId){
                var existsItem = $(obj);
                var existsFunc = $("#FUNC_"+funcId);
                var isAdd = existsItem.attr("tag");
                if(existsFunc.attr("checked")){
                    if(isAdd == "ADD"){
                        var addFuncs = $("#ADD_FUNCS").val();
                        if(addFuncs.length == 0)
                            addFuncs = funcId;
                        else
                            addFuncs += ","+funcId;
                        $("#ADD_FUNCS").val(addFuncs);
                    }
                    else{
                        var delFuncs = $("#DEL_FUNCS").val();
                        delFuncs = (delFuncs + ",").replace(funcId + ",", "");
                        if(delFuncs.length > 0 && delFuncs.charAt(delFuncs.length -1) == ',')
                            delFuncs = delFuncs.substring(0, delFuncs.length - 1);

                        $("#DEL_FUNCS").val(delFuncs);
                    }
                }
                else{
                    var delFuncs = $("#DEL_FUNCS").val();
                    if(isAdd == 'ADD'){
                        var addFuncs = $("#ADD_FUNCS").val();
                        addFuncs = (addFuncs + ",").replace(funcId + ",", "");
                        if(addFuncs.length > 0 && addFuncs.charAt(addFuncs.length - 1) == ',')
                            addFuncs = addFuncs.substring(0, addFuncs.length - 1);

                        $("#ADD_FUNCS").val(addFuncs);
                    }
                    else {
                        if (delFuncs.length == 0)
                            delFuncs = funcId;
                        else
                            delFuncs += "," + funcId;

                        $("#DEL_FUNCS").val(delFuncs);
                    }
                }
            },

            clickRestFunc : function(obj, funcId){
                var style = $(obj).attr("class");
                if(style == '') {
                    $(obj).addClass("checked");
                }
                else{
                    $(obj).removeClass("checked");
                }
            },

            confirmAdd : function(){
                var addFuncs = $("#rest_funcs li");
                if(addFuncs == null || addFuncs.length <= 0){
                    hidePopup('UI-popup','UI-popup-rest-func');
                    return;
                }

                var oldAddFuncs = $("li[tag=ADD]");
                if(oldAddFuncs != null && oldAddFuncs.length > 0){
                    var oldSize = oldAddFuncs.length;
                    for(var i=0;i<oldSize;i++){
                        var oldAddFunc = $(oldAddFuncs[i]);
                        oldAddFunc.remove();
                    }
                }

                var addFuncValue = '';
                var size = addFuncs.length;
                var html = [];
                for(var i=0;i<size;i++){
                    var addFunc = $(addFuncs[i]);
                    if(addFunc.attr("class") == '')
                        continue;
                    addFuncValue += addFunc.attr("funcId") + ",";
                    html.push("<li ontap=\"$.user.clickFunc(this,"+addFunc.attr("funcId")+")\" tag='ADD'>");
                    html.push("<label class='group link'>");
                    html.push("<div class='main'><span class='e_blue'>");
                    html.push(addFunc.attr("funcDesc")+"["+addFunc.attr("typeName")+"]");
                    html.push("</span></div>");
                    html.push("<div class='fn'>");
                    html.push("<input type='checkbox' checked name='func' id='FUNC_"+addFunc.attr("funcId")+"' value='"+addFunc.attr("funcId")+"'/>");
                    html.push("</div>");
                    html.push("</label>");
                    html.push("</li>");
                }
                $.insertHtml('beforeend', $("#user_funcs"), html.join(""));
                addFuncValue = addFuncValue.substring(0, addFuncValue.length - 1);
                $("#ADD_FUNCS").val(addFuncValue);
                hidePopup('UI-popup','UI-popup-rest-func');
            },

            initAddFunc : function(){
                var addFuncs = $("#rest_funcs li");
                var addFuncValue = $("#ADD_FUNCS").val();
                if(addFuncs != null && addFuncs.length > 0){
                    var size = addFuncs.length;
                    for(var i=0;i<size;i++){
                        var addFunc = $(addFuncs[i]);
                        var funcId = addFunc.attr("funcId");
                        if((","+addFuncValue+",").indexOf(","+funcId+",") >= 0)
                            addFunc.addClass("checked");
                        else
                            addFunc.removeClass("checked");
                    }
                }
                showPopup('UI-popup','UI-popup-rest-func');
            },

            queryRestFuncs : function(){
                var searchText = $("#SEARCH_TEXT").val();
                if(this.restFuncs == null || this.restFuncs.length <= 0)
                    return;
                var length = this.restFuncs.length;
                var html = [];
                $("#rest_funcs").empty();
                for(var i=0;i<length;i++){
                    var data = this.restFuncs.get(i);
                    var name = data.get("FUNC_DESC");
                    if(name.indexOf(searchText) >= 0 || searchText == ""){
                        html.push("<li ontap=\"$.user.clickRestFunc(this)\" funcId='"+data.get("FUNC_ID")+"' typeName='"+data.get("TYPE_NAME")+"' funcDesc='"+data.get("FUNC_DESC")+"'>");
                        html.push("<label class='group link'>");
                        html.push("<div class='main'>");
                        html.push(data.get("FUNC_DESC")+"["+data.get("TYPE_NAME")+"]");
                        html.push("</div>");
                        html.push("<div class='fn'>");
                        html.push("</div>");
                        html.push("</label>");
                        html.push("</li>");
                    }
                }
                $.insertHtml('beforeend', $("#rest_funcs"), html.join(""));
            },

            submit : function(){
                var addFuncs = $("#ADD_FUNCS").val();
                var delFuncs = $("#DEL_FUNCS").val();

                if(addFuncs.length == 0 && delFuncs.length == 0){
                    MessageBox.alert("提示信息", "亲，您没有做任何操作，不能提交哦");
                    return;
                }
                var userId = $("#USER_ID").val();
                $.beginPageLoading();
                $.ajaxPost('assignPermission','&USER_ID='+userId+"&ADD_FUNCS="+addFuncs+"&DEL_FUNCS="+delFuncs,function(data){
                    $.endPageLoading();
                    MessageBox.success("员工权限分配成功","点击确定返回页面，点击取消关闭当前页面", function(btn){
                        $.redirect.closeCurrentPage();
                    });
                });

            }
        }});
})($);