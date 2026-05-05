package com.framework.pages.inventory;

import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;

import java.util.List;

public class InventoryPage extends BasePage {
    private final String inventoryItem = ".inventory_item";
    private final String productName = ".inventory_item_name";
    private final String productPrice = ".inventory_item_price";
    private final String cartLink = "[data-test='shopping-cart-link']";
    private final String cartBadge = ".shopping_cart_badge";
    private final String menuButton = "#react-burger-menu-btn";
    private final String logoutLink = "[data-test='logout-sidebar-link']";
    private final String addToCartButton = "[data-test^='add-to-cart']";

    public void addItemToCartByName(String itemName) {
        List<String> names = getAllProductNames();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).contains(itemName)) {
                getPage().locator(".inventory_item").nth(i).getByText("Add to cart").click();
                waitFor(500);
                return;
            }
        }
    }

    public void sortBy(String sortOption) {
        String sortDropdown = "[data-test='product-sort-container']";
        selectOption(sortDropdown, sortOption);
    }

    public List<String> getAllProductNames() {
        return getAllTextContents(productName);
    }

    public int getItemCount() {
        return locator(inventoryItem).count();
    }

    public int getCartBadgeCount() {
        if (isVisible(cartBadge)) {
            String text = getText(cartBadge).trim();
            if (!text.isEmpty()) {
                return Integer.parseInt(text);
            }
        }
        return 0;
    }

    public String getItemName(int index) {
        return locator(productName).nth(index).textContent();
    }

    public String getItemPrice(int index) {
        return locator(productPrice).nth(index).textContent();
    }

    public void openCart() {
        click(cartLink);
    }

    public void logout() {
        click(menuButton);
        waitFor(300);
        click(logoutLink);
    }

    @Override
    public boolean isLoaded() {
        return isVisible(inventoryItem);
    }
}