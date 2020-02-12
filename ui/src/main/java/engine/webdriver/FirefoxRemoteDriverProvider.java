package engine.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxRemoteDriverProvider implements RemoteDriver {

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, getFirefoxOptions());
        return instantiateDriver(capabilities);
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxProfile profile = new FirefoxProfile();
        return new FirefoxOptions()
                .setProfile(profile)
                .setLegacy(false);
    }
}
