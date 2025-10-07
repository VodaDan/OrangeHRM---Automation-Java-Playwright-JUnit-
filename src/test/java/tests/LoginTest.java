package tests;

import base.BaseTest;
import com.microsoft.playwright.assertions.LocatorAssertions;
import jdk.jfr.Description;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {

    private String loginUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    @Test
    @Description("Login_TC01 - Verify that a user can log in with valid credentials")
    public void validLoginTest () {
        page.navigate(loginUrl);

        loginPage.fillUsername(globalUser.getUsername());
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();
        assertThat(page.locator("h6[data-v-7b563373]")).hasText("Dashboard");
    }

    @Test
    @Description("Login_TC02 - Verify that login fails with invalid credentials")
    public void invalidCredentialsLoginTest() {
        page.navigate(loginUrl);
        loginPage.fillUsername("WrongUsername");
        loginPage.fillPassword("WrongPassword");
        loginPage.submitLogin();

        assertThat(page).hasURL(loginUrl);
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Description("Login_TC03 - Verify that login fails when the username is left blank")
    public void blankUsernameLoginTest() {
        page.navigate(loginUrl);
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();

        assertThat(page).hasURL(loginUrl);
        assertThat(page.locator("div:has(input[placeholder='Username']) + span")).hasText("Required");
    }

    @Test
    @Description("Login_TC04 - Verify that login fails when the password is left blank")
    public void blankPasswordLoginTest() {
        navigation.navigateLogin();
        loginPage.fillUsername(globalUser.getUsername());
        loginPage.submitLogin();

        assertThat(page).hasURL(loginUrl);
        assertThat(page.locator("div:has(input[placeholder='Password']) + span")).hasText("Required");
    }

    @Test
    @Description("Login_TC05 - Verify that login fail when username is correct but password is invalid")
    public void validUsernameInvalidPasswordLoginTest() {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername());
        loginPage.fillPassword("Invalid Password");
        loginPage.submitLogin();

        assertThat(page).hasURL(loginUrl);
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Description("Login_TC09 - Verify that login fail when username is correct and password contains aditional characters")
    public void validUsernameValidPasswordWithAdditionalCharactersLoginTest() {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername());
        loginPage.fillPassword(globalUser.getPassword() + "additional1234");
        loginPage.submitLogin();

        assertThat(page).hasURL(loginUrl);
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Description("Login_TC06 - Verify that login fail when username is correct and password contains aditional characters")
    public void invalidUsernameValidPassowrdLoginTest() {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername()+"additional1234");
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();

        assertThat(page).hasURL(loginUrl);
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Description("Login_TC08 - Verify 'Forgot My Password' button redirects to password reset link.")
    public void forgotMyPassowrdRedirectLoginTest() {
        navigation.navigateLogin();
        loginPage.clickForgotMyPassword();
        assertThat(page).hasURL("https://opensource-demo.orangehrmlive.com/web/index.php/auth/requestPasswordResetCode");
    }


}
