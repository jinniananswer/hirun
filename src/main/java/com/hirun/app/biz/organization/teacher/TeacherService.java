package com.hirun.app.biz.organization.teacher;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.course.CourseBean;
import com.hirun.app.dao.org.TeacherDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import javax.xml.ws.Service;
import java.util.*;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-12-01 23:01
 **/
public class TeacherService extends GenericService {

    public ServiceResponse initTeacherManage(ServiceRequest request) throws Exception {
        JSONObject courseTree = CourseBean.getCourseTree(false);

        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);
        RecordSet teachers = dao.queryTeachers(null,null,null);
        teachers = this.filterTeachers(teachers);
        if(teachers != null && teachers.size() > 0) {
            int size = teachers.size();
            for(int i=0;i<size;i++) {
                Record teacher = teachers.get(i);
                teacher.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", teacher.get("JOB_ROLE")));
            }
        }

        ServiceResponse response = new ServiceResponse();
        response.set("COURSE", courseTree);
        response.set("TEACHER", ConvertTool.toJSONArray(teachers));
        return response;
    }

    public ServiceResponse initCreateTeacher(ServiceRequest request) throws Exception {
        JSONObject courseTree = CourseBean.getCourseTree(true);

        ServiceResponse response = new ServiceResponse();
        response.set("COURSE", courseTree);
        return response;
    }

    public ServiceResponse createTeacher(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);

        Map<String, String> parameter = ConvertTool.toMap(request.getBody().getData());
        String type = request.getString("TYPE");
        if(StringUtils.equals("0", type)) {
            parameter.put("ID", request.getString("EMPLOYEE_ID"));
            parameter.put("NAME", request.getString("EMPLOYEE_NAME"));

            Map<String, String> queryParameter = new HashMap<String, String>();
            queryParameter.put("ID", request.getString("EMPLOYEE_ID"));
            queryParameter.put("STATUS", "0");
            RecordSet teachers = dao.query("ins_teacher", queryParameter);
            if(ArrayTool.isNotEmpty(teachers)) {
                throw new Exception("该员工已经是讲师了，请重新选择");
            }
        }
        else {
            parameter.put("ID", "-1");
        }
        parameter.put("STATUS", "0");
        parameter.put("CREATE_USER_ID", userId);
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());
        parameter.put("CREATE_DATE", session.getCreateTime());
        long teacherId = dao.insertAutoIncrement("ins_teacher", parameter);

        String[] courseIds = request.getString("HOLD_COURSE_ID").split(",");
        List<Map<String, String>> rels = new ArrayList<Map<String, String>>();
        for(String courseId : courseIds) {
            Map<String, String> rel = new HashMap<String, String>();
            rel.put("TEACHER_ID", teacherId+"");
            rel.put("COURSE_ID", courseId);
            rel.put("STATUS", "0");
            rel.put("CREATE_USER_ID", userId);
            rel.put("UPDATE_USER_ID", userId);
            rel.put("UPDATE_TIME", session.getCreateTime());
            rel.put("CREATE_DATE", session.getCreateTime());
            rels.add(rel);
        }
        dao.insertBatch("ins_teacher_course_rel", rels);
        ServiceResponse response = new ServiceResponse();
        response.set("TEACHER_ID", teacherId+"");
        return response;
    }

    public ServiceResponse deleteTeacher(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TEACHER_ID", request.getString("TEACHER_ID"));
        parameter.put("STATUS", "1");
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());

        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);
        dao.save("ins_teacher", parameter);
        dao.deleteTeacherCourseRel(request.getString("TEACHER_ID"));
        return new ServiceResponse();
    }

    public ServiceResponse initTeacher(ServiceRequest request) throws Exception {
        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);
        RecordSet teachers = dao.queryTeachers(null, null, request.getString("TEACHER_ID"));
        teachers = this.filterTeachers(teachers);

        ServiceResponse response = new ServiceResponse();
        if(ArrayTool.isNotEmpty(teachers)) {
            Record teacher = teachers.get(0);
            teacher.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", teacher.get("JOB_ROLE")));
            response.set("TEACHER", ConvertTool.toJSONObject(teachers.get(0)));
        }
        return response;
    }

    public static RecordSet filterTeachers(RecordSet teachers) throws Exception {
        if(teachers == null || teachers.size() <= 0) {
            return teachers;
        }

        int size = teachers.size();
        Map<String, Record> tempTeacher = new HashMap<String, Record>();
        for(int i=0;i<size;i++) {
            Record teacher = teachers.get(i);
            String teacherId = teacher.get("TEACHER_ID");
            String courseName = teacher.get("COURSE_NAME");
            String courseId = teacher.get("COURSE_ID");
            if(!tempTeacher.containsKey(teacherId)) {
                tempTeacher.put(teacherId, teacher);
            }
            else {
                tempTeacher.get(teacherId).put("COURSE_NAME", tempTeacher.get(teacherId).get("COURSE_NAME") + "," + courseName);
                tempTeacher.get(teacherId).put("COURSE_ID", tempTeacher.get(teacherId).get("COURSE_ID") + "," + courseId);
            }
        }

        RecordSet rst = new RecordSet();
        Set<String> keys = tempTeacher.keySet();
        for(String key : keys){
            rst.add(tempTeacher.get(key));
        }
        return rst;
    }

    public ServiceResponse queryTeacher(ServiceRequest request) throws Exception {
        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);
        RecordSet teachers = dao.queryTeachers(request.getString("TEACHER_NAME"),request.getString("COURSE_ID"),null);
        teachers = this.filterTeachers(teachers);

        if(ArrayTool.isNotEmpty(teachers)) {
            int size = teachers.size();
            for(int i=0;i<size;i++) {
                Record teacher = teachers.get(i);
                teacher.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", teacher.get("JOB_ROLE")));
            }
        }

        ServiceResponse response = new ServiceResponse();
        response.set("TEACHER", ConvertTool.toJSONArray(teachers));

        return response;
    }

    public ServiceResponse initChangeTeacher(ServiceRequest request) throws Exception {
        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);
        RecordSet teachers = dao.queryTeachers(null, null, request.getString("TEACHER_ID"));
        teachers = this.filterTeachers(teachers);
        ServiceResponse response = new ServiceResponse();
        if(ArrayTool.isNotEmpty(teachers)) {
            response.set("TEACHER", ConvertTool.toJSONObject(teachers.get(0)));
        }
        return response;
    }

    public ServiceResponse changeTeacher(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);

        Map<String, String> parameter = ConvertTool.toMap(request.getBody().getData());

        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());
        dao.save("ins_teacher", parameter);

        String teacherId = request.getString("TEACHER_ID");
        dao.deleteTeacherCourseRel(teacherId);

        String[] courseIds = request.getString("HOLD_COURSE_ID").split(",");
        List<Map<String, String>> rels = new ArrayList<Map<String, String>>();
        for(String courseId : courseIds) {
            Map<String, String> rel = new HashMap<String, String>();
            rel.put("TEACHER_ID", teacherId+"");
            rel.put("COURSE_ID", courseId);
            rel.put("STATUS", "0");
            rel.put("CREATE_USER_ID", userId);
            rel.put("UPDATE_USER_ID", userId);
            rel.put("UPDATE_TIME", session.getCreateTime());
            rel.put("CREATE_DATE", session.getCreateTime());
            rels.add(rel);
        }
        dao.insertBatch("ins_teacher_course_rel", rels);
        ServiceResponse response = new ServiceResponse();
        response.set("TEACHER_ID", teacherId+"");
        return response;
    }
}
