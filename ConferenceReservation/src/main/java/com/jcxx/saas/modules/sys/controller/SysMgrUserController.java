package com.jcxx.saas.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jcxx.saas.modules.sys.entity.SysMgrUserEntity;
import com.jcxx.saas.modules.sys.service.SysMgrUserService;
import com.jcxx.saas.common.utils.PageUtils;
import com.jcxx.saas.common.utils.Result;



/**
 * 系统管理_管理门户用户表
 *
 * @author carfield
 * @email 59702707@qq.com
 * @date 2019-08-28 10:13:19
 */
@RestController
@RequestMapping("sys/sysmgruser")
public class SysMgrUserController {
    @Autowired
    private SysMgrUserService sysMgrUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:sysmgruser:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = sysMgrUserService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:sysmgruser:info")
    public Result info(@PathVariable("id") Long id){
		SysMgrUserEntity sysMgrUser = sysMgrUserService.getById(id);

        return Result.ok().put("sysMgrUser", sysMgrUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:sysmgruser:save")
    public Result save(@RequestBody SysMgrUserEntity sysMgrUser){
		sysMgrUserService.save(sysMgrUser);

        return Result.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:sysmgruser:update")
    public Result update(@RequestBody SysMgrUserEntity sysMgrUser){
		sysMgrUserService.updateById(sysMgrUser);

        return Result.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:sysmgruser:delete")
    public Result delete(@RequestBody Long[] ids){
		sysMgrUserService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
