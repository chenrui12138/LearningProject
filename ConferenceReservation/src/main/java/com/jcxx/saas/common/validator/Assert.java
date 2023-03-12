/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.common.validator;

import org.apache.commons.lang.StringUtils;

import com.jcxx.saas.common.exception.SaaSException;

/**
 * 数据校验
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new SaaSException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new SaaSException(message);
        }
    }
}
