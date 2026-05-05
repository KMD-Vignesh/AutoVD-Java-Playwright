package com.framework.pages.checkout;

import com.framework.driver.DriverManager;
import com.framework.pages.BasePage;

public class CheckoutInfoPage extends BasePage {
    private final String firstNameField = "#first-name";
    private final String lastNameField = "#last-name";
    private final String postalCodeField = "#postal-code";
    private final String continueBtn = "input:has-text('Continue')";
    private final String cancelLink = ".cart_cancel_link";
    private final String errorMessage = ".error-message";

    public void fillInfo(String firstName, String lastName, String postalCode) {
        fill(firstNameField, firstName);
        fill(lastNameField, lastName);
        fill(postalCodeField, postalCode);
    }

    public void continueNext() {
        click(continueBtn);
        waitForLoadState("domcontentloaded");
    }

    public void cancel() {
        click(cancelLink);
        waitForLoadState("domcontentloaded");
    }

    public String getErrorMessage() {
        if (isVisible(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }

    public boolean isErrorDisplayed() {
        return isVisible(errorMessage);
    }
    
    @Override
    public boolean isLoaded() {
        return isVisible(continueBtn);
    }
}