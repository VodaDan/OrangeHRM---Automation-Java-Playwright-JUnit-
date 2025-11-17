package tests;

import base.BaseTest;
import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.LoadState;
import jdk.jfr.Description;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.LoginPage;
import utils.DataProvider;

import java.io.FileWriter;
import java.util.Collections;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LoginTest.class);

    @BeforeEach
    public void setup() {
        super.setup();
    }



    @Test
    @Tag("login")
    @Description("Login_TC01 - Verify that a user can log in with valid credentials")
    public void validLoginTest () {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername());
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();
        assertThat(page.locator("h6[data-v-7b563373]")).hasText("Dashboard");
    }

    @Test
    @Tag("login")
    @Description("Login_TC02 - Verify that login fails with invalid credentials")
    public void invalidCredentialsLoginTest() {
        navigation.navigateLogin();
        loginPage.fillUsername("WrongUsername");
        loginPage.fillPassword("WrongPassword");
        loginPage.submitLogin();

        assertThat(page).hasURL(navigation.getLoginUrl());
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Tag("login")
    @Description("Login_TC03 - Verify that login fails when the username is left blank")
    public void blankUsernameLoginTest() {
        navigation.navigateLogin();
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();

        assertThat(page).hasURL(navigation.getLoginUrl());
        assertThat(page.locator("div:has(input[placeholder='Username']) + span")).hasText("Required");
    }

    @Test
    @Tag("login")
    @Description("Login_TC04 - Verify that login fails when the password is left blank")
    public void blankPasswordLoginTest() {
        navigation.navigateLogin();
        loginPage.fillUsername(globalUser.getUsername());
        loginPage.submitLogin();

        assertThat(page).hasURL(navigation.getLoginUrl());
        assertThat(page.locator("div:has(input[placeholder='Password']) + span")).hasText("Required");
    }

    @Test
    @Tag("login")
    @Description("Login_TC05 - Verify that login fail when username is correct but password is invalid")
    public void validUsernameInvalidPasswordLoginTest() {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername());
        loginPage.fillPassword("Invalid Password");
        loginPage.submitLogin();

        assertThat(page).hasURL(navigation.getLoginUrl());
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Tag("login")
    @Description("Login_TC09 - Verify that login fail when username is correct and password contains additional characters")
    public void validUsernameValidPasswordWithAdditionalCharactersLoginTest() {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername());
        loginPage.fillPassword(globalUser.getPassword() + "additional1234");
        loginPage.submitLogin();

        assertThat(page).hasURL(navigation.getLoginUrl());
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Tag("login")
    @Description("Login_TC06 - Verify that login fail when username is correct and password contains aditional characters")
    public void invalidUsernameValidPassowrdLoginTest() {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername()+"additional1234");
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();

        assertThat(page).hasURL(navigation.getLoginUrl());
        assertThat(loginPage.getLoginValidationError()).hasText("Invalid credentials",new LocatorAssertions.HasTextOptions().setTimeout(10000));
    }

    @Test
    @Tag("login")
    @Description("Login_TC08 - Verify 'Forgot My Password' button redirects to password reset link")
    public void forgotMyPassowrdRedirectLoginTest() {
        navigation.navigateLogin();
        loginPage.clickForgotMyPassword();
        assertThat(page).hasURL(loginPage.getForgotPasswordURL());
    }


    @Test
    @Tag("login")
    @Description("Login_TC10 - Verify case sensitivity with valid login data")
    public void verifyLoginCaseSensitivityLoginTest() {
        navigation.navigateLogin();

        loginPage.fillUsername(globalUser.getUsername().toUpperCase());
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();


        assertThat(page.locator("h6[data-v-7b563373]")).hasText("Dashboard");
    }

    @Test
    @Tag("login")
    @Description("Login_TC11 - Verify logout redirects to login page")
    public void verifyLogoutRedirectsToLoginTest() {
        navigation.navigateLogin();
        loginPage.loginUser(globalUser);

        loginPage.clickLogoutButton();
        assertThat(page).hasURL(navigation.getLoginUrl());
    }

    @Test
    @Tag("login")
    @Description("Login_TC12 - Verify social media buttons link to correct page")
    public void verifySocialMediaButtonsLoginTest() {
        navigation.navigateLogin();
        Locator links = page.locator("div.orangehrm-login-footer-sm a");

        Locator linkedinButton = links.nth(0);
        Locator youtubeButton = links.nth(3);
        Locator facebookButton = links.nth(1);
        Locator twitterButton = links.nth(2);;

        //Linkedin
        Page linkedinPage = context.waitForPage(() -> {
            linkedinButton.click();
        });
        linkedinPage.waitForLoadState();
        assertThat(linkedinPage).hasURL("https://www.linkedin.com/company/orangehrm");
        linkedinPage.close();

        //Facebook
        Page facebookPage = context.waitForPage(() -> {
            facebookButton.click();
        });
        facebookPage.waitForLoadState();
        assertThat(facebookPage).hasURL("https://www.facebook.com/OrangeHRM/");
        facebookPage.close();

        //Youtube
        Page youtubePage = context.waitForPage(() -> {
            youtubeButton.click();
        });
        youtubePage.waitForLoadState();
        assertThat(youtubePage).hasURL(Pattern.compile(".*"+"www.youtube.com"+".*")); // for guest user the link opens the consent cookies page of youtube not the channel
        youtubePage.close();

        //Twitter
        Page twitterPage = context.waitForPage(() -> {
            twitterButton.click();
        });
        twitterPage.waitForLoadState();
        assertThat(twitterPage).hasURL("https://x.com/orangehrm?lang=en");
        twitterPage.close();

    }

    @Test
    @Tag("login")
    @Description("LoginAccessibility_TC01 - Verify login flow with keyboard")
    public void verifyLoginFlowWithKeyboard () {
        navigation.navigateLogin();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.keyboard().insertText(globalUser.getUsername());
        page.keyboard().press("Tab");
        page.keyboard().insertText(globalUser.getPassword());
        page.keyboard().press("Tab");
        page.keyboard().press("Enter");
        assertThat(page.locator("h6[data-v-7b563373]")).hasText("Dashboard");
    }

    @Test
    @Tag("login")
    @Description("LoginAccessibility_TC03 - Scan with Axe Core for common accessibility problems")
    public void axecoreScanForAccesibilityLoginTest() {
        navigation.navigateLogin();

        AxeResults accessibilityScanResults = new AxeBuilder(page).analyze();
        try (FileWriter writer = new FileWriter("Documents/4_Results/login_accessibility.txt", false)) {
            for (Rule violation : accessibilityScanResults.getViolations()) {
                writer.write("Rule: " + violation.getId() + "\n");
                writer.write("Impact: " + violation.getImpact() + "\n");
                writer.write("Description: " + violation.getDescription() + "\n");
                writer.write("Help URL: " + violation.getHelpUrl() + "\n\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(Collections.emptyList(), accessibilityScanResults.getViolations());
    }


    @Tag("login")
    @Tag("parameterized")
    @ParameterizedTest
    @MethodSource("utils.DataProvider#loginInvalidCsvProvider")
    @Description(" - Verify login behaviour with invalid credentials from csv file")
    public void loginInvalidParameteriezdTest(String username, String password) {
        navigation.navigateLogin();
        User mockUser = new User(username,password);
        loginPage.loginUser(mockUser);
        assertThat(page).hasURL(loginUrl);
    }

}
