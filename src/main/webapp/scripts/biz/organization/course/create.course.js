(function($){
    $.extend({course:{
            init : function(){
                window["UI-popup"] = new Wade.Popup("UI-popup");
                $.Select.append(
                    // 对应元素，在 el 元素下生成下拉框，el 可以为元素 id，或者原生 dom 对象
                    "course_type_container",
                    // 参数设置
                    {
                        id:"TYPE",
                        name:"TYPE",
                        addDefault:false
                    },
                    // 数据源，可以为 JSON 数组，或 JS 的 DatasetLsit 对象
                    [
                        {TEXT:"职前", VALUE:"1"},
                        {TEXT:"在职", VALUE:"2"}
                    ]
                );


                $("#TYPE").bind("change", function(){
                    $("#TYPE").val(this.value); // 当前值
                });

                $("#TYPE").val("1");

                window["START_DATE"] = new Wade.DateField(
                    "START_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false
                    }
                );

                window["END_DATE"] = new Wade.DateField(
                    "END_DATE",
                    {
                        dropDown:true,
                        format:"yyyy-MM-dd",
                        useTime:false
                    }
                );
            },

            createPlan : function() {
                var startDate = $("#START_DATE").val();
                var endDate = $("#END_DATE").val();
                alert(startDate);
                alert(endDate);
                if(startDate == null || startDate == "undefined" || startDate == "") {
                    return;
                }

                if(endDate == null || endDate == "undefined" || endDate == "") {
                    return;
                }

                var differ = ((new Date(endDate)) - (new Date(startDate))) / 1000*60*60*24;
                alert(differ);
            },

            submit : function() {

            }
        }});
})($);