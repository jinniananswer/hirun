/*!
 * frame component
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2015, WADE
 */
(function ($, window, doc) {
	"use strict";
	
	if( !$ || typeof $.Frame != "undefined" )
		return;
	
	var Frame = function(el, settings){
		var that = this;
		that.el = el && el.nodeType == 1 ? el : doc.getElementById(el);
		if( !that.el || !that.el.nodeType || !(that.id = $.attr(that.el, "id")) )
			return;
		
		if(settings && $.isObject(settings))
			$.extend(that, settings);
		
		if(!$.attr(that.el, "x-wade-uicomponent")){	
			$.attr(that.el, "x-wade-uicomponent", "frame");	
		}
		
		that._init();	
		
		that.constructor.call(that);	
	};
	
	Frame.prototype = $.extend(new $.UIComponent(), {
		init: function(){
			var that = this;
			
			if(true === that.inited) return;
			
			if(that.page && $.isString(that.page)){
				that.redirect(that.page, that.listener, that.params, that.subsys);
				that.inited = true;
			}else if(that.src && $.isString(that.src)){
				that.redirectByUrl(that.src);
				that.inited = true;
			}
		},
		focus : function(){
			var that = this;
			
			var win = that.el.contentWindow;
			try{
				if(win){
					win.focus();
				}
			}catch(ex){
			}
			win = null;
		},
		reload: function(){
			var that = this;
			that.redirect(that.page, that.listener, that.params, that.subsys);
		},
		redirect: function(page, listener, params, subsys){
			var that = this;
			
			if(!page || !$.isString(page))
				return;
				
			that.beginLoading();
			that.el.src = $.redirect.buildUrl(subsys, page,listener, params);
		},
		reset: function(page, listener, params, subsys, title){ //重置
			var that = this;
			if(!page || !$.isString(page))
				return;
			
			var isReset = ( $.md5(page + listener + params + subsys) !== $.md5(that.page + that.listener + that.params + that.subsys) );
			if(isReset){
				that.page = page;
				that.listener = listener;
				that.params = params;
				that.subsys = subsys;
				
				//设置初始状态
				that.inited = false;
				that.beginLoading();
			}
			
			if(title){
				that.title = title;
			}
			
			return isReset;
		},
		reloadByUrl: function(){
			var that = this;
			that.redirectByUrl(that.src);
		},
		redirectByUrl: function(url){
			var that = this;
			
			if(!url || !$.isString(url))
				return;
				
			that.beginLoading();
			that.el.src = url;
		},
        forceResetByUrl: function(url, title){
            var that = this;

            if(!url || !$.isString(url))
                return;

			that.src = url;

			//设置初始状态
			that.inited = false;
			that.beginLoading();

            if(title){
                that.title = title;
            }

            return true;
        },
		resetByUrl: function(url, title){
			var that = this;
			
			if(!url || !$.isString(url))
				return;
				
			var isReset = ( $.md5(url) !== $.md5(that.src) );
			if(isReset){
				that.src = url;
				
				//设置初始状态
				that.inited = false;
				that.beginLoading();
			}
			
			if(title){
				that.title = title;
			}
			
			return isReset;
		},
		setAttribute: function(name, val, prop){
			var that = this;
			
			if(!name || !$.isString(name))
				return;
			
			if(!val || !$.isString(val))
				return;
			
			$.attr(that.el, name, val);
			
			if(true === prop){
				that[name] = val;
			}
		},
		beginLoading: function(text){
			var that = this;
			
			if(text && $.isString(text)){
				that.loadingTxtEl.innerHTML = text;
			}
			
			that.el.style.display = "none";
			that.loadingEl.style.display = "";	
			that.loading = true;		
		},
		endLoading: function(){
			var that = this;
			
			that.loadingEl.style.display = "none";
			that.el.style.display = "";
			that.loading = false;
		},
		adjust: function(){
			var that = this;
			var win = that.el.contentWindow;
			try{
				if(win && win.document && win.$ && win.$.expando){
					//适应Scroller高度
					if(win.$.Scroller){
						win.$.Scroller.refresh();
					}
					//适应Table高度
					if(win.$.Table){
						win.$.Table.adjust();
					}		
				}
			}catch(ex){
				console.log(ex)
			}finally{
				win = null;
			}
		},
		destroy: function(){
			var that = this;
			
			that.loadingEl = null;
			that.loadingTxtEl = null;
			
			that.el = null;
		},
		_init: function(){
			var that = this;
			
			that.loadingEl = $(that.el).next("div.c_msg-loading:first")[0];
			that.loadingTxtEl = $(that.loadingEl).find("div.title:first")[0];
			
			if( "none" != that.el.style.display ){
				that.el.style.display = "none";
			}
			
			/*************** 绑定事件 开始 **************/
			$(that.el).bind($.browser.msie && window.attachEvent ? "readystatechange" : "load", function(e){
				//For IE
				if($.browser.msie && window.attachEvent && "complete" != this.readyState)
					return;
				
				//先展示内容
				if(true === that.loading){
					that.endLoading();
				}	
				
				//设置弹出窗口标题
				if(that.title && $.isString(that.title)){
					var win = that.el.contentWindow;
					try{
						if(win && win.document && win.$ && win.$.expando){
							//设置标题
							win.$("div[class=c_header]:first", win.document.body).find("div[class=back]:first").text(that.title);
							//适应Scroller高度
							if(win.$.Scroller){
								win.$.Scroller.refresh();
							}
							//适应Table高度
							if(win.$.Table){
								win.$.Table.adjust();
							}
							//处理readonly元素
							if($.browser.msie){ //判断为IE
								win.$("input,textarea,select").each(function(){
									if(this.readOnly){
										this["onfocus"] = function(){this.blur();};
									}
								});	
							}
							
							//获取焦点
							//win.focus();
						}
					}catch(ex){
						console.log(ex);
					}finally{
						win = null;
					}
				}
					
				
			});
			/*************** 绑定事件 结束 **************/
			
			//加载url
			if(true === that.autoInit){
				that.init();
			}
		}
	});
	
	//export 
	window.Frame = $.Frame = Frame;
})(window.Wade, window, document);	