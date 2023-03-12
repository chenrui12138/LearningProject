package com.jcxx.saas.common.utils.poi.policys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deepoove.poi.data.CellRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.policy.MiniTableRenderPolicy;
import com.deepoove.poi.policy.TextRenderPolicy;
import com.deepoove.poi.util.ObjectUtils;
import com.deepoove.poi.util.StyleUtils;
import com.jcxx.saas.common.utils.poi.DocxExportUtils;

/**
 * @filename: FillTableHelper.java
 * @author:   carfield 
 * @version:  1.0.0 
 * @creatat:  2019年9月2日 上午11:19:45
 *
 * @Copyright (C) 2019  IreadTech.com All Rights Reserved.
 */

public class FillTableHelper {
    private static final Logger log = LoggerFactory.getLogger(FillTableHelper.class);

    /**
     * 填充表格一行的数据
     * 
     * @param table
     * @param row
     *            第几行
     * @param rowData
     *            行数据：确保行数据的大小不超过表格该行的单元格数量
     */
    public static void renderRow(XWPFTable table, int row, RowRenderData rowData, int startCol) {
        if (null == rowData || rowData.size() <= 0) return;
        XWPFTableRow tableRow = table.getRow(row);
        ObjectUtils.requireNonNull(tableRow, "Row " + row + " do not exist in the table");

        TableStyle rowStyle = rowData.getRowStyle();
        List<CellRenderData> cellList = rowData.getCellDatas();
        XWPFTableCell cell = null;
        
        int spanCol = 0;
        for (int i = 0; i < cellList.size() + startCol; i++) {
            cell = tableRow.getCell(i);
            if (null == cell) {
                break;
            }
            if(cell.getCTTc().getTcPr().getGridSpan() != null) {
            	spanCol = cell.getCTTc().getTcPr().getGridSpan().getVal().intValue() -1;
            	log.info("startCol is {}", startCol);
            }
        }
        startCol = startCol - spanCol;
        for (int i = 0; i < cellList.size() + startCol; i++) {
        	if (i < startCol) {
        		log.info("cureent startCol is " + i);
        		continue;
        	} 
            cell = tableRow.getCell(i);
            if (null == cell) {
            	log.warn("Extra cell data at row {}, but no extra cell: col {}", row, i);
                break;
            }
//            logger.info("data is ", cellList.get(i - startCol));
            MiniTableRenderPolicy.Helper.renderCell(cell, cellList.get(i - startCol), rowStyle);
        }
    }



}
