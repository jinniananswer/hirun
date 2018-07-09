package com.hirun.app.push;

import com.hirun.app.dao.user.UserDeviceDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import com.most.core.pub.data.RecordSet;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AndroidMsgPushServer {

    private static Logger log = LogManager.getLogger(AndroidMsgPushServer.class.getName());

    public static void sendNotification(String deviceToken, String messagePayload){
        try{

            String regId = deviceToken;
            Constants.useOfficial();
            Sender sender = new Sender("fsZF+oY0TYVDesI+QhLOpA==");
            String title = "鸿扬家装";
            String description = messagePayload;
            Message message = new Message.Builder()
                    .title(title)
                    .description(description).payload(messagePayload)
                    .restrictedPackageName("com.hirun.android")
                    .notifyType(1)     // 使用默认提示音提示
                    .extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_ACTIVITY)
                    .extra(Constants.EXTRA_PARAM_INTENT_URI, "intent:#Intent;component=com.hirun.android/.MainActivity;end")
                    .build();
            Result result = sender.send(message, regId, 3);
        }
        catch (Exception e){

        }
    }
}
