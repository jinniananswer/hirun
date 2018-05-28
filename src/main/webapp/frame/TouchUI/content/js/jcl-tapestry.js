/*!
 * tapestry adapter
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2011, WADE
 */
!function(t,e,a){t.extend({UI_POPUP_JS:"v5/jcl/ui/component/base/popup.js",UI_DIALOG_JS:"v5/jcl/ui/component/base/dialog.js",UI_FRAME_JS:"v5/jcl/ui/component/base/frame.js",UI_TABSET_JS:"v5/jcl/ui/component/tabset/tabset.js",FILE_UPLOAD_URL:"attach?action=upload",FILE_DELETE_URL:"attach?action=delete",FILE_DOWNLOAD_URL:"attach?action=download",FILE_QUERY_URL:"attach?action=query",IMPEXP_URL:"impexp"})}(window.Wade,window,document),/*!
 * page redirect api for webframe 
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2011, WADE
 */
function(t,e,a){"use strict";if(t&&"undefined"==typeof t.redirect){var n,r,i=t.ctx.attr("urlparams");t.redirect={getPathName:function(){if(void 0==n){if(e.getPathName&&t.isFunction(e.getPathName))return n=e.getPathName();var a=e.location.protocol;"http:"==a||"https:"==a?(n=e.location.pathname,n&&n.indexOf("/")>0&&(n="/"+n)):n=""}return n},getPageName:function(){var a=t.ctx.attr("page");if(!a){var n=e.location.pathname,r=n.indexOf(".html");if(r>0){pathName=n.substring(0,r);var i=pathName.split("/");a=i[i.length-1]}}return a},getSessionIdFromUrl:function(){return this.isSessionIdFromUrl()?r:null},isSessionIdFromUrl:function(){if(void 0==r){var t=e.location.search;if(t){var a=t.lastIndexOf("SESSIONID=");a>-1&&(t=t.substring(a+10,t.length),a=t.indexOf("&"),a>-1&&(t=t.substring(0,a)),r=t)}r||(r=null)}return null!=r},parseUrl:function(e){if(e&&t.isString(e)){if(i&&""!=i){for(var a,n,o,l,d=t.params.load(e),s=i.split(","),c=0;c<s.length;c++)if(s[c]&&s[c].length){if(a=s[c].indexOf(":"),a==-1?n=o=s[c]:(n=s[c].substring(a+1),o=s[c].substring(0,a)),d.get(o)&&""!=d.get(o))continue;var u=d.get("service");if(u&&"ajax"==u?"page"==n?l=d.get("page"):(l=d.get(n),null!=l&&""!=l||(l=t.ctx.attr(n))):"page"==n?l=u?u.substring(u.indexOf("/")+1):"":(l=d.get(n),null!=l&&""!=l||(l=t.ctx.attr(n))),u=null,null!=l&&""!=l){e+=(e.indexOf("?")!=-1?"&":"?")+o+"="+l;continue}null!=d.get(n)&&""!=d.get(n)||(e+=(e.indexOf("?")!=-1?"&":"?")+n+"="+l)}d=null}return this.isSessionIdFromUrl()&&(e+=(e.indexOf("?")!=-1?"&":"?")+"SESSIONID="+r),e}},buildUrl:function(e,a,n,r){for(var i=0;i<arguments.length;i++)null!=arguments[i]&&void 0!=arguments[i]||(arguments[i]="");3==arguments.length?(r=n,n=a,a=e,e=null):2==arguments.length?(n=a,a=e,e=null):1==arguments.length&&(a=e,e=null);var o="";if(e?o+=e:o=t.redirect.getPathName()+o,a&&(o+="?service=page/"+a),n&&(o+="&listener="+n),r&&"string"==typeof r){var l=r.charAt(0);"?"==l?r="&"+r.substring(1):"&"!=l&&(r="&"+r),o+=r}return o=this.parseUrl(o)},buildSysUrl:function(e,a){if(1==arguments.length&&(a=e,e=null),a){a=t.trim(a);var n="";return 0!=a.indexOf("http")&&"/"!=a.charAt(0)&&a.indexOf("?")<0&&(e?n+=e:n=t.redirect.getPathName()+n,n+="?"),n+=a,n=this.parseUrl(n)}},to:function(t){t&&(t=this.parseUrl(t),a.location.href=t)},toUrl:function(e){e&&(e=t.redirect.parseUrl(e),a.location.href=e)},toSysUrl:function(t,e){e=this.buildSysUrl.apply(this,arguments),a.location.href=e},toPage:function(t,e,n,r){var i=this.buildUrl.apply(this,arguments);a.location.href=i}},e.redirectTo=t.redirectTo=t.redirect.toPage}}(window.Wade,window,document),/*!
 * ajax request library for webframe
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2011, WADE
 */
function(t,e,a){"use strict";function n(e){var a=[];if(e&&t.isPlainObject(e))for(var n in e){var r=e[n],i=encodeURIComponent(n),o=typeof r;if("undefined"==o||null==r||""==r)a.push('"'+i+'":""');else if("function"!=o&&"object"!=o)a.push('"'+i+'":"'+encodeURIComponent(r)+'"');else if(t.isArray(r))if(r.length)for(var l=0,d=r.length;l<d;l++)a.push('"'+i+'":"'+encodeURIComponent(void 0===r[l]?"":r[l])+'"');else a.push('"'+i+'":""')}return"{"+a.join(",")+"}"}function r(a,n,r){if(a&&t.isString(a)){var i={},o=t("#"+a+" input[name],#"+a+" select[name],#"+a+" textarea[name]");if(o.length){var l,d,s,c,u=!1;o.each(function(){l=this,d=t.attr(l,"name"),u=!1,d&&(n&&t.isString(n)&&0!=d.indexOf(n)||(t.nodeName(l,"input")?(s=t.attr(l,"type"),u=!0,!s||"radio"!=s&&"checkbox"!=s||l.checked||(u=!1)):u=!0,t.nodeName(l,"select")&&!t("option:first",l).length&&(u=!1),u&&(c=t(l).val(),t.isString(c)&&(c=t.trim(c)),t.nodeName(l,"textarea")&&e.CKEDITOR&&"true"==t.attr(l,"__ckeditor")&&(c=CKEDITOR.instances[t.attr(l,"id")].getData()),void 0!=c&&null!=c||(c=""),c=t.xss(c,1==r),t.isArray(c)?"undefined"!=typeof i[d]?t.isArray(i[d])?i[d]=i[d].concat(c):(i[d]=[i[d]],i[d]=i[d].concat(c)):i[d]=c:"undefined"!=typeof i[d]?t.isArray(i[d])?i[d].push(c):(i[d]=[i[d]],i[d].push(c)):i[d]=c)))})}return i}}function i(e,a,i,o){if(e&&t.isString(e)){var l=t("#"+e).attr("uiid"),d=l&&t.isString(l),s=r(e,i,o);if(d){var c=t.ctx.attr("oldAjaxJsonData");return"false"==c?l+"="+n(s):l+"=["+n(s)+","+n(t("#"+e).data("oldAjaxJsonData"))+"]"}var u=[];for(var p in s){var g=s[p],v=0==a?p:encodeURIComponent(p),f=typeof g;if("undefined"==f||null==g||""==g)u.push(v,"=&");else if("function"!=f&&"object"!=f)u.push(v,"=",0==a?g:encodeURIComponent(g),"&");else if(t.isArray(g))if(g.length)for(var h=0,y=g.length;h<y;h++)u.push(v,"=",0==a?void 0===g[h]?"":g[h]:encodeURIComponent(void 0===g[h]?"":g[h]),"&");else u.push(v,"=&")}return u.pop(),u.join("")}}function o(e,a){var n="";if(e&&t.isString(e)){var r,o=[];t.each(e.split(","),function(e,a){o.push(i(a)),r=t("#"+a).attr("exparams"),r&&t.isString(r)&&(n+=r),r=null}),n+="&"+o.join("&")}return a&&a instanceof t.DataMap&&a.length&&(a=a.map),a&&t.isObject(a)&&(a=t.param(a)),a&&(n+="&"+a),n&&0==n.indexOf("&")&&(n=n.substring(1)),n}function l(e){var a=t.extend(!0,{},e);t.ajaxRequest(a)}function d(e,a,n,i){if(i.simple)return void(i.callback&&t.isFunction(i.callback)&&i.callback(e,a,n));var o,l,d,s,c,u,p,g;if(e&&t.isString(e)){var v=e.lastIndexOf("\n");v>-1&&(g=e.substring(0,v),e=e.substring(v+1,e.length)),e=t.parseJSON(t.parseJsonString(e)),l=e.context.x_resultcode,d=e.context.x_resultinfo,s=e.context.x_excelevel,p=e.context.x_errorlevel,"DEBUG"==p||"LOG"==p?(c=e.context.x_errorid,u=e.context.x_errorinfo,u=s+"|"+p+"|"+c+"|"+u):u=s+"|"+u,e=e.data}if(i.partids&&g&&t.each(i.partids.split(","),function(e,a){var n=a.substring(a.lastIndexOf(".")+1),i="<__part_"+n+">",o="</__part_"+n+">",l=g.indexOf(i),d=g.indexOf(o);if(!(l<0||d<0)){var s=g.substring(l+i.length,d),c=t("#"+n);if(c.length&&(c.html(s),c.attr("uiid"))){var u=t.ctx.attr("oldAjaxJsonData");"false"!=u&&c.data("oldAjaxJsonData",r(c.attr("id")))}}}),e&&(t.isArray(e)||t.isObject(e)))if(t.isArray(e)){var f=!0;if(e.length){var h=e.length;h&&/^\d+$/.test(h)&&parseInt(h)>0||(f=!1)}else f=!1;o=f?new t.DatasetList(e):new t.DatasetList}else o=new t.DataMap(e);if(!/^(-)?[@#\s\.:;$\{\}\[\]a-zA-Z0-9_-]+$/.test(l))return void(i.errorback&&t.isFunction(i.errorback)?i.errorback(-1,"Incorrect format:X_RESULTCODE","Incorrect format:X_RESULTCODE "+l):alert("Incorrect format:X_RESULTCODE "+l));if("0"!=l){d||(d="");var y="";return y=d.indexOf("Caused")!=-1?d.indexOf(":")!=-1?d.substring(d.lastIndexOf(":")+1):d.substring(d.lastIndexOf("Caused")+6):d,void(i.errorback&&t.isFunction(i.errorback)?i.errorback(l,d,u):alert(y))}o&&i.callback&&t.isFunction(i.callback)&&i.callback(o,a,n)}function s(e,a,n,r){var i=e&&4==e.readyState?e.status:a,o=n?n.getMessage():"HttpStatus:"+i+"\nXMLHttp Request Error";r.errorback&&t.isFunction(r.errorback)?r.errorback(i,o,o):alert(o)}if(t&&"undefined"==typeof t.httphandler){"undefined"==typeof t.ajax&&(t.ajax={});var c,u="handler";(c=t.ctx.attr("ajax"))||(c="ajax"),t.extend(t.ajax,{buildUrl:function(e,a,n,r){for(var i=0;i<arguments.length;i++)null!=arguments[i]&&void 0!=arguments[i]||(arguments[i]="");3==arguments.length?(r=n,n=a,a=e,e=null):2==arguments.length?(n=a,a=e,e=null):1==arguments.length&&(a=e,e=null),a||(a=t.redirect.getPageName());var o="";return e?o+=e:o=t.redirect.getPathName()+o,o+="?service="+c,a&&(o+="&page="+a),n&&(o+="&listener="+n),o=t.redirect.parseUrl(o),r&&"string"==typeof r&&(o+=("&"==r.indexOf(0)?"":"&")+r),o},buildJsonData:function(t,e,a){return r(t,e,a)},buildPostData:function(t,e,a,n){return i(t,e,a,n)},buildSubmitData:function(t,e){return o(t,e)},ajaxSettings:{simple:!1,type:"GET",async:!0,cache:!1,dataType:"text",loading:!0,timeout:18e4},setup:function(e){e&&t.isPlainObject(e)&&t.extend(this.ajaxSettings,e)},request:function(e,a,n,r,i,o,c){function u(t,e,a){d(t,e,a,y)}function p(t,e,a){s(t,e,a,y)}n&&t.isFunction(n)&&(o=i,i=n,n=null),n&&n instanceof t.DataMap&&n.length&&(n=n.map);var g=this.buildUrl(e,a);if(r&&t.isString(r)){var v=[],f=r.split(",");t.each(f,function(e,a){if(a&&a.indexOf(".")<0){var n=t("#"+a).attr("_jwc_path");v[e]=n?n:""}}),r=f.join(","),g+="&partids="+r,g+="&jwcpaths="+v.join(","),v=f=null}n||t.isString(n)||(n="");var h=t.ctx.attr("ws_randomCode");h&&(n+="&ws_randomCode="+h);var y=t.extend({url:g,data:n,type:"GET",partids:r,success:u,error:p,callback:i,errorback:o},t.ajax.ajaxSettings,c);"text"!=y.dataType&&(y.simple=!0),l(y)},getString:function(e,a,n,r,i,o){t.ajax.request(e,a,n,null,r,i,t.extend({simple:!0},o))},getJson:function(e,a,n,r,i,o){t.ajax.request(e,a,n,null,r,i,t.extend({dataType:"json",simple:!0},o))},getJsonp:function(e,a,n,r,i){t.ajax.request(e,a,n,null,r,i,{dataType:"jsonp",simple:!0})},getScript:function(e,a,n,r,i,o){t.ajax.request(e,a,n,null,r,i,t.extend({dataType:"script",simple:!0},o))},get:function(e,a,n,r,i,o,l){t.ajax.request(e,a,n,r,i,o,l)},post:function(e,a,n,r,i,o,l){t.ajax.request(e,a,n,r,i,o,t.extend({type:"POST",encoding:"UTF-8"},l))},submit:function(e,a,n,r,i,l,d){t.ajax.post(null,a,o(e,n),r,i,l,d)}}),t.httphandler={buildUrl:function(e,a,n,r){for(var i=0;i<arguments.length;i++)null!=arguments[i]&&void 0!=arguments[i]||(arguments[i]="");3==arguments.length?(r=n,n=a,a=e,e=null):2==arguments.length?(n=a,a=e,e=null):1==arguments.length&&(n=e,e=null);var o="";if(e)o+=e;else{var l=t.redirect.getPathName();o=l.substring(0,l.lastIndexOf("/")+1)+u+o}o+="?clazz="+a,n&&(o+="&method="+n);var d=t.redirect.getPageName();return d&&(o+="&page="+d),o=t.redirect.parseUrl(o),r&&"string"==typeof r&&(o+=("&"==r.indexOf(0)?"":"&")+r),o},buildJsonData:function(t,e,a){return r(t,e,a)},buildPostData:function(t,e,a,n){return i(t,e,a,n)},buildSubmitData:function(t,e){return o(t,e)},httphandlerSettings:{simple:!1,type:"GET",async:!0,cache:!1,dataType:"text",loading:!0,timeout:18e4},setup:function(e){e&&t.isPlainObject(e)&&t.extend(this.httphandlerSettings,e)},request:function(e,a,n,r,i,o){function c(t,e,a){d(t,e,a,g)}function u(t,e,a){s(t,e,a,g)}n&&t.isFunction(n)&&(i=r,r=n,n=null),n&&n instanceof t.DataMap&&n.length&&(n=n.map);var p=this.buildUrl(e,a),g=t.extend({url:p,data:n,type:"GET",success:c,error:u,callback:r,errorback:i},t.httphandler.httphandlerSettings,o);"text"!=g.dataType&&(g.simple=!0),l(g)},getJson:function(e,a,n,r,i,o){t.httphandler.request(e,a,n,r,i,t.extend({dataType:"json",simple:!0},o))},getJsonp:function(e,a,n,r,i){t.httphandler.request(e,a,n,r,i,{dataType:"jsonp",simple:!0})},getScript:function(e,a,n,r,i,o){t.httphandler.request(e,a,n,r,i,t.extend({dataType:"script",simple:!0},o))},get:function(e,a,n,r,i,o){t.httphandler.request(e,a,n,r,i,o)},post:function(e,a,n,r,i,o){t.httphandler.request(e,a,n,r,i,t.extend({type:"POST",encoding:"UTF-8"},o))},submit:function(e,a,n,r,i,l,d){t.httphandler.post(a,n,o(e,r),i,l,d)}};var p=t.ctx.attr("oldAjaxJsonData");"false"!=p&&t(a).ready(function(){t("*[uiid]").each(function(){var e=t.attr(this,"id");e&&t.data(this,"oldAjaxJsonData",r(e))})}),e.ajaxGet=t.ajax.get,e.ajaxPost=t.ajax.post,e.ajaxSubmit=t.ajax.submit,e.hhGet=t.httphandler.get,e.hhPost=t.httphandler.post,e.hhSubmit=t.httphandler.submit}}(window.Wade,window,document),/*!
 * nav control api
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2011, WADE
 */
function(t,e,a){"use strict";function n(){void 0==s&&(s=t.isSameDomain(top)),void 0==c&&(c=t.isSamePage(top))}function r(){return t.nav&&t.nav.adapter}function i(t){return t&&n(),s&&!c&&top.Wade&&top.Wade.nav&&top.Wade.nav.adapter}function o(t){return t&&n(),s&&top.Wade&&top.Wade&&top.Wade.extNavAdapter}function l(e,a,n,r){var i;return i=e&&t.isString(e)?t.redirect.buildUrl(e,a,n,r):t.redirect.buildUrl(a,n,r)}function d(e,a){return a=e&&t.isString(e)?t.redirect.buildSysUrl(e,a):t.redirect.buildSysUrl(a)}if(t&&"undefined"==typeof t.nav){var s,c;t.nav={adapter:void 0,events:{active:"onActive",unactive:"onUnActive",close:"onClose"},methods:{init:t.lang.get("ui.nav.init")+"void init(window win,document doc)",open:t.lang.get("ui.nav.open")+"void open(string title,string page,string listener,string params,string subsys,object data)",openLock:t.lang.get("ui.nav.openLock")+"void openLock(string title,string page,string listener,string params,string subsys,object data)",openByUrl:t.lang.get("ui.nav.openByUrl")+"void openByUrl(string title,string url,string subsys,object data)",redirect:t.lang.get("ui.nav.redirect")+"void redirect(string title,string page,string listener,string params,string subsys,object data)",redirectByUrl:t.lang.get("ui.nav.redirectByUrl")+"void redirectByUrl(string title,string url,string subsys,object data)",reload:t.lang.get("ui.nav.reload")+"void reload(string url)",getData:t.lang.get("ui.nav.getData")+"object getData()",getDataByTitle:t.lang.get("ui.nav.getDataByTitle")+"object getDataByTitle()",getTitle:t.lang.get("ui.nav.getTitle")+"string getTitle()",getContentWindow:t.lang.get("ui.nav.getContentWindow")+"[frame]contentWindow getContentWindow()",getContentWindowByTitle:t.lang.get("ui.nav.getContentWindowByTitle")+"[frame]contentWindow getContentWindowByTitle(string title)",switchByTitle:t.lang.get("ui.nav.switchByTitle")+"void switchByTitle(string title)",close:t.lang.get("ui.nav.close")+"void close()",closeByTitle:t.lang.get("ui.nav.closeByTitle")+"void closeByTitle()"},init:function(e,a){o(!0)?top.Wade.extNavAdapter.init(e,a):i()?top.Wade.nav.adapter.init(e,a):r()&&t.nav.adapter.init(e,a)},open:function(a,n,d,s,c,u){a&&t.isString(a)||t.error("title"+t.lang.get("ui.nav.notnull")),n&&t.isString(n)||t.error("page"+t.lang.get("ui.nav.notnull"));var p=t.redirect.getPathName();if(o(!0)&&top.Wade.extNavAdapter.openByUrl){var g=l(c,n,d,s);return void top.Wade.extNavAdapter.openByUrl.apply(e,[a,g,c?c:p,u])}return i()?void top.Wade.nav.adapter.open.apply(e,[a,n,d,s,c?c:p,u]):r()?void t.nav.adapter.open.apply(e,[a,n,d,s,c,u]):void t.redirect.toPage(c,n,d,s)},openLock:function(a,n,d,s,c,u){a&&t.isString(a)||t.error("title"+t.lang.get("ui.nav.notnull")),n&&t.isString(n)||t.error("page"+t.lang.get("ui.nav.notnull"));var p=t.redirect.getPathName();if(o(!0)&&top.Wade.extNavAdapter.openLockByUrl){var g=l(c,n,d,s);return void top.Wade.extNavAdapter.openLockByUrl.apply(e,[a,g,c?c:p,u])}return i()?void top.Wade.nav.adapter.openLock.apply(e,[a,n,d,s,c?c:p,u]):r()?void t.nav.adapter.openLock.apply(e,[a,n,d,s,c,u]):void t.redirect.toPage(c,n,d,s)},openByUrl:function(a,n,l,s){return a&&t.isString(a)||t.error("title"+t.lang.get("ui.nav.notnull")),n&&t.isString(n)||t.error("url"+t.lang.get("ui.nav.notnull")),n.indexOf("?")<0&&!l&&(l=t.redirect.getPathName()),o(!0)&&top.Wade.extNavAdapter.openByUrl?(n=d(l,n),void top.Wade.extNavAdapter.openByUrl.apply(e,[a,n,l,s])):i()?void top.Wade.nav.adapter.openByUrl.apply(e,[a,n,l,s]):r()?void t.nav.adapter.openByUrl.apply(e,[a,n,l,s]):void t.redirect.toUrl(n)},openLockByUrl:function(a,n){return a&&t.isString(a)||t.error("title"+t.lang.get("ui.nav.notnull")),n&&t.isString(n)||t.error("url"+t.lang.get("ui.nav.notnull")),n.indexOf("?")<0&&!subsys&&(subsys=t.redirect.getPathName()),o(!0)&&top.Wade.extNavAdapter.openLockByUrl?(n=d(subsys,n),void top.Wade.extNavAdapter.openLockByUrl.apply(e,[a,n,subsys,data])):i()?void top.Wade.nav.adapter.openLockByUrl.apply(e,[a,n,subsys,data]):r()?void t.nav.adapter.openLockByUrl.apply(e,[a,n,subsys,data]):void t.redirect.toUrl(n)},redirect:function(a,n,d,s,c,u){var p=t.redirect.getPathName();if(o(!0)&&top.Wade.extNavAdapter.redirectByUrl){var g=l(c,n,d,s);return void top.Wade.extNavAdapter.redirectByUrl.apply(e,[a,g,c?c:p,u])}return i()?void top.Wade.nav.adapter.redirect.apply(e,[a,n,d,s,c?c:p,u]):r()?void t.nav.adapter.redirect.apply(e,[a,n,d,s,c,u]):void t.redirect.toPage(c,n,d,s)},redirectByUrl:function(a,n,l,s){return a&&t.isString(a)||t.error("title"+t.lang.get("ui.nav.notnull")),n&&t.isString(n)||t.error("url"+t.lang.get("ui.nav.notnull")),n.indexOf("?")<0&&!l&&(l=t.redirect.getPathName()),o(!0)&&top.Wade.extNavAdapter.redirectByUrl?(n=d(l,n),void top.Wade.extNavAdapter.redirectByUrl.apply(e,[a,n,l,s])):i()?void top.Wade.nav.adapter.redirectByUrl.apply(e,[a,n,l,s]):r()?void t.nav.adapter.redirectByUrl.apply(e,[a,n,l,s]):void t.redirect.toUrl(n)},reload:function(a){var n=e.location.href;if(n){if(n=n.replace(/#.*$/,""),o(!0)&&top.Wade.extNavAdapter.reload)return top.Wade.extNavAdapter.reload(n,a);if(i())return top.Wade.nav.adapter.reload(n,a);if(r())return t.nav.adapter.reload(n,a)}},getData:function(){return o(!0)&&top.Wade.extNavAdapter.getData?top.Wade.extNavAdapter.getData():i()?top.Wade.nav.adapter.getData():r()?t.nav.adapter.getData():void 0},getDataByTitle:function(e){return e&&t.isString(e)||t.error("title"+t.lang.get("ui.nav.notnull")),o(!0)&&top.Wade.extNavAdapter.getDataByTitle?top.Wade.extNavAdapter.getDataByTitle(e):i()?top.Wade.nav.adapter.getDataByTitle(e):r()?t.nav.adapter.getDataByTitle(e):void 0},getTitle:function(){return o(!0)&&top.Wade.extNavAdapter.getTitle?top.Wade.extNavAdapter.getTitle():i()?top.Wade.nav.adapter.getTitle():r()?t.nav.adapter.getTitle():void 0},getContentWindow:function(){return o(!0)&&top.Wade.extNavAdapter.getContentWindow?top.Wade.extNavAdapter.getContentWindow():i()?top.Wade.nav.adapter.getContentWindow():r()?t.nav.adapter.getContentWindow():void 0},getContentWindowByTitle:function(e){return e&&t.isString(e)||t.error("title"+t.lang.get("ui.nav.notnull")),o(!0)&&top.Wade.extNavAdapter.getContentWindowByTitle?top.Wade.extNavAdapter.getContentWindowByTitle(e):i()?top.Wade.nav.adapter.getContentWindowByTitle(e):r()?t.nav.adapter.getContentWindowByTitle(e):void 0},switchByTitle:function(e){return e&&t.isString(e)||t.error("title"+t.lang.get("ui.nav.notnull")),o(!0)&&top.Wade.extNavAdapter.switchByTitle?top.Wade.extNavAdapter.switchByTitle(e):i()?top.Wade.nav.adapter.switchByTitle(e):!!r()&&t.nav.adapter.switchByTitle(e)},close:function(){return o(!0)&&top.Wade.extNavAdapter.close?top.Wade.extNavAdapter.close():i()?top.Wade.nav.adapter.close():r()?t.nav.adapter.close():(e.close(),!1)},closeByTitle:function(a){return a&&t.isString(a)||t.error("title"+t.lang.get("ui.nav.notnull")),o(!0)&&top.Wade.extNavAdapter.closeByTitle?top.Wade.extNavAdapter.closeByTitle(a):i()?top.Wade.nav.adapter.closeByTitle(a):r()?t.nav.adapter.closeByTitle(a):(e.close(),!1)},createAdapter:function(e){e&&t.isPlainObject(e)||t.error(t.lang.get("ui.nav.navobj")),t.each("init,open,openByUrl,redirect,redirectByUrl,reload,getData,getDataByTitle,getTitle,getContentWindow,getContentWindowByTitle,switchByTitle,close,closeByTitle".split(","),function(a,n){e[n]&&t.isFunction(e[n])||t.error(t.lang.get("ui.nav.navimp",'"'+(t.nav.methods[n]?t.nav.methods[n]:n)+'"'))}),t.nav.adapter=e}},t(function(){t.nav.init(e,a)}),e.openNav=t.nav.open,e.openNavByUrl=t.nav.openByUrl,e.redirectNav=t.nav.redirect,e.redirectNavByUrl=t.nav.redirectByUrl,e.reloadNav=t.nav.reload,e.closeNav=t.nav.close,e.closeNavByTitle=t.nav.closeByTitle,e.getNavTitle=t.nav.getTitle,e.getNavContent=t.nav.getContentWindow,e.getNavContentByTitle=t.nav.getContentWindowByTitle,e.switchToNav=t.nav.switchByTitle,e.getNavData=t.nav.getData,e.getNavDataByTitle=t.nav.getDataByTitle}}(window.Wade,window,document),/*!
 * resource util for webframe
 * http://www.wadecn.com/
 * auth:xiedx@asiainfo.com
 * Copyright 2011, WADE
 */
function(t,e,a){"use strict";t&&"undefined"==typeof t.resource&&(t.resource={combinePath:function(a,n,r){if(a&&t.isString(a)){var i=e.releaseNumber||t.ctx.attr("v"),o=e.wadeWebResourcePath&&t.isString(e.wadeWebResourcePath);return r||(n&&o?e.wadeWebResourcePath&&(a=t.combinePath(e.wadeWebResourcePath,a)):e.webResourcePath&&(a=t.combinePath(e.webResourcePath,a))),a+=n&&o?(a.indexOf("?")>-1?"&":"?")+"v="+e.wadeWebResourceVersion:(a.indexOf("?")>-1?"&":"?")+"v="+i}}},e.includeScript=function(e,a,n,r){e=t.resource.combinePath(e,n,r),t.includeScript(e,a)},t.extend({vendorIncludeScript:function(a,n,r,i){a&&(a=(""+a).replace(/(.*)\/([0-9a-zA-Z_-]+).js/g,"$1/$2"+t.feat.osSuffix+".js"),t.isFunction(e.includeScript)?e.includeScript(a,n,r,i):t.includeScript(a,n))}}),e.vendorIncludeScript=t.vendorIncludeScript,a.write('<script src="handler?clazz=com.ailk.web.BaseHttpHandlerJSVariable&method=init&v='+t.ctx.attr("v")+'"></script>\r\n'))}(window.Wade,window,document);