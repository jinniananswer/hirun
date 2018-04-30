package com.hirun.pub.domain.entity.param;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PlanUnfinishCauseEntity extends GenericEntity{

	public PlanUnfinishCauseEntity(){
		super();
	}

	public PlanUnfinishCauseEntity(Map<String, String> data){
		super(data);
	}
	
	public String getCauseId(){
		return this.get("CAUSE_ID");
	}

	public void setCauseId(String causeId){
		this.set("CAUSE_ID", causeId);
	}
	
	public String getCauseName(){
		return this.get("CAUSE_NAME");
	}

	public void setCauseName(String causeName){
		this.set("CAUSE_NAME", causeName);
	}
	
	public String getActionCode(){
		return this.get("ACTION_CODE");
	}

	public void setActionCode(String actionCode){
		this.set("ACTION_CODE", actionCode);
	}
	
	public String getAfterDeal(){
		return this.get("AFTER_DEAL");
	}

	public void setAfterDeal(String afterDeal){
		this.set("AFTER_DEAL", afterDeal);
	}
	
	public String getStatus(){
		return this.get("STATUS");
	}

	public void setStatus(String status){
		this.set("STATUS", status);
	}
	
}