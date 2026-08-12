package com.framework.base;

import com.aventstack.extentreports.ExtentTest;
import com.framework.config.Author;
import com.framework.config.TestID;
import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;
import com.framework.utils.ExtentReportManager;
import com.framework.utils.GeneralUtil;
import com.framework.utils.PathConfig;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

/**
 * Base test class providing browser lifecycle management, ExtentReport integration,
 * and utility accessors for all test classes.
 */
public abstract class BaseTest {
    protected final PathConfig pathConfig = new PathConfig();

    /**
     * Returns the ExtentTest instance for the current thread.
     */
    protected ExtentTest getExtentTest() {
        return ExtentReportManager.getTest();
    }

    /**
     * Returns the PathConfig for report/screenshot paths.
     */
    protected PathConfig getPathConfig() {
        return pathConfig;
    }

    @BeforeMethod
    public void setup(Method method, Object[] testData) {
        if (testData != null && testData.length > 0 && testData[0] instanceof String) {
            String browserName = (String) testData[0];
            DriverManager.setBrowser(browserName);
        }
        DriverManager.initDriver();

        // Thread-safe lazy init for ExtentReports
        ExtentReportManager.ensureInitialized();

        String testName = method.getName();
        String description = getTestDescription(method);
        String category = getTestCategory(method);
        String author = getTestAuthor(method);
        String browser = DriverManager.getBrowserName();

        ExtentReportManager.startTest(testName, description, category, author, browser);
        ExtentReportManager.info("Test started: " + testName);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        byte[] screenshot = null;

        if (result.getStatus() == ITestResult.FAILURE) {
            screenshot = DriverManager.takeScreenshot();
            ExtentReportManager.fail(result.getThrowable(), screenshot);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentReportManager.pass("Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentReportManager.skip("Test Skipped");
        }

        ExtentReportManager.removeThread();
        DriverManager.quit();
    }

    protected <T extends BasePage> T getPageObject(Class<T> pageClass) {
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate page: " + pageClass.getName(), e);
        }
    }

    /**
     * Extract description from @Test annotation or method Javadoc.
     */
    private String getTestDescription(Method method) {
        org.testng.annotations.Test testAnnotation = method.getAnnotation(org.testng.annotations.Test.class);
        if (testAnnotation != null && testAnnotation.description() != null
                && !testAnnotation.description().isEmpty()) {
            return testAnnotation.description();
        }
        return "No description";
    }

    /**
     * Extract test category from TestNG groups.
     */
    private String getTestCategory(Method method) {
        org.testng.annotations.Test testAnnotation = method.getAnnotation(org.testng.annotations.Test.class);
        if (testAnnotation != null && testAnnotation.groups().length > 0) {
            for (String group : testAnnotation.groups()) {
                if ("Login".equals(group) || "E2E".equals(group) || "smoke".equals(group)
                        || "negative".equals(group) || "failure".equals(group)) {
                    return group;
                }
            }
            return testAnnotation.groups()[0];
        }
        return "Regression";
    }

    /**
     * Extract author from @Author annotation or TestNG groups.
     */
    private String getTestAuthor(Method method) {
        Author authorAnnotation = method.getAnnotation(Author.class);
        if (authorAnnotation != null && !authorAnnotation.Name().isEmpty()) {
            return authorAnnotation.Name();
        }

        org.testng.annotations.Test testAnnotation = method.getAnnotation(org.testng.annotations.Test.class);
        if (testAnnotation != null) {
            for (String group : testAnnotation.groups()) {
                if ("Vignesh".equals(group) || "Vicky".equals(group) || "KMDV".equals(group)) {
                    return group;
                }
            }
        }
        return "Automation Team";
    }
}
