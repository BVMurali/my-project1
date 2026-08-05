package com.eventhub.framework.utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads tabular Excel (.xlsx) test data into a list of row-maps keyed by
 * the header row, for data-driven scenarios.
 */
public final class ExcelUtils {

    private ExcelUtils() {
    }

    public static List<Map<String, String>> readSheet(String filePath, String sheetName) {
        List<Map<String, String>> rows = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName + " in " + filePath);
            }
            Row headerRow = sheet.getRow(0);
            int lastCol = headerRow.getLastCellNum();

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                if (currentRow == null) {
                    continue;
                }
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int colIndex = 0; colIndex < lastCol; colIndex++) {
                    String header = getCellValueAsString(headerRow.getCell(colIndex));
                    String value = getCellValueAsString(currentRow.getCell(colIndex));
                    rowData.put(header, value);
                }
                rows.add(rowData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        return rows;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }
}
