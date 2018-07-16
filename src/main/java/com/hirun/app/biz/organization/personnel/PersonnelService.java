package com.hirun.app.biz.organization.personnel;

import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.exception.GenericException;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.security.Encryptor;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/5/18 19:30
 * @Description:
 */
public class PersonnelService extends GenericService {

    public ServiceResponse changePassword(ServiceRequest request) throws Exception{
        AppSession session = SessionManager.getSession();
        String userId = session.getSessionEntity().getUserId();
        UserDAO dao = new UserDAO("ins");
        UserEntity user = dao.queryUserByPk(userId);
        if(user == null)
            throw new GenericException("ORG.CHANGE.PASSWORD.001", "用户不存在");

        String userPassword = user.getPassword();
        String oldPassword = request.getString("OLD_PASSWORD");
        String encryptorOldPassword = Encryptor.encryptMd5(oldPassword);

        if(!StringUtils.equals(userPassword, encryptorOldPassword))
            throw new GenericException("ORG.CHANGE.PASSWORD.002", "原密码输入错误");
        String newPassword = request.getString("PASSWORD");
        String encryptNewPassword = Encryptor.encryptMd5(newPassword);
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PASSWORD", encryptNewPassword);
        parameter.put("USER_ID", userId);

        dao.save("ins_user", parameter);
        return new ServiceResponse();
    }

    public ServiceResponse initCreateEmployee(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String today = TimeTool.today();
        response.set("TODAY", today);
        OrgDAO dao = new OrgDAO("ins");
        List<OrgEntity> orgs = dao.queryOrgByCityAndType("1", "4");
        response.set("SHOPS", ConvertTool.toJSONArray(orgs));
        if (ArrayTool.isNotEmpty(orgs)) {
            response.set("DEFAULT_SHOP_NAME", orgs.get(0).getName());
            response.set("DEFAULT_SHOP_ID", orgs.get(0).getOrgId());
        }
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
        long userId = dao.insertAutoIncrement("ins_user", user);

        log.debug("=============userid============"+userId);

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
        employee.put("WORK_PLACE", "1");
        employee.put("STATUS", "0");
        employee.put("CREATE_DATE", session.getCreateTime());
        employee.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        employee.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        employee.put("UPDATE_TIME", session.getCreateTime());
        long employeeId = dao.insertAutoIncrement("ins_employee", employee);

        OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
        OrgEntity org = orgDAO.queryMarketByShop(request.getString("SHOP"));
        String orgId = "";
        if(org != null)
            orgId = org.getOrgId();
        Map<String, String> job = new HashMap<String, String>();
        job.put("EMPLOYEE_ID", employeeId+"");
        job.put("JOB_ROLE", request.getString("JOB_ROLE"));
        job.put("JOB_ROLE_NATURE", "1");
        job.put("ORG_ID", orgId);
        job.put("PARENT_EMPLOYEE_ID", request.getString("PARENT_ORG_ID"));
        job.put("START_DATE", session.getCreateTime());
        job.put("END_DATE", "3000-12-31 23:59:59");
        job.put("CREATE_DATE", session.getCreateTime());
        job.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
        job.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
        job.put("UPDATE_TIME", session.getCreateTime());
        dao.insertAutoIncrement("ins_employee_job_role", job);

        String funcs = "";
        if("42".equals(request.getString("JOB_ROLE")))
            funcs = "1,3,4,5,17,18,20,22,23";
        else
            funcs = "1,2,3,4,5,17,18,20,22,23,13,14,15,16";

        String[] funcsArray = funcs.split(",");
        List<Map<String, String>> userFuncs = new ArrayList<Map<String, String>>();
        for(String func : funcsArray){
            Map<String, String> userFunc = new HashMap<String, String>();
            userFunc.put("USER_ID", userId+"");
            userFunc.put("FUNC_ID", func);
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

        parameter.clear();
        parameter.put("USER_ID", userId);
        dao.delete("ins_user_func", new String[]{"USER_ID"}, parameter);
        return new ServiceResponse();
    }
}
