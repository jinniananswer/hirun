package com.hirun.web.biz.organization.score;

import com.hirun.pub.consts.FileConst;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Controller
public class ScoreController extends RootController {


    @RequestMapping("/initExamQuery")
    public @ResponseBody String initExamQuery(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.score.ScoreService.initExamQuery", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryExamScore")
    public @ResponseBody String queryExamScore(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.score.ScoreService.queryExamScore", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryPostJobScore")
    public @ResponseBody String queryPostJobScore(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.score.ScoreService.queryPostJobScore", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/inputScore")
    public @ResponseBody String inputScore(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.score.ScoreService.inputScore", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initScoreQuery")
    public @ResponseBody String initScoreQuery(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.score.ScoreService.initScoreQuery", parameter);
        return response.toJsonString();
    }
}
