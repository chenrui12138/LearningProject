package com.jcxx.saas.modules.mrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMeetingEntity {
    /**
     * 会议id
     */
    private Integer id;

    /**
     * 会议主题
     */
    private String motif;

    /**
     * 日期
     */
    private String date;

    /**
     * 会议开始时间
     */
    private String  startTime;

    /**
     * 会议结束时间
     */
    private String endTime;

    /**
     * 会议室地址
     */
    private String address;

    /**
     * 会议状态  2：未开始  1：会议进行中  0：会议已结束
     */
    private Integer state;

}
