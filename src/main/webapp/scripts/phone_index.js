(function($){
    $.extend({index:{
            init : function(){
                $.ajaxPost('initUser',null,function(data){
                    var userInfo = new Wade.DataMap(data);
                    var name = userInfo.get("NAME");
                    var orgName = userInfo.get("ORG_NAME");
                    var jobRoleName = userInfo.get("JOB_ROLE_NAME");
                    var headImage = userInfo.get("HEAD_IMAGE");

                    $.insertHtml('beforeend',$("#USER_NAME"),name);
                    $.insertHtml('beforeend',$("#ORG_NAME"),orgName);
                    $.insertHtml('beforeend',$("#JOB_ROLE_NAME"),jobRoleName);
                    $("#HEAD_IMAGE").attr("src", headImage);

                },function(){
                    alert('error');
                });
            },


        }});
})($);

(function($){
    $.menus = {
        init:function(){
            $.ajaxPost("initMenu", null, function(data){
                var menus = new Wade.DatasetList(data);
                $.menus.draw(menus);
            },function(){

            })
        },

        draw : function(menus){
            if(menus == null || typeof(menus) == 'undefined'){
                return;
            }

            var length = menus.length;
            var html=[];
            for(var i=0;i<length;i++){
                var menu = menus.get(i);
                html.push("<div class='c_list'>");
                html.push("<ul><li><div class='content'><div class='pic'></div><div class='main'><div class='title e_strong'>");
                html.push(menu.get("TITLE"));
                html.push("</div>");
                html.push("</div>");
                html.push("</div>");
                html.push("</li></ul></div>");

                var subMenus = menu.get("SUB_MENU");

                if(subMenus == null || subMenus == 'undefined' || subMenus.length <= 0){
                    continue;
                }

                html.push("<div class='c_list c_list-v c_list-col-2'>");
                html.push("<ul>");
                var subMenuLength = subMenus.length;
                for(var j=0;j<subMenuLength;j++){
                    var subMenu = subMenus.get(j);
                    html.push("<li class='link' onclick=\"$.menus.openNav(\'"+subMenu.get("MENU_URL")+"\')\">");
                    html.push("<div class='pic'><img src='"+subMenu.get("ICO_URL")+"'/></div>");
                    html.push("<div class='main'>");
                    html.push("<div class='content content-row-2'>"+subMenu.get("TITLE")+"</div>");
                    html.push("</div>");
                    html.push("</li>");
                }
                html.push("</ul>");
                html.push("</div>");

                html.push("<div class=\"c_line c_line-dashed\"></div>");
            }
            $.insertHtml('beforeend',$("#menus"),html.join(""));
        },

        openNav : function(url){
            window.location.href = url;
        }
    };

    $(function(){
        $.menus.init();
    });
})(Wade);