package com.most.core.pub.tools.transform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.data.GenericEntity;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;

import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/3/20 10:10
 * @Description:
 */
public class ConvertTool {

    public static JSONArray toJSONArray(RecordSet recordSet){
        if(recordSet == null || recordSet.size() <= 0){
            return new JSONArray();
        }

        JSONArray jsonArray = new JSONArray();
        int size = recordSet.size();
        for(int i=0;i<size;i++){
            Record record = recordSet.get(i);
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(record.getData()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray toJSONArray(List<? extends GenericEntity> entities){
        if(ArrayTool.isEmpty(entities)){
            return new JSONArray();
        }

        JSONArray jsonArray = new JSONArray();
        for(GenericEntity entity : entities){
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(entity.getContent()));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
