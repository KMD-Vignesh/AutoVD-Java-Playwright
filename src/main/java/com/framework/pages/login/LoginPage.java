package com.framework.pages.login;

import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;
import com.framework.utils.ConfigLoader;
import com.microsoft.playwright.Locator;

public class LoginPage extends BasePage {
    private final String usernameInput = "[data-test='username']";
    private final String passwordInput = "[data-test='password']";
    private final String loginButton = "[data-test='login-button']";
    private final String errorMessage = "[data-test='error']";
    private final String inventoryItem = ".inventory_item";

    public void navigate() {
        navigate(ConfigLoader.get("base.url"));
    }

    public String getCurrentUrl() {
        return getUrl();
    }

    public String getPageTitle() {
        return getTitle();
    }

    public void enterUsername(String username) {
        fill(usernameInput, username);
    }

    public void enterPassword(String password) {
        fill(passwordInput, password);
    }

    public void clickLogin() {
        click(loginButton);
        waitFor(500);
    }

    public void clickLoginAndWaitForInventory() {
        click(loginButton);
        waitFor(2000);
    }

    public boolean isOnInventoryPage() {
        return locator(inventoryItem).first().isVisible();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isVisible(errorMessage);
    }

    @Override
    public boolean isLoaded() {
        return isVisible(usernameInput);
    }
}