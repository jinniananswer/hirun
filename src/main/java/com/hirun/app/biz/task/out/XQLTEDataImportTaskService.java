package com.hirun.app.biz.task.out;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.out.hirunplusdata.CustServiceScandataImport;
import com.hirun.app.bean.out.hirunplusdata.XQLTEdataImport;
import com.hirun.app.dao.out.DataGetTimeDAO;
import com.hirun.pub.domain.out.DataGetTimeEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;

import java.time.temporal.ChronoUnit;


public class XQLTEDataImportTaskService extends GenericService {

    public ServiceResponse hirunplusDataImport(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String now = TimeTool.now();
        DataGetTimeDAO dataGetTimeDAO = DAOFactory.createDAO(DataGetTimeDAO.class);
        DataGetTimeEntity dataGetTimeEntity = dataGetTimeDAO.getDataGetTimeEntityByGetType("HIRUNPLUS_XQLTEDATA");
        String start = null;
        if(dataGetTimeEntity == null) {
            start = now;
        } else {
            String indbTime = dataGetTimeEntity.getIndbTime();
            start = TimeTool.addTime(indbTime, TimeTool.TIME_PATTERN, ChronoUnit.MINUTES, -5);
        }

        XQLTEdataImport.dataImport(start, now);

        if(dataGetTimeEntity == null) {
            dataGetTimeEntity = new DataGetTimeEntity();
            dataGetTimeEntity.setGetType("HIRUNPLUS_XQLTEDATA");
            dataGetTimeEntity.setIndbTime(now);
            dataGetTimeDAO.insert("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
        } else {
            dataGetTimeEntity.setIndbTime(now);
            dataGetTimeDAO.update("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
        }

        return response;
    }


}
