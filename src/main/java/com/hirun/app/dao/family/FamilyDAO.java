package com.hirun.app.dao.family;


import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.StrongObjectDAO;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


@DatabaseName("ins")
public class FamilyDAO extends StrongObjectDAO {

    public FamilyDAO(String databaseName) {
        super(databaseName);
    }

    public RecordSet queryParty4CreateFamily(String employeeIds, String roleType, String name, String mobile, String custservieEmpId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_project_linkman a ,ins_project b, ins_party c ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= :ROLE_TYPE  ");
        sb.append(" and c.PARTY_STATUS='0'  ");
        sb.append(" and not EXISTS (select 1 from ins_party_family f where f.party_id=c.party_id and f.end_date > now() )  ");

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


        sb.append(" order by c.create_time  desc ");


        parameter.put("ROLE_TYPE", roleType);
        return this.queryBySql(sb.toString(), parameter);
    }


    public RecordSet queryPartyInfo(String partyId,String projectId) throws Exception{
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

    public RecordSet getGroupIdByPartyId(String partyId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party_family a ");
        sb.append(" where a.PARTY_ID = :PARTY_ID");
        sb.append(" and a.end_date > now() ");
        parameter.put("PARTY_ID",partyId);
        return this.queryBySql(sb.toString(),parameter);
    }


    public RecordSet queryFamilyMemberInfo( String roleType, String partyId) throws Exception {
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select c.*,a.link_employee_id,b.project_id from  ins_project_linkman a ,ins_project b, ins_party c  ");
        sb.append(" where a.PROJECT_ID=b.PROJECT_ID ");
        sb.append(" and b.PARTY_ID=c.PARTY_ID  ");
        sb.append(" and a.ROLE_TYPE= :ROLE_TYPE  ");

        if(StringUtils.isNotBlank(partyId)){
            sb.append("and c.party_id in ( "+partyId+") ");
        }

        sb.append(" order by c.create_time  desc ");

        parameter.put("ROLE_TYPE", roleType);

        return this.queryBySql(sb.toString(), parameter);
    }

    public RecordSet getAllMember(String groupId) throws Exception{
        Map<String, String> parameter = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ins_party_family a ");
        sb.append(" where a.GROUP_ID = :GROUP_ID");
        sb.append(" and a.end_date > now() ");
        parameter.put("GROUP_ID",groupId);
        return this.queryBySql(sb.toString(),parameter);
    }

}