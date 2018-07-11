package com.hirun.app.task;

import com.alibaba.fastjson.JSONObject;
import com.hirun.app.bean.cust.CustBean;
import com.hirun.app.dao.common.HisPerformDueTaskDAO;
import com.hirun.app.dao.common.PerformDueTaskDAO;
import com.hirun.pub.domain.entity.common.PerformDueTaskEntity;
import com.most.core.app.database.dao.factory.DAOFactory;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.pub.data.ServiceRequest;
import org.omg.IOP.ENCODING_CDR_ENCAPS;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc on 2018-05-20.
 */
public class PerformDueTask implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map parameter = new HashMap();
        JSONObject jsonObject = new JSONObject(parameter);
        ServiceRequest request = new ServiceRequest(jsonObject);
        try {
            ServiceInvoker.invoke("Common.common.PerformDueTaskService.deal", request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
