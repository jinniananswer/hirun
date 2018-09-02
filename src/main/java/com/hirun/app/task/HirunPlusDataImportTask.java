package com.hirun.app.task;

import com.alibaba.fastjson.JSONObject;
import com.most.core.app.service.invoker.ServiceInvoker;
import com.most.core.pub.data.ServiceRequest;
import com.most.core.pub.data.ServiceResponse;
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
public class HirunPlusDataImportTask implements Job {

    private static Logger log = LogManager.getLogger(HirunPlusDataImportTask.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map parameter = new HashMap();
        JSONObject jsonObject = new JSONObject(parameter);
        ServiceRequest request = new ServiceRequest(jsonObject);
        try {
            ServiceInvoker.invoke("OperationCenter.plan.DataImportTaskService.hirunplusDataImport", request);
        } catch (Exception e) {
            log.error("数据导入后台任务执行失败：", e);
        }
    }
}
