package com.hirun.pub.domain.entity.cust;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class CustChangeRelaEmployeeLogEntity extends GenericEntity{

	public CustChangeRelaEmployeeLogEntity(){
		super();
	}

	public CustChangeRelaEmployeeLogEntity(Map<String, String> data){
		super(data);
	}
	
	public String getLogId(){
		return this.get("LOG_ID");
	}

	public void setLogId(String logId){
		this.set("LOG_ID", logId);
	}
	
	public String getCustId(){
		return this.get("CUST_ID");
	}

	public void setCustId(String custId){
		this.set("CUST_ID", custId);
	}
	
	public String getJobRole(){
		return this.get("JOB_ROLE");
	}

	public void setJobRole(String jobRole){
		this.set("JOB_ROLE", jobRole);
	}
	
	public String getOldEmployeeId(){
		return this.get("OLD_EMPLOYEE_ID");
	}

	public void setOldEmployeeId(String oldEmployeeId){
		this.set("OLD_EMPLOYEE_ID", oldEmployeeId);
	}
	
	public String getNewEmployeeId(){
		return this.get("NEW_EMPLOYEE_ID");
	}

	public void setNewEmployeeId(String newEmployeeId){
		this.set("NEW_EMPLOYEE_ID", newEmployeeId);
	}
	
	public String getChangeReason(){
		return this.get("CHANGE_REASON");
	}

	public void setChangeReason(String changeReason){
		this.set("CHANGE_REASON", changeReason);
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
	
}