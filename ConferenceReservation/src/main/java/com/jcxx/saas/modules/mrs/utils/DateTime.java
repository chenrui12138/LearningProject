package com.jcxx.saas.modules.mrs.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jcxx.saas.modules.mrs.entity.UserMeetingVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateTime {

    public static String TimeFormat(String time){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = simpleDateFormat.format(parse);

        return format;
    }

    /**
     * wll 获取当前日期时间
     * @return
     */
    public static String nowDateTime(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(date);
        return format1;
    }

    /**
     * wll 获取当前日期
     * @return
     */
    public static String nowDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * wll 获取当前时间
     * @return
     */
    public static String nowTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * wll 对时间进行比较
     * @return 0:已结束 1：在时间段内 2：未开始
     * @throws ParseException
     */
    public static Integer timeState(String date,String startTime,String endTime){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = null;
        Date stime = null;
        Date etime = null;
        try {
            stime = format.parse(date + " " + startTime);
            nowDate = format.parse(DateTime.nowDateTime());
            etime = format.parse(date + " " + endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(stime.getTime() <= nowDate.getTime() && etime.getTime() >= nowDate.getTime()){
            return 1;
        }
        if(etime.getTime() <= nowDate.getTime()){
            return 0;
        }
        if(stime.getTime() >= nowDate.getTime()){
            return 2;
        }
        return null;
    }

    /**
     * wll 确定会议当前状态
     * @param page
     * @return
     */
    public static IPage<UserMeetingVO> nowState(IPage<UserMeetingVO> page){
        List<UserMeetingVO> meetingEntities = page.getRecords();
        for (UserMeetingVO meetingEntity : meetingEntities) {
            Integer timeState = DateTime.timeState(meetingEntity.getDate(), meetingEntity.getStartTime(), meetingEntity.getEndTime());
            meetingEntity.setStartTime(DateTime.TimeFormat(meetingEntity.getStartTime()));
            meetingEntity.setEndTime(DateTime.TimeFormat(meetingEntity.getEndTime()));
            meetingEntity.setState(timeState);
        }
        return page;
    }

   /**
    *  对两个时间进行比较
    * @Author cr
    * @Date 2021/7/20 9:13
    * @Description
    *
    */
    public static boolean TimeState(String startTime, String endTime) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date stime = null;
        Date etime = null;
        try {
            stime = format.parse(startTime);
            etime = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (stime.getTime() > etime.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断一个时间在一个时间段内
     * @Author cr
     * @Date 2021/7/20 9:13
     * @Description
     *
     */
    public static boolean DateTimeState(String time, String startTime, String endTime) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date time1 = null;
        Date stime = null;
        Date etime = null;
        try {
            stime = format.parse(startTime);
            time1 = format.parse(time);
            etime = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (stime.getTime() <= time1.getTime() && etime.getTime() >= time1.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  对两个时间是否相等进行比较
     * @Author cr
     * @Date 2021/7/20 9:13
     * @Description
     *
     */
    public static boolean TimeState1(String time1, String time2) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date stime = null;
        Date etime = null;
        try {
            stime = format.parse(time1);
            etime = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (stime.getTime() == etime.getTime()) {
            return true;
        } else {
            return false;
        }
    }
}
