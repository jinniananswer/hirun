package com.hirun.app.biz.task.out;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.out.hirunplusdata.GZGZHDataImport;
import com.hirun.app.bean.out.hirunplusdata.LTZDSTSDataImport;
import com.hirun.app.bean.out.hirunplusdata.SCANDataImport;
import com.hirun.app.bean.out.hirunplusdata.XQLTYTSDataImport;
import com.hirun.app.bean.plan.ActionCheckRuleProcess;
import com.hirun.app.bean.plan.PlanBean;
import com.hirun.app.dao.out.DataGetTimeDAO;
import com.hirun.pub.domain.out.DataGetTimeEntity;
import com.most.core.app.database.dao.GenericDAO;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import com.most.core.pub.tools.transform.ConvertTool;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-17.
 */
public class DataImportTaskService extends GenericService {

    public ServiceResponse hirunplusDataImport(ServiceRequest request) throws Exception {
        ServiceResponse response = new ServiceResponse();
        JSONObject requestData = request.getBody().getData();
        String now = TimeTool.now();
        DataGetTimeDAO dataGetTimeDAO = DAOFactory.createDAO(DataGetTimeDAO.class);
        DataGetTimeEntity dataGetTimeEntity = dataGetTimeDAO.getDataGetTimeEntityByGetType("HIRUNPLUS_ACTION");
        String start = null;
        if(dataGetTimeEntity == null) {
            start = now;
        } else {
            String indbTime = dataGetTimeEntity.getIndbTime();
            start = TimeTool.addTime(indbTime, TimeTool.TIME_PATTERN, ChronoUnit.SECONDS, 1);
        }

//        GZGZHDataImport.dataImport(start, now);
//        SCANDataImport.dataImport(start, now);
        LTZDSTSDataImport.dataImport(start, now);
//        XQLTYTSDataImport.dataImport(start, now);

        if(dataGetTimeEntity == null) {
            dataGetTimeEntity = new DataGetTimeEntity();
            dataGetTimeEntity.setGetType("HIRUNPLUS_ACTION");
            dataGetTimeEntity.setIndbTime(now);
            dataGetTimeDAO.insert("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
        } else {
            dataGetTimeEntity.setIndbTime(now);
            dataGetTimeDAO.update("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
        }

        return response;
    }
}
