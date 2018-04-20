package com.most.core.pub.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:20
 * @Description:
 */
public class JSONData {

    protected JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public Object get(String key){
        return this.data.get(key);
    }

    public String getString(String key){
        return this.data.getString(key);
    }

    public JSONObject getJSONObject(String key){
        return this.data.getJSONObject(key);
    }

    public JSONArray getJSONArray(String key){
        return this.data.getJSONArray(key);
    }

    public int getInt(String key){
        return this.data.getIntValue(key);
    }

    public void set(String key, Object value){
        this.data.put(key, value);
    }

    public void set(String key, String value){
        this.data.put(key, value);
    }

    public void set(String key, JSONObject jsonObject){
        this.data.put(key, jsonObject);
    }

    public void set(String key, JSONArray jsonArray){
        this.data.put(key, jsonArray);
    }
}
