package com.framework.pages.checkout;

import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;

public class CheckoutOverviewPage extends BasePage {
    private final String totalLabel = ".summary_total_label";
    private final String finishBtn = "button:has-text('Finish')";
    private final String cancelLink = ".cart_cancel_link";

    public void finish() {
        click(finishBtn);
        waitForLoadState("domcontentloaded");
    }

    public void cancel() {
        click(cancelLink);
        waitForLoadState("domcontentloaded");
    }

    public String getSubtotal() {
        return getText(totalLabel);
    }
    
    @Override
    public boolean isLoaded() {
        return isVisible(finishBtn);
    }
}