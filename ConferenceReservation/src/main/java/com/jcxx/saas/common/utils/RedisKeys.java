/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.common.utils;

/**
 * Redis所有Keys
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }
}
