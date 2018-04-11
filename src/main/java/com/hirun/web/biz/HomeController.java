package com.hirun.web.biz;

import com.most.core.web.RootController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController extends RootController {

    @RequestMapping("/index1")
    public String index(){
        logger.debug("it is the first page");
        return "home";
    }
}
