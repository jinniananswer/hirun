var nav    = get("UI-nav");
var navLis = nav.getElementsByTagName("LI");
var curIdx = 10000;
var frame  = get("UI-mainFrame");


for (var i = 0; i < navLis.length; i++) {
	navLis[i].setAttribute("idx",i);
	$(navLis[i]).bind("click",function(){
		frame.src = this.getAttribute("docUrl");
		removeClass(nav,'side-show');
		if(navLis[curIdx]){
			navLis[curIdx].className = "";
		}
		this.className = "on";
		curIdx = this.getAttribute("idx");
	});
}