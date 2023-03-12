package com.jcxx.saas.common.utils.poi.policys;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TextRenderPolicy;
import com.deepoove.poi.util.ObjectUtils;
import com.deepoove.poi.util.StyleUtils;

/**
 * @filename: FillTableMapDataRenderPolicy.java
 * @author:   carfield 
 * @version:  1.0.0 
 * @creatat:  2019年8月30日 下午3:36:32
 *
 * @Copyright (C) 2019  IreadTech.com All Rights Reserved.
 */

public class FillTableMapDataRenderPolicy extends DynamicTableRenderPolicy{

     public static class Helper {}

	public void render(XWPFTable table, Object data) {
        if (null == data) return;
        
        Map<String, Object> tableData = (Map<String, Object>)data;

        List<RowRenderData> datalist = (List<RowRenderData>) tableData.get("datalist");
        int startRow = (int) tableData.get("startRow");
        int startCol = (int) tableData.get("startColum");;
        if (null != datalist) {
            for (int i = 0; i < datalist.size(); i++) {
                FillTableHelper.renderRow(table, startRow + i, datalist.get(i), startCol);
            }
        }
		
	}


}
