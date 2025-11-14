package base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.Options;
import io.qameta.allure.Allure;
import model.User;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
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
    protected User globalUser = new User("admin","admin","user" ,true,"adminuser","!Adminuser123","0001");
    protected RegisterPage registerPage;
    protected LoginPage loginPage;
    protected EmployeeListPage employeeListPage;

    // URL
    protected static String baseUrl;

    // Endpoints
    protected static String employeeEndpoint;
    protected static String usersEndpoint;
    protected static String projectsEndpoint;

    // Admin
    protected static String adminUser;
    protected static String password;

    // Browser - Playwright
    protected static String browserType;
    protected static Boolean headless;
    protected static int slowMo;
    protected static double timeout;

    @BeforeAll
    public static void importConfig() {
        Properties properties = new Properties();

        try (InputStream config = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(config);

            // Access properties

            // URL
            baseUrl = properties.getProperty("baseUrl");

            // Endpoints
            employeeEndpoint = "api/v2/pim/employees";
            usersEndpoint = "api/v2/admin/users";
            projectsEndpoint = "api/v2/time/projects";

            // Admin
            adminUser = properties.getProperty("username");
            password = properties.getProperty("password");

            // Browser - Playwright
            browserType = properties.getProperty("browser");
            headless = Boolean.valueOf(properties.getProperty("headless"));
            slowMo = Integer.valueOf(properties.getProperty("slowMo"));
            timeout = Double.valueOf(properties.getProperty("timeout"));

        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setup() {
        playwright = Playwright.create();

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless).setTimeout(timeout).setSlowMo(slowMo);

        switch (browserType){
            case "chromium":
                browser = playwright.chromium().launch(options);
                break;
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }

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
