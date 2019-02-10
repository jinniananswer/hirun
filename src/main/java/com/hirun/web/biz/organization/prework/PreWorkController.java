package com.hirun.web.biz.organization.prework;

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
 * @create: 2019-01-27 00:35
 **/
@Controller
public class PreWorkController extends RootController {

    @RequestMapping("/initQueryPreWorkEvaluation")
    public @ResponseBody String initQueryPreWorkEvaluation(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initQueryPreWorkEvaluation", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToViewPreWorkNotice")
    public String redirectToViewPreWorkNotice(HttpServletRequest request) throws Exception {
        return "/biz/organization/prework/show_prework_evaluation_notice";
    }

    @RequestMapping("/initViewPreWorkNotice")
    public @ResponseBody String initViewPreWorkNotice(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initViewPreWorkNotice", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToSignPreWork")
    public String redirectToSignPreWork(HttpServletRequest request) throws Exception {
        return "/biz/organization/prework/choose_employee_sign_prework";
    }

    @RequestMapping("/initChooseEmployeeSignPreWork")
    public @ResponseBody String initChooseEmployeeSignPreWork(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initChooseEmployeeSignPreWork", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/addNewPreworkSign")
    public @ResponseBody String addNewPreworkSign(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.addNewPreworkSign", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToPreworkSignList")
    public String redirectToPreworkSignList(HttpServletRequest request) throws Exception {
        return "/biz/organization/prework/prework_sign_list";
    }

    @RequestMapping("/initQueryPreworkSignList")
    public @ResponseBody String initQueryPreworkSignList(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.prework.PreWorkService.initQueryPreworkSignList", parameter);
        return response.toJsonString();
    }
}
