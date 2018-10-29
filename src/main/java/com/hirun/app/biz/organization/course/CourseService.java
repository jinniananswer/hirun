package com.hirun.app.biz.organization.course;

import com.hirun.app.dao.org.CourseDAO;
import com.hirun.app.dao.org.CourseFileDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-10-27 16:03
 **/
public class CourseService extends GenericService {

    public ServiceResponse initPreworkCourseUpload(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        CourseDAO dao = DAOFactory.createDAO(CourseDAO.class);
        RecordSet courseTypes = dao.queryCoursesByType("1");

        response.set("COURSE", ConvertTool.toJSONArray(courseTypes));
        return response;
    }

    public ServiceResponse uploadPreworkCourse(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        CourseFileDAO dao = DAOFactory.createDAO(CourseFileDAO.class);
        AppSession session = SessionManager.getSession();

        String newFileName = request.getString("NEW_FILE_NAME");
        String storagePath = "http%3A%2F%2Fwww.hi-run.net%3A8080%2Fdoc%2F"+newFileName;

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("COURSE_ID", request.getString("COURSE_ID"));
        parameter.put("STORAGE_PATH", storagePath);
        parameter.put("NAME", request.getString("FILE_NAME"));
        parameter.put("FILE_TYPE", request.getString("FILE_TYPE"));
        parameter.put("STATUS", "0");
        String userId = session.getSessionEntity().getUserId();
        parameter.put("CREATE_USER_ID", userId);
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());
        parameter.put("CREATE_DATE", session.getCreateTime());
        dao.insertAutoIncrement("ins_course_file", parameter);
        return response;
    }

    public ServiceResponse initPreworkCourseQuery(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        CourseDAO dao = DAOFactory.createDAO(CourseDAO.class);
        RecordSet courseTypes = dao.queryCoursesByType("1");

        response.set("COURSE", ConvertTool.toJSONArray(courseTypes));

        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        RecordSet courseList = fileDAO.queryCourseFilesByCourseType("1");
        response.set("COURSE_LIST", ConvertTool.toJSONArray(courseList));

        return response;
    }

    public ServiceResponse queryPreworkCourse(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        RecordSet courseList = fileDAO.queryCourseFilesByCourseId(request.getString("COURSE_ID"));
        response.set("COURSE_LIST", ConvertTool.toJSONArray(courseList));
        return response;
    }

    public ServiceResponse queryCourseFile(ServiceRequest request) throws Exception{
        String fileId = request.getString("FILE_ID");
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("FILE_ID", fileId);
        CourseFileDAO dao = DAOFactory.createDAO(CourseFileDAO.class);
        Record file = dao.queryByPk("ins_course_file", parameter);

        ServiceResponse response = new ServiceResponse();
        response.set("FILE", ConvertTool.toJSONObject(file));
        return response;
    }
}
