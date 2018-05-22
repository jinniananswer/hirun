package com.hirun.pub.domain.out;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class DataGetTimeEntity extends GenericEntity{

	public DataGetTimeEntity(){
		super();
	}

	public DataGetTimeEntity(Map<String, String> data){
		super(data);
	}
	
	public String getId(){
		return this.get("ID");
	}

	public void setId(String id){
		this.set("ID", id);
	}
	
	public String getGetType(){
		return this.get("GET_TYPE");
	}

	public void setGetType(String getType){
		this.set("GET_TYPE", getType);
	}
	
	public String getIndbTime(){
		return this.get("INDB_TIME");
	}

	public void setIndbTime(String indbTime){
		this.set("INDB_TIME", indbTime);
	}
	
}