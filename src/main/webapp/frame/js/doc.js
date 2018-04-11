var paper       = get("UI-paper");
var sessionList = get("UI-sessionList");
var scroller    = get("UI-scroller");
var curIdx      = 0;
var tempHTML    = '<li class="on" idx="0" onclick="scroller.scrollTop = 0; sessionList.children[curIdx].className = \'link\'; this.className = \'on\'; hideFloatLayer(\'UI-session\'); curIdx = 0;"><div class="main">介绍</div></li>\r\n';
var titles      = (function(){
	var tempAry = [];
	for (var i = 0; i < paper.children.length; i++) {
		if(hasClass(paper.children[i],"c_title")){
			tempAry.push({
				text:paper.children[i].children[0].innerHTML,
				posi:paper.children[i].offsetTop
			});
		}
	}
	return tempAry;
})();



function closeNav(){
	get('UI-mainFrame').src = "home.html";
}



// 拼接 HTML
for (var i = 0; i < titles.length; i++) {
	tempHTML += '<li idx = "' + (i+1) + '" class="link" onclick="scroller.scrollTop = ' + titles[i].posi + '; sessionList.children[curIdx].className = \'link\'; this.className = \'on\'; curIdx = this.getAttribute(\'idx\'); hideFloatLayer(\'UI-session\')"><div class="main">' + titles[i].text + '</div></li>\r\n';
}
sessionList.innerHTML = tempHTML;



// 滚动监听
bind(scroller,"scroll",function(){
	var posi = scroller.scrollTop;
	for (var i = 0; i < titles.length; i++) {
		if(posi >= titles[i].posi){
			sessionList.children[curIdx].className = "link";
			sessionList.children[i + 1].className = "on";
			curIdx = i + 1;
		} else if (posi == 0) {
			sessionList.children[curIdx].className = "link";
			sessionList.children[0].className = "on";
			curIdx = 0;
		}
	}
})