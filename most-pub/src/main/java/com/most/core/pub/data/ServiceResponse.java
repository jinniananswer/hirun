package com.most.core.pub.data;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:13
 * @Description:
 */
public class ServiceResponse extends  ServiceMessage{

    public ServiceResponse(){
        this.header = new Header();
        this.body = new Body();
    }

    public ServiceResponse(JSONObject data){
        this.header = new Header();
        this.body = new Body(data);
    }

    public ServiceResponse(JSONObject header, JSONObject data){
        this.header = new Header(header);
        this.body = new Body(data);
    }

    public void setError(String errorCode, String errInfo){
        this.header.set("RESULT_CODE", errorCode);
        this.header.set("RESULT_INFO", errInfo);
    }

    public void setSuccess(){
        this.header.set("RESULT_CODE", "0");
        this.header.set("RESULT_INFO", "PROCESS OK!");
    }

    public String getResultCode(){
        return this.header.getString("RESULT_CODE");
    }

    public String getResultInfo(){
        return this.header.getString("RESULT_INFO");
    }

    public boolean isSuccess(){
        return StringUtils.equals("0", getResultCode());
    }
}
