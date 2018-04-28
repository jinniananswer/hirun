package com.most.core.pub.tools.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static void main(String[] args){
        System.out.println(now());
    }
}
