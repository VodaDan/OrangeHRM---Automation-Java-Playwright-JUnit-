package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

    private static Page page;
    private static Locator usernameLocator;
    private static Locator passwordLocator;
    private static Locator loginLocator;
    private static Locator loginValidationError;
    private static Locator forgotMyPasswordLocator;

    public LoginPage (Page existingPage) {
        page = existingPage;
        usernameLocator = page.locator("input[placeholder='Username']");
        passwordLocator = page.locator("input[placeholder='Password']");
        loginLocator = page.locator("button.orangehrm-login-button");
        loginValidationError = page.locator("p.oxd-alert-content-text");
        forgotMyPasswordLocator = page.locator("p.orangehrm-login-forgot-header");
    }

    public void fillUsername(String username) {
        usernameLocator.fill(username);
    }

    public void fillPassword(String password) {
        passwordLocator.fill(password);
    }

    public void submitLogin() {
        loginLocator.click();
    }

    public Locator getLoginValidationError() {
        return loginValidationError;
    }

    public void clickForgotMyPassword() {
        forgotMyPasswordLocator.click();
    }
}
