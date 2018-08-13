package com.hirun.app.biz.employee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.bean.plan.PlanBean;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.employee.EmployeeJobRoleDAO;
import com.hirun.app.dao.func.FuncDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.xml.ws.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-06-12.
 */
public class EmployeeService extends GenericService {

    public ServiceResponse getAllSubordinatesCounselors(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String employeeIds = requestData.getString("EMPLOYEE_IDS");
        String columns = requestData.getString("COLUMNS");

        List<EmployeeEntity> list = EmployeeBean.getAllSubordinatesCounselors(employeeIds);
        if(StringUtils.isBlank(columns)) {
            response.set("EMPLOYEE_LIST", ConvertTool.toJSONArray(list));
        } else {
            response.set("EMPLOYEE_LIST", ConvertTool.toJSONArray(list, columns.split(",")));
        }

        return response;
    }


    public ServiceResponse queryContacts(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        RecordSet recordSet = dao.queryContacts(request.getString("SEARCH_TEXT"));
        if(recordSet == null || recordSet.size() <= 0)
            return response;

        int size = recordSet.size();
        for(int i=0;i<size;i++){
            Record record = recordSet.get(i);
            if(StringUtils.equals("69", record.get("USER_ID")) || StringUtils.equals("72", record.get("USER_ID")))
                record.put("CONTACT_NO","***********");
            record.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", record.get("JOB_ROLE")));
        }
        response.set("DATAS", ConvertTool.toJSONArray(recordSet));
        return response;
    }

    public ServiceResponse entryHoliday(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String holidayStartDate = requestData.getString("HOLIDAY_START_DATE");
        String holidayEndDate = requestData.getString("HOLIDAY_END_DATE");
        String planExecutorId = requestData.getString("PLAN_EXECUTOR_ID");

        PlanBean.addHolidayPlansByStartAndEnd(holidayStartDate, holidayEndDate, planExecutorId);

        return response;
    }

    public ServiceResponse initCreateEmployee(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String today = TimeTool.today();
        response.set("TODAY", today);
        JSONObject orgTree = OrgBean.getOrgTree();
        response.set("ORG_TREE", orgTree);
        RecordSet jobRoles = StaticDataTool.getCodeTypeDatas("JOB_ROLE");
        RecordSet jobs = new RecordSet();
        if(jobRoles != null && jobRoles.size() > 0){
            for(int i=0;i<jobRoles.size();i++){
                Record jobRole = jobRoles.get(i);
                if(!StringUtils.equals("0", jobRole.get("CODE_VALUE"))){
                    jobs.add(jobRole);
                }
            }
        }
        response.set("JOB_ROLES", ConvertTool.toJSONArray(jobs));

        AppSession session = SessionManager.getSession();
        SessionEntity sessionEntity = session.getSessionEntity();
        String orgId = OrgBean.getOrgId(sessionEntity);
        OrgEntity org = null;
        if (StringUtils.isNotBlank(orgId)) {
            OrgDAO dao = new OrgDAO("ins");
            org = dao.queryOrgById(orgId);
        }
        boolean needAllCity = Permission.hasAllCity();

        JSONArray citys = null;
        if (needAllCity) {
            citys = ConvertTool.toJSONArray(StaticDataTool.getCodeTypeDatas("BIZ_CITY"));
        } else {
            citys = new JSONArray();
            JSONObject city = new JSONObject();
            city.put("CODE_VALUE", org.getCity());
            String cityName = StaticDataTool.getCodeName("BIZ_CITY", org.getCity());
            city.put("CODE_NAME", cityName);
            citys.add(city);
            response.set("DEFAULT_CITY_ID", org.getCity());
            response.set("DEFAULT_CITY_NAME", cityName);

        }
        response.set("CITYS", citys);

        return response;
    }

    public ServiceResponse initParentEmployee(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String shopId= request.getString("SHOP");
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        String jobRole = request.getString("JOB_ROLE");
        List<EmployeeEntity> parentEmployees = null;
        if("42".equals(jobRole)) {
            parentEmployees = dao.queryEmployeeByParentOrgJobRole(shopId, "58");
            if(ArrayTool.isEmpty(parentEmployees))
                parentEmployees = dao.queryEmployeeByParentOrgJobRoleAndMarket(shopId, "103");
        }
        else
            parentEmployees = dao.queryEmployeeByParentOrgJobRoleAndMarket(shopId, "103");

        response.set("PARENT_EMPLOYEES", ConvertTool.toJSONArray(parentEmployees));
        return response;
    }

    public ServiceResponse createEmployee(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();


        Map<String, String> user = new HashMap<String, String>();
        user.put("USERNAME", request.getString("MOBILE_NO"));
        user.put("PASSWORD", "711be3iidqb6lrsln0avp0v21u");
        user.put("MOBILE_NO", request.getString("MOBILE_NO"));
        user.put("STATUS", "0");
        user.put("CREATE_DATE", session.getCreateTime());
        user.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        user.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        user.put("UPDATE_TIME", session.getCreateTime());

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);

        UserEntity userEntity = dao.queryUserByUsername(request.getString("MOBILE_NO"));
        if(userEntity != null){
            response.setError("HIRUN_CREATEEMPLOYEE_000001","该手机号码的员工已经存在");
            return response;
        }

        long userId = dao.insertAutoIncrement("ins_user", user);

        Map<String, String> employee = new HashMap<String, String>();
        employee.put("USER_ID", userId+"");
        employee.put("NAME", request.getString("NAME"));
        employee.put("SEX", request.getString("SEX"));
        employee.put("IDENTITY_NO", request.getString("IDENTITY_NO"));
        employee.put("HOME_ADDRESS", request.getString("HOME_ADDRESS"));
        employee.put("NATIVE_PROV", "0");
        employee.put("NATIVE_CITY", "0");
        employee.put("NATIVE_REGION", "0");
        employee.put("IN_DATE", request.getString("IN_DATE"));
        employee.put("WORK_NATURE", "1");
        employee.put("WORK_PLACE", request.getString("CITY"));
        employee.put("STATUS", "0");
        employee.put("CREATE_DATE", session.getCreateTime());
        employee.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        employee.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        employee.put("UPDATE_TIME", session.getCreateTime());
        long employeeId = dao.insertAutoIncrement("ins_employee", employee);

        Map<String, String> job = new HashMap<String, String>();
        job.put("EMPLOYEE_ID", employeeId+"");
        job.put("JOB_ROLE", request.getString("JOB_ROLE"));
        job.put("JOB_ROLE_NATURE", "1");
        job.put("ORG_ID", request.getString("ORG_ID"));
        job.put("PARENT_EMPLOYEE_ID", request.getString("PARENT_EMPLOYEE_ID"));
        job.put("START_DATE", session.getCreateTime());
        job.put("END_DATE", "3000-12-31 23:59:59");
        job.put("CREATE_DATE", session.getCreateTime());
        job.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        job.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        job.put("UPDATE_TIME", session.getCreateTime());
        dao.insertAutoIncrement("ins_employee_job_role", job);

        FuncDAO funcDAO = DAOFactory.createDAO(FuncDAO.class);
        RecordSet jobFuncs = funcDAO.queryJobFunc(request.getString("JOB_ROLE"));
        List<Map<String, String>> userFuncs = new ArrayList<Map<String, String>>();
        if(jobFuncs != null && jobFuncs.size() > 0){
            int jobFuncSize = jobFuncs.size();
            for(int i=0;i<jobFuncSize;i++){
                Record jobFunc = jobFuncs.get(i);
                Map<String, String> userFunc = new HashMap<String, String>();
                userFunc.put("USER_ID", userId+"");
                userFunc.put("FUNC_ID", jobFunc.get("FUNC_ID"));
                userFunc.put("START_DATE", session.getCreateTime());
                userFunc.put("END_DATE", "3000-12-31 23:59:59");
                userFunc.put("STATUS", "0");
                userFunc.put("CREATE_DATE", session.getCreateTime());
                userFunc.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
                userFunc.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
                userFunc.put("UPDATE_TIME", session.getCreateTime());
                userFuncs.add(userFunc);
            }
            dao.insertBatch("ins_user_func", userFuncs);
        }

        /** 新增user_contact信息 **/
        Map<String, String> userContact = new HashMap<String, String>();
        userContact.put("USER_ID", userId+"");
        userContact.put("CONTACT_TYPE", "1");
        userContact.put("CONTACT_NO", request.getString("MOBILE_NO"));
        userContact.put("CREATE_DATE", session.getCreateTime());
        userContact.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        userContact.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        userContact.put("UPDATE_TIME", session.getCreateTime());
        dao.insert("ins_user_contact", userContact);

        return response;
    }

    public ServiceResponse hasSubordinates(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String userId = request.getString("USER_ID");
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        EmployeeEntity employee = employeeDAO.queryEmployeeByUserId(userId);
        String employeeId = employee.getEmployeeId();
        List<EmployeeEntity> subordinates = EmployeeBean.getDirectSubordinates(employeeId);
        if(ArrayTool.isEmpty(subordinates)){
            response.set("HAS_SUB", "false");
        }
        else{
            response.set("HAS_SUB", "true");
        }
        return response;
    }

    public ServiceResponse destroyEmployee(ServiceRequest request) throws Exception{
        String userId = request.getString("USER_ID");
        Map<String, String> parameter = new HashMap<String, String>();

        AppSession session = SessionManager.getSession();

        UserDAO dao = DAOFactory.createDAO(UserDAO.class);
        parameter.put("USER_ID", userId);
        parameter.put("STATUS", "3");
        parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        parameter.put("UPDATE_TIME", session.getCreateTime());
        parameter.put("REMOVE_DATE", session.getCreateTime());
        dao.save("ins_user", parameter);


        parameter.clear();
        parameter.put("USER_ID", userId);
        parameter.put("STATUS", "3");
        parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        parameter.put("UPDATE_TIME", session.getCreateTime());
        parameter.put("DESTROY_DATE", session.getCreateTime());
        dao.save("ins_employee", new String[]{"USER_ID"}, parameter);

        parameter.clear();
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        EmployeeEntity employeeEntity = employeeDAO.queryEmployeeByUserId(userId);
        parameter.put("EMPLOYEE_ID", employeeEntity.getEmployeeId());
        parameter.put("END_DATE", session.getCreateTime());
        parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        parameter.put("UPDATE_TIME", session.getCreateTime());
        dao.save("ins_employee_job_role", new String[]{"EMPLOYEE_ID"}, parameter);
        try {
            dao.save("ins_houses_plan", new String[]{"EMPLOYEE_ID"}, parameter);
        }catch (SQLException e){

        }

        String parentEmployeeId = request.getString("PARENT_EMPLOYEE_ID");
        if(StringUtils.isNotBlank(parentEmployeeId)){
            employeeDAO.changeEmployeeParent(employeeEntity.getEmployeeId(), parentEmployeeId);
        }

        parameter.clear();
        parameter.put("USER_ID", userId);
        dao.delete("ins_user_func", new String[]{"USER_ID"}, parameter);
        return new ServiceResponse();
    }

    public ServiceResponse initQueryEmployees(ServiceRequest request) throws Exception{
        ServiceResponse response = this.initCreateEmployee(request);
        response.remove("TODAY");
        JSONArray citys = response.getJSONArray("CITYS");
        if(Permission.hasAllCity()){
            JSONArray allCitys = new JSONArray();
            JSONObject allCity = new JSONObject();
            allCity.put("CODE_VALUE", "");
            allCity.put("CODE_NAME", "所有城市");
            allCitys.add(allCity);
            allCitys.addAll(citys);
            response.set("CITYS", allCitys);
        }
        return response;
    }

    public ServiceResponse queryEmployees(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        String orgId = request.getString("ORG_ID");
        if(StringUtils.isNotBlank(orgId))
            orgId = OrgBean.getOrgLine(orgId);
        RecordSet employees = dao.queryEmployees(request.getString("NAME"), request.getString("SEX"), request.getString("CITY"), request.getString("MOBILE_NO"), request.getString("IDENTITY_NO"), orgId, request.getString("JOB_ROLE"), request.getString("PARENT_EMPLOYEE_ID"));
        if(employees == null || employees.size() <= 0)
            return response;

        int size = employees.size();
        for(int i=0;i<size;i++){
            Record employee = employees.get(i);
            if(StringUtils.equals("69", employee.get("USER_ID")) || StringUtils.equals("72", employee.get("USER_ID"))) {
                employee.put("CONTACT_NO", "***********");
            }
            employee.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", employee.get("JOB_ROLE")));
        }
        response.set("DATAS", ConvertTool.toJSONArray(employees));
        return response;
    }

    public ServiceResponse initChangeEmployee(ServiceRequest request) throws Exception{
        ServiceResponse response = this.initCreateEmployee(request);
        String employeeId = request.getString("EMPLOYEE_ID");

        JSONObject employeeInfo = new JSONObject();
        response.set("EMPLOYEE", employeeInfo);

        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        EmployeeEntity employee = dao.queryEmployeeByEmployeeId(employeeId);

        if(employee == null)
            return response;

        employeeInfo.put("NAME", employee.getName());
        employeeInfo.put("SEX", employee.getSex());
        employeeInfo.put("IDENTITY_NO", employee.getIdentityNo());
        String inDate = employee.getInDate();
        if(StringUtils.isNotBlank(inDate)){
            inDate = TimeTool.formatLocalDateTimeToString(TimeTool.stringToLocalDateTime(inDate,TimeTool.TIME_PATTERN),TimeTool.DATE_FMT_3);
        }
        employeeInfo.put("IN_DATE", inDate);
        employeeInfo.put("HOME_ADDRESS", employee.getHomeAddress());
        employeeInfo.put("USER_ID", employee.getUserId());
        employeeInfo.put("CITY", employee.getWorkPlace());
        employeeInfo.put("CITY_NAME", StaticDataTool.getCodeName("BIZ_CITY", employee.getWorkPlace()));

        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
        UserEntity user = userDAO.queryUserByPk(employee.getUserId());
        employeeInfo.put("MOBILE_NO", user.getMobileNo());

        EmployeeJobRoleDAO jobDAO = DAOFactory.createDAO(EmployeeJobRoleDAO.class);
        List<EmployeeJobRoleEntity> jobRoles = jobDAO.queryJobRoleByEmployeeId(employeeId);

        if(ArrayTool.isNotEmpty(jobRoles)) {
            EmployeeJobRoleEntity jobRole = jobRoles.get(0);
            employeeInfo.put("JOB_ROLE", jobRole.getJobRole());
            employeeInfo.put("JOB_ROLE_NAME", StaticDataTool.getCodeName("JOB_ROLE", jobRole.getJobRole()));
            employeeInfo.put("ORG_ID", jobRole.getOrgId());
            OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
            OrgEntity org = orgDAO.queryOrgById(jobRole.getOrgId());
            employeeInfo.put("ORG_NAME", org.getName());

            String parentEmployeeId = jobRole.getParentEmployeeId();
            if(StringUtils.isNotBlank(parentEmployeeId)){
                employeeInfo.put("PARENT_EMPLOYEE_ID", parentEmployeeId);
                EmployeeEntity parentEmployee = dao.queryEmployeeByEmployeeId(parentEmployeeId);
                employeeInfo.put("PARENT_EMPLOYEE_NAME", parentEmployee.getName());
            }

        }
        return response;
    }

    public ServiceResponse changeEmployee(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();

        String employeeId = request.getString("EMPLOYEE_ID");

        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
        UserEntity user = userDAO.queryUserByEmployeeId(employeeId);

        String mobileNo = request.getString("MOBILE_NO");
        if(!StringUtils.equals(mobileNo, user.getMobileNo())){

            //更新用户信息
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("USER_ID", user.getUserId());
            parameter.put("USERNAME", mobileNo);
            parameter.put("MOBILE_NO", mobileNo);
            parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            parameter.put("UPDATE_TIME", session.getCreateTime());
            userDAO.save("ins_user", parameter);

            parameter.clear();
            parameter.put("USER_ID", user.getUserId());
            parameter.put("CONTACT_TYPE", "1");
            parameter.put("CONTACT_NO", mobileNo);
            parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            parameter.put("UPDATE_TIME", session.getCreateTime());
            userDAO.save("ins_user_contact", new String[]{"USER_ID", "CONTACT_TYPE"}, parameter);
        }

        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        EmployeeEntity employee = employeeDAO.queryEmployeeByEmployeeId(employeeId);
        String name = request.getString("NAME");
        String sex = request.getString("SEX");
        String city = request.getString("CITY");
        String identityNo = request.getString("IDENTITY_NO");
        String homeAddress = request.getString("HOME_ADDRESS");
        String inDate = request.getString("IN_DATE");

        if(!StringUtils.equals(name, employee.getName())
                || !StringUtils.equals(sex, employee.getSex())
                || !StringUtils.equals(city, employee.getWorkPlace())
                || !StringUtils.equals(identityNo, employee.getIdentityNo())
                || !StringUtils.equals(homeAddress, employee.getHomeAddress())
                || !StringUtils.equals(inDate+" 00:00:00", employee.getInDate())){
            //修改了员工信息，更新员工表
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("EMPLOYEE_ID", employeeId);
            parameter.put("NAME", name);
            parameter.put("SEX", sex);
            parameter.put("WORKPLACE", city);
            parameter.put("IDENTITY_NO", identityNo);
            parameter.put("HOME_ADDRESS", homeAddress);
            parameter.put("IN_DATE", inDate);
            parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            parameter.put("UPDATE_TIME", session.getCreateTime());
            employeeDAO.save("ins_employee", parameter);
        }

        EmployeeJobRoleDAO jobRoleDAO = DAOFactory.createDAO(EmployeeJobRoleDAO.class);
        List<EmployeeJobRoleEntity> jobRoles = jobRoleDAO.queryJobRoleByEmployeeId(employeeId);
        if(ArrayTool.isNotEmpty(jobRoles)){
            EmployeeJobRoleEntity jobRoleEntity = jobRoles.get(0);
            String jobRole = request.getString("JOB_ROLE");
            String orgId = request.getString("ORG_ID");
            String parentEmployeeId = request.getString("PARENT_EMPLOYEE_ID");
            if(!StringUtils.equals(jobRole, jobRoleEntity.getJobRole()) || !StringUtils.equals(orgId, jobRoleEntity.getOrgId()) || !StringUtils.equals(parentEmployeeId, jobRoleEntity.getParentEmployeeId())){
                Map<String, String> parameter = new HashMap<String, String>();
                parameter.put("EMPLOYEE_ID", employeeId);
                parameter.put("END_DATE", session.getCreateTime());
                parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
                parameter.put("UPDATE_TIME", session.getCreateTime());
                jobRoleDAO.save("ins_employee_job_role", new String[]{"EMPLOYEE_ID"}, parameter);

                parameter.clear();
                parameter.put("EMPLOYEE_ID", employeeId);
                parameter.put("JOB_ROLE", jobRole);
                parameter.put("JOB_ROLE_NATURE", "1");
                parameter.put("ORG_ID", orgId);
                parameter.put("PARENT_EMPLOYEE_ID", parentEmployeeId);
                parameter.put("START_DATE", session.getCreateTime());
                parameter.put("END_DATE", "3000-12-31 23:59:59");
                parameter.put("CREATE_DATE", session.getCreateTime());
                parameter.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
                parameter.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
                parameter.put("UPDATE_TIME", session.getCreateTime());

                jobRoleDAO.insertAutoIncrement("ins_employee_job_role", parameter);

                if(!StringUtils.equals(jobRole, jobRoleEntity.getJobRole())){
                    //重新导入权限
                    parameter.clear();
                    parameter.put("USER_ID", user.getUserId());
                    userDAO.delete("ins_user_func", new String[]{"USER_ID"}, parameter);

                    FuncDAO funcDAO = DAOFactory.createDAO(FuncDAO.class);
                    RecordSet jobFuncs = funcDAO.queryJobFunc(jobRole);
                    List<Map<String, String>> userFuncs = new ArrayList<Map<String, String>>();
                    if(jobFuncs != null && jobFuncs.size() > 0){
                        int jobFuncSize = jobFuncs.size();
                        for(int i=0;i<jobFuncSize;i++){
                            Record jobFunc = jobFuncs.get(i);
                            Map<String, String> userFunc = new HashMap<String, String>();
                            userFunc.put("USER_ID", user.getUserId()+"");
                            userFunc.put("FUNC_ID", jobFunc.get("FUNC_ID"));
                            userFunc.put("START_DATE", session.getCreateTime());
                            userFunc.put("END_DATE", "3000-12-31 23:59:59");
                            userFunc.put("STATUS", "0");
                            userFunc.put("CREATE_DATE", session.getCreateTime());
                            userFunc.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
                            userFunc.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
                            userFunc.put("UPDATE_TIME", session.getCreateTime());
                            userFuncs.add(userFunc);
                        }
                        userDAO.insertBatch("ins_user_func", userFuncs);
                    }
                }
            }
        }
        return response;
    }
}
