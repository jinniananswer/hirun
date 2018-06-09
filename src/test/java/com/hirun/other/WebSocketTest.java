package com.hirun.other;

import com.alibaba.fastjson.JSONObject;
import com.hirun.pub.websocket.MsgWebSocketClient;
import org.junit.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

/**
 * Created by pc on 2018-06-08.
 */
public class WebSocketTest {

    @Test
    public void socketTest() {
        MsgWebSocketClient client = new MsgWebSocketClient();
        client.start("ws://localhost:8080/websocketServer/server");
        try{
            JSONObject json = new JSONObject();
            json.put("TYPE","SERVER");
            json.put("EMPLOYEE_ID","222");
            json.put("CONTENT","太棒了2222222222");
            client.sendMessage(json.toJSONString());
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
