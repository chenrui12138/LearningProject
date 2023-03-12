/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jcxx.saas.modules.sys.entity.SysUserTokenEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);
	
}
