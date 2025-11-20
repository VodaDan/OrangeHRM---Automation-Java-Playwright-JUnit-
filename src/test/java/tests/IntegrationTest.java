package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IntegrationTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    public void leavePageTest() {
        loginPage.loginUser(globalUser);
        navigation.navigateToLeavePage();
        leavePage.clickStartMonth();
        leavePage.selectMonthOrDate("May");
        leavePage.clickStartDate();
        leavePage.selectMonthOrDate("21");
    }
}
