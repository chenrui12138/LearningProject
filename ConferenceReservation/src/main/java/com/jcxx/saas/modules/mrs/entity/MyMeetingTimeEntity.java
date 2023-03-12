package com.jcxx.saas.modules.mrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMeetingTimeEntity {
    /**
     * 会议开始时间
     */
    private String startTime;

    /**
     * 会议结束时间
     */
    private String endTime;
}
