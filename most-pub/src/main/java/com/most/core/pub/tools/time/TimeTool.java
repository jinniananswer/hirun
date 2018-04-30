package com.most.core.pub.tools.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Author jinnian
 * @Date 2018/4/16 12:05
 * @Description:
 */
public class TimeTool {

    public static String now(){
        LocalDateTime localDateTime =  LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String now = localDateTime.format(formatter);
        return now;
    }

    public static String now(String format){
        LocalDateTime localDateTime =  LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String now = localDateTime.format(formatter);
        return now;
    }

    public static String today(){
        return now("yyyy-MM-dd");
    }

    public static String addMonths(String dateTime, String pattern,  int month){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate time = LocalDate.parse(dateTime,dateTimeFormatter);
        LocalDate nextTime = time.plus(month, ChronoUnit.MONTHS);
        return nextTime.format(dateTimeFormatter);
    }

    public static String addMonths(int month){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime nextTime = LocalDateTime.now().plus(month, ChronoUnit.MONTHS);
        return nextTime.format(dateTimeFormatter);
    }

    public static void main(String[] args){
        System.out.println(addMonths("2018-04-19", "yyyy-MM-dd", 24));
    }
}
