package master.basetest;

import master.driver.DriverManagerTL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;

public class CommontoAllTest {


    //To call the Start the Webdriver

    //Down the Webdriver


    @BeforeMethod
    protected  void setup() throws MalformedURLException {
        DriverManagerTL.init();
    }

    @AfterMethod
    protected  void tearDown(){
        DriverManagerTL.down();
    }
}
