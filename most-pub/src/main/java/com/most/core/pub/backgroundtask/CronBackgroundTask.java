package com.most.core.pub.backgroundtask;

import org.quartz.JobDetail;
import org.quartz.Trigger;

/**
 * Created by pc on 2018-05-20.
 */
public class CronBackgroundTask {

    private String taskName;
    private Trigger trigger;
    private JobDetail job;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public JobDetail getJob() {
        return job;
    }

    public void setJob(JobDetail job) {
        this.job = job;
    }
}
