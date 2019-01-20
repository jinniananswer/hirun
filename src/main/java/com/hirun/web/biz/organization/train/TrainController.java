package com.hirun.web.biz.organization.train;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("/initManagerTrains")
    public @ResponseBody String initManagerTrains(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initManagerTrains", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initQueryTrains")
    public @ResponseBody String initQueryTrains(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initQueryTrains", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToTrainDetail")
    public String redirectToTrainDetail(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/train_detail";
    }

    @RequestMapping("/initTrainDetail")
    public @ResponseBody String initTrainDetail(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initTrainDetail", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/deleteTrain")
    public @ResponseBody String deleteTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.deleteTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToChangeTrain")
    public String redirectToChangeTrain(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/change_train";
    }

    @RequestMapping("/initChangeTrain")
    public @ResponseBody String initChangeTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initChangeTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/changeTrain")
    public @ResponseBody String changeTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.changeTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/signTrain")
    public @ResponseBody String signTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.signTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initQuerySignList")
    public @ResponseBody String initQuerySignList(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initQuerySignList", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToSignList")
    public String redirectToSignList(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/sign_list";
    }

    @RequestMapping("/auditSignTrain")
    public @ResponseBody String auditSignTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.auditSignTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/endSign")
    public @ResponseBody String endSign(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.endSign", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToSign")
    public String redirectToSign(HttpServletRequest request) throws Exception {
        return "/biz/organization/train/choose_employee_sign_train";
    }

    @RequestMapping("/initChooseEmployeeSignTrain")
    public @ResponseBody String initChooseEmployeeSignTrain(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.initChooseEmployeeSignTrain", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryWantSignEmployee")
    public @ResponseBody String queryWantSignEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.queryWantSignEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/signNewEmployee")
    public @ResponseBody String signNewEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.signNewEmployee", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/deleteSignedEmployee")
    public @ResponseBody String deleteSignedEmployee(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.train.TrainService.deleteSignedEmployee", parameter);
        return response.toJsonString();
    }
}
