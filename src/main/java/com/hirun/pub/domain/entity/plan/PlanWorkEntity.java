package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PlanWorkEntity extends GenericEntity{

	public PlanWorkEntity(){
		super();
	}

	public PlanWorkEntity(Map<String, String> data){
		super(data);
	}
	
	public String getPlanWorkId(){
		return this.get("PLAN_WORK_ID");
	}

	public void setPlanWorkId(String planWorkId){
		this.set("PLAN_WORK_ID", planWorkId);
	}
	
	public String getPlanId(){
		return this.get("PLAN_ID");
	}

	public void setPlanId(String planId){
		this.set("PLAN_ID", planId);
	}
	
	public String getWorkMode(){
		return this.get("WORK_MODE");
	}

	public void setWorkMode(String workMode){
		this.set("WORK_MODE", workMode);
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
	
}