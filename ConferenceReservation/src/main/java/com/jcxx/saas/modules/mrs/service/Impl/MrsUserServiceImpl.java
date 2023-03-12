package com.jcxx.saas.modules.mrs.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcxx.saas.modules.mrs.dao.MrsUserDao;
import com.jcxx.saas.modules.mrs.entity.MrsUserEntity;
import com.jcxx.saas.modules.mrs.service.MrsUserService;
import org.springframework.stereotype.Service;

@Service("MrsUserService")
public class MrsUserServiceImpl extends ServiceImpl<MrsUserDao,MrsUserEntity> implements MrsUserService {


    @Override
    public MrsUserEntity selectById(Integer userid) {
        MrsUserEntity mrsUserEntity = baseMapper.selectById(userid);
        return mrsUserEntity;
    }
}
