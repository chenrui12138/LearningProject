package com.jcxx.saas.modules.mrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMeetingVO extends MrsMeetingEntity{

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户电话
     */
    private String mobile;
}
