package base;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import model.User;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import pages.EmployeeListPage;
import pages.LoginPage;
import pages.RegisterPage;
import utils.Navigation;


public class BaseTest {
    protected static Playwright playwright;
    protected Browser browser;
    protected Page page;
    protected BrowserContext context;
    protected Navigation navigation;
    protected User globalUser = new User("admin","admin","user" ,"enabled","adminuser","!Adminuser123","0001");
    protected RegisterPage registerPage;
    protected LoginPage loginPage;
    protected EmployeeListPage employeeListPage;


    @BeforeEach
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true).setTimeout(0));
        context = browser.newContext();
        String traceFile = "target/traces/" + "_" + UUID.randomUUID() + ".zip";
        try {
            Files.createDirectories(Paths.get(traceFile).getParent());
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.page = context.newPage();
        // Page Models
        registerPage = new RegisterPage(page);
        loginPage = new LoginPage(page);
        employeeListPage = new EmployeeListPage(page);
        // Navigation
        navigation = new Navigation(page);


    }

    public Page getPage() {
        return page;
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) throws IOException {
        // Screenshot
        if (page != null) {
            Path screenshotDir = Paths.get("screenshots");
            Files.createDirectories(screenshotDir);
            String screenshotFile = screenshotDir.resolve(testInfo.getDisplayName() + "_" + UUID.randomUUID() + ".png").toString();
            Files.write(Paths.get(screenshotFile), page.screenshot(new Page.ScreenshotOptions().setFullPage(true)));
            Allure.addAttachment(testInfo.getDisplayName(), "image/png", Files.newInputStream(Paths.get(screenshotFile)), "png");
        }

        // Stop tracing
        if (context != null) {
            String traceFile = "target/traces/" + testInfo.getDisplayName() + "_" + UUID.randomUUID() + ".zip";
            Files.createDirectories(Paths.get(traceFile).getParent());
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(traceFile)));
            context.close();
            playwright.close();
        }


    }

}
