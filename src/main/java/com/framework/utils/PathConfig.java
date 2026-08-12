package com.framework.utils;

import java.io.File;

/**
 * Manages framework directory paths for reports, screenshots, test data, etc.
 * Auto-creates all directories on first access.
 */
public class PathConfig {

    private final String projectRoot;
    private final String reportPath;
    private final String screenshotPath;
    private final String passedShotPath;
    private final String failedShotPath;
    private final String skippedShotPath;
    private final String extentReportPath;
    private final String excelReportPath;
    private final String testDataPath;
    private final String jsonTestDataPath;
    private final String excelTestDataPath;
    private final String textTestDataPath;
    private final String suitesXMLPath;
    private final String propertiesPath;
    private final String imagesPath;

    public PathConfig() {
        this.projectRoot = new File(".").getAbsolutePath();
        this.reportPath = projectRoot + File.separator + "Report-ScreenShot" + File.separator;
        this.screenshotPath = reportPath + "screenshots" + File.separator;
        this.passedShotPath = screenshotPath + "passed" + File.separator;
        this.failedShotPath = screenshotPath + "failed" + File.separator;
        this.skippedShotPath = screenshotPath + "skipped" + File.separator;
        this.extentReportPath = projectRoot + File.separator + "target" + File.separator + "extentreport" + File.separator;
        this.excelReportPath = reportPath + "excel" + File.separator;
        this.testDataPath = projectRoot + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "TestData" + File.separator;
        this.jsonTestDataPath = testDataPath + "JSON" + File.separator;
        this.excelTestDataPath = testDataPath + "Excel" + File.separator;
        this.textTestDataPath = testDataPath + "Text" + File.separator;
        this.suitesXMLPath = projectRoot + File.separator + "SuitesXML" + File.separator;
        this.propertiesPath = projectRoot + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        this.imagesPath = projectRoot + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "Images" + File.separator;

        createDirectories();
    }

    private void createDirectories() {
        dirs(passedShotPath, failedShotPath, skippedShotPath,
             extentReportPath, excelReportPath,
             testDataPath, jsonTestDataPath, excelTestDataPath, textTestDataPath,
             suitesXMLPath, imagesPath);
    }

    private void dirs(String... paths) {
        for (String p : paths) {
            new File(p).mkdirs();
        }
    }

    public String fileFromPassedShot(String fileName) {
        return passedShotPath + fileName;
    }

    public String fileFromFailedShot(String fileName) {
        return failedShotPath + fileName;
    }

    public String fileFromSkippedShot(String fileName) {
        return skippedShotPath + fileName;
    }

    public String fileFromExtentReport(String fileName) {
        return extentReportPath + fileName;
    }

    public String fileFromExcelReport(String fileName) {
        return excelReportPath + fileName;
    }

    public String fileFromProperties(String fileName) {
        return propertiesPath + fileName;
    }

    public String fileFromJsonTestData(String fileName) {
        return jsonTestDataPath + fileName;
    }

    public String fileFromExcelTestData(String fileName) {
        return excelTestDataPath + fileName;
    }

    public String fileFromTextTestData(String fileName) {
        return textTestDataPath + fileName;
    }

    public String getReportScreenShotPath() {
        return screenshotPath;
    }

    public String getTestOutputPath() {
        return projectRoot + File.separator + "target" + File.separator;
    }

    public String getProjectRoot() {
        return projectRoot;
    }
}
