package tests;

import base.BaseTest;
import com.microsoft.playwright.options.LoadState;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import pages.RegisterPage;
import pages.LoginPage;
import utils.ApiUtils;
import utils.UrlUtils;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    public void createEmployeeTest() {
        navigation.navigateLogin();
        loginPage.fillUsername(globalUser.getUsername()); // GlobalUser for testing - cannot be deleted
        loginPage.fillPassword(globalUser.getPassword());
        loginPage.submitLogin();

        User mockUser = new User();
        mockUser = mockUser.generateRandomUser();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        navigation.navigateAddEmployee();

        registerPage.addEmployee(mockUser);
        assertThat(page).hasURL(Pattern.compile(registerPage.getViewPersonalDetailsURL()+".*"));
        assertThat(page.locator("div.orangehrm-edit-employee-name > h6")).hasText(mockUser.getFirstName() + " " + mockUser.getLastName());
    }

}
