package com.jcxx.saas.modules.mrs.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jcxx.saas.modules.mrs.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface MrsMeetingDao extends BaseMapper<MrsMeetingEntity> {


    /**
     * cr
     *  查询此用户所有的会议记录
     */
    List<MyMeetingEntity> selectAllMyMeeting(Integer creatorId);

    /**
     * cr
     * 查询某个会议室某一天的预约情况
     */
    List<MyMeetingTimeEntity> selectOneDayMeeting(String date, Integer meetingRoomId);

    /**
     * cr
     * 会议室大屏
     * 查询此会议室当天的已经预约的会议和未预约会议的时间
     */
    List<Map<String,Object>> selectMeetingDay(String date, Integer meetingRoomId);

    /**
     * cr
     * 根据会议室id和日期查询已预约会议
     */
    List<Map<String, Object>> selectByMeetingRoomId(Integer meetingRoomId,String date);


    /**
     * wll 分页查询所有会议
     * @param page
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id where m.status=1 order by case\n" +
            "when CONCAT(m.date,' ',m.start_time) > (select NOW()) then 1\n" +
            "when CONCAT(m.date,' ',m.start_time) <= (select NOW()) and CONCAT(m.date,' ',m.end_time) > (select NOW()) then 2\n" +
            "ELSE 3 end ,m.date")
    List<UserMeetingVO> selectAll(Page<UserMeetingVO> page);

    /**
     * wll 查找状态为已结束的所有会议
     * @param page
     * @param date
     * @param time
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
           "where m.date=#{nowDate} and m.end_time<#{nowTime} and m.status=1 or m.date<#{nowDate} and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByState0(Page<UserMeetingVO> page,@Param("nowDate") String date,@Param("nowTime") String time);

    /**
     * wll 查找状态为进行中的所有会议
     * @param page
     * @param date
     * @param time
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date=#{nowDate} and m.start_time<#{nowTime} and m.end_time>#{nowTime} and m.status=1  order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByState1(Page<UserMeetingVO> page,@Param("nowDate") String date,@Param("nowTime") String time);

    /**
     * wll 查找状态为未开始的所有会议
     * @param page
     * @param date
     * @param time
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date>#{nowDate} and m.status=1 or m.date=#{nowDate} and m.start_time>#{nowTime} and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByState2(Page<UserMeetingVO> page,@Param("nowDate") String date,@Param("nowTime") String time);

    /**
     * wll 通过主题查找会议
     * @param page
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id" +
            " where m.motif like CONCAT('%',#{motif},'%') and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByMotif(Page<UserMeetingVO> page,@Param("motif")String motif);

    /**
     * wll 通过日期查找会议
     * @param page
     * @param date
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id" +
            " where m.date=#{date} and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDate(Page<UserMeetingVO> page,@Param("date")String date);

    /**
     * wll 通过主题查找状态为已结束的会议
     * @param page
     * @param date
     * @param time
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date=#{nowDate} and m.end_time<#{nowTime} and m.motif like CONCAT('%',#{motif},'%') and m.status=1 " +
            "or m.date<#{nowDate} and m.motif like CONCAT('%',#{motif},'%') and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByMotifState0(Page<UserMeetingVO> page,@Param("nowDate") String date,@Param("nowTime") String time,@Param("motif") String motif);

    /**
     * wll 通过主题查找状态为会议中的会议
     * @param page
     * @param date
     * @param time
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date=#{nowDate} and m.start_time<#{nowTime} and m.end_time>#{nowTime} and m.motif like CONCAT('%',#{motif},'%') " +
            "and m.status=1  order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByMotifState1(Page<UserMeetingVO> page,@Param("nowDate") String date,@Param("nowTime") String time,@Param("motif") String motif);

    /**
     * wll 通过主题查找状态为未开始的会议
     * @param page
     * @param date
     * @param time
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date>#{nowDate} and m.motif like CONCAT('%',#{motif},'%')  and m.status=1 or m.date=#{nowDate} and m.motif like CONCAT('%',#{motif},'%') " +
            " and m.start_time>#{nowTime} and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByMotifState2(Page<UserMeetingVO> page,@Param("nowDate") String date,@Param("nowTime") String time,@Param("motif") String motif);

    /**
     * wll 通过主题和日期查找会议
     * @param page
     * @param date
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id" +
            " where m.date=#{date} and m.motif like CONCAT('%',#{motif},'%') and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDateMotif(Page<UserMeetingVO> page,@Param("date")String date,@Param("motif") String motif);

    /**
     * wll 通过日期查找状态为已结束的会议
     * @param page
     * @param nowDate
     * @param time
     * @param date
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date=#{nowDate} and m.end_time<#{nowTime} and m.date=#{date} and m.status=1 " +
            "or m.date<#{nowDate} and  m.date=#{date} and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDateState0(Page<UserMeetingVO> page,@Param("nowDate") String nowDate,@Param("nowTime") String time,@Param("date") String date);

    /**
     * wll 通过日期查找状态为会议中的会议
     * @param page
     * @param nowDate
     * @param time
     * @param date
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date=#{nowDate} and m.start_time<#{nowTime} and m.end_time>#{nowTime} and  m.date=#{date} " +
            "and m.status=1  order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDateState1(Page<UserMeetingVO> page,@Param("nowDate") String nowDate,@Param("nowTime") String time,@Param("date") String date);

    /**
     * wll 通过日期查找状态为未开始的会议
     * @param page
     * @param nowDate
     * @param time
     * @param date
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date>#{nowDate} and m.date=#{date}  and m.status=1 or m.date=#{nowDate} and  m.date=#{date} " +
            " and m.start_time>#{nowTime} and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDateState2(Page<UserMeetingVO> page,@Param("nowDate") String nowDate,@Param("nowTime") String time,@Param("date") String date);

    /**
     * wll 通过日期主题查找状态为已结束的所有会议
     * @param page
     * @param nowDate
     * @param time
     * @param date
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date=#{nowDate} and m.end_time<#{nowTime} and m.date=#{date} and m.motif like CONCAT('%',#{motif},'%') and m.status=1 " +
            "or m.date<#{nowDate} and  m.date=#{date} and m.motif like CONCAT('%',#{motif},'%') and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDateMotifState0(Page<UserMeetingVO> page,@Param("nowDate") String nowDate,@Param("nowTime") String time,@Param("date") String date,@Param("motif") String motif);

    /**
     * wll 通过日期主题查找状态为会议中的所有会议
     * @param page
     * @param nowDate
     * @param time
     * @param date
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date=#{nowDate} and m.start_time<#{nowTime} and m.end_time>#{nowTime} and  m.date=#{date} and m.motif like CONCAT('%',#{motif},'%') " +
            "and m.status=1  order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDateMotifState1(Page<UserMeetingVO> page,@Param("nowDate") String nowDate,@Param("nowTime") String time,@Param("date") String date,@Param("motif") String motif);

    /**
     * wll 通过日期主题查找状态为未开始的所有会议
     * @param page
     * @param nowDate
     * @param time
     * @param date
     * @param motif
     * @return
     */
    @Select(" select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id " +
            "where m.date>#{nowDate} and m.date=#{date} and m.motif like CONCAT('%',#{motif},'%')  and m.status=1 or m.date=#{nowDate} and  m.date=#{date} " +
            " and m.start_time>#{nowTime} and m.motif like CONCAT('%',#{motif},'%') and m.status=1 order by m.date desc,m.start_time desc")
    List<UserMeetingVO> selectByDateMotifState2(Page<UserMeetingVO> page,@Param("nowDate") String nowDate,@Param("nowTime") String time,@Param("date") String date,@Param("motif") String motif);

    /**
     * wll 通过会议id查询会议
     * @param id
     * @return
     */
    @Select("select m.*,u.`name`,u.`mobile` from mrs_meeting m left join sys_user u on m.creator_id=u.user_id where m.id=#{id}")
    UserMeetingVO selectByMeetingId( @Param("id")Integer id);


}