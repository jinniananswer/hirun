package com.hirun.app.bean.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.common.MsgDAO;
import com.hirun.pub.domain.entity.common.MsgEntity;
import com.hirun.pub.domain.enums.common.MsgType;
import com.hirun.pub.websocket.MsgWebSocketClient;
import com.hirun.pub.websocket.WebSocketMsg;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.List;

/**
 * Created by pc on 2018-06-09.
 */
public class MsgBean {

    public static void sendMsg(String recvUserId, String content, String sendUserId, String sendTime, MsgType msgType) throws Exception {
        MsgDAO msgDAO = DAOFactory.createDAO(MsgDAO.class);

        MsgEntity msgEntity = new MsgEntity();
        msgEntity.setRecvId(recvUserId);
        msgEntity.setMsgContent(content);
        msgEntity.setSendId(sendUserId);
        msgEntity.setSendTime(sendTime);
        msgEntity.setMsgType(msgType.getValue());
        msgEntity.setMsgStatus("0");

        msgDAO.insert("INS_MSG", msgEntity.getContent());

        //推送消息到前端
        WebSocketMsg webSocketMsg = new WebSocketMsg();
        webSocketMsg.setType("SERVER");
        webSocketMsg.setUserId(recvUserId);
        webSocketMsg.setContent(content);
        MsgWebSocketClient.sendMessage(webSocketMsg);
    }

    public static JSONArray setDescAndTOJSONArray(List<MsgEntity> msgEntityList) {
        JSONArray array = ConvertTool.toJSONArray(msgEntityList);
        for(int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            object.put("MSG_TYPE_DESC", "系统消息");
            object.put("USERNAME", "系统");
        }

        return array;
    }
}
