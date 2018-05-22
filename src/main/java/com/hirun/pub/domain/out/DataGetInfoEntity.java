package com.hirun.pub.domain.out;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class DataGetInfoEntity extends GenericEntity{

	public DataGetInfoEntity(){
		super();
	}

	public DataGetInfoEntity(Map<String, String> data){
		super(data);
	}
	
	public String getOutGetId(){
		return this.get("OUT_GET_ID");
	}

	public void setOutGetId(String outGetId){
		this.set("OUT_GET_ID", outGetId);
	}
	
	public String getApi(){
		return this.get("API");
	}

	public void setApi(String api){
		this.set("API", api);
	}
	
	public String getRequestData(){
		return this.get("REQUEST_DATA");
	}

	public void setRequestData(String requestData){
		this.set("REQUEST_DATA", requestData);
	}
	
	public String getIndbTime(){
		return this.get("INDB_TIME");
	}

	public void setIndbTime(String indbTime){
		this.set("INDB_TIME", indbTime);
	}
	
}