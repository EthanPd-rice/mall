package com.ethan.mall.util;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {
    public static Object getCellValue(Cell cell){
        switch (cell.getCellTypeEnum()){
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            //数值
            case NUMERIC:
                return cell.getNumericCellValue();
        }
        return null;
    }
}
