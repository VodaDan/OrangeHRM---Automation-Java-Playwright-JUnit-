package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

    private static Page page;
    private static Locator usernameLocator;
    private static Locator passwordLocator;
    private static Locator loginLocator;

    public LoginPage () {
        usernameLocator = page.locator("input[placeholder='Username']");
        passwordLocator = page.locator("input[placeholder='Password']");
        usernameLocator = page.locator("button.orangehrm-login-button']");
    }

    public void fillUsername(String username) {
        usernameLocator.fill(username);
    }

    public void fillPassword(String password) {
        passwordLocator.fill
    }
}
