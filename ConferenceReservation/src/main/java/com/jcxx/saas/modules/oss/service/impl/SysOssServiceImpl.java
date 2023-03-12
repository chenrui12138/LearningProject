/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcxx.saas.common.utils.PageUtils;
import com.jcxx.saas.common.utils.Query;
import com.jcxx.saas.modules.oss.dao.SysOssDao;
import com.jcxx.saas.modules.oss.entity.SysOssEntity;
import com.jcxx.saas.modules.oss.service.SysOssService;

import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SysOssEntity> page = this.page(
			new Query<SysOssEntity>().getPage(params)
		);

		return new PageUtils(page);
	}
	
}
