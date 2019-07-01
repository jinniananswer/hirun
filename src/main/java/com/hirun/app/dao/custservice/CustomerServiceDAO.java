package com.hirun.app.dao.custservice;

import com.hirun.pub.domain.entity.custservice.PartyEntity;
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

    public RecordSet queryLinkmanByProjectIdAndRoleType(String project_id, String roleType) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from ins_project_linkman t  , ins_employee a ");
        sb.append(" where t.LINK_EMPLOYEE_ID =a.EMPLOYEE_ID ");
        sb.append(" and a.status ='0' ");
        sb.append(" and t.PROJECT_ID=:PROJECT_ID ");
        sb.append(" and t.ROLE_TYPE =:ROLE_TYPE ");
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
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and d.job_role in (29,70,100,105,106) ");

        if(StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if(StringUtils.isNotBlank(orgId)){
            sb.append("and d.org_id in ( "+orgId+") ");
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

    public RecordSet queryPartyInfoByLinkEmployeeId( String roleType, String name, String wxnick, String mobile, String houseaddress,String employeeIds) throws Exception {
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

        if(StringUtils.isNotBlank(employeeIds)){
            sb.append("and a.LINK_EMPLOYEE_ID in ( "+employeeIds+") ");
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


        sb.append(" order by c.create_date desc ");


        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet queryPartyInfoByLinkEmployeeIds(String employeeIds, String roleType, String name,  String mobile, String custservieEmpId) throws Exception {
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

        if(StringUtils.isNotBlank(employeeIds)){
            sb.append("and a.LINK_EMPLOYEE_ID in ( "+employeeIds+") ");
        }

        if (StringUtils.isNotBlank(mobile)) {
            sb.append("and c.MOBILE_NO=:MOBILE_NO ");
            parameter.put("MOBILE_NO", mobile);
        }

        if (StringUtils.isNotBlank(custservieEmpId)) {
            sb.append("and a.LINK_EMPLOYEE_ID=:LINK_EMPLOYEE_ID ");
            parameter.put("LINK_EMPLOYEE_ID", custservieEmpId);
        }


        sb.append(" order by c.create_date desc ");


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

    public ProjectIntentionEntity queryProjectIntentionInfoByProjectId(String project_id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        parameter.put("PROJECT_ID", project_id);
        List<ProjectIntentionEntity> projectIntentionEntities = this.query(ProjectIntentionEntity.class, "ins_project_intention", parameter);

        if (ArrayTool.isEmpty(projectIntentionEntities)) {
            return null;
        }
        return projectIntentionEntities.get(0);
    }

    public RecordSet queryBluePrintByOpenIdAndActionCode(String openid, String action_code) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_blueprint_action ");
        sb.append("where OPEN_ID=:OPEN_ID AND ACTION_CODE=:ACTION_CODE ");
        sb.append("order by create_date ");
        parameter.put("OPEN_ID", openid);
        parameter.put("ACTION_CODE", action_code);
        RecordSet recordSet = queryBySql(sb.toString(), parameter);

        if (recordSet.size() < 0) {
            return null;
        }
        return recordSet;
    }

    public RecordSet queryXQLTEByOpenIdAndActionCode(String openid, String action_code,String empId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_blueprint_action ");
        sb.append("where OPEN_ID=:OPEN_ID AND ACTION_CODE=:ACTION_CODE ");
        sb.append("AND REL_EMPLOYEE_ID = :EMPLOYEE_ID ");
        sb.append("order by XQLTE_CREATE_TIME ");
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
        sb.append("select * from ins_project_linkman a ,ins_project b, ins_party c ");
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

    public RecordSet getCityCabinByCityId(String city_id,String name) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_citycabin  ");
        sb.append(" where BIZ_CITY= :BIZ_CITY ");
        sb.append(" and SCAN_END_TIME > now()  ");
        sb.append(" and SCAN_END_TIME > SCAN_START_TIME  ");

        if(StringUtils.isNotBlank(name)){
            sb.append("and CITYCABIN_ADDRESS like concat('%',:CITYCABIN_ADDRESS,'%') ");
            parameter.put("CITYCABIN_ADDRESS", name);
        }

        parameter.put("BIZ_CITY",city_id);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public RecordSet queryInsScanCityInfoByProIdAndPId(String projectId,String partyId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_scan_citycabin  ");
        sb.append(" where PARTY_ID= :PARTY_ID ");
        sb.append(" and PROJECT_ID= :PROJECT_ID  ");
        sb.append(" order by CREATE_DATE DESC ");
        parameter.put("PARTY_ID",partyId);
        parameter.put("PROJECT_ID",projectId);

        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }


    public RecordSet queryCustServFinishActionInfo(String startDate,String endDate,String employeeIds,String orgIds) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select v.*,s.city_cabins,s.experience_time,s.experience , u.FUNCPRINT_CREATE_TIME,u.STYLEPRINT_CREATE_TIME from ");
        sb.append(" ( ");
        sb.append("SELECT a.WX_NICK,a.OPEN_ID,a.CREATE_DATE,a.PARTY_NAME,c.LINK_EMPLOYEE_ID,a.PARTY_ID,b.PROJECT_ID,d.FINISH_TIME,b.HOUSE_ADDRESS from ");
        sb.append("ins_party a, ins_project b, ins_project_linkman c , ins_project_original_action d ,ins_employee e , ins_employee_job_role f ");
        sb.append("WHERE a.PARTY_ID = b.PARTY_ID ");
        sb.append("AND b.PROJECT_ID = c.PROJECT_ID ");
        sb.append("AND b.PROJECT_ID = d.PROJECT_ID ");
        sb.append("AND c.ROLE_TYPE = 'CUSTOMERSERVICE' ");
        sb.append("AND a.PARTY_STATUS = '0' ");
        sb.append("AND d.ACTION_CODE = 'SMJRLC' ");
        sb.append("AND e.EMPLOYEE_ID=f.EMPLOYEE_ID ");
        sb.append("AND e.STATUS='0' ");
        sb.append("AND NOW() BETWEEN f.START_DATE AND f.END_DATE ");
        sb.append("AND c.LINK_EMPLOYEE_ID=e.EMPLOYEE_ID ");


        if(StringUtils.isNotBlank(startDate)){
            sb.append("and a.CREATE_DATE > :START_DATE ");
            parameter.put("START_DATE", startDate);
        }

        if(StringUtils.isNotBlank(endDate)){
            sb.append("and a.CREATE_DATE < :END_DATE ");
            parameter.put("END_DATE", endDate);
        }

        if(StringUtils.isNotBlank(employeeIds)){
            sb.append("and e.EMPLOYEE_ID IN ("+employeeIds+") ");
        }

        if(StringUtils.isNotBlank(orgIds)){
            sb.append("and f.org_id in ( "+orgIds+") ");
        }

        sb.append(" order by c.LINK_EMPLOYEE_ID, a.CREATE_DATE desc ");

        sb.append(" ) v");
        sb.append(" left join (select * from ins_scan_citycabin x where x.SCAN_ID in (select min(y.scan_id) from ins_scan_citycabin y group by y.PARTY_ID))  s on (s.PARTY_ID = v.PARTY_ID) ");
        sb.append(" left join (select * from ins_blueprint_action r where r.BLUEPRINT_ACTION_ID in (select min(t.BLUEPRINT_ACTION_ID) from ins_blueprint_action t where t.ACTION_CODE='XQLTE' group by t.OPEN_ID, t.REL_EMPLOYEE_ID)) u on (u.OPEN_ID = v.OPEN_ID and u.REL_EMPLOYEE_ID = v.LINK_EMPLOYEE_ID) ");


        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public Record getCityCabinById(String id) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_citycabin  ");
        sb.append(" where CITY_CABIN_ID= :ID ");
        parameter.put("ID",id);
        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        if(recordSet.size()<0 || recordSet ==null){
            return null;
        }
        return recordSet.get(0);
    }

    public RecordSet queryCustServMonStatInfo(String employeeId,String monDate) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_employee a,stat_custservice_month b ");
        sb.append("where a.EMPLOYEE_ID=b.object_id ");
        sb.append("and b.stat_month= :MONDATE  ");

        parameter.put("MONDATE",monDate);

        if(StringUtils.isNotBlank(employeeId)){
            sb.append("and b.OBJECT_ID IN ("+employeeId+") ");
        }


        RecordSet recordSet = this.queryBySql(sb.toString(), parameter);
        return recordSet;
    }

    public RecordSet queryPartyInfoForCustClear(String partyId,String projectId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party a, ins_project b ,ins_project_linkman c ");
        sb.append(" where a.PARTY_ID=b.PARTY_ID ");
        sb.append(" and b.PROJECT_ID=c.PROJECT_ID ");
        sb.append(" and a.PARTY_ID = :PARTY_ID");
        sb.append(" and b.PROJECT_ID = :PROJECT_ID");
        parameter.put("PARTY_ID",partyId);
        parameter.put("PROJECT_ID",projectId);
        return this.queryBySql(sb.toString(),parameter);
    }

    public RecordSet queryCustClearInfo(String partyId,String status,String applyEmpId,String aduitEmpId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party_clear a where 1=1 ");

        if(StringUtils.isNotBlank(partyId)){
            sb.append("and a.PARTY_ID IN ("+partyId+") ");
        }

        if(StringUtils.isNotBlank(status)){
            sb.append("and a.STATUS IN ("+status+") ");
        }

        if(StringUtils.isNotBlank(applyEmpId)){
            sb.append("and a.APPLY_EMPLOYEE_ID IN ("+applyEmpId+") ");
        }

        if(StringUtils.isNotBlank(aduitEmpId)){
            sb.append("and a.AUDIT_EMPLOYEE_ID IN ("+aduitEmpId+") ");
        }

        if(StringUtils.isNotBlank(aduitEmpId)){
            sb.append("and a.APPLY_EMPLOYEE_ID IN ("+aduitEmpId+") ");
        }


        return this.queryBySql(sb.toString(),parameter);
    }


    public RecordSet queryPartyVisitInfo(String partyId,String employeeId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party_visit a where 1=1 ");

        if(StringUtils.isNotBlank(partyId)){
            sb.append("and a.PARTY_ID IN ("+partyId+") ");
        }


        if(StringUtils.isNotBlank(employeeId)){
            sb.append("and a.VISIT_EMPLOYEE_ID IN ("+employeeId+") ");
        }

        sb.append("order by create_date desc ");

        return this.queryBySql(sb.toString(),parameter);
    }

    public RecordSet queryChildEmpByEmpIdsAndName(String employeeIds,String name,String orgId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city, f.NAME parent_org_name  ");
        sb.append("from ins_user a, ins_employee b , ");
        sb.append("ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");

        if(StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if(StringUtils.isNotBlank(orgId)){
            sb.append("and d.org_id in ( "+orgId+") ");
        }

        if(StringUtils.isNotBlank(employeeIds)){
            sb.append("and b.employee_id in ( "+employeeIds+") ");
        }

        return this.queryBySql(sb.toString(),parameter);
    }

    public RecordSet queryAllCustServiceByName(String name,String orgId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();

        sb.append("select a.USER_ID,b.NAME,b.employee_id,b.sex,a.mobile_no contact_no, d.JOB_ROLE, e.name org_name, e.city, f.NAME parent_org_name  ");
        sb.append("from ins_user a, ins_employee b , ");
        sb.append("ins_employee_job_role d,ins_org e ");
        sb.append("left join ins_org f on(f.ORG_ID = e.PARENT_ORG_ID) ");
        sb.append("where b.USER_ID = a.USER_ID ");
        sb.append("and d.EMPLOYEE_ID = b.EMPLOYEE_ID ");
        sb.append("and a.status = '0' ");
        sb.append("and b.status = '0' " );
        sb.append("and now() < d.end_date ");
        sb.append("and e.ORG_ID = d.ORG_ID ");
        sb.append("and d.job_role in ('46','118','0','69','119') ");

        if(StringUtils.isNotBlank(name)) {
            sb.append("and b.name like concat('%',:NAME,'%') ");
            parameter.put("NAME", name);
        }

        if(StringUtils.isNotBlank(orgId)){
            sb.append("and d.org_id in ( "+orgId+") ");
        }



        return this.queryBySql(sb.toString(),parameter);
    }
}