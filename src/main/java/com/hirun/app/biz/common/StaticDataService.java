package com.hirun.app.biz.common;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.database.tools.StaticDataTool;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.transform.ConvertTool;

/**
 * Created by pc on 2018-05-17.
 */
public class StaticDataService extends GenericService {

    public ServiceResponse getCodeTypeDatas(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String code_type = requestData.getString("CODE_TYPE");
        response.set("STATICDATA_LIST",ConvertTool.toJSONArray(StaticDataTool.getCodeTypeDatas(code_type)));

        return response;
    }
}
