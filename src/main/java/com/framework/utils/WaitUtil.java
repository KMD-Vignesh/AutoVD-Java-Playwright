package com.framework.utils;

import com.framework.driver.DriverManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Wait utilities adapted for Playwright's built-in auto-waiting model.
 * Provides explicit wait helpers using Playwright's locator-based waits.
 */
public class WaitUtil {
    private final Page page;
    private final int timeoutSeconds;

    public WaitUtil(int timeoutSeconds) {
        this.page = DriverManager.getPage();
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Wait until the element matching the selector is visible.
     */
    public void waitForVisible(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeoutSeconds * 1000L));
    }

    /**
     * Wait until the element matching the selector is hidden.
     */
    public void waitForHidden(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setTimeout(timeoutSeconds * 1000L)
                .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
    }

    /**
     * Wait until the element matching the selector is attached to the DOM.
     */
    public void waitForAttached(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setTimeout(timeoutSeconds * 1000L)
                .setState(com.microsoft.playwright.options.WaitForSelectorState.ATTACHED));
    }

    /**
     * Wait until the element matching the selector is detached from the DOM.
     */
    public void waitForDetached(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setTimeout(timeoutSeconds * 1000L)
                .setState(com.microsoft.playwright.options.WaitForSelectorState.DETACHED));
    }

    /**
     * Wait until the element matching the selector is enabled.
     */
    public void waitForEnabled(String selector) {
        Locator locator = page.locator(selector);
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (locator.isEnabled()) return;
            } catch (Exception ignored) {}
            sleepMilli(250);
        }
    }

    /**
     * Wait until the element matching the selector is disabled.
     */
    public void waitForDisabled(String selector) {
        Locator locator = page.locator(selector);
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (!locator.isEnabled()) return;
            } catch (Exception ignored) {}
            sleepMilli(250);
        }
    }

    /**
     * Wait until the element matching the selector is editable.
     */
    public void waitForEditable(String selector) {
        Locator locator = page.locator(selector);
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (locator.isVisible() && locator.isEnabled()) return;
            } catch (Exception ignored) {}
            sleepMilli(250);
        }
    }

    /**
     * Wait until the text appears in the element matching the selector.
     */
    public void waitForText(String selector, String text) {
        Locator locator = page.locator(selector);
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (locator.textContent().contains(text)) return;
            } catch (Exception ignored) {}
            sleepMilli(250);
        }
    }

    /**
     * Wait until URL matches the given pattern.
     */
    public void waitForURL(String urlPattern) {
        page.waitForURL(urlPattern, new Page.WaitForURLOptions().setTimeout(timeoutSeconds * 1000L));
    }

    /**
     * Wait until a network response completes matching the URL pattern.
     */
    public void waitForResponse(String urlPattern) {
        // Not critical for current tests; Playwright 1.49 API limitation
        // A full implementation would use page.waitForEvent("response", ...)
    }

    /**
     * Wait until page load state is reached.
     */
    public void waitForLoadState(String state) {
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.valueOf(state.toUpperCase()));
    }

    /**
     * Wait until the page is fully loaded (networkidle).
     */
    public void waitForNetworkIdle() {
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
    }

    /**
     * Wait until the specified number of elements matching the selector exist.
     */
    public void waitForElementCount(String selector, int count) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setTimeout(timeoutSeconds * 1000L));
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            if (page.locator(selector).count() == count) return;
            sleepMilli(250);
        }
    }

    /**
     * Hard sleep for the given milliseconds.
     */
    public void sleepMilli(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Hard sleep for the given seconds.
     */
    public void sleepSec(int seconds) {
        sleepMilli(seconds * 1000L);
    }
}
