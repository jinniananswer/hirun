package com.hirun.pub.domain.entity.cust;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class CustomerEntity extends GenericEntity{

	public CustomerEntity(){
		super();
	}

	public CustomerEntity(Map<String, String> data){
		super(data);
	}
	
	public String getCustId(){
		return this.get("CUST_ID");
	}

	public void setCustId(String custId){
		this.set("CUST_ID", custId);
	}
	
	public String getIdentifyCode(){
		return this.get("IDENTIFY_CODE");
	}

	public void setIdentifyCode(String identifyCode){
		this.set("IDENTIFY_CODE", identifyCode);
	}
	
	public String getCustName(){
		return this.get("CUST_NAME");
	}

	public void setCustName(String custName){
		this.set("CUST_NAME", custName);
	}
	
	public String getSex(){
		return this.get("SEX");
	}

	public void setSex(String sex){
		this.set("SEX", sex);
	}
	
	public String getWxNick(){
		return this.get("WX_NICK");
	}

	public void setWxNick(String wxNick){
		this.set("WX_NICK", wxNick);
	}
	
	public String getCustStatus(){
		return this.get("CUST_STATUS");
	}

	public void setCustStatus(String custStatus){
		this.set("CUST_STATUS", custStatus);
	}
	
	public String getHouseId(){
		return this.get("HOUSE_ID");
	}

	public void setHouseId(String houseId){
		this.set("HOUSE_ID", houseId);
	}
	
	public String getMobileNo(){
		return this.get("MOBILE_NO");
	}

	public void setMobileNo(String mobileNo){
		this.set("MOBILE_NO", mobileNo);
	}
	
	public String getHouseDetail(){
		return this.get("HOUSE_DETAIL");
	}

	public void setHouseDetail(String houseDetail){
		this.set("HOUSE_DETAIL", houseDetail);
	}
	
	public String getHouseMode(){
		return this.get("HOUSE_MODE");
	}

	public void setHouseMode(String houseMode){
		this.set("HOUSE_MODE", houseMode);
	}
	
	public String getHouseArea(){
		return this.get("HOUSE_AREA");
	}

	public void setHouseArea(String houseArea){
		this.set("HOUSE_AREA", houseArea);
	}
	
	public String getCustDetail(){
		return this.get("CUST_DETAIL");
	}

	public void setCustDetail(String custDetail){
		this.set("CUST_DETAIL", custDetail);
	}
	
	public String getLastAction(){
		return this.get("LAST_ACTION");
	}

	public void setLastAction(String lastAction){
		this.set("LAST_ACTION", lastAction);
	}
	
	public String getLastActionDate(){
		return this.get("LAST_ACTION_DATE");
	}

	public void setLastActionDate(String lastActionDate){
		this.set("LAST_ACTION_DATE", lastActionDate);
	}
	
	public String getFirstPlanDate(){
		return this.get("FIRST_PLAN_DATE");
	}

	public void setFirstPlanDate(String firstPlanDate){
		this.set("FIRST_PLAN_DATE", firstPlanDate);
	}
	
	public String getRestoreDate(){
		return this.get("RESTORE_DATE");
	}

	public void setRestoreDate(String restoreDate){
		this.set("RESTORE_DATE", restoreDate);
	}
	
	public String getHouseCounselorId(){
		return this.get("HOUSE_COUNSELOR_ID");
	}

	public void setHouseCounselorId(String houseCounselorId){
		this.set("HOUSE_COUNSELOR_ID", houseCounselorId);
	}
	
	public String getCustAgentId(){
		return this.get("CUST_AGENT_ID");
	}

	public void setCustAgentId(String custAgentId){
		this.set("CUST_AGENT_ID", custAgentId);
	}
	
	public String getCustDesigenerId(){
		return this.get("CUST_DESIGENER_ID");
	}

	public void setCustDesigenerId(String custDesigenerId){
		this.set("CUST_DESIGENER_ID", custDesigenerId);
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