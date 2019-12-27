package com.hirun.pub.domain.entity.custservice;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class PartyEntity extends GenericEntity{

	public PartyEntity(){
		super();
	}

	public PartyEntity(Map<String, String> data){
		super(data);
	}
	
	public String getParytId(){
		return this.get("PARTY_ID");
	}

	public void setParytId(String parytId){
		this.set("PARTY_ID", parytId);
	}
	
	public String getOpenId(){
		return this.get("OPEN_ID");
	}

	public void setOpenId(String openId){
		this.set("OPEN_ID", openId);
	}
	
	public String getPartyName(){
		return this.get("PARTY_NAME");
	}

	public void setPartyName(String partyName){
		this.set("PARTY_NAME", partyName);
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

	public String getHeadUrl(){
		return this.get("HEAD_URL");
	}

	public void setHeadUrl(String headUrl){
		this.set("HEAD_URL", headUrl);
	}
	
	public String getCustStatus(){
		return this.get("PARTY_STATUS");
	}

	public void setCustStatus(String custStatus){
		this.set("PARTY_STATUS", custStatus);
	}

	
	public String getMobileNo(){
		return this.get("MOBILE_NO");
	}

	public void setMobileNo(String mobileNo){
		this.set("MOBILE_NO", mobileNo);
	}
	
	public String getQQNO(){
		return this.get("QQ_NO");
	}

	public void setQQNO(String houseDetail){
		this.set("QQ_NO", houseDetail);
	}

	public String getWXNO(){
		return this.get("WX_NO");
	}

	public void setWXNO(String wxNo){
		this.set("WX_NO", wxNo);
	}
	
	public String getCompany(){
		return this.get("COMPANY");
	}

	public void setCompany(String company){
		this.set("COMPANY", company);
	}
	
	public String getEducational(){
		return this.get("EDUCATIONAL");
	}

	public void setEducational(String educational){
		this.set("EDUCATIONAL", educational);
	}
	
	public String getLastAction(){
		return this.get("LAST_ACTION");
	}

	public void setFamilyMembersCount(String familyMembersCount){
		this.set("FAMILY_MEMBERS_COUNT", familyMembersCount);
	}
	
	public String getfamilyMembersCount(){
		return this.get("FAMILY_MEMBERS_COUNT");
	}

	public void setOldmanCount(String oldmanCount){
		this.set("OLDMAN_COUNT", oldmanCount);
	}
	
	public String getOldmanCount(){
		return this.get("OLDMAN_COUNT");
	}

	public void setOlderDetail(String olderDetail){
		this.set("OLDER_DETAIL", olderDetail);
	}

	public String getOlderDetail(){
		return this.get("OLDER_DETAIL");
	}


	public void setOldwomanCount(String oldwomanCount){
		this.set("OLDWOMAN_COUNT", oldwomanCount);
	}
	
	public String getOldwomanCount(){
		return this.get("OLDWOMAN_COUNT");
	}

	public void setBoyCount(String boyCount){
		this.set("BOY_COUNT", boyCount);
	}
	
	public String getBoyCount(){
		return this.get("BOY_COUNT");
	}

	public void setGirlCount(String girlCount){
		this.set("GIRL_COUNT", girlCount);
	}
	
	public String getGirlCount(){
		return this.get("GIRL_COUNT");
	}

	public void setChildDetail(String childDetail){
		this.set("CHILD_DETAIL", childDetail);
	}

	public String getChildDetail(){
		return this.get("CHILD_DETAIL");
	}


	public void setHobby(String hobby){
		this.set("HOBBY", hobby);
	}
	
	public String getHobby(){
		return this.get("HOBBY");
	}

	public void setOtherHobby(String otherHobby){
		this.set("OTHER_HOBBY", otherHobby);
	}

	public String getOtherHobby(){
		return this.get("OTHER_HOBBY");
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

	public void setCreateTime(String createDate){
		this.set("CREATE_TIME", createDate);
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

	public String getAge(){
		return this.get("AGE");
	}

	public void setAge(String age){
		this.set("AGE", age);
	}


}