package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PlanDayEntity extends GenericEntity{

	public PlanDayEntity(){
		super();
	}

	public PlanDayEntity(Map<String, String> data){
		super(data);
	}
	
	public String getStatDay(){
		return this.get("STAT_DAY");
	}

	public void setStatDay(String statDay){
		this.set("STAT_DAY", statDay);
	}
	
	public String getStatType(){
		return this.get("STAT_TYPE");
	}

	public void setStatType(String statType){
		this.set("STAT_TYPE", statType);
	}
	
	public String getObjectId(){
		return this.get("OBJECT_ID");
	}

	public void setObjectId(String objectId){
		this.set("OBJECT_ID", objectId);
	}
	
	public String getStatResult(){
		return this.get("STAT_RESULT");
	}

	public void setStatResult(String statResult){
		this.set("STAT_RESULT", statResult);
	}
	
}