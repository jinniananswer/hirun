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
@ResponseBody
public class EmployeeController extends RootController {

    @RequestMapping(value = "/employee/getAllSubordinatesCounselors", method = RequestMethod.POST)
    public String editCust(@RequestParam Map pageData) throws Exception {
        ServiceResponse response = ServiceClient.call("OrgCenter.employee.EmployeeService.getAllSubordinatesCounselors", pageData);
        return response.toJsonString();
    }

    @RequestMapping("/queryContacts")
    public @ResponseBody String queryContacts(HttpServletRequest request) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("SEARCH_TEXT", request.getParameter("SEARCH_TEXT"));
        ServiceResponse response = ServiceClient.call("OrgCenter.personnel.EmployeeService.queryContacts", parameter);
        return response.toJsonString();
    }
}
