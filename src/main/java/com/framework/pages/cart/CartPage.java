package com.framework.pages.cart;

import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;

import java.util.List;

public class CartPage extends BasePage {
    private final String cartItem = ".cart_item";
    private final String itemName = ".inventory_item_name";
    private final String checkoutBtn = "button:has-text('Checkout')";

    public void clickCheckout() {
        click(checkoutBtn);
        waitForLoadState("domcontentloaded");
    }

    public List<String> getItemNames() {
        return getAllTextContents(itemName);
    }

    public int getCartItemCount() {
        waitFor(1500);
        try {
            int count = locator(cartItem).count();
            if (count > 0) {
                return count;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean isLoaded() {
        return isVisible(checkoutBtn);
    }
}