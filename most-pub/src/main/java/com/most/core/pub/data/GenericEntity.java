package com.most.core.pub.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/18 11:37
 * @Description:
 */
public class GenericEntity {

    protected Map<String, String> content;

    public GenericEntity(){
        this.content = new HashMap<String, String>();
    }

    public GenericEntity(Map<String, String> content){
        this.content = new HashMap<String, String>();
        this.content.putAll(content);
    }

    public String get(String key){
        return this.content.get(key);
    }

    public void set(String key, String value){
        this.content.put(key, value);
    }

    public Map<String, String> getContent(){
        return this.content;
    }

    public void setContent(Map<String, String> content){
        this.content = new HashMap<String, String>();
        this.content.putAll(content);
    }

    public String toString(){
        return this.content.toString();
    }

    public JSONObject toJson(){
        return JSONObject.parseObject(JSON.toJSONString(this.content, SerializerFeature.DisableCircularReferenceDetect));
    }

    public JSONObject toJSON(String[] keys) {
        JSONObject jsonObject = new JSONObject();
        for(String key : keys) {
            jsonObject.put(key, this.content.get(key));
        }

        return jsonObject;
    }
}
