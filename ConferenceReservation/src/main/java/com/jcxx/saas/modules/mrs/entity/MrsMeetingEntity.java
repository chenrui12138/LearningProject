package com.jcxx.saas.modules.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mrs_meeting")
public class MrsMeetingEntity {

    /**
     * 会议id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 创建人id
     */
    private Integer creatorId;

    /**
     * 会议室id
     */
    private Integer meetingRoomId;

    /**
     * 会议参与人文件路径
     */
    private String participant;

    /**
     * 会议主题
     */
    private String motif;

    /**
     * 会议室使用原由
     */
    private String reason;

    /**
     * 会议开始时间
     */
    private String startTime;

    /**
     * 会议结束时间
     */
    private String endTime;

    /**
     * 会议日期
     */
    private String date;

    /**
     * 会议状态 0：已结束 1：进行中 2：未开始
     */
    private Integer state;

    /**
     * 会议创建时间
     */
    private String createTime;

    /**
     * 会议状态
     */
    private Integer status;

    /**
     * 会议签到状态
     */
    private Integer sign = 0;

}
