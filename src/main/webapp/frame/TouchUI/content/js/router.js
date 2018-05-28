var router = function(id){
	this.container = document.getElementById(id);
	this.curIdx = 0;
	this.isBacking = false;
}
router.prototype.open = function(url) {
	var that = this;
	this.isBacking = false;

	if(!$("html").hasClass("s_phone")) {
		$("#mainFrame").attr("src",url);
	} else {
		// 建立 HTML 结构
		var tempHTML = '	<iframe src="blank.html" frameborder="0" style="display:none;" id="mainFrame' + (this.curIdx + 1) + '"></iframe>';
		tempHTML +=    '	<div class="l_float" id="mainLoading' + (this.curIdx + 1) + '">';
		tempHTML +=    '		<div class="c_header">';
		tempHTML +=    '			<div class="back" ontap="top.router.close();">载入中...</div>';
		tempHTML +=    '		</div>';
		tempHTML +=    '		<div class="c_scroll c_scroll-float c_scroll-header">';
		tempHTML +=    '			<div class="c_msg c_msg-full c_msg-loading">';
		tempHTML +=    '				<div class="wrapper">';
		tempHTML +=    '					<div class="emote"></div>';
		tempHTML +=    '					<div class="info">';
		tempHTML +=    '						<div class="text">';
		tempHTML +=    '							<div class="title">努力加载中</div>';
		tempHTML +=    '							<div class="content">loading</div>';
		tempHTML +=    '						</div>';
		tempHTML +=    '					</div>';
		tempHTML +=    '				</div>';
		tempHTML +=    '			</div>';
		tempHTML +=    '		</div>';
		tempHTML +=    '	</div>';
		var tempDOM = document.createElement("DIV");
		tempDOM.className = "li";
		tempDOM.id = "mainLi" + (this.curIdx + 1);
		tempDOM.style.transform = "translateX(100%)";
		tempDOM.style.webkitTransform = "translateX(100%)";
		tempDOM.style.transition = "transform 0.3s ease-out";
		tempDOM.style.webkitTransition = "translateX(100%)";
		tempDOM.innerHTML = tempHTML;
		this.container.appendChild(tempDOM);


		// 动画进入
		setTimeout(function(){
			that.oldLi.style.transform = "translateX(-50%)";
			that.oldLi.style.webkitTransform = "translateX(-50%)";
			that.curLi.style.transform = "translateX(0)";
			that.curLi.style.webkitTransform = "translateX(0)";
		},20)


		// 动画完成后异步加载 HTML
		setTimeout(function(){
			if(!that.isBacking) {
				that.curFrame.src = url;
				that.curFrame.onload = function() {
					that.curFrame.style.display = "";
					that.curLi.removeChild(that.curLoading);
				}
			}
		},400)


		// 重置属性
		this.curIdx += 1;
		this.oldLi = document.getElementById("mainLi" + (this.curIdx - 1));
		this.curLi = document.getElementById("mainLi" + this.curIdx);
		this.curFrame = document.getElementById("mainFrame" + this.curIdx);
		this.curLoading = document.getElementById("mainLoading" + this.curIdx);


		// 层级超过 5 级，移除第一级
		if(this.container.children.length >= 6) {
			this.container.removeChild(this.container.children[1]);
		}
	}
}
router.prototype.close = function() {
	var that = this;
	this.isBacking = true;


	// 动画退出
	this.curLi.style.transform = "translateX(100%)";
	this.curLi.style.webkitTransform = "translateX(100%)";


	// 动画完成后移除元素
	setTimeout(function(){
		that.container.removeChild(that.container.children[that.container.children.length - 1]);
		// that.isBacking = false;
	},400)


	// 重置旧层与属性
	if(this.container.children.length == 2) {
		this.curIdx = 0;
	} else {
		this.curIdx -= 1;
	}
	this.curLi = document.getElementById("mainLi" + this.curIdx);
	this.curFrame = document.getElementById("mainFrame" + this.curIdx);
	this.curLoading = document.getElementById("mainLoading" + this.curIdx);
	this.curLi.style.transform = "translateX(0)";
	this.curLi.style.webkitTransform = "translateX(0)";
}