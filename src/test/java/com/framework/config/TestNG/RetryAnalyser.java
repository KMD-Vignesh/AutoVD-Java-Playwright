package com.framework.config.TestNG;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer that retries failed tests up to 2 times.
 */
public class RetryAnalyser implements IRetryAnalyzer {
    private int count = 0;
    private static final int retryLimit = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (count < retryLimit) {
            count++;
            System.out.println("[RETRY] Retrying test: " + result.getName()
                    + " (attempt " + count + "/" + retryLimit + ")");
            return true;
        }
        return false;
    }
}
