package engine.webdriver;

import com.codeborne.selenide.WebDriverProvider;
import engine.DriverDownloadDirManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;

public class ChromeDriverProvider implements WebDriverProvider {

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", DriverDownloadDirManager.clearSetGet());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); //
        options.addArguments("disable-infobars");
        options.addArguments("--headless");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--window-size=1920,1080");
        options.setExperimentalOption("prefs", chromePrefs);
        return new ChromeDriver(options);
    }
}
