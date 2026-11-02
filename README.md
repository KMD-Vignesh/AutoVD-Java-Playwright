<div align="center">

<h1>AutoVD-Java-Playwright</h1>

<p><strong>Playwright UI test automation framework for Java</strong><br>
Playwright · TestNG · Page Objects · ExtentReports · Multi-browser</p>

<p>
  <a href="https://github.com/KMD-Vignesh/AutoVD-Java-Playwright"><img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"></a>
  <a href="https://github.com/KMD-Vignesh/AutoVD-Java-Playwright"><img src="https://img.shields.io/badge/Playwright-1.49.0-2EAD33?style=for-the-badge&logo=playwright&logoColor=white" alt="Playwright 1.49.0"></a>
  <a href="https://github.com/KMD-Vignesh/AutoVD-Java-Playwright"><img src="https://img.shields.io/badge/TestNG-7.10.2-FF6F00?style=for-the-badge" alt="TestNG 7.10.2"></a>
  <a href="https://github.com/KMD-Vignesh/AutoVD-Java-Playwright/blob/main/LICENSE"><img src="https://img.shields.io/badge/License-MIT-2EA44F?style=for-the-badge" alt="MIT License"></a>
</p>

</div>

## Intro

<div>

<img src="https://img.shields.io/badge/Vignesh&nbsp;D,&nbsp;QA&nbsp;Lead/Architect,&nbsp;Chennai,India-B2C6DB?style=for-the-badge" alt="Vignesh">

<p>
    <a href="https://www.linkedin.com/in/kmd-vignesh/"><img src="https://img.shields.io/badge/LinkedIn-0A75AD?style=for-the-badge" alt="LinkedIn"></a>
    <a href="https://github.com/KMD-Vignesh"><img src="https://img.shields.io/badge/GitHub-FF6A80?style=for-the-badge" alt="GitHub"></a>
    <a href="mailto:KMD.Vignesh@outlook.com"><img src="https://img.shields.io/badge/KMD.Vignesh@Outlook-2a9d8f?style=for-the-badge" alt="Email"></a>

</p>

</div>

## What It Is

A Playwright + TestNG framework for browser tests. Extend `BaseTest`, ask for a page object with `getPageObject(...)`, and the framework creates a thread-local Playwright browser, starts an Extent test, and captures failure screenshots when the method ends.

- **Three browsers by default** — every test runs once on Chromium, Firefox, and WebKit through a TestNG data provider.
- **Page objects with a thin base** — `BasePage` wraps navigation, locators, waits, clicks, and text reads on the Playwright `Page`.
- **Parallel by design** — `Browser`, `BrowserContext`, `Page`, and config all live in `ThreadLocal`s.
- **Reports that name the browser** — ExtentReports entries carry the browser name, category, and author; failures attach a screenshot.

## Framework Family

Seven implementations of the same layered idea. Pick the stack your team already uses; the concepts and reports stay familiar.

| Framework                       | Stack                         | Covers    | Repository                                                                                  |
| :------------------------------ | :---------------------------- | :-------- | :------------------------------------------------------------------------------------------ |
| **Java + Selenium**             | Java 17 · Selenium 4 · TestNG | UI + REST | [AutoVD-Java-Selenium](https://github.com/KMD-Vignesh/AutoVD-Java-Selenium)                 |
| **Java + Playwright** — this repo | Java 17 · Playwright · TestNG | UI + REST | [AutoVD-Java-Playwright](https://github.com/KMD-Vignesh/AutoVD-Java-Playwright)             |
| **C# + Playwright**             | C# · Playwright · NUnit       | UI        | [AutoVD-CS-Playwright](https://github.com/KMD-Vignesh/AutoVD-CS-Playwright)                 |
| **Python + Selenium**           | Python · Selenium · pytest    | UI + REST | [AutoVD-Python-Selenium](https://github.com/KMD-Vignesh/AutoVD-Python-Selenium)             |
| **Python + Playwright**         | Python · Playwright · pytest  | UI        | [AutoVD-Python-Playwright](https://github.com/KMD-Vignesh/AutoVD-Python-Playwright)         |
| **TypeScript + Playwright**     | TypeScript · Playwright       | UI + REST | [AutoVD-Typescript-Playwright](https://github.com/KMD-Vignesh/AutoVD-Typescript-Playwright) |
| **TypeScript + Cypress**        | TypeScript · Cypress          | UI (E2E)  | [AutoVD-Typescript-Cypress](https://github.com/KMD-Vignesh/AutoVD-Typescript-Cypress)       |

## Quick Start

**Prerequisites:** JDK 17+, Maven 3.8+, and the Playwright browser binaries.

```bash
git clone https://github.com/KMD-Vignesh/AutoVD-Java-Playwright.git
cd AutoVD-Java-Playwright
mvn playwright:install
mvn test
```

The suite runs 8 test methods across 3 browsers: 24 runs in total (5 login scenarios and 3 end-to-end flows per browser). Headless is the default.

Open the reports:

```bash
open target/extentreport/report.html
open Report-ScreenShot/screenshots/failed/
```

On Linux use `xdg-open`; on Windows open the files from Explorer. Failure screenshots live alongside the report entries.

## Configuration

The runtime config is `src/main/resources/config.properties`.

```properties
base.url=https://www.saucedemo.com
browser=chromium,firefox,webkit
headless=true
timeout.default=30000
thread.count=4
```

| Key | Default | What it does |
| :--- | :--- | :--- |
| `base.url` | SauceDemo URL | Starting page for every test |
| `browser` | `chromium,firefox,webkit` | Fallback browser when a test does not supply one |
| `headless` | `true` | Set `false` to watch the run; overridable with `-Dheadless=false` |
| `slowmo` | `0` | Milliseconds between Playwright actions; overridable with `-Dslowmo=500` |
| `viewport.width` / `viewport.height` | `1920` / `1080` | Browser context size |
| `timeout.default` | `30000` | Default action timeout in milliseconds |
| `screenshot.on.failure` | `true` | Attach a screenshot when a test fails |
| `report.title` | SauceDemo report | Extent report title |
| `parallel.methods` / `thread.count` | `true` / `4` | TestNG parallelism defaults |

> The three browsers come from `TestDataProvider.browsers()`, not from `config.properties`. To run fewer browsers, edit that data provider or the TestNG suite XML.

## Writing Tests

Extend `BaseTest`, take a page object, and assert with TestNG:

```java
public class LoginBrowserTest extends BaseTest {

    @Author(Name = "Vignesh")
    @Test(dataProvider = "browsers", dataProviderClass = TestDataProvider.class,
          description = "Verify successful login with standard user",
          groups = {"Login", "smoke"})
    public void testSuccessfulLogin_AsStandardUser(String browser) {
        LoginPage loginPage = getPageObject(LoginPage.class);
        loginPage.navigate();
        loginPage.enterUsername(TestData.Users.STANDARD_USER);
        loginPage.enterPassword(TestData.Users.VALID_PASSWORD);
        loginPage.clickLoginAndWaitForInventory();

        Assert.assertTrue(loginPage.isOnInventoryPage(), "Should reach inventory");
    }
}
```

A page object extends `BasePage`; selectors stay in the page, not the test:

```java
public class LoginPage extends BasePage {

    private final String usernameInput = "[data-test='username']";
    private final String loginButton = "[data-test='login-button']";

    public void navigate() {
        navigate(ConfigLoader.get("base.url"));
    }

    public void enterUsername(String username) {
        fill(usernameInput, username);
    }

    public void clickLogin() {
        click(loginButton);
        waitFor(500);
    }

    public boolean isOnInventoryPage() {
        return locator(".inventory_item").first().isVisible();
    }
}
```

## Framework Map

Read these in order. Everything else supports them.

| Package | Key classes | What it does |
| :--- | :--- | :--- |
| `com.framework.base` | `BaseTest` | Browser setup, Extent test, screenshot on failure. **Start here.** |
| `com.framework.driver` | `DriverManager` | Thread-local Playwright, browser, context, and page |
| `com.framework.pages` | `BasePage`, `LoginPage`, `InventoryPage`, checkout pages | Page objects and the shared Playwright wrappers |
| `com.framework.utils` | `ConfigLoader`, `ExtentReportManager`, `PathConfig`, `WaitUtil`, `TestData` | Config, report, paths, waits, and test data |
| `com.framework.config` | `Author`, `TestID`, `RetryAnalyser`, `RetryTransform` | Test metadata and retry |
| `com.framework.dataprovider` | `TestDataProvider` | Browser matrix and data sets |
| `com.framework.tests` | `LoginBrowserTest`, `E2EBrowserTest` | Where the tests live |

A few things carry most of the work:

| Item | Use |
| :--- | :--- |
| `getPageObject(Class<T>)` | Create a page object already wired to the current page |
| `DriverManager.getPage()` | Raw Playwright `Page` when a wrapper is missing |
| `BasePage.locator(selector)` | Playwright `Locator` with framework logging |
| `BasePage.click/fill/getText` | Actions with waits and logs |
| `ExtentReportManager.info(...)` | Write a step to the report |
| `getExtentTest()` | The current thread's `ExtentTest` |

## How a Run Works

1. TestNG reads `testng-parallel.xml`; the `browsers` data provider supplies Chromium, Firefox, and WebKit.
2. `@BeforeMethod` creates a thread-local Playwright browser and page, then starts an Extent test.
3. The test uses page objects; every action is logged to the report.
4. `@AfterMethod` records pass/fail/skip, attaches a failure screenshot, and quits the browser.
5. The TestNG listener flushes the Extent report at the end of the suite.

## Project Layout

```text
AutoVD-Java-Playwright/
├── pom.xml                          # Java 17, Playwright, TestNG, Surefire
├── src/main/java/com/framework/     # framework: base, driver, pages, utils, config
├── src/main/resources/              # config.properties
├── src/test/java/com/framework/     # tests, data provider, listener
├── src/test/resources/              # testng-*.xml suites and test-data.json
├── target/extentreport/             # generated: Extent report
└── Report-ScreenShot/screenshots/   # generated: pass/fail/skip screenshots
```

`target/` and `Report-ScreenShot/` are generated output.

## Reports

| Output | Location |
| :--- | :--- |
| ExtentReports HTML | `target/extentreport/report.html` |
| Failure screenshots | `Report-ScreenShot/screenshots/failed/` |
| Passed screenshots | `Report-ScreenShot/screenshots/passed/` |
| Skipped screenshots | `Report-ScreenShot/screenshots/skipped/` |

Each entry includes the browser name, test category, and author, so one report covers all three browsers.

## Troubleshooting

| Symptom | Fix |
| :--- | :--- |
| `Executable doesn't exist` when a browser starts | Run `mvn playwright:install` once. |
| All three browsers run when you expected one | Browsers come from `TestDataProvider.browsers()`; edit it or the suite XML. |
| `config.properties not found in classpath` | Run through Maven so `src/main/resources` is on the classpath. |
| Browser opens during CI | Set `headless=true` or pass `-Dheadless=true`. |
| Report is empty | The JVM exited before the suite finished; let TestNG flush the report. |

## License

MIT. See [`LICENSE`](LICENSE).
