package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import model.User;

import java.util.regex.Pattern;

public class RegisterPage {

    private Page page;
    private Locator firstNameLocator;
    private Locator lastNameLocator;
    private Locator employeeIdLocator;
    private Locator saveEmployeeButton;
    private String viewPersonalDetailsURL;

    public RegisterPage(Page testPage) {
        page = testPage;

        // Employee Register

        firstNameLocator = page.locator("input[name='firstName']");
        lastNameLocator = page.locator("input[name='lastName']");
        employeeIdLocator = page.locator("//label[text()=\"Employee Id\"]/ancestor::div[contains(@class,\"oxd-input-group__label-wrapper\")]/following-sibling::div//input"); // no stable selector found
        saveEmployeeButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        viewPersonalDetailsURL = "http://localhost/orangehrm-5.7/web/index.php/pim/viewPersonalDetails";

        // User Register
    }

    public void fillFirstName(String firstName) {
        firstNameLocator.fill(firstName);
    }

    public void fillLastName(String lastName) {
        lastNameLocator.fill(lastName);
    }

    public void fillEmployeeId(String id) {
        employeeIdLocator.fill(id);
    }

    public void saveEmployee() {
        saveEmployeeButton.click();

    }

    public void addEmployee(User user) {
        fillFirstName(user.getFirstName());
        fillLastName(user.getLastName());
        fillEmployeeId(user.getEmployeeId());
        saveEmployee();
    }

    public String getViewPersonalDetailsURL() {
        return viewPersonalDetailsURL;
    }
}
