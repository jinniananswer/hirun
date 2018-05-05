package com.hirun.pub.tool;

import com.most.core.pub.tools.time.TimeTool;

import java.time.temporal.ChronoUnit;

/**
 * Created by pc on 2018-04-29.
 */
public class PlanTool {

    public static String getPlanDate() {
        String planDate = null;
        String now = TimeTool.now();
        int hour = Integer.parseInt(TimeTool.now("HH"));
        if(hour < 12) {
            planDate = now;
        } else {
            planDate = TimeTool.addTime(now, TimeTool.TIME_PATTERN, ChronoUnit.DAYS, 1);
        }

        planDate = planDate.substring(0, 10);

        return planDate;
    }

    public static String getPlanDate4Summarize() {
        String planDate = null;
        String now = TimeTool.now();
        int hour = Integer.parseInt(TimeTool.now("HH"));
        if(hour < 10) {
            planDate = TimeTool.addTime(now, TimeTool.TIME_PATTERN, ChronoUnit.DAYS, -1);
        } else {
            planDate = now;
        }

        planDate = planDate.substring(0, 10);

        return planDate;
    }

    public static void main(String[] args) {
        System.out.println(PlanTool.getPlanDate());
    }
}
