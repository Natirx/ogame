package engine;

import com.codeborne.selenide.Configuration;
import engine.webdriver.*;
import utils.EnumUtil;

import java.time.Duration;

import static data.SystemProperties.DRIVER_TYPE;
import static data.SystemProperties.REMOTE_DRIVER_TYPE;

public final class DriverConfigurator {

    public static void configure() {
        Configuration.timeout = Duration.ofSeconds(15).toMillis();
        Configuration.startMaximized = true;
        Configuration.reopenBrowserOnFail = true;
        Configuration.savePageSource = false;
        Configuration.screenshots = false;
        Configuration.browser = getDriverClassName();
        Configuration.proxyEnabled = true;

    }

    private static String getDriverClassName() {
        DriverType driver = EnumUtil.searchEnum(DriverType.class, e -> e.name().equals(DRIVER_TYPE), DRIVER_TYPE);
        switch (driver) {
            case CHROME:
                return ChromeDriverProvider.class.getName();
            case FF:
                return FirefoxDriverProvider.class.getName();
            case IE:
                return IEDriverProvider.class.getName();
            case REMOTE: {
                RemoteDriverType remoteDriver =
                        EnumUtil.searchEnum(RemoteDriverType.class,
                                e -> e.name().equals(REMOTE_DRIVER_TYPE), REMOTE_DRIVER_TYPE);
                switch (remoteDriver) {
                    case CHROME:
                        return ChromeRemoteDriverProvider.class.getName();
                    case FF:
                        return FirefoxRemoteDriverProvider.class.getName();
                    default:
                        throw new IllegalArgumentException(String.format("No implementation for remote driver type: " +
                                "Remote driver Type[%s]", REMOTE_DRIVER_TYPE));
                }
            }
            default:
                throw new IllegalArgumentException(String.format("No implementation for provided driver type: " +
                        "Driver Type[%s]", DRIVER_TYPE));
        }
    }
}
