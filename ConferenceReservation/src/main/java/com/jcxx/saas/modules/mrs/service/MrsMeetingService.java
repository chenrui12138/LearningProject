package com.jcxx.saas.modules.mrs.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jcxx.saas.modules.mrs.entity.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public interface MrsMeetingService extends IService<MrsMeetingEntity> {
    /**
     * cr
     * 查询此用户的所有会议
     */
    List<MyMeetingEntity> selectAllMyMeeting(Integer creatorId);

    /**
     * cr
     * 添加会议预约
     */
    int addMeeting(MrsMeetingEntity mrsMeetingEntity);

    /**
     * cr
     * 查询某个会议室某一天的预约情况
     */
    List<MyMeetingTimeEntity> selectOneDayMeeting(String date, Integer meetingRoomId);

    /**
     * cr
     * 上传文件 仅支持Excel文件（.xls和.xlsx均可）
     */
    String excelUpload(MultipartFile file);


    /**
     * cr
     * 会议室大屏
     * 查询此会议室当天的已经预约的会议
     * @return
     */
    ArrayList<Object> selectMeetingDay(String date, Integer meetingRoomId);

    /**
     * cr
     * 会议室大屏签到功能
     */
    int updateByMeetingId(Integer id);


    /**
     * wll 查询所有会议
     * @param page
     * @return
     */
    IPage<UserMeetingVO> selectAll(Page<UserMeetingVO> page);

    /**
     * wll 通过状态查询会议
     * @param page
     * @param state
     * @return
     */
    IPage<UserMeetingVO> selectByState(Page<UserMeetingVO> page, Integer state);

    /**
     * wll 通过主题查询会议
     * @param page
     * @param motif
     * @return
     */
    IPage<UserMeetingVO> selectByMotif(Page<UserMeetingVO> page, String motif);

    /**
     * wll 通过日期查询会议
     * @param page
     * @param date
     * @return
     */
    IPage<UserMeetingVO> selectByDate(Page<UserMeetingVO> page, String date);

    /**
     * wll 通过主题和状态查询会议
     * @param page
     * @param motif
     * @param state
     * @return
     */
    IPage<UserMeetingVO> selectByMotifState(Page<UserMeetingVO> page, String motif,Integer state);

    /**
     * wll 通过日期和主题查询会议
     * @param page
     * @param date
     * @param motif
     * @return
     */
    IPage<UserMeetingVO> selectByDateMotif(Page<UserMeetingVO> page, String date,String motif);

    /**
     * wll 通过日期和状态查会议
     * @param page
     * @param date
     * @param state
     * @return
     */
    IPage<UserMeetingVO> selectByDateState(Page<UserMeetingVO> page, String date,Integer state);

    /**
     * wll 通过日期状态主题查询会议
     * @param page
     * @param date
     * @param motif
     * @param state
     * @return
     */
    IPage<UserMeetingVO> selectByDateMotifState(Page<UserMeetingVO> page, String date,String motif,Integer state);

    /**
     * wll 通过会议id查询会议
     * @param id
     * @return
     */
    UserMeetingVO selectByMeetingId(Integer id);

    /**
     * wll 通过会议id上传文件
     * @param file
     */
    String upload(MultipartFile file);

    /**
     * wll 通过id修改会议
     * @param meetingEntity
     * @return
     */
    int updateMeetingById(MrsMeetingEntity meetingEntity);

    /**
     * wll 通过会议id下载文件
     * @param id
     * @param response
     */
    boolean down(Integer id, HttpServletResponse response);

    /**
     * wll
     * 通过会议id取消会议
     * @param id
     * @return
     */
    int deleteById(Integer id);

    /**
     * @Author wll
     * @Date 2021/7/22 17:29
     * @Description 通过会议室id查询会议
     *
     */
    List<MrsMeetingEntity> selectByMeetingRoom(Integer id);

    /**
     * 删除上传没提交的文件
     * @return
     */
    boolean deleteFile(String path);
}
