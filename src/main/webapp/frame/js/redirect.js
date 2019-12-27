(function($){
    $.extend({
        redirect:{
            open : function(url, title){
                if($.os.phone){
                    // window.location.href = url;
                    this.popupPageByUrl(title, url);
                }
                else{
                    try {
                        top.$.index.openNav(url, title);
                    } catch(err) {
                        try {
                            top.layui.index.openTabsPage(url, title);
                        } catch (error) {

                        }
                    }

                }
            },
            topOpen : function(url, title){
                if($.os.phone){
                    window.top.location.href = url;
                }

            },
            closeCurrentPage : function () {
                if($.os.phone){
                    if(self == top) {
                        back();
                    } else {
                        backPopup(this);
                    }

                }
                else{
                    top.$.index.closeCurrentPage();
                }
            },
            popupPageByUrl: function(title, url, className, afterAction, hideAction, srcWindow){

                //判断父窗口
                if($.isSameDomain(parent) && !$.isSamePage(parent) && true !== parent.StopPopupRecursion && parent.Wade && ( parent.Wade.Popup || parent.Wade.Dialog || parent.Wade.Tabset )){
                    var popupId, tabsetId, frame = $.getParentIframe();
                    if(frame && (
                            ( (popupId = $.attr(frame, "popupId")) && parent[popupId] && ( (parent.Wade.Popup && parent[popupId] instanceof parent.Wade.Popup) || (parent.Wade.Dialog && parent[popupId] instanceof parent.Wade.Dialog) ) )
                            || ( (tabsetId = $.attr(frame, "tabsetId")) && parent[tabsetId] && parent.Wade.Tabset && parent[tabsetId] instanceof parent.Wade.Tabset )
                        ) ){
                        parent.$.redirect.popupPageByUrl(title, url, className, afterAction, hideAction, srcWindow);
                        return;
                    }
                }

                if(!title || !$.isString(title))
                    return;

                if(!$.Popup){ //引入popup组件js
                    window.includeScript($.UI_POPUP_JS, true, true);
                }

                if(!$.Frame){ //引入frame组件js
                    window.includeScript($.UI_FRAME_JS, true, true);
                }

                //处理参数
                if($.isFunction(className)){
                    hideAction = afterAction;
                    afterAction = className;
                    className = null;
                }

                title = $.xss(title);

                var path = url.split('?')[0];
                var guid = $.md5("popup_" + path), popup;
                if( ( popup = window["popup_" + guid] ) ){
                    // if(true !== popup.visible){
                        var reset = window["frame_" + guid].forceResetByUrl(url, title);
                        popup.show(1, reset);
                    // }
                }else{
                    //简化样式入参
                    if(!className) className = "c_popup";
                    else if("half" == className) className = "c_popup c_popup-half";
                    else if("full" == className) className = "c_popup c_popup-full";
                    else if("wide" == className) className = "c_popup c_popup-wide";

                    //构造popup和frame的html
                    var html = [];
                    html.push('<div id="popup_' + guid + '" class="' + className + '">');
                    html.push('<div class="c_popupBg"></div>');
                    html.push('<div class="c_popupBox">');
                    html.push('<div class="c_popupWrapper">');
                    html.push('<div id="popup_' + guid + '_group1" class="c_popupGroup" level="1">'); //必须加上level属性
                    html.push('<div class="c_popupItem">');

                    html.push('<iframe id="frame_' + guid + '" style="width:100%;height:100%;display:none" frameborder="0"></iframe>');
                    html.push('<div class="c_msg c_msg-full c_msg-loading">');
                    html.push('<div class="wrapper">');
                    html.push('<div class="emote"></div>');
                    html.push('<div class="info"><div class="text"><div class="title">loading</div></div></div>');
                    html.push('</div>');
                    html.push('</div>');

                    html.push('</div>');
                    html.push('</div>');
                    html.push('</div>');
                    html.push('</div>');

                    //插入到body下
                    $(document.body).append(html.join(""));
                    html = null;

                    //绑定事件
                    if(afterAction){
                        if($.isFunction(afterAction)){
                            $("#popup_" + guid).afterAction(afterAction);
                        }else if($.isString(afterAction)){
                            $("#popup_" + guid).attr("onafterAction", afterAction);
                        }
                    }
                    if(hideAction){
                        if($.isFunction(hideAction)){
                            $("#popup_" + guid).hideAction(hideAction);
                        }else if($.isString(hideAction)){
                            $("#popup_" + guid).attr("onhideAction", hideAction);
                        }
                    }

                    window["frame_" + guid] = new Wade.Frame("frame_" + guid, {autoInit:false, title:title, src:url});
                    popup = window["popup_" + guid] = new Wade.Popup("popup_" + guid, {visible:false, mask:false, srcWindow: srcWindow});
                    popup.show(1);
                }

                return popup;
            },
        },


    });
})($);