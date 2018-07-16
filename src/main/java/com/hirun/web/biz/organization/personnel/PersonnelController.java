package com.hirun.web.biz.organization.personnel;

import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.security.Encryptor;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class PersonnelController extends RootController {

    @RequestMapping("/checkOldPassword")
    public @ResponseBody String checkOldPassword(HttpServletRequest request) throws Exception{
        String oldPassword = request.getParameter("OLD_PASSWORD");
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("USER");
        String userPassword = user.getPassword();
        String encrytOldPassword = Encryptor.encryptMd5(oldPassword);
        ServiceResponse response = new ServiceResponse();
        response.setSuccess();
        if(StringUtils.equals(userPassword, encrytOldPassword)){
            response.set("RESULT_CODE", "0");
        }
        else{
            response.set("RESULT_CODE", "-1");
            response.set("RESULT_INFO", "原密码不正确");
        }
        return response.toJsonString();
    }

    @RequestMapping("/submitChangePassword")
    public @ResponseBody String submitChangePassword(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.personnel.PersonnelService.changePassword", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initCreateEmployee")
    public @ResponseBody String initCreateEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.personnel.PersonnelService.initCreateEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initParentEmployee")
    public @ResponseBody String initParentEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.personnel.PersonnelService.initParentEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/createEmployee")
    public @ResponseBody String createEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.personnel.PersonnelService.createEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/destroyEmployee")
    public @ResponseBody String destroyEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.personnel.PersonnelService.destroyEmployee", parameter);
        return response.toJsonString();
    }
}
