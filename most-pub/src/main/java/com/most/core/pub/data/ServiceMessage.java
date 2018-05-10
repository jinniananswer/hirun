package com.most.core.pub.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/17 13:23
 * @Description:
 */
public class ServiceMessage {

    protected Header header;

    protected Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void set(String key, Object value){
        this.body.set(key, value);
    }

    public void set(String key, String value){
        this.body.set(key, value);
    }

    public void set(String key, JSONObject jsonObject){
        this.body.set(key, jsonObject);
    }

    public void set(String key, JSONArray jsonArray){
        this.body.set(key, jsonArray);
    }

    public JSONObject getJSONObject(String key){
        return this.body.getJSONObject(key);
    }

    public JSONArray getJSONArray(String key){
        return this.body.getJSONArray(key);
    }

    public String getString(String key){
        return this.body.getString(key);
    }

    public void setHeader(String key, Object value){
        this.header.set(key, value);
    }

    public JSONObject toJSONObject(){
        JSONObject json = new JSONObject();
        if(header != null)
            json.put("HEAD", header.getData());
        if(body != null)
            json.put("BODY", body.getData());
        return json;
    }

    public Map toMap(){
        Map map = new HashMap();
        if(header != null)
            map.put("HEAD", JSON.parseObject(header.getData().toString(), Map.class));
        if(body != null)
            map.put("BODY", JSON.parseObject(body.getData().toString(), Map.class));

        return map;
    }

    public void remove(String key){
        body.remove(key);
    }

    public String toJsonString(){
        return JSONObject.toJSONString( this.toJSONObject(), SerializerFeature.DisableCircularReferenceDetect);
    }

    public String toString(){
        return this.toJSONObject().toString();
    }
}
