/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.common.aspect;

import com.google.gson.Gson;
import com.jcxx.saas.common.annotation.SysLog;
import com.jcxx.saas.common.utils.HttpContextUtils;
import com.jcxx.saas.common.utils.IPUtils;
import com.jcxx.saas.modules.sys.entity.SysLogEntity;
import com.jcxx.saas.modules.sys.entity.SysUserEntity;
import com.jcxx.saas.modules.sys.service.SysLogService;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 系统日志，切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.jcxx.saas.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLog = new SysLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = new Gson().toJson(args);
            sysLog.setParams(params);
        } catch (Exception e) {

        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));

        //用户名
        String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        sysLog.setUsername(username);

        sysLog.setTime(time);
        sysLog.setCreateDate(new Date());
        logger.info("User[%s] is excuting className[%s] ,methodName[%s] , params[%s] ", username, className, method, sysLog.getParams());
        //保存系统日志
        sysLogService.save(sysLog);
    }
}
