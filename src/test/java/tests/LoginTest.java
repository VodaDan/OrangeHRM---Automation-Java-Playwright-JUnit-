package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {

    LoginPage loginPage;

    @Override
    @BeforeEach
    public void setup () {
        super.setup();
        loginPage = new LoginPage(page);
    }

    @Test
    public void validLoginTest () {
        page.navigate("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        loginPage.fillUsername("Admin");
        loginPage.fillPassword("admin123");
        loginPage.submitLogin();

        assertThat(page.locator("h6[data-v-7b563373]")).hasText("Dashboard");
    }
}
