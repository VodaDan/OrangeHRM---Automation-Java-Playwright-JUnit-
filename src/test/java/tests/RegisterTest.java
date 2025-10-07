package tests;

import base.BaseTest;
import com.microsoft.playwright.options.LoadState;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.RegisterPage;
import pages.LoginPage;
import utils.ApiUtils;
import utils.UrlUtils;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterTest extends BaseTest {

    @Test
    public void createEmployeeTest() {
        navigation.navigateLogin();
        loginPage.fillUsername("Admin"); // GlobalUser for testing - cannot be deleted
        loginPage.fillPassword("admin123");
        loginPage.submitLogin();

        User mockUser = new User();
        mockUser = mockUser.generateRandomUser();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        navigation.navigateAddEmployee();

        registerPage.addEmployee(mockUser);
        assertThat(page).hasURL(Pattern.compile("https://opensource-demo.orangehrmlive.com/web/index.php/pim/viewPersonalDetails/.*"));
        assertThat(page.locator("div.orangehrm-edit-employee-name > h6")).hasText(mockUser.getFirstName() + " " + mockUser.getLastName());
    }

}
