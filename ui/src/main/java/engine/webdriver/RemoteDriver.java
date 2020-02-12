package engine.webdriver;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static data.SystemProperties.REMOTE_URL;

interface RemoteDriver extends WebDriverProvider {

    default WebDriver instantiateDriver(DesiredCapabilities capabilities) {
        capabilities.setCapability("enableVNC", false);
        capabilities.setCapability("enableVideo", false);
        if (REMOTE_URL == null) {
            throw new IllegalArgumentException("Remote URL can not be null");
        }
        try {
            return new RemoteWebDriver(new URL(REMOTE_URL), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
