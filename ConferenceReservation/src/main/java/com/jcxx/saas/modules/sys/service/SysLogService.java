/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jcxx.saas.common.utils.PageUtils;
import com.jcxx.saas.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
