package com.hirun.web.biz.organization.train;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-12-12 23:54
 **/
@Controller
public class TrainController extends RootController {

    @RequestMapping("/initCreateTrain")
    public @ResponseBody String initCreateTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initCreateTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/createTrain")
    public @ResponseBody String createTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.createTrain", parameter);
        return response.toJsonString();
    }
}
