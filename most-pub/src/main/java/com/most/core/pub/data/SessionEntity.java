package com.most.core.pub.data;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author jinnian
 * @Date 2018/4/30 15:41
 * @Description:
 */
public class SessionEntity {

    protected JSONObject data;

    public SessionEntity(JSONObject json){
        this.data = new JSONObject();
        this.data.putAll(json);
    }

    public SessionEntity(){
        this.data = new JSONObject();
    }

    public JSONObject getData(){
        return this.data;
    }

    public String getUserId() {
        return data.getString("USER_ID");
    }

    public void setUserId(String userId) {
        this.data.put("USER_ID", userId);
    }
}
