package com.most.core.pub.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/3/25 15:22
 * @Description:
 */
public class Record implements Serializable{

    private Map<String, String> data;

    public Record(Map<String, String> data){
        this.data = data;
    }

    public Record(){
        this.data = new HashMap<String, String>();
    }

    public Map<String, String> getData(){
        return this.data;
    }

    public void put(String key, String value){
        this.data.put(key, value);
    }

    public void put(String key, int value){
        this.data.put(key, String.valueOf(value));
    }

    public void put(String key, long value){
        this.data.put(key, String.valueOf(value));
    }

    public void put(String key, double value){
        this.data.put(key, String.valueOf(value));
    }

    public void put(String key, boolean value){
        this.data.put(key, String.valueOf(value));
    }

    public String get(String key){
        return this.data.get(key);
    }

    public int getInt(String key){
        return Integer.parseInt(this.data.get(key));
    }

    public long getLong(String key){
        return Long.parseLong(this.data.get(key));
    }

    public double getDouble(String key){
        return Double.parseDouble(this.data.get(key));
    }

    public boolean getBoolean(String key){
        return Boolean.parseBoolean(this.data.get(key));
    }

    public boolean containsKey(String key){
        return this.data.containsKey(key);
    }

    public int size(){
        return this.data.size();
    }

    public String toString(){
        return super.toString();
    }
}
