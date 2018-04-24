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
                html.push("<div class='c_title'>");
                html.push("<div class='text'>");
                html.push(menu.get("TITLE"));
                html.push("</div>");
                html.push("</div>");

                var subMenus = menu.get("SUB_MENU");

                if(subMenus == null || subMenus == 'undefined' || subMenus.length <= 0){
                    continue;
                }

                html.push("<div class='c_list c_list-border c_list-line c_list-col-6 c_list-v c_list-pic-s'>");
                html.push("<ul>");
                var subMenuLength = subMenus.length;
                for(var j=0;j<subMenuLength;j++){
                    var subMenu = subMenus.get(j);
                    html.push("<li class='link' onclick=\"parent.$.index.openNav(\'"+subMenu.get("MENU_URL")+"\',\'"+subMenu.get("TITLE")+"\')\">");
                    html.push("<div class='pic'><img src='"+subMenu.get("ICO_URL")+"'/></div>");
                    html.push("<div class='main'>");
                    html.push("<div class='content content-row-2'>"+subMenu.get("TITLE")+"</div>");
                    html.push("</div>");
                }
            }
            $.insertHtml('beforeend',$("#menus"),html.join(""));
        }
    };

    $(function(){
        $.menus.init();
    });
})(Wade);