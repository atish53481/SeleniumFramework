package master.pages.POM;

import master.base.CommmonToAllPage;
import org.openqa.selenium.By;

public class DashboardPage_POM extends CommmonToAllPage {

    DashboardPage_POM() {
    }

    // User name is shown inside the user menu (avatar, top right), not on the page itself
    By userMenuButton = By.cssSelector("[data-qa=\"btn-user-menu\"] button");
    By userNameOnDashboard = By.cssSelector("ul.dropdown-menu-list li.no-hover h6");

    // Page Actions
    public String loggedInUserName() {
        presenceOfElement(userMenuButton);
        visibilityOfElement(userMenuButton);
        clickElement(userMenuButton);
        visibilityOfElement(userNameOnDashboard);
        return getElement(userNameOnDashboard).getText();

    }



}