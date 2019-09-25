package com.hirun.app.bean.custservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.cache.ActionCache;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.dao.custservice.CustomerServiceDAO;
import com.hirun.pub.consts.CustomerServiceConst;
import com.hirun.pub.domain.entity.custservice.PartyEntity;
import com.hirun.pub.domain.entity.custservice.PartyOriginalActionEntity;
import com.hirun.pub.domain.entity.org.EmployeeEntity;
import com.hirun.pub.domain.entity.param.ActionEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.datastruct.ArrayTool;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;


public class BluePrintBean {

    public static JSONObject getFuncTree(Record record) {
        if (record == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();

        String func = record.get("FUNC");
        if (StringUtils.isBlank(func) || StringUtils.equals("false", func)) {
            return null;
        }
        JSONArray funcArray = JSONArray.parseArray(func);
        int size = funcArray.size();

        JSONObject root = new JSONObject();
        root.put("text", "功能蓝图");
        root.put("id", "-1");
        root.put("dataid", "-1");
        root.put("expand", "true");
        root.put("order", "0");
        root.put("disabled", "false");
        root.put("complete", "false");
        root.put("showcheck", "false");
        root.put("haschild", "true");
        root.put("value", "-1");


        JSONObject children = buildFuncTree(funcArray, "-1");

        if (children == null) {
            root.put("haschild", "false");
        } else {
            root.put("haschild", "true");
            root.put("childNodes", children);
        }
        jsonObject.put("-1", root);
        return jsonObject;
    }


    public static JSONObject buildFuncTree(JSONArray objJson, String prefix) {
        JSONObject childJsonObject = new JSONObject();
        if (objJson == null) {
            return null;
        }
        for (int i = 0; i < objJson.size(); i++) {
            JSONObject childNode = new JSONObject();
            JSONObject jsonObject = objJson.getJSONObject(i);
            childNode.put("text", jsonObject.getString("name"));
            childNode.put("dataid", prefix + "●" + jsonObject.getString("caid"));
            childNode.put("id", jsonObject.getString("caid"));
            childNode.put("disabled", "false");
            childNode.put("value", jsonObject.getString("caid"));
            JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
            if (subsJsonArray.size() <= 0 || subsJsonArray == null) {
                childNode.put("haschild", "false");
            } else {
                JSONObject child = buildChildTree(subsJsonArray, prefix + "●" + jsonObject.getString("caid"));
                childNode.put("haschild", "true");
                childNode.put("childNodes", child);
            }
            childJsonObject.put(jsonObject.getString("caid"), childNode);
        }
        return childJsonObject;
    }

    public static JSONObject buildChildTree(JSONArray subArray, String prefix) {
        JSONObject childJsonObject = new JSONObject();
        if (subArray == null) {
            return null;
        }
        for (int i = 0; i < subArray.size(); i++) {
            JSONObject childNode = new JSONObject();
            JSONObject jsonObject = subArray.getJSONObject(i);
            childNode.put("text", jsonObject.getString("name"));
            childNode.put("dataid", prefix + "●" + jsonObject.getString("caid"));
            childNode.put("id", jsonObject.getString("caid"));
            childNode.put("disabled", "false");
            childNode.put("value", jsonObject.getString("caid"));

            String subs = jsonObject.getString("subs");
            if (StringUtils.equals(subs, "false") || StringUtils.isBlank(subs)) {
                childNode.put("haschild", "false");
            }else {
                JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
                if (subsJsonArray.size() <= 0 || subsJsonArray == null) {
                    childNode.put("haschild", "false");
                } else {
                    JSONObject child = buildChildTree1(subsJsonArray, prefix + "●" + jsonObject.getString("caid"));
                    childNode.put("haschild", "true");
                    childNode.put("childNodes", child);
                }
                childJsonObject.put(jsonObject.getString("caid"), childNode);
            }
        }
        return childJsonObject;
    }

    public static JSONObject buildChildTree1(JSONArray subArray, String prefix) {
        JSONObject childJsonObject = new JSONObject();
        if (subArray == null) {
            return null;
        }
        for (int i = 0; i < subArray.size(); i++) {
            JSONObject childNode = new JSONObject();
            JSONObject jsonObject = subArray.getJSONObject(i);
            childNode.put("text", jsonObject.getString("name"));
            childNode.put("dataid", prefix + "●" + jsonObject.getString("caid"));
            childNode.put("id", jsonObject.getString("caid"));
            childNode.put("disabled", "false");
            childNode.put("value", jsonObject.getString("caid"));

            String subs = jsonObject.getString("subs");
            if (StringUtils.equals(subs, "false") || StringUtils.isBlank(subs)) {
                childNode.put("haschild", "false");
            } else {
                JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
                if (subsJsonArray.size() <= 0 || subsJsonArray == null) {
                    childNode.put("haschild", "false");
                } else {
                    JSONObject child = buildChildTree2(subsJsonArray, prefix + "●" + jsonObject.getString("caid"));
                    childNode.put("haschild", "true");
                    childNode.put("childNodes", child);
                }
            }
            childJsonObject.put(jsonObject.getString("caid"), childNode);
        }
        return childJsonObject;
    }

    public static JSONObject buildChildTree2(JSONArray subArray, String prefix) {
        JSONObject childJsonObject = new JSONObject();
        if (subArray == null) {
            return null;
        }
        for (int i = 0; i < subArray.size(); i++) {
            JSONObject childNode = new JSONObject();
            JSONObject jsonObject = subArray.getJSONObject(i);
            childNode.put("text", jsonObject.getString("name"));
            childNode.put("dataid", prefix + "●" + jsonObject.getString("caid"));
            childNode.put("id", jsonObject.getString("caid"));
            childNode.put("disabled", "false");
            childNode.put("value", jsonObject.getString("caid"));

            String types = jsonObject.getString("types");
            if (StringUtils.equals(types, "false") || StringUtils.isBlank(types)) {
                childNode.put("haschild", "false");
            } else {
                JSONArray subsJsonArray = jsonObject.getJSONArray("types");
                if (subsJsonArray.size() <= 0 || subsJsonArray == null) {
                    childNode.put("haschild", "false");
                } else {
                    JSONObject child = buildChildTree3(subsJsonArray, prefix + "●" + jsonObject.getString("caid"));
                    childNode.put("haschild", "true");
                    childNode.put("childNodes", child);
                }
            }
            childJsonObject.put(jsonObject.getString("caid"), childNode);
        }
        return childJsonObject;
    }

    public static JSONObject buildChildTree3(JSONArray subArray, String prefix) {
        JSONObject childJsonObject = new JSONObject();
        if (subArray == null) {
            return null;
        }
        for (int i = 0; i < subArray.size(); i++) {
            JSONObject childNode = new JSONObject();
            JSONObject jsonObject = subArray.getJSONObject(i);
            childNode.put("text", jsonObject.getString("name"));
            childNode.put("dataid", prefix + "●" + jsonObject.getString("typeid"));
            childNode.put("id", jsonObject.getString("typeid"));
            childNode.put("disabled", "false");
            childNode.put("value", jsonObject.getString("typeid"));
            JSONArray subsJsonArray = jsonObject.getJSONArray("subs");

            if (subsJsonArray.size() <= 0 || subsJsonArray == null) {
                childNode.put("haschild", "false");
            } else {
                JSONObject child = buildChildTree4(subsJsonArray, prefix + "●" + jsonObject.getString("typeid"));
                childNode.put("haschild", "true");
                childNode.put("childNodes", child);
            }
            childJsonObject.put(jsonObject.getString("typeid"), childNode);
        }
        return childJsonObject;
    }

    public static JSONObject buildChildTree4(JSONArray subArray, String prefix) {
        JSONObject childJsonObject = new JSONObject();
        if (subArray == null) {
            return null;
        }
        for (int i = 0; i < subArray.size(); i++) {
            JSONObject childNode = new JSONObject();
            JSONObject jsonObject = subArray.getJSONObject(i);
            childNode.put("text", jsonObject.getString("name"));
            childNode.put("dataid", prefix + "●" + jsonObject.getString("typeid"));
            childNode.put("id", jsonObject.getString("typeid"));
            childNode.put("disabled", "false");
            childNode.put("value", jsonObject.getString("typeid"));

            childJsonObject.put(jsonObject.getString("typeid"), childNode);
        }
        return childJsonObject;
    }


    public static JSONArray getStyleContent(Record styleRecod) {
        JSONArray styleJsonArray = new JSONArray();
        if (styleRecod == null) {
            return null;
        }

        String style = styleRecod.get("STYLE");
        if (StringUtils.isBlank(style) || StringUtils.equals("false", style)) {
            return null;
        }
        JSONArray styleArray = JSONArray.parseArray(style);
        int size = styleArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = new JSONObject();
            JSONObject styleJsonObject = styleArray.getJSONObject(i);
            jsonObject.put("PROJECT_ID", styleJsonObject.getString("project_id "));
            jsonObject.put("NAME", styleJsonObject.getString("name"));
            jsonObject.put("CONTENT", styleJsonObject.getString("content"));
            styleJsonArray.add(jsonObject);
        }

        return styleJsonArray;
    }
}

