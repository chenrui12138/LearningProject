package com.jcxx.saas.modules.mrs.utils;

import com.jcxx.saas.modules.mrs.entity.MrsMeetingRoomEntity;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 *将对象写入Excel
 */
public class ExcelUtil {
    public static Workbook fillExcelWithTemplate(List<MrsMeetingRoomEntity> meetingRoomEntities) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //取得模板的第一个sheet页
        HSSFSheet sheet =  wb.createSheet("会议室");
        sheet.setDefaultColumnWidth(35);
        //拿到sheet页有多少列
//            int cellNum = sheet.getRow(0).getLastCellNum();
        //从第二行开始，下标1为第二行
        int rowIndex = 1;
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("会议室名称");
        row.createCell(1).setCellValue("会议室地址");
        row.createCell(2).setCellValue("座位");
        row.createCell(3).setCellValue("设备");
        row.createCell(4).setCellValue("状态");
        for (MrsMeetingRoomEntity roomEntity : meetingRoomEntities) {
            row = sheet.createRow(rowIndex);
            rowIndex++;
            row.createCell(0).setCellValue(roomEntity.getName());
            row.createCell(1).setCellValue(roomEntity.getAddress());
            row.createCell(2).setCellValue(roomEntity.getSeat());
            row.createCell(3).setCellValue(roomEntity.getEquipment());
            String state = null;
            if(roomEntity.getState() == 1){
                state = "正常";
            }
            if(roomEntity.getState() == 2){
                state = "维修";
            }
            if(roomEntity.getState() == 3){
                state = "报废";
            }
            row.createCell(4).setCellValue(state);
        }
        return wb;
    }
}
