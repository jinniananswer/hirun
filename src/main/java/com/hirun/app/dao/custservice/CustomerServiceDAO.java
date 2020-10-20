package com.hirun.app.dao.custservice;

import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.custservice.PartyOriginalActionEntity;
import com.hirun.pub.domain.entity.custservice.ProjectEntity;
import com.hirun.pub.domain.entity.custservice.ProjectIntentionEntity;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@DatabaseName("ins")
public class CustomerServiceDAO extends StrongObjectDAO {

    public CustomerServiceDAO(String databaseName) {
        super(databaseName);
    }


    public RecordSet queryPartyFlowByProjectId(String project_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party a,ins_project_original_action b ");
        sb.append(" where a.PARTY_ID=b.PARTY_ID ");
        sb.append(" and b.PROJECT_ID= :PROJECT_ID ");
        parameter.put("PROJECT_ID", project_id);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryLinkmanByProjectIdAndRoleType(String project_id, String roleType, String validTag) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from ins_project_linkman t  , ins_employee a ");
        sb.append(" where t.LINK_EMPLOYEE_ID =a.EMPLOYEE_ID ");
        sb.append(" and t.PROJECT_ID=:PROJECT_ID ");
        sb.append(" and t.ROLE_TYPE =:ROLE_TYPE ");

        if (StringUtils.equals(validTag, "valid")) {
            sb.append(" and a.status ='0' ");
        }

        parameter.put("PROJECT_ID", project_id);
        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }

    public PartyEntity queryPartyInfoByPartyId(String party_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        parameter.put("PARTY_ID", party_id);
        List<PartyEntity> partyEntityList = this.query(PartyEntity.class, "ins_party", parameter);

        if (ArrayTool.isEmpty(partyEntityList)) {
            return null;
        }
        return partyEntityList.get(0);
    }

    public RecordSet queryDesignerByOrgId(String orgId, String name) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();


        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city, f.NAME parent_org_name  ");
        sb.append("from ins_user a, ins_employee b , ");
        sb.append("ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and e.nature='5' ");

        if (StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if (StringUtils.isNotBlank(orgId)) {
            sb.append("and d.org_id in ( " + orgId + ") ");
        }

        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryProjectLinkManByProjectId(String projectId, String roleType) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ins_project_linkman where PROJECT_ID= :PROJECT_ID AND ROLE_TYPE= :ROLE_TYPE");
        parameter.put("PROJECT_ID", projectId);
        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryPartyInfoByLinkEmployeeId(String roleType, String name, String wxnick, String mobile, String houseaddress, String employeeIds) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_project_linkman a ,ins_project b, ins_party c ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= :ROLE_TYPE  ");
        sb.append(" and c.PARTY_STATUS= '0'  ");


        if (StringUtils.isNotBlank(name)) {
            sb.append("and c.party_name like concat('%',:PARTY_NAME,'%') ");
            parameter.put("PARTY_NAME", name);
        }

        if (StringUtils.isNotBlank(employeeIds)) {
            sb.append("and a.LINK_EMPLOYEE_ID in ( " + employeeIds + ") ");
        }

        if (StringUtils.isNotBlank(wxnick)) {
            sb.append("and c.WX_NICK like concat('%',:WX_NICK,'%') ");
            parameter.put("WX_NICK", wxnick);
        }

        if (StringUtils.isNotBlank(mobile)) {
            sb.append("and c.MOBILE_NO=:MOBILE_NO ");
            parameter.put("MOBILE_NO", mobile);
        }

        if (StringUtils.isNotBlank(houseaddress)) {
            sb.append("and b.HOUSE_ADDRESS like concat('%',:HOUSE_ADDRESS,'%') ");
            parameter.put("HOUSE_ADDRESS", houseaddress);
        }


        sb.append(" order by c.create_time  desc ");


        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryPartyInfoByLinkEmployeeIdAndTag(String roleType, String name, String wxnick, String mobile, String houseaddress, String employeeIds, String tagId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select c.*, a.*, b.*, d.* from  ins_project_linkman a ,ins_project b, ins_party c  ");
        sb.append("left join ins_party_tag d on ");
        sb.append("(c.PARTY_ID=d.PARTY_ID AND d.Status='0') ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= :ROLE_TYPE  ");
        sb.append(" and c.PARTY_STATUS= '0'  ");


        if (StringUtils.isNotBlank(name)) {
            sb.append("and c.party_name like concat('%',:PARTY_NAME,'%') ");
            parameter.put("PARTY_NAME", name);
        }

        if (StringUtils.isNotBlank(employeeIds)) {
            sb.append("and a.LINK_EMPLOYEE_ID in ( " + employeeIds + ") ");
        }

        if (StringUtils.isNotBlank(wxnick)) {
            sb.append("and c.WX_NICK like concat('%',:WX_NICK,'%') ");
            parameter.put("WX_NICK", wxnick);
        }

        if (StringUtils.isNotBlank(mobile)) {
            sb.append("and c.MOBILE_NO=:MOBILE_NO ");
            parameter.put("MOBILE_NO", mobile);
        }

        if (StringUtils.isNotBlank(houseaddress)) {
            sb.append("and b.HOUSE_ADDRESS like concat('%',:HOUSE_ADDRESS,'%') ");
            parameter.put("HOUSE_ADDRESS", houseaddress);
        }

        if (StringUtils.isNotBlank(tagId)) {
            sb.append("and d.TAG_ID= :TAG_ID");
            parameter.put("TAG_ID", tagId);
        }
        sb.append(" order by c.create_time  desc ");


        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryPartyInfoByLinkEmployeeIds(String employeeIds, String roleType, String name, String mobile, String custservieEmpId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_project_linkman a ,ins_project b, ins_party c ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= :ROLE_TYPE  ");
        sb.append(" and c.PARTY_STATUS='0'  ");


        if (StringUtils.isNotBlank(name)) {
            sb.append("and c.party_name like concat('%',:PARTY_NAME,'%') ");
            parameter.put("PARTY_NAME", name);
        }

        if (StringUtils.isNotBlank(employeeIds)) {
            sb.append("and a.LINK_EMPLOYEE_ID in ( " + employeeIds + ") ");
        }

        if (StringUtils.isNotBlank(mobile)) {
            sb.append("and c.MOBILE_NO=:MOBILE_NO ");
            parameter.put("MOBILE_NO", mobile);
        }

        if (StringUtils.isNotBlank(custservieEmpId)) {
            sb.append("and a.LINK_EMPLOYEE_ID=:LINK_EMPLOYEE_ID ");
            parameter.put("LINK_EMPLOYEE_ID", custservieEmpId);
        }


        sb.append(" order by c.create_time  desc ");


        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }

    public ProjectEntity queryProjectInfoByProjectId(String project_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        parameter.put("PROJECT_ID", project_id);
        List<ProjectEntity> projectEntityList = this.query(ProjectEntity.class, "ins_project", parameter);

        if (ArrayTool.isEmpty(projectEntityList)) {
            return null;
        }
        return projectEntityList.get(0);
    }

    public ProjectEntity queryProjectInfoByPartyId(String party_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        parameter.put("PARTY_ID", party_id);
        List<ProjectEntity> projectEntityList = this.query(ProjectEntity.class, "ins_project", parameter);

        if (ArrayTool.isEmpty(projectEntityList)) {
            return null;
        }
        return projectEntityList.get(0);
    }

    public Record queryProjectIntentionInfoByProjectId(String project_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PROJECT_ID", project_id);
        return this.query("ins_project_intention", parameter).get(0);
    }


    public RecordSet queryBluePrintByOpenIdAndActionCode(String openid, String action_code) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_blueprint_action ");
        sb.append("where OPEN_ID=:OPEN_ID  ");
        if (StringUtils.equals(action_code, "XQLTY")) {
            sb.append(" and action_code in ('XQLTY','XQLTY_A','XQLTY_B','XQLTY_C')");
        } else {
            sb.append(" and action_code=:ACTION_CODE");
            parameter.put("ACTION_CODE", action_code);
        }
        sb.append(" order by create_time ");
        parameter.put("OPEN_ID", openid);
        RecordSet recordSet = queryBySql(sb.toString(), parameter);

        if (recordSet.size() < 0) {
            return null;
        }
        return recordSet;
    }

    public RecordSet queryXQLTEByOpenIdAndActionCode(String openid, String action_code, String empId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_blueprint_action ");
        sb.append("where OPEN_ID=:OPEN_ID AND ACTION_CODE=:ACTION_CODE ");
        sb.append("AND REL_EMPLOYEE_ID = :EMPLOYEE_ID ");
        sb.append("order by XQLTE_CREATE_TIME desc ");
        parameter.put("OPEN_ID", openid);
        parameter.put("ACTION_CODE", action_code);
        parameter.put("EMPLOYEE_ID", empId);
        RecordSet recordSet = queryBySql(sb.toString(), parameter);


        return recordSet;
    }


    public RecordSet querySignInInfoByOpenIdAndEmpId(String openid) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_signin_action ");
        sb.append("where OPEN_ID=:OPEN_ID ");
        sb.append("order by SIGNIN_TIME  ");
        parameter.put("OPEN_ID", openid);
        RecordSet recordSet = queryBySql(sb.toString(), parameter);

        return recordSet;
    }

    public PartyEntity queryPartyInfoByOpenId(String open_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        parameter.put("OPEN_ID", open_id);
        List<PartyEntity> partyEntityList = this.query(PartyEntity.class, "ins_party", parameter);

        if (ArrayTool.isEmpty(partyEntityList)) {
            return null;
        }
        return partyEntityList.get(0);
    }

    public RecordSet queryPartyInfoByOpenIdAndEmployeeId(String openid, String employeeid) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from  ins_party c , ins_project_linkman a ,ins_project b ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= 'CUSTOMERSERVICE'  ");
        sb.append(" and c.PARTY_STATUS= '0'  ");
        sb.append(" and a.LINK_EMPLOYEE_ID= :EMPLOYEE_ID ");
        sb.append(" and c.OPEN_ID= :OPEN_ID ");
        parameter.put("EMPLOYEE_ID", employeeid);
        parameter.put("OPEN_ID", openid);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public RecordSet queryLinkManByPartyId(String party_id, String role_type) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_project_linkman a ,ins_project b, ins_party c ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= :ROLE_TYPE  ");
        sb.append(" and c.party_id= :PARTY_ID ");
        parameter.put("PARTY_ID", party_id);
        parameter.put("ROLE_TYPE", role_type);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public RecordSet getCityCabinByCityId(String city_id, String name) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_citycabin  ");
        sb.append(" where BIZ_CITY= :BIZ_CITY ");
        sb.append(" and SCAN_END_TIME > now()  ");
        sb.append(" and SCAN_END_TIME > SCAN_START_TIME  ");

        if (StringUtils.isNotBlank(name)) {
            sb.append("and CITYCABIN_ADDRESS like concat('%',:CITYCABIN_ADDRESS,'%') ");
            parameter.put("CITYCABIN_ADDRESS", name);
        }

        parameter.put("BIZ_CITY", city_id);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public RecordSet queryInsScanCityInfoByProIdAndPId(String projectId, String partyId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_scan_citycabin  ");
        sb.append(" where PARTY_ID= :PARTY_ID ");
        sb.append(" and PROJECT_ID= :PROJECT_ID  ");
        sb.append(" order by create_time DESC ");
        parameter.put("PARTY_ID", partyId);
        parameter.put("PROJECT_ID", projectId);

        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }


    public RecordSet queryCustServFinishActionInfo(String startDate, String endDate, String employeeIds, String orgIds, String name, String tagId, String wxNick, String busiTypeTime) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select v.*,s.city_cabins,s.experience_time,s.experience , u.FUNCPRINT_CREATE_TIME,u.STYLEPRINT_CREATE_TIME , j.visitcount ,o.tag_id from ");
        sb.append(" ( ");
        sb.append("SELECT a.WX_NICK,a.OPEN_ID,a.create_time,a.PARTY_NAME,c.LINK_EMPLOYEE_ID,a.PARTY_ID,b.PROJECT_ID,d.FINISH_TIME,b.HOUSE_ADDRESS,d.ACTION_CODE,a.consult_time,b.house_id from ");
        sb.append("ins_party a, ins_project b, ins_project_linkman c , ins_project_original_action d ,ins_employee e , ins_employee_job_role f ");
        sb.append("WHERE a.PARTY_ID = b.PARTY_ID ");
        sb.append("AND b.PROJECT_ID = c.PROJECT_ID ");
        sb.append("AND b.PROJECT_ID = d.PROJECT_ID ");
        sb.append("AND c.ROLE_TYPE = 'CUSTOMERSERVICE' ");
        sb.append("AND a.PARTY_STATUS = '0' ");
        sb.append("AND d.ACTION_CODE in ('SMJRLC','HZHK','APSJS') ");
        sb.append("AND e.EMPLOYEE_ID=f.EMPLOYEE_ID ");
        sb.append("AND e.STATUS='0' ");
        sb.append("AND NOW() BETWEEN f.START_DATE AND f.END_DATE ");
        sb.append("AND c.LINK_EMPLOYEE_ID=e.EMPLOYEE_ID ");

        if (StringUtils.equals(busiTypeTime, "1")) {
            if (StringUtils.isNotBlank(startDate)) {
                sb.append("and a.create_time  > :START_DATE ");
                parameter.put("START_DATE", startDate);
            }

            if (StringUtils.isNotBlank(endDate)) {
                sb.append("and a.create_time  < :END_DATE ");
                parameter.put("END_DATE", endDate);
            }
        }

        if (StringUtils.equals(busiTypeTime, "2")) {
            if (StringUtils.isNotBlank(startDate)) {
                sb.append("and a.consult_time  > :START_DATE ");
                parameter.put("START_DATE", startDate);
            }

            if (StringUtils.isNotBlank(endDate)) {
                sb.append("and a.consult_time  < :END_DATE ");
                parameter.put("END_DATE", endDate);
            }
        }

        if (StringUtils.isNotBlank(employeeIds)) {
            sb.append("and e.EMPLOYEE_ID IN (" + employeeIds + ") ");
        }

        if (StringUtils.isNotBlank(orgIds)) {
            sb.append("and f.org_id in ( " + orgIds + ") ");
        }

        if (StringUtils.isNotBlank(name)) {
            sb.append("and a.party_name like concat('%',:PARTY_NAME,'%') ");
            parameter.put("PARTY_NAME", name);
        }

        if (StringUtils.isNotBlank(wxNick)) {
            sb.append("and a.wx_nick like concat('%',:WX_NICK,'%') ");
            parameter.put("WX_NICK", wxNick);
        }

        sb.append(" order by c.LINK_EMPLOYEE_ID, a.create_time  desc ");

        sb.append(" ) v");
        sb.append(" left join (select * from ins_scan_citycabin x where x.SCAN_ID in (select min(y.scan_id) from ins_scan_citycabin y group by y.PARTY_ID))  s on (s.PARTY_ID = v.PARTY_ID) ");
        sb.append(" left join (select * from ins_blueprint_action r where r.BLUEPRINT_ACTION_ID in (select max(t.BLUEPRINT_ACTION_ID) from ins_blueprint_action t where t.ACTION_CODE='XQLTE' group by t.OPEN_ID, t.REL_EMPLOYEE_ID)) u on (u.OPEN_ID = v.OPEN_ID and u.REL_EMPLOYEE_ID = v.LINK_EMPLOYEE_ID) ");
        sb.append(" left join (select k.PARTY_ID,count(1) visitcount from ins_party_visit k group by k.PARTY_ID ) j ON (v.PARTY_ID=j.PARTY_ID) ");
        //2020/03/15新增
        sb.append(" left join ins_party_tag o  ON (v.PARTY_ID=o.PARTY_ID) ");

        if (StringUtils.isNotBlank(tagId)) {
            sb.append(" where o.tag_id =:TAG_ID ");
            parameter.put("TAG_ID", tagId);
        }

        sb.append(" order by v.create_time  desc ");


        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public Record getCityCabinById(String id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_citycabin  ");
        sb.append(" where CITY_CABIN_ID= :ID ");
        parameter.put("ID", id);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        if (recordSet.size() < 0 || recordSet == null) {
            return null;
        }
        return recordSet.get(0);
    }

    public RecordSet queryCustServMonStatInfo(String employeeId, String monDate) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_employee a,stat_custservice_month b ");
        sb.append("where a.EMPLOYEE_ID=b.object_id ");
        sb.append("and b.stat_month= :MONDATE  ");

        parameter.put("MONDATE", monDate);

        if (StringUtils.isNotBlank(employeeId)) {
            sb.append("and b.OBJECT_ID IN (" + employeeId + ") ");
        }


        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public RecordSet queryPartyInfoForCustClear(String partyId, String projectId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party a, ins_project b ,ins_project_linkman c ");
        sb.append(" where a.PARTY_ID=b.PARTY_ID ");
        sb.append(" and b.PROJECT_ID=c.PROJECT_ID ");
        sb.append(" and a.PARTY_ID = :PARTY_ID");
        sb.append(" and b.PROJECT_ID = :PROJECT_ID");
        parameter.put("PARTY_ID", partyId);
        parameter.put("PROJECT_ID", projectId);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryCustClearInfo(String partyId, String status, String applyEmpId, String aduitEmpId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party_clear a where 1=1 ");

        if (StringUtils.isNotBlank(partyId)) {
            sb.append("and a.PARTY_ID IN (" + partyId + ") ");
        }

        if (StringUtils.isNotBlank(status)) {
            sb.append("and a.STATUS IN (" + status + ") ");
        }

        if (StringUtils.isNotBlank(applyEmpId)) {
            sb.append("and a.APPLY_EMPLOYEE_ID IN (" + applyEmpId + ") ");
        }

        if (StringUtils.isNotBlank(aduitEmpId)) {
            sb.append("and a.AUDIT_EMPLOYEE_ID IN (" + aduitEmpId + ") ");
        }


        return this.queryBySql(sb.toString(), parameter);
    }


    public RecordSet queryPartyVisitInfo(String partyId, String employeeId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party_visit a where 1=1 ");

        if (StringUtils.isNotBlank(partyId)) {
            sb.append("and a.PARTY_ID IN (" + partyId + ") ");
        }


        if (StringUtils.isNotBlank(employeeId)) {
            sb.append("and a.VISIT_EMPLOYEE_ID IN (" + employeeId + ") ");
        }

        sb.append("order by create_time desc ");

        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryChildEmpByEmpIdsAndName(String employeeIds, String name, String orgId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city, f.NAME parent_org_name  ");
        sb.append("from ins_user a, ins_employee b , ");
        sb.append("ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");


        if (StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if (StringUtils.isNotBlank(orgId)) {
            sb.append("and d.org_id in ( " + orgId + ") ");
        }

        if (StringUtils.isNotBlank(employeeIds)) {
            sb.append("and b.employee_id in ( " + employeeIds + ") ");
        }

        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryAllCustServiceByName(String name, String orgId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city, f.NAME parent_org_name  ");
        sb.append("from ins_user a, ins_employee b , ");
        sb.append("ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' ");
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and d.job_role in ('46','118','69','119') ");
        sb.append("and e.name='客户部' ");

        if (StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if (StringUtils.isNotBlank(orgId)) {
            sb.append("and d.org_id in ( " + orgId + ") ");
        }


        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryPartyTagInfoByPartyId(String partyId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party_tag a ");
        sb.append("where a.PARTY_ID= :PARTY_ID ");
        sb.append("and a.STATUS= '0' ");
        parameter.put("PARTY_ID", partyId);

        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public List<PartyOriginalActionEntity> queryPartyOriginalAction(String partyId, String projectId, String actionCode) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_project_original_action a ");
        sb.append("where 1=1 ");

        if (StringUtils.isNotBlank(partyId)) {
            sb.append("and a.PARTY_ID=:PARTY_ID ");
            parameter.put("PARTY_ID", partyId);
        }

        if (StringUtils.isNotBlank(partyId)) {
            sb.append("and a.PROJECT_ID=:PROJECT_ID ");
            parameter.put("PROJECT_ID", projectId);
        }

        if (StringUtils.isNotBlank(actionCode)) {
            sb.append("and a.ACTION_CODE=:ACTION_CODE ");
            parameter.put("ACTION_CODE", actionCode);
        }

        List<PartyOriginalActionEntity> list = this.queryBySql(PartyOriginalActionEntity.class, sb.toString(), parameter);
        if (list.size() <= 0) {
            return null;
        }
        return list;
    }

    /**
     * 2020/03/27新增实时统计客户代表月报表
     *
     * @param employeeId
     * @param startDate
     * @param endDate
     * @return
     */
    public RecordSet queryNewCustServMonStatInfo(String employeeId, String startDate, String endDate,String busiTypeTime) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select v.employee_id ,count(1) as consult_count,sum(v.sm_count) as scan_count,");
        sb.append(" SUM(city_Count) as scancityhouse_count,SUM(v.func_count) as func_count,SUM(style_count) as style_count,SUM(xqlte_count) as xqlte_count");
        sb.append(" from (");
        sb.append(" select link_employee_id as employee_id,");
        sb.append(" case WHEN (d.`status`='1' and d.finish_time BETWEEN :START_DATE and :END_DATE ) then '1' else 0 " +
                "   end as sm_count,");
        sb.append(" case WHEN EXISTS (select 1 from ins_scan_citycabin x where b.project_id=x.project_id and x.employee_id=c.link_employee_id " +
                "        and x.experience_Time BETWEEN :START_DATE and :END_DATE) then '1' else 0 " +
                "   end as city_count,");
        sb.append(" case WHEN EXISTS (SELECT 1 FROM ins_blueprint_action m where m.open_id=a.open_id and c.link_employee_id=m.rel_employee_id " +
                "       and m.funcprint_create_time BETWEEN :START_DATE and :END_DATE ) then '1' else 0 " +
                "  end as func_count,");
        sb.append(" case WHEN EXISTS (SELECT 1 FROM ins_blueprint_action n where n.open_id=a.open_id and c.link_employee_id=n.rel_employee_id " +
                "       and n.styleprint_create_time BETWEEN :START_DATE and :END_DATE) then '1' else 0 " +
                "   end as style_count,");
        sb.append(" case WHEN EXISTS (SELECT 1 FROM ins_blueprint_action y where y.open_id=a.open_id and c.link_employee_id=y.rel_employee_id " +
                "       and (y.styleprint_create_time BETWEEN :START_DATE and :END_DATE) and (y.funcprint_create_time BETWEEN :START_DATE and :END_DATE))  then '1' else 0 " +
                "   end as xqlte_count");
        sb.append(" from ins_party a,ins_project b,ins_project_linkman c ,ins_project_original_action d");
        sb.append(" where a.party_id=b.party_id");
        sb.append(" and b.project_id=c.project_id");
        sb.append(" and a.party_status='0' ");
        sb.append(" and b.project_id=d.project_id");
        sb.append(" and d.action_code='SMJRLC' ");
        sb.append(" and c.ROLE_TYPE = 'CUSTOMERSERVICE' ");

        if(StringUtils.equals(busiTypeTime,"2")){
            sb.append(" and a.consult_time BETWEEN :START_DATE and :END_DATE ");
        }else{
            sb.append(" and a.create_time BETWEEN :START_DATE and :END_DATE ");
        }
        sb.append(" and c.link_employee_id= :EMPLOYEE_ID ");
        sb.append(" ) v ");
        sb.append(" group by v.employee_id ");

        parameter.put("START_DATE", startDate);
        parameter.put("END_DATE", endDate);
        parameter.put("EMPLOYEE_ID", employeeId);

        return this.queryBySql(sb.toString(), parameter);
    }


    //2020/03/01新增
    public Record queryCustomerInfoByCustId(String partyId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARTY_ID", partyId);
        return this.queryByPk("ins_party", parameter);
    }

    //2020/03/02新增
    public Record queryProjectInfoByCustId(String custId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("PARTY_ID", custId);
        return this.query("ins_project", parameter).get(0);
    }

    //2020/05/17新增
    public RecordSet queryGoodLiveInfoActionInfo(String partyId, String projectId, String actionCode, String status) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_project_original_action a ");
        sb.append(" where a.PROJECT_ID=:PROJECT_ID ");
        sb.append(" and   a.PARTY_ID=:PARTY_ID ");
        sb.append(" and   a.ACTION_CODE=:ACTION_CODE ");
        sb.append(" and   a.STATUS=:STATUS ");
        sb.append(" and   a.FINISH_TIME IS NOT NULL ");
        parameter.put("PROJECT_ID", projectId);
        parameter.put("PARTY_ID", partyId);
        parameter.put("ACTION_CODE", actionCode);
        parameter.put("STATUS", status);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryCustomerInfo4Merge(String roleType, String mobile, String custservieEmpId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_project_linkman a ,ins_project b, ins_party c ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= :ROLE_TYPE  ");
        sb.append(" and c.PARTY_STATUS='0'  ");
        sb.append(" and (c.OPEN_ID IS NULL or c.OPEN_ID='')  ");


        if (StringUtils.isNotBlank(mobile)) {
            sb.append("and c.MOBILE_NO=:MOBILE_NO ");
            parameter.put("MOBILE_NO", mobile);
        }

        if (StringUtils.isNotBlank(custservieEmpId)) {
            sb.append("and a.LINK_EMPLOYEE_ID=:LINK_EMPLOYEE_ID ");
            parameter.put("LINK_EMPLOYEE_ID", custservieEmpId);
        }

        sb.append(" order by c.create_time  desc ");


        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }


    /**
     * 查询客户号码是否有报备记录
     * @param mobileNo
     * @return
     * @throws Exception
     */
    public RecordSet queryCustomerByMobile(String mobileNo) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select a.cust_id,a.cust_no,a.cust_name,b.prepare_employee_id,b.prepare_time,a.mobile_no," +
                " b.status as prepare_status,c.house_id,c.house_building,c.house_mode,c.house_room_no, b.id as prepare_id, c.project_id ," +
                " e.employee_id as custservice_employee_id,a.cust_type,a.cust_status,d.status as order_status");
        sb.append(" from cust_base a LEFT JOIN cust_preparation b on (a.prepare_id=b.id)," +
                "   ins_project c," +
                "   order_base d LEFT JOIN order_worker e on (d.order_id=e.order_id and e.role_id='15' and now() BETWEEN e.start_date and e.end_date)" +
                "   where a.cust_id=c.party_id");
        sb.append(" and a.cust_id=d.cust_id");
        sb.append(" and a.mobile_no= :MOBILE_NO ");

        parameter.put("MOBILE_NO",mobileNo);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }


    public RecordSet queryCustomerBaseInfo(String partyId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from cust_base a ");
        sb.append(" where   a.PARTY_ID=:PARTY_ID ");
        parameter.put("PARTY_ID", partyId);
        return this.queryBySql(sb.toString(), parameter);
    }


}