package base;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.TestInfo;
import utils.Navigation;


public class BaseTest {
    private Playwright playwright;
    private Browser browser;
    protected Page page;
    BrowserContext context;
    protected Navigation navigation;

    @BeforeEach
    public void setup() {

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(150));
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = browser.newPage();
        navigation = new Navigation(page);
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) throws IOException {
        String uniqueName = testInfo.getDisplayName() + "_" + System.currentTimeMillis();
        String fullPath = "screenshots/" + uniqueName + ".png";

        Files.createDirectories(Paths.get("screenshots"));
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Files.write(Paths.get(fullPath),screenshot);

        Path tracePath = Paths.get("target/trace.zip");
        context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));

        Allure.addAttachment(uniqueName,"image/png", Files.newInputStream(Paths.get(fullPath)),"png");
        browser.close();
        playwright.close();
    }
}
