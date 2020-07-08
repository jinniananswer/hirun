package com.hirun.app.biz.family;


import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.org.OrgBean;
import com.hirun.app.bean.permission.Permission;
import com.hirun.app.cache.EmployeeCache;
import com.hirun.app.dao.family.FamilyDAO;
import com.hirun.pub.consts.CustomerServiceConst;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.org.OrgEntity;
import com.hirun.pub.tool.CustomerNoTool;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.*;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.util.*;


public class FamilyService extends GenericService {

    /**
     * 查询用户信息进行组网
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse queryParty4CreateFamily(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        String name = request.getString("NAME");
        String mobile = request.getString("MOBILE");
        String custserviceEmpId = request.getString("CUSTSERVICEEMPLOYEEID");
        FamilyDAO dao = DAOFactory.createDAO(FamilyDAO.class);
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        String employeeIds = "";
        String orgId = "";

        List<OrgEntity> allOrgs = OrgBean.getAllOrgs();

        if (Permission.hasAllCity()) {
            orgId = "7";
        } else if (Permission.hasAllShop()) {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "3").getOrgId();
        } else {
            orgId = EmployeeBean.queryOrgByEmployee(employeeId, "2").getOrgId();
        }

        orgId = OrgBean.getOrgLine(orgId, allOrgs);

        if (StringUtils.isNotBlank(custserviceEmpId)) {
            employeeIds = custserviceEmpId;
        } else if (Permission.hasAllCity()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds.substring(0, employeeIds.length() - 1);
        } else if (Permission.hasAllShop()) {
            RecordSet allCustServiceEmpEntity = EmployeeBean.queryEmployeeByEmpIdsAndOrgId("", orgId);
            for (int i = 0; i < allCustServiceEmpEntity.size(); i++) {
                Record childRecord = allCustServiceEmpEntity.get(i);
                employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
            }
            employeeIds = employeeIds.substring(0, employeeIds.length() - 1);
        } else {
            RecordSet childEmployeeRecordSet = EmployeeBean.recursiveAllSubordinatesByPempIdAndVaild(employeeId, "0");
            if (childEmployeeRecordSet.size() <= 0 || childEmployeeRecordSet == null) {
                employeeIds = employeeId;
            } else {
                for (int i = 0; i < childEmployeeRecordSet.size(); i++) {
                    Record childRecord = childEmployeeRecordSet.get(i);
                    employeeIds += childRecord.get("EMPLOYEE_ID") + ",";
                }
                employeeIds = employeeIds + employeeId;
            }
        }

        RecordSet recordSet = dao.queryParty4CreateFamily(employeeIds, CustomerServiceConst.CUSTOMERSERVICEROLETYPE, name, mobile, "");
        if (recordSet.size() <= 0) {
            return response;
        }

        for (int i = 0; i < recordSet.size(); i++) {
            Record record = recordSet.get(i);
            String linkemployeeid = record.get("LINK_EMPLOYEE_ID");
            EmployeeEntity employeeEntity = EmployeeBean.getEmployeeByEmployeeId(linkemployeeid);
            record.put("CUSTSERVICENAME", employeeEntity.getName());
        }

        response.set("PARTYINFOLIST", ConvertTool.toJSONArray(recordSet));
        return response;
    }

    /**
     * 创建家庭
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse createFamily(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        AppSession session = SessionManager.getSession();
        FamilyDAO dao = DAOFactory.createDAO(FamilyDAO.class);
        String partyIds = request.getString("PARTY_IDS");
        String[] partyIdArr = partyIds.split(",");
        String groupId=CustomerNoTool.getFamilyGroupId();
        for (int i = 0; i < partyIdArr.length; i++) {
            Map<String, String> familyMember = new HashMap<String, String>();
            familyMember.put("GROUP_ID", groupId);
            familyMember.put("PARTY_ID", partyIdArr[i]);
            familyMember.put("REMARK", "家庭网创建");
            familyMember.put("START_DATE", TimeTool.now());
            familyMember.put("END_DATE", "3000-12-31 12:59:59");
            familyMember.put("CREATE_USER_ID", session.getSessionEntity().getUserId());
            familyMember.put("CREATE_TIME", session.getCreateTime());
            familyMember.put("UPDATE_USER_ID", session.getSessionEntity().getUserId());
            familyMember.put("UPDATE_TIME", session.getCreateTime());
            dao.insertAutoIncrement("ins_party_family",familyMember);
        }
        return response;
    }



    /**
     * 查询家庭网成员完成的记录
      * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse initShowFamilyMember(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        FamilyDAO dao = DAOFactory.createDAO(FamilyDAO.class);
        String partyId = request.getString("PARTY_ID");
        AppSession session = SessionManager.getSession();
        String employeeId = session.getSessionEntity().get("EMPLOYEE_ID");
        //根据party_id查询group_id
        RecordSet familySet=dao.getGroupIdByPartyId(partyId);
        if(familySet.size()<=0){
            return response;
        }
        //根据groupId查询所有partyId
        String groupId=familySet.get(0).get("GROUP_ID");

        RecordSet memberSet=dao.getAllMember(groupId);
        if(memberSet.size()<=0){
            return response;
        }
        String memberIds="";
        for(int i=0;i<memberSet.size();i++){
            Record memberRecord=memberSet.get(i);
            memberIds += memberRecord.get("PARTY_ID")+",";
        }

        RecordSet familyMemberSet=dao.queryFamilyMemberInfo(CustomerServiceConst.CUSTOMERSERVICEROLETYPE,memberIds.substring(0,memberIds.length()-1));

        if (familyMemberSet.size() <= 0) {
            return response;
        }

        for (int i = 0; i < familyMemberSet.size(); i++) {
            Record record = familyMemberSet.get(i);
            String linkEmployeeId = record.get("LINK_EMPLOYEE_ID");
            if (StringUtils.equals(employeeId, linkEmployeeId)) {
                record.put("SHOWMOBILE", "YES");
            } else {
                record.put("SHOWMOBILE", "NO");
            }
            record.put("CUSTSERVICENAME", EmployeeCache.getEmployeeNameEmployeeId(linkEmployeeId));
        }
        response.set("FAMILY_MEMBER_INFO",ConvertTool.toJSONArray(familyMemberSet));

        return response;
    }



}
