# Playwright SauceDemo Test Framework

Production-grade Java test automation framework using **Playwright**, **TestNG**, and **Extent Reports** for [SauceDemo](https://www.saucedemo.com).

---

## Project Structure

```
AutoVD-Java-Playwright/
├── .gitignore                     # Git ignore rules
├── pom.xml                        # Maven configuration
├── README.md                     # This file
│
├── src/
│   └── main/
│       ├── java/com/framework/
│       │   ├── driver/
│       │   │   └── DriverManager.java      # Browser lifecycle (ThreadLocal)
│       │   ├── pages/
│       │   │   ├── BasePage.java        # Base POM methods
│       │   │   ├── login/LoginPage.java
│       │   │   ├── inventory/InventoryPage.java
│       │   │   ├── cart/CartPage.java
│       │   │   └── checkout/
│       │   │       ├── CheckoutInfoPage.java
│       │   │       ├── CheckoutOverviewPage.java
│       │   │       └── CheckoutCompletePage.java
│       │   └── utils/
│       │       ├── ConfigLoader.java       # Config loader
│       │       ├── TestData.java         # Test data constants
│       │       ├── ExtentReportManager.java # Reports
│       │       └── TestListener.java    # Screenshot on failure
│       └── resources/
│           └── config.properties     # App configuration
│
├── src/test/
│   ├── java/com/framework/
│   │   ├── base/
│   │   │   └── BaseTest.java      # Base test class
│   │   ├── dataprovider/
│   │   │   └── TestDataProvider.java
│   │   └── tests/
│   │       ├── LoginBrowserTest.java  # Login tests
│   │       └── E2EBrowserTest.java    # E2E flow tests
│   └── resources/
│       ├── testng-chromium.xml    # Chromium suite
│       ├── testng-firefox.xml   # Firefox suite
│       ├── testng-webkit.xml  # WebKit suite
│       └── testng-parallel.xml # Parallel config
```

---

## Configuration

### `src/main/resources/config.properties`

| Key | Default | Description |
| --- | --- | --- |
| `base.url` | https://www.saucedemo.com | Target app URL |
| `browser` | chromium | Browser (chromium,firefox,webkit) |
| `headless` | true | Run browser headless |
| `slowmo` | 0 | Slow motion delay |
| `viewport.width` | 1920 | Viewport width |
| `viewport.height` | 1080 | Viewport height |
| `screenshot.on.failure` | true | Capture failure screenshot |
| `report.title` | SauceDemo Test Report | Report title |

---

## Execution Commands

### Prerequisites

```bash
# Install Playwright browsers
mvn playwright:install
```

### Single Browser

```bash
# Chromium
mvn test -Dbrowser=chromium

# Firefox
mvn test -Dbrowser=firefox

# WebKit
mvn test -Dbrowser=webkit
```

### All Browsers (Sequential)

```bash
mvn test -Dbrowser=chromium -Dsurefire.suiteXmlFiles=src/test/resources/testng-chromium.xml
mvn test -Dbrowser=firefox -Dsurefire.suiteXmlFiles=src/test/resources/testng-firefox.xml
mvn test -Dbrowser=webkit -Dsurefire.suiteXmlFiles=src/test/resources/testng-webkit.xml
```

### Parallel Methods

```bash
mvn test
```

### Run Specific Test

```bash
mvn test -Dtest=LoginBrowserTest#testSuccessfulLogin_AsStandardUser
mvn test -Dtest=E2EBrowserTest#testCompletePurchaseFlow
```

### Headed Mode (Browser Visible)

```bash
mvn test -Dheadless=false
```

---

## Test Cases

### LoginBrowserTest (5 tests x 3 browsers = 15 runs)

| Test | Description | Category |
| --- | --- | --- |
| testSuccessfulLogin_AsStandardUser | Valid login, verify inventory loads | Login, Smoke |
| testFailedLogin_AsLockedOutUser | Locked user, verify error message | Login, Negative |
| testFailedLogin_AsInvalidUser | Wrong credentials, verify error | Login, Negative |
| testFailedLogin_WithEmptyCredentials | Empty input, verify validation | Login, Negative |
| testLoginPageLoads | Verify login page title | Login, Smoke |

### E2EBrowserTest (3 tests x 3 browsers = 9 runs)

| Test | Description | Category |
| --- | --- | --- |
| testCompletePurchaseFlow | Login → Add items → Cart → Checkout → Complete → Back Home | E2E, Smoke |
| testLogoutFlow | Login → Menu → Logout → Verify redirect | E2E |
| testVerifyInventoryItems | Login → Verify 6 items → Check name & price | E2E |

### Total: 8 tests across 3 browsers = 24 test runs

---

## Reports

Reports generated at: `target/extentreport/report.html`

| Feature | Value |
| --- | --- |
| Browser Tags | Chromium, Firefox, WebKit |
| Categories | Login, E2E, Smoke, Negative |
| Author | Automation Team |
| Failure Screenshots | Auto-attached on failure |

---

## Dependencies

| Package | Version | Purpose |
| --- | --- | --- |
| Microsoft.Playwright | 1.49.0 | Browser automation |
| TestNG | 7.10.2 | Test framework |
| ExtentReports | 5.0.9 | HTML reporting |

---

## Quick Reference

| Task | Command |
| --- | --- |
| Build | `mvn compile` |
| All tests (parallel) | `mvn test` |
| Chromium only | `mvn test -Dbrowser=chromium` |
| Firefox only | `mvn test -Dbrowser=firefox` |
| WebKit only | `mvn test -Dbrowser=webkit` |
| Single test | `mvn test -Dtest=LoginBrowserTest#testLoginPageLoads` |
| Headed mode | `mvn test -Dheadless=false` |
| View report | `open target/extentreport/report.html` |