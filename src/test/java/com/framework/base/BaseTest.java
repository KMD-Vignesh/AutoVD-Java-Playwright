package com.framework.base;

import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;
import com.framework.utils.ConfigLoader;
import com.framework.utils.ExtentReportManager;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public abstract class BaseTest {
    protected static boolean initialized = false;
    protected static boolean reportInitialized = false;
    protected String category = "Regression";

    @BeforeClass
    public void beforeClass(ITestContext context) {
        if (!initialized) {
            ConfigLoader.loadDefault();
            initialized = true;
        }
        if (!reportInitialized) {
            ExtentReportManager.initReport();
            reportInitialized = true;
        }
    }

    @BeforeMethod
    public void setup(Method method, Object[] testData) {
        if (testData != null && testData.length > 0 && testData[0] instanceof String) {
            String browserName = (String) testData[0];
            DriverManager.setBrowser(browserName);
        }
        DriverManager.initDriver();
        
        org.testng.annotations.Test testAnnotation = method.getAnnotation(org.testng.annotations.Test.class);
        String description = testAnnotation != null ? testAnnotation.description() : method.getName();
        String testCategory = getCategory(method);
        String author = getAuthor(method);
        String browser = DriverManager.getBrowserName();
        
        ExtentReportManager.startTest(method.getName(), description, testCategory, author, browser);
        ExtentReportManager.info("Test started: " + method.getName());
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
        
        ExtentReportManager.flush();
        DriverManager.quit();
    }

    @AfterClass
    public void afterClass() {
        ExtentReportManager.flush();
    }

    protected <T extends BasePage> T getPageObject(Class<T> pageClass) {
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate page: " + pageClass.getName(), e);
        }
    }

    protected String getCategory(Method method) {
        org.testng.annotations.Test annotation = method.getAnnotation(org.testng.annotations.Test.class);
        if (annotation != null && annotation.groups().length > 0) {
            for (String group : annotation.groups()) {
                if (group.equals("Login") || group.equals("E2E") || group.equals("smoke") || 
                    group.equals("negative")) {
                    return group;
                }
            }
            return annotation.groups()[0];
        }
        return "Regression";
    }

    private static final String[] AUTHORS = {"Vignesh", "Vicky", "KMDV"};
    
    protected String getAuthor(Method method) {
        org.testng.annotations.Test annotation = method.getAnnotation(org.testng.annotations.Test.class);
        if (annotation != null && annotation.groups().length > 0) {
            for (String group : annotation.groups()) {
                for (String author : AUTHORS) {
                    if (author.equals(group)) {
                        return author;
                    }
                }
            }
        }
        return "Automation Team";
    }
}