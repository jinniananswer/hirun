package com.hirun.pub.domain.entity.org;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/23 22:09
 * @Description:
 */
public class EmployeeJobRoleEntity extends GenericEntity {

    public EmployeeJobRoleEntity(Map<String, String> data){
        super(data);
    }

    public EmployeeJobRoleEntity(){
        super();
    }

    public String getJobRoleId(){
        return this.get("JOB_ROLE_ID");
    }

    public void setJobRoleId(String jobRoleId){
        this.set("JOB_ROLE_ID", jobRoleId);
    }

    public String getEmployeeId(){
        return this.get("EMPLOYEE_ID");
    }

    public void setEmployeeId(String employeeId){
        this.set("EMPLOYEE_ID", employeeId);
    }

    public String getJobRole(){
        return this.get("JOB_ROLE");
    }

    public void setJobRole(String jobRole){
        this.set("JOB_ROLE", jobRole);
    }

    public String getOrgId(){
        return this.get("ORG_ID");
    }

    public void setOrgId(String orgId){
        this.set("ORG_ID", orgId);
    }

    public String getParentEmployeeId(){
        return this.get("PARENT_EMPLOYEE_ID");
    }

    public void setParentEmployeeId(String parentEmployeeId){
        this.set("PARENT_EMPLOYEE_ID", parentEmployeeId);
    }

    public String getStartDate(){
        return this.get("START_DATE");
    }

    public void setStartDate(String startDate){
        this.set("START_DATE", startDate);
    }

    public String getEndDate(){
        return this.get("END_DATE");
    }

    public void setEndDate(String endDate){
        this.set("END_DATE", endDate);
    }

    public String getRemark(){
        return this.get("REMARK");
    }

    public void setRemark(String remark){
        this.set("REMARK", remark);
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
