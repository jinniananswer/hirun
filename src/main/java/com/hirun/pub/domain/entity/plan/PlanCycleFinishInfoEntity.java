package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PlanCycleFinishInfoEntity extends GenericEntity{

	public PlanCycleFinishInfoEntity(){
		super();
	}

	public PlanCycleFinishInfoEntity(Map<String, String> data){
		super(data);
	}
	
	public String getLogId(){
		return this.get("LOG_ID");
	}

	public void setLogId(String logId){
		this.set("LOG_ID", logId);
	}
	
	public String getExecutorId(){
		return this.get("EXECUTOR_ID");
	}

	public void setExecutorId(String executorId){
		this.set("EXECUTOR_ID", executorId);
	}
	
	public String getActionCode(){
		return this.get("ACTION_CODE");
	}

	public void setActionCode(String actionCode){
		this.set("ACTION_CODE", actionCode);
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
	
	public String getCurrCycleFinishNum(){
		return this.get("CURR_CYCLE_FINISH_NUM");
	}

	public void setCurrCycleFinishNum(String currCycleFinishNum){
		this.set("CURR_CYCLE_FINISH_NUM", currCycleFinishNum);
	}
	
	public String getCurrCycleImproperDays(){
		return this.get("CURR_CYCLE_IMPROPER_DAYS");
	}

	public void setCurrCycleImproperDays(String currCycleImproperDays){
		this.set("CURR_CYCLE_IMPROPER_DAYS", currCycleImproperDays);
	}
	
}