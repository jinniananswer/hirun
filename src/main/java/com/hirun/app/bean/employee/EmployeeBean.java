package com.hirun.app.bean.employee;

import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    public static List<EmployeeEntity> getDirectSubordinates(String employeeId) throws Exception{
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        return dao.querySubordinatesByParentEmployee(employeeId);
    }


    public static List<EmployeeEntity> recursiveAllSubordinates(String parentEmployeeIds) throws Exception{
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        List<EmployeeEntity> subordinates = dao.querySubordinatesInParentEmployee(parentEmployeeIds);
        if(ArrayTool.isEmpty(subordinates))
            return subordinates;

        List<EmployeeEntity> rst = new ArrayList<EmployeeEntity>();
        rst.addAll(subordinates);

        String recursiveParentEmployeeIds = "";
        for(EmployeeEntity employee : subordinates){
            recursiveParentEmployeeIds += employee.getEmployeeId() + ",";
        }

        recursiveParentEmployeeIds = recursiveParentEmployeeIds.substring(0, recursiveParentEmployeeIds.length() - 1);
        if(StringUtils.isNotEmpty(recursiveParentEmployeeIds)){
            List<EmployeeEntity> tmpSubordinates = recursiveAllSubordinates(recursiveParentEmployeeIds);
            if(ArrayTool.isNotEmpty(tmpSubordinates))
                rst.addAll(tmpSubordinates);
        }

        return rst;
    }

}
