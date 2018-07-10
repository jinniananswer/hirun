package com.hirun.app.bean.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.dao.common.MsgDAO;
import com.hirun.app.dao.common.PerformDueTaskDAO;
import com.hirun.pub.domain.entity.common.MsgEntity;
import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.hirun.pub.domain.enums.common.MsgType;
import com.hirun.pub.websocket.MsgWebSocketClient;
import com.hirun.pub.websocket.WebSocketMsg;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.List;

/**
 * Created by pc on 2018-06-09.
 */
public class PerformDueTaskBean {

    public static void addPerformDueTask(PerformDueTaskEntity performDueTaskEntity) throws Exception {
        PerformDueTaskDAO dao = DAOFactory.createDAO(PerformDueTaskDAO.class);
        dao.insert("INS_PERFORM_DUE_TASK", performDueTaskEntity.getContent());
    }
}
