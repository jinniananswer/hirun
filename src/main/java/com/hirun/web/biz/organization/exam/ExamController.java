package com.hirun.web.biz.organization.exam;

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
 * @create: 2019-01-03 22:41
 **/
@Controller
public class ExamController extends RootController {

    @RequestMapping("/initPreworkExam")
    public @ResponseBody String initPreworkExam(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.exam.ExamService.initPreworkExam", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/startPreworkExam")
    public @ResponseBody String startPreworkExam(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.exam.ExamService.startPreworkExam", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/submitExam")
    public @ResponseBody String submitExam(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.exam.ExamService.submitExam", parameter);
        return response.toJsonString();
    }
}
