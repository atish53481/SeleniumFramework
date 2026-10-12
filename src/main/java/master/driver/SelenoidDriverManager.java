package master.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

// Starts Firefox inside a Selenoid (or Selenium Grid) container
public class SelenoidDriverManager {

    public static WebDriver createDriver(String gridUrl, String testName) throws MalformedURLException {
        FirefoxOptions options = new FirefoxOptions();

        HashMap<String, Object> selenoidOptions = new HashMap<>();
        // Lets Selenoid UI show the live browser screen for this session
        selenoidOptions.put("enableVNC", true);
        // Shows the test name on the session in Selenoid UI instead of a random id
        selenoidOptions.put("name", testName);
        // Records the run; watch it later under the Videos tab of Selenoid UI
        selenoidOptions.put("enableVideo", true);
        selenoidOptions.put("videoName", testName + ".mp4");
        options.setCapability("selenoid:options", selenoidOptions);

        return new RemoteWebDriver(new URL(gridUrl), options);
    }

}
