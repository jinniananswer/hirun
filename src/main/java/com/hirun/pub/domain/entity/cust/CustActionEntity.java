package com.hirun.pub.domain.entity.cust;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class CustActionEntity extends GenericEntity{

    public CustActionEntity(){
        super();
    }

    public CustActionEntity(Map<String, String> data){
        super(data);
    }

    public String getActionId(){
        return this.get("ACTION_ID");
    }

    public void setActionId(String actionId){
        this.set("ACTION_ID", actionId);
    }

    public String getCustId(){
        return this.get("CUST_ID");
    }

    public void setCustId(String custId){
        this.set("CUST_ID", custId);
    }

    public String getActionCode(){
        return this.get("ACTION_CODE");
    }

    public void setActionCode(String actionCode){
        this.set("ACTION_CODE", actionCode);
    }

    public String getPlanId(){
        return this.get("PLAN_ID");
    }

    public void setPlanId(String planId){
        this.set("PLAN_ID", planId);
    }

    public String getActionStatus(){
        return this.get("ACTION_STATUS");
    }

    public void setActionStatus(String actionStatus){
        this.set("ACTION_STATUS", actionStatus);
    }

    public String getPlanDealDate(){
        return this.get("PLAN_DEAL_DATE");
    }

    public void setPlanDealDate(String planDealDate){
        this.set("PLAN_DEAL_DATE", planDealDate);
    }

    public String getFinishTime(){
        return this.get("FINISH_TIME");
    }

    public void setFinishTime(String finishTime){
        this.set("FINISH_TIME", finishTime);
    }

    public String getExecutorId(){
        return this.get("EXECUTOR_ID");
    }

    public void setExecutorId(String executorId){
        this.set("EXECUTOR_ID", executorId);
    }

    public String getUnfinishCauseId(){
        return this.get("UNFINISH_CAUSE_ID");
    }

    public void setUnfinishCauseId(String unfinishCauseId){
        this.set("UNFINISH_CAUSE_ID", unfinishCauseId);
    }

    public String getUnfinishCauseDesc(){
        return this.get("UNFINISH_CAUSE_DESC");
    }

    public void setUnfinishCauseDesc(String unfinishCauseDesc){
        this.set("UNFINISH_CAUSE_DESC", unfinishCauseDesc);
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