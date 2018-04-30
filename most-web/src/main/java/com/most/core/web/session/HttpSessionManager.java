package com.most.core.web.session;

import com.most.core.pub.data.SessionEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jinnian
 * @Date 2018/4/30 15:05
 * @Description:
 */
public class HttpSessionManager {

    private static Map<String, SessionEntity> cache = new HashMap<String, SessionEntity>();

    public static <K extends SessionEntity> K getSessionEntity(String sessionId){
        return (K) cache.get(sessionId);
    }

    public static <K extends SessionEntity> void putSessionEntity(String sessionId, K sessionEntity){
        cache.put(sessionId, sessionEntity);
    }
}
