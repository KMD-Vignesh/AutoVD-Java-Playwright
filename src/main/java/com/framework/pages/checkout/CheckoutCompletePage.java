package com.framework.pages.checkout;

import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;

public class CheckoutCompletePage extends BasePage {
    private final String completeHeader = ".complete-header";
    private final String completeText = ".complete-text";
    private final String backHomeBtn = "button:has-text('Back Home')";

    public void backHome() {
        click(backHomeBtn);
        waitForLoadState("domcontentloaded");
    }

    public String getCompleteHeaderText() {
        return getText(completeHeader);
    }

    public String getCompleteMessage() {
        return getText(completeText);
    }

    public boolean isComplete() {
        return isVisible(completeHeader);
    }
    
    @Override
    public boolean isLoaded() {
        return isVisible(completeHeader);
    }
}