package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import model.User;
import utils.DataProvider;

import java.util.regex.Pattern;

public class RegisterPage {

    private Page page;
    private Locator firstNameLocator;
    private Locator middleNameLocator;
    private Locator lastNameLocator;
    private Locator employeeIdLocator;
    private Locator saveEmployeeButton;
    private Locator usernameField;
    private Locator passwordField;
    private Locator confirmPasswordField;
    private Locator createLoginDetailsButton;
    private Locator statusEnabledButton;
    private Locator statusDisabledButton;
    private String viewPersonalDetailsURL;


    public RegisterPage(Page testPage) {
        page = testPage;

        // Employee Register

        firstNameLocator = page.locator("input[name='firstName']");
        middleNameLocator = page.locator("input[name='middleName']");
        lastNameLocator = page.locator("input[name='lastName']");
        usernameField = page.locator("//div[label[text()=\"Username\"]]/following-sibling::div/input");
        passwordField = page.locator("//div[label[text()=\"Password\"]]/following-sibling::div/input");
        confirmPasswordField = page.locator("//div[label[text()=\"Confirm Password\"]]/following-sibling::div/input");
        employeeIdLocator = page.locator("//label[text()=\"Employee Id\"]/ancestor::div[contains(@class,\"oxd-input-group__label-wrapper\")]/following-sibling::div//input"); // no stable selector found
        saveEmployeeButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        createLoginDetailsButton = page.locator("//label[input[@type='checkbox']]/span");
        statusEnabledButton = page.locator("//label[text()=\"Enabled\"]");
        statusDisabledButton = page.locator("//label[text()=\"Disabled\"]");
        viewPersonalDetailsURL = "http://localhost/orangehrm-5.7/web/index.php/pim/viewPersonalDetails";

        // User Register
    }

    public void fillFirstName(String firstName) {
        firstNameLocator.fill(firstName);
    }

    public void fillMiddleName(String middleName) {
        middleNameLocator.fill(middleName);
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

    public void addEmployeeAndUser(User user) {
        fillFirstName(user.getFirstName());
        fillMiddleName(user.getMiddleName());
        fillLastName(user.getLastName());
        fillEmployeeId(user.getEmployeeId());

        clickCreateLoginDetailsButton();

        if(user.getStatus()) {
            clickStatusEnabled();
        } else {
            clickStatusDisabled();
        }

        fillUsernameField(user.getUsername());
        fillPasswordField(user.getPassword());

        saveEmployee();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public String getViewPersonalDetailsURL() {
        return viewPersonalDetailsURL;
    }

    // User Creation

    public void clickCreateLoginDetailsButton() {
        createLoginDetailsButton.click();
    }

    public void clickStatusEnabled() {
        statusEnabledButton.click();
    }

    public void clickStatusDisabled() {
        statusDisabledButton.click();
    }

    public void fillUsernameField(String username) {
          usernameField.fill(username);
    }

    public void fillPasswordField(String password) {
        passwordField.fill(password);
        confirmPasswordField.fill(password);
    }
}
