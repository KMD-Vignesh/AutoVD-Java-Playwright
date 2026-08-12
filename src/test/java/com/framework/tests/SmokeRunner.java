package com.framework.config;

import org.testng.annotations.Test;

import com.framework.tests.LoginBrowserTest;

/**
 * Smoke-test entry point that runs a single test outside the TestNG suite.
 * Useful for quick verification before running the full suite.
 */
public class SmokeRunner {

    @Test
    public void quickLoginSmoke() {
        LoginBrowserTest test = new LoginBrowserTest();
        // Run just one browser for a quick sanity check
        test.testSuccessfulLogin_AsStandardUser("Chromium");
    }
}
