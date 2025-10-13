package utils;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;

public class Navigation {

    private String loginUrl;
    private String registerUserUrl;
    private String registerEmployeeUrl;
    private String viewEmployeeListUrl;
    private Page page;

    public Navigation (Page testPage) {
        page = testPage;
        loginUrl = "http://localhost/orangehrm-5.7/web/index.php/auth/login";
        registerUserUrl = "http://localhost/orangehrm-5.7/web/index.php/admin/saveSystemUser";
        registerEmployeeUrl = "http://localhost/orangehrm-5.7/web/index.php/pim/addEmployee";
        viewEmployeeListUrl = "http://localhost/orangehrm-5.7/web/index.php/pim/viewEmployeeList";
    }

    public void navigateLogin() {
        page.navigate(loginUrl);
    }

    public void navigateAddUser() {
        page.navigate(registerUserUrl);
    }

    public void navigateAddEmployee() {
        page.navigate(registerEmployeeUrl);
    }

    public void navigateEmployeeList() {
        page.navigate(viewEmployeeListUrl);
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getRegisterUserUrl() {
        return registerUserUrl;
    }

    public String getRegisterEmployeeUrl() {
        return registerEmployeeUrl;
    }

    public Page getPage() {
        return page;
    }
}
