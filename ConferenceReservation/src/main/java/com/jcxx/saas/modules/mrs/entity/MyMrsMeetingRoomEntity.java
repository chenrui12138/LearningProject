package com.jcxx.saas.modules.mrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回会议预约首页的所有数据
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMrsMeetingRoomEntity {

    /**
     * 会议室id
     */
    private Integer meetingRoomId;

    /**
     * 会议室预约状态
     * 4：已满   1,2,3：可预约 0：空闲
     */
    private Integer state = 0;

    /**
     * 会议日期
     */
    private String date;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 会议室座位数量
     */
    private Integer seat;

    /**
     * 会议室设备
     */
    private String equipment;

    /**
     * 会议主题
     */
    private String motif;

    /**
     * 会议开始时间
     */
    private String startTime;

    /**
     * 会议结束时间
     */
    private String endTime;

    /**
     * 会议室地址
     */
    private String address;

}
