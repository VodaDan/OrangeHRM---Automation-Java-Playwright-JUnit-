package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    private Playwright playwright;
    private Browser browser;
    protected Page page;

    @BeforeEach
    public void setup () {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(150));
        page = browser.newPage();
    }

    @AfterEach
    public void tearDown () {
        browser.close();
        playwright.close();
    }
}
