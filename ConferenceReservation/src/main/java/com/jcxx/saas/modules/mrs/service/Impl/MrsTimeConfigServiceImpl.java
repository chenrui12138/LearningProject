package com.jcxx.saas.modules.mrs.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcxx.saas.modules.mrs.dao.MrsTimeConfigDao;
import com.jcxx.saas.modules.mrs.entity.MrsTimeConfigEntity;
import com.jcxx.saas.modules.mrs.entity.MyMeetingTimeEntity;
import com.jcxx.saas.modules.mrs.service.MrsTimeConfigService;
import com.jcxx.saas.modules.mrs.utils.DateTime;
import com.jcxx.saas.modules.mrs.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MrsTimeConfigService")
public class MrsTimeConfigServiceImpl extends ServiceImpl<MrsTimeConfigDao, MrsTimeConfigEntity> implements MrsTimeConfigService {

    /**
     * cr
     */
    @Autowired
    private MrsTimeConfigDao mrsTimeConfigDao;

    /**
     * cr
     * 查询会议室的所有时间段
     */
    @Override
    public List<MyMeetingTimeEntity> selectAllMeetingTime(String date, Integer meetingRoomId) {
        List<MyMeetingTimeEntity> myMeetingTimeEntities = mrsTimeConfigDao.selectAllTime(date, meetingRoomId);

        return myMeetingTimeEntities;
    }

    @Override
    public List<MrsTimeConfigEntity> selectTime() {
        List<MrsTimeConfigEntity> timeConfigEntities = baseMapper.selectList(new QueryWrapper<MrsTimeConfigEntity>()
                .eq("state", 1)
                .groupBy("stage"));
        for (MrsTimeConfigEntity timeConfigEntity : timeConfigEntities) {
            timeConfigEntity.setStartTime(DateTime.TimeFormat(timeConfigEntity.getStartTime()));
            timeConfigEntity.setEndTime(DateTime.TimeFormat(timeConfigEntity.getEndTime()));
        }
        return timeConfigEntities;
    }

    @Override
    public Result deleteTimeByRoom(Integer roomId) {
        List<MrsTimeConfigEntity> timeConfigEntities = baseMapper.selectList(new QueryWrapper<MrsTimeConfigEntity>()
                .eq("meeting_room_id", roomId));
        int size = timeConfigEntities.size();
        int temp = 0;
        for (MrsTimeConfigEntity timeConfigEntity : timeConfigEntities) {
            Integer id = timeConfigEntity.getId();
            MrsTimeConfigEntity mrsTimeConfigEntity = baseMapper.selectById(id);
            mrsTimeConfigEntity.setState(0);
            int i = baseMapper.updateById(mrsTimeConfigEntity);
            temp += i;
        }
        return temp < size ? Result.error() : Result.ok();
    }

    @Override
    public int updateTime(MrsTimeConfigEntity timeConfigEntity) {
        int i = baseMapper.updateById(timeConfigEntity);
        return i;
    }

    @Override
    public List<MrsTimeConfigEntity> selectByStage(Integer stage) {
        List<MrsTimeConfigEntity> timeConfigEntities = baseMapper.selectList(new QueryWrapper<MrsTimeConfigEntity>()
                .eq("state", 1)
                .eq("stage", stage));
        return timeConfigEntities;
    }

    @Override
    public int insertTime(MrsTimeConfigEntity timeConfigEntity) {
        timeConfigEntity.setState(1);
        timeConfigEntity.setUpdateId(null);
        timeConfigEntity.setUpdateTime(null);
        int insert = baseMapper.insert(timeConfigEntity);
        return insert;
    }
}
