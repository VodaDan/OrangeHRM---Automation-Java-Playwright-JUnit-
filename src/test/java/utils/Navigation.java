package utils;
import com.microsoft.playwright.*;

public class Navigation {

    private String loginUrl;
    private String registerUserUrl;
    private String registerEmployeeUrl;
    private Page page;

    public Navigation (Page testPage) {
        page = testPage;
        loginUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
        registerUserUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/admin/saveSystemUser";
        registerEmployeeUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/pim/addEmployee";
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
}
