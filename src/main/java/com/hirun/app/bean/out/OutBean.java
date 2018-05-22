package com.hirun.app.bean.out;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.out.DataGetInfoDAO;
import com.most.core.app.database.dao.factory.DAOFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-21.
 */
public class OutBean {

    public static void insertDataGetInfo(String api, String requestData, String indbTime) throws Exception{
        DataGetInfoDAO dataGetInfoDAO = DAOFactory.createDAO(DataGetInfoDAO.class);
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("API", api);
        JSONObject reqestData = new JSONObject();
        parameter.put("REQUEST_DATA", requestData);
        parameter.put("INDB_TIME", indbTime);
        dataGetInfoDAO.insert("OUT_DATA_GET_INFO", parameter);
    }
}
