package com.most.core.pub.tools;

import com.alibaba.fastjson.JSONObject;
import com.most.core.pub.exception.GenericException;

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
}
