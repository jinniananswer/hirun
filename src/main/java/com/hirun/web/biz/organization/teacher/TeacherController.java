package com.hirun.web.biz.organization.teacher;

import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.web.RootController;
import com.most.core.web.client.ServiceClient;
import org.apache.commons.lang3.StringUtils;
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
 * @create: 2018-12-01 22:59
 **/
@Controller
public class TeacherController extends RootController {

    @RequestMapping("/initTeacherManage")
    public @ResponseBody String initTeacherManage(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.initTeacherManage", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initCreateTeacher")
    public @ResponseBody String initCreateTeacher(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.initCreateTeacher", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/createTeacher")
    public @ResponseBody String createTeacher(HttpServletRequest request, @RequestParam("PIC_FILE") CommonsMultipartFile file) throws Exception {

        Map<String, String> parameter = new HashMap<String, String>();
        String fileName = file.getOriginalFilename();
        if(StringUtils.isNotBlank(fileName)) {
            String path=request.getSession().getServletContext().getRealPath("doc/teacher/");
            String now = TimeTool.now(TimeTool.TIME_PATTERN_MILLISECOND);
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = now + fileType;
            File newFile=new File(path, newFileName);
            file.transferTo(newFile);
            parameter.put("PIC", newFileName);
        }

        parameter.put("TYPE", request.getParameter("TYPE"));
        parameter.put("EMPLOYEE_ID", request.getParameter("EMPLOYEE_ID"));
        parameter.put("NAME", request.getParameter("NAME"));
        parameter.put("EMPLOYEE_NAME", request.getParameter("EMPLOYEE_NAME"));
        parameter.put("HOLD_COURSE_ID", request.getParameter("HOLD_COURSE_ID"));
        parameter.put("QQ_NO", request.getParameter("QQ_NO"));
        parameter.put("WECHAT_NO", request.getParameter("WECHAT_NO"));
        parameter.put("LEVEL", request.getParameter("LEVEL"));

        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.createTeacher", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/deleteTeacher")
    public @ResponseBody String deleteCourse(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.deleteTeacher", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/redirectToTeacherDetail")
    public String redirectToCourseDetail(HttpServletRequest request) throws Exception {
        return "/biz/organization/teacher/view_teacher";
    }

    @RequestMapping("/initTeacher")
    public @ResponseBody String initTeacher(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.initTeacher", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/queryTeacher")
    public @ResponseBody String queryTeacher(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.queryTeacher", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/initChangeTeacher")
    public @ResponseBody String initChangeTeacher(@RequestParam Map parameter) throws Exception{
        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.initChangeTeacher", parameter);
        return response.toJsonString();
    }

    @RequestMapping("/changeTeacher")
    public @ResponseBody String changeTeacher(HttpServletRequest request, @RequestParam("CHANGE_PIC_FILE") CommonsMultipartFile file) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        String fileName = file.getOriginalFilename();
        if(StringUtils.isNotBlank(fileName)) {
            String path=request.getSession().getServletContext().getRealPath("doc/teacher/");
            String now = TimeTool.now(TimeTool.TIME_PATTERN_MILLISECOND);
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = now + fileType;
            File newFile=new File(path, newFileName);
            file.transferTo(newFile);
            parameter.put("PIC", newFileName);
        }

        parameter.put("TEACHER_ID", request.getParameter("CHANGE_TEACHER_ID"));
        parameter.put("NAME", request.getParameter("CHANGE_NAME"));
        parameter.put("HOLD_COURSE_ID", request.getParameter("CHANGE_HOLD_COURSE_ID"));
        parameter.put("QQ_NO", request.getParameter("CHANGE_QQ_NO"));
        parameter.put("WECHAT_NO", request.getParameter("CHANGE_WECHAT_NO"));
        parameter.put("LEVEL", request.getParameter("CHANGE_LEVEL"));

        ServiceResponse response = ServiceClient.call("OrgCenter.teacher.TeacherService.changeTeacher", parameter);
        return response.toJsonString();
    }
}
