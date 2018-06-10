package com.most.core.pub.data;

import com.most.core.pub.tools.datastruct.ArrayTool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/3/25 15:22
 * @Description:
 */
public class RecordSet implements Serializable{

    private List<Record> dataset;

    public RecordSet(List<Record> dataset){
        this.dataset = dataset;
    }

    public RecordSet(){
        this.dataset = new ArrayList<Record>();
    }

    public List<Record> getDataset(){
        return this.dataset;
    }

    public List<Map<String, String>> clone(){
        if(ArrayTool.isEmpty(dataset))
            return new ArrayList<Map<String, String>>();

        List<Map<String, String>> dataset = new ArrayList<Map<String, String>>();
        for(Record record : this.dataset){
            Map<String, String> data = new HashMap<String, String>();
            data.putAll(record.getData());
            dataset.add(data);
        }
        return dataset;
    }

    public void add(Record data){
        this.dataset.add(data);
    }

    public int size(){
        return this.dataset.size();
    }

    public Record get(int index){
        return this.dataset.get(index);
    }

    public String get(int index, String key){
        if(ArrayTool.isEmpty(this.dataset) || index >= dataset.size())
            throw new IndexOutOfBoundsException("输入的下标已经超过集合的最大长度");

        return this.dataset.get(index).get(key);
    }

    public int getInt(int index, String key){
        if(ArrayTool.isEmpty(this.dataset) || index >= dataset.size())
            throw new IndexOutOfBoundsException("输入的下标已经超过集合的最大长度");

        return this.dataset.get(index).getInt(key);
    }

    public long getLong(int index, String key){
        if(ArrayTool.isEmpty(this.dataset) || index >= dataset.size())
            throw new IndexOutOfBoundsException("输入的下标已经超过集合的最大长度");

        return this.dataset.get(index).getInt(key);
    }

    public double getDouble(int index, String key){
        if(ArrayTool.isEmpty(this.dataset) || index >= dataset.size())
            throw new IndexOutOfBoundsException("输入的下标已经超过集合的最大长度");

        return this.dataset.get(index).getDouble(key);
    }

    public boolean getBoolean(int index, String key){
        if(ArrayTool.isEmpty(this.dataset) || index >= dataset.size())
            throw new IndexOutOfBoundsException("输入的下标已经超过集合的最大长度");

        return this.dataset.get(index).getBoolean(key);
    }

    public void clear(){
        this.dataset.clear();
    }

    public String toString(){
        return super.toString();
    }
}
