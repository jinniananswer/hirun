package com.hirun.web.biz.custservice.citycabin;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CityCabinController extends RootController {

    @RequestMapping(value = "initManagerCityCabin")
    public @ResponseBody
    String initManagerCityCabin(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.citycabin.CityCabinService.initManagerCityCabin", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "createCityCabin")
    public @ResponseBody
    String createCityCabin(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.citycabin.CityCabinService.createCityCabin", paramter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToChangeCityCabin")
    public String redirectToChangeTrain(HttpServletRequest request) throws Exception {
        return "/biz/operations/customerservice/citycabin/change_citycabin";
    }

    @RequestMapping(value = "initChangeCityCabin")
    public @ResponseBody
    String initChangeCityCabin(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.citycabin.CityCabinService.initChangeCityCabin", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "changeCityCabin")
    public @ResponseBody
    String changeCityCabin(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.citycabin.CityCabinService.changeCityCabin", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "deleteCityCabin")
    public @ResponseBody
    String deleteCityCabin(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.citycabin.CityCabinService.deleteCityCabin", paramter);
        return response.toJsonString();
    }

    @RequestMapping(value = "queryCityCabinInfo")
    public @ResponseBody
    String queryCityCabinInfo(@RequestParam Map paramter) throws Exception {
        ServiceResponse response = ServiceClient.call("OperationCenter.custservice.citycabin.CityCabinService.queryCityCabinInfo", paramter);
        return response.toJsonString();
    }
}