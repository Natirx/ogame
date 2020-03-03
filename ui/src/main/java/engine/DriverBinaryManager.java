package engine;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.EnumUtil;

import static data.SystemProperties.DRIVER_TYPE;

public final class DriverBinaryManager {

    public static void setupWebDriverBinary() {
        DriverType driver = EnumUtil.searchEnum(DriverType.class, e -> e.name().equals(DRIVER_TYPE), DRIVER_TYPE);
        switch (driver) {
            case CHROME:
                break;
            case FF:
                WebDriverManager.firefoxdriver().setup();
                break;
            case IE:
                WebDriverManager.iedriver().arch32().setup();
                break;
            /**
             * Remote machine must contain wishful drivers
             * */
            case REMOTE:
                break;
            default:
                throw new IllegalArgumentException(String.format("No implementation for provided driver type: " +
                        "Driver Type[%s]", DRIVER_TYPE));
        }
    }
}