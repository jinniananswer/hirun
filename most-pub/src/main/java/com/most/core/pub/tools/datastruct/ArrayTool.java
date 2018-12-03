package com.most.core.pub.tools.datastruct;

import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;

import java.util.List;

/**
 * @Author jinnian
 * @Date 2018/2/20 23:51
 * @Description: 数组型及LIST数据的工具类，可以判断数组是否为空及数组型数据的简便操作
 */
public class ArrayTool {

    public static boolean isNotEmpty(Object[] objects){
        if(objects == null)
            return false;
        if(objects.length <= 0)
            return false;

        return true;
    }

    public static boolean isEmpty(Object[] objects){
        return !isNotEmpty(objects);
    }

    public static boolean isNotEmpty(List list){
        if(list == null)
            return false;
        if(list.size() <= 0)
            return false;

        return true;
    }

    public static boolean isEmpty(List list){
        return !isNotEmpty(list);
    }

    public static boolean isNotEmpty(RecordSet recordSet) {
        if(recordSet == null || recordSet.size() <= 0) {
            return false;
        }

        return true;
    }

    public static boolean isEmpty(RecordSet recordSet) {
        return !isNotEmpty(recordSet);
    }
}
