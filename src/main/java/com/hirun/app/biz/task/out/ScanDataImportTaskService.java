package com.hirun.app.biz.task.out;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.employee.EmployeeBean;
import com.hirun.app.bean.out.hirunplusdata.*;
import com.hirun.app.cache.HirunPlusStaffDataCache;
import com.hirun.app.dao.out.DataGetTimeDAO;
import com.hirun.app.dao.user.UserDAO;
import com.hirun.pub.domain.entity.user.UserEntity;
import com.hirun.pub.domain.out.DataGetTimeEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.GenericService;
import com.most.core.app.session.AppSession;
import com.most.core.app.session.SessionManager;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
import com.most.core.pub.tools.time.TimeTool;
import org.apache.commons.lang3.StringUtils;

import java.time.temporal.ChronoUnit;


public class ScanDataImportTaskService extends GenericService {

    public ServiceResponse hirunplusDataImport(ServiceRequest request) throws Exception {

        ServiceResponse response = new ServiceResponse();


        JSONObject requestData = request.getBody().getData();
        String inModeCode=requestData.getString("IN_MODE_CODE");
        String now = TimeTool.now();

        DataGetTimeDAO dataGetTimeDAO = DAOFactory.createDAO(DataGetTimeDAO.class);
        DataGetTimeEntity dataGetTimeEntity = dataGetTimeDAO.getDataGetTimeEntityByGetType("HIRUNPLUS_SCANDATA");
        String start = null;

        if(StringUtils.equals("0",inModeCode)){
            String employeeId= requestData.getString("EMPLOYEE_ID");

            UserDAO userDAO=DAOFactory.createDAO(UserDAO.class);
            UserEntity userEntity=userDAO.queryUserByEmployeeId(employeeId);
            String staffId="";
            if(userEntity==null){
                staffId="";
            }else{
                staffId=HirunPlusStaffDataCache.getStaffIdByMobile(userEntity.getMobileNo());
            }

            start = TimeTool.addTime(now, TimeTool.TIME_PATTERN, ChronoUnit.MINUTES, -10);
            CustServiceScandataImport.dataImport(start, now,staffId);
        }else {
            if(dataGetTimeEntity == null) {
                start = now;
            } else {
                String indbTime = dataGetTimeEntity.getIndbTime();
                start = TimeTool.addTime(indbTime, TimeTool.TIME_PATTERN, ChronoUnit.MINUTES, -5);
            }
            CustServiceScandataImport.dataImport(start, now,"");

            if(dataGetTimeEntity == null) {
                dataGetTimeEntity = new DataGetTimeEntity();
                dataGetTimeEntity.setGetType("HIRUNPLUS_SCANDATA");
                dataGetTimeEntity.setIndbTime(now);
                dataGetTimeDAO.insert("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
            } else {
                dataGetTimeEntity.setIndbTime(now);
                dataGetTimeDAO.update("OUT_DATA_GET_TIME", dataGetTimeEntity.getContent());
            }
        }

        return response;
    }


}
