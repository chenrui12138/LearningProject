package com.jcxx.saas.modules.mrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MrsMeetingRoomScreenEntity {
    /**
     * 会议室id
     */
    private Integer meetingRoomId;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 会议室状态  0：空闲   1：有会议预约  2：已过会议时间
     * 默认值为0
     */
    private Integer meetingScreenState = 0;

    /**
     * 会议开始时间
     */
    private String startTime;

    /**
     * 会议结束时间
     */
    private String endTime;

    /**
     * 会议主题
     */
    private String motif;

    /**
     * 会议创建人
     */
    private String username;

}
