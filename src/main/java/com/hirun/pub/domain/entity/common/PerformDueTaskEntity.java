package com.hirun.pub.domain.entity.common;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PerformDueTaskEntity extends GenericEntity{

	public PerformDueTaskEntity(){
		super();
	}

	public PerformDueTaskEntity(Map<String, String> data){
		super(data);
	}
	
	public String getTaskId(){
		return this.get("TASK_ID");
	}

	public void setTaskId(String taskId){
		this.set("TASK_ID", taskId);
	}
	
	public String getTaskType(){
		return this.get("TASK_TYPE");
	}

	public void setTaskType(String taskType){
		this.set("TASK_TYPE", taskType);
	}
	
	public String getObjectId(){
		return this.get("OBJECT_ID");
	}

	public void setObjectId(String objectId){
		this.set("OBJECT_ID", objectId);
	}
	
	public String getObjectType(){
		return this.get("OBJECT_TYPE");
	}

	public void setObjectType(String objectType){
		this.set("OBJECT_TYPE", objectType);
	}
	
	public String getExecTime(){
		return this.get("EXEC_TIME");
	}

	public void setExecTime(String execTime){
		this.set("EXEC_TIME", execTime);
	}
	
	public String getDealTag(){
		return this.get("DEAL_TAG");
	}

	public void setDealTag(String dealTag){
		this.set("DEAL_TAG", dealTag);
	}
	
	public String getParams(){
		return this.get("PARAMS");
	}

	public void setParams(String params){
		this.set("PARAMS", params);
	}
	
	public String getResultCode(){
		return this.get("RESULT_CODE");
	}

	public void setResultCode(String resultCode){
		this.set("RESULT_CODE", resultCode);
	}
	
	public String getResultInfo(){
		return this.get("RESULT_INFO");
	}

	public void setResultInfo(String resultInfo){
		this.set("RESULT_INFO", resultInfo);
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