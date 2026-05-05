package com.framework.tests;

import com.framework.base.BaseTest;
import com.framework.dataprovider.TestDataProvider;
import com.framework.pages.inventory.InventoryPage;
import com.framework.pages.login.LoginPage;
import com.framework.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginBrowserTest extends BaseTest {

    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Verify successful login with standard user", groups = {"Login", "smoke", "Vignesh"})
    public void testSuccessfulLogin_AsStandardUser(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();
        
        loginPage.enterUsername(TestData.Users.STANDARD_USER);
        loginPage.enterPassword(TestData.Users.VALID_PASSWORD);
        loginPage.clickLoginAndWaitForInventory();
        
        boolean onInventory = loginPage.isOnInventoryPage();
        Assert.assertTrue(onInventory, "Should be on inventory page after login. URL: " + loginPage.getCurrentUrl());
        
        InventoryPage inventoryPage = getPageObject(InventoryPage.class);
        int itemCount = inventoryPage.getItemCount();
        Assert.assertTrue(itemCount > 0, "Should display inventory items");
    }

    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Verify login fails with locked out user", groups = {"Login", "negative", "Vicky"})
    public void testFailedLogin_AsLockedOutUser(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();
        
        loginPage.enterUsername(TestData.Users.LOCKED_OUT_USER);
        loginPage.enterPassword(TestData.Users.VALID_PASSWORD);
        loginPage.clickLogin();
        
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Epic sadface"), "Error should contain expected text. Got: " + errorMessage);
    }

    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Verify login fails with invalid credentials", groups = {"Login", "negative", "KMDV"})
    public void testFailedLogin_AsInvalidUser(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();
        
        loginPage.enterUsername(TestData.Users.INVALID_USER);
        loginPage.enterPassword("wrong_password");
        loginPage.clickLogin();
        
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
    }

    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Verify login fails with empty credentials", groups = {"Login", "negative", "Vignesh"})
    public void testFailedLogin_WithEmptyCredentials(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();
        
        loginPage.clickLogin();
        
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error message should be displayed");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Username is required"), "Error should indicate username is required");
    }

    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Verify login page loads with correct title", groups = {"Login", "smoke", "Vicky"})
    public void testLoginPageLoads(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();
        
        Assert.assertTrue(loginPage.isLoaded(), "Login page should be loaded");
        Assert.assertTrue(loginPage.getPageTitle().contains("Swag Labs"), "Title should contain Swag Labs");
    }

    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Intentional failure test to verify screenshot capture", groups = {"Login", "failure", "KMDV"})
    public void testIntentionallyFail(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();
        
        loginPage.enterUsername(TestData.Users.STANDARD_USER);
        loginPage.enterPassword(TestData.Users.VALID_PASSWORD);
        loginPage.clickLoginAndWaitForInventory();
        
        Assert.fail("This is an intentional failure to test screenshot capture on failure");
    }
}