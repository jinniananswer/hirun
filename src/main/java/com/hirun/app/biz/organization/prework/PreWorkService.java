package com.hirun.app.biz.organization.prework;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.course.CourseBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.biz.organization.train.TrainService;
import com.hirun.app.dao.org.TrainDAO;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2019-01-27 00:38
 **/
public class PreWorkService extends GenericService {

    public ServiceResponse initQueryPreWorkEvaluation(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        RecordSet trains = dao.queryPreWorks(null, true);
        response.set("PREWORKS", ConvertTool.toJSONArray(trains));
        return response;
    }

    public ServiceResponse initViewPreWorkNotice(ServiceRequest request) throws Exception {
        String trainId = request.getString("TRAIN_ID");
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("TRAIN_ID", trainId);

        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);
        Record train = dao.queryByPk("ins_train", parameter);
        ServiceResponse response = new ServiceResponse();
        response.set("TRAIN", ConvertTool.toJSONObject(train));
        return response;
    }

    public ServiceResponse initChooseEmployeeSignPreWork(ServiceRequest request) throws Exception {
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
        if(StringUtils.equals("1", type)) {
            RecordSet mustSignEmployees = dao.queryNeedSignPreWorkEmployee(trainId, true, true);
            if(ArrayTool.isNotEmpty(mustSignEmployees)) {
                int size = mustSignEmployees.size();
                for(int i=0;i<size;i++) {
                    Record employee = mustSignEmployees.get(i);
                    employee.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", employee.get("JOB_ROLE")));
                }
            }
            response.set("MUST_SIGN_EMPLOYEE", ConvertTool.toJSONArray(mustSignEmployees));

            RecordSet needSignEmployees = dao.queryNeedSignPreWorkEmployee(trainId, false, false);
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

        RecordSet signEmployees = dao.queryPreworkEmployeeBySignEmployeeId(trainId, employeeId);

        filterPreworkSignList(signEmployees);

        JSONObject orgTree = OrgBean.getOrgTree();

        RecordSet jobRoles = StaticDataTool.getCodeTypeDatas("JOB_ROLE");

        response.set("JOB_ROLE", ConvertTool.toJSONArray(jobRoles));
        response.set("SIGN_EMPLOYEE", ConvertTool.toJSONArray(signEmployees));
        response.set("ORG_TREE", orgTree);

        return response;
    }

    public ServiceResponse addNewPreworkSign(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONArray newSignEmployees = request.getJSONArray("NEW_EMPLOYEE");
        List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();

        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        String signEmployeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        TrainDAO dao = DAOFactory.createDAO(TrainDAO.class);

        for (Object temp : newSignEmployees) {
            JSONObject employee = (JSONObject)temp;
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("TRAIN_ID", request.getString("TRAIN_ID"));
            parameter.put("EMPLOYEE_ID", employee.getString("EMPLOYEE_ID"));
            parameter.put("SIGN_EMPLOYEE_ID", signEmployeeId);
            parameter.put("STATUS", "0");
            parameter.put("CREATE_USER_ID", userId);
            parameter.put("UPDATE_USER_ID", userId);
            parameter.put("CREATE_DATE", session.getCreateTime());
            parameter.put("UPDATE_TIME", session.getCreateTime());
            long signId = dao.insertAutoIncrement("ins_train_sign", parameter);

            String examType = employee.getString("EXAM_TYPE");
            Map<String, String> item = new HashMap<String, String>();
            item.put("SIGN_ID", signId+"");
            item.put("TYPE", examType);
            item.put("STATUS", "0");
            item.put("CREATE_USER_ID", userId);
            item.put("UPDATE_USER_ID", userId);
            item.put("CREATE_DATE", session.getCreateTime());
            item.put("UPDATE_TIME", session.getCreateTime());
            if (StringUtils.equals("1", examType) || StringUtils.equals("3", examType)) {
                String examItemComm = employee.getString("EXAM_ITEM_COMM");
                String examItemPro = employee.getString("EXAM_ITEM_PRO");
                String itemDetail = "|";
                if (StringUtils.equals("true", examItemComm)) {
                    itemDetail += "0|";
                }

                if (StringUtils.equals("true", examItemPro)) {
                    itemDetail += "1|";
                }

                if (!StringUtils.equals("true", examItemComm) && !StringUtils.equals("true", examItemPro)) {
                    itemDetail += "-1|";
                }
                item.put("ITEM", itemDetail);
            }
            else {
                item.put("ITEM", "|-1|");
            }
            items.add(item);
        }
        dao.insertBatch("ins_train_sign_item", items);
        return response;
    }

    public ServiceResponse initQueryPreworkSignList(ServiceRequest request) throws Exception {
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
            signList = dao.queryPreworkSignList(trainId, orgs);
        }
        else {
            signList = dao.queryPreworkSignList(trainId, null);
        }

        filterPreworkSignList(signList);
        response.set("TOTAL_NUM", signList.size()+"");
        JSONObject sign = TrainService.filterSignList(signList);
        response.set("SIGN_LIST", sign);
        return response;
    }

    public ServiceResponse initViewPreworkCourseware(ServiceRequest request) throws Exception {
        CourseBean bean = new CourseBean();
        JSONObject courseTree = bean.getCourseTreeByExceptCourseId("12");

        ServiceResponse response = new ServiceResponse();
        response.set("COURSE_TREE", courseTree);

        return response;
    }

    public static void filterPreworkSignList(RecordSet signEmployees) throws Exception {
        if (ArrayTool.isNotEmpty(signEmployees)) {
            int size = signEmployees.size();
            for (int i=0;i<size;i++) {
                Record employee = signEmployees.get(i);
                String item = employee.get("ITEM");
                String itemType = employee.get("TYPE");
                if (StringUtils.equals("1", itemType) || StringUtils.equals("3", itemType)) {
                    String[] itemArray = item.split("|");
                    for (String key : itemArray) {
                        if (StringUtils.isBlank(key)) {
                            continue;
                        }
                        if (StringUtils.equals("-1", key)) {
                            employee.put("EXAM_ITEM_COMM", "false");
                            employee.put("EXAM_ITEM_PRO", "false");
                        }
                        else if(StringUtils.equals("0", key)) {
                            employee.put("EXAM_ITEM_COMM", "true");
                        }
                        else if(StringUtils.equals("1", key)) {
                            employee.put("EXAM_ITEM_PRO", "true");
                        }
                        else {
                            employee.put("EXAM_ITEM_COMM", "false");
                            employee.put("EXAM_ITEM_PRO", "false");
                        }
                    }
                }

            }
        }
    }
}
