package master.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class main1 {

    public static void main(String[] args) throws InterruptedException, MalformedURLException {

        FirefoxOptions options = new FirefoxOptions();

        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);

        driver.get("https://www.testing.com");
        driver.manage().window().maximize();

        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("The test Academy");
        searchBox.submit();


        Thread.sleep(5000);

        driver.quit();


    }
}
