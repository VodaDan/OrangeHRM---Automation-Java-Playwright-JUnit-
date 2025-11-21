package tests;

import base.BaseTest;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.LoadState;
import jdk.jfr.Description;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.ApiUtils;
import utils.UrlUtils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class IntegrationTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    @Tag("integration")
    @Description("PIM-INT-TC01 - Validate that an employee created in PIM can apply for leave in the Leave module.")
    public void leavePageTest() {
        loginPage.loginUser(globalUser);
        navigation.navigateAddEmployee();

        // Create new Employee with credentials
        User mockUser = new User().generateRandomUser();
        registerPage.addEmployeeAndUser(mockUser);

            // Extract employee id for cleanup
            String emp = UrlUtils.extractEmpId(page.url());

        // Logout admin, and try login with mockUser
        loginPage.clickLogoutButton();
        loginPage.loginUser(mockUser);

        assertThat(page).hasURL(dashboardIndexUrl);

        // Navigate to leavePage and make a leave request
        navigation.navigateToLeavePage();
        leavePage.clickStartDate();
        leavePage.selectMonthOrDate("July");
        leavePage.clickStartMonth();
        leavePage.selectMonthOrDate("19");

        // Cleanup
        ApiUtils.extractToken();
        APIResponse deleteEmployeeResponse =  ApiUtils.deleteEmployee(emp);
        System.out.println("Delete Employee status: " + deleteEmployeeResponse.status());
    }
}
