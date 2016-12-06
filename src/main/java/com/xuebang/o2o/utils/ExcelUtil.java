package com.xuebang.o2o.utils;

import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Created by Administrator on 2016/1/15.
 */
public class ExcelUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static void ExcelWrite(List<String[]> writeDate, File file) {

        // 创建excel
        HSSFWorkbook wb = new HSSFWorkbook();

        // 创建sheet
        HSSFSheet sheet = wb.createSheet("运单数据");

        // 创建一行
        HSSFRow rowTitle = sheet.createRow(0);

        // 创建标题栏样式
        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
        HSSFFont fontTitle = wb.createFont();
        // 宋体加粗
        fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontTitle.setFontName("宋体");
        fontTitle.setFontHeight((short) 200);
        styleTitle.setFont(fontTitle);

        // 在行上创建1列
//        HSSFCell cellTitle = rowTitle.createCell(0);
//
//        // 列标题及样式
//        cellTitle.setCellValue("运单号");
//        cellTitle.setCellStyle(styleTitle);
//
//        // 在行上创建2列
//        cellTitle = rowTitle.createCell(1);
//        cellTitle.setCellValue("代理人");
//        cellTitle.setCellStyle(styleTitle);

        HSSFCellStyle styleCenter = wb.createCellStyle();
        styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中

        // 取数据

        int f = 1;
        for (int i = 0; i < writeDate.size(); i++) {

            String[] data = writeDate.get(i);
            if(i %65534 == 0){

                sheet = wb.createSheet("new"+String.valueOf(f));
                f++;
                // 创建一行
                HSSFRow rowTitle2 = sheet.createRow(0);

                // 创建标题栏样式
                HSSFCellStyle styleTitle2 = wb.createCellStyle();
                styleTitle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
                HSSFFont fontTitle2 = wb.createFont();
                // 宋体加粗
                fontTitle2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                fontTitle2.setFontName("宋体");
                fontTitle2.setFontHeight((short) 200);
                styleTitle2.setFont(fontTitle);
            }
            HSSFRow row = sheet.createRow((i + 1)%65535);

            for (int j = 0; j < data.length; j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(data[j]);
                cell.setCellStyle(styleCenter);
            }

        }

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            wb.write(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        logger.info("导出完成");
    }

}
