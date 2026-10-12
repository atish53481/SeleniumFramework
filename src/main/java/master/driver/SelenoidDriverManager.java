package master.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

// Starts Firefox inside a Selenoid (or Selenium Grid) container
public class SelenoidDriverManager {

    public static WebDriver createDriver(String gridUrl) throws MalformedURLException {
        FirefoxOptions options = new FirefoxOptions();

        // Lets Selenoid UI show the live browser screen for this session
        HashMap<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        options.setCapability("selenoid:options", selenoidOptions);

        return new RemoteWebDriver(new URL(gridUrl), options);
    }

}
