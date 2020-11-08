package com.hirun.web.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.web.RootController;
import com.most.core.web.agent.UserAgentUtil;
import com.most.core.web.client.ServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController extends RootController {

    @RequestMapping("/initUser")
    public @ResponseBody String initUser(HttpServletRequest request, HttpSession session) throws Exception{
        UserEntity user = (UserEntity)session.getAttribute("USER");
        EmployeeEntity employee = (EmployeeEntity) session.getAttribute("EMPLOYEE");
        JSONArray jobRoles = (JSONArray)session.getAttribute("JOB_ROLE");

        String path = request.getContextPath();
        String basePath = request.getScheme()+"://" +request.getServerName()+":" +request.getServerPort()+path+"/" ;

        ServiceResponse rst = new ServiceResponse();
        rst.setSuccess();
        if(employee != null){
            rst.set("NAME", employee.getName());
            String jobRoleName = "";
            String orgName = "";
            if(ArrayTool.isNotEmpty(jobRoles)){
                int size = jobRoles.size();
                for(int i=0;i<size;i++){
                    JSONObject jobRole = jobRoles.getJSONObject(i);
                    String tempJobRoleName = jobRole.getString("JOB_ROLE_NAME");
                    if(StringUtils.isBlank(jobRoleName)){
                        jobRoleName += tempJobRoleName;
                    }
                    else{
                        jobRoleName += "/"+tempJobRoleName;
                    }
                    JSONObject org = jobRole.getJSONObject("ORG_INFO");
                    if(org != null){
                        String tempOrgName = org.getString("NAME");
                        if(StringUtils.isBlank(orgName)){
                            orgName += tempOrgName;
                        }
                        else{
                            orgName += "/" + orgName;
                        }
                    }

                }
            }
            rst.set("ORG_NAME", orgName);
            rst.set("JOB_ROLE_NAME", jobRoleName);

            if("1".equals(employee.getSex())){
                rst.set("HEAD_IMAGE", basePath + "frame/img/male.png");
            }
            else{
                rst.set("HEAD_IMAGE", basePath + "frame/img/female.png");
            }
        }
        else{
            rst.set("NAME", user.getUserName());
            rst.set("ORG_NAME", "");
            rst.set("JOB_ROLE_NAME", "");
            rst.set("HEAD_IMAGE", basePath + "frame/img/male.png");
        }
        return rst.toJsonString();
    }

    @RequestMapping("/initMenu")
    public @ResponseBody String initMenu(HttpSession session) throws Exception{
        ServiceRequest request = new ServiceRequest();
        ServiceResponse response = ServiceClient.call("OrgCenter.menu.MenuService.loadMenus", request);
        return response.toJsonString();
    }

    @RequestMapping("/out")
    public String loginOut(HttpSession session, HttpServletRequest request) throws Exception{
        session.invalidate();
        String userAgent = request.getHeader("User-Agent");
        UserAgentUtil agentUtil = new UserAgentUtil(userAgent);
        if(agentUtil.phone()){
            return "phone/login_new";
        }
        else {
            return "login";
        }
    }
}
