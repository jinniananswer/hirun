package com.hirun.app.bean.employee;

import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.employee.EmployeeJobRoleDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.EmployeeJobRoleEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
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

    public static RecordSet recursiveAllSubordinatesReordSet(String parentEmployeeIds) throws Exception{
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        RecordSet subordinates = dao.querySubordinatesEmployeeInParentEmployee(parentEmployeeIds);
        if(subordinates == null || subordinates.size() <= 0)
            return subordinates;

        RecordSet rst = new RecordSet();
        rst.addAll(subordinates);

        String recursiveParentEmployeeIds = "";
        int size = subordinates.size();
        for(int i=0;i<size;i++){
            Record employee = subordinates.get(i);
            recursiveParentEmployeeIds += employee.get("EMPLOYEE_ID") + ",";
        }

        recursiveParentEmployeeIds = recursiveParentEmployeeIds.substring(0, recursiveParentEmployeeIds.length() - 1);
        if(StringUtils.isNotEmpty(recursiveParentEmployeeIds)){
            RecordSet tmpSubordinates = recursiveAllSubordinatesReordSet(recursiveParentEmployeeIds);
            if(tmpSubordinates != null && tmpSubordinates.size() > 0)
                rst.addAll(tmpSubordinates);
        }

        return rst;
    }

    public static RecordSet getAllSubordinatesCounselorRecordSet(String parentEmployeeIds) throws Exception{
        RecordSet subordinates = recursiveAllSubordinatesReordSet(parentEmployeeIds);

        if(subordinates == null || subordinates.size() <= 0)
            return null;

        RecordSet rst = new RecordSet();
        int size = subordinates.size();
        for(int i=0;i<size;i++){
            Record subordinate = subordinates.get(i);
            if(StringUtils.equals("42", subordinate.get("JOB_ROLE")) || StringUtils.equals("58", subordinate.get("JOB_ROLE"))){
                rst.add(subordinate);
            }
        }
        return rst;
    }

    public static List<EmployeeEntity> getAllSubordinatesCounselors(String parentEmployeeIds) throws Exception{
        RecordSet counselors = getAllSubordinatesCounselorRecordSet(parentEmployeeIds);

        if(counselors == null || counselors.size() <= 0)
            return null;

        int size = counselors.size();
        List<EmployeeEntity> rst = new ArrayList<EmployeeEntity>();
        for(int i=0;i<size;i++){
            EmployeeEntity employee = new EmployeeEntity(counselors.get(i).getData());
            rst.add(employee);
        }
        return rst;
    }

    public static String queryParentEmployeeIdByEmployeeIdAndJobRoles(String employeeId, String jobRoles) throws Exception {
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryParentEmployeeIdByEmployeeIdAndJobRoles(employeeId, jobRoles);
    }

    public static List<EmployeeEntity> queryCounselorByOrgId(String orgId) throws Exception{
        String orgLine = OrgBean.getOrgLine(orgId);
        if(StringUtils.isBlank(orgLine))
            return null;

        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        List<EmployeeEntity> counselors = dao.queryCounselorsByOrgIds(orgLine);
        return counselors;
    }

    public static OrgEntity queryOrgByEmployee(String employeeId, String type) throws Exception{
        EmployeeJobRoleDAO dao = DAOFactory.createDAO(EmployeeJobRoleDAO.class);
        List<EmployeeJobRoleEntity> jobRoles = dao.queryJobRoleByEmployeeId(employeeId);
        if(ArrayTool.isEmpty(jobRoles))
            return null;

        EmployeeJobRoleEntity jobRole = jobRoles.get(0);
        return OrgBean.getAssignTypeOrg(jobRole.getOrgId(), type);
    }
}
