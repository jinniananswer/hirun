package com.hirun.app.biz.common;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.common.MsgBean;
import com.hirun.app.dao.common.MsgDAO;
import com.hirun.pub.domain.entity.common.MsgEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.List;

/**
 * Created by pc on 2018-06-09.
 */
public class MsgService extends GenericService {

    public ServiceResponse queryMsgListByRecvId(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String recvId = requestData.getString("RECV_ID");
        MsgDAO msgDAO = DAOFactory.createDAO(MsgDAO.class);

        List<MsgEntity> msgEntityList = msgDAO.queryMsgListByRecvId(recvId);

        response.set("MSG_LIST", MsgBean.setDescAndTOJSONArray(msgEntityList));

        return response;
    }

    public ServiceResponse getUnReadMsgCount(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String recvId = requestData.getString("RECV_ID");
        MsgDAO msgDAO = DAOFactory.createDAO(MsgDAO.class);

        int num = msgDAO.queryUnReadMsgCountByRecvId(recvId, "0");

        response.set("UNREAD_NUM", num);

        return response;
    }

    public ServiceResponse setMsgListToRead(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String recvId = requestData.getString("RECV_ID");
        MsgDAO msgDAO = DAOFactory.createDAO(MsgDAO.class);

        msgDAO.updateMsgStatusByRecvId(recvId, "1", "0");
        return response;
    }
}
