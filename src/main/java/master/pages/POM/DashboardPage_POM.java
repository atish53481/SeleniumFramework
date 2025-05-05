package master.pages.POM;

import master.base.CommmonToAllPage;
import org.openqa.selenium.By;

public class DashboardPage_POM extends CommmonToAllPage {

    DashboardPage_POM() {
    }

    By userNameOnDashboard = By.cssSelector("[data-qa=\"lufexuloga\"]");

    // Page Actions
    public String loggedInUserName() {
        presenceOfElement(userNameOnDashboard);
        return getElement(userNameOnDashboard).getText();

    }



}