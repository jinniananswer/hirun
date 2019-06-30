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


public class HirunPlusSignInDataImportTask implements Job {

    private static Logger log = LogManager.getLogger(HirunPlusSignInDataImportTask.class);

    /**
     * 扫码登陆PC端数据获取
     * @param
     * @throws
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map parameter = new HashMap();
        JSONObject jsonObject = new JSONObject(parameter);
        ServiceRequest request = new ServiceRequest(jsonObject);
        try {
            ServiceInvoker.invoke("OperationCenter.custservice.SignInDataImportTaskService.hirunplusDataImport", request);
        } catch (Exception e) {
            log.error("数据导入后台任务执行失败：", e);
        }
    }
}
