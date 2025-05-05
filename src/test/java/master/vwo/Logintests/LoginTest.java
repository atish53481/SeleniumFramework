package master.vwo.Logintests;

import master.basetest.CommontoAllTest;
import master.pages.POM.DashboardPage_POM;
import master.pages.POM.LoginPage_POM;
import master.utils.PropertyReader;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends CommontoAllTest {



    @Test(priority = 2)
    public void testLoginPositive() throws Exception {
        //who will start the Webdiver?

        //How to enter the user, pass and go the Dashboard and verify
        LoginPage_POM pagepom = new LoginPage_POM();
        DashboardPage_POM dashboardPagePom = pagepom.openURL(PropertyReader.readKey("url")).loginToVWOPositive().afterLogin();
        String expected_username = dashboardPagePom.loggedInUserName();
        Assertions.assertThat(expected_username)
                .isNotNull()
                .isNotBlank()
                .contains(PropertyReader.readKey("expected_username"));


        //Who will close the Webdriver
    }

    @Test(priority = 1)
    public  void testLoginPNegative() throws Exception {
        //How to enter the user, pass and go the Dashboard and verify
        LoginPage_POM pagePom = new LoginPage_POM();
        pagePom.openURL(PropertyReader.readKey("url"));
        String error_message = pagePom.loginToVWONegative();
        Assertions.assertThat(error_message)
                .isNotNull()
                .isNotBlank()
                .contains(PropertyReader.readKey("error_message"));
    }
}
