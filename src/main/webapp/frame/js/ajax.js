(function($){
	$.extend({
		ajaxGet: function(url,param,successFunc, errFunc){
			if(successFunc == null || typeof(successFunc) == "undefined" || typeof(successFunc) != "function"){
				successFunc = function(data){};
			}
			
			if(errFunc == null || typeof(errFunc) == "undefined" || typeof(errFunc) != "function"){
                errFunc = function(data){
                    MessageBox.error("错误信息","对不起，偶们的系统出错了，55555555555555", null,"", "亲，赶紧联系管理员报告功能问题吧");
                };
			}
			$.ajaxRequest(
				{
					url:url,
					data: param,
					type:'GET',
					dataType:'json',
					async:true,
					timeout:5000,
					success:successFunc,
					error:errFunc
				}
			)
		},
		
		ajaxPost: function(url,param,successFunc, errFunc){
			if(successFunc == null || typeof(successFunc) == "undefined" || typeof(successFunc) != "function"){
				successFunc = function(data){};
			}
			
			if(errFunc == null || typeof(errFunc) == "undefined" || typeof(errFunc) != "function"){
				errFunc = function(data){
                    MessageBox.error("错误信息","对不起，偶们的系统出错了，55555555555555", null,"", "亲，赶紧联系管理员报告功能问题吧");
				};
			}
			$.ajaxRequest(
				{
					url:url,
					data: param,
					type:'POST',
					dataType:'json',
					async:true,
					success:successFunc,
					error:errFunc
				}
			)
		},
        ajaxReq: function(setting){
            var successFunc = function(data){
                var resultCode = data.HEAD.RESULT_CODE;
                if(resultCode == '0') {
                	setting.successFunc ? setting.successFunc(data.BODY) : '';
				} else {
                	var resultInfo = data.HEAD.RESULT_INFO;
                    setting.errorFunc ? setting.errorFunc(resultCode, resultInfo) : '';
				}
			}

            var errFunc = function(data){
                MessageBox.error("错误信息","对不起，偶们的系统出错了，55555555555555", null,"", "亲，赶紧联系管理员报告功能问题吧");
            };

            $.ajaxRequest(
                {
                    url:setting.url,
                    data: setting.data,
                    type: setting.type ? setting.type : 'POST',
                    dataType:setting.dataType ? setting.dataType : 'json',
                    async:setting.async,
                    timeout:setting._time,
                    success:successFunc,
                    error:errFunc
                }
            )
        },
        buildJsonData: function(areaId, prefix, xss){
			if(!areaId || !$.isString(areaId))return;
			var data = {};
			var els = $("#" + areaId + " input[name],#" + areaId + " select[name],#" + areaId + " textarea[name]");
			if(els.length){
				var el, name, type, val, add = false;
				els.each(function(){
					el = this; name = $.attr(el,"name"); add = false;
					if(!name) return;
					if(prefix && $.isString(prefix)){
						if(name.indexOf(prefix) != 0) return;
						//name=name.substring(prefix.length);
						//if(!name)return;
					}
					//if(!name || el.disabled)return;
					//input
					if($.nodeName(el,"input")){
						type = $.attr(el,"type");
						add = true;
						if(type && (("radio"==type || "checkbox"==type) && !el.checked)){
							add = false;
						}
						/* if(!type) return;
						if("text" == type || "hidden" == type
							|| (("radio" == type || "checkbox" == type) && el.checked)
							){
								add = true;
								//val = $(el).val();
							}
						 */
					}else{ //other
						add = true;
						//val=$(el).val();
					}
					if($.nodeName(el,"select") && !$("option:first",el).length){
						add = false;
					}
					if(add){
						val = $(el).val();
						if( $.isString(val) ) val = $.trim(val); //由于$(el).val可能不是string类型，需要先判断类型再做trim
						if($.nodeName(el,"textarea") && window.CKEDITOR && "true" == $.attr(el,"__ckeditor")){
							val = CKEDITOR.instances[ $.attr(el,"id") ].getData();
						}
						if(undefined == val || null == val){val = "";}

						//val = $.trim(val);
						val = $.xss(val, true == xss ? true : false);
						if($.isArray(val)){
							if(typeof(data[name]) != "undefined"){
								if($.isArray(data[name])){
									data[name] = data[name].concat(val);
								}else{
									data[name] = [data[name]];
									data[name] = data[name].concat(val);
								}
							}else{
								data[name] = val;
							}
						}else{
							if(typeof(data[name]) != "undefined"){
								if($.isArray(data[name])){
									data[name].push(val);
								}else{
									data[name] = [data[name]];
									data[name].push(val);
								}
							}else{
								data[name] = val;
							}
						}
					}
            	});
        	}
        	return data;
    	},
        buildPostData : function(areaId, encode, prefix, xss){
			if(!areaId || !$.isString(areaId)) return;
			var uiid = $("#" + areaId).attr("uiid");
			var bj = uiid && $.isString(uiid);
			var data = buildJsonData(areaId, prefix, xss);

			if(bj){
				var isOldAjaxJsonData = $.ctx.attr("oldAjaxJsonData");
				if(isOldAjaxJsonData == "false"){
					return uiid+ "=" + jsonObjectToString(data);
				}else{
					return uiid + "=[" + jsonObjectToString(data) + "," + jsonObjectToString($("#" + areaId).data("oldAjaxJsonData")) + "]";
				}
			}else{
				var buf = [];
				for(var key in data){
					var ov = data[key], k = (false==encode ? key : encodeURIComponent(key));
					var type=typeof(ov);
					if(type== "undefined" || null==ov || ""==ov){
						buf.push(k, "=&");
					}else if(type != "function" && type != "object"){
						buf.push(k, "=", (false == encode ? ov : encodeURIComponent(ov)), "&");
					}else if($.isArray(ov)){
						if(ov.length){
							for(var i = 0, len = ov.length; i < len; i++) {
								buf.push(k, "=", (false == encode ? (ov[i] === undefined ? '' : ov[i]) : encodeURIComponent(ov[i] === undefined ? '' : ov[i])), "&");
							}
						}else{
							buf.push(k, "=&");
						}
					}
				}
				buf.pop();
				return buf.join("");
			}
    	},

		buildSubmitData : function(areaids, params){
			var post_params = "";
			if(areaids && $.isString(areaids)){
				var buf = [], exparams;
				$.each(areaids.split(","), function(idx, val){
					buf.push(buildPostData(val));
					exparams = $("#" + val).attr("exparams");
					if (exparams && $.isString(exparams)){
						post_params += exparams;
					}
					exparams = null;
				});
				post_params += "&" + buf.join("&");
			}

			//处理$.DataMap类型
			if(params && (params instanceof $.DataMap) && params.length){
				params = params.map;
			}

			if(params && $.isObject(params)){
				params = $.param(params);
			}

			if(params){
				post_params += "&" + params;
			}
			if(post_params && post_params.indexOf("&") == 0){
				post_params = post_params.substring(1);
			}
			return post_params;
		}
});
})($);