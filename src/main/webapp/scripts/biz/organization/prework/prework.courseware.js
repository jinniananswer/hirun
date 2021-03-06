(function($){
    $.extend({prework:{
            treeId : null,
            treeName : null,
            dataId : null,
            hasChild : null,

            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                window["courseTree"] = new Wade.Tree("courseTree");
                $("#courseTree").textAction(function(e, nodeData){
                    $.prework.treeId = nodeData.id;
                    $.prework.treeName = nodeData.text;
                    $.prework.dataId = nodeData.dataid;
                    $.prework.hasChild = nodeData.haschild;
                    let isExpand = (document.getElementById("courseTree○" + nodeData.dataid).className.indexOf("unfold") >= 0 ? true : false);
                    if (!isExpand && nodeData.haschild) {
                        window["courseTree"].expand("courseTree○" + nodeData.dataid);
                    } else if (isExpand) {
                        window["courseTree"].collapse("courseTree○" + nodeData.dataid);
                    }
                    return false;
                });

                $("#courseTree").expandAction(function(e, nodeData) {
                    $.prework.treeId = nodeData.id;
                    $.prework.treeName = nodeData.text;
                    $.prework.dataId = nodeData.dataid;
                    $.prework.hasChild = nodeData.haschild;
                    return true;
                });

                $.ajaxPost('initViewPreworkCourseware',null,function(data) {
                    var trees = data.COURSE_TREE;

                    if(trees != null){
                        window["courseTree"].data = trees;
                        window["courseTree"].init();
                        window["courseTree"].expandByPath("-1", "●");
                    }
                });
            },

            initCourseFile : function() {
                if(this.treeId == null) {
                    MessageBox.alert("请先选择需要查看课件的课程");
                    return false;
                }
                if(this.treeId == -1) {
                    MessageBox.alert("根结点无课件可查看");
                    return false;
                }

                $.ajaxPost('initCourseFile',"&COURSE_ID="+$.prework.treeId,function(data){
                    var rst = $.DataMap(data);
                    $.prework.drawFile(rst.get("FILES"));
                });
            },

            drawFile : function(datas) {
                var html = [];
                var ul = $("#courseFileArea");
                ul.empty();

                if(datas == null || datas.length <= 0) {
                    showPopup('UI-popup','UI-COURSE_FILE');
                    $("#messagebox").css("display","");
                    return;
                }
                $("#messagebox").css("display","none");
                var length = datas.length;
                for(var i=0;i<length;i++) {
                    var data = datas.get(i);
                    html.push("<li class='link' ontap=\"$.prework.openCourseware('"+data.get("STORAGE_PATH")+"','"+data.get("FILE_ID")+"')\">");
                    html.push("<div class='main'><div class='title'>");
                    html.push(data.get("NAME"));
                    html.push("</div></div>");
                    html.push("<div class='fn'>");
                    html.push("</div></li>");
                }
                $.insertHtml('beforeend', ul, html.join(""));
                showPopup('UI-popup','UI-COURSE_FILE');
            },

            openCourseware : function(url, fileId) {
                if($.os.phone) {

                    if($.os.ios) {
                        try {
                            window.webkit.messageHandlers.openCourseware.postMessage(url);
                        } catch (err) {

                        }
                    }
                    else{
                        document.location = "office://courseware?FILE_URL="+url;
                    }

                }
                else if($.os.pad) {
                    $.redirect.open('redirectToViewPdf?FILE_ID='+fileId,'资料详情');
                }
                else{
                    $.redirect.open('redirectToViewFile?FILE_ID='+fileId,'资料详情');
                }
            }
        }});
})($);