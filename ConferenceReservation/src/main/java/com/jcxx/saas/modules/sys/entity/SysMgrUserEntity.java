package com.jcxx.saas.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统管理_管理门户用户表
 * 
 * @author carfield
 * @email 59702707@qq.com
 * @date 2019-08-28 10:13:19
 */
@Data
@TableName("sys_mgr_user")
public class SysMgrUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键，自动增长
	 */
	@TableId
	private Long id;
	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 显示名
	 */
	private String displayName;
	/**
	 * 登录密码
	 */
	private String userPwd;
	/**
	 * 密码盐值
	 */
	private String pwdSalt;
	/**
	 * 头像地址
	 */
	private String avatarUrl;
	/**
	 * 手机号码
	 */
	private String userMobile;
	/**
	 * 邮箱地址
	 */
	private String mailAddr;
	/**
	 * 账号类型，默认为0；0非管理员 1系统管理员  2二级管理员
	 */
	private Integer userType;
	/**
	 * 账号状态，默认为0，正常；1为注销； 2为逻辑删除
	 */
	private Integer userStatus;
	/**
	 * 备注信息
	 */
	private String remark;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
