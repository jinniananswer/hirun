package com.hirun.pub.websocket;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by pc on 2018-06-08.
 */
public class WebSocketMapUtil {

    public static ConcurrentMap<String, MsgWebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    public static void put(String key, MsgWebSocketServer msgWebSocketServer){
        webSocketMap.put(key, msgWebSocketServer);
    }

    public static MsgWebSocketServer get(String key){
        return webSocketMap.get(key);
    }

    public static void remove(String key){
        webSocketMap.remove(key);
    }

    public static Collection<MsgWebSocketServer> getValues(){
        return webSocketMap.values();
    }
}
