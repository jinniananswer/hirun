/*!
 * datefield component
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2015, WADE
 */
(function ($, window, doc) {
    "use strict";

    if( !$ || typeof $.DateField != "undefined" )
        return;

    var push = Array.prototype.push,
        splice = Array.prototype.splice;

    var input 		 = doc.createElement("input");
    var hasInput     = "oninput" in input;
    var hasPattern   = "pattern" in input;
    input 		 = null;

    var hasTouch     = typeof $.hasTouch != "undefined" ? $.hasTouch : "ontouchstart" in window;
    var START_EVENT  = hasTouch ? 'touchstart': 'mousedown';

    var phoneMode = $.os.phone || true === $.ratioPhone; //手机模式
    var dropDownCalendarId = "_Wade_DropDownCalendar";
    var dropDownCalendar;

    var activeDateFieldId;

    var DateField = function(el, settings){
        var that = this;

        that.el = el && el.nodeType == 1 ? el : doc.getElementById(el);
        if( !that.el || !that.el.nodeType || !(that.id = $.attr(that.el, "id")) )
            return;

        if(settings && $.isObject(settings))
            $.extend(that, settings);

        if(!$.attr(that.el, "x-wade-uicomponent")){
            $.attr(that.el, "x-wade-uicomponent", "datefield");
        }

        that._init();

        that.constructor.call(that);
    };

    DateField.prototype = $.extend(new $.UIComponent(), {
        val: function(value){
            var that = this;

            if(value == undefined){
                return that.el.value;
            }

            that.value = that.el.value = value;
        },
        refresh: function(){
            var that = this;

            if(!that.spanEl)
                that.spanEl = $(that.el).parent("span.e_mix:first")[0];

            var offset = that.spanEl.getBoundingClientRect(); //$(that.spanEl).offset();
            that.spanLeft = offset.left, that.spanTop = offset.top,
                that.spanHeight = that.spanEl.offsetHeight, that.spanWidth = that.spanEl.offsetWidth;

            var topSize = that.spanTop, leftSize = that.spanLeft; //顶距，左距 (不包括body滚动条产生的scroll高度)
            var body = doc.body;
            var downSize =  body.offsetHeight - that.spanHeight - topSize; //底距 body.offsetHeight 只包括可见范围高度

            if($.isNumber(body.scrollTop)){
                that.spanTop += body.scrollTop - 2;  //BoundingClientRect 不包括body滚动条产生的scroll高度
            }

            if($.isNumber(body.scrollLeft)){
                that.spanLeft += body.scrollLeft; //BoundingClientRect 不包括body滚动条产生的scroll宽度
            }

            that.dir = downSize >= topSize ? "down" : "up";

            var maxHeight = that.dir == "down" ? downSize : topSize ; //多减去2像素边框
            var floatWidth = 0, floatHeight = 0;

            var floatEl = doc.getElementById(dropDownCalendarId + "_float");
            var className = floatEl.className ? floatEl.className : "";
            if( (" " + className + " ").indexOf(" c_float-show ") < 0 ){
                floatEl.style.left = "-99999px";
                floatEl.style.top = "-99999px";
                floatEl.style.display = "block";
                floatWidth = floatEl.offsetWidth;
                floatHeight = floatEl.offsetHeight;
                floatEl.style.display = "";
            }else{
                floatWidth = floatEl.offsetWidth;
                floatHeight = floatEl.offsetHeight;
            }

            var h = Math.min(maxHeight, floatHeight);
            var docHeight = document.documentElement.clientHeight;
            floatEl.style.left = (that.spanLeft + that.spanWidth - floatWidth) + "px";

            //如果浮动高度小于可见高度，但是上下高度都不足，则垂直居中显示
            if( floatHeight <= docHeight && topSize < floatHeight && downSize < floatHeight) {
                floatEl.style.top = (docHeight - floatHeight) / 2 + "px";
            }else{
                floatEl.style.top = (that.dir == "down" ? that.spanTop + that.spanHeight : that.spanTop - h) + "px";
            }

            //floatEl.style.width = that.spanWidth + "px";
        },
        getDisabled: function(){
            var that = this;
            return that.disabled;
        },
        setDisabled: function(value){
            var that = this;

            if(typeof value == "undefined")
                return;

            that.disabled = !!value;
            that.el.disabled = that.disabled;

            setTimeout(function(){
                var el = that.spanEl ? that.spanEl : that.el;
                var className = el.className ? el.className : "";
                //设置样式
                if(that.disabled){
                    if( (" " + className + " ").indexOf(" e_dis ") < 0 )
                        el.className = $.trim(className + " e_dis");
                }else{
                    className = $.trim( (" " + className + " ").replace(/ e_dis /ig, " ") );
                    el.className = className;
                }
            }, 0);
        },
        getReadonly: function(){
            var that = this;
            return that.readonly;
        },
        setReadonly: function(value){
            var that = this;
            if(typeof value == "undefined")
                return;

            that.readonly = !!value;

            setTimeout(function(){
                $.attr(that.el, "readonly", that.readonly);
            }, 0);
        },
        destroy: function(){
            var that = this;

            that.spanEl = null;
            that.icoEl = null;

            that.el = null;
        },
        _init: function(){
            var that = this;

            that.spanEl = $(that.el).parent("span.e_mix:first")[0];
            that.icoEl = $(that.el).next("span.e_ico-date:first")[0];

            if(that.icoEl){
                that.icoEl.style.display = that.dropDown ? "" : "none";
            }

            if(phoneMode || $.os.pad || that.readonly){
                that.setReadonly(true);
            }

            if(that.disabled){
                that.setDisabled(true);
            }

            /********** 绑定事件 开始**********/
            if( hasInput ){
                $(that.el).bind("input", function(e){
                    this.value = ("" + this.value).replace(/[^0-9-\/\s:]+/ig, "");
                });
            }else{
                // "-" keyCode 189, "/" keCode 191
                $(that.el).keydown(function(e){
                    if(e.shiftKey || e.altKey || e.ctrlKey)
                        return false;
                    if( (e.keyCode > 47 && e.keyCode < 58) || (e.keyCode > 95 && e.keyCode < 106)
                        || e.keyCode == 8 || e.keyCode == 46
                        || e.keyCode == 189 || e.keyCode == 191 ){
                        return true;
                    }
                    return false;
                });
            }

            if(true === that.dropDown){

                var dropDownFn = function(e){
                    if(that.disabled) return;
                    if(true !== that.dropDown) return;

                    if (e.originalEvent)
                        e = e.originalEvent;

                    //生成下拉框
                    _createDropDownCalendar();

                    if(dropDownCalendar){

                        $.extend(dropDownCalendar, {
                            now: that.now,
                            format: that.format,
                            value: that.value,
                            useTime: that.useTime,
                            useMode: that.useMode,
                        });

                        //重置
                        dropDownCalendar.reset();
                    }

                    that.refresh();

                    var floatEl = doc.getElementById(dropDownCalendarId + "_float");
                    var className = floatEl.className ? floatEl.className : "";

                    if((" " + className + " ").indexOf(" c_float-show ") < 0)
                        floatEl.className = $.trim(className + " c_float-show");

                    activeDateFieldId = that.id;
                };

                //绑定dropDown事件
                $(that.el).bind("tap", dropDownFn);
                $(that.icoEl).bind("tap", dropDownFn);
            }

            /********** 绑定事件 结束**********/
        }
    });


    function _createDropDownCalendar(){
        if(dropDownCalendar)
            return dropDownCalendar;

        $(doc.body).append('<div id="' + dropDownCalendarId + '_float" class="c_float' + (phoneMode ? ' c_float-phone-auto' :'') + '" style="width:22em;"><div  id="' + dropDownCalendarId + '_float_bg" class="bg"></div><div  id="' + dropDownCalendarId + '_float_content" class="content"></div></div>');

        dropDownCalendar = $.Calendar.append(dropDownCalendarId + "_float_content", {
            name: dropDownCalendarId,
            className: "c_calendar" + (phoneMode ? "" : " c_calendar-s")
        });

        /********************* 绑定日历事件 开始 ***************/

        //绑定日历选择事件
        $("#" + dropDownCalendarId).select(function(e){

            //只有非time模式才设置返回值
            if(activeDateFieldId && window[activeDateFieldId]
                && window[activeDateFieldId] instanceof DateField
                && true !== window[activeDateFieldId].useTime
                && true === window[activeDateFieldId].dropDown){
                //设置值
                window[activeDateFieldId].val( this.val() );

                //执行afterAction
                $.event.trigger("afterAction", null, window[activeDateFieldId].el);


                //隐藏日历
                hideDropDownCalendar();
            }
        });

        //绑定日历清除事件
        $("#" + dropDownCalendarId).clear(function(e){

            if(activeDateFieldId && window[activeDateFieldId]
                && window[activeDateFieldId] instanceof DateField
                && true === window[activeDateFieldId].dropDown){

                //清空值
                window[activeDateFieldId].val( "" );

                //执行afterAction
                $.event.trigger("afterAction", null, window[activeDateFieldId].el);
            }

            //隐藏日历
            hideDropDownCalendar();
        });

        $("#" + dropDownCalendarId).ok(function(e){

            if(activeDateFieldId && window[activeDateFieldId]
                && window[activeDateFieldId] instanceof DateField
                && true === window[activeDateFieldId].useTime
                && true === window[activeDateFieldId].dropDown){

                //清空值
                window[activeDateFieldId].val( this.val() );

                //执行afterAction
                $.event.trigger("afterAction", null, window[activeDateFieldId].el);

                //隐藏日历
                hideDropDownCalendar();

                return false;
            }
        });

        $("#" + dropDownCalendarId + "_float_bg").bind(START_EVENT, function(){
            //隐藏日历
            hideDropDownCalendar();
        });
        /********************* 绑定日历事件 结束 ***************/

        return dropDownCalendar;
    }

    function hideDropDownCalendar(){
        if(!activeDateFieldId)
            return;

        var floatEl = doc.getElementById(dropDownCalendarId + "_float");
        if(floatEl != null && floatEl.nodeType == 1){
            var className = floatEl.className ? floatEl.className : "";
            if( (" " + className + " ").indexOf(" c_float-show ") > -1 ){
                floatEl.className = $.trim( (" " + className + " ").replace(/ c_float-show /ig, " ") );
            }
        }
        floatEl = null;

        activeDateFieldId = null;
    }

    $(function(){

        $(doc.body).bind(START_EVENT, function(e){
            if(!e || !e.target)
                return
            var node = e.target;
            if(!node || !node.nodeType) return;

            if(!dropDownCalendar) return;

            var i = 0, found = false;
            while(node && node.nodeType && node != doc.body && i < 50){
                var id = $.attr(node, "id");
                if(id && $.isString(id) && ( id.indexOf(activeDateFieldId) == 0 || id.indexOf(dropDownCalendarId) == 0 ) ){
                    found = true;
                    break;
                }
                node = node.parentNode;
                i ++;
            }

            if(!found){
                hideDropDownCalendar();
            }
        });

        //窗口尺寸变化时隐藏
        $(window).bind('onorientationchange' in window ? 'orientationchange' : 'resize', function(){
            hideDropDownCalendar();
        });

        //鼠标滚轮事件
        if(!hasTouch){
            $(doc.body).bind("mousewheel", function(e){
                //setTimeout(hideDropDownCalendar, 0);
                hideDropDownCalendar();
            });
        }
    });

    //export
    window.DateField = $.DateField = DateField;

})(window.Wade, window, document);