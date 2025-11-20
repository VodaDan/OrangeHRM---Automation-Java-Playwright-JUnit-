package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.Navigation;

public class LeavePage {
    private static Page page;
    private Navigation navigation;

    private Locator startMonth;
    private Locator startDate;

    private Locator saveButton;
    private Locator resetButton;

    public LeavePage (Page pageSent) {
        page = pageSent;
        navigation = new Navigation(page);

        startMonth = page.locator("//div[text()='January']");
        startDate = page.locator("//div[text()='01']");

        saveButton = page.locator("//button[text()=' Save ']");
        resetButton = page.locator("//button[text()=' Reset ']");
    }

    public void clickStartMonth() {
        startMonth.click();
    }

    public void clickStartDate() {
        startDate.click();
    }

    public void selectMonthOrDate(String input) {
        String locator = String.format("//div[@role='option']//span[text()='%s']", input);
        page.click(locator);
    }
}
