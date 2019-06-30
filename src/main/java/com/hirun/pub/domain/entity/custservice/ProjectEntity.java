package com.hirun.pub.domain.entity.custservice;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class ProjectEntity extends GenericEntity{

	public ProjectEntity(){
		super();
	}

	public ProjectEntity(Map<String, String> data){
		super(data);
	}
	
	public String getParytId(){
		return this.get("PARTY_ID");
	}

	public void setParytId(String parytId){
		this.set("PARTY_ID", parytId);
	}
	
	public String getProjectId(){
		return this.get("PROJECT_ID");
	}

	public void setProjectId(String projectId){
		this.set("PROJECT_ID", projectId);
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
	
	public String getHouseAddress(){
		return this.get("HOUSE_ADDRESS");
	}

	public void setHouseAddress(String houseAddress){
		this.set("HOUSE_ADDRESS", houseAddress);
	}
	
	public String getHouseCounselorId(){
		return this.get("HOME_COUNSELOR_ID");
	}

	public void setHouseCounserlorId(String houseCounserlorId){
		this.set("HOME_COUNSELOR_ID", houseCounserlorId);
	}

	
	public String getHomeAgentId(){
		return this.get("HOME_AGENT_ID");
	}

	public void setHomeAgentId(String homeAgentId){
		this.set("HOME_AGENT_ID", homeAgentId);
	}
	
	public String getHomeDesignerId(){
		return this.get("HOME_DESIGENER_ID");
	}

	public void setHomeDesignerId(String homeDesignerId){
		this.set("HOME_DESIGENER_ID", homeDesignerId);
	}

	public String getGaugeHouseTime(){
		return this.get("GAUGE_HOUSE_TIME");
	}

	public void setGaugeHouseTime(String gaugeHouseTime){
		this.set("GAUGE_HOUSE_TIME", gaugeHouseTime);
	}
	
	public String getOfferPlaneTime(){
		return this.get("OFFER_PLANE_TIME");
	}

	public void setOfferPlaneTime(String offerPlaneTime){
		this.set("OFFER_PLANE_TIME", offerPlaneTime);
	}
	
	public String getContactTime(){
		return this.get("CONTACT_TIME");
	}

	public void setContactTime(String contactTime){
		this.set("CONTACT_TIME", contactTime);
	}
	
	public String getCriticalProcess(){
		return this.get("CRITICAL_PROCESS");
	}

	public void setCriticalProcess(String criticalProcess){
		this.set("CRITICAL_PROCESS", criticalProcess);
	}
	
	public String getOtherInfo(){
		return this.get("OTHER_INFO");
	}

	public void setOtherInfo(String otherInfo){
		this.set("OTHER_INFO", otherInfo);
	}
	
	public String getMWExperienceTime(){
		return this.get("MW_EXPERIENCE_TIME");
	}

	public void setMWExperienceTime(String mwExperienceTime){
		this.set("MW_EXPERIENCE_TIME", mwExperienceTime);
	}
	
	public String getAdvantage(){
		return this.get("ADVANTAGE");
	}

	public void setAdvantage(String advantage){
		this.set("ADVANTAGE", advantage);
	}
	
	public String getIsScanShowRoom(){
		return this.get("IS_SCAN_SHOWROOM");
	}

	public void setIsScanVideo(String isScanVideo){
		this.set("IS_SCAN_VIDEO", isScanVideo);
	}

	public String getIsScanVideo(){
		return this.get("IS_SCAN_VIDEO");
	}

	public void setIsScanShowRoom(String isScanShowRoom){
		this.set("IS_SCAN_SHOWROOM", isScanShowRoom);
	}
	
	public String getCounselorName(){
		return this.get("COUNSELOR_NAME");
	}

	public void setCounselorName(String counselorName){
		this.set("COUNSELOR_NAME", counselorName);
	}
	
	public String getInformationSource(){
		return this.get("INFORMATION_SOURCE");
	}

	public void setInformationSource(String informationSource){
		this.set("INFORMATION_SOURCE", informationSource);
	}

	public String getOtherInformationSource(){
		return this.get("OTHER_INFORMATION_SOURCE");
	}

	public void setOtherInformationSource(String otherInformationSource){
		this.set("OTHER_INFORMATION_SOURCE", otherInformationSource);
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