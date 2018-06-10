package com.most.core.pub.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author jinnian
 * @Date 2018/4/30 15:41
 * @Description:
 */
public class SessionEntity {

    private JSONObject data;

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

    public String getUsername(){
        return data.getString("USERNAME");
    }

    public void setUsername(String username){
        this.data.put("USERNAME", username);
    }

    public String get(String key){
        return this.data.getString(key);
    }

    public JSONObject getJSONObject(String key){
        return this.data.getJSONObject(key);
    }

    public JSONArray getJSONArray(String key){
        return this.data.getJSONArray(key);
    }

    public void put(String key, String value){
        this.data.put(key, value);
    }

    public void put(String key, JSONObject json){
        this.data.put(key, json);
    }

    public void put(String key, JSONArray jsonArray){
        this.data.put(key, jsonArray);
    }
}
