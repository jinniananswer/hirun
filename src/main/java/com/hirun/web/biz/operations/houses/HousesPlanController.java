package com.hirun.web.biz.operations.houses;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

        return response.toJsonString();
    }

    @RequestMapping("/initArea")
    public @ResponseBody String initArea(HttpServletRequest req) throws Exception{
        String cityId = req.getParameter("CITY_ID");
        ServiceRequest request = new ServiceRequest();
        request.set("CITY_ID", cityId);

        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initArea", request);

        return response.toJsonString();
    }

    @RequestMapping("/initCounselors")
    public @ResponseBody String initCounselors(HttpServletRequest req) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("ORG_ID", req.getParameter("ORG_ID"));

        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initCounselors", parameter);

        return response.toJsonString();
    }


    @RequestMapping("/submitHousesPlan")
    public @ResponseBody String submitHousesPlan(@RequestParam Map submitData) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.createHousesPlan", submitData);
        return response.toJsonString();
    }

    @RequestMapping("/queryHousesPlan")
    public @ResponseBody String queryHousesPlan(@RequestParam Map condition) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryHousesPlan", condition);

        return response.toJsonString();
    }

    @RequestMapping("/submitAudit")
    public @ResponseBody String submitAudit(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.submitAudit", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToChangeHousesPlan")
    public String redirectChangeHousesPlan() throws Exception{
        return "/biz/operations/houses/change_houses_plan";
    }

    @RequestMapping("/initChangeHousesPlan")
    public @ResponseBody String initChangeHousesPlan(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSES_ID", request.getParameter("HOUSES_ID"));
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initChangeHousesPlan", parameter);

        return response.toJsonString();
    }

    @RequestMapping("/queryHousesByEmployeeId")
    public @ResponseBody String queryHousesByEmployeeId(@RequestParam Map condition) throws Exception{
        String employeeId = (String)condition.get("EMPLOYEE_ID");
        if(StringUtils.isBlank(employeeId)) {
            logger.info("/queryHousesByEmployeeId没有取到EMPLOYEE_ID");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            employeeId = sessionEntity.get("EMPLOYEE_ID");
            logger.info("/queryHousesByEmployeeId从session里【"+session.getId()+"】取EMPLOYEE_ID，值为" + employeeId);
        }

        condition.put("EMPLOYEE_ID", employeeId);
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryHousesByEmployeeId", condition);
        return response.toJsonString();
    }

    @RequestMapping("/queryHousesByEmployeeId2")
    public @ResponseBody String queryHousesByEmployeeId2(@RequestParam Map condition) throws Exception{
        String employeeId = (String)condition.get("EMPLOYEE_ID");
        if(StringUtils.isBlank(employeeId)) {
            logger.info("/queryHousesByEmployeeId2没有取到EMPLOYEE_ID");
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            SessionEntity sessionEntity = HttpSessionManager.getSessionEntity(session.getId());
            employeeId = sessionEntity.get("EMPLOYEE_ID");
            logger.info("/queryHousesByEmployeeId2从session里【"+session.getId()+"】取EMPLOYEE_ID，值为" + employeeId);
        }

        condition.put("EMPLOYEE_ID", employeeId);
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryHousesByEmployeeId", condition);
        return response.toJsonString();
    }

    @RequestMapping("/changeHousesPlan")
    public @ResponseBody String changeHousesPlan(@RequestParam Map submitData) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.changeHousePlan", submitData);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToDetail")
    public String redirectToDetail() throws Exception{
        return "/biz/operations/houses/house_detail";
    }

    @RequestMapping("/showHouseDetail")
    public @ResponseBody String showHouseDetail(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSES_ID", request.getParameter("HOUSES_ID"));
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.showHouseDetail", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryHouses")
    public @ResponseBody String queryHouses(@RequestParam Map condition) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryHouses", condition);
        return response.toJsonString();
    }

    @RequestMapping("/queryMyHouses")
    public @ResponseBody String queryMyHouses(HttpSession session) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        String sessionId = session.getId();
        SessionEntity se = HttpSessionManager.getSessionEntity(sessionId);
        parameter.put("EMPLOYEE_ID", se.get("EMPLOYEE_ID"));

        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryMyHouses", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/showMyHouseDetail")
    public @ResponseBody String queryMyHouseDetail(HttpServletRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSE_ID", request.getParameter("HOUSES_ID"));
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.showMyHouseDetail", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToMyDetail")
    public String redirectToMyDetail() throws Exception{
        return "/biz/operations/houses/my_house_detail";
    }

    @RequestMapping("/initCreateScatterHouses")
    public @ResponseBody String initCreateScatterHouses() throws Exception{
        ServiceRequest request = new ServiceRequest();
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initCreateScatterHouses", request);
        return response.toJsonString();
    }

    @RequestMapping("/submitScatterHouses")
    public @ResponseBody String submitScatterHouses(@RequestParam Map submitData) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.submitScatterHouses", submitData);
        return response.toJsonString();
    }

    @RequestMapping("/queryScatterHousesByCond")
    public @ResponseBody String queryScatterHousesByCond(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryScatterHousesByCond", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryMyScatterHouses")
    public @ResponseBody String queryMyScatterHouses(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.queryMyScatterHouses", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initChangeScatterHouses")
    public @ResponseBody String initChangeScatterHouses(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSES_ID", request.getParameter("HOUSES_ID"));
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.initChangeScatterHouses", parameter);

        return response.toJsonString();
    }

    @RequestMapping("/redirectToChangeScatter")
    public String redirectToChangeScatter() throws Exception {
        return "/biz/operations/houses/change_scatter_houses";
    }

    @RequestMapping("/changeScatterHouse")
    public @ResponseBody String changeScatterHouse(@RequestParam Map parameter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.changeScatterHouse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToScatterDetail")
    public String redirectToScatterDetail() throws Exception{
        return "/biz/operations/houses/my_scatter_house_detail";
    }

    @RequestMapping("/showMyScatterHouseDetail")
    public @ResponseBody String showMyScatterHouseDetail(HttpServletRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("HOUSES_ID", request.getParameter("HOUSES_ID"));
        ServiceResponse response = ServiceClient.call("OperationCenter.house.HousesService.showMyScatterHouseDetail", parameter);
        return response.toJsonString();
    }
}
