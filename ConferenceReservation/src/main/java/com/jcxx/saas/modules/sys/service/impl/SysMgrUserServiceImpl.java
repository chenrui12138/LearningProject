package com.jcxx.saas.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcxx.saas.common.utils.PageUtils;
import com.jcxx.saas.common.utils.Query;

import com.jcxx.saas.modules.sys.dao.SysMgrUserDao;
import com.jcxx.saas.modules.sys.entity.SysMgrUserEntity;
import com.jcxx.saas.modules.sys.service.SysMgrUserService;


@Service("sysMgrUserService")
public class SysMgrUserServiceImpl extends ServiceImpl<SysMgrUserDao, SysMgrUserEntity> implements SysMgrUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysMgrUserEntity> page = this.page(
                new Query<SysMgrUserEntity>().getPage(params),
                new QueryWrapper<SysMgrUserEntity>()
        );

        return new PageUtils(page);
    }

}