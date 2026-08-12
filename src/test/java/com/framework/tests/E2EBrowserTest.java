package com.framework.tests;

import com.framework.config.Author;
import com.framework.base.BaseTest;
import com.framework.dataprovider.TestDataProvider;
import com.framework.pages.cart.CartPage;
import com.framework.pages.checkout.CheckoutCompletePage;
import com.framework.pages.checkout.CheckoutInfoPage;
import com.framework.pages.checkout.CheckoutOverviewPage;
import com.framework.pages.inventory.InventoryPage;
import com.framework.pages.login.LoginPage;
import com.framework.utils.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class E2EBrowserTest extends BaseTest {

    @Author(Name = "Vignesh")
    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Complete purchase flow: login, add items, checkout, complete order", groups = {"E2E", "smoke"})
    public void testCompletePurchaseFlow(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();

        loginPage.enterUsername(TestData.Users.STANDARD_USER);
        loginPage.enterPassword(TestData.Users.VALID_PASSWORD);
        loginPage.clickLoginAndWaitForInventory();

        Assert.assertTrue(loginPage.isOnInventoryPage(), "Should navigate to inventory after login");

        InventoryPage inventoryPage = getPageObject(InventoryPage.class);

        inventoryPage.addItemToCartByName("Sauce Labs Backpack");
        inventoryPage.addItemToCartByName("Sauce Labs Bike Light");

        int cartCount = inventoryPage.getCartBadgeCount();
        Assert.assertEquals(cartCount, 2, "Cart should have 2 items");

        inventoryPage.openCart();

        CartPage cartPage = getPageObject(CartPage.class);
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should be visible");

        int cartItemCount = cartPage.getCartItemCount();
        Assert.assertEquals(cartItemCount, 2, "Cart should have 2 items");

        cartPage.clickCheckout();

        CheckoutInfoPage checkoutInfo = getPageObject(CheckoutInfoPage.class);
        checkoutInfo.fillInfo("John", "Doe", "12345");
        checkoutInfo.continueNext();

        CheckoutOverviewPage overviewPage = getPageObject(CheckoutOverviewPage.class);

        overviewPage.finish();

        CheckoutCompletePage completePage = getPageObject(CheckoutCompletePage.class);
        Assert.assertTrue(completePage.isComplete(), "Should show completion message");
    }

    @Author(Name = "Vicky")
    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Verify user can logout successfully", groups = {"E2E"})
    public void testLogoutFlow(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();

        loginPage.enterUsername(TestData.Users.STANDARD_USER);
        loginPage.enterPassword(TestData.Users.VALID_PASSWORD);
        loginPage.clickLoginAndWaitForInventory();

        Assert.assertTrue(loginPage.isOnInventoryPage(), "Should be on inventory after login");

        InventoryPage inventoryPage = getPageObject(InventoryPage.class);
        inventoryPage.logout();

        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("saucedemo.com"), "Should redirect after logout");
    }

    @Author(Name = "Vignesh")
    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class, description = "Verify all inventory items are displayed", groups = {"E2E", "smoke"})
    public void testVerifyInventoryItems(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();

        loginPage.enterUsername(TestData.Users.STANDARD_USER);
        loginPage.enterPassword(TestData.Users.VALID_PASSWORD);
        loginPage.clickLoginAndWaitForInventory();

        Assert.assertTrue(loginPage.isOnInventoryPage(), "Should be on inventory after login");

        InventoryPage inventoryPage = getPageObject(InventoryPage.class);

        int itemCount = inventoryPage.getItemCount();
        Assert.assertEquals(itemCount, 6, "Should display 6 inventory items");

        String itemName = inventoryPage.getItemName(0);
        String itemPrice = inventoryPage.getItemPrice(0);

        Assert.assertNotNull(itemName, "Item name should not be null");
        Assert.assertNotNull(itemPrice, "Item price should not be null");
        Assert.assertTrue(itemPrice.startsWith("$"), "Price should start with $");
    }
}
