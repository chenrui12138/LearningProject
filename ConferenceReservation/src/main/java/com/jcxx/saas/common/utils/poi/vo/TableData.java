package com.jcxx.saas.common.utils.poi.vo;

import java.util.List;

import com.deepoove.poi.data.RowRenderData;

import lombok.Data;

/**
 * @filename: TableData.java
 * @author:   carfield 
 * @version:  1.0.0 
 * @creatat:  2019年8月30日 下午2:32:28
 *
 * @Copyright (C) 2019  IreadTech.com All Rights Reserved.
 */

@Data
public class TableData {
	int startRow;
	int startColum;
	private List<RowRenderData> basicDataList;	
}
