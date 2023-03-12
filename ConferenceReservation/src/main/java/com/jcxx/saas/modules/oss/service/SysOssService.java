/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jcxx.saas.common.utils.PageUtils;
import com.jcxx.saas.modules.oss.entity.SysOssEntity;

import java.util.Map;

/**
 * 文件上传
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
