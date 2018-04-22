(function($){
	$.extend({index:{
		openNav : function(url, title){
			if(this.exists(title)){
				this.switchPage(title);
				return;
			}
			
			if(!this.checkOpenNum()){
				MessageBox.alert("告警提示","打开的页面数过多，请关闭一些再重新打开!");
				return;
			}
			this.addNav(title);
			this.addContent(url,title);
		},
		
		exists: function(title){
			var pageTitles = $("#page_titles li");
			if(!pageTitles){
				return false;
			}
			
			for(var i=0;i<pageTitles.length;i++){
				var pageTitle = $(pageTitles[i]);
				var currentTitle = pageTitle.attr("title");
				if(currentTitle == title){
					return true;
				}
			}
			return false;
		},
		
		addNav : function(title){
			this.hideAllTitles();
			var pageTitle = $("#page_titles");
			var html = "<li class='on' title='"+title+"' onclick=$.index.switchPage('"+title+"')><div class='text'>"+title+"</div><div class='close' onclick=$.index.closePage('"+title+"',this)><span class='e_ico-close'></span></div></li>";
			pageTitle.append(html);
		},
		
		checkOpenNum: function(){
			var pageTitles = $("#page_titles li");
			if(!pageTitles){
				return false;
			}
			
			if(pageTitles.length > 10){
				return false;
			}
			
			return true;
		},
		
		hideAllTitles: function(){
			var pageTitles = $("#page_titles li");
			if(!pageTitles){
				return;
			}
			
			for(var i=0;i<pageTitles.length;i++){
				var pageTitle = pageTitles[i];
				$(pageTitle).removeClass("on");
			}
		},
		
		addContent: function(url, title){
			this.hideAllContents();
			var content = $("#page_contents");
			var html="<iframe src='"+url+"' title='"+title+"' frameborder='0'></iframe>";
			content.append(html);
		},
		
		hideAllContents: function(){
			var contents = $("#page_contents iframe");
			if(!contents){
				return;
			}
			for(var i=0;i<contents.length;i++){
				var content = contents[i];
				$(content).css("display","none");
			}
		},
		
		switchPage: function(title){
			var pageTitles = $("#page_titles li");
			if(pageTitles.length == 1){
				return;
			}
			for(var i=0;i<pageTitles.length;i++){
				var pageTitle = $(pageTitles[i]);
				var currentTitle = pageTitle.attr("title");
				if(title == currentTitle){
					pageTitle.addClass("on");
				}
				else{
					pageTitle.removeClass("on");
				}
			}
			
			var contents = $("#page_contents iframe");
			for(var i=0;i<contents.length;i++){
				var content = $(contents[i]);
				var currentTitle = content.attr('title');
				if(currentTitle == title){
					content.css("display","");
				}
				else{
					content.css("display","none");
				}
			}
		},
		
		closePage: function(title, obj){
			var isActive = false;
			var pageTitles = $("#page_titles li");

			for(var i=0;i<pageTitles.length;i++){
				var pageTitle = $(pageTitles[i]);
				var currentTitle = pageTitle.attr("title");
				var currentClass = pageTitle.attr("class");
				if(title == currentTitle){
					if(currentClass == "on"){
						isActive = true;
					}

					pageTitle.remove();
					break;
				}
			}

			pageTitles = $("#page_titles li");

			if(isActive){
				var pageTitle = $(pageTitles[pageTitles.length-1]);
				pageTitle.addClass("on");
			}

			var contents = $("#page_contents iframe");
			$(contents[i]).remove();
			for(var i=0;i<contents.length;i++){
				var content = $(contents[i]);
				var currentTitle = content.attr('title');
				if(currentTitle == title){
					content.remove();
					break;
				}
			}

			contents = $("#page_contents iframe");
			if(isActive){
				var content = $(contents[contents.length-1]);
				content.css("display","");
			}

			event.stopPropagation();
		}
	}});
})($);