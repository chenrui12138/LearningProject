package com.jcxx.saas.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jcxx.saas.common.utils.PageUtils;
import com.jcxx.saas.modules.sys.entity.SysMgrUserEntity;

import java.util.Map;

/**
 * 系统管理_管理门户用户表
 *
 * @author carfield
 * @email 59702707@qq.com
 * @date 2019-08-28 10:13:19
 */
public interface SysMgrUserService extends IService<SysMgrUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

