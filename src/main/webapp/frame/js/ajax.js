(function($){
	$.extend({
		ajaxGet: function(url,param,successFunc, errFunc){
			if(successFunc == null || typeof(successFunc) == "undefined" || typeof(successFunc) != "function"){
				successFunc = function(data){};
			}
			
			if(errFunc == null || typeof(errFunc) == "undefined" || typeof(errFunc) != "function"){
				errFunc = function(){};
			}
			$.ajaxRequest(
				{
					url:url,
					data:{
						param
					},
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
				errFunc = function(){};
			}
			$.ajaxRequest(
				{
					url:url,
					data:{
						param
					},
					type:'POST',
					dataType:'json',
					async:true,
					timeout:5000,
					success:successFunc,
					error:errFunc
				}
			)
		}
	});
})($);