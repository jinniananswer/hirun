package com.most.core.pub.data;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:22
 * @Description:
 */
public class Body extends JSONData{

    public Body(){
        this.data = new JSONObject();
    }

    public Body(JSONObject object){
        this.data = new JSONObject();
        this.data.putAll(object);
    }
}
