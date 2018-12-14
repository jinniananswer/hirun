package com.hirun.app.biz.organization.train;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.course.CourseBean;
import com.hirun.app.biz.organization.teacher.TeacherService;
import com.hirun.app.dao.org.TeacherDAO;
import com.hirun.app.dao.org.TrainDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
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
 * @create: 2018-12-12 23:56
 **/
public class TrainService extends GenericService {

    public ServiceResponse initCreateTrain(ServiceRequest request) throws Exception {
        JSONObject singleCourseTree = CourseBean.getCourseTree(false);
        JSONObject courseTree = CourseBean.getCourseTree(true);

        TeacherDAO dao = DAOFactory.createDAO(TeacherDAO.class);
        RecordSet teachers = dao.queryTeachers(null,null,null);
        teachers = TeacherService.filterTeachers(teachers);

        ServiceResponse response = new ServiceResponse();
        response.set("COURSE", courseTree);
        response.set("SINGLE_COURSE", singleCourseTree);
        response.set("TEACHERS", ConvertTool.toJSONArray(teachers));
        return response;
    }

    public ServiceResponse createTrain(ServiceRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        /** 保存培训 **/
        parameter.put("NAME", request.getString("NAME"));
        parameter.put("TRAIN_DESC", request.getString("TRAIN_DESC"));
        parameter.put("CHARGE_EMPLOYEE_ID", request.getString("CHARGE_EMPLOYEE_ID"));
        parameter.put("STATUS", "0");
        parameter.put("START_DATE", request.getString("START_DATE"));
        parameter.put("END_DATE", request.getString("END_DATE"));
        parameter.put("CREATE_USER_ID", userId);
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("CREATE_DATE", session.getCreateTime());
        parameter.put("UPDATE_TIME", session.getCreateTime());

        long trainId = dao.insertAutoIncrement("ins_train", parameter);
        String courseIds = request.getString("COURSE_IDS");

        /** 保存培训与课程关系 **/
        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
        String[] courseIdArray = courseIds.split(",");
        for(String courseId : courseIdArray) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("TRAIN_ID", trainId+"");
            param.put("COURSE_ID", courseId);
            param.put("STATUS", "0");
            param.put("CREATE_USER_ID", userId);
            param.put("UPDATE_USER_ID", userId);
            param.put("CREATE_DATE", session.getCreateTime());
            param.put("UPDATE_TIME", session.getCreateTime());
            parameters.add(param);
        }

        dao.insertBatch("ins_train_course_rel", parameters);

        /** 保存课程表 **/
        parameters.clear();
        int scheduleNum = Integer.parseInt(request.getString("SCHEDULE_NUM"));
        for(int i=0;i<scheduleNum;i++) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("TRAIN_ID", trainId+"");
            String nature = request.getString("NATURE_"+i);
            param.put("NATURE", nature);

            if (StringUtils.equals("0", nature)) {
                param.put("COURSE_ID", request.getString("COURSE_ID_"+i));
                param.put("COURSE_NAME", request.getString("COURSE_NAME_"+i));
            }
            else {
                param.put("COURSE_ID", "-1");
                param.put("COURSE_NAME", request.getString("COURSE_CONTENT_"+i));
            }
            param.put("TEACHER_ID", request.getString("TEACHER_ID_"+i));
            param.put("STATUS", "0");
            param.put("START_DATE", request.getString("START_DATE_"+i));
            param.put("END_DATE", request.getString("END_DATE_"+i));
            param.put("CREATE_USER_ID", userId);
            param.put("UPDATE_USER_ID", userId);
            param.put("CREATE_DATE", session.getCreateTime());
            param.put("UPDATE_TIME", session.getCreateTime());
            parameters.add(param);
        }

        dao.insertBatch("ins_schedule", parameters);
        return new ServiceResponse();
    }
}
