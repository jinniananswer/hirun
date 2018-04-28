var custAdd = {
    init : function () {
        window["SEX"] = new Wade.Switch("SEX",{
            switchOn:true,
            onValue:"1",
            offValue:"2",
            onColor:"blue",
            offColor:"red"
        });

        window["HOUSE_ID"] = new Wade.Select(
            "HOUSE_ID",
            {
                value:"",
                inputable:false,
                disabled:false,
                addDefault:true,
                selectedIndex:-1,
                optionAlign:"left"
            }
        );

        $("#SEX").val("1");

        HOUSE_ID.append("湘江世纪城一期","1");
        HOUSE_ID.append("保利西海岸一期","2");
        HOUSE_ID.append("四方坪一期","3");
        HOUSE_ID.append("四方坪二期","4");
    },
    submit : function () {
        if($.validate.verifyAll("custForm")){
            var param = $.buildJsonData("custForm");
            $.ajaxPost('cust/addCust',param,function(data){
                // var result = new Wade.DataMap(data);

            },function(){
                alert('error');
            });
        }
        else{

        }
    }
}