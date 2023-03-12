package com.jcxx.saas.modules.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mrs_meeting_room")
public class MrsMeetingRoomEntity {

    /**
     * 会议室id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;


    /**
     * 管理人
     */
    private String administrator;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 会议室地址
     */
    private String address;

    /**
     * 会议室座位数量
     */
    private Integer seat;

    /**
     * 会议室设备
     */
    private String equipment;

    /**
     * 会议室状态
     */
    private Integer state;

    /**
     * 会议室创建时间
     */
    private String createTime;

    /**
     * 会议室创建人id
     */
    private Integer creatorId;

    /**
     * 禁用状态 0：禁用 1:正常
     */
    private Integer status;
}
