package com.jcxx.saas.modules.mrs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcxx.saas.common.utils.Result;
import com.jcxx.saas.modules.mrs.dao.MrsMeetingDao;
import com.jcxx.saas.modules.mrs.entity.MrsMeetingEntity;
import com.jcxx.saas.modules.mrs.entity.MyMeetingEntity;
import com.jcxx.saas.modules.mrs.entity.MyMeetingTimeEntity;
import com.jcxx.saas.modules.mrs.entity.UserMeetingVO;
import com.jcxx.saas.modules.mrs.service.MrsMeetingService;
import com.jcxx.saas.modules.mrs.service.MrsUserService;
import com.jcxx.saas.modules.mrs.utils.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Author wll
 * @Date 2021/7/14 10:10
 * @Description
 */
@RestController
@RequestMapping("/mrs")
public class MrsMeetingController {

    @Autowired
    private MrsMeetingService meetingService;

    //不规范写法，不应该出现Dao层，应该在service层调用dao层
    @Autowired
    private MrsMeetingDao mrsMeetingDao;

    /**
     * cr
     * 查询用户的所有预约
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/selectAllMyMeeting")
    public Result selectAllMyMeeting(Integer creatorId) {

        if (null == creatorId) {
            return Result.error("请输入需要查询的用户id");
        }

        List<MyMeetingEntity> myMeetinglist = meetingService.selectAllMyMeeting(creatorId);
        return Result.ok().put("myMeetingList", myMeetinglist);
    }

    /**
     * cr
     * 添加会议预约
     *
     * @param map
     * @return
     */
    @PostMapping("/addMeeting")
    public Result addMeeting(@RequestBody Map<String, Object> map) {

        JSONObject jsonObject = new JSONObject(map);
        MrsMeetingEntity mrsMeetingEntity = new MrsMeetingEntity();

        //取出json中的time数组
        JSONArray time1 = jsonObject.getJSONArray("time");

        for (int i = 0; i < time1.length(); i++) {

            mrsMeetingEntity.setStartTime((String) time1.getJSONObject(i).get("startTime"));
            mrsMeetingEntity.setEndTime((String) time1.getJSONObject(i).get("endTime"));
            if (map.get("motif").toString().length() > 40) {
                return Result.error("主题字符过长");
            }

            mrsMeetingEntity.setMotif((String) map.get("motif"));
            if (map.get("reason").toString().length() > 400) {
                return Result.error("使用事由字符过长");
            }
            mrsMeetingEntity.setReason((String) map.get("reason"));
            mrsMeetingEntity.setMeetingRoomId((Integer) map.get("meetingRoomId"));
            mrsMeetingEntity.setDate((String) map.get("date"));
            mrsMeetingEntity.setCreatorId((Integer) map.get("creatorId"));
            mrsMeetingEntity.setParticipant((String) map.get("participant"));
            mrsMeetingEntity.setSign(0);

            //会议状态 0: 已结束 1：进行中 2：未开始会议状态 0: 已结束 1：进行中 2：未开始
            mrsMeetingEntity.setState(2);
            //会议状态 0：禁用  1：正常（逻辑删除的标志值）
            mrsMeetingEntity.setStatus(1);

            //添加会议室创建时间
            mrsMeetingEntity.setCreateTime(DateTime.nowDateTime());

            //对预约时间进行判断   只有大于当前时间的预约时间才能判断
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            //当天时间
            Date date = new Date();
            String replace = DateTime.nowDate().replace("-", "");
            String format = simpleDateFormat.format(date).replace(":", "");
            //接收的需要预约的时间
            String date1 = ((String) map.get("date")).replace("-", "");
            String startTime = ((String) time1.getJSONObject(i).get("startTime")).replace(":", "");
            String endTime = ((String) time1.getJSONObject(i).get("endTime")).replace(":", "");
            if ((Long.valueOf(replace + format)) > Long.valueOf(date1 + endTime)) {
                return Result.error("请选择正确的时间");
            }

            //判断选择的时间，是否已经被预约
            List<MyMeetingTimeEntity> list = mrsMeetingDao.selectOneDayMeeting((String) map.get("date"), (Integer) map.get("meetingRoomId"));
            for (int j = 0; j < list.size(); j++) {
                String startTime1 = list.get(j).getStartTime().replace(":", "");
                if (startTime1.equals(startTime + "00")) {
                    return Result.error("该时间已被预约请选择正确的时间");
                }
            }

            int j = meetingService.addMeeting(mrsMeetingEntity);
            if (j == 0) {
                return Result.error("插入失败");
            }
        }

        return Result.ok();
    }

    /**
     * cr
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/excelUpload")
    public Result excelUpload(MultipartFile file) {

        if (!file.isEmpty()) {
            String s = meetingService.excelUpload(file);
            return s.equals("") ? Result.error("文件上传错误") : Result.ok().put("path", s);
        } else {
            return Result.error("文件不可为空");
        }
    }

    /**
     * cr
     * 会议室大屏
     * 查出已经预约了会议和未预约的会议时间
     */
    @GetMapping("/meetingScreen")
    public Result meetingScreen(Integer meetingRoomId) {
        if (null == meetingRoomId) {
            return Result.error("请输入需要查询的会议室id");
        }

        ArrayList<Object> list = meetingService.selectMeetingDay(DateTime.nowDate(), meetingRoomId);

        return Result.ok().put("list", list);
    }

    /**
     * cr
     * 会议室大屏
     * 签到功能
     */
    @GetMapping("/meetingRoomScreen/updateByMeetingId")
    public Result updateByMeetingId(Integer id) {
        int i = meetingService.updateByMeetingId(id);
        return i == 0 ? Result.error("签到失败") : Result.ok();
    }

    /**
     * cr
     * 会议室大屏
     * 已经预约的会议信息
     */
    @Autowired
    private MrsUserService mrsUserService;

    @GetMapping("/meetingScreenMeetingInfo")
    public Result meetingScreenMeetingInfo(Integer meetingRoomId) {
        List<Map<String, Object>> list = mrsMeetingDao.selectByMeetingRoomId(meetingRoomId, DateTime.nowDate());

        ArrayList<Map> meetingScreenMeetingInfo = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", list.get(i).get("id"));
            map.put("meetingRoomId", list.get(i).get("meeting_room_id"));
            map.put("startTime", DateTime.TimeFormat(simpleDateFormat.format(list.get(i).get("start_time"))));
            map.put("endTime", DateTime.TimeFormat(simpleDateFormat.format(list.get(i).get("end_time"))));
            map.put("motif", list.get(i).get("motif"));
            map.put("name", list.get(i).get("name"));
            map.put("username", mrsUserService.selectById((Integer) list.get(i).get("creator_id")).getName());
            map.put("sign", list.get(i).get("sign"));
            meetingScreenMeetingInfo.add(map);
        }

        HashMap<String, Object> hashMap;
        //按时间进行排序
        for (int i = 0; i < (meetingScreenMeetingInfo.size() - 1); i++) {
            for (int x = 0; x < (meetingScreenMeetingInfo.size() - i - 1); x++) {
                if (Integer.parseInt(((String) meetingScreenMeetingInfo.get(x).get("startTime")).replace(":", "")) >
                        Integer.parseInt(((String) meetingScreenMeetingInfo.get(x + 1).get("startTime")).replace(":", ""))) {
                    hashMap = (HashMap<String, Object>) meetingScreenMeetingInfo.get(x);
                    meetingScreenMeetingInfo.set(x, meetingScreenMeetingInfo.get(x + 1));
                    meetingScreenMeetingInfo.set(x + 1, hashMap);
                }
            }
        }
        return Result.ok().put("meetingScreenMeetingInfo", meetingScreenMeetingInfo);
    }

    /**
     * wll
     * 会议室状态：state  0:已结束 1：正在进行 2：未开始
     * 会议管理主页查询
     */
    @GetMapping("/meeting/index")
    public Result findByPage(Integer pageNo, Integer state, String motif, String date, Integer size) {
        if (motif.length() >= 40) {
            return Result.error("字数超过限制");
        }
        Page<UserMeetingVO> mrsMeetingEntityPage = new Page<>(pageNo, size);
        if (state == null && motif.length() == 0 && date.length() == 0) {
            IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectAll(mrsMeetingEntityPage);
            return Result.ok().put("meeting", mrsMeetingEntityIPage);
        }
        if (state == null && motif.length() == 0) {
            IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectByDate(mrsMeetingEntityPage, date);
            return Result.ok().put("meeting", mrsMeetingEntityIPage);
        }
        if (motif.length() == 0 && date.length() == 0) {
            IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectByState(mrsMeetingEntityPage, state);
            return Result.ok().put("meeting", mrsMeetingEntityIPage);
        }
        if (date.length() == 0 && state == null) {
            IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectByMotif(mrsMeetingEntityPage, motif);
            return Result.ok().put("meeting", mrsMeetingEntityIPage);
        }
        if (date.length() == 0) {
            IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectByMotifState(mrsMeetingEntityPage, motif, state);
            return Result.ok().put("meeting", mrsMeetingEntityIPage);
        }
        if (state == null) {
            IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectByDateMotif(mrsMeetingEntityPage, date, motif);
            return Result.ok().put("meeting", mrsMeetingEntityIPage);
        }
        if (motif.length() == 0) {
            IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectByDateState(mrsMeetingEntityPage, date, state);
            return Result.ok().put("meeting", mrsMeetingEntityIPage);
        }
        IPage<UserMeetingVO> mrsMeetingEntityIPage = meetingService.selectByDateMotifState(mrsMeetingEntityPage, date, motif, state);
        return Result.ok().put("meeting", mrsMeetingEntityIPage);
    }

    /**
     * wll 通过id查询会议
     *
     * @param id
     * @return
     */
    @GetMapping("/meeting/selectById")
    public Result selectById(Integer id) {
        UserMeetingVO userMeetingVO = meetingService.selectByMeetingId(id);
        return Result.ok().put("meeting", userMeetingVO);
    }

    /**
     * wll 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/meeting/upload")
    public Result upload(MultipartFile file) {
        if (file.getSize() >= (1048576 * 15)) {
            return Result.error("文件大小超过限制");
        }
        String upload = meetingService.upload(file);
        return upload == null ? Result.error("上传文件错误") : Result.ok().put("participant", upload);
    }

    /**
     * wll 下载文件
     *
     * @param id
     * @param response
     * @return
     */
    @GetMapping("/meeting/down")
    public Result down(Integer id, HttpServletResponse response) {

        boolean down = meetingService.down(id, response);
        if (down == false) {
            return Result.ok("没有存入人员文件");
        }
        return Result.ok("下载");
    }

    /**
     * wll 通过id修改会议
     *
     * @param meetingEntity
     * @return
     */
    @PostMapping("/meeting/updateById")
    public Result updateById(@RequestBody MrsMeetingEntity meetingEntity) {
        if (meetingEntity.getMotif().length() > 40 || meetingEntity.getReason().length() > 400) {
            return Result.error("字数超过限制");
        }
        int i = meetingService.updateMeetingById(meetingEntity);
        return i == 0 ? Result.error() : Result.ok();
    }

    /**
     * wll 通过id取消会议
     *
     * @param id
     * @return
     */
    @GetMapping("/meeting/deleteById")
    public Result delete(Integer id) {
        int i = meetingService.deleteById(id);
        return i == 0 ? Result.error() : Result.ok();
    }

    /**
     *wll 删除文件
     *
     * @return
     */
    @PostMapping("/meeting/deleteFile")
    public Result deleteFile(@RequestBody Map<String, String> path) {
        JSONObject jsonObject = new JSONObject(path);
        String newPath = jsonObject.getString("path");
        try {
            Integer meetingId = jsonObject.getInt("meetingId");
            UserMeetingVO userMeetingVO = meetingService.selectByMeetingId(meetingId);
            userMeetingVO.setParticipant("");
            meetingService.updateMeetingById(userMeetingVO);
        }catch (Exception e){
            boolean b = meetingService.deleteFile(newPath);
            return b == true ? Result.ok() : Result.error("文件不存在");
        }
        boolean b = meetingService.deleteFile(newPath);
        return b ? Result.ok() : Result.error("文件不存在");
    }
}