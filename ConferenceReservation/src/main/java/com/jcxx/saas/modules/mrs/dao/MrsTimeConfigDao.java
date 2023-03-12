package com.jcxx.saas.modules.mrs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcxx.saas.modules.mrs.entity.MrsTimeConfigEntity;
import com.jcxx.saas.modules.mrs.entity.MyMeetingTimeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface MrsTimeConfigDao extends BaseMapper<MrsTimeConfigEntity> {
    /**
     * cr
     * 查询会议室预约的所有时间段
     */
    List<MyMeetingTimeEntity> selectAllTime(String date, Integer meetingRoomId);

    /**
     * cr
     * 根据会议室id查询预约时间段
     */
    List<Map<String,Object>> selectByMeetingRoomId(Integer meetingRoomId);
}
