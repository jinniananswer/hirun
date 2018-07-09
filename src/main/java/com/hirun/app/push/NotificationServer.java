package com.hirun.app.push;

import com.hirun.app.dao.user.UserDeviceDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationServer {

    private static Logger log = LogManager.getLogger(NotificationServer.class.getName());

    public static void pushMessage(String userId, String message) throws Exception{
        UserDeviceDAO dao = DAOFactory.createDAO(UserDeviceDAO.class);
        RecordSet userDevices = dao.queryUserDevicesByUserId(userId);
        if(userDevices == null || userDevices.size() <= 0)
            return;


        int size = userDevices.size();
        for(int i=0;i<size;i++){
            Record userDevice = userDevices.get(i);
            String deviceToken = userDevice.get("DEVICE_TOKEN");
            String os = userDevice.get("OPERATION_SYSTEM");
            if(StringUtils.equals("1", os)){
                IOSMsgPushServer.sendNotification(deviceToken, message);
            }
            else if(StringUtils.equals("2", os)){
                AndroidMsgPushServer.sendNotification(deviceToken, message);
            }
        }

    }
}
