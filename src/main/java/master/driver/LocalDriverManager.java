package master.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

// Starts Firefox on this machine
public class LocalDriverManager {

    public static WebDriver createDriver() {
        FirefoxOptions options = new FirefoxOptions();
        // -Dheadless=true for CI agents with no desktop (e.g. Jenkins running as a service)
        if (Boolean.getBoolean("headless")) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

}
