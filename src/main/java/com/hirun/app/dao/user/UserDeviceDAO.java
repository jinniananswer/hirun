package com.hirun.app.dao.user;

import com.most.core.app.database.annotation.DatabaseName;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@DatabaseName("ins")
public class UserDeviceDAO extends GenericDAO {

    public UserDeviceDAO(String databaseName){
        super(databaseName);
    }

    public Record queryUserDeviceByUserId(String userId) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);
        RecordSet recordSet = this.query("ins_user_device", parameter);
        if(recordSet != null && recordSet.size() > 0)
            return recordSet.get(0);
        else
            return null;
    }

    public Record queryUserDeviceByUserIdAndDevice(String userId, String deviceToken) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);
        parameter.put("DEVICE_TOKEN", deviceToken);
        RecordSet recordSet = this.query("ins_user_device", parameter);
        if(recordSet != null && recordSet.size() > 0)
            return recordSet.get(0);
        else
            return null;
    }

    public RecordSet queryUserDeviceByUserIdAndOperationSystem(String userId, int operationSystem) throws SQLException{
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("USER_ID", userId);
        parameter.put("OPERATION_SYSTEM", operationSystem+"");
        RecordSet recordSet = this.query("ins_user_device", parameter);
        return recordSet;
    }
}
