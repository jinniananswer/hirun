package com.hirun.pub.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by pc on 2018-06-08.
 */
@ClientEndpoint()
public class MsgWebSocketClient {

    private static Logger logger = LoggerFactory.getLogger(MsgWebSocketClient.class);

    private static MsgWebSocketClient client = null;

    private Session session;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
    /**
     * 连接关闭调用的方法
     * @throws Exception
     */
    @OnClose
    public void onClose() throws Exception{
    }

    /**
     * 关闭链接方法
     * @throws IOException
     */
    public void closeSocket() throws IOException{
        this.session.close();
    }

    /**
     * 发送消息方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    //启动客户端并建立链接
    public void start(String uri) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            this.session = container.connectToServer(MsgWebSocketClient.class, URI.create(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(WebSocketMsg webSocketMsg) {
        if(client == null) {
            try{
                client = new MsgWebSocketClient();
                client.start("ws://localhost:8080/websocketServer/server");
            } catch(Exception e) {
                logger.error("连接websocket失败",e);
            }
        }
        try {
            client.sendMessage(webSocketMsg.toJSONString());
        } catch (Exception e) {
            logger.error("发送websocket消息失败:{}",e);
        }
    }

    public static void main(String[] args) throws Exception {
        MsgWebSocketClient client = new MsgWebSocketClient();
        client.start("ws://localhost:8080/websocketServer/server123");
        try{
            client.sendMessage("测试一下");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.closeSocket();
            } catch(Exception e) {

            }
        }
    }
}
