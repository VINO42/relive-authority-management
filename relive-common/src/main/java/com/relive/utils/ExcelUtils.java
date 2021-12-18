package com.relive.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author ReLive
 * @Date 2021/1/18-14:36
 */
public class ExcelUtils {


    public static HSSFWorkbook createExcel(String[] headers, List<List<String>> data) throws IOException {
        File file = new File("template.xls");

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        cellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.index);

        //通过工作簿创建sheet
        HSSFSheet sheet = hssfWorkbook.createSheet();
        //获取第一行
        HSSFRow row = sheet.createRow(0);
        //设置第一行第一列单元格数据
        for (int var = 0; var < headers.length; var++) {
            row.createCell(var).setCellValue(headers[var]);
        }
        for (int var1 = 0; var1 < data.size(); var1++) {
            HSSFRow row1 = sheet.createRow(var1 + 1);
            List<String> rowData = data.get(var1);
            for (int var2 = 0; var2 < rowData.size(); var2++) {
                row1.createCell(var2).setCellStyle(cellStyle);
                row1.createCell(var2).setCellValue(rowData.get(var2));
            }
        }

        hssfWorkbook.setActiveSheet(0);
        return hssfWorkbook;
    }

}
