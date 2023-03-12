package com.jcxx.saas.modules.mrs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jcxx.saas.modules.mrs.entity.MrsUserEntity;

public interface MrsUserService extends IService<MrsUserEntity> {
    /**
     * cr
     * 根据id查询用户名字
     */
    MrsUserEntity selectById(Integer userid);
}
