/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcxx.saas.modules.app.entity.UserEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
