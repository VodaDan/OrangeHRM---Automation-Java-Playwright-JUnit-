package tests;

import base.BaseTest;
import com.microsoft.playwright.options.LoadState;
import jdk.jfr.Description;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    @Description("Register_TC01 - Verify that a new employee can be added with valid first name, last name, and unique employee ID")
    public void createEmployeeTest() {
        loginPage.loginUser(globalUser);

        User mockUser = new User().generateRandomUser();
        navigation.navigateAddEmployee();

        registerPage.addEmployee(mockUser);
        assertThat(page).hasURL(Pattern.compile(registerPage.getViewPersonalDetailsURL()+".*"));
        assertThat(page.locator("div.orangehrm-edit-employee-name > h6")).hasText(mockUser.getFirstName() + " " + mockUser.getLastName());
    }

    @Test
    @Description("Register_TC02a - Verify that employee cannot be added when Last Name is left blank")
    public void lastNameBlankFieldEmployeeTest() {
        loginPage.loginUser(globalUser);

        User mockUser = new User().generateRandomUser();

        navigation.navigateAddEmployee();

        registerPage.fillFirstName(mockUser.getFirstName());
        registerPage.fillEmployeeId(mockUser.getEmployeeId());
        registerPage.saveEmployee();

        assertThat(page.locator("div:has(input[name='lastName']) + span")).hasText("Required");
    }

    @Test
    @Description("Register_TC02b - Verify that employee cannot be added when First Name is left blank")
    public void firstNameBlankFieldEmployeeTest() {
        loginPage.loginUser(globalUser);

        User mockUser = new User().generateRandomUser();

        navigation.navigateAddEmployee();

        registerPage.fillLastName(mockUser.getLastName());
        registerPage.fillEmployeeId(mockUser.getEmployeeId());
        registerPage.saveEmployee();

        assertThat(page.locator("div:has(input[name='firstName']) + span")).hasText("Required");

    }

    @Test
    @Description("Register_TC02c -  Verify that employee cannot be added when ID is left blank" +
            "Input field has no placeholder" +
            "Bug - No validation error is being raised")
    @Disabled
    public void idBlankFieldEmployeeTest() {
        loginPage.loginUser(globalUser);

        User mockUser = new User().generateRandomUser();

        navigation.navigateAddEmployee();

        registerPage.fillFirstName(mockUser.getFirstName());
        registerPage.fillLastName(mockUser.getLastName());
        registerPage.saveEmployee();

        assertThat(page.locator("div:has(input[name='employeeId']) + span")).hasText("Required");
    }

    @Test
    @Description("Register_TC03 - Verify that First Name and Last Name fields do not accept numeric or special characters" +
            "Bug - No validation error is being raised")
    @Disabled
    public void nameFieldsNumericInputValidationEmployeeTest() {
        loginPage.loginUser(globalUser);

        User mockUser = new User().generateRandomUser();

        navigation.navigateAddEmployee();

        registerPage.fillFirstName(mockUser.getEmployeeId());
        registerPage.fillLastName(mockUser.getEmployeeId());
        registerPage.fillEmployeeId(mockUser.getEmployeeId());
        registerPage.saveEmployee();

        assertThat(page.locator("div:has(input[name='firstName']) + span")).hasText("Invalid name");
        assertThat(page.locator("div:has(input[name='lastName']) + span")).hasText("Invalid name");
    }



}
