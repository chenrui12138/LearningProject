package com.jcxx.saas.modules.mrs.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcxx.saas.modules.mrs.entity.MrsTimeConfigEntity;
import com.jcxx.saas.modules.mrs.entity.MyMeetingTimeEntity;
import com.jcxx.saas.modules.mrs.service.MrsMeetingService;
import com.jcxx.saas.modules.mrs.service.MrsTimeConfigService;
import com.jcxx.saas.modules.mrs.utils.DateTime;
import com.jcxx.saas.modules.mrs.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @Author wll
 * @Date 2021/7/14 10:10
 * @Description
 */

@RestController
@RequestMapping("/mrs")
public class MrsTimeConfigController {
    @Autowired
    private MrsTimeConfigService timeConfigService;

    /**
     * cr
     * 查询出可以预约的时间
     */
    @Autowired
    private MrsMeetingService mrsMeetingService;

    @GetMapping("/selectMeetingTime")
    public Result selectMeetingTime(String date, Integer meetingRoomId) {

        if ("".equals(date)) {
            return Result.error("请输入需要查询的时间");
        }
        if (null == meetingRoomId) {
            return Result.error("请输入需要查询的会议室id");
        }

        //查询出已预约的时间段
        List<MyMeetingTimeEntity> list = mrsMeetingService.selectOneDayMeeting(date, meetingRoomId);
        //查询出会议室所有时间段
        List<MyMeetingTimeEntity> list1 = timeConfigService.selectAllMeetingTime(date, meetingRoomId);

        if (list != null) {
            //去除已经预约的会议时间，查询出未预约的时间
            for (int i = 0; i < list.size(); i++) {
                //取出已经预约时间的会议结束时间
                String endTime1 = list.get(i).getEndTime();
                String startTime1 = list.get(i).getStartTime();

                for (int j = 0; j < list1.size(); j++) {
                    //如果已经预约会议的开始结束时间在此时间段内则去除
                    String startTime = list1.get(j).getStartTime();
                    String endTime = list1.get(j).getEndTime();
                    if (DateTime.DateTimeState(endTime1, startTime, endTime) || DateTime.DateTimeState(startTime1, startTime, endTime)) {
                        list1.remove(j);
                    }
                }
            }
            //控制会议场次不超过四场
            for (int i = 1; i < list1.size() + 1; i++) {
                if (list.size() + list1.size() > 4) {
                    list1.remove(list1.size() - i);
                }
            }
            //如果当前时间大于配置时间的结束时间则去除
            for (int j = 0; j < list1.size(); j++) {
                String startTime = list1.get(j).getStartTime();
                String endTime = list1.get(j).getEndTime();
                Integer integer = DateTime.timeState(date, startTime, endTime);
                if (integer == 0) {
                    list1.remove(j);
                    j = j-1;
                }
            }
            for (MyMeetingTimeEntity myMeetingTimeEntity : list1) {
                myMeetingTimeEntity.setStartTime(DateTime.TimeFormat(myMeetingTimeEntity.getStartTime()));
                myMeetingTimeEntity.setEndTime(DateTime.TimeFormat(myMeetingTimeEntity.getEndTime()));
            }
            return Result.ok().put("list1", list1);
        } else {

            //如果当前时间大于配置时间的结束时间则去除
            for (int j = 0; j < list1.size(); j++) {
                //如果已经预约会议的开始结束时间在此时间段内则去除
                String startTime = list1.get(j).getStartTime();
                String endTime = list1.get(j).getEndTime();
                Integer integer = DateTime.timeState(date, startTime, endTime);
                if (integer == 0) {
                    list1.remove(j);
                    j = j-1;
                }
            }
            for (MyMeetingTimeEntity myMeetingTimeEntity : list1) {
                myMeetingTimeEntity.setStartTime(DateTime.TimeFormat(myMeetingTimeEntity.getStartTime()));
                myMeetingTimeEntity.setEndTime(DateTime.TimeFormat(myMeetingTimeEntity.getEndTime()));
            }
            return Result.ok().put("list1", list1);
        }

    }

    /**
     * wll
     * 查询时间
     *
     * @return
     */
    @GetMapping("/meetingRoom/selectTime")
    public Result selectTime() {
        List<MrsTimeConfigEntity> timeConfigEntities = timeConfigService.selectTime();
        return Result.ok().put("time", timeConfigEntities);
    }

    /**
     * wll
     * 修改时间，
     *
     * @param
     * @return
     */
    @PostMapping("/meetingRoom/updateTime")
    public Result updateTime(@RequestBody Map<String, String> timeMap) {

        String[] strings = new String[]{"One", "Two", "Three", "Four"};
        int temp = 0;
        for (int i = 1; i <= 4; i++) {
            List<MrsTimeConfigEntity> mrsTimeConfigEntities1 = timeConfigService.selectByStage(i);
            for (MrsTimeConfigEntity mrsTimeConfigEntity : mrsTimeConfigEntities1) {
                mrsTimeConfigEntity.setUpdateTime(DateTime.nowDateTime());
                mrsTimeConfigEntity.setUpdateId(Integer.valueOf(timeMap.get("updateId")));
                mrsTimeConfigEntity.setStartTime(timeMap.get("startTime" + strings[i - 1]));
                mrsTimeConfigEntity.setEndTime(timeMap.get("endTime" + strings[i - 1]));
                int i1 = timeConfigService.updateTime(mrsTimeConfigEntity);
                temp += i1;
            }
        }
        return temp == 0 ? Result.error() : Result.ok();
    }
}