package utils;
import base.BaseTest;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Navigation {

    private String loginUrl;
    private String registerUserUrl;
    private String registerEmployeeUrl;
    private String viewEmployeeListUrl;
    private Page page;

    public Navigation (Page testPage) {
        page = testPage;

        Properties properties = new Properties();
        try (InputStream config = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(config);

            // URL
            loginUrl = properties.getProperty("loginUrl");
            registerUserUrl = properties.getProperty("registerUrl");;
            registerEmployeeUrl = properties.getProperty("registerEmployeeUrl");;
            viewEmployeeListUrl = properties.getProperty("viewEmployeeListUrl");;

        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
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
