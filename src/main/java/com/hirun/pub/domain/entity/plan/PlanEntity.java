package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PlanEntity extends GenericEntity{

	public PlanEntity(){
		super();
	}

	public PlanEntity(Map<String, String> data){
		super(data);
	}
	
	public String getPlanId(){
		return this.get("PLAN_ID");
	}

	public void setPlanId(String planId){
		this.set("PLAN_ID", planId);
	}
	
	public String getPlanName(){
		return this.get("PLAN_NAME");
	}

	public void setPlanName(String planName){
		this.set("PLAN_NAME", planName);
	}
	
	public String getPlanDate(){
		return this.get("PLAN_DATE");
	}

	public void setPlanDate(String planDate){
		this.set("PLAN_DATE", planDate);
	}
	
	public String getPlanType(){
		return this.get("PLAN_TYPE");
	}

	public void setPlanType(String planType){
		this.set("PLAN_TYPE", planType);
	}
	
	public String getPlanStatus(){
		return this.get("PLAN_STATUS");
	}

	public void setPlanStatus(String planStatus){
		this.set("PLAN_STATUS", planStatus);
	}
	
	public String getPlanMode(){
		return this.get("PLAN_MODE");
	}

	public void setPlanMode(String planMode){
		this.set("PLAN_MODE", planMode);
	}
	
	public String getIsAdditionalRecord(){
		return this.get("IS_ADDITIONAL_RECORD");
	}

	public void setIsAdditionalRecord(String isAdditionalRecord){
		this.set("IS_ADDITIONAL_RECORD", isAdditionalRecord);
	}
	
	public String getPlanExecutorId(){
		return this.get("PLAN_EXECUTOR_ID");
	}

	public void setPlanExecutorId(String planExecutorId){
		this.set("PLAN_EXECUTOR_ID", planExecutorId);
	}
	
	public String getCurrPlanApperId(){
		return this.get("CURR_PLAN_APPER_ID");
	}

	public void setCurrPlanApperId(String currPlanApperId){
		this.set("CURR_PLAN_APPER_ID", currPlanApperId);
	}
	
	public String getLastApproveTime(){
		return this.get("LAST_APPROVE_TIME");
	}

	public void setLastApproveTime(String lastApproveTime){
		this.set("LAST_APPROVE_TIME", lastApproveTime);
	}
	
	public String getCreateUserId(){
		return this.get("CREATE_USER_ID");
	}

	public void setCreateUserId(String createUserId){
		this.set("CREATE_USER_ID", createUserId);
	}
	
	public String getCreateTime(){
		return this.get("CREATE_TIME");
	}

	public void setCreateTime(String createTime){
		this.set("CREATE_TIME", createTime);
	}
	
	public String getSummarizeUserId(){
		return this.get("SUMMARIZE_USER_ID");
	}

	public void setSummarizeUserId(String summarizeUserId){
		this.set("SUMMARIZE_USER_ID", summarizeUserId);
	}
	
	public String getSummarizeDate(){
		return this.get("SUMMARIZE_DATE");
	}

	public void setSummarizeDate(String summarizeDate){
		this.set("SUMMARIZE_DATE", summarizeDate);
	}
	
	public String getIsAdditionalRecordSummarize(){
		return this.get("IS_ADDITIONAL_RECORD_SUMMARIZE");
	}

	public void setIsAdditionalRecordSummarize(String isAdditionalRecordSummarize){
		this.set("IS_ADDITIONAL_RECORD_SUMMARIZE", isAdditionalRecordSummarize);
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