/*!
 * calendar component
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2015, WADE
 */
(function($, window, doc){
    "use strict";

    if( !$ || typeof $.Calendar != "undefined" )
        return;

    var phoneMode = $.os.phone || true === $.ratioPhone; //手机模式

    var Calendar = function(el, settings){
        var that = this;

        that.el = el && el.nodeType == 1 ? el : doc.getElementById(el);

        if( !that.el || !that.el.nodeType || !$.nodeName(that.el, "div") || !(that.id = $.attr(that.el, "id")) )
            return;

        if(settings && $.isObject(settings))
            $.extend(that, settings);

        if(!$.attr(that.el, "x-wade-uicomponent")){
            $.attr(that.el, "x-wade-uicomponent", "calendar");
        }

        that._init();

        that.constructor.call(that);
    };

    Calendar.prototype = $.extend(new $.UIComponent(), {
        nowDate: function(){
            var that = this;
            if(that.now){
                var now = new Date();
                return new Date(that.now.getFullYear(), that.now.getMonth(), that.now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds());
            }else{
                return new Date();
            }
        },
        val: function(value){
            var that = this;
            if($.isString(value)){
                var date = (value == "" ? that.nowDate() : value.toDate(that.format));
                if(date && date instanceof Date){
                    that.value = date;
                    that.date = new Date(that.value);
                    that._fillDate(that.date);
                }
            }else if(that.value && that.value instanceof Date){
                return that.value.format(that.format);
            }
        },
        lunarVal: function(){
            var that = this;

            if(that.useLunar && that.value && that.value instanceof Date){
                var ldate = new LunarDate(that.value);
                return ldate.chineseEra() + "年 【" + ldate.chineseZodiac() + "年】" + " " + ldate.lmonth() + " " + ldate.lday();
            }
        },
        showSelect: function(){
            var that = this;
            that.fn.style.display = "none";
            that.select.style.display = "";

            //还原年份
            if(that.iyear != that.date.getFullYear()){
                that.iyear = that.date.getFullYear();
                that._fillYear(that.iyear);
            }

            //还原月份
            if(that.imonth != that.date.getMonth()){
                that.imonth = that.date.getMonth();
                that._fillMonth(that.imonth);
            }
        },
        hideSelect: function(){
            var that = this;
            that.fn.style.display = "";
            that.select.style.display = "none";
        },
        reset: function(){
            var that = this;

            //TODO 待优化
            if(that.useMode && that.useMode == 'month') {
                that.timeCt.style.display = 'none';
                that.dayCt.style.display = 'none';
                that.weekCt.style.display = 'none';
            } else {
                that.timeCt.style.display = '';
                that.dayCt.style.display = '';
                that.weekCt.style.display = '';
            }

            //设置时分秒显示
            that.timeCt.style.display = that.useTime ? "" :"none";

            //hasTime样式处理
            var className = that.el.className ? that.el.className : "";
            if(that.useTime && (" " + className + " ").indexOf(" c_calendar-hasTime ") < 0 ){
                that.el.className = $.trim(className + " c_calendar-hasTime");
            }else if(!that.useTime && (" " + className + " ").indexOf(" c_calendar-hasTime ") > -1 ){
                that.el.className = $.trim((" " + className + " ").replace(/ c_calendar-hasTime /ig, " "));
            }

            //设置初始值，并生成初始值日期显示
            if(!that.format){
                that.format = that.useTime ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd"; //默认格式
            }

            if(that.value && $.isString(that.value)
                && that.format && $.isString(that.format)){
                that.value = that.value.toDate(that.format);
            }

            if(!that.value){
                that.value = that.nowDate(); //设置选中时间为当前时间
            }

            that.date = new Date(that.value); //显示时间
            that._fillDate(that.date);
        },
        destroy: function(){
            var that = this;

            that.header = null;
            that.yearPrev = null;
            that.yearNext = null;
            that.monthPrev = null;
            that.monthNext = null;

            that.btnSelect = null;
            that.yearText = null;
            that.monthText = null;

            that.select = null;
            that.selectYear = null;
            that.selectMonth = null;
            that.selectYearPrev = null;
            that.selectYearNext = null;
            that.selectYearList = null;
            that.selectMonthList = null;

            that.dayCt = null;
            that.dayList = null;

            that.timeCt = null;
            that.hourCt = null;
            that.minCt = null;
            that.secCt = null;

            that.hourOption = null;
            that.minutesOption = null;
            that.secondOption = null;

            that.hourIpt = null;
            that.minutesIpt = null;
            that.secondIpt = null;

            that.quickSelFn = null;

            that.fn = null;
            that.btnClear = null;
            that.btnQuickSel = null;
            that.btnOk = null;

            that.el = null;
        },
        _init: function(){
            var that = this;

            that.header = $(that.el).children("div.header:first")[0];
            that.yearPrev = $(that.header).children("div.prevYear:first")[0];
            that.yearNext = $(that.header).children("div.nextYear:first")[0];
            that.monthPrev = $(that.header).children("div.prev:first")[0];
            that.monthNext = $(that.header).children("div.next:first")[0];

            that.btnSelect = $(that.header).children("div.info:first")[0];//显示选择界面按钮
            that.yearText = $(that.btnSelect).children("span.year:first")[0];
            that.monthText = $(that.btnSelect).children("span.month:first")[0];

            that.select = $(that.el).children("div.select:first")[0];
            that.selectYear = $(that.select).children("div.year:first")[0];
            that.selectMonth = $(that.select).children("div.month:first")[0];
            that.selectYearPrev = $(that.selectYear).children("span.prev:first")[0];
            that.selectYearNext = $(that.selectYear).children("span.next:first")[0];
            that.selectYearList = $(that.selectYear).children("ul:first")[0];
            that.selectMonthList = $(that.selectMonth).children("ul:first")[0];

            that.weekCt = $(that.el).children("div.week:first")[0];

            that.dayCt = $(that.el).children("div.day:first")[0];
            that.dayList = $(that.dayCt).children("ul:first")[0];

            that.timeCt = $(that.el).children("div.time:first")[0];
            that.hourCt = $(that.timeCt).children("div.hour:first")[0];
            that.minCt = $(that.timeCt).children("div.min:first")[0];
            that.secCt = $(that.timeCt).children("div.sec:first")[0];

            that.hourOption = $(that.hourCt).children("div.option:first")[0];
            that.minutesOption = $(that.minCt).children("div.option:first")[0];
            that.secondOption = $(that.secCt).children("div.option:first")[0];

            that.hourIpt = $(that.hourCt).find("input[type=text]:first")[0];
            that.minutesIpt = $(that.minCt).find("input[type=text]:first")[0];
            that.secondIpt = $(that.secCt).find("input[type=text]:first")[0];

            that.quickSelFn = $(that.el).children("div.shortcut:first")[0];   //快捷选择区域

            that.fn = $(that.el).children("div.fn:first")[0];   //功能按钮区域
            that.btnClear = $(that.fn).children("button[tag=clear]:first")[0]; //清楚按钮
            that.btnQuickSel = $(that.fn).children("button[tag=quicksel]:first")[0]; //快捷选择
            that.btnOk = $(that.fn).children("button[tag=ok]:first")[0]; //确认选择按钮

            if(that.useLunar){
                var className = that.el.className ? that.el.className : "";
                if( (" " + className + " ").indexOf(" c_calendar-lunar ") < 0 ){
                    that.el.className = $.trim(className + " c_calendar-lunar");
                }
            }

            //重置
            that.reset();

            if(that.useTime){
                //手机平板设置readonly
                if(phoneMode || $.os.pad){
                    $("#" + that.id + "_hour_ipt").attr("readonly", true); //时
                    $("#" + that.id + "_minutes_ipt").attr("readonly", true); //分
                    $("#" + that.id + "_second_ipt").attr("readonly", true); //秒
                }
            }

            /********* 绑定事件 开始 *************/
            //切换到上一年
            $(that.yearPrev).tap(function(){
                that.date.setFullYear(that.date.getFullYear() - 1);
                that._fillDate(that.date);
                that.select.style.display = "none"; //隐藏年月选择界面
            });

            //切换到下一年
            $(that.yearNext).tap(function(){
                that.date.setFullYear(that.date.getFullYear() + 1);
                that._fillDate(that.date);
                that.select.style.display = "none"; //隐藏年月选择界面
            });

            //切换到上一月
            $(that.monthPrev).tap(function(){
                that.date.setMonth(that.date.getMonth() - 1);
                that._fillDate(that.date);
                that.select.style.display = "none"; //隐藏年月选择界面
            });

            //切换到下一月
            $(that.monthNext).tap(function(){
                that.date.setMonth(that.date.getMonth() + 1);
                that._fillDate(that.date);
                that.select.style.display = "none"; //隐藏年月选择界面
            });

            //显示/隐藏  年份和月份选择界面
            $(that.btnSelect).tap(function(){
                if(that.select.style.display == "none"){
                    that.showSelect();
                }else{
                    that.hideSelect();
                }
            });

            //时间选择事件,由于datefield共用一个calendar实例，默认不做useTime判断都进行事件绑定
            //if(that.useTime){
            $(that.hourOption).tap(function(e){
                if(e.originalEvent)
                    e = $.event.fix(e.originalEvent);

                var el = e.target;
                if(el && el.nodeType == 1 && $.nodeName(el, "li")){
                    var val = $.trim(el.innerHTML);
                    if(val == "") return;

                    $("li[class=on]", that.hourOption).attr("className", "");

                    el.className = "on";
                    that.hourIpt.value = val;

                    that.date.setHours(parseInt(val));
                    that.value = new Date(that.date);

                    //todo 设置返回值
                    that._triggerSelectAction(e);
                }
                that.hourOption.style.display = "none";
            });

            $(that.minutesOption).tap(function(e){
                if(e.originalEvent)
                    e = $.event.fix(e.originalEvent);
                var el = e.target;
                if(el && el.nodeType == 1 && $.nodeName(el, "li")){
                    var val = $.trim(el.innerHTML);
                    if(val == "") return;

                    $("li[class=on]", that.minutesOption).attr("className", "");

                    el.className = "on";
                    that.minutesIpt.value = val;

                    that.date.setMinutes(parseInt(val));
                    that.value = new Date(that.date);

                    //todo 设置返回值
                    that._triggerSelectAction(e);
                }
                that.minutesOption.style.display = "none";
            });

            $(that.secondOption).tap(function(e){
                if(e.originalEvent)
                    e = $.event.fix(e.originalEvent);
                var el = e.target;
                if(el && el.nodeType == 1 && $.nodeName(el, "li")){
                    var val = $.trim(el.innerHTML);
                    if(val == "") return;

                    $("li[class=on]", that.secondOption).attr("className", "");

                    el.className = "on";
                    that.secondIpt.value = val;

                    that.date.setSeconds(parseInt(val));
                    that.value = new Date(that.date);

                    //todo 设置返回值
                    that._triggerSelectAction(e);
                }
                that.secondOption.style.display = "none";
            });

            $(that.hourIpt).tap(function(){
                that.minutesOption.style.display = "none";
                that.secondOption.style.display = "none";

                that.hourOption.style.display = "none" == that.hourOption.style.display ? "" : "none";
            });

            $(that.minutesIpt).tap(function(){
                that.hourOption.style.display = "none"
                that.secondOption.style.display = "none";

                that.minutesOption.style.display = "none" == that.minutesOption.style.display ? "" : "none";
            });

            $(that.secondIpt).tap(function(){
                that.hourOption.style.display = "none"
                that.minutesOption.style.display = "none";

                that.secondOption.style.display = "none" == that.secondOption.style.display ? "" : "none";
            });
            //}

            //执行年份月份选择
            function doYearMonthSelect(){
                if(that.iyear != that.date.getFullYear()
                    || that.imonth != that.date.getMonth()){

                    that.date.setFullYear(that.iyear);
                    that.date.setMonth(that.imonth);

                    that.value = new Date(that.date);

                    that._fillDate(that.date);
                    that._fillYear(that.iyear);
                    that._fillMonth(that.imonth);
                }
                that.hideSelect();
            }

            //切换到前10年
            $(that.selectYearPrev).tap(function(){
                if(that.iyear - 6 < 1920) return; //最小1920年
                that.iyear -= 10;
                that._fillYear(that.iyear);
            });

            //切换到后10年
            $(that.selectYearNext).tap(function(){
                if(that.iyear + 5 > 2050) return; //最大2050年
                that.iyear += 10;
                that._fillYear(that.iyear);
            });

            //选择年份
            $(that.selectYearList).tap(function(e){
                if(e.originalEvent)
                    e = $.event.fix(e.originalEvent);
                var el = e.target;
                if(el && el.nodeType == 1 && $.nodeName(el, "li")){
                    if($.trim(el.innerHTML) == "") return;

                    $("li[class=on]", that.selectYearList).attr("className", "");

                    el.className = "on";
                    that.iyear = el.innerHTML;
                }
            });

            //选择月份
            $(that.selectMonthList).tap(function(e){
                if(e.originalEvent)
                    e = $.event.fix(e.originalEvent);
                var el = e.target;
                if(el && el.nodeType == 1 && $.nodeName(el, "li")){
                    if($.trim(el.innerHTML) == "") return;

                    $("li[class=on]", that.selectMonthList).attr("className", "");

                    el.className = "on";
                    that.imonth = $.attr(el, "val");
                }

                //执行选择
                doYearMonthSelect();
            });

            //选择日期
            $(that.dayList).tap(function(e){
                if(e.originalEvent)
                    e = $.event.fix(e.originalEvent);
                var el = e.target;
                if(el && el.nodeType == 1){
                    var i = 0, node = el, found = false;
                    while(i < 3 && node && node.nodeType){
                        if(node.nodeType == 1 && $.nodeName(node, "li")){
                            found = true;
                            break;
                        }
                        node = node.parentNode;
                        i ++;
                    }

                    if(!found) return;

                    //判断今天样式
                    /*
                     if((" " + node.className + " ").indexOf(" on ") > -1 && (" " + node.className + " ").indexOf(" cur ") < 0)
                     return;
                     */

                    var dayVal = $("span:first", node).text();

                    if(!dayVal || !/^\d+$/.test(dayVal)) return;

                    $("li[class^=on]", that.dayList).attr("className", ""); //今天的样式为 "on cur"

                    node.className = "on";
                    node = null;

                    that.date.setDate(dayVal);
                    if(!that.useTime){ //同步时分秒
                        var nowDate = new Date();
                        that.date.setHours(nowDate.getHours());
                        that.date.setMinutes(nowDate.getMinutes());
                        that.date.setSeconds(nowDate.getSeconds());
                    }
                    that.value = new Date(that.date);

                    //todo 设置返回值
                    that._triggerSelectAction(e);
                }
            });

            //清空日历控件对应表单输入框值
            $(that.btnClear).tap(function(e){
                //todo 设置返回值
                if(false !== $.event.trigger("clear", null, that.el)){
                    if(phoneMode){
                        backPopup(that.el);
                    }
                }
            });

            //显示快捷选择
            function showQuickSelect(){

                //隐藏时分秒选择
                if( "none" != that.hourOption.style.display )
                    that.hourOption.style.display = "none";
                if( "none" != that.minutesOption.style.display )
                    that.minutesOption.style.display = "none";
                if( "none" != that.secondOption.style.display )
                    that.secondOption.style.display = "none";

                that.quickSelFn.style.display = "";
                $("span[tag=fold]", that.btnQuickSel).attr("className", "e_ico-unfold");
            }

            //隐藏快捷选择
            function hideQuickSelect(){
                that.quickSelFn.style.display = "none";
                $("span[tag=fold]", that.btnQuickSel).attr("className", "e_ico-fold");
            }

            $(that.quickSelFn).tap(function(e){
                if(e.originalEvent)
                    e = $.event.fix(e.originalEvent);
                var el = e.target;
                if(!el || !el.nodeType || !$.nodeName(el, "li"))
                    return;

                var tag = $.attr(el, "tag");
                that.date = that.nowDate(); //快捷选择时间以当前时间为准
                if( "HalfYearAgo" == tag ){ //半年前
                    that.date.setMonth(that.date.getMonth() - 6);
                }else if( "ThreeMonthAgo" == tag ){ //三月前
                    that.date.setMonth(that.date.getMonth() - 3);
                }else if( "PrevMonthToday" == tag ){ //上月
                    that.date.setMonth(that.date.getMonth() - 1);
                }else if( "Today" == tag ){
                    //that.date = that.nowDate();
                }else if( "NextMonthFirst" == tag ){ //下月一日
                    that.date.setMonth(that.date.getMonth() + 1);
                    that.date.setDate(1);
                }else if( "NextMonthLast" == tag ){ //下月末日
                    that.date.setMonth(that.date.getMonth() + 2);
                    that.date.setDate(0);
                }else if( "NextYearToday" == tag ){	//明年今日
                    that.date.setFullYear(that.date.getFullYear() + 1);
                }else if( "2050" == tag ){	//2050
                    that.date.setFullYear(2050);
                    that.date.setMonth(11);
                    that.date.setDate(31);
                    that.date.setHours(23); //设置时
                    that.date.setMinutes(59, 59); //设置分秒
                }else{
                    return;
                }

                that.value = new Date(that.date);
                that._fillDate(that.date);

                that.hideSelect();

                hideQuickSelect();

                //todo 设置返回值
                that._triggerSelectAction(e);
            });

            $(that.btnQuickSel).tap(function(){
                if("none" == that.quickSelFn.style.display){
                    showQuickSelect();
                }else{
                    hideQuickSelect();
                }
            });

            //确认选择
            $(that.btnOk).tap(function(e){

                //执行选择年月
                doYearMonthSelect();

                //处理时分秒
                if(that.useTime){
                    var hour = that.hourIpt.value;
                    var minutes = that.minutesIpt.value;
                    var second = that.secondIpt.value;

                    if($.isNumeric(hour) && $.isNumeric(minutes) && $.isNumeric(second)){
                        that.date.setHours(hour, minutes, second);
                    }
                }

                //判断当前值
                if(!that.value || that.value != that.date){
                    that.value = new Date(that.date);
                    that._fillDate(that.date);

                    //todo 设置返回值
                    if(false !== $.event.trigger({type:"ok", originalEvent:e.originalEvent, context:that}, null, that.el)){
                        that._triggerSelectAction(e);
                    }
                }
            });

            /********* 绑定事件 结束*************/

        },
        _fillDate: function(date){ //填充日期
            if(!date || !date instanceof Date)
                return;

            var that = this;

            var nYear = that.now.getFullYear();
            var nMonth = that.now.getMonth();
            var nDay = that.now.getDate();

            var vYear = that.value.getFullYear();
            var vMonth = that.value.getMonth();
            var vDay = that.value.getDate();

            var year = date.getFullYear();
            var month = date.getMonth();
            var day = date.getDate();

            var firstDay = new Date(year, month, 1);
            var lastDay = new Date(year, month + 1, 0);
            var dfd = firstDay.getDay(); //day of firstDay
            var dld = lastDay.getDate(); //date of lastDay

            that.yearText.innerHTML = year;
            that.monthText.innerHTML = month + 1;

            //生成日期列表
            var n, i, childs, li, licls, iday, idate, html, indate;
            var ldate, lyear, lday, lmonth, ltext, lfd;
            childs = that.dayList.childNodes;
            if(childs && childs.length > 0){
                n = i = 0;
                while(i < childs.length){
                    indate = false;
                    li = childs[i];
                    if(li && li.nodeType == 1 && $.nodeName(li, "li")){
                        if(n >= dfd && (iday = n - dfd + 1) ){
                            if(iday <= dld){
                                lfd = false;
                                indate = true;
                                html = [];
                                html.push("<span class=\"solar\">");
                                html.push(iday);
                                html.push("</span>");
                                if(that.useLunar){
                                    idate = new Date(year, month, iday);
                                    ldate = new LunarDate(idate);
                                    //lyear = ldate.chineseEra() + "年 【" + ldate.chineseZodiac() + "年】";
                                    //lmonth = ldate.lmonth();
                                    //lday = ldate.lday();
                                    ltext = idate.festival(); //公历节日
                                    if(!ltext) ltext = ldate.festival(); //农历节日
                                    if(!ltext) ltext = ldate.solarTerm(); //农历节气
                                    if(ldate.day == 1){
                                        lfd = true;
                                        if(!ltext) ltext = ldate.lmonth(); //农历每月第一天显示月份
                                    }
                                    if(!ltext) ltext = ldate.lday(); //农历日期
                                    html.push("<span class=\"lunar" + (lfd ? " lunar-month" : "") + "\">");
                                    html.push(ltext);
                                    html.push("</span>");
                                }
                                li.innerHTML = html.join('');
                                licls = "";
                                if(vYear == year && vMonth == month && vDay == iday) licls += " on";
                                if(nYear == year && nMonth == month && nDay == iday) licls += " cur";
                                var weekday = (dfd + iday - 1) % 7; //计算当前星期几
                                if(weekday == 0){ //周日
                                    licls += " sun";
                                }else if(weekday == 6){ //周六
                                    licls += " sat";
                                }
                                li.className = $.trim(licls);
                            }
                        }
                        if(!indate){ //不在当月日期之内
                            li.innerHTML = "";
                            li.className = "empty";
                        }
                        n ++;
                    }
                    i ++;
                }
            }

            n = i = childs = li = licls = idate = ldate = ltext = html = indate = null;
            firstDay = lastDay = null;

            that.iyear = year; //当前页年份
            that._fillYear(year);

            that.imonth = month; //当前选中月份
            that._fillMonth(month);

            //填充时分秒
            //if(that.useTime){
            that.hourIpt.value = date.getHours();
            that.minutesIpt.value = date.getMinutes();
            that.secondIpt.value = date.getSeconds();
            //}
        },
        _fillYear: function(year){
            var that = this;

            //生成年份列表
            var dateYear = that.date.getFullYear(); //日历当前日期年份
            var n, i, yearVal, li;
            var childs = that.selectYearList.childNodes;
            if(childs && childs.length > 0){
                n = i = 0;
                while(i < childs.length){
                    li = childs[i];
                    if(li && li.nodeType == 1 && $.nodeName(li, "li")){
                        yearVal = (year - 5 + n); //当前年份排在第六
                        if(yearVal < 1920 || yearVal > 2050){ //只显示1920-2050之间的年份
                            li.innerHTML = "";
                        }else{
                            li.innerHTML = yearVal;
                            li.className = (yearVal == dateYear ? "on" : "");
                        }
                        n ++;
                    }
                    i ++;
                }
            }
            n = i = yearVal = li = childs = null;
        },
        _fillMonth: function(month){
            var that = this;

            //处理月份列表
            var n, i, li;
            var childs = that.selectMonthList.childNodes;
            if(childs && childs.length > 0){
                n = i = 0;
                while(i < childs.length){
                    li = childs[i];
                    if(li && li.nodeType == 1 && $.nodeName(li, "li")){
                        li.className = (month == n) ? "on" : "";
                        n ++;
                    }
                    i ++;
                }
            }
            n = i = li = childs = null;
        },
        _triggerSelectAction: function(e){
            var that = this;

            //todo 设置返回值
            var ret = $.event.trigger({type:"select", originalEvent:e.originalEvent, context:that}, null, that.el);
            if(false !== ret){
                /*
                 //手机端隐藏日期
                 if(phoneMode){
                 backPopup(that.el);
                 }
                 */
            }
            return ret;
        }
    });

    var _findCalendarByDom = function(_node){
        if(!_node || !_node.nodeType == 1)
            return null;

        var id = $.attr(_node, "id");
        if(id && window[id] && window[id] instanceof $.Calendar)
            return window[id];
    };

    //Calendar构造
    $.extend(Calendar, {
        append: function(el, settings, source){
            return _createCalendar(el, "append", settings);
        },
        prepend: function(el, settings, source){
            return _createCalendar(el, "prepend", settings);
        },
        before: function(el, settings, source){
            return _createCalendar(el, "before", settings);
        },
        after: function(el, settings, source){
            return _createCalendar(el, "after", settings);
        },
        build: function(settings){
            return _buildCalendar(settings);
        }
    });

    function _buildCalendar(s){

        if(!s.name || !$.isString(s.name))
            return;

        if(!s.id || !$.isString(s.id))
            s.id = s.name;

        var className = s.className ? s.className : "";

        if(!className){
            className = "c_calendar";
            if(s.useLunar){
                className += " c_calendar-lunar";
            }
        }

        var html = [];

        html.push('<div x-wade-uicomponent="calendar" id="' + s.id + '" class="' + className + '" style="' + (s.style ? s.style : '') + '">');

        //头部 开始
        html.push('<div class="header">');
        html.push('	<div class="prevYear"></div>');
        html.push('	<div class="prev"></div>');
        html.push('	<div class="info">');
        html.push('		<span class="year"></span>');
        html.push('		<span class="month"></span>');
        html.push('	</div>');
        html.push('	<div class="next"></div>');
        html.push('	<div class="nextYear"></div>');
        html.push('</div>');
        //头部 结束

        //年月选择 开始
        html.push('<div class="select" style="display:none;">');
        html.push('	<div class="year">');
        html.push('		<span class="prev"></span>');
        html.push('		<ul>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('			<li></li>');
        html.push('		</ul>');
        html.push('		<span class="next"></span>');
        html.push('	</div>');
        html.push('	<div class="month">');
        html.push('		<ul>');
        html.push('			<li val="0">' + $.lang.get("ui.component.calendar.janurary-text")+ '</li>');
        html.push('			<li val="1">' + $.lang.get("ui.component.calendar.february-text")+ '</li>');
        html.push('			<li val="2">' + $.lang.get("ui.component.calendar.march-text")+ '</li>');
        html.push('			<li val="3">' + $.lang.get("ui.component.calendar.april-text")+ '</li>');
        html.push('			<li val="4">' + $.lang.get("ui.component.calendar.may-text")+ '</li>');
        html.push('			<li val="5">' + $.lang.get("ui.component.calendar.june-text")+ '</li>');
        html.push('			<li val="6">' + $.lang.get("ui.component.calendar.july-text")+ '</li>');
        html.push('			<li val="7">' + $.lang.get("ui.component.calendar.august-text")+ '</li>');
        html.push('			<li val="8">' + $.lang.get("ui.component.calendar.september-text")+ '</li>');
        html.push('			<li val="9">' + $.lang.get("ui.component.calendar.october-text")+ '</li>');
        html.push('			<li val="10">' + $.lang.get("ui.component.calendar.november-text")+ '</li>');
        html.push('			<li val="11">' + $.lang.get("ui.component.calendar.december-text")+ '</li>');
        html.push('		</ul>');
        html.push('	</div>');
        html.push('</div>');
        //年月选择 结束

        //周 开始
        html.push('<div class="week">');
        html.push('	<ul>');
        html.push('		<li>' + $.lang.get("ui.component.calendar.sunday-text")+ '</li>');
        html.push('		<li>' + $.lang.get("ui.component.calendar.monday-text")+ '</li>');
        html.push('		<li>' + $.lang.get("ui.component.calendar.tuesday-text")+ '</li>');
        html.push('		<li>' + $.lang.get("ui.component.calendar.wednesday-text")+ '</li>');
        html.push('		<li>' + $.lang.get("ui.component.calendar.thursday-text")+ '</li>');
        html.push('		<li>' + $.lang.get("ui.component.calendar.friday-text")+ '</li>');
        html.push('		<li>' + $.lang.get("ui.component.calendar.saturday-text")+ '</li>');
        html.push('	</ul>');
        html.push('</div>');
        //周 结束

        //日期 开始
        html.push('<div class="day">');
        html.push('	<ul>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');

        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');

        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');

        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');

        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');

        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('		<li></li>');
        html.push('	</ul>');
        html.push('</div>');
        //日期 结束

        //时间 开始
        html.push('<div class="time" style="display:none">');
        html.push('	<div class="hour">');
        html.push('		<div class="input"><input type="text" value="00" maxlength="2" /></div>');
        html.push('		<div class="text">' + $.lang.get("ui.component.calendar.hour-text") + '</div>');
        html.push('		<div class="option" style="display:none">');
        html.push('			<ul>');
        html.push('				<li>1</li>');
        html.push('				<li>2</li>');
        html.push('				<li>3</li>');
        html.push('				<li>4</li>');
        html.push('				<li>5</li>');
        html.push('				<li>6</li>');
        html.push('				<li>7</li>');
        html.push('				<li>8</li>');
        html.push('				<li>9</li>');
        html.push('				<li>10</li>');
        html.push('				<li>11</li>');
        html.push('				<li>12</li>');
        html.push('				<li>13</li>');
        html.push('				<li>14</li>');
        html.push('				<li>15</li>');
        html.push('				<li>16</li>');
        html.push('				<li>17</li>');
        html.push('				<li>18</li>');
        html.push('				<li>19</li>');
        html.push('				<li>20</li>');
        html.push('				<li>21</li>');
        html.push('				<li>22</li>');
        html.push('				<li>23</li>');
        html.push('				<li>24</li>');
        html.push('			</ul>');
        html.push('		</div>');
        html.push('	</div>');
        html.push('	<div class="min">');
        html.push('		<span class="input"><input type="text" value="00" maxlength="2" /></span>');
        html.push('		<span class="text">' + $.lang.get("ui.component.calendar.minutes-text") + '</span>');
        html.push('		<div class="option" style="display:none">');
        html.push('			<ul>');
        html.push('				<li>0</li>');
        html.push('				<li>5</li>');
        html.push('				<li>10</li>');
        html.push('				<li>15</li>');
        html.push('				<li>20</li>');
        html.push('				<li>25</li>');
        html.push('				<li>30</li>');
        html.push('				<li>35</li>');
        html.push('				<li>40</li>');
        html.push('				<li>45</li>');
        html.push('				<li>50</li>');
        html.push('				<li>55</li>');
        html.push('			</ul>');
        html.push('		</div>');
        html.push('	</div>');
        html.push('	<div class="sec">');
        html.push('		<span class="input"><input type="text" value="00" maxlength="2" /></span>');
        html.push('		<span class="text">' + $.lang.get("ui.component.calendar.second-text") + '</span>');
        html.push('		<div class="option" style="display:none">');
        html.push('			<ul>');
        html.push('				<li>0</li>');
        html.push('				<li>5</li>');
        html.push('				<li>10</li>');
        html.push('				<li>15</li>');
        html.push('				<li>20</li>');
        html.push('				<li>25</li>');
        html.push('				<li>30</li>');
        html.push('				<li>35</li>');
        html.push('				<li>40</li>');
        html.push('				<li>45</li>');
        html.push('				<li>50</li>');
        html.push('				<li>55</li>');
        html.push('			</ul>');
        html.push('		</div>');
        html.push('	</div>');
        html.push('</div>');
        //时间 结束

        //快捷选择开始
        html.push('<div class="shortcut" style="display:none;">');
        html.push('	<ul>');
        html.push('		<li tag="HalfYearAgo">' + $.lang.get("ui.component.calendar.quicksel-btn-hyaday-text") + '</li>');
        html.push('		<li tag="ThreeMonthAgo">' + $.lang.get("ui.component.calendar.quicksel-btn-tmaday-text") + '</li>');
        html.push('		<li tag="PrevMonthToday">' + $.lang.get("ui.component.calendar.quicksel-btn-pmtday-text") + '</li>');
        html.push('		<li tag="Today">' + $.lang.get("ui.component.calendar.quicksel-btn-today-text") + '</li>');
        html.push('		<li tag="NextMonthFirst">' + $.lang.get("ui.component.calendar.quicksel-btn-nmfday-text") + '</li>');
        html.push('		<li tag="NextMonthLast">' + $.lang.get("ui.component.calendar.quicksel-btn-nmlday-text") + '</li>');
        html.push('		<li tag="NextYearToday">' + $.lang.get("ui.component.calendar.quicksel-btn-nytday-text") + '</li>');
        html.push('		<li tag="2050">' + $.lang.get("ui.component.calendar.quicksel-btn-2050day-text") + '</li>');
        html.push('	</ul>');
        html.push('</div>');
        //快捷选择结束

        //按钮开始
        html.push('<div class="fn">');
        html.push('	<button tag="clear" type="button" class="e_button-l e_button-navy">' + $.lang.get("ui.component.calendar.btn-clear-text") + '</button>');
        html.push('	<button tag="quicksel" type="button" class="e_button-l e_button-navy"><span>' + $.lang.get("ui.component.calendar.btn-quicksel-text") + '</span><span tag="fold" class="e_ico-fold"></span></button>');
        html.push('	<button tag="ok" type="button" class="e_button-l e_button-blue">' + $.lang.get("ui.component.calendar.btn-ok-text") + '</button>');
        html.push('</div>');
        //按钮 结束

        html.push('</div>');

        return html.join('');
    }

    function _createCalendar(el, action, settings){
        if(el && $.isString(el))
            el = doc.getElementById(el);

        if(!el || !el.nodeType)
            return;


        var s = $.extend({}, settings);

        $(el)[action]( _buildCalendar(s) );

        window[s.id] = new Wade.Calendar(s.id, {
            now: s.now ? s.now : new Date(),
            value: s.value ? s.value : null,
            format: s.format ? s.foramt : "yyyy-MM-dd",
            useMode : s.useMode ? s.useMode : null,
            useTime: true === s.useTime ? true : false,
            useLunar: true === s.useLunar ? true : false
        });

        return window[s.id];
    }

    window.Calendar = $.Calendar = Calendar;

    /*
     农历支持
     */
    var lunarInfo = new Array(19416, 19168, 42352, 21717, 53856, 55632, 91476, 22176, 39632, 21970, 19168, 42422, 42192, 53840, 119381, 46400, 54944, 44450, 38320, 84343, 18800, 42160, 46261, 27216, 27968, 109396, 11104, 38256, 21234, 18800, 25958, 54432, 59984, 28309, 23248, 11104, 100067, 37600, 116951, 51536, 54432, 120998, 46416, 22176, 107956, 9680, 37584, 53938, 43344, 46423, 27808, 46416, 86869, 19872, 42448, 83315, 21200, 43432, 59728, 27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051, 55632, 23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840, 54616, 46400, 46496, 103846, 38320, 18864, 43380, 42160, 45690, 27216, 27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859, 59984, 27480, 21952, 43872, 38613, 37600, 51552, 55636, 54432, 55888, 30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240, 47780, 44368, 21977, 19360, 42416, 86390, 21168, 43312, 31060, 27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576, 23200, 30371, 38608, 19415, 19152, 42192, 118966, 53840, 54560, 56645, 46496, 22224, 21938, 18864, 42359, 42160, 43600, 111189, 27936, 44448);
    var solarMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31); //公历月日期
    var Gan = new Array("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"); //天干
    var Zhi = new Array("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"); //地支
    var Animals = new Array("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"); //生肖
    var solarTerm = new Array("小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"); //节气
    var sTermInfo = new Array(0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758); //节气数据
    var lFtv = new Array("0101 春节", "0115 元宵节", "0505 端午节", "0707 七夕", "0715 中元节", "0815 中秋节", "0909 重阳节", "1208 腊八节", "1224 小年", "0100 除夕"); //农历节日
    var sFtv = new Array("0101 元旦", "0214 情人节", "0308 妇女节", "0312 植树节", "0315 消费者权益日", "0401 愚人节", "0501 劳动节", "0504 青年节", "0601 儿童节", "0701 建党节", "0801 建军节", "0910 教师节", "1001 国庆节", "1225 圣诞节"); //公历节日
    var nStr1 = new Array("日", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"); //农历字符
    var nStr2 = new Array("初", "十", "廿", "卅", "　");  //农历字符

    /*
     农历日期对象，使用Date入参初始化
     */
    var LunarDate = function(date){
        var that = this;
        that.sdate = date;
        var i, leap = 0, temp = 0;
        var baseDate = new Date(1900, 0, 31);
        var offset = (that.sdate - baseDate) / 864e5;
        that.dayCyl = offset + 40;
        that.monCyl = 14;
        for (i = 1900; i < 2050 && offset > 0; i++) {
            temp = _lYearDays(i);
            offset -= temp;
            that.monCyl += 12;
        }
        if (offset < 0) {
            offset += temp;
            i--;
            that.monCyl -= 12;
        }
        that.year = i;
        that.yearCyl = i - 1864;
        leap = _leapMonth(i);
        that.isLeap = false;
        for (i = 1; i < 13 && offset > 0; i++) {
            if (leap > 0 && i == leap + 1 && that.isLeap == false) {
                --i;
                that.isLeap = true;
                temp = _leapDays(that.year);
            } else {
                temp = _monthDays(that.year, i);
            }
            if (that.isLeap == true && i == leap + 1) that.isLeap = false;
            offset -= temp;
            if (that.isLeap == false) that.monCyl++;
        }
        if (offset == 0 && leap > 0 && i == leap + 1) if (that.isLeap) {
            that.isLeap = false;
        } else {
            that.isLeap = true;
            --i;
            --that.monCyl;
        }
        if (offset < 0) {
            offset += temp;
            --i;
            --that.monCyl;
        }
        that.month = i;
        that.day = parseInt(offset + 1);
    };

    LunarDate.prototype = {
        chineseZodiac: function(){ //年份生肖
            var that = this;
            if(that.chineseZodiacText == undefined && that.year){
                that.chineseZodiacText = Animals[this.year % 12 - 4];
                if(!that.chineseZodiacText) that.chineseZodiacText = "";
            }
            return that.chineseZodiacText;
        },
        chineseEra: function(){ //年份天干地支
            var that = this;
            if(that.chineseEraText == undefined && that.year){
                var num = (this.year - 1900 + 36);
                that.chineseEraText = Gan[num % 10] + Zhi[num % 12];
                if(!that.chineseEraText) that.chineseEraText = "";
            }
            return that.chineseEraText;
        },
        festival: function(){  //节日
            var that = this;
            if(that.festivalText == undefined){
                for(var i = 0; i < lFtv.length; i++) {
                    var tmp1, tmp2;
                    if (lFtv[i].match(/^(\d{2})(\d{2})([\s*])(.+)$/)) {
                        tmp1 = parseInt(RegExp.$1) - that.month;
                        tmp2 = parseInt(RegExp.$2) - that.day;
                        if (tmp1 == 0 && tmp2 == 0){
                            that.festivalText = RegExp.$4;
                        }
                    }
                }
                if(!that.festivalText) that.festivalText = "";
            }
            return that.festivalText;
        },
        solarTerm: function(){ //24节气
            var that = this;
            if(that.solarTermText == undefined){
                var syear = that.sdate.getFullYear(), smonth = that.sdate.getMonth(), sdate = that.sdate.getDate();
                var tmp1 = new Date(31556925974.7 * (syear - 1900) + sTermInfo[smonth * 2 + 1] * 6e4 + Date.UTC(1900, 0, 6, 2, 5));
                var tmp2 = tmp1.getUTCDate();
                if (tmp2 == sdate) that.solarTermText = solarTerm[smonth * 2 + 1];
                tmp1 = new Date(31556925974.7 * (syear - 1900) + sTermInfo[smonth * 2] * 6e4 + Date.UTC(1900, 0, 6, 2, 5));
                tmp2 = tmp1.getUTCDate();
                if (tmp2 == sdate) that.solarTermText = solarTerm[smonth * 2];
                if(!that.solarTermText) that.solarTermText = "";
            }
            return that.solarTermText;
        },
        lmonth: function(){
            var that = this, s;
            switch (that.month) {
                case 1:
                    s = "正月";
                    break;
                case 2:
                    s = "二月";
                    break;
                case 3:
                    s = "三月";
                    break;
                case 4:
                    s = "四月";
                    break;
                case 5:
                    s = "五月";
                    break;
                case 6:
                    s = "六月";
                    break;
                case 7:
                    s = "七月";
                    break;
                case 8:
                    s = "八月";
                    break;
                case 9:
                    s = "九月";
                    break;
                case 10:
                    s = "十月";
                    break;
                case 11:
                    s = "冬月";
                    break;
                case 12:
                    s = "腊月";
                    break;
                default:
                    break;
            }
            return s;
        },
        lday: function(){
            var that = this, s;
            switch (that.day) {
                case 10:
                    s = "初十";
                    break;
                case 20:
                    s = "二十";
                    break;
                case 30:
                    s = "三十";
                    break;
                default:
                    s = nStr2[Math.floor(that.day / 10)];
                    s += nStr1[that.day % 10];
                    break;
            }
            return s;
        }
    };

    function _lYearDays(y) {
        var i, sum = 348;
        for (i = 32768; i > 8; i >>= 1) sum += lunarInfo[y - 1900] & i ? 1 : 0;
        return sum + _leapDays(y);
    }

    function _leapDays(y) {
        if (_leapMonth(y)) return lunarInfo[y - 1900] & 65536 ? 30 : 29; else return 0;
    }

    function _leapMonth(y) {
        return lunarInfo[y - 1900] & 15;
    }

    function _monthDays(y, m) {
        return lunarInfo[y - 1900] & 65536 >> m ? 30 : 29;
    }

    /*
     公历节日
     */
    Date.prototype.festival = function(){
        var that = this;
        if(that.festivalText == undefined){
            for(var i = 0; i < sFtv.length; i++) {
                var tmp1, tmp2;
                if (sFtv[i].match(/^(\d{2})(\d{2})([\s*])(.+)$/)) {
                    tmp1 = parseInt(RegExp.$1) - (that.getMonth() + 1);
                    tmp2 = parseInt(RegExp.$2) - that.getDate();
                    if (tmp1 == 0 && tmp2 == 0) that.festivalText = RegExp.$4;
                }
            }
            if(!that.festivalText) that.festivalText = "";
        }
        return that.festivalText;
    };

    window.LunarDate = $.LunarDate = LunarDate;

})(window.Wade, window, document);