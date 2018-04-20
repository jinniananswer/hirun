package com.hirun.app.biz.login;

import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.security.Encryptor;
import org.apache.commons.lang3.StringUtils;

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
        String passwordEncrpt = Encryptor.encryptMd5(password);
        if(!StringUtils.equals(userPassword, passwordEncrpt)){
            response.setError("HIRUN_LOGIN_000002", "密码不正确，请重新输入");
            return response;
        }

        EmployeeDAO employeeDao = new EmployeeDAO("ins");
        EmployeeEntity employee = employeeDao.queryEmployeeByUserId(user.getUserId());
        response.set("USER", user.toJson());
        response.set("EMPLOYEE", employee.toJson());

        return response;
    }
}
