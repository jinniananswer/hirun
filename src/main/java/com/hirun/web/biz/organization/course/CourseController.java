package com.hirun.web.biz.organization.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-10-21 22:17
 **/
@Controller
public class CourseController extends RootController {

    @RequestMapping("/initCourseManage")
    public @ResponseBody String initCourseManage(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.initCourseManage", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/createCourse")
    public @ResponseBody String createCourse(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.createCourse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initChangeCourse")
    public @ResponseBody String initChangeCourse(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.initChangeCourse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/changeCourse")
    public @ResponseBody String changeCourse(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.changeCourse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/deleteCourse")
    public @ResponseBody String deleteCourse(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.deleteCourse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/uploadCourse")
    public @ResponseBody String uploadCourse(HttpServletRequest request, @RequestParam("COURSE_FILE") CommonsMultipartFile[] files) throws Exception {
        String path=request.getSession().getServletContext().getRealPath("doc/");
        String now = TimeTool.now(TimeTool.TIME_PATTERN_MILLISECOND);

        if(ArrayTool.isEmpty(files)) {
            return new ServiceResponse().toJsonString();
        }

        JSONObject parameter = new JSONObject();
        int i = 0;

        JSONArray fileParams = new JSONArray();
        for(CommonsMultipartFile file : files) {
            JSONObject fileParam = new JSONObject();
            String fileName = file.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = now + i + fileType;
            File newFile=new File(path, newFileName);
            file.transferTo(newFile);

            fileParam.put("NEW_FILE_NAME", newFileName);
            fileParam.put("COURSE_ID", request.getParameter("UPLOAD_COURSE_ID"));
            fileParam.put("FILE_NAME", fileName);
            fileParam.put("FILE_TYPE", fileType);
            fileParams.add(fileParam);
            i++;
        }

        parameter.put("FILES", fileParams);
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.uploadCourse", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToCourseDetail")
    public String redirectToCourseDetail(HttpServletRequest request) throws Exception {
        return "/biz/organization/course/course_detail";
    }

    @RequestMapping("/initCourseDetail")
    public @ResponseBody String initCourseDetail(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.initCourseDetail", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToViewFile")
    public String redirectToViewFile(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FILE_ID", request.getParameter("FILE_ID"));
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.queryCourseFile", parameter);
        JSONObject file = response.getJSONObject("FILE");
        String filePath = file.getString("STORAGE_PATH");
        request.setAttribute("PATH", filePath);
        return "/biz/organization/course/view_courseware";
    }

    @RequestMapping("/redirectToViewPdf")
    public String redirectToViewPdf(HttpServletRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("FILE_ID", request.getParameter("FILE_ID"));
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.queryCourseFile", parameter);
        JSONObject file = response.getJSONObject("FILE");
        String filePath = file.getString("STORAGE_PATH");
        filePath = "/biz/organization/course/view_pdf.jsp?"+filePath;
        request.setAttribute("PATH", filePath);
        return "/biz/organization/course/view_pad_pdf";
    }

    @RequestMapping("/initCourseFile")
    public @ResponseBody String initCourseFile(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.initCourseFile", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/deleteCourseFile")
    public @ResponseBody String deleteCourseFile(HttpServletRequest request) throws Exception{
        String[] fileIds = request.getParameter("DELETE_FILE").split(",");
        if(fileIds == null || fileIds.length <= 0){
            throw new Exception("没有传要删除的文件ID");
        }
        String deleteFileIds = "";
        for(String fileId : fileIds) {
            deleteFileIds += fileId + ",";
        }
        deleteFileIds = deleteFileIds.substring(0, deleteFileIds.length() - 1);
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("DELETE_FILE_ID", deleteFileIds);
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.deleteCourseFile", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initCoursewareQuery")
    public @ResponseBody String initCoursewareQuery(HttpServletRequest request) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.initCoursewareQuery", new HashMap());
        return response.toJsonString();
    }

    @RequestMapping("/queryCourseware")
    public @ResponseBody String queryCourseware(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.course.CourseService.queryCourseware", parameter);
        return response.toJsonString();
    }
}
