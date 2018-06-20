package com.hirun.pub.domain.entity.cust;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class CustContactEntity extends GenericEntity{

	public CustContactEntity(){
		super();
	}

	public CustContactEntity(Map<String, String> data){
		super(data);
	}
	
	public String getCustContactId(){
		return this.get("CUST_CONTACT_ID");
	}

	public void setCustContactId(String custContactId){
		this.set("CUST_CONTACT_ID", custContactId);
	}
	
	public String getCustId(){
		return this.get("CUST_ID");
	}

	public void setCustId(String custId){
		this.set("CUST_ID", custId);
	}
	
	public String getContactDate(){
		return this.get("CONTACT_DATE");
	}

	public void setContactDate(String contactDate){
		this.set("CONTACT_DATE", contactDate);
	}
	
	public String getContactNote(){
		return this.get("CONTACT_NOTE");
	}

	public void setContactNote(String contactNote){
		this.set("CONTACT_NOTE", contactNote);
	}
	
	public String getRestoreDate(){
		return this.get("RESTORE_DATE");
	}

	public void setRestoreDate(String restoreDate){
		this.set("RESTORE_DATE", restoreDate);
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