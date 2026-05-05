package com.framework.pages;

import com.framework.driver.DriverManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.util.List;

public abstract class BasePage {
    protected Page getPage() {
        return DriverManager.getPage();
    }

    protected String getUrl() {
        return getPage().url();
    }

    protected String getTitle() {
        return getPage().title();
    }

    protected void navigate(String url) {
        getPage().navigate(url);
        log("Navigated to: " + url);
    }

    protected Locator locator(String selector) {
        return getPage().locator(selector);
    }

    protected void click(String selector) {
        waitForSelector(selector);
        getPage().click(selector);
        log("Clicked: " + selector);
    }

    protected void click(String selector, int timeout) {
        getPage().click(selector, new Page.ClickOptions().setTimeout(timeout));
    }

    protected void click(String selector, int index, int timeout) {
        getPage().locator(selector).nth(index).click(new Locator.ClickOptions().setTimeout(timeout));
    }

    protected void fill(String selector, String value) {
        waitForSelector(selector);
        getPage().fill(selector, value);
        log("Filled [" + selector + "] with: " + value);
    }

    protected void clearAndFill(String selector, String value) {
        waitForSelector(selector);
        getPage().locator(selector).clear();
        getPage().fill(selector, value);
    }

    protected void type(String selector, String value, int delay) {
        waitForSelector(selector);
        getPage().type(selector, value, new Page.TypeOptions().setDelay(delay));
    }

    protected String getText(String selector) {
        waitForSelector(selector);
        return getPage().locator(selector).textContent();
    }

    protected String getAttribute(String selector, String attribute) {
        waitForSelector(selector);
        return getPage().locator(selector).getAttribute(attribute);
    }

    protected boolean isVisible(String selector) {
        try {
            return getPage().locator(selector).isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isEnabled(String selector) {
        return getPage().locator(selector).isEnabled();
    }

    protected boolean isChecked(String selector) {
        return getPage().locator(selector).isChecked();
    }

    protected void selectOption(String selector, String value) {
        waitForSelector(selector);
        getPage().selectOption(selector, value);
    }

    protected List<String> getAllTextContents(String selector) {
        return getPage().locator(selector).allTextContents();
    }

    protected void waitForSelector(String selector, int timeout) {
        getPage().waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout));
    }

    protected void waitForSelector(String selector) {
        getPage().waitForSelector(selector);
    }

    protected void waitForLoadState(String state) {
        getPage().waitForLoadState(LoadState.valueOf(state.toUpperCase()));
    }

    protected void waitForLoad() {
        getPage().waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    protected void waitForURL(String urlPattern) {
        getPage().waitForURL(urlPattern);
    }

    protected void hover(String selector) {
        getPage().hover(selector);
    }

    protected void press(String selector, String key) {
        getPage().press(selector, key);
    }

    protected void pressKey(String key) {
        getPage().keyboard().press(key);
    }

    protected void scrollIntoViewIfNeeded(String selector) {
        getPage().locator(selector).scrollIntoViewIfNeeded();
    }

    protected byte[] screenshot() {
        return getPage().screenshot();
    }

    public abstract boolean isLoaded();

    protected void log(String message) {
        String prefix = "[PAGE]";
        System.out.println(prefix + " " + message);
        try {
            com.framework.utils.ExtentReportManager.info(message);
        } catch (Exception e) {}
    }

    protected void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}