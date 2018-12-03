package com.hirun.app.biz.organization.course;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.course.CourseBean;
import com.hirun.app.dao.org.CourseDAO;
import com.hirun.app.dao.org.CourseFileDAO;
import com.hirun.pub.consts.CourseConst;
import com.hirun.pub.consts.FileConst;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-10-27 16:03
 **/
public class CourseService extends GenericService {

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

    public ServiceResponse initCourseManage(ServiceRequest request) throws Exception {
        CourseBean bean = new CourseBean();
        JSONObject courseTree = bean.getCourseTree(false);

        ServiceResponse response = new ServiceResponse();
        response.set("COURSE_TREE", courseTree);

        return response;
    }

    public ServiceResponse createCourse(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("NAME", request.getString("COURSE_NAME"));
        parameter.put("PARENT_COURSE_ID", request.getString("PARENT_COURSE_ID"));
        parameter.put("TYPE", "1");
        parameter.put("NEED_EXAM", "1");
        parameter.put("STATUS", "0");
        parameter.put("COURSE_DESC", request.getString("COURSE_DESC"));
        parameter.put("CREATE_USER_ID", userId);
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());
        parameter.put("CREATE_DATE", session.getCreateTime());

        CourseDAO dao = DAOFactory.createDAO(CourseDAO.class);
        long courseId = dao.insertAutoIncrement("ins_course", parameter);

        ServiceResponse response = new ServiceResponse();
        response.set("NEW_COURSE_ID", courseId);
        return response;
    }

    public ServiceResponse initChangeCourse(ServiceRequest request) throws Exception {
        String courseId = request.getString("COURSE_ID");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("COURSE_ID", courseId);

        CourseDAO dao = DAOFactory.createDAO(CourseDAO.class);
        Record course = dao.queryByPk("ins_course", parameter);

        ServiceResponse response = new ServiceResponse();

        if(course != null) {
            String parentCourseId = course.get("PARENT_COURSE_ID");
            if(StringUtils.equals("-1", parentCourseId)) {
                course.put("PARENT_COURSE_NAME", CourseConst.COURSE_ROOT);
            }
            else {
                parameter.put("COURSE_ID", parentCourseId);
                Record parentCourse = dao.queryByPk("ins_course", parameter);

                if(parentCourse != null) {
                    course.put("PARENT_COURSE_NAME", parentCourse.get("NAME"));
                }
            }
            response.set("COURSE", ConvertTool.toJSONObject(course));
        }

        return response;
    }

    public ServiceResponse changeCourse(ServiceRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        parameter.put("COURSE_ID", request.getString("CHANGE_COURSE_ID"));
        parameter.put("NAME", request.getString("CHANGE_COURSE_NAME"));
        parameter.put("COURSE_DESC", request.getString("CHANGE_COURSE_DESC"));
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());

        CourseDAO dao = DAOFactory.createDAO(CourseDAO.class);
        dao.save("ins_course", parameter);

        return new ServiceResponse();
    }

    public ServiceResponse deleteCourse(ServiceRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        CourseDAO dao = DAOFactory.createDAO(CourseDAO.class);
        RecordSet courses = dao.queryAllValidCourse();

        String courseId = request.getString("COURSE_ID");
        List<String> children = CourseBean.findChildren(courses, courseId);

        if(children == null) {
            children = new ArrayList<String>();
        }

        children.add(courseId);

        String courseIds = "";
        int size = children.size();
        int i=1;
        for(String child : children) {
            if(i != size) {
                courseIds += child + ",";
            }
            else {
                courseIds += child;
            }
            i++;
        }
        dao.deleteCoursesById(courseIds, userId, session.getCreateTime());
        return new ServiceResponse();
    }

    public ServiceResponse uploadCourse(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        CourseFileDAO dao = DAOFactory.createDAO(CourseFileDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();

        JSONArray files = request.getJSONArray("FILES");
        if(files == null || files.size() <= 0) {
            return new ServiceResponse();
        }

        int size = files.size();
        for(int i=0;i<size;i++){
            JSONObject file = files.getJSONObject(i);
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("COURSE_ID", file.getString("COURSE_ID"));
            String newFileName = file.getString("NEW_FILE_NAME");
            String storagePath = FileConst.STORAGE_PATH +newFileName;

            parameter.put("STORAGE_PATH", storagePath);
            parameter.put("NAME", file.getString("FILE_NAME"));
            parameter.put("FILE_TYPE", file.getString("FILE_TYPE"));
            parameter.put("STATUS", "0");
            parameter.put("CREATE_USER_ID", userId);
            parameter.put("UPDATE_USER_ID", userId);
            parameter.put("UPDATE_TIME", session.getCreateTime());
            parameter.put("CREATE_DATE", session.getCreateTime());
            parameters.add(parameter);
        }


        dao.insertBatch("ins_course_file", parameters);
        return new ServiceResponse();
    }

    public ServiceResponse initCourseDetail(ServiceRequest request) throws Exception {
        String courseId = request.getString("COURSE_ID");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("COURSE_ID", courseId);

        CourseDAO courseDAO = DAOFactory.createDAO(CourseDAO.class);
        Record course = courseDAO.queryByPk("ins_course", parameter);

        String parentCourseId = course.get("PARENT_COURSE_ID");
        if(StringUtils.equals("-1", parentCourseId)) {
            course.put("PARENT_COURSE_NAME", CourseConst.COURSE_ROOT);
        }
        else {
            parameter.clear();
            parameter.put("COURSE_ID", parentCourseId);
            Record parent = courseDAO.queryByPk("ins_course", parameter);
            if(parent != null) {
                course.put("PARENT_COURSE_NAME", parent.get("NAME"));
            }
        }

        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        RecordSet courseFiles = fileDAO.queryCourseFilesByCourseId(courseId, null);

        ServiceResponse response = new ServiceResponse();
        response.set("COURSE", ConvertTool.toJSONObject(course));
        response.set("FILES", ConvertTool.toJSONArray(courseFiles));
        return response;
    }

    public ServiceResponse initCourseFile(ServiceRequest request) throws Exception {
        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        RecordSet courseFiles = fileDAO.queryCourseFilesByCourseId(request.getString("COURSE_ID"), null);

        ServiceResponse response = new ServiceResponse();
        response.set("FILES", ConvertTool.toJSONArray(courseFiles));
        return response;
    }

    public ServiceResponse deleteCourseFile(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        fileDAO.deleteCourseFilesById(request.getString("DELETE_FILE_ID"), userId, session.getCreateTime());
        return response;
    }

    public ServiceResponse initCoursewareQuery(ServiceRequest request) throws Exception {
        CourseBean bean = new CourseBean();
        JSONObject courseTree = bean.getCourseTree(false);

        ServiceResponse response = new ServiceResponse();
        response.set("COURSEWARE", courseTree);

        return response;
    }

    public ServiceResponse queryCourseware(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        CourseFileDAO fileDAO = DAOFactory.createDAO(CourseFileDAO.class);
        RecordSet courseList = fileDAO.queryCourseFilesByCourseId(request.getString("COURSE_ID"), request.getString("NAME"));
        response.set("COURSEWARE", ConvertTool.toJSONArray(courseList));
        return response;
    }
}
