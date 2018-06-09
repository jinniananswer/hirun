package com.hirun.pub.domain.entity.common;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class MsgEntity extends GenericEntity{

	public MsgEntity(){
		super();
	}

	public MsgEntity(Map<String, String> data){
		super(data);
	}
	
	public String getMsgId(){
		return this.get("MSG_ID");
	}

	public void setMsgId(String msgId){
		this.set("MSG_ID", msgId);
	}
	
	public String getRecvId(){
		return this.get("RECV_ID");
	}

	public void setRecvId(String recvId){
		this.set("RECV_ID", recvId);
	}
	
	public String getMsgStatus(){
		return this.get("MSG_STATUS");
	}

	public void setMsgStatus(String msgStatus){
		this.set("MSG_STATUS", msgStatus);
	}
	
	public String getMsgContent(){
		return this.get("MSG_CONTENT");
	}

	public void setMsgContent(String msgContent){
		this.set("MSG_CONTENT", msgContent);
	}
	
	public String getMsgType(){
		return this.get("MSG_TYPE");
	}

	public void setMsgType(String msgType){
		this.set("MSG_TYPE", msgType);
	}
	
	public String getSendId(){
		return this.get("SEND_ID");
	}

	public void setSendId(String sendId){
		this.set("SEND_ID", sendId);
	}
	
	public String getSendTime(){
		return this.get("SEND_TIME");
	}

	public void setSendTime(String sendTime){
		this.set("SEND_TIME", sendTime);
	}
	
}