package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class CyclePlanFinishInfoEntity extends GenericEntity{

	public CyclePlanFinishInfoEntity(){
		super();
	}

	public CyclePlanFinishInfoEntity(Map<String, String> data){
		super(data);
	}
	
	public String getLogId(){
		return this.get("LOG_ID");
	}

	public void setLogId(String logId){
		this.set("LOG_ID", logId);
	}
	
	public String getEmployeeId(){
		return this.get("EMPLOYEE_ID");
	}

	public void setEmployeeId(String employeeId){
		this.set("EMPLOYEE_ID", employeeId);
	}
	
	public String getPreCycleEndDate(){
		return this.get("PRE_CYCLE_END_DATE");
	}

	public void setPreCycleEndDate(String preCycleEndDate){
		this.set("PRE_CYCLE_END_DATE", preCycleEndDate);
	}
	
	public String getUnfinishNum(){
		return this.get("UNFINISH_NUM");
	}

	public void setUnfinishNum(String unfinishNum){
		this.set("UNFINISH_NUM", unfinishNum);
	}

	public String getActionCode(){
		return this.get("ACTION_CODE");
	}

	public void setActionCode(String actionCode){
		this.set("ACTION_CODE", actionCode);
	}
	
}