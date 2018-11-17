package com.hirun.web.biz.organization.course;

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

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-11-12 20:56
 **/
@Controller
public class ExamController extends RootController {

    @RequestMapping("/uploadExamGuideFile")
    public @ResponseBody
    String uploadExamGuideFile(HttpServletRequest request, @RequestParam("EXAM_GUIDE_FILE") CommonsMultipartFile file) throws Exception {
        String path=request.getSession().getServletContext().getRealPath("doc/");
        String now = TimeTool.now(TimeTool.TIME_PATTERN_MILLISECOND);
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        String newFileName = now+fileType;
        File newFile=new File(path, newFileName);
        file.transferTo(newFile);

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("NEW_FILE_NAME", newFileName);
        parameter.put("FILE_NAME", fileName);
        parameter.put("FILE_TYPE", fileType);
        ServiceResponse response = ServiceClient.call("OrgCenter.course.ExamService.uploadExamGuide", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initExamGuideQuery")
    public @ResponseBody String initPreworkCourseQuery(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        ServiceResponse response = ServiceClient.call("OrgCenter.course.ExamService.initExamGuideQuery", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryExamGuide")
    public @ResponseBody String queryExamGuide(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FILE_NAME", request.getParameter("FILE_NAME"));
        ServiceResponse response = ServiceClient.call("OrgCenter.course.ExamService.queryExamGuide", parameter);
        return response.toJsonString();
    }
}
