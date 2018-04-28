package com.hirun.pub.domain.entity.cust;

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 16:03
 * @Description:
 */
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

    public String getHouseDetail(){
        return this.get("HOUSE_DETAIL");
    }

    public void setHouseDetail(String houseDetail){
        this.set("HOUSE_DETAIL", houseDetail);
    }
}
