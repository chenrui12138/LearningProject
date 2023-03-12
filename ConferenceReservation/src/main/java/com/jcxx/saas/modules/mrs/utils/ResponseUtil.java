package com.jcxx.saas.modules.mrs.utils;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * wll 数据流导入导出
 */
public class ResponseUtil {

    public static void write(HttpServletResponse response,Object o)throws Exception{
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.println(o.toString());
        out.flush();
        out.close();
    }

    public static  void export(HttpServletResponse response,Workbook wb,String fileName)throws Exception{
        response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"iso8859-1"));
        OutputStream out=response.getOutputStream();
        wb.write(out);
        out.flush();
        out.close();
    }


}
