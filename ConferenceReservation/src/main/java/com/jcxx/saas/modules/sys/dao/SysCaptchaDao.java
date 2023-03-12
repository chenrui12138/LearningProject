/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcxx.saas.modules.sys.entity.SysCaptchaEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
