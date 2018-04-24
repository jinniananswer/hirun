package com.hirun.pub.domain.entity.org;

import com.most.core.pub.data.GenericEntity;

/**
 * @Author jinnian
 * @Date 2018/4/23 22:23
 * @Description:
 */
public class OrgEntity extends GenericEntity {

    public String getOrgId(){
        return this.get("ORG_ID");
    }

    public void setOrgId(String orgId){
        this.set("ORG_ID", orgId);
    }

    public String getName(){
        return this.get("NAME");
    }

    public void setName(String name){
        this.set("NAME", name);
    }

    public String getContactNo(){
        return this.getContactNo();
    }

    public void setContactNo(String contactNo){
        this.set("CONTACT_NO", contactNo);
    }

    public String getEnterpriseId(){
        return this.get("ENTERPRISE_ID");
    }

    public void setEnterpriseId(String enterpriseId){
        this.set("ENTERPRISE_ID",enterpriseId);
    }

    public String getParentOrgId(){
        return this.get("PARENT_ORG_ID");
    }

    public void setParentOrgId(String parentOrgId){
        this.set("PARENT_ORG_ID", parentOrgId);
    }

    public String getStatus(){
        return this.get("STATUS");
    }

    public void setStatus(String status){
        this.set("STATUS", status);
    }

    public String getCreateUserId(){
        return this.get("CREATE_USER_ID");
    }

    public void setCreateUserId(String createUserId){
        this.set("CREATE_USER_ID", createUserId);
    }

    public String getCreateDate(){
        return this.get("CREATE_DATE");
    }

    public void setCreateDate(String createDate){
        this.set("CREATE_DATE", createDate);
    }

    public String getUpdateUserId(){
        return this.get("UPDATE_USER_ID");
    }

    public void setUpdateUserId(String updateUserId){
        this.set("UPDATE_USER_ID", updateUserId);
    }

    public String getUpdateTime(){
        return this.get("UPDATE_TIME");
    }

    public void setUpdateTime(String updateTime){
        this.set("UPDATE_TIME", updateTime);
    }
}
