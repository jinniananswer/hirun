package com.most.core.pub.tools;

import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.exception.GenericException;

import java.util.Random;

/**
 * Created by awx on 2018/7/22/022.
 */
public class Utility {

    public static final Throwable getBottomException(Throwable exception) {
        if (exception == null)
            return null;
        if (exception.getCause() != null)
            return getBottomException(exception.getCause());
        else
            return exception;
    }

    public static void checkEmptyParam(JSONObject object, String[] keys) {
        StringBuilder errorMsg = new StringBuilder();
        for(String key : keys) {
            if(!object.containsKey(key) || "".equals(object.getString(key))) {
                errorMsg.append(key).append("、");
            }
        }

        if(errorMsg.length() > 0) {
            throw new GenericException("-1", "【" + errorMsg.substring(0, errorMsg.length() - 1) + "】不能为空");
        }
    }

    public static int[] randomArray(int min,int max,int n){
        int len = max-min+1;

        if(max < min || n > len){
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }
}
