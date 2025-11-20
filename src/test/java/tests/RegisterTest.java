package tests;

import base.BaseTest;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import jdk.jfr.Description;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.RegisterPage;
import pages.LoginPage;
import utils.ApiUtils;
import utils.DataProvider;
import utils.UrlUtils;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RegisterTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    @Tag("register")
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
    @Tag("register")
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
    @Tag("register")
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
    @Tag("register")
    @Description("Register_TC02c -  Verify that employee cannot be added when ID is left blank" +
            "Bug - Input field has no placeholder" +
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
    @Tag("register")
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

    @Test
    @Tag("register")
    @Description("Register_TC04 - Verify that field length restrictions are enforced")
    public void verifyFieldInputMaxLengthEmployeeTest() {
        loginPage.loginUser(globalUser);

        String firstName = "John".repeat(8).substring(0,31); // max 30 char
        String lastName = "Doe".repeat(11).substring(0,31); // max 30 char
        String id = UUID.randomUUID().toString().substring(0,11); // max 10 char

        User mockUser = new User(firstName,lastName,id);

        navigation.navigateAddEmployee();

        registerPage.fillFirstName(mockUser.getFirstName());
        registerPage.fillLastName(mockUser.getLastName());
        registerPage.fillEmployeeId(mockUser.getEmployeeId());
        registerPage.saveEmployee();

        Locator messages = page.locator("span.oxd-input-group__message");

        assertThat(messages).hasCount(3);

        assertThat(messages.nth(0)).hasText("Should not exceed 30 characters");
        assertThat(messages.nth(1)).hasText("Should not exceed 30 characters");
        assertThat(messages.nth(2)).hasText("Should not exceed 10 characters");
    }

    @Test
    @Tag("register")
    @Description("Register_TC06 - Verify that the newly added employee appears in the employee list/table after submission.")
    public void verifyNewEmployeeIsInEmployeeListTest() {
        loginPage.loginUser(globalUser);

        User mockUser = new User().generateRandomUser();

        navigation.navigateAddEmployee();
        registerPage.addEmployee(mockUser);
        page.waitForURL(Pattern.compile("http://localhost/orangehrm-5.7/web/index.php/pim/viewPersonalDetails"+".*"));
        navigation.navigateEmployeeList();
        employeeListPage.fillSearchByName(mockUser.getFirstName());
        employeeListPage.fillSearchByEmployeeId(mockUser.getEmployeeId());

        employeeListPage.clickSearch();

        assertThat(employeeListPage.getEmployeeTable()).containsText(mockUser.getFirstName());
        assertThat(employeeListPage.getEmployeeTable()).containsText(mockUser.getEmployeeId());
    }

    @ParameterizedTest
    @MethodSource("utils.DataProvider#registerJsonProvider")
    @Tag("register")
    @Tag("parameterized")
    public void registerValidAndInvalidParameterizedTest(DataProvider.RegisterTestDTO user) {
        loginPage.loginUser(globalUser);
        navigation.navigateAddEmployee();
        registerPage.addEmployeeAndUser(user);

        switch (user.getExpectedPage()) {
            case "register":
                assertThat(page).hasURL(addEmployeeUrl);
                break;
            case "dashboard":
                page.waitForLoadState(LoadState.NETWORKIDLE);

                assertThat(page).hasURL(Pattern.compile(registerPage.getViewPersonalDetailsURL()+".*"));

                // Cleanup
                String emp = UrlUtils.extractEmpId(page.url());
                ApiUtils.extractToken();
                APIResponse deleteEmployeeResponse =  ApiUtils.deleteEmployee(emp);
                System.out.println("Delete Employee status: " + deleteEmployeeResponse.status());
                break;
        }







    }



}
