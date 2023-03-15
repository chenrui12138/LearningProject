package com.jcxx.saas.modules.mrs.controller;

import com.jcxx.saas.modules.mrs.entity.MrsMeetingRoomEntity;
import com.jcxx.saas.modules.mrs.entity.MrsTimeConfigEntity;
import com.jcxx.saas.modules.mrs.entity.MyMrsMeetingRoomEntity;
import com.jcxx.saas.modules.mrs.service.MrsMeetingRoomService;
import com.jcxx.saas.modules.mrs.service.MrsTimeConfigService;
import com.jcxx.saas.modules.mrs.utils.DateTime;
import com.jcxx.saas.modules.mrs.utils.ExcelUtil;
import com.jcxx.saas.modules.mrs.utils.ResponseUtil;
import com.jcxx.saas.modules.mrs.utils.Result;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/mrs")
public class MrsMeetingRoomController {
    @Autowired
    private MrsMeetingRoomService roomService;

    @Autowired
    private MrsTimeConfigService timeConfigService;

    /**
     * cr
     * 查询某天某个会议室的预约情况
     *
     * @param date
     * @param meetingRoomId
     * @return
     */
    @GetMapping("/selectMeetingRoom")
    public Result selectMeetingRoom(String date, Integer meetingRoomId) {
        if ("".equals(date)) {
            return Result.error("请输入需要查询的时间");
        }
        if (null == meetingRoomId) {
            return Result.error("请输入需要查询的会议室id");
        }
        //查询出已经预约的会议
        List<MyMrsMeetingRoomEntity> myMrsMeetingRoomEntities = roomService.selectMeetingRoom(date, meetingRoomId);

        //查询会议室的预约时间段  固定为4段
        List<MyMrsMeetingRoomEntity> myMrsMeetingRoomEntities1 = roomService.selectNoMeetingRoom(date, meetingRoomId);
        if (myMrsMeetingRoomEntities != null) {
            for (int i = 0; i < myMrsMeetingRoomEntities.size(); i++) {
                MyMrsMeetingRoomEntity myMrsMeetingRoomEntity = myMrsMeetingRoomEntities.get(i);
                //myMrsMeetingRoomEntities.size()作为会议室可不可预约的状态值
                //4:已满  123：可预约   默认值为0，空闲
                myMrsMeetingRoomEntity.setState(myMrsMeetingRoomEntities.size());
            }

            //去除已经预约的会议时间，查询出未预约的时间
            for (int i = 0; i < myMrsMeetingRoomEntities.size(); i++) {
                //取出已经预约时间的会议结束时间
                String time = myMrsMeetingRoomEntities.get(i).getEndTime();

                for (int j = 0; j < myMrsMeetingRoomEntities1.size(); j++) {

                    //如果已经预约会议的结束时间在此时间段内则去除
                    String startTime = myMrsMeetingRoomEntities1.get(j).getStartTime();
                    String endTime = myMrsMeetingRoomEntities1.get(j).getEndTime();
                    if (DateTime.DateTimeState(time, startTime, endTime)) {
                        myMrsMeetingRoomEntities1.remove(j);
                    }
                }
            }
            //转换时间格式
            for (MyMrsMeetingRoomEntity myMrsMeetingRoomEntity : myMrsMeetingRoomEntities) {
                myMrsMeetingRoomEntity.setStartTime(DateTime.TimeFormat(myMrsMeetingRoomEntity.getStartTime()));
                myMrsMeetingRoomEntity.setEndTime(DateTime.TimeFormat(myMrsMeetingRoomEntity.getEndTime()));
            }
            for (MyMrsMeetingRoomEntity myMrsMeetingRoomEntity : myMrsMeetingRoomEntities1) {
                myMrsMeetingRoomEntity.setStartTime(DateTime.TimeFormat(myMrsMeetingRoomEntity.getStartTime()));
                myMrsMeetingRoomEntity.setEndTime(DateTime.TimeFormat(myMrsMeetingRoomEntity.getEndTime()));
            }

            for (MyMrsMeetingRoomEntity myMrsMeetingRoomEntity : myMrsMeetingRoomEntities1) {
                myMrsMeetingRoomEntity.setMeetingRoomId(meetingRoomId);
                myMrsMeetingRoomEntity.setDate(date);
                myMrsMeetingRoomEntity.setState(myMrsMeetingRoomEntities.size());
            }

            return Result.ok().put("myMrsMeetingRoomEntities", myMrsMeetingRoomEntities).put("myMrsMeetingRoomEntities1", myMrsMeetingRoomEntities1);

        } else {

            //转换时间格式
            for (MyMrsMeetingRoomEntity myMrsMeetingRoomEntity : myMrsMeetingRoomEntities1) {
                myMrsMeetingRoomEntity.setStartTime(DateTime.TimeFormat(myMrsMeetingRoomEntity.getStartTime()));
                myMrsMeetingRoomEntity.setEndTime(DateTime.TimeFormat(myMrsMeetingRoomEntity.getEndTime()));
            }
            return Result.ok().put("myMrsMeetingRoomEntities1", myMrsMeetingRoomEntities1);
        }

    }

    /**
     * cr
     * 查询某天所有会议室的预约情况
     */
    @GetMapping("/selectAllMeetingRoomAllMeeting")
    public Result selectAllMeetingRoomAllMeeting(String date) {

        if ("".equals(date)) {
            return Result.error("请输入需要查询的时间");
        }

        ArrayList<Object> objects = roomService.selectAllMeetingRoomAllMeeting(date);
        return Result.ok().put("objects", objects);
    }

    /**
     * @Author wll
     * @Date 2021/7/16 8:59
     * @Description 会议室管理主页
     */
    @GetMapping("/meetingRoom/index")
    public Result index(String name, Integer state) {
        if (name.length() > 40) {
            return Result.error("字数超过限制");
        }
        if (state == null && name.length() == 0) {
            List<MrsMeetingRoomEntity> mrsMeetingRoomEntities = roomService.selectAll();
            return Result.ok().put("meetingRoom", mrsMeetingRoomEntities);
        }
        if (state == null) {
            List<MrsMeetingRoomEntity> mrsMeetingRoomEntities = roomService.selectByName(name);
            return Result.ok().put("meetingRoom", mrsMeetingRoomEntities);
        }
        if (name.length() == 0) {
            List<MrsMeetingRoomEntity> mrsMeetingRoomEntities = roomService.selectByState(state);
            return Result.ok().put("meetingRoom", mrsMeetingRoomEntities);
        }
        List<MrsMeetingRoomEntity> mrsMeetingRoomEntities = roomService.selectByNameState(name, state);
        return Result.ok().put("meetingRoom", mrsMeetingRoomEntities);
    }

    /**
     * @Author wll
     * @Date 2021/7/16 8:59
     * @Description 通过id查询会议室
     */
    @GetMapping("/meetingRoom/selectById")
    public Result selectById(Integer id) {
        MrsMeetingRoomEntity roomEntity = roomService.selectById(id);
        return Result.ok().put("meetingRoom", roomEntity);
    }

    /**
     * @Author wll
     * @Date 2021/7/16 9:00
     * @Description 增加会议室，同时会在时间配置表里增加对应会议室id的时间
     */
    @PostMapping("/meetingRoom/insertMeetingRoom")
    private Result insertMeetingRoom(@RequestBody MrsMeetingRoomEntity roomEntity) {
        if (roomEntity.getName().length() > 40 || roomEntity.getAddress().length() > 30) {
            return Result.error("字数长度超过限制");
        }
        MrsTimeConfigEntity mrsTimeConfigEntity = new MrsTimeConfigEntity();
        String nowDate = DateTime.nowDateTime();
        roomEntity.setCreatorId(roomEntity.getCreatorId());
        roomEntity.setCreateTime(nowDate);
        roomEntity.setStatus(1);
        if (roomEntity.getState() == null) {
            roomEntity.setState(1);
        }
        int i = roomService.insertMeetingRoom(roomEntity);
        MrsMeetingRoomEntity roomEntityByTime = roomService.selectByTime(nowDate);
        List<MrsTimeConfigEntity> timeConfigEntities = timeConfigService.selectTime();
        if (timeConfigEntities.size() == 0) {
            MrsTimeConfigEntity timeConfig = new MrsTimeConfigEntity();
            for (int j = 1; j <= 4; j++) {
                timeConfig.setMeetingRoomId(roomEntityByTime.getId());
                timeConfig.setStage(j);
                if (j == 1) {
                    timeConfig.setStartTime("08:30");
                    timeConfig.setEndTime("10:00");
                }
                if (j == 2) {
                    timeConfig.setStartTime("10:30");
                    timeConfig.setEndTime("12:00");
                }
                if (j == 3) {
                    timeConfig.setStartTime("14:00");
                    timeConfig.setEndTime("15:30");
                }
                if (j == 4) {
                    timeConfig.setStartTime("16:30");
                    timeConfig.setEndTime("17:30");
                }
                timeConfig.setCreateTime(nowDate);
                timeConfig.setCreateId(roomEntity.getCreatorId());
                timeConfigService.insertTime(timeConfig);
            }
        }

        for (MrsTimeConfigEntity timeConfigEntity : timeConfigEntities) {
            mrsTimeConfigEntity.setMeetingRoomId(roomEntityByTime.getId());
            mrsTimeConfigEntity.setStartTime(timeConfigEntity.getStartTime());
            mrsTimeConfigEntity.setEndTime(timeConfigEntity.getEndTime());
            mrsTimeConfigEntity.setStage(timeConfigEntity.getStage());
            mrsTimeConfigEntity.setCreateId(roomEntity.getCreatorId());
            mrsTimeConfigEntity.setCreateTime(nowDate);
            timeConfigService.insertTime(mrsTimeConfigEntity);
        }

        return i == 0 ? Result.error() : Result.ok();
    }

    /**
     * @Author wll
     * @Date 2021/7/16 9:00
     * @Description 修改会议室
     */

    @PostMapping("/meetingRoom/updateMeetingRoom")
    private Result updateMeetingRoom(@RequestBody MrsMeetingRoomEntity roomEntity) {
        if (roomEntity.getAddress().length() > 30 || roomEntity.getName().length() > 40) {
            return Result.error("字数长度超过限制");
        }
        int i = roomService.updateMeetingRoom(roomEntity);
        return i == 0 ? Result.error() : Result.ok();
    }

    /**
     * wll 删除会议室 把会议室和对应的时间配置状态改为0  0：禁用 1：正常
     *
     * @param id
     * @return
     */
    @GetMapping("/meetingRoom/deleteMeetingRoom")
    private Result deleteMeetingRoom(Integer id) {
        int i = roomService.deleteMeetingRoom(id);
        timeConfigService.deleteTimeByRoom(id);
        return i == 0 ? Result.error() : Result.ok();
    }

    /**
     * wll 将会议室数据导出
     *
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/meetingRoom/downExcel")
    public Result downExcel(HttpServletResponse response) throws Exception {
        List<MrsMeetingRoomEntity> mrsMeetingRoomEntities = roomService.selectAll();
        Workbook wb = ExcelUtil.fillExcelWithTemplate(mrsMeetingRoomEntities);
        ResponseUtil.export(response, wb, "会议室.xls");
        return Result.ok();
    }

}
