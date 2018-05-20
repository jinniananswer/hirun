package com.most.core.pub.backgroundtask;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2018-05-20.
 */
public class BackgroundTaskFactory {

    private static List<CronBackgroundTask> cronBackgroundTaskList = new ArrayList<CronBackgroundTask>();

    public static void start() {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            for(CronBackgroundTask task : cronBackgroundTaskList) {
                scheduler.scheduleJob(task.getJob(), task.getTrigger());
            }
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    static{
        cronBackgroundTaskList = BackgroundTaskXml.getCronBackgroundTask();
    }
}
