package com.jcxx.saas.modules.mrs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jcxx.saas.modules.mrs.entity.MrsMeetingRoomEntity;
import com.jcxx.saas.modules.mrs.entity.MyMrsMeetingRoomEntity;

import java.util.ArrayList;
import java.util.List;


public interface MrsMeetingRoomService extends IService<MrsMeetingRoomEntity> {

    /**
     * cr
     * 查询 某天（date） 某个会议室的预约情况
     */
    List<MyMrsMeetingRoomEntity> selectMeetingRoom(String date, Integer meetingRoomId);

    /**
     * cr
     * 查询 某天（date） 某个会议室的预约的时间段
     */
    List<MyMrsMeetingRoomEntity> selectNoMeetingRoom(String date, Integer meetingRoomId);

    /**
     * cr
     * 查询某天所有会议室的预约情况
     * @return
     */
    ArrayList<Object> selectAllMeetingRoomAllMeeting(String date);

    /**
     * wll
     * 查询所有会议室
     * @return
     */
    List<MrsMeetingRoomEntity> selectAll();

    /**
     * wll
     * 通过会议室名称，状态查询会议室
     * @param name
     * @param state
     * @return
     */
    List<MrsMeetingRoomEntity> selectByNameState(String name,Integer state);

    /**
     * wll
     * 通过id查询会议室
     * @param id
     * @return
     */
    MrsMeetingRoomEntity selectById(Integer id);



    /**
     * wll
     * 新增会议室
     * @param roomEntity
     * @return
     */
    int insertMeetingRoom(MrsMeetingRoomEntity roomEntity);

    /**
     * wll
     * 修改会议室
     * @param roomEntity
     * @return
     */
    int updateMeetingRoom(MrsMeetingRoomEntity roomEntity);

    /**
     * wll
     * 删除会议室
     * @param id
     * @return
     */
    int deleteMeetingRoom(Integer id);

    /**
     * wll
     * 通过创建时间查找会议室
     * @param time
     * @return
     */
    MrsMeetingRoomEntity selectByTime(String time);

    /**
     * wll
     * 通过状态查询
     * @param state
     * @return
     */
    List<MrsMeetingRoomEntity> selectByState(Integer state);

    /**
     * wll
     * 通过名字查询
     * @param name
     * @return
     */
    List<MrsMeetingRoomEntity> selectByName(String name);


}
