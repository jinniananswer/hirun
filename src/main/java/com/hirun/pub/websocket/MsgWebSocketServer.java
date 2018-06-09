package com.hirun.pub.websocket;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by pc on 2018-06-08.
 */
@ServerEndpoint(value="/websocketServer/{userId}")
public class MsgWebSocketServer {

    private Logger logger = LoggerFactory.getLogger(MsgWebSocketServer.class);

    private Session session;


    @OnOpen
    public void onOpen(@PathParam(value="userId") String userId, Session session) throws IOException {
        if(!userId.startsWith("server")) {
            WebSocketMapUtil.put(userId, this);
            this.session = session;
        }
    }

    @OnClose
    public void onClose() {
        logger.debug("连接{}关闭");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        WebSocketMsg msg = new WebSocketMsg(message);
        String content = msg.getContent();
        String type = msg.getType();
        if("SERVER".equals(type)) {
            String userId = msg.getUserId();
            MsgWebSocketServer msgWebSocketServer = WebSocketMapUtil.get(userId);
            if(msgWebSocketServer != null) {
                msg.setType("CLIENT");
                msgWebSocketServer.onMessage(msg.toJSONString(), msgWebSocketServer.session);
            }
        } else {
            session.getBasicRemote().sendText(content);
        }
    }

    //连接错误时执行
    @OnError
    public void onError(Session session, Throwable error){
        logger.debug("用户id为：{}的连接发送错误");
        error.printStackTrace();
    }
}
