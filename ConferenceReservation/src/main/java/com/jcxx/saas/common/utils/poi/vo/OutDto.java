package com.jcxx.saas.common.utils.poi.vo;

import com.deepoove.poi.config.Name;

import lombok.Data;

/**
 * @filename: OutDto.java
 * @author:   carfield 
 * @version:  1.0.0 
 * @creatat:  2019年8月30日 下午2:30:35
 *
 * @Copyright (C) 2019  IreadTech.com All Rights Reserved.
 */

@Data
public class OutDto {
    private String submitcorp;
    private String outday;
    @Name("detail_table")
    private TableData tableData;
 
}
