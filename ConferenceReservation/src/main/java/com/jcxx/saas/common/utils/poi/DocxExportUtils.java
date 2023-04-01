package com.jcxx.saas.common.utils.poi;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.jcxx.saas.common.utils.poi.policys.FillTableMapDataRenderPolicy;
import com.jcxx.saas.common.utils.poi.vo.OutDto;
import com.jcxx.saas.common.utils.poi.vo.TableData;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @filename: DocxExportUtils.java
 * @author: carfield
 * @version: 1.0.0
 * @creatat: 2019年8月30日 下午5:58:42
 * @Copyright (C) 2019 IreadTech.com All Rights Reserved.
 */

public class DocxExportUtils {
    private static final Logger log = LoggerFactory.getLogger(DocxExportUtils.class);

    /**
     * exportFieldDocx 导出简单表的Word文档
     *
     * @param modelPath  模板全路径
     * @param exportPath 导出文件存放全路径
     * @param data       源数据
     * @throws IOException
     */
    public static void exportFieldDocx(String modelPath, String exportPath, Map<String, Object> data)
            throws IOException {
        log.info("start exportFieldDocx  to {}", exportPath);
        XWPFTemplate template = XWPFTemplate.compile(modelPath).render(data);

        FileOutputStream out = new FileOutputStream(exportPath);
        template.write(out);
        out.flush();
        out.close();
        template.close();
    }

    /**
     * exportTableDocx 导出模板表格的Word文档
     *
     * @param modelPath  模板全路径
     * @param exportPath 导出文件存放全路径
     * @param tablename  模板当中表格名称
     * @param data       源数据
     * @throws IOException
     */
    public static void exportTableDocx(String modelPath, String exportPath, String tablename, Map<String, Object> data)
            throws IOException {
        Configure config = Configure.newBuilder().customPolicy(tablename, new FillTableMapDataRenderPolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile(modelPath, config).render(data);
        FileOutputStream out = new FileOutputStream(exportPath);
        template.write(out);
        out.flush();
        out.close();
        template.close();

    }

    // -------------以下均为测试用代码-----------------------------
    // [BEGIN]

    public static void main(String[] args) throws IOException {

        exportTablePojo();

        exportSingle();
    }

    private static OutDto initTableData() {
        OutDto data = new OutDto();
        data.setOutday(LocalDate.now().toString());
        data.setSubmitcorp("湖南嘉创信息科技有限公司");
        List<RowRenderData> datalist = new ArrayList<RowRenderData>();
        RowRenderData good = RowRenderData.build("总数111", "预备党员", "女", "少数民族", "台湾省籍", "30岁及以下", "31至35岁", "36至40岁",
                "41至45岁", "46至50岁", "51至55岁", "56至60岁", "61至65岁", "66至70岁", "71岁及以上");
        datalist = Arrays.asList(good, good, good, good, good, good, good, good, good, good, good, good, good, good,
                good, good);
        TableData tableData = new TableData();
        tableData.setStartRow(3);
        tableData.setStartColum(4);
        tableData.setBasicDataList(datalist);
        data.setTableData(tableData);

        return data;
    }

    private static Map<String, Object> initTableMapData() {
        Map<String, Object> data = new HashedMap();
        data.put("submitcorp", "湖南嘉创信息科技有限公司");
        data.put("outday", LocalDate.now().toString());

        List<RowRenderData> datalist = new ArrayList<RowRenderData>();
        RowRenderData good = RowRenderData.build("总数111", "预备党员", "女", "少数民族", "台湾省籍", "30岁及以下", "31至35岁", "36至40岁",
                "41至45岁", "46至50岁", "51至55岁", "56至60岁", "61至65岁", "66至70岁", "71岁及以上");
        datalist = Arrays.asList(good, good, good, good, good, good, good, good, good, good, good, good, good, good,
                good, good);

        Map<String, Object> tableData = new HashedMap();
        tableData.put("startRow", 3);
        tableData.put("startColum", 4);
        tableData.put("datalist", datalist);

        data.put("detail_table", tableData);

        return data;
    }

    private static Map<String, Object> initPojoData() {
        Map<String, Object> data = new HashedMap();
        data.put("submitcorp", "湖南嘉创信息科技有限公司");
        data.put("lastyearTotal", "925");
        data.put("curyearadded", 18);

        return data;
    }

    private static void exportTablePojo() throws FileNotFoundException, IOException {
        String modelPath = "D:\\Docs\\templates\\党员基本情况.docx";
        String exportPath = "D:\\Docs\\output\\党员基本情况.docx";
        exportTableDocx(modelPath, exportPath, "detail_table", initTableMapData());
    }

    private static void exportSingle() {
        String modelPath = "D:\\Docs\\templates\\党员数量变化情况.docx";
        String exportPath = "D:\\Docs\\output\\党员数量变化情况.docx";
        try {
            DocxExportUtils.exportFieldDocx(modelPath, exportPath, initPojoData());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // [END] 测试用代码

}
