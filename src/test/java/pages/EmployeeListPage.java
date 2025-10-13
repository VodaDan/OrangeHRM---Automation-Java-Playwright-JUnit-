package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import utils.Navigation;

public class EmployeeListPage {

    private static Page page;
    private Navigation navigation;

    // Locators:
    private static Locator searchByName;
    private static Locator searchByEmployeeId;
    private static Locator searchButton;
    private static Locator employeeTable;


    public EmployeeListPage (Page existingPage) {
        page = existingPage;
        navigation = new Navigation(page);

        searchByName = page.locator(".oxd-input-group:has(label:has-text('Employee Name')) input");
        searchByEmployeeId = page.locator(".oxd-input-group:has(label:has-text('Employee Id')) input");
        searchButton = page.getByRole(AriaRole.BUTTON).getByText("Search");

        employeeTable = page.locator(".orangehrm-container");
    }

    public void fillSearchByName (String name) {
        searchByName.fill(name);
    }

    public void fillSearchByEmployeeId (String id) {
        searchByEmployeeId.fill(id);
    }

    public void clickSearch() {
        searchButton.click();
    }

    public Locator getEmployeeTable() {
        return employeeTable;
    }
}
