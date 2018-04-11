package com.hirun.web.biz.controller.login;

import com.hirun.web.biz.data.login.LoginData;
import com.most.core.web.RootController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController extends RootController {

    @RequestMapping("/login")
    public String loginAccess(){
        return "login";
    }

    @RequestMapping(value = "/loginPost", method = RequestMethod.POST)
    public String login(LoginData loginData){
        logger.debug("***********************loginData*********************"+loginData.getUsername());
        return "index";
    }
}
