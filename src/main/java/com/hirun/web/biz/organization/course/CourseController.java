package com.hirun.web.biz.organization.course;

import com.alibaba.fastjson.JSONObject;
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
 * @create: 2018-10-21 22:17
 **/
@Controller
public class CourseController extends RootController {

    @RequestMapping("/initPreworkCourseUpload")
    public @ResponseBody String initPreworkCourseUpload(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.initPreworkCourseUpload", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/uploadPreworkCourseware")
    public @ResponseBody String uploadPreworkCourseware(HttpServletRequest request, @RequestParam("COURSE_FILE") CommonsMultipartFile file) throws Exception {
        String path=request.getSession().getServletContext().getRealPath("doc/");
        String now = TimeTool.now(TimeTool.TIME_PATTERN_MILLISECOND);
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = now+fileType;
        File newFile=new File(path, newFileName);
        file.transferTo(newFile);

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("NEW_FILE_NAME", newFileName);
        parameter.put("COURSE_ID", request.getParameter("COURSE_ID"));
        parameter.put("FILE_NAME", fileName);
        parameter.put("FILE_TYPE", fileType);
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.uploadPreworkCourse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initPreworkCourseQuery")
    public @ResponseBody String initPreworkCourseQuery(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.initPreworkCourseQuery", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryPreworkCourse")
    public @ResponseBody String queryPreworkCourse(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("COURSE_ID", request.getParameter("COURSE_ID"));
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.queryPreworkCourse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToViewFile")
    public String redirectToViewFile(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FILE_ID", request.getParameter("FILE_ID"));
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.queryCourseFile", parameter);
        JSONObject file = response.getJSONObject("FILE");
        String filePath = "http://api.idocv.com/view/url?url=" + file.getString("STORAGE_PATH");
        request.setAttribute("PATH", filePath);
        return "/biz/organization/course/view_courseware";
    }
}
