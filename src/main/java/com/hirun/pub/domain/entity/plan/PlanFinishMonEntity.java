package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PlanFinishMonEntity extends GenericEntity{

	public PlanFinishMonEntity(){
		super();
	}

	public PlanFinishMonEntity(Map<String, String> data){
		super(data);
	}
	
	public String getStatMon(){
		return this.get("STAT_MON");
	}

	public void setStatMon(String statMon){
		this.set("STAT_MON", statMon);
	}
	
	public String getEmployeeId(){
		return this.get("EMPLOYEE_ID");
	}

	public void setEmployeeId(String employeeId){
		this.set("EMPLOYEE_ID", employeeId);
	}
	
	public String getStatResult(){
		return this.get("STAT_RESULT");
	}

	public void setStatResult(String statResult){
		this.set("STAT_RESULT", statResult);
	}
	
}