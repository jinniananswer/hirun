package com.hirun.pub.domain.entity.custservice;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PartyOriginalActionEntity extends GenericEntity{

	public PartyOriginalActionEntity(){
		super();
	}

	public PartyOriginalActionEntity(Map<String, String> data){
		super(data);
	}
	
	public String getActionId(){
		return this.get("ACTION_ID");
	}

	public void setActionId(String actionId){
		this.set("ACTION_ID", actionId);
	}
	
	public String getPartyId(){
		return this.get("PARTY_ID");
	}

	public void setPartyId(String partyId){
		this.set("PARTY_ID", partyId);
	}


	public void setStatus(String status){
		this.set("STATUS", status);
	}

	public String getStatus(){
		return this.get("STATUS");
	}


	public String getActionCode(){
		return this.get("ACTION_CODE");
	}

	public void setActionCode(String actionCode){
		this.set("ACTION_CODE", actionCode);
	}
	
	public String getFinishTime(){
		return this.get("FINISH_TIME");
	}

	public void setFinishTime(String finishTime){
		this.set("FINISH_TIME", finishTime);
	}
	
	public String getEmployeeId(){
		return this.get("EMPLOYEE_ID");
	}

	public void setEmployeeId(String employeeId){
		this.set("EMPLOYEE_ID", employeeId);
	}
	
	public String getOutId(){
		return this.get("OUT_ID");
	}

	public void setOutId(String outId){
		this.set("OUT_ID", outId);
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
	
}