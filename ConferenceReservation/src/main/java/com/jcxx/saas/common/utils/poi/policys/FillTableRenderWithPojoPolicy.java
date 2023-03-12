package com.jcxx.saas.common.utils.poi.policys;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFTable;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.jcxx.saas.common.utils.poi.vo.TableData;

/**
 * @filename: CustomTableRenderPolicy.java
 * @author:   carfield 
 * @version:  1.0.0 
 * @creatat:  2019年8月30日 下午3:36:32
 *
 * @Copyright (C) 2019  IreadTech.com All Rights Reserved.
 */

public class FillTableRenderWithPojoPolicy extends DynamicTableRenderPolicy{

	public void render(XWPFTable table, Object data) {
        if (null == data) return;
        TableData detailData = (TableData) data;

        List<RowRenderData> datalist = detailData.getBasicDataList();
        if (null != datalist) {
            for (int i = 0; i < datalist.size(); i++) {
            	FillTableHelper.renderRow(table, detailData.getStartRow() + i, datalist.get(i), detailData.getStartColum());
            }
        }
		
	}


}
