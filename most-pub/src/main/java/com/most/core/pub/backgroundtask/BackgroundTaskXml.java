package com.most.core.pub.backgroundtask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.quartz.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2018-05-20.
 */
public class BackgroundTaskXml {

    protected static Logger log = LogManager.getLogger(BackgroundTaskXml.class.getClass().getName());

    public static List<CronBackgroundTask> getCronBackgroundTask() {
        List<CronBackgroundTask> list = new ArrayList<CronBackgroundTask>();
        SAXReader reader = new SAXReader();
        InputStream ins = null;
        Element root = null;
        try{
            ins = BackgroundTaskFactory.class.getClassLoader().getResourceAsStream("background_task.xml");
            if(ins == null) {
                throw new IOException("没有找到配置文件");
            }
            Document document = reader.read(ins);
            root = document.getRootElement();
        } catch(Exception e) {
            log.error("解析错误", e);
            return new ArrayList<CronBackgroundTask>();
        } finally {
            if (null != ins) {
                try {
                    ins.close();
                } catch (IOException e) {
                    log.error("关闭本地缓存配置文件句柄错误!", e);
                }
            }
        }

        List elementTaskList = root.element("cronTask").elements();
        for(int i = 0, size = elementTaskList.size(); i < size; i++) {
            Element elementTask = (Element)elementTaskList.get(i);
            CronBackgroundTask task = new CronBackgroundTask();
            String taskName = elementTask.attributeValue("taskName");
            String cronExpr = elementTask.attributeValue("cronExpr");
            String className = elementTask.attributeValue("className");

            task.setTaskName(elementTask.attributeValue("taskName"));

            Class clazz = null;
            try{
                clazz = Class.forName(className);
            } catch(Exception e) {
                continue;
            }

            JobDetail job = JobBuilder.newJob(clazz)
                    .withIdentity(taskName, "group")
                    .build();
            task.setJob(job);

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(taskName, "group")
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
                    .build();
            task.setTrigger(trigger);

            list.add(task);
        }

        return list;
    }
}
