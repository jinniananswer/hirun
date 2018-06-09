package com.hirun.app.bean.employee;

import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.factory.DAOFactory;

/**
 * Created by pc on 2018-05-29.
 */
public class EmployeeBean {

    /**
     * 根据mobileNo查员工信息
     * @param mobileNo
     * @return
     * @throws Exception
     */
    public static EmployeeEntity getEmployeeByMobileNo(String mobileNo) throws Exception {
        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);

        UserEntity userEntity = userDAO.queryUserByMobileNo(mobileNo);
        if(userEntity == null) {
            return null;
        }

        EmployeeEntity employeeEntity = employeeDAO.queryEmployeeByUserId(userEntity.getUserId());
        return employeeEntity;
    }

    public static EmployeeEntity getEmployeeByEmployeeId(String employeeId) throws Exception {
        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);

        return employeeDAO.queryEmployeeByEmployeeId(employeeId);
    }
}
