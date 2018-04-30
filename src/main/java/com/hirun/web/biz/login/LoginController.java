package com.hirun.web.biz.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.session.BizSessionEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController extends RootController {

    @RequestMapping(value="/login")
    public String loginAccess(){
        return "login";
    }

    @RequestMapping(value = "/loginPost", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestParam Map loginData, HttpSession session) throws Exception{

        ServiceResponse response = ServiceClient.call("OrgCenter.login.LoginService.login", loginData);

        if(response.isSuccess()) {

            JSONObject userInfo = response.getJSONObject("USER");
            JSONObject employeeInfo = response.getJSONObject("EMPLOYEE");
            JSONArray jobRoles = response.getJSONArray("JOB_ROLE");

            UserEntity user = new UserEntity(JSON.parseObject(userInfo.toJSONString(), Map.class));
            EmployeeEntity employee = new EmployeeEntity(JSON.parseObject(employeeInfo.toJSONString(), Map.class));
            BizSessionEntity sessionEntity = new BizSessionEntity();
            if(user != null)
                sessionEntity.setUserId(user.getUserId());

            if(employee != null)
                sessionEntity.setEmployeeId(employee.getEmployeeId());

            if(ArrayTool.isNotEmpty(jobRoles))
                sessionEntity.setJobRoles(jobRoles);

            HttpSessionManager.putSessionEntity(session.getId(), sessionEntity);

            session.setAttribute("USER", user);
        }
        return response.toJsonString();
    }
}
