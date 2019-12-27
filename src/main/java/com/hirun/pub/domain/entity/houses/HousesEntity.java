package com.hirun.pub.domain.entity.houses;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class HousesEntity extends GenericEntity{

	public HousesEntity(){
		super();
	}

	public HousesEntity(Map<String, String> data){
		super(data);
	}
	
	public String getHousesId(){
		return this.get("HOUSES_ID");
	}

	public void setHousesId(String housesId){
		this.set("HOUSES_ID", housesId);
	}
	
	public String getName(){
		return this.get("NAME");
	}

	public void setName(String name){
		this.set("NAME", name);
	}
	
	public String getCity(){
		return this.get("CITY");
	}

	public void setCity(String city){
		this.set("CITY", city);
	}
	
	public String getArea(){
		return this.get("AREA");
	}

	public void setArea(String area){
		this.set("AREA", area);
	}
	
	public String getOrgId(){
		return this.get("ORG_ID");
	}

	public void setOrgId(String orgId){
		this.set("ORG_ID", orgId);
	}
	
	public String getNature(){
		return this.get("NATURE");
	}

	public void setNature(String nature){
		this.set("NATURE", nature);
	}
	
	public String getCheckDate(){
		return this.get("CHECK_DATE");
	}

	public void setCheckDate(String checkDate){
		this.set("CHECK_DATE", checkDate);
	}
	
	public String getHouseNum(){
		return this.get("HOUSE_NUM");
	}

	public void setHouseNum(String houseNum){
		this.set("HOUSE_NUM", houseNum);
	}
	
	public String getPlanCounselorNum(){
		return this.get("PLAN_COUNSELOR_NUM");
	}

	public void setPlanCounselorNum(String planCounselorNum){
		this.set("PLAN_COUNSELOR_NUM", planCounselorNum);
	}
	
	public String getPlanInDate(){
		return this.get("PLAN_IN_DATE");
	}

	public void setPlanInDate(String planInDate){
		this.set("PLAN_IN_DATE", planInDate);
	}
	
	public String getActualInDate(){
		return this.get("ACTUAL_IN_DATE");
	}

	public void setActualInDate(String actualInDate){
		this.set("ACTUAL_IN_DATE", actualInDate);
	}
	
	public String getDestroyDate(){
		return this.get("DESTROY_DATE");
	}

	public void setDestroyDate(String destroyDate){
		this.set("DESTROY_DATE", destroyDate);
	}
	
	public String getStatus(){
		return this.get("STATUS");
	}

	public void setStatus(String status){
		this.set("STATUS", status);
	}
	
	public String getRemark(){
		return this.get("REMARK");
	}

	public void setRemark(String remark){
		this.set("REMARK", remark);
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