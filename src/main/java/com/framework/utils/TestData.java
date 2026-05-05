package com.framework.utils;

public final class TestData {
    private TestData() {}

    public static final class Users {
        public static final String STANDARD_USER = "standard_user";
        public static final String LOCKED_OUT_USER = "locked_out_user";
        public static final String PROBLEM_USER = "problem_user";
        public static final String PERFORMANCE_USER = "performance_glitch_user";
        public static final String ERROR_USER = "error_user";
        public static final String VISUAL_USER = "visual_user";
        public static final String INVALID_USER = "invalid_user";
        public static final String VALID_PASSWORD = "secret_sauce";
    }

    public static final class Products {
        public static final String SAUCE_LABS_FLEECE_JACKET = "Sauce Labs Fleece Jacket";
        public static final String SAUCE_LABS_BACKPACK = "Sauce Labs Backpack";
        public static final String SAUCE_LABS_BIKE_LIGHT = "Sauce Labs Bike Light";
        public static final String SAUCE_LABS_BOLT_TSHIRT = "Sauce Labs Bolt T-Shirt";
        public static final String SAUCE_LABS_ONESIE = "Sauce Labs Onesie";
        public static final String TESTALLTHETHINGS_TSHIRT = "Test.allTheThings() T-Shirt";
    }

    public static final class Checkout {
        public static final String FIRST_NAME = "John";
        public static final String LAST_NAME = "Doe";
        public static final String POSTAL_CODE = "12345";
    }

    public static final class SortOptions {
        public static final String AZ = "az";
        public static final String ZA = "za";
        public static final String LOW_TO_HIGH = "lohi";
        public static final String HIGH_TO_LOW = "hilo";
    }

    public static final class ErrorMessages {
        public static final String USERNAME_REQUIRED = "Username is required";
        public static final String PASSWORD_REQUIRED = "Password is required";
        public static final String INVALID_CREDENTIALS = "Username and password do not match any user in this service";
        public static final String LOCKED_OUT_USER = "Sorry, this user has been locked out.";
        public static final String FIRST_NAME_REQUIRED = "Error: First Name is required";
        public static final String LAST_NAME_REQUIRED = "Error: Last Name is required";
        public static final String POSTAL_CODE_REQUIRED = "Error: Postal Code is required";
    }
}