package com.hirun.app.biz.organization.train;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.course.CourseBean;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.biz.organization.teacher.TeacherService;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.org.TeacherDAO;
import com.hirun.app.dao.org.TrainDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
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
import org.apache.kafka.common.record.Records;

import javax.xml.ws.Service;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2018-12-12 23:56
 **/
public class TrainService extends GenericService {

    public ServiceResponse initCreateTrain(ServiceRequest request) throws Exception {
        JSONObject singleCourseTree = CourseBean.getCourseTree(false, null);
        JSONObject courseTree = CourseBean.getCourseTree(true, null);

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
        parameter.put("TYPE", request.getString("TYPE"));
        parameter.put("TRAIN_DESC", request.getString("TRAIN_DESC"));
        parameter.put("CHARGE_EMPLOYEE_ID", request.getString("CHARGE_EMPLOYEE_ID"));
        parameter.put("STATUS", "0");
        parameter.put("SIGN_STATUS", "0");
        parameter.put("START_DATE", request.getString("START_DATE"));
        parameter.put("END_DATE", request.getString("END_DATE"));
        parameter.put("TRAIN_ADDRESS", request.getString("TRAIN_ADDRESS"));
        parameter.put("HOTEL_ADDRESS", request.getString("HOTEL_ADDRESS"));
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
            if(StringUtils.isEmpty(nature)){
                continue;
            }
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

    public ServiceResponse initManagerTrains(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        RecordSet trains = dao.queryTrains(null, false);
        trains = filterTrains(trains);
        response.set("TRAINS", ConvertTool.toJSONArray(trains));
        return response;
    }

    public ServiceResponse initQueryTrains(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        RecordSet trains = dao.queryTrains(null, false);
        trains = filterTrains(trains);
        response.set("TRAINS", ConvertTool.toJSONArray(trains));
        return response;
    }

    public ServiceResponse initTrainDetail(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        RecordSet trains = dao.queryTrains(request.getString("TRAIN_ID"), false);
        trains = filterTrains(trains);
        response.set("TRAINS", ConvertTool.toJSONArray(trains));

        RecordSet schedules = dao.querySchedules(request.getString("TRAIN_ID"));
        JSONObject schedule = filterSchedules(schedules);
        response.set("SCHEDULES", schedule);
        return response;
    }

    public ServiceResponse deleteTrain(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        Map<String, String> parameter = new HashMap<String, String>();
        String trainId = request.getString("TRAIN_ID");
        parameter.put("TRAIN_ID", trainId);
        parameter.put("STATUS", "1");
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());
        dao.save("ins_train", parameter);

        dao.deleteTrainCourseRelByTrainId(trainId);
        dao.deleteScheduleByTrainId(trainId);

        return response;
    }

    public ServiceResponse initChangeTrain(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        RecordSet trains = dao.queryTrains(request.getString("TRAIN_ID"), true);
        trains = filterTrains(trains);

        if(ArrayTool.isEmpty(trains)) {
            throw new Exception("该培训已过时间，不能编辑!");
        }

        Record train = trains.get(0);
        response.set("TRAIN", ConvertTool.toJSONObject(train));
        String selectedCourseIds = train.get("COURSE_ID");
        JSONObject courseTree = CourseBean.getCourseTree(true, selectedCourseIds);
        response.set("COURSE", courseTree);

        JSONObject singleCourseTree = CourseBean.getCourseTree(false, null);
        response.set("SINGLE_COURSE", singleCourseTree);

        RecordSet schedules = dao.querySchedules(request.getString("TRAIN_ID"));
        response.set("SCHEDULE", ConvertTool.toJSONArray(schedules));

        TeacherDAO teacherDAO = DAOFactory.createDAO(TeacherDAO.class);
        RecordSet teachers = teacherDAO.queryTeachers(null,null,null);
        teachers = TeacherService.filterTeachers(teachers);
        response.set("TEACHERS", ConvertTool.toJSONArray(teachers));

        return response;
    }

    public ServiceResponse changeTrain(ServiceRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        /** 保存培训 **/
        String trainId = request.getString("TRAIN_ID");
        parameter.put("TRAIN_ID", trainId);
        parameter.put("NAME", request.getString("NAME"));
        parameter.put("TYPE", request.getString("TYPE"));
        parameter.put("TRAIN_DESC", request.getString("TRAIN_DESC"));
        parameter.put("CHARGE_EMPLOYEE_ID", request.getString("CHARGE_EMPLOYEE_ID"));
        parameter.put("STATUS", "0");
        parameter.put("START_DATE", request.getString("START_DATE"));
        parameter.put("END_DATE", request.getString("END_DATE"));
        parameter.put("TRAIN_ADDRESS", request.getString("TRAIN_ADDRESS"));
        parameter.put("HOTEL_ADDRESS", request.getString("HOTEL_ADDRESS"));
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("UPDATE_TIME", session.getCreateTime());

        dao.save("ins_train", parameter);
        String courseIds = request.getString("COURSE_IDS");

        /** 保存培训与课程关系 **/

        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
        String[] courseIdArray = courseIds.split(",");
        dao.deleteTrainCourseRelByTrainId(trainId);
        for(String courseId : courseIdArray) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("TRAIN_ID", trainId);
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
        dao.deleteScheduleByTrainId(trainId);
        for(int i=0;i<scheduleNum;i++) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("TRAIN_ID", trainId);
            String nature = request.getString("NATURE_"+i);
            if(StringUtils.isEmpty(nature)){
                continue;
            }
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

    public ServiceResponse signTrain(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", request.getString("TRAIN_ID"));
        parameter.put("EMPLOYEE_ID", employeeId);
        parameter.put("STATUS", "0");
        parameter.put("CREATE_USER_ID", userId);
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("CREATE_DATE", session.getCreateTime());
        parameter.put("UPDATE_TIME", session.getCreateTime());

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        dao.insertAutoIncrement("ins_train_sign", parameter);
        return new ServiceResponse();
    }

    public ServiceResponse initQuerySignList(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeOrgId = OrgBean.getOrgId(session.getSessionEntity());
        OrgEntity org = OrgBean.getAssignTypeOrg(employeeOrgId, "3");

        boolean hasAllCity = Permission.hasAllCity();
        if(org == null && !hasAllCity) {
            return response;
        }

        String orgs = null;
        if(org != null) {
             orgs = OrgBean.getOrgLine(org.getOrgId());
        }

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);

        String trainId = request.getString("TRAIN_ID");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);
        Record train = dao.queryByPk("ins_train", parameter);

        response.set("TRAIN", ConvertTool.toJSONObject(train));
        RecordSet signList = null;
        if(!hasAllCity) {
            signList = dao.querySignList(trainId, orgs);
        }
        else {
            signList = dao.querySignList(trainId, null);
        }
        response.set("TOTAL_NUM", signList.size()+"");
        JSONObject sign = filterSignList(signList);
        response.set("SIGN_LIST", sign);
        JSONObject orgTree = OrgBean.getOrgTree();
        response.set("ORG_TREE", orgTree);
        response.set("HAS_END_SIGN_OPER", Permission.hasEndSignOper()+"");
        return response;
    }

    public ServiceResponse deleteSignedEmployee(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String time = session.getCreateTime();
        String status = "1";
        String selectedEmployeeIds = request.getString("SELECTED_EMPLOYEE_ID");
        String trainId = request.getString("TRAIN_ID");

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        dao.updateSignStatus(selectedEmployeeIds, trainId, status, userId, time);
        return response;
    }

    public ServiceResponse endSign(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String time = session.getCreateTime();

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", request.getString("TRAIN_ID"));
        parameter.put("SIGN_STATUS", "1");
        parameter.put("UPDATE_TIME", time);
        parameter.put("UPDATE_USER_ID", userId);

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        dao.save("ins_train", parameter);
        return response;
    }

    public ServiceResponse initChooseEmployeeSignTrain(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        String trainId = request.getString("TRAIN_ID");

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        Record train = dao.queryByPk("ins_train", parameter);
        response.set("TRAIN", ConvertTool.toJSONObject(train));

        String type = train.get("TYPE");
        if(StringUtils.equals("2", type)) {
            List<OrgEntity> allOrgs = OrgBean.getAllOrgs();
            String orgId = OrgBean.getOrgId(session.getSessionEntity());
            OrgEntity rootOrg = OrgBean.findEmployeeRoot(orgId, allOrgs);
            if(StringUtils.equals("122", rootOrg.getParentOrgId())) {
                orgId = "122";
            }
            else {
                orgId = rootOrg.getOrgId();
            }

            orgId = OrgBean.getOrgLine(orgId, allOrgs);

            RecordSet mustSignEmployees = dao.queryNeedSignPreTrainEmployee(trainId, true, orgId);
            fillAllOrgName(mustSignEmployees);
            if(ArrayTool.isNotEmpty(mustSignEmployees)) {
                int size = mustSignEmployees.size();
                for(int i=0;i<size;i++) {
                    Record employee = mustSignEmployees.get(i);
                    employee.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", employee.get("JOB_ROLE")));
                }
            }
            response.set("MUST_SIGN_EMPLOYEE", ConvertTool.toJSONArray(mustSignEmployees));

            RecordSet needSignEmployees = dao.queryNeedSignPreTrainEmployee(trainId, false, orgId);
            fillAllOrgName(needSignEmployees);
            RecordSet temp = new RecordSet();
            if(ArrayTool.isNotEmpty(needSignEmployees)) {

                int needSize = needSignEmployees.size();
                int mustSize = mustSignEmployees.size();
                for(int i=0;i<needSize;i++) {
                    boolean isFind = false;
                    Record needEmployee = needSignEmployees.get(i);
                    for(int j=0;j<mustSize;j++) {
                        Record mustEmployee = mustSignEmployees.get(j);
                        if(StringUtils.equals(needEmployee.get("EMPLOYEE_ID"), mustEmployee.get("EMPLOYEE_ID"))) {
                            isFind = true;
                            break;
                        }
                    }

                    if(!isFind) {
                        needEmployee.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", needEmployee.get("JOB_ROLE")));
                        temp.add(needEmployee);
                    }
                }
                response.set("NEED_SIGN_EMPLOYEE", ConvertTool.toJSONArray(temp));

            }

        }

        RecordSet signEmployees = dao.queryEmployeeBySignEmployeeId(trainId, employeeId);
        JSONObject orgTree = OrgBean.getOrgTree();

        RecordSet jobRoles = StaticDataTool.getCodeTypeDatas("JOB_ROLE");

        response.set("JOB_ROLE", ConvertTool.toJSONArray(jobRoles));
        response.set("SIGN_EMPLOYEE", ConvertTool.toJSONArray(signEmployees));
        response.set("ORG_TREE", orgTree);

        return response;
    }

    public ServiceResponse queryWantSignEmployee(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");

        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        String orgId = request.getString("ORG_ID");

        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();
        if(StringUtils.isBlank(orgId)) {
            orgId = OrgBean.getOrgId(session.getSessionEntity());
            OrgEntity rootOrg = OrgBean.findEmployeeRoot(orgId, allOrgs);
            if(StringUtils.equals("122", rootOrg.getParentOrgId())) {
                orgId = "122";
            }
            else {
                orgId = rootOrg.getOrgId();
            }
        }

        orgId = OrgBean.getOrgLine(orgId, allOrgs);
        RecordSet employees = dao.queryEmployees(request.getString("NAME"), null, null, null, null, orgId, request.getString("JOB_ROLE"), null);
        fillAllOrgName(employees);
        if(employees == null || employees.size() <= 0)
            return response;

        int size = employees.size();
        for(int i=0;i<size;i++){
            Record employee = employees.get(i);
            employee.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", employee.get("JOB_ROLE")));
        }
        response.set("EMPLOYEE", ConvertTool.toJSONArray(employees));
        return response;
    }

    public ServiceResponse signNewEmployee(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String newEmployeeIds = request.getString("NEW_EMPLOYEE_ID");
        String[] addEmployeeIdArray = newEmployeeIds.split(",");

        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String signEmployeeId = session.getSessionEntity().get("EMPLOYEE_ID");

        JSONArray employeeItems = request.getJSONArray("EMPLOYEE_ITEMS");

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);

        for(String employeeId : addEmployeeIdArray) {
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("TRAIN_ID", request.getString("TRAIN_ID"));
            parameter.put("EMPLOYEE_ID", employeeId);
            parameter.put("STATUS", "0");

            RecordSet sames = dao.query("ins_train_sign", parameter);

            if(ArrayTool.isNotEmpty(sames)) {
                Record same = sames.get(0);
                String signedEmployeeId = same.get("SIGN_EMPLOYEE_ID");

                EmployeeEntity employee = EmployeeBean.getEmployeeByEmployeeId(employeeId);
                EmployeeEntity signedEmployee = EmployeeBean.getEmployeeByEmployeeId(signedEmployeeId);
                response.setError("HIRUN_TRAIN_000001", "员工"+employee.getName()+"已经由"+signedEmployee.getName()+"报过名了，请剔除后重新提交!");
                return response;
            }
            parameter.put("SIGN_EMPLOYEE_ID", signEmployeeId);

            if(ArrayTool.isNotEmpty(employeeItems)) {
                for(Object obj : employeeItems) {
                    JSONObject item = (JSONObject)obj;
                    String gradeEmployeeId = item.getString("EMPLOYEE_ID");
                    if(StringUtils.equals(gradeEmployeeId, employeeId)) {
                        parameter.put("BUSI_GRADE", item.getString("BUSI_GRADE"));
                        break;
                    }
                }
            }
            parameter.put("STATUS", "0");
            parameter.put("CREATE_USER_ID", userId);
            parameter.put("UPDATE_USER_ID", userId);
            parameter.put("CREATE_DATE", session.getCreateTime());
            parameter.put("UPDATE_TIME", session.getCreateTime());
            parameters.add(parameter);
        }
        dao.insertBatch("ins_train_sign", parameters);
        return response;
    }

    public ServiceResponse createPreworkEvaluation(ServiceRequest request) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();

        /** 保存培训 **/
        parameter.put("NAME", request.getString("NAME"));
        parameter.put("TYPE", "1");
        parameter.put("TRAIN_DESC", request.getString("TRAIN_DESC"));
        parameter.put("CHARGE_EMPLOYEE_ID", "-1");
        parameter.put("STATUS", "0");
        parameter.put("SIGN_STATUS", "0");
        parameter.put("START_DATE", request.getString("START_DATE"));
        parameter.put("END_DATE", request.getString("END_DATE"));
        parameter.put("SIGN_END_DATE", request.getString("SIGN_END_DATE"));
        parameter.put("TRAIN_ADDRESS", request.getString("TRAIN_ADDRESS"));
        parameter.put("CREATE_USER_ID", userId);
        parameter.put("UPDATE_USER_ID", userId);
        parameter.put("CREATE_DATE", session.getCreateTime());
        parameter.put("UPDATE_TIME", session.getCreateTime());

        dao.insertAutoIncrement("ins_train", parameter);

        return new ServiceResponse();
    }

    public ServiceResponse initViewTrainNotice(ServiceRequest request) throws Exception {
        String trainId = request.getString("TRAIN_ID");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        Record train = dao.queryByPk("ins_train", parameter);

        RecordSet schedules = dao.querySchedules(trainId);

        ServiceResponse response = new ServiceResponse();
        response.set("TRAIN", ConvertTool.toJSONObject(train));

        if (ArrayTool.isNotEmpty(schedules)) {
            response.set("SCHEDULE", ConvertTool.toJSONObject(schedules.get(0)));
        }

        String chargeEmployeeId = train.get("CHARGE_EMPLOYEE_ID");
        if (StringUtils.isNotBlank(chargeEmployeeId)) {
            EmployeeEntity employee = EmployeeBean.getEmployeeByEmployeeId(chargeEmployeeId);
            response.set("EMPLOYEE_NAME", employee.getName());

            String userId = employee.getUserId();
            UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
            parameter.clear();
            parameter.put("USER_ID", userId);
            Record user = userDAO.queryByPk("ins_user", parameter);
            response.set("MOBILE_NO", user.get("USERNAME"));
        }

        insertNoticeView(trainId);
        return response;
    }

    public ServiceResponse initQueryMyTrain(ServiceRequest request) throws Exception {
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");

        EmployeeEntity employee = EmployeeBean.getEmployeeByEmployeeId(employeeId);
        String now = session.getCreateTime().substring(0, 10);

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        RecordSet myTrains = dao.queryMyTrain(employeeId);

        ServiceResponse response = new ServiceResponse();
        response.set("EMPLOYEE", employee.toJson());

        if(ArrayTool.isEmpty(myTrains)) {
            return response;
        }

        int size = myTrains.size();
        JSONArray current = new JSONArray();
        JSONArray history = new JSONArray();

        for(int i=0;i<size;i++) {
            Record myTrain = myTrains.get(i);
            String endDate = myTrain.get("END_DATE");

            String trainId = myTrain.get("TRAIN_ID");

            RecordSet courses = dao.queryCourseByTrainId(trainId);

            if (ArrayTool.isEmpty(courses)) {
                myTrain.put("COURSE_NAME", "暂无");
            }
            else {
                int length = courses.size();
                String courseName = "";
                for (int j=0;j<length;j++) {
                    Record course = courses.get(j);
                    courseName += course.get("NAME") + "<br/>";
                }
                myTrain.put("COURSE_NAME", courseName);
            }
            RecordSet scores = dao.queryMyTrainScore(employeeId, trainId);
            JSONObject temp = ConvertTool.toJSONObject(myTrain);
            temp.put("SCORES", ConvertTool.toJSONArray(scores));
            if(endDate.compareTo(now) >= 0) {
                current.add(temp);
            }
            else {
                history.add(temp);
            }
        }

        response.set("CURRENT", current);
        response.set("HISTORY", history);
        return response;
    }

    public static RecordSet filterTrains(RecordSet trains) throws Exception {
        if(ArrayTool.isEmpty(trains)) {
            return trains;
        }

        int size = trains.size();
        Map<String, Record> tempTrain = new HashMap<String, Record>();
        RecordSet rst = new RecordSet();
        for(int i=0;i<size;i++) {
            Record train = trains.get(i);
            String trainId = train.get("TRAIN_ID");
            String courseName = train.get("COURSE_NAME");
            String courseId = train.get("COURSE_ID");
            if(!tempTrain.containsKey(trainId)) {
                tempTrain.put(trainId, train);
                rst.add(train);
            }
            else {
                tempTrain.get(trainId).put("COURSE_NAME", tempTrain.get(trainId).get("COURSE_NAME") + "," + courseName);
                tempTrain.get(trainId).put("COURSE_ID", tempTrain.get(trainId).get("COURSE_ID") + "," + courseId);
            }
        }
        return rst;
    }

    public static JSONObject filterSchedules(RecordSet schedules) throws Exception {
        if(ArrayTool.isEmpty(schedules)) {
            return null;
        }

        int size = schedules.size();
        JSONObject rst = new JSONObject(true);

        for(int i=0;i<size;i++) {
            Record schedule = schedules.get(i);
            String startDate = schedule.get("START_DATE").substring(0,10);
            JSONArray tempSchedules = null;
            if(rst.containsKey(startDate)) {
                tempSchedules = rst.getJSONArray(startDate);
            }
            else {
                tempSchedules = new JSONArray();
                rst.put(startDate, tempSchedules);
            }
            tempSchedules.add(ConvertTool.toJSONObject(schedule));
        }
        return rst;
    }

    public static JSONObject filterSignList(RecordSet signList) throws Exception {
        JSONObject rst = new JSONObject();
        if(ArrayTool.isEmpty(signList)) {
            return rst;
        }

        int size = signList.size();

        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        //按最大组织合并
        for (int i=0;i<size;i++) {
            Record sign = signList.get(i);
            String orgId = sign.get("ORG_ID");
            List<OrgEntity> bloodLine = OrgBean.bloodOrgDesc(orgId, allOrgs);
            String rootOrgName = "鸿扬";

            if (ArrayTool.isNotEmpty(bloodLine)) {
                OrgEntity org = bloodLine.get(0);
                rootOrgName = org.getName();

                String allOrgName = "";
                for (OrgEntity blood : bloodLine) {
                    allOrgName += blood.getName() + "-";
                }

                sign.put("ALL_ORG_NAME", allOrgName.substring(0, allOrgName.length() - 1));
            }

            JSONArray datas = null;

            if (rst.containsKey(rootOrgName)) {
                datas = rst.getJSONArray(rootOrgName);
            }
            else {
                datas = new JSONArray();
                rst.put(rootOrgName, datas);
            }
            sign.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", sign.get("JOB_ROLE")));
            sign.put("EDUCATION", StaticDataTool.getCodeName("EDUCATION_LEVEL", sign.get("EDUCATION_LEVEL")));
            datas.add(ConvertTool.toJSONObject(sign));
        }
        return rst;
    }

    public static void fillAllOrgName(RecordSet employees) throws Exception {
        if(ArrayTool.isEmpty(employees)) {
            return;
        }

        int size = employees.size();

        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        //按最大组织合并
        for (int i=0;i<size;i++) {
            Record employee = employees.get(i);
            String orgId = employee.get("ORG_ID");
            List<OrgEntity> bloodLine = OrgBean.bloodOrgDesc(orgId, allOrgs);
            String rootOrgName = "鸿扬";

            if (ArrayTool.isNotEmpty(bloodLine)) {
                OrgEntity org = bloodLine.get(0);
                rootOrgName = org.getName();

                String allOrgName = "";
                for (OrgEntity blood : bloodLine) {
                    allOrgName += blood.getName() + "-";
                }

                employee.put("ALL_ORG_NAME", allOrgName.substring(0, allOrgName.length() - 1));
            }
        }
        return;
    }

    public static void insertNoticeView(String trainId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        String userId = session.getSessionEntity().getUserId();
        parameter.clear();
        parameter.put("TRAIN_ID", trainId);
        parameter.put("EMPLOYEE_ID", employeeId);

        RecordSet viewers = dao.query("ins_train_notice_view", parameter);
        if(ArrayTool.isEmpty(viewers)) {
            parameter.clear();
            parameter.put("TRAIN_ID", trainId);
            parameter.put("EMPLOYEE_ID", employeeId);
            parameter.put("CREATE_USER_ID", userId);
            parameter.put("CREATE_DATE", session.getCreateTime());
            parameter.put("UPDATE_USER_ID", userId);
            parameter.put("UPDATE_TIME", session.getCreateTime());
            dao.insert("ins_train_notice_view", parameter);
        }
    }
}
