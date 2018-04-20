package com.most.core.app.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author jinnian
 * @Date 2018/4/16 11:20
 * @Description:
 */
public class SessionManager {

    private static Logger log = LogManager.getLogger(SessionManager.class.getName());

    private static final ThreadLocal<AppSession> threadSession = new ThreadLocal<AppSession>();

    public static AppSession getSession(){
        AppSession session = threadSession.get();
        if(session == null){
            session = new AppSession();
            threadSession.set(session);
        }
        return session;
    }

    public static void destroy(){
        threadSession.remove();
    }
}
