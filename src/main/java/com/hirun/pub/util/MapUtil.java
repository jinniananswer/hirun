package com.hirun.pub.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pc on 2018-04-24.
 */
public class MapUtil {

    public static Map<String, String> jsonToMap(JSONObject jsonObject) {
        Map<String, String> result = new HashMap<String, String>();

        Iterator<String> iterator = jsonObject.keySet().iterator();
        String key = null;
        String value = null;
        while (iterator.hasNext())
        {
            key = iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);
        }

        return result;
    }
}
