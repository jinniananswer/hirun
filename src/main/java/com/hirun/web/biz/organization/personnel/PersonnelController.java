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
        JSONObject rst = new JSONObject();
        if(StringUtils.equals(userPassword, encrytOldPassword)){
            rst.put("RESULT_CODE", "0");
        }
        else{
            rst.put("RESULT_CODE", "-1");
            rst.put("RESULT_INFO", "原密码不正确");
        }
        return rst.toJSONString();
    }

    @RequestMapping("/submitChangePassword")
    public @ResponseBody String submitChangePassword(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.menu.PersonnelService.changePassword", parameter);
        return "";
    }
}
