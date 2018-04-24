package com.hirun.pub.domain.entity.org;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 16:12
 * @Description:
 */
public class EmployeeEntity extends GenericEntity{

    public EmployeeEntity(Map<String, String> data){
        super(data);
    }

    public EmployeeEntity(){
        super();
    }

    public String getEmployeeId(){
        return this.get("EMPLOYEE_ID");
    }

    public void setEmployeeId(String employeeId){
        this.set("EMPLOYEE_ID", employeeId);
    }

    public String getUserId(){
        return this.get("USER_ID");
    }

    public void setUserId(String userId){
        this.set("USER_ID", userId);
    }

    public String getName(){
        return this.get("NAME");
    }

    public void setName(String name){
        this.set("NAME", name);
    }

    public String getSex(){
        return this.get("SEX");
    }

    public void setSex(String sex){
        this.set("SEX", sex);
    }

    public String getIndentityNo(){
        return this.get("IDENTITY_NO");
    }

    public void setIdentityNo(String identityNo){
        this.set("IDENTITY_NO",identityNo);
    }

    public String getHomeAddress(){
        return this.get("HOME_ADDRESS");
    }

    public void setHomeAddress(String homeAddress){
        this.set("HOME_ADDRESS", homeAddress);
    }

    public String getNativeProv(){
        return this.get("NATIVE_PRO");
    }

    public void setNativeProv(String nativeProv){
        this.set("NATIVE_PROV", nativeProv);
    }

    public String getNativeCity(){
        return this.get("NATIVE_CITY");
    }

    public void setNativeCity(String nativeCity){
        this.set("NATIVE_CITY", nativeCity);
    }

    public String getNativeRegion(){
        return this.get("NATIVE_REGION");
    }

    public void setNativeRegion(String nativeRegion){
        this.set("NATIVE_REGION", nativeRegion);
    }

    public String getInDate(){
        return this.get("IN_DATE");
    }

    public void setInDate(String inDate){
        this.set("IN_DATE", inDate);
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
