package com.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel utility — reads and writes test data from .xlsx files.
 */
public class ExcelUtil {

    private final FileInputStream fin;
    private final FileOutputStream fout;
    private final XSSFWorkbook workbook;
    private final String excelPath;

    public ExcelUtil(String excelPath) {
        this.excelPath = excelPath;
        try {
            fin = new FileInputStream(new File(excelPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Excel file not found: " + excelPath, e);
        }
        try {
            workbook = new XSSFWorkbook(fin);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open Excel workbook: " + excelPath, e);
        }
        this.fout = null;
    }

    /**
     * Read cell data by sheet name, row name (header match), and column name (header match).
     */
    public String getCellDataByValue(String sheetName, String rowName, String colName) {
        int rowNum = getRowNumber(sheetName, rowName);
        int colNum = getColumnNumber(sheetName, colName);
        return getCellDataByNum(sheetName, rowNum, colNum);
    }

    /**
     * Read all data from a sheet as a 2D String array.
     */
    public String[][] getFullSheetData(String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return new String[0][0];

        int totalRows = sheet.getLastRowNum();
        int totalCols = sheet.getRow(0) != null ? sheet.getRow(0).getLastCellNum() : 0;

        String[][] data = new String[totalRows][totalCols];
        for (int i = 0; i < totalRows; i++) {
            XSSFRow row = sheet.getRow(i);
            for (int j = 0; j < totalCols; j++) {
                XSSFCell cell = row != null ? row.getCell(j) : null;
                DataFormatter formatter = new DataFormatter();
                data[i][j] = cell != null ? formatter.formatCellValue(cell) : "";
            }
        }
        return data;
    }

    public int getRowCount(String sheetName) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        return sheet != null ? sheet.getLastRowNum() : 0;
    }

    public int getCellCount(String sheetName, int rowNum) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        XSSFRow row = sheet != null ? sheet.getRow(rowNum) : null;
        return row != null ? row.getLastCellNum() : 0;
    }

    private int getRowNumber(String sheetName, String value) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    DataFormatter formatter = new DataFormatter();
                    if (formatter.formatCellValue(row.getCell(j)).equals(value)) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    private int getColumnNumber(String sheetName, String value) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null || sheet.getRow(0) == null) return 0;
        for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
            DataFormatter formatter = new DataFormatter();
            if (formatter.formatCellValue(sheet.getRow(0).getCell(j)).equals(value)) {
                return j;
            }
        }
        return 0;
    }

    private String getCellDataByNum(String sheetName, int rowNum, int colNum) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null || sheet.getRow(rowNum) == null) return "";
        XSSFCell cell = sheet.getRow(rowNum).getCell(colNum);
        DataFormatter formatter = new DataFormatter();
        return cell != null ? formatter.formatCellValue(cell) : "";
    }

    /**
     * Write data to a cell (creates file/sheet/row if needed).
     */
    public void setCellData(String sheetName, int rowNum, int colNum, String data) {
        XSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) sheet = workbook.createSheet(sheetName);

        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) row = sheet.createRow(rowNum);

        XSSFCell cell = row.getCell(colNum);
        if (cell == null) cell = row.createCell(colNum);
        cell.setCellValue(data);

        try (FileOutputStream fos = new FileOutputStream(new File(excelPath))) {
            workbook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel: " + excelPath, e);
        }
    }

    public void close() {
        try {
            if (fin != null) fin.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
