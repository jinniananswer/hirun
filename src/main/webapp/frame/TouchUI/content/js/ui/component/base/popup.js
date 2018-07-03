/*!
 * popup component
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2015, WADE
 */
(function ($, window, doc) {
	"use strict";

	if( !$ || typeof $.Popup != "undefined" )
		return;
	
	var m = Math,
		dummyStyle = doc.createElement('div').style,
		vendor = (function () {
			var vendors = 't,webkitT,MozT,msT,OT'.split(','),
				t,
				i = 0,
				l = vendors.length;
	
			for ( ; i < l; i++ ) {
				t = vendors[i] + 'ransform';
				if ( t in dummyStyle ) {
					return vendors[i].substr(0, vendors[i].length - 1);
				}
			}
	
			return false;
		})(),
		cssVendor = vendor ? '-' + vendor.toLowerCase() + '-' : '',
	
		// Style properties
		transform = prefixStyle('transform'),
		transitionProperty = prefixStyle('transitionProperty'),
		transitionDuration = prefixStyle('transitionDuration'),
		transformOrigin = prefixStyle('transformOrigin'),
		transitionTimingFunction = prefixStyle('transitionTimingFunction'),
		transitionDelay = prefixStyle('transitionDelay'),
	
	    has3d = prefixStyle('perspective') in dummyStyle,
	    hasTouch = typeof $.hasTouch != "undefined" ? $.hasTouch : "ontouchstart" in window,
	    hasTransform = vendor !== false,
	    hasTransitionEnd = prefixStyle('transition') in dummyStyle,
	    
		TRNEND_EV = (function () {
			if ( vendor === false ) return false;
	
			var transitionEnd = {
					''			: 'transitionend',
					'webkit'	: 'webkitTransitionEnd',
					'Moz'		: 'transitionend',
					'O'			: 'otransitionend',
					'ms'		: 'MSTransitionEnd'
				};
	
			return transitionEnd[vendor];
		})(),
	
		// Helpers
		translateZ = has3d ? ' translateZ(0)' : '';	
		dummyStyle[transform] = translateZ;
		
	var push = Array.prototype.push,
		splice = Array.prototype.splice;
	
	var phoneMode = $.os.phone || true === $.ratioPhone; //手机模式
	var maxLevel = 8; //最大层级
	var boxWidth = 57.14; //单位em
	var halfBoxWidth = Math.round(boxWidth/2 * 100)/100;
	var regexpLevel = /^[1-9]{1}$/;
	var regexpItem = /^[1-9]{1}$/;

	var popups = [];
	
	var Popup = function(el, settings){
		var that = this;
		
		that.el = el && el.nodeType == 1 ? el : doc.getElementById(el);
		if( !that.el || !that.el.nodeType || !(that.id = $.attr(that.el, "id")) )
			return;
			
		if(settings && $.isObject(settings))
			$.extend(that, settings);
		
		if(!$.attr(that.el, "x-wade-uicomponent")){
			$.attr(that.el, "x-wade-uicomponent", "popup");
		}	
		
		that._init();
		
		that.constructor.call(that);
	};
	
	Popup.prototype = $.extend(new $.UIComponent(), {
		handleEvent: function (e) {
			var that = this;
			switch(e.type) {
				case TRNEND_EV: that._transitionEnd(e); break;
			}
		},
		show: function(item, reset){  //item 第一层默认显示item, reset 是否重置为第一层
			var that = this;
			
			//避免弹窗没隐藏的情况下，重复调用showPoup重置到第一层
			if(true === that.visible && !reset) return;
			
			//需要隐藏其它已显示的popup弹出
			if(true !== that.visible && true !== that.mask && true !== that.overlap){ //只有无mask的Popup才限制同时只显示一个
				var popupObj;
				$.each(popups, function(idx, popupId){
					if(popupId != that.id){
						popupObj = window[popupId];
						if(popupObj.visible == true && popupObj.mask == that.mask){ //根据mask判断是否同一类型
							popupObj.hide();
						}
					}
				});
			}
	
			//显示标记的c_popupItem，必须在showItem前执行
			var items = $("div[visible-snapshot=true]", that.el);
			items.attr("visible", true);
			items.attr("visible-snapshot", false);
			if(hasTransform){
				items.css("visibility", "visible");
			}else{
				items.css("display", "");
			}
			items = null;
			
			//重置
			if(false !== reset){
				that.reset();
				
				//设置第一层要显示的item
				that._showItem(1, item);
				that._itemPath = [item]; //重置
			}
			
			that.el.style.zIndex = $.zIndexer.get(that.id); //设置zIndex
			that.el.style.visibility = "visible";
			
			if(hasTransform){
				setTimeout(function(){
					that.el.style[transitionProperty] = cssVendor + 'transform';
					that.el.style[transform] = 'translate(0,0)' + translateZ;
				}, 1);
			}

			that.visible = true;

			if(true === that.mask){
				//window.focus();
			}
		},
		hide: function(){
			var that = this;
			
			if(false === that.visible) return;
			
			if( false === $.event.trigger("hideAction", null, that.el) )
				return;
			
			$.zIndexer.remove(that.id);
			
			if(hasTransform){
				setTimeout(function(){
					that.el.style[transitionProperty] = cssVendor + 'transform';
					that.el.style[transform] = 'translate(100%,0)' + translateZ;
				}, 1);
			}else{
				that.el.style.visibility = "hidden";
				
				//隐藏已显示的c_popupItem
				var items = $("div[visible=true]", that.el);
				items.attr("visible", false);
				items.attr("visible-snapshot", true);
				items.css("display", "none"); //IE8 用display控制
				items = null;
			}
			
			that.visible = false;
		},
		reset: function(){
			var that = this;
			var transProp = ( true === that.visible ? cssVendor + 'transform' : "none" ); //弹窗已显示，则使用动画
			
			//重置到第一层			
			if(that.isFull || that.isWide){
				if(hasTransform){
					that.wrapper.style[transitionProperty] = transProp;
					that.wrapper.style[transform] = 'translate(0,0)' + translateZ;
				}else{
					that.wrapper.style.marginLeft = "";
				}
			}else if(that.isHalf){
				if(hasTransform){
					if(that.isHalfBg){
						that.box.style[transitionProperty] = cssVendor + 'transform';
						that.box.style[transform] = 'translate(0,0)' + translateZ;
					}else{
						that.el.style[transitionProperty] = cssVendor + 'transform';
						that.el.style[transform] = 'translate(0,0)' + translateZ;
					}
					/*
					if(that.isHalfBg){
						that.box.style[transitionProperty] = transProp;
						that.box.style[transform] = 'translate(50%,0) ' + translateZ;
					}else{
						that.el.style[transitionProperty] = transProp;
						that.el.style[transform] = 'translate(100%,0) ' + translateZ;
					}
					*/
				}else{
					that.el.style.left = "";
					that.box.style.left = "";
					that.wrapper.style.marginLeft = "";
				}
			}else{
				if(!phoneMode){
					if(hasTransform){
						that.box.style[transitionProperty] = transProp;
						that.box.style[transform] = 'translate(0,0) ' + translateZ;
						that.wrapper.style[transitionProperty] = cssVendor + 'transform';
						that.wrapper.style[transform] = 'translate(0,0)' + translateZ;
					}else{
						that.box.style.left = halfBoxWidth + "em";
						that.wrapper.style.marginLeft = "";
					}
				}else{
					if(hasTransform){
						that.wrapper.style[transitionProperty] = transProp;
						that.wrapper.style[transform] = 'translate(0,0)' + translateZ;
					}else{
						that.wrapper.style.marginLeft = "";
					}
				}
			}
			
			//重置级别为1
			that.level = 1;
		},
		forward: function(node, item){  //node 触发调用forward的dom元素,用以判定当前操作元素在第几层
			var that = this;
			
			var optGroup = null, optLevel = null;
			if(node && node.nodeType){
				optGroup = _findGroupByParent(node);
				optLevel = parseInt($.attr(optGroup, "level"));
			}
			
			var nextGroup = null, nextLevel = null;
			if(optGroup){
				var n = 0, next = optGroup.nextSibling;
				while(n < 20 && next){
					if(next.nodeType && $.nodeName(next, "div")
							&& (" " + next.className + " ").indexOf(" c_popupGroup ") > -1){
						nextGroup = next;	
						nextLevel = parseInt($.attr(next, "level"));
						break;
					}
					next = next.nextSibling
					n ++;
				}
			}else{
				nextLevel = that.level + 1;
				nextGroup = that._findGroup(nextLevel);
			}
			
			if(nextLevel > maxLevel) return;
			
			if(nextGroup){
				
				if(undefined == item) item = 1; //默认为第一项
				
				//设置要显示的item
				that._showItem(nextGroup, item);
				
				if(that.isFull || that.isWide){
					if(hasTransform){
						setTimeout(function(){
							that.wrapper.style[transitionProperty] = cssVendor + 'transform';
							that.wrapper.style[transform] = 'translate(-' + (12.5 * (nextLevel - 1)) + '%,0)' + translateZ;
						}, 1);
					}else{
						that.wrapper.style.marginLeft = '-' + (100 * (nextLevel - 1)) + "%";
					}
				}else if(that.isHalf){
					if(hasTransform){
						if(phoneMode){ //手机上和Full一致
							setTimeout(function(){
								that.wrapper.style[transitionProperty] = cssVendor + 'transform';
								that.wrapper.style[transform] = 'translate(-' + (12.5 * (nextLevel - 1)) + '%,0)' + translateZ;
							}, 1);
						}else{
							if(that.isHalfBg){
								if(nextLevel == 2){
									setTimeout(function(){
										that.box.style[transitionProperty] = cssVendor + 'transform';
										that.box.style[transform] = 'translate(-50%,0)' + translateZ;
									}, 1);
								}
							}else{
								if(nextLevel == 2){
									setTimeout(function(){
										that.el.style[transitionProperty] = cssVendor + 'transform';
										that.el.style[transform] = 'translate(-50%,0)' + translateZ;
									}, 1);
								}
							}
							if(nextLevel >= 3){
								setTimeout(function(){
									that.wrapper.style[transitionProperty] = cssVendor + 'transform';
									that.wrapper.style[transform] = 'translate(-' + (12.5 * (nextLevel - 2)) + '%,0)' + translateZ;
								}, 1);
							}
						}
					}else{
						if(that.isHalfBg){
							if(nextLevel == 2){
								that.box.style.left = "0";
							}
						}else{
							if(nextLevel == 2){
								that.el.style.left = "0";
							}
						}
						if(nextLevel >= 3){
							that.wrapper.style.marginLeft = "-" + (50 * (nextLevel - 2)) + "%";
						}
					}
				}else{			
					if(!phoneMode && nextLevel == 2){
						if(hasTransform){
							setTimeout(function(){
								that.box.style[transitionProperty] = cssVendor + 'transform';
								that.box.style[transform] = 'translate(-' + halfBoxWidth + 'em,0)' + translateZ;
							}, 1);
						}else{
							that.box.style.left = "0";
						}
					}
					
					var start = phoneMode ? 2 : 3;
					if(nextLevel >= start){
						if(hasTransform){
							setTimeout(function(){
								that.wrapper.style[transitionProperty] = cssVendor + 'transform';
								that.wrapper.style[transform] = 'translate(-' + (halfBoxWidth * (nextLevel - start + 1)) + 'em,0)' + translateZ;
							}, 1);
						}else{
							that.wrapper.style.marginLeft = '-' + (halfBoxWidth * (nextLevel - start + 1)) + 'em';
						}
					}
				}
				
				that.level = nextLevel;
				
				that._itemPath[that.level - 1] = item; //由于forward可能用于切换，所以不能使用push操作
			}
		},
		back: function(node, item, el, value){ //node 触发调用forward的dom元素,用以判定当前操作元素在第几层,支持返回时带回数据到上一个界面
			var that = this;
			
			//处理参数
			if($.isObject(item)){
				value = el;
				el = item;
				item = null;
			}
			
			/*
			* 由于返回只能逐级返回, 按当前级别 -1
			*/
			var prevLevel = that.level - 1;	
			if(prevLevel < 1) { //如果已经是第一级，则隐藏Popup
				that.hide();
				return; 
			}
		
			//查找当前gorup
			var optGroup = null, optLevel = null;
			if(node && node.nodeType){
				optGroup = _findGroupByParent(node);
				optLevel = parseInt($.attr(optGroup, "level"));
			}
			
			//隐藏当前group下的item
			if(optGroup){
				var item, childs = optGroup.childNodes;
				var i = 0, node, nodeClassName;
				while(i < childs.length){
					node = childs[i];
					if(node.nodeType == 1 && $.nodeName(node, "div")){
						nodeClassName = node.className ? node.className : ""; 
						//清除 c_popupItem-show 样式
						if( (" " + nodeClassName + " ").indexOf(" c_popupItem ") > -1 && (" " + nodeClassName + " ").indexOf(" c_popupItem-show ") > -1 ){
							node.className = $.trim( ( " " + nodeClassName + " " ).replace(/ c_popupItem-show /g, " ") );
						}
					}
					i ++;	
				}
				node = nodeClassName = null;
				item = childs = null;
			}
			
			var prevGroup = that._findGroup(prevLevel);
			if(prevGroup){
				
				if(!item) item = that._itemPath[prevLevel - 1];
				
				//设置要显示的item
				var itemNode = that._showItem(prevGroup, item);
				
				if(that.isFull || that.isWide){
					if(hasTransform){
						setTimeout(function(){
							that.wrapper.style[transitionProperty] = cssVendor + 'transform';
							that.wrapper.style[transform] = 'translate(-' + (12.5 * (prevLevel - 1)) + '%,0)' + translateZ;
						}, 1);
					}else{
						that.wrapper.style.marginLeft = '-' + (100 * (prevLevel - 1)) + "%";
					}
				}else if(that.isHalf){
					if(hasTransform){
						if(phoneMode){
							setTimeout(function(){
								that.wrapper.style[transitionProperty] = cssVendor + 'transform';
								that.wrapper.style[transform] = 'translate(-' + (12.5 * (prevLevel - 1)) + '%,0)' + translateZ;
							}, 1);
						}else{
							if(prevLevel == 1){
								if(that.isHalfBg){
									setTimeout(function(){
										that.box.style[transitionProperty] = cssVendor + 'transform';
										that.box.style[transform] = 'translate(0,0)' + translateZ;
									}, 1);
								}else{
									setTimeout(function(){
										that.el.style[transitionProperty] = cssVendor + 'transform';
										that.el.style[transform] = 'translate(0,0)' + translateZ;
									}, 1);
								}							
							}else if(prevLevel >= 2){
								setTimeout(function(){
									that.wrapper.style[transitionProperty] = cssVendor + 'transform';
									that.wrapper.style[transform] = 'translate(-' + (12.5 * (prevLevel - 2)) + '%,0)' + translateZ;
								}, 1);
							}
						}
					}else{
						if(prevLevel == 1){
							if(that.isHalfBg){
								that.box.style.left = "";
							}else{
								that.el.style.left = "";
							}
						}else if(prevLevel >= 2){
							that.wrapper.style.marginLeft = "-" + (50 * (prevLevel - 2)) + "%";
						}
					}
				}else{	
					if(!phoneMode && prevLevel == 1){
						if(hasTransform){
							setTimeout(function(){
								that.box.style[transitionProperty] = cssVendor + 'transform';
								that.box.style[transform] = 'translate(0,0) ' + translateZ;
							}, 1);
						}else{
							that.box.style.left = halfBoxWidth + "em";
						}
					}
					
					var start = phoneMode ? 2 : 3;
					if(prevLevel >= (start - 1)){
						if(hasTransform){
							setTimeout(function(){
								that.wrapper.style[transitionProperty] = cssVendor + 'transform';
								that.wrapper.style[transform] = 'translate(-' + (halfBoxWidth * (prevLevel - start + 1)) + 'em,0)' + translateZ;
							}, 1);
						}else{
							that.wrapper.style.marginLeft = '-' + (halfBoxWidth * (prevLevel - start + 1)) + 'em';
						}
					}
				}
				
				that.level = prevLevel;
				
				//删除itemPath
				splice.call(that._itemPath, that._itemPath.length - 1, 1);
				
				//设置返回值
				_fillElementValue(itemNode, el, value);
			}
			
			optGroup = optLevel = null;
			prevGroup = null;
		},
		append: function(node, title){ //创建结构
			var that = this;
			
			if(!node || !node.nodeType || !title || !$.isString(title))
				return;
			
			var optGroup = _findGroupByParent(node);
			if(!optGroup) return;
			
			var optLevel = parseInt($.attr(optGroup, "level"));
			
			var group;
			//查找已有group的情况
			var n = 0, next = optGroup.nextSibling;
			while(n < 10 && next){
				if(next.nodeType && $.nodeName(next, "div")
						&& (" " + next.className + " ").indexOf(" c_popupGroup ") > -1
						&& (optLevel + 1) == $.attr(next, "level")){
					group = next;	
					break;
				}
				next = next.nextSibling
				n ++;
			}
			
			var groupId = that.id + "_group" + (optLevel + 1);
			if(group && group.nodeType){
				var _groupId = $.attr(group, "id");
				if(!_groupId){
					$.attr(group, "id", groupId);
				}
			}else{
				$(that.wrapper).append('<div id="' + groupId + '" class="c_popupGroup" level="' + (optLevel + 1) + '"></div>');
				group = doc.getElementById(groupId);
			}
			
			var itemId = groupId + "_item" + $.md5(title);
			var frameId = "frame_" + itemId;
			
			var item = doc.getElementById(itemId);
			if(!item){
				$(group).append('<div id="' + itemId + '" class="c_popupItem"></div>');
				item = doc.getElementById(itemId);
			}
			var frameEl = doc.getElementById(frameId);
			if(!frameEl){
				var html = [];
				html.push('<iframe id="' + frameId + '" style="width:100%;height:100%;display:none" frameborder="0"></iframe>');
				html.push('<div id="' + frameId + '_loading" class="c_msg c_msg-full c_msg-loading">');
				html.push('<div class="wrapper">');
				html.push('<div class="emote"></div>');
				html.push('<div class="info"><div class="text"><div class="title" id=id="' + frameId + '_loading_txt">loading</div></div></div>');
				html.push('</div>');
				html.push('</div>');
				$(item).append(html.join(''));
				html = null;
				
				window[frameId] = new Wade.Frame(frameId, {autoInit:false, title:title});				
			}
			optGroup = frameEl = item = group = null;
			
			return itemId;
		},
		getFrame: function(frameId){
			return window[frameId];
		},
		setPopupReturnValue: function(el, value, close){
			var that = this;
			
			if(!el) return;
			
			if($.isPlainObject(el)) close = value;
			
			_fillElementValue(null, el, value, that.srcWindow ? that.srcWindow : null);
			
			if(false !== close){
				that.hide();
			}
			
			$.event.trigger("afterAction", null, that.el);
		},
		destroy: function(){
			var that = this;
			
			$.zIndexer.remove(that.id);
			
			if(hasTransform){
				that._unbind(TRNEND_EV);
			}
			
			var idx = $.inArray(that.id, popups);	
			if(idx > -1) splice.call(popups, idx, 1);
			
			that._itemPath = [];
			that._itemPath = null;
			
			that.srcWindow = null;
			
			that.bg = null;
			that.wrapper = null;
			that.box = null;
			
			that.el = null;
		},
		_init: function(){
			var that = this;
			
			that.level = 1; //初始值为1
			that.bg = $(that.el).children("div.c_popupBg:first")[0];	
			that.box = $(that.el).children("div.c_popupBox:first")[0];
			that.wrapper = $(that.el).find("div.c_popupWrapper:first")[0];		

			push.call(popups, that.id);
			
			var className = that.el.className ? that.el.className : "";
			that.isHalf = (" " + className + " ").indexOf(" c_popup-half ") > -1;
			that.isFull = (" " + className + " ").indexOf(" c_popup-full ") > -1;
			that.isWide = (" " + className + " ").indexOf(" c_popup-wide ") > -1;
			that.isHalfBg = (" " + className + " ").indexOf(" c_popup-half-hasBg ") > -1;
			that.isDefShow = that.defaultShowItemId || (" " + className + " ").indexOf(" c_popup-show ") > -1; //是否默认显示
			
			if( that.isHalf && !that.isHalfBg){
				that.mask = false;
				$(that.bg).remove();
				that.bg = null;
			} 
			
			if(that.bg && that.bg.nodeType){
				$(that.bg).tap(function(){
					var className = this.className ? this.className : "";
					if( !className || (" " + className + " ").indexOf(" c_popupBg ") ) 
						return;
					
					var id = $(this).parents("div.c_popup:first").attr("id");
					if(!id) return;
					var popup = window[id];
					if(popup && popup instanceof $.Popup){
						popup.hide();
					}
				});
			}
			
			var childs = that.wrapper.childNodes;
			if(childs && childs.length > 0){
				var i = 0, n = 0, node;
				while(i < childs.length){
					node = childs[i];
					if(node.nodeType && $.nodeName(node, "div") 
							&& (" " + node.className + " ").indexOf(" c_popupGroup ") > -1){ //根据tagName和样式名判定
						n ++;
						$.attr(node, "level", n); //在group元素上保存level标记
					}
					i ++;
				}
			}
			childs = null;
			
			//记录每级的item值，用于back操作
			that._itemPath = [];
						
			//初始化动画
			if(hasTransform){
				that.el.style[transitionProperty] = "none";
				that.el.style[transitionDuration] = '300ms';
				that.el.style[transitionTimingFunction] = 'ease-out'; 
				//that.el.style[transform] = 'translate(' + (that.isHalf && that.isHalfBg ? '50%' : '100%') + ',0)' + translateZ;
				that.el.style[transform] = 'translate(100%,0)' + translateZ;
				
				//box
				that.box.style[transitionProperty] = "none";
				that.box.style[transitionDuration] = '300ms';
				that.box.style[transitionTimingFunction] = 'ease-out'; 
				that.box.style[transform] = 'translate(0,0)' + translateZ;
				
				//wrapper
				that.wrapper.style[transitionProperty] = "none";
				that.wrapper.style[transitionDuration] = '300ms';
				that.wrapper.style[transitionTimingFunction] = 'ease-out';
				that.wrapper.style[transform] = 'translate(0,0)' + translateZ;
				
				that._bind(TRNEND_EV);
			}
			
			var defaultShowItemId = that.defaultShowItemId;
			$("div.c_popupItem", that.el).each(function(){
				var itemId = $.attr(this, "id");
				if(itemId && !defaultShowItemId){
					defaultShowItemId = itemId;
				}
				
				//处理默认显示
				var className = this.className ? this.className : "";
				if( (" " + className + " ").indexOf( " c_popupItem-show " ) > -1 ){
					//标记为需要显示
					this.setAttribute("visible-snapshot", true);
					/*
					this.style.visibility = "visible";
					this.setAttribute("visible", true);
					*/
				}
				
				if(hasTransform){
					this.style[transitionProperty] = "none";
					this.style[transitionDuration] = '300ms';
					this.style[transitionTimingFunction] = 'ease-out';
					this.style[transform] = 'translate(0,0)' + translateZ;
				}else{
					//IE8使用display模式
					this.style.visibility = "visible";
					this.style.display = "none";
				}
			});
			
			if(defaultShowItemId && that.isDefShow){
				that.show(defaultShowItemId);
				/*
				$(function(){
					that.show(defaultShowItemId);
				});
				*/
			}
		},
		_findGroup: function(_group){
			var that = this;
			
			if(!regexpLevel.test(_group) && !$.isString(_group)) 
				return null;
			
			if($.isNumber(_group) && (_group < 1 || _group > maxLevel))
				return null;
			
			var group = null;
			
			if($.isNumber(_group)){
				var childs = that.wrapper.childNodes;
				if(childs && childs.length > 0){
					var i = 0, n = 0, node;
					while(i < childs.length){
						node = childs[i];
						if(node.nodeType && $.nodeName(node, "div") 
								&& (" " + node.className + " ").indexOf(" c_popupGroup ") > -1){ //根据tagName和样式名判定
							n ++;
							if(_group == n){
								group = node;
								break;
							}
						}
						i ++;
					}
				}
			}else if($.isString(_group)){
				group = doc.getElementById(_group);
			}
			
			return group;
		},
		_showItem: function(_group, _item){
			var that = this;

			if(!regexpItem.test(_item) && !$.isString(_item)) return;
			
			var group = _group && _group.nodeType ? _group :  that._findGroup(_group);
			if(!group || !group.nodeType) return;
			
			//只有同级切换才使用动画
			var anim = $.attr(group, "level") == that.level;
			
			var item, childs = group.childNodes;
			var i = 0, n = 0, node, nodeClassName;
			while(i < childs.length){
				node = childs[i];
				if(node.nodeType == 1 && $.nodeName(node, "div")){
					
					nodeClassName = node.className ? node.className : ""; 
					if( (" " + nodeClassName + " ").indexOf(" c_popupItem ") > -1){
						n ++;
	
						//设置id为_item或者第_item个c_popupItem为显示状态
						if( ($.isNumber(_item) && n == _item) 
							|| ($.isString(_item) && _item == $.attr(node, "id")) ){
							
							item = node;
							
							node.setAttribute("visible", true);
							
							if("visible" !=  node.style.visibility){
								node.style.visibility = "visible";
							}
							if(!hasTransform){
								node.style.display = "";
							}
							
							if(hasTransform){
								setTimeout( (function(node){
									return function(){
										node.style[transitionProperty] = anim ? cssVendor + 'transform' : "none";
										node.style[transform] = 'translate(0,0)' + translateZ;
									};
								})(node), 1);
							
								//node.style[transitionProperty] = anim ? cssVendor + 'transform' : "none";
								//node.style[transform] = 'translate(0,0)' + translateZ;
							}
							
							/*
							if( (" " + nodeClassName + " ").indexOf(" c_popupItem-show ") < 0 ){
								node.className = $.trim( nodeClassName + " c_popupItem-show" );
							}
							*/
							_initPopupItemFrame(node, that.id); //初始化Frame  
							_refreshPopupItemScroller(node); //刷新Scroller组件
							_adjustPopupItemTable(node); //自适应Table组件
							
							/*
							if($.browser.msie){ //判断为IE
								_unfocusFormElement(node); //readonly表单元素不允许获得焦点
							}
							*/
						}else{
							//否则隐藏

							node.setAttribute("visible", false);
							
							if(hasTransform && !anim){
								node.style.visibility = "hidden";
							}else if(!hasTransform){
								node.style.display = "none";
							}
							
							/*
							if(!hasTransform || !anim){
								node.style.visibility = "hidden";
							}
							*/
							
							if(hasTransform){
								setTimeout( (function(node){
									return function(){
										node.style[transitionProperty] = anim ? cssVendor + 'transform' : "none";
										node.style[transform] = 'translate(100%,0)' + translateZ;
									};
								})(node), 1);
							}
							
						}
					}
				}
				i ++;
			}
			
			//返回显示的item元素
			return item;
		},
		_bind: function (type, el, bubble) {
			var that = this;
			if(doc.addEventListener){
				(el || that.el).addEventListener(type, that, !!bubble);
			}
		},
		_unbind: function (type, el, bubble) {
			var that = this;
			if(doc.removeEventListener){
				(el || that.el).removeEventListener(type, that, !!bubble);
			}
		},
		_transitionEnd: function(e){
			var that = this;
			var el = e.target;
			if(!el || !el.nodeType)
				return;
			
			var className = el.className ? el.className : "";
			if( (" " + className + " ").indexOf(" c_popup ") > -1 ){
				if(false == that.visible){
					el.style.visibility = "hidden";
					
					//隐藏已显示的c_popupItem
					var items = $("div[visible=true]", that.el);
					items.attr("visible", false);
					items.attr("visible-snapshot", true);
					items.css("visibility", "hidden");
					items = null;
				}
			}else if( (" " + className + " ").indexOf(" c_popupBox ") > -1 ){
				
			}else if( (" " + className + " ").indexOf(" c_popupWrapper ") > -1 ){
			
			}else if( (" " + className + " ").indexOf(" c_popupItem ") > -1 ){
				if( "false" == el.getAttribute("visible") ){
					el.style.visibility = "hidden";
				}
			}
			
			//判断是popup相关的元素才清除动画，避免和popup结构里其它组件的动画事件冲突
			if( (" " + className + " ").indexOf(" c_popup ") > -1 ){
				//清除动画属性
				el.style[transitionProperty] = "none";
			}
		}	
	});
	
	$.extend(Popup, {
		back: function(mask){ //后退当前页面顶层Popup对象, mask为true时，只后退有遮罩的弹窗
			var ret = false;
			//逆序遍历zIndexer里的id
			$.zIndexer.each(function(i, id){
				if( $.inArray(id, popups) > -1 && window[id] && window[id] instanceof Popup 
					&& ( typeof mask == "undefined" || (true == mask && mask == window[id].mask) )
					){
					window[id].back();
					ret = true;
					return false;
				}
			});
			return ret;
		}
	});
	
	//初始化c_popupItem 中的未初始化Frame组件
	var _initPopupItemFrame = function(item, popupId){
		if(!$.Frame || !$.isFunction($.Frame) || !$.Frame.prototype._init)
			return;
			
		var id, frame;	
		$("iframe[x-wade-uicomponent=frame]", item).each(function(){
			id = $.attr(this, "id");
			if( ( frame = window[id] ) && frame instanceof $.Frame){
				frame.setAttribute("popupId", popupId); //setPopupId
				if(true !== frame.inited){
					setTimeout("window['" + id + "'].init()", hasTransform ? 350 : 0); //setTimeout， 比弹窗动画效果时间稍长，避免卡动画
				}else{
					//setTimeout("window['" + id + "'].adjust();window['" + id + "'].focus();", 350);  //执行frame对象的focus方法会将焦点移动到frame的contentWindow上
					setTimeout("window['" + id + "'].adjust();", hasTransform ? 350 : 0); 
				}
			}
		});
	};
	
	//刷新弹窗中Scroller的高度
	var _refreshPopupItemScroller = function(item){
		if(!$.Scroller || !$.isFunction($.Scroller) || !$.Scroller.prototype._init)
			return;
			
		var id, scroller;
		$("div[x-wade-uicomponent=scroller]", item).each(function(){
			id = $.attr(this, "id");
			if( ( scroller = window[id] ) && scroller instanceof $.Scroller){
				setTimeout("window['" + id + "'].refresh()", hasTransform ? 350 : 0); //setTimeout， 比弹窗动画效果时间稍长
			}
		});
	};
	
	//自适应弹窗中Table的尺寸
	var _adjustPopupItemTable = function(item){
		if(!$.Table || !$.isFunction($.Table) || !$.Table.prototype._init)
			return;
		
		var id, table;
		$("div[x-wade-uicomponent=table]", item).each(function(){
			id = $.attr(this, "id");
			if( ( table = window[id] ) && table instanceof $.Table){
				setTimeout("window['" + id + "'].adjust()", hasTransform ? 350 : 0); //setTimeout， 比弹窗动画效果时间稍长
			}
		});	
	}
	
	var _unfocusFormElement = function(item){
		$("input,textarea,select", item).each(function(){
			if(this.readOnly){
				this["onfocus"] = function(){this.blur();};
			}
		});	
	}
	
	//查找父节点的popupGroup元素
	var _findGroupByParent = function(node){
		if(!node || !node.nodeType)
			return null;
		
		var n = 0, id, p = node.parentNode;
		while(n < 20 && p && p.nodeType && p != doc.body){  //最大查找20层节点
			if($.nodeName(p, "div")
			     && (" " + p.className + " ").indexOf(" c_popupGroup ") > -1){
				return p;
			}
			p = p.parentNode;
			n ++;
		}		
	};
	
	//填充元素的值
	var _fillElementValue = function(itemNode, el, value, srcWindow){
		var win = srcWindow ? srcWindow : window;
		var frame = itemNode ? $("iframe[x-wade-uicomponent=frame]", itemNode)[0] : null;
		
		if(frame && frame.nodeType && $.nodeName(frame, "iframe")){ //处理iframe里设置值得情况
			win = frame.contentWindow; 
		}
		
		if(!win || !win.Wade || !win.$) return;
		
		if($.isString(el)){
			win.$("#" + el).val(value);	
		}else if($.isPlainObject(el)){
			for(var id in el){
				win.$("#" + id).val( el[id] );
			}
		}else if ($.isArray(el) && el.length && el.length % 2 == 0) {
            for (var i = 0; i < el.length; i = i + 2) {
                var id = el[i], val = el[i + 1];
                if (id && id != "undefined") {
                    win.$("#" + id).val(val);
                }
            }
        }
	};
	
	function prefixStyle(style) {
		if ( vendor === '' ) return style;
	
		style = style.charAt(0).toUpperCase() + style.substr(1);
		return vendor + style;
	};	
	
	dummyStyle = null;	// for the sake of it
	
	window.Popup = $.Popup = Popup;
})(window.Wade, window, document);	