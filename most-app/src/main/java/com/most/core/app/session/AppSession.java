package com.most.core.app.session;

import com.most.core.app.database.conn.ConnectionFactory;
import com.most.core.app.database.wrapper.ConnectionWrapper;
import com.most.core.pub.data.SessionEntity;
import com.most.core.pub.tools.datastruct.MapTool;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Author jinnian
 * @Date 2018/4/15 18:19
 * @Description:
 */
public class AppSession{

    private transient Logger log = LogManager.getLogger(AppSession.class.getName());

    private Map<String, ConnectionWrapper> connections = new HashMap<String, ConnectionWrapper>();

    private String createTime;

    private String sessionId;

    private SessionEntity sessionEntity;

    public AppSession(){
        this.createTime = TimeTool.now();
        this.sessionId = UUID.randomUUID().toString();
    }

    public ConnectionWrapper getConnection(String databaseName){
        if(connections.containsKey(databaseName)){
            return connections.get(databaseName);
        }

        ConnectionWrapper conn = ConnectionFactory.getConnection(databaseName);
        connections.put(databaseName, conn);
        return conn;
    }

    public void commit(){
        if(MapTool.isEmpty(connections)){
            return;
        }
        Set<String> keys = connections.keySet();
        for(String key : keys){
            ConnectionWrapper conn = connections.get(key);
            try {
                conn.commit();
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    public void rollback(){
        if(MapTool.isEmpty(connections)){
            return;
        }
        Set<String> keys = connections.keySet();
        for(String key : keys){
            ConnectionWrapper conn = connections.get(key);
            try {
                conn.rollback();
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    public void close(){
        if(MapTool.isEmpty(connections)){
            return;
        }
        Set<String> keys = connections.keySet();
        for(String key : keys){
            ConnectionWrapper conn = connections.get(key);
            try {
                conn.close();
            } catch (SQLException e) {
                log.error(e);
            }
        }
    }

    public <K extends SessionEntity> K getSessionEntity(){
        return (K)this.sessionEntity;
    }

    public <K extends SessionEntity> void setSessionEntity(K sessionEntity){
        this.sessionEntity = sessionEntity;
    }

    public String getCreateTime(){
        return this.createTime;
    }
}
