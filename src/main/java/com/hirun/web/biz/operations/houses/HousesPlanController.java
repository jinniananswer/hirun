package com.hirun.web.biz.operations.houses;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/26 10:17
 * @Description:
 */
@Controller
public class HousesPlanController extends RootController{

    @RequestMapping("/initCreateHousesPlan")
    public @ResponseBody String initCreate() throws Exception{
        ServiceRequest request = new ServiceRequest();

        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initCreatePlan", request);
        JSONObject rst = new JSONObject();
        rst.put("CITYS", response.getJSONArray("CITYS"));
        rst.put("TODAY", response.getString("TODAY"));
        return rst.toJSONString();
    }

    @RequestMapping("/initArea")
    public @ResponseBody String initArea(HttpServletRequest req) throws Exception{
        String cityId = req.getParameter("CITY_ID");
        ServiceRequest request = new ServiceRequest();
        request.set("CITY_ID", cityId);

        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initArea", request);
        JSONArray areas = response.getJSONArray("AREAS");
        JSONArray shops = response.getJSONArray("SHOPS");
        JSONObject obj = new JSONObject();
        obj.put("AREAS", areas);
        obj.put("SHOPS", shops);
        return obj.toJSONString();
    }

    @RequestMapping("/initCounselors")
    public @ResponseBody String initCounselors(HttpServletRequest req) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", req.getParameter("ORG_ID"));

        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initCounselors", parameter);

        JSONArray counselors = response.getJSONArray("COUNSELORS");
        return counselors.toJSONString();
    }


    @RequestMapping("/submitHousesPlan")
    public @ResponseBody String submitHousesPlan(@RequestParam Map submitData) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.createHousesPlan", submitData);
        return null;
    }

    @RequestMapping("/queryHousesPlan")
    public @ResponseBody String queryHousesPlan(@RequestParam Map condition) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryHousesPlan", condition);
        JSONArray data = response.getJSONArray("DATA");
        if(data == null){
            return "";
        }
        return data.toJSONString();
    }
}
