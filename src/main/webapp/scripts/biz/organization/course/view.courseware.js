(function($){
    $.extend({courseware:{
            init : function() {
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.ajaxPost('initPreworkCourseQuery',null,function(data){
                    var rst = $.DataMap(data);
                    $.courseware.drawCourseType(rst.get("COURSE"));
                    $.courseware.drawCourse(rst.get("COURSE_LIST"));
                });
            }
        }});
})($);