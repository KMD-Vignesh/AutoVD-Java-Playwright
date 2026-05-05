package com.framework.dataprovider;

import org.testng.annotations.DataProvider;

public class TestDataProvider {

    @DataProvider(name = "browsers", parallel = false)
    public static Object[][] browsers() {
        return new Object[][]{
            {"Chromium"},
            {"Firefox"},
            {"WebKit"}
        };
    }

    @DataProvider(name = "validUsers")
    public static Object[][] validUsers() {
        return new Object[][]{
            {"standard_user"},
            {"problem_user"},
            {"performance_glitch_user"},
            {"error_user"},
            {"visual_user"}
        };
    }

    @DataProvider(name = "invalidCredentials")
    public static Object[][] invalidCredentials() {
        return new Object[][]{
            {"", "", "Username is required"},
            {"", "secret_sauce", "Username is required"},
            {"standard_user", "", "Password is required"},
            {"invalid_user", "secret_sauce", "Username and password do not match any user in this service"},
            {"locked_out_user", "secret_sauce", "Sorry, this user has been locked out."}
        };
    }

    @DataProvider(name = "checkoutValidation")
    public static Object[][] checkoutValidation() {
        return new Object[][]{
            {"", "Doe", "12345", "Error: First Name is required"},
            {"John", "", "12345", "Error: Last Name is required"},
            {"John", "Doe", "", "Error: Postal Code is required"}
        };
    }

    @DataProvider(name = "users")
    public static Object[][] users() {
        return new Object[][]{
            {"standard_user", "secret_sauce", "Standard User", true},
            {"locked_out_user", "secret_sauce", "Locked Out User", false},
            {"problem_user", "secret_sauce", "Problem User", true},
            {"invalid_user", "wrong_password", "Invalid User", false}
        };
    }

    @DataProvider(name = "products")
    public static Object[][] products() {
        return new Object[][]{
            {"Sauce Labs Backpack", "$29.99"},
            {"Sauce Labs Bike Light", "$9.99"},
            {"Sauce Labs Bolt T-Shirt", "$15.99"}
        };
    }
}