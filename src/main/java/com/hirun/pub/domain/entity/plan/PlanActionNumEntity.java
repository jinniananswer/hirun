package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PlanActionNumEntity extends GenericEntity{

	public PlanActionNumEntity(){
		super();
	}

	public PlanActionNumEntity(Map<String, String> data){
		super(data);
	}
	
	public String getPlanActionId(){
		return this.get("PLAN_ACTION_ID");
	}

	public void setPlanActionId(String planActionId){
		this.set("PLAN_ACTION_ID", planActionId);
	}
	
	public String getPlanId(){
		return this.get("PLAN_ID");
	}

	public void setPlanId(String planId){
		this.set("PLAN_ID", planId);
	}
	
	public String getPlanDate(){
		return this.get("PLAN_DATE");
	}

	public void setPlanDate(String planDate){
		this.set("PLAN_DATE", planDate);
	}
	
	public String getActonCode(){
		return this.get("ACTON_CODE");
	}

	public void setActonCode(String actonCode){
		this.set("ACTON_CODE", actonCode);
	}
	
	public String getNum(){
		return this.get("NUM");
	}

	public void setNum(String num){
		this.set("NUM", num);
	}
	
}