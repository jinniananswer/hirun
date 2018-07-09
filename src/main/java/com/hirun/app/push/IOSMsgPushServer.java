package com.hirun.app.push;

import com.hirun.app.dao.user.UserDeviceDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.data.Record;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class IOSMsgPushServer {

    private static Logger log = LogManager.getLogger(IOSMsgPushServer.class.getName());

    public static void sendNotification(String deviceToken, String message){
        try{
            // 图标小红圈的数值
            int badge = 1;
            // 铃音
            String sound = "default";

            // 推送证书的路径
            String certificatePath = Thread.currentThread().getContextClassLoader().getResource("/").getPath()+"push.p12";
            // 证书的密码
            String certificatePassword = "123456";// 此处注意导出的证书密码不能为空因为空密码会报错
            boolean sendCount = true;

            PushNotificationPayload payLoad = new PushNotificationPayload();
            payLoad.addAlert(message);         // 消息内容
            payLoad.addBadge(badge);         // iphone应用图标上小红圈上的数值、

            if (!StringUtils.isBlank(sound)) {
                payLoad.addSound(sound);    // 铃音
            }
            PushNotificationManager pushManager = new PushNotificationManager();
            // true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
            pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, false));
            List<PushedNotification> notifications = new ArrayList<PushedNotification>();
            // 发送push消息

            Device device = new BasicDevice();
            device.setToken(deviceToken);
            PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
            notifications.add(notification);

            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
            int failed = failedNotifications.size();
            int successful = successfulNotifications.size();

            pushManager.stopConnection();
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
}
