package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import model.User;
import utils.Navigation;

public class LoginPage {

    private static Page page;
    private Navigation navigation;

    // Locators
    private static Locator usernameLocator;
    private static Locator passwordLocator;
    private static Locator loginLocator;
    private static Locator loginValidationError;
    private static Locator forgotMyPasswordLocator;
    private static Locator logoutButton;
    private static Locator dropDownMenu;

    private static String forgotPasswordURL;


    public LoginPage (Page existingPage) {
        page = existingPage;
        navigation = new Navigation(page);
        usernameLocator = page.locator("input[placeholder='Username']");
        passwordLocator = page.locator("input[placeholder='Password']");
        loginLocator = page.locator("button.orangehrm-login-button");
        loginValidationError = page.locator("p.oxd-alert-content-text");
        forgotMyPasswordLocator = page.locator("p.orangehrm-login-forgot-header");
        dropDownMenu = page.locator("p.oxd-userdropdown-name");
        logoutButton = page.locator("a:has-text('Logout')");
        forgotPasswordURL = "http://localhost/orangehrm-5.7/web/index.php/auth/requestPasswordResetCode";
    }

    public void fillUsername(String username) {
        usernameLocator.fill(username);
    }

    public void fillPassword(String password) {
        passwordLocator.fill(password);
    }

    public void submitLogin() {
        loginLocator.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public Locator getLoginValidationError() {
        return loginValidationError;
    }

    public void clickForgotMyPassword() {
        forgotMyPasswordLocator.click();
    }

    public String getForgotPasswordURL() {
        return forgotPasswordURL;
    }

    public void clickLogoutButton() {
        dropDownMenu.click();
        logoutButton.click();
    }

    //  login method for preconditions
    public void loginUser(User user) {
        navigation.navigateLogin();
        fillUsername(user.getUsername());
        fillPassword(user.getPassword());
        submitLogin();
    }


}
