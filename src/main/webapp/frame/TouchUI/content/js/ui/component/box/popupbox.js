/*!
 * PopupBox
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2015, WADE
 */
(function($, window, doc){
    "use strict";

    if( !$ || typeof $.PopupBox != "undefined")
        return;

    var push = Array.prototype.push,
        splice = Array.prototype.splice;

    var hasTouch     = typeof $.hasTouch != "undefined" ? $.hasTouch : "ontouchstart" in window;
    var START_EVENT  = hasTouch ? 'touchstart': 'mousedown';

    var popupIframe = null; //popup组件对象所在的iframe

    var floatLayers = [];
    var floatLayerGUID = 0;

    /*
     获取css样式表中的样式
     */
    function getStyle(obj, attr){
        if(!obj || !obj.nodeType || !attr)
            return;

        if(obj.currentStyle){
            return obj.currentStyle[attr];
        }else{
            return doc.defaultView.getComputedStyle(obj,false)[attr];
        }
    }

    //从父窗口中查找弹窗组件
    var _findParentPopup = function(){
        var popup, frame = $.getParentIframe();
        if(frame && parent.Wade && parent.Wade.PopupBox){
            popup = parent.Wade.PopupBox.findPopup(frame);
        }

        if(popup){
            popupIframe = frame;
        }
        return popup;
    }

    var	_findPopupByNode = function(node){

        if(!node) return;

        var hasPopup = $.Popup && $.isFunction($.Popup) && $.Popup.prototype._init;
        var hasDialog = $.Dialog && $.isFunction($.Dialog) && $.Dialog.prototype._init;

        if( $.isString(node) && window[node] && (
                ( hasPopup && window[node] instanceof $.Popup ) ||
                ( hasDialog && window[node] instanceof $.Dialog )
            ) ){
            return window[node];
        }

        if(!node.nodeType)
            return;

        var n = 0, id, p = node.parentNode, className;
        while(n < 50 && p && p.nodeType && p != doc.body){  //最大查找50层节点
            if($.nodeName(p, "div")
                && ( className = (" " + p.className + " ") ) && ( className.indexOf(" c_popup ") > -1 || className.indexOf(" c_dialog ") > -1)
                && ( id = $.attr(p, "id") ) ){

                if(window[id] && (
                        ( hasPopup && window[id] instanceof $.Popup ) ||
                        ( hasDialog && window[id] instanceof $.Dialog )
                    )){
                    return window[id];
                }
            }
            p = p.parentNode;
            n ++;
        }
    };

    var _findPopup = function(node){
        var popup; popupIframe = null; //置空 popupIframe
        if( !( popup = _findPopupByNode(node) ) ){
            return  _findParentPopup();
        }
        return popup;
    }

    var PopupBox = {
        findPopup: function(node){
            return _findPopup(node);
        },
        showPopup: function(node, item, reset){
            var popup = _findPopup(node);
            if(popup){
                popup.show(item, reset);
            }
        },
        hidePopup: function(node){
            var popup = _findPopup(node);
            if(popup){
                popup.hide();
            }
        },
        forwardPopup: function(node, arg1, page, listener, params, subsys){ //根据参数个数判断是那种方式 forwardPopup(node, title, page, listener, params)
            var popup = _findPopup(node);
            if(popup){
                if(arguments.length < 3){
                    //popupIframe 不为空，则Popup组件对象位于父窗口， 替换forward入参元素
                    popup.forward(popupIframe ? popupIframe : node, arg1); //popup.forward(node, item);
                }else{
                    var title = arg1;
                    if(!node || !node.nodeType || !title || !$.isString(title) || !page || !$.isString(page))
                        return;

                    if(!$.Frame){ //有可能存在未加载Frame脚本的情况，需要检测并引入frame组件js
                        window.includeScript($.UI_FRAME_JS, true, true);
                    }

                    var itemId = popup.append(popupIframe ? popupIframe : node, title); //返回itemId
                    var frameId = "frame_" + itemId;  //约定frameId为 'frame_' + itemId

                    popup.getFrame(frameId).reset(page, listener, params, subsys, title);
                    popup.forward(popupIframe ? popupIframe : node, itemId);

                    itemId = frameId = null;
                }
            }
        },
        backPopup: function(node, item, el, value){ //支持返回时带回数据到上一个界面
            var popup = _findPopup(node);
            if(popup){
                popup.back(popupIframe ? popupIframe : node, item, el, value);
                return true;
            }
            return false;
        },
        popupPage: function(title, page, listener, params, subsys, className, afterAction, hideAction, srcWindow){

            //判断父窗口
            if($.isSameDomain(parent) && !$.isSamePage(parent) && true !== parent.StopPopupRecursion && parent.Wade && ( parent.Wade.Popup || parent.Wade.Dialog || parent.Wade.Tabset )){
                var popupId, tabsetId, frame = $.getParentIframe();
                if(frame && (
                        ( (popupId = $.attr(frame, "popupId")) && parent[popupId] && ( (parent.Wade.Popup && parent[popupId] instanceof parent.Wade.Popup) || (parent.Wade.Dialog && parent[popupId] instanceof parent.Wade.Dialog) ) )
                        || ( (tabsetId = $.attr(frame, "tabsetId")) && parent[tabsetId] && parent.Wade.Tabset && parent[tabsetId] instanceof parent.Wade.Tabset )
                    ) ){
                    parent.popupPage(title, page, listener, params, subsys, className, afterAction, hideAction, window);
                    return;
                }
            }

            if(!title || !$.isString(title) || !page || !$.isString(page))
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

            var guid = $.md5("popup_" + title), popup;
            if( ( popup = window["popup_" + guid] ) ){
                if(true !== popup.visible){
                    var reset = window["frame_" + guid].reset(page, listener, params, subsys, title);
                    popup.show(1, reset);
                }
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
                $(doc.body).append(html.join(""));
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

                window["frame_" + guid] = new Wade.Frame("frame_" + guid, {autoInit:false, title:title, page:page, listener:listener, params:params, subsys:subsys});
                popup = window["popup_" + guid] = new Wade.Popup("popup_" + guid, {visible:false, mask:true, srcWindow: srcWindow});
                popup.show(1);
            }

            return popup;
        },
        popupDialog: function(title, page, listener, params, subsys, width, height, closeable, afterAction, hideAction, srcWindow){

            //预处理参数
            if($.isNumeric(subsys)){
                hideAction = afterAction;
                afterAction = closeable;
                closeable = height;
                height = width;
                width = subsys;
                subsys = null;
            }

            //预处理参数
            if($.isFunction(closeable)){
                afterAction = closeable;
                hideAction = afterAction;
                closeable = true;
            }

            //判断父窗口
            if($.isSameDomain(parent) && !$.isSamePage(parent) && true !== parent.StopPopupRecursion && parent.Wade && ( parent.Wade.Popup || parent.Wade.Dialog || parent.Wade.Tabset )){
                var popupId, tabsetId, frame = $.getParentIframe();
                if(frame && (
                        ( (popupId = $.attr(frame, "popupId")) && parent[popupId] && ( (parent.Wade.Popup && parent[popupId] instanceof parent.Wade.Popup) || (parent.Wade.Dialog && parent[popupId] instanceof parent.Wade.Dialog) ) )
                        || ( (tabsetId = $.attr(frame, "tabsetId")) && parent[tabsetId] && parent.Wade.Tabset && parent[tabsetId] instanceof parent.Wade.Tabset )
                    ) ){
                    parent.popupDialog(title, page, listener, params, subsys, width, height, closeable, afterAction, hideAction, window);
                    return;
                }
            }

            if(!title || !$.isString(title) || !page || !$.isString(page))
                return;

            if(!$.Dialog){ //引入popup组件js
                window.includeScript($.UI_DIALOG_JS, true, true);
            }

            if(!$.Frame){ //引入frame组件js
                window.includeScript($.UI_FRAME_JS, true, true);
            }

            title = $.xss(title);

            var guid = $.md5("dialog_" + title), dialog;
            if( ( dialog = window["dialog_" + guid] ) ){
                if(true !== dialog.visible){
                    var reset = window["frame_" + guid].reset(page, listener, params, subsys, title);
                    dialog.show();
                }
            }else{

                if($.isNumeric(width)){
                    width += "em";
                }

                if($.isNumeric(height)){
                    height += "em";
                }

                //构造dialog和frame的html
                var html = [];
                html.push('<div id="dialog_' + guid + '" class="c_dialog" style="display:none;">');
                html.push('<div class="wrapper">');

                html.push('<div class="header">');
                html.push('<div class="text">' + title + '</div>');
                html.push('<div class="fn">');
                html.push('<div class="close"></div>');
                html.push('</div>');
                html.push('</div>');

                html.push('<div class="content">');

                html.push('<iframe id="frame_' + guid + '" style="width:100%;height:100%;display:none" frameborder="0"></iframe>');
                html.push('<div class="c_msg c_msg-full c_msg-loading">');
                html.push('<div class="wrapper">');
                html.push('<div class="emote"></div>');
                html.push('<div class="info"><div class="text"><div class="title">loading</div></div></div>');
                html.push('</div>');
                html.push('</div>');

                html.push('</div>');
                html.push('</div>');

                //插入到body下
                $(doc.body).append(html.join(""));
                html = null;

                //绑定事件
                if(afterAction){
                    if($.isFunction(afterAction)){
                        $("#dialog_" + guid).afterAction(afterAction);
                    }else if($.isString(afterAction)){
                        $("#dialog_" + guid).attr("onafterAction", afterAction);
                    }
                }
                if(hideAction){
                    if($.isFunction(hideAction)){
                        $("#dialog_" + guid).hideAction(hideAction);
                    }else if($.isString(hideAction)){
                        $("#dialog_" + guid).attr("onhideAction", hideAction);
                    }
                }

                window["frame_" + guid] = new Wade.Frame("frame_" + guid, {autoInit:false, title:title, page:page, listener:listener, params:params, subsys:subsys});
                dialog = window["dialog_" + guid] = new Wade.Dialog("dialog_" + guid, {closeable:false == closeable ? false : true, height: height, width: width, srcWindow: srcWindow});

                dialog.show();
            }

            return dialog;
        },
        setPopupReturnValue: function(node, el, value, close){
            var same = $.isSameDomain(parent);
            var wade3 = same && parent.Wade && parent.System;
            var wade4 = same && parent.Wade && parent.popupPageExternal;
            var srcFrame;
            if(wade3 || wade4){
                var currentPopupFrameId, frameElementThis;
                if(wade3){
                    srcFrame = parent;
                }else{
                    currentPopupFrameId = parent.Wade.getGlobalFrame().popUpExternalPageStack.slice(parent.Wade.getGlobalFrame().popUpExternalPageStack.length - 1)[0];
                    var popUpFrameElement = parent.Wade.getFrame(currentPopupFrameId, null, "id");
                    frameElementThis = popUpFrameElement ? popUpFrameElement.frameElement : same ? window.frameElement : null;
                    var srcframeName = frameElementThis ? frameElementThis.srcframeName ? frameElementThis.srcframeName : frameElementThis.getAttribute("srcframeName") : "";
                    if (srcframeName && srcframeName != "") {
                        srcFrame = parent.Wade.getFrame(srcframeName, null, "globalUniqueFrameId");
                    } else {
                        srcFrame = parent.Wade.getSameWadeDomainTop();
                    }
                }

                if(!srcFrame) return;

                if ($.isPlainObject(value)) {
                    for (var id in value) {
                        if (id && id != "undefined") {
                            var el = wade3 ? srcFrame.getElement(id) : srcFrame.$("#" + id);
                            if (wade3) {
                                if (el && el.nodeType) srcFrame.setFormElementValue(el.tagName.toLowerCase(), el, value[id]);
                            } else {
                                el.val(value[id]);
                            }
                        }
                    }
                } else if ($.isArray(value) && value.length && value.length % 2 == 0) {
                    for (var i = 0; i < value.length; i = i + 2) {
                        var id = value[i], val = value[i + 1];
                        if (id && id != "undefined") {
                            var el = wade3 ? srcFrame.getElement(id) : srcFrame.$("#" + id);
                            if (wade3) {
                                if (el && el.nodeType) srcFrame.setFormElementValue(el.tagName.toLowerCase(), el, val);
                            } else {
                                el.val(val);
                            }
                        }
                    }
                }
                if (false !== close) {
                    if (wade3) {
                        parent.Wade.dialog.closeCurrentDialog();
                    } else {
                        var eventId = frameElementThis ? frameElementThis.eventId ? frameElementThis.eventId : frameElementThis.getAttribute("eventId") : "";
                        var guidValue = frameElementThis ? frameElementThis.guidValue ? frameElementThis.guidValue : frameElementThis.getAttribute("guidValue") : "";
                        srcFrame.Wade.closePopupPageExternal(false, null, eventId, guidValue, currentPopupFrameId, false);
                    }
                }
            }else{
                var popup = _findPopup(node);
                if(popup){
                    popup.setPopupReturnValue(el, value, close);
                }
            }
        },
        showFloatLayer: function(el, display){ //显示指定的浮动层，支持点击浮动层之外区域时，自动隐藏该浮动层
            if(el && $.isString(el))
                el = doc.getElementById(el);

            if(!el || !el.nodeType)
                return;

            var id = $.attr(el, "id");
            if(!id){
                id = "_floatLayer_" + (++ floatLayerGUID);
                $.attr(el, "id", id);
            }

            //设置z-Index
            var className = el.className ? el.className : "";
            if( (" " + className + " ").indexOf(" c_float ") > -1 ){
                if( (" " + className + " ").indexOf(" c_float-show ") > -1 )
                    return;
                //设置样式
                el.className = $.trim(className + " c_float-show"); //设置样式
            }else{
                var disp = display && $.isString(display) ? display : "";
                if(el.style.display  == disp)
                    return;
                //设置显示
                el.style.display = disp;
            }

            el.style.zIndex = $.zIndexer.get(id); //设置zIndex
            push.call(floatLayers, id);
        },
        hideFloatLayer: function(el){  //隐藏指定的浮动层
            if(el && $.isString(el))
                el = doc.getElementById(el);

            if(!el || !el.nodeType)
                return;

            var id = $.attr(el, "id");
            if(!id) return;

            //移除z-Index
            var className = el.className ? el.className : "";
            if( (" " + className + " ").indexOf(" c_float ") > -1 ){
                if( (" " + className + " ").indexOf(" c_float-show ") < 0 )
                    return;

                //设置样式
                el.className = $.trim(  (" " + className + " ").replace(/ c_float-show /g, " ") );
            }else{
                if("none" == el.style.display)
                    return;

                //设置隐藏
                el.style.display = "none";
            }

            $.zIndexer.remove(id);
            splice.call(floatLayers, $.inArray(id, floatLayers), 1);
        },
        toggleFloatLayer: function(el, display){
            if(el && $.isString(el))
                el = doc.getElementById(el);

            if(!el || !el.nodeType)
                return;

            var className = el.className ? el.className : "";
            var isDisplay = (" " + className + " ").indexOf(" c_float ") > -1 ? (" " + className + " ").indexOf(" c_float-show ") > -1
                : getStyle(el, "display") != "none";
            if(isDisplay){
                PopupBox.hideFloatLayer(el);
            }else{
                PopupBox.showFloatLayer(el, display);
            }
        }
    };

    $(function(){

        $(doc.body).bind(START_EVENT, function(e){
            if(!e || !e.target)
                return;
            var node = e.target;
            if(!node || !node.nodeType) return;

            if(floatLayers.length <= 0) return;

            var i = 0, found = false;
            while(node && node.nodeType && node != doc.body && i < 50){
                var id = $.attr(node, "id"), xwf = $.attr(node, "x-wade-float");
                if(xwf && $.inArray(xwf, floatLayers) == (floatLayers.length - 1)){
                    found = true;
                    break;
                }
                if(id && $.inArray(id, floatLayers) == (floatLayers.length - 1) && "none" != node.style.display){
                    found = true;
                    break;
                }
                node = node.parentNode;
                i ++;
            }

            if(!found){
                PopupBox.hideFloatLayer(floatLayers[floatLayers.length - 1]);
            }
        });

    });

    window.PopupBox = $.PopupBox = PopupBox;

    $.each("showPopup,hidePopup,forwardPopup,backPopup,popupPage,popupDialog,setPopupReturnValue,showFloatLayer,hideFloatLayer,toggleFloatLayer,popupPageByUrl".split(","), function(idx, name){
        window[name] = $.PopupBox[name];
    });

})(window.Wade, window, document);