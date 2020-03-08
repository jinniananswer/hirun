package com.hirun.app.bean.employee;

import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.dao.employee.EmployeeDAO;
import com.hirun.app.dao.employee.EmployeeJobRoleDAO;
import com.hirun.app.dao.org.OrgDAO;
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
//        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);

//        UserEntity userEntity = userDAO.queryUserByMobileNo(mobileNo);
//        if(userEntity == null) {
//            return null;
//        }

        EmployeeEntity employeeEntity = employeeDAO.getEmployeeByMobileNo(mobileNo);
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

    public static RecordSet recursiveAllSubordinatesByPempIdAndVaild(String parentEmployeeIds,String vaild) throws Exception{
        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        RecordSet subordinates = dao.querySubordinatesEmployeeBypEmpIdAndVaild(parentEmployeeIds,vaild);
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
            RecordSet tmpSubordinates = recursiveAllSubordinatesByPempIdAndVaild(recursiveParentEmployeeIds,vaild);
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
            if(StringUtils.equals("42", subordinate.get("JOB_ROLE")) || StringUtils.equals("58", subordinate.get("JOB_ROLE"))
                    ||StringUtils.equals("3",subordinate.get("NATURE"))){
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

    public static RecordSet queryCounselorByOrgId(String orgId) throws Exception{
        String orgLine = OrgBean.getOrgLine(orgId);
        if(StringUtils.isBlank(orgLine))
            return null;

        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        RecordSet counselors = dao.queryCounselorsByOrgIds(orgLine);
        return counselors;
    }

    public static RecordSet queryCounselorByOrgId(String orgId, List<OrgEntity> orgs) throws Exception{
        String orgLine = OrgBean.getOrgLine(orgId, orgs);
        if(StringUtils.isBlank(orgLine))
            return null;

        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        RecordSet counselors = dao.queryCounselorsByOrgIds(orgLine);
        return counselors;
    }

    public static RecordSet queryCounselorByOrgId(String orgId, List<OrgEntity> orgs, String employeeName) throws Exception{
        String orgLine = OrgBean.getOrgLine(orgId, orgs);
        if(StringUtils.isBlank(orgLine))
            return null;

        EmployeeDAO dao = DAOFactory.createDAO(EmployeeDAO.class);
        RecordSet counselors = dao.queryCounselorsByOrgIds(orgLine, employeeName);
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

    public static OrgEntity queryOrgByOrgId(String orgId, String type, List<OrgEntity> orgs) throws Exception{
        return OrgBean.getAssignTypeOrg(orgId, type, orgs);
    }

    public static RecordSet queryAllCounselors(String orgId, List<OrgEntity> orgs) throws Exception{
        RecordSet counselors = queryCounselorByOrgId(orgId, orgs);
        if(counselors == null || counselors.size() <= 0)
            return null;

        int size = counselors.size();
        for(int i=0;i<size;i++){
            Record counselor = counselors.get(i);
            String counselorOrgId = counselor.get("ORG_ID");
            OrgEntity shop = queryOrgByOrgId(counselorOrgId, "2", orgs);
            if(shop != null) {
                counselor.put("SHOP", shop.getOrgId());
                counselor.put("SHOP_NAME",shop.getName());
            }

            OrgEntity company = queryOrgByOrgId(counselorOrgId, "3", orgs);
            if(company != null) {
                counselor.put("COMPANY", company.getOrgId());
                counselor.put("COMPANY_NAME", company.getName());
            }
        }
        return counselors;
    }

    public static RecordSet queryAllCounselors(String orgId, List<OrgEntity> orgs, String employeeName) throws Exception{
        RecordSet counselors = queryCounselorByOrgId(orgId, orgs, employeeName);
        if(counselors == null || counselors.size() <= 0)
            return null;

        int size = counselors.size();
        for(int i=0;i<size;i++){
            Record counselor = counselors.get(i);
            String counselorOrgId = counselor.get("ORG_ID");
            OrgEntity shop = queryOrgByOrgId(counselorOrgId, "2", orgs);
            if(shop != null) {
                counselor.put("SHOP", shop.getOrgId());
                counselor.put("SHOP_NAME",shop.getName());
            }

            OrgEntity company = queryOrgByOrgId(counselorOrgId, "3", orgs);
            if(company != null) {
                counselor.put("COMPANY", company.getOrgId());
                counselor.put("COMPANY_NAME", company.getName());
            }
        }
        return counselors;
    }

    public static boolean isChangshaEmployee(String employeeId) throws Exception{
        EmployeeJobRoleDAO jobRoleDAO = DAOFactory.createDAO(EmployeeJobRoleDAO.class);
        List<EmployeeJobRoleEntity> jobRoles = jobRoleDAO.queryJobRoleByEmployeeId(employeeId);

        if(ArrayTool.isEmpty(jobRoles))
            return false;

        EmployeeJobRoleEntity jobRole = jobRoles.get(0);
        String orgId = jobRole.getOrgId();

        OrgDAO orgDAO = DAOFactory.createDAO(OrgDAO.class);
        OrgEntity org = orgDAO.queryOrgById(orgId);
        if(org == null)
            return false;

        String enterpriseId = org.getEnterpriseId();
        if(StringUtils.equals("2", enterpriseId))
            return true;
        else
            return false;
    }

    public static List<EmployeeEntity> queryUnEntryPlanEmployeeList(String planDate) throws Exception{
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryUnEntryPlanEmployeeList(planDate);
    }

    public static List<EmployeeEntity> queryEmployeeListByPlanType(String planDate, String planType) throws Exception{
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryEmployeeListByPlanType(planDate, planType);
    }

    public static RecordSet queryEmployeeJobRoleByOrgId(String orgId,String name) throws Exception{
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryEmployeeJobRoleByOrgId(orgId,name);
    }

    public static RecordSet queryEmployeeJobRoleByOrgId1(String orgId,String name,String employeeIds) throws Exception{
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryEmployeeJobRoleByOrgId1(orgId,name,employeeIds);
    }

    public static RecordSet queryEmployeeByEmpIdsAndOrgId(String empIds,String orgId) throws Exception{
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryEmployeeByEmpIdsAndOrgId(empIds,orgId);
    }

    public static EmployeeJobRoleEntity queryEmployeeJobRoleByEmpId(String employeeId) throws Exception {
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryEmployeeJobRoleByEmpId(employeeId);
    }

    public static RecordSet queryEmployeeByEmpIdsAndName(String employeeIds,String name) throws Exception {
        EmployeeDAO employeeDAO = DAOFactory.createDAO(EmployeeDAO.class);
        return employeeDAO.queryEmployeeByEmpIdsAndName(employeeIds,name);
    }
}
