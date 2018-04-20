package com.most.core.pub.data;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:12
 * @Description:
 */
public class ServiceRequest extends ServiceMessage{

    public ServiceRequest(){
        this.header = new Header();
        this.body = new Body();
    }

    public ServiceRequest(JSONObject data){
        this.header = new Header();
        this.body = new Body(data);
    }

    public ServiceRequest(JSONObject header, JSONObject data){
        this.header = new Header(header);
        this.body = new Body(data);
    }
}
