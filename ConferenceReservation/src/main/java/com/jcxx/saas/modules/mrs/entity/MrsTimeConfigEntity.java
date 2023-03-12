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
@TableName("mrs_time_config")
public class MrsTimeConfigEntity {

    /**
     * 时间id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 会议室id
     */
    private Integer meetingRoomId;

    /**
     * 修改者id
     */
    private Integer updateId;

    /**
     * 创建人id
     */
    private Integer createId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 时间段
     */
    private Integer stage;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 状态 0：禁用 1：正常
     */
    private Integer state;
}
