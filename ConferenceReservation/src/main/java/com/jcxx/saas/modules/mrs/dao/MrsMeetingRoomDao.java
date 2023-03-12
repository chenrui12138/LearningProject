package com.jcxx.saas.modules.mrs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcxx.saas.modules.mrs.entity.MrsMeetingRoomEntity;
import com.jcxx.saas.modules.mrs.entity.MyMrsMeetingRoomEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface MrsMeetingRoomDao extends BaseMapper<MrsMeetingRoomEntity> {
    /**
     * cr
     * 查询 某天（date） 某个会议室已经的预约情况
     */
    List<MyMrsMeetingRoomEntity> selectMeetingRoom(String date, Integer meetingRoomId);

    /**
     * cr
     * 查询 某天（date） 某个会议室的预约的时间段
     */
    List<MyMrsMeetingRoomEntity> selectNoMeetingRoom(String date, Integer meetingRoomId);

    /**
     * cr
     * 查询所有会议室
     */
    List<Map<String,Object>> selectAllMeetingRoom();

    /**
     * wll 对会议室列表进行排序查询
     * @return
     */
    @Select("select * from mrs_meeting_room m where  m.status = 1 order by m.`state`,m.state<>1,m.id desc")
    List<MrsMeetingRoomEntity> selectAll();
}
