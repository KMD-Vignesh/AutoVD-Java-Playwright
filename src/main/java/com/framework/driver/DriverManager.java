package com.framework.driver;

import com.framework.utils.ConfigLoader;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.Properties;

public class DriverManager {
    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();
    private static final ThreadLocal<Properties> CONFIG = new ThreadLocal<>();
    private static final ThreadLocal<String> BROWSER_NAME = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_BROWSER = new ThreadLocal<>();

    private DriverManager() {}

    public static void setBrowser(String browserName) {
        CURRENT_BROWSER.set(browserName);
    }

    public static void initDriver() {
        String browser = CURRENT_BROWSER.get();
        if (browser == null || browser.isEmpty()) {
            browser = System.getProperty("browser", "chromium");
        }
        initDriver(browser);
    }

    public static void initDriver(String browserName) {
        if (CONFIG.get() == null) {
            ConfigLoader.loadDefault();
            CONFIG.set(ConfigLoader.getAll());
        }

        String selectedBrowser = browserName;
        if (selectedBrowser == null || selectedBrowser.isEmpty()) {
            selectedBrowser = CONFIG.get().getProperty("browser", "chromium");
        }
        if (selectedBrowser.contains(",")) {
            selectedBrowser = selectedBrowser.split(",")[0].trim();
        }
        
        selectedBrowser = normalizeBrowserName(selectedBrowser);
        
        BROWSER_NAME.set(selectedBrowser);

        Playwright playwright = Playwright.create();
        PLAYWRIGHT.set(playwright);

        Properties config = CONFIG.get();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", config.getProperty("headless", "true")));
        int slowmo = Integer.parseInt(System.getProperty("slowmo", config.getProperty("slowmo", "0")));

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions();
        launchOptions.setHeadless(headless);
        launchOptions.setSlowMo(slowmo);

        BrowserType browserLauncher = getBrowserType(playwright, selectedBrowser);
        Browser newBrowser = browserLauncher.launch(launchOptions);
        BROWSER.set(newBrowser);

        int width = Integer.parseInt(config.getProperty("viewport.width", "1920"));
        int height = Integer.parseInt(config.getProperty("viewport.height", "1080"));

        BrowserContext newContext = newBrowser.newContext(new Browser.NewContextOptions()
                .setViewportSize(width, height));
        CONTEXT.set(newContext);

        Page newPage = newContext.newPage();
        PAGE.set(newPage);

        String baseUrl = config.getProperty("base.url");
        newPage.navigate(baseUrl);
    }

private static BrowserType getBrowserType(Playwright playwright, String browserType) {
        return switch (browserType.toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            case "chromium" -> playwright.chromium();
            default -> playwright.chromium();
        };
    }
    
    private static String normalizeBrowserName(String browserName) {
        if (browserName == null) return "Chromium";
        return switch (browserName.toLowerCase()) {
            case "firefox" -> "Firefox";
            case "webkit" -> "WebKit";
            case "chromium" -> "Chromium";
            default -> "Chromium";
        };
    }
    
    public static String getBrowserName() {
        return BROWSER_NAME.get();
    }

    public static Page getPage() {
        return PAGE.get();
    }

    public static BrowserContext getContext() {
        return CONTEXT.get();
    }

    public static Browser getBrowser() {
        return BROWSER.get();
    }

    public static Properties getConfig() {
        return CONFIG.get();
    }

    public static String getConfigValue(String key) {
        return CONFIG.get().getProperty(key);
    }

    public static String getBrowserName() {
        return BROWSER_NAME.get();
    }

    public static void quit() {
        if (PAGE.get() != null) {
            PAGE.get().close();
        }
        if (CONTEXT.get() != null) {
            CONTEXT.get().close();
        }
        if (BROWSER.get() != null) {
            BROWSER.get().close();
        }
        if (PLAYWRIGHT.get() != null) {
            PLAYWRIGHT.get().close();
        }

        PAGE.remove();
        CONTEXT.remove();
        BROWSER.remove();
        PLAYWRIGHT.remove();
        BROWSER_NAME.remove();
    }

    public static byte[] takeScreenshot() {
        Page page = PAGE.get();
        if (page != null) {
            return page.screenshot();
        }
        return new byte[0];
    }
}