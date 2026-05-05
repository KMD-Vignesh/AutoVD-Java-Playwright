package com.framework.utils;

import com.framework.driver.DriverManager;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Base64;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[STARTED] " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[PASSED] " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[FAILED] " + result.getName());
        
        if (ConfigLoader.getBoolean("screenshot.on.failure")) {
            try {
                byte[] screenshot = DriverManager.takeScreenshot();
                if (screenshot != null && screenshot.length > 0) {
                    String base64 = Base64.getEncoder().encodeToString(screenshot);
                    result.setAttribute("screenshot", base64);
                }
            } catch (Exception e) {
                System.out.println("[ERROR] Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[SKIPPED] " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("[FAILED Within %] " + result.getName());
    }
}