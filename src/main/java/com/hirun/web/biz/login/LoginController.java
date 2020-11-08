package com.hirun.web.biz.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.security.Encryptor;
import com.most.core.web.RootController;
import com.most.core.web.agent.UserAgentUtil;
import com.most.core.web.client.ServiceClient;
import com.most.core.web.session.HttpSessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.*;
import java.util.Map;

@Controller
public class LoginController extends RootController {

    @RequestMapping(value="/index")
    public String index(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        UserAgentUtil agentUtil = new UserAgentUtil(userAgent);
        if(agentUtil.phone())
            return "phone/home";
        else
            return "index";
    }

    @RequestMapping(value="/login")
    public String loginAccess(HttpServletRequest request){

        String userAgent = request.getHeader("User-Agent");
        UserAgentUtil agentUtil = new UserAgentUtil(userAgent);

        if(agentUtil.phone()){
            return "phone/login_new";
        }
        else {
            return "login";
        }
    }

    @RequestMapping(value = "/loginPost", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestParam Map loginData, HttpSession session, HttpServletRequest request, HttpServletResponse servletResponse) throws Exception{
        SessionEntity sessionEntity = new SessionEntity();
        HttpSessionManager.putSessionEntity(session.getId(), sessionEntity);

        String userAgent = request.getHeader("User-Agent");
        UserAgentUtil agentUtil = new UserAgentUtil(userAgent);
        if(agentUtil.ios())
            sessionEntity.put("OPERATION_SYSTEM","IOS");
        else if(agentUtil.android() || agentUtil.androidICS())
            sessionEntity.put("OPERATION_SYSTEM", "ANDROID");

        ServiceResponse response = ServiceClient.call("OrgCenter.login.LoginService.login", loginData);

        if(response.isSuccess()) {

            JSONObject userInfo = response.getJSONObject("USER");
            JSONObject employeeInfo = response.getJSONObject("EMPLOYEE");
            JSONArray jobRoles = response.getJSONArray("JOB_ROLE");

            UserEntity user = new UserEntity(JSON.parseObject(userInfo.toJSONString(), Map.class));
            EmployeeEntity employee = new EmployeeEntity(JSON.parseObject(employeeInfo.toJSONString(), Map.class));

            session.setAttribute("USER", user);
            session.setAttribute("EMPLOYEE", employee);
            session.setAttribute("JOB_ROLE", jobRoles);
            if(user != null) {
                sessionEntity.setUserId(user.getUserId());
                sessionEntity.setUsername(user.getUserName());
                sessionEntity.put("USER", userInfo);
            }

            if(employee != null) {
                sessionEntity.put("EMPLOYEE_ID", employee.getEmployeeId());
                sessionEntity.put("EMPLOYEE_NAME", employee.getName());
                sessionEntity.put("EMPLOYEE", employeeInfo);
            }

            if(ArrayTool.isNotEmpty(jobRoles))
                sessionEntity.put("JOB_ROLES", jobRoles);

            session.setAttribute("USER", user);

            if(StringUtils.equals("1", request.getParameter("automatic"))){
                //自动登陆保存
                Cookie cookie = new Cookie("auth",user.getUserName()+"@"+user.getPassword());
                cookie.setMaxAge(5*365*24*60*60);
                cookie.setPath("/");
                servletResponse.addCookie(cookie);
            }
        }
        return response.toJsonString();
    }

    @RequestMapping("/phone_index")
    public String phoneLogin() throws Exception{
        return "/phone/phone_index";
    }
}
