package com.hirun.pub.domain.entity.plan;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class MonPlanTargetEntity extends GenericEntity{

	public MonPlanTargetEntity(){
		super();
	}

	public MonPlanTargetEntity(Map<String, String> data){
		super(data);
	}
	
	public String getId(){
		return this.get("ID");
	}

	public void setId(String id){
		this.set("ID", id);
	}
	
	public String getEffectObjType(){
		return this.get("EFFECT_OBJ_TYPE");
	}

	public void setEffectObjType(String effectObjType){
		this.set("EFFECT_OBJ_TYPE", effectObjType);
	}
	
	public String getEffectObj(){
		return this.get("EFFECT_OBJ");
	}

	public void setEffectObj(String effectObj){
		this.set("EFFECT_OBJ", effectObj);
	}
	
	public String getTargetTimeType(){
		return this.get("TARGET_TIME_TYPE");
	}

	public void setTargetTimeType(String targetTimeType){
		this.set("TARGET_TIME_TYPE", targetTimeType);
	}
	
	public String getTargetTimeValue(){
		return this.get("TARGET_TIME_VALUE");
	}

	public void setTargetTimeValue(String targetTimeValue){
		this.set("TARGET_TIME_VALUE", targetTimeValue);
	}
	
	public String getTargetValue(){
		return this.get("TARGET_VALUE");
	}

	public void setTargetValue(String targetValue){
		this.set("TARGET_VALUE", targetValue);
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