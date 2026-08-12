package com.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.util.Base64;

/**
 * Extent Reports manager — initializes, starts/stops tests, captures screenshots.
 */
public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final PathConfig pathConfig = new PathConfig();

    private static volatile boolean initialized = false;

    /**
     * Thread-safe lazy initialization of the ExtentReports instance.
     */
    public static void ensureInitialized() {
        if (!initialized) {
            synchronized (ExtentReportManager.class) {
                if (!initialized) {
                    initReport();
                    initialized = true;
                }
            }
        }
    }

    private ExtentReportManager() {}

    public static void initReport() {
        if (extentReports != null) {
            return;
        }

        String reportTitle = ConfigLoader.get("report.title", "SauceDemo Test Report");
        String reportTheme = ConfigLoader.get("report.theme", "standard");
        String reportEncoding = ConfigLoader.get("report.encoding", "UTF-8");
        String reportTimestampFormat = ConfigLoader.get("report.timestamp.format", "dd MMM yyyy HH:mm:ss a");

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(pathConfig.fileFromExtentReport("report.html"));
        sparkReporter.config().setTheme(reportTheme.equalsIgnoreCase("dark") ? Theme.DARK : Theme.STANDARD);
        sparkReporter.config().setDocumentTitle(reportTitle);
        sparkReporter.config().setReportName(reportTitle);
        sparkReporter.config().setEncoding(reportEncoding);
        sparkReporter.config().setTimeStampFormat(reportTimestampFormat);
        sparkReporter.config().setTimelineEnabled(true);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java", System.getProperty("java.version"));
        extentReports.setSystemInfo("Browser(s)", ConfigLoader.get("browser", "chromium,firefox,webkit"));
        extentReports.setSystemInfo("Framework", "AutoVD-Java-Playwright");
        extentReports.setSystemInfo("Author", ConfigLoader.get("report.author", "Automation Team"));
    }

    public static void startTest(String testName, String description, String category, String author, String device) {
        ExtentTest extentTest = extentReports.createTest(testName, description);
        extentTest.assignCategory(category);
        extentTest.assignAuthor(author);
        if (device != null && !device.isEmpty()) {
            extentTest.assignDevice(device);
        }
        test.set(extentTest);
    }

    public static void startTest(String testName, String description, String category, String author) {
        startTest(testName, description, category, author, null);
    }

    public static void startTest(String testName, String description) {
        String category = ConfigLoader.get("report.category", "Regression");
        String author = ConfigLoader.get("report.author", "Automation Team");
        startTest(testName, description, category, author);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void pass(String message) {
        if (test.get() != null) {
            test.get().pass(message);
        }
    }

    public static void pass(String message, byte[] screenshot) {
        if (test.get() != null) {
            if (screenshot != null && screenshot.length > 0) {
                try {
                    String base64 = Base64.getEncoder().encodeToString(screenshot);
                    test.get().pass(message, MediaEntityBuilder
                            .createScreenCaptureFromBase64String(base64)
                            .build());
                } catch (Exception e) {
                    test.get().pass(message);
                }
            } else {
                test.get().pass(message);
            }
        }
    }

    public static void fail(String message) {
        if (test.get() != null) {
            test.get().fail(message);
        }
    }

    public static void fail(String message, byte[] screenshot) {
        if (test.get() != null && screenshot != null && screenshot.length > 0) {
            try {
                String base64 = Base64.getEncoder().encodeToString(screenshot);
                test.get().fail(message, MediaEntityBuilder
                        .createScreenCaptureFromBase64String(base64)
                        .build());
            } catch (Exception e) {
                test.get().fail(message);
            }
        } else {
            fail(message);
        }
    }

    public static void fail(Throwable throwable, byte[] screenshot) {
        if (test.get() != null) {
            if (screenshot != null && screenshot.length > 0) {
                try {
                    String base64 = Base64.getEncoder().encodeToString(screenshot);
                    test.get().fail(throwable, MediaEntityBuilder
                            .createScreenCaptureFromBase64String(base64)
                            .build());
                } catch (Exception e) {
                    test.get().fail(throwable);
                }
            } else {
                test.get().fail(throwable);
            }
        }
    }

    public static void fail(Throwable throwable) {
        if (test.get() != null) {
            test.get().fail(throwable);
        }
    }

    public static void skip(String message) {
        if (test.get() != null) {
            test.get().skip(message);
        }
    }

    public static void info(String message) {
        if (test.get() != null) {
            test.get().info(message);
        }
    }

    public static void warning(String message) {
        if (test.get() != null) {
            test.get().warning(message);
        }
    }

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static void endReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static void removeThread() {
        test.remove();
    }
}
