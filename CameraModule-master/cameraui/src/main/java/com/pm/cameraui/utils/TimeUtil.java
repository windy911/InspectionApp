package com.pm.cameraui.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static String recordString  = "%1$s:%2$s:%3$s";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
    private static SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:MM");
    /**
     * 格式化时间（xx天xx时xx分xx秒）
     * @return
     */
    public static String formatTime(long ms) {
        if (ms == 0) {
        return "";
        }
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;

        long hour = ms / hh;
        long minute = (ms - hour * hh) / mi;
        long seconds = (ms - hour * hh - minute * mi) / ss;

        return String.format(recordString,hour < 10 ? "0"+hour : hour,minute < 10? "0"+minute : minute ,seconds <10? "0"+seconds : seconds);
    }

    /**
     * 返回格式 yyyy-MM-dd HH:MM:ss
     * @param time
     * @return
     */
    public static String getFormatDateTime(long time){
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * 返回格式 yyyy-MM-dd
     * @param time
     * @return
     */
    public static String getFormatDate(long time){
        return dateFormatDate.format(new Date(time));
    }

    /**
     * 返回格式 yyyy-MM-dd HH:MM
     * @param time
     * @return
     */
    public static String getFormatDateTimeString(long time){
        return dateFormatDateTime.format(new Date(time));
    }
}
