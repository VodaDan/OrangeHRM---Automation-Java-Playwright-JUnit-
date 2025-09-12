package tests;

import base.BaseTest;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.RegisterPage;
import pages.LoginPage;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterTest extends BaseTest {

    private RegisterPage registerPage;
    private LoginPage loginPage;

    @Override
    @BeforeEach
    public void setup() {
        super.setup();
        registerPage = new RegisterPage(page);
        loginPage = new LoginPage(page);
    }

    @Test
    public void createEmployeeTest() {

        navigation.navigateLogin();
        loginPage.fillUsername("Admin"); // GlobalUser for testing - cannot be deleted
        loginPage.fillPassword("admin123");
        loginPage.submitLogin();

        User mockUser = new User();
        mockUser = mockUser.generateRandomUser();
        navigation.navigateAddEmployee();

        registerPage.addEmployee(mockUser);
        assertThat(page).hasURL(Pattern.compile("https://opensource-demo.orangehrmlive.com/web/index.php/pim/viewPersonalDetails/.*"));
        assertThat(page.locator("div.orangehrm-edit-employee-name > h6")).hasText(mockUser.getFirstName() + " " + mockUser.getLastName());
    }

}
