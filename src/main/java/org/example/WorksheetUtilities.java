package org.example;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class WorksheetUtilities {
    static Sheet createSheet (XSSFWorkbook workbook, String name) {
        Sheet sheet = workbook.createSheet(name);
        return sheet;
    }

}
