package com.most.core.pub.tools.office;

import com.most.core.pub.tools.datastruct.ArrayTool;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: hirun
 * @description: ${description}
 * @author: jinnian
 * @create: 2019-03-15 09:51
 **/
public class ExcelExport {

    public static HSSFWorkbook getDataExcel(String sheetName, List<String> titles, List<List<String>> values) throws Exception {

        if(ArrayTool.isEmpty(titles) || ArrayTool.isEmpty(values)) {
            return null;
        }

        HSSFWorkbook excel = new HSSFWorkbook();

        HSSFSheet sheet = excel.createSheet(sheetName);

        //创建单元格居中的样式
        HSSFCellStyle style = excel.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);

        HSSFRow titleRow = sheet.createRow(0);

        //创建标题
        int colIndex=0;
        for(String title : titles) {
            HSSFCell cell = titleRow.createCell(colIndex);
            cell.setCellValue(title);
            cell.setCellStyle(style);
            colIndex++;
        }

        //创建表格内容
        int rowNum = 1;
        for(List<String> value : values) {
            HSSFRow row = sheet.createRow(rowNum);
            int colNum = 0;
            for(String cellValue : value) {
                HSSFCell cell = row.createCell(colNum);
                cell.setCellValue(cellValue);
                cell.setCellStyle(style);
                colNum++;
            }
            rowNum++;
        }

        for(int i=0;i<colIndex;i++) {
            sheet.autoSizeColumn(i);
        }
        return excel;
    }
}
