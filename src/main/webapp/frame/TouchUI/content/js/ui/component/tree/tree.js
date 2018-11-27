/*!
 * tree component
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2015, WADE
 */
(function ($, window, doc) {
	"use strict";
	
	if( !$ || typeof $.Tree != "undefined" )
		return;
	
	var splice  = Array.prototype.splice;
	
	var hasTouch  = typeof $.hasTouch != "undefined" ? $.hasTouch : "ontouchstart" in window;
	
	var Tree = function(el, settings){
		var that = this;

		that.el = el && el.nodeType == 1 ? el : doc.getElementById(el);
		if( !that.el || !that.el.nodeType || !(that.id = $.attr(that.el, "id")) )
			return;
		
		if(settings && $.isObject(settings))
			$.extend(that, settings);
		
		if(!$.attr(that.el, "x-wade-uicomponent")){
			$.attr(that.el, "x-wade-uicomponent", "tree");	
		}
		
		that._init();		
		
		that.constructor.call(that);
	};
	
	Tree.prototype = $.extend(new $.UIComponent(), {
		setParam: function(key, val){
			var that = this;
			
			if(!that.params)
				that.params = {};
				
			that.params[key] = val;
		},
		getCheckedNodeIds: function(doFilter) {  //doFilter = true ,过滤父节点已被勾选的子节点
            var that = this;
            
            if (!that.isShowCheckBox) 
            	return;
            
            var ret = [];
            $("input[name=" + that.checkBoxName + "]:checked", that.el).each(function() {
                ret.push($.attr(this.parentNode.parentNode, "id"));
            });
            
            if (ret.length > 1 && doFilter) { //过滤
                //按分隔符个数从少到多排序
                ret.sort(function(nodeId1, nodeId2) {
                    return nodeId1.lastIndexOf("●") - nodeId2.lastIndexOf("●");
                });
                
                //倒序从最长的nodeId开始遍历
                var dataid, pdata;
                for (var i = ret.length - 1; i > -1; i--) {
                    if( !( dataid = that._getDataIdByNodeId(ret[i]) ) ) continue;
                    pdata = that._getParentNodeDataByDataId(dataid);
                    if (pdata && $.inArray(treeId + "○" + pdata.dataid, ret) > -1) {
                        ret.splice(i, 1);
                    }
                }
            }
            return ret;
        },
		init: function(){
			var that = this;
			
			if(!that.data){ //加载数据
				if("normal" != $.DATA_INTERACTION_MODE && !that.clazz && !that.method && !that.listener && !that.componentId){
					MessageBox.error($.lang.get("ui.component.tree.tip.msg-title"), $.lang.get("ui.component.tree.tip.invalid-parameter", that.id));
					return;
				}
				
				//添加loading
				$(that.el).append("<span id=\"" + that.id + "_loading\" class=\"e_ico-loading\"></span>");
				
				//callback
				var callback = function(data){
					$("#" + that.id + "_loading").remove();
					if("normal" == $.DATA_INTERACTION_MODE){
						that.data = data;
					}else{
						var ctx = data.context;
						if("0" != ctx.x_resultcode){
							MessageBox.error($.lang.get("ui.component.tree.tip.msg-title"), ctx.x_resultinfo);
							return;
						}
						that.data = data.data;
					}
					that.draw();
					
					if(that.expandPath){
						that.expandByPath(that.expandPath);
					}
					$.event.trigger("afterAction", null, that.el);
				}
				
				var params = that._buildParams();
				if("normal" == $.DATA_INTERACTION_MODE && that.dataurl){
					$.ajax.post(that.dataurl, params, null, callback, null, {
						dataType : "json"
					});
				}else if(that.clazz && that.method){
					$.httphandler.post(that.clazz, that.method, params, callback ,null, {
						dataType:"json",
						simple:true
					});
				}else{
					$.ajax.post((that.page ? that.page : null), that.listener, params, (that.componentId ? that.componentId : null), callback, null, {
						dataType:"json",
						simple:true
					});
				}
			}else{
				that.draw();
				if(that.expandPath){
					that.expandByPath(that.expandPath);
				}
				//this.triggerInitAfterAction();
			}
		},
		insert: function(dir, id, text, value, showcheck, pdataid, haschild){
			var that = this;
			var plainObj = $.isPlainObject(id);
			
			if( undefined == id || null == id )
				return;
			if( !plainObj && (undefined == text || null == text) )
				return;
			
			if(plainObj && undefined == pdataid){
				pdataid = text;
				text = null;
			}

			var pData;
			if(pdataid){
				pData = that._getNodeDataByDataId(pdataid);
				if(!pData) return;
			}

			var nodeData = $.extend({"showcheck": "false", "haschild": "false", "complete": "false", 
										"expand": "false", "groupid": null, "href": null, "checked": "false","disabled": "false"}, 
										plainObj ? id : {"id": id, "text": text, "value": value, "showcheck": showcheck ? "true": "false", "haschild": haschild ? "true" : "false"});
			//设置dataid
			nodeData.dataid = pData ? pData.dataid + "●" + nodeData.id : nodeData.id;
			
			//合并数据
			if(pData){
				if(!pData.childNodes)
					pData.childNodes = {};
				if("true" != pData.haschild)
					pData.haschild = "true";
					
				if(typeof pData.childNodes[nodeData.id] != "undefined")
					return;
				else
					pData.childNodes[nodeData.id] = nodeData;
			}else{
				if(!that.data)
					that.data = {};
				if(typeof that.data[nodeData.id] != "undefined")
					return;
				else
					that.data[nodeData.id] = nodeData;
			}
			
			var html = that._buildNode( nodeData );
			if(pData){				
				var pli = $("#" + that.id + "○" + pdataid), ul = pli.children("ul:first");
				var className = ("" + pli.attr("className"));
				if( (" " + className + " ").indexOf(" leaf ") > -1){ //叶子节点样式判断
					className = $.trim( (" " + className + " ").replace(/ leaf /g, " unfold ") );
					pli.attr("className", className);
				}
				if(ul && ul.length){
					switch(dir){
						case "prepend":
							ul.prepend(html);
							break;	
						case "append":
						default:
							//如果父级是枝干节点，且新增的是枝干节点，则插入到最后一个枝干节点之后
							var foldLi = ul.children("li[class!=leaf]:last");
							if(haschild && foldLi && foldLi.length){
								foldLi.after(html);
							}else{
								ul.append(html);	
							}
							break;
					}
				}else{
					pli.append("<ul>\n" + html + "</ul>\n");
				}
				pli = ul = null;
			}else{
				var ul = $(that.el).children("ul:first");
				if(ul && ul.length){
					switch(dir){
						case "prepend":
							ul.prepend(html);
							break;	
						case "append":
						default:
							//如果父级是枝干节点，且新增的是枝干节点，则插入到最后一个枝干节点之后
							var foldLi = ul.children("li[class!=leaf]:last");
							if(haschild && foldLi && foldLi.length){
								foldLi.after(html);
							}else{
								ul.append(html);	
							}
							break;
					}
				}else{
					$(that.el).html("<ul>\n" + html + "</ul>\n");
				}
				ul = null;
			}
			html = null;
		},
		append: function(id, text, value, showcheck, pdataid, haschild){
			var that = this;
			that.insert("append", id, text, value, showcheck, pdataid, haschild);
		},
		prepend: function(id, text, value, showcheck, pdataid, haschild){
			var that = this;
			that.insert("prepend", id, text, value, showcheck, pdataid, haschild);
		},
		remove: function(dataid, haschild){
			var that = this;
			
			if(!dataid || !$.isString(dataid))
				return;
			
			var nodeData = that._getNodeDataByDataId(dataid);
			if(!nodeData)
				return;
				
			var nodeId = nodeData.id;
			var pData = that._getParentNodeDataByDataId(dataid);

			//删除数据
			if(pData){
				delete pData.childNodes[nodeId];
				
				//处理节点样式
				if($.isEmptyObject(pData.childNodes) && !haschild){
					pData.haschild = "false";
					var pli = $("#" + that.id + "○" + pData.dataid), ul = pli.children("ul:first");
					if("leaf" != pli.attr("className")){ //节点样式判断
						pli.attr("className", "leaf");
					}
					pli = ul = null;
				}
			}else{
				delete that.data[nodeId];
			}
			
			//删除DOM结构
			$("#" + that.id + "○" + dataid).remove();
			
			nodeData = null;
		},
		empty: function(){
			var that = this;
			if(!that.data) return;
			
			$(that.el).empty();

			//递归删除元素
			var deleteData = function(data){
				for(var p in data){
					if("childNodes" == p){
						deleteData(data[p]);
					}
					delete data[p];
				}
			};
			deleteData(that.data);
			
			delete that.data;
			that.data = null;
			
			//清除标记变量
			that.lastActiveTapNodeId = null;
			that.lastExpandNodeId = null;
			that.lastCheckedNodeId = null;
			
			if($.browser.msie){
				CollectGarbage();
			}
		},
		draw: function(){
			var that = this;
			
			if(!that.data) return;
			
			var buffer = [];
			
			buffer.push("<ul>\n");
			
			var tmpArray = [];
			for(var nid in that.data){
				if(that.data[nid] && $.isObject(that.data[nid])){
					tmpArray.push([nid, that.data[nid].order]);
				}
			}
			//排序
			tmpArray.sort(function(n1, n2){
				return parseInt(n1[1]) - parseInt(n2[1]);
			});
			//生成节点
			for(var i = 0;i < tmpArray.length; i++){
				buffer.push( that._buildNode( that.data[ tmpArray[i][0] ] ) );
				tmpArray.splice(i--, 1);
			}
			tmpArray = null;
			
			buffer.push("</ul>\n");
			
			$(that.el).html(buffer.join(''));
		},
		expand: function(nodeId){
			var that = this;
			var ndata = that._getNodeDataByNodeId(nodeId);
			if(!ndata) return;
			
			var hasChild = "true" == ("" + ndata["haschild"]),
				complete = "true" == ("" + ndata["complete"]);
			
			if( false === $.event.trigger("expandAction", ndata, that.el) )
				return;
				
			if( hasChild && !complete ){ //判断有子节点且未加载
				
				//禁止同时加载多个节点
				if(that.isAsync && that.asyncLoading){
					MessageBox.alert($.lang.get("ui.component.tree.tip.msg-title"), $.lang.get("ui.component.tree.tip.node-loading-uncompleted"));
					return;
				}
				
				if(that.isAsync && !ndata["childNodes"]){
					
					var params = that._buildParams();
					params += "&Tree_Parent_NodeID=" + encodeURIComponent(ndata["id"]);
					params += "&Tree_Parent_DataID=" + encodeURIComponent(ndata["dataid"]);
					params += "&Tree_Parent_GroupID=" + (ndata["groupid"] != null && ndata["groupid"] != undefined ? encodeURIComponent(ndata["groupid"]) : "");
					params += "&Tree_Parent_isChecked=" + encodeURIComponent(ndata["checked"]);
					
					that.asyncLoading = true;

					var callback = function(data){	
						that.asyncLoading=false;
						$("#" + nodeId + "_loading").remove();
						if("normal" == $.DATA_INTERACTION_MODE){
							if(data && $.isObject(data)){
								ndata["childNodes"] = data;
								that.expand(nodeId);
								return;	
							}
						}else{
							var ctx = data.context;
							if("0" != ctx.x_resultcode){
								that.queue.length = [];
								MessageBox.error($.lang.get("ui.component.tree.tip.msg-title"), ctx.x_resultinfo);
								return;
							}else{
								if(data.data && $.isObject(data.data)){
									ndata["childNodes"] = data.data;
									that.expand(nodeId);
									return;				
								}
							}
						}
					};
					
					//增加loading
					$("#" + nodeId + " div[class=text]").prepend("<span id=\"" + nodeId + "_loading\" class=\"e_ico-loading\"></span>");
					if("normal" == $.DATA_INTERACTION_MODE && that.dataurl){
						$.ajax.post(that.dataurl, params, null, callback, null, {
							dataType : "json"
						});
					}else if(that.clazz && that.method){
						$.httphandler.post(that.clazz, that.method, params, callback, null, {
							dataType : "json",
							simple : true
						});
					}else if(that.listener || that.componentId){
						$.ajax.post((that.page ? that.page : null), that.listener, params, (that.componentId ? that.componentId : null), callback, null,{
							dataType:"json",
							simple : true
						});
					}
					
					//返回
					return;
				}
				
				var buffer = [], childNodes = ndata["childNodes"];
				
				if(childNodes && $.isObject(childNodes)){
					
					var tmpArray = [];
					for(var nid in childNodes){
						if(childNodes[nid] && $.isObject(childNodes[nid])){
							tmpArray.push( [ nid, childNodes[nid].order ] );
						}
					}
					//排序
					tmpArray.sort(function(n1, n2){
						return parseInt( n1[1] ) - parseInt( n2[1] );
					});
					//生成节点
					for(var i = 0; i < tmpArray.length; i++){
						buffer.push( that._buildNode( childNodes[ tmpArray[i][0] ] ) );
						tmpArray.splice(i--, 1);
					}
					tmpArray = null;
					
				 	$("#" + nodeId + " ul:first").html( buffer.join('') );
				 	
				 	buffer = null;
				 	
				 	ndata["complete"] = "true";
				 	
				}else{
					if(false !== that.isNodataWarning){
						MessageBox.alert($.lang.get("ui.component.tree.tip.msg-title"), $.lang.get("ui.component.tree.tip.childnode-no-data"));
					}
				}	
			}
			
			if(hasChild){
			
				var li = doc.getElementById(nodeId);
				if(li && li.nodeType == 1){
					var className = li.className ? li.className : "";
					if( (" " + className + " ").indexOf(" leaf ") < 0){ 
						li.className = $.trim( (" " + className + " ").replace(/ fold /ig, " ") ) + " unfold";
					}
				}
				li = null;
				
				//适配滚动组件高度
				that._autoRefreshScroller();
			}
			
			that.queue.splice( $.inArray(nodeId, that.queue), 1 );	
		},
		expandQueue: function(){
			var that = this;
			
			if(that.queue.length){
				//展开queue中的节点
				if(!that.asyncLoading){
					that.expand( that.queue[0] );
				}
				that.timer = setTimeout("window['" + that.id + "'].expandQueue();", 500);
			}else{
				if(that.timer){
					clearTimeout(that.timer);
				}
			}
		},
		expandByPath: function(path, separator, callback){
			var that = this;
			
			if($.isFunction(separator)){
				callback = separator;
				separator = null;
			}
			
			if(!path || !$.isString(path)){
				MessageBox.alert($.lang.get("ui.component.tree.tip.msg-title"), $.lang.get("ui.component.tree.tip.invalid-expandpath"));
				return;
			}
			
			if(!separator || !$.isString(separator)){
				separator = "-";
			}
			
			if($.isFunction(callback)){
				that.expandByPathCallback = callback;
			}
			
			//分解path
			var nodes = path.split(separator);
			var arr = [], nid;
			for(var i = 0; i < nodes.length; i++){
				arr.push( nodes[i] );
				nid = that.id + "○" + arr.join("●");
				
				if(i == (nodes.length - 1)) //记录展开的最后一级节点
					that.lastExpandNodeId = nid;
					
				if(that.isAsync){
					//如果是异步树，插入节点唯一id到队列数组
					that.queue.push(nid);
				}else{
					that.expand(nid);
				}
			}
			nodes = arr = null;
			
			//异步树才使用队列展开
			if(that.isAsync){
				that.expandQueue();		
			}	
		},
		collapse: function(nodeId){
			var that = this;
			
			var li = doc.getElementById(nodeId);
			if(li && li.nodeType == 1){
				var className = li.className ? li.className : "";
				if( (" " + className + " ").indexOf(" leaf ") < 0){ 
					li.className = $.trim( (" " + className + " ").replace(/ unfold /ig, " ") ) + " fold";
				}
			}
			li = null;
			
			//适配滚动组件高度
			that._autoRefreshScroller();
		},
		destroy: function(){
			var that = this;

			if(that.data){
				for(var id in that.data){
					delete that.data[id];
				}
			}
			that.data = null;
			
			if($.isObject(that.params)){
				for(var key in that.params){
					delete that.params[key];
				}
				that.params = null;
			}
			
			for(var i = 0; i < that.queue.length; i++){
				splice.call(that.queue, i--, 1);
			}
			that.queue = null;
			
			that.el = null;
		},
		_init: function(){
			var that = this;
			
			this.queue = [];
			
			/*************** 绑定事件  ******************/

			$(that.el).tap(function(e){	
				if(e.originalEvent)
					e = $.event.fix(e.originalEvent);
								
				var el = e.target;
				if(!el || !el.nodeType == 1) return;
				
				var className = el.className ? el.className : "";
				var li, prevLi, nodeId, isExpand, liClassName;
				
				if( $.nodeName(el, "input") && $.attr(el, "type") == that.checkBoxType
					&& (li = el.parentNode) && (li = li.parentNode) && li.nodeType == 1
					&& $.nodeName(li, "li") && (nodeId = $.attr(li, "id")) ){ //checkbox
					
					//if(hasTouch) return; //触摸设备不支持直接选择复选框
										
					that._activeNode(nodeId); //设置 active样式
						
					//执行checkBoxAction事件
					if(false === $.event.trigger("checkBoxAction", that._getNodeDataByNodeId(nodeId), that.el)){
						el.checked = !el.checked; //还原checked状态
						li = prevLi = className = null;
						return;
					}

					//由于tap事件延迟处理， el.checked实际是已完成check的状态
					that._checkNode(nodeId, el.checked);
					
				}else if( $.nodeName(el, "div") && (li = el.parentNode) && li.nodeType == 1 
					&& $.nodeName(li, "li") && (nodeId = $.attr(li, "id")) ){
					
					liClassName = li.className ? li.className : "";
					isExpand = (" " + liClassName + " ").indexOf(" unfold ") > -1;
	
					that._activeNode(nodeId); //设置 active样式
												
					if( (" " + className + " ").indexOf(" text ") > -1 ){ //text

						//判断是否叶子节点
						//if( (" " + liClassName + " ").indexOf(" leaf ") > -1){	}
						
						//执行textAction事件
						if(false === $.event.trigger("textAction", that._getNodeDataByNodeId(nodeId), that.el)){
							li = prevLi = className = liClassName = null;
							return;
						}
						
						var triggerCheck = false;
						if(that.isShowCheckBox){
							//查找 div .checkbox
							var n = 0, pre = el.previousSibling, f = false;
							while(n < 3 && pre){
								if(pre.nodeType == 1 && $.nodeName(pre, "div") 
									&& (" " + pre.className + " ").indexOf(" checkbox ") > -1){
									f = true;
									break;
								}
								pre = pre.previousSibling;
								n ++;
							}
							
							if(true === f){
								var cbx = pre.getElementsByTagName("input")[0];
								if(cbx && cbx.nodeType == 1 && $.attr(cbx, "type") == that.checkBoxType){
									triggerCheck = true;
									//执行checkBoxAction
									if(false === $.event.trigger("checkBoxAction", that._getNodeDataByNodeId(nodeId), that.el)){
										li = prevLi = className = liClassName = null;
										return;
									}
									
									that._checkNode(nodeId, !cbx.checked, false);
								}
							}
						}
						
						if(!triggerCheck){
							that._triggerNode(nodeId, isExpand); //展开或收缩节点
						}
					}else if( (" " + className + " ").indexOf(" ico ") > -1 ){	//图标
						that._triggerNode(nodeId, isExpand); //展开或收缩节点
					}	
				}
				
				li = prevLi = className = liClassName = null;
			});
			
			/*************** 绑定事件  ******************/
		},
		_autoRefreshScroller: function(){ //如果树组件上级有Scroller组件，则进行高度宽度匹配
			var that = this;
			
			var p = that.el.parentNode;
			var i = 0, id, scroller;
			while(i < 50 && p && p.nodeType && p != doc.body){ //递归刷新所有scroller
				if(p.nodeType == 1 && "scroller" == $.attr(p, "x-wade-uicomponent")
					&& (id = $.attr(p, "id")) && window[id] && window[id] instanceof $.Scroller){
					window[id].refresh(); 
				}
				p = p.parentNode;
				i ++;
			}
			p = null; 
		},
		_getDataIdByNodeId: function(nodeid) {
            if (!nodeid || !$.isString(nodeid)) return;
            var lidx = nodeid.lastIndexOf("○");
            return nodeid.substring(lidx + 1, nodeid.length);
        },
		_getNodeDataByDataId: function(dataId){
			var that = this;
			if(!dataId || !$.isString(dataId)) return;
			if(!that.data || !$.isObject(that.data)) return;
			
			var data, arr = dataId.split("●");
			if(arr && arr.length){
				var data, childNodes;
				for(var i = 0; i < arr.length; i++){
					if(i == 0){
						data = that.data[ arr[i] ];
					}else if(data){
						childNodes = data["childNodes"];
						if(childNodes && $.isObject(childNodes)){
							data = childNodes[ arr[i] ];
						}
					}
					if(!data) break;
				}
			}
			arr = null;
			return data;
		},
		_getParentNodeDataByDataId: function(dataId){
			var that = this;
			if(!dataId || !$.isString(dataId)) return;
			
			var data, arr = dataId.split("●");
			if(arr && arr.length >= 2 ){
				//删除最后一个节点
				arr.splice(arr.length - 1, 1);
				data = that._getNodeDataByDataId( arr.join("●") );
			}
			arr = null;
			
			return data;
		},
		_getNodeDataByNodeId: function(nodeId){
			var that = this;
			if(!nodeId || !$.isString(nodeId))return;
			
			var idx = nodeId.lastIndexOf("○");
			var dataId = nodeId.substring(idx + 1, nodeId.length);

			return that._getNodeDataByDataId(dataId);
		},
		_buildParams: function(){	
			var that = this;
			var buffer = [];
			
			buffer.push("Tree_ID=" + encodeURIComponent(that.id));
			//buffer.push("Tree_IsExpandRoot=" + that.isExpandRoot);
			buffer.push("Tree_IsShowCheckBox=" + that.isShowCheckBox);
			//buffer.push("Tree_IsExpandAll=" + that.isExpandAll);
			buffer.push("Tree_IsSearch=" + that.isSearch);
			buffer.push("Tree_IsFolder=" + that.isFolder);
			buffer.push("Tree_IsAsync=" +  that.isAsync);
			
			if(that.checkBoxName){
				buffer.push("Tree_CheckBoxName=" + encodeURIComponent(that.checkBoxName));
			}
			if(that.checkBoxType){
				buffer.push("Tree_CheckBoxType=" + encodeURIComponent(that.checkBoxType));
			}
			if(that.iconDir){
				buffer.push("Tree_IconDir=" + encodeURIComponent(that.iconDir));
			}
			if(!$.isEmptyObject(that.params)){
				for(var key in that.params){
					buffer.push(encodeURIComponent(key) + "=" + encodeURIComponent(that.params[key]));
				}
			}
			
			return buffer.join("&");
		},
		_buildNode: function(nodeData){
			var that = this;
			
			if(!nodeData || !$.isObject(nodeData)) return;
			
			var buffer = [];
			var dataId = nodeData["dataid"],
				nodeId = that.id + "○" + dataId,
				hasChild = ( "true" == ("" + nodeData["haschild"]) ),
				complete = ("true" == ("" + nodeData["complete"]) ),
				icon = nodeData["icon"],
				href = nodeData["href"],
				cls = hasChild ? "fold" : "leaf";

			buffer.push("<li id=\"" + nodeId + "\" class=\"" + cls + "\">\n");
			buffer.push("<div class=\"ico\"></div>\n");
			
			if("true" == nodeData["showcheck"]){
				//checkBoxAction 只在当前选择节点触发
				buffer.push("<div class=\"checkbox\">");
				buffer.push("<input name=\"" + that.checkBoxName + "\" type=\"" + (that.checkBoxType == "radio" ? "radio" : "checkbox") + "\" value=\"" + nodeData["value"] + "\" " + ("true" == ("" + nodeData["checked"]) ? "checked" : "") + " " + ("true" == ("" + nodeData["disabled"]) ? "disabled" : "") + " />");
				buffer.push("</div>\n");
			}
			
			//var isnew = ("true" == ("" + nodeData["isnew"]));
			buffer.push("<div class=\"text\" title=\"" + nodeData["text"] + "\" >");
			if(icon){
				buffer.push("<span class=\"" + icon + "\"></span>");
			}
			buffer.push( nodeData["text"] );
			buffer.push("</div>\n");
			
			//有子节点才生成ul,避免ul在ie6下占高度
			if(hasChild){
				buffer.push("<ul></ul>");
			}
			
			buffer.push("</li>\n");		
			
			return buffer.join('');
		},
		_triggerNode: function(nodeId, isExpand){ //处理展开收拢
			var that = this;
			
			if(!nodeId) return;
			if(isExpand){	
				that.collapse(nodeId);
			}else{
				that.expand(nodeId);
			}
		},
		_checkNode: function(nodeId, checked, fromCheckBox){  //处理单选和复选框, fromCheckBox - 是否有复选框点击操作 默认为true
			var that = this;
			
			if(!that.isShowCheckBox) return;
			
			var nodeData = that._getNodeDataByNodeId(nodeId);
			if(!nodeData) return;
			
			if("radio" == that.checkBoxType){
				nodeData.checked = "true";
				if(that.lastCheckedNodeId && nodeId != that.lastCheckedNodeId
					&& (nodeData = that._getNodeDataByNodeId(that.lastCheckedNodeId))){
					nodeData.checked = "false";
					
					if( false == fromCheckBox ){
						$("#" + that.lastCheckedNodeId + " input[type=radio]:first").attr("checked", checked ? true : false);
					}
				}
				
				if( false == fromCheckBox && nodeId != that.lastCheckedNodeId){
					$("#" + nodeId + " input[type=radio]:first").attr("checked", checked ? true : false);
				}
			}else if("checkbox" == that.checkBoxType){
				nodeData.checked = checked ? "true" : "false";

				if(false == fromCheckBox){
					$("#" + nodeId + " input[type=checkbox]:first").attr("checked", checked ? true : false);
				}
				
				if("true" == nodeData.haschild){
					var childNodes = nodeData["childNodes"];
						if(childNodes && $.isObject(childNodes)){
							var childNodeId, childNodeData;
							for(var id in childNodes){
								childNodeData = childNodes[id];
								if($.isObject(childNodeData)){
									//递归调用，选择子节点
					 		 		//setTimeout("window['" + that.id + "']._checkNode('" + that.id + "○" + childNodeData.dataid + "', " + checked + ")", 0);
						 		 	that._checkNode(that.id + "○" + childNodeData.dataid, checked, false);
						 		 }
							}
					 	}
				}
			}
			that.lastCheckedNodeId = nodeId;
		},
		_activeNode: function(nodeId){ //设置为活动
			var that = this;
			var li, prevLi;
			if(nodeId && $.isString(nodeId) && nodeId != that.lastActiveTapNodeId){
				li = doc.getElementById(nodeId);
				prevLi = that.lastActiveTapNodeId ? doc.getElementById(that.lastActiveTapNodeId) : null;
				
				if(prevLi && prevLi.nodeType == 1){
					prevLi.className = $.trim((" " + prevLi.className + " ").replace(/ on /ig, " "));
				}
				 
				if(li && li.nodeType == 1){
					li.className = $.trim( (li.className ? li.className : "") + " on"); 
					that.lastActiveTapNodeId = nodeId;
				}
			}
			li = prevLi = null;
		}
	});
	
	//export 
	window.Tree = $.Tree = Tree;	
	
})(Wade, window, document);	