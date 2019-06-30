package com.hirun.pub.tool;

import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HouseParamTool {

    public static RecordSet getHouseTopicByType(String topic_type) throws SQLException {

        Map<String, String> parameter = new HashMap<String, String>();
        GenericDAO dao=new GenericDAO("sys");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sys_house_topic where TOPIC_TYPE= :TOPIC_TYPE and STATUS = :STATUS ");
        parameter.put("TOPIC_TYPE",topic_type);
        parameter.put("STATUS","1");
        return dao.queryBySql(sb.toString(),parameter);
    }

    public static RecordSet getFuncSystemByFuncType(String func_type) throws SQLException {

        Map<String, String> parameter = new HashMap<String, String>();
        GenericDAO dao=new GenericDAO("sys");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sys_house_func where FUNC_TYPE= :FUNC_TYPE and STATUS = :STATUS ");
        parameter.put("FUNC_TYPE",func_type);
        parameter.put("STATUS","1");
        return dao.queryBySql(sb.toString(),parameter);
    }

    public static String getTopicNameByTypeAndCode(String type,String code) throws SQLException {

        Map<String, String> parameter = new HashMap<String, String>();
        GenericDAO dao=new GenericDAO("sys");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sys_house_topic where TOPIC_TYPE= :TOPIC_TYPE and STATUS = :STATUS  and TOPIC_CODE= :TOPIC_CODE ");
        parameter.put("TOPIC_TYPE",type);
        parameter.put("TOPIC_CODE",code);
        parameter.put("STATUS","1");
        RecordSet recordSet=dao.queryBySql(sb.toString(),parameter);
        if(recordSet == null || recordSet.size() <= 0){
            return null;
        }
        return recordSet.get(0).get("TOPIC_NAME");
    }


    public static String getFuncNameByTypeAndCode(String type,String code) throws SQLException {

        Map<String, String> parameter = new HashMap<String, String>();
        GenericDAO dao=new GenericDAO("sys");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM sys_house_func where FUNC_TYPE= :FUNC_TYPE and STATUS = :STATUS  and FUNC_CODE= :FUNC_CODE ");
        parameter.put("FUNC_TYPE",type);
        parameter.put("FUNC_CODE",code);
        parameter.put("STATUS","1");
        RecordSet recordSet=dao.queryBySql(sb.toString(),parameter);
        if(recordSet == null || recordSet.size() <= 0){
            return null;
        }
        return recordSet.get(0).get("FUNC_NAME");
    }
}
