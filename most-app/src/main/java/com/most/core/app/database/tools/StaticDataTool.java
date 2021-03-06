package com.most.core.app.database.tools;

import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.RecordSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/23 21:58
 * @Description:
 */
public class StaticDataTool {

    private static Map<String, String> nameCached = new HashMap<String, String>();

    private static Map<String, RecordSet> codeTypeCached = new HashMap<String, RecordSet>();

    public static String getCodeName(String codeType, String codeValue) throws SQLException{
        String key = codeType+"_"+codeValue;
        if(nameCached.containsKey(key)){
            return nameCached.get(key);
        }
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CODE_TYPE", codeType);
        parameter.put("CODE_VALUE", codeValue);

        GenericDAO dao = new GenericDAO("sys");
        RecordSet recordSet = dao.query("sys_static_data", parameter);
        if(recordSet == null || recordSet.size() <= 0){
            nameCached.put(key, null);
            return null;
        }
        String name = recordSet.get(0).get("CODE_NAME");
        nameCached.put(key, name);
        return name;
    }

    public static RecordSet getCodeTypeDatas(String codeType) throws SQLException{
        if(codeTypeCached.containsKey(codeType)){
            return codeTypeCached.get(codeType);
        }

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CODE_TYPE", codeType);

        GenericDAO dao = new GenericDAO("sys");
        RecordSet recordSet = dao.query("sys_static_data", parameter);
        codeTypeCached.put(codeType, recordSet);
        return recordSet;
    }

    public static RecordSet getRelCodeTypeDatas(String codeType, String codeValue) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("CODE_TYPE", codeType);
        parameter.put("CODE_VALUE", codeValue);

        StringBuilder sb = new StringBuilder();
        sb.append("select c.* from sys_static_data a, sys_static_data_rel b, sys_static_data c ");
        sb.append("where a.code_type = :CODE_TYPE and a.code_value = :CODE_VALUE ");
        sb.append("and b.data_id = a.data_id ");
        sb.append("and c.data_id = b.rel_data_id ");

        GenericDAO dao = new GenericDAO("sys");
        RecordSet recordSet = dao.queryBySql(sb.toString(), parameter);
        return recordSet;
    }
}
