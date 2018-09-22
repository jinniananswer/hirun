package com.hirun.app.biz.out.hirunplus;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.Utility;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;

import java.util.Map;

/**
 * @Author awx
 * @Date 2018/4/17 21:11
 * @Description:
 */
public class HirunPlusService extends GenericService{

    /**
     * 删除的客户恢复
     * @param request
     * @return
     * @throws Exception
     */
    public ServiceResponse syncSubscription(ServiceRequest request) throws Exception{
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        GenericDAO dao = new GenericDAO("out");
        String now = TimeTool.now();

        Utility.checkEmptyParam(requestData, new String[] {"uid","subscribe_time","openid"});
        JSONObject dbParam = new JSONObject();
        ConvertTool.copy(requestData, new String[] {"nickname","uid","subscribe_time","openid"},
                dbParam, new String[] {"NICKNAME","UID","SUBSCRIBE_TIME","OPENID"});

        dbParam.put("INDB_TIME", now);
        dbParam.put("DEAL_TAG", "0");
        dao.insert("out_hirunplus_reg_2",ConvertTool.toMap(dbParam));

        return response;
    }
}
