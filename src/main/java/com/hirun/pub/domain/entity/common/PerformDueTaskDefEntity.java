package com.hirun.pub.domain.entity.common;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PerformDueTaskDefEntity extends GenericEntity{

	public PerformDueTaskDefEntity(){
		super();
	}

	public PerformDueTaskDefEntity(Map<String, String> data){
		super(data);
	}
	
	public String getId(){
		return this.get("ID");
	}

	public void setId(String id){
		this.set("ID", id);
	}
	
	public String getTaskType(){
		return this.get("TASK_TYPE");
	}

	public void setTaskType(String taskType){
		this.set("TASK_TYPE", taskType);
	}
	
	public String getTaskName(){
		return this.get("TASK_NAME");
	}

	public void setTaskName(String taskName){
		this.set("TASK_NAME", taskName);
	}
	
	public String getTaskDesc(){
		return this.get("TASK_DESC");
	}

	public void setTaskDesc(String taskDesc){
		this.set("TASK_DESC", taskDesc);
	}
	
	public String getStatus(){
		return this.get("STATUS");
	}

	public void setStatus(String status){
		this.set("STATUS", status);
	}
	
	public String getServiceName(){
		return this.get("SERVICE_NAME");
	}

	public void setServiceName(String serviceName){
		this.set("SERVICE_NAME", serviceName);
	}
	
}