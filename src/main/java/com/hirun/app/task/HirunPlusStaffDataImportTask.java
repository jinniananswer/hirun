package com.hirun.app.task;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.pub.data.ServiceRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2018-05-20.
 */
public class HirunPlusStaffDataImportTask implements Job {

    private static Logger log = LogManager.getLogger(HirunPlusStaffDataImportTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map parameter = new HashMap();
        JSONObject jsonObject = new JSONObject(parameter);
        ServiceRequest request = new ServiceRequest(jsonObject);
        try {
            ServiceInvoker.invoke("OperationCenter.plan.DataImportTaskService.hirunplusStaffDataImport", request);
        } catch (Exception e) {
            log.error("同步员工信息失败：", e);
        }
    }
}
