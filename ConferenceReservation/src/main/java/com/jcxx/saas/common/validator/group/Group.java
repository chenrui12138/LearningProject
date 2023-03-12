/**
 * Copyright (c) 2010 - 2019  All rights reserved.
 */

package com.jcxx.saas.common.validator.group;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
