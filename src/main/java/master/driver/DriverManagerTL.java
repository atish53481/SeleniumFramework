package master.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DriverManagerTL {

    public static ThreadLocal<WebDriver> dr = new ThreadLocal<>();


    public static  void  setDriver(WebDriver driverRef){
        dr.set(driverRef);
    }


    public static WebDriver getDriver(){
        return  dr.get();

    }


    //unload
    public static void unload(){
        dr.remove();
    }

    public static  void down(){
        if(Objects.nonNull(DriverManagerTL.getDriver())){
            getDriver().quit();
            unload();
        }
    }

    public static void init(String suiteGridUrl, String testName) throws MalformedURLException {
     /*   ChromeOptions options = new ChromeOptions();
        options.setCapability("browserVersion", "128.0");
        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            *//* How to add test badge *//*
            put("name", "Test badge...");

            *//* How to set session timeout *//*
            put("sessionTimeout", "15m");

            *//* How to set timezone *//*
            put("env", new ArrayList<String>() {{
                add("TZ=UTC");
            }});

            *//* How to add "trash" button *//*
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});

            *//* How to enable video recording *//*
            put("enableVideo", true);
        }});*/

        // Grid URL set (suite xml, or -Dgrid.url which wins) runs on Selenoid, otherwise local browser
        String gridUrl = System.getProperty("grid.url", suiteGridUrl);
        WebDriver driver;
        if (gridUrl == null || gridUrl.isBlank()) {
            driver = LocalDriverManager.createDriver();
        } else {
            driver = SelenoidDriverManager.createDriver(gridUrl, testName);
        }
        setDriver(driver);

    }

}
