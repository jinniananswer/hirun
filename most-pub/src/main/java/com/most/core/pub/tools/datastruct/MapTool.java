package com.most.core.pub.tools.datastruct;

import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/2/20 23:55
 * @Description: Map的工具类，可以用来判断是否为空或者取值上的简便操作
 */
public class MapTool {

    public static boolean isNotEmpty(Map map){
        if(map == null)
            return false;
        if(map.isEmpty())
            return false;

        return true;
    }

    public static boolean isEmpty(Map map){
        return !isNotEmpty(map);
    }

    public static String getValue(Map map, String key, String defaultValue){
        if(map == null)
            return defaultValue;

        Object valueObj = map.get(key);
        if(valueObj == null)
            return defaultValue;
        String value = valueObj.toString();
        if(value == null || "".equals(value))
            return defaultValue;

        return value;
    }
}
