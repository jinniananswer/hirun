package com.hirun.app.biz.login;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.employee.EmployeeJobRoleDAO;
import com.hirun.app.dao.org.OrgDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.app.dao.user.UserDeviceDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.security.Encryptor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/17 21:11
 * @Description:
 */
public class LoginService extends GenericService{

    public ServiceResponse login(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String username = request.getBody().getString("username");
        String password = request.getBody().getString("password");

        UserDAO dao = new UserDAO("ins");
        UserEntity user = dao.queryUserByUsername(username);
        if(user == null){
            response.setError("HIRUN_LOGIN_000001", "用户名不正确，请重新输入");
            return response;
        }

        String userPassword = user.getPassword();
        String passwordEncrpt = null;
        if(StringUtils.equals("1", request.getString("IS_ENCRPT"))){
            passwordEncrpt = password;
        }
        else{
            passwordEncrpt = Encryptor.encryptMd5(password);
        }
        if(!StringUtils.equals(userPassword, passwordEncrpt)){
            response.setError("HIRUN_LOGIN_000002", "密码不正确，请重新输入");
            return response;
        }

        EmployeeDAO employeeDao = new EmployeeDAO("ins");
        EmployeeEntity employee = employeeDao.queryEmployeeByUserId(user.getUserId());

        if(employee != null) {
            EmployeeJobRoleDAO jobRoleDAO = new EmployeeJobRoleDAO("ins");
            List<EmployeeJobRoleEntity> jobRoles = jobRoleDAO.queryJobRoleByEmployeeId(employee.getEmployeeId());
            if(ArrayTool.isNotEmpty(jobRoles)){
                JSONArray jobRoleArray = new JSONArray();
                OrgDAO orgDAO = new OrgDAO("ins");
                for(EmployeeJobRoleEntity jobRole : jobRoles){
                    JSONObject jobRoleJson = jobRole.toJson();
                    String jobRoleName = StaticDataTool.getCodeName("JOB_ROLE", jobRole.getJobRole());
                    jobRoleJson.put("JOB_ROLE_NAME", jobRoleName);
                    OrgEntity org = orgDAO.queryOrgById(jobRole.getOrgId());
                    jobRoleJson.put("ORG_INFO", org.toJson());
                    jobRoleArray.add(jobRoleJson);
                }
                response.set("JOB_ROLE", jobRoleArray);
            }
        }
        response.set("USER", user.toJson());
        response.set("EMPLOYEE", employee.toJson());

        //保存用户的设备号
        String deviceToken = request.getString("USER_DEVICE_TOKEN");
        if(StringUtils.isNotEmpty(deviceToken)){
            deviceToken = deviceToken.replaceAll("&lt;", "");
            deviceToken = deviceToken.replaceAll("&gt;", "");
            deviceToken = deviceToken.replaceAll(" ", "");
            UserDeviceDAO deviceDAO = DAOFactory.createDAO(UserDeviceDAO.class);
            Record userDevice = deviceDAO.queryUserDeviceByUserId(user.getUserId());
            String os = request.getHeader().getString("OPERATION_SYSTEM");
            int osCode = 0;
            if(StringUtils.equals(os, "IOS"))
                osCode = 1;
            else if(StringUtils.equals(os, "ANDROID"))
                osCode = 2;
            if(userDevice == null){
                //没有，新增一条用户设备信息
                Map<String, String> deviceParameter = new HashMap<String, String>();
                deviceParameter.put("USER_ID", user.getUserId());
                deviceParameter.put("DEVICE_TOKEN", deviceToken);
                deviceParameter.put("CREATE_USER_ID", user.getUserId());
                deviceParameter.put("UPDATE_USER_ID", user.getUserId());
                deviceParameter.put("OPERATION_SYSTEM", osCode+"");
                String now = SessionManager.getSession().getCreateTime();
                deviceParameter.put("CREATE_TIME", now);
                deviceParameter.put("UPDATE_TIME", now);
                deviceDAO.insert("ins_user_device", deviceParameter);
            }
            else{
                Map<String, String> deviceParameter = userDevice.getData();
                deviceParameter.put("DEVICE_TOKEN", deviceToken);
                deviceParameter.put("OPERATION_SYSTEM", osCode+"");
                deviceDAO.save("ins_user_device", deviceParameter);
            }
        }

        return response;
    }

    public ServiceResponse autoLogin(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        String username = request.getBody().getString("username");

        UserDAO dao = new UserDAO("ins");
        UserEntity user = dao.queryUserByUsername(username);
        if(user == null){
            response.setError("HIRUN_LOGIN_000001", "用户名不正确，请重新输入");
            return response;
        }

        EmployeeDAO employeeDao = new EmployeeDAO("ins");
        EmployeeEntity employee = employeeDao.queryEmployeeByUserId(user.getUserId());

        if(employee != null) {
            EmployeeJobRoleDAO jobRoleDAO = new EmployeeJobRoleDAO("ins");
            List<EmployeeJobRoleEntity> jobRoles = jobRoleDAO.queryJobRoleByEmployeeId(employee.getEmployeeId());
            if(ArrayTool.isNotEmpty(jobRoles)){
                JSONArray jobRoleArray = new JSONArray();
                OrgDAO orgDAO = new OrgDAO("ins");
                for(EmployeeJobRoleEntity jobRole : jobRoles){
                    JSONObject jobRoleJson = jobRole.toJson();
                    String jobRoleName = StaticDataTool.getCodeName("JOB_ROLE", jobRole.getJobRole());
                    jobRoleJson.put("JOB_ROLE_NAME", jobRoleName);
                    OrgEntity org = orgDAO.queryOrgById(jobRole.getOrgId());
                    jobRoleJson.put("ORG_INFO", org.toJson());
                    jobRoleArray.add(jobRoleJson);
                }
                response.set("JOB_ROLE", jobRoleArray);
            }
        }
        response.set("USER", user.toJson());
        response.set("EMPLOYEE", employee.toJson());

        //保存用户的设备号
        String deviceToken = request.getString("USER_DEVICE_TOKEN");
        if(StringUtils.isNotEmpty(deviceToken)){
            deviceToken = deviceToken.replaceAll("&lt;", "");
            deviceToken = deviceToken.replaceAll("&gt;", "");
            deviceToken = deviceToken.replaceAll(" ", "");
            UserDeviceDAO deviceDAO = DAOFactory.createDAO(UserDeviceDAO.class);
            Record userDevice = deviceDAO.queryUserDeviceByUserId(user.getUserId());
            String os = request.getHeader().getString("OPERATION_SYSTEM");
            int osCode = 0;
            if(StringUtils.equals(os, "IOS"))
                osCode = 1;
            else if(StringUtils.equals(os, "ANDROID"))
                osCode = 2;
            if(userDevice == null){
                //没有，新增一条用户设备信息
                Map<String, String> deviceParameter = new HashMap<String, String>();
                deviceParameter.put("USER_ID", user.getUserId());
                deviceParameter.put("DEVICE_TOKEN", deviceToken);
                deviceParameter.put("CREATE_USER_ID", user.getUserId());
                deviceParameter.put("UPDATE_USER_ID", user.getUserId());
                deviceParameter.put("OPERATION_SYSTEM", osCode+"");
                String now = SessionManager.getSession().getCreateTime();
                deviceParameter.put("CREATE_TIME", now);
                deviceParameter.put("UPDATE_TIME", now);
                deviceDAO.insert("ins_user_device", deviceParameter);
            }
            else{
                Map<String, String> deviceParameter = userDevice.getData();
                deviceParameter.put("DEVICE_TOKEN", deviceToken);
                deviceParameter.put("OPERATION_SYSTEM", osCode+"");
                deviceDAO.save("ins_user_device", deviceParameter);
            }
        }

        return response;
    }
}
