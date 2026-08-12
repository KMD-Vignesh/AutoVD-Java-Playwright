package com.framework.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.framework.driver.DriverManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/**
 * TestNG listener — captures screenshots on failure/skip, logs to ExtentReports.
 */
public class TestListener implements ITestListener {

    private static final PathConfig PATH_CONFIG = new PathConfig();

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[STARTED] " + result.getName()
                + " | Browser: " + (DriverManager.getBrowserName() != null ? DriverManager.getBrowserName() : "N/A"));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[PASSED] " + result.getName());
        try {
            ExtentTest t = ExtentReportManager.getTest();
            if (t != null) {
                byte[] screenshot = DriverManager.takeScreenshot();
                if (screenshot != null && screenshot.length > 0) {
                    String base64 = Base64.getEncoder().encodeToString(screenshot);
                    t.pass("Test Passed", MediaEntityBuilder
                            .createScreenCaptureFromBase64String(base64)
                            .build());
                } else {
                    t.pass("Test Passed");
                }
            }
        } catch (Exception e) {
            System.err.println("[WARN] Screenshot capture failed: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[FAILED] " + result.getName());

        try {
            byte[] screenshot = DriverManager.takeScreenshot();
            ExtentTest t = ExtentReportManager.getTest();
            if (t != null && screenshot != null && screenshot.length > 0) {
                String base64 = Base64.getEncoder().encodeToString(screenshot);
                t.fail(result.getThrowable(), MediaEntityBuilder
                        .createScreenCaptureFromBase64String(base64)
                        .build());
            } else if (t != null) {
                t.fail(result.getThrowable());
            }

            // Also save a physical screenshot file for failure review
            if (screenshot != null && screenshot.length > 0
                    && ConfigLoader.getBoolean("screenshot.on.failure")) {
                saveScreenshotToFile(result.getName(), screenshot, "failed");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to capture failure screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[SKIPPED] " + result.getName());
        try {
            ExtentTest t = ExtentReportManager.getTest();
            if (t != null) {
                t.skip("Test Skipped" + (result.getThrowable() != null ? ": " + result.getThrowable().getMessage() : ""));
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("[FAILED Within %] " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n===============================================");
        System.out.println("Test Suite Started: " + context.getName());
        System.out.println("===============================================\n");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.endReport();
        System.out.println("\n===============================================");
        System.out.println("Test Suite Completed: " + context.getName());
        System.out.println("Total: " + context.getAllTestMethods().length
                + " | Passed: " + context.getPassedTests().size()
                + " | Failed: " + context.getFailedTests().size()
                + " | Skipped: " + context.getSkippedTests().size());
        System.out.println("===============================================\n");
    }

    /**
     * Save a screenshot byte array to a physical file in the appropriate folder.
     */
    private void saveScreenshotToFile(String testName, byte[] screenshot, String status) {
        try {
            String timestamp = GeneralUtil.getTimeStamp();
            String fileName = testName + "_" + timestamp + "_" + status + ".png";
            File dest = new File(PATH_CONFIG.getReportScreenShotPath() + status + File.separator + fileName);
            dest.getParentFile().mkdirs();
            Files.write(dest.toPath(), screenshot);
            System.out.println("[SCREENSHOT] Saved: " + dest.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("[ERROR] Could not save screenshot: " + e.getMessage());
        }
    }
}
