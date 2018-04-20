package com.hirun.pub.domain.entity.user;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 16:03
 * @Description:
 */
public class UserEntity extends GenericEntity{

    public UserEntity(){
        super();
    }

    public UserEntity(Map<String, String> data){
        super(data);
    }

    public String getUserId(){
        return this.get("USER_ID");
    }

    public void setUserId(String userId){
        this.set("USER_ID", userId);
    }

    public String getUserName(){
        return this.get("USERNAME");
    }

    public void setUserName(String userName){
        this.set("USERNAME", userName);
    }

    public String getPassword(){
        return this.get("PASSWORD");
    }

    public void setPassword(String password){
        this.set("PASSWORD", password);
    }

    public String getMobileNo(){
        return this.get("MOBILE_NO");
    }

    public void setMobileNo(String mobileNo){
        this.set("MOBILE_NO", mobileNo);
    }

    public String getStatus(){
        return this.get("STATUS");
    }

    public void setStatus(String status){
        this.set("STATUS", status);
    }

    public String getCreateDate(){
        return this.get("CREATE_DATE");
    }

    public void setCreateDate(String createDate){
        this.set("CREATE_DATE", createDate);
    }

    public String getRemoveDate(){
        return this.get("REMOVE_DATE");
    }

    public void setRemoveDate(String removeDate){
        this.set("REMOVE_DATE", removeDate);
    }

    public void setCreateUserId(String createUserId){
        this.set("CREATE_USER_ID", createUserId);
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
