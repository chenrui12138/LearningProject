package com.jcxx.saas.modules.mrs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jcxx.saas.modules.mrs.entity.MrsTimeConfigEntity;
import com.jcxx.saas.modules.mrs.entity.MyMeetingTimeEntity;
import com.jcxx.saas.modules.mrs.utils.Result;

import java.util.List;

public interface MrsTimeConfigService extends IService<MrsTimeConfigEntity> {

    /**
     * cr
     * 查询会议室的所有时间段
     * @param date
     * @param meetingRoomId
     * @return
     */
    List<MyMeetingTimeEntity> selectAllMeetingTime(String date, Integer meetingRoomId);

    /**
     * wll 通过时间段查询时间
     * @return
     */
    List<MrsTimeConfigEntity> selectTime();

    /**
     * wll 通过会议室id删除时间
     * @param roomId
     * @return
     */
    Result deleteTimeByRoom(Integer roomId);

    /**
     * wll 修改时间
     * @param timeConfigEntity
     * @return
     */
    int updateTime(MrsTimeConfigEntity timeConfigEntity);

    /**
     * wll 查询出时间段的所有会议的时间段
     * @param stage
     * @return
     */
    List<MrsTimeConfigEntity> selectByStage(Integer stage);

    /**
     * wll 新增时间
     * @param timeConfigEntity
     * @return
     */
    int insertTime(MrsTimeConfigEntity timeConfigEntity);


}
