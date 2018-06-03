package com.hirun.app.bean.housesplan;

import com.alibaba.fastjson.JSONArray;
import com.hirun.app.dao.houses.HousesPlanDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.RecordSet;
import com.most.core.pub.tools.transform.ConvertTool;

/**
 * @Author jinnian
 * @Date 2018/5/8 10:47
 * @Description:
 */
public class HousesPlanBean {

    public static JSONArray queryHousesByEmployeeId(String employeeId) throws Exception{
        HousesPlanDAO dao = new HousesPlanDAO("ins");
        RecordSet recordset = dao.queryHousesByEmployeeId(employeeId);
        return ConvertTool.toJSONArray(recordset);
    }

    public static JSONArray queryHouses() throws Exception{
        HousesPlanDAO dao = DAOFactory.createDAO(HousesPlanDAO.class);
        RecordSet recordset = dao.queryHouses();
        return ConvertTool.toJSONArray(recordset);
    }
}
