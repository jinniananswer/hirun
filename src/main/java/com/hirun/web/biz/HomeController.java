package com.hirun.web.biz;

import com.alibaba.fastjson.JSONArray;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController extends RootController {

    @RequestMapping("/initMenu")
    public @ResponseBody String initMenu(HttpSession session) throws Exception{
        ServiceRequest request = new ServiceRequest();
        ServiceResponse response = ServiceClient.call("OrgCenter.menu.MenuService.loadMenus", request);
        JSONArray menus = response.getJSONArray("MENUS");
        logger.debug("=============================menus length====================="+menus.size());
        session.setAttribute("MENUS", menus);
        return menus.toJSONString();
    }
}
