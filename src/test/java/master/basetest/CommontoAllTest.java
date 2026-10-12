package master.basetest;

import master.driver.DriverManagerTL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class CommontoAllTest {


    //To call the Start the Webdriver

    //Down the Webdriver


    // grid.url comes from the suite xml (testng-selenoid.xml); empty means local browser
    @Parameters("grid.url")
    @BeforeMethod
    protected  void setup(@Optional("") String gridUrl, Method method) throws MalformedURLException {
        DriverManagerTL.init(gridUrl, method.getName());
    }

    @AfterMethod
    protected  void tearDown(){
        DriverManagerTL.down();
    }
}
