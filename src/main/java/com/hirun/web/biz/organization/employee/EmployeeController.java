package com.hirun.web.biz.organization.employee;

import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.security.Encryptor;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/5/18 16:48
 * @Description:
 */
@Controller
public class EmployeeController extends RootController {

    @RequestMapping(value = "/employee/getAllSubordinatesCounselors", method = RequestMethod.POST)
    public @ResponseBody String editCust(@RequestParam Map pageData) throws Exception {
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.getAllSubordinatesCounselors", pageData);
        return response.toJsonString();
    }

    @RequestMapping("/queryContacts")
    public @ResponseBody String queryContacts(HttpServletRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("SEARCH_TEXT", request.getParameter("SEARCH_TEXT"));
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.queryContacts", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/employee/entryHoliday")
    public @ResponseBody String entryHoliday(@RequestParam Map pageData) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.entryHoliday", pageData);
        return response.toJsonString();
    }

    @RequestMapping("/initCreateEmployee")
    public @ResponseBody String initCreateEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.initCreateEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initParentEmployee")
    public @ResponseBody String initParentEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.initParentEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/createEmployee")
    public @ResponseBody String createEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.createEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/hasSubordinates")
    public @ResponseBody String hasSubordinates(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.hasSubordinates", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/destroyEmployee")
    public @ResponseBody String destroyEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.destroyEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initQueryEmployees")
    public @ResponseBody String initQueryEmployees(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.initQueryEmployees", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryEmployees")
    public @ResponseBody String queryEmployees(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.queryEmployees", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectChangeEmployee")
    public String redirectChangeEmployee() throws Exception{
        return "/biz/organization/personnel/change_employee";
    }

    @RequestMapping("/initChangeEmployee")
    public @ResponseBody String initChangeEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.initChangeEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/changeEmployee")
    public @ResponseBody String changeEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.changeEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryEmployeeFuncs")
    public @ResponseBody String queryEmployeeFuncs(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.queryEmployeeFuncs", parameter);
        return response.toJsonString();
    }


    @RequestMapping("/redirectAssignEmployeeFunc")
    public String redirectAssignEmployeeFunc() throws Exception{
        return "/biz/organization/personnel/assign_permission_detail";
    }

    @RequestMapping("/assignPermission")
    public @ResponseBody String assignPermission(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.assignPermission", parameter);
        return response.toJsonString();
    }
}
