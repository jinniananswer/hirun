package com.hirun.app.biz.task.out;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.out.hirunplusdata.ShareProductDataImport;
import com.hirun.app.bean.out.hirunplusdata.ShareProductSendDataImport;
import com.hirun.app.dao.out.DataGetTimeDAO;
import com.hirun.pub.domain.out.DataGetTimeEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;

import java.time.temporal.ChronoUnit;

/**
 * @author
 *中间产品推送采集
 */
public class ShareProductImportTaskService extends GenericService {

    public ServiceResponse hirunplusDataImport(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String now = TimeTool.now();
        DataGetTimeDAO dataGetTimeDAO = DAOFactory.createDAO(DataGetTimeDAO.class);
        DataGetTimeEntity dataGetTimeEntity = dataGetTimeDAO.getDataGetTimeEntityByGetType("HIRUNPLUS_PRODSHAREDATA");
        String start = null;
        if(dataGetTimeEntity == null) {
            start = now;
        } else {
            String indbTime = dataGetTimeEntity.getIndbTime();
            start = TimeTool.addTime(indbTime, TimeTool.TIME_PATTERN, ChronoUnit.MINUTES, -5);
        }

        ShareProductDataImport.dataImport(start, now);

        ShareProductSendDataImport.dataImport(start, now);


        if(dataGetTimeEntity == null) {
            dataGetTimeEntity = new DataGetTimeEntity();
            dataGetTimeEntity.setGetType("HIRUNPLUS_PRODSHAREDATA");
            dataGetTimeEntity.setIndbTime(now);
            dataGetTimeDAO.insert("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
        } else {
            dataGetTimeEntity.setIndbTime(now);
            dataGetTimeDAO.update("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
        }

        return response;
    }


}
