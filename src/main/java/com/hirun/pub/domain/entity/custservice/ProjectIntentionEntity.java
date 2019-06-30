package com.hirun.pub.domain.entity.custservice;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class ProjectIntentionEntity extends GenericEntity{

	public ProjectIntentionEntity(){
		super();
	}

	public ProjectIntentionEntity(Map<String, String> data){
		super(data);
	}
	
	public String getIntetionId(){
		return this.get("INTENTION_ID");
	}

	public void setIntentionId(String intentionId){
		this.set("INTENTION_ID", intentionId);
	}
	
	public String getProjectId(){
		return this.get("PROJECT_ID");
	}

	public void setProjectId(String projectId){
		this.set("PROJECT_ID", projectId);
	}
	
	public String getChineseStyleTopic(){
		return this.get("CHINESESTYLE_TOPIC");
	}

	public void setChineseStyleTopic(String chineseStyleTopic){
		this.set("CHINESESTYLE_TOPIC", chineseStyleTopic);
	}
	
	public String getEuropeanClassicsTopic(){
		return this.get("EUROPEANCLASSICS_TOPIC");
	}

	public void setEuropeanClassicsTopic(String europeanClassicsTopic){
		this.set("EUROPEANCLASSICS_TOPIC", europeanClassicsTopic);
	}
	
	public String getModernSource(){
		return this.get("MODERNSOURCE_TOPIC");
	}

	public void setModernSource(String modernSource){
		this.set("MODERNSOURCE_TOPIC", modernSource);
	}
	
	public String getOtherTopicReq(){
		return this.get("OTHER_TOPIC_REQ");
	}

	public void setOtherTopicReq(String otherTopicReq){
		this.set("OTHER_TOPIC_REQ", otherTopicReq);
	}

	
	public String getFunc(){
		return this.get("FUNC");
	}

	public void setFunc(String func){
		this.set("FUNC", func);
	}
	
	public String getHasBluePrint(){
		return this.get("HASBLUEPRINT");
	}

	public void setHasBluePrint(String hasBluePrint){
		this.set("HASBLUEPRINT", hasBluePrint);
	}

	public String getFuncSpecReq(){
		return this.get("FUNC_SPEC_REQ");
	}

	public void setFuncSpencReq(String funcSpencReq){
		this.set("FUNC_SPEC_REQ", funcSpencReq);
	}
	
	public String getTotalPricePlan(){
		return this.get("TOTAL_PRICEPLAN");
	}

	public void setTotalPricePlan(String totalPricePlan){
		this.set("TOTAL_PRICEPLAN", totalPricePlan);
	}
	
	public String getBasicAndWoodPricePlan(){
		return this.get("BASICANDWOOD_PRICEPLAN");
	}

	public void setBasicAndWoodPricePlan(String basicAndWoodPricePlan){
		this.set("BASICANDWOOD_PRICEPLAN", basicAndWoodPricePlan);
	}
	
	public String getHvacPricePlan(){
		return this.get("HVAC_PRICEPLAN");
	}

	public void setHvacPricePlan(String hvacPricePlan){
		this.set("HVAC_PRICEPLAN", hvacPricePlan);
	}
	
	public String getMaterialPricePlan(){
		return this.get("MATERIAL_PRICEPLAN");
	}

	public void setMaterialPricePlan(String materialPricePlan){
		this.set("MATERIAL_PRICEPLAN", materialPricePlan);
	}
	
	public String getFurniturePricePlan(){
		return this.get("FURNITURE_PRICEPLAN");
	}

	public void setFurniturePricePlan(String furniturePricePlan){
		this.set("FURNITURE_PRICEPLAN", furniturePricePlan);
	}
	
	public String getElectricalPricePlan(){
		return this.get("ELECTRICAL_PRICEPLAN");
	}

	public void setElectricalPricePlan(String electricalPricePlan){
		this.set("ELECTRICAL_PRICEPLAN", electricalPricePlan);
	}
	
	public String getPlanLiveTime(){
		return this.get("PLAN_LIVE_TIME");
	}

	public void setPlanLiveTime(String planLiveTime){
		this.set("PLAN_LIVE_TIME", planLiveTime);
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