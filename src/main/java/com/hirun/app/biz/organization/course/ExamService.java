package com.hirun.app.biz.organization.course;

import com.hirun.app.dao.org.CourseFileDAO;
import com.hirun.pub.consts.FileConst;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
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
 * @create: 2018-11-12 21:02
 **/
public class ExamService extends GenericService {

    public ServiceResponse uploadExamGuide(ServiceRequest request) throws  Exception{
        ServiceResponse response = new ServiceResponse();
        CourseFileDAO dao = DAOFactory.createDAO(CourseFileDAO.class);
        AppSession session = SessionManager.getSession();

        String newFileName = request.getString("NEW_FILE_NAME");
        String storagePath = FileConst.OFFICE_FILE_ANALYSIS_PREFIX +newFileName;

        Map<String, String> parameter = new HashMap<String, String>();
        String courseId = "9";
        parameter.put("COURSE_ID", courseId);
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

    public ServiceResponse initExamGuideQuery(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        RecordSet courseList = fileDAO.queryCourseFilesByCourseType("0");
        response.set("EXAM_GUIDE_LIST", ConvertTool.toJSONArray(courseList));

        return response;
    }

    public ServiceResponse queryExamGuide(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        RecordSet courseList = fileDAO.queryCourseFilesByCourseId("9", request.getString("FILE_NAME"), "0");
        response.set("EXAM_GUIDE_LIST", ConvertTool.toJSONArray(courseList));

        return response;
    }
}
